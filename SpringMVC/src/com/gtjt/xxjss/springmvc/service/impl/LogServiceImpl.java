package com.gtjt.xxjss.springmvc.service.impl;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gtjt.xxjss.springmvc.common.util.IpUtils;
import com.gtjt.xxjss.springmvc.dao.CommonDaoI;
import com.gtjt.xxjss.springmvc.model.Log;
import com.gtjt.xxjss.springmvc.service.LogServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;

@Service("logService")
public class LogServiceImpl implements LogServiceI {
	
	@Autowired
	private CommonDaoI commonDao;

	@Override
	synchronized public void add(HttpServletRequest request, ActiveUserInfo activeUserInfo, Log log) throws Exception {
		
		if (activeUserInfo != null) {
			log.setOperuser(activeUserInfo.getUsername());
			log.setIp(IpUtils.getRealRemoteAddr(request));
		}
		String browser = request.getHeader("User-Agent");
	    if (browser == null) {
	      browser = "无法获取浏览器信息";
	    }
	    log.setBrowser(browser);
	    log.setCreatedate(new Date());
	    
	    commonDao.sava(log);
	}

}
