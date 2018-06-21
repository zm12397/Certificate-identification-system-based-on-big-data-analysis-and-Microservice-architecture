package com.zfm.mydfs.gleaning.pojo;

import java.util.List;

public class ResultDTO {
	private LocationDTO location;
	private String formatted_address;
	private String business;
	private AddressComponentDTO addressComponent;
	private List<String> pois;
	private List<String> roads;
	private List<PoiRegionsDTO> poiRegions;
	private String sematic_description;
	private String cityCode;
	public LocationDTO getLocation() {
		return location;
	}
	public void setLocation(LocationDTO location) {
		this.location = location;
	}
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public AddressComponentDTO getAddressComponent() {
		return addressComponent;
	}
	public void setAddressComponent(AddressComponentDTO addressComponent) {
		this.addressComponent = addressComponent;
	}
	public List<String> getPois() {
		return pois;
	}
	public void setPois(List<String> pois) {
		this.pois = pois;
	}
	public List<String> getRoads() {
		return roads;
	}
	public void setRoads(List<String> roads) {
		this.roads = roads;
	}
	public List<PoiRegionsDTO> getPoiRegions() {
		return poiRegions;
	}
	public void setPoiRegions(List<PoiRegionsDTO> poiRegions) {
		this.poiRegions = poiRegions;
	}
	public String getSematic_description() {
		return sematic_description;
	}
	public void setSematic_description(String sematic_description) {
		this.sematic_description = sematic_description;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	@Override
	public String toString() {
		return "ResultDTO [location=" + location + ", formatted_address=" + formatted_address + ", business=" + business
				+ ", addressComponent=" + addressComponent + ", pois=" + pois + ", roads=" + roads + ", poiRegions="
				+ poiRegions + ", sematic_description=" + sematic_description + ", cityCode=" + cityCode + "]";
	}
	public ResultDTO(LocationDTO location, String formatted_address, String business,
			AddressComponentDTO addressComponent, List<String> pois, List<String> roads, List<PoiRegionsDTO> poiRegions,
			String sematic_description, String cityCode) {
		super();
		this.location = location;
		this.formatted_address = formatted_address;
		this.business = business;
		this.addressComponent = addressComponent;
		this.pois = pois;
		this.roads = roads;
		this.poiRegions = poiRegions;
		this.sematic_description = sematic_description;
		this.cityCode = cityCode;
	}
	public ResultDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
