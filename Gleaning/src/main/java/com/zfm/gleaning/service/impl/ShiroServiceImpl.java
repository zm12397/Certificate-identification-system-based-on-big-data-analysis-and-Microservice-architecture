package com.zfm.gleaning.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.gleaning.MyTargetDataSource;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;
import com.zfm.gleaning.dao.UrlPermissionInitDao;
import com.zfm.gleaning.pojo.UrlPermissionInitDO;
import com.zfm.gleaning.service.ShiroService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@MyTargetDataSource(value = MyDataSourceType.SLAVE)
public class ShiroServiceImpl implements ShiroService{
	/*@Autowired
	private ShiroFilterFactoryBean shiroFilterFactoryBean;*/
	@Autowired
	private UrlPermissionInitDao urlPermissionInitDao;

	@Override
	public Map<String, String> getInitUrlPermission() {
		// TODO Auto-generated method stub
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		List<UrlPermissionInitDO> urlPermissionInits = urlPermissionInitDao.findByOrderBySeq();
		for (UrlPermissionInitDO urlPermissionInit : urlPermissionInits) {
			String url = urlPermissionInit.getUrl();
			String permission = urlPermissionInit.getPermissionName();
			filterChainDefinitionMap.put(url, permission);
			log.info(url + "：" + permission);
		}
		return filterChainDefinitionMap;
	}
	
	/**
	 * 重新加载权限
	 */
	@Override
	public void updatePermission() {
		/*synchronized (shiroFilterFactoryBean) {
			AbstractShiroFilter shiroFilter = null;
			try {
				shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean
						.getObject();
			} catch (Exception e) {
				throw new RuntimeException(
						"get ShiroFilter from shiroFilterFactoryBean error!");
			}
			PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
					.getFilterChainResolver();
			DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
					.getFilterChainManager();
			// 清空老的权限控制
			manager.getFilterChains().clear();
			shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
			shiroFilterFactoryBean
					.setFilterChainDefinitionMap(getInitUrlPermission());
			// 重新构建生成
			Map<String, String> chains = shiroFilterFactoryBean
					.getFilterChainDefinitionMap();
			for (Map.Entry<String, String> entry : chains.entrySet()) {
				String url = entry.getKey();
				String chainDefinition = entry.getValue().trim()
						.replace(" ", "");
				manager.createChain(url, chainDefinition);
			}
			System.out.println("更新权限成功！！");
		}*/
	}
}
