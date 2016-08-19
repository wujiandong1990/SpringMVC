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
import com.gtjt.xxjss.springmvc.common.util.EncodeUtils;
import com.gtjt.xxjss.springmvc.dao.CommonDaoI;
import com.gtjt.xxjss.springmvc.dao.UserDaoI;
import com.gtjt.xxjss.springmvc.exception.SystemException;
import com.gtjt.xxjss.springmvc.model.Permission;
import com.gtjt.xxjss.springmvc.model.User;
import com.gtjt.xxjss.springmvc.service.PermissionServiceI;
import com.gtjt.xxjss.springmvc.service.UserServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.DataGrid;
import com.gtjt.xxjss.springmvc.web.vo.Page;
import com.gtjt.xxjss.springmvc.web.vo.UserVo;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserServiceI {

	@Autowired
	private CommonDaoI commonDao;
	
	@Autowired
	private UserDaoI userDao;
	
	@Autowired
	private PermissionServiceI permissionService;

	@Override
	public ActiveUserInfo login(String username, String password) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", EncodeUtils.MD5Hex(password.getBytes()));
		User user = userDao.get(" from User u where u.username = :username and u.password = :password", params);
		if (user == null) {
			return null;
		}
		ActiveUserInfo activeUserInfo = new ActiveUserInfo();
		activeUserInfo.setId(user.getId());
		activeUserInfo.setUsername(user.getUsername());
		activeUserInfo.setNickname(user.getNickname());
		
		List<Permission> permissions = permissionService.findPermissionsByUserId(user.getId());
		activeUserInfo.setPermissions(permissions);
		
		return activeUserInfo;
	}

	@Override
	public DataGrid getUserDataGrid(UserVo userVo, Page page) throws Exception {
		DataGrid dataGrid = new DataGrid();
		List<UserVo> userVos = new ArrayList<UserVo>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User t ";
		List<User> users = userDao.find(hql + whereHql(userVo, params) + orderHql(page), params, page.getPage(), page.getRows());
		
		if (users != null && users.size() > 0) {
			for (User user : users) {
				UserVo uv = new UserVo();
				BeanUtils.copyProperties(user, uv);
				userVos.add(uv);
			}
		}
		dataGrid.setRows(userVos);
		dataGrid.setTotal(userDao.count("select count(*) " + hql + whereHql(userVo, params), params));
		
		return dataGrid;
	}
	
	private String whereHql(UserVo userVo, Map<String, Object> params) {
		String hql = "";
		if (userVo != null) {
			hql += " where 1=1 ";
			if (userVo.getUsername() != null) {
				hql += " and t.username like :username";
				params.put("username", "%%" + userVo.getUsername() + "%%");
			}
			if (userVo.getCreatedateStart() != null) {
				hql += " and t.createdate >= :createdateStart";
				params.put("createdateStart", userVo.getCreatedateStart());
			}
			if (userVo.getCreatedateEnd() != null) {
				hql += " and t.createdate <= :createdateEnd";
				params.put("createdateEnd", userVo.getCreatedateEnd());
			}
			if (userVo.getModifydateStart() != null) {
				hql += " and t.modifydate >= :modifydateStart";
				params.put("modifydateStart", userVo.getModifydateStart());
			}
			if (userVo.getModifydateEnd() != null) {
				hql += " and t.modifydate <= :modifydateEnd";
				params.put("modifydateEnd", userVo.getModifydateEnd());
			}
		}
		return hql;
	}

	private String orderHql(Page page) {
		String orderString = "";
		if (page.getSort() != null && page.getOrder() != null) {
			orderString = " order by t." + page.getSort() + " " + page.getOrder();
		}
		return orderString;
	}

	@Override
	synchronized public void add(UserVo userVo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", userVo.getUsername());
		if (userDao.count("select count(*) from User t where t.username = :username", params) > 0) {
			throw new SystemException("用户名已存在！");
		} else {
			/**
			 * @DynamicInsert(true)
			 * 实体中某一个属性为NULL，那么在插入语句中就不包括该属性
			 * 
			 */
			User u = new User();
			BeanUtils.copyProperties(userVo, u);
			u.setPassword(EncodeUtils.MD5Hex(userVo.getPassword().getBytes()));
			userDao.save(u);
		}
	}
	
	@Override
	public void delete(String id) {
		userDao.delete(userDao.get(User.class, id));
	}

	@Override
	public UserVo getUserById(String id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String sql = "SELECT UAR.ID,UAR.USERNAME,UAR.NICKNAME,UAR.STATUS,TO_CHAR(WM_CONCAT(ROLEID)) ROLEIDS,TO_CHAR(WM_CONCAT(ROLENAME)) ROLENAMES FROM "
				+ "(SELECT U.ID,U.USERNAME,U.NICKNAME,U.STATUS,R.ID ROLEID,R.NAME ROLENAME FROM T_USER U, T_USER_ROLE UR, T_ROLE R WHERE U.ID = UR.USER_ID(+) AND UR.ROLE_ID = R.ID(+) AND "
				+ "U.ID = :id ORDER BY U.ID, R.ID) UAR GROUP BY UAR.ID,UAR.USERNAME,UAR.NICKNAME,UAR.STATUS";
		
		List<UserVo> userVos = commonDao.findBySql(sql, params, UserVo.class);

		UserVo userVo = new UserVo();
		if (userVos != null && userVos.size() > 0) {
			BeanUtils.copyProperties(userVos.get(0), userVo);
		}
		
		return userVo;
	}
	
	@Override
	synchronized public void edit(HttpSession session, UserVo userVo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", userVo.getId());
		params.put("username", userVo.getUsername());
		if (userDao.count("select count(*) from User t where t.username = :username and t.id != :id", params) > 0) {
			throw new SystemException("用户名已存在！");
		} else {
			/**
			 * @DynamicUpdate(true)
			 * 第1步是:查找出需要更新的实体;第2步是更新需要更新的属性;第3步是保存更新实体。
			 * 
			 * 要保证使用的(更新方法)要与(查找方法)和(更新属性操作)在同一个session当中
			 * 如果这3步操作没有在同一个session的管理之下，那么即便设置了@DynamicUpdate(true)注解也是不会起任何作用的
			 * 
			 */
			User u = userDao.get(User.class, userVo.getId());
			BeanUtils.copyProperties(userVo, u, new String[] { "password", "uuid", "createdate", "createuser" });
			
			u.setModifydate(new Date());
			ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
			if (activeUserInfo != null) {
				u.setModifyuser(activeUserInfo.getUsername());
			}
			/**
			 * 1.对于刚创建的一个对象，如果session中和数据库中都不存在该对象，那么该对象就是瞬时对象(Transient)
			 * 2.瞬时对象调用save方法，或者离线对象调用update方法可以使该对象变成持久化对象.如果对象是持久化对象(persistent)时，那么对该对象的任何修改，都会在提交事务时才会与之进行比较，如果不同，则发送一条update语句，否则就不会发送语句
			 * 3.离线对象(detached)就是，数据库存在该对象，但是该对象又没有被session所托管
			 * 
			 */
//			userDao.update(u);
		}
	}
	
	@Override
	public void editPwd(HttpSession session, UserVo userVo) throws Exception {
		if (userVo != null && userVo.getPassword() != null && !userVo.getPassword().trim().equalsIgnoreCase("")) {
			User u = userDao.get(User.class, userVo.getId());
			u.setPassword(EncodeUtils.MD5Hex(userVo.getPassword().getBytes()));
			u.setModifydate(new Date());
			ActiveUserInfo activeUserInfo = (ActiveUserInfo) session.getAttribute(GlobalConstants.ACTIVEUSERINFO);
			if (activeUserInfo != null) {
				u.setModifyuser(activeUserInfo.getUsername());
			}
		}
	}
	
	@Override
	public boolean editCurrentUserPwd(ActiveUserInfo activeUserInfo, String oldPwd, String pwd) throws Exception {
		User u = userDao.get(User.class, activeUserInfo.getId());
		if (u.getPassword().equalsIgnoreCase(EncodeUtils.MD5Hex(oldPwd.getBytes()))) {// 说明原密码输入正确
			u.setPassword(EncodeUtils.MD5Hex(pwd.getBytes()));
			u.setModifydate(new Date());
			return true;
		}
		return false;
	}

	@Override
	public void grant(String ids, UserVo userVo)  throws Exception {
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null && !id.equalsIgnoreCase("")) {
					//删除该用户在用户角色表中的所有对应关系
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("id", id);
					String urDelSql = "DELETE FROM T_USER_ROLE UR WHERE UR.USER_ID = :id";
					commonDao.executeSql(urDelSql, params);
					
					//在用户角色表中插入该用户拥有的所有角色
					if (userVo.getRoleIds() != null) {
						String urInsSql = "INSERT INTO T_USER_ROLE (USER_ID, ROLE_ID) ";
						boolean b = false;
						for (String roleId : userVo.getRoleIds().split(",")) {
							if (b) {
								urInsSql += " UNION ";
							} else {
								b = true;
							}
							urInsSql += "SELECT '" + id + "', '" + roleId + "' FROM DUAL";
						}
						commonDao.executeSql(urInsSql);
					}
				}
			}
		}
	}

}
