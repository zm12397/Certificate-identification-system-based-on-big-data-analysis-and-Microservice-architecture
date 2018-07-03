package com.zfm.gleaning.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zfm.gleaning.pojo.UserInfoDO;

public interface GoodPersonService {
	Page<UserInfoDO> listTopGood(Integer top);

	Integer getRank(Integer id);
}
