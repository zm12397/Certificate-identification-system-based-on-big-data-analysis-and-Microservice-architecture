package com.zfm.gleaning.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 招领表
 * @author zm
 *
 */
@Entity
@Table(name = "send_back")
@DynamicUpdate
@DynamicInsert
@Data
public class SendBackDO  implements Serializable{
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
	// 有效日期（默认-1，表示永久，否则为过期日期的毫秒数）
	@Column(name = "valid_date",length = 16)
	private Long validDate;
	// 邮寄地址
	@Column(name = "post_addr",length = 100)
	private String postAddr;
	// 邮寄姓名
	@Column(name = "post_name",length = 50)
	private String postName;
	// 邮寄手机号
	@Column(name = "post_tel",length = 50)
	private String postTel;
	// 快递号
	@Column(name = "post_number",length = 50)
	private String postNumber;
	// 捡到地址
	@Column(name = "pick_addr",length = 100)
	private String pickAddr;
	// 奖励金额
	@Column(precision = 10, scale = 2)
	private Double money;
	// 类型：0社会，1学校
	@Column(length = 1)
	private Short type;
	
	@ManyToOne
	@JoinColumn(name = "certificate_id")
	private CertificateDO certificate;
	
	@ManyToOne
	@JoinColumn(name = "lost_id")
	private UserInfoDO lost;
	
	@ManyToOne
	@JoinColumn(name = "pick_id")
	private UserInfoDO pick;
	
	@ManyToOne
	@JoinColumn(name = "location_id")
	private LostFoundLocationDO location;
}
