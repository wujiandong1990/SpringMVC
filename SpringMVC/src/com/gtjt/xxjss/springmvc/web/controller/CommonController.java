package com.gtjt.xxjss.springmvc.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 公共控制器
 * @author Wjd
 * @date 2016年6月26日 上午11:29:40
 *
 */
@Controller
@RequestMapping("/")
@Scope("prototype")
public class CommonController {

	/**
	 * 跳转到主界面
	 */
	@RequestMapping("/index")
	public String index() {
		return "/index";
	}

	/**
	 * 跳转到主界面上部
	 */
	@RequestMapping("/layoutNorth")
	public String layoutNorth() {
		return "/layout/north";
	}
	
	/**
	 * 跳转到主界面左侧
	 */
	@RequestMapping("/layoutWest")
	public String layoutWset() {
		return "/layout/west";
	}
	
	/**
	 * 跳转到主界面底部
	 */
	@RequestMapping("/layoutSouth")
	public String layoutSouth() {
		return "/layout/south";
	}
	
	/**
	 * 跳转到主界面首页
	 */
	@RequestMapping("/portalIndex")
	public String portalIndex() {
		return "/portal/index";
	}
	
	
	/**
	 * 跳转到主界面首页的链接部分
	 */
	@RequestMapping("/portalLink")
	public String portalLink() {
		return "/portal/link";
	}
	
	/**
	 * 跳转到主界面首页的修复部分
	 */
	@RequestMapping("/portalRepair")
	public String portalRepair() {
		return "/portal/repair";
	}
	
	/**
	 * 跳转到主界面首页的关于部分
	 */
	@RequestMapping("/portalAbout")
	public String portalAbout() {
		return "/portal/about";
	}
	
	/**
	 * 跳转到主界面首页的顺序部分
	 */
	@RequestMapping("/portalSeq")
	public String portalSeq() {
		return "/portal/seq";
	}
	
}
