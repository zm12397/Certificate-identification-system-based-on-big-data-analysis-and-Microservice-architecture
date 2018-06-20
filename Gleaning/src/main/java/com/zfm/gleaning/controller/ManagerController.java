package com.zfm.gleaning.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.CertificateDO;
import com.zfm.gleaning.pojo.FileResourcesDO;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.SendBackDO;
import com.zfm.gleaning.pojo.TableResultDTO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.CertificateService;
import com.zfm.gleaning.service.FileService;
import com.zfm.gleaning.service.RegisterService;
import com.zfm.gleaning.service.SendBackService;
import com.zfm.gleaning.service.UserService;

/**
 * 系统管理相关功能controller
 * 
 * @author zm
 *
 */
@RestController
@RequestMapping("/manage")
public class ManagerController {
	@Autowired
	private UserService userService;
	@Autowired
	private RegisterService registerService;
	@Autowired
	private CertificateService certificateService;
	@Autowired
	private SendBackService sendBackService;
	@Autowired
	private FileService fileService;

	/**
	 * 功能：分页获取用户列表
	 * 
	 * @param page
	 * @param cols
	 * @return
	 */
	@RequestMapping(value = "/user/page.action", method = RequestMethod.GET)
	public TableResultDTO userPage(Integer page, Integer rows) {

		TableResultDTO result = new TableResultDTO("9999", "unknown error", null, 0l);
		Page<UserInfoDO> users = null;
		try {
			users = userService.listUsers(page, rows);
			result.setCode("0000");
			result.setMessage("successful");
			result.setRows(users.getContent());
			result.setTotal(users.getTotalElements());
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：添加用户
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/user/add.action", method = RequestMethod.POST)
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
			result.setMessage("用户添加成功");
		}catch(CustomerException e){
			result.setMessage("用户添加失败");
			return result;
		}
		return result;
	}
	

	/**
	 * 功能：修改用户
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/user/edit.action", method = RequestMethod.POST)
	public NormalResultDTO userEdit(UserInfoDO user) {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			userService.updateUser(user);
			result.setCode("0000");
			result.setMessage("修改成功");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：逻辑删除用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/user/remove.action", method = RequestMethod.POST)
	public NormalResultDTO userRemove(Integer id) {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			userService.removeUser(id);
			result.setCode("0000");
			result.setMessage("successful");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 添加证件
	 * 
	 * @param cert
	 * @return
	 */
	@RequestMapping(value = "/cert/add.action", method = RequestMethod.POST)
	public NormalResultDTO certAdd(CertificateDO cert) {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			certificateService.addCertificate(cert);
			result.setCode("0000");
			result.setMessage("successful");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：编辑证件
	 * 
	 * @param cert
	 * @return
	 */
	@RequestMapping(value = "/cert/edit.action", method = RequestMethod.POST)
	public NormalResultDTO certEdit(CertificateDO cert) {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			certificateService.updateCertificate(cert);
			result.setCode("0000");
			result.setMessage("修改成功");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：修改招领信息
	 * 
	 * @param sendBack
	 * @return
	 */
	@RequestMapping(value = "/sendback/edit.action", method = RequestMethod.POST)
	public NormalResultDTO sendbackEdit(SendBackDO sendBack) {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			sendBackService.updateSendBack(sendBack);
			result.setCode("0000");
			result.setMessage("修改成功");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：逻辑删除证件
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/cert/remove.action", method = RequestMethod.POST)
	public NormalResultDTO certRemove(Integer id) {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			certificateService.removeCertificate(id);
			result.setCode("0000");
			result.setMessage("successful");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
}
