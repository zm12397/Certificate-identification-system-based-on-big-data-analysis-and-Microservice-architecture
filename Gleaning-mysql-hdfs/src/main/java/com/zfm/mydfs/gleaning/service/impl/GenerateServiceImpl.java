package com.zfm.mydfs.gleaning.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zfm.mydfs.gleaning.dao.HdfsDao;
import com.zfm.mydfs.gleaning.dao.SendbackDao;
import com.zfm.mydfs.gleaning.service.GenerateService;
import com.zfm.mydfs.gleaning.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GenerateServiceImpl implements GenerateService {
	@Autowired
	private SendbackDao sendbackDao;
	@Autowired
	private HdfsDao hdfsDao;
	
//	注：如果使用ip的话，需要将配置文件core-site.xml中配置的localhost主机名和
//		mapred-site.xml中的localhost主机名及masters和slaves中的localhost主机名全部改为DHCP获取或者自己设置的IP地址
//	private final String PLACES_URI = "hdfs://202.118.212.186:9000/user/hadoop/input/places.txt";
//	private final String LAST_URI = "hdfs://202.118.212.186:9000/user/hadoop/input/last.txt";
//	地点文件URI
	private final String PLACES_URI = "hdfs://localhost:9000/user/hadoop/input/places.txt";
//	总地点数文件URI
	private final String COUNT_URI = "hdfs://localhost:9000/user/hadoop/input/count.txt";
//	最后修改地点文件时间URI
	private final String LAST_URI = "hdfs://localhost:9000/user/hadoop/input/last.txt";
//	最后修改地点文件时间格式
//	注：必须用大写H
	private final String LAST_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 初始化生成地点文件
	 */
	@Override
	public void initGenerate() {
		// TODO Auto-generated method stub
//		System.out.println(new Date());
//		获取当前时间字符串
		String now = TimeUtils.TimeToString(System.currentTimeMillis(), LAST_FORMAT);
//		从mysql中读取所有地点列表
		List<String> places = sendbackDao.readPickAddr(now);
		int count = places.size();
		StringBuffer sb = new StringBuffer();
		for(String place : places){
			sb.append(place + "\n");
		}
		System.out.println(sb.toString());
		try {
			hdfsDao.write(PLACES_URI, sb.toString());	//写入地点文件
			hdfsDao.write(COUNT_URI, count + "");		//写入地点数量
			hdfsDao.write(LAST_URI, now);				//写入最后时间文件
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("HDFS文件写入失败： " + e.getMessage());
		}
		
	}

	/**
	 * 追加地点文件
	 */
	@Override
	public void appendGenerate() {
		// TODO Auto-generated method stub
//		字节数组输出流，用于从输出流中读取字符串
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			hdfsDao.read(LAST_URI, out);	//从hdfs中读取数据到输出流中
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		String last = new String(out.toByteArray());				//最后修改时间
		String now = TimeUtils.TimeToString(System.currentTimeMillis(), LAST_FORMAT);	//获取当前时间
		
		
		List<String> places = sendbackDao.readPickAddr(last,now);	//从mysql中读取从最后修改时间到当前的地点
		
		if(places.isEmpty()){
			return;
		}
		
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		try {
			hdfsDao.read(COUNT_URI, out2);	//从hdfs中读取数据到输出流中
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("HDFS文件读取失败：" + e.getMessage());
		}
		long last_count = Integer.parseInt(new String(out2.toByteArray()));
		long count = last_count + places.size();
		StringBuffer sb = new StringBuffer();
		for(String place : places){
			sb.append(place + "\n");
		}
		try {
			hdfsDao.append(PLACES_URI, sb.toString());		//将刚读出的地点追加到地点文件中
			hdfsDao.write(COUNT_URI, count + "");			//写入地点数量
			hdfsDao.write(LAST_URI, now);					//更新最后修改时间
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("HDFS追加|HDFS文件写入失败：" + e.getMessage());
		}finally{
			try {
				out.close();
				out2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("IO流关闭异常：" + e.getMessage());
			}
			
		}
		
	}

	/**
	 * 检查最后修改时间是否存在
	 */
	@Override
	public boolean checkLastFile() {
		// TODO Auto-generated method stub
		try {
//			检查hdfs中最后修改时间文件是否存在
			return hdfsDao.check(LAST_URI);
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			log.error("HDFS文件检查失败：" + e.getMessage());
			return false;
		}
	}
	
	
	
	
}
