<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/frm.css" />
		<!--jqplot插件-->
<link rel="stylesheet" type="text/css" href="${ctx}/s/jqplot/jquery.jqplot.css" /
</head>
<body style="text-align: center;">
		<div>

		</div>
		<div style="margin-top:25px">
			<div id="xtGasRealTimeData" style="float:left; ">

			</div>
			<!--雨水管线液位历史曲线-->
			<div id="alarmLiquidRecord" style="width:690px;height:360px;"></div>
		</div>
</body>
</html>

<script src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<%--jqplot插件--%>
<script src="${ctx}/s/jqplot/jquery.jqplot.js" type="text/javascript"></script>

<script src="${ctx}/s/jqplot/plugins/jqplot.dateAxisRenderer.min.js"
	type="text/javascript"></script>
<script src="${ctx}/s/jqplot/plugins/jqplot.canvasTextRenderer.min.js"
	type="text/javascript"></script>
<script
	src="${ctx}/s/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js"
	type="text/javascript"></script>
<script src="${ctx}/s/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"
	type="text/javascript"></script>
<script src="${ctx}/s/jqplot/plugins/jqplot.barRenderer.min.js"
	type="text/javascript"></script>
<!--[if lte IE 8]><script type="text/javascript" src="${ctx}/s/jqplot/excanvas.min.js"></script><![endif]-->

<script type="text/javascript"
	src="${ctx}/s/justgage/raphael.2.1.0.min.js"></script>
<script type="text/javascript"
	src="${ctx}/s/justgage/justgage.1.0.1.min.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/featureDisplay.js"></script>
<script type="text/javascript">
	//document.onreadystatechange = function() { 
		//if (document.readyState == "complete") { 

			var featureName = window.top.document.getElementById("currentFeatureName").value;
			//显示雨水管线历史监测数据曲线图
			$.ajax({
				url : "../../alarm/device-realtime-data!getAlarmLiquidHistory.do?model.dbCord=" + featureName,
				type : "POST",
				success : function(data) {
					var data = eval('(' + data + ')');
					var dataArray = [];
					var labelArray = [ '雨水管线液位历史曲线' ];
					for ( var i = 0; i < data.length; i++) {
						var uptime = data[i].uptime;
						var liquid = data[i].liquid;					
						dataArray.push([ uptime, liquid ]);
					}					
					$("#alarmLiquidRecord").html("");
					var jqplot = $.jqplot('alarmLiquidRecord', [ dataArray ], {
						title : '雨水管线液位历史曲线',
						series : [ {
							lineWidth : 2,
							markerOptions : {
								style : 'dimaon'
							}
						} ],
						axesDefaults : {
							tickRenderer : $.jqplot.CanvasAxisTickRenderer,
							tickOptions : {
								angle : -30,
								fontSize : '10pt'
							}
						},
						axes : {
							xaxis : {
								renderer : $.jqplot.CategoryAxisRenderer
							}
						},
						legend : {
							show : true,
							location : 'e',
							labels : labelArray
						},
						cursor:{
							show:true,
							showTooltip:true
						},
						highlighter: {
							show:true,
							sizeAdjust:7.5
						}
					});
					//jqplot.resetZoom();
				}
			});
		//}
	//};
</script>
