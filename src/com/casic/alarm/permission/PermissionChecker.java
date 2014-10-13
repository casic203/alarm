package com.casic.alarm.permission;

import javax.annotation.Resource;



public class PermissionChecker
{
  private PermissionMatcher permissionMatcher = new PermissionMatcher();
 
  @Resource
  private HttpUrlSourceFetcher httpUrlSourceFetcher;
  
  public HttpUrlSourceFetcher getHttpUrlSourceFetcher() 
  {
	return httpUrlSourceFetcher;
  }
  public void setHttpUrlSourceFetcher(HttpUrlSourceFetcher httpUrlSourceFetcher)
  {
	this.httpUrlSourceFetcher = httpUrlSourceFetcher;
  }

public boolean isAuthorized(String text,String userName)
  {
	
    for (String want : text.split(",")) {
      for (String have : httpUrlSourceFetcher.getSource(userName)) {
        if (this.permissionMatcher.match(want, have)) {
          return true;
        }
      }
    }

    return false;
  }

  public void setReadOnly(boolean readOnly) {
    this.permissionMatcher.setReadOnly(readOnly);
  }

  public boolean isReadOnly() {
    return this.permissionMatcher.isReadOnly();
  }
}