package com.tecsun.sisp.iface.server.model.service;

import com.tecsun.sisp.iface.common.vo.ApersonVO;
import com.tecsun.sisp.iface.common.vo.NetUserInfoVO;
import com.tecsun.sisp.iface.common.vo.PersonVO;
import com.tecsun.sisp.iface.common.vo.PolicyVO;
import com.tecsun.sisp.iface.common.vo.SecQueryBean;
import com.tecsun.sisp.iface.common.vo.employment.param.job.AddJobSeekerInfoVo;
import com.tecsun.sisp.iface.common.vo.mess.AccountVO;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: NetUserServiceImpl
 * Description:
 * CreateTime： 2015年07月22日 14时:07分
 */
@Service("netUserService")
public class NetUserServiceImpl {

    private SqlSessionTemplate sqlSessionTemplate;

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public Long getSequence() {
        return this.sqlSessionTemplate.selectOne("getSequence");
    }

    @Transactional("net")
    public void updateMobile(SecQueryBean bean){
        this.sqlSessionTemplate.update("updateMobile",bean);
    }

    @Transactional("net")
    public void updateNetpwd(NetUserInfoVO bean){
        this.sqlSessionTemplate.update("updateNetpwd",bean);
    }

    public NetUserInfoVO userLogin(NetUserInfoVO netUserInfoVO){
        return this.sqlSessionTemplate.selectOne("userLogin",netUserInfoVO);
    }

    public Long getPolicyTitleCount(PolicyVO bean){
        return (Long)this.sqlSessionTemplate.selectOne("getPolicyTitleCount",bean);
    }

    public List<PolicyVO> getPolicyTitleList(PolicyVO bean){
        return this.sqlSessionTemplate.selectList("getPolicyTitleList", bean);
    }

    public PolicyVO getPolicyContext(PolicyVO bean){
        return this.sqlSessionTemplate.selectOne("getPolicyContext", bean);
    }
    

    //获取手机号码
    public NetUserInfoVO getPhoneNo(SecQueryBean bean)throws Exception {
        return sqlSessionTemplate.selectOne("getPhoneNo",bean);
    }
    
    
    //获取银行卡号
    public NetUserInfoVO getPseronBank(SecQueryBean bean)throws Exception {
        return sqlSessionTemplate.selectOne("getPseronBank",bean);
    }

    //插入省厅数据到本地数据库
    public int insertUser(NetUserInfoVO vo)throws Exception {
        return sqlSessionTemplate.insert("insertUser",vo);
    }
    //获取本地数据库最大id
    public long getMaxRetime(){
        return this.sqlSessionTemplate.selectOne("getMaxRetime");
    }

    //求职注册信息添加到平台
    public long registerBySISP(AddJobSeekerInfoVo vo)throws Exception {
        return sqlSessionTemplate.insert("registerBySISP",vo);
    }

    //获取求职者信息
    public AddJobSeekerInfoVo getRegistrationInfo(AddJobSeekerInfoVo vo)throws Exception {
        return sqlSessionTemplate.selectOne("getRegistrationInfo",vo);
    }
    //修改求职者信息
    public long updateRegistrationInfo(AddJobSeekerInfoVo vo)throws Exception {
        return sqlSessionTemplate.update("updateRegistrationInfo",vo);
    }
    //获取所有可用状态人员信息
    public List<PersonVO> findAllByPersonState(Integer personState)throws Exception {
		return sqlSessionTemplate.selectList("findAllByPersonState",1);
	}
    
    //2018/6/22
    public List<ApersonVO> queryByCardPerson(ApersonVO vo)throws Exception {
		return sqlSessionTemplate.selectList("queryByCardPerson",vo);
	}
    
    
    
    
   
    
   

}
