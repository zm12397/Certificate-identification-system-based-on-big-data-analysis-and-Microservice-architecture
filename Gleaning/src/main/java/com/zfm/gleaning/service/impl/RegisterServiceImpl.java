package com.zfm.gleaning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.MyTargetDataSource;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;
import com.zfm.gleaning.dao.SysRoleDao;
import com.zfm.gleaning.dao.SysUserRoleDao;
import com.zfm.gleaning.dao.UserInfoDao;
import com.zfm.gleaning.pojo.SysRoleDO;
import com.zfm.gleaning.pojo.SysUserRoleDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.RegisterService;
import com.zfm.gleaning.util.CodeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@MyTargetDataSource(value = MyDataSourceType.SLAVE)
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Override
	public boolean validateUsername(String username) {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			if (userInfoDao.findByUsername(username).isEmpty()) {
				flag = true;
				return flag;
			}
		} catch (Exception e) {
			log.error("数据库查询错误：" + e.getMessage());
			throw new CustomerException("数据库查询错误");
		}
		return flag;
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void addUser(UserInfoDO user) {
		// TODO Auto-generated method stub
		user.setState((short) 1);
		// 先获取输入的原始密码
		String oPassword = "123";
		// 获取用户名
		String username = user.getUsername();
		// 生成盐值：以用户名为前缀补上3位随机数
		String salt = CodeUtils.generateSalt(username, 3);
		// 生成2次md5加密的（密码+盐），作为新的密码
		String nPassword = CodeUtils.generateMD5Code(oPassword, salt, 2);
		user.setPassword(nPassword);
		user.setSalt(salt);
		
		try {
			userInfoDao.save(user);
		} catch (Exception e) {
			log.error("数据库插入用户错误：" + e.getMessage());
			throw new CustomerException("用户添加失败");
		}
//		注册完的用户默认有普通用户的角色
		SysRoleDO role = sysRoleDao.findByRole("ordinary");
		SysUserRoleDO userRole = new SysUserRoleDO();
		userRole.setUserInfo(user);
		userRole.setSysRole(role);
		userRole.setState((short) 1);
		try {
			sysUserRoleDao.save(userRole);
		} catch (Exception e) {
			log.error("数据库插入用户权限关联错误：" + e.getMessage());
			throw new CustomerException("权限添加失败");
		}
	}

}
