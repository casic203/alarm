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
			<table id="xtGasDeviceInfoTbl" class="easyui-datagrid">
			</table>
			<table id="xtGasDeviceConfigTbl" class="easyui-datagrid">
			</table> 
		</div>
		<div style="margin-top:25px">
			<div id="xtGasRealTimeData" style="float:left; ">
				<label style="font-size:12px;">距离刷新时间还剩：<span
					id="xtGasCountDown"></span></label>
				<!--显示燃气进站、出站压力、电量-->
				<div id="inPressure" style="width:150px; height:100px; "></div>
				<div id="outPressure" style="width:150px; height:100px;"></div>
				<div id="power" style="width:150px; height:100px;"></div>
			</div>
			<!--燃气浓度历史曲线-->
			<div id="gasDensityRecord" style="width:690px;height:360px;"></div>
		</div>
		
		<div id="rqparamConfigDlg" style="z-index:1;display:none;"> 
			<table>   
				<tr>
					<td class="fitem"><label style="font-size:14px;">采集间隔:</label> <input id="sensor-rqcjjg-edit" name="rqcjjg" style="width:140px;" /></td>
					<td class="fitem"><label style="font-size:14px;">发送次数:</label> <input id="sensor-rqfscs-edit" name="rqfscs" style="width:140px;" /></td>
 				</tr> 
			    <tr>
			    	<td colspan="3">
				 		<input id="rqdo_submitConfig_btn" type="button" value="确定" />
				 	</td> 
				</tr>
			</table> 
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
			var featureName = window.top.document
					.getElementById("currentFeatureName").value;
			var rqparamConfigDlgIndex;
			//显示设备信息
			$("#xtGasDeviceInfoTbl")
					.datagrid(
							{
								columns : deviceGridColumn,
								url : encodeURI("/alarm/alarm/xt-gas-display!getDeviceProps.do?model.devCode="
										+ featureName + "&time=" + new Date()),
								onClickRow:function(rowIndex, rowData){
									
								}
								 
							});
			$("#xtGasDeviceConfigTbl")
					.datagrid(
							{
								columns : rqConfigColumn,
								url : encodeURI("/alarm/alarm/xt-gas-display!checkDeviceOnline.do?model.devCode=" + featureName ) 
							});

			//显示燃气进站、出站压力、电量
			$("#inPressure").html("");
			$("#outPressure").html("");
			$("#power").html("");
			var inPressureGage = new JustGage({
				id : "inPressure",
				value : 0,
				min : 0,
				max : 1000,
				title : "燃气进站压力",
				label : "",
				startAnimationTime : 2000,
				startAnimationType : ">",
				refreshAnimationTime : 1000,
				refreshAnimationType : "bounce"
			});
			var outPressureGage = new JustGage({
				id : "outPressure",
				value : 0,
				min : 0,
				max : 1000,
				title : "燃气出站压力",
				label : "",
				startAnimationTime : 2000,
				startAnimationType : ">",
				refreshAnimationTime : 1000,
				refreshAnimationType : "bounce"
			});
			var powerGage = new JustGage({
				id : "power",
				value : 0,
				min : 0,
				max : 1000,
				title : "电量",
				label : "",
				startAnimationTime : 2000,
				startAnimationType : ">",
				refreshAnimationTime : 1000,
				refreshAnimationType : "bounce"
			});
			var second = 5;
			intervalId = setInterval(
					function() {
						if (second != 0) {
							$("#xtGasCountDown").html(second + "秒");
							second--;
							return;
						} else {
							$("#xtGasCountDown").html(second + "秒");
							second = 5;
							$
									.ajax({
										url : "../../alarm/xt-gas-display!getDeviceRealtimeData.do",
										type : "POST",
										data : {
											'model.devCode' : featureName
										},
										success : function(data) {
											var dataContent = eval('(' + data
													+ ')');
											var inPress = dataContent.inPress;
											var outPress = dataContent.outPress;
											var cell = dataContent.cell;
											if (typeof (inPress) != 'undefined'
													&& inPress != null) {
												inPressureGage.refresh(inPress);
											}
											if (typeof (outPress) != 'undefined'
													&& outPress != null) {
												outPressureGage
														.refresh(outPress);
											}
											if (typeof (cell) != 'undefined'
													&& cell != null) {
												powerGage.refresh(cell);
											}
										}
									});
						}

					}, 1000);

			//显示燃气浓度历史曲线
			$.ajax({
				url : "../../alarm/xt-gas-display!getStrengthHistoryData.do",
				type : "POST",
				data : {
					'model.devCode' : featureName
				},
				success : function(data) {
					var data = eval('(' + data + ')');
					var dataArray = [];
					var labelArray = [ '燃气浓度历史曲线' ];
					for ( var i = 0; i < data.length; i++) {
						var uptime = data[i].uptime;
						var strength = data[i].strength;
						dataArray.push([ uptime, strength ]);
					}

					$("#gasDensityRecord").html("");
					var jqplot = $.jqplot('gasDensityRecord', [ dataArray ], {
						title : '燃气浓度历史曲线',
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
