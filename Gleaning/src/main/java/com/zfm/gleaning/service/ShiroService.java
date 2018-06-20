package com.zfm.gleaning.service;

import java.util.Map;

public interface ShiroService {

	Map<String, String> getInitUrlPermission();
	
	void updatePermission();
}
