package com.casic.alarm.permission;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.casic.core.mapper.JsonMapper;

public class UsersHttpUrlSourceFetcher implements UrlSourceFetcher
{

	private static Logger logger = LoggerFactory.getLogger(UsersHttpUrlSourceFetcher.class);
	private String repoCode;
	private String url;
	private String relativePath;
	
	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRepoCode() {
		return repoCode;
	}

	public void setRepoCode(String repoCode) {
		this.repoCode = repoCode;
	}

	public List<String> getSource(String paramString) 
	{
		try
		{
			this.url= this.relativePath;
			if ((this.url.indexOf("?repoCode=") == -1) || (this.url.indexOf("&repoCode=") == -1))
		      {
		        if (this.url.indexOf('?') != -1)
		          this.url += "&";
		        else {
		          this.url += "?";
		        }

		        if(paramString!="")
		        {
		        	  this.url = (this.url + "repoCode=" + paramString);
		        }
		        else
		        {
		        	 this.url = (this.url + "repoCode=" + this.repoCode);
		        }
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
		      List list = (List)jsonMapper.fromJson(content, List.class);
		      return list;
		}
		catch(Exception ex)
		{
		    logger.error("", ex);
		    throw new RuntimeException(ex);
		}

	}
}
