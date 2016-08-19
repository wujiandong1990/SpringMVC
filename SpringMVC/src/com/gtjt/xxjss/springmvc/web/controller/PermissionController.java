package com.gtjt.xxjss.springmvc.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gtjt.xxjss.springmvc.common.GlobalConstants;
import com.gtjt.xxjss.springmvc.common.util.EHCacheUtil;
import com.gtjt.xxjss.springmvc.exception.ParameterException;
import com.gtjt.xxjss.springmvc.security.interceptors.LoginInterceptor;
import com.gtjt.xxjss.springmvc.service.PermissionServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.Json;
import com.gtjt.xxjss.springmvc.web.vo.PermissionVo;
import com.gtjt.xxjss.springmvc.web.vo.Tree;

/**
 * @Description: 资源控制器
 * @author Wjd
 * @date 2016年6月23日 上午12:23:25
 *
 */
@Controller
@RequestMapping("/permission")
@Scope("prototype")
public class PermissionController {
	
	private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Autowired
	private PermissionServiceI permissionService;

	@RequestMapping("/tree")
	@ResponseBody
	public List<Tree> getTreeMenu(HttpSession session) throws Exception {
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
		List<Tree> trees = permissionService.findTreeMenuByUserId(activeUserInfo.getId());
		return trees;
	}
	
	/**
	 * @Description: 获得资源树，包括所有资源类型
	 * @return
	 * @throws Exception
	 * @author Wjd
	 * @date 2016年8月2日 下午11:43:33
	 */
	@RequestMapping("/treePermission")
	@ResponseBody
	public List<Tree> getTreePermission() throws Exception {
		return permissionService.getTreePermission();
	}
	
	/**
	 * 跳转到资源管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager() {
		return "/admin/permission";
	}

	/**
	 * 获得资源列表
	 * 
	 * 通过用户ID判断，他能看到的资源
	 * 
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/permissionList")
	@ResponseBody
	public List<PermissionVo> treeGrid() throws Exception {
		return permissionService.getTreeGrid();
	}
	
	/**
	 * 跳转到资源添加页面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		List<Map<String, String>> resourceTypeList = (List<Map<String, String>>) EHCacheUtil.get("resourceTypes");
		List<Map<String, String>> statusTypeList = (List<Map<String, String>>) EHCacheUtil.get("statusTypes");
		
		if (resourceTypeList == null || statusTypeList == null) {
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
		}
		request.setAttribute("resourceTypes", resourceTypeList);
		request.setAttribute("statusTypes", statusTypeList);
		
		return "/admin/permissionAdd";
	}
	
	/**
	 * 添加权限
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(HttpSession session, PermissionVo permissionVo) {
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
		if (activeUserInfo != null) {
			permissionVo.setCreateuser(activeUserInfo.getUsername());
		}
		
		Json j = new Json();
		try {
			permissionService.add(activeUserInfo, permissionVo);
			j.setSuccess(true);
			j.setMsg("增加成功！");
		} catch (Exception e) {
			logger.error("增加权限", e);
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	/**
	 * 删除资源
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) throws Exception {
		Json j = new Json();
		permissionService.delete(id);
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 跳转到资源编辑页面
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) throws Exception {
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		List<Map<String, String>> resourceTypeList = (List<Map<String, String>>) EHCacheUtil.get("resourceTypes");
		List<Map<String, String>> statusTypeList = (List<Map<String, String>>) EHCacheUtil.get("statusTypes");
		
		if (resourceTypeList == null || statusTypeList == null) {
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
		}
		request.setAttribute("resourceTypes", resourceTypeList);
		request.setAttribute("statusTypes", statusTypeList);
		
		PermissionVo permissionVo = permissionService.getPermissionById(id);
		request.setAttribute("permission", permissionVo);
		
		return "/admin/permissionEdit";
	}

	/**
	 * 编辑资源
	 * 
	 * @param permissionVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(HttpSession session, PermissionVo permissionVo) throws Exception {
		Json j = new Json();
		if (permissionVo.getId().equalsIgnoreCase(permissionVo.getPid())) {
			j.setSuccess(false);
			j.setMsg("上级权限不能是自身！");
		} else {
			permissionService.edit(session, permissionVo);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		}
		return j;
	}



}
