package com.zfm.gleaning.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.LocationDTO;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.PlacesDTO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.GoodPersonService;
import com.zfm.gleaning.service.ZookeeperService;

/**
 * 大数据分析相关功能controller
 * @author zm
 *
 */
@RestController
@RequestMapping("/anysis")
public class AnysisController {
	@Autowired
	private ZookeeperService zookeeperService;
	
	
	/**
	 * 功能：获取分词地点排行
	 * 
	 * @param top
	 * @return
	 */
	@RequestMapping(value = "/places.action", method = RequestMethod.GET)
	public NormalResultDTO places() {
		NormalResultDTO result = new NormalResultDTO("9999","unknown error",null);
		PlacesDTO places = null;
		try{
			places = zookeeperService.getPlaces();
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(places);
		}catch(CustomerException e){
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
	/**
	 * 功能：获取聚类分析结果
	 * @return
	 */
	@RequestMapping(value = "/provices.action", method = RequestMethod.GET)
	public NormalResultDTO provices() {
		NormalResultDTO result = new NormalResultDTO("9999","unknown error",null);
		Map<String,String> map = null;
		List<String> provices = new ArrayList();
		List<LocationDTO> lals = new ArrayList();
		
		try{
			map = zookeeperService.getJWProvice();
			for(Entry<String,String> entry : map.entrySet()){
				provices.add(entry.getKey());
				String[] locations = entry.getValue().split(",");
				LocationDTO location = new LocationDTO(locations[0],locations[1]);
				lals.add(location);
			}
			Object[] obj = new Object[]{provices,lals};
			result.setCode("0000");
			result.setMessage("successful");
			result.setData(obj);
		}catch(CustomerException e){
			result.setMessage(e.getMessage());
			return result;
		}
		return result;
	}
}
