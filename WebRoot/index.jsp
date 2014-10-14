﻿<%@page import="org.jasig.cas.client.validation.Assertion"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/frm.css" />
	<link type="text/css" href="${ctx}/s/scroll/style.css" rel="stylesheet" />

	<!--jqplot插件-->
	<link rel="stylesheet" type="text/css" href="${ctx}/s/jqplot/jquery.jqplot.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/index.css" />
	
	<script src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js" type="text/javascript"></script>
	<script src="${ctx}/s/jquery-tmpl/jquery.tmpl.min.js" type="text/javascript"></script>


	
	<%-- 对于三维控件接口的调用 --%>
	<%--  
    <script src="${ctx}/s/gis/scripts/GlobeControl.js" type="text/javascript"></script> 
    --%>
	<%-- 所有的业务数据处理方法 包括页面加载三维控件及数据的方法--%>
	<script src="${ctx}/s/gis/scripts/3dgis1.js" type="text/javascript"></script>
	<%--连接数据库--%>
	<script src="${ctx}/s/gis/scripts/sqlhelper.js" type="text/javascript"></script>
	<%--<script src="${ctx}/layer/jquery.min.js"></script>--%>
	<script src="${ctx}/s/layer/layer.min.js"></script>
	<script src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${ctx}/s/scroll/scroll.js"></script>
	<!-- 在线报警相关 -->
	<script src="${ctx}/s/myScroll/alarmScroll.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/s/myScroll/alarmOnlineDisplay.css" />
</head>
<body>
	<div class="easyui-layout" id="sys-content" style="width: 100%; height: 100%;" fit="true">
		<div data-options="region:'north'" id="headArea" style="height: 50px;background-image:url(images/555.png);background-repeat: no-repeat; display:none">
			<%@include file="/common/layout/header.jsp"%>
		</div>
		<div data-options="region:'west',split:true" title="操作选项" id="leftArea" style="width: 290px; display:none">
			<%@include file="/common/layout/left.jsp"%>
		</div>
		<div data-options="region:'center'">
			<div id="tab_div">
			    <%@include file="/common/layout/content.jsp"%>
			</div>
		</div>
	</div>
	<%@include file="/common/layout/dialog.jsp"%>
    <%@include file="/common/layout/viewDeviceData.jsp" %>

	<%--jqplot插件--%>
	<script src="${ctx}/s/jqplot/jquery.jqplot.js" type="text/javascript"></script>
	<script src="${ctx}/s/jqplot/plugins/jqplot.dateAxisRenderer.min.js" type="text/javascript"></script>
	<script src="${ctx}/s/jqplot/plugins/jqplot.canvasTextRenderer.min.js" type="text/javascript"></script>
	<script src="${ctx}/s/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js" type="text/javascript"></script>
	<script src="${ctx}/s/jqplot/plugins/jqplot.categoryAxisRenderer.min.js" type="text/javascript"></script>
	<script src="${ctx}/s/jqplot/plugins/jqplot.barRenderer.min.js" type="text/javascript"></script>
	<!--[if lte IE 8]><script type="text/javascript" src="${ctx}/s/jqplot/excanvas.min.js"></script><![endif]-->
	<%--
        <script src="${ctx}/s/jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js" type="text/javascript"></script>
    --%>

	<%-- 打印插件   --%>
	<%--
    <script src="${ctx}/s/print/jquery.jqprint-0.3.js" type="text/javascript"></script>
  	--%>
  	<script src="${ctx}/s/comet4j/comet4j.js" ></script>
	<script type="text/javascript" src="${ctx}/s/alarm/index.js"></script>
	<script type="text/javascript" src="${ctx}/s/alarm/globeFeatureManager.js"></script>
	
	<script type="text/javascript" src="${ctx}/s/alarm/region-manage.js"></script>
	<script type="text/javascript" src="${ctx}/s/alarm/alarm-record-export.js"></script>
	<script type="text/javascript" src="${ctx}/s/alarm/work-sheet-feedback.js"></script>
	
	<script type="text/javascript" src="${ctx}/s/justgage/raphael.2.1.0.min.js"></script>
    <script type="text/javascript" src="${ctx}/s/justgage/justgage.1.0.1.min.js"></script>
    <%--
    <script type="text/javascript" src="${ctx}/s/countdown/jquery.countdown.js" ></script>
    --%>
    <script type="text/javascript" src="${ctx}/s/alarm/featureDisplay.js" ></script>
    <script type="text/javascript" src="${ctx}/s/alarm/pipeline-analysis.js"></script>
    <script type="text/javascript" src="${ctx}/s/alarm/xt-gas.js" ></script>
    <script type="text/javascript" src="${ctx}/s/alarm/water-pipe.js" ></script>