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
<!--jqplot插件-->
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jqplot/jquery.jqplot.css"/
</head>
<body style="text-align: center;">

	<!--光纤-->
	<div style="margin-top:25px">
		<div>
			<table id="fiberInfoTbl"></table>
		</div>
		<div id="fiberHistoryData"
			style="height:390px; padding:10px; width:600px; text-align:left">
			<!--<label style="font-size:12px;">距离刷新时间还剩：<span id="countdown"></span></label>-->
			<!--光纤最新曲线（扰动、温度,应力）-->
			<div id="fiberRecord" style="width:903px;height:360px;"></div>
		</div>
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
<script src="${ctx}/s/jqplot/plugins/jqplot.highlighter.js"
	type="text/javascript"></script>
<!--[if lte IE 8]><script type="text/javascript" src="${ctx}/s/jqplot/excanvas.min.js"></script><![endif]-->

<script type="text/javascript"
	src="${ctx}/s/justgage/raphael.2.1.0.min.js"></script>
<script type="text/javascript"
	src="${ctx}/s/justgage/justgage.1.0.1.min.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/featureDisplay.js"></script>
<script type="text/javascript">
	document.onreadystatechange = function() { // 当页面加载状态改变的时候执行这个方法
		if (document.readyState == "complete") { // 当页面加载完成之后执行

			var featureName = window.top.document
					.getElementById("currentFeatureName").value;

			//显示光纤设备信息
			var fiberDevicePropName = window.top.document
					.getElementById("fiberDevicePropName").innerHTML;
			var fiberDevicePropValue = window.top.document
					.getElementById("fiberDevicePropValue").innerHTML;
			fiberDevicePropName = eval('(' + fiberDevicePropName + ')');
			fiberDevicePropValue = eval('(' + fiberDevicePropValue + ')');
			$("#fiberInfoTbl").datagrid({
				title:'光纤信息',
				columns : fiberDevicePropName,
				data : fiberDevicePropValue,
				singleSelect : true,
				onAfterRender : function(target) {
				}
			});

			//光纤最新曲线（压力,温度,振动）
			$
					.ajax({
						url : "/alarm/alarm/nk-gx-curve!getDeviceRealtimeData.do",
						type : "POST",
						data : {
							'model.devCode' : featureName
						},
						success : function(data) {
							try {
								var labelArray = [ '压力', '温度', '振动' ];
								var stress = [], temperature = [], vibrating = [];
								var data = eval('(' + data + ')');
								var distanceArray = data.stress.distance
										.split(",");
								var stressDataArray = data.stress.data
										.split(",");
								var temperatureDataArray = data.temperature.data
										.split(",");
								var vibratingDataArray = data.vibrating.data
										.split(",");

								for ( var i = 0; i < distanceArray.length; i++) {
									stress.push([ parseFloat(distanceArray[i]),
											parseFloat(stressDataArray[i]) ]);
									temperature
											.push([
													parseFloat(distanceArray[i]),
													parseFloat(temperatureDataArray[i]) ]);
									vibrating
											.push([
													parseFloat(distanceArray[i]),
													parseFloat(vibratingDataArray[i]) ]);
								}

								$("#fiberRecord").html("");
								var jqplot = $
										.jqplot(
												'fiberRecord',
												[ stress, temperature,
														vibrating ],
												{
													title : '光纤最新曲线（压力,温度,振动)',
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
															tickOptions : {
																formatString : '%.5f'
															}
														},
														yaxis : {
															tickOptions : {
																formatString : '%.3f'
															}
														}
													},
													legend : {
														show : true,
														location : 'e',
														labels : labelArray
													},
													highlighter : {
														show : true,
														sizeAdjust : 7.5
													}
												});
							} catch (e) {
							}
						}
					});
		}
	};
</script>
