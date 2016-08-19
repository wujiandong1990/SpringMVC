package com.gtjt.xxjss.springmvc.web.vo;

import java.io.Serializable;
import java.util.List;

import com.gtjt.xxjss.springmvc.model.Permission;

/**
 * @Description: TODO(session信息模型)
 * @author Wjd
 * @date 2016年6月22日 下午10:31:22
 *
 */
public class ActiveUserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String username;
	private String nickname;
	private String password;
	private List<Permission> permissions;
	
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
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	

}
