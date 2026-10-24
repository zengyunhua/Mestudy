package com.tecsun.sisp.iface.server.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tecsun.sisp.iface.common.vo.*;
import com.tecsun.sisp.iface.common.vo.cssp.*;
import com.tecsun.sisp.iface.server.model.dao_cssp.CsspQueryDAO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hhl on 2016/6/16.
 */
@Service("csspQueryService")
public class CsspQueryServiceImpl  implements CsspQueryService {
    public static final Logger logger = Logger.getLogger(CsspQueryServiceImpl.class);

    @Autowired
    private CsspQueryDAO csspQueryDAO;

    public Integer getPersonInfoSequence() throws Exception{
        return csspQueryDAO.getPersonInfoSequence();
    }

    public Integer getBusPersonalApplySequence() throws Exception{
        return csspQueryDAO.getBusPersonalApplySequence();
    }

    
    public Integer insertBasePersonInfo(Applybean bean) throws Exception {
        Integer seq = getPersonInfoSequence();
        bean.setId(seq);
        Integer result = csspQueryDAO.insertBasePersonInfo(bean);
        if(result > 0){
            return seq;
        }
        return null;
    }

    public Integer insertBusHisInfo(Applybean bean) throws Exception{
        return csspQueryDAO.insertBusHisInfo(bean);
    }

    public Integer insertBusPersonalApply(Applybean bean) throws Exception{
        Integer seq = getBusPersonalApplySequence();
        bean.setApId(seq);
        Integer result = csspQueryDAO.insertBusPersonalApply(bean);
        if(result > 0){
            return seq;
        }
        return null;
    }

    public  boolean updatePersonPic(Map map) throws Exception{
        return csspQueryDAO.updatePersonPic(map)>0;
    }

    public PicVO queryPicByPicName(String picName) throws Exception{
        return csspQueryDAO.queryPicByPicName(picName);
    }

   
    public String getOrgAddress(Integer id) throws Exception {
        return csspQueryDAO.getOrgAddress(id);
    }

    public PicVO queryPicByCertNum(String certNum) throws Exception{
        return csspQueryDAO.queryPicByCertNum(certNum);
    }

   
    public boolean insertPic(Map map) {
        return csspQueryDAO.insertPic(map)>0;
    }

   
    public boolean updatePic(Map map) {
        return csspQueryDAO.updatePic(map)>0;
    }

   
    public List<PicInfoPO> getPicIsCheck() {
        return csspQueryDAO.getPicIsCheck();
    }

   
    public List<NewPicInfoPO> getUpdatePic(String picName) {
        return csspQueryDAO.getUpdatePic(picName);
    }

	
	public List<PicInfoPO> getBusinessCardInfoByPC() {
		return csspQueryDAO.getBusinessCardInfoByPC();
	}

	
	public Applybean queryPhotoUrl(String name, String certNum) {
        Map map = new HashMap();
        map.put("name",name);
        map.put("certNum",certNum);
		return csspQueryDAO.queryPhotoUrl(map);
	}

	
	public Integer updatePhotoUrl(String name, String certNum, String photoUrl) {
        Map map = new HashMap();
        map.put("name",name);
        map.put("certNum",certNum);
        map.put("photoUrl",photoUrl);
		return csspQueryDAO.updatePhotoUrl(map);
	}
    public List<BasicPersonInfoBean> getPersonInfo(String name,String certNum) throws Exception{
        Map map = new HashMap();
        map.put("name",name);
        map.put("certNum",certNum);
        return csspQueryDAO.getPersonInfo(map);
    }
   
    public ResultLoginvo login(LoginVO bean) throws Exception {
        return csspQueryDAO.login(bean);
    }

   
    public List<BusRcmCardVO> getCardinfo(String certNum) {
        return csspQueryDAO.getCardinfo(certNum);
    }

   
    public List<ResultXhcVO> getCardku(String  orgAddress) {
        return csspQueryDAO.getCardku(orgAddress);
    }

   
    public boolean insertBin(BusRcmBinVO binVO) {
        return csspQueryDAO.insertBin(binVO)>0;
    }

   
    public List<BusRcmBinVO> queryBin(String orgAddress) {
        return csspQueryDAO.queryBin(orgAddress);
    }

   
    public boolean insertBox(BusRcmBoxVO boxVO) {
        return csspQueryDAO.insertBox(boxVO)>0;

    }

   
    public List<BusRcmBoxVO> queryBox(String orgAddress) {
        return csspQueryDAO.queryBox(orgAddress);
    }

   
    public boolean insertCard(BusRcmCardVO bean) {
        return csspQueryDAO.insertCard(bean)>0;
    }

   
    public boolean updateBasic(String soCardNum) {
        return csspQueryDAO.updateBasic(soCardNum)>0;
    }

   
    public boolean updateBox(BusRcmBoxVO boxVO) {
        return csspQueryDAO.updateBox(boxVO)>0;
    }

   
    public boolean updateBin(BusRcmBinVO binVO) {
        return csspQueryDAO.updateBin(binVO)>0;
    }

   
    public ResultBusMakeDetal getCardwz(String certNum) {
        return csspQueryDAO.getCardwz(certNum);
    }

   
    public List<ResultBusRcmCardApplyVO> getrcmApply(String certNum) {
        return csspQueryDAO.getrcmApply(certNum);
    }

    public boolean insertApplyPic(BusApplyPicVO bean) throws Exception {
        return csspQueryDAO.insertApplyPic(bean)>0;
    }

   
    public  ResultUpVO getboxid(String soCardNum) {
        return csspQueryDAO.getboxid(soCardNum);
    }

   
    public boolean updataBox(long boxid) {
        return csspQueryDAO.updataBox(boxid)>0;
    }

    public boolean updataRcm(String soCardNum) {
        return csspQueryDAO.updataRcm(soCardNum)>0;
    }

   
    public String getrcmWdname(Integer id) throws Exception {
        return csspQueryDAO.getrcmWdname(id);
    }

   
    public List<ResultPersonApplyVO> getapplyhistory(String certnum) {
        return csspQueryDAO.getapplyhistory(certnum);
    }

   
    public boolean updatePicStatus(String picName) {

        return csspQueryDAO.updatePicStatus(picName)>0;
    }

   
    public boolean updateCard(ResultUpVO resultUpVO) {
        return csspQueryDAO.updateCard(resultUpVO)>0;
    }

   
    public List<BusRcmCardVO> getHnowcount(BusRcmCardVO bean) {
        return csspQueryDAO.getHnowcount(bean);
    }

   
    public boolean updateboxnew(long boxid) {
        return csspQueryDAO.updateboxnew(boxid)>0;
    }

  //2018/05/08
    public PersonalApply querybyPersonMessage(String certNum) throws Exception {
        return csspQueryDAO.querybyPersonMessage(certNum);
    }
    
    

}
