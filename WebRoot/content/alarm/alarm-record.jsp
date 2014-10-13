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
	<div id="dlg" style="padding:10px 20px;">
		<div class="ftitle">报警信息</div>
		<br>
		<form id="fm" method="post" novalidate>
			<table>
				<tr>
					<td class="fitem">
						<label style="font-size:14px;">报警人:</label> 
						<input name="sendPerson" />
					</td>
					<td class="fitem">
						<label style="font-size:14px;">联系方式:</label> 
						<input name="sendContact" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="fitem"><label style="font-size:14px;">设备:</label> <input id="device" name="device.id" /></td>
					<td class="fitem"><label style="font-size:14px;">接警人:</label> <input id="acceptPerson" name="acceptPerson.id" /></td>
					<td class="fitem"><label style="font-size:14px;">接警方式:</label> <input id="alarmType" name="alarmType.id" /></td>
				</tr>
				<tr>
					<td colspan="3" class="fitem"><label style="font-size:14px;">内容:</label> <textarea id="message" name="message" /></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="export-report-dlg" class="easyui-dialog" style="width:600px;height:120px;padding:10px 20px;" closed="true" buttons="#export-dlg-buttons">
		<!--  <div class="ftitle">导出报警信息报表</div>-->
		<table>
			<tr>
				<td class="fitem" required="true"><label style="font-size:14px;">报警记录日期范围:</label> <input id="alarmBeginDate" type="text" class="easyui-datebox" required="required" /> <label style="font-size:14px;">~</label> <input id="alarmEndDate" type="text" class="easyui-datebox" required="required" /></td>
			</tr>
		</table>
	</div>
	<div id="export-dlg-buttons">
		<a href="#" id="export-ok" class="easyui-linkbutton" iconCls="icon-ok">确定</a> <a href="#" id="export-cancel" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#export-report-dlg').dialog('close')">返回</a>
	</div>
</body>
</html>
<script type="text/javascript">
	/**
	 * 导出报表
	 */
	function exportReport() {
		$("#export-report-dlg").dialog("open").dialog("setTitle", "导出报警信息报表");
		$("#export-ok").attr("onclick", "doExport()");
	}

	function doExport() {
		var beginDate = $('#alarmBeginDate').datetimebox('getValue');
		var endDate = $('#alarmEndDate').datetimebox('getValue');
		//alert("beginDate:" + beginDate);
		//alert("endDate:" + endDate);

		$.ajax({
					type : "POST",
					url : "alarm-record-ext!generatorReport.do?model.alarmBeginDateStr="
							+ beginDate + "&model.alarmEndDateStr=" + endDate,
					dataType : "html",
					success : function(data) {
						//alert("data" + data);
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
					}
				});
	}
</script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/device.js"></script>
