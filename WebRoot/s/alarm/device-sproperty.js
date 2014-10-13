$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({title : "新增"});

	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({title : "编辑"});

	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({title : "查询"});
	
	$("#param-standardcol-add").validatebox({
		required : true
	});
	$("#param-standardcolname-add").validatebox({
		required : true
	}); 
	
	$("#param-collen-add").validatebox({
		required : true
	}); 
	
	$("#param-standardcol-edit").validatebox({
		required : true
	});
	
	$("#param-standardcolname-edit").validatebox({
		required : true
	});
	
	$("#param-collen-edit").validatebox({
		required : true
	});    
	
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({
		"onOpen" : function() {
			$("#param-coldatatype-add").combobox({
				valueField : 'value',
				textField : 'label',
				editable : true,
				data : [{"value":"num","label":"num","selected":true},{"value":"string","label":"string"}]
			});
		}
	});
	
	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({
		"onOpen" : function() {
			$("#param-coldatatype-edit").combobox({
				valueField : 'value',
				textField : 'label',
				editable : true,
				data : [{"value":"num","label":"num","selected":true},{"value":"string","label":"string"}]
			});
		}
	});
	
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({
		"onOpen" : function() {
			$("#param-status-query").combobox({
				valueField : 'value',
				textField : 'label',
				editable : true,
				data : [{"value":true,"label":"在用","selected":true},{"value":false,"label":"已删除"}]
			});
		}
	});
	
	$("#dg").datagrid(dg_config);
	
	$("#dg").datagrid({
		url : "device-sproperty!query.do"
	}).datagrid("load", {});
}

var col = [ [  {
	field : 'standardcol',
	title : '参数代码',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'standardcolname',
	title : '参数名称',
	width : document.body.offsetWidth * 0.1
},{
	field : 'coldatatype',
	title : '参数项数据类型',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'collen',
	title : '参数项长度',
	width : document.body.offsetWidth * 0.1
}] ];

var dg_config = {
	title : "参数配置管理",
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
						$.post("device-sproperty!delete.do", {
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
				$("#dlg_edit").dialog("open");
			}
		}
	}, "-", {
		iconCls : 'icon-search',
		text : "查询",
		handler : function() {
			$("#dlg_query").dialog("open");
		}
	}  ]
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
				url : "device-sproperty!save.do",
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
					url : "device-sproperty!edit.do?id=" + row.id,
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
					status : $("#param-status-query").val() 
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