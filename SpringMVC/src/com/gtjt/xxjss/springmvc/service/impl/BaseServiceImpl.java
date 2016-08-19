package com.gtjt.xxjss.springmvc.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtjt.xxjss.springmvc.common.util.HqlFilter;
import com.gtjt.xxjss.springmvc.dao.BaseDaoI;
import com.gtjt.xxjss.springmvc.service.BaseServiceI;

/**
 * @Description: 基础业务逻辑
 * @author Wjd
 * @date 2016年6月22日 下午11:19:23
 *
 */
@Service("baseService")
public class BaseServiceImpl<T> implements BaseServiceI<T> {
	
	@Autowired
	private BaseDaoI<T> baseDao;

	@Override
	public Serializable save(T o) {
		return baseDao.save(o);
	}

	@Override
	public void delete(T o) {
		baseDao.delete(o);
	}

	@Override
	public void update(T o) {
		baseDao.update(o);
	}

	@Override
	public void saveOrUpdate(T o) {
		baseDao.saveOrUpdate(o);
	}

	@Override
	public T get(Serializable id) {
		@SuppressWarnings("unchecked")
		Class<T> clz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return baseDao.get(clz, id);
	}

	@Override
	public T get(String hql) {
		return baseDao.get(hql);
	}

	@Override
	public T get(String hql, Map<String, Object> params) {
		return baseDao.get(hql, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(HqlFilter hqlFilter) {
		String className = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
		String hql = "select distinct t from " + className + " t";
		return get(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
	}

	@Override
	public List<T> find() {
		return find(new HqlFilter());
	}

	@Override
	public List<T> find(String hql) {
		return baseDao.find(hql);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		return baseDao.find(hql, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(HqlFilter hqlFilter) {
		String className = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
		String hql = "select distinct t from " + className + " t";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
	}

	@Override
	public List<T> find(String hql, int page, int rows) {
		return baseDao.find(hql, page, rows);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		return baseDao.find(hql, params, page, rows);
	}

	@Override
	public List<T> find(int page, int rows) {
		return find(new HqlFilter(), page, rows);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(HqlFilter hqlFilter, int page, int rows) {
		String className = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
		String hql = "select distinct t from " + className + " t";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams(), page, rows);
	}

	@Override
	public Long count(String hql) {
		return baseDao.count(hql);
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		return baseDao.count(hql, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long count(HqlFilter hqlFilter) {
		String className = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
		String hql = "select count(distinct t) from " + className + " t";
		return count(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
	}

	@Override
	public Long count() {
		return count(new HqlFilter());
	}

	@Override
	public int executeHql(String hql) {
		return baseDao.executeHql(hql);
	}

	@Override
	public int executeHql(String hql, Map<String, Object> params) {
		return baseDao.executeHql(hql, params);
	}

	@Override
	public List<T> findBySql(String sql) throws Exception {
		return baseDao.findBySql(sql);
	}

	@Override
	public List<T> findBySql(String sql, int page, int rows) throws Exception {
		return baseDao.findBySql(sql, page, rows);
	}

	@Override
	public List<T> findBySql(String sql, Map<String, Object> params) throws Exception {
		return baseDao.findBySql(sql, params);
	}

	@Override
	public List<T> findBySql(String sql, Map<String, Object> params, int page, int rows) throws Exception {
		return baseDao.findBySql(sql, params, page, rows);
	}
	
	@Override
	public List<T> findListBySql(String sql) throws Exception {
		return baseDao.findListBySql(sql);
	}

	@Override
	public List<T> findListOfPageBySql(String sql, int page, int rows) throws Exception {
		return baseDao.findListOfPageBySql(sql, page, rows);
	}

	@Override
	public int executeSql(String sql) {
		return baseDao.executeSql(sql);
	}

	@Override
	public int executeSql(String sql, Map<String, Object> params) {
		return baseDao.executeSql(sql, params);
	}

	@Override
	public long countBySql(String sql) {
		return baseDao.countBySql(sql);
	}

	@Override
	public long countBySql(String sql, Map<String, Object> params) {
		return baseDao.countBySql(sql, params);
	}


}
