package com.tecsun.sisp.iface.common.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * A single page of results, used as the payload for list endpoints and as the return type
 * of most client-SDK operations.
 *
 * <p>Deliberately <em>not</em> a {@link Result} subclass: a {@code Page} carries no status
 * code or message. When the client SDK unwraps a response, the envelope is discarded and
 * only the paging fields and rows survive — so a failed query and an empty result are
 * indistinguishable at this level. Callers that need the failure reason must inspect the
 * raw response.
 *
 * <p>Sentinel values:
 * <ul>
 *   <li>{@code pageSize == -1} — do not paginate, return everything.</li>
 *   <li>{@code count == -1} — the total was not requested.</li>
 * </ul>
 *
 * <p>{@link #setPageSize(int)} coerces non-positive values to {@code 10}, so you cannot
 * actually request "no pagination" by passing {@code 0}.
 *
 * @param <T> row type
 */
public class Page<T>{
    private int pageNo; // 当前页码
    private int pageSize; // 页面大小，设置为“-1”表示不进行分页（分页无效）
    private long count;// 总记录数，设置为“-1”表示不查询总数
    //private int first;// 首页索引
   // private int last;// 尾页索引
    private List<T> data = new ArrayList<T>();
//    private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc

    public Page() {

    }

    public Page(int pageNo, int pageSize) {
        this(pageNo, pageSize, 0);
    }

    public Page(int pageNo, int pageSize, long count) {
        this(pageNo, pageSize, count, new ArrayList<T>());
    }

    public Page(int pageNo, int pageSize, long count, List<T> data) {
        this.count = count;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.data = data;
    }

    public void setCount(long count) {
        this.count = count;
        if (pageSize >= count) {
            pageNo = 1;
        }
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? 10 : pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getCount() {
        return count;
    }

//    public int getFirst() {
//        return first;
//    }
//
//    public void setFirst(int first) {
//        this.first = first;
//    }
//
//    public int getLast() {
//        return last;
//    }
//
//    public void setLast(int last) {
//        this.last = last;
//    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

//    public String getOrderBy() {
//        return orderBy;
//    }
//
//    public void setOrderBy(String orderBy) {
//        this.orderBy = orderBy;
//    }

//    public int getFirstResult() {
//        int firstResult = (getPageNo() - 1) * getPageSize();
//        if (firstResult >= getCount()) {
//            firstResult = 0;
//        }
//        return firstResult;
//    }

//    public int getMaxResults() {
//        return getPageSize();
//    }
}
