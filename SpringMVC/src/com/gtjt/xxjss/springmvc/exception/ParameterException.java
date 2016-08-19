/**
 * @(#)ParameterException.java 2011-12-20 Copyright 2011 it.kedacom.com, Inc.
 *                             All rights reserved.
 */

package com.gtjt.xxjss.springmvc.exception;

/**
 * @Description: 自定义异常处理,描述类..throw new ParameterException("XXXX")
 * @author Wjd
 * @date 2016年7月4日 上午11:32:45
 *
 */
public class ParameterException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParameterException() {
		super();
	}

	public ParameterException(String message) {
		super(message);
	}

	public ParameterException(Throwable cause) {
		super(cause);
	}

	public ParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
