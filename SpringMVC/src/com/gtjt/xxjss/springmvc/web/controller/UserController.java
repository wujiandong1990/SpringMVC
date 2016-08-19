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

import com.alibaba.fastjson.JSON;
import com.gtjt.xxjss.springmvc.common.GlobalConstants;
import com.gtjt.xxjss.springmvc.common.util.EHCacheUtil;
import com.gtjt.xxjss.springmvc.exception.ParameterException;
import com.gtjt.xxjss.springmvc.security.interceptors.LoginInterceptor;
import com.gtjt.xxjss.springmvc.service.PermissionServiceI;
import com.gtjt.xxjss.springmvc.service.UserServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.DataGrid;
import com.gtjt.xxjss.springmvc.web.vo.Json;
import com.gtjt.xxjss.springmvc.web.vo.Page;
import com.gtjt.xxjss.springmvc.web.vo.UserVo;

/**
 * @Description: 用户控制器
 * @author Wjd
 * @date 2016年6月23日 上午12:24:17
 *
 */
@Controller
@RequestMapping("/user")
@Scope("prototype")
public class UserController {
	
	private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Autowired
	private UserServiceI userService;
	
	@Autowired
	private PermissionServiceI permissionService;

	/**
	 * @Description: 用户登录
	 * @param session
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Json login(HttpSession session, String username, String password) throws Exception {
		Json j = new Json();
		ActiveUserInfo activeUserInfo = userService.login(username, password);
		
		if (activeUserInfo != null) {
			session.setAttribute(GlobalConstants.ACTIVEUSERINFO, activeUserInfo);
			j.setSuccess(true);
			j.setMsg("登录成功！");
		} else {
			j.setSuccess(false);
			j.setMsg("用户名或密码错误！");
		}
		
		return j;
	}
	
	@RequestMapping("/manager")
	public String manager() {
		return "/admin/user";
	}
	
	/**
	 * 获取用户数据表格
	 * 
	 * @param userVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/userList") 
	@ResponseBody
	public DataGrid getUserDataGrid(UserVo userVo, Page page) throws Exception {
		return userService.getUserDataGrid(userVo, page);
	}
	
	/**
	 * 跳转到添加用户页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) throws Exception {
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		List<Map<String, String>> list = (List<Map<String, String>>) EHCacheUtil.get("statusTypes");
		if (list == null) {
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
		}
		request.setAttribute("statusTypes", list);
		
		return "/admin/userAdd";
	}
	
	/**
	 * 增加用户
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(HttpSession session, UserVo userVo) throws Exception {
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
		if (activeUserInfo != null) {
			userVo.setCreateuser(activeUserInfo.getUsername());
		}
		
		Json j = new Json();
		try {
			userService.add(userVo);
			j.setSuccess(true);
			j.setMsg("增加成功！");
			j.setObj(userVo);
		} catch (Exception e) {
			logger.error("增加用户", e);
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(HttpSession session, String id) throws Exception {
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
		Json j = new Json();
		if (id != null && !id.equalsIgnoreCase(activeUserInfo.getId())) {// 不能删除自己
			userService.delete(id);
		}
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 批量删除用户
	 * 
	 * @param ids ('0','1','2')
	 * @return
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public Json batchDelete(HttpSession session, String ids) throws Exception {
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.delete(session, id);
				}
			}
		}
		j.setMsg("批量删除成功！");
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 跳转到用户修改页面
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) throws Exception {
		UserVo userVo = userService.getUserById(id);
		request.setAttribute("user", userVo);
		
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		List<Map<String, String>> list = (List<Map<String, String>>) EHCacheUtil.get("statusTypes");
		if (list == null) {
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
		}
		request.setAttribute("statusTypes", list);
		
		return "/admin/userEdit";
	}

	/**
	 * 编辑用户
	 * 
	 * @param userVo
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(HttpSession session, UserVo userVo) throws Exception {
		Json j = new Json();
		try {
			userService.edit(session, userVo);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(userVo);
		} catch (Exception e) {
			logger.error("编辑用户", e);
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	/**
	 * 跳转到编辑用户密码页面
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPwdPage")
	public String editPwdPage(HttpServletRequest request, String id) throws Exception {
		UserVo userVo = userService.getUserById(id);
		request.setAttribute("user", userVo);
		return "/admin/userEditPwd";
	}
	
	/**
	 * 编辑用户密码
	 * 
	 * @param userVo
	 * @return
	 */
	@RequestMapping("/editPwd")
	@ResponseBody
	public Json editPwd(HttpSession session, UserVo userVo) throws Exception {
		Json j = new Json();
		userService.editPwd(session, userVo);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		return j;
	}

	/**
	 * 跳转到编辑自己的密码页面
	 * 
	 * @return
	 */
	@RequestMapping("/editCurrentUserPwdPage")
	public String editCurrentUserPwdPage() throws Exception {
		return "/user/userEditPwd";
	}

	/**
	 * 修改自己的密码
	 * 
	 * @param session
	 * @param pwd
	 * @return
	 */
	@RequestMapping("/editCurrentUserPwd")
	@ResponseBody
	public Json editCurrentUserPwd(HttpSession session, String oldPwd, String pwd) throws Exception {
		Json j = new Json();
		if (session != null) {
			ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
			if (activeUserInfo != null) {
				if (userService.editCurrentUserPwd(activeUserInfo, oldPwd, pwd)) {
					j.setSuccess(true);
					j.setMsg("编辑密码成功，下次登录生效！");
				} else {
					j.setMsg("原密码错误！");
				}
			} else {
				j.setMsg("登录超时，请重新登录！");
			}
		} else {
			j.setMsg("登录超时，请重新登录！");
		}
		return j;
	}
	
	/**
	 * 跳转到用户授权页面
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/grantPage")
	public String grantPage(HttpServletRequest request, String ids) throws Exception {
		request.setAttribute("ids", ids);
		if (ids != null && !ids.equalsIgnoreCase("") && ids.indexOf(",") == -1) {
			UserVo userVo = userService.getUserById(ids);
			request.setAttribute("user", userVo);
		}
		return "/admin/userGrant";
	}
	
	/**
	 * 用户授权
	 * 
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/grant")
	@ResponseBody
	public Json grant(String ids, UserVo userVo) throws Exception {
		Json j = new Json();
		userService.grant(ids, userVo);
		j.setSuccess(true);
		j.setMsg("授权成功！");
		return j;
	}
	
	/**
	 * 跳转到显示用户角色页面
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/currentUserRolePage")
	public String currentUserRolePage(HttpServletRequest request, HttpSession session) throws Exception {
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
		request.setAttribute("userRoles", JSON.toJSONString(userService.getUserById(activeUserInfo.getId())));
		return "/user/userRole";
	}
	
	/**
	 * 跳转到显示用户权限页面
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/currentUserResourcePage")
	public String currentUserResourcePage(HttpServletRequest request, HttpSession session) throws Exception {
		request.setAttribute("userResources", JSON.toJSONString(permissionService.getTreePermission()));
		return "/user/userResource";
	}

//	/**
//	 * 用户注册
//	 * 
//	 * @param user
//	 *            用户对象
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/reg")
//	public Json reg(User user) {
//		Json j = new Json();
//		try {
//			userService.reg(user);
//			j.setSuccess(true);
//			j.setMsg("注册成功！新注册的用户没有任何权限，请让管理员赋予权限后再使用本系统！");
//			j.setObj(user);
//		} catch (Exception e) {
//			// e.printStackTrace();
//			j.setMsg(e.getMessage());
//		}
//		return j;
//	}


//
//	/**
//	 * 用户登录时的autocomplete
//	 * 
//	 * @param q
//	 *            参数
//	 * @return
//	 */
//	@RequestMapping("/loginCombobox")
//	@ResponseBody
//	public List<User> loginCombobox(String q) {
//		return userService.loginCombobox(q);
//	}
//
//	/**
//	 * 用户登录时的combogrid
//	 * 
//	 * @param q
//	 * @param ph
//	 * @return
//	 */
//	@RequestMapping("/loginCombogrid")
//	@ResponseBody
//	public DataGrid loginCombogrid(String q, PageHelper ph) {
//		return userService.loginCombogrid(q, ph);
//	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		return "/login";
	}

	
}
