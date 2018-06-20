package com.zfm.gleaning.controller;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.CertificateDO;
import com.zfm.gleaning.pojo.LostFoundLocationDO;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.SendBackDO;
import com.zfm.gleaning.pojo.TableResultDTO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.RegisterService;
import com.zfm.gleaning.service.SendBackService;
import com.zfm.gleaning.service.ThanksLettersService;
import com.zfm.gleaning.util.TimeUtils;

/**
 * 认领相关功能Controller
 * @author zm
 *
 */
@RestController
@RequestMapping("/claim")
public class ClaimController {
	@Autowired
	private SendBackService sendBacksService;
	@Autowired
	private ThanksLettersService thanksLettersService;

	/**
	 * 功能：分页查询招领数据
	 * @param page要查的页号
	 * @param rows返回的行数
	 * @return
	 */
	@RequestMapping(value = "/page.action", method = RequestMethod.GET)
	public TableResultDTO get(Integer page,Integer rows) {
		TableResultDTO result = new TableResultDTO("9999", "unknown error", null, 0l);
		Page<SendBackDO> sendBacks = null;
		try{
			sendBacks = sendBacksService.listSendBacks(page,rows);
			result.setCode("0000");
			result.setMessage("successful");
			result.setRows(sendBacks.getContent());
			result.setTotal(sendBacks.getTotalElements());
		}catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
	
	/**
	 * 功能：根据条件查询招领数据
	 * @param name证件姓名
	 * @param number证件号码
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/condition.action", method = RequestMethod.GET)
	public TableResultDTO get(String name,String number,Integer page,Integer rows) {
		TableResultDTO result = new TableResultDTO("9999", "unknown error", null, 0l);
		Page<SendBackDO> sendBacks = null;
		try{
			sendBacks = sendBacksService.listSendBacks(page,rows,name,number);
			result.setCode("0000");
			result.setMessage("successful");
			result.setRows(sendBacks.getContent());
			result.setTotal(sendBacks.getTotalElements());
		}catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
	
	/**
	 * 功能：查看某招领的详细信息
	 * @param id：招领id
	 * @return
	 */
	@RequestMapping(value = "/info.action", method = RequestMethod.GET)
	public NormalResultDTO info(Integer id) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		SendBackDO sendBack = null;
		try{
			sendBack = sendBacksService.getSendBack(id);
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(sendBack);
		}catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
	
	/**
	 * 功能：认领操作，更新招领信息，同时给捡拾者发送邮件通知寄送
	 * @param id
	 * @param userId
	 * @param postAddr
	 * @param postName
	 * @param postTel
	 * @param money
	 * @return
	 */
	@RequestMapping(value = "/claim.action", method = RequestMethod.POST)
	public NormalResultDTO claim(Integer id,String postAddr,String postName,String postTel,Double money) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		UserInfoDO user = (UserInfoDO)SecurityUtils.getSubject().getPrincipal();	//获得当前执行的subject
		Integer userId = user.getId();
		try{
			sendBacksService.claim(id,userId,postAddr,postName,postTel,money);
			result.setCode("0000");
			result.setMessage("认领成功");
		}catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
	
	/**
	 * 功能：验证身份
	 * @param name
	 * @param number
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/validate.action", method = RequestMethod.POST)
	public NormalResultDTO validate(String name,String number,Integer id) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		boolean flag = false;
		try{
			flag = sendBacksService.validate(id,name,number);
			if(flag){
				result.setCode("0000");
				result.setMessage("验证成功");
			}else{
				result.setMessage("验证失败，信息不正确");
			}
		}catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
	
	/**
	 * 功能：查看我的认领记录列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getMy.action", method = RequestMethod.GET)
	public NormalResultDTO get() {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		List<SendBackDO> sendBacks = null;
		UserInfoDO user = (UserInfoDO)SecurityUtils.getSubject().getPrincipal();	//获得当前执行的subject
		Integer id = user.getId();
		try {
			sendBacks = sendBacksService.listClaims(id);
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
	 * 功能：提交认领
	 * @param id认领的招领id
	 * @param option操作，1表示完成认领，0表示取消认领
	 * @param isThanks是否发送感谢信，1表示发送感谢信，0表示不发送感谢信
	 * @return
	 */
	@RequestMapping(value = "/submit.action", method = RequestMethod.POST)
	public NormalResultDTO submit(Integer id,Integer option,Integer isThanks/*,Integer reason*/) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		try {
			if(option == 1){
				sendBacksService.complete(id);
				result.setMessage("证件认领已成功进行");
				if(isThanks == 1){
					thanksLettersService.addThanksLetters(id);
					result.setMessage("证件认领已成功进行，感谢信已经寄出");
				}
			}else{
				sendBacksService.incomplete(id);
				result.setMessage("证件认领取消成功");
			}
			result.setCode("0000");
			
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
}
