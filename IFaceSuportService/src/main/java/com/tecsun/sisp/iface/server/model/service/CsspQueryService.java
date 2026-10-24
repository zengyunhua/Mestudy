package com.tecsun.sisp.iface.server.model.service;

import java.util.List;
import java.util.Map;

import com.tecsun.sisp.iface.common.vo.*;
import com.tecsun.sisp.iface.common.vo.cssp.*;

/**
 * Created by hhl on 2016/6/16.
 */
public interface CsspQueryService {

    Integer getPersonInfoSequence() throws Exception;

    Integer getBusPersonalApplySequence() throws Exception;

    //申领信息收集--插入到个人基础信息表
    Integer insertBasePersonInfo(Applybean bean) throws Exception;

    Integer insertBusHisInfo(Applybean bean) throws Exception;

    Integer insertBusPersonalApply(Applybean info) throws Exception;

    PicVO queryPicByPicName(String picName) throws Exception;

    String getOrgAddress(Integer id) throws Exception;

    boolean updatePersonPic(Map map) throws Exception;
    
    public PicVO queryPicByCertNum(String certNum) throws Exception;

    public List<BasicPersonInfoBean> getPersonInfo(String name, String certNum) throws Exception;
    
    boolean insertPic(Map map);

    boolean updatePic(Map map);

    List<PicInfoPO> getPicIsCheck();

    List<NewPicInfoPO>getUpdatePic(String picName);
    
    List<PicInfoPO> getBusinessCardInfoByPC();
    
    public Applybean queryPhotoUrl(String name, String certNum);
    
    public Integer updatePhotoUrl(String name, String certNum, String photoUrl);

    

    public ResultLoginvo login(LoginVO bean) throws Exception;

    public List<BusRcmCardVO> getCardinfo(String certNum);//查找数据中是否存在

    public List<ResultXhcVO> getCardku(String  orgAddress);//查找箱子

    public boolean insertBin(BusRcmBinVO binVO);

    public  List<BusRcmBinVO> queryBin(String orgAddress);

    public boolean insertBox(BusRcmBoxVO boxVO);

    public List<BusRcmBoxVO> queryBox(String orgAddress);

    public boolean insertCard(BusRcmCardVO bean);

    public boolean updateBasic(String soCardNum);

    public boolean updateBox(BusRcmBoxVO boxVO);

    public boolean updateBin(BusRcmBinVO binVO);

    public ResultBusMakeDetal getCardwz(String certNum);

    public List<ResultBusRcmCardApplyVO> getrcmApply(String certNum);

    public boolean insertApplyPic(BusApplyPicVO bean) throws Exception;

    public  ResultUpVO getboxid(String soCardNum);

    public  boolean  updataBox(long boxid);

    public boolean updataRcm(String soCardNum);

    public String getrcmWdname(Integer id) throws Exception;

    //申领记录
    public List<ResultPersonApplyVO> getapplyhistory(String certnum);

    //图片处理超时
    public boolean updatePicStatus(String picName);

    //public ResultBasicCardVO getBasicInfo(String soCardNum);

    public boolean updateCard(ResultUpVO resultUpVO);

    public List<BusRcmCardVO> getHnowcount(BusRcmCardVO bean);

    public  boolean updateboxnew(long boxid);

    //2018/520
    public PersonalApply querybyPersonMessage(String certNum) throws Exception;
}
