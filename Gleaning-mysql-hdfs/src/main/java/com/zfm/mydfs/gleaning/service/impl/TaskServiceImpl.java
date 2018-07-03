package com.zfm.mydfs.gleaning.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.mydfs.gleaning.service.AnysisService;
import com.zfm.mydfs.gleaning.service.GenerateService;
import com.zfm.mydfs.gleaning.service.TaskService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
	@Autowired
	private GenerateService generateService;
	@Autowired
	private AnysisService anysisService;
	@Override
	public void init() {
		// TODO Auto-generated method stub
//		System.out.println("init");
		log.info("初始化生成开始-------------");
		generateService.initGenerate();
		log.info("初始化生成结束，分析开始-------------");
		anysisService.ansis();
		log.info("分析结束-------------");
	}
	@Override
	public void timer() {
		// TODO Auto-generated method stub
//		System.out.println("timer");
		log.info("追加生成开始-------------");
		generateService.appendGenerate();
		log.info("追加生成结束，分析开始-------------");
		anysisService.ansis();
		log.info("分析结束-------------");
	}
	
}
