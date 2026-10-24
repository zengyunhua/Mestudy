package com.tecsun.sisp.npm.bean;

import java.sql.Timestamp;

import oracle.sql.BLOB;
import bee.cloud.engine.db.Doe;
import bee.cloud.engine.db.annotation.Column;
import bee.cloud.engine.db.annotation.TableName;
import bee.cloud.engine.db.core.Table;

/**
 * 网办后台公告实体
 *
 */
@TableName(name = "t_policy", alias = "policy")
public class PolicyVO extends Table {

    public PolicyVO(Doe doe) {
        super(doe);
        this.createTime = new Timestamp(System.currentTimeMillis());
    }

    public static final String ID = "id";
    @Column(name = ID, pk = true)
    private long id;

    public static final String TITLE = "title";
    @Column(name = TITLE)
    private String title;

    public static final String CONTEXT = "context";
    @Column(name = CONTEXT)
    private String context;
    
    public static final String CREATE_TIME = "create_time";
    @Column(name = CREATE_TIME)
    private Timestamp createTime;

    public static final String FILENO= "fileno";
    @Column(name = FILENO)
    private String fileNo;
    
    public static final String TYPE = "type"; 
    @Column(name = TYPE)
    private String type;

    public static final String PUBLISH_DATE = "publish_date"; 
    @Column(name = PUBLISH_DATE)
    private String publishDate;

    public static final String DISTRICT = "district"; 
    @Column(name = DISTRICT)
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
