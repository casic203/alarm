<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div id="printBtn">
	    <button onclick="javascript:window.print();">打印</button>
	</div>
	<div id="content"></div>
</body>
</html>
<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript">
    var alarmContent = window.top.document.getElementById("alarmRecordExportHtml").innerHTML;
    $("#content").html(alarmContent);
</script>
