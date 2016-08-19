package com.gtjt.xxjss.springmvc.dao.impl;

import org.springframework.stereotype.Repository;

import com.gtjt.xxjss.springmvc.dao.UserDaoI;
import com.gtjt.xxjss.springmvc.model.User;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDaoI {

}
