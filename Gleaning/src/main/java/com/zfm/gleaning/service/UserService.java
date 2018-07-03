package com.zfm.gleaning.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.zfm.gleaning.pojo.UserInfoDO;

public interface UserService {

	Map init(Integer id);
	
	UserInfoDO getUser(Integer id);
	
	UserInfoDO getUser(String username);

	void recharge(Integer id, Double money);

	void withdrawals(Integer id, Double money);

	Page<UserInfoDO> listUsers(Integer page, Integer cols);


	void updateUser(UserInfoDO user);

	void removeUser(Integer id);

	void generateValidationEmail(String username);
}
