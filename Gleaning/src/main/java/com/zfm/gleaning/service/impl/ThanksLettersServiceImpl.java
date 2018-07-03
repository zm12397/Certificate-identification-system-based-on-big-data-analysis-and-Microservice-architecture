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
import com.zfm.gleaning.dao.SendBackDao;
import com.zfm.gleaning.dao.ThanksLettersDao;
import com.zfm.gleaning.dao.UserInfoDao;
import com.zfm.gleaning.pojo.SendBackDO;
import com.zfm.gleaning.pojo.ThanksLettersDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.ThanksLettersService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@MyTargetDataSource(value = MyDataSourceType.SLAVE)
public class ThanksLettersServiceImpl implements  ThanksLettersService{
	@Autowired
	private ThanksLettersDao thanksLettersDao;
	@Autowired
	private SendBackDao sendBackDao;
	
	private final String content = "真心感谢：$，帮我找回了证件，十分感谢！";

	@Override
	public Page<ThanksLettersDO> listNewLetters(Integer news) {
		// TODO Auto-generated method stub
		Page<ThanksLettersDO> thanks = null;
		Sort sort = new Sort(Direction.DESC,"gmtCreate");
		Pageable pageable = new PageRequest(0,news,sort);
		try{
			thanks = thanksLettersDao.findAll(pageable);
		}catch(Exception e){
			log.error("数据库分页查询失败：" + e.getMessage());
			throw new CustomerException("数据库分页查询失败");
		}
		return thanks;
	}

	@Override
	public Page<ThanksLettersDO> listPageLetters(Integer page, Integer rows) {
		// TODO Auto-generated method stub
		Page<ThanksLettersDO> thanks = null;
		Sort sort = new Sort(Direction.DESC,"gmtCreate");
		Pageable pageable = new PageRequest(page,rows,sort);
		try{
			thanks = thanksLettersDao.findAll(pageable);
		}catch(Exception e){
			log.error("数据库分页查询失败：" + e.getMessage());
			throw new CustomerException("数据库分页查询失败");
		}
		return thanks;
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void addThanksLetters(Integer id) {
		// TODO Auto-generated method stub
		ThanksLettersDO thanksLetters = new ThanksLettersDO();
		SendBackDO sendBack = null;
		try{
			sendBack = sendBackDao.findOne(id);
		}catch(Exception e){
			log.error("招领查询失败：" + e.getMessage());
			throw new CustomerException("招领查询失败");
		}
		UserInfoDO lost = sendBack.getLost();
		UserInfoDO pick = sendBack.getPick();
		thanksLetters.setSender(lost);
		thanksLetters.setReceiver(pick);
		thanksLetters.setState((short) 1);
		thanksLetters.setContent(content.replace("$", pick.getName()));
		try{
			thanksLettersDao.saveAndFlush(thanksLetters);
		}catch(Exception e){
			log.error("添加感谢信失败：" + e.getMessage());
			throw new CustomerException("添加感谢信失败");
		}
	}
	
	
	
}
