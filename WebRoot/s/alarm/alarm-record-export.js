	/**
	 * 导出报表
	 */
	function exportReport() {
		addTab('导出报警信息报表', '');
		$("#export-report-dlg").dialog("open").dialog("setTitle", "导出报警信息报表");
	}

	function doExport() {
		var beginDate = $('#alarmBeginDate').datetimebox('getValue');
		var endDate = $('#alarmEndDate').datetimebox('getValue');

		$.ajax({
					type : "POST",
					url : "alarm/alarm-record-ext!generatorReport.do?model.alarmBeginDateStr="
							+ beginDate + "&model.alarmEndDateStr=" + endDate,
					dataType : "html",
					success : function(data) {
						try {
						    var testData = eval('(' + data + ')');
						    if(!testData.success) {
							    alert('没有记录');
							    return;
						    }
						} catch(e){}
						
						$("#alarmRecordExportHtml").html(data);
//						var winObj = window.open("");
//						winObj.document.write("<html>");
//						winObj.document.write("<head>");
//						winObj.document.write("<title>报警记录打印页面<\/title>");
//						winObj.document.write("<\/head>");
//						winObj.document.write("<body>");
//						winObj.document.write("<div id=\"printBtn\">");
//						winObj.document
//								.write("<button onclick=\"javascript:window.print();\">打印<\/button>");
//						winObj.document.write("<\/div>");
//						winObj.document.write("<div id=\"printArea\">");
//						winObj.document.write(data);
//						winObj.document.write("<\/div>");
//						winObj.document.write("<\/body>");
//						winObj.document.write("<\/html>");
//						winObj.document.close();
						$.layer({
							type : 2,
							title : "报警信息报表",
							offset : [ '20px', '' ],
							area : [ "900px", "600px" ],
							iframe : {
								src : 'common/layout/alarm-record-export.jsp'
							}
						});
					}
				});
	}