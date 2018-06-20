package com.zfm.gleaning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.TableResultDTO;
import com.zfm.gleaning.pojo.ThanksLettersDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.GoodPersonService;
import com.zfm.gleaning.service.ThanksLettersService;

/**
 * 感谢信相关功能controller
 * 
 * @author zm
 *
 */
@RestController
@RequestMapping("/thank")
public class ThanksLettersController {
	@Autowired
	private ThanksLettersService thanksLettersService;

	/**
	 * 功能：获取最新n条感谢信
	 * 
	 * @param top
	 * @return
	 */
	@RequestMapping(value = "/new.action", method = RequestMethod.GET)
	public NormalResultDTO news(Integer news) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		Page<ThanksLettersDO> thanks;
		try {
			thanks = thanksLettersService.listNewLetters(news);
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(thanks);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}

	/**
	 * 功能：获取分页感谢信
	 * 
	 * @return
	 */
	@RequestMapping(value = "/page.action", method = RequestMethod.GET)
	public TableResultDTO page(Integer page, Integer rows) {
		TableResultDTO result = new TableResultDTO("9999", "unknown error", null, 0l);
		Page<ThanksLettersDO> thanks;
		try {
			thanks = thanksLettersService.listPageLetters(page, rows);
			result.setCode("0000");
			result.setMessage("successful");
			result.setRows(thanks.getContent());
			result.setTotal(thanks.getTotalElements());
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
}
