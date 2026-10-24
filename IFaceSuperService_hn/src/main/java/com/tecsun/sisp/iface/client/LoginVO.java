package com.tecsun.sisp.iface.client;

/**
 * ClassName: LoginVO
 * Description:
 * CreateTime： 2015年06月09日 19时:47分
 */
public class LoginVO {

    private String userName; //用户名
    private String type;  //渠道类型：网办 NetPortal，德生宝 TSB，自助终端 SelfService,微信 WeChat,手机app App
    private String net_password;// 登录密码

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
