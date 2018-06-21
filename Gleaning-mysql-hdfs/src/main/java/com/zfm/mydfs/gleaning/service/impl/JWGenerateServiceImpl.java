package com.zfm.mydfs.gleaning.service.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zfm.mydfs.gleaning.dao.HdfsDao;
import com.zfm.mydfs.gleaning.dao.SendbackDao;
import com.zfm.mydfs.gleaning.pojo.JWSourceDTO;
import com.zfm.mydfs.gleaning.service.JWGenerateService;
import com.zfm.mydfs.gleaning.util.SnCalUtils;
import com.zfm.mydfs.gleaning.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWGenerateServiceImpl implements JWGenerateService {
//	经纬地点文件URI
	private final String PLACES_URI = "hdfs://localhost:9000/user/hadoop/input/lal.txt";
	@Autowired
	private SendbackDao sendbackDao;
	@Autowired
	private HdfsDao hdfsDao;

	@Override
	public void generate() {
		// TODO Auto-generated method stub
//		从mysql中读取所有地点列表
		List<String> places = sendbackDao.readAddr();
		System.out.println(places);
		StringBuffer sb = new StringBuffer();
		for(String place : places){
			JWSourceDTO source = null;
			try {
				source =  SnCalUtils.translateAddress(place);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				log.error("json格式转换失败： " + e.getMessage());
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				log.error("json映射失败： " + e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				log.error("NoSuchAlgorithmException： " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("IO异常： " + e.getMessage());
			}
			if(source == null || source.getResult() == null || source.getResult().getLocation() == null){
				log.error("返回结果错误：  Null");
			}else{
				sb.append(source.getResult().getLocation() + "\n");
			}
			
		}
		System.out.println(sb.toString());
		try {
			hdfsDao.write(PLACES_URI, sb.toString());	//写入地点文件
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("HDFS文件写入失败： " + e.getMessage());
		}
	}
	
}
