package org.zdxue.microservice.xxx.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xuezd
 */
public class HttpUtil {
    private static final String HTTP_CHARSET = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String getStringPostData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), HTTP_CHARSET));
            String line = null;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (Exception e) {
            logger.error("get post data error.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return data.toString();
    }

    public static byte[] getBytePostData(HttpServletRequest request) {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            InputStream input = request.getInputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = input.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error("get post data error.", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    public static String getHeader(HttpServletRequest request, String header) {
        header = request.getHeader(header);
        if (header == null) {
            return "";
        }
        return header;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-real-IP");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }

        if (ip == null) {
            ip = "";
        }

        return ip;
    }

    public static boolean isXHR(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            return true;
        }
        return false;
    }

    public static Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String value = request.getParameter(key);
            params.put(key, value);
        }

        return params;
    }

    public static Map<String, String> getParamsAndIp(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ip", HttpUtil.getIpAddress(request));

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String value = request.getParameter(key);
            params.put(key, value);
        }

        return params;
    }

    public static Map<String, String> getParamsMap(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String key : requestParams.keySet()) {
            String[] values = (String[]) requestParams.get(key);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            paramsMap.put(key, valueStr);
        }
        return paramsMap;
    }

}