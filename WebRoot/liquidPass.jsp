<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'liquidPass.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
  </head>
  
  <body>
      <%-- 频次统计(wei)--%>
		<div id="fill-query-dialog">
			<div style="margin-top:15px;">
				<span><font style="font-size:13px;">分析起始时间: <br/></font>				
				</span>
				
				<span>
				<input class="easyui-datebox" id="upTime"
				         data-options="required:true,showSeconds:false" value="3/3/2014" style="width:150px"><br/><br/>
				</span>				
				<span><font style="font-size:13px;">分析结束时间: <br/></font>				
			    </span>				
				<span>
				<input class="easyui-datebox" id="endTime"
				         data-options="required:true,showSeconds:false" value="3/10/2014" style="width:150px"><br/><br/>
				</span>
				
		        <span><font style="font-size:13px;">过程浏览速度: <br/></font>				
			    </span>
				<span>				
				<select id="upYear" class="easyui-combobox" name="dept" style="width:80px;">
				<option value="1">X1</option> 
				<option value="2">X2</option> 
				<option value="4">X4</option>
				<option value="8">X8</option> 
				<option value="16">X16</option>
				</select>
				<font style="font-size:13px;">倍<br/><br/><br/></font>
				</span>
				<button id="myButton" type="button" onclick="fillAnaly();">进行分析</button>	
			 
			</div>
			<div style="margin-top:10px;">
			<%-- 
            <table id="query-result-datagrid  window.parent.myFunction();  fillStat() "></table>
           --%>
			</div>
		</div>
     
     
  </body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script><%--  --%>
<script src="${ctx}/s/alarm/liquidRecord.js" type="text/javascript"></script>
 
 
 