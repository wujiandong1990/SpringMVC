package com.gtjt.xxjss.springmvc.security.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gtjt.xxjss.springmvc.common.GlobalConstants;
import com.gtjt.xxjss.springmvc.common.util.EHCacheUtil;
import com.gtjt.xxjss.springmvc.exception.ParameterException;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;

public class LoginInterceptor implements HandlerInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	//在执行handler之前来执行的
	//用于用户认证校验、用户权限校验
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		logger.debug("LoginInterceptor...preHandle");
		
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
			throw new ParameterException("系统启动过程中参数初始化到缓存时发生异常！");
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
		
		//判断用户身份在session中是否存在
		HttpSession session = request.getSession();
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
		//如果用户身份在session中存在放行
		if(activeUserInfo != null) {
			return true;
		}
		
		//执行到这里拦截，跳转到登陆页面，用户进行身份认证
		request.getRequestDispatcher("/WEB-INF/jsp/relogin.jsp").forward(request, response);
		
		//如果返回false表示拦截不继续执行handler，如果返回true表示放行
		return false;
	}
	
	//在执行handler返回modelAndView之前来执行
	//如果需要向页面提供一些公用 的数据或配置一些视图信息，使用此方法实现 从modelAndView入手
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		logger.debug("LoginInterceptor...postHandle");//logger.info(...) logger.warn(...) logger.error(..., e);
	}
	
	//执行handler之后执行此方法
	//作系统 统一异常处理，进行方法执行性能监控，在preHandle中设置一个时间点，在afterCompletion设置一个时间，两个时间点的差就是执行时长
	//实现 系统 统一日志记录
	@Override
	public void afterCompletion(HttpServletRequest request,	HttpServletResponse response, Object handler, Exception ex)	throws Exception {
		logger.debug("LoginInterceptor...afterCompletion");
	}

}
