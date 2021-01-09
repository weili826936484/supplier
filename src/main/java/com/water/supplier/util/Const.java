package com.water.supplier.util;

/**
 *    通用常量
 * @author wang-ql
 *
 */
public class Const {

	/** 通用字符集编码 */
    public static final String CHARSET_UTF8 = "UTF-8";
    
    /** 日期格式 */
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    /** 日期时间格式 */
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    
    /** 数据交换返回代码：处理成功 */
    public static final String CODE_SUCCESS = "0";
    
    /** 数据交换警告代码：数据不存在 */
    public static final String CODE_WARN_NONE = "W001"; 
    
    /** 数据交换警告代码：数据重复 */
    public static final String CODE_WARN_DUP = "W002";
    
    /** 数据交换错误代码：权限验证错误 */
    public static final String CODE_ERR_AUTH = "E001";

    /** 数据交换错误代码：参数错误 */
    public static final String CODE_ERR_PARAM = "E002";
    
    /** 数据交换错误代码：登录失效*/
    public static final String CODE_LOGIN_EXPIRE = "E101";

    /** 数据交换错误代码：异常错误 */
    public static final String CODE_ERR_EXP = "E999";
    
    /**
     * <B>构造方法</B><BR>
     */
    private Const() {
    }

}
