package com.zfm.gleaning.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.FileService;
import com.zfm.gleaning.service.GoodPersonService;
import com.zfm.gleaning.service.UserService;
import com.zfm.gleaning.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录相关功能controller
 * 
 * @author zm
 *
 */
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
	@Autowired
	private UserService userService;
	@Autowired
	private FileService fileService;

	/**
	 * 功能：登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login.action", method = RequestMethod.POST)
	public NormalResultDTO login(String username, String password) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password.toCharArray());
			// usernamePasswordToken.setRememberMe(true);// 记住我
			try {
				SecurityUtils.getSubject().login(usernamePasswordToken);
				result.setCode("0000");
				result.setMessage("登录成功");
			} catch (UnknownAccountException uae) {
				log.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
				result.setMessage("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
			} catch (IncorrectCredentialsException ice) {
				log.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证：" + ice.getMessage());
				result.setMessage("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
			} catch (LockedAccountException lae) {
				log.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
				result.setMessage("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
			} catch (ExcessiveAttemptsException eae) {
				log.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
				result.setMessage("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
			} catch(DisabledAccountException dae){
				log.info("对用户[" + username + "]进行登录验证..验证未通过,该账户不是激活状态");
				result.setMessage("该用户已被注销");
			}catch (AuthenticationException ae) {
				// 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
				log.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
				result.setMessage("用户名或密码不正确");
			}

		} catch (Exception e) {
			log.info("登录异常" + e.getMessage());
			result.setMessage("登录异常");
		}
		return result;
	}

	/**
	 * 功能：获取验证码
	 * 
	 * @return
	 */
	@RequestMapping(value = "/validate.action", method = RequestMethod.GET)
	public NormalResultDTO validate() {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		String filePath = "img/validate/" + TimeUtils.getNow() + ".png";
		String code = null;
		Map map = new HashMap();
		try {
			code = fileService.generateValidationImage(filePath);
			result.setCode("0000");
			result.setMessage("successful");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		map.put("file", filePath);
		map.put("code", code);
		result.setData(map);
		return result;
	}

	/**
	 * 功能：提交忘记密码的用户名
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/forget.action", method = RequestMethod.POST)
	public NormalResultDTO forget(String username) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		UserInfoDO user = null;
		try {
			user = userService.getUser(username);
			result.setCode("0000");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		if (user == null) {
			result.setMessage("用户名不存在");
		} else {
			result.setMessage(user.getEmail());
		}
		return result;
	}

	/**
	 * 功能：提交忘记密码的邮箱，后台修改用户信息并发送验证邮件
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/email.action", method = RequestMethod.POST)
	public NormalResultDTO email(String username) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			userService.generateValidationEmail(username);
			result.setCode("0000");
			result.setMessage("提交成功，验证信息已经发送给账户邮箱，请前往验证邮箱查看（验证邮件只在24小时内有效）");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：校验，判断是否是邮件中发送的校验码
	 * 
	 * @param id
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/code.action", method = RequestMethod.POST)
	public NormalResultDTO code(Integer id, String code) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		UserInfoDO user = null;
		try {
			user = userService.getUser(id);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		if (user == null) {
			result.setMessage("无效用户");
		} else {
			if (user.getVtime() <= TimeUtils.getNow()) {
				result.setMessage("对不起，您的验证码已经失效，请重新找回密码并获取验证码");
			} else {
				if (user.getValidation().equals(code)) {
					result.setCode("0000");
					result.setMessage("验证成功，请修改密码");
				} else {
					result.setMessage("验证失败，请输入正确的验证码");
				}
			}
		}
		return result;
	}

	/**
	 * 功能：修改密码
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/change.action", method = RequestMethod.POST)
	public NormalResultDTO change(Integer id, String password) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		UserInfoDO user = new UserInfoDO();
		user.setId(id);
		user.setPassword(password);
		try {
			userService.updateUser(user);
			result.setCode("0000");
			result.setMessage("密码修改成功，请前往登录");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
}
