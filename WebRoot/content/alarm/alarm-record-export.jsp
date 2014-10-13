<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body style="text-align: center;">
	<div id="printBtn"></div>
	<div id="content"></div>
</body>
</html>

<script type="text/javascript">
	$.ajax({
				type : "POST",
				url : "alarm/alarm-record-ext!generatorReport.do?model.alarmBeginDateStr="
						+ beginDate + "&model.alarmEndDateStr=" + endDate,
				dataType : "html",
				success : function(data) {
					try {
						var testData = eval('(' + data + ')');
						if (!testData.success) {
							alert('没有记录');
							return;
						}
					} catch (e) {
					}

					$("#content").html(data);
					/*
					var winObj = window.open("");
					winObj.document.write("<html>");
					winObj.document.write("<head>");
					winObj.document.write("<title>报警记录打印页面<\/title>");
					winObj.document.write("<\/head>");
					winObj.document.write("<body>");
					winObj.document.write("<div id=\"printBtn\">");
					winObj.document
							.write("<button onclick=\"javascript:window.print();\">打印<\/button>");
					winObj.document.write("<\/div>");
					winObj.document.write("<div id=\"printArea\">");
					winObj.document.write(data);
					winObj.document.write("<\/div>");
					winObj.document.write("<\/body>");
					winObj.document.write("<\/html>");
					winObj.document.close();
					 */
				}
			});
</script>
