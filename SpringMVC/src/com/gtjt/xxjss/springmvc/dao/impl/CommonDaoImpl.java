package com.gtjt.xxjss.springmvc.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gtjt.xxjss.springmvc.common.util.ClassTools;
import com.gtjt.xxjss.springmvc.dao.CommonDaoI;

@Repository("commonDao")
public class CommonDaoImpl implements CommonDaoI {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public <T> Serializable sava(T o) {
		if (o != null) {
			return this.getCurrentSession().save(o);
		}
		return null;
	}

	@Override
	public <T> void delete(T o) {
		if (o != null) {
			this.getCurrentSession().delete(o);
		}
	}

	@Override
	public <T> void update(T o) {
		if (o != null) {
			this.getCurrentSession().update(o);
		}
	}

	@Override
	public <T> void saveOrUpdate(T o) {
		if (o != null) {
			this.getCurrentSession().saveOrUpdate(o);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clz, Serializable id) {
		return (T) this.getCurrentSession().get(clz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String hql) {
		Query query = this.getCurrentSession().createQuery(hql);
		List<T> list = query.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String hql, Map<String, Object> params) {
		Query query = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		List<T> list = query.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String hql) {
		Query query = this.getCurrentSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String hql, Map<String, Object> params) {
		Query query = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String hql, int page, int rows) {
		Query query = this.getCurrentSession().createQuery(hql);
		return query.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		Query query = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		return query.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public Long count(String hql) {
		Query query = this.getCurrentSession().createQuery(hql);
		return (Long) query.uniqueResult();
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		Query query = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		return (Long) query.uniqueResult();
	}

	@Override
	public int executeHql(String hql) {
		Query query = this.getCurrentSession().createQuery(hql);
		return query.executeUpdate();
	}

	@Override
	public int executeHql(String hql, Map<String, Object> params) {
		Query query = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		return query.executeUpdate();
	}

	public <T> List<T> toListOfBean(List<Map<String, Object>> maps, Class<T> clz) throws Exception {
		if (maps != null && maps.size() == 0) return null;
		List<T> list = new ArrayList<T>();
		for (Map<String, Object> map : maps) {
			T t = clz.newInstance();
			ClassTools.buildMapBean(t, map);
			list.add(t);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findBySql(String sql, Class<T> clz) throws Exception {
		SQLQuery sqlQuery = (SQLQuery) this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return toListOfBean(sqlQuery.list(), clz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findBySql(String sql, Map<String, Object> params, Class<T> clz) throws Exception {
		SQLQuery sqlQuery = (SQLQuery) this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				sqlQuery.setParameter(key, params.get(key));
			}
		}
		return toListOfBean(sqlQuery.list(), clz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findBySql(String sql, int page, int rows, Class<T> clz) throws Exception {
		SQLQuery sqlQuery = (SQLQuery) this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return toListOfBean(sqlQuery.setFirstResult((page - 1) * rows).setMaxResults(rows).list(), clz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findBySql(String sql, Map<String, Object> params, int page, int rows, Class<T> clz) throws Exception {
		SQLQuery sqlQuery = (SQLQuery) this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				sqlQuery.setParameter(key, params.get(key));
			}
		}
		return toListOfBean(sqlQuery.setFirstResult((page - 1) * rows).setMaxResults(rows).list(), clz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findListBySql(String sql, Class<T> clz) throws Exception {
		return toListOfBean(this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list(), clz);
	}

	@Override
	public <T> List<T> findListOfPageBySql(String sql, int page, int rows, Class<T> clz) throws Exception {
		int begin = (page - 1) * rows + 1;
		int end = page * rows;
		String sqlText = "SELECT * FROM (SELECT T.*, ROWNUM ROWNO FROM (" + sql + ") T WHERE ROWNUM <= " + end + ") TT WHERE TT.ROWNO >= " + begin;
		return findListBySql(sqlText, clz);
	}

	@Override
	public int executeSql(String sql) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		return sqlQuery.executeUpdate();
	}

	@Override
	public int executeSql(String sql, Map<String, Object> params) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				sqlQuery.setParameter(key, params.get(key));
			}
		}
		return sqlQuery.executeUpdate();
	}

	@Override
	public long countBySql(String sql) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		return (long) ((BigDecimal) sqlQuery.uniqueResult()).intValue();
	}

	@Override
	public long countBySql(String sql, Map<String, Object> params) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				sqlQuery.setParameter(key, params.get(key));
			}
		}
		return (long) ((BigDecimal) sqlQuery.uniqueResult()).intValue();
	}
	

}
