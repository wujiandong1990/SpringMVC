package com.gtjt.xxjss.springmvc.service;

import javax.servlet.http.HttpServletRequest;

import com.gtjt.xxjss.springmvc.model.Log;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;

public interface LogServiceI {
	
	/**
	 * @Description: 保存日志
	 * @author Wjd
	 * @date 2016年8月25日 下午2:59:43
	 */
	public void add(HttpServletRequest request, ActiveUserInfo activeUserInfo, Log log) throws Exception;
	
}
