package com.tecsun.sisp.iface.common.vo;

/**
 * 短信请求bean
 * 
 * @ClassName:
 *
 */
public class SmsRequstBean {

	
	private String deviceid;//设备编码
	private String GHF;// 挂号费
	private String regdate;//挂号日期；
	private String newPhoneNo;//手机号码
	private String DEPARTMENTMC;//科室名称
	private String DOCTORMC;//专家名称；
	private String HOSPITALMC;//医院名称；
    private String channelcode;// 接口类型型(如：大众端、网办、德生宝)
	private String channelType;//接口类型(如：大众端-02、德生宝-01)
	private String REGTIME;// AM/PM
	private String name;
	private String type;//1为预约挂号，2为取消挂号,3为发送验证码；
	private String random;//验证码
    private String cernum;//身份证
    private String token;//token

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCernum() {
        return cernum;
    }

    public void setCernum(String cernum) {
        this.cernum = cernum;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGHF() {
		return GHF;
	}

	public void setGHF(String gHF) {
		GHF = gHF;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getDEPARTMENTMC() {
		return DEPARTMENTMC;
	}

	public void setDEPARTMENTMC(String dEPARTMENTMC) {
		DEPARTMENTMC = dEPARTMENTMC;
	}

	public String getDOCTORMC() {
		return DOCTORMC;
	}

	public void setDOCTORMC(String dOCTORMC) {
		DOCTORMC = dOCTORMC;
	}

	public String getHOSPITALMC() {
		return HOSPITALMC;
	}

	public void setHOSPITALMC(String hOSPITALMC) {
		HOSPITALMC = hOSPITALMC;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getREGTIME() {
		if(REGTIME.equals("AM")){
			REGTIME ="上午";
		}else if(REGTIME.equals("PM")){
			REGTIME ="下午";
		}else{
			REGTIME = REGTIME;
		}
		return REGTIME;
	}

	public void setREGTIME(String rEGTIME) {
		REGTIME = rEGTIME;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	
	public String getNewPhoneNo() {
		return newPhoneNo;
	}

	public void setNewPhoneNo(String newPhoneNo) {
		this.newPhoneNo = newPhoneNo;
	}
}
