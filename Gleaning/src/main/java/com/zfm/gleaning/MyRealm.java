package com.zfm.gleaning;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zfm.gleaning.pojo.SysPermissionDO;
import com.zfm.gleaning.pojo.SysRoleDO;
import com.zfm.gleaning.pojo.SysRolePermissionDO;
import com.zfm.gleaning.pojo.SysUserRoleDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * shiro权限realm认证实现
 * @author zm
 *
 */
@Slf4j
public class MyRealm extends AuthorizingRealm{
	@Autowired
	private UserService userService;
	/**
	 * 此方法调用 hasRole,hasPermission的时候才会进行回调.
	 * 
	 * 权限信息.(授权): 1、如果用户正常退出，缓存自动清空； 2、如果用户非正常退出，缓存自动清空；
	 * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。 （需要手动编程进行实现；放在service进行调用）
	 * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例， 调用clearCached方法；
	 * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
	 * 
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*
		 * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行， 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
		 * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了， 缓存过期之后会再次执行。
		 */
		log.info("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 参数中获取登录对象
		UserInfoDO user = (UserInfoDO) principals.getPrimaryPrincipal();

		 /*实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		 UserInfo userInfo = userInfoService.findByUsername(username)

		 权限单个添加;
		 或者按下面这样添加
		 添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色
		 authorizationInfo.addRole("admin");
		 添加权限
		 authorizationInfo.addStringPermission("userInfo:query");

		 在认证成功之后返回.
		 设置角色信息.
		 支持 Set集合,
		 用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
		 List<Role> roleList=user.getRoleList();
		 for (Role role : roleList) {
		 info.addStringPermissions(role.getPermissionsName());
		 }*/

		// 获取登录用户的所有角色和权限（没有缓存时每次访问url都要执行一次）
		List<SysUserRoleDO> sysUserRoles = user.getSysUserRoles();
		for(SysUserRoleDO userRole : sysUserRoles){
			SysRoleDO role = userRole.getSysRole();
			authorizationInfo.addRole(role.getRole());// 添加角色
			log.info("role:" + role.getRole());
			List<SysRolePermissionDO> sysRolePermissions = role.getSysRolePermissions();
			for(SysRolePermissionDO rolePermission : sysRolePermissions){
				SysPermissionDO permission = rolePermission.getSysPermission();
				authorizationInfo.addStringPermission(permission.getName());	// 添加权限
				log.info("permission:" + permission.getName());
			}
		}

		// 设置权限信息.
		// authorizationInfo.setStringPermissions(getStringPermissions(userInfo.getRoleList()));

		return authorizationInfo;
	}

	/**
	 * 验证用户登录
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("用户登录验证-----------------------");
		String username = (String) token.getPrincipal();	//用户名
		log.info("登录用户名：" + token.getPrincipal());
		//根据用户名获取用户对象
		UserInfoDO user = userService.getUser(username);
		
		SimpleAuthenticationInfo authenticationInfo = null;
		if(user == null){
			throw new AccountException("帐号或密码不正确！");
			// authenticationInfo = new SimpleAuthenticationInfo();
		}else{
			if(user.getState() == 0){
				throw new DisabledAccountException("帐号已经禁止登录！");
			}
			//根据用户名、盐、getName（）方面作为三个参数获取authenticationInfo对象
//			authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
//					/*ByteSource.Util.bytes(user.getSalt()), */getName());
			authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
					ByteSource.Util.bytes(user.getSalt()),getName());
//			log.info(user.getSalt());
		}
		return authenticationInfo;
	}
}
