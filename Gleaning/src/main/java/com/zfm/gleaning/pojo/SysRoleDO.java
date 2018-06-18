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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

/**
 * 角色表
 * 
 * @author zm
 *
 */
@Entity
@Table(name = "sys_role")
@DynamicUpdate
@DynamicInsert
@Data
public class SysRoleDO  implements Serializable{
	// 无关主键ID
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length = 10)
	private Integer id;
	// 角色名称
	@Column(length = 30, nullable = false)
	private String role;
	// 角色描述
	@Column(length = 50)
	private String description;

	// 状态：0无效 1有效
	@Column(length = 1)
	private Short state;

	// 用户角色关系映射
	@OneToMany(mappedBy = "sysRole")
	private List<SysUserRoleDO> sysUserRoleDOes;

	// 角色权限关系映射
	@OneToMany(mappedBy = "sysRole",fetch = FetchType.EAGER)
	private List<SysRolePermissionDO> sysRolePermissions;

}
