package com.zfm.gleaning.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.RegisterService;
import com.zfm.gleaning.service.UserService;

/**
 * 个人中心相关功能controller
 * 
 * @author zm
 *
 */
@RestController
@RequestMapping("/center")
public class PersonalCenterController {
	@Autowired
	private UserService userService;

	/**
	 * 
	 * 功能：初始化个人中心
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/init.action", method = RequestMethod.GET)
	public NormalResultDTO init() {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		Map resultMap = new HashMap();
		UserInfoDO user = (UserInfoDO) SecurityUtils.getSubject().getPrincipal(); // 获得当前执行的subject
		Integer id = user.getId();
		try {
			resultMap = userService.init(id);
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(resultMap);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 
	 * 功能：充值/提现
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/recharge.action", method = RequestMethod.POST)
	public NormalResultDTO recharge(Double money, Short type) {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		UserInfoDO user = (UserInfoDO) SecurityUtils.getSubject().getPrincipal(); // 获得当前执行的subject
		Integer id = user.getId();
		try {
			if (type == 0) {
				userService.recharge(id, money);
			} else {
				userService.withdrawals(id, money);
			}
			result.setCode("0000");
			result.setMessage("操作成功");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
}
