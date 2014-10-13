<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}//s/jquery-easyui-1.3.2/themes/frm.css" />
<style type="text/css">
table,td,tr {
	font-size: 12px;
}
</style>
</head>
<body style="text-align: center">
	<table id="dg"></table>
	
	<div id="region_dialog" style="display: none;">
	<div id="win_add" style="padding:10px 20px;background-color: #FAFAFA;">
		<div class="ftitle">工单信息</div>
		<br>
		<form id="fm_add" method="post">
			<table>
				<tr>
					<td>设备</td>
					<td><input id="add_device" name="device.id" editable="false" required="true"/></td>
					<td>报警记录</td>
					<td><input id="add_record" name="alarmRecord.id" editable="false" required="true"/></td>
					<td>负责人</td>
					<td><input id="add_charger" name="charger" editable="false" required="true"/></td>
				</tr>
				<tr>
					<td>开始时间</td>
					<td><input id="add_begin" name="beginDate" class="easyui-datebox" editable="false" required="true"/></td>
					<td>结束时间</td>
					<td><input id="add_end" name="endDate" class="easyui-datebox" editable="false" required="true"/></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>故障现象</td>
					<td colspan="5" style="text-align: center;"><textarea id="add_message" rows="6" style="width: 100%;"  editable="false"></textarea></td>
				</tr>
				<tr>
					<td>任务描述</td>
					<td colspan="5" style="text-align: center;"><textarea name="task" rows="6" style="width: 100%;"></textarea></td>
				</tr>
				<tr>
					<td colspan="6" style="text-align: right; padding: 5;"><a id="add_ok_btn" href="#">确定</a> <a id="add_cls_btn" href="#" onclick="javascript:$('#win_add').window('close');">关闭</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="win_edit" style="padding:10px 20px;background-color: #FAFAFA;">
		<div class="ftitle">工单信息</div>
		<br>
		<form id="fm_edit" method="post">
			<input type="hidden" name="id">
			<table>
				<tr>
					<td>负责人</td>
					<td><input id="edit_charger" name="charger" editable="false" required="true"/></td>
					<td>开始时间</td>
					<td><input id="edit_begin" name="beginDate"  class="easyui-datebox" editable="false" required="true"/></td>
					<td>结束时间</td>
					<td><input id="edit_end" name="endDate" class="easyui-datebox" editable="false" required="true"></td>
				</tr>
				<tr>
					<td>任务描述</td>
					<td colspan="5" style="text-align: center;"><textarea name="task" rows="6" style="width: 100%;"></textarea></td>
				</tr>
				<tr>
					<td colspan="6" style="text-align: right; padding: 5;"><a id="edit_ok_btn" href="#">确定</a> <a id="edit_cls_btn" href="#" onclick="javascript:$('#win_edit').window('close');">关闭</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="win_query" style="padding:10px 20px;background-color: #FAFAFA;">
		<div class="ftitle">工单信息</div>
		<br>
		<form id="fm_query" method="post">
			<input type="hidden" name="id">
			<table>
				<tr>
					<td>工单号</td>
					<td colspan="3"><input id="query_sheet" style="width: 100%" /></td>
				</tr>
				<tr>
					<td>负责人</td>
					<td><input id="query_charger" editable="false"/></td>
					<td>设备</td>
					<td><input id="query_device" editable="false"/></td>
				</tr>
				<tr>
					<td>开始时间</td>
					<td><input id="query_begin" class="easyui-datebox" editable="false"/></td>
					<td>结束时间</td>
					<td><input id="query_end"  class="easyui-datebox" editable="false"/></td>
				</tr>
				<tr>
					<td colspan="6" style="text-align: right; padding: 5;"><a id="query_ok_btn" href="#">确定</a> <a id="query_cls_btn" href="#" onclick="javascript:$('#win_query').window('close');">关闭</a></td>
				</tr>
			</table>
		</form>
	</div>
	</d
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/work-sheet-add.js"></script>
