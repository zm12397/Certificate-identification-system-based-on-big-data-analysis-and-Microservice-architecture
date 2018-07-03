package com.zfm.gleaning.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.LostFoundLocationDO;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.SendBackDO;
import com.zfm.gleaning.pojo.TableResultDTO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.SchoolService;
import com.zfm.gleaning.service.SendBackService;

/**
 * 校园专区相关功能controller
 * 
 * @author zm
 *
 */
@RestController
@RequestMapping("/school")
public class SchoolController {
	@Autowired
	private SendBackService sendBacksService;
	@Autowired
	private SchoolService schoolService;

	/**
	 * 功能：获取校园的招领信息,当schoolName为null时获取全部学校的招领信息，否则只获取对应学校的招领信息
	 * 
	 * @param schoolName
	 * @return
	 */
	@RequestMapping(value = "/get.action", method = RequestMethod.GET)
	public TableResultDTO get(Integer page, Integer rows, String schoolName, String name, String number) {
		TableResultDTO result = new TableResultDTO("9999", "unknown error", null, 0l);
		Page<SendBackDO> sendBacks = null;
		try {
			sendBacks = sendBacksService.listSendBacks(page, rows, (short) 1, schoolName, name, number);
			result.setCode("0000");
			result.setMessage("successful");
			result.setRows(sendBacks.getContent());
			result.setTotal(sendBacks.getTotalElements());
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：校园认领
	 * 
	 * @return
	 */
	@RequestMapping(value = "/claim.action", method = RequestMethod.POST)
	public NormalResultDTO claim(Integer id, Double money) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		UserInfoDO user = (UserInfoDO) SecurityUtils.getSubject().getPrincipal(); // 获得当前执行的subject
		Integer userId = user.getId();
		try {
			sendBacksService.claim(id, userId, null, null, null, money);
			result.setCode("0000");
			result.setMessage("认领成功");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
	/*
	 * @RequestMapping(value = "/submit.action", method = RequestMethod.GET)
	 * public NormalResultDTO submit(Integer id,Integer userId,Double
	 * money,Integer isThanks) { NormalResultDTO result = new
	 * NormalResultDTO("9999", "unknown error", null); try{
	 * schoolService.complete(id,userId,money); if(isThanks == 1){
	 * thanksLettersService.addThanksLetters(id); } result.setCode("0000");
	 * result.setMessage("successful"); }catch (CustomerException e) {
	 * result.setMessage(e.getMessage()); return result; } return result; }
	 */

	/**
	 * 功能：获取全部学校
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getSchool.action", method = RequestMethod.GET)
	public NormalResultDTO getSchool() {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		List<String> schools = new ArrayList();
		try {
			schools = schoolService.listSchools();
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(schools);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：获取某学校的全部招领处
	 * 
	 * @param school
	 * @return
	 */
	@RequestMapping(value = "/getLocation.action", method = RequestMethod.GET)
	public NormalResultDTO getLocation(String school) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		List<LostFoundLocationDO> locations = new ArrayList();
		try {
			locations = schoolService.listLocations(school);
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(locations);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
}
