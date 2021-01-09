package com.water.supplier.util.jddj;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * 签名验证
 * @author wang-ql
 *
 */
public class SignUtils {

	private static String concatParams(Map<String, String> params2) {
	    Object[] key_arr = params2.keySet().toArray();
	    Arrays.sort(key_arr);
	    StringBuilder sb = new StringBuilder();
	    for (Object key : key_arr) {
	      String val = params2.get(key);
	      sb.append(key).append(val);
	    } 
	    return sb.toString();
	  }
	
	public static Map<String, String> getProperty(Object entityName) throws Exception {
	    Map<String, String> mapResult = new HashMap<String, String>();
	    Class<? extends Object> c = (Class)entityName.getClass();
	    Field[] field = c.getDeclaredFields();
	    for (Field f : field) {
	      int mod = f.getModifiers();
	      if (!Modifier.isFinal(mod) && !Modifier.isStatic(mod)) {
	        Object v = invokeMethod(entityName, f.getName(), null);
	        if (v != null && !"sign".equals(f.getName()))
	        {
	          
	          mapResult.put(f.getName(), v.toString()); } 
	      } 
	    } 
	    return mapResult;
	  }

	  private static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
		    Class<? extends Object> ownerClass = (Class)owner.getClass();
		    methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		    Method method = null;
		    try {
		      method = ownerClass.getMethod("get" + methodName, new Class[0]);
		    } catch (Exception e) {
		      return null;
		    } 
		    return method.invoke(owner, new Object[0]);
		  }
	  
	  public static String getSignByMD5(Object param, String appSecret) throws Exception {
		    String sysStr = concatParams(getProperty(param));
		    StringBuilder resultStr = new StringBuilder("");
		    resultStr.append(appSecret).append(sysStr).append(appSecret);
		    return MD5Util.getMD5String(resultStr.toString()).toUpperCase();
		  }
	  
	  public static String getSignBySHA512(Object param, String appSecret) throws Exception {
		    String sysStr = concatParams(getProperty(param));
		    StringBuilder resultStr = new StringBuilder("");
		    resultStr.append(appSecret).append(sysStr).append(appSecret);
		    return DigestUtils.sha512Hex(resultStr.toString()).toUpperCase();
		  }
}
