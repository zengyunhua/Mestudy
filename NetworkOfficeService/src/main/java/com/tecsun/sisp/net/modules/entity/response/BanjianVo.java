package com.tecsun.sisp.net.modules.entity.response;

import com.tecsun.sisp.net.modules.entity.BaseVO;

import java.util.Date;

/**
 * Row of a draft together with the item it belongs to and the office details.
 *
 * <p><strong>Reconstructed.</strong> The original source of this class was not
 * included in the published snapshot, which left
 * {@code DraftsDAO.findAllDraftsMessage} and its three callers without a return
 * type. The field set below is derived from the evidence that is in this
 * repository, not guessed freely:</p>
 *
 * <ul>
 *   <li>{@code mappings/draftsDAO.xml} runs
 *       {@code select t.*, m.*, o.* from t_drafts t left join T_ITEM_MESSAGE m
 *       on t.business_id = m.sxbm left join t_office_detailed o
 *       on t.office_id = o.o_id} with {@code resultType} - that is, MyBatis
 *       auto-mapping by column name, so every property here must be named
 *       after a column of one of those three tables.</li>
 *   <li>The sibling query {@code queryByOfficeStatus} selects the same three
 *       tables into {@link DraftsVo}, so that class supplies the property names
 *       and types. Its auditing columns are omitted here because this query
 *       does not join {@code t_auditing}.</li>
 *   <li>The remaining columns ({@code state}, {@code tcqcode}, {@code tcqname},
 *       {@code unitcode}, {@code unitname}, {@code sxbm}, {@code sxlx},
 *       {@code blbm}, {@code blsj}, {@code bltj}, {@code sfsf}, {@code sxsm},
 *       {@code bslc}) are proven to exist by other statements in the same
 *       mapper, so {@code t.*} returns them.</li>
 * </ul>
 *
 * <p>Residual risk: a column that exists in the schema but is never referenced
 * by any SQL in this repository would be absent here, and MyBatis would drop it
 * silently. Compare against your own {@code t_drafts} / {@code T_ITEM_MESSAGE} /
 * {@code t_office_detailed} DDL before relying on the JSON payload.</p>
 */
public class BanjianVo extends BaseVO {

	private long mid;
	private String oid;
	private String tid;
	private String xm;
	private String sfzh;
	private String officeId;
	private long businessId;
	private String matterName;
	private Date applytime;
	private Date updatetime;
	private String remarks;
	private String name;
	private String submitter;
	private String sfcode;
	private String sfname;
	private String dycode;
	private String dyname;
	private String sxmc;
	private String address;
	private String phone;
	private String pstatus;
	private String worktime;
	private String insuredplace;
	private String insuredunit;
	private String ylbxtime;
	private String ybtime;
	private String deyltime;
	private String filename;
	private String insuredaddress;
	private String insuredcode;
	private String state;
	private String tcqcode;
	private String tcqname;
	private String unitcode;
	private String unitname;
	private String sxbm;
	private String sxlx;
	private String blbm;
	private String blsj;
	private String bltj;
	private String sfsf;
	private String sxsm;
	private String bslc;

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getXm() {
		return xm;
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

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public String getMatterName() {
		return matterName;
	}

	public void setMatterName(String matterName) {
		this.matterName = matterName;
	}

	public Date getApplytime() {
		return applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getSfcode() {
		return sfcode;
	}

	public void setSfcode(String sfcode) {
		this.sfcode = sfcode;
	}

	public String getSfname() {
		return sfname;
	}

	public void setSfname(String sfname) {
		this.sfname = sfname;
	}

	public String getDycode() {
		return dycode;
	}

	public void setDycode(String dycode) {
		this.dycode = dycode;
	}

	public String getDyname() {
		return dyname;
	}

	public void setDyname(String dyname) {
		this.dyname = dyname;
	}

	public String getSxmc() {
		return sxmc;
	}

	public void setSxmc(String sxmc) {
		this.sxmc = sxmc;
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

	public String getPstatus() {
		return pstatus;
	}

	public void setPstatus(String pstatus) {
		this.pstatus = pstatus;
	}

	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

	public String getInsuredplace() {
		return insuredplace;
	}

	public void setInsuredplace(String insuredplace) {
		this.insuredplace = insuredplace;
	}

	public String getInsuredunit() {
		return insuredunit;
	}

	public void setInsuredunit(String insuredunit) {
		this.insuredunit = insuredunit;
	}

	public String getYlbxtime() {
		return ylbxtime;
	}

	public void setYlbxtime(String ylbxtime) {
		this.ylbxtime = ylbxtime;
	}

	public String getYbtime() {
		return ybtime;
	}

	public void setYbtime(String ybtime) {
		this.ybtime = ybtime;
	}

	public String getDeyltime() {
		return deyltime;
	}

	public void setDeyltime(String deyltime) {
		this.deyltime = deyltime;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getInsuredaddress() {
		return insuredaddress;
	}

	public void setInsuredaddress(String insuredaddress) {
		this.insuredaddress = insuredaddress;
	}

	public String getInsuredcode() {
		return insuredcode;
	}

	public void setInsuredcode(String insuredcode) {
		this.insuredcode = insuredcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTcqcode() {
		return tcqcode;
	}

	public void setTcqcode(String tcqcode) {
		this.tcqcode = tcqcode;
	}

	public String getTcqname() {
		return tcqname;
	}

	public void setTcqname(String tcqname) {
		this.tcqname = tcqname;
	}

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getSxbm() {
		return sxbm;
	}

	public void setSxbm(String sxbm) {
		this.sxbm = sxbm;
	}

	public String getSxlx() {
		return sxlx;
	}

	public void setSxlx(String sxlx) {
		this.sxlx = sxlx;
	}

	public String getBlbm() {
		return blbm;
	}

	public void setBlbm(String blbm) {
		this.blbm = blbm;
	}

	public String getBlsj() {
		return blsj;
	}

	public void setBlsj(String blsj) {
		this.blsj = blsj;
	}

	public String getBltj() {
		return bltj;
	}

	public void setBltj(String bltj) {
		this.bltj = bltj;
	}

	public String getSfsf() {
		return sfsf;
	}

	public void setSfsf(String sfsf) {
		this.sfsf = sfsf;
	}

	public String getSxsm() {
		return sxsm;
	}

	public void setSxsm(String sxsm) {
		this.sxsm = sxsm;
	}

	public String getBslc() {
		return bslc;
	}

	public void setBslc(String bslc) {
		this.bslc = bslc;
	}

}
