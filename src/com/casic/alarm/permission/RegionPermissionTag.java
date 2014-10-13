package com.casic.alarm.permission;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
public class RegionPermissionTag extends BodyTagSupport
{
	public static final long serialVersionUID = 0L;
	  private static Logger logger = LoggerFactory.getLogger(RegionPermissionTag.class);

	  private String permission = null;
	  private String region = null;

	  public int doStartTag() throws JspException {
		  
		logger.info("enter the region-permission tag");
		Object object=this.pageContext.getSession().getAttribute("_const_cas_assertion_");
		
	    if (this.permission == null||object==null ) {
	      logger.error("permission should not be null");
	      return 0;
	    }
	    
	    //获取当前用户名
	    Assertion assertion = (Assertion) object;  
        String currentUserName = assertion.getPrincipal().getName(); 
        logger.debug("current user:"+currentUserName);
	    String text = ((PermissionDecorator)getBean(PermissionDecorator.class)).decode(this.permission, this.region);
	   
	    logger.debug("decoded text : {}", text);
	    
	    boolean authorized = ((PermissionChecker)getBean(PermissionChecker.class)).isAuthorized(text,currentUserName);

	    if (!authorized) {
	      return 0;
	    }

	    return 1;
	  }

	  protected <T> T getBean(Class<T> clz) {
	    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.pageContext.getServletContext());

	    return ctx.getBean(clz);
	  }

	  public void setPermission(String permission)
	  {
	    this.permission = permission;
	  }

	  public void setRegion(String region) {
	    this.region = region;
	  }

}
