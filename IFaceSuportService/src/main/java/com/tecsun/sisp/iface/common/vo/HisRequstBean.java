package com.tecsun.sisp.iface.common.vo;

/**
 * 医院门诊请求bean
 * 
 * @ClassName: DepartmentBean
 * @date 2015年6月19日 下午1:08:49
 *
 */
public class HisRequstBean {

	
	 private String deviceid;//设备编码
	 
	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	private String deptNo; // 科室编号

	private String registerTime; // 号表时间

	private String cardType; // 卡类型

	private String YYBH;// 医院编号
	private String CZYBH;// 操作员编号
	private String YYJYLSH;// 医院交易流水号
	private String MZZYLSH;// 门诊住院流水号
	private String KSBH;// 科室编号
	private String DJH;// 单据号
	private String GHF;// 挂号费
	private String YLLB;// 医疗类别
	private String YYSFXMNM;// 医院收费项目内码
	private String SFXMZXBM;// 收费项目中心编
	private String YYSFXMMC;// 医院收费项目名称
	private String KMM;// 卡密码
	
	
	private String cardNo; // 卡号， 与卡类型对应
	private String cernum;//身份证号码
	private String name;//姓名
	private String id; //业务id 
	private String accounttype; //支付帐户类型；
	private String status;//状态；
	private String regdate;//挂号日期；
	private String hosid;//医院编号；
	private String JZH;//就诊号；
	private String SXH;// 顺序号

	public String getSXH() {
		return SXH;
	}

	public void setSXH(String sXH) {
		SXH = sXH;
	}

	public String getJZH() {
		return JZH;
	}

	public void setJZH(String jZH) {
		JZH = jZH;
	}

	public String getHosid() {
		return hosid;
	}

	public void setHosid(String hosid) {
		this.hosid = hosid;
	}

	private String newPhoneNo;//手机号码
	
	public String getNewPhoneNo() {
		return newPhoneNo;
	}

	public void setNewPhoneNo(String newPhoneNo) {
		this.newPhoneNo = newPhoneNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String YLLX; //医疗类型
	

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	private String xmlData; // 终端缴费数据

	private String channelcode;// 接口类型型(如：大众端、网办、德生宝)
	
	private String channelType;//接口类型(如：大众端-02、德生宝-01)
	
	//分页
	private int pagesize;  
	
	private int pageno;

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getYYBH() {
		return YYBH;
	}

	public void setYYBH(String YYBH) {
		this.YYBH = YYBH;
	}

	public String getCZYBH() {
		return CZYBH;
	}

	public void setCZYBH(String CZYBH) {
		this.CZYBH = CZYBH;
	}

	public String getYYJYLSH() {
		return YYJYLSH;
	}

	public void setYYJYLSH(String YYJYLSH) {
		this.YYJYLSH = YYJYLSH;
	}

	public String getMZZYLSH() {
		return MZZYLSH;
	}

	public void setMZZYLSH(String MZZYLSH) {
		this.MZZYLSH = MZZYLSH;
	}

	public String getKSBH() {
		return KSBH;
	}

	public void setKSBH(String KSBH) {
		this.KSBH = KSBH;
	}

	public String getDJH() {
		return DJH;
	}

	public void setDJH(String DJH) {
		this.DJH = DJH;
	}

	public String getGHF() {
		return GHF;
	}

	public void setGHF(String GHF) {
		this.GHF = GHF;
	}

	public String getYLLB() {
		return YLLB;
	}

	public void setYLLB(String YLLB) {
		this.YLLB = YLLB;
	}

	public String getYYSFXMNM() {
		return YYSFXMNM;
	}

	public void setYYSFXMNM(String YYSFXMNM) {
		this.YYSFXMNM = YYSFXMNM;
	}

	public String getSFXMZXBM() {
		return SFXMZXBM;
	}

	public void setSFXMZXBM(String SFXMZXBM) {
		this.SFXMZXBM = SFXMZXBM;
	}

	public String getYYSFXMMC() {
		return YYSFXMMC;
	}

	public void setYYSFXMMC(String YYSFXMMC) {
		this.YYSFXMMC = YYSFXMMC;
	}

	public String getKMM() {
		return KMM;
	}

	public void setKMM(String KMM) {
		this.KMM = KMM;
	}

	public String getYLLX() {
		return YLLX;
	}

	public void setYLLX(String YLLX) {
		this.YLLX = YLLX;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getXmlData() {
		return xmlData;
	}

	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getCernum() {
		return cernum;
	}

	public void setCernum(String cernum) {
		this.cernum = cernum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	

	
}
