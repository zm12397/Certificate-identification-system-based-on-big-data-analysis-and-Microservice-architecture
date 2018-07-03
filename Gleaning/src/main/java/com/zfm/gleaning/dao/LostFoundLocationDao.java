package com.zfm.gleaning.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.zfm.gleaning.pojo.LostFoundLocationDO;

public interface LostFoundLocationDao  extends BaseDao<LostFoundLocationDO,Integer>{
	@Query("SELECT DISTINCT lfl.schoolName FROM LostFoundLocationDO lfl ")
	List<String> findSchool();

	List<LostFoundLocationDO> findBySchoolName(String school);

}
