package com.tecsun.sisp.net.modules.service.impl;

import com.tecsun.sisp.net.common.Page;
import com.tecsun.sisp.net.modules.dao.area_sisppublic.AreaDAO;
import com.tecsun.sisp.net.modules.entity.request.DictionaryBean;
import com.tecsun.sisp.net.modules.entity.request.MatterRoleBean;
import com.tecsun.sisp.net.modules.entity.request.UserRoleBean;
import com.tecsun.sisp.net.modules.entity.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("areaService")
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDAO areaDAO;

    public List<AreaVo> queryByArea() {
        return areaDAO.queryByArea();
    }

    public List<DictionaryVo> queryPersonStatus(DictionaryBean bean) {
        return areaDAO.queryPersonStatus(bean);
    }

    /** @deprecated No verified role schema/SQL is available in the public project. */
    @Deprecated
    public long insertUserRol(UserRolVo vo) {
        throw new UnsupportedOperationException("Role schema and SQL are not available");
    }

    /** @deprecated Use only after implementing and testing verified role SQL. */
    @Deprecated
    public Page<UserRolVo> queryUserRloe(UserRoleBean vo) {
        throw new UnsupportedOperationException("Role schema and SQL are not available");
    }

    @Override
    public long inserttMatteRole(MatterRoleBean vo) {
        long row = areaDAO.inserttMatteRole(vo);
        return row;
    }

    @Override
    public List<MatterRoleBean> queryByCorrelation(MatterRoleBean vo) {
        return areaDAO.queryByCorrelation(vo);
    }

    public UserVO getUserById(int userId) {
        return areaDAO.getUserById(userId);
    }


    @Override
    public List<OrgVo> queryByOrg(OrgVo vo) {
        return areaDAO.queryByOrg(vo);

    }

    @Override
    public List<OrgUserVo> queryByOrgUser(OrgUserVo vo) {

        return areaDAO.queryByOrgUser(vo);
    }

    @Override
    public List<OrgUserVo> queryUserList() {
        return areaDAO.queryUserList();
    }


//    public long deleteUserMatter(UserAndRoleVo vo) {
//
//        List<UserRoleList> lsit1 = vo.getDeleteUserRoleList();
//        for (UserRoleList userRoleList1 : lsit1) {
//            deleteUserMatter(userRoleList1);
//        }
//        List<UserRoleList> lsit2 = vo.getAddUserRoleList();
//        HashMap map = new HashMap<>();
//        for (UserRoleList userRoleList2 : lsit2) {
//            String userId = userRoleList2.getUserId();
//            String matterId = userRoleList2.getMatterId();
//            map.put("userId", userId);
//            map.put("matterId", matterId);
//
//            List<UserRoleList> matterRole = getMatterRole(map);
//            if (matterRole != null && matterRole.size() > 0) {
//                return 1;
//            }
//            return insertMatterAndRole(userRoleList2);
//        }
//        return 1;
//
//    }


    @Override
    public List<UserAndRoleVo> queryByMatterAndRole(UserAndRoleVo bean) {

        return areaDAO.queryByMatterAndRole(bean);
    }

    @Override
    public long deleteUserMatter(Map map) {
        long row = areaDAO.deleteUserMatter(map);
        return row;
    }

    @Override
    public long insertMatterAndRole(List<UserRoleList> vo) {
        long row = areaDAO.insertMatterAndRole(vo);
        return row;
    }

    @Override
    public List<UserRoleList> getMatterRole(HashMap map) {

        return areaDAO.getMatterRole(map);
    }


    public List<DictVO> querydystatus() {
        return areaDAO.querydystatus();
    }


}
