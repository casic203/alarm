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
			<div class="ftitle">设备传感器信息</div><br>
			<form id="fm_add" method="post" novalidate>
				<div class="fitem">
					<label>设备编码:</label>
					<input id="dev-code-add" name="deviceid" >
				</div>
				<div class="fitem">
					<label>传感器类型:</label>
					<input id="param-code-add" name="sensorcode" >
				</div>
				 
				<div class="fitem">
					<label>传感器编号:</label>
					<input id="param-defaultid-add" name="sensorid" >
				</div> 	 
			</form>
		</div>
		
		<div id="dlg_edit" style="padding:10px 20px">
			<div class="ftitle">设备传感器信息</div><br>
			<form id="fm_edit" method="post" novalidate>
				<div class="fitem"> 
					<input type="hidden" id="dev-code-edit" name="deviceid" > 
					<input type="hidden" id="param-code-edit" name="sensorcode" >
				</div>  
				<div class="fitem">
					<label>默认地址:</label>
					<input id="param-defaultid-edit" name="sensorid" >
				</div> 
			</form>
		</div>
		
		<div id="dlg_query" style="padding:10px 20px">
			<div class="ftitle">设备参数信息</div><br>
			<form id="fm_query" method="post" novalidate>  
				<div class="fitem">
					<label>设备类型名称:</label>
					<input id="param-name-query">
				</div> 
			</form>
		</div>
	</div
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/device-sensor.js"></script>
