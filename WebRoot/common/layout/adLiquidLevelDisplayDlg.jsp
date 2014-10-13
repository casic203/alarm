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

	<!--液位监测仪-->
	<div title=""
		style="width:890px;min-height:480px;height:auto;padding:10px;background:#fafafa;"
		data-options="iconCls:'icon-search', closable:false, collapsible:true,minimizable:true,maximizable:true">
		<div>
			<table id="adLiquidLevelDeviceInfoTbl" class="easyui-datagrid">
			</table>
		</div>
		<div style="margin-top:25px">
			<div id="adLiquidLevelRealTimeData" style="float:left; ">
				<label style="font-size:12px;">距离刷新时间还剩：<span
					id="liquidCountDown"></span></label>
				<!--电池电量-->
				<div id="liquidCell" style="width:150px; height:100px;"></div>
			</div>
			<!--液位曲线-->
			<div id="adLiquidLevelRecord" style="width:690px;height:360px;"></div>
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
			$("#adLiquidLevelDeviceInfoTbl").datagrid({
				columns : deviceGridColumn,
				url:encodeURI("/alarm/alarm/ad-dj-liquid!getDeviceProps.do?model.devCode=" + featureName + "&time=" + new Date())
			});
			
			//显示电池电量
			var liquidCellGage = new JustGage({
		        id: "liquidCell", 
		        value: 0, 
		        min: 0,
		        max: 1000,
		        title: "电量",
		        label: "",  
		        startAnimationTime: 2000,
		        startAnimationType: ">",
		        refreshAnimationTime: 1000,
		        refreshAnimationType: "bounce"  
			});
			var second = 5;
			intervalId = setInterval(function() {
				if(second != 0) {
					$("#liquidCountDown").html(second + "秒");
					second --;
					return;
				} else {
					$("#liquidCountDown").html(second + "秒");
					second = 5;
				    $.ajax({
					      url:"/alarm/alarm/ad-dj-liquid!getDeviceCell.do",
					      type : "POST",
					      data: {
					    	  'model.devCode':featureName
					      },
					      success : function(data) {
						      var dataContent = eval('(' + data + ')');
						      var cell = dataContent.cell;
						      if(typeof(cell) != 'undefined' && cell != null) {
						    	  liquidCellGage.refresh(cell);   
						      }
					  }
				    });
				}
				
			}, 1000);
			
			
			//显示液位曲线
			$.ajax({
				  url:"/alarm/alarm/ad-dj-liquid!getLiquidHistoryData.do",
				  type : "POST",
			      data: {
			    	  'model.devCode':featureName
			      },
			      success:function(data) {
			    	  var data = eval('(' + data + ')');
			    	  var label = ['液位数据'];
			    	  var dataArray = [];
			    	  for(var i = 0;i < data.length; i ++) {
			    		  var uptime = data[i].uptime;
			    		  var liquidData = data[i].liquidData;
			    		  dataArray.push([uptime, liquidData]);
			    	  }
			    		  
					  $("#adLiquidLevelRecord").html("");
					  var jqplot = $.jqplot('adLiquidLevelRecord', [dataArray], {
							title:'液位曲线',
							series:[{
								lineWidth:2,
								markerOptions:{
									style:'dimaon'
								}
							}],
							axesDefaults:{
								tickRenderer:$.jqplot.CanvasAxisTickRenderer,
							    tickOptions:{
								    angle:-30,
								    fontSize:'10pt'
							    }
						    },
							axes : {
								xaxis : {
									renderer : $.jqplot.CategoryAxisRenderer
								}
							},
							legend:{
								show:true,
								location:'e',
								labels:label
							}
						});
			      }
			});
		}
	};
</script>
