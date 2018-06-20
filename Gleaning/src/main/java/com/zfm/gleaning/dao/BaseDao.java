package com.zfm.gleaning.dao;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.zfm.gleaning.pojo.UserInfoDO;

/**
 * DAO基本接口，继承基本CRUD操作
 * 
 * @author zm
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseDao<T, ID extends Serializable> extends JpaRepository<T, ID> {
	// 自定义save方法
	void save(Object entity, boolean ignoreNullProperties, boolean ignoreCollections, String... ignoreProperties)
			throws IllegalArgumentException, IllegalAccessException, IntrospectionException;

	// 分页查询findAll
	Page<T> findAll(Pageable pageable);
}
