/*
 * @EncodeUtils.java
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * 各种格式的编码加码工具类.
 * 
 * 集成Commons-Codec,Commons-Lang及JDK提供的编解码方法.
 * 
 * @author calvin
 */
/**
 * EncodeUtils Description. import from springside3.3.4
 * 
 * @version Change History:
 * @version  <1> Eric (Nov 21, 2011) create this file. 
 */
public class EncodeUtils {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	public static final String KEY_SHA = "SHA";   
    public static final String KEY_MD5 = "MD5";

	/**
	 * Hex编码.
	 */
	public static String hexEncode(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * Hex解码.
	 */
	public static byte[] hexDecode(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Base64编码.
	 */
	public static String base64Encode(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
	 */
	public static String base64UrlSafeEncode(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Base64解码.
	 */
	public static byte[] base64Decode(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * URL 编码, Encode默认为UTF-8. 
	 */
	public static String urlEncode(String input) {
		try {
			return URLEncoder.encode(input, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 
	 */
	public static String urlDecode(String input) {
		try {
			return URLDecoder.decode(input, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * Html 转码.
	 */
	public static String htmlEscape(String html) {
		return StringEscapeUtils.escapeHtml(html);
	}

	/**
	 * Html 解码.
	 */
	public static String htmlUnescape(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String xmlEscape(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String xmlUnescape(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}
	
    /**  
     * MD5加密  
     *   
     * @param data  
     * @return  
     * @throws Exception  
     */  
    public static byte[] encryptMD5(byte[] data) throws Exception {   
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);   
        md5.update(data);   
        return md5.digest();   
    }   
    
    public static String MD5Hex(byte[] data) throws Exception {
    	return hexEncode(encryptMD5(data));
    }
    
    public static String MD5Base64(byte[] data) throws Exception {
    	return base64Encode(encryptMD5(data));
    }
  
    /**  
     * SHA加密  
     *   
     * @param data  
     * @return  
     * @throws Exception  
     */  
    public static byte[] encryptSHA(byte[] data) throws Exception {   
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);   
        sha.update(data);   
        return sha.digest();   
    }   
    
    public static String SHAHex(byte[] data) throws Exception {
    	return hexEncode(encryptSHA(data));
    }
    
    public static String SHABase64(byte[] data) throws Exception {
    	return base64Encode(encryptSHA(data));
    }
    
}
