package com.zfm.gleaning.service;

import org.springframework.data.domain.Page;

import com.zfm.gleaning.pojo.CertificateDO;

public interface CertificateService {
	// 分页查询证件信息
	Page<CertificateDO> listCertificates(Integer page, Integer cols);
	// 添加证件
	void addCertificate(CertificateDO cert);
	// 更新证件信息
	void updateCertificate(CertificateDO cert);
	// 删除证件
	void removeCertificate(Integer id);
	// 获取证件
	CertificateDO getCertificate(Integer id);
}
