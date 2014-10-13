<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}//s/jquery-easyui-1.3.2/themes/frm.css" />
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/contact-book.js"></script>
<script type="text/javascript">
	
</script>
</head>
<body style="text-align: center;">
	<table id="dg"></table>
	<div id="dlg_edit" title="编辑通讯方式" style="padding:20;">
		<div class="ftitle">通讯记录</div>
		<br>
		<form id="fm_edit" method="post" novalidate>
			<div class="fitem">
				<label>姓名:</label> 
				<input id="combobox-edit-person" name="acceptPerson.id" style="width:140px;" data-options="required:true" />
			</div>
			<div class="fitem">
				<label>接警方式:</label> 
				<input id="combobox-edit-alarm" name="alarmType.id" style="width:140px;" data-options="required:true" />
			</div>
			<div class="fitem">
				<label>联系方式:</label> 
				<input name="contact" style="width:140px;" class="easyui-validatebox" data-options="required:true" />
			</div>
		</form>
	</div>

	<div id="dlg_add" title="新增通讯方式" style="padding:20;">
		<div class="ftitle">通讯记录</div>
		<br>
		<form id="fm_add" method="post" novalidate>
			<div class="fitem" 　required="true">
				<label>姓名:</label> 
				<input id="combobox-add-person" name="acceptPerson.id" style="width:140px;" data-options="required:true" />
			</div>
			<div class="fitem" 　required="true">
				<label>接警方式:</label> 
				<input id="combobox-add-alarm" name="alarmType.id" style="width:140px;" data-options="required:true" />
			</div>
			<div class="fitem">
				<label>联系方式:</label> 
				<input name="contact" style="width:140px;" class="easyui-validatebox" data-options="required:true" />
			</div>
		</form>
	</div>
	
	<div id="dlg_query" title="查询通讯方式" style="padding:20;">
		<div class="ftitle">通讯记录</div>
		<br>
		<form id="fm_query" method="post" novalidate>
			<div class="fitem" 　required="true">
				<label>姓名:</label> 
				<input id="combobox-query-person" name="acceptPerson.id" style="width:140px;" data-options="required:true" />
			</div>
		</form>
	</div>
	
</body>
</html>
