package ru.ws.utils;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class Utility {
    private static final Logger logger = LoggerFactory.getLogger(Utility.class);
   
    public static void checkParameters(Object[] list) throws Exception {
    	for (Object obj : list) {  
    		if (obj==null || StringUtils.isEmpty(obj.toString())) {
    			logger.error("checkParameters Parameter input is null or empty");
    			throw new Exception("checkParameters Parameter input is null or empty");
    		}
    	}
    }

    public static void checkNumber(String value) throws Exception {
        Pattern pattern = Pattern.compile("(\\+|^)[0-9]+"); // potrebbero metterlo nel formato +39 ecc
        Matcher matcher = pattern.matcher(value.replaceAll(" ", ""));
        if (!matcher.matches()) {
            logger.error("checkNumber input is not number");
            throw new Exception("checkNumber input is not number");
        }
    }

    public static void checkDouble(String value) throws Exception {
        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            logger.error("checkDouble input is not double");
            throw new Exception("checkDouble input is not double");
        }
    }
    public static void checkInteger(Object value) throws Exception {
        try {
            Integer.parseInt(value.toString());
        } catch (Exception e) {
            logger.error("checkInteger input is not integer");
            throw new Exception("checkInteger input is not integer");
        }
    }


    public static void checkEmail(String value) throws Exception {
        String emailPattern = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,11})$";
        value = value.replaceAll(" ", "");
        if (!value.trim().matches(emailPattern)) {
            logger.error("checkEmail input is not email");
            throw new Exception("checkEmail input is not email");
        }
    }

    public static void checkSizeMin(String value, int size) throws Exception {
        if (value.trim().length() < size) {
            logger.error("checkSizeMin input is not long");
            throw new Exception("checkSizeMin input is not long");
        }
    }

    public static void checkSizeMax(String value, int size) throws Exception {
        if (value.trim().length() > size) {
            logger.error("checkSizeMin input is long");
            throw new Exception("checkSizeMin input is long");
        }
    }
    
    public static void checkTimeStamp(Object value) throws Exception { 
    	SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    	try{
    		String str=value.toString();
    		format.parse(str);
    	}catch(Exception e){
    		logger.error("checkTimeStamp input is not Timestamp");
    		throw new Exception("checkTimeStamp input is not Timestamp");
    	}
    }

    public static void checkSizeEqual(String value, int size) throws Exception {
        if (value.trim().length() != size) {
            logger.error("checkSizeEqual input is error");
            throw new Exception("checkSizeEqual input is error");
        }
    }

    public static Timestamp timestamp() {
    	return new Timestamp(System.currentTimeMillis());
    }
    
    public static String generateRandom(int len) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
    
    /**
     * Il metodo seleziona i valori presenti nell'header
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key.toLowerCase(), value);
        }
        return map;
    }
    
    public static Map<String, String> setHeadersInfo(Map<String, String> map, HttpServletResponse response) {
    	for (Map.Entry<String, String> entry : map.entrySet()) {
        	response.addHeader(entry.getKey(), entry.getValue());
        }
        return map;
    }
    
}
