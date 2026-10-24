package com.tecsun.sisp.iface.server.model.dao_cssp;

import java.util.List;
import java.util.Map;

import com.tecsun.sisp.iface.common.vo.*;
import com.tecsun.sisp.iface.common.vo.cssp.*;
import com.tecsun.sisp.iface.server.util.MyBatisDao;

/**
 * Created by jerry on 2015/5/31.
 */
@MyBatisDao
public interface CsspQueryDAO {

    public Integer getPersonInfoSequence() throws Exception;

    public Integer getBusPersonalApplySequence() throws Exception;

    public Integer insertBusHisInfo(Applybean bean) throws Exception;

    public Integer insertBasePersonInfo(Applybean info) throws Exception;

    public Integer insertBusPersonalApply(Applybean info) throws Exception;

    public PicVO queryPicByPicName(String picName) throws Exception;

    public String getOrgAddress(Integer id) throws Exception;

    public int updatePersonPic(Map map) throws Exception;

    public PicVO queryPicByCertNum(String certNum) throws Exception;

    public List<BasicPersonInfoBean> getPersonInfo(Map map) throws Exception;

    int insertPic(Map map);

    int updatePic(Map map);

    List<PicInfoPO> getPicIsCheck();

    List<NewPicInfoPO> getUpdatePic(String picName);
    
    List<PicInfoPO> getBusinessCardInfoByPC();
    
    public Applybean queryPhotoUrl(Map map);
    
    public Integer updatePhotoUrl(Map map);

    

   /* ===============================*/
    public ResultLoginvo login(LoginVO bean) throws Exception;

    public List<BusRcmCardVO> getCardinfo(String certNum);//查找数据中是否存在

    public List<ResultXhcVO> getCardku(String  orgAddress);//查找箱子

    public int insertBin(BusRcmBinVO binVO);

    public  List<BusRcmBinVO> queryBin(String orgAddress);

    public int insertBox(BusRcmBoxVO boxVO);

    public List<BusRcmBoxVO> queryBox(String orgAddress);

    public int insertCard(BusRcmCardVO bean);

    public int updateBasic(String soCardNum);

    public int updateBox(BusRcmBoxVO boxVO);

    public int updateBin(BusRcmBinVO binVO);

    public ResultBusMakeDetal getCardwz(String certNum);

    public List<ResultBusRcmCardApplyVO> getrcmApply(String certNum);

    public int insertApplyPic(BusApplyPicVO bean) throws Exception;

    public ResultUpVO getboxid(String soCardNum);

    public  int updataBox(long boxid);

    public int updataRcm(String soCardNum);

    public String getrcmWdname(Integer id) throws Exception;

    //申领记录
    public List<ResultPersonApplyVO> getapplyhistory(String certnum);

    public int updatePicStatus(String picName);

   // public ResultBasicCardVO getBasicInfo(String soCardNum);
    public int updateCard(ResultUpVO resultUpVO);

    public List<BusRcmCardVO> getHnowcount(BusRcmCardVO bean);

    public  int updateboxnew(long boxid);
    
    //2018/05/8
    public PersonalApply querybyPersonMessage(String certNum) throws Exception;

}
