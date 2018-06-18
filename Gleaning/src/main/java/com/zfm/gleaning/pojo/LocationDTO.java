package com.zfm.gleaning.pojo;

public class LocationDTO {
	private String lng;
	private String lat;
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	@Override
	public String toString() {
		return lat + "," + lng;
	}
	public LocationDTO(String lng, String lat) {
		super();
		this.lng = lng;
		this.lat = lat;
	}
	public LocationDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
