package com.zfm.mirco.gleaning;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyServiceRegister implements ApplicationRunner{
	@Value("${zookeeper.address}")
	private String zAddress;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		CuratorFramework client = CuratorFrameworkFactory.newClient(zAddress, new RetryOneTime(1000));
		client.start();
		client.blockUntilConnected();
//		ServiceInstance<Object> instance = 
//				ServiceInstance.builder().address("192.168.136.131").name("placeService")
//							   .port(8080).build();
		ServiceInstance<Object> instance = 
				ServiceInstance.builder().address(MircoConfiguration.getAddress()).name(MircoConfiguration.getName())
							   .port(MircoConfiguration.getPort()).build();
		ServiceDiscovery<Object> serviceDiscovery = 
				ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/" + MircoConfiguration.getPath()).build();
		serviceDiscovery.registerService(instance);
		serviceDiscovery.start();
	}

}
