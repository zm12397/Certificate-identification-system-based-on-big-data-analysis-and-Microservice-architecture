package com.zfm.mydfs.gleaning.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zfm.mydfs.gleaning.CustomerException;
import com.zfm.mydfs.gleaning.dao.HdfsDao;
import com.zfm.mydfs.gleaning.dao.SendbackDao;
import com.zfm.mydfs.gleaning.pojo.PlacesDTO;
import com.zfm.mydfs.gleaning.pojo.SourceDTO;
import com.zfm.mydfs.gleaning.service.AnysisService;
import com.zfm.mydfs.gleaning.service.GenerateService;
import com.zfm.mydfs.gleaning.util.SnCalUtils;
import com.zfm.mydfs.gleaning.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnysisServiceImpl implements AnysisService {
	@Autowired
	private SendbackDao sendbackDao;
	@Autowired
	private HdfsDao hdfsDao;
	// shell脚本执行命令
	private static final String SHELL_URI = "sh /usr/local/hadoop/workspace/gleaning/mapper.sh";
	
//	总地点数文件URI
	private final String COUNT_URI = "hdfs://localhost:9000/user/hadoop/input/count.txt";
//	结果文件URI
	private final String SORT_RESULT_URI = "hdfs://localhost:9000/user/hadoop/output/sort/part-r-00000";
//	聚类结果地点文件
	private final String JW_RESULT_URI = "hdfs://localhost:9000/user/hadoop/output/lal/result$";
	@Override
	public void ansis() {
		// TODO Auto-generated method stub
		// 分析的过程就是调用shell脚本来执行mapreduce程序
		Process process = null;
		Runtime runtime = Runtime.getRuntime();
		if (runtime == null) {
			log.error("创建Runtime失败！");
			return;
		}
		try {
			process = runtime.exec(SHELL_URI);
			process.waitFor();
			// process.getInputStream()可以获取shell执行结果的字节流
			read(process.getInputStream(), System.out);
			read(process.getErrorStream(), System.err);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("执行shell命令失败，错误原因：" + e.getMessage());
			throw new CustomerException("执行shell命令失败");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error("执行shell命令中断，错误原因：" + e.getMessage());
			throw new CustomerException("执行shell命令中断");
		} catch(CustomerException e){
			throw e;
		}catch(Exception e){
			log.error("执行shell命令失败，错误原因：" + e.getMessage());
			throw new CustomerException("执行shell命令失败");
		}finally {
			process.destroy();
		}
	}

	private void read(InputStream inputStream, PrintStream out) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				out.println(line);
			}
		} catch (IOException e) {
			log.error("shell命令执行过程中IO异常：" + e.getMessage());
			throw new CustomerException("shell命令执行过程中IO异常");
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				log.error("shell命令执行过程中IO关闭异常：" + e.getMessage());
				throw new CustomerException("shell命令执行过程中IO关闭异常");
			}
		}
	}

	@Override
	public PlacesDTO getSortResult() {
		// TODO Auto-generated method stub
		PlacesDTO places = new PlacesDTO();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			hdfsDao.read(SORT_RESULT_URI, out);	//从hdfs中读取数据到输出流中
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		List<String> lines = new ArrayList();
		String str_lines = new String(out.toByteArray());
		lines = Arrays.asList(str_lines.split("\n"));
		places.setLines(lines);
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		try {
			hdfsDao.read(COUNT_URI, out2);	//从hdfs中读取数据到输出流中
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		long count = Long.parseLong(new String(out2.toByteArray()));
		places.setCount(count);
		return places;
	}

	@Override
	public Map getLalResult() {
		// TODO Auto-generated method stub
		String result = new String();
		Map resMap = new HashMap();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int i = 0;
		try {
			while(true){
				String uri = JW_RESULT_URI.replace("$", i+"");
				if(hdfsDao.check(uri)){
					i ++;
				}else{
					i --;
					hdfsDao.read(JW_RESULT_URI.replace("$", i+"/part-r-00000"), out);
					break;
				}
			}
				
		} catch (IllegalArgumentException | IOException e1) {
			// TODO Auto-generated catch block
			log.error(e1.getMessage());
		}
		
		result = new String(out.toByteArray());
		System.out.println(result);
		String[] provices = result.split("\n");
		for(String provice : provices){
			String[] locations = provice.split("\t");
			String location = locations[0] + "," + locations[1];
			try {
				SourceDTO source = SnCalUtils.translateLocation(location);
				if(source != null && source.getResult() != null && source.getResult().getAddressComponent() != null){
					resMap.put(source.getResult().getAddressComponent().getProvince(), location);
				}
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resMap;
	}
}
