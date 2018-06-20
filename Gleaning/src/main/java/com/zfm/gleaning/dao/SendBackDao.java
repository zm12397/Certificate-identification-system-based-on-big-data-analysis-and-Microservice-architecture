package com.zfm.gleaning.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.zfm.gleaning.pojo.SendBackDO;

public interface SendBackDao  extends BaseDao<SendBackDO,Integer>{

	Page<SendBackDO> findByState(Short i, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.certificate.name = ?1")
	Page<SendBackDO> findByName(String name, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.certificate.number = ?1")
	Page<SendBackDO> findByNumber(String number, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.certificate.name = ?1 AND sbd.certificate.number = ?2")
	Page<SendBackDO> findByNameAndNumber(String name,String number, Pageable pageable);

	Page<SendBackDO> findByType(Short type, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.type = ?1 AND sbd.location.schoolName = ?2")
	Page<SendBackDO> findByTypeAndSchoolName(Short type,String schoolName, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.type = ?1 AND sbd.certificate.name = ?2")
	Page<SendBackDO> findByTypeAndName(Short type,String name, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.type = ?1 AND sbd.certificate.number = ?2")
	Page<SendBackDO> findByTypeAndNumber(Short type,String number, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.type = ?1 AND sbd.location.schoolName = ?2 AND sbd.certificate.name = ?3")
	Page<SendBackDO> findByTypeAndSchoolNameAndName(Short type,String schoolName, String name,Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.type = ?1 AND sbd.certificate.name = ?2 AND sbd.certificate.number = ?3")
	Page<SendBackDO> findByTypeAndNameAndNumber(Short type,String name,String number, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.type = ?1 AND sbd.location.schoolName = ?2 AND sbd.certificate.number = ?3")
	Page<SendBackDO> findByTypeAndSchoolNameAndNumber(Short type,String schoolName,String number, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.type = ?1 AND sbd.location.schoolName = ?2 AND sbd.certificate.name = ?3 AND sbd.certificate.number = ?4")
	Page<SendBackDO> findByTypeAndSchoolNameAndNameAndNumber(Short type,String schoolName,String name,String number, Pageable pageable);
	
	@Query("FROM SendBackDO sbd WHERE sbd.state = ?1 AND sbd.validDate != ?2 AND sbd.validDate <= ?3")
	List<SendBackDO> findByStateAndValidDate(short state,long not,long now);
	
	
}
