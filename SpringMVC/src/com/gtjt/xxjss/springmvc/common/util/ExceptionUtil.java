package com.gtjt.xxjss.springmvc.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 异常工具类
 * 
 * @author Wjd
 * 
 */
public class ExceptionUtil {
	
	/**
	 * 获取异常的堆栈信息
	 * 
	 * @param e
	 * @return
	 */
	public static String getStackTrace(Throwable e) {
		String expMessage = null;
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			e.printStackTrace(new java.io.PrintWriter(buf, true));
			expMessage = buf.toString();
			buf.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return expMessage;
	}	


}
