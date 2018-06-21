package com.zfm.mydfs.gleaning.pojo;

public class JWResultDTO {
	private LocationDTO location;
	private String precise;
	private String confidence;
	private String level;
	public LocationDTO getLocation() {
		return location;
	}
	public void setLocation(LocationDTO location) {
		this.location = location;
	}
	public String getPrecise() {
		return precise;
	}
	public void setPrecise(String precise) {
		this.precise = precise;
	}
	public String getConfidence() {
		return confidence;
	}
	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	@Override
	public String toString() {
		return "JWResultDTO [location=" + location + ", precise=" + precise + ", confidence=" + confidence + ", level="
				+ level + "]";
	}
	public JWResultDTO(LocationDTO location, String precise, String confidence, String level) {
		super();
		this.location = location;
		this.precise = precise;
		this.confidence = confidence;
		this.level = level;
	}
	public JWResultDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
