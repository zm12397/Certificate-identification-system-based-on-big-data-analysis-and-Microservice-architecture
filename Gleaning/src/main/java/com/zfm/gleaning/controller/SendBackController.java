package com.zfm.gleaning.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.CertificateDO;
import com.zfm.gleaning.pojo.FileResourcesDO;
import com.zfm.gleaning.pojo.LostFoundLocationDO;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.SendBackDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.CertificateService;
import com.zfm.gleaning.service.FileService;
import com.zfm.gleaning.service.SendBackService;
import com.zfm.gleaning.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 证件招领相关功能controller
 * 
 * @author zm
 *
 */
@RestController
@RequestMapping("/sendBack")
@Slf4j
public class SendBackController {
	@Autowired
	private SendBackService sendBacksService;
	@Autowired
	private FileService fileService;
	@Autowired
	private CertificateService certificateService;

	/**
	 * 功能：查看我的招领记录列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getMy.action", method = RequestMethod.GET)
	public NormalResultDTO get() {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		List<SendBackDO> sendBacks = null;
		UserInfoDO user = (UserInfoDO) SecurityUtils.getSubject().getPrincipal(); // 获得当前执行的subject
		Integer id = user.getId();
		try {
			sendBacks = sendBacksService.listSendBacks(id);
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(sendBacks);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：发布招领
	 * 
	 * @return
	 */
	@RequestMapping(value = "/post.action", method = RequestMethod.POST)
	public NormalResultDTO post(CertificateDO certificate, String pickAddr, Short sendBackType, Integer locationId,
			Integer img, Long validDate) {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		if (img != null) {
			FileResourcesDO file = null;
			try {
				file = fileService.getFileResources(img);
			} catch (CustomerException e) {
				result.setMessage(e.getMessage());
				return result;
			}
			if (file != null) {
				certificate.setImage(file);
			}
		}
		try {
			certificateService.addCertificate(certificate);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		SendBackDO sendBack = new SendBackDO();
		sendBack.setCertificate(certificate);
		sendBack.setPickAddr(pickAddr);
		sendBack.setType(sendBackType);
		sendBack.setState((short) 3); // 3是招领状态

		UserInfoDO user = (UserInfoDO) SecurityUtils.getSubject().getPrincipal(); // 获得当前执行的subject
		Integer userId = user.getId();

		UserInfoDO user1 = new UserInfoDO();
		user1.setId(userId);
		sendBack.setPick(user1);

		// 社会招领
		if (sendBackType == 0) {
			if (validDate == null || validDate <= 0) {
				validDate = -1l;
			} else {
				// 有效时间为当前时间加上设置的小时数对应的毫秒值
				validDate = validDate * 3600000 + TimeUtils.getNow();
			}
			sendBack.setValidDate(validDate);
		} else if (sendBackType == 1) {
			// 校园招领
			LostFoundLocationDO location = new LostFoundLocationDO();
			location.setId(locationId);
			sendBack.setLocation(location);
		}
		try {
			sendBacksService.addSendBack(sendBack);
			result.setCode("0000");
			result.setMessage("发布成功");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		UserInfoDO user2 = sendBack.getPick();
		if (user2.getCreditScore() < 2) {
			result.setMessage("发布成功(由于当前用户信用较低，暂时无法获得奖励)");
		}
		return result;
	}

	/**
	 * 功能：提交快递单号，同时给丢失者发送邮件提醒签收
	 * 
	 * @param id
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/postNumber.action", method = RequestMethod.POST)
	public NormalResultDTO postNumber(Integer id, String number) {

		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			sendBacksService.updateSendBack(id, number);
			result.setCode("0000");
			result.setMessage("successful");
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
}
