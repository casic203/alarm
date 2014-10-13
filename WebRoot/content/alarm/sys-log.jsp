<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}//s/jquery-easyui-1.3.2/themes/frm.css" />
</head>
<body style="text-align: center;">

	<table id="dg"></table>

	<div id="#region_dialog"></div>
	<div id="dlg_query" title="查询" class="easyui-dialog" style="padding:20;width: 300px;"
		data-options="border : true,closed : true">
		<form id="fm_query" method="post">
			<div class="fitem">
				<label>操作人:</label> <input id="person_query"/>
			</div>
			<div class="fitem">
				<label>开始时间:</label> <input id="beginTime_query" class="easyui-datebox" editable="false"/>
			</div>
			<div class="fitem">
				<label>结束时间:</label> <input id="endTime_query" class="easyui-datebox" editable="false"/>
			</div>
		</form>
	</div>

</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/sys-log.js"></script>
