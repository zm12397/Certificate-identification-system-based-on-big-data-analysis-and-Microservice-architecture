package com.zfm.gleaning.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.MyTargetDataSource;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;
import com.zfm.gleaning.dao.EmailRecordDao;
import com.zfm.gleaning.dao.UserInfoDao;
import com.zfm.gleaning.pojo.EmailRecordDO;
import com.zfm.gleaning.pojo.SendBackDO;
import com.zfm.gleaning.pojo.ThanksLettersDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.UserService;
import com.zfm.gleaning.util.CodeUtils;
import com.zfm.gleaning.util.MailUtils;
import com.zfm.gleaning.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@MyTargetDataSource(value = MyDataSourceType.SLAVE)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private EmailRecordDao emailRecordDao;

	@Override
	public Map init(Integer id) {
		// TODO Auto-generated method stub
		UserInfoDO user = getUser(id);
		Map resultMap = new HashMap();
		// 我的认领
		List<SendBackDO> claimings = user.getLost_sendBackes();
		SendBackDO claiming = claimings.isEmpty() ? null : claimings.get(claimings.size() - 1);
		Integer claimingNum = claimings.size();
		// 全部招领
		List<SendBackDO> allPicking = user.getPick_sendBackes();
		// 正在招领
		List<SendBackDO> pickings = new ArrayList();
		
		// 已经成功归还的
		List<SendBackDO> returneds = new ArrayList();
		
		for (SendBackDO sendBack : allPicking) {
			if (sendBack.getState() == 1) {
				returneds.add(sendBack);
			} else if (sendBack.getState() == 2 || sendBack.getState() == 3){
				pickings.add(sendBack);
			}
		}
		
		SendBackDO picking = pickings.isEmpty() ? null : pickings.get(pickings.size() - 1);
		Integer pickingNum = pickings.size();
		SendBackDO returned = returneds.isEmpty() ? null : returneds.get(returneds.size() - 1);
		Integer returnedNum = returneds.size();
		
//		已收到的感谢信
		List<ThanksLettersDO> thanksLetters = user.getReceive_thanksLetters();
		String name = user.getName();								//姓名
		String username = user.getUsername();						//用户名 
		String sex = user.getSex() == 0 ? "男" : "女";				//性别
		Integer age = user.getBirthday() == null ? 0 : TimeUtils.transformToAge(user.getBirthday());		//年龄
		String email = user.getEmail();								//邮箱
		String certificateNumber = user.getCertificateNumber();		//身份证号
		String tel = user.getTel();									//手机号
		double account = user.getAccount();							//账户余额
		double sumAccount = user.getSumAccount();					//总奖励金额		
		int sumTime = user.getSumTime();							//总次数
		int creditScore = user.getCreditScore();					//信用分
		String createDate = TimeUtils.parse(user.getGmtCreate(), "yyyy年MM月dd日");	//注册时间
		String image = user.getImage() == null ? "default-head.jpg" : user.getImage().getUrl();
		resultMap.put("claiming", claiming);
		resultMap.put("claimingNum", claimingNum);
		resultMap.put("picking", picking);
		resultMap.put("pickingNum", pickingNum);
		resultMap.put("returned", returned);
		resultMap.put("returnedNum", returnedNum);
		resultMap.put("thanksLetters", thanksLetters);
		resultMap.put("name", name);
		resultMap.put("username", username);
		resultMap.put("sex", sex);
		resultMap.put("age", age);
		resultMap.put("email", email);
		resultMap.put("certificateNumber", certificateNumber);
		resultMap.put("tel", tel);
		resultMap.put("account", account);
		resultMap.put("sumAccount", sumAccount);
		resultMap.put("sumTime", sumTime);
		resultMap.put("creditScore", creditScore);
		resultMap.put("createDate", createDate);
		resultMap.put("image", image);
		return resultMap;
	}

	@Override
	public UserInfoDO getUser(Integer id) {
		// TODO Auto-generated method stub
		UserInfoDO user = null;
		try {
			user = userInfoDao.findOne(id);
		} catch (Exception e) {
			log.error("用户查询失败：" + e.getMessage());
			throw new CustomerException("用户查询失败");
		}
		return user;
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void recharge(Integer id, Double money) {
		// TODO Auto-generated method stub
		UserInfoDO user = getUser(id);
		UserInfoDO admin = getUser("admin"); 
		try {
			user = userInfoDao.findOne(id);
		} catch (Exception e) {
			log.error("用户查询失败：" + e.getMessage());
			throw new CustomerException("用户查询失败");
		}
		double user_account = user.getAccount();
		double admin_account = admin.getAccount();
		user_account += money;
		admin_account -= money;
		user.setAccount(user_account);
		admin.setAccount(admin_account);
		try {
			userInfoDao.saveAndFlush(user);
			userInfoDao.saveAndFlush(admin);
		} catch (Exception e) {
			log.error("充值失败：" + e.getMessage());
			throw new CustomerException("充值失败");
		}
	}

	@Override
	public UserInfoDO getUser(String username) {
		// TODO Auto-generated method stub
		UserInfoDO user = null;
		try {
			List<UserInfoDO> users = userInfoDao.findByUsername(username);
			if(!users.isEmpty()){
				user = users.get(0);
			}
		} catch (Exception e) {
			log.error("用户数据查询失败：" + e.getMessage());
			throw new CustomerException("用户数据查询失败");
		}
		return user;
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void withdrawals(Integer id, Double money) {
		// TODO Auto-generated method stub
		UserInfoDO user = getUser(id);
		UserInfoDO admin = getUser("admin"); 
		try {
			user = userInfoDao.findOne(id);
		} catch (Exception e) {
			log.error("用户查询失败：" + e.getMessage());
			throw new CustomerException("用户查询失败");
		}
		double user_account = user.getAccount();
		double admin_account = admin.getAccount();
		user_account -= money;
		if(user_account < 0){
			throw new CustomerException("提现失败，账户余额不足");
		}
		admin_account += money;
		user.setAccount(user_account);
		admin.setAccount(admin_account);
		try {
			userInfoDao.saveAndFlush(user);
			userInfoDao.saveAndFlush(admin);
		} catch (Exception e) {
			log.error("提现失败：" + e.getMessage());
			throw new CustomerException("提现失败");
		}
	}

	@Override
	public Page<UserInfoDO> listUsers(Integer page, Integer cols) {
		// TODO Auto-generated method stub
		Page<UserInfoDO> users = null;
		Pageable pageable = new PageRequest(page,cols);
		try{
			users = userInfoDao.findAll(pageable);
		}catch (Exception e) {
			log.error("用户查询失败：" + e.getMessage());
			throw new CustomerException("用户查询失败");
		}
		return users;
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void updateUser(UserInfoDO user) {
		// TODO Auto-generated method stub
		try{
			userInfoDao.save(user,true,true);
		}catch (Exception e) {
			log.error("用户更新失败，数据库更新错误：" + e.getMessage());
			throw new CustomerException("用户更新失败");
		}
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void removeUser(Integer id) {
		// TODO Auto-generated method stub
		UserInfoDO user = this.getUser(id);
		user.setState((short) 0);
		this.updateUser(user);
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void generateValidationEmail(String username) {
		// TODO Auto-generated method stub
		UserInfoDO user = this.getUser(username);
		if(user == null){
			throw new CustomerException("用户名不存在");
		}
		String validation = CodeUtils.generateCode(10);
		user.setValidation(validation);
		long vtime = TimeUtils.getNow() + 24*60*60*1000;
		user.setVtime(vtime);
		try{
			userInfoDao.saveAndFlush(user);
		}catch (Exception e) {
			log.error("用户更新失败，数据库更新错误：" + e.getMessage());
			throw new CustomerException("用户更新失败");
		}
		EmailRecordDO email = new EmailRecordDO();
		email.setState((short) 1);
		email.setType((short) 3);
		email.setUser(user);
		String content = MailUtils.content3;
		content = content.replace("$1", validation);
		content = content.replace("$2", user.getId()+"");
		email.setContent(content);
		try {
			MailUtils.sendHtmlMail(user.getEmail(), "“拾遗”证件认领归还系统密码找回邮件", content);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			log.error("编码格式不支持：" + e1.getMessage());
			throw new CustomerException("编码格式不支持");
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			log.error("邮件发送异常：" + e1.getMessage());
			throw new CustomerException("邮件发送异常");
		}
		try{
			emailRecordDao.saveAndFlush(email);
		}catch (Exception e) {
			log.error("邮件记录插入失败，数据库插入错误：" + e.getMessage());
			throw new CustomerException("邮件记录插入失败");
		}
	}

}
