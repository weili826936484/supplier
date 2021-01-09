package com.water.supplier.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 *      请求响应工具类
 * @author wang-ql
 *
 */
public class ResponseUtils {

	/**
     * <B>方法名称：</B>设定JSON对象响应<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param response
     *            响应
     * @param json
     *            JSON对象
     */
    public static void putJsonResponse(HttpServletResponse response, JSONObject json) {
        if (json == null) {
            putTextResponse(response, "{}");
        } else {
            putTextResponse(response, json.toString());
        }
    }
    
    /**
     * <B>方法名称：</B>设定文本响应<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param response
     *            响应
     * @param text
     *            文本信息
     */
    public static void putTextResponse(HttpServletResponse response, String text) {
        response.setContentType("text/plain;charset=" + Const.CHARSET_UTF8);
        response.setCharacterEncoding(Const.CHARSET_UTF8);
        try {
            response.getWriter().write(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
