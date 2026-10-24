package com.tecsun.sisp.iface.server.entity.requestBean;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title
 * @2018年12月28日-下午2:31:34
 * @version2018
 */
@Setter
@Getter
@ToString
//生物识别出入参数
public class PersonParam {
	
	
	private String aac002;//社会保障号码
	private String deviceid;//设备物理ID
	private String features1;//认证特征值
	private String remark1;//认证备注
	private String authen;//认证图像
	private String cbdid;//	办理业务所在地
	private String rzyw;//认证业务
	private Date createtime;//创建时间
	

}
