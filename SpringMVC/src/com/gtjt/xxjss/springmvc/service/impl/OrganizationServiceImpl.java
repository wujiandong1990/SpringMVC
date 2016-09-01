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
import com.gtjt.xxjss.springmvc.dao.OrganizationDaoI;
import com.gtjt.xxjss.springmvc.model.Organization;
import com.gtjt.xxjss.springmvc.service.OrganizationServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.OrganizationVo;
import com.gtjt.xxjss.springmvc.web.vo.Tree;

@Service("organizationService")
public class OrganizationServiceImpl extends BaseServiceImpl<Organization> implements OrganizationServiceI {

	@Autowired
	private CommonDaoI commonDao;
	
	@Autowired
	private OrganizationDaoI organizationDao;
	
	
	@Override
	public List<OrganizationVo> getTreeGrid() throws Exception {
		List<OrganizationVo> organizationVos = new ArrayList<OrganizationVo>();
		
		String sql = "SELECT O.*, CONNECT_BY_ISLEAF ISLEAF FROM T_ORGANIZATION O START WITH O.PID IS NULL CONNECT BY NOCYCLE PRIOR ID = O.PID ORDER SIBLINGS BY O.SEQ";
		
		List<Organization> organizations = organizationDao.findBySql(sql);
		
		if (organizations != null && organizations.size() > 0) {
			for (Organization organization : organizations) {
				OrganizationVo organizationVo = new OrganizationVo();
				BeanUtils.copyProperties(organization, organizationVo);
				organizationVo.setIconCls(organization.getIcon());
				organizationVos.add(organizationVo);
			}
		}
		
		return organizationVos;
	}
	
	@Override
	public List<Tree> getTreeOrganization() throws Exception {
		String sql = "SELECT O.*, CONNECT_BY_ISLEAF ISLEAF FROM T_ORGANIZATION O START WITH O.PID IS NULL CONNECT BY NOCYCLE PRIOR ID = O.PID ORDER SIBLINGS BY O.SEQ";
		
		List<OrganizationVo> menuOrganizationVos = commonDao.findBySql(sql, OrganizationVo.class);
		
		List<Tree> trees = new ArrayList<Tree>();
		
		if (menuOrganizationVos != null && menuOrganizationVos.size() > 0) {
			for (OrganizationVo mo : menuOrganizationVos) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(mo, tree);
				tree.setText(mo.getName());
				tree.setIconCls(mo.getIcon());
				trees.add(tree);
			}
		}
		
		return trees;
	}
	
	@Override
	public void add(ActiveUserInfo activeUserInfo, OrganizationVo organizationVo) throws Exception {
		Organization organization = new Organization();
		BeanUtils.copyProperties(organizationVo, organization);
		
		if (organizationVo.getIconCls() != null && !organizationVo.getIconCls().equalsIgnoreCase("")) {
			organization.setIcon(organizationVo.getIconCls());
		}
		
		organizationDao.save(organization);
	}

//	@Override
//	public List<Permission> findPermissionsByUserId(String userid) throws Exception {
//		return permissionDao.findPermissionsByUserId(userid);
//	}
//
//	@Override
//	public List<Tree> findTreeMenuByUserId(String userid) throws Exception {
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("userid", userid);
//		
//		String sql = "SELECT DISTINCT MP.ID,MP.NAME,MP.ICON,MP.URL,MP.PID,CONNECT_BY_ISLEAF ISLEAF,MP.SEQ,MP.STATUS,MP.PIDS "
//				+ "FROM (SELECT P.* FROM T_PERMISSION P, T_ROLE_PERMISSION RP, T_USER_ROLE UR WHERE P.ID = RP.PERMISSION_ID(+) AND UR.ROLE_ID(+) = RP.ROLE_ID "
//				+ "AND P.TYPE = 'M' AND P.STATUS = '1' AND UR.USER_ID = :userid) MP START WITH MP.PID IS NULL CONNECT BY NOCYCLE PRIOR ID = MP.PID ORDER SIBLINGS BY MP.SEQ";
//		
//		List<PermissionVo> menuPermissionVos = commonDao.findBySql(sql, params, PermissionVo.class);
//		
//		List<Tree> trees = new ArrayList<Tree>();
//		
//		if (menuPermissionVos != null && menuPermissionVos.size() > 0) {
//			for (PermissionVo mp : menuPermissionVos) {
//				Tree tree = new Tree();
//				BeanUtils.copyProperties(mp, tree);
//				tree.setText(mp.getName());
//				tree.setIconCls(mp.getIcon());
//				Map<String, Object> attr = new HashMap<String, Object>();
//				attr.put("url", mp.getUrl());
//				tree.setAttributes(attr);
//				trees.add(tree);
//			}
//		}
//		return trees;
//	}
	
	@Override
	public void delete(String id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String sql = "SELECT O.* FROM T_ORGANIZATION O START WITH O.ID = :id CONNECT BY O.PID = PRIOR ID ORDER SIBLINGS BY O.SEQ";
		List<Organization> organizations = organizationDao.findBySql(sql, params);
		
		if (organizations != null && organizations.size() > 0) {
			for (Organization o : organizations) {
				params.clear();
				params.put("organizationid", o.getId());
				
				//删除该资源
				String oSql = "DELETE FROM T_ORGANIZATION O WHERE O.ID = :organizationid";
				commonDao.executeSql(oSql, params);
			}
		}
		
	}
	
	@Override
	public OrganizationVo getOrganizationById(String id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Organization organization = organizationDao.get("from Organization o where o.id = :id", params);
		
		OrganizationVo organizationVo = new OrganizationVo();
		BeanUtils.copyProperties(organization, organizationVo);
		organizationVo.setIconCls(organization.getIcon());
		
		return organizationVo;
	}
	
	@Override
	public void edit(HttpSession session, OrganizationVo organizationVo) {
		Organization organization = organizationDao.get(Organization.class, organizationVo.getId());
		
		if (organization != null) {
			BeanUtils.copyProperties(organizationVo, organization, new String[] { "uuid", "createdate", "createuser" });
			
			organization.setIcon(organizationVo.getIconCls());
			organization.setModifydate(new Date());
			ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
			if (activeUserInfo != null) {
				organization.setModifyuser(activeUserInfo.getUsername());
			}
			
		}
	}


}
