package com.casic.alarm.permission;

import com.casic.core.mapper.JsonMapper;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpUrlSourceFetcher
  implements UrlSourceFetcher
{
  private static Logger logger = LoggerFactory.getLogger(HttpUrlSourceFetcher.class);
  public static final int DEFAULT_BUFFER_SIZE = 1024;
  private String url;
  private String appId;
  private String repoCode;
  private String relativePath;
  
  public String getRelativePath() {
	return relativePath;
}

public void setRelativePath(String relativePath) {
	this.relativePath = relativePath;
}

public String getRepoCode() 
  {
	return repoCode;
  }

public void setRepoCode(String repoCode) 
   {
	this.repoCode = repoCode;
   }

public List<String> getSource(String type)
  {
    try
    {
      this.url=this.relativePath;
     //add the appId parameter
      if ((this.url.indexOf("?appId=") == -1) || (this.url.indexOf("&appId=") == -1))
      {
        if (this.url.indexOf('?') != -1)
          this.url += "&";
        else {
          this.url += "?";
        }

        this.url = (this.url + "appId=" + this.appId);
      }
      
      //add the repoID parameter
      if ((this.url.indexOf("?repoCode=") == -1) || (this.url.indexOf("&repoCode=") == -1))
      {
        if (this.url.indexOf('?') != -1)
          this.url += "&";
        else {
          this.url += "?";
        }

        this.url = (this.url + "repoCode=" + this.repoCode);
      }
      
      if ((this.url.indexOf("?username=") == -1) || (this.url.indexOf("&username=") == -1))
      {
        if (this.url.indexOf('?') != -1)
          this.url += "&";
        else {
          this.url += "?";
        }
        this.url = (this.url + "username=" + type);
      }
      
      logger.info(this.url);
      HttpURLConnection conn = (HttpURLConnection)new URL(this.url).openConnection();

      InputStream is = conn.getInputStream();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] b = new byte[1024];
      int len = 0;

      while ((len = is.read(b, 0, 1024)) != -1) {
        baos.write(b, 0, len);
      }

      is.close();
      baos.flush();
      baos.close();

      String content = new String(baos.toByteArray(), "UTF-8");
      logger.info(content);

      JsonMapper jsonMapper = new JsonMapper();
      Map map = (Map)jsonMapper.fromJson(content, Map.class);
  
      List<String> authorities = (List)map.get("authorities");

      StringBuffer buff = new StringBuffer();
 
      List<String> result = new ArrayList();
      
      for(String authority : authorities)
      {
    	  result.add("app."+appId+":"+authority);
    	 
      }
    
      return result;
      
    } catch (Exception ex) {
      logger.error("", ex);
      throw new RuntimeException(ex);
    }
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

}