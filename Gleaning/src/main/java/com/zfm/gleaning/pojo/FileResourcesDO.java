package com.zfm.gleaning.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 文件资源表
 * @author zm
 *
 */
@Entity
@Table(name = "file_resources")
@DynamicUpdate
@DynamicInsert
@Data
public class FileResourcesDO  implements Serializable{
	// 无关主键ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 10)
	private Integer id;
	// 创建时间戳
	@Column(name = "gmt_create",insertable = true, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date gmtCreate;
	// 修改时间戳
	@Column(name = "gmt_modified",insertable = false, updatable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date gmtModified;
	// 状态：0无效 1有效
	@Column(length = 1)
	private Short state;
	// 资源路径
	@Column(length = 50, nullable = false)
	private String url;
	@JsonIgnore
	@OneToOne(mappedBy = "image")
	private UserInfoDO user;
	@JsonIgnore
	@OneToOne(mappedBy = "image")
	private CertificateDO certificate;
}
