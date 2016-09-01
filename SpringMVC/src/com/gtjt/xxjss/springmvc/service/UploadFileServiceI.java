package com.gtjt.xxjss.springmvc.service;

import com.gtjt.xxjss.springmvc.model.UploadFile;

public interface UploadFileServiceI {
	
	/**
	 * @Description: 保存上传文件信息
	 * @author Wjd
	 * @date 2016年8月25日 下午2:59:43
	 */
	public void add(UploadFile uploadFile) throws Exception;
	
}
