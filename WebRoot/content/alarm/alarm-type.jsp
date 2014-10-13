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
	<div id="dlg_add" style="padding:10px 20px">
		<div class="ftitle">报警方式信息</div>
		<br>
		<form id="fm_add" method="post" novalidate>
			<div class="fitem">
				<label>编号:</label> <input id="alarm-code-add" name="alarmCode" />
			</div>
			<div class="fitem">
				<label>名称:</label> <input id="alarm-name-add" name="alarmName" />
			</div>
		</form>
	</div>
	<div id="dlg_edit" style="padding:10px 20px">
		<div class="ftitle">报警方式信息</div>
		<br>
		<form id="fm_edit" method="post" novalidate>
			<div class="fitem">
				<label>编号:</label> <input id="alarm-code-edit" name="alarmCode" />
			</div>
			<div class="fitem">
				<label>名称:</label> <input id="alarm-name-edit" name="alarmName" />
			</div>
		</form>
	</div>
</body>
</html>

<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/alarm-type.js"></script>