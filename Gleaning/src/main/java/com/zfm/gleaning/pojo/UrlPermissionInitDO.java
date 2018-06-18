package com.zfm.gleaning.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

/**
 * 权限路径初始化表
 * @author zm
 *
 */
@Entity
@Table(name = "url_permission_init")
@DynamicUpdate
@DynamicInsert
@Data
public class UrlPermissionInitDO  implements Serializable{
	// 无关主键ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 10)
	private Integer id;
	// 初始化权限的路径
	@Column(length = 50, nullable = false)
	private String url;
	// 初始化权限的名称
	@Column(name ="permission_name",length = 30, nullable = false)
	private String permissionName;
	// 初始化权限的顺序
	@Column(length = 4, nullable = false)
	private Integer seq;
}
