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
	<input id='hiden_dev_id' type="hidden">
	<input id='hiden_person_id' type="hidden">
	<input id='hiden_active' type="hidden">
	<div id="region_dialog" style="display: none;">
		<div id="dlg_add" class="easyui-dialog"  style="padding:10px 20px; width:600px;" data-options="title : '新增报警记录',	border : true,closed : true"> 
			<div class="ftitle">报警信息</div>
			<br>
			<form id="fm_add" method="post" novalidate>
				<table>
					<tr>
						<td class="fitem"><label style="font-size:14px;">报警人:</label> <input name="sendPerson" /></td>
						<td class="fitem"><label style="font-size:14px;">联系方式:</label> <input name="sendContact" /></td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td class="fitem"><label style="font-size:14px;">设备:</label> <input id="device_add" name="device.id" /></td>
						<td class="fitem"><label style="font-size:14px;">接警人:</label> <input id="acceptPerson_add" name="acceptPerson.id" /></td>
						<td class="fitem"><label style="font-size:14px;">接警方式:</label> <input id="alarmType_add" name="alarmType.id" /></td>
					</tr>
					<tr>
						<td colspan="3" class="fitem">
							<label style="font-size:14px;">内容:</label> 
							<textarea id="message" name="message" style="width: 100%"></textarea>
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg_query"  class="easyui-dialog" style="padding:10px 20px; width:300px;" data-options="title : '查询报警记录',border : true,closed : true">
			<div class="ftitle">报警信息</div>
			<br>
			<form id="fm_query" method="post">
				<div class="fitem">
					<label style="font-size:14px;">设备:</label> <input id="device_query" />
				</div>
				<div class="fitem">
					<label style="font-size:14px;">接警人:</label> <input id="acceptPerson_query" />
				</div>
				<div class="fitem">
					<label style="font-size:14px;">任务状态:</label>
					<select id="alarmStatus_query" class="easyui-combobox" editable="false">
						<option selected></option>   
						<option value="1">未处理</option>   
						<option value="0">已处理</option>  
					</select> 
				</div>
			</form>
		</div>
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/add-alarm-record.js"></script>