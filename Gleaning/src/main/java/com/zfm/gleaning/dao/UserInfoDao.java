package com.zfm.gleaning.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zfm.gleaning.pojo.UserInfoDO;

public interface UserInfoDao  extends BaseDao<UserInfoDO,Integer>{
	List<UserInfoDO> findByUsername(String username);
	List<UserInfoDO> findBySumTimeGreaterThan(Integer sumTime);
}
