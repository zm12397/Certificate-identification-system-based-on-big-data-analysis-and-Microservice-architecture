package com.zfm.mydfs.gleaning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.zfm.mydfs.gleaning.service.TaskService;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时器
 * 此时默认是串行定时器方式，为保证效率可参考并行实现
 * @author zm
 *
 */
@Component
@Slf4j
public class MyTask {
	@Autowired
	private TaskService taskService;

	
	/**
	 * 每天都更新hdfs的数据，并重新分析，得到新的数据
	 * // 设置为每天0时0分0秒执行
	 */
	@Scheduled(cron = "0 0 0 * * ?")
//	@Scheduled(cron = "0 0/15 * * * ?") // 测试查看每15分钟的执行情况
	public void updateExamState() {
		taskService.timer();
	}
}
