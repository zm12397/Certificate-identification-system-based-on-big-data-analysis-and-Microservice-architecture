package com.zfm.gleaning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 主controller
 * @author zm
 *
 */
@Controller
@RequestMapping("/")
public class MainController {
	private Logger logger = LoggerFactory.getLogger(MainController.class);
	/**
	 * 主视图映射
	 * @param page
	 * @return
	 */
	@RequestMapping(path = "{page}", method = RequestMethod.GET)
	public String main(@PathVariable String page) {
		return "/" + page;
	}
	
}
