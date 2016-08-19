package com.gtjt.xxjss.springmvc.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.gtjt.xxjss.springmvc.dao.PermissionDaoI;
import com.gtjt.xxjss.springmvc.model.Permission;

@Repository("permissionDao")
public class PermissionDaoImpl extends BaseDaoImpl<Permission> implements PermissionDaoI {

	@Override
	@Cacheable(value="generalCache", key="'p' + #userid")
	public List<Permission> findPermissionsByUserId(String userid) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		
		String sql = "SELECT DISTINCT P.* FROM T_PERMISSION P, T_ROLE_PERMISSION RP, T_USER_ROLE UR "
				+ "WHERE P.ID = RP.PERMISSION_ID(+) AND UR.ROLE_ID(+) = RP.ROLE_ID AND UR.USER_ID = :userid";
		
		List<Permission> permissions = this.findBySql(sql, params);
		
		return permissions;
	}

}
