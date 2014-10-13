<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.casic.core.jdbc.DataSourceFactoryBean"%>
<%@page import="com.casic.core.jdbc.DataSourceRegistryFactoryBean"%>
<%
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
	DataSourceRegistryFactoryBean dataSourceRegistryFactoryBean = (DataSourceRegistryFactoryBean) ctx.getBean("&dataSourceRegistry");
%>
<html>
  <head>
    <meta charset="utf-8">
	<title>jdbc</title>
    <style>
tbody tr:nth-child(odd) td,
tbody tr:nth-child(odd) th {
  background-color: #f9f9f9;
}
    </style>
	<script>
function toggle(key) {
	if (key == "") {
		return;
	}
	var el = document.getElementById(key);
	if (el.style.display == 'none') {
		el.style.display = '';
	} else {
		el.style.display = 'none';
	}
}
	</script>
  </head>
  <body>
    <table border="1">
	  <thead>
        <tr>
          <th>prefix</th>
          <th>driverClassName</th>
          <th>url</th>
          <th>username</th>
          <th>password</th>
          <th>initialSize</th>
          <th>maxActive</th>
          <th>numActive</th>
          <th>numIdle</th>
          <th>closed</th>
          <th>log4jdbcEnabled</th>
          <th>error</th>
        </tr>
	  </thead>
      <tbody>
<%
	for (Map.Entry<String, DataSourceFactoryBean> entry
		: dataSourceRegistryFactoryBean.getDataSourceFactoryBeanMap().entrySet()) {

		pageContext.setAttribute("entry", entry);
%>
        <tr onclick="toggle('${entry.key}')" style="${entry.value.throwable != null ? 'cursor:pointer' : ''}">
          <td>${entry.key}</td>
          <td>${entry.value.basicDataSource.driverClassName}</td>
          <td>${entry.value.basicDataSource.url}</td>
          <td>${entry.value.basicDataSource.username}</td>
          <td><span title="${entry.value.basicDataSource.password}">****</span></td>
          <td>${entry.value.basicDataSource.initialSize}</td>
          <td>${entry.value.basicDataSource.maxActive}</td>
          <td>${entry.value.basicDataSource.numActive}</td>
          <td>${entry.value.basicDataSource.numIdle}</td>
          <td>${entry.value.basicDataSource.closed}</td>
          <td>${entry.value.log4jdbcEnabled}</td>
          <td>${entry.value.throwable != null}</td>
        </tr>
<%
		if (entry.getValue().getThrowable() != null) {
%>
		<tr id="${entry.key}" style="display:none;">
		  <td colspan="12">
		    <pre><%entry.getValue().getThrowable().printStackTrace(new PrintWriter(out));%>
			</pre>
		  </td>
		</tr>
<%
		}
	}
%>
      </tbody>
    </table>
  </body>
</html>
