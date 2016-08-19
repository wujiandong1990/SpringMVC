package com.gtjt.xxjss.springmvc.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.gtjt.xxjss.springmvc.common.util.HqlFilter;

/**
 * @Description: TODO(基础业务逻辑类，其他service继承此service获得基本的业务)
 * @author Wjd
 * @date 2016年6月22日 下午11:19:06
 *
 */
public interface BaseServiceI<T> {

	/**
	 * 保存一个对象
	 * @param o
	 * @return 对象的ID
	 */
	public Serializable save(T o);

	/**
	 * 删除一个对象
	 * @param o
	 */
	public void delete(T o);

	/**
	 * 更新一个对象
	 * @param o
	 */
	public void update(T o);

	/**
	 * 保存或更新一个对象
	 * @param o
	 */
	public void saveOrUpdate(T o);

	/**
	 * 通过主键获得对象
	 * @param c
	 * @param id
	 * @return 对象
	 */
	public T get(Serializable id);

	/**
	 * 通过HQL语句获取一个对象
	 * @param hql
	 * @return 对象
	 */
	public T get(String hql);

	/**
	 * 通过HQL语句获取一个对象
	 * @param hql
	 * @param params
	 * @return 对象
	 */
	public T get(String hql, Map<String, Object> params);

	/**
	 * 通过HqlFilter获取一个对象
	 * @param hqlFilter
	 * @return
	 */
	public T get(HqlFilter hqlFilter);

	/**
	 * 获得对象列表
	 * @return
	 */
	public List<T> find();

	/**
	 * 获得对象列表
	 * @param hql
	 * @return List
	 */
	public List<T> find(String hql);

	/**
	 * 获得对象列表
	 * @param hql
	 * @param params
	 * @return List
	 */
	public List<T> find(String hql, Map<String, Object> params);

	/**
	 * 获得对象列表
	 * @param hqlFilter
	 * @return
	 */
	public List<T> find(HqlFilter hqlFilter);

	/**
	 * 获得分页后的对象列表
	 * @param hql HQL语句
	 * @param page 要显示第几页
	 * @param rows 每页显示多少条
	 * @return List
	 */
	public List<T> find(String hql, int page, int rows);

	/**
	 * 获得分页后的对象列表
	 * @param hql HQL语句
	 * @param params 参数
	 * @param page 要显示第几页
	 * @param rows 每页显示多少条
	 * @return List
	 */
	public List<T> find(String hql, Map<String, Object> params, int page, int rows);

	/**
	 * 获得分页后的对象列表
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<T> find(int page, int rows);

	/**
	 * 获得分页后的对象列表
	 * @param hqlFilter
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<T> find(HqlFilter hqlFilter, int page, int rows);

	/**
	 * 统计数目
	 * @param hql HQL语句(select count(*) from T)
	 * @return long
	 */
	public Long count(String hql);

	/**
	 * 统计数目
	 * @param hql HQL语句(select count(*) from T where xx = :xx)
	 * @param params 参数
	 * @return long
	 */
	public Long count(String hql, Map<String, Object> params);

	/**
	 * 统计数目
	 * @param hqlFilter
	 * @return
	 */
	public Long count(HqlFilter hqlFilter);

	/**
	 * 统计数目
	 * @return long
	 */
	public Long count();

	/**
	 * 执行一条HQL语句
	 * @param hql
	 * @return 响应结果数目
	 */
	public int executeHql(String hql);

	/**
	 * 执行一条HQL语句
	 * @param hql
	 * @param params
	 * @return 响应结果数目
	 */
	public int executeHql(String hql, Map<String, Object> params);

	/**
	 * 获得结果集
	 * @param sql
	 * @return 结果集
	 * @throws Exception 
	 */
	public List<T> findBySql(String sql) throws Exception;

	/**
	 * 获得结果集
	 * @param sql SQL语句
	 * @param page 要显示第几页
	 * @param rows 每页显示多少条
	 * @return 结果集
	 * @throws Exception 
	 */
	public List<T> findBySql(String sql, int page, int rows) throws Exception;

	/**
	 * 获得结果集
	 * @param sql SQL语句
	 * @param params 参数
	 * @return 结果集
	 */
	public List<T> findBySql(String sql, Map<String, Object> params) throws Exception;

	/**
	 * 获得结果集
	 * @param sql SQL语句
	 * @param params 参数
	 * @param page 要显示第几页
	 * @param rows 每页显示多少条
	 * @return 结果集
	 * @throws Exception 
	 */
	public List<T> findBySql(String sql, Map<String, Object> params, int page, int rows) throws Exception;
	
	/**
	 * 获得结果集
	 * @param sql
	 * @return 结果集
	 * @throws Exception 
	 */
	public List<T> findListBySql(String sql) throws Exception;
	
	/**
	 * 获得结果集
	 * @param sql SQL语句
	 * @param page 要显示第几页
	 * @param rows 每页显示多少条
	 * @return 结果集
	 * @throws Exception 
	 */
	public List<T> findListOfPageBySql(String sql, int page, int rows) throws Exception;

	/**
	 * 执行SQL语句
	 * @param sql
	 * @return 响应行数
	 */
	public int executeSql(String sql);

	/**
	 * 执行SQL语句
	 * @param sql
	 * @param params
	 * @return 响应行数
	 */
	public int executeSql(String sql, Map<String, Object> params);

	/**
	 * 统计
	 * @param sql
	 * @return 数目
	 */
	public long countBySql(String sql);

	/**
	 * 统计
	 * @param sql
	 * @param params
	 * @return 数目
	 */
	public long countBySql(String sql, Map<String, Object> params);
}
