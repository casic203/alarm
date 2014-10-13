$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({title : "新增"});

	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({title : "编辑"});
	
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({title : "查询"});
	
	$("#dev-type-add").combobox({
		width : 140,
		valueField : 'sensorcode',
		textField : 'sensorname',
		required : true
	});
	
	$("#item-name-edit").validatebox({
		required : true
	});
	$("#param-name-add").validatebox({
		required : true
	}); 
	$("#dev-type-edit").combobox({
		width : 140,
		valueField : 'sensorcode',
		textField : 'sensorname',
		required : true
	});
	
	$("#dev-type-query").combobox({
		width : 140,
		valueField : 'sensorcode',
		textField : 'sensorname',
		required : true
	});
	
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({
		"onOpen" : function() {
			$("#param-isuse-add").combobox({
				valueField : 'value',
				textField : 'label',width : 140,
				editable : true,
				data : [{"value":true,"label":"启用","selected":true},{"value":false,"label":"未启用"}]
			});
			$("#dev-type-add").combobox("reload","sensor-type!querySensorCode.do");
		}
	});
	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({
		"onOpen" : function() { 
			$("#dev-type-edit").combobox("reload","sensor-type!querySensorCode.do");
			$("#dev-type-edit").combobox('disable',false);
		} 
	});
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({
		"onOpen" : function() { 
			$("#dev-type-query").combobox("reload","sensor-type!querySensorCode.do"); 
		}
	});
	$("#dg").datagrid(dg_config);
	$("#dg").datagrid({
		url : "device-sensorformat!query.do"
	}).datagrid("load", {});
}

var col = [[{
	field : 'sensortype',
	title : '传感器类型',
	width : document.body.offsetWidth * 0.07
},  {
	field : 'sortid',
	title : '排序',
	width : document.body.offsetWidth * 0.03
},  {
	field : 'itemnameCn',
	title : '帧格式名称',
	width : document.body.offsetWidth * 0.1
},  {
	field : 'itemname',
	title : '帧格式代码',
	width : document.body.offsetWidth * 0.1
},  {
	field : 'itemvalue',
	title : '帧内容',
	width : document.body.offsetWidth * 0.1
}] ];

var dg_config = {
	title : "数据帧信息管理",
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
						$.post("device-sensorformat!delete.do", {
							id : row.id,
							sensortype: row['sensortype'],
							itemname:row['itemname']
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
				url : "device-sensorformat!save.do",
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
					url : "device-sensorformat!edit.do?id=" + row.id,
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
				sensortype : $("#dev-type-query").combobox("getValue") 
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