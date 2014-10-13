$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#dlg_add").dialog(dlg_btn_add_cfg);
	$("#dlg_edit").dialog(dlg_btn_edit_cfg);
	$("#dlg_query").dialog(dlg_btn_query_cfg);
	$("#dg").datagrid(dg_config);
}

var col = [ [ {
	field : 'personCode',
	title : '账号',
	width : document.body.offsetWidth * 0.25
}, {
	field : 'personName',
	title : '姓名',
	width : document.body.offsetWidth * 0.25
}, {
	field : 'telePhone',
	title : '电话',
	width : document.body.offsetWidth * 0.25
}, {
	field : 'email',
	title : '邮件',
	width : document.body.offsetWidth * 0.25
} ] ];

var dg_config = {
	title : "接警人信息",
	columns : col,
	fit : true,
	pagePosition : "top",
	pagination : true,
	rownumbers : true,
	singleSelect : true,
	pageList : [ 10, 20, 30, 40, 50 ],
	pageSize : 10,
	idField : "id",
	url : "accept-person!query.do",
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
						$.post("accept-person!delete.do", {
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
	} ]
};

var dlg_btn_add_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			$('#fm_add').form('submit', {
				url : "accept-person!save.do",
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
					url : "accept-person!edit.do?id=" + row.id,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var result = eval("(" + result + ")");
						if (result.success) {
							$("#dg").datagrid("reload"); // reload the user
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
				personCode : $("#personCode_query").val(),
				personName : $("#personName_query").val()
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
	CHS : {
		validator : function(value) {
			return /^[\u0391-\uFFE5]+$/.test(value);
		},
		message : "只能输入汉字."
	},
    mobile : {
    	validator : function (value) {
            var reg = /^1[3|4|5|8|9]\d{9}$/;
            return reg.test(value);
        },
        message : "输入手机号码格式不准确."
    },
    account: {
        validator: function (value, param) {
            if (value.length < param[0] || value.length > param[1]) {
                $.fn.validatebox.defaults.rules.account.message = '用户名长度必须在' + param[0] + '至' + param[1] + '范围';
                return false;
            } else {
                if (!/^[\w]+$/.test(value)) {
                    $.fn.validatebox.defaults.rules.account.message = '用户名只能数字、字母、下划线组成.';
                    return false;
                } else {
                    return true;
                }
            }
        }, message: ""
    }
});
