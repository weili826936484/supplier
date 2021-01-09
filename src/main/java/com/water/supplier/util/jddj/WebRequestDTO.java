package com.water.supplier.util.jddj;

public class WebRequestDTO
{
  private String token;
  private String app_key;
  private String timestamp;
  private String v;
  private String format;
  private String jd_param_json;
  
  public String getToken() { return this.token; }


  
  public void setToken(String token) { this.token = token; }


  
  public String getApp_key() { return this.app_key; }


  
  public void setApp_key(String app_key) { this.app_key = app_key; }


  
  public String getTimestamp() { return this.timestamp; }


  
  public void setTimestamp(String timestamp) { this.timestamp = timestamp; }


  
  public String getV() { return this.v; }


  
  public void setV(String v) { this.v = v; }


  
  public String getFormat() { return this.format; }


  
  public void setFormat(String format) { this.format = format; }


  
  public String getJd_param_json() { return this.jd_param_json; }


  
  public void setJd_param_json(String jd_param_json) { this.jd_param_json = jd_param_json; }



  
  public String toString() { return "WebRequestDTO [token=" + this.token + ", app_key=" + this.app_key + ", timestamp=" + this.timestamp + ", v=" + this.v + ", format=" + this.format + ", jd_param_json=" + this.jd_param_json + "]"; }
}
