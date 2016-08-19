package com.gtjt.xxjss.springmvc.security.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gtjt.xxjss.springmvc.common.util.EHCacheUtil;
import com.gtjt.xxjss.springmvc.common.util.ResourcesUtil;
import com.gtjt.xxjss.springmvc.exception.ParameterException;
import com.gtjt.xxjss.springmvc.model.Permission;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;

/**
 * 
 * <p>Title: HandlerInterceptor1</p>
 * <p>Description: 授权拦截器</p>
 * <p>Company: www.itcast.com</p> 
 * @author	传智.燕青
 * @date	2015-3-22下午4:11:44
 * @version 1.0
 */
public class PermissionInterceptor implements HandlerInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);
	
	//在执行handler之前来执行的
	//用于用户认证校验、用户权限校验
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		logger.debug("PermissionInterceptor...preHandle");
		
		//得到请求的url
		String url = request.getServletPath();
		
		//判断是否是公开 地址
		//实际开发中需要公开 地址配置在配置文件中
		//从配置中取逆名访问url
//		List<String> open_urls = ResourcesUtil.gekeyList("anonymousURL");
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		List<String> open_urls = (List<String>) EHCacheUtil.get("open_urls");
		if (open_urls == null) {
			open_urls = ResourcesUtil.gekeyList("anonymousURL");
			EHCacheUtil.put("open_urls", open_urls);
		}
		//遍历公开 地址，如果是公开 地址则放行
//		for(String open_url : open_urls) {
//			if(url.indexOf(open_url) >= 0) {
//				//如果是公开 地址则放行
//				return true;
//			}
//		}
		if (open_urls != null && open_urls.contains(url)) {
			return true;
		}
		
		//从配置文件中获取公共访问地址
//		List<String> common_urls = ResourcesUtil.gekeyList("commonURL");
		List<String> common_urls = (List<String>) EHCacheUtil.get("common_urls");
		if (common_urls == null) {
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
		}
		//遍历公用 地址，如果是公用 地址则放行
//		for(String common_url : common_urls) {
//			if(url.indexOf(common_url) >= 0) {
//				//如果是公开 地址则放行
//				return true;
//			}
//		}
		if (common_urls != null && common_urls.contains(url)) {
			return true;
		}
		
		//获取session
		HttpSession session = request.getSession();
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute("activeUserInfo");
		//从session中取权限范围的url
		List<Permission> permissions = activeUserInfo.getPermissions();
		for(Permission permission : permissions) {
			//权限的url
			if(url.equals(permission.getUrl())) {
				//如果是权限的url 地址则放行
				return true;
			}
		}
		
		request.setAttribute("msg", "拒绝访问！您没有[" + url + "]的访问权限！");
		//执行到这里拦截，跳转到无权访问的提示页面
		request.getRequestDispatcher("/WEB-INF/jsp/error/noSecurity.jsp").forward(request, response);
		
		//如果返回false表示拦截不继续执行handler，如果返回true表示放行
		return false;
	}
	//在执行handler返回modelAndView之前来执行
	//如果需要向页面提供一些公用 的数据或配置一些视图信息，使用此方法实现 从modelAndView入手
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		logger.debug("PermissionInterceptor...postHandle");
		
	}
	//执行handler之后执行此方法
	//作系统 统一异常处理，进行方法执行性能监控，在preHandle中设置一个时间点，在afterCompletion设置一个时间，两个时间点的差就是执行时长
	//实现 系统 统一日志记录
	@Override
	public void afterCompletion(HttpServletRequest request,	HttpServletResponse response, Object handler, Exception ex) throws Exception {
		logger.debug("PermissionInterceptor...afterCompletion");
	}

}
