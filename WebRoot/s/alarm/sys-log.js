$(window).load(function() {
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({
		title : "查询"
	});
	$("#dg").datagrid(dg_config);
});

var col = [ [ {
	field : 'businessName',
	title : '业务名称',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'operationType',
	title : '业务类型',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'content',
	title : '操作内容',
	width : document.body.offsetWidth * 0.3
}, {
	field : 'createUser',
	title : '操作人',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'createTime',
	title : '操作时间',
	width : document.body.offsetWidth * 0.1
} ] ];

var dg_config = {
	title : "系统操作日志",
	columns : col,
	fit : true,
	pagePosition : "top",
	pagination : true,
	rownumbers : true,
	singleSelect : true,
	pageList : [ 10, 20, 30, 40, 50 ],
	pageSize : 10,
	idField : "id",
	url : "sys-log!query.do",
	toolbar : [ {
		iconCls : 'icon-search',
		text : "查询",
		handler : function() {
			$("#dlg_query").dialog("open").dialog("center");
		}
	}, "-", {
		iconCls : 'icon-remove',
		text : "删除",
		handler : function() {
			var row = $("#dg").datagrid("getSelected");
			if (row) {
				$.messager.confirm("Confirm", "您确定删除吗?", function(r) {
					if (r) {
						$.post("sys-log!delete.do", {
							id : row.id
						}, function(result) {
							if (result.success) {
								$("#dg").datagrid("reload");
							}
							$.messager.alert('结果', result.msg);
						}, "json");
					}
				});
			}else{
				$.messager.alert('提示', "请选择要删除的日志！");
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

var dlg_btn_query_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			$("#dg").datagrid("load", {
				person : $("#person_query").val(),
				beginTime : $('#beginTime_query').datebox("getValue"),
				endTime : $('#endTime_query').datebox("getValue")
			});
			$("#dlg_query").dialog("close");
			$("#fm_query").form("clear");
		}
	}, {
		text : "取消",
		iconCls : 'icon-cancel',
		handler : function() {
			$("#dlg_query").dialog("close");
		}
	} ]
};
