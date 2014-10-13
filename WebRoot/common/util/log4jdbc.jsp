<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.casic.core.jdbc.DataSourceRegistryFactoryBean"%>
<%
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
	DataSourceRegistryFactoryBean dataSourceRegistryFactoryBean = (DataSourceRegistryFactoryBean) ctx.getBean("&dataSourceRegistry");

    if (request.getParameter("toggle") != null) {
		if (dataSourceRegistryFactoryBean.isLog4jdbcEnabled()) {
			dataSourceRegistryFactoryBean.disableLog4jdbc();
		} else {
			dataSourceRegistryFactoryBean.enableLog4jdbc();
		}
        response.sendRedirect("log4jdbc.jsp");
    }

	pageContext.setAttribute("dataSourceRegistryFactoryBean", dataSourceRegistryFactoryBean);
%>
<html>
  <head>
    <meta charset="utf-8">
	<title>log4jdbc</title>
    <style>
tbody tr:nth-child(odd) td,
tbody tr:nth-child(odd) th {
  background-color: #f9f9f9;
}
    </style>
  </head>
  <body>
    <table border="1">
     <tbody>
        <tr>
          <td>状态</td>
          <td>${dataSourceRegistryFactoryBean.log4jdbcEnabled}</td>
          <td><button onclick="location.href='log4jdbc.jsp?toggle=true'">${dataSourceRegistryFactoryBean.log4jdbcEnabled ? '禁用' : '启用'}</button></td>
        </tr>
      </tbody>
    </table>
  </body>
</html>
