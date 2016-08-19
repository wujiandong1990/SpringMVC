package com.gtjt.xxjss.springmvc.service;

import javax.servlet.http.HttpSession;

import com.gtjt.xxjss.springmvc.model.User;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;
import com.gtjt.xxjss.springmvc.web.vo.DataGrid;
import com.gtjt.xxjss.springmvc.web.vo.Page;
import com.gtjt.xxjss.springmvc.web.vo.UserVo;


/**
 * @Description: 用户Service
 * @author Wjd
 * @date 2016年6月22日 下午11:18:04
 *
 */
public interface UserServiceI extends BaseServiceI<User> {

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public ActiveUserInfo login(String username, String password) throws Exception;
	
	/**
	 * 获得用户对象
	 * 
	 * @param id
	 * @return
	 */
	public UserVo getUserById(String id) throws Exception;
	
	/**
	 * 获取用户数据表格
	 * 
	 * @param user
	 * @return
	 */
	public DataGrid getUserDataGrid(UserVo user, Page page) throws Exception;
	
	/**
	 * 增加用户
	 * @param userVo
	 */
	public void add(UserVo userVo) throws Exception;
	
	/**
	 * 删除用户
	 * 
	 * @param id
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 编辑用户
	 * @param session 
	 * 
	 * @param userVo
	 */
	public void edit(HttpSession session, UserVo userVo) throws Exception;
	
	/**
	 * 编辑用户密码
	 * 
	 * @param userVo
	 */
	public void editPwd(HttpSession session, UserVo userVo) throws Exception;
	
	/**
	 * 修改用户自己的密码
	 * 
	 * @param activeUserInfo
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	public boolean editCurrentUserPwd(ActiveUserInfo activeUserInfo, String oldPwd, String pwd) throws Exception;
	
	
	/**
	 * 用户授权
	 * 
	 * @param ids
	 * @param userVo
	 *            需要user.roleIds的属性值
	 */
	public void grant(String ids, UserVo userVo) throws Exception;
	
//	/**
//	 * 用户注册
//	 * 
//	 * @param user
//	 *            里面包含登录名和密码
//	 * @throws Exception
//	 */
//	public void reg(User user) throws Exception;


//
//	/**
//	 * 获得用户能访问的资源地址
//	 * 
//	 * @param id
//	 *            用户ID
//	 * @return
//	 */
//	public List<String> resourceList(String id);


//
//	/**
//	 * 用户登录时的autocomplete
//	 * 
//	 * @param q
//	 *            参数
//	 * @return
//	 */
//	public List<User> loginCombobox(String q);
//
//	/**
//	 * 用户登录时的combogrid
//	 * 
//	 * @param q
//	 * @param ph
//	 * @return
//	 */
//	public DataGrid loginCombogrid(String q, PageHelper ph);
//
//	/**
//	 * 用户创建时间图表
//	 * 
//	 * @return
//	 */
//	public List<Long> userCreateDatetimeChart();

}
