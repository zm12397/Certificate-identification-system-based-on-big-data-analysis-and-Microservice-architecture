package com.zfm.gleaning.dao;

import com.zfm.gleaning.pojo.SysRoleDO;

public interface SysRoleDao  extends BaseDao<SysRoleDO,Integer>{

	SysRoleDO findByRole(String string);

}
