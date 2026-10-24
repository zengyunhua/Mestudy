package com.tecsun.sisp.iface.common.vo.xgcard;

import java.util.Date;

/**
* @version 
* 创建时间：2017年8月8日 下午4:30:18
* 说明：孝感补换卡系统申领数据bean
*/

public class XgApplyBean {
	

	    //个人基本信息
	    private Long id;
	    private String name;
	    private String  namePinyin;//姓名全拼
	    private String  certIssuingOrg;//证件签发机关
	    private String  nation;  //民族 参考 sys_param 表的参数组： PERSON_NATION
	    private String  isForeigner;//是否是外国籍人员 00: 否 ; 01:是
	    private String  otherAddress;//其他地址
	    private String sex;
	    private String birthday;
	    private String guoji;//国籍
	    private String certType;//证件类型
	    private String certNum;//证件号码
	    private String certValidity;//证件有效期
	    private String personType;//人员类别
	    private String personStatus;//人员状态
	    private String accountProties;//户口性质
	    private String companyName;//单位名称
	    private String companyNo;//单位编号
	    private String domicile;//户口地址
	    private String address;//常住地址
	    private String phone;//固定电话
	    private String mobile;//手机
	    private String zipCode;//常驻地邮政编码
	    private String TsbAddress;//申领人通讯地址(德生宝所在地地址)
	    private String photoUrl;//相片路径
	    private String  email;                //电子邮箱
	    private String  qq;                   //QQ
	    private String  occupation;           //职业(参考 sys_param 表的参数名称: person_occupation )
	    private String  culturalLevel;       //文化程度(参考 sys_param 表的参数组: person_cultural_level )
	    private String  workingState;        //工作状态(参考 sys_param 表的参数组: JOBSTATUS_TYPE )
	    private Integer  preferredBank;       //首选银行(参考 sys_param 表中参数组: PERSON_PREFERRED_BANK )
	    private Integer  alternativeBank;     //备选银行(参考 sys_param 表中参数组: PERSON_ALTERNATIVE_BANK )
	    private String  status;               //状态 （0:未参保或者已参保,1:申领业务中,2:待领卡,3:已领卡,4:已挂失,5:补卡中,6:换卡中,7:注销）
	    private String  medicareAccount;     //医保账号
	    private String  financeAccount;      //金融账号
	    private String  soPersonNum;        //个人参保号
	    private String  soNum;               //社会保障号
	    private String  hasDuplicateSoNum; //是否多参保号 取值 1:是 0:否
	    private Integer cardId;              //社保卡 物理表 主键
	    private Integer groupInfoId;        //所属 集体表的主键
	    private String  companyAddr;         //单位地址
	    private String  isEnabled;           //是否禁用(1:有效0:禁用)
	    private String addDate;             //创建时间
	    private String addUserId;          //创建操作员
	    private Date    modDate;             //最后更新时间
	    private Integer modUserId;          //更新操作员
	    private String  remark;               //备注
	    private String applyChannel;//申领渠道

	    //申领业务
	    private Long apId;
	    private Long personId;
	    private String agentRelation;
	    private String agentName;
	    private String agentSex;
	    private String agentCertNo;
	    private String agentCertType;
	    private String agentPhone;
	    private Date applyTime;
	    private String applyType;
	    private String applyFormCode;
	    private String netType;
	    private Integer netId;
	    private String operatorId;
	    private String makeCardType;
	    private String issuingCardType;
	    private String mailingAddress;
	    private String mailingCost;
	    private String apStatus;
	    private String errorMsg;
	    private String cardIssueTime;
	    private Integer cardIssueOperatorId;
	    private Date updateTime;
	    private String apRemark;
	    private String certPhoto;
	    private String processDataType;
	    private String busDataType;
	    private Integer exBusId;
	    private Integer bankId;
	    private Integer apCardId;
	    private String bankOpenType;
	    private Integer ispackage;
	    private Integer iswritedata;
	    private String cardIssueNetType;
	    private Integer cardIssueNetId;
	    private Integer bankNetId;
	    private String channel;
	    private String signPhoto;
	    private String signName;
	    private String base64String;
	    private String photoName;
	    private  String devcode;
	    private String isSuccessPhoto;//照片是否合格
	  

	    public String getDevcode() {
	        return devcode;
	    }

	    public void setDevcode(String devcode) {
	        this.devcode = devcode;
	    }

	    //历史记录
	    private Integer hisId;
	    private Integer hisPersonId;
	    private String busType;
	    private Long busId;
	    private String busContent;
	    private Date busStartTime;
	    private Date busEndTime;
	    private String busStatus;
	    private Date hisAddDate;
	    private String hisOperatorId;
	    private String hisRemark;

	    
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getNamePinyin() {
	        return namePinyin;
	    }

	    public void setNamePinyin(String namePinyin) {
	        this.namePinyin = namePinyin;
	    }

	    public String getCertIssuingOrg() {
	        return certIssuingOrg;
	    }

	    public void setCertIssuingOrg(String certIssuingOrg) {
	        this.certIssuingOrg = certIssuingOrg;
	    }

	    public String getNation() {
	        return nation;
	    }

	    public void setNation(String nation) {
	        this.nation = nation;
	    }

	    public String getIsForeigner() {
	        return isForeigner;
	    }

	    public void setIsForeigner(String isForeigner) {
	        this.isForeigner = isForeigner;
	    }

	    public String getOtherAddress() {
	        return otherAddress;
	    }

	    public void setOtherAddress(String otherAddress) {
	        this.otherAddress = otherAddress;
	    }

	    public String getSex() {
	        return sex;
	    }

	    public void setSex(String sex) {
	        this.sex = sex;
	    }

	    public String getBirthday() {
	        return birthday;
	    }

	    public void setBirthday(String birthday) {
	        this.birthday = birthday;
	    }

	    public String getGuoji() {
	        return guoji;
	    }

	    public void setGuoji(String guoji) {
	        this.guoji = guoji;
	    }

	    public String getCertType() {
	        return certType;
	    }

	    public void setCertType(String certType) {
	        this.certType = certType;
	    }

	    public String getCertNum() {
	        return certNum;
	    }

	    public void setCertNum(String certNum) {
	        this.certNum = certNum;
	    }

	    public String getCertValidity() {
	        return certValidity;
	    }

	    public void setCertValidity(String certValidity) {
	        this.certValidity = certValidity;
	    }

	    public String getPersonType() {
	        return personType;
	    }

	    public void setPersonType(String personType) {
	        this.personType = personType;
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

	    public String getDomicile() {
	        return domicile;
	    }

	    public void setDomicile(String domicile) {
	        this.domicile = domicile;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public String getMobile() {
	        return mobile;
	    }

	    public void setMobile(String mobile) {
	        this.mobile = mobile;
	    }

	    public String getZipCode() {
	        return zipCode;
	    }

	    public void setZipCode(String zipCode) {
	        this.zipCode = zipCode;
	    }

	    public String getTsbAddress() {
	        return TsbAddress;
	    }

	    public void setTsbAddress(String tsbAddress) {
	        TsbAddress = tsbAddress;
	    }

	    public String getPhotoUrl() {
	        return photoUrl;
	    }

	    public void setPhotoUrl(String photoUrl) {
	        this.photoUrl = photoUrl;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getQq() {
	        return qq;
	    }

	    public void setQq(String qq) {
	        this.qq = qq;
	    }

	    public String getOccupation() {
	        return occupation;
	    }

	    public void setOccupation(String occupation) {
	        this.occupation = occupation;
	    }

	    public String getCulturalLevel() {
	        return culturalLevel;
	    }

	    public void setCulturalLevel(String culturalLevel) {
	        this.culturalLevel = culturalLevel;
	    }

	    public String getWorkingState() {
	        return workingState;
	    }

	    public void setWorkingState(String workingState) {
	        this.workingState = workingState;
	    }

	    public Integer getPreferredBank() {
	        return preferredBank;
	    }

	    public void setPreferredBank(Integer preferredBank) {
	        this.preferredBank = preferredBank;
	    }

	    public Integer getAlternativeBank() {
	        return alternativeBank;
	    }

	    public void setAlternativeBank(Integer alternativeBank) {
	        this.alternativeBank = alternativeBank;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public String getMedicareAccount() {
	        return medicareAccount;
	    }

	    public void setMedicareAccount(String medicareAccount) {
	        this.medicareAccount = medicareAccount;
	    }

	    public String getFinanceAccount() {
	        return financeAccount;
	    }

	    public void setFinanceAccount(String financeAccount) {
	        this.financeAccount = financeAccount;
	    }

	    public String getSoPersonNum() {
	        return soPersonNum;
	    }

	    public void setSoPersonNum(String soPersonNum) {
	        this.soPersonNum = soPersonNum;
	    }

	    public String getSoNum() {
	        return soNum;
	    }

	    public void setSoNum(String soNum) {
	        this.soNum = soNum;
	    }

	    public String getHasDuplicateSoNum() {
	        return hasDuplicateSoNum;
	    }

	    public void setHasDuplicateSoNum(String hasDuplicateSoNum) {
	        this.hasDuplicateSoNum = hasDuplicateSoNum;
	    }

	    public Integer getCardId() {
	        return cardId;
	    }

	    public void setCardId(Integer cardId) {
	        this.cardId = cardId;
	    }

	    public Integer getGroupInfoId() {
	        return groupInfoId;
	    }

	    public void setGroupInfoId(Integer groupInfoId) {
	        this.groupInfoId = groupInfoId;
	    }

	    public String getCompanyAddr() {
	        return companyAddr;
	    }

	    public void setCompanyAddr(String companyAddr) {
	        this.companyAddr = companyAddr;
	    }

	    public String getIsEnabled() {
	        return isEnabled;
	    }

	    public void setIsEnabled(String isEnabled) {
	        this.isEnabled = isEnabled;
	    }

	    public String getAddDate() {
			return addDate;
		}

		public void setAddDate(String addDate) {
			this.addDate = addDate;
		}

		public String getAddUserId() {
	        return addUserId;
	    }

	    public void setAddUserId(String addUserId) {
	        this.addUserId = addUserId;
	    }

	    public Date getModDate() {
	        return modDate;
	    }

	    public void setModDate(Date modDate) {
	        this.modDate = modDate;
	    }

	    public Integer getModUserId() {
	        return modUserId;
	    }

	    public void setModUserId(Integer modUserId) {
	        this.modUserId = modUserId;
	    }

	    public String getRemark() {
	        return remark;
	    }

	    public void setRemark(String remark) {
	        this.remark = remark;
	    }

	    public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getApId() {
			return apId;
		}

		public void setApId(Long apId) {
			this.apId = apId;
		}

		public Long getPersonId() {
			return personId;
		}

		public void setPersonId(Long personId) {
			this.personId = personId;
		}

		public String getAgentRelation() {
	        return agentRelation;
	    }

	    public void setAgentRelation(String agentRelation) {
	        this.agentRelation = agentRelation;
	    }

	    public String getAgentName() {
	        return agentName;
	    }

	    public void setAgentName(String agentName) {
	        this.agentName = agentName;
	    }

	    public String getAgentSex() {
	        return agentSex;
	    }

	    public void setAgentSex(String agentSex) {
	        this.agentSex = agentSex;
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

	    public Date getApplyTime() {
	        return applyTime;
	    }

	    public void setApplyTime(Date applyTime) {
	        this.applyTime = applyTime;
	    }

	    public String getApplyType() {
	        return applyType;
	    }

	    public void setApplyType(String applyType) {
	        this.applyType = applyType;
	    }

	    public String getApplyFormCode() {
	        return applyFormCode;
	    }

	    public void setApplyFormCode(String applyFormCode) {
	        this.applyFormCode = applyFormCode;
	    }

	    public String getNetType() {
	        return netType;
	    }

	    public void setNetType(String netType) {
	        this.netType = netType;
	    }

	    public Integer getNetId() {
	        return netId;
	    }

	    public void setNetId(Integer netId) {
	        this.netId = netId;
	    }

	    public String getOperatorId() {
	        return operatorId;
	    }

	    public void setOperatorId(String operatorId) {
	        this.operatorId = operatorId;
	    }

	    public String getMakeCardType() {
	        return makeCardType;
	    }

	    public void setMakeCardType(String makeCardType) {
	        this.makeCardType = makeCardType;
	    }

	    public String getIssuingCardType() {
	        return issuingCardType;
	    }

	    public void setIssuingCardType(String issuingCardType) {
	        this.issuingCardType = issuingCardType;
	    }

	    public String getMailingAddress() {
	        return mailingAddress;
	    }

	    public void setMailingAddress(String mailingAddress) {
	        this.mailingAddress = mailingAddress;
	    }

	    public String getMailingCost() {
	        return mailingCost;
	    }

	    public void setMailingCost(String mailingCost) {
	        this.mailingCost = mailingCost;
	    }

	    public String getApStatus() {
	        return apStatus;
	    }

	    public void setApStatus(String apStatus) {
	        this.apStatus = apStatus;
	    }

	    public String getErrorMsg() {
	        return errorMsg;
	    }

	    public void setErrorMsg(String errorMsg) {
	        this.errorMsg = errorMsg;
	    }

	    public String getCardIssueTime() {
	        return cardIssueTime;
	    }

	    public void setCardIssueTime(String cardIssueTime) {
	        this.cardIssueTime = cardIssueTime;
	    }

	    public Integer getCardIssueOperatorId() {
	        return cardIssueOperatorId;
	    }

	    public void setCardIssueOperatorId(Integer cardIssueOperatorId) {
	        this.cardIssueOperatorId = cardIssueOperatorId;
	    }

	    public Date getUpdateTime() {
	        return updateTime;
	    }

	    public void setUpdateTime(Date updateTime) {
	        this.updateTime = updateTime;
	    }

	    public String getApRemark() {
	        return apRemark;
	    }

	    public void setApRemark(String apRemark) {
	        this.apRemark = apRemark;
	    }

	    public String getCertPhoto() {
	        return certPhoto;
	    }

	    public void setCertPhoto(String certPhoto) {
	        this.certPhoto = certPhoto;
	    }

	    public String getProcessDataType() {
	        return processDataType;
	    }

	    public void setProcessDataType(String processDataType) {
	        this.processDataType = processDataType;
	    }

	    public String getBusDataType() {
	        return busDataType;
	    }

	    public void setBusDataType(String busDataType) {
	        this.busDataType = busDataType;
	    }

	    public Integer getExBusId() {
	        return exBusId;
	    }

	    public void setExBusId(Integer exBusId) {
	        this.exBusId = exBusId;
	    }

	    public Integer getBankId() {
	        return bankId;
	    }

	    public void setBankId(Integer bankId) {
	        this.bankId = bankId;
	    }

	    public Integer getApCardId() {
	        return apCardId;
	    }

	    public void setApCardId(Integer apCardId) {
	        this.apCardId = apCardId;
	    }

	    public String getBankOpenType() {
	        return bankOpenType;
	    }

	    public void setBankOpenType(String bankOpenType) {
	        this.bankOpenType = bankOpenType;
	    }

	    public Integer getIspackage() {
	        return ispackage;
	    }

	    public void setIspackage(Integer ispackage) {
	        this.ispackage = ispackage;
	    }

	    public Integer getIswritedata() {
	        return iswritedata;
	    }

	    public void setIswritedata(Integer iswritedata) {
	        this.iswritedata = iswritedata;
	    }

	    public String getCardIssueNetType() {
	        return cardIssueNetType;
	    }

	    public void setCardIssueNetType(String cardIssueNetType) {
	        this.cardIssueNetType = cardIssueNetType;
	    }

	    public Integer getCardIssueNetId() {
	        return cardIssueNetId;
	    }

	    public void setCardIssueNetId(Integer cardIssueNetId) {
	        this.cardIssueNetId = cardIssueNetId;
	    }

	    public Integer getBankNetId() {
	        return bankNetId;
	    }

	    public void setBankNetId(Integer bankNetId) {
	        this.bankNetId = bankNetId;
	    }

	    public String getChannel() {
	        return channel;
	    }

	    public void setChannel(String channel) {
	        this.channel = channel;
	    }

	    public String getSignPhoto() {
	        return signPhoto;
	    }

	    public void setSignPhoto(String signPhoto) {
	        this.signPhoto = signPhoto;
	    }

	    public String getPhotoName() {
	        return photoName;
	    }

	    public void setPhotoName(String photoName) {
	        this.photoName = photoName;
	    }

	    public String getBase64String() {
	        return base64String;
	    }

	    public void setBase64String(String base64String) {
	        this.base64String = base64String;
	    }

	    public Integer getHisId() {
	        return hisId;
	    }

	    public void setHisId(Integer hisId) {
	        this.hisId = hisId;
	    }

	    public Integer getHisPersonId() {
	        return hisPersonId;
	    }

	    public void setHisPersonId(Integer hisPersonId) {
	        this.hisPersonId = hisPersonId;
	    }

	    public String getBusType() {
	        return busType;
	    }

	    public void setBusType(String busType) {
	        this.busType = busType;
	    }

	    

	    public Long getBusId() {
			return busId;
		}

		public void setBusId(Long busId) {
			this.busId = busId;
		}

		public String getBusContent() {
	        return busContent;
	    }

	    public void setBusContent(String busContent) {
	        this.busContent = busContent;
	    }

	    public Date getBusStartTime() {
	        return busStartTime;
	    }

	    public void setBusStartTime(Date busStartTime) {
	        this.busStartTime = busStartTime;
	    }

	    public Date getBusEndTime() {
	        return busEndTime;
	    }

	    public void setBusEndTime(Date busEndTime) {
	        this.busEndTime = busEndTime;
	    }

	    public String getBusStatus() {
	        return busStatus;
	    }

	    public void setBusStatus(String busStatus) {
	        this.busStatus = busStatus;
	    }

	    public Date getHisAddDate() {
	        return hisAddDate;
	    }

	    public void setHisAddDate(Date hisAddDate) {
	        this.hisAddDate = hisAddDate;
	    }

	    public String getHisOperatorId() {
	        return hisOperatorId;
	    }

	    public void setHisOperatorId(String hisOperatorId) {
	        this.hisOperatorId = hisOperatorId;
	    }

	    public String getHisRemark() {
	        return hisRemark;
	    }

	    public void setHisRemark(String hisRemark) {
	        this.hisRemark = hisRemark;
	    }

	    public String getSignName() {
	        return signName;
	    }

	    public void setSignName(String signName) {
	        this.signName = signName;
	    }

	    public String getCompanyName() {
	        return companyName;
	    }

	    public void setCompanyName(String companyName) {
	        this.companyName = companyName;
	    }

	    public String getCompanyNo() {
	        return companyNo;
	    }

	    public void setCompanyNo(String companyNo) {
	        this.companyNo = companyNo;
	    }

		public String getApplyChannel() {
			return applyChannel;
		}

		public void setApplyChannel(String applyChannel) {
			this.applyChannel = applyChannel;
		}

		public String getIsSuccessPhoto() {
			return isSuccessPhoto;
		}

		public void setIsSuccessPhoto(String isSuccessPhoto) {
			this.isSuccessPhoto = isSuccessPhoto;
		}
	    

    
    

}
