package com.tecsun.sisp.net.modules.dao.dr;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tecsun.sisp.net.common.MyBatisDao;
import com.tecsun.sisp.net.modules.entity.business.QueryVo;

/**
 * 各类查询业务接口
 *
 */
@MyBatisDao
public interface QueryDao {

	/**
	 * 个人实缴记录单查询
	 * @param grbh 身份证号
	 * @return
	 */
	public List<Map<String,Object>> getGrsjjl(QueryVo qv);
	
	/**
	 * 个人参保证明查询
	 * @param grbh 身份证号
	 * @return
	 */
	public List<Map<String, Object>> getGrcbzm(@Param("idcard")String idcard);
	
	/**
	 * 灵活就业人员缴费基数查询
	 * @param grbh 身份证号
	 * @return
	 */
	public List<Map<String,Object>> getLhjyryjfjs(QueryVo qv);

	/**
	 * 根据身份证号查询个人编号
	 * @param xz 险种
	 * @param grbh
	 * @return
	 */
	public List<String> getGrbh(@Param("idcard")String idcard, @Param("xz")String xz);

	/**
	 * 根据个人编号得到个人信息
	 * @param grbh
	 * @return
	 */
	public Map<String, Object> getGrxxByGrbh(@Param("grbh")String grbh);

	/**
	 * 个人养老保险年账户信息查询
	 * @param qv
	 * @return
	 */
	public List<Map<String, Object>> getGrylbxnzhxx(QueryVo qv);
	/**
	 * 参保日期
	 * @param grbh
	 * @return
	 */
	public String getCbrq(@Param("grbh")String grbh);

	/**
	 * 缴至年月
	 * @param grbh
	 * @return
	 */
	public String getJzny(@Param("grbh")String grbh);

	/**
	 * 根据身份证号得到个人信息
	 * @param idcard
	 * @return
	 */
	public List<Map<String, Object>> getGrxxByIdcard(@Param("idcard")String idcard);
}
