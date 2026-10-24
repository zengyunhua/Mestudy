package com.tecsun.sisp.iface.common.vo;

/**
 * Created by Administrator on 2018/8/30 0030.
 */
public class PersonInfo {
//    <id>43025744</id>
//    <personNumber>43025744</personNumber>
//    <name>涂坚喜</name>
//    <idType>1</idType>
//    <idNumber>110101199001010015</idNumber>
//    <idSocialensurenumber>110101199001010015</idSocialensurenumber>
//    <idCountry>中国</idCountry>
//    <profileStatus>1</profileStatus>
//    <areaCode>420999</areaCode>
    private String  id;       //
    private long  personNumber;       //
    private String  name;       //
    private String  idType;       //
    private String  idNumber;       //
    private String  idSocialensurenumber;       //
    private String  idCountry;       //
    private String  profileStatus;       //
    private String  areaCode;       //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(long personNumber) {
        this.personNumber = personNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdSocialensurenumber() {
        return idSocialensurenumber;
    }

    public void setIdSocialensurenumber(String idSocialensurenumber) {
        this.idSocialensurenumber = idSocialensurenumber;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
