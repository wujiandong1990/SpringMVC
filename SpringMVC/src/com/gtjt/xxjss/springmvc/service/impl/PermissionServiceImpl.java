package com.gtjt.xxjss.springmvc.service.impl;

import java.util.ArrayList;
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
import com.gtjt.xxjss.springmvc.dao.PermissionDaoI;
import com.gtjt.xxjss.springmvc.model.Permission;
import com.gtjt.xxjss.springmvc.service.PermissionServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.PermissionVo;
import com.gtjt.xxjss.springmvc.web.vo.Tree;
import com.gtjt.xxjss.springmvc.web.vo.UserRoleVo;

@Service("permissionService")
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionServiceI {

	@Autowired
	private CommonDaoI commonDao;
	@Autowired
	private PermissionDaoI permissionDao;
	

	@Override
	public List<Permission> findPermissionsByUserId(String userid) throws Exception {
		return permissionDao.findPermissionsByUserId(userid);
	}
	
	@Override
	public boolean hasPermission(String userid, String permission) throws Exception {
		List<Permission> permissions = permissionDao.findPermissionsByUserId(userid);
		if (permissions != null && permissions.size() > 0) {
			for (Permission p : permissions) {
				if (permission.equals(p.getUrl())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<Tree> findTreeMenuByUserId(String userid) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		
		String sql = "SELECT DISTINCT MP.ID,MP.NAME,MP.ICON,MP.URL,MP.PID,CONNECT_BY_ISLEAF ISLEAF,MP.SEQ,MP.STATUS,MP.PIDS "
				+ "FROM (SELECT P.* FROM T_PERMISSION P, T_ROLE_PERMISSION RP, T_USER_ROLE UR WHERE P.ID = RP.PERMISSION_ID(+) AND UR.ROLE_ID(+) = RP.ROLE_ID "
				+ "AND P.TYPE = 'M' AND P.STATUS = '1' AND UR.USER_ID = :userid) MP START WITH MP.PID IS NULL CONNECT BY NOCYCLE PRIOR ID = MP.PID ORDER SIBLINGS BY MP.SEQ";
		
		List<PermissionVo> menuPermissionVos = commonDao.findBySql(sql, params, PermissionVo.class);
		
		List<Tree> trees = new ArrayList<Tree>();
		
		if (menuPermissionVos != null && menuPermissionVos.size() > 0) {
			for (PermissionVo mp : menuPermissionVos) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(mp, tree);
				tree.setText(mp.getName());
				tree.setIconCls(mp.getIcon());
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("url", mp.getUrl());
				tree.setAttributes(attr);
				trees.add(tree);
			}
		}
		return trees;
	}
	
	@Override
	public List<Tree> getTreePermission() throws Exception {
		String sql = "SELECT P.*, CONNECT_BY_ISLEAF ISLEAF FROM T_PERMISSION P START WITH P.PID IS NULL CONNECT BY NOCYCLE PRIOR ID = P.PID ORDER SIBLINGS BY P.SEQ";
		
		List<PermissionVo> menuPermissionVos = commonDao.findBySql(sql, PermissionVo.class);
		
		List<Tree> trees = new ArrayList<Tree>();
		
		if (menuPermissionVos != null && menuPermissionVos.size() > 0) {
			for (PermissionVo mp : menuPermissionVos) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(mp, tree);
				tree.setText(mp.getName());
				tree.setIconCls(mp.getIcon());
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("url", mp.getUrl());
				tree.setAttributes(attr);
				trees.add(tree);
			}
		}
		return trees;
	}

	
	@Override
	public List<PermissionVo> getTreeGrid() throws Exception {
		List<PermissionVo> permissionVos = new ArrayList<PermissionVo>();
		
		String sql = "SELECT P.*, CONNECT_BY_ISLEAF ISLEAF FROM T_PERMISSION P START WITH P.PID IS NULL CONNECT BY NOCYCLE PRIOR ID = P.PID ORDER SIBLINGS BY P.SEQ";
		
		List<Permission> permissions = permissionDao.findBySql(sql);
		
		for (Permission permission : permissions) {
			PermissionVo permissionVo = new PermissionVo();
			BeanUtils.copyProperties(permission, permissionVo);
			permissionVo.setIconCls(permission.getIcon());
			permissionVos.add(permissionVo);
		}
		
		return permissionVos;
	}

	@Override
	public void add(ActiveUserInfo activeUserInfo, PermissionVo permissionVo) throws Exception {
		Permission permission = new Permission();
		BeanUtils.copyProperties(permissionVo, permission);
		
		if (permissionVo.getPid() != null && !permissionVo.getPid().equalsIgnoreCase("")) {
			permission.setPid(permissionVo.getPid());
		}
		if (permissionVo.getType() != null && !permissionVo.getType().equalsIgnoreCase("")) {
			permission.setType(permissionVo.getType());
		}
		if (permissionVo.getIconCls() != null && !permissionVo.getIconCls().equalsIgnoreCase("")) {
			permission.setIcon(permissionVo.getIconCls());
		}
		permissionDao.save(permission);

		//由于当前用户所属的角色，没有访问新添加的资源权限，所以在新添加资源的时候，将当前资源授权给当前用户的所有角色，以便添加资源后在资源列表中能够找到
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", activeUserInfo.getId());
		String urSql = "SELECT UR.USER_ID USERID,UR.ROLE_ID ROLEID FROM T_USER_ROLE UR WHERE UR.USER_ID = :id";
		List<UserRoleVo> userRoleVos = commonDao.findBySql(urSql, params, UserRoleVo.class);
		
		for (UserRoleVo userRoleVo : userRoleVos) {
			params.clear();
			params.put("roleid", userRoleVo.getRoleid());
			params.put("permissionid", permission.getId());
			String rpSql = "INSERT INTO T_ROLE_PERMISSION(ROLE_ID, PERMISSION_ID) VALUES(:roleid, :permissionid)";
			commonDao.executeSql(rpSql, params);
		}
	}
	
	@Override
	public void delete(String id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String sql = "SELECT P.* FROM T_PERMISSION P START WITH P.ID = :id CONNECT BY P.PID = PRIOR ID ORDER SIBLINGS BY P.SEQ";
		List<Permission> permissions = permissionDao.findBySql(sql, params);
		
		if (permissions != null && permissions.size() > 0) {
			for (Permission p : permissions) {
				params.clear();
				params.put("permissionid", p.getId());
				
				//删除该权限在角色权限表中的所有的对应关系
				String rpSql = "DELETE FROM T_ROLE_PERMISSION RP WHERE RP.PERMISSION_ID = :permissionid";
				commonDao.executeSql(rpSql, params);
				//删除该权限
				String pSql = "DELETE FROM T_PERMISSION P WHERE P.ID = :permissionid";
				commonDao.executeSql(pSql, params);
			}
		}
	}
	
	@Override
	public PermissionVo getPermissionById(String id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Permission permission = permissionDao.get("from Permission p where p.id = :id", params);
		
		PermissionVo permissionVo = new PermissionVo();
		BeanUtils.copyProperties(permission, permissionVo);
		permissionVo.setIconCls(permission.getIcon());
		
		return permissionVo;
	}
	
	@Override
	public void edit(HttpSession session, PermissionVo permissionVo) {
		Permission permission = permissionDao.get(Permission.class, permissionVo.getId());
		
		if (permission != null) {
			BeanUtils.copyProperties(permissionVo, permission, new String[] { "uuid", "createdate", "createuser" });
			
			permission.setIcon(permissionVo.getIconCls());
			permission.setModifydate(new Date());
			ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
			if (activeUserInfo != null) {
				permission.setModifyuser(activeUserInfo.getUsername());
			}
			
		}
	}


}
