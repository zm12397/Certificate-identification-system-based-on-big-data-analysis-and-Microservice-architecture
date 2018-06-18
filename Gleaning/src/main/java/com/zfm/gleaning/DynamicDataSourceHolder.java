package com.zfm.gleaning;

import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;

/**
 * 动态数据源管理
 * @author zm
 *
 */
public class DynamicDataSourceHolder {
	//使用ThreadLocal把数据源与当前线程绑定,数据源标识保存在线程变量中，避免多线程操作数据源时互相干扰
    private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

    public static void setDataSource(String dataSourceName) {
        dataSources.set(dataSourceName);
    }

    public static String getDataSource() {
        return (String) dataSources.get();
    }

    public static void clearDataSource() {
        dataSources.remove();
    }
}
