package com.tecsun.sisp.iface.client;

/**
 * ClassName: LoginVO
 * Description:
 * @author Zhang Qingjie
 * @since 2015-06-09
 */
public class LoginVO {

    private String userName; // user name
    private String type;  // channel type: NetPortal, TSB (Deshunbao), SelfService, WeChat, App
    private String net_password;// login password

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNet_password() {
        return net_password;
    }

    public void setNet_password(String net_password) {
        this.net_password = net_password;
    }
}
