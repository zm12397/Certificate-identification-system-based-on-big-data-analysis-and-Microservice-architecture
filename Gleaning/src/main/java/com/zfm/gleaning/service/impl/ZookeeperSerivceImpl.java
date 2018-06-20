package com.zfm.gleaning.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.web.client.RestTemplate;

import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.ZookeeperConfiguration;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.pojo.PlacesDTO;
import com.zfm.gleaning.service.ZookeeperService;
import com.zfm.gleaning.util.LoadEquals;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ZookeeperSerivceImpl implements ZookeeperService {
	
	@Override
	public PlacesDTO getPlaces() {
		// TODO Auto-generated method stub
//		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.136.131:4180",new RetryOneTime(1000));
		CuratorFramework client = CuratorFrameworkFactory.newClient(ZookeeperConfiguration.getIp() + ":" + ZookeeperConfiguration.getPort(),new RetryOneTime(ZookeeperConfiguration.getRetry()));
		client.start();
    	try {
			client.blockUntilConnected();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			log.error("zookeeper客户端启动连接失败：" + e2.getMessage());
			throw new CustomerException("zookeeper客户端启动连接失败");
		}
    	ServiceDiscovery<Object> serviceDiscovery = 
    			ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/gleaning").build();
    	Collection<ServiceInstance<Object>> list = null;
//    	System.out.println(serviceDiscovery);
		try {
			list = serviceDiscovery.queryForInstances("placeService");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.error("zookeeper服务发现失败：" + e1.getMessage());
			throw new CustomerException("zookeeper服务发现失败");
		}
    	List<String> services = new ArrayList();
//    	System.out.println(services + list.toString());
    	if(list != null){
    		for(ServiceInstance<Object> instance : list){
        		String host = instance.getAddress();
        		Integer port = instance.getPort();
        		services.add(host + ":" +port);
        	}
    	}else{
    		log.error("zookeeper服务发现失败，无可用服务");
    		throw new CustomerException("zookeeper服务发现失败，无可用服务");
    	}
//    	System.out.println(services);
    	LoadEquals loadEquals = new LoadEquals(services);
    	RestTemplate rs = new RestTemplate();
		String content = rs.getForObject("http://" + loadEquals.choose() + "/gleaning-mysql-hdfs/anysis/place/get.action", String.class);
//		System.out.println(content);
		ObjectMapper objectMapper = new ObjectMapper();
		PlacesDTO places = null;
		try {
			places = objectMapper.readValue(content, PlacesDTO.class);
			System.out.println(places);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			log.error("zookeeper服务调用失败，Json解析错误：" + e.getMessage());
			throw new CustomerException("zookeeper服务调用失败，Json解析错误");
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			log.error("zookeeper服务调用失败，Json映射错误：" + e.getMessage());
			throw new CustomerException("zookeeper服务调用失败，Json映射错误");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("zookeeper服务调用失败，IO错误：" + e.getMessage());
			throw new CustomerException("zookeeper服务调用失败，IO错误");
		}
//    	System.out.println(res);
		log.info(places.toString());
		return places;
	}

	@Override
	public Map getJWProvice() {
		// TODO Auto-generated method stub
		CuratorFramework client = CuratorFrameworkFactory.newClient(ZookeeperConfiguration.getIp() + ":" + ZookeeperConfiguration.getPort()
		,new RetryOneTime(ZookeeperConfiguration.getRetry()));	//retryOneTime设定多久之后重连一次
		client.start();
    	try {
			client.blockUntilConnected();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			log.error("zookeeper客户端启动连接失败：" + e2.getMessage());
			throw new CustomerException("zookeeper客户端启动连接失败");
		}
    	ServiceDiscovery<Object> serviceDiscovery = 
    			ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/gleaning").build();
    	Collection<ServiceInstance<Object>> list = null;
//    	System.out.println(serviceDiscovery);
		try {
			list = serviceDiscovery.queryForInstances("placeService");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.error("zookeeper服务发现失败：" + e1.getMessage());
			throw new CustomerException("zookeeper服务发现失败");
		}
    	List<String> services = new ArrayList();
//    	System.out.println(services + list.toString());
    	if(list != null){
    		for(ServiceInstance<Object> instance : list){
        		String host = instance.getAddress();
        		Integer port = instance.getPort();
        		services.add(host + ":" +port);
        	}
    	}else{
    		log.error("zookeeper服务发现失败，无可用服务");
    		throw new CustomerException("zookeeper服务发现失败，无可用服务");
    	}
//    	System.out.println(services);
    	LoadEquals loadEquals = new LoadEquals(services);
    	RestTemplate rs = new RestTemplate();
		String content = rs.getForObject("http://" + loadEquals.choose() + "/gleaning-mysql-hdfs/anysis/provice/get.action", String.class);
//		System.out.println(content);
		ObjectMapper objectMapper = new ObjectMapper();
		Map map = null;
		try {
			map = objectMapper.readValue(content, HashMap.class);
			System.out.println(map);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			log.error("zookeeper服务调用失败，Json解析错误：" + e.getMessage());
			throw new CustomerException("zookeeper服务调用失败，Json解析错误");
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			log.error("zookeeper服务调用失败，Json映射错误：" + e.getMessage());
			throw new CustomerException("zookeeper服务调用失败，Json映射错误");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("zookeeper服务调用失败，IO错误：" + e.getMessage());
			throw new CustomerException("zookeeper服务调用失败，IO错误");
		}
//    	System.out.println(res);
		log.info(map.toString());
		return map;
	}
	
}
