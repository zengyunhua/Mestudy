package com.tecsun.sisp.iface.common.vo.employment.param.job;

import java.util.Date;

/**
 * @date 2016年12月19日 上午11:42:15
 */
public class AddJobSeekerInfoVo  extends JobBasicVo{

	private String zjlxid;// 证件类型id(字典表的id)
	private String zjhm;// 证件号码、需编码
	private String dlid;// 登录id，需编码(不区分大小写)第一个字符必须为字母
	private String dlmm;// 登录密码（md5加密后转为大写）
	private String cym;// 曾用名、需编码
	private String grzplj;// 个人照片路径
	private String xczplj;// 现场照片路径
	private String sjhm;// 手机号码、需编码
	private String dzyx;// 电子邮箱、需编码
	private String xzz;// 现住址、需编码
	private String hkxzid;// 户口性质id(字典表的id)
	private String xldm;// 学历代码（第二字典表的代码）
	private String slid;// 视力
	private String jkzkid;// 健康状况id(字典表的id)
	private String sg;// 身高
	private String hyzkid;// 婚姻状况id(字典表的id)
	private String zzmmid;// 政治面貌id(字典表的id)
	private String sfdszn;// 是否独生子女1：是2：否
	private String sfrnh;// 是否二女户1：是2：否
	private String sbh;// 社保号、需编码
	private String byyx;// 毕业院校、需编码
	private String byrq;// 毕业日期
	private String gzdw;// 工作单位、需编码
	private String sfmfzyjs;// 是否享受免费职业介绍1：是2：否
	private String sfmfzypx;// 是否享受免费职业培训1：是2：否
	private String jysyh;// 就业失业号、需编码
	private String dqbh;// 地区编号（第二字典表的代码）
	private String zydm;// 专业代码（第二字典表的代码）
	private String jndjdm;// 技能等级代码（第二字典表的代码）
	private String gzdm;// 工种代码（第二字典表的代码）
	private String csnxid;// 从事年限id(字典表的id)
	private String grjj;// 个人简介、需编码
	private String sfjstsdx;// 接受自动发送的招聘会/招聘岗位推送短信
	private String qq;// qq号码
	private String qzzt;// 求职状态
	private String fzid;// 分组id
	private String sblx;// 设备类型(3:自助一体机)
	private String czyid;// 操作员id

	//刷身份证添加身份证信息
	private String sfzh;// 身份证号、需编码
	private String xm;// 姓名、需编码
	private String xbid;// 性别1：男2：女3：未说明性别
	private String mz;// 民族、需编码
	private String mzdm;// 民族代码
	private String csny;// 出生年月yyyy-mm-dd
	private String jtzz;// 家庭住址、需编码
	private String zxzz;// 最新住址、需编码
	private String qfjg;// 签发机关、需编码
	private String qssj;// 起始时间yyyy-mm-dd
	private String jssj;// 结束时间yyyy-mm-dd
	private String sfzzplj;// 身份证照片路径
	
	//上传照片
	private String picBase64Str;// 照片的base64字符串
	private String qzzid;// 求职者id
    private long id;//主键
    private Date createTime;//创建时间
    private Date updateTime;//修改时间

    public String getQzzid() {
        return qzzid;
    }

    public void setQzzid(String qzzid) {
        this.qzzid = qzzid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getZjlxid() {
		return zjlxid;
	}

	public void setZjlxid(String zjlxid) {
		this.zjlxid = zjlxid;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}

	public String getDlid() {
		return dlid;
	}

	public void setDlid(String dlid) {
		this.dlid = dlid;
	}

	public String getDlmm() {
		return dlmm;
	}

	public void setDlmm(String dlmm) {
		this.dlmm = dlmm;
	}

	public String getCym() {
		return cym;
	}

	public void setCym(String cym) {
		this.cym = cym;
	}

	public String getGrzplj() {
		return grzplj;
	}

	public void setGrzplj(String grzplj) {
		this.grzplj = grzplj;
	}

	public String getXczplj() {
		return xczplj;
	}

	public void setXczplj(String xczplj) {
		this.xczplj = xczplj;
	}

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}

	public String getDzyx() {
		return dzyx;
	}

	public void setDzyx(String dzyx) {
		this.dzyx = dzyx;
	}

	public String getXzz() {
		return xzz;
	}

	public void setXzz(String xzz) {
		this.xzz = xzz;
	}

	public String getHkxzid() {
		return hkxzid;
	}

	public void setHkxzid(String hkxzid) {
		this.hkxzid = hkxzid;
	}

	public String getXldm() {
		return xldm;
	}

	public void setXldm(String xldm) {
		this.xldm = xldm;
	}

	public String getSlid() {
		return slid;
	}

	public void setSlid(String slid) {
		this.slid = slid;
	}

	public String getJkzkid() {
		return jkzkid;
	}

	public void setJkzkid(String jkzkid) {
		this.jkzkid = jkzkid;
	}

	public String getSg() {
		return sg;
	}

	public void setSg(String sg) {
		this.sg = sg;
	}

	public String getHyzkid() {
		return hyzkid;
	}

	public void setHyzkid(String hyzkid) {
		this.hyzkid = hyzkid;
	}

	public String getZzmmid() {
		return zzmmid;
	}

	public void setZzmmid(String zzmmid) {
		this.zzmmid = zzmmid;
	}

	public String getSfdszn() {
		return sfdszn;
	}

	public void setSfdszn(String sfdszn) {
		this.sfdszn = sfdszn;
	}

	public String getSfrnh() {
		return sfrnh;
	}

	public void setSfrnh(String sfrnh) {
		this.sfrnh = sfrnh;
	}

	public String getSbh() {
		return sbh;
	}

	public void setSbh(String sbh) {
		this.sbh = sbh;
	}

	public String getByyx() {
		return byyx;
	}

	public void setByyx(String byyx) {
		this.byyx = byyx;
	}

	public String getByrq() {
		return byrq;
	}

	public void setByrq(String byrq) {
		this.byrq = byrq;
	}

	public String getGzdw() {
		return gzdw;
	}

	public void setGzdw(String gzdw) {
		this.gzdw = gzdw;
	}

	public String getSfmfzyjs() {
		return sfmfzyjs;
	}

	public void setSfmfzyjs(String sfmfzyjs) {
		this.sfmfzyjs = sfmfzyjs;
	}

	public String getSfmfzypx() {
		return sfmfzypx;
	}

	public void setSfmfzypx(String sfmfzypx) {
		this.sfmfzypx = sfmfzypx;
	}

	public String getJysyh() {
		return jysyh;
	}

	public void setJysyh(String jysyh) {
		this.jysyh = jysyh;
	}

	public String getDqbh() {
		return dqbh;
	}

	public void setDqbh(String dqbh) {
		this.dqbh = dqbh;
	}

	public String getZydm() {
		return zydm;
	}

	public void setZydm(String zydm) {
		this.zydm = zydm;
	}

	public String getJndjdm() {
		return jndjdm;
	}

	public void setJndjdm(String jndjdm) {
		this.jndjdm = jndjdm;
	}

	public String getGzdm() {
		return gzdm;
	}

	public void setGzdm(String gzdm) {
		this.gzdm = gzdm;
	}

	public String getCsnxid() {
		return csnxid;
	}

	public void setCsnxid(String csnxid) {
		this.csnxid = csnxid;
	}

	public String getGrjj() {
		return grjj;
	}

	public void setGrjj(String grjj) {
		this.grjj = grjj;
	}

	public String getSfjstsdx() {
		return sfjstsdx;
	}

	public void setSfjstsdx(String sfjstsdx) {
		this.sfjstsdx = sfjstsdx;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getQzzt() {
		return qzzt;
	}

	public void setQzzt(String qzzt) {
		this.qzzt = qzzt;
	}

	public String getFzid() {
		return fzid;
	}

	public void setFzid(String fzid) {
		this.fzid = fzid;
	}

	public String getSblx() {
		return sblx;
	}

	public void setSblx(String sblx) {
		this.sblx = sblx;
	}

	public String getCzyid() {
		return czyid;
	}

	public void setCzyid(String czyid) {
		this.czyid = czyid;
	}

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

	public String getXbid() {
		return xbid;
	}

	public void setXbid(String xbid) {
		this.xbid = xbid;
	}

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getMzdm() {
		return mzdm;
	}

	public void setMzdm(String mzdm) {
		this.mzdm = mzdm;
	}

	public String getCsny() {
		return csny;
	}

	public void setCsny(String csny) {
		this.csny = csny;
	}

	public String getJtzz() {
		return jtzz;
	}

	public void setJtzz(String jtzz) {
		this.jtzz = jtzz;
	}

	public String getZxzz() {
		return zxzz;
	}

	public void setZxzz(String zxzz) {
		this.zxzz = zxzz;
	}

	public String getQfjg() {
		return qfjg;
	}

	public void setQfjg(String qfjg) {
		this.qfjg = qfjg;
	}

	public String getQssj() {
		return qssj;
	}

	public void setQssj(String qssj) {
		this.qssj = qssj;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getSfzzplj() {
		return sfzzplj;
	}

	public void setSfzzplj(String sfzzplj) {
		this.sfzzplj = sfzzplj;
	}

	public String getPicBase64Str() {
		return picBase64Str;
	}

	public void setPicBase64Str(String picBase64Str) {
		this.picBase64Str = picBase64Str;
	}

}
