$(window).load(function() {
	loadPage();
	$("#region_dialog").css("display", "block");
});

function loadPage() {
	$("#device_add")
			.combobox(
					{
						valueField : 'id',
						textField : 'devName',
						cache : false,
						editable : false,
						required : true,
						onHidePanel : function() {
							$("#acceptPerson_add").combobox("setValue", "");
							$("#alarmType_add").combobox("setValue", "");
							var dev = $("#device_add").combobox("getValue");
							if ("" != dev) {
								$
										.ajax({
											type : "POST",
											url : "../../alarm/device!queryPersonByDevice.do?id="
													+ dev,
											dataType : "json",
											success : function(data) {
												$("#acceptPerson_add")
														.combobox("loadData",
																data);
											}
										});
							}
						}
					});

	$("#acceptPerson_add").combobox({
		valueField : 'id',
		textField : 'personName',
		editable : false,
		required : true,
		cache : false
	});

	$("#alarmType_add").combobox({
		valueField : 'id',
		textField : 'alarmName'
	});

	$("#device_query").combobox({
		valueField : 'id',
		textField : 'devName',
		cache : false
	});

	$("#acceptPerson_query").combobox({
		valueField : 'id',
		editable : false,
		textField : 'personName'
	});

	$("#message").validatebox({
		required : true
	});

	$("#dlg_add").dialog(dlg_add_btn_cfg).dialog(
			{
				"onOpen" : function() {
					$("#device_add").combobox("reload",
							"../../alarm/device!queryActive.do");
					$("#alarmType_add").combobox("reload",
							"../../alarm/alarm-type!queryType.do");
					$("#alarmType_add").combobox("setValue", "");
				}
			});

	$("#dlg_query").dialog(dlg_query_btn_cfg).dialog(
			{
				"onOpen" : function() {
					$("#device_query").combobox("reload",
							"../../alarm/device!queryActive.do");
					$("#acceptPerson_query").combobox("reload",
							"../../alarm/accept-person!queryPerson.do");
				}
			});

	$("#dg").datagrid(dg_config);
	$("#dg").datagrid({
		url : "../../alarm/alarm-record!query.do"
	}).datagrid("load", {
		active : 2
	});
}

var col = [ [ {
	field : 'code',
	title : '设备编码',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'device',
	title : '设备名称',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'message',
	title : '记录内容',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'recordDate',
	title : '日期',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'acceptPerson',
	title : '接警人',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'messageStatus',
	title : '是否报警',
	width : document.body.offsetWidth * 0.1,
	formatter : function(value, row, index) {
		if (value > 0) {
			return '已报警';
		}
		if (value <= 0) {
			return '未报警';
		}
	}
}, {
	field : 'active',
	title : '是否处理',
	width : document.body.offsetWidth * 0.1,
	formatter : function(value, row, index) {
		if (value) {
			return "未处理";
		} else {
			return "已处理";
		}
	}
} ] ];

var dg_config = {
	title : "报警",
	columns : col,
	fit : true,
	pagePosition : "top",
	pagination : true,
	rownumbers : true,
	singleSelect : true,
	pageList : [ 10, 20, 30, 40, 50 ],
	pageSize : 10,
	idField : "id",
	toolbar : [ {
		iconCls : "icon-add",
		text : "新增",
		handler : function() {
			$("#dlg_add").dialog("open").dialog("center");
		}
	}, "-", {
		iconCls : 'icon-remove',
		text : "删除",
		handler : function() {
			var row = $("#dg").datagrid("getSelected");
			if (row) {
				$.messager.confirm("Confirm", "您确定删除吗?", function(r) {
					if (r) {
						$.post("../../alarm/alarm-record!delete.do", {
							id : row.id
						}, function(result) {
							if (result.success) {
								$("#dg").datagrid("reload");
							}
							$.messager.alert('结果', result.msg);
						}, "json");
					}
				});
			}
		}
	}, "-", {
		iconCls : 'icon-search',
		text : "查询",
		handler : function() {
			$("#dlg_query").dialog("open").dialog("center");
		}
	}, "-", {
		iconCls : 'icon-search',
		text : "导出",
		handler : function() {
			$.ajax({
				type : "POST",
				url : "../../alarm/alarm-record!expToExcel.do",
				data : {
					active : $("#hiden_active").val(),
					devId : $("#hiden_dev_id").val(),
					personId : $("#hiden_person_id").val()
				},
				dataType : "json",
				success : function(data) {
					location.href="../../xls/alarm.xls";
				}
			});
		}
	} ]
};

var dlg_add_btn_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			$('#fm_add').form('submit', {
				url : "../../alarm/alarm-record!save.do",
				onSubmit : function() {
					return $(this).form("validate");
				},
				success : function(result) {
					var result = eval("(" + result + ")");
					if (result.success) {
						$("#dg").datagrid("reload"); // reload the user data
					}
					$.messager.alert('结果', result.msg);
					$("#dlg_add").dialog("close");
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

var dlg_query_btn_cfg = {
	buttons : [
			{
				text : "确定",
				iconCls : 'icon-ok',
				handler : function() {
					$("#dg").datagrid(
							{
								queryParams : {
									devId : $("#device_query").combobox(
											"getValue"),
									personId : $("#acceptPerson_query")
											.combobox("getValue"),
									active : $("#alarmStatus_query").combobox(
											"getValue")
								}
							});
					$("#hiden_dev_id").val(
							$("#device_query").combobox("getValue"));
					$("#hiden_person_id").val(
							$("#acceptPerson_query").combobox("getValue"));
					$("#hiden_active").val(
							$("#alarmStatus_query").combobox("getValue"));
					$("#fm_query").form("clear");
					$("#dlg_query").dialog("close");
				}
			}, {
				text : "取消",
				iconCls : 'icon-cancel',
				handler : function() {
					$("#dlg_query").dialog("close");
				}
			} ]
};
