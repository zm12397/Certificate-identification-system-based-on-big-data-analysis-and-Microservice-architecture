package com.zfm.gleaning.pojo;

import java.util.List;

public class PlacesDTO {
	private List<String> lines;
	private long count;
	public List<String> getLines() {
		return lines;
	}
	public void setLines(List<String> lines) {
		this.lines = lines;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public PlacesDTO(List<String> lines, long count) {
		super();
		this.lines = lines;
		this.count = count;
	}
	public PlacesDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "PlacesDTO [lines=" + lines + ", count=" + count + "]";
	}
	
}
