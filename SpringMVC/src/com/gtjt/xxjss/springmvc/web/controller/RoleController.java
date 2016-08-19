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
import com.gtjt.xxjss.springmvc.service.RoleServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.Json;
import com.gtjt.xxjss.springmvc.web.vo.RoleVo;

/**
 * @Description: 角色控制器
 * @author Wjd
 * @date 2016年6月23日 上午12:23:43
 *
 */
@Controller
@RequestMapping("/role")
@Scope("prototype")
public class RoleController {
	private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Autowired
	private RoleServiceI roleService;
	
	/**
	 * 跳转到角色管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager() {
		return "/admin/role";
	}

	/**
	 * 获得角色列表
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/roleList")
	@ResponseBody
	public List<RoleVo> getDataGrid() throws Exception {
		return roleService.getDataGrid();
	}
	
//	/**
//	 * 角色树(只能看到自己拥有的角色)
//	 * 
//	 * @return
//	 * @throws Exception 
//	 */
//	@RequestMapping("/tree")
//	@ResponseBody
//	public List<Tree> tree(HttpSession session) throws Exception {
//		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
//		return roleService.tree(activeUserInfo);
//	}

	/**
	 * 跳转到角色添加页面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		List<Map<String, String>> list = (List<Map<String, String>>) EHCacheUtil.get("statusTypes");
		if (list == null) {
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
		}
		request.setAttribute("statusTypes", list);
		return "/admin/roleAdd";
	}
	
	/**
	 * 添加角色
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(HttpSession session, RoleVo roleVo) throws Exception {
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
		Json j = new Json();
		roleService.add(activeUserInfo, roleVo);
		j.setSuccess(true);
		j.setMsg("添加成功！");
		return j;
	}
	
	/**
	 * 删除角色
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) throws Exception {
		Json j = new Json();
		roleService.delete(id);
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 跳转到角色修改页面
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) throws Exception {
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		List<Map<String, String>> list = (List<Map<String, String>>) EHCacheUtil.get("statusTypes");
		if (list == null) {
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
		}
		request.setAttribute("statusTypes", list);
		
		RoleVo roleVo = roleService.getRoleById(id);
		request.setAttribute("role", roleVo);
		return "/admin/roleEdit";
	}

	/**
	 * 修改角色
	 * 
	 * @param session, roleVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(HttpSession session, RoleVo roleVo) throws Exception {
		Json j = new Json();
		roleService.edit(session, roleVo);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		return j;
	}

	/**
	 * 跳转到角色授权页面
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/grantPage")
	public String grantPage(HttpServletRequest request, String id) throws Exception {
		RoleVo roleVo = roleService.getRoleById(id);
		request.setAttribute("role", roleVo);
		return "/admin/roleGrant";
	}

	/**
	 * 授权
	 * 
	 * @param role
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/grant")
	@ResponseBody
	public Json grant(RoleVo roleVo) throws Exception {
		Json j = new Json();
		roleService.grant(roleVo);
		j.setMsg("授权成功！");
		j.setSuccess(true);
		return j;
	}

}
