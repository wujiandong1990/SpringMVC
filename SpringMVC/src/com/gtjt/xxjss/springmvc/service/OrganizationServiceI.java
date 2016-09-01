package com.gtjt.xxjss.springmvc.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.gtjt.xxjss.springmvc.model.Organization;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.OrganizationVo;
import com.gtjt.xxjss.springmvc.web.vo.Tree;


/**
 * @Description: 机构Service
 * @author Wjd
 * @date 2016年6月22日 下午11:18:45
 *
 */
public interface OrganizationServiceI extends BaseServiceI<Organization> {
	
//	/**
//	 * @Description: 获取用户的所有机构
//	 * @param userid
//	 * @return
//	 * @throws Exception
//	 * @author Wjd
//	 * @date 2016年6月23日 上午11:45:41
//	 */
//	public List<Permission> findPermissionsByUserId(String userid) throws Exception;
//	
//	/**
//	 * @Description: 获取用户的树形菜单
//	 * @param userid
//	 * @return
//	 * @throws Exception
//	 * @author Wjd
//	 * @date 2016年6月23日 下午4:59:54
//	 */
//	public List<Tree> findTreeMenuByUserId(String userid) throws Exception;
	
	/**
	 * @Description: 获得机构树，包括所有机构类型
	 * @return
	 * @throws Exception
	 * @author Wjd
	 * @date 2016年8月2日 下午11:41:05
	 */
	public List<Tree> getTreeOrganization() throws Exception;
	
	/**
	 * 获得机构列表
	 * 
	 * @return
	 * @throws Exception 
	 */
	public List<OrganizationVo> getTreeGrid() throws Exception;
	
	/**
	 * 添加机构
	 * 
	 * @param permissionVo
	 * @param activeUserInfo
	 */
	public void add(ActiveUserInfo activeUserInfo, OrganizationVo organizationVo) throws Exception;
	
	/**
	 * 删除机构
	 * 
	 * @param id
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 获得一个机构
	 * 
	 * @param id
	 * @return
	 */
	public OrganizationVo getOrganizationById(String id) throws Exception;
	
	/**
	 * 修改机构
	 * 
	 * @param permissionVo
	 */
	public void edit(HttpSession session, OrganizationVo organizationVo);
//
////	/**
////	 * 获得机构树(机构类型为菜单类型)
////	 * 
////	 * 通过用户ID判断，他能看到的机构
////	 * 
////	 * @param sessionInfo
////	 * @return
////	 */
////	public List<Tree> tree(ActiveUserInfo sessionInfo);





}
