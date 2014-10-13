<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/frm.css" />
</head>
<body style="text-align: center;">
	<table id="dg"></table>
	
	<div id="region_dialog" style="display: none;">
	<div id="dlg_add" style="padding:10px 20px">
		<div class="ftitle">传感器标准配置信息</div><br>
		<form id="fm_add" method="post" novalidate> 
			<div class="fitem">
				<label>参数代码:</label>
				<input id="param-standardcol-add" name="standardcol" >
			</div>
			<div class="fitem">
				<label>参数名称:</label>
				<input id="param-standardcolname-add" name="standardcolname" >
			</div>  
			<div class="fitem">
				<label>参数项数据类型:</label>
				<input id="param-coldatatype-add" name="coldatatype" >
			</div> 			
			<div class="fitem">
				<label>参数项长度:</label>
				<input id="param-collen-add" name="collen">  
			</div> 
		</form>
	</div>
	
	<div id="dlg_edit" style="padding:10px 20px">
		<div class="ftitle">参数信息</div><br>
		<form id="fm_edit" method="post" novalidate> 
			<div class="fitem">
				<label>参数名称:</label>
				<input id="param-standardcolname-edit" name="standardcolname" >
			</div> 
			<div class="fitem">
				<label>参数项数据类型:</label>
				<input id="param-coldatatype-edit" name="coldatatype" >
			</div> 
			<div class="fitem">
				<label>参数项长度:</label>
				<input id="param-collen-edit" name="collen" >
			</div> 
		</form>
	</div>
	
	<div id="dlg_query" style="padding:10px 20px">
		<div class="ftitle">参数信息</div><br>
		<form id="fm_query" method="post" novalidate>  
			<div class="fitem">
				<label>参数状态:</label>
				<input id="param-status-query">  
			</div> 
		</form>
	</div>
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/device-sproperty.js"></script>
