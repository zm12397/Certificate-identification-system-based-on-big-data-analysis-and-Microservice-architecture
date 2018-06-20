package com.zfm.gleaning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.FileResourcesDO;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.FileService;
import com.zfm.gleaning.service.RegisterService;

/**
 * 注册相关功能controller
 * @author zm
 *
 */
@RestController
@RequestMapping("/register")
public class RegisterController {
	@Autowired
	private RegisterService registerService;
	@Autowired
	private FileService fileService;
	/**
	 *  功能：验证用户名，若存在提示用户名已经存在
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/validate.action", method = RequestMethod.POST)
	public NormalResultDTO validate(String username) {
		
		NormalResultDTO result = new NormalResultDTO("9999","unknown error",null);
		boolean isUsed;
		try{
			isUsed = registerService.validateUsername(username);
			result.setCode("0000");
		}catch(CustomerException e){
			result.setMessage(e.getMessage());
			return result;
		}
		if (isUsed) {
			result.setMessage("用户名可以使用");
			return result;
		} else {
			result.setMessage("用户名已经存在");
		}
		return result;
	}
	/**
	 *  功能：注册新用户
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/register.action", method = RequestMethod.POST)
	public NormalResultDTO register(UserInfoDO user,Integer img) {
		NormalResultDTO result = new NormalResultDTO("9999","unknown error",null);
		if(img != null){
			FileResourcesDO file = null;
			try{
				file = fileService.getFileResources(img);
			}catch(CustomerException e){
				result.setMessage(e.getMessage());
				return result;
			}
			if(file != null){
				user.setImage(file);
			}
		}
		
		user.setAccount(100.0);
		try{
			registerService.addUser(user);
			result.setCode("0000");
			result.setMessage("注册成功");
		}catch(CustomerException e){
			result.setMessage("注册失败");
			return result;
		}
		return result;
	}
}
