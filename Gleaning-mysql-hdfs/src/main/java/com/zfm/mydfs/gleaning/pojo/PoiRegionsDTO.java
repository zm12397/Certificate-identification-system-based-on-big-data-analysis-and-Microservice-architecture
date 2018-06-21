package com.zfm.mydfs.gleaning.pojo;

public class PoiRegionsDTO {
	private String direction_desc;
	private String name;
	private String tag;
	private String uid;
	public String getDirection_desc() {
		return direction_desc;
	}
	public void setDirection_desc(String direction_desc) {
		this.direction_desc = direction_desc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "PoiRegionsDTO [direction_desc=" + direction_desc + ", name=" + name + ", tag=" + tag + ", uid=" + uid
				+ "]";
	}
	public PoiRegionsDTO(String direction_desc, String name, String tag, String uid) {
		super();
		this.direction_desc = direction_desc;
		this.name = name;
		this.tag = tag;
		this.uid = uid;
	}
	public PoiRegionsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
