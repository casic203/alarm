$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({title:"新增"});
	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({title:"编辑"});
	
	$("#dev-param-add").validatebox({
		required : true
	}); 
	 
	$("#dev-code-add").combobox({
		width : 140,
		valueField : 'devCode',
		textField : 'devName',
		required : true
	});
	$("#sensor-code-add").combobox({
		width : 140,
		valueField : 'sensorcode',
		textField : 'sensorname',
		required : true
	});
	$("#param-code-add").combobox({
		width : 140,
		valueField : 'itemname',
		textField : 'itemname',
		required : true
	});
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({
		"onOpen" : function() {  
			$("#dev-code-add").combobox("reload","device!queryDeviceid.do"); 
			$("#sensor-code-add").combobox("reload","device-sensor!querySensorCode.do");  
			$("#param-code-add").combobox("reload","device-sconfig!queryParamitem.do");   
		}
	});
	
	$("#dg").datagrid(dg_config);
	$("#dg").datagrid({url : "alarm-rule!query.do"}).datagrid("load", {});
}
var col = [ [ {
	field : 'deviceid',
	title : '设备编号',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'sensorcode',
	title : '传感器编号',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'paramcode',
	title : '参数名称',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'secullval',
	title : '警限LL',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'seculval',
	title : '警限L',
	width : document.body.offsetWidth * 0.1
} , {
	field : 'secuokval',
	title : '警限OK',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'secuhval',
	title : '警限H',
	width : document.body.offsetWidth * 0.1
} , {
	field : 'secuhhval',
	title : '警限HH',
	width : document.body.offsetWidth * 0.1
}  ] ];

var dg_config = {
	title : "报警规则管理",
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
						$.post("alarm-rule!delete.do", {
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
				$("#dlg_edit").dialog(dlg_btn_edit_cfg).dialog("open");
			}
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
			$('#fm_add').form('submit', {
				url : "alarm-rule!save.do",
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

var dlg_btn_edit_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			var row = $("#dg").datagrid("getSelected");
			if (row) {
				$('#fm_edit').form('submit', {
					url : "alarm-rule!edit.do?id=" + row.id,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var result = eval("(" + result + ")");
						if (result.success) {
							$("#dg").datagrid("reload"); // reload the user
															// data
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