package com.zfm.gleaning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.gleaning.dao.SendBackDao;
import com.zfm.gleaning.service.TaskService;
import com.zfm.gleaning.service.impl.TaskServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时器
 * 此时默认是串行定时器方式，为保证效率可参考并行实现
 * @author zm
 *
 */
@Component
@Slf4j
@Transactional
public class MyTask {
	@Autowired
	private TaskService taskService;

	
	/**
	 * 检查更新招领信息，查看是否过期
	 * // 设置为每天0时0分0秒执行
	 */
	@Scheduled(cron = "0 0 0 * * ?")
//	@Scheduled(cron = "0 * * * * ?") // 测试查看每分钟的执行情况
	public void updateExamState() {
		taskService.checkOverdue();
	}
}
