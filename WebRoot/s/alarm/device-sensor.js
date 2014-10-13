$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({title : "新增"});
	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({title : "编辑"});
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({title : "查询"});
	
	$("#dev-code-add").combobox({
		width : 140,
		valueField : 'id',
		textField : 'devName',
		required : true
	});
	
	$("#param-code-add").combobox({
		width : 140,
		valueField : 'sensorcode',
		textField : 'sensorname',
		required : true
	});
	
	$("#param-defaultid-add").validatebox({
		required : true
	}); 
	
	$("#param-name-query").combobox({
		width : 140,
		valueField : 'id',
		textField : 'devName'
	});
	
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({
		"onOpen" : function() { 
			$("#dev-code-add").combobox("reload","device!queryDeviceid.do");
			$("#param-code-add").combobox("reload","sensor-type!querySensorCode.do");
		}
	});
	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({
		"onOpen" : function() {
			$("#param-isuse-edit").combobox({
				valueField : 'value',
				textField : 'label',width : 140,
				editable : true,
				data : [{"value":true,"label":"启用","selected":true},{"value":false,"label":"未启用"}]
			});
		}
	});
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({
		"onOpen" : function() { 
			$("#param-name-query").combobox("reload","device!queryDeviceid.do"); 
		}
	});
	$("#dg").datagrid(dg_config);
	$("#dg").datagrid({
		url : "device-sensor!query.do"
	});
}

var col = [[{
	field : 'deviceid',
	title : '设备编码',
	width : document.body.offsetWidth * 0.1
},  {
		field : 'devname',
		title : '设备名称',
		width : document.body.offsetWidth * 0.1
	}, {
	field : 'sensorcode',
	title : '传感器类型编码',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'sensorname',
	title : '传感器名称',
	width : document.body.offsetWidth * 0.1
},{
	field : 'sensorid',
	title : '传感器编号',
	width : document.body.offsetWidth * 0.1
} 
] ];

var dg_config = {
	title : "传感器参数信息管理",
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
						$.post("device-sensor!delete.do", {
							id : row.id,
							sensorcode: row['sensorcode'],
							deviceid:row['deviceid'],
							sensorid:row['sensorid']
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
				$("#dlg_edit").dialog("open");
			}
		}
	}, "-", {
		iconCls : 'icon-search',
		text : "查询",
		handler : function() {
			$("#dlg_query").dialog("open");
		}
	} ]
};

var dlg_cfg = {
	width : 300,
	height : 300,
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
				
				url : "device-sensor!save.do",
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
					url : "device-sensor!edit.do?",
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
				deviceid : $("#param-name-query").combobox("getValue")
			});
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