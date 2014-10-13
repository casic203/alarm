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
		<div class="ftitle">帧格式信息</div><br>
		<form id="fm_add" method="post" novalidate>
		 	<div class="fitem">
				<label>传感器类型:</label>
				<input id="dev-type-add" name="sensortype" >
			</div>
			<div class="fitem">
				<label>帧格式代码:</label>
				<input id="item-name-add" name="itemname" >
			</div>
			<div class="fitem">
				<label>帧格式名称:</label>
				<input id="item-code-add" name="itemnameCn" >
			</div>  
			<div class="fitem">
				<label>帧内容:</label>
				<input id="item-value-add" name="itemvalue" >
			</div>   
			<div class="fitem">
				<label>序号:</label>
				<input id="item-sortid-add" name="sortid">  
			</div>  			
		</form>
	</div>
	
	<div id="dlg_edit" style="padding:10px 20px">
		<div class="ftitle">数据帧信息</div><br>
		<form id="fm_edit" method="post" novalidate>
			<div class="fitem">
				<label>传感器类型:</label> 
				<input id="dev-type-edit" name="sensortype" >
			</div>
			<div class="fitem">
				<label>帧格式代码:</label> 
				<input id="item-name-edit" name="itemname" >
			</div>
			<div class="fitem">
				<label>帧格式名称:</label>
				<input id="item-code-edit" name="itemnameCn" >
			</div>  
			<div class="fitem">
				<label>帧内容:</label>
				<input id="item-value-edit" name="itemvalue" >
			</div>  
			<div class="fitem">
				<label>序号:</label>
				<input id="item-sortid-edit" name="sortid">  
			</div>
		</form>
	</div>
	
	<div id="dlg_query" style="padding:10px 20px">
		<div class="ftitle">数据帧信息</div><br>
		<form id="fm_query" method="post" novalidate>  
			<div class="fitem">
				<label>传感器类型:</label>
				<input id="dev-type-query">
			</div> 
		</form>
	</div>
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/device-sensorformat.js"></script>
