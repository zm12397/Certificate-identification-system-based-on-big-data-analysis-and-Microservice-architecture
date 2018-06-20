package com.zfm.gleaning.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.GoodPersonService;

/**
 * 好人榜相关功能controller
 * @author zm
 *
 */
@RestController
@RequestMapping("/good")
public class GoodPersonController {
	@Autowired
	private GoodPersonService goodPersonService;
	
	
	/**
	 * 功能：获取好人榜排行
	 * 
	 * @param top
	 * @return
	 */
	@RequestMapping(value = "/top.action", method = RequestMethod.GET)
	public NormalResultDTO top(Integer top) {
		NormalResultDTO result = new NormalResultDTO("9999","unknown error",null);
		Page<UserInfoDO> users;
		try{
			users = goodPersonService.listTopGood(top);
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(users);
		}catch(CustomerException e){
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
	/**
	 *  功能：获取当前用户的好人榜排名
	 * 
	 * @param top
	 * @return
	 */
	@RequestMapping(value = "/getMy.action", method = RequestMethod.GET)
	public NormalResultDTO get() {
		NormalResultDTO result = new NormalResultDTO("9999","unknown error",null);
		Integer rank = 0;
		UserInfoDO user = (UserInfoDO)SecurityUtils.getSubject().getPrincipal();	//获得当前执行的subject
		Integer id = user.getId();
		try{
			rank = goodPersonService.getRank(id);
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(rank);
		}catch(CustomerException e){
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
}
