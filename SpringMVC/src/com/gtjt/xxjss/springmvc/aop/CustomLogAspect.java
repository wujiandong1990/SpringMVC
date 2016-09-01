package com.gtjt.xxjss.springmvc.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gtjt.xxjss.springmvc.annotation.CustomLog;
import com.gtjt.xxjss.springmvc.common.GlobalConstants;
import com.gtjt.xxjss.springmvc.common.util.ExceptionUtil;
import com.gtjt.xxjss.springmvc.common.util.StringUtil;
import com.gtjt.xxjss.springmvc.model.Log;
import com.gtjt.xxjss.springmvc.service.LogServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;

/**
 * @Description: 切点类
 * @author Wjd
 * @date 2016年8月25日 下午3:26:06
 */
@Aspect
@Component
public class CustomLogAspect {

	private static final Logger logger = LoggerFactory.getLogger(CustomLogAspect.class);

	@Autowired
	private LogServiceI logService;

	/**
	 * @Description: Controller层切点
	 * @author Wjd
	 * @date 2016年8月25日 下午3:56:40
	 */
	@Pointcut("@annotation(com.gtjt.xxjss.springmvc.annotation.CustomLog)")
	public void controllerAspect() {}

	/**
	 * @Description: 前置通知 用于拦截Controller层记录用户的操作
	 * @author Wjd
	 * @date 2016年8月25日 下午3:56:15
	 */
	@Around("controllerAspect()")
	public Object doController(ProceedingJoinPoint point) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) request.getSession().getAttribute(GlobalConstants.ACTIVEUSERINFO);

		Object result = null;
		Map<String, Object> map = null;
		Log log = new Log();

		Long start = 0L;
		Long end = 0L;
		Long time = 0L;

		try {
			map = getControllerMethodDescription(point);
			// 执行方法所消耗的时间
			start = System.currentTimeMillis();
			result = point.proceed();
			end = System.currentTimeMillis();
			time = end - start;
		} catch (Throwable e) {
			throw new RuntimeException(ExceptionUtil.getStackTrace(e));
		}

		try {
			String url = map.get("url").toString();
			if (StringUtil.isEmpty(url)) url = request.getServletPath();
			log.setUrl(url);
			
			String desc = map.get("description").toString();
			if (StringUtil.isEmpty(desc)) {
				desc = "执行成功!";
			} else {
				desc += ":执行成功!";
			}
			log.setDescription(desc);
			
			log.setRuntime(time.toString());
			log.setType(GlobalConstants.LOGTYPE_USR_OPR);

			logService.add(request, activeUserInfo, log);
		} catch (Exception e) {
			logger.error(e.getMessage());// 记录本地异常日志
		}
		return result;
	}

	/**
	 * @Description: 操作异常记录
	 * @author Wjd
	 * @date 2016年8月25日 下午3:47:40
	 */
	@AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint point, Throwable e) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		ActiveUserInfo activeUserInfo = (ActiveUserInfo) request.getSession().getAttribute(GlobalConstants.ACTIVEUSERINFO);
		Map<String, Object> map = null;
		Log log = new Log();

		try {
			map = getControllerMethodDescription(point);
			
			String url = map.get("url").toString();
			if (StringUtil.isEmpty(url)) url = request.getServletPath();
			log.setUrl(url);
			
			log.setDescription(e.toString());
			log.setRuntime("0");
			log.setType(GlobalConstants.LOGTYPE_SYS_ERR);

			logService.add(request, activeUserInfo, log);
		} catch (Exception ex) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * @Description: 获取注解中对方法的描述信息 用于Controller层注解
	 * @author Wjd
	 * @date 2016年8月25日 下午3:55:02
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					map.put("url", method.getAnnotation(CustomLog.class).url());
					map.put("description", method.getAnnotation(CustomLog.class).description());					
					break;
				}
			}
		}
		return map;
	}
}
