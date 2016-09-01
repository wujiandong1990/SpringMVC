package com.gtjt.xxjss.springmvc.dao.impl;

import org.springframework.stereotype.Repository;

import com.gtjt.xxjss.springmvc.dao.OrganizationDaoI;
import com.gtjt.xxjss.springmvc.model.Organization;

@Repository("organizationDao")
public class OrganizationDaoImpl extends BaseDaoImpl<Organization> implements OrganizationDaoI {

}
