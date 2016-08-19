package com.gtjt.xxjss.springmvc.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据源控制器
 * 
 * @author 孙宇
 * 
 */
@Controller
@RequestMapping("/druidController")
@Scope("prototype")
public class DruidController {

	/**
	 * 转向到数据源监控页面
	 * 
	 * @return
	 */
	@RequestMapping("/druid")
	public String druid() {
		return "redirect:/druid/index.html";
	}

}
