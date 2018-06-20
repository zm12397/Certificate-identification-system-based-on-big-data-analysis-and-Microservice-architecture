package com.zfm.gleaning.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.MyTargetDataSource;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;
import com.zfm.gleaning.dao.CapitalDetailsDao;
import com.zfm.gleaning.dao.LostFoundLocationDao;
import com.zfm.gleaning.dao.SendBackDao;
import com.zfm.gleaning.dao.UserInfoDao;
import com.zfm.gleaning.pojo.CapitalDetailsDO;
import com.zfm.gleaning.pojo.LostFoundLocationDO;
import com.zfm.gleaning.pojo.SendBackDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.SchoolService;
import com.zfm.gleaning.service.SendBackService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@MyTargetDataSource(value = MyDataSourceType.SLAVE)
public class SchoolServiceImpl implements SchoolService {
	@Autowired
	private SendBackService sendBackService;
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private SendBackDao sendBackDao;
	@Autowired
	private CapitalDetailsDao capitalDetailsDao;
	@Autowired
	private LostFoundLocationDao lostFoundLocationDao;

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void complete(Integer id, Integer userId, Double money) {
		// TODO Auto-generated method stub
		SendBackDO sendBack = sendBackService.getSendBack(id);
		// 1.先修改招领信息，招领状态、添加丢失人
		if (sendBack.getState() == 0) {
			throw new CustomerException("无法完成对无效证件的认领");
		} else if (sendBack.getState() == 1) {
			throw new CustomerException("无法完成对已完成证件的认领");
		}
		sendBack.setState((short) 1);
		UserInfoDO lost = null;
		try {
			lost = userInfoDao.findOne(userId);
		} catch (Exception e) {
			log.error("用户数据查询失败：" + e.getMessage());
			throw new CustomerException("用户数据查询失败");
		}
		sendBack.setLost(lost);
		try {
			sendBackDao.saveAndFlush(sendBack);
		} catch (Exception e) {
			log.error("招领数据更新失败：" + e.getMessage());
			throw new CustomerException("招领数据更新失败");
		}
		// 2.捡拾者信用+1，如果有奖励金，则进行资金变动
		UserInfoDO pick = sendBack.getPick();
		pick.setCreditScore(pick.getCreditScore() + 1);
		if (money != null || money != 0.0) {
			double lost_account = lost.getAccount();
			if (lost_account < money) {
				throw new CustomerException("丢失者账户余额不足，请及时充值");
			}
			lost_account -= money;
			double pick_account = pick.getAccount();
			pick_account += money;
			lost.setAccount(lost_account);
			pick.setAccount(pick_account);
			pick.setSumTime(pick.getSumTime() + 1);
			pick.setSumAccount(pick.getSumAccount() + money);
		}
		try {
			userInfoDao.saveAndFlush(lost);
			userInfoDao.saveAndFlush(pick);
		} catch (Exception e) {
			log.error("用户数据更新失败：" + e.getMessage());
			throw new CustomerException("用户数据更新失败");
		}
		// 3.添加资金明细记录
		CapitalDetailsDO in = new CapitalDetailsDO();
		CapitalDetailsDO out = new CapitalDetailsDO();
		in.setNum(money);
		in.setState((short) 1);
		in.setUser(pick);
		in.setType((short) 1);
		out.setNum(money);
		out.setState((short) 1);
		out.setUser(lost);
		out.setType((short) 0);
		try {
			capitalDetailsDao.saveAndFlush(in);
			capitalDetailsDao.saveAndFlush(out);
		} catch (Exception e) {
			log.error("资金明细添加失败：" + e.getMessage());
			throw new CustomerException("资金明细添加失败");
		}
	}

	@Override
	public List<String> listSchools() {
		// TODO Auto-generated method stub
		return lostFoundLocationDao.findSchool();
	}

	@Override
	public List<LostFoundLocationDO> listLocations(String school) {
		// TODO Auto-generated method stub
		return lostFoundLocationDao.findBySchoolName(school);
	}

}
