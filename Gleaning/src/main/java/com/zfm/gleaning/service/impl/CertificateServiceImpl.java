package com.zfm.gleaning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.MyTargetDataSource;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;
import com.zfm.gleaning.dao.CertificateDao;
import com.zfm.gleaning.dao.UserInfoDao;
import com.zfm.gleaning.pojo.CertificateDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.CertificateService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@MyTargetDataSource(value = MyDataSourceType.SLAVE)
public class CertificateServiceImpl implements CertificateService {
	@Autowired
	private CertificateDao certificateDao;

	// 分页查询证件信息
	@Override
	public Page<CertificateDO> listCertificates(Integer page, Integer cols) {
		// TODO Auto-generated method stub
		Page<CertificateDO> certs = null;
		Pageable pageable = new PageRequest(page, cols);
		try {
			certs = certificateDao.findAll(pageable);
		} catch (Exception e) {
			log.error("证件查询失败：" + e.getMessage());
			throw new CustomerException("证件查询失败");
		}
		return certs;
	}

	// 添加证件
	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void addCertificate(CertificateDO cert) {
		// TODO Auto-generated method stub
		cert.setState((short) 1);
		try {
			certificateDao.save(cert);
		} catch (Exception e) {
			log.error("证件添加失败，数据库插入错误：" + e.getMessage());
			throw new CustomerException("证件添加失败");
		}
	}

	// 更新证件信息
	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void updateCertificate(CertificateDO cert) {
		// TODO Auto-generated method stub
		try {
			// 这里最好用自定义的save方法
			certificateDao.save(cert, true, true);
		} catch (Exception e) {
			log.error("证件更新失败，数据库更新错误：" + e.getMessage());
			throw new CustomerException("证件更新失败");
		}
	}

	// 删除证件
	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void removeCertificate(Integer id) {
		// TODO Auto-generated method stub
		CertificateDO certificate = this.getCertificate(id);
		certificate.setState((short) 0);
		this.updateCertificate(certificate);
	}

	// 获取证件
	@Override
	public CertificateDO getCertificate(Integer id) {
		// TODO Auto-generated method stub
		CertificateDO certificate = null;
		try {
			certificate = certificateDao.findOne(id);
		} catch (Exception e) {
			log.error("证件查找失败" + e.getMessage());
			throw new CustomerException("证件查找失败");
		}
		return certificate;
	}

}
