var alarmGridColumns = [[{
	field : 'device',
	title : '设备名',
	width:100
},{
	field : 'message',
	title : '报警信息',
	width:100
},{
	field : 'itemName',
	title : 'itemName',
	width:100
},{
	field : 'itemValue',
	title : 'itemValue',
	width:100
},{
	field : 'recordDate',
	title : '报警日期',
	width:100
}]];

var gridColumns = [[{
	field : 'devName',
	title : '设备名',
	width:100
},{
	field : 'latitude',
	title : '经度',
	width:100
},{
	field : 'longtitude',
	title : '纬度',
	width:100
},{
	field : 'height',
	title : '高度',
	width:100
},{
	field : 'factory',
	title : '工厂',
	width:100
},{
	field : 'outDate',
	title : '出厂日期',
	width:100
},{
	field : 'installDate',
	title : '安装日期',
	width:100
}]];

var gridCfg = {
		columns:gridColumns,
		singleSelect:true,
		onDblClickRow : function(rowIndex, rowData) {
			jumpToByFeatureName(rowData.devCode);
		},
		onClickRow : function(rowIndex,
				rowData) {
		}
}

var alarmGridCfg = {
		columns:alarmGridColumns,
		singleSelect:true,
		onDblClickRow : function(rowIndex, rowData) {
			jumpToByFeatureName(rowData.code);
		},
		onClickRow : function(rowIndex,
				rowData) {
		}
};

var waterPipeDeviceControl = {
		deviceScan:function() {
			$.ajax({
				url:'alarm/water-pipeline-device-manage!queryDevice.do',
				type:'POST',
				success : function(data) {
					data = eval('(' + data + ')');
					$("#choice-area-query-tbl").datagrid(gridCfg);
					$("#choice-area-query-tbl").datagrid("loadData", data);
					$("#arlam_record_layout").layout("expand", "south");
				}
			});
		},
		deviceAlarmScan:function() {
			$.ajax({
				url:'alarm/water-pipeline-device-manage!queryDeviceAlarmRecord.do',
				type:'POST',
				success : function(data) {
					data = eval('(' + data + ')');
					$("#choice-area-query-tbl").datagrid(alarmGridCfg);
					$("#choice-area-query-tbl").datagrid("loadData", data);
					$("#arlam_record_layout").layout("expand", "south");
				}
			});
		}
}