package com.gtjt.xxjss.springmvc.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * @author bruno zhang
 *
 */
public final class IpUtils {
	
	/**
	 * 根据http header来精准获取客户端ip地址值
	 * @param request
	 * @return
	 */
	public static String getRealRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		int index = ip.indexOf(",");
		if (index > -1) {
			ip = ip.substring(0, index);
		}
		return ip;
	}
}
