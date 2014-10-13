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
	<div id="win_query" style="padding:10px 20px;background-color: #FAFAFA;">
		<div class="ftitle">工单信息</div>
		<br>
		<form id="fm_query" method="post">
			<input type="hidden" name="id">
			<table>
				<tr>
					<td>工单号</td>
					<td><input id="query_sheet" style="width: 100%" /></td>
					<td>设备</td>
					<td><input id="query_device"  editable="false"/></td>
				</tr>
				<tr>
					<td>开始时间</td>
					<td><input id="query_begin" class="easyui-datebox" editable="false"/></td>
					<td>结束时间</td>
					<td><input id="query_end" class="easyui-datebox" editable="false"/></td>
				</tr>
				<tr>
					<td colspan="6" style="text-align: right; padding: 5;"><a id="query_ok_btn" href="#">确定</a> <a id="query_cls_btn" href="#" onclick="javascript:$('#win_query').window('close');">关闭</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="region_dialog" style="display: none;">
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/work-sheet-chk.js"></script>
