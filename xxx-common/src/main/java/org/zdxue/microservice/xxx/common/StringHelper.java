package org.zdxue.microservice.xxx.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xuezd
 */
public class StringHelper {
	
	private static final String HEX = "0123456789abcdef";
	private static final String FUZZ_CHAR = "*";

	public static String fuzz(String s, int begin, int end) {
		StringBuilder buf = new StringBuilder(s.length());
		buf.append(s.substring(0, begin));
		for(int i = 0; i< end - begin; i++) {
			buf.append(FUZZ_CHAR);
		}
		buf.append(s.substring(end, s.length()));
		return buf.toString();
	}
	
	public static byte[] getBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("指定的编码集不对, 当前指定的编码集是:" + charset);
		}
	}
	
	public static int parseInt(String s, int defValue) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return defValue;
		}
	}
	
	public static long parseLong(String s, long defValue) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return defValue;
		}
	}
	
	public static double parseDouble(String s, double defValue) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			return defValue;
		}
	}
	
	public static int[] toIntArray(String str) {
		if(StringUtils.isEmpty(str)) {
			return new int[0];
		}
		
		String[] idArr = str.split(",");
		int[] ids = new int[idArr.length];
		for(int i=0; i<ids.length; i++) {
			ids[i] = parseInt(idArr[i], -1);
		}
		return ids;
	}
	
	public static String[] toStringArray(String str) {
		if(StringUtils.isEmpty(str)) {
			return new String[]{};
		}
		
		String[] strArr = str.split(",");
		for(int i = 0; i < strArr.length; i++) {
			strArr[i] = strArr[i].trim(); 
		}
		return strArr;
	}
	
	public static List<String> toList(String str) {
		List<String> list = new ArrayList<String>();
		if(StringUtils.isEmpty(str)) {
			return list;
		}
		
		for(String s : str.split(",")) {
			list.add(s.trim());
		}
		return list;
	}
	
	public static List<Integer> toIntList(String str) {
		List<Integer> list = new ArrayList<Integer>();
		if(StringUtils.isEmpty(str)) {
			return list;
		}
		
		for(String s : str.split(",")) {
			list.add(parseInt(s.trim(), 0));
		}
		return list;
	}
	
	public static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			// 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
			sb.append(HEX.charAt((b >> 4) & 0x0f));
			// 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
			sb.append(HEX.charAt(b & 0x0f));
		}
		return sb.toString();
	}
	
	public static String toString(List<String> list) {
		StringBuilder buf = new StringBuilder();
		for(String str : list) {
			buf.append(str).append(",");
		}
		return buf.toString();
	}
	
	/**
	 * 除去空值参数和签名参数
	 * 
	 * @param params
	 * @return
	 */
	public static Map<String, String> filter(Map<String, String> params) {
		Map<String, String> newParams = new HashMap<String, String>();

		if (params == null || params.size() <= 0) {
			return newParams;
		}

		for (String key : params.keySet()) {
			String value = params.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
				continue;
			}

			newParams.put(key, value);
		}

		return newParams;
	}

	/**
	 * 把参数排序，并按照“参数键=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 * @return
	 */
	public static String sortAndConcat(Map<String, String> params) {
		//构建有序的参数key
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		//按有序的key，获取参数键值并拼接成字符串
		int count = 0;
		StringBuilder str = new StringBuilder();
		for (String key : keys) {
			if(count++ > 0) {
				str.append("&");
			}

			str.append(key).append("=").append(params.get(key));
		}

		return str.toString();
	}
	
	public static String urlEncode(String s, String charset) {
		try {
			return URLEncoder.encode(s, charset);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static Map<String, String> jsonToStringMap(String json) {
		Map<String, String> map = new HashMap<String, String>();

		JSONObject jsonObj = JSONObject.parseObject(json);
		for (String key : jsonObj.keySet()) {
			String value = jsonObj.get(key).toString();
			if (value.startsWith("{") && value.endsWith("}")) {
				continue;
			} else {
				map.put(key, value);
			}
		}

		return map;
	}
	
	public static Map<String, Object> jsonToMap(String json) {
		Map<String, Object> map = new HashMap<String, Object>();

		JSONObject jsonObj = JSONObject.parseObject(json);
		for (String key : jsonObj.keySet()) {
			String value = jsonObj.get(key).toString();
			if (value.startsWith("{") && value.endsWith("}")) {
				map.put(key, jsonToMap(value));
			} else {
				map.put(key, value);
			}
		}

		return map;
	}
	
}