/*
 * @StringUtils.java
 *
 * Copyright (c) 2011-2011 MOR, Inc.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of MOR,
 * Inc. ("Confidential Information").  You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with MOR.
 */

package com.gtjt.xxjss.springmvc.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;
import com.google.common.base.Function;
import com.google.common.collect.Maps;

/**
 * The Class StringUtils.
 *
 * @author Administrator
 */
public class StringUtil {
	/**
	* @Title: isLetter
	* @Description: 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	* @param @param c
	* @param @return    
	* @return boolean   
	* @throws 
	* @author Hawking Zhou
	* @date 2012-1-9 下午03:13:54
	*/ 
	public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }
	
    /**
    * @Title: length
    * @Description: 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
    * @param @param s
    * @param @return    
    * @return int   
    * @throws 
    * @author Hawking Zhou
    * @date 2012-1-9 下午03:14:44
    */ 
    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }
	/**
	* @Title: cutString
	* @Description: 按位截取字符串，一个汉字两位。如"1你好2" 截取5为"1你好" 截取4为"1你"（好字一半被截取） 截取3为"1你"
	* @param @param str
	* @param @param length
	* @param @return    
	* @return String   
	* @throws 
	* @author Hawking Zhou
	* @date 2012-1-9 下午02:51:37
	*/ 
	public static String cutString(String str, int length){
		if(str==null)
			return null;
		try {
			byte[] bytes = str.getBytes("Unicode");
			int n = 0; // 表示当前的字节数
			int i = 2; // 要截取的字节数，从第3个字节开始
			for (; i < bytes.length && n < length; i++) {
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
				if (i % 2 == 1) {
					n++; // 在UCS2第二个字节时n加1
				} 
				else {
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
					if (bytes[i] != 0) {
						n++;
					}
				}
			}
			// 如果i为奇数时，处理成偶数
			if (i % 2 == 1){
				// 该UCS2字符是汉字时，去掉这个截一半的汉字
				if (bytes[i - 1] != 0)
					i = i - 1;
				// 该UCS2字符是字母或数字，则保留该字符
				else
					i = i + 1;
			}
			return new String(bytes, 0, i, "Unicode");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	

	// 将为空字段按第二参数返回
	/**
	 * Do null string.
	 *
	 * @param input the input
	 * @param output the output
	 * @return the string
	 */
	public static String doNullString(String input, String output) {
		try {
			if (input == null || input.isEmpty())
				return output;
			else
				return input;
		} catch (Exception ex) {
			return output;
		}
	}

	// 将为空字段返回&nbsp;
	/**
	 * Do null string.
	 *
	 * @param input the input
	 * @return the string
	 */
	public static String doNullString(String input) {
		return doNullString(input, "&nbsp;");
	}

	// 查找字符串是否在字符串数组中
	/**
	 * In string array.
	 *
	 * @param aryString the ary string
	 * @param strSearch the str search
	 * @return true, if successful
	 */
	public static boolean inStringArray(String[] aryString, String strSearch) {
		boolean bln = false;
		if (aryString == null)
			return bln;
		for (String temp : aryString) {
			if (temp.equals(strSearch)) {
				bln = true;
				break;
			}
		}
		return bln;
	}

	// 填充字符串，参数为原字符串，填充字符，返回字符串长度
	/**
	 * Fill string.
	 *
	 * @param strSrc the str src
	 * @param charFill the char fill
	 * @param intLength the int length
	 * @return the string
	 */
	public static String fillString(String strSrc, char charFill, int intLength) {
		while (strSrc.length() < intLength)
			strSrc = charFill + strSrc;
		return strSrc;
	}

	// 填充字符串数组，参数为原字符串数组，填充字符，返回字符串长度
	/**
	 * Fill string array.
	 *
	 * @param strSrc the str src
	 * @param charFill the char fill
	 * @param intLength the int length
	 * @return the string[]
	 */
	public static String[] fillStringArray(String[] strSrc, char charFill,
			int intLength) {
		for (int i = 0; i < strSrc.length; i++)
			strSrc[i] = fillString(strSrc[i], charFill, intLength);
		return strSrc;
	}

	// 还原数字字符串
	/**
	 * Int string array.
	 *
	 * @param strSrc the str src
	 * @return the string[]
	 */
	public static String[] IntStringArray(String[] strSrc) {
		for (int i = 0; i < strSrc.length; i++)
			strSrc[i] = String.valueOf(Integer.valueOf(strSrc[i]));
		return strSrc;
	}

	// 连接字符串数组，参数为原字符串数组，分隔字符
	/**
	 * Join string array.
	 *
	 * @param strSrc the str src
	 * @param strDelimit the str delimit
	 * @return the string
	 */
	public static String joinStringArray(String[] strSrc, String strDelimit) {
		String str = "";
		if (strSrc.length < 1)
			return str;
		if (strSrc.length == 1)
			return strSrc[0];
		if (strSrc.length > 1) {
			for (int i = 0; i < (strSrc.length - 1); i++)
				str = str + strSrc[i] + strDelimit;
			str = str + strSrc[(strSrc.length - 1)];
		}
		return str;
	}
    /**
    * @Title: removeRepeatItemFormStringArray
    * @Description: 传入一个字符串数组，将其中重复项删除后返回
    * @param @param strSrc
    * @param @return    
    * @return String[]   
    * @throws 
    * @author Hawking Zhou
    * @date 2012-5-16 下午08:09:51
    */ 
    public static String[] removeRepeatItemFormStringArray(String[] strSrc){
    	Set<String> set=new HashSet<String>();
    	for(String str:strSrc)
    		set.add(str);
    	return set.toArray(new String[0]);
    	
    }
	// 连接字符串数组，参数为原字符串数组，分隔字符,添加字符如' "
	/**
	 * Join string array.
	 *
	 * @param strSrc the str src
	 * @param strDelimit the str delimit
	 * @param fillChar the fill char
	 * @return the string
	 */
	public static String joinStringArray(String[] strSrc, String strDelimit,
			String fillChar) {
		String str = "";
		if (strSrc.length < 1)
			return str;
		if (strSrc.length == 1)
			return fillChar + strSrc[0] + fillChar;
		if (strSrc.length > 1) {
			for (int i = 0; i < (strSrc.length - 1); i++)
				str = str + fillChar + strSrc[i] + fillChar + strDelimit;
			str = str + fillChar + strSrc[(strSrc.length - 1)] + fillChar;
		}
		return str;
	}

	// 传入日期字符0000-00-00，返回一个毫秒字符串
	// 参数strStatus为begin end begin 小时分钟秒为0，end小时23，分钟59 秒59
	// 得到一个uuid
	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public static String getUuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	// 得到一个guid（oracle sys_guid()格式）
	/**
	 * Gets the guid.
	 *
	 * @return the guid
	 */
	public static String getGuid() {
		return getUuid().replaceAll("-", "").toUpperCase();
	}

	// 传入字符串，替换空格为&nbsp;
	/**
	 * Gets the html format.
	 *
	 * @param str the str
	 * @return the html format
	 */
	public static String getHtmlFormat(String str) {
		// str=str.trim();
		str = str.replace(" ", "&nbsp;");
		str = str.replace("\r\n", "<br/>");
		// str="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+str;
		return str;
	}

	// 传入字符串，首字母小写
	/**
	 * To lower of first char.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String toLowerOfFirstChar(String str) {
		char[] chars = str.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		return new String(chars);
	}

	// 在System打印Map
	/**
	 * System out.
	 *
	 * @param map the map
	 */
	public static void systemOut(Map<String, Object> map) {
		for (String s : map.keySet()) {
			System.out.println(s + map.get(s));
		}
	}
	
	public static String toString(Object o){
		if(o instanceof Map){
			Map<String,String> cm = Maps.transformValues((Map<String, Object>)o, new Function<Object, String>() {
				@Override
				public String apply(Object arg0) {
					if(arg0 instanceof Object[]){
						return Arrays.toString((Object[])arg0);
					}
					return arg0.toString();
				}
			});
			return cm.toString();
		}else{
			return o.toString();
		}
	}

	// 全角字符串转半角字符串
	/**
	 * To dbc.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String toDBC(String str) {
		char c[] = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		return new String(c);

	}

	// 半角字符串转全角字符串
	/**
	 * To sbc.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String toSBC(String str) {
		char c[] = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * Parses the longs.
	 *
	 * @param source the source
	 * @return the long[]
	 * @description String[]提取Long[]
	 * @author Eric (Sep 23, 2011)
	 * @version Change History:
	 * @version <1> Sep 23, 2011 Eric Modification purpose.
	 */
	public static Long[] parseLongs(String[] source) {
		Long[] results = new Long[source.length];
		for (int i = 0; i < source.length; i++) {
			results[i] = Long.parseLong(source[i]);
		}
		return results;
	}


	/**
	 * Parses the json.
	 *
	 * @param source the source
	 * @return the list
	 * @description String source matches [aaa,bbb,ccc]
	 * @author Eric (Oct 21, 2011)
	 * @version Change History:
	 * @version <1> Oct 21, 2011 Eric Modification purpose.
	 */
	public static List<String> parseJson(String source) {
		String sub = source.substring(1, source.length()-1);
		return Arrays.asList(sub.split(","));
	}

	/**
	 * Checks if is not empty.
	 *
	 * @param obj the obj
	 * @return true, if is not empty
	 */
	public static boolean isNotEmpty(Object obj) {
		boolean result = false;
		if(obj != null && obj instanceof String){
			if(obj.equals("null")) //add by eric for json bug 2011/11/24
				return false;
		}
		if (obj != null && !(obj.toString()).trim().equals(""))
			result = true;
		return result;
	}

	/**
	 * Checks if is empty.
	 *
	 * @param pObj the obj
	 * @return true, if is empty
	 * @description 判断对象是否为空
	 * @author yang (Oct 10, 2011)
	 * @version Change History:
	 * @version <1> Oct 10, 2011 yang Modification purpose.
	 */
	public static boolean isEmpty(Object pObj) {
		if (pObj == null) {
			return true;
		}
		
		if (pObj instanceof String) {
			if(pObj.equals("null")) {//add by eric for json bug 2011/11/24
				return true;
			}
			if (((String) pObj).length() == 0) {
				return true;
			}
		} 
		else if(pObj instanceof String[]){//add by Hawking Zhou 2012/06/26
			String[] temp=(String[])pObj;
			if(temp.length<1){
				return true;
			}
			else if(temp.length==1){
			    if(isEmpty(temp[0])){
			    	return true;
			    }
			}
		}
		else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} 
		else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Null to string.
	 *
	 * @param pObj the obj
	 * @return the string
	 * @description 判断对象是否为空,若为空,返回""
	 * @author yang (Oct 10, 2011)
	 * @version Change History:
	 * @version <1> Oct 10, 2011 yang Modification purpose.
	 */
	public static String nullToString(Object pObj) {
		if (isEmpty(pObj)) {
			return "";
		} else {
			return pObj.toString().trim();
		}
	}

	/**
	 * Gets the iterate string.
	 *
	 * @param length the length
	 * @param str the str
	 * @return the iterate string
	 * @description 根据length，返回相应长度的str。
	 * @author 周珍文 (Oct 19, 2011)
	 * @version Change History:
	 * @version <1> Oct 19, 2011 yang Modification purpose.
	 */
	public static String getIterateString(int length, String str) {
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < length; index++)
			sb.append(str);
		return sb.toString();
	}

	/**
	 * Gets the string from j query get content.
	 *
	 * @param str the str
	 * @return the string from j query get content
	 * @description 根据length，返回相应长度的str。
	 * @author 周珍文 (Oct 19, 2011)
	 * @version Change History:
	 * @version <1> Oct 19, 2011 yang Modification purpose.
	 */
	public static String getStringFromJQueryGetContent(String str) {

		try {
			return new String(str.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * 格式化字符串
	 * 删除空格、用中文括号替换英文括号、用半角数字替换全角数字
	 */
    /**
	 * Format string.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String formatString(String str){
    	str=str.replaceAll(" ","");//replace to replaceAll
    	str=str.replace("(","（");
    	str=str.replace(")","）");
    	str=StringUtil.toDBC(str);
    	return str;
    }
	/**
	 * 针对新增企业名称过滤
	 * @param unitName
	 * @return
	 */
    public static String filterStringForUnitName(String unitName){
    	if(isNotEmpty(unitName)){
	    	//汉字 英文 数字 和-()（）
	    	return toDBC(unitName.replaceAll("[^a-zA-Z0-9\u4E00-\u9FA5（）()-/]", ""));
    	}else{
    		return "";
    	}
    }
    /**
     * Fill zero if(digitLength> length(digit)).
     *
     * @param digit the digit
     * @param digitLength the digit length
     * @return the string
     * @description
     * @author Eric (Nov 15, 2011)
     * @version Change History:
     * @version  <1> Nov 15, 2011 Eric  Modification purpose.
     */
    public static String fillZero(int digit,int digitLength){
    	char[] digitChar= Integer.toString(digit).toCharArray();
    	StringBuffer result = new StringBuffer();
    	if(digitLength>digitChar.length)
    	for (int i = 0; i < digitLength-digitChar.length; i++) {
			result.append('0');
		}
    	result.append(digitChar);
    	return result.toString();
    }
    
    
    /**
	* @Title: zipString
	* @Description: 返回一个压缩后的字符串
	* @param @param str
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	* @author Hawking Zhou
	* @date 2011-12-22 下午10:31:52
	*/ 
	public static String zipString(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes("utf-8"));
			gzip.close();
//			return new BASE64Encoder().encode(out.toByteArray());
			return Base64.encodeBase64String(out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	* @Title: unZipString
	* @Description: 返回一个解压缩后的字符串
	* @param @param str
	* @param @return
	* @param @throws IOException    设定文件
	* @return String    返回类型
	* @throws
	* @author Hawking Zhou
	* @date 2011-12-22 下午10:31:52
	*/ 
	public static String unZipString(String str){
	    if (str == null || str.length() == 0) {
	      return str;
	    }
	    try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			ByteArrayInputStream in = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(str));
			ByteArrayInputStream in = new ByteArrayInputStream(Base64.decodeBase64(str));
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
			  out.write(buffer, 0, n);
			}
			return out.toString("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	  }

	/**
	* @Title: getMergeArray
	* @Description: 将两个字符串数组合并成一个
	* @param @param al
	* @param @param bl
	* @param @return    
	* @return String[]   
	* @throws 
	* @author Hawking Zhou
	* @date 2012-2-27 下午05:11:43
	*/ 
	public static String[] getMergeArray(String[] al, String[] bl) {
		String[] a = al;
		String[] b = bl;
		String[] c = new String[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
	
	/**
	* @Title: compareObject
	* @Description: 比较两个对象，处理o1为空的情况
	* @param @param o1
	* @param @param o2
	* @param @return    
	* @return boolean   
	* @throws 
	* @author Hawking Zhou
	* @date 2012-5-29 上午11:28:26
	*/ 
	public static boolean compareObject(Object o1,Object o2){
		if(isEmpty(o1) && isEmpty(o2)){
			return true;
		}
		else if(isEmpty(o1) && isNotEmpty(o2)){
			return false;
		}
		else{
			return o1.equals(o2);
		}
	}
	/**
	 * 替换字符串中的特殊字符
	 * @param str
	 * @return
	 */
	public static String toString(String str){
		String s=str;
		if(str.indexOf("&")>=0){
			s=s.replaceAll("&", "&#38;");
		}
		if(str.indexOf("'")>=0){
			s=s.replaceAll("'", "‘");
		}
		if(str.indexOf("<")>=0){
			s=s.replaceAll("<", "&#60;");
		}
		if(str.indexOf(">")>=0){
			s=s.replaceAll(">", "&#62;");
		}
		if(str.indexOf("*")>=0){
			s=s.replaceAll("*", "-*");
		}
		if(str.indexOf("--")>=0){
			s=s.replaceAll("--", "&#175;&#175;");
		}
		if(str.indexOf("%")>=0){
			s=s.replaceAll("%", "&#888;");
		}
		
		return s;
	}
	/**
	 * 判断字符串长度是否超过预期长度
	 * @param str
	 * @param length
	 * @return
	 */
	public static boolean isLeagth(String str,int length){
		if(str==null||str.equals("")||str.length()<=length){
			return true;
		}
		return false;
	}
	
	/**
	 * 过滤掉传参的sql注入信息
	 * @author zb
	 * @param s
	 * @return
	 */
	public static String dealToSql(String s) {
		if(s==null)
			return null;
		 s=s.replaceAll("'", "''");	
		return s;
	}
	/**
	 * 判断字符串是否全由*号组成的
	 * @author cui
	 * @param s
	 * @return
	 */
	public static boolean isAllXing(String s) {
		if(s==null)
			return false;
		for(int i=0;i<s.length();i++){
			if(!s.substring(i, i+1).equals("*")){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 添加路局ID
	 * @param srcBurs
	 * @param addBurId
	 * @return
	 */
	public static String addBurId(String srcBurs,String addBurId){
		if(StringUtil.isEmpty(addBurId)){
			return srcBurs;
		}
		if(StringUtil.isEmpty(srcBurs)){
			return addBurId;
		}
		List<String> burs = Arrays.asList(srcBurs.split(","));
		if(burs.contains(addBurId)){
			return srcBurs;
		}else{
			srcBurs += ","+addBurId;
			return srcBurs;
		}
	}
}
