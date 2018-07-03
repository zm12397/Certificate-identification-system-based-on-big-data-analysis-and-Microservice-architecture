package com.zfm.gleaning.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

/**
 * 用户表
 * 
 * @author zm
 *
 */
@Entity
@Table(name = "user_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
@DynamicUpdate
@DynamicInsert
@Data
@ToString(exclude = {"sysUserRoles","emailRecords","lost_sendBackes","pick_sendBackes","image","send_thanksLetters"
		,"receive_thanksLetters","capitalDetails"})
public class UserInfoDO implements Serializable {
	// 无关主键ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 10)
	private Integer id;
	// 用户名
	@Column(length = 50, nullable = false)
	private String username;
	// 姓名
	@Column(length = 50, nullable = false)
	private String name;
	// 密码
	@Column(length = 50, nullable = false)
	private String password;
	// 密码盐
	@Column(length = 50)
	private String salt;
	// 用户状态： 0创建未认证 1有效
	@Column(length = 1)
	private Short state;
	// 性别： 0男 1女
	@Column(length = 1)
	private Short sex;
	// 电话
	@Column(length = 50)
	private String tel;
	// 生日 16位unix时间戳
	@Column(length = 16)
	private Long birthday;
	// 身份证号
	@Column(name = "certificate_number", length = 50, nullable = false)
	private String certificateNumber;
	// 账户余额,默认0
	@Column(precision = 10, scale = 2)
	private Double account;
	// 账户总奖励金额,默认0
	@Column(name = "sum_account", precision = 10, scale = 2)
	private Double sumAccount;
	// 邮箱
	@Column(length = 50, nullable = false)
	private String email;
	// 账户总信用值，默认0
	@Column(name = "credit_score", length = 10)
	private Integer creditScore;
	// 验证信息
	@Column(length = 10)
	private String validation;
	// 验证信息有效时间
	@Column(length = 16)
	private Long vtime;

	// 创建时间戳
	@Column(name = "gmt_create", insertable = true, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date gmtCreate;
	// 修改时间戳
	@Column(name = "gmt_modified", insertable = false, updatable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date gmtModified;

	// 总归还次数
	@Column(name = "sum_time", length = 10)
	private Integer sumTime;

	// 用户头像映射
	@OneToOne
	@JoinColumn(name = "image")
	private FileResourcesDO image;

	// 用户角色关系映射
	@JsonIgnore
	@OneToMany(mappedBy = "userInfo",fetch = FetchType.EAGER)
	private List<SysUserRoleDO> sysUserRoles;

	// 用户邮件映射
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<EmailRecordDO> emailRecords;

	@JsonIgnore
	@OneToMany(mappedBy = "lost")
	private List<SendBackDO> lost_sendBackes;

	@JsonIgnore
	@OneToMany(mappedBy = "pick")
	private List<SendBackDO> pick_sendBackes;

	@JsonIgnore
	@OneToMany(mappedBy = "sender")
	private List<ThanksLettersDO> send_thanksLetters;

	@JsonIgnore
	@OneToMany(mappedBy = "receiver")
	private List<ThanksLettersDO> receive_thanksLetters;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<CapitalDetailsDO> capitalDetails;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfoDO other = (UserInfoDO) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	
}
