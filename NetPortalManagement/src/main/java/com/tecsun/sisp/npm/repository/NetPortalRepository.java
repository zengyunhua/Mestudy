package com.tecsun.sisp.npm.repository;

import com.tecsun.sisp.npm.bean.NetUserInfoVO;
import com.tecsun.sisp.npm.bean.PolicyVO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetPortalRepository {
    private static final String USER_COLUMNS =
            "cityno, grbh, cardno, name, cernum, mobile_address, address, mobile, phone, " +
            "unitno, unitname, is_update, birthday, net_password, nation, sex, gj, " +
            "card_status, fkrq, yxqz, bankid, wd_bank, bankno, email, remark, person_status";

    public List<NetUserInfoVO> findUsers(Connection connection, String name, String cernum,
                                         String cardno, int page, int pageSize) throws SQLException {
        List<Object> parameters = new ArrayList<Object>();
        String where = userWhere(name, cernum, cardno, parameters);
        String base = "select " + USER_COLUMNS + " from t_az01b where " + where + " order by cernum";
        String sql = "select * from (select q.*, rownum rn from (" + base + ") q where rownum <= ?) " +
                "where rn > ?";
        parameters.add(page * pageSize);
        parameters.add((page - 1) * pageSize);
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            bind(statement, parameters);
            ResultSet resultSet = statement.executeQuery();
            try {
                List<NetUserInfoVO> users = new ArrayList<NetUserInfoVO>();
                while (resultSet.next()) {
                    users.add(readUser(resultSet));
                }
                return users;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    public NetUserInfoVO findUser(Connection connection, String cernum) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "select " + USER_COLUMNS + " from t_az01b where cernum = ?");
        try {
            statement.setString(1, cernum);
            ResultSet resultSet = statement.executeQuery();
            try {
                return resultSet.next() ? readUser(resultSet) : null;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    public int updatePassword(Connection connection, String cernum, String password, String updateCount)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "update t_az01b set net_password = ?, is_update = ? where cernum = ?");
        try {
            statement.setString(1, password);
            statement.setString(2, updateCount);
            statement.setString(3, cernum);
            return statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    public List<PolicyVO> findPolicies(Connection connection, String title, int page, int pageSize)
            throws SQLException {
        String filter = title == null || title.trim().isEmpty() ? "" : " where title like ?";
        String sql = "select id, context, district, fileno, create_time, publish_date, title, type " +
                "from (select q.*, rownum rn from (select id, context, district, fileno, create_time, " +
                "publish_date, title, type from t_policy" + filter + " order by id desc) q where rownum <= ?) " +
                "where rn > ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            int index = 1;
            if (!filter.isEmpty()) {
                statement.setString(index++, "%" + title.trim() + "%");
            }
            statement.setInt(index++, page * pageSize);
            statement.setInt(index, (page - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            try {
                List<PolicyVO> policies = new ArrayList<PolicyVO>();
                while (resultSet.next()) {
                    policies.add(readPolicy(resultSet));
                }
                return policies;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    public int countPolicies(Connection connection, String title) throws SQLException {
        String filter = title == null || title.trim().isEmpty() ? "" : " where title like ?";
        PreparedStatement statement = connection.prepareStatement("select count(1) from t_policy" + filter);
        try {
            if (!filter.isEmpty()) {
                statement.setString(1, "%" + title.trim() + "%");
            }
            ResultSet resultSet = statement.executeQuery();
            try {
                resultSet.next();
                return resultSet.getInt(1);
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    public PolicyVO findPolicy(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "select id, context, district, fileno, create_time, publish_date, title, type " +
                "from t_policy where id = ?");
        try {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            try {
                return resultSet.next() ? readPolicy(resultSet) : null;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    public void savePolicy(Connection connection, long id, String title, byte[] context, String type,
                           String fileNo, String district, Timestamp publishDate, Timestamp createTime)
            throws SQLException {
        PreparedStatement statement;
        if (id > 0) {
            statement = connection.prepareStatement(
                    "update t_policy set title = ?, context = ?, type = ?, fileno = ?, district = ?, " +
                    "publish_date = ? where id = ?");
            statement.setString(1, title);
            statement.setBytes(2, context);
            statement.setString(3, type);
            statement.setString(4, fileNo);
            statement.setString(5, district);
            statement.setTimestamp(6, publishDate);
            statement.setLong(7, id);
        } else {
            statement = connection.prepareStatement(
                    "insert into t_policy (id, title, context, type, fileno, district, publish_date, create_time) " +
                    "values (T_POLICY_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, title);
            statement.setBytes(2, context);
            statement.setString(3, type);
            statement.setString(4, fileNo);
            statement.setString(5, district);
            statement.setTimestamp(6, publishDate);
            statement.setTimestamp(7, createTime);
        }
        try {
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    public int deletePolicy(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from t_policy where id = ?");
        try {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    public List<Map<String, String>> connectionStats(Connection connection, Timestamp start, Timestamp end,
                                                       String format) throws SQLException {
        String sql = "select to_char(create_time, '" + format + "') as bucket, count(1) as total " +
                "from T_WEB_CONNECTION_INFO where create_time >= ? and create_time < ? " +
                "group by to_char(create_time, '" + format + "') order by bucket asc";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            ResultSet resultSet = statement.executeQuery();
            try {
                List<Map<String, String>> values = new ArrayList<Map<String, String>>();
                while (resultSet.next()) {
                    Map<String, String> value = new HashMap<String, String>();
                    value.put("name", resultSet.getString("bucket"));
                    value.put("group", "网站访问量");
                    value.put("value", resultSet.getString("total"));
                    values.add(value);
                }
                return values;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    private String userWhere(String name, String cernum, String cardno, List<Object> parameters) {
        StringBuilder where = new StringBuilder("1 = 1");
        if (name != null && !name.trim().isEmpty()) {
            where.append(" and name like ?");
            parameters.add("%" + name.trim() + "%");
        }
        if (cernum != null && !cernum.trim().isEmpty()) {
            where.append(" and cernum like ?");
            parameters.add("%" + cernum.trim() + "%");
        }
        if (cardno != null && !cardno.trim().isEmpty()) {
            where.append(" and cardno like ?");
            parameters.add("%" + cardno.trim() + "%");
        }
        return where.toString();
    }

    private void bind(PreparedStatement statement, List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            statement.setObject(i + 1, parameters.get(i));
        }
    }

    private NetUserInfoVO readUser(ResultSet resultSet) throws SQLException {
        NetUserInfoVO user = new NetUserInfoVO();
        user.setCityno(resultSet.getString("cityno"));
        user.setGrbh(resultSet.getString("grbh"));
        user.setCardno(resultSet.getString("cardno"));
        user.setName(resultSet.getString("name"));
        user.setCernum(resultSet.getString("cernum"));
        user.setMobile_address(resultSet.getString("mobile_address"));
        user.setAddress(resultSet.getString("address"));
        user.setMobile(resultSet.getString("mobile"));
        user.setPhone(resultSet.getString("phone"));
        user.setUnitno(resultSet.getString("unitno"));
        user.setUnitname(resultSet.getString("unitname"));
        user.setIs_update(resultSet.getString("is_update"));
        user.setBirthday(resultSet.getString("birthday"));
        user.setNet_password(resultSet.getString("net_password"));
        user.setNation(resultSet.getString("nation"));
        user.setSex(resultSet.getString("sex"));
        user.setGj(resultSet.getString("gj"));
        user.setCard_status(resultSet.getString("card_status"));
        user.setFkrq(resultSet.getString("fkrq"));
        user.setYxqz(resultSet.getString("yxqz"));
        user.setBankid(resultSet.getString("bankid"));
        user.setWd_bank(resultSet.getString("wd_bank"));
        user.setBankno(resultSet.getString("bankno"));
        user.setEmail(resultSet.getString("email"));
        user.setRemark(resultSet.getString("remark"));
        user.setPerson_status(resultSet.getString("person_status"));
        return user;
    }

    private PolicyVO readPolicy(ResultSet resultSet) throws SQLException {
        PolicyVO policy = new PolicyVO();
        policy.setId(resultSet.getLong("id"));
        policy.setContext(readBlob(resultSet.getBlob("context")));
        policy.setDistrict(resultSet.getString("district"));
        policy.setFileNo(resultSet.getString("fileno"));
        policy.setCreateTime(resultSet.getTimestamp("create_time"));
        policy.setPublishDate(resultSet.getString("publish_date"));
        policy.setTitle(resultSet.getString("title"));
        policy.setType(resultSet.getString("type"));
        return policy;
    }

    private String readBlob(Blob blob) throws SQLException {
        if (blob == null) {
            return null;
        }
        InputStream input = blob.getBinaryStream();
        try {
            byte[] buffer = new byte[(int) blob.length()];
            int offset = 0;
            int read;
            while (offset < buffer.length && (read = input.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += read;
            }
            return new String(buffer, 0, offset, "UTF-8");
        } catch (IOException e) {
            throw new SQLException("Unable to read policy context", e);
        } finally {
            try {
                input.close();
            } catch (IOException ignored) {
                // Best effort close for Java 8 compatibility.
            }
        }
    }
}
