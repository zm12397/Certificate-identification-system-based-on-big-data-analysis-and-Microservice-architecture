package com.zfm.mydfs.gleaning.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.mydfs.gleaning.CustomerException;
import com.zfm.mydfs.gleaning.pojo.NormalResultDTO;
import com.zfm.mydfs.gleaning.pojo.PlacesDTO;
import com.zfm.mydfs.gleaning.service.AnysisService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/anysis")
public class AnysisController {
	@Autowired
	private AnysisService anysisService;
	
	@RequestMapping(path = "/place/get.action",method = RequestMethod.GET)
	public PlacesDTO getPlace(){
		PlacesDTO places = null;
		try{
			places = anysisService.getSortResult();
		}catch(CustomerException e){
			log.error(e.getMessage());
		}
		return places;
	}
	
	@RequestMapping(path = "/provice/get.action",method = RequestMethod.GET)
	public Map getProvice(){
		Map map = new HashMap();
		try{
			map = anysisService.getLalResult();
		}catch(CustomerException e){
			log.error(e.getMessage());
		}
		return map;
	}
}
