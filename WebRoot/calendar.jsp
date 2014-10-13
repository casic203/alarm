<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}//s/jquery-easyui-1.3.2/themes/frm.css" />
<style type="text/css">
table,td,tr {
	font-size: 12px;
}

td {
	padding: 3px 3px 3px 3px;
}

input {
	width: 120px;
}
</style>
</head>
<body style="text-align: center;">
	<table>
		<tr>
			<td>开始时间</td>
			<td><input id="txt_time_begin" type="text"
				class="easyui-datebox"></input></td>
		</tr>
		<tr>
			<td>结束时间</td>
			<td><input id="txt_time_end" type="text" class="easyui-datebox"></input></td>
		</tr>
		<tr>
			<td colspan="2" style="text-align: right;">
				<a id="time_sh_btn" href="#" class="easyui-linkbutton"	data-options="iconCls:'icon-search'">确定</a></td>
		</tr>
	</table>
</body>
</html>
<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
<!--
	parent.clear_data_grid();
	$("#time_sh_btn").bind("click", function() {
		var begin = $("#txt_time_begin").datebox("getValue");
	   	var end = $("#txt_time_end").datebox("getValue");
		var scope=getQueryString("scope");
		if(scope=="all"){
			parent.query_all_area_alarm_record_unactive(begin,end);
		}
		if(scope=="part"){
			parent.query_part_area_alarm_record_unactive(begin,end);
		}
	   	var index = parent.layer.getFrameIndex(window.name);
	   	parent.layer.close(index);
	});
	function getQueryString(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
			return unescape(r[2]);
		return null;
	}
	
//-->
</script>
