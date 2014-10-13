$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#dev-type-add").combobox({
		width : 140,
		valueField : 'id',
		textField : 'typeName',
		required : true
	});

	$("#dev-accept-person-add").combobox({
		width : 140,
		valueField : 'id',
		textField : 'personName'
	});

	$("#dev-type-edt").combobox({
		width : 140,
		valueField : 'id',
		textField : 'typeName',
		required : true
	});

	$("#dev-accept-person-edt").combobox({
		width : 140,
		valueField : 'id',
		textField : 'personName'
	});

	$("#dev-type-query").combobox({
		width : 140,
		valueField : 'id',
		textField : 'typeName'
	});

	$("#dev-accept-person-query").combobox({
		width : 140,
		valueField : 'id',
		textField : 'personName'
	});

	$("#dlg_add").dialog(dlg_btn_add_cfg).dialog({
		"onOpen" : function() {
			$("#dev-type-add").combobox("reload",
					"device-type!queryType.do");
			$("#dev-accept-person-add").combobox("reload",
					"accept-person!queryPerson.do");
		}
	});

	$("#dlg_edit").dialog(dlg_btn_edit_cfg).dialog({
		"onOpen" : function() {
			$("#dev-type-edt").combobox("reload",
					"device-type!queryType.do");
			$("#dev-accept-person-edt").combobox("reload",
					"accept-person!queryPerson.do");
		}
	});

	$("#dlg_query").dialog(dlg_btn_query_cfg).dialog({
		"onOpen" : function() {
			$("#dev-type-query").combobox("reload",
					"device-type!queryType.do");
			$("#dev-accept-person-query").combobox("reload",
					"accept-person!queryPerson.do");
		}
	});

	$("#dg").datagrid(dg_config);

	var p = $("#dg").datagrid("getPager");
	
	p.pagination({
		onRefresh : function() {
			
		}
	});
}

var col = [ [ {
	field : 'devCode',
	title : '编号'
}, {
	field : 'no',
	title : '编码'
}, {
	field : 'simid',
	title : 'SIM卡号'
}, {
	field : 'devName',
	title : '名称'
}, {
	field : 'typeName',
	title : '类型'
}, {
	field : 'longtitude',
	title : '经度'
}, {
	field : 'latitude',
	title : '纬度'
}, {
	field : 'height',
	title : '高度'
}, {
	field : 'personName',
	title : '负责人'
}, {
	field : 'factory',
	title : '工厂'
}, {
	field : 'beginUseTime',
	title : '开始使用时间'
}, {
	field : 'outDate',
	title : '出厂日期'
}, {
	field : 'installDate',
	title : '安装日期'
}, {
	field : 'installPosition',
	title : '安装位置'
}, {
	field : 'typeId',
	hidden : true
}, {
	field : 'personId',
	hidden : true
} ] ];

var dg_config = {
	title : "设备信息维护",
	columns : col,
	fit : true,
	pagePosition : "top",
	pagination : true,
	rownumbers : true,
	singleSelect : true,
	pageList : [ 10, 20, 30, 40, 50 ],
	pageSize : 10,
	idField : "id",
	url : "device!query.do",
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
						$.post("device!delete.do", {
							id : row.id
						}, function(result) {
							if (result.success) {
								$("#dg").datagrid("reload");
								parent.delDev(result);
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
	}, "-", {
		iconCls : 'icon-search',
		text : "查询",
		handler : function() {
			$("#dlg_query").dialog("open").dialog("center");
		}
	} ]
};

var dlg_btn_add_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			$('#fm_add').form('submit', {
				url : "device!save.do",
				onSubmit : function() {
					return $(this).form("validate");
				},
				success : function(result) {
					var result = eval("(" + result + ")");
					if (result.success) {
						$("#dg").datagrid("reload"); // reload the user data
						$('#fm_add').form("clear");
					}
					$.messager.alert('结果', result.msg);
					$("#dlg_add").dialog("close");
					parent.addDev(result);
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
					url : "device!edit.do?id=" + row.id,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var result = eval("(" + result + ")");
						if (result.success) {
							$("#dg").datagrid("reload");
							parent.editDev(result);
							$('#fm_edit').form("clear");
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
			// alert($("#dev-code-query").val());
			$("#dg").datagrid({
				url : "device!query.do",
				queryParams : {
					devCode : $("#dev-code-query").val(),
					devName : $("#dev-name-query").val(),
					personId : $("#dev-accept-person-query").combobox("getValue"),
					typeId : $("#dev-type-query").combobox("getValue")
				}
			});
			$("#dlg_query").dialog("close");
			$('#fm_query').form("clear");
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