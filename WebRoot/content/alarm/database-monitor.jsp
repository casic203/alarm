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
<script type="text/javascript">
	var col = [ [ {
		field : 'item',
		title : '监控项目'
	},{
		field : 'sqlString',
		title : 'SQL'
	}, {
		field : 'personName',
		title : '联系人'
	}, {
		field : 'phone',
		title : '电话'
	}, {
		field : 'mail',
		title : 'e_mail',
		align : 'right'
	}, {
		field : 'maxValue',
		title : '最大值',
		align : 'right'
	}, {
		field : 'minValue',
		title : '最小值',
		align : 'right'
	}
	, {
		field : 'active',
		title : '状态',
		align : 'right'
	} ] ];

	var dg_config = {
		title : "数据库管理员",
		columns : col,
		fit : true,
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
				},
				"-",
				{
					iconCls : 'icon-edit',
					handler : function() {
						var row = $("#dg").datagrid("getSelected");
						if (row) {
							$("#fm_edit").form("load", row);
							$("#dlg_edit").dialog(dlg_btn_edit_cfg).dialog("open");
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


	var dlg_btn_save_cfg = {
		buttons : [ {
			text : "新增",
			iconCls : 'icon-save',
			handler : function() {
				$('#fm_save').form('submit', {
					url : "",
					onSubmit : function() {
						return $(this).form("validate");
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
				$("#dlg_save").dialog("close");
			}
		} ]
	};

	var dlg_btn_edit_cfg = {
		buttons : [ {
			text : "编辑",
			iconCls : 'icon-save',
			handler : function() {
				$('#fm_edit').form('submit', {
					url : "",
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var result=eval("("+result+")");
						if(result.success){
							$("#dg").datagrid("reload");	// reload the user data
						}
						$.messager.alert('结果',result.msg);
						$("#dlg_edit").dialog("close");
					}
				});
			}
		}, {
			text : "取消",
			iconCls : 'icon-cancel',
			handler : function() {
				$("#dlg_edit").dialog("close");
			}
		} ]
	};

	$(document).ready(function() {
		$("#dg").datagrid(dg_config);
		$("#dlg_save").dialog(dlg_cfg);
		$("#dlg_edit").dialog(dlg_cfg);
		$("#dg").datagrid({
			url : "${ctx}/alarm/database-monitor!query.do"
		}).datagrid("load", {});
	});
</script>
</head>
<body style="text-align: center;">
	<table id="dg"></table>
	
	<div id="dlg_save" style="padding:10px 20px">
		<div class="ftitle">数据库监控条件录入</div>
		<form id="fm_save" method="post" novalidate>
			<div class="fitem">
				<label>监控项目:</label> <input id="txt_item_save" name="item"/>
			</div>
			<div class="fitem">
				<label>联系人:</label> <input id="txt_personName_save" name="personName" />
			</div>
			<div class="fitem">
				<label>电话:</label> <input id="txt_phone_save" name="phone"/>
			</div>
			<div class="fitem">
				<label>邮箱:</label> <input id="txt_mail_save" name="mail"
					class="easyui-validatebox" data-options="required:true,validType:'email'" />
			</div>
			<div class="fitem">
				<label>最大值:</label> <input id="txt_maxValue_save" name="maxValue"/>
			</div>
			<div class="fitem">
				<label>最小值:</label> <input id="txt_minValue_save" name="minValue"/>
			</div>
			<div class="fitem">
				<label>SQL:</label> <input type="text" id="txt_sql_save" name="sqlString"/>
			</div>
		</form>
	</div>
	
	<div id="dlg_edit" style="padding:10px 20px">
		<div class="ftitle">数据库监控条件修改</div>
		<form id="fm_edit" method="post" novalidate>
			<div class="fitem">
				<label>监控项目:</label> <input id="txt_item_edit" name="item"/>
			</div>
			<div class="fitem">
				<label>联系人:</label> <input id="txt_personName_edit" name="personName" />
			</div>
			<div class="fitem">
				<label>电话:</label> <input id="txt_phone_edit" name="phone"/>
			</div>
			<div class="fitem">
				<label>邮箱:</label> <input id="txt_mail_edit" name="mail"
					class="easyui-validatebox" data-options="required:true,validType:'email'" />
			</div>
			<div class="fitem">
				<label>最大值:</label> <input id="txt_maxValue_edit" name="maxValue"/>
			</div>
			<div class="fitem">
				<label>最小值:</label> <input id="txt_minValue_edit" name="minValue"/>
			</div>
			<div class="fitem">
				<label>SQL:</label> <input type="text" id="txt_sql_edit" name="sqlString"/>
			</div>
		</form>
	</div>
</body>
</html>