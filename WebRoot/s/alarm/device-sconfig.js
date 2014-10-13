$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({title : "新增"});

	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({title : "编辑"});
	
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({title : "查询"});
	
	$("#param-code-add").combobox({
		width : 140,
		valueField : 'sensorcode',
		textField : 'sensorname',
		required : true
	});
	$("#item-name-add").combobox({
		width : 140,
		valueField : 'standardcol',
		textField : 'standardcolname',
		required : true
	}); 
	$("#item-value-edit").validatebox({
		required : true
	});
	$("#param-code-query").combobox({
		width : 140,
		valueField : 'sensorcode',
		textField : 'sensorname',
		required : true
	});
	
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({
		"onOpen" : function() {
						$("#param-code-add").combobox("reload","sensor-type!querySensorCode.do");    
						$("#item-name-add").combobox("reload","device-sproperty!queryStandardCol.do");   
					}
	}); 
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({
		"onOpen" : function() {
						$("#param-code-query").combobox("reload","sensor-type!querySensorCode.do");   
					}
	});
	$("#dg").datagrid(dg_config);
	$("#dg").datagrid({
		url : "device-sconfig!query.do"
	});
}

var col = [ [  {
	field : 'sensorcode',
	title : '传感器类型编码',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'sensorname',
	title : '传感器类型',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'itemnum',
	title : '配置参数排序',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'itemname',
	title : '配置参数项名称',
	width : document.body.offsetWidth * 0.1
},{
	field : 'itemdatatype',
	title : '配置参数项数据类型',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'itemvalue',
	title : '配置参数项值',
	width : document.body.offsetWidth * 0.1
}] ];

var dg_config = {
	title : "传感器标准配置信息管理",
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
						$.post("device-sconfig!delete.do", {
							id : row.id,
							sensorcode: row['sensorcode'] 
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
				url : "device-sconfig!save.do",
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
					url : "device-sconfig!edit.do?id=" + row.id,
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
				sensorcode : $("#param-code-query").combobox("getValue")
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