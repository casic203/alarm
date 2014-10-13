$(window).load(function() {
	$("#device").combobox({
		valueField : 'id',
		textField : 'devName',
		cache : false,
		editable : false,
		onHidePanel : function() {
			$("#per").combobox("setValue", "");
			$("#type").combobox("setValue", "");
			var dev = $("#device").combobox("getValue");
			if ("" != dev) {
				$.ajax({
					type : "POST",
					url : "device!queryPersonByDevice.do?id=" + dev,
					dataType : "json",
					success : function(data) {
						$("#acceptPerson").combobox("loadData", data);
					}
				});
			}
		}
	});
	
	$("#acceptPerson").combobox({
		valueField : 'id',
		textField : 'personName',
		editable : false,
		cache : false,
		onHidePanel : function() {
			$("#alarmType").combobox("setValue", "");
			var per = $("#acceptPerson").combobox("getValue");
			if ("" != per) {
				$.ajax({
					type : "POST",
					url : "contact-book!queyAlarmTypeByPerson.do?id=" + per,
					dataType : "json",
					success : function(data) {
						$("#alarmType").combobox("loadData", data);
					}
				});
			}
		}
	});
	
	$("#alarmType").combobox({
		valueField : 'id',
		textField : 'alarmName'
	});

	$("#message").validatebox({
		required : true
	});
	
	$("#dlg").dialog(dlg_cfg).dialog(dlg_btn_cfg).dialog({title:"新增报警记录"}).dialog({
		"onOpen" : function() {
			$("#device").combobox("reload","device!queryActive.do");
		}
	});
	
	$("#dg").datagrid(dg_config);
	$("#dg").datagrid({
		url : "alarm-record!query.do"
	}).datagrid("load", {});
});

var col = [ [ {
	field : 'code',
	title : '设备编码',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'device',
	title : '设备名称',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'acceptPerson',
	title : '接警人',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'alarmType',
	title : '接警方式',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'message',
	title : '记录内容',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'sendPerson',
	title : '报警人',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'sendContact',
	title : '联系方式',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'recordDate',
	title : '日期',
	width : document.body.offsetWidth * 0.1
} ] ];

var dg_config = {
	title : "设备报警记录",
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
			$("#dlg").dialog("open").dialog("center");
		}
	}, "-", {
		iconCls : 'icon-remove',
		text : "删除",
		handler : function() {
			var row = $("#dg").datagrid("getSelected");
			if (row) {
				alert(row.id);
				$.messager.confirm("Confirm", "您确定删除吗?", function(r) {
					if (r) {
						$.post("alarm-record!delete.do", {
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

				$("#fm").form("load", row);
				$("#dlg").dialog(dlg_btn_edit_cfg).dialog("open");
			}
		}
	}, "-", {
		iconCls : 'icon-search',
		text : "查询",
		handler : function() {

		}
	} ]
};
var dlg_cfg = {
	width : 300,
	height : 380,
	border : true,
	closable : true,
	resizable : true,
	closed : true
}

var dlg_btn_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			$('#fm_add').form('submit', {
				url : "alarm-record!save.do",
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
