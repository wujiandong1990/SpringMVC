package com.gtjt.xxjss.springmvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtjt.xxjss.springmvc.dao.CommonDaoI;
import com.gtjt.xxjss.springmvc.model.UploadFile;
import com.gtjt.xxjss.springmvc.service.UploadFileServiceI;

@Service("uploadFileService")
public class UploadFileServiceImpl implements UploadFileServiceI {
	
	@Autowired
	private CommonDaoI commonDao;
	
	@Override
	public void add(UploadFile uploadFile) throws Exception {
	    commonDao.sava(uploadFile);
	}

}
