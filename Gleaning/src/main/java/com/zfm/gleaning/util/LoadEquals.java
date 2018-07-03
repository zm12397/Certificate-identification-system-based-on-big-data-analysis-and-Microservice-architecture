package com.zfm.gleaning.util;

import java.util.ArrayList;
import java.util.List;

public class LoadEquals {
	private List<String> services = new ArrayList<>();
	private int n = 0;
	
	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}
	
	public LoadEquals(List<String> services) {
		super();
		this.services = services;
	}

	public String choose(){
		String service = services.get(n);
		n ++;
		if(n >= services.size()){
			n = 0;
		}
		return service;
	}
}
