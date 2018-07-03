package com.zfm.gleaning.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.MyTargetDataSource;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;
import com.zfm.gleaning.dao.CapitalDetailsDao;
import com.zfm.gleaning.dao.CertificateDao;
import com.zfm.gleaning.dao.EmailRecordDao;
import com.zfm.gleaning.dao.LostFoundLocationDao;
import com.zfm.gleaning.dao.SendBackDao;
import com.zfm.gleaning.dao.UserInfoDao;
import com.zfm.gleaning.pojo.CapitalDetailsDO;
import com.zfm.gleaning.pojo.CertificateDO;
import com.zfm.gleaning.pojo.EmailRecordDO;
import com.zfm.gleaning.pojo.LostFoundLocationDO;
import com.zfm.gleaning.pojo.SendBackDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.SendBackService;
import com.zfm.gleaning.util.MailUtils;
import com.zfm.gleaning.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@MyTargetDataSource(value = MyDataSourceType.SLAVE)
public class SendBackServiceImpl implements SendBackService {
	@Autowired
	private SendBackDao sendBackDao;
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private CertificateDao certificateDao;
	@Autowired
	private LostFoundLocationDao lostFoundLocationDao;
	@Autowired
	private CapitalDetailsDao capitalDetailsDao;
	@Autowired
	private EmailRecordDao emailRecordDao;

	@Override
	public List<SendBackDO> listSendBacks(Integer id) {
		// TODO Auto-generated method stub
		try {
			UserInfoDO user = userInfoDao.findOne(id);
			if (user == null) {
				log.error("该用户不存在：" + id);
				throw new CustomerException("该用户不存在");
			} else {
				return user.getPick_sendBackes();
			}
		} catch (Exception e) {
			log.error("数据库查询失败：" + e.getMessage());
			throw new CustomerException("数据库查询失败");
		}
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void addSendBack(SendBackDO sendBack) {
		// TODO Auto-generated method stub
		try {
			UserInfoDO pick = userInfoDao.findOne(sendBack.getPick().getId());
			sendBack.setPick(pick);
		} catch (Exception e) {
			log.error("用户数据库查询失败：" + e.getMessage());
			throw new CustomerException("用户数据库查询失败");
		}
		// 如果证件不存在则先将证件存入数据库
		CertificateDO certificate = sendBack.getCertificate();
		if (certificate.getId() == null) {
			try {
				certificateDao.save(certificate);
			} catch (Exception e) {
				log.error("证件保存失败，数据库错误：" + e.getMessage());
				throw new CustomerException("证件保存失败，数据库错误");
			}

		}
		if (sendBack.getType() == 1) {
			// 如果是校园招领，则要设置招领处关联并处理有效时间
			LostFoundLocationDO location = null;
			try {
				location = lostFoundLocationDao.findOne(sendBack.getLocation().getId());
			} catch (Exception e) {
				log.error("学校招领处数据库查询失败：" + e.getMessage());
				throw new CustomerException("学校招领处数据库查询失败");
			}
			if (location == null || location.getId() == null || location.getId() == 0) {
				log.error("学校招领处不存在");
				throw new CustomerException("学校招领处不存在");
			} else {
				sendBack.setLocation(location);
				// 如果是学校的话，过期时间为当前时间+学校招领处设置的有效时间，但是如果有效时间为-1，即表示无限时间，那么过期时间也为-1
				Long validDate = location.getValidDate() == -1 ? -1 : TimeUtils.getNow() + location.getValidDate();
				sendBack.setValidDate(validDate);
			}
		}
		try {
			sendBackDao.saveAndFlush(sendBack);
		} catch (Exception e) {
			log.error("招领保存失败，数据库错误：" + e.getMessage());
			throw new CustomerException("招领保存失败，数据库错误");
		}

	}

	@Override
	public Page<SendBackDO> listSendBacks(Integer page, Integer rows) {
		// TODO Auto-generated method stub
		Page<SendBackDO> sendBacks = null;
		Sort sort = new Sort(Direction.DESC, "state");
		Pageable pageable = new PageRequest(page, rows, sort);
		try {
			sendBacks = sendBackDao.findAll(pageable);
		} catch (Exception e) {
			log.error("招领数据查询失败：" + e.getMessage());
			throw new CustomerException("招领数据查询失败");
		}

		return sendBacks;
	}

	@Override
	public Page<SendBackDO> listSendBacks(Integer page, Integer rows, String name, String number) {
		// TODO Auto-generated method stub
		Page<SendBackDO> sendBacks = null;
		Sort sort = new Sort(Direction.DESC, "state");
		Pageable pageable = new PageRequest(page, rows, sort);
		int flag = 0;
		// log.info(name + "," + number);
		if (name != null && !name.equals("")) {
			flag += 1;
		}
		if (number != null && !number.equals("")) {
			flag += 2;
		}
		log.info(flag + "");
		try {
			switch (flag) {
			case 0:
				sendBacks = sendBackDao.findAll(pageable);
				break;
			case 1:
				sendBacks = sendBackDao.findByName(name, pageable);
				break;
			case 2:
				sendBacks = sendBackDao.findByNumber(number, pageable);
				break;
			case 3:
				sendBacks = sendBackDao.findByNameAndNumber(name, number, pageable);
				break;
			}
		} catch (Exception e) {
			log.error("招领数据查询失败：" + e.getMessage());
			throw new CustomerException("招领数据查询失败");
		}

		return sendBacks;
	}

	@Override
	public boolean validate(Integer id, String name, String number) {
		// TODO Auto-generated method stub
		SendBackDO sendBack = null;
		try {
			sendBack = sendBackDao.findOne(id);
		} catch (Exception e) {
			log.error("招领数据查询失败：" + e.getMessage());
			throw new CustomerException("招领数据查询失败");
		}
		if (sendBack == null) {
			throw new CustomerException("找不到该招领记录");
		} else {
			if (sendBack.getCertificate().getName().equals(name)
					&& sendBack.getCertificate().getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 认领操作
	 */
	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void claim(Integer id, Integer userId, String postAddr, String postName, String postTel, Double money) {
		// TODO Auto-generated method stub
		// 1.先更新招领表信息
		SendBackDO sendBack = null;
		try {
			sendBack = sendBackDao.findOne(id);
		} catch (Exception e) {
			log.error("招领数据查询失败：" + e.getMessage());
			throw new CustomerException("招领数据查询失败");
		}
		if (sendBack.getState() != 3) {
			throw new CustomerException("当前证件不是招领状态");
		}
		UserInfoDO user = null;
		try {
			user = userInfoDao.findOne(userId);
		} catch (Exception e) {
			log.error("用户数据查询失败：" + e.getMessage());
			throw new CustomerException("用户数据查询失败");
		}
		if (user == null) {
			throw new CustomerException("找不到丢失者信息");
		}
		sendBack.setLost(user);
		sendBack.setState((short) 2);
		sendBack.setMoney(money);
		sendBack.setPostAddr(postAddr);
		sendBack.setPostName(postName);
		sendBack.setPostTel(postTel);
		try {
			sendBackDao.saveAndFlush(sendBack);
		} catch (Exception e) {
			log.error("招领数据更新失败：" + e.getMessage());
			throw new CustomerException("招领数据更新失败");
		}
		// 2.更新丢失者账户信息：出账，更新admin账户信息：入账
		double lost_account = user.getAccount();
		if (lost_account < money) {
			throw new CustomerException("账户余额不足，请充值");
		}
		lost_account -= money;
		UserInfoDO admin = null;
		try {
			admin = userInfoDao.findByUsername("admin").get(0);
		} catch (Exception e) {
			log.error("管理员用户数据查询失败：" + e.getMessage());
			throw new CustomerException("用户数据查询失败");
		}
		double admin_account = admin.getAccount();
		admin_account += money;
		user.setAccount(lost_account);
		admin.setAccount(admin_account);
		try {
			userInfoDao.saveAndFlush(user);
			userInfoDao.saveAndFlush(admin);
		} catch (Exception e) {
			log.error("用户数据更新失败：" + e.getMessage());
			throw new CustomerException("用户数据更新失败");
		}
		// 3.添加资金明细记录
		CapitalDetailsDO in = new CapitalDetailsDO();
		CapitalDetailsDO out = new CapitalDetailsDO();
		in.setNum(money);
		in.setState((short) 1);
		in.setUser(admin);
		in.setType((short) 1);
		out.setNum(money);
		out.setState((short) 1);
		out.setUser(user);
		out.setType((short) 0);
		try {
			capitalDetailsDao.saveAndFlush(in);
			capitalDetailsDao.saveAndFlush(out);
		} catch (Exception e) {
			log.error("资金明细添加失败：" + e.getMessage());
			throw new CustomerException("资金明细添加失败");
		}
		// 4.如果是公共招领，则要发送邮件进行通知
		EmailRecordDO email = new EmailRecordDO();
		email.setState((short) 1);
		email.setType((short) 0); // 0为邮寄通知
		email.setUser(sendBack.getPick()); // 发送给捡拾者
		String content = MailUtils.content0;
		email.setContent(content);
		try {
			MailUtils.sendHtmlMail(user.getEmail(), "“拾遗”证件认领归还系统证件认领通知", content);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			log.error("编码格式不支持：" + e1.getMessage());
			throw new CustomerException("编码格式不支持");
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			log.error("邮件发送异常：" + e1.getMessage());
			throw new CustomerException("邮件发送异常");
		}
		try {
			emailRecordDao.saveAndFlush(email);
		} catch (Exception e) {
			log.error("邮件记录插入失败，数据库插入错误：" + e.getMessage());
			throw new CustomerException("邮件记录插入失败");
		}
	}

	@Override
	public List<SendBackDO> listClaims(Integer id) {
		// TODO Auto-generated method stub
		try {
			UserInfoDO user = userInfoDao.findOne(id);
			if (user == null) {
				log.error("该用户不存在：" + id);
				throw new CustomerException("该用户不存在");
			} else {
				return user.getLost_sendBackes();
			}
		} catch (Exception e) {
			log.error("数据库查询失败：" + e.getMessage());
			throw new CustomerException("数据库查询失败");
		}
	}

	@Override
	public SendBackDO getSendBack(Integer id) {
		// TODO Auto-generated method stub
		SendBackDO sendBack = null;
		try {
			sendBack = sendBackDao.findOne(id);
		} catch (Exception e) {
			log.error("数据库查询失败：" + e.getMessage());
			throw new CustomerException("数据库查询失败");
		}

		return sendBack;
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void complete(Integer id) {
		// TODO Auto-generated method stub
		SendBackDO sendBack = getSendBack(id);
		// 先判断状态
		if (sendBack == null) {
			throw new CustomerException("招领信息不存在");
		}
		if (sendBack.getState() == 1) {
			throw new CustomerException("招领信息已经是完成状态，无法再次完成");
		}
		if (sendBack.getState() == 0) {
			throw new CustomerException("招领信息无效，无法完成");
		}
		// 1.先修改招领信息
		sendBack.setState((short) 1);
		try {
			sendBackDao.saveAndFlush(sendBack);
		} catch (Exception e) {
			log.error("招领数据更新失败：" + e.getMessage());
			throw new CustomerException("招领数据更新失败");
		}
		// 2.资金变动，同时捡拾者信用+1，总归还次数、总奖励金、账户余额变动
		// 当捡拾者信用<2时，无资金变动
		UserInfoDO pick = sendBack.getPick();

		double pick_account = pick.getAccount();
		double money = sendBack.getMoney();
		pick_account += money;
		UserInfoDO admin = null;
		try {
			admin = userInfoDao.findByUsername("admin").get(0);
		} catch (Exception e) {
			log.error("管理员用户数据查询失败：" + e.getMessage());
			throw new CustomerException("用户数据查询失败");
		}
		double admin_account = admin.getAccount();
		admin_account -= money;

		int score = pick.getCreditScore();
		if (score >= 2) {
			// 只有信用值大于等于2，才发生资金出入账以及资金明细记录
			pick.setAccount(pick_account);
			admin.setAccount(admin_account);
			// 3.添加资金明细记录
			CapitalDetailsDO in = new CapitalDetailsDO();
			CapitalDetailsDO out = new CapitalDetailsDO();
			in.setNum(money);
			in.setState((short) 1);
			in.setUser(pick);
			in.setType((short) 1);
			out.setNum(money);
			out.setState((short) 1);
			out.setUser(admin);
			out.setType((short) 0);
			try {
				capitalDetailsDao.saveAndFlush(in);
				capitalDetailsDao.saveAndFlush(out);
			} catch (Exception e) {
				log.error("资金明细添加失败：" + e.getMessage());
				throw new CustomerException("资金明细添加失败");
			}
		}

		pick.setSumTime(pick.getSumTime() + 1);
		pick.setSumAccount(pick.getSumAccount() + money);
		pick.setCreditScore(score + 1);
		try {
			userInfoDao.saveAndFlush(pick);
			userInfoDao.saveAndFlush(admin);
		} catch (Exception e) {
			log.error("用户数据更新失败：" + e.getMessage());
			throw new CustomerException("用户数据更新失败");
		}

	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void incomplete(Integer id) {
		// TODO Auto-generated method stub
		SendBackDO sendBack = getSendBack(id);
		if (sendBack == null) {
			throw new CustomerException("招领信息不存在");
		}
		if (sendBack.getState() == 1) {
			throw new CustomerException("招领信息已经是完成状态，无法再次完成");
		}
		if (sendBack.getState() == 0) {
			throw new CustomerException("招领信息无效，无法完成");
		}
		// 1.先修改招领信息
		sendBack.setState((short) 0);
		try {
			sendBackDao.saveAndFlush(sendBack);
		} catch (Exception e) {
			log.error("招领数据更新失败：" + e.getMessage());
			throw new CustomerException("招领数据更新失败");
		}
		// 2.资金变动，同时捡拾者信用减少，除2上取整，资金退回丢失者账户
		UserInfoDO pick = sendBack.getPick();
		Integer score = pick.getCreditScore();
		score = (int) (score == 1 ? 0 : Math.ceil((double) score / 2));
		pick.setCreditScore(score);
		UserInfoDO lost = sendBack.getLost();
		double lost_account = lost.getAccount();
		double money = sendBack.getMoney();
		lost_account += money;
		UserInfoDO admin = null;
		try {
			admin = userInfoDao.findByUsername("admin").get(0);
		} catch (Exception e) {
			log.error("管理员用户数据查询失败：" + e.getMessage());
			throw new CustomerException("用户数据查询失败");
		}
		double admin_account = admin.getAccount();
		admin_account -= money;
		lost.setAccount(lost_account);
		admin.setAccount(admin_account);
		try {
			userInfoDao.saveAndFlush(pick);
			userInfoDao.saveAndFlush(lost);
			userInfoDao.saveAndFlush(admin);
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
		out.setUser(admin);
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
	public Page<SendBackDO> listSendBacks(Integer page, Integer rows, Short type, String schoolName, String name,
			String number) {
		// TODO Auto-generated method stub
		Page<SendBackDO> sendBacks = null;
		Sort sort = new Sort(Direction.DESC, "state");
		Pageable pageable = new PageRequest(page, rows, sort);

		int flag = 0;
		// log.info(name + "," + number);
		if (name != null && !name.equals("")) {
			flag += 1;
		}
		if (number != null && !number.equals("")) {
			flag += 2;
		}
		if (schoolName != null && !schoolName.equals("")) {
			flag += 4;
		}
		try {
			switch (flag) {
			case 0:
				sendBacks = sendBackDao.findByType(type, pageable);
				break;
			case 1:
				sendBacks = sendBackDao.findByTypeAndName(type, name, pageable);
				break;
			case 2:
				sendBacks = sendBackDao.findByTypeAndNumber(type, number, pageable);
				break;
			case 3:
				sendBacks = sendBackDao.findByTypeAndNameAndNumber(type, name, number, pageable);
				break;
			case 4:
				sendBacks = sendBackDao.findByTypeAndSchoolName(type, schoolName, pageable);
				break;
			case 5:
				sendBacks = sendBackDao.findByTypeAndSchoolNameAndName(type, schoolName, name, pageable);
				break;
			case 6:
				sendBacks = sendBackDao.findByTypeAndSchoolNameAndNumber(type, schoolName, number, pageable);
				break;
			case 7:
				sendBacks = sendBackDao.findByTypeAndSchoolNameAndNameAndNumber(type, schoolName, name, number,
						pageable);
				break;
			}

		} catch (Exception e) {
			log.error("招领数据查询失败：" + e.getMessage());
			throw new CustomerException("招领数据查询失败");
		}

		return sendBacks;
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void updateSendBack(SendBackDO sendBack) {
		// TODO Auto-generated method stub
		try {
			sendBackDao.save(sendBack, true, true);
		} catch (Exception e) {
			log.error("招领数据更新失败：" + e.getMessage());
			throw new CustomerException("招领数据更新失败");
		}
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void updateSendBack(Integer id, String number) {
		// TODO Auto-generated method stub
		SendBackDO sendBack = null;
		try {
			sendBack = this.getSendBack(id);
			if (sendBack == null) {
				throw new CustomerException("查询不到招领信息");
			} else {
				sendBack.setPostNumber(number);
				this.updateSendBack(sendBack);
			}
		} catch (Exception e) {
			log.error("招领数据更新失败：" + e.getMessage());
			throw new CustomerException("招领数据更新失败");
		}
		// 发送邮件进行通知丢失者，及时查收
		UserInfoDO lost = sendBack.getLost();
		EmailRecordDO email = new EmailRecordDO();
		email.setState((short) 1);
		email.setType((short) 1); 	// 1为提醒签收通知
		email.setUser(lost); 		// 发送给丢失者
		String content = MailUtils.content1;
		content = content.replace("$1", number);
		email.setContent(content);
		try {
			MailUtils.sendHtmlMail(lost.getEmail(), "“拾遗”证件认领归还系统证件认领签收通知", content);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			log.error("编码格式不支持：" + e1.getMessage());
			throw new CustomerException("编码格式不支持");
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			log.error("邮件发送异常：" + e1.getMessage());
			throw new CustomerException("邮件发送异常");
		}
		try {
			emailRecordDao.saveAndFlush(email);
		} catch (Exception e) {
			log.error("邮件记录插入失败，数据库插入错误：" + e.getMessage());
			throw new CustomerException("邮件记录插入失败");
		}
	}

}
