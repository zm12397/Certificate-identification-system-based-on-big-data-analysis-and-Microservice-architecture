package com.zfm.gleaning;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;

import lombok.extern.slf4j.Slf4j;

/**
 * 动态数据源切换
 * @author zm
 *
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {

        //简单的负载均衡策略
    	String lookupKey = DynamicDataSourceHolder.getDataSource();
        log.info("------------lookupKey---------"+lookupKey);
        return lookupKey;
    }
}
