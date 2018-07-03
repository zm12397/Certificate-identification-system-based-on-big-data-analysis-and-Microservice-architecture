package com.zfm.gleaning.service;

import com.zfm.gleaning.pojo.UserInfoDO;

public interface RegisterService {
	boolean validateUsername(String username);

	void addUser(UserInfoDO user);
}
