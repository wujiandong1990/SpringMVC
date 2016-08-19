package com.gtjt.xxjss.springmvc.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.gtjt.xxjss.springmvc.model.Permission;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.Tree;
import com.gtjt.xxjss.springmvc.web.vo.PermissionVo;


/**
 * @Description: 权限Service
 * @author Wjd
 * @date 2016年6月22日 下午11:18:45
 *
 */
public interface PermissionServiceI extends BaseServiceI<Permission> {
	
	/**
	 * @Description: 获取用户的所有权限
	 * @param userid
	 * @return
	 * @throws Exception
	 * @author Wjd
	 * @date 2016年6月23日 上午11:45:41
	 */
	public List<Permission> findPermissionsByUserId(String userid) throws Exception;
	
	/**
	 * @Description: 判断用户在页面中是否具指定权限
	 * @param userid
	 * @param permission
	 * @return
	 * @throws Exception
	 * @author Wjd
	 * @date 2016年6月29日 下午1:15:03
	 */
	public boolean hasPermission(String userid, String permission) throws Exception;
	
	/**
	 * @Description: 获取用户的树形菜单
	 * @param userid
	 * @return
	 * @throws Exception
	 * @author Wjd
	 * @date 2016年6月23日 下午4:59:54
	 */
	public List<Tree> findTreeMenuByUserId(String userid) throws Exception;
	
	/**
	 * @Description: 获得权限树，包括所有权限类型
	 * @return
	 * @throws Exception
	 * @author Wjd
	 * @date 2016年8月2日 下午11:41:05
	 */
	public List<Tree> getTreePermission() throws Exception;
	
	/**
	 * 获得权限权限列表
	 * 
	 * @return
	 * @throws Exception 
	 */
	public List<PermissionVo> getTreeGrid() throws Exception;
	
	/**
	 * 添加权限
	 * 
	 * @param permissionVo
	 * @param activeUserInfo
	 */
	public void add(ActiveUserInfo activeUserInfo, PermissionVo permissionVo) throws Exception;
	
	/**
	 * 删除权限
	 * 
	 * @param id
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 获得一个权限
	 * 
	 * @param id
	 * @return
	 */
	public PermissionVo getPermissionById(String id) throws Exception;
	
	/**
	 * 修改权限
	 * 
	 * @param permissionVo
	 */
	public void edit(HttpSession session, PermissionVo permissionVo) throws Exception;

//	/**
//	 * 获得权限树(权限类型为菜单类型)
//	 * 
//	 * 通过用户ID判断，他能看到的权限
//	 * 
//	 * @param sessionInfo
//	 * @return
//	 */
//	public List<Tree> tree(ActiveUserInfo sessionInfo);





}
