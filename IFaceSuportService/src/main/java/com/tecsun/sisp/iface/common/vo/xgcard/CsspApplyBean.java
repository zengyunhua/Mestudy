package com.tecsun.sisp.iface.common.vo.xgcard;

import com.tecsun.sisp.iface.common.vo.SecQueryBean;

public class CsspApplyBean extends SecQueryBean {
    private long applyId;//
    private long personId;//
    private String birthday;//出生日期
    private String sex;//性别
    private String namePinyin;//姓名拼音
    private String personType;//人员类别 1：城镇职工 2：城镇居民 3：农村居民 9：其他
    private String nation;//民族
    private String certType;//证件类型 01:身份证（户口簿）;02:军官证;03:警官证;04:香港护照（通行证）;05:澳门护照（通行证）;06:台湾居民通行证;07:外国人永久居留证;08:外国人护照;09:残疾人证;10:军烈属证明;11:外国人就业证;12:外国专家证;13:外国人常驻记者证;14:台港澳人员就业证;15:回国（来华）定居专家证;80:中国护照;90:社会保障卡;99:其他身份证件;
    private String guoji;//国籍
    private String isForeigner;//是否是外国人 1-是，0否
    private String mobile;//手机号码
    private String phone;//固定电话
    private String domicile;//户籍地址
    private String certIssuingOrg;//签发机关
    private String address;//联系地址
    private String tsbAddress;//领卡地址
    private String certValidity;//证件有效期 截止时间yyyyMMdd/长期
    private String bankCode;//银行编码
    private String bankName;//银行名称
    private String personStatus;//人员状态 1:就业 2:退休 3: 离休 4:待业 5:无业 6:从未就业 7:务农 8:退职 9:失业 10:就读 11:外出务工 99:其他
    private String accountProties;//户口性质 11:本市非农业;12:外市非农业;21:本市农业;22:外市农业;30:台港澳人员;40:外籍人士;
    private String zipCode;//邮编
    private String agentCertNo;//代办人证件号码
    private String agentCertType;//代办证件类型(同证件类型)
    private String agentPhone;//代办人联系电话
    private String scannPhoto;//电子申领表
    private String isUpload;//是否已经被上传成功Y 表示上传成功 N  表示尚未上传或失败
    private String uploadtime;//上传时间
    private String photoSource;//01代表相片无需处理（如来自公安库） 02代表相片需（等待）处理（如自拍）
    private long photoBuzId;//参保人相片ID
    private long picupId;//身份证正面id
    private long picdownId;//身份证反面id
    private long signphotoId;//身份证签名id
    private String remark;//备注
    private String createTime;
    private String photo64String;//参保人相片64编码string
    private String sign64String;//电子签名64编码string
    private String idcard64StringUp;//身份证正面64编码string
    private String idcard64StringDown;//身份证反面64编码string
    private String companyNo;//单位编码
    private String companyName;//单位名称
    private long uploadNum;//上传次数
    private String addrType;//领卡方式：1：快递邮寄；2：到网点领取；3：德生宝所在地领取
    private String cardAddress;//领卡地址（邮寄地址、网点地址）
    private String cardAddressShort;//地址简称或网点
    private String addrOrgCode;//网点编码
    private String addrPhone;//收件人手机号
    private String addressee;//收件人姓名
    private long applyAddrId;//领卡地址表ID
    private String distinctId;//所属区县ID
    private String picPath;//参保人照片路径
    private String picStatus;//参保人照片状态
    public long getApplyAddrId() {
        return applyAddrId;
    }

    public void setApplyAddrId(long applyAddrId) {
        this.applyAddrId = applyAddrId;
    }

    public String getAddrType() {
        return addrType;
    }

    public void setAddrType(String addrType) {
        this.addrType = addrType;
    }

    public String getCardAddress() {
        return cardAddress;
    }

    public void setCardAddress(String cardAddress) {
        this.cardAddress = cardAddress;
    }

    public String getCardAddressShort() {
        return cardAddressShort;
    }

    public void setCardAddressShort(String cardAddressShort) {
        this.cardAddressShort = cardAddressShort;
    }

    public String getAddrOrgCode() {
        return addrOrgCode;
    }

    public void setAddrOrgCode(String addrOrgCode) {
        this.addrOrgCode = addrOrgCode;
    }

    public String getAddrPhone() {
        return addrPhone;
    }

    public void setAddrPhone(String addrPhone) {
        this.addrPhone = addrPhone;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public long getApplyId() {
        return applyId;
    }

    public void setApplyId(long applyId) {
        this.applyId = applyId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getGuoji() {
        return guoji;
    }

    public void setGuoji(String guoji) {
        this.guoji = guoji;
    }

    public String getIsForeigner() {
        return isForeigner;
    }

    public void setIsForeigner(String isForeigner) {
        this.isForeigner = isForeigner;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public String getCertIssuingOrg() {
        return certIssuingOrg;
    }

    public void setCertIssuingOrg(String certIssuingOrg) {
        this.certIssuingOrg = certIssuingOrg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTsbAddress() {
        return tsbAddress;
    }

    public void setTsbAddress(String tsbAddress) {
        this.tsbAddress = tsbAddress;
    }

    public String getCertValidity() {
        return certValidity;
    }

    public void setCertValidity(String certValidity) {
        this.certValidity = certValidity;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPersonStatus() {
        return personStatus;
    }

    public void setPersonStatus(String personStatus) {
        this.personStatus = personStatus;
    }

    public String getAccountProties() {
        return accountProties;
    }

    public void setAccountProties(String accountProties) {
        this.accountProties = accountProties;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAgentCertNo() {
        return agentCertNo;
    }

    public void setAgentCertNo(String agentCertNo) {
        this.agentCertNo = agentCertNo;
    }

    public String getAgentCertType() {
        return agentCertType;
    }

    public void setAgentCertType(String agentCertType) {
        this.agentCertType = agentCertType;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }

    public String getScannPhoto() {
        return scannPhoto;
    }

    public void setScannPhoto(String scannPhoto) {
        this.scannPhoto = scannPhoto;
    }

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getPhotoSource() {
        return photoSource;
    }

    public void setPhotoSource(String photoSource) {
        this.photoSource = photoSource;
    }

    public long getPhotoBuzId() {
        return photoBuzId;
    }

    public void setPhotoBuzId(long photoBuzId) {
        this.photoBuzId = photoBuzId;
    }

    public long getPicupId() {
        return picupId;
    }

    public void setPicupId(long picupId) {
        this.picupId = picupId;
    }

    public long getPicdownId() {
        return picdownId;
    }

    public void setPicdownId(long picdownId) {
        this.picdownId = picdownId;
    }

    public long getSignphotoId() {
        return signphotoId;
    }

    public void setSignphotoId(long signphotoId) {
        this.signphotoId = signphotoId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPhoto64String() {
        return photo64String;
    }

    public void setPhoto64String(String photo64String) {
        this.photo64String = photo64String;
    }

    public String getSign64String() {
        return sign64String;
    }

    public void setSign64String(String sign64String) {
        this.sign64String = sign64String;
    }

    public String getIdcard64StringUp() {
        return idcard64StringUp;
    }

    public void setIdcard64StringUp(String idcard64StringUp) {
        this.idcard64StringUp = idcard64StringUp;
    }

    public String getIdcard64StringDown() {
        return idcard64StringDown;
    }

    public void setIdcard64StringDown(String idcard64StringDown) {
        this.idcard64StringDown = idcard64StringDown;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getUploadNum() {
        return uploadNum;
    }

    public void setUploadNum(long uploadNum) {
        this.uploadNum = uploadNum;
    }

	public String getDistinctId() {
		return distinctId;
	}

	public void setDistinctId(String distinctId) {
		this.distinctId = distinctId;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getPicStatus() {
		return picStatus;
	}

	public void setPicStatus(String picStatus) {
		this.picStatus = picStatus;
	}
	
    
}