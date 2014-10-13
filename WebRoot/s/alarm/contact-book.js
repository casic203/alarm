$(window).load(
		function() {
			$("#combobox-add-person").combobox(
					{
						url : "accept-person!queryPerson.do",
						valueField : "id",
						textField : "personName",
						onSelect : function(record) {
							$("#combobox-add-alarm").combobox(
									"reload",
									"alarm-type!queryUnAllotAlarmTypeByPerson.do?id="
											+ record.id);
							$("#combobox-add-alarm").combobox("clear");
						}
					});
			$("#combobox-add-alarm").combobox({
				valueField : "id",
				textField : "alarmName"
			});

			$("#combobox-edit-person").combobox(
					{
						url : "accept-person!queryPerson.do",
						valueField : "id",
						textField : "personName",
						onSelect : function(record) {
							$("#combobox-edit-alarm").combobox(
									"reload",
									"alarm-type!queryUnAllotAlarmTypeByPerson.do?id="+ record.id);
							$("#combobox-edit-alarm").combobox("clear");
						}
					});
			$("#combobox-edit-alarm").combobox({
				valueField : "id",
				textField : "alarmName"
			});
			
			$("#combobox-query-person").combobox(
					{
						url : "accept-person!queryPerson.do",
						valueField : "id",
						textField : "personName",
						onSelect : function(record) {
							$("#combobox-edit-alarm").combobox(
									"reload",
									"alarm-type!queryUnAllotAlarmTypeByPerson.do?id="+ record.id);
							$("#combobox-edit-alarm").combobox("clear");
						}
					});

			$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg);
			$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg);
			$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg);

			$("#dg").datagrid(dg_config);
			$("#dg").datagrid({
				url : "contact-book!query.do"
			}).datagrid("load", {});
		});

var col = [ [ {
	field : 'acceptPerson',
	title : '姓名',
	width : 100
}, {
	field : 'alarmType',
	title : '接警方式',
	width : 100
}, {
	field : 'contact',
	title : '联系方式',
	width :200
}, {
	field : 'active',
	title : '状态',
	width : 50
} ] ];

var dg_config = {
	title : "通讯簿维护",
	columns : col,
	fit : true,
	pagePosition : "top",
	pagination : true,
	rownumbers : true,
	singleSelect : true,
	idField : "id",
	toolbar : [
			{
				iconCls : "icon-add",
				text : "新增",
				handler : function() {
					$("#dlg_add").dialog("open").dialog("center");
				}
			},
			"-",
			{
				iconCls : 'icon-remove',
				text : "删除",
				handler : function() {
					var row = $("#dg").datagrid("getSelected");
					if (row) {
						$.messager
								.confirm(
										"Confirm",
										"您确定删除吗?",
										function(r) {
											if (r) {
												$.post(
												"contact-book!delete.do",
												{
													id : row.id
												},
												function(result) {
													if (result.success) {
														$("#dg")
																.datagrid(
																		{
																			url : "contact-book!query.do"
																		})
																.datagrid(
																		"load",
																		{});
													}
													$.messager
															.alert(
																	'结果',
																	result.msg);
												}, "json");
											}
										});
					}
				}
			},
			"-",
			{
				iconCls : 'icon-edit',
				text : "编辑",
				handler : function() {
					var row = $("#dg").datagrid("getSelected");
					if (row) {
						$("#fm_edit").form("load", row);
						var data = $("#combobox-edit-person").combobox(
								"getData");

						for (x in data) {
							if (row.acceptPerson == data[x].personName) {
								$("#combobox-edit-person").combobox("select",
										data[x].id);
							}
						}
						var data1 = $("#combobox-edit-alarm").combobox("getData");
						$("#dlg_edit").dialog(dlg_btn_edit_cfg).dialog("open");
					}
				}
			}, "-", {
				iconCls : 'icon-search',
				text : "查询",
				handler : function() {
					$("#dlg_query").dialog("open").dialog("center");
				}
			} ]
};

var dlg_cfg = {
	width : 300,
	height : 260,
	border : true,
	closable : true,
	resizable : true,
	closed : true
}

var dlg_btn_add_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			$("#fm_add").form("submit", {
				url : "contact-book!save.do",
				onSubmit : function() {
					return $("#fm_add").form("validate");
				},
				success : function(result) {
					var result = eval("(" + result + ")");
					if (result.success) {
						$("#fm_add").form("clear");
						$("#dlg_add").dialog("close");
						$("#dg").datagrid("reload"); // reload the user data
					} else {
						$.messager.alert('结果', result.msg);
					}
				}
			});
		}
	}, {
		text : "取消",
		iconCls : 'icon-cancel',
		handler : function() {
			$("#dlg_add").dialog("close");
		}
	} ]
};

var dlg_btn_edit_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			var row = $("#dg").datagrid("getSelected");
			if (row) {
				$('#fm_edit').form('submit', {
					url : "contact-book!edit.do?id=" + row.id,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var result = eval("(" + result + ")");
						if (result.success) {
							$("#dg").datagrid("reload"); // reload the user data
						}
						$.messager.alert('结果', result.msg);
						$("#dlg_edit").dialog("close");
					}
				});
			}
		}
	}, {
		text : "取消",
		iconCls : 'icon-cancel',
		handler : function() {
			$("#dlg_edit").dialog("close");
		}
	} ]
};

var dlg_btn_query_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			$("#dg").datagrid("load", {
				id : $("#combobox-query-person").combobox("getValue")
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