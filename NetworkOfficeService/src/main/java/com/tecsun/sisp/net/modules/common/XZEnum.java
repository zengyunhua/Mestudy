package com.tecsun.sisp.net.modules.common;

/**
 * 险种类型枚
 *
 */
public enum XZEnum {

	xz11("11","企业基本养老保险"),
	xz12("12","机关事业养老保险"),
	xz14("14","企事业年金"),
	xz15("15","乡镇事业养老保险"),
	xz21("21","失业保险"),
	xz31("31","基本医疗保险"),
	xz32("32","大额医疗保险"),
	xz33("33","公务员补助"),
	xz34("34","补充医疗保险"),
	xz35("35","离休医疗保险"),
	xz36("36","伤残军人医疗保险"),
	xz37("37","住院统筹保险"),
	xz41("41","工伤保险"),
	xz42("42","建筑工伤保险"),
	xz51("51","生育保险"),
	xz61("61","城镇居民医疗保险");
	
	
	private String key;
	private String name;
	
	XZEnum(String key,String name){
		this.key = key;
		this.name= name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
