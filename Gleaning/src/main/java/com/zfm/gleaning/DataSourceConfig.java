package com.zfm.gleaning;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源配置类
 * @author zm
 *
 */
@Configuration
public class DataSourceConfig {

    private final static String WRITE_DATASOURCE_KEY = "writeDruidDataSource";
    private final static String READ_DATASOURCE_KEY = "readDruidDataSource";

    /**
     * 功能：注入AbstractRoutingDataSource
     * @param readDruidDataSource
     * @param writeDruidDataSource
     * @return
     * @throws Exception
     */
    @Bean
    public AbstractRoutingDataSource routingDataSource(
            @Qualifier(READ_DATASOURCE_KEY) DataSource readDruidDataSource,
            @Qualifier(WRITE_DATASOURCE_KEY) DataSource writeDruidDataSource
    ) throws Exception {
        DynamicDataSource dataSource = new DynamicDataSource();

        Map<Object, Object> targetDataSources = new HashMap();
        targetDataSources.put(WRITE_DATASOURCE_KEY, writeDruidDataSource);
        targetDataSources.put(READ_DATASOURCE_KEY, readDruidDataSource);
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(writeDruidDataSource);
        return dataSource;
    }
}
