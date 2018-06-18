package com.zfm.gleaning.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
 * 权限表
 * @author zm
 *
 */
@Entity
@Table(name = "sys_permission")
@DynamicUpdate
@DynamicInsert
@Data
public class SysPermissionDO  implements Serializable{
	// 无关主键ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 10)
	private Integer id;

	// 权限名称
	@Column(length = 30, nullable = false)
	private String name;
	// 资源类型
	@Column(length = 10)
	private String type;
	// 资源路径
	@Column(length = 50, nullable = false)
	private String url;
	// 权限字符串
	@Column(length = 30)
	private String permission;
	// 父权限ID
	@Column(length = 10)
	private Integer pid;
	// 父权限ID列表
	@Column(length = 50)
	private String pids;

	// 状态：0无效 1有效
	@Column(length = 1)
	private Short state;

	// 角色权限关系映射
	@OneToMany(mappedBy = "sysPermission")
	private List<SysRolePermissionDO> sysRolePermissions;
}
