package com.tecsun.sisp.npm.bean;

import java.sql.Timestamp;


/**
 * 网办后台公告实体
 *
 */
public class PolicyVO {
    public static final String ID = "id";
    private long id;

    public static final String TITLE = "title";
    private String title;

    public static final String CONTEXT = "context";
    private String context;
    
    public static final String CREATE_TIME = "create_time";
    private Timestamp createTime;

    public static final String FILENO= "fileno";
    private String fileNo;
    
    public static final String TYPE = "type"; 
    private String type;

    public static final String PUBLISH_DATE = "publish_date"; 
    private String publishDate;

    public static final String DISTRICT = "district"; 
    private String district;
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

}
