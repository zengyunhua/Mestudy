package com.tecsun.sisp.iface.common.vo.card;

/*
@date     2015-7-7
@version
@return
@throws
@ 五险查询VO
*/
public class TsInsuranceVO {
    private 	String	xzlx	;//	险种类型
    private 	String	kx	;//	款项
    private 	String	ssq	;//	所属期
    private 	String	jfjs	;//	缴费基数
    private 	String	sjje	;//	实缴金额
    private 	String	dzrq	;//	到帐日期
    private    String  xm; //姓名
    private    String  sfzh;//身份证号
    private    String  cbzt;//参保状态
    private    double bqyj;//本期应缴
    private    String  dwmc;//单位名称；
    private    String  xb;//性别；
    private    String  grsf;//个人身份
    private    String  ryzt;//人员状态；
    private    String  jfksrq;//缴费开始时间；
    private    String  jfzzrq;//缴费终止时间；
    private    String  mz;//民族
    private    String  cjgzsj;//参加工作日期
    private 	String	ksny;//	开始年月
    private 	String	zzny;//	终止年月
    private 	String	jbsj;//	经办日期
    //权益单打印
    private   double agedPserSum;//养老个人缴费合计；
    private   double agedUnitSum;//养老单位缴费合计；
    
    private  double  medicalPserSum;//医疗个人缴费合计；
    private  double  medicalGovSum;//医疗财政拨付合计；
    private  double  medicalUnitSum;//医疗单位缴费合计；

    private  double  lossPserSum;//失业 个人缴费合计；
    private  double  lossUnitSum;//失业单位缴费合计；
    
    private  double  injuryPserSum;//工伤个人缴费合计；
    private  double  injuryUnitSum;//工伤单位缴费合计；
    
    
    private  double  fertilityPserSum;//生育个人缴费合计；
    private  double  fertilityUnitSum;//生育单位缴费合计；

    public double getMedicalGovSum() {
        return medicalGovSum;
    }

    public void setMedicalGovSum(double medicalGovSum) {
        this.medicalGovSum = medicalGovSum;
    }

    public String getKsny() {
        return ksny;
    }

    public void setKsny(String ksny) {
        this.ksny = ksny;
    }

    public String getZzny() {
        return zzny;
    }

    public void setZzny(String zzny) {
        this.zzny = zzny;
    }

    public String getJbsj() {
        return jbsj;
    }

    public void setJbsj(String jbsj) {
        this.jbsj = jbsj;
    }

    public String getCjgzsj() {
        return cjgzsj;
    }

    public void setCjgzsj(String cjgzsj) {
        this.cjgzsj = cjgzsj;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public double getAgedPserSum() {
		return agedPserSum;
	}
	public void setAgedPserSum(double agedPserSum) {
		this.agedPserSum = agedPserSum;
	}
	public double getAgedUnitSum() {
		return agedUnitSum;
	}
	public void setAgedUnitSum(double agedUnitSum) {
		this.agedUnitSum = agedUnitSum;
	}
	public double getMedicalPserSum() {
		return medicalPserSum;
	}
	public void setMedicalPserSum(double medicalPserSum) {
		this.medicalPserSum = medicalPserSum;
	}
	public double getMedicalUnitSum() {
		return medicalUnitSum;
	}
	public void setMedicalUnitSum(double medicalUnitSum) {
		this.medicalUnitSum = medicalUnitSum;
	}
	public double getLossPserSum() {
		return lossPserSum;
	}
	public void setLossPserSum(double lossPserSum) {
		this.lossPserSum = lossPserSum;
	}
	public double getLossUnitSum() {
		return lossUnitSum;
	}
	public void setLossUnitSum(double lossUnitSum) {
		this.lossUnitSum = lossUnitSum;
	}
	public double getInjuryPserSum() {
		return injuryPserSum;
	}
	public void setInjuryPserSum(double injuryPserSum) {
		this.injuryPserSum = injuryPserSum;
	}
	public double getInjuryUnitSum() {
		return injuryUnitSum;
	}
	public void setInjuryUnitSum(double injuryUnitSum) {
		this.injuryUnitSum = injuryUnitSum;
	}
	public double getFertilityPserSum() {
		return fertilityPserSum;
	}
	public void setFertilityPserSum(double fertilityPserSum) {
		this.fertilityPserSum = fertilityPserSum;
	}
	public double getFertilityUnitSum() {
		return fertilityUnitSum;
	}
	public void setFertilityUnitSum(double fertilityUnitSum) {
		this.fertilityUnitSum = fertilityUnitSum;
	}
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx) {
		this.yblx = yblx;
	}
	private    String cbdq;//参保地区
    private    String jbrq;//经办日期；
    private   double ylye; //医疗余额； 
    
    private String yblx; //医保类型；
    
    
    private   String grbh;//个人编号；
    private   double  czhj;//财政合计；
    
    private String sfny;//城镇职工退休-实付年月
    private String ffjg;//城镇职工退休-发放机构
    private String ffzh;//城镇职工退休-发放帐号
    private double ffje;//城镇职工退休-退休金额
    private double ffjehj;//城镇职工退休-退休金额合计
    
    public String getSfny() {
		return sfny;
	}
	public void setSfny(String sfny) {
		this.sfny = sfny;
	}
	public String getFfjg() {
		return ffjg;
	}
	public void setFfjg(String ffjg) {
		this.ffjg = ffjg;
	}
	public String getFfzh() {
		return ffzh;
	}
	public void setFfzh(String ffzh) {
		this.ffzh = ffzh;
	}
	public double getFfje() {
		return ffje;
	}
	public void setFfje(double ffje) {
		this.ffje = ffje;
	}
	public double getFfjehj() {
		return ffjehj;
	}
	public void setFfjehj(double ffjehj) {
		this.ffjehj = ffjehj;
	}
    
    
   
	public double getCzhj() {
		return czhj;
	}
	public void setCzhj(double czhj) {
		this.czhj = czhj;
	}
	public String getGrbh() {
		return grbh;
	}
	public void setGrbh(String grbh) {
		this.grbh = grbh;
	}
	public double getYlye() {
		return ylye;
	}
	public void setYlye(double ylye) {
		this.ylye = ylye;
	}
	public double getYlje() {
		return ylje;
	}
	public void setYlje(double ylje) {
		this.ylje = ylje;
	}
	public String getYlrq() {
		return ylrq;
	}
	public void setYlrq(String ylrq) {
		this.ylrq = ylrq;
	}
	public String getYllx() {
		return yllx;
	}
	public void setYllx(String yllx) {
		this.yllx = yllx;
	}
	public String getYldd() {
		return yldd;
	}
	public void setYldd(String yldd) {
		this.yldd = yldd;
	}
	private   String dwjfhj;//单位实缴；
    private   String grjfhj;//个人实缴
    private   String jjly;//基金来源
    
    private   double ylje;//医疗消费(划入)金额；
    private   String ylrq;//医疗消费(划入)日期；
    private   String yllx;////医疗消费(划入)类型；
    private  String yldd;////医疗消费(划入)地点；
    
    
    
    
 
	public String getJjly() {
		return jjly;
	}
	public void setJjly(String jjly) {
		this.jjly = jjly;
	}
	public String getDwjfhj() {
		return dwjfhj;
	}
	public void setDwjfhj(String dwjfhj) {
		this.dwjfhj = dwjfhj;
	}
	public String getGrjfhj() {
		return grjfhj;
	}
	public void setGrjfhj(String grjfhj) {
		this.grjfhj = grjfhj;
	}
	public String getJbrq() {
		return jbrq;
	}
	public void setJbrq(String jbrq) {
		this.jbrq = jbrq;
	}
	public String getCbdq() {
		return cbdq;
	}
	public void setCbdq(String cbdq) {
		this.cbdq = cbdq;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	public String getGrsf() {
		return grsf;
	}
	public void setGrsf(String grsf) {
		this.grsf = grsf;
	}
	public String getRyzt() {
		return ryzt;
	}
	public void setRyzt(String ryzt) {
		this.ryzt = ryzt;
	}
	public double getBqyj() {
		return bqyj;
	}
	public void setBqyj(double bqyj) {
		this.bqyj = bqyj;
	}
	private     double  gejfhj;//个人缴费合计
    private     double  wjjfhj;//单位缴费合计
	public double getGejfhj() {
		return gejfhj;
	}
	public void setGejfhj(double gejfhj) {
		this.gejfhj = gejfhj;
	}
	public double getWjjfhj() {
		return wjjfhj;
	}
	public void setWjjfhj(double wjjfhj) {
		this.wjjfhj = wjjfhj;
	}
	public String getCbzt() {
		return cbzt;
	}
	public void setCbzt(String cbzt) {
		this.cbzt = cbzt;
	}
	public String getXm() {
		return xm;
	}
	public String getSjje() {
		return sjje;
	}
	public void setSjje(String sjje) {
		this.sjje = sjje;
	}
	public String getDzrq() {
		return dzrq;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public void setDzrq(String dzrq) {
		this.dzrq = dzrq;
	}
	  public String getXzlx() {
	        return xzlx==null?"":xzlx;
	    }

	    public void setXzlx(String xzlx) {
	        this.xzlx = xzlx;
	    }

	    public String getKx() {
	        return kx==null?"":kx;
	    }

	    public void setKx(String kx) {
	        this.kx = kx;
	    }
	public String getSsq() {
		return ssq;
	}
	public void setSsq(String ssq) {
		this.ssq = ssq;
	}
	public String getJfjs() {
		return jfjs;
	}
	public void setJfjs(String jfjs) {
		this.jfjs = jfjs;
	}

    public String getJfksrq() {
        return jfksrq;
    }

    public void setJfksrq(String jfksrq) {
        this.jfksrq = jfksrq;
    }

    public String getJfzzrq() {
        return jfzzrq;
    }

    public void setJfzzrq(String jfzzrq) {
        this.jfzzrq = jfzzrq;
    }
}
