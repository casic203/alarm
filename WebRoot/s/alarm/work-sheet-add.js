$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#add_device").combobox(
			{
				valueField : 'id',
				textField : 'devName',
				cache : false,
				editable : false,
				url : "device!queryDeviceWithUndealAlarmRecord.do",
				onSelect : function(record) {
					$("#add_record").combobox(
							"reload",
							"alarm-record!queryActiveAlarmRecordByDevice.do?devId="
									+ record.id);
				}
			});
	$("#add_record").combobox({
		valueField : 'id',
		textField : 'recordCode',
		cache : false,
		editable : false,
		onSelect : function(record) {
			$.post("alarm-record!queryMsgOfAlarmRecordById.do", {
				id : record.id
			}, function(result) {
				$("#add_message").text(result.data);
			}, "json");
		}
	});
	$("#add_charger").combobox({
		valueField : 'personName',
		textField : 'personName',
		url : "work-sheet-add!queryPerson.do",
		cache : false,
		editable : false
	});
	$("#fm_add").form({
		url : "work-sheet-add!save.do",
		onSubmit : function() {
			var isValid = $(this).form('validate');
			if (!isValid) {
				$.messager.progress('close'); // 隐藏进度条虽然形式是无效的
			}
			return isValid; // 返回false,将阻止表单提交
		},
		success : function(result) {
			var result = eval("(" + result + ")");
			if (result.success) {
				$("#dg").datagrid("reload"); // reload the user data
			}
			$.messager.alert('结果', result.msg);
			$("#win_add").window("close");
		}
	});
	$("#add_ok_btn").linkbutton({
		iconCls : "icon-ok"
	}).bind("click", function() {
		$("#fm_add").submit();
	});
	$("#add_cls_btn").linkbutton({
		iconCls : "icon-no"
	});
	$("#win_add").window({
		width : 730,
		height : 430,
		closed : true,
		modal : true,
		closable : false,
		minimizable : false,
		maximizable : false,
		resizable : false,
		title : "派发工单"
	});

	$("#edit_charger").combobox({
		valueField : 'personName',
		textField : 'personName',
		url : "work-sheet-add!queryPerson.do",
		cache : false,
		editable : false
	});
	$("#fm_edit").form({
		url : "work-sheet-add!edit.do",
		onSubmit : function() {
			var isValid = $(this).form('validate');
			if (!isValid) {
				$.messager.progress('close'); // 隐藏进度条虽然形式是无效的
			}
			return isValid; // 返回false,将阻止表单提交
		},
		success : function(result) {
			var result = eval("(" + result + ")");
			if (result.success) {
				$("#dg").datagrid("reload"); // reload the user data
			}
			$.messager.alert('结果', result.msg);
			$("#win_edit").window("close");
		}
	});
	$("#edit_ok_btn").linkbutton({
		iconCls : "icon-ok"
	}).bind("click", function() {
		$("#fm_edit").submit();
	});
	$("#edit_cls_btn").linkbutton({
		iconCls : "icon-no"
	});
	$("#win_edit").window({
		width : 730,
		height : 300,
		closed : true,
		modal : true,
		closable : false,
		minimizable : false,
		maximizable : false,
		resizable : false,
		title : "修改工单"
	});

	$("#query_device").combobox({
		valueField : 'id',
		textField : 'devName',
		url : "device!queryActive.do",
		cache : false,
		editable : false
	});
	$("#query_charger").combobox({
		valueField : 'personName',
		textField : 'personName',
		url : "work-sheet-add!queryPerson.do",
		cache : false,
		editable : false
	});
	$("#query_ok_btn").linkbutton({
		iconCls : "icon-ok"
	}).bind("click", function() {
		$("#dg").datagrid({
			url : "work-sheet-add!query.do",
			queryParams : {
				sheetNo : $("#query_sheet").val(),
				chargerId : $("#query_charger").combobox("getValue"),
				devId : $("#query_device").combobox("getValue"),
				beginDate : $("#query_begin").datebox("getValue"),
				endDate : $("#query_end").datebox("getValue")
			}
		});
		$("#win_query").window("close");
		$("#fm_query").form("clear");
		$("#query_charger").combobox("clear");
	});
	$("#query_cls_btn").linkbutton({
		iconCls : "icon-no"
	});
	$("#win_query").window({
		width : 500,
		height : 240,
		closed : true,
		modal : true,
		closable : false,
		minimizable : false,
		maximizable : false,
		resizable : true,
		title : "查询工单"
	});

	$("#dg").datagrid(_dg_cfg).datagrid({
		url : "work-sheet-add!query.do"
	});
}

var col = [ [ {
	field : 'sheetNo',
	title : '单号',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'device',
	title : '设备',
	width : document.body.offsetWidth * 0.12
}, {
	field : 'alarmRecord',
	title : '故障',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'task',
	title : '任务',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'charger',
	title : '负责人',
	width : document.body.offsetWidth * 0.05
}, {
	field : 'beginDate',
	title : '开始日期',
	width : document.body.offsetWidth * 0.07
}, {
	field : 'endDate',
	title : '结束日期',
	width : document.body.offsetWidth * 0.07
}, {
	field : 'sheetStatus',
	title : '状态',
	width : document.body.offsetWidth * 0.05
} ] ];

var _dg_cfg = {
	title : "工单派发",
	columns : col,
	fit : true,
	pagePosition : "top",
	pagination : true,
	rownumbers : true,
	singleSelect : true,
	pageList : [ 5, 10, 20, 30, 40, 50 ],
	pageSize : 20,
	idField : "id",
	toolbar : [ {
		iconCls : "icon-add",
		text : "新增",
		handler : function() {
			$("#win_add").window("open").window("center");
		}
	}, "-", {
		iconCls : 'icon-remove',
		text : "删除",
		handler : function() {
			var row = $("#dg").datagrid("getSelected");
			if (row) {
				$.messager.confirm("Confirm", "您确定删除吗?", function(r) {
					if (r) {
						$.post("work-sheet-add!delete.do", {
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
		iconCls : 'icon-edit',
		text : "编辑",
		handler : function() {
			var row = $("#dg").datagrid("getSelected");
			if (row) {
				$("#fm_edit").form("load", row);
				$.post("work-sheet-add!queryChargerFromWorkSheetById.do", {
					id : row.id
				}, function(result) {
					$("#edit_charger").combobox("setValue", result.id);
				}, "json");
				$("#win_edit").window("open").window("center");
			}
		}
	}, "-", {
		iconCls : 'icon-search',
		text : "查询",
		handler : function() {
			$("#win_query").window("open").window("center");
		}
	} ]
};