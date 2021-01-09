package com.water.supplier.util;
import java.io.IOException;
import java.util.Properties;
public class Cfg {
	/**
     * 引入Properties
     */
    private static Properties properties = new Properties();
    
    static {
        try {
        	properties.load(Cfg.class.getClassLoader().getResourceAsStream(
                    "wx.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * <B>方法名称：</B>根据Key获取值<BR>
     * <B>概要说明：</B><BR>
     * @param key 
     * @return value 
     */
    public static String getConfig(String key) {
        return (String) properties.get(key);
    }
}
