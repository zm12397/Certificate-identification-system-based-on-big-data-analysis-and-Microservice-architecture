package com.zfm.gleaning.dao;

import java.util.List;

import com.zfm.gleaning.pojo.UrlPermissionInitDO;

public interface UrlPermissionInitDao  extends BaseDao<UrlPermissionInitDO,Integer>{

	List<UrlPermissionInitDO> findByOrderBySeq();

}
