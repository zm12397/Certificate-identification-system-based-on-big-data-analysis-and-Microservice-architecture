package com.zfm.gleaning;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * 写数据源配置
 * @author zm
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(value = "com.zfm.gleaning.dao",
        entityManagerFactoryRef = "writeEntityManagerFactory",
        transactionManagerRef = "writeTransactionManager", 
        repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
public class WriteDataSourceConfig1 {

    @Autowired
    JpaProperties jpaProperties;

    @Autowired
    @Qualifier("writeDruidDataSource")
    private DataSource writeDruidDataSource;

    /**
     * 通过LocalContainerEntityManagerFactoryBean来获取EntityManagerFactory实例
     * @return
     */
    @Bean(name = "writeEntityManagerFactoryBean")
//    @Primary
    public LocalContainerEntityManagerFactoryBean writeEntityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(writeDruidDataSource)
                .properties(jpaProperties.getProperties())
                .packages("com.zfm.gleaning.pojo") //设置实体类所在位置
                .persistenceUnit("writePersistenceUnit")
                .build();
        //.getObject();//这里直接获取EntityManagerFactory会报空指针
    }

    /**
     * EntityManagerFactory装配
     * @param builder
     * @return
     */
    @Bean(name = "writeEntityManagerFactory")
    @Primary
    public EntityManagerFactory writeEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return this.writeEntityManagerFactoryBean(builder).getObject();
    }

    /**
     * 装配事物管理器
     * @return
     */
    @Bean(name = "writeTransactionManager")
    @Primary
    public PlatformTransactionManager writeTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(writeEntityManagerFactory(builder));
    }
}
