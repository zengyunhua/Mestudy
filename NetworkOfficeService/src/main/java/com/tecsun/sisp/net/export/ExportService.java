package com.tecsun.sisp.net.export;

import com.tecsun.sisp.net.common.Page;

/**
 * 导出接口
 *
 * @param <T>
 */
public interface ExportService<T> {
	
	//取数据
	public Page<T> getPageList(T obj,Page<T> pages);
	
}
