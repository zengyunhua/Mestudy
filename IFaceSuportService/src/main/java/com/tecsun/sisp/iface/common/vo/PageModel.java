package com.tecsun.sisp.iface.common.vo;

import java.util.List;

/**
 *  内存分页
* @ClassName: PageModel 
* @date 2015年6月25日 下午12:09:05 
* 
 */
public class PageModel<T> {
    private int page = 1; // 当前页

    public int totalPages = 0; // 总页数

    private int pageRecorders;// 每页12条数据

    private int totalRows = 0; // 总数据数

    private int pageStartRow = 0;// 每页的起始数

    private int pageEndRow = 0; // 每页显示数据的终止数

    private boolean hasNextPage = false; // 是否有下一页

    private boolean hasPreviousPage = false; // 是否有前一页

    private List<T> data;

    // private Iterator it;

    public PageModel() {}
    
    public PageModel(List<T> list, int pageRecorders) {
    	if(pageRecorders!=0){
    		init(list, pageRecorders);// 通过对象集，记录总数划分
    	}
    }
    
    /**
     * 初始化list，并告之该list每页的记录数
     * @param list
     * @param pageRecorders
     */
    public void init(List<T> list, int pageRecorders) {
    	if(pageRecorders!=0){
    		this.pageRecorders = pageRecorders;
            this.data = list;
            totalRows = list.size();
            // it = list.iterator();
            hasPreviousPage = false;
            if ((totalRows % pageRecorders) == 0) {
                totalPages = totalRows / pageRecorders;
            } else {
                totalPages = totalRows / pageRecorders + 1;
            }

            if (page >= totalPages) {
                hasNextPage = false;
            } else {
                hasNextPage = true;
            }

            if (totalRows < pageRecorders) {
                this.pageStartRow = 0;
                this.pageEndRow = totalRows;
            } else {
                this.pageStartRow = 0;
                this.pageEndRow = pageRecorders;
            }
    	}
        
    }

    // 判断要不要分页
    public boolean isNext() {
        return data.size() > pageRecorders;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public String toString(int temp) {
        String str = Integer.toString(temp);
        return str;
    }

    public void description() {

        String description = "共有数据数:" + this.getTotalRows() +

        "共有页数: " + this.getTotalPages() + 

        "当前页数为:" + this.getPage() +

        " 是否有前一页: " + this.isHasPreviousPage() +

        " 是否有下一页:" + this.isHasNextPage() +

        " 开始行数:" + this.getPageStartRow() +

        " 终止行数:" + this.getPageEndRow();

        System.out.println(description);
    }

    public List<T> getNextPage() {
        page = page + 1;

        disposePage();

        System.out.println("用户凋用的是第" + page + "页");
        this.description();
        return getObjects(page);
    }

    /**
     * 处理分页
     */
    private void disposePage() {

        if (page == 0) {
            page = 1;
        }

        if ((page - 1) > 0) {
            hasPreviousPage = true;
        } else {
            hasPreviousPage = false;
        }

        if (page >= totalPages) {
            hasNextPage = false;
        } else {
            hasNextPage = true;
        }
    }

    public List<T> getPreviousPage() {

        page = page - 1;

        if ((page - 1) > 0) {
            hasPreviousPage = true;
        } else {
            hasPreviousPage = false;
        }
        if (page >= totalPages) {
            hasNextPage = false;
        } else {
            hasNextPage = true;
        }
        this.description();
        return getObjects(page);
    }

    /**
     * 获取第几页的内容
     * 
     * @param page
     * @return
     */
    public List<T> getObjects(int page) {
    	 List<T> objects = null;
    	if(page!=0){
    		if (page == 0)
                this.setPage(1);
            else
                this.setPage(page);
            this.disposePage();
            if (page * pageRecorders < totalRows) {// 判断是否为最后一页
                pageEndRow = page * pageRecorders;
                pageStartRow = pageEndRow - pageRecorders;
            } else {
                pageEndRow = totalRows;
                pageStartRow = pageRecorders * (totalPages - 1);
            }

           
            if (!data.isEmpty()) {
                objects = data.subList(pageStartRow, pageEndRow);
            }
            //this.description();
            
    	}
    	return objects;
       
    }

    public List<T> getFistPage() {
        if (this.isNext()) {
            return data.subList(0, pageRecorders);
        } else {
            return data;
        }
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageEndRow() {
        return pageEndRow;
    }

    public void setPageEndRow(int pageEndRow) {
        this.pageEndRow = pageEndRow;
    }

    public int getPageRecorders() {
        return pageRecorders;
    }

    public void setPageRecorders(int pageRecorders) {
        this.pageRecorders = pageRecorders;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }

    public void setPageStartRow(int pageStartRow) {
        this.pageStartRow = pageStartRow;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

}
