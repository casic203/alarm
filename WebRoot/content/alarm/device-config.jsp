<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/frm.css" />
</head>
<body style="text-align: center;">
	<table id="dg"></table>
	<table id="deviceConfigTbls" ></table>  
	
	<div id="region_dialog" style="display: none;">
	<div id="dlg_edit" style="padding:10px 20px">
		<div class="ftitle">设备信息</div>
		<br>
		<form id="fm_edit" method="post"> 
				
			<div class="fitem"> 
				<label>传感器id： </label> <input id="sensor-code-edit"  name="dcmodel.sensorid" style="width:140px;" />
			</div>  
			<div class="fitem">
				<label>采集间隔： </label> <input id="sensor-cjjg-edit" name="cjjg" style="width:140px;" />
			</div>   
			<div class="fitem">
				<label>发送次数： </label> <input id="sensor-fscs-edit" name="fscs" style="width:140px;" />
			</div>  
			<div class="fitem">
				<label>密集开始时间： </label> <input id="sensor-mjkssj-edit" name="mjkssj" style="width:140px;" />
			</div>  
			<div class="fitem">
				<label>密集间隔： </label> <input id="sensor-mjjg-edit" name="mjjg" style="width:140px;" />
			</div> 
			<div class="fitem">
				<label>密集样本数： </label> <input id="sensor-mjybs-edit" name="mjybs" style="width:140px;" />
			</div>  
			<div class="fitem">
				<label>无线开启时间： </label> <input id="sensor-wxkqsj-edit" name="wxkqsj" style="width:140px;" />
			</div> 
			<div class="fitem">
				<label>无线关闭时间： </label> <input id="sensor-wxgbsj-edit" name="wxgbsj" style="width:140px;" />
			</div> 
			<div class="fitem">
				<label>松散开始时间： </label> <input id="sensor-sskssj-edit" name="sskssj" style="width:140px;" />
			</div>  
			<div class="fitem">
				<label>松散间隔： </label> <input id="sensor-ssjg-edit" name="ssjg" style="width:140px;" />
			</div> 
			<div class="fitem">
				<label>松散样本数： </label> <input id="sensor-ssybs-edit" name="ssybs" style="width:140px;" />
			</div>  
		</form>
	</div>
 
	<div id="dlg_query" class="easyui-dialog" style="padding:10px 20px">
		<div class="ftitle">设备信息</div>
		<br>
		<form id="fm_query" method="post" novalidate>
			<div class="fitem">
				<label>编号:</label> <input id="dev-code-query" name="devCode" style="width:140px;" />
			</div>
			<div class="fitem">
				<label>名称:</label> <input id="dev-name-query" name="devName" style="width:140px;" />
			</div>
			<div class="fitem">
				<label>类型:</label> <input id="dev-type-query" name="deviceType.id" />
			</div>
			 
		</form>
	</div>
	</div>          
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/device-config.js"></script>
