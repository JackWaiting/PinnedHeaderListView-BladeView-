package com.hpsvse.entity;

public class City {
	String name;
	String province;
	public City() {
		super();
	}
	public City(String name, String province) {
		super();
		this.name = name;
		this.province = province;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}

}
