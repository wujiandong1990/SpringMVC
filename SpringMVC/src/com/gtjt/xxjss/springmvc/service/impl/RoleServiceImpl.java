package com.gtjt.xxjss.springmvc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtjt.xxjss.springmvc.common.GlobalConstants;
import com.gtjt.xxjss.springmvc.dao.CommonDaoI;
import com.gtjt.xxjss.springmvc.dao.RoleDaoI;
import com.gtjt.xxjss.springmvc.model.Role;
import com.gtjt.xxjss.springmvc.service.RoleServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.RoleVo;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleServiceI {

	@Autowired
	private CommonDaoI commonDao;
	@Autowired
	private RoleDaoI roleDao;
	
	@Override
	public List<RoleVo> getDataGrid() throws Exception {
		String sql = "SELECT R.ID,R.NAME,R.ICON,R.DESCRIPTION,R.SEQ,R.STATUS,R.CREATEDATE,R.CREATEUSER,R.MODIFYDATE,R.MODIFYUSER, "
				+ "TO_CHAR(WM_CONCAT(P.ID)) PERMISSIONIDS,TO_CHAR(WM_CONCAT(P.NAME)) PERMISSIONNAMES FROM T_ROLE R, T_ROLE_PERMISSION RP, T_PERMISSION P "
				+ "WHERE R.ID = RP.ROLE_ID(+) AND RP.PERMISSION_ID = P.ID(+) GROUP BY R.ID,R.NAME,R.ICON,R.DESCRIPTION,R.SEQ,R.STATUS,R.CREATEDATE,R.CREATEUSER,R.MODIFYDATE,R.MODIFYUSER ORDER BY R.SEQ";
		
		List<RoleVo> roleVos = commonDao.findBySql(sql, RoleVo.class);
		
		return roleVos;
	}

	@Override
	public void add(ActiveUserInfo activeUserInfo, RoleVo roleVo) throws Exception {
		Role role = new Role();
		BeanUtils.copyProperties(roleVo, role);
		roleDao.save(role);

		// 刚刚添加的角色，赋予给当前的用户
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", activeUserInfo.getId());
		params.put("roleid", role.getId());
		String urSql = "INSERT INTO T_USER_ROLE(USER_ID, ROLE_ID) VALUES(:userid ,:roleid)";
		commonDao.executeSql(urSql, params);
	}
	
	@Override
	public void delete(String id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		//删除该角色在用户角色表中的所有对应关系
		String urSql = "DELETE FROM T_USER_ROLE UR WHERE UR.ROLE_ID = :id";
		commonDao.executeSql(urSql, params);
		//删除该角色在角色权限表中的所有对应关系
		String rpSql = "DELETE FROM T_ROLE_PERMISSION RP WHERE RP.ROLE_ID = :id";
		commonDao.executeSql(rpSql, params);
		//删除该角色
		String pSql = "DELETE FROM T_ROLE R WHERE R.ID = :id";
		commonDao.executeSql(pSql, params);
	}

	@Override
	public RoleVo getRoleById(String id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		
		String sql = "SELECT R.ID,R.NAME,R.ICON,R.DESCRIPTION,R.SEQ,R.STATUS,R.CREATEDATE,R.CREATEUSER,R.MODIFYDATE,R.MODIFYUSER, "
				+ "TO_CHAR(WM_CONCAT(P.ID)) PERMISSIONIDS,TO_CHAR(WM_CONCAT(P.NAME)) PERMISSIONNAMES FROM T_ROLE R, T_ROLE_PERMISSION RP, T_PERMISSION P "
				+ "WHERE R.ID = RP.ROLE_ID(+) AND RP.PERMISSION_ID = P.ID(+) AND R.ID = :id GROUP BY R.ID,R.NAME,R.ICON,R.DESCRIPTION,R.SEQ,R.STATUS,R.CREATEDATE,R.CREATEUSER,R.MODIFYDATE,R.MODIFYUSER ORDER BY R.SEQ";
		
		List<RoleVo> roleVos = commonDao.findBySql(sql, params, RoleVo.class);
		
		RoleVo roleVo = new RoleVo();
		if (roleVos != null && roleVos.size() > 0) {
			BeanUtils.copyProperties(roleVos.get(0), roleVo);
		}
		return roleVo;
	}

	@Override
	public void edit(HttpSession session, RoleVo roleVo) throws Exception {
		Role role = roleDao.get(Role.class, roleVo.getId());
		
		if (role != null) {
			BeanUtils.copyProperties(roleVo, role, new String[] { "uuid", "createdate", "createuser" });
			
			role.setModifydate(new Date());
			ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
			if (activeUserInfo != null) {
				role.setModifyuser(activeUserInfo.getUsername());
			}
		}
	}

	@Override
	public void grant(RoleVo roleVo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", roleVo.getId());
		//删除该角色在角色权限表中的所有对应关系
		String rpDelSql = "DELETE FROM T_ROLE_PERMISSION RP WHERE RP.ROLE_ID = :id";
		commonDao.executeSql(rpDelSql, params);
		
		//赋予权限
		if (roleVo.getPermissionIds() != null && !roleVo.getPermissionIds().equalsIgnoreCase("")) {
			//在角色权限表中插入该角色拥有的所有权限
			String rpInsSql = "INSERT INTO T_ROLE_PERMISSION (ROLE_ID, PERMISSION_ID) ";
			String[] permissionIds = roleVo.getPermissionIds().split(",");
			boolean b = false;
			
			for (String permissionid : permissionIds) {
				if (b) {
					rpInsSql += " UNION ";
				} else {
					b = true;
				}
				rpInsSql += "SELECT '" + roleVo.getId() + "', '" + permissionid + "' FROM DUAL";
			}
			
			commonDao.executeSql(rpInsSql);
		}
	}

}
