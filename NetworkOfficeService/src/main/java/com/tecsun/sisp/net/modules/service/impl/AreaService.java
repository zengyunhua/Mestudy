package com.tecsun.sisp.net.modules.service.impl;

import com.tecsun.sisp.net.common.Page;
import com.tecsun.sisp.net.modules.entity.request.DictionaryBean;
import com.tecsun.sisp.net.modules.entity.request.MatterRoleBean;
import com.tecsun.sisp.net.modules.entity.request.UserRoleBean;
import com.tecsun.sisp.net.modules.entity.response.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AreaService {
	
	public List<AreaVo> queryByArea();
	public List<DictionaryVo> queryPersonStatus(DictionaryBean bean);
	/** @deprecated Disabled until verified role SQL exists. */
	@Deprecated
	public long insertUserRol(UserRolVo vo);
	/** @deprecated Disabled until verified role SQL exists. */
	@Deprecated
	public Page<UserRolVo> queryUserRloe(UserRoleBean vo);
	public long inserttMatteRole(MatterRoleBean vo);//保存权限
	public List<MatterRoleBean> queryByCorrelation(MatterRoleBean vo);//关联查询
	 public UserVO getUserById(int userId);
	 public List<OrgVo> queryByOrg(OrgVo vo);//查询所有机构
	public List<OrgUserVo> queryByOrgUser(OrgUserVo vo);//获取机构下的用户
	public List<OrgUserVo> queryUserList();//显示用户列表
	public long deleteUserMatter(Map map);//删除用户事项关联
	public long insertMatterAndRole(List<UserRoleList> vo);//用户事项关联添加
	public List<UserRoleList> getMatterRole(HashMap map);//查询
	public List<UserAndRoleVo> queryByMatterAndRole(UserAndRoleVo bean);//查询出用户事项
	public List<DictVO> querydystatus();  //查询所有的医疗待遇类别

}
