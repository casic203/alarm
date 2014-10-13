$(window).load(function() {
	
	$("#code-add").validatebox({
		required : true
	});
	$("#name-add").validatebox({
		required : true
	});
	$("#code-edit").validatebox({
		required : true
	});
	$("#name-edit").validatebox({
		required : true
	});
	
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({title:"新增"});
	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({title:"编辑"});
	
	$("#dg").datagrid(dg_config);
	$("#dg").datagrid({url : "data-base-operator!query.do"}).datagrid("load", {});
});

var col = [ [ {
	field : 'code',
	title : '编号',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'name',
	title : '名称',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'active',
	title : '状态',
	width : document.body.offsetWidth * 0.1
} ] ];

var dg_config = {
	title : "数据库管理员维护",
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
						$.post("data-base-operator!delete.do", {
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
	height : 240,
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
				url : "data-base-operator!save.do",
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
					url : "data-base-operator!edit.do?id=" + row.id,
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