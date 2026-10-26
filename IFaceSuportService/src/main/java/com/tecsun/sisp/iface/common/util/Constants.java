package com.tecsun.sisp.iface.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqingjie on 15-5-11.
 */
public class Constants {

    public static String RESULT_MESSAGE_SUCCESS = "200";
    public static String RESULT_MESSAGE_EXCEPTION = "999";
    public static String RESULT_MESSAGE_ERROR = "0";
    public static String RESULT_MESSAGE_PARAM = "-300";//参数错误
    //2018/05/09加
    public static String RESULT_MESSAGE_EMPTY = "201";//参数缺失（社保卡业务）

    public static final String TSB = "TSB";//德生宝
    public static final String APP = "App";//手机应用
    public static final String NETPORTAL = "NetPortal";//网办
    public static final String SELFSERVICE = "SelfService";//自助终端
    public static final String WECHAT = "WeChat";//微信

    //自助终端，网办等渠道签到用到的key前缀+uuid,value为当前客户端的ip
    public static String TOKENNAME = "sisp_iface:token:";

    //终端金融业务签到用到的key,value:pik;mac;tdk;patchNo;zdh;shh;seq
    public static String SECRETWK = "sisp_iface:fee:bank_code:zdh:shh";

    //字典组 key
    public static String DICTIONGROUPKEY = "sisp_public:cmp_core:dictionary:{groupId}:detaillist";

    //自助终端超时时间 单位分钟
    public static Integer SELFSERVICETIMEOUT = 120;


    /**
     * 字典表---渠道字典组*
     */
    public static String CHANNELCODE = "channeltype";
    public static final String BANK_GROUP = "BANK";//银行类型
    public static final String CARDTYPE_GROUP = "CARDTYPE";//卡类型
    public static final String CERTYPE_GROUP = "CERTYPE";//证件类型
    public static final String FIVEPERATATUS_GROUP = "FIVEPERATATUS";//(五险)人员状态
    public static final String INSURECANCETYPE_GROUP = "INSURECANCETYPE";//险种类型
    public static final String MEDICALCLASS_GROUP = "MEDICALCLASS";//医疗类别
    public static final String MONEYITEMTYPE_GROUP = "MONEYITEMTYPE";//款项类型
    public static final String PARAM_NATION_GROUP = "PARAM_NATION";//名族
    public static final String PAYSTATUS_GROUP = "PAYSTATUS";//缴费标志
    public static final String PERATATUS_GROUP = "PERATATUS";//人员状态
    public static final String PERSONINSUREDSTATE_GROUP = "PERSONINSUREDSTATE";//个人参保状态
    public static final String REGNATURE_GROUP = "REGNATURE";//户口性质
    public static final String RETIREDCATAGORY_GROUP = "RETIREDCATAGORY";//离退休类别
    public static final String SEX_GROUP = "SEX";//性别
    public static final String APPLYTYPE_GROUP = "APPLYTYPE";//申请类型
    public static final String UNITINSUREDSTATE_GROUP = "UNITINSUREDSTATE";//单位参保状态
    public static final String ARCHIVEAAE017_GROUP = "ARCHIVEAAE017";//性别
    
    
    
    

    /**
     * 字典表---中心医院字典组*
     */

    public static final String SCHEDULEFLAG_GROUP = "scheduleflag";//中心医院-预约状态
    public static final String RESERVEFLAG_GROUP = "ReserveFlag";//中心医院-记录状态
    public static final String TIMEINTERVAL_GROUP = "timeinterval";//中心医院-时间段
    
    public static final String SCHEDULEFLAGVALUE_0 = "0";//中心医院-预约状态(无号可约)
    public static final String SCHEDULEFLAGVALUE_1 = "1";//中心医院-预约状态（有号可约）


    public static final String TS_V_JBXX = "ts_v_jbxx";//17.个人基本信息
    public static final String TS_V_BP_GRCBXX = "ts_v_bp_grcbxx";//1.个人社保基本信息
    public static final String TS_V_BP_GRJFJSXX = "ts_v_bp_grjfjsxx";//2.个人缴费基数信息
    public static final String TS_V_BP_GRJFXX = "ts_v_bp_grjfxx";//3.个人养老保险缴费信息
    public static final String TS_V_BP_GSBXJBXX = "ts_v_bp_gsbxjbxx";//4.个人工伤保险基本信息
    public static final String TS_V_BP_LYBXJBXX = "ts_v_bp_lybxjbxx";//5.失业保险基本信息
    public static final String TS_V_BP_MTBXFYJSXX = "ts_v_bp_mtbxfyjsxx";//6.医疗保险费用结算信息
    public static final String TS_V_BP_MTBXGRJBXX = "ts_v_bp_mtbxgrjbxx";//7.医疗保险个人帐户信息
    public static final String TS_V_BP_MTBXGRJFMX = "ts_v_bp_mtbxgrjfmx";//8.医疗保险个人缴费信息
    public static final String TS_V_BP_SYBXJBXX = "ts_v_bp_sybxjbxx";//9.生育保险基本信息
    public static final String TS_V_BP_SYDYFFXX = "ts_v_bp_sydyffxx";//10.失业人员待遇支付明细表
    public static final String TS_V_BP_YLBXGRJFMX = "ts_v_bp_ylbxgrjfmx";//11.养老保险个人缴费明细
    public static final String TS_V_BP_YLBXJBXX = "ts_v_bp_ylbxjbxx";//12.养老保险基本信息
    public static final String TS_V_BP_YLBXLTXRYDYXX = "ts_v_bp_ylbxltxrydyxx";//13.离退休人员待遇信息
    public static final String TS_V_BP_YLDYFFXX = "ts_v_bp_yldyffxx";//14.养老保险待遇支付信息
    public static final String TS_V_BP_YLGRZHXX = "ts_v_bp_ylgrzhxx";//15.养老保险个人年帐户
    public static final String TS_V_BP_YLYZHXX = "ts_v_bp_ylyzhxx";//16.养老保险月账户

    public static final String DW_CODE = "20";//单位编码；
    public static final String GR_CODE = "10";//个人编码；

    public static final String RYLB_CODE = "9";//医疗类型； 9开关的是居民，非9开头的是职工；(select * from hbsimis_xgjb.AA10 where aaa100='AKC021')
    public static final String RYLB_VALUE1 ="居民";
    public static final String RYLB_VALUE2 ="职工";
    
    //航天医院编号
    public final static String HT_HOSPITAL_CODE = "140034";

    public static Map<String, String> BUSINESSCODE = new HashMap<String, String>();

    static {
        BUSINESSCODE.put("changePhoneNo", "SISP_IFACE_SO_001");
       BUSINESSCODE.put("getPersonIncureSum", "SISP_IFACE_SO_007");
        BUSINESSCODE.put("getHealthIncureSum", "SISP_IFACE_SO_008");
        BUSINESSCODE.put("getLoseFeeIncureSum", "SISP_IFACE_SO_009");
        BUSINESSCODE.put("getBirthInfoIncureSum", "SISP_IFACE_SO_010");
        BUSINESSCODE.put("getHurtInfoIncureSum", "SISP_IFACE_SO_011");
        BUSINESSCODE.put("getPersonInfo", "SISP_IFACE_SO_013");
        BUSINESSCODE.put("getHealthIncureSum", "SISP_IFACE_SO_015");
        BUSINESSCODE.put("getQuitPaySum", "SISP_IFACE_SO_017");
        BUSINESSCODE.put("noticePlatfrom_ht", "SISP_IFACE_HIS_HT_005");
        BUSINESSCODE.put("sendHisSal_ht", "SISP_IFACE_HIS_HT_006");
        BUSINESSCODE.put("getHisZDQH_ht", "SISP_IFACE_HIS_HT_008");
        BUSINESSCODE.put("cancelRegister_ht", "SISP_IFACE_HIS_HT_009");
        BUSINESSCODE.put("getCardInfo", "SISP_IFACE_CARD_001");
        BUSINESSCODE.put("getCardAllInfo", "SISP_IFACE_CARD_002");
        BUSINESSCODE.put("getCardStatus", "SISP_IFACE_CARD_004");
        BUSINESSCODE.put("setStart", "SISP_IFACE_CARD_005");
        BUSINESSCODE.put("setLoss", "SISP_IFACE_CARD_006");
        BUSINESSCODE.put("setHanging", "SISP_IFACE_CARD_007");
        BUSINESSCODE.put("getCard", "SISP_IFACE_CARD_008");
        
        BUSINESSCODE.put("signOn4hex", "SISP_IFACE_BANK_001");//金融-签到
        BUSINESSCODE.put("queryTransPayInfo", "SISP_IFACE_BANK_002");//金融-社保卡查询交易(湖北)
        BUSINESSCODE.put("consumer4HexFee", "SISP_IFACE_BANK_003");//金融-社保卡缴费交易(湖北)
        
        //就业（JobQueryController）
        BUSINESSCODE.put("getJobOffer", "SISP_IFACE_JOB_001");
        BUSINESSCODE.put("getJobOfferInfo", "SISP_IFACE_JOB_002");
        BUSINESSCODE.put("getPersonJob", "SISP_IFACE_JOB_003");
        BUSINESSCODE.put("addJobSeekerInfo", "SISP_IFACE_JOB_004");
        BUSINESSCODE.put("modifyJobSeekerInfo", "SISP_IFACE_JOB_005");
        BUSINESSCODE.put("personUserLogin", "SISP_IFACE_JOB_006");
        BUSINESSCODE.put("isPhoneNumExist", "SISP_IFACE_JOB_007");
        BUSINESSCODE.put("getAllDic", "SISP_IFACE_JOB_008");
        BUSINESSCODE.put("getAllSecondDic", "SISP_IFACE_JOB_009");
        BUSINESSCODE.put("queryJobFairs", "SISP_IFACE_JOB_010");
        BUSINESSCODE.put("getZphInfo", "SISP_IFACE_JOB_011");
        BUSINESSCODE.put("queryCompanys", "SISP_IFACE_JOB_012");
        BUSINESSCODE.put("getCompanyInfo", "SISP_IFACE_JOB_013");
        BUSINESSCODE.put("getCompanyImgAndVideo", "SISP_IFACE_JOB_014");
        BUSINESSCODE.put("getQzyxByQzz", "SISP_IFACE_JOB_015");
        BUSINESSCODE.put("addJobDirection", "SISP_IFACE_JOB_016");
        BUSINESSCODE.put("dropJobDirection", "SISP_IFACE_JOB_017");
        BUSINESSCODE.put("getCompanyByZph", "SISP_IFACE_JOB_018");
        BUSINESSCODE.put("savePicture", "SISP_IFACE_JOB_019");
        BUSINESSCODE.put("lookPicture", "SISP_IFACE_JOB_020");
        BUSINESSCODE.put("getAllMajor", "SISP_IFACE_JOB_021");
        BUSINESSCODE.put("sendJobChoice", "SISP_IFACE_JOB_022");
        BUSINESSCODE.put("addIdCardInfo", "SISP_IFACE_JOB_023");
        BUSINESSCODE.put("getPolicyDocuments", "SISP_IFACE_JOB_024");
        BUSINESSCODE.put("getJobLineInfo", "SISP_IFACE_JOB_025");
        BUSINESSCODE.put("smsSendCode", "SISP_IFACE_JOB_026");
        BUSINESSCODE.put("registerByIdCard", "SISP_IFACE_JOB_027");
        BUSINESSCODE.put("queryPolicyDocument", "SISP_IFACE_JOB_028");
        BUSINESSCODE.put("queryAgencyIntroduction", "SISP_IFACE_JOB_029");
        BUSINESSCODE.put("querySalaryguidance", "SISP_IFACE_JOB_030");
        BUSINESSCODE.put("getPdf", "SISP_IFACE_JOB_031");
        BUSINESSCODE.put("addViewInfo", "SISP_IFACE_JOB_032");
    }

    //航天医院业务状态值   预约-01，缴费成功-02，挂号成功-03，已取号-04，挂号失败-05，未取号-06，已退号-07,退号失败-08，已取号-09,已打印凭证-10；
    public static final String HIS_HT_STATUS_01 = "01";
    public static final String HIS_HT_STATUS_02 = "02";
    public static final String HIS_HT_STATUS_03 = "03";
    public static final String HIS_HT_STATUS_04 = "04";
    public static final String HIS_HT_STATUS_05 = "05";
    public static final String HIS_HT_STATUS_06 = "06";
    public static final String HIS_HT_STATUS_07 = "07";
    public static final String HIS_HT_STATUS_08 = "08";
    public static final String HIS_HT_STATUS_09 = "09";
    public static final String HIS_HT_STATUS_10 = "10";

    //渠道接口类型 大众端-02、德生宝-01
    public static final String CHANNELTYPE_01 = "01";
    public static final String CHANNELTYPE_02 = "02";

    //五险险种
    public static final String  PesionCvrgTypeUrl="'11','12','14','15'";//养老保险险种；
    public static final String HealthCvrgTypeUrl="'31','32'";//医疗保险险种；
    public static final String LoseFeeCvrgTypeUrl="'21'";//失业保险险种；
    public static final String BirthInfoCvrgTypeUrl="'51'";//生育保险险种；
    public static final String HurtInfoCvrgTypeUrl="'41'";//工伤保险险种；

    //业务分析子系统请求地址
    public static final String TRANSANALYSIS_URI="/ans/translog/addTransLog";
    //字典表查询，根据输入的groupid查询({groupid})
    public static final String CHANNLCODES_URI="/core/dictionary/queryRedisDictionByGroupId";
    //查询设备管理子系统校验设备是否存在({deviceId})
    public static final String DEV_URI="/dev/deviceRegist/checkDevExist";

    //查询设备表是否需要更新秘钥(/getIsupdateWKByMac/{macAddress})
    public static final String ISUPDATEWK_URI="/dev/deviceRegist/getIsupdateWKByMac";
    //客户端更新完秘钥后需要把设备表是否更新秘钥的状态改为 0 已更新（/updateIsupdateWKByMac/{macAddress}）
    public static final String UPDATEWKBYMAC_URI="/dev/deviceRegist/updateIsupdateWKByMac";
    //根据设备编码获取银行，终端号，商户号，用于请求到bankservice(queryBankByDeviceId/{deviceId})
    public static final String GETBANK_URI="/core/bank/queryBankByDeviceId";

    //==============================地税缴费模式========================
    //签到业务
    public static final String FEESIGNON4HEX="/bank/trans/feeSignon4hex";
    //余额查询
    public static final String QUERY4HEX="/bank/trans/balanceQuery4Hex";
    //消费
    public static final String CONSUMER4HEXFEE="/bank/trans/consumer4HexFee";
    //转账
    public static final String TRANSFER4HEX="/bank/trans/transfer4Hex";

    //============================商家模式==========================
    //根据设备编码获取商家，终端号，商户号(queryBankByDeviceId/{deviceId})
    public static final String GETMERCHANT_URI="/core/bank/queryMerchantByDeviceId";
    //签到业务
    public static final String MERCHANTSIGNON4HEX="/bank/trans/merchantSignOn4hex";
    //消费查询
    public static final String CONSUMER4HEXMERCHANT="/bank/trans/consumer4HexMerchant";
    //余额查询
    public static final String QUERYBALANCE4HEXMERCHANT="/bank/trans/queryBalance4HexMerchant";
    //取款
    public static final String DRAWCASH4HEX="/bank/trans/drawCash4Hex";

    //交易结果查询
    public static final String GETTRANSRESULT="/bank/trans/getTransResult";

    //保存读卡信息
    public static final String HANGTIANHISJYLSSAVE="/his/hangtianHis/insertHostTradeSerialInfo";

    //保存航天医院缴费组装报文的信息
    public static final String HANGTIANHISJFXML="/his/hangtianHis/insertHisMxInfo";

    //记录TSB、SelfService预约挂号的信息
    public static final String INSERTBUSINFO_HT="/his/hangtianHis/insertBusInfo_ht";
    //获取中心医院就诊号
    public static final String GETVISIDSEQ_HT="/his/hangtianHis/getZXHisVisidSeq";
    //终端缴费
    public static final String GETHISTERMINALPAY="/his/hangtianHis/getHisTerminalPay";
    //修改预约挂号信息
    public static final String UPDATEBUSINFO_HT="/his/hangtianHis/updateBusInfo_ht";
    //插入预约挂号信息
    //public static final String INSERTBUSINFO_HT="/his/hangtianHis/insertBusInfo_ht";
    //获取航天医院终端取号
    public static final String GETHISZDQH_HT="/his/hangtianHis/getHisZDQH_ht";
    //获取已经有效挂号的列表信息
    public static final String GETREGISTERINFOLIST_HT="/his/hangtianHis/getRegisterInfoList_ht";
    //验证参保人是否可挂号（所有的预约挂号前必须先调用此接口）
    public static final String CHECKREGISTER_HT="/his/hangtianHis/checkRegister_ht";

    //获取航天医院终端取号
    public static final String GETHIXCQH_HT="/his/hangtianHis/getHisXCQH_ht";
    //根据ID查询信息
    public static final String GETHISByID_HT="/his/hangtianHis/getHisByID";
    
    //时间段AM上午,PM 下午
    public static final String TIMEINTERVALAM="上午";
    //时间段AM上午,PM 下午
    public static final String TIMEINTERVALPM="下午";
    
    //时间段上午 code  AM
    public static final String TIMEINTERVALAMCODE="AM";
    //时间段AM上午,PM 下午
    public static final String TIMEINTERVALPMCODE="PM";
    
    //统筹区域
    public static final String DISTRICT_GROUP="DISTRICT";





    //============================就业失业服务请求url==========================//
    public static final String BaseUrl = "http://10.132.1.85:7002";
    //    就业失业登记证查询
    public static final String Cc0mUrl = BaseUrl + "/hrmis/services/QueryCc0mServiceProxy?wsdl";
    //    就业信息查询
    public static final String Cc03Url = BaseUrl + "/hrmis/services/QueryCc03ServiceProxy?wsdl";
    //    失业信息查询
    public static final String Cc02Url = BaseUrl + "/hrmis/services/QueryCc02ServiceProxy?wsdl";
    //    就业援助信息查询
    public static final String Ac13Url = BaseUrl + "/hrmis/services/QueryAc13ServiceProxy?wsdl";
    //    就业扶持政策信息查询
    public static final String Ac14Url = BaseUrl + "/hrmis/services/QueryAc14ServiceProxy?wsdl";
    //    公共就业服务信息查询
    public static final String Ac15Url = BaseUrl + "/hrmis/services/QueryAc15ServiceProxy?wsdl";
    //    失业保险待遇信息查询
    public static final String Ac16Url = BaseUrl + "/hrmis/services/QueryAc16ServiceProxy?wsdl";
    //    年检信息查询
    public static final String Ac21Url = BaseUrl + "/hrmis/services/QueryAc21ServiceProxy?wsdl";
    //    其他记载事项查询
    public static final String Ac19Url = BaseUrl + "/hrmis/services/QueryAc19ServiceProxy?wsdl";

    public static final String soapaction = "http://webservice.apps.lemis.neusoft.com";

    public static Map<String, String> NATION = new HashMap<String, String>();
    static {
        NATION.put("01", "汉族");
        NATION.put("02", "蒙古族");
        NATION.put("03", "回族");
        NATION.put("04", "藏族");
        NATION.put("05", "维吾尔族");
        NATION.put("06", "苗族");
        NATION.put("07", "彝族");
        NATION.put("08", "壮族");
        NATION.put("09", "布依族");
        NATION.put("10", "朝鲜族");
        NATION.put("11", "满族");
        NATION.put("12", "侗族");
        NATION.put("13", "瑶族");
        NATION.put("14", "白族");
        NATION.put("15", "土家族");
        NATION.put("16", "哈尼族");
        NATION.put("17", "哈萨克族");
        NATION.put("18", "傣族");
        NATION.put("19", "黎族");
        NATION.put("20", "傈傈族");
        NATION.put("21", "佤族");
        NATION.put("22", "畲族");
        NATION.put("23", "高山族");
        NATION.put("24", "拉祜族");
        NATION.put("25", "水族");
        NATION.put("26", "东乡族");
        NATION.put("27", "纳西族");
        NATION.put("28", "景颇族");
        NATION.put("29", "柯尔克孜族");
        NATION.put("30", "土族");
        NATION.put("31", "达翰尔族");
        NATION.put("32", "仫佬族");
        NATION.put("33", "羌族");
        NATION.put("34", "布朗族");
        NATION.put("35", "撒拉族");
        NATION.put("36", "毛南族");
        NATION.put("37", "仡佬族");
        NATION.put("38", "锡伯族");
        NATION.put("39", "阿昌族");
        NATION.put("40", "普米族");
        NATION.put("41", "塔吉克族");
        NATION.put("42", "怒族");
        NATION.put("43", "乌孜别克族");
        NATION.put("44", "俄罗斯族");
        NATION.put("45", "鄂温克族");
        NATION.put("46", "德昂族");
        NATION.put("47", "保安族");
        NATION.put("48", "裕固族");
        NATION.put("49", "京族");
        NATION.put("50", "塔塔尔族");
        NATION.put("51", "独龙族");
        NATION.put("52", "鄂伦春族");
        NATION.put("53", "赫哲族");
        NATION.put("54", "门巴族");
        NATION.put("55", "珞巴族");
        NATION.put("56", "基诺族");
    }


    public static Map<String, String> NATION_Code = new HashMap<String, String>();
    static {
        NATION_Code.put("汉","01");
        NATION_Code.put("蒙古","02");
        NATION_Code.put("回","03");
        NATION_Code.put("藏","04");
        NATION_Code.put("维吾尔","05");
        NATION_Code.put("苗", "06");
        NATION_Code.put("彝", "07");
        NATION_Code.put("壮", "08");
        NATION_Code.put("布依", "09");
        NATION_Code.put("朝鲜","10");
        NATION_Code.put("满","11");
        NATION_Code.put("侗","12");
        NATION_Code.put("瑶","13");
        NATION_Code.put("白","14");
        NATION_Code.put("土家","15");
        NATION_Code.put("哈族","16");
        NATION_Code.put("哈萨克","17");
        NATION_Code.put("傣","18");
        NATION_Code.put("黎","19");
        NATION_Code.put("傈傈","20");
        NATION_Code.put("佤","20");
        NATION_Code.put("畲","22");
        NATION_Code.put("高山","23");
        NATION_Code.put("拉祜","24");
        NATION_Code.put("水","25");
        NATION_Code.put("东乡","26");
        NATION_Code.put("纳西","27");
        NATION_Code.put("景颇","28");
        NATION_Code.put("柯尔克孜","29");
        NATION_Code.put("土","30");
        NATION_Code.put("达翰尔","31");
        NATION_Code.put("仫佬","32");
        NATION_Code.put("羌","33");
        NATION_Code.put("布朗","34");
        NATION_Code.put("撒拉","35");
        NATION_Code.put("毛南","36");
        NATION_Code.put("仡佬","37");
        NATION_Code.put("锡伯","38");
        NATION_Code.put("阿昌","39");
        NATION_Code.put("普米","40");
        NATION_Code.put("塔吉克","41");
        NATION_Code.put("怒","42");
        NATION_Code.put("乌孜别克","43");
        NATION_Code.put("俄罗斯","44");
        NATION_Code.put("鄂温克","45");
        NATION_Code.put("德昂","46");
        NATION_Code.put("保安","47");
        NATION_Code.put("裕固","48");
        NATION_Code.put("京","49");
        NATION_Code.put("塔塔尔","50");
        NATION_Code.put("独龙","51");
        NATION_Code.put("鄂伦春","52");
        NATION_Code.put("赫哲","53");
        NATION_Code.put("门巴","54");
        NATION_Code.put("珞巴","55");
        NATION_Code.put("基诺","56");
    }

    public static Map<String, String> EDUCATION = new HashMap<String, String>();
    static {
        EDUCATION.put("10", "研究生及以上");
        EDUCATION.put("11", "博士研究生");
        EDUCATION.put("14", "硕士研究生");
        EDUCATION.put("20", "大学本科");
        EDUCATION.put("30", "大学专科");
        EDUCATION.put("40", "中专技校");
        EDUCATION.put("41", "中等专科");
        EDUCATION.put("44", "职业高中");
        EDUCATION.put("47", "技工学校");
        EDUCATION.put("61", "高中");
        EDUCATION.put("70", "初中及以下");
        EDUCATION.put("71", "初级中学");
        EDUCATION.put("81", "小学");
        EDUCATION.put("90", "其他");
    }

    public static Map<String, String> MARITALSTATUS = new HashMap<String, String>();
    static {
        MARITALSTATUS.put("1", "已婚");
        MARITALSTATUS.put("2", "未婚");
        MARITALSTATUS.put("3", "离异");
        MARITALSTATUS.put("4", "丧偶");
    }

    public static Map<String, String> SEX = new HashMap<String, String>();
    static {
        SEX.put("男", "01");
        SEX.put("女", "02");
    }

    public static Map<String, String> POLITICALOUTLOOK = new HashMap<String, String>();
    static {
        POLITICALOUTLOOK.put("01", "中共党员");
        POLITICALOUTLOOK.put("02", "中共预备党员");
        POLITICALOUTLOOK.put("03", "共青团员");
        POLITICALOUTLOOK.put("04", "民革会员");
        POLITICALOUTLOOK.put("05", "民盟盟员");
        POLITICALOUTLOOK.put("06", "民建会员");
        POLITICALOUTLOOK.put("07", "民进会员");
        POLITICALOUTLOOK.put("08", "农工党党员");
        POLITICALOUTLOOK.put("09", "致公党党员");
        POLITICALOUTLOOK.put("10", "九三学社社员");
        POLITICALOUTLOOK.put("11", "台盟盟员");
        POLITICALOUTLOOK.put("12", "无党派民主人士");
        POLITICALOUTLOOK.put("13", "群众");
    }


    public static Map<String, String> PHYSICAL = new HashMap<String, String>();
    static {
        PHYSICAL.put("1", "健康");
        PHYSICAL.put("5", "残疾");
    }

    public static Map<String, String> RESIDENCE = new HashMap<String, String>();
    static {
        RESIDENCE.put("10", "非农业户口（城镇）");
        RESIDENCE.put("11", "本地非农业户口（本地城镇）");
        RESIDENCE.put("12", "外地非农业户口（外地城镇）");
        RESIDENCE.put("20", "农业户口（农村）");
        RESIDENCE.put("21", "本地农业户口（本地农村）");
        RESIDENCE.put("22", "外地农业户口（外地农村）");
        RESIDENCE.put("30", "台港澳人员");
        RESIDENCE.put("40", "外籍人士");
        RESIDENCE.put("50", "居民户");
    }


    public static Map<String, String> ACCOUNTPROTIES = new HashMap<String, String>();
    static {
        ACCOUNTPROTIES.put("本省农业", "1");
        ACCOUNTPROTIES.put("本省非农业", "2");
        ACCOUNTPROTIES.put("外省农业", "3");
        ACCOUNTPROTIES.put("外省非农业", "4");
        ACCOUNTPROTIES.put("本省居民", "5");
        ACCOUNTPROTIES.put("外省居民", "6");
        ACCOUNTPROTIES.put("港澳台", "7");
        ACCOUNTPROTIES.put("外籍", "8");
    }

    public static Map<String, String> Voca_qual_level = new HashMap<String, String>();
    static {
        Voca_qual_level.put("1", "职业资格一级(高级技师)");
        Voca_qual_level.put("2", "职业资格二级(技师）");
        Voca_qual_level.put("3", "职业资格三级(高级)");
        Voca_qual_level.put("4", "职业资格四级(中级)");
        Voca_qual_level.put("5", "职业资格五级(初级)");
    }

    public static Map<String, String> InsuranceTypes = new HashMap<String, String>();
    static {
        InsuranceTypes.put("1", "养老");
        InsuranceTypes.put("2", "医疗");
        InsuranceTypes.put("3", "失业");
        InsuranceTypes.put("4", "工伤");
        InsuranceTypes.put("5", "生育");
    }

    public static Map<String, String> Current_State = new HashMap<String, String>();
    static {
        Current_State.put("1", "无效");
        Current_State.put("2", "有效");
        Current_State.put("3", "冻结");
        Current_State.put("4", "注销");
    }

    public static Map<String, String> Employment_Kind = new HashMap<String, String>();
    static {
        Employment_Kind.put("10", "原固定职工");
        Employment_Kind.put("20", "城镇合同制");
        Employment_Kind.put("30", "农民合同制");
        Employment_Kind.put("40", "临时工");
        Employment_Kind.put("50", "聘任制");
        Employment_Kind.put("60", "全日制");
        Employment_Kind.put("70", "非全日制");
        Employment_Kind.put("90", "其他");
    }

    public static Map<String, String> Work_Kind = new HashMap<String, String>();
    static {
        Work_Kind.put("010", "单位就业");
        Work_Kind.put("020", "自主就业");
        Work_Kind.put("021", "个体经营");
        Work_Kind.put("022", "灵活就业");
        Work_Kind.put("023", "其他自主就业");
        Work_Kind.put("030", "公益性岗位安置");
        Work_Kind.put("990", "其它就业形式");
    }

    public static Map<String, String> PERSONSTATUS = new HashMap<String, String>();
    static {
        PERSONSTATUS.put("就业", "1");
        PERSONSTATUS.put("退休", "2");
        PERSONSTATUS.put("离休", "3");
        PERSONSTATUS.put("待业", "4");
        PERSONSTATUS.put("无业", "5");
        PERSONSTATUS.put("从未就业", "6");
        PERSONSTATUS.put("务农", "7");
        PERSONSTATUS.put("退职", "8");
        PERSONSTATUS.put("失业", "9");
        PERSONSTATUS.put("就读", "10");
        PERSONSTATUS.put("外出务工", "11");
        PERSONSTATUS.put("其他", "99");
    }

    public static Map<String, String> Material_List = new HashMap<String, String>();
    static {
        Material_List.put("1", "劳动合同");
        Material_List.put("2", "解除劳动关系证明");
        Material_List.put("3", "毕业证书（明）");
        Material_List.put("4", "参保缴费证明");
        Material_List.put("5", "工商营业执照（组织起来就业证书）");
        Material_List.put("6", "失地（林）证明灵活就业证明");
        Material_List.put("7", "其他证明");
    }

    public static Map<String, String> Employ_Diff_Level = new HashMap<String, String>();
    static {
        Employ_Diff_Level.put("010", "国企下岗失业人员");
        Employ_Diff_Level.put("020", "国企关闭破产安置人员");
        Employ_Diff_Level.put("030", "集体企业下岗职工");
        Employ_Diff_Level.put("040", "享受低保的长期失业人员");
        Employ_Diff_Level.put("050", "大龄就业困难人员");
        Employ_Diff_Level.put("060", "残疾人");
        Employ_Diff_Level.put("070", "零就业家庭成员");
        Employ_Diff_Level.put("080", "失地农民");
        Employ_Diff_Level.put("090", "农村零转移就业贫困家庭成员");
        Employ_Diff_Level.put("100", "毕业一年以上未就业的高校毕业生");
        Employ_Diff_Level.put("110", "各级社会福利机构供养的成年孤儿和社会成年孤儿");
        Employ_Diff_Level.put("990", "其他");
    }

    public static Map<String,String> EMPLOYMENT_LOGOUT_REASON = new HashMap<String, String>();
    static {
        EMPLOYMENT_LOGOUT_REASON.put("01","参军");
        EMPLOYMENT_LOGOUT_REASON.put("02","升学");
        EMPLOYMENT_LOGOUT_REASON.put("03","移居外省市");
        EMPLOYMENT_LOGOUT_REASON.put("04","移居境外");
        EMPLOYMENT_LOGOUT_REASON.put("05","退休");
        EMPLOYMENT_LOGOUT_REASON.put("06","两劳");
        EMPLOYMENT_LOGOUT_REASON.put("07","失踪");
        EMPLOYMENT_LOGOUT_REASON.put("08","死亡");
        EMPLOYMENT_LOGOUT_REASON.put("99","其他");
    }

    public static Map<String,String> UNEMPLOYMENT_LOGOUT_REASON = new HashMap<String, String>();
    static {
        UNEMPLOYMENT_LOGOUT_REASON.put("10","享受基本养老保险待遇的和男满60女满55周岁");
        UNEMPLOYMENT_LOGOUT_REASON.put("20","完全丧失劳动能力");
        UNEMPLOYMENT_LOGOUT_REASON.put("30","入学、服兵役、移居境外");
        UNEMPLOYMENT_LOGOUT_REASON.put("40","被判刑收监执行或被劳动教养");
        UNEMPLOYMENT_LOGOUT_REASON.put("50","连续3个月未与失业登记机构联系");
        UNEMPLOYMENT_LOGOUT_REASON.put("60","已从事有稳定收入工作且收入不低于当地城市居民最低生活保障标准");
        UNEMPLOYMENT_LOGOUT_REASON.put("70","死亡");
        UNEMPLOYMENT_LOGOUT_REASON.put("99","其他");
    }

    public static Map<String,String> UNEMPLOYMENT_REASON = new HashMap<String, String>();
    static {
        UNEMPLOYMENT_REASON.put("10","年满16周岁，从各类学校毕业、肄业，未能继续升学的");
        UNEMPLOYMENT_REASON.put("20","与企业解除或终止劳动关系的");
        UNEMPLOYMENT_REASON.put("30","从机关事业单位辞职或被辞退解聘");
        UNEMPLOYMENT_REASON.put("40","由农业户口转为非农业户口，并失去承包土地的（含转产渔民和牧民）");
        UNEMPLOYMENT_REASON.put("50","军人退出现役、且未纳入国家统一安置的");
        UNEMPLOYMENT_REASON.put("60","刑满释放或假释、监外执行的");
        UNEMPLOYMENT_REASON.put("70","劳动教养期满或提前解除劳动教养的");
        UNEMPLOYMENT_REASON.put("80","个体工商户业主或私营企业业主停止经营的");
        UNEMPLOYMENT_REASON.put("90","其他");
    }

    public static Map<String,String> ACCEPT_ASSISTANCE_WAY = new HashMap<String, String>();
    static {
        ACCEPT_ASSISTANCE_WAY.put("1","推荐就业");
        ACCEPT_ASSISTANCE_WAY.put("2","职业培训");
        ACCEPT_ASSISTANCE_WAY.put("3","公益性岗位安置");
        ACCEPT_ASSISTANCE_WAY.put("4","创业服务");
        ACCEPT_ASSISTANCE_WAY.put("5","灵活就业");
        ACCEPT_ASSISTANCE_WAY.put("6","社会保险补贴");
        ACCEPT_ASSISTANCE_WAY.put("7","其他");
    }

    public static Map<String,String> SUBMIT_RECORD_CONTENTS = new HashMap<String, String>();
    static {
        SUBMIT_RECORD_CONTENTS.put("1","国有（集体）企业下岗失业证明");
        SUBMIT_RECORD_CONTENTS.put("2","低保证");
        SUBMIT_RECORD_CONTENTS.put("3","零就业家庭证明");
        SUBMIT_RECORD_CONTENTS.put("4","其他");
    }

    public static Map<String,String> LOGOUT_REASON = new HashMap<String, String>();
    static {
        LOGOUT_REASON.put("010", "单位就业");
        LOGOUT_REASON.put("020", "个体就业");
        LOGOUT_REASON.put("030", "灵活就业");
        LOGOUT_REASON.put("040", "退休");
        LOGOUT_REASON.put("050", "丧失劳动能力");
        LOGOUT_REASON.put("060", "退出市场");
        LOGOUT_REASON.put("070", "判刑劳教");
        LOGOUT_REASON.put("080", "不接受服务");
        LOGOUT_REASON.put("090", "未主动联系");
        LOGOUT_REASON.put("990", "其他");
    }

    public static Map<String,String> ENJOY_POLICY_TYPE = new HashMap<String, String>();
    static {
        ENJOY_POLICY_TYPE.put("010", "公益性岗位补贴");
        ENJOY_POLICY_TYPE.put("020", "社会保险补贴");
        ENJOY_POLICY_TYPE.put("030", "职业介绍补贴");
        ENJOY_POLICY_TYPE.put("040", "职业培训补贴");
        ENJOY_POLICY_TYPE.put("050", "职业技能鉴定补贴");
        ENJOY_POLICY_TYPE.put("060", "税收优惠政策");
        ENJOY_POLICY_TYPE.put("070", "行政事业性收费减免政策");
        ENJOY_POLICY_TYPE.put("080", "小额担保贷款贴息政策");
        ENJOY_POLICY_TYPE.put("990", "其他就业扶持政策");
    }

    public static Map<String,String> SERVICE_DETAILS = new HashMap<String, String>();
    static {
        SERVICE_DETAILS.put("010", "职业介绍");
        SERVICE_DETAILS.put("020", "职业指导");
        SERVICE_DETAILS.put("030", "创业服务");
        SERVICE_DETAILS.put("040", "职业培训");
        SERVICE_DETAILS.put("050", "其他服务");
    }

    public static Map<String,String> UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS = new HashMap<String, String>();
    static {
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("10", "不符合享受条件");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("20", "正在享受");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("30", "本次享受期满");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("40", "停止领取失业保险金");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("41", "重新就业");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("42", "应征服兵役");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("43", "移居境外");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("44", "享受基本养老保险待遇");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("45", "被判刑收监执行或者被劳动教养");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("46", "无正当理由，拒不接受政府机构介绍的工作");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("47", "有法律、行政法规定的其他情形");
        UNEMPLOYMENT_INSURANCE_TREATMENT_STATUS.put("90", "其他");

    }

    public static Map<String,String> PERSON_TYPE = new HashMap<String, String>();
    static {
        PERSON_TYPE.put("城镇职工","1");
        PERSON_TYPE.put("城镇居民","2");
        PERSON_TYPE.put("农村居民", "3");
        PERSON_TYPE.put("其他","9");
    }
    
    //人社局单位
    public static final String ORGANIZATION_NAME="孝感人社局";
    
    public static final String SMSSENDURL = "http://10.132.1.78:8081/sms/send";
    
    //航天医院
    //public static final String HOSWEBSERVICEURL= "http://10.132.7.3:3388/SiCardWebService.asmx";
    public static final String HOSWEBSERVICEURL= "http://10.132.1.85:3388/SiCardWebService.asmx";
    
  //中心医院
    //public static final String HOSWEBSERVICEURL= "http://10.132.7.3:3388/SiCardWebService.asmx";
    public static final String ZXHOSWEBSERVICEURL= "http://10.132.1.85/OrderTreat/services/HosiptalManager";
    
    public static final String CITYTOWNURL="http://10.128.8.17:8080/dxserveic";
    
  //建档
    public static final String SIGNCARDURL="http://10.132.1.85/powermopsrv/services/sign";
    
    //获取患者信息
    public static final String GETPATIENTINFOURL ="http://10.132.1.85/powermopsrv/services/general";
    
    //省农保接口
    public static final String DXSERVEICURL ="http://10.132.1.85:8080/dxservice";

    //湖北省农保接口
    public static final String HBNBSERVEICURL ="http://10.128.5.155:8060/ws/commonInterface/c1/hbjcwdjk";

    // DataExchange服务登录用户名，默认为admin
    public static final String HBNBusername ="xgscx";
    // DataExchange服务登录用户名，默认为1
    private static String requiredEnvironment(String key) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }
        return value;
    }

    public static String HBNBpassword() {
        return requiredEnvironment("HBNB_SERVICE_PASSWORD");
    }

    public static final String HBNBJBXXtaskOId ="GGYWDW_HBCXJB_002";//省农保-个人基本参保信息查询
    public static final String HBNBBGXXtaskOId ="GGYWDW_HBCXJB_003";// 省农保-变更信息查询
    public static final String HBNBJFXXtaskOId ="GGYWDW_HBCXJB_004";// 省农保-缴费信息查询
    public static final String HBNBNZHtaskOId ="GGYWDW_HBCXJB_005";// 省农保-个人年账户信息查询
    public static final String HBNBDYBFXXtaskOId ="GGYWDW_HBCXJB_006";// 省农保-待遇支付查询


    //档案查询
    public static final String ArchiveURL="http://10.132.1.85:7002/hrmis/services/DaiLiServiceProxy";

    public static final String AAE017="420502";

    // DataExchange服务登录用户名，默认为admin
    public static final String SNBusername ="admin";
    // DataExchange服务登录用户名，默认为1
    public static String SNBpassword() {
        return requiredEnvironment("SNB_SERVICE_PASSWORD");
    }
    // 任务OID，定义的数据查询任务业务标识，可以到“任务”页面查看
    public static final String SNBJBXXtaskOId ="QUERY_JBXX_FROM_DB";//省农保-个人基本参保信息查询
    public static final String SNBBGXXtaskOId ="QUERY_BGXX_FROM_DB";// 省农保-变更信息查询
    public static final String SNBJFXXtaskOId ="QUERY_JFXX_FROM_DB";// 省农保-缴费信息查询
    public static final String SNBNZHtaskOId ="QUERY_NZH_FROM_DB";// 省农保-个人年账户信息查询
    public static final String SNBDYBFXXtaskOId ="QUERY_DYBFXX_FROM_DB";// 省农保-待遇支付查询

   // 客户端应用名称，可以为空
   public static final String SNBappName ="城乡居保公共服务查询平台";
   
   //人脸验证平台--接口地址
   public static Map<String,String> TECSUN_VERIFY_URL = new HashMap<String, String>();
   static{
	   TECSUN_VERIFY_URL.put("getPhoto", "/iface/business/getPhoto"); //获取证件照
	   TECSUN_VERIFY_URL.put("ImgFilter", "/iface/business/ImgFilter");//人工审核
	   TECSUN_VERIFY_URL.put("uploadPhoto", "/iface/business/uploadPhoto");//上传照片
	   TECSUN_VERIFY_URL.put("getVerifyResult", "/iface/business/getVerifyResult");//获取人检认证结果信息
	   TECSUN_VERIFY_URL.put("comparePhoto", "/iface/business/comparePhoto");//机器审核
	   TECSUN_VERIFY_URL.put("nrtPicCheck", "/iface/business/nrtPicCheck");//人脸比对与人工审核nrtPicCheck机检接口（非实时）
   }
   //人脸验证平台--照片信息存放的路径
   public static final String TECSUN_VERIFY_PHOTO_PATH = "/home/disk/voicePrint/xg_sbPic"; 
   
   //人脸验证登陆类别
   public static Map<String,Integer> TECSUN_VERIFY_LOGIN_TYPE = new HashMap<String, Integer>();
   static{
	   TECSUN_VERIFY_LOGIN_TYPE.put("loginType_1", 1);//使用身份证+姓名+密码
	   TECSUN_VERIFY_LOGIN_TYPE.put("loginType_2", 2);//使用身份证+姓名+照片
	   TECSUN_VERIFY_LOGIN_TYPE.put("loginType_3", 3);//使用身份证+姓名 -- 读社保卡
   }
   
   //人脸验证人员表中isCheck状态的描述--注册用
   public static Map<String,String> TECSUN_VERIFY_PERSON_ISCHECK = new HashMap<String, String>();
   static{
	   TECSUN_VERIFY_PERSON_ISCHECK.put("PERSON_ISCHECK_01", "01");//注册信息未审核
	   TECSUN_VERIFY_PERSON_ISCHECK.put("PERSON_ISCHECK_02", "02");//人工审核通过
	   TECSUN_VERIFY_PERSON_ISCHECK.put("PERSON_ISCHECK_03", "03");//照片上传成功
	   TECSUN_VERIFY_PERSON_ISCHECK.put("PERSON_ISCHECK_04", "04");//三张照片审核成功
	   TECSUN_VERIFY_PERSON_ISCHECK.put("PERSON_ISCHECK_05", "05");//更新密码
   }
   
   public static Map<String,String> TECSUN_VERIFY_BUSSINESS_AAE038 = new HashMap<String, String>();
   static{
	 /*  TECSUN_VERIFY_BUSSINESS_AAE038.put("bussiness_success", "0");//验证成功
	   TECSUN_VERIFY_BUSSINESS_AAE038.put("bussiness_fail", "1");//验证失败
*/	   
	   TECSUN_VERIFY_BUSSINESS_AAE038.put("bussiness_success", "1");//机检认证成功
	   TECSUN_VERIFY_BUSSINESS_AAE038.put("bussiness_fail", "0");//机检认证失败
	   
	   TECSUN_VERIFY_BUSSINESS_AAE038.put("bussiness_success_value", "1");//验证成功值
	   TECSUN_VERIFY_BUSSINESS_AAE038.put("bussiness_fail_value", "0");//验证失败值
   }
   
   public static Map<String,String> TECSUN_VERIFY_BUSSINESS_STATUS = new HashMap<String, String>();
   static{
	   TECSUN_VERIFY_BUSSINESS_STATUS.put("bussiness_success", "00");//验证成功
	   TECSUN_VERIFY_BUSSINESS_STATUS.put("bussiness_fail", "01");//验证失败
   }
   
   

  //生存认证状态：1.机检成功；2.机检失败；3.待审核；4.人检成功；5.人检失败
   public static Map<String,String> FACESTATUS = new HashMap<String, String>();
   static{
	   /*FACESTATUS.put("personcheck_success", "1");//机检成功
	   FACESTATUS.put("personcheck_fail", "2");//机检失败
	   FACESTATUS.put("machincheck_no_success", "3");//待审核
	   FACESTATUS.put("machincheck_success", "4");//人检成功
	   FACESTATUS.put("machincheck_fail", "5");//人检失败*/ 
	   
	   FACESTATUS.put("machincheck_success", "1");//机检成功
	   FACESTATUS.put("machincheck_fail", "2");//机检失败
	   FACESTATUS.put("machincheck_no_success", "3");//待审核
	   FACESTATUS.put("personcheck_success", "4");//人检成功
	   FACESTATUS.put("personcheck_fail", "5");//人检失败
	   
	   
  }
   
   //生存认证-认证方式
   public static String FACESTATUSAUTHTYPE ="FACESTATUSAUTHTYPE";
   
   //生存结果-认证结果
   public static String FACESTATUSVALUE ="FACESTATUSVALUE";
   
   
   //是离退休人员或遗嘱
   public static String RESULT_MESSAGE_RETIREMENT  = "301";
   //非离退休人员或遗嘱
   public static String RESULT_MESSAGE_NORETIREMENT  = "302";
   //已采集照片
   public static String RESULT_MESSAGE_COLLECT  = "201";
   //未采集过照片
   public static String RESULT_MESSAGE_NOCOLLECT  = "202";
   
   //生存认证-采集类型 1：退休人员
   public static String COLL_TYPE1="1";//
        
   //生存认证-采集类型 2：非退休人员
   public static String COLL_TYPE2="2";//
   
   public static Map<String,String> STATUSMAP = new HashMap<String , String>();
	
	static{
		STATUSMAP.put("STATUSMAP_01", "01");
		STATUSMAP.put("STATUSMAP_02", "02");
		STATUSMAP.put("STATUSMAP_03", "03");
		STATUSMAP.put("STATUSMAP_04", "04");
		STATUSMAP.put("STATUSMAP_05", "05");
		STATUSMAP.put("STATUSMAP_06", "06");
		STATUSMAP.put("STATUSMAP_07", "07");
	}
    //第三方登陆
    public static final String INTERFACE_USERLOGIN = "/interfacefunction/user/userLogin";
    //根据用户id，用户方法检测接口用户权限
    public static final String INTERFACE_CHECKUSER = "/interfacefunction/user/checkInterfaceUserRoleFunc";

    public static String USERKEY = "userkey:";
    public static final String CHANNELKEY = "channelkey:";

    public static String USERAREAID = "userAreaid:";
    //根据区域id获取区域信息
    public static final String QUERYDISTINCT_URL = "/core/distinct/queryDistinct";


    //档案查询 机构编号
    public static Map<Object,String> Archive_AAE017 = new HashMap<Object, String>();
    static{
        Archive_AAE017.put(1,"420502");//孝感
        Archive_AAE017.put(2,"42050202");//孝南
        Archive_AAE017.put(3,"42050203");//汉川
        Archive_AAE017.put(4,"42050205");//安陆
        Archive_AAE017.put(5,"42050204");//云梦
        Archive_AAE017.put(6,"42050206");//大悟
        Archive_AAE017.put(7,"42050207");//孝昌
        Archive_AAE017.put(8,"42050201");//应城

    }
}
