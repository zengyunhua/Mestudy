package com.tecsun.sisp.iface.server.entity.card;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PerPayDetalVO {
	/**
	 * 个人历年缴费明细信息返回字段VO
	 *
	 */

	private String dwbh;//单位编号
	private String dwmc;//(50)	单位名称
	private String qsny;//起始年月，YYYYMM
	private String zzny;//	终止年月，YYYYMM
	private String jfrq;//	缴费日期，YYYYMMDD
	private String jfzt;//	缴费状态，0未缴，1已缴
	private String grjfjs;//	缴费基数
	private String dwzjfje;//	单位总缴费金额
	private String grzjfje;//	单位总缴费金额
	private String zjze;//	划账金额
	public String getDwbh() {
		return dwbh;
	}
	public void setDwbh(String dwbh) {
		this.dwbh = dwbh;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getQsny() {
		return qsny;
	}
	public void setQsny(String qsny) {
		this.qsny = qsny;
	}
	public String getZzny() {
		return zzny;
	}
	public void setZzny(String zzny) {
		this.zzny = zzny;
	}
	public String getJfrq() {
		return jfrq;
	}
	public void setJfrq(String jfrq) {
		if(jfrq!=null && !"".equals(jfrq)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
			try {
				jfrq = sdf1.format(sdf.parse(jfrq));
			} catch (ParseException e) {
				e.printStackTrace();
			}  
			this.jfrq = jfrq;
		}
		this.jfrq = jfrq;
	}
	public String getJfzt() {
		return jfzt;
	}
	public void setJfzt(String jfzt) {
		this.jfzt = jfzt;
	}
	public String getGrjfjs() {
		return grjfjs;
	}
	public void setGrjfjs(String grjfjs) {
		this.grjfjs = grjfjs;
	}
	public String getDwzjfje() {
		return dwzjfje;
	}
	public void setDwzjfje(String dwzjfje) {
		this.dwzjfje = dwzjfje;
	}
	public String getGrzjfje() {
		return grzjfje;
	}
	public void setGrzjfje(String grzjfje) {
		this.grzjfje = grzjfje;
	}
	public String getZjze() {
		return zjze;
	}
	public void setZjze(String zjze) {
		this.zjze = zjze;
	}

}
