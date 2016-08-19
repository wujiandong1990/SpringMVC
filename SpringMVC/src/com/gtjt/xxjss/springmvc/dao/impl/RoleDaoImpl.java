package com.gtjt.xxjss.springmvc.dao.impl;

import org.springframework.stereotype.Repository;

import com.gtjt.xxjss.springmvc.dao.RoleDaoI;
import com.gtjt.xxjss.springmvc.model.Role;

@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDaoI {

}
