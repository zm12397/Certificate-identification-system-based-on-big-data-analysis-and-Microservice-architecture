package com.zfm.gleaning.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.MyTargetDataSource;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;
import com.zfm.gleaning.dao.SendBackDao;
import com.zfm.gleaning.pojo.SendBackDO;
import com.zfm.gleaning.service.TaskService;
import com.zfm.gleaning.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@MyTargetDataSource(value = MyDataSourceType.SLAVE)
public class TaskServiceImpl implements TaskService {
	@Autowired
	private SendBackDao sendBackDao;

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void checkOverdue() {
		// TODO Auto-generated method stub
		// 获取当前时间
		long now = TimeUtils.getNow();
		List<SendBackDO> sendBackes = new ArrayList();
		try {
			// 查询所有状态不为0、过期时间小于当前时间戳（已经过期的）、过期时间不为-1（-1表示无限，永不过期）
			sendBackes = sendBackDao.findByStateAndValidDate((short) 3, -1l, now);
		} catch (Exception e) {
			log.error("招领数据查询失败：" + e.getMessage());
			throw new CustomerException("招领数据查询失败");
		}
		for (SendBackDO sendBack : sendBackes) {
			sendBack.setState((short) 0); // 设置状态为0失效
			try {
				sendBackDao.save(sendBack);
			} catch (Exception e) {
				log.error("招领数据更新失败：" + e.getMessage());
				throw new CustomerException("招领数据更新失败");
			}
		}
	}
}
