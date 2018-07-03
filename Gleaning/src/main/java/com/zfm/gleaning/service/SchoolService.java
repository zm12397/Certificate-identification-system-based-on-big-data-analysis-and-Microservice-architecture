package com.zfm.gleaning.service;

import java.util.List;

import com.zfm.gleaning.pojo.LostFoundLocationDO;

public interface SchoolService {

	void complete(Integer id, Integer userId, Double money);

	List<String> listSchools();

	List<LostFoundLocationDO> listLocations(String school);

}
