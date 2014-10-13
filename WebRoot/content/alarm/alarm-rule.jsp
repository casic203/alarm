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
	
	<div id="region_dialog" style="display: none;">
	
		<div id="dlg_add" style="padding:10px 20px">
			<div class="ftitle">报警规则新建</div>
			<br>
			<form id="fm_add" method="post" novalidate>
				<div class="fitem">
					<label>设备编号:</label>
					<input id="dev-code-add" name="deviceid" >
				</div>
				<div class="fitem">
					<label>传感器类型编码:</label>
					<input id="sensor-code-add" name="sensorcode" >
				</div>
				<div class="fitem">
					<label>传感器参数名称:</label> <input id="param-code-add" name="paramcode" />
				</div>
				<div class="fitem">
					<label>警限LL:</label> <input id="dev-rulell-add" name="secullval" />
				</div>
				<div class="fitem">
					<label>警限L:</label> <input id="dev-rulel-add" name="seculval" />
				</div>
				<div class="fitem">
					<label>警限OK:</label> <input id="dev-ruleok-add" name="secuokval" />
				</div>
				<div class="fitem">
					<label>警限H:</label> <input id="dev-ruleh-add" name="secuhval" />
				</div>
				<div class="fitem">
					<label>警限HH:</label> <input id="dev-rulehh-add" name="secuhhval" />
				</div>
			</form>
		</div>
		<div id="dlg_edit" style="padding:10px 20px">
			<div class="ftitle">报警规则编辑</div>
			<br>
			<form id="fm_edit" method="post" novalidate> 
				<div class="fitem">
					<label>警限LL:</label> <input id="dev-rulell-edit" name="secullval" />
				</div>
				<div class="fitem">
					<label>警限L:</label> <input id="dev-rulel-edit" name="seculval" />
				</div>
				<div class="fitem">
					<label>警限OK:</label> <input id="dev-ruleok-edit" name="secuokval" />
				</div>
				<div class="fitem">
					<label>警限H:</label> <input id="dev-ruleh-edit" name="secuhval" />
				</div>
				<div class="fitem">
					<label>警限HH:</label> <input id="dev-rulehh-edit" name="secuhhval" />
				</div>
			</form>
		</div>
		
	</div>
</body>
</html>

<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/alarm-rule.js"></script>