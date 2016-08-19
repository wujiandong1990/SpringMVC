package com.gtjt.xxjss.springmvc.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.gtjt.xxjss.springmvc.service.PermissionServiceI;

@Component
public class RBACSecurityFunctions {
	
//	private static PermissionServiceI permissionService;
	
	//这个方法不能定义为static，因为这样会导致spring无法注入
//	public void setPermissionService(PermissionServiceI permissionService) {
//		RBACSecurityFunctions.permissionService = permissionService;
//	}
	
	@Autowired
	private PermissionServiceI permissionService;
	
	private static RBACSecurityFunctions rbac;
	
	@PostConstruct  
	public void init() {
	  rbac = this;  
	  rbac.permissionService = this.permissionService;  
	}
	
	public static boolean hasPermission(String userid, String permission) throws Exception {
		return rbac.permissionService.hasPermission(userid, permission);
	}

	
}
