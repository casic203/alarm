$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#dlg_add").dialog(dlg_btn_add_cfg);
	$("#dlg_edit").dialog(dlg_btn_edit_cfg);
	$("#dlg_query").dialog(dlg_btn_query_cfg);
	$("#dlg_set").dialog(dlg_btn_set_cfg);
	$("#dg").datagrid(dg_config);
	
	$.ajax({
        type: "POST",
        url: "data-base-monitor-rule!querySetCycle.do",
        data: {username:$("#username").val(), content:$("#content").val()},
        dataType: "json",
        success: function(data){
                    $("#cycle_set").val(data._cycle);
                    $("#path_set").val(data._path);
                 }
    });
}
var col = [ [ {
	field : 'name',
	title : '项目名称',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'max',
	title : '上限值',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'min',
	title : '下限值',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'sql',
	title : '监控语句',
	width : document.body.offsetWidth * 0.7
} ] ];

var dg_config = {
	title : "监控规则信息",
	columns : col,
	fit : true,
	pagePosition : "top",
	pagination : true,
	rownumbers : true,
	singleSelect : true,
	pageList : [ 10, 20, 30, 40, 50 ],
	pageSize : 10,
	idField : "id",
	url : "data-base-monitor-rule!query.do",
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
						$.post("data-base-monitor-rule!delete.do", {
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
				$("#dlg_edit").dialog("open").dialog("center");
			}
		}
	}, "-", {
		iconCls : 'icon-search',
		text : "查询",
		handler : function() {
			$("#dlg_query").dialog("open").dialog("center");
		}
	}, "-", {
		iconCls : 'icon-cycle-set',
		text : "备份周期设定",
		handler : function() {
			$("#dlg_set").dialog("open").dialog("center");
		}
	} ]
};

var dlg_btn_add_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			$('#fm_add').form('submit', {
				url : "data-base-monitor-rule!save.do",
				onSubmit : function() {
					return $(this).form("validate");
				},
				success : function(result) {
					var result = eval("(" + result + ")");
					if (result.success) {
						$("#dg").datagrid("reload"); // reload the user data
						$("#fm_add").form("clear");
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
					url : "data-base-monitor-rule!edit.do?id=" + row.id,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var result = eval("(" + result + ")");
						if (result.success) {
							$("#dg").datagrid("reload"); // reload the user
							$("#fm_edit").form("clear");
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
				name : $("#name_query").val()
			});
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

var dlg_btn_set_cfg = {
		buttons : [ {
			text : "确定",
			iconCls : 'icon-ok',
			handler : function() {
				$('#fm_set').form('submit', {
					url : "data-base-monitor-rule!doSetCycle.do",
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var result = eval("(" + result + ")");
						if (result.success) {
							$("#fm_set").form("clear");
						}
						$.messager.alert('结果', result.msg);
						$("#dlg_set").dialog("close");
					}
				});
			}
		}, {
			text : "取消",
			iconCls : 'icon-cancel',
			handler : function() {
				$("#dlg_set").dialog("close");
			}
		} ]
	};