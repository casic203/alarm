$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#dlg_add").dialog(dlg_cfg).dialog(dlg_btn_add_cfg).dialog({title : "新增"});

	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({title : "编辑"});
	
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({title : "查询"});

	$("#type-code-add").validatebox({
		required : true
	});
	$("#type-name-add").validatebox({
		required : true
	});
	$("#type-location-add").validatebox({
		required : true
	});
	$("#type-code-edit").validatebox({
		required : true
	});
	$("#type-name-edit").validatebox({
		required : true
	});
	$("#type-location-edit").validatebox({
		required : true
	});

	$("#dg").datagrid(dg_config);
	$("#dg").datagrid({
		url : "device-type!query.do"
	}).datagrid("load", {});
}

var col = [ [ {
	field : 'typeCode',
	title : '编码',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'typeName',
	title : '名称',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'location',
	title : '模型路径',
	width : document.body.offsetWidth * 0.5
} ] ];

var dg_config = {
	title : "设备类别维护",
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
						$.post("device-type!delete.do", {
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
	} ]
};

var dlg_cfg = {
	width : 300,
	height : 260,
	border : true,
	resizable : true,
	closed : true
}

var dlg_btn_add_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			$('#fm_add').form('submit', {
				url : "device-type!save.do",
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
					url : "device-type!edit.do?id=" + row.id,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var result = eval("(" + result + ")");
						if (result.success) {
							$("#dg").datagrid("reload"); // reload the user data
							parent.editDevType(result.src,result.dst,result.nam);
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
			$("#dg").datagrid({ 
					queryParams : {
						typeCode : $("#type-code-query").val(),
						typeName : $("#type-name-query").val()}
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

$.extend($.fn.validatebox.defaults.rules, {
	intOrFloat : {
		validator : function(value) {
			return /^\d+(\.\d+)?$/i.test(value);
		},
		message : '只能是数字.'
	},
	mobile : {
    	validator : function (value) {
            var reg = /^1[3|4|5|8|9]\d{9}$/;
            return reg.test(value);
        },
        message : '输入手机号码格式不准确.'
    }
});