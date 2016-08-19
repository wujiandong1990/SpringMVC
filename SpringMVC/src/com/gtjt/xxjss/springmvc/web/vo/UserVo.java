package com.gtjt.xxjss.springmvc.web.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class UserVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String username;
	private String nickname;
	private String password;
	private String status;
	private String uuid;
	private Date createdate;
	private String createuser;
	private Date modifydate;
	private String modifyuser;
	private String bz;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdateStart;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdateEnd;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date modifydateStart;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date modifydateEnd;
	
	private String roleIds;
	private String roleNames;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Date getModifydate() {
		return modifydate;
	}
	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}
	public String getModifyuser() {
		return modifyuser;
	}
	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public Date getCreatedateStart() {
		return createdateStart;
	}
	public void setCreatedateStart(Date createdateStart) {
		this.createdateStart = createdateStart;
	}
	public Date getCreatedateEnd() {
		return createdateEnd;
	}
	public void setCreatedateEnd(Date createdateEnd) {
		this.createdateEnd = createdateEnd;
	}
	public Date getModifydateStart() {
		return modifydateStart;
	}
	public void setModifydateStart(Date modifydateStart) {
		this.modifydateStart = modifydateStart;
	}
	public Date getModifydateEnd() {
		return modifydateEnd;
	}
	public void setModifydateEnd(Date modifydateEnd) {
		this.modifydateEnd = modifydateEnd;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public String getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	

}
