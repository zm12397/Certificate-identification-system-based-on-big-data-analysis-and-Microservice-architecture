package com.zfm.gleaning.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
/**
 * 用户角色多对多关联表
 * @author zm
 *
 */
@Entity
@Table(name = "sys_user_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "role_id" }) })
@DynamicUpdate
@DynamicInsert
@Data
public class SysUserRoleDO  implements Serializable{
	// 无关主键ID
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length = 10)
	private Integer id;

	// 状态
	@Column(length = 1)
	private Short state;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "user_id")
	private UserInfoDO userInfo;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "role_id")
	private SysRoleDO sysRole;
	
}
