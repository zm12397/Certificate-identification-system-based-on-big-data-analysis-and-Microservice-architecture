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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

/**
 * 资金明细表
 * @author zm
 *
 */
@Entity
@Table(name = "capital_details")
@DynamicUpdate
@DynamicInsert
@Data
public class CapitalDetailsDO implements Serializable{
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
	// 操作标识：0减少 1增加
	@Column(length = 1, nullable = false)
	private Short type;
	// 金额数值
	@Column(precision = 10, scale = 2, nullable = false)
	private Double num;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserInfoDO user;
}
