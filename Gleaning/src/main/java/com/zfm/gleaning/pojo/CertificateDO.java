package com.zfm.gleaning.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 证件表
 * @author zm
 *
 */
@Entity
@Table(name = "certificate")
@DynamicUpdate
@DynamicInsert
@Data
public class CertificateDO implements Serializable {
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
	// 证件号码
	@Column(length = 50, nullable = false)
	private String number;
	// 证件类型：0身份证，1学生证，2驾驶证，3一卡通，4银行卡，5其他
	@Column(length = 5, nullable = false)
	private Integer type;
	// 证件姓名
	@Column(length = 50)
	private String name;
	// 证件信息
	@Column(length = 100)
	private String info;
	// 证件备注
	@Column(length = 100)
	private String remark;
	// 证件照片映射
	@OneToOne
	@JoinColumn(name = "image")
	private FileResourcesDO image;
	
	@JsonIgnore
	@OneToMany(mappedBy = "certificate")
	private List<SendBackDO> sendBackes;
}
