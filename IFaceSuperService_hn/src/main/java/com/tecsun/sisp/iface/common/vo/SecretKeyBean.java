package com.tecsun.sisp.iface.common.vo;

/**工作秘钥
 * ClassName: SecretKeyBean
 * Description:
 * CreateTime： 2015年06月18日 11时:44分
 */
public class SecretKeyBean {

    private String devId;//设备编号
    private String bank_code;//设备对应银行编码

    private String pik;//pin号
    private String mak;//Mac地址
    private String tdk;//磁道秘钥
    private String patchNo;//批次号
    private String seq;//序列号，一个批次中唯一的序列号
    private String zdh;//终端号
    private String shh;//商户号
    private String isupdatewk;//是否需要更新秘钥,1 是
    public String bodyHexStr;//请求银行返回完整的报文，提供给德生宝

    public String getBodyHexStr() {
        return bodyHexStr;
    }

    public void setBodyHexStr(String bodyHexStr) {
        this.bodyHexStr = bodyHexStr;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getIsupdatewk() {
        return isupdatewk;
    }

    public void setIsupdatewk(String isupdatewk) {
        this.isupdatewk = isupdatewk;
    }

    public String getPik() {
        return pik;
    }

    public void setPik(String pik) {
        this.pik = pik;
    }

    public String getMak() {
        return mak;
    }

    public void setMak(String mak) {
        this.mak = mak;
    }

    public String getTdk() {
        return tdk;
    }

    public void setTdk(String tdk) {
        this.tdk = tdk;
    }

    public String getPatchNo() {
        return patchNo;
    }

    public void setPatchNo(String patchNo) {
        this.patchNo = patchNo;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getZdh() {
        return zdh;
    }

    public void setZdh(String zdh) {
        this.zdh = zdh;
    }

    public String getShh() {
        return shh;
    }

    public void setShh(String shh) {
        this.shh = shh;
    }
}
