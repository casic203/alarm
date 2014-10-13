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
		<div class="ftitle">数据库管理员信息</div>
		<br>
		<form id="fm_add" method="post" novalidate>
			<div class="fitem">
				<label>账号:</label> <input id="code-add" name="code" />
			</div>
			<div class="fitem">
				<label>姓名:</label> <input id="name-add" name="name" />
			</div>
		</form>
	</div>
	<div id="dlg_edit" style="padding:10px 20px">
		<div class="ftitle">数据库管理员信息</div>
		<br>
		<form id="fm_edit" method="post" novalidate>
			<div class="fitem">
				<label>账号:</label> <input id="code-edit" name="code" />
			</div>
			<div class="fitem">
				<label>姓名:</label> <input id="name-edit" name="name" />
			</div>
		</form>
	</div>
	<div id="dlg_query" style="padding:10px 20px">
		<div class="ftitle">数据库管理员信息</div>
		<form id="fm_query" method="post" novalidate>
			<div class="fitem">
				<label>账号:</label> <input id="txt_code_query" name="code" />
			</div>
			<div class="fitem">
				<label>姓名:</label> <input id="txt_name_query" name="name" />
			</div>
		</form>
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/data-base-operator.js"></script>