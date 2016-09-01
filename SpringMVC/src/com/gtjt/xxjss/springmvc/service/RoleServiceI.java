package com.gtjt.xxjss.springmvc.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.gtjt.xxjss.springmvc.model.Role;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.RoleVo;


/**
 * @Description: TODO(角色Service)
 * @author Wjd
 * @date 2016年6月22日 下午11:17:33
 *
 */
public interface RoleServiceI extends BaseServiceI<Role> {
	
	
	/**
	 * 获得角色getDataGrid
	 * 
	 * @return
	 */
	public List<RoleVo> getDataGrid() throws Exception;
	
//	/**
//	 * 获得角色树(只能看到自己拥有的角色)
//	 * 
//	 * @return
//	 */
//	public List<Tree> tree(ActiveUserInfo activeUserInfo) throws Exception;

	/**
	 * 保存角色
	 * 
	 * @param role
	 */
	public void add(ActiveUserInfo activeUserInfo, RoleVo roleVo) throws Exception;
	
	/**
	 * 删除角色
	 * 
	 * @param id
	 */
	public void delete(String id) throws Exception;

	/**
	 * 获得角色
	 * 
	 * @param id
	 * @return
	 */
	public RoleVo getRoleById(String id) throws Exception;

	/**
	 * 编辑角色
	 * 
	 * @param roleVo
	 */
	public void edit(HttpSession session, RoleVo roleVo) throws Exception;

	/**
	 * 为角色授权
	 * 
	 * @param roleVo
	 */
	public void grant(RoleVo roleVo) throws Exception;

}
