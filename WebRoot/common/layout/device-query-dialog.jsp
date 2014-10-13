<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/frm.css" />
</head>
<body style="text-align: center;">
<%-- 设备查询 --%>
<div id="device-query-dialog">
	<div style="margin-top:15px; font-size:13px;">
		<div style="float:left">设备名: <input type="text" id="deviceQueryKeyText" /></div>
		<div> 
		    <a href="#" iconCls="icon-search" class="easyui-linkbutton" onclick="deviceQuery()">查询</a>
		</div>
	</div>
</div>
</body>
</html>
<script src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript">
	var gridData = window.top.document.getElementById("deviceQuery").value;
	gridData = eval("(" + gridData + ")");
	$("#deviceQueryKeyText").combobox({
	    data:gridData,   
	    valueField:'id',    
	    textField:'text'
	});
	
	function deviceQuery() {
		parent.deviceQuery($("#deviceQueryKeyText").combobox("getText"));
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	}
</script>
