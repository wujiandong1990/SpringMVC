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
import com.gtjt.xxjss.springmvc.service.OrganizationServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.Json;
import com.gtjt.xxjss.springmvc.web.vo.OrganizationVo;
import com.gtjt.xxjss.springmvc.web.vo.Tree;

/**
 * @Description: 机构控制器
 * @author Wjd
 * @date 2016年6月23日 上午12:23:25
 *
 */
@Controller
@RequestMapping("/organization")
@Scope("prototype")
public class OrganizationController {
	
	private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Autowired
	private OrganizationServiceI organizationService;
	
	/**
	 * 跳转到机构管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager() {
		return "/admin/organization";
	}
	
	/**
	 * 获得机构列表
	 * 
	 * 通过用户ID判断，他能看到的机构
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/organizationList")
	@ResponseBody
	public List<OrganizationVo> treeGrid() throws Exception {
		return organizationService.getTreeGrid();
	}
	
	/**
	 * @Description: 获得机构树，包括所有机构类型
	 * @return
	 * @throws Exception
	 * @author Wjd
	 * @date 2016年8月2日 下午11:43:33
	 */
	@RequestMapping("/treeOrganization")
	@ResponseBody
	public List<Tree> getTreePermission() throws Exception {
		return organizationService.getTreeOrganization();
	}
	
//	@RequestMapping("/tree")
//	@ResponseBody
//	public List<Tree> getTreeMenu(HttpSession session) throws Exception {
//		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
//		List<Tree> trees = permissionService.findTreeMenuByUserId(activeUserInfo.getId());
//		return trees;
//	}
	
	/**
	 * 跳转到机构添加页面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		List<Map<String, String>> statusTypeList = (List<Map<String, String>>) EHCacheUtil.get("statusTypes");
		
		if (statusTypeList == null) {
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
		}
		request.setAttribute("statusTypes", statusTypeList);
		
		return "/admin/organizationAdd";
	}
	
	/**
	 * 添加机构
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(HttpSession session, OrganizationVo organizationVo) {
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
		if (activeUserInfo != null) {
			organizationVo.setCreateuser(activeUserInfo.getUsername());
		}
		
		Json j = new Json();
		try {
			organizationService.add(activeUserInfo, organizationVo);
			j.setSuccess(true);
			j.setMsg("增加成功！");
		} catch (Exception e) {
			logger.error("增加机构", e);
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	/**
	 * 删除机构
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) throws Exception {
		Json j = new Json();
		organizationService.delete(id);
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 跳转到机构编辑页面
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) throws Exception {
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		List<Map<String, String>> statusTypeList = (List<Map<String, String>>) EHCacheUtil.get("statusTypes");
		
		if (statusTypeList == null) {
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
		}
		request.setAttribute("statusTypes", statusTypeList);
		
		OrganizationVo organizationVo = organizationService.getOrganizationById(id);
		request.setAttribute("organization", organizationVo);
		
		return "/admin/organizationEdit";
	}

	/**
	 * 编辑机构
	 * 
	 * @param organizationVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(HttpSession session, OrganizationVo organizationVo) throws Exception {
		Json j = new Json();
		if (organizationVo.getId().equalsIgnoreCase(organizationVo.getPid())) {
			j.setSuccess(false);
			j.setMsg("上级机构不能是自身！");
		} else {
			organizationService.edit(session, organizationVo);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		}
		return j;
	}



}
