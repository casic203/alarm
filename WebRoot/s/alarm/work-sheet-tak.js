$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})
function loadPage(){
	$("#query_device").combobox({
			valueField : 'id',
			textField : 'devName',
			url : "device!queryActive.do",
			cache : false,
			editable : false
		});
	$("#query_ok_btn").linkbutton({
			iconCls : "icon-ok"
		}).bind("click", function() {
			$("#dg").datagrid("load", {
				sheetNo : $("#query_sheet").val(),
				devId : $("#query_device").combobox("getValue"),
				beginDate : $("#query_begin").datebox("getValue"),
				endDate : $("#query_end").datebox("getValue")
			});
			$("#fm_query").form("clear");
			$("#win_query").window("close");
		});
	$("#query_cls_btn").linkbutton({
			iconCls : "icon-no"
		});
	$("#win_query").window({
			width : 500,
			height : 240,
			closed : true,
			modal : true,
			closable : false,
			minimizable : false,
			maximizable : false,
			resizable : true,
			title : "工单领取"
		});

	$("#dg").datagrid(_dg_cfg).datagrid({
			url : "work-sheet-tak!query.do"
		});
}

var col = [ [ {
	field : 'sheetNo',
	title : '单号',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'device',
	title : '设备',
	width : document.body.offsetWidth * 0.12
}, {
	field : 'alarmRecord',
	title : '故障',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'task',
	title : '任务',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'charger',
	title : '负责人',
	width : document.body.offsetWidth * 0.05
}, {
	field : 'beginDate',
	title : '开始日期',
	width : document.body.offsetWidth * 0.07
}, {
	field : 'endDate',
	title : '结束日期',
	width : document.body.offsetWidth * 0.07
}, {
	field : 'sheetStatus',
	title : '状态',
	width : document.body.offsetWidth * 0.05
} ] ];

var _dg_cfg = {
	title : "工单清单",
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
		iconCls : 'icon-remove',
		text : "领取",
		handler : function() {
			var row = $("#dg").datagrid("getSelected");
			if (row) {
				$.messager.confirm("Confirm", "您确定领取吗?", function(r) {
					if (r) {
						$.post("work-sheet-tak!takeWorkSheet.do", {
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
				$.messager.alert("提示","请选择要领取的工单！");
			}
		}
	}, "-", {
		iconCls : 'icon-search',
		text : "查询",
		handler : function() {
			$("#win_query").window("open").window("center");
		}
	} ]
};