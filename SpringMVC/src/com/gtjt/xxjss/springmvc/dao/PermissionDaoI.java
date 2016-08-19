package com.gtjt.xxjss.springmvc.dao;

import java.util.List;

import com.gtjt.xxjss.springmvc.model.Permission;

public interface PermissionDaoI extends BaseDaoI<Permission> {

	/**
	 * @Description: 获取用户的所有权限
	 * @param userid
	 * @return
	 * @throws Exception
	 * @author Wjd
	 * @date 2016年6月23日 上午11:45:41
	 */
	public List<Permission> findPermissionsByUserId(String userid) throws Exception;
	
}
