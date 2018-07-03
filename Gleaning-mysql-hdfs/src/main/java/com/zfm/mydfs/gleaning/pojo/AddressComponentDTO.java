package com.zfm.mydfs.gleaning.pojo;

public class AddressComponentDTO {
	private String country;
	private String country_code;
	private String country_code_iso;
	private String country_code_iso2;
	private String province;
	private String city;
	private String city_level;
	private String district;
	private String town;
	private String adcode;
	private String street;
	private String street_number;
	private String direction;
	private String distance;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getCountry_code_iso() {
		return country_code_iso;
	}
	public void setCountry_code_iso(String country_code_iso) {
		this.country_code_iso = country_code_iso;
	}
	public String getCountry_code_iso2() {
		return country_code_iso2;
	}
	public void setCountry_code_iso2(String country_code_iso2) {
		this.country_code_iso2 = country_code_iso2;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity_level() {
		return city_level;
	}
	public void setCity_level(String city_level) {
		this.city_level = city_level;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getAdcode() {
		return adcode;
	}
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreet_number() {
		return street_number;
	}
	public void setStreet_number(String street_number) {
		this.street_number = street_number;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public AddressComponentDTO(String country, String country_code, String country_code_iso, String country_code_iso2,
			String province, String city, String city_level, String district, String town, String adcode, String street,
			String street_number, String direction, String distance) {
		super();
		this.country = country;
		this.country_code = country_code;
		this.country_code_iso = country_code_iso;
		this.country_code_iso2 = country_code_iso2;
		this.province = province;
		this.city = city;
		this.city_level = city_level;
		this.district = district;
		this.town = town;
		this.adcode = adcode;
		this.street = street;
		this.street_number = street_number;
		this.direction = direction;
		this.distance = distance;
	}
	public AddressComponentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "AddressComponent [country=" + country + ", country_code=" + country_code + ", country_code_iso="
				+ country_code_iso + ", country_code_iso2=" + country_code_iso2 + ", province=" + province + ", city="
				+ city + ", city_level=" + city_level + ", district=" + district + ", town=" + town + ", adcode="
				+ adcode + ", street=" + street + ", street_number=" + street_number + ", direction=" + direction
				+ ", distance=" + distance + "]";
	}
	
}
