package com.water.supplier.util.wx;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.json.JSONException;
import org.json.JSONObject;
public class HttpGetRequest {
	/**
     * <B>构造方法</B><BR>
     */
    private HttpGetRequest() {
        
    }
    
    /**
     * *获取JSON对象
     * 
     * @author
     * @date 创建时间 2016年7月28日 上午11:25:01
     * @param url 
     * @return JSONObject
     * @throws IOException 
     * @throws JSONException 
     */
    public static JSONObject doGet(String url) throws IOException, JSONException {
        URL localURL = new URL(url);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/text");

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        if (httpURLConnection.getResponseCode() >= 300) {
            try {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

        } finally {

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new JSONObject(resultBuffer.toString());
    }
    
    
    /**
     * 发送https请求
     * @param requestUrl  请求地址
     * @param requestMethod  请求方式 get post
     * @param outputStr      提交的数据
     * @return JSONObject  (通过JSONobject.getKey("key")的方式获取JSON对象的属性值)
     */
    public static JSONObject doGet(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            //创建SSLContext对象 并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            
            //设置请求方式
            conn.setRequestMethod(requestMethod);
            
            //当outputStr 不为null时 向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            
            
            // 读取返回数据
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new  InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            
            //释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = new JSONObject(buffer.toString());
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return jsonObject;
    }
}
