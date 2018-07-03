package com.zfm.gleaning.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zfm.gleaning.pojo.SendBackDO;

public interface SendBackService {

	List<SendBackDO> listSendBacks(Integer id);
	
	void addSendBack(SendBackDO sendBack);

	Page<SendBackDO> listSendBacks(Integer page, Integer rows);

	Page<SendBackDO> listSendBacks(Integer page, Integer rows, String name, String number);

	boolean validate(Integer id, String name, String number);

	void claim(Integer id, Integer userId, String postAddr, String postName, String postTel, Double money);

	List<SendBackDO> listClaims(Integer id);

	SendBackDO getSendBack(Integer id);

	void complete(Integer id);

	void incomplete(Integer id);

	Page<SendBackDO> listSendBacks(Integer page, Integer rows, Short type,String schoolName,String name,String number);

	void updateSendBack(SendBackDO sendBack);

	void updateSendBack(Integer id, String number);
}
