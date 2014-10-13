$(function(){
	loadPage()
	$("#region_dialog").css("display","block");
})

function loadPage(){
	$("#sensor-cjjg-edit").validatebox({
		required : false 
	});
	$("#sensor-fscs-edit").validatebox({
		required : false 
	});
	$("#sensor-mjkssj-edit").validatebox({
		required : false 
	});
	$("#sensor-mjjg-edit").validatebox({
		required : false 
	});
	$("#sensor-mjybs-edit").validatebox({
		required : false 
	});
	$("#sensor-wxkqsj-edit").validatebox({
		required : false 
	});
	$("#sensor-wxgbsj-edit").validatebox({
		required : false 
	});
	$("#sensor-sskssj-edit").validatebox({
		required : false
	});
	$("#sensor-ssjg-edit").validatebox({
		required : false 
	});
	$("#sensor-ssybs-edit").validatebox({
		required : false
	});
	
	$("#sensor-code-edit").combobox({
		width : 140,
		valueField : 'sensorid',
		textField : 'sensorname',
		editable : false,
		required : true,
		onHidePanel : function() {
			//$("#sensor-cjjg-edit").validatebox.required=true;
		}
	});
	
	$("#dev-type-query").combobox({
		width : 140,
		valueField : 'id',
		textField : 'typeName'
	}); 
	
	$("#dlg_edit").dialog(dlg_cfg).dialog(dlg_btn_edit_cfg).dialog({
		"onOpen":function(){
			$("#sensor-code-edit").combobox("reload","device-sensor!querySensorId.do");
		}
	});
	  
 
	$("#dlg_query").dialog(dlg_cfg).dialog(dlg_btn_query_cfg).dialog({
		"onOpen":function(){
			$("#dev-type-query").combobox("reload","device-type!queryType.do");
			$("#dev-accept-person-query").combobox("reload","accept-person!queryPerson.do");
		}
	});
	$("#dg").datagrid(dg_config);
	
	$("#dg").datagrid({
		url : "device-config!query.do"
	}).datagrid("load", {});
	
	$("#dg").datagrid({
		"onClickRow":function(rowIndex, rowData){
						$.ajax({ 
							  url:"device-realtime-data!getDeviceData.do?model.dataType=param&model.devCode=" + rowData.devCode,
							  type : "POST",
							  dataType : "html", 
							  success : function(data) {   
									var data = eval('(' + data + ')'); 
									var dataContent = data.data; 
				 
									var logtime= dataContent.logtime; 
									var ldataValue = dataContent.ldataValue;
									var ddataValue= dataContent.ddataValue;
									var dbeginValue= dataContent.dbeginValue;
									var dintervalValue= dataContent.dintervalValue;
									var dcountValue= dataContent.dcountValue;
									var lbeginValue= dataContent.lbeginValue;
									var lintervalValue= dataContent.lintervalValue;
									var lcountValue= dataContent.lcountValue;
									var warelessopenValue= dataContent.warelessopenValue;
									var warelesscloseValue= dataContent.warelesscloseValue;  
									
									var winObj = window.open("");
									winObj.document.write("<html>");
									winObj.document.write("<head>");
									winObj.document.write("<title>传感器配置信息<\/title>");
									winObj.document.write("<\/head>");
									winObj.document.write("<body>"); 
									winObj.document.write("<div id=\"ConfigInfo\">"); 
									winObj.document.write("噪声配置信息");  
									winObj.document.write("<table border=\"1\" id=\"configTbl\" class=\"easyui-layout\"><tr><th>记录日期<\/th><th>密集开始时间<\/th><th>密集间隔<\/th><th>密集样本数<\/th>");
									winObj.document.write("<th>松散开始时间<\/th><th>松散间隔<\/th><th>松散样本数<\/th><th>无线开始时间<\/th><th>无线结束时间<\/th><\/tr>");
									winObj.document.write("<tr><td>"+logtime+"<\/td><td>"+dbeginValue+"<\/td><td>"+dintervalValue+"<\/td><td>"+dcountValue+"<\/td><td>"
											+lbeginValue+"<\/td><td>"+lintervalValue+"<\/td><td>"+lcountValue+"<\/td><td>"+warelessopenValue+"<\/td><td>"+warelesscloseValue+"<\/td><\/tr>");
									winObj.document.write("<\/table>"); 
									
									
									winObj.document.write("<\/div>");
									winObj.document.write("<\/body>");
									winObj.document.write("<\/html>");
									winObj.document.close();
								   
							  }
						});
		  
		}
	}); 
}
var col = [ [ {
	field : 'id',
	title : '编号',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'devName',
	title : '名称',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'typeName',
	title : '类型',
	width : document.body.offsetWidth * 0.1
}, {
	field : 'active',
	title : '状态',
	width : document.body.offsetWidth * 0.1
},{
	field : 'active',
	title : '查看',
	width : document.body.offsetWidth * 0.1
} ] ];

var dg_config = {
	title : "设备信息",
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

var dlg_cfg = {
	width : 300,
	height : 250,
	border : true,
	closable : true,
	resizable : true,
	closed : true
}
  
var dlg_btn_edit_cfg = {
	buttons : [ {
		text : "确定",
		iconCls : 'icon-ok',
		handler : function() {
			var row = $("#dg").datagrid("getSelected"); 
			if (row) {
				$('#fm_edit').form('submit', {   
					url :"device-config!sendData.do?model.devCode="+row['id'],//confirmDeviceQuery(),//"device-config!edit.do?id=" + row.id,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						 
						var result = eval("(" + result + ")");
						if (result.success) {
							$("#dg").datagrid("reload"); 
							parent.editDev(result);
						}
						//$.messager.alert('结果', result.msg);
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
				url : "device!query.do"
			}).datagrid("load", {
				devCode : $("#dev-code-query").val(),
				devName : $("#dev-name-query").val(),
				personId : $("#dev-accept-person-query").combobox("getValue"),
				typeId : $("#dev-type-query").combobox("getValue")
			});
			$("#dlg_query").dialog("load", {});
		}
	}, {
		text : "取消",
		iconCls : 'icon-cancel',
		handler : function() {
			$("#dlg_query").dialog("close");
		}
	} ]
};

/**
*根据设备和传感器查询参数
*/
function confirmDeviceQuery(){  
	alert("here");
	$("viewDeviceConfigData").show();
	alert("here2");
	//打开显示设备配置数据弹出框
	viewSensorConfigDataDlgIndex = $.layer({
		type : 1,
		offset : [ '10px', '' ],
		shade : [ '', '', false ],
		area : [ 'auto', 'auto' ],
		page : {
			dom : '#viewDeviceConfigData'
		}
	});
	//生成datagrid
	var gridColumn = [[{
	    field : 'devCode',
	    title : '编号'
    }, {
	    field : 'devName',
	    title : '设备名称'
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
    	field : 'outDate',
    	title : '出厂日期'
    }, {
    	field : 'installDate',
    	title : '安装日期'
    }]];
$("#deviceTbls").datagrid({
	columns : gridColumn,
	url:"alarm/device-realtime-data!getDeviceProps.do?model.devCode=dev_car_1"
});
$("#deviceTbls").datagrid({
	"onClickRow" : function(rowIndex, rowData) {   

		document.getElementById("viewDeviceConfigDataDlg").style.display="block";
	}
});

//生成配置datagrid
var gridConfigColumn = [[{
    field : 'devCode',
    title : '编号'
}, {
    field : 'recordTime',
    title : '记录时间'
}, {
	field : 'dbegin',
	title : '密集开始时间'
}, {
	field : 'dinterval',
	title : '密集间隔'
}, {
	field : 'dcount',
	title : '密集样本数'
}, {
	field : 'lbegin',
	title : '松散开始时间'
}, {
	field : 'linterval',
	title : '松散间隔'
}, {
	field : 'lcount',
	title : '密集样本数'
}, {
	field : 'warelessopen',
	title : '无线开始时间'
}, {
	field : 'warelessclose',
	title : '无线结束时间'
}]]; 
$("#deviceConfigTbls").datagrid({ 
	columns : gridConfigColumn,
	url:"alarm/device-realtime-data!getDeviceData.do?model.dataType=param&model.devCode=dev_car_1"
});

//
//生成配置flowdatagrid
}