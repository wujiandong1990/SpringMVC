package com.gtjt.xxjss.springmvc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.gtjt.xxjss.springmvc.common.util.EHCacheUtil;
import com.gtjt.xxjss.springmvc.common.util.ResourcesUtil;
import com.gtjt.xxjss.springmvc.common.util.XmlUtil;
import com.gtjt.xxjss.springmvc.service.ApplicationInitiServiceI;

@Service("applicationInitiService")
public class ApplicationInitiServiceImpl implements ApplicationInitiServiceI, ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
            init();
        }
	}
	
	/**
	 * @Description: 初始化系统参数到缓存
	 * 
	 * 结构：
	 * 1.[{text=菜单, value=M}, {text=功能, value=F}]
	 * 2.[{text=启用, value=1}, {text=禁用, value=0}]
	 * 从缓存中取出：
	 * EHCacheUtil.get("resourceType")
	 * 
	 * @author Wjd
	 * @date 2016年7月3日 下午5:40:17
	 */
	private void init() {
		EHCacheUtil.initCacheManager();
		EHCacheUtil.initCache("eternalCache");
		
		//缓存匿名url和公共url
		List<String> open_urls = ResourcesUtil.gekeyList("anonymousURL");
		List<String> common_urls = ResourcesUtil.gekeyList("commonURL");
		EHCacheUtil.put("open_urls", open_urls);
		EHCacheUtil.put("common_urls", common_urls);
		
		//解析xml参数文件
		String filePath = Thread.currentThread().getContextClassLoader().getResource("/").getPath().concat("application.init.xml");
		Document document = XmlUtil.getDocument(filePath);
		Element rootElement = XmlUtil.getRootNode(document);//根节点
		
		for(Iterator<Element> i = XmlUtil.getIterator(rootElement); i.hasNext();) {
			Element paraTypeElement = i.next();//参数类型
			
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			for(Iterator<Element> j = XmlUtil.getIterator(paraTypeElement); j.hasNext();) {
				Element paraElement = j.next();//具体参数内容，包括真实值和显示值
				String value = paraElement.attributeValue("value");
				String text = paraElement.attributeValue("text");
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", value);
				map.put("text", text);
				list.add(map);
			}
			
			//缓存xml参数
			EHCacheUtil.put(paraTypeElement.getName(), list);
		}
	}


}
