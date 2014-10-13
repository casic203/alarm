<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}//s/jquery-easyui-1.3.2/themes/frm.css" />
</head>
<body style="text-align: center;">
	<table id="dg"></table>
	<div id="region_dialog" style="display: none;">
		<div id="dlg_add" style="padding:10px 20px">
			<div class="ftitle">设备类型信息</div><br>
			<form id="fm_add" method="post" novalidate>
				<div class="fitem">
					<label>编码:</label>
					<input id="type-code-add" name="typeCode" required="true">
				</div>
				<div class="fitem">
					<label>名称:</label>
					<input id="type-name-add" name="typeName" required="true">
				</div>
				<div class="fitem">
					<label>路径:</label>
					<input id="type-location-add" name="location" required="true">
				</div>
			</form>
		</div>
	
		<div id="dlg_edit" style="padding:10px 20px">
			<div class="ftitle">设备类型信息</div><br>
			<form id="fm_edit" method="post" novalidate>
				<div class="fitem">
					<label>编码:</label>
					<input id="type-code-edit" name="typeCode" required="true">
				</div>
				<div class="fitem">
					<label>名称:</label>
					<input id="type-name-edit" name="typeName" required="true">
				</div>
				<div class="fitem">
					<label>路径:</label>
					<input id="type-location-edit" name="location" required="true">
				</div>
			</form>
		</div>
	
		<div id="dlg_query" style="padding:10px 20px">
			<div class="ftitle">设备类型信息</div><br>
			<form id="fm_query" method="post" novalidate>
				<div class="fitem">
					<label>编码:</label>
					<input id="type-code-query">
				</div>
				<div class="fitem">
					<label>名称:</label>
					<input id="type-name-query">
				</div>
			</form>
		</div>
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/device-type.js"></script>
