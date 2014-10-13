<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/easy-ui/css/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/easy-ui/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/easy-ui/frm.css" />

<script type="text/javascript" src="${ctx}/s/easy-ui/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/easy-ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	var col = [ [ {
		field : 'device',
		title : '设备'
	},{
		field : 'sendPerson',
		title : '报警人'
	}, {
		field : 'sendContact',
		title : '联系方式'
	}, {
		field : 'acceptPerson',
		title : '接警人'
	}, {
		field : 'recordDate',
		title : '报警日期'
	}, {
		field : 'message',
		title : '故障信息'
	} ] ];

	var dg_config = {
		title : "故障处理",
		columns : col,
		fit : true,
		fitColumns:true,
		pagePosition : "top",
		pagination : true,
		rownumbers : true,
		fitColumns : true,
		singleSelect : true,
		idField : "id",
		toolbar : [
				{
					iconCls : "icon-add",
					handler : function() {
						$("#dlg_save").dialog(dlg_btn_save_cfg).dialog("open");
					}
				},
				"-",
				{
					iconCls : 'icon-remove',
					handler : function() {
						var row = $("#dg").datagrid("getSelected");
						if (row) {
							$.messager.confirm("Confirm","您确定删除吗?",function(r) {
												if (r) {
													$.post(
														"database-monitor!delete.do",
														{
															id : row.id
														},
														function(result) {
															if (result.success) {
																$("#dg").datagrid({
																					url : "${ctx}/alarm/data-base-operator!query.do"
																		}).datagrid("load",{});
															}
															$.messager.alert('结果',result.msg);
														}, "json");
												}
							});
					  	}
					}
				} ]
	};

	var dlg_cfg = {
		width : 400,
		height : 280,
		closable : true,
		closed : true
	}


	var dlg_btn_fg = {
		buttons : [ {
			text : "保存",
			iconCls : 'icon-save',
			handler : function() {
				$('#fm').form('submit', {
					url : "${ctx}/alarm/alarm-record!dealAlarmRecord.do",
					onSubmit : function(param) {
						var row=$("#dg").datagrid("getSelected");
						if(row){
							param.id=row.id;
							return $(this).form("validate");
						}else{
							$.messager.alert('结果',"您还没有选择要处理的记录！");
							return false;
						}
					},
					success : function(result) {
						var result=eval("("+result+")");
						if(result.success){
							$("#dg").datagrid("reload");	// reload the user data
						}
						$.messager.alert('结果',result.msg);
						$("#dlg_save").dialog("close");
					}
				});
			}
		}, {
			text : "取消",
			iconCls : 'icon-cancel',
			handler : function() {
				$("#dlg").dialog("close");
			}
		} ]
	};

	$(document).ready(function() {
		$("#dlg").dialog(dlg_cfg);
		$("#dg").datagrid(dg_config);
		$("#dg").datagrid({
			url : "${ctx}/alarm/alarm-record!queryActiveAlarmRecord.do"
		}).datagrid("load", {});
	});
</script>
</head>
<body style="text-align: center;">
	<table id="dg"></table>
	
	<div id="dlg" style="padding:10px 20px">
		<div class="ftitle">故障处理结果录入</div>
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<label>处理人:</label> <input name="dealPerson"/>
			</div>
			<div class="fitem">
				<label>处理结果:</label> <input type="text" name="dealResult"/>
			</div>
		</form>
	</div>
	</div>
</body>
</html>