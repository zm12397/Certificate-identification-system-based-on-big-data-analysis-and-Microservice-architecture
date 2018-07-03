package com.zfm.gleaning.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.MyTargetDataSource;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;
import com.zfm.gleaning.dao.UserInfoDao;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.GoodPersonService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@MyTargetDataSource(value = MyDataSourceType.SLAVE)
public class GoodPersonServiceImpl implements  GoodPersonService{
	@Autowired
	private UserInfoDao userInfoDao;

	@Override
	public Page<UserInfoDO> listTopGood(Integer top) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Direction.DESC,"sumTime");
		Pageable pageable = new PageRequest(0,top,sort);
		Page<UserInfoDO> users = null;
		try{
			users = userInfoDao.findAll(pageable);
		}catch(Exception e){
			log.error("数据库分页查询失败：" + e.getMessage());
			throw new CustomerException("数据库分页查询失败");
		}
		return users;
	}

	@Override
	public Integer getRank(Integer id) {
		// TODO Auto-generated method stub
		UserInfoDO user = null; 
		Integer rank = 0;
		try{
			user = userInfoDao.findOne(id);
			if(user == null){
				log.error("该用户不存在：" + id);
				throw new CustomerException("该用户不存在");
			}
			List<UserInfoDO> users = userInfoDao.findBySumTimeGreaterThan(user.getSumTime());
			rank = users.size() + 1;
		}catch(Exception e){
			log.error("数据库查询失败：" + e.getMessage());
			throw new CustomerException("数据库查询失败");
		}
		return rank;
	}
	
	
	
}
