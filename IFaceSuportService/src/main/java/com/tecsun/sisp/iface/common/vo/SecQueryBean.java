package com.tecsun.sisp.iface.common.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 社保卡请求对象
 * Created by jerry on 2015/5/31.
 */
public class SecQueryBean extends BaseVO{
    /**
     * 1 请求头 start
     */
    private String channelcode;//渠道编码
    private String deviceid;//设备编码
    private String tokenId;//权限验证码
    /**
     * 请求头 end
     */

    /**
     * 2 请求体(包含社保所有业务入参) start
     */
    private String name;//用户姓名
    private String id;//身份证
    private String newPhoneNo;//手机号码
    
    private String cernum;//身份证
    private String cardno;//社保卡号
    
    private String aac003;//姓名
    
    private String sfzh;
    private String xm;

    public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getAac003() {
		return aac003;
	}

	public void setAac003(String aac003) {
		this.aac003 = aac003;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCernum() {
		return cernum;
	}

	public void setCernum(String cernum) {
		this.cernum = cernum;
	}

	/**
     * 提供五险使用的字段 start
     */
    private String aae140;//险种类型；
    
    private String aac001;//个人编号；
    
    private String aac002;//个人身份证号码
    
    /**
     * 提供五险使用的字段 end
     */
    
    public String getAac002() {
		return aac002;
	}

	public void setAac002(String aac002) {
		this.aac002 = aac002;
	}

	/**
     * 请求体(包含社保所有业务入参) end
     */

    public String getAac001() {
		return aac001;
	}

	public void setAac001(String aac001) {
		this.aac001 = aac001;
	}

	/**
     * 3 请求底 start
     */
    private int pagesize;
  

	public String getAae140() {
		return aae140;
	}

	public void setAae140(String aae140) {
		this.aae140 = aae140;
	}

    /**
     * Converts the legacy comma-separated insurance type expression into
     * values that MyBatis can bind safely in an IN clause.
     */
    public List<String> getInsuranceTypes() {
        if (aae140 == null || aae140.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<String> insuranceTypes = new ArrayList<String>();
        for (String value : aae140.split(",")) {
            String normalized = value.trim();
            if (normalized.startsWith("'") && normalized.endsWith("'") && normalized.length() >= 2) {
                normalized = normalized.substring(1, normalized.length() - 1);
            }
            if (!normalized.isEmpty()) {
                insuranceTypes.add(normalized);
            }
        }
        return insuranceTypes;
    }

	private int pageno;

    /**
     * 请求底 end
     */

    private String grbh;

    public SecQueryBean() {
    }

    public String getGrbh() {
        return grbh;
    }

    public void setGrbh(String grbh) {
        this.grbh = grbh;
    }

    public String getChannelcode() {
        return channelcode;
    }

    public void setChannelcode(String channelcode) {
        this.channelcode = channelcode;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
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

    public String getNewPhoneNo() {
        return newPhoneNo;
    }

    public void setNewPhoneNo(String newPhoneNo) {
        this.newPhoneNo = newPhoneNo;
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
