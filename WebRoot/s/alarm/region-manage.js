var regionDlgIndex = null;
var choiceParentRegionIndex = null;
var choiceDeviceIndex = null;
var queryRegionDlgIndex = null;

$("#regionTbl").treegrid(
		{
			animate : true,
			fit : true,
			fitColumns : true,
			collapsible : true,
			treeField : 'elementName',
			url : 'alarm/region-manage!queryRegionTreeData.do',
			toolbar : '#regionMenu',
			idField : 'id',
			singleSelect : true,
			checkOnSelect : false,
			selectOnCheck : false,
			onCheck : function(rowData) {
				if (!rowData.isRegion) {
					featureVisible(rowData.deviceCode, true);
				} else {
					var childrenNodes = $('#regionTbl').treegrid('getChildren',
							rowData.id);
					for ( var i = 0; i < childrenNodes.length; i++) {
						$("#regionTbl").treegrid('update', {
							id : childrenNodes[i].id,
							row : {
								checked : true
							}
						});
						if (!childrenNodes[i].isRegion) {
							featureVisible(childrenNodes[i].deviceCode, true);
						}
					}
				}
			},
			onUncheck : function(rowData) {
				if (!rowData.isRegion) {
					featureVisible(rowData.deviceCode, false);
				} else {
					$("#regionTbl").treegrid('update', {
						id : rowData.id,
						row : {
							checked : false
						}
					});
					var childrenNodes = $('#regionTbl').treegrid('getChildren',
							rowData.id);
					unCheckSubNodes(rowData, childrenNodes);
				}
			},
			onDblClickRow : function(row) {
				if (!row.isRegion) {
					flyToDeviceLayerByFeatureId(row.deviceCode);
				}
			}
		});

function unCheckSubNodes(parentNode, childrenNodes) {
	var popNodes = [];
	for ( var i = 0; i < childrenNodes.length; i++) {
		$("#regionTbl").treegrid('update', {
			id : childrenNodes[i].id,
			row : {
				checked : false
			}
		});
		if (!childrenNodes[i].isRegion) {
			featureVisible(childrenNodes[i].deviceCode, false);
		}
	}
	var curData = $("#regionTbl").treegrid("getData");
	$('#regionTbl').treegrid('loadData', curData);
}

function clickRowCallback(row) {
	$("#deviceTbl").datagrid({
		url : "alarm/region-manage!queryDevicesByRegionId.do?model.regionId=" + row.id
	});
}

/**
 * 开始选择分区
 */
function beginCreateRegion() {
	$("#tab_div").tabs("select", 'Home');
	$("#operationType").val('0');
	$("#labelBtn").html("创建");
	regionDlgIndex = $.layer({
		type : 1,
		offset : [ '150px', '' ],
		shade : [ '', '', false ],
		area : [ 'auto', 'auto' ],
		page : {
			dom : '#createRegionDlg'
		}
	});
	$("#regionName").val('');
	$("#regionArea").val('');
	$("#parentRegionId").val('');
	$("#choiceName").html('');
	$("#regionId").val('');
	$("#choiceDeviceTbl").datagrid({
		url : "alarm/region-manage!queryNoRegionDevice.do"
	});
}

/**
 * 弹出选择父分区对话框
 */
function choiceParentRegion() {
	choiceParentRegionIndex = $.layer({
		type : 1,
		offset : [ '150px', '' ],
		shade : [ '', '', false ],
		area : [ 'auto', 'auto' ],
		page : {
			dom : '#choiceParentRegionDlg'
		}
	});
	$("#choiceParentRegionTbl").datagrid({
		url : "alarm/region-manage!queryRegion.do"
	});
}

/**
 * 
 * 确认选择父分区
 */
function confirmChoicePRegion() {
	var selectedRow = $("#choiceParentRegionTbl").datagrid("getSelected");
	if (selectedRow.regionName == $("#regionName").val()) {
		alert("不能选择当前分区作为父分区");
		return;
	}
	$("#parentRegionId").val(selectedRow.id);
	$("#choiceName").html(selectedRow.regionName);
	parent.layer.close(choiceParentRegionIndex);
}

/**
 * 
 * 确认创建分区
 */
function confirmCreateOrUpdateRegion() {
	var submitUrl = null;
	var oType = $("#operationType").val();
	if (oType == '0') {
		submitUrl = "alarm/region-manage!createRegion.do";
	} else if (oType == '1') {
		submitUrl = "alarm/region-manage!confirmModifyRegion.do";
	}

	var devices = $("#choiceDeviceTbl").datagrid("getSelections");
	var deviceIds = "";
	for ( var i = 0; i < devices.length; i++) {
		deviceIds += (devices[i].id + "-");
	}
	deviceIds = deviceIds.substring(0, deviceIds.length - 1);
	$("#deviceIds").val(deviceIds);

	$("#ff").form("submit", {
		url : submitUrl,
		onSubmit : function() {
			var isValid = $(this).form('validate');// do some check
			if (!isValid) {
			}
			return isValid; // return false will stop the form submission
		},
		success : function(data) {
			var data = eval("(" + data + ")");
			if (data.success) {
				$("#regionTbl").treegrid("reload");
				parent.layer.close(regionDlgIndex);
			}
		}
	});
}

/**
 * 
 * 删除选择的分区
 */
function beginDeleteRegion() {
	$("#tab_div").tabs("select", 'Home');

	var selected = $("#regionTbl").treegrid("getSelected");
	if (selected != null) {
		if (!selected.isRegion) {
			alert("只能操作分区");
			return;
		}

		if (!confirm("确认删除选择的分区?")) {
			return;
		}

		$.ajax({
			type : "POST",
			url : "alarm/region-manage!deleteRegion.do?model.regionId="	+ selected.id,
			success : function(data) {
				var data = eval("(" + data + ")");
				if (data.success) {
					$("#regionTbl").treegrid("reload");
				}
			}
		});
	}
}

/**
 * 
 * 修改分区
 */
function beginModifyRegion() {
	$("#tab_div").tabs("select", 'Home');
	$("#operationType").val('1');
	$("#labelBtn").html("修改");
	// 查找选择的分区信息
	var seledtedRegion = $("#regionTbl").treegrid("getSelected");
	if (seledtedRegion != null) {
		if (!seledtedRegion.isRegion) {
			alert("只能操作分区");
			return;
		}

		$.ajax({
			type : "GET",
			url : "alarm/region-manage!beginModifyRegion.do?model.regionId=" + seledtedRegion.id + "&time=" + new Date(),
			success : function(data) {
				regionDlgIndex = $.layer({
					type : 1,
					offset : [ '150px', '' ],
					shade : [ '', '', false ],
					area : [ 'auto', 'auto' ],
					page : {
						dom : '#createRegionDlg'
					}
				});
	
				// 取出分区信息
				var data = eval("(" + data + ")");
				var regionId = data.id;
				var regionName = data.regionName;
				var regionArea = data.regionArea;
				var parentId = data._parentId;
				var parentName = data.parentName;
				var deviceIdArray = [];
				if (data.deviceIds != null) {
					deviceIdArray = data.deviceIds.split("-");
				}
				$("#choiceDeviceTbl")
						.datagrid(
								{
									url : "alarm/region-manage!queryNoRegionDevice.do?model.regionId="
											+ regionId,
									onLoadSuccess : function(data) {
										var curPageRows = $(
												"#choiceDeviceTbl")
												.datagrid("getRows");
										for ( var i = 0; i < deviceIdArray.length; i++) {
											for ( var j = 0; j < curPageRows.length; j++) {
												var tmpRow = curPageRows[j];
												if (tmpRow.id == parseInt(deviceIdArray[i])) {
													$(
															"#choiceDeviceTbl")
															.datagrid(
																	"selectRow",
																	j);
													break;
												}
											}
										}
									}
								});
	
				$("#regionName").val(regionName);
				$("#regionArea").val(regionArea);
				$("#parentRegionId").val(parentId);
				$("#choiceName").html(parentName);
				$("#regionId").val(regionId);
			}
		});
	}
}

function queryRegion() {
	var queryKey = $("#queryRegionName").val();
	$.post("alarm/region-manage!queryRegion.do", {
		"model.regionName" : queryKey
	}, function(result) {
		$("#regionTbl").treegrid("loadData", result);
	}, "json");
	parent.layer.close(queryRegionDlgIndex);
}

function popRegionQueryDialog() {
	queryRegionDlgIndex = $.layer({
		type : 1,
		offset : [ '100px', '' ],
		shade : [ '', '', false ],
		area : [ 'auto', 'auto' ],
		page : {
			dom : '#queryRegionDlg'
		}
	});
	$("#queryRegionName").val('');
}

$("#waterPipeLineFunTbl")
		.treegrid(
				{
					animate : true,
					fit : true,
					fitColumns : true,
					collapsible : true,
					idField : 'id',
					treeField : 'name',
					singleSelect : true,
					checkOnSelect : false,
					selectOnCheck : false,
					data :  [ {
						id : 1,
						iconCls : 'icon-menm-item',
						name : '分区管理'
					}, {
						id : 2,
						iconCls : 'icon-menm-item',
						name : '全区域分区漏损查看'
					}, {
					    id : 8,
					    iconCls:'icon-menm-item',
					    name:'设备在线动态监测'
					}, {
					    id : 9,
					    iconCls:'icon-menm-item',
					    name:'设备运行状态纵览'
					}, {
					    id : 10,
					    iconCls:'icon-menm-item',
					    name:'派工处置'
					}, {
						"id" : 3,
						"iconCls" : 'icon-menm-item',
						name : '水平衡分析'
					}, {
						"id" : 4,
						"iconCls" : 'icon-menm-item',
						name : '漏损评估对比分析'
					}, {
						"id" : 5,
						"iconCls" : 'icon-menm-item',
						name : '产销差分析'
					}, {
						"id" : 6,
						"iconCls" : 'icon-menm-item',
						name : '给水管线形变信息分析'
					}, {
						"id" : 7,
						"iconCls" : 'icon-menm-item',
						name : '给水管线开挖报警分析'
					} ],
					onClickRow : function(row) {
						$("#tab_div").tabs("select", 'Home');
						$("#arlam_record_layout").layout("collapse", "east");
						if (row.id == 1) {
							$.layer({
								type : 2,
								title : "分区管理",
								offset : [ '20px', '' ],
								area : [ "900px", "600px" ],
								iframe : {
									src : 'dma.jsp'
								}
							});
						}
						if (row.id == 2 || row.id == 4) {
							$("#arlam_record_layout").layout("expand", "south");

							var camerastate = myGlobalCtrl.Globe.CameraState;
							camerastate.Longitude = 120.610963;
							camerastate.Latitude = 31.187621;
							camerastate.Distance = 4000;
							myGlobalCtrl.Globe.FlyToCameraState(camerastate);

							var gridColumn = "[[{field : 'BData_DMA', title:'分区编号'}, {field : 'ReportDate', title:'评估日期'},"
									+ "{field : 'LeakRate', title:'漏损率'}, {field : 'LeakControlRate', title:'阶段漏损控制目标'},"
									+ "{field : 'SaleDiffWaterRate', title:'产销差量'}, {field : 'LeakState', title:'是否漏损状态'}]]";

							gridColumn = eval('(' + gridColumn + ')');

							// 查询webservice
							var leakRegionDlgHeightoffset = ($(document)
									.height() - 590)
									+ 'px';
							var leakRegionDlgWidthoffset = ($(document).width() - 280)
									+ 'px';
							$
									.ajax({
										type : "POST",
										url : 'alarm/water-pipeline-analysis!allLeakRegion.do',
										success : function(data) {
											var data = eval('(' + data + ')');
											$('#choice-area-query-tbl')
													.datagrid({
														url : ''
													});
											$('#choice-area-query-tbl')
													.datagrid(
															{
																columns : gridColumn,
																data : data,
																singleSelect : true,
																fit : true,
																onClickRow : function(
																		rowIndex,
																		rowData) {

																	$(
																			"#arlam_record_layout")
																			.layout(
																					"expand",
																					"east");
																	var propColumn = "[[{field : 'propName', title:'属性名'}, {field : 'propValue', title:'属性值'}]]";
																	propColumn = eval('('
																			+ propColumn
																			+ ')');
																	// 显示漏损信息
																	$(
																			"#leakRegionInfo")
																			.datagrid(
																					{
																						columns : propColumn,
																						url : 'alarm/water-pipeline-analysis!getLeakInfoByRegionId.do?model.regionId='
																								+ rowData.BData_DMA,
																						singleSelect : true,
																						fit : true
																					});
																}
															});
										}
									});
						}
						if (row.id == 3) {
							$("#arlam_record_layout").layout("expand", "south");

							var gridColumn = "[[{field:'DmaID', title:'分区编号'}, {field:'AnalysisDate', title:'分析日期'}, {field:'WaterSupply', title:'供水量'},"
									+ "{field:'WaterSale', title:'售水量'}, {field:'NoValueWater', title:'无收益水量'}, {field:'LR_Leakage', title:'漏失率'},"
									+ "{field:'LR_WaterME', title:'表误'}, {field:'LR_MeterE', title:'计量错误'}, {field:'LR_Favor', title:'人情水'},"
									+ "{field:'LR_Steal', title:'偷水'}, {field:'LR_Pressure', title:'压力'}, {field:'SaleDiffWater', title:'产销差'}]]";
							gridColumn = eval('(' + gridColumn + ')');
							$('#choice-area-query-tbl')
									.datagrid(
											{
												columns : gridColumn,
												url : 'alarm/water-pipeline-analysis!waterBalance.do',
												singleSelect : true,
												fit : true,
												onClickRow : function(rowIndex,
														rowData) {
												}
											});
						}
						if (row.id == 5) {
							$("#arlam_record_layout").layout("expand", "south");

							var gridColumn = "[[{field : 'BData_DMA', title:'分区编号'}, {field : 'ReportDate', title:'评估日期'},"
									+ "{field : 'SupplyWater', title:'日供水量'}, {field : 'SaleWater', title:'售水量'},"
									+ "{field : 'SaleDiffWater', title:'产销差量'}, {field : 'SaleDiffWaterRate', title:'产销差率'}]]";
							gridColumn = eval('(' + gridColumn + ')');
							$('#choice-area-query-tbl')
									.datagrid(
											{
												columns : gridColumn,
												url : 'alarm/water-pipeline-analysis!getAllRegionDisLosses.do',
												singleSelect : true,
												fit : true,
												onClickRow : function(rowIndex,
														rowData) {
												}
											});
						}
						if(row.id == 6) {
							shapeInfoAnalysis('给水管线', '光纤');
						}
						if(row.id == 7) {
							excavationAlarmAnalysis('给水管线');
						}
						if(row.id == 8) {
							waterPipeDeviceControl.deviceScan();
						} 
						if(row.id == 9) {
							waterPipeDeviceControl.deviceAlarmScan();
						}
						if(row.id == 10) {
							addTab('工单派发', 'alarm/work-sheet-add.do');
						}
					}
				});

$("#device_info_manager_tbl").treegrid({
	animate : true,
	fit : true,
	fitColumns : true,
	collapsible : true,
	idField : 'id',
	treeField : 'name',
	singleSelect : true,
	checkOnSelect : false,
	selectOnCheck : false,
	data : [ {
		"iconCls" : "icon-menm-item",
		"id" : 1,
		"name" : "设备信息维护"
	}, {
		"iconCls" : "icon-menm-item",
		"id" : 2,
		"name" : "设备类别维护"
	}, {
		"iconCls" : "icon-menm-item",
		"id" : 3,
		"name" : "传感器类型管理"
	}, {
		"iconCls" : "icon-menm-item",
		"id" : 4,
		"name" : "设备传感器管理"
	} ],
	onClickRow : function(row) {
		if (row.id == 1) {
			addTab('设备信息维护', 'alarm/device.do');
		}
		if (row.id == 2) {
			addTab('设备类别维护', 'alarm/device-type.do');
		}
		if (row.id == 3) {
			addTab('传感器类型管理', 'alarm/sensor-type.do');
		}
		if (row.id == 4) {
			addTab('设备传感器管理', 'alarm/device-sensor.do');
		}
	}
});

$("#alarm_info_manager_tbl").treegrid({
	animate : true,
	fit : true,
	fitColumns : true,
	collapsible : true,
	idField : 'id',
	treeField : 'name',
	singleSelect : true,
	checkOnSelect : false,
	selectOnCheck : false,
	data : [ {
		"iconCls" : 'icon-menm-item',
		"id" : 1,
		"name" : "接警人维护"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 2,
		"name" : "报警查询"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 4,
		"name" : "生成报表"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 5,
		"name" : "未处理报警摘要查询",
		"children" : [ {
			"iconCls" : 'icon-menm-item',
			"id" : 51,
			"name" : "全部区域统计"
		}, {
			"iconCls" : 'icon-menm-item',
			"id" : 52,
			"name" : "选择区域统计"
		} ]
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 6,
		"name" : "已处理报警摘要查询",
		"children" : [ {
			"iconCls" : 'icon-menm-item',
			"id" : 61,
			"name" : "全部区域统计"
		}, {
			"iconCls" : 'icon-menm-item',
			"id" : 62,
			"name" : "选择区域统计"
		} ]
	} ],
	onClickRow : function(row) {
		if (row.id == 1) {
			addTab('接警人维护', 'alarm/accept-person.do')
		}
		if (row.id == 2) {
			addTab('报警查询', 'content/alarm/add-alarm-record.jsp')
		}
		if (row.id == 3) {
			addTab('故障处理', 'content/alarm/deal-alarm-record.jsp')
		}
		if (row.id == 4) {
			exportReport();
		}
		if (row.id == 51) {
			get_all_region_alarm_record_active();
		}
		if (row.id == 52) {
			get_part_region_alarm_record_active();
		}
		if (row.id == 61) {
			$.layer({
				type : 2,
				title : "日期选择",
				offset : [ '20px', '' ],
				area : [ "300px", "310px" ],
				iframe : {
					src : 'calendar.jsp?scope=all'
				}
			});
		}
		if (row.id == 62) {
			$.layer({
				type : 2,
				title : "日期选择",
				offset : [ '20px', '' ],
				area : [ "300px", "310px" ],
				iframe : {
					src : 'calendar.jsp?scope=part'
				}
			});
		}
	}
});

$("#work_sheet_manager_tbl").treegrid({
	animate : true,
	fit : true,
	fitColumns : true,
	collapsible : true,
	idField : 'id',
	treeField : 'name',
	singleSelect : true,
	checkOnSelect : false,
	selectOnCheck : false,
	data : [ {
		"iconCls" : 'icon-menm-item',
		"id" : 1,
		"name" : "工单派发"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 2,
		"name" : "工单审核"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 3,
		"name" : "工单领取"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 4,
		"name" : "维修反馈"
	} ],
	onClickRow : function(row) {
		if (row.id == 1) {
			addTab('工单派发', 'alarm/work-sheet-add.do');
		}
		if (row.id == 2) {
			addTab('工单审核', 'alarm/work-sheet-chk.do');
		}
		if (row.id == 3) {
			addTab('工单领取', 'alarm/work-sheet-tak.do');
		}
		if (row.id == 4) {
			addTab('维修反馈', 'alarm/work-sheet-feedback.do');
		}
	}
});

$("#pipe_search_tbl").treegrid({
	animate : true,
	fit : true,
	fitColumns : true,
	collapsible : true,
	idField : 'id',
	treeField : 'name',
	singleSelect : true,
	checkOnSelect : false,
	selectOnCheck : false,
	data : [ {
		"iconCls" : 'icon-menm-item',
		"id" : 1,
		"name" : "设备统计",
		"children" : [ {
			"iconCls" : 'icon-menm-item',
			"id" : 11,
			"name" : "全部区域统计"
		}, {
			"iconCls" : 'icon-menm-item',
			"id" : 12,
			"name" : "选择区域统计"
		} ]
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 2,
		"name" : "设备查询",
		"children" : [ {
			"iconCls" : 'icon-menm-item',
			"id" : 21,
			"name" : "根据设备名称查询"
		} ]
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 3,
		"name" : "管线统计",
		"children" : [ {
			"iconCls" : 'icon-menm-item',
			"id" : 31,
			"name" : "全部区域统计"
		}, {
			"iconCls" : 'icon-menm-item',
			"id" : 32,
			"name" : "选择区域统计"
		} ]
	},{
		"iconCls" : 'icon-menm-item',
		"id" : 4,
		"name" : "管线查询"
	} ],
	onClickRow : function(row) {
		if (row.id == 11) {
			globeControl_query.displayAllDevices()
		}
		if (row.id == 12) {
			trackPolygonAnalysisBeginEvent(0);
		}
		if (row.id == 21) {
			deviceQueryDialog();
		}
		if (row.id == 31) {
			allAreaCal(1);
		}
		if (row.id == 32) {
			trackPolygonAnalysisBeginEvent(1);
		}
		if (row.id == 4) {
			keyQueryDialog(1);
		}
	}
});

$("#database_manage_tbl").treegrid({
	animate : true,
	fit : true,
	fitColumns : true,
	collapsible : true,
	idField : 'id',
	treeField : 'name',
	singleSelect : true,
	checkOnSelect : false,
	selectOnCheck : false,
	data : [ {
		"iconCls" : 'icon-menm-item',
		"id" : 1,
		"name" : "数据库备份"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 2,
		"name" : "监控结果"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 3,
		"name" : "传感器标准参数配置"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 4,
		"name" : "传感器参数配置"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 5,
		"name" : "设备参数维护"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 6,
		"name" : "数据帧格式管理"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 7,
		"name" : "传感器报警规则管理"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 8,
		"name" : "系统日志查看"
	} ],
	onClickRow : function(row) {
		if (row.id == 1) {
			addTab('数据库备份', 'alarm/data-base-monitor-rule.do');
		}
		if (row.id == 2) {
			addTab('数据库监控', 'content/alarm/data-base-monitor-result.jsp');
		}
		if(row.id==3){
			addTab('传感器标准参数配置','alarm/device-sconfig.do');
		}
		if(row.id==4){
			addTab('传感器参数配置','alarm/device-sproperty.do');
		}
		if(row.id==5){
			addTab('设备参数维护','alarm/device-config.do');
		}
		if(row.id==6){
			addTab('数据帧格式管理','alarm/device-sensorformat.do');
		}
		if(row.id==7){
			addTab('传感器报警规则管理','alarm/alarm-rule.do');
		}
		if(row.id==8){
			addTab('系统日志查看','alarm/sys-log.do');
		}
	}
});

$("#gasPipeTbl").treegrid({
	animate : true,
	fit : true,
	fitColumns : true,
	collapsible : true,
	idField : 'id',
	treeField : 'name',
	singleSelect : true,
	checkOnSelect : false,
	selectOnCheck : false,
	data : [ {
		"iconCls" : 'icon-menm-item',
		"id" : 11,
		"name" : "燃气管线状态动态监测（GIS地图）"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 3,
		"name" : "燃气管线现状评估"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 4,
		"name" : "燃气管线局部纵断面分析"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 5,
		"name" : "燃气管线接入方案评估"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 6,
		"name" : "燃气管线破损风险评估"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 7,
		"name" : "燃气管线布局评估与优化"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 8,
		"name" : "燃气阀门井燃气浓度分析"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 9,
		"name" : "燃气管线形变信息分析"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 10,
		"name" : "燃气管线开挖报警分析"
	}],
	onClickRow : function(row) {
		if(row.id == 9) {
			shapeInfoAnalysis('天然气', '光纤');
		}
		if(row.id == 10) {
			excavationAlarmAnalysis('天然气');
		}
		if(row.id == 11) {
			deviceControl.viewDevice('设备图层', '燃气');
		}
		if(row.id == 8) {
			deviceControl.viewDevice('设备图层', '燃气');
		}
	}
});

$("#otherPipeLineTbl").treegrid({
	animate : true,
	fit : true,
	fitColumns : true,
	collapsible : true,
	idField : 'id',
	treeField : 'name',
	singleSelect : true,
	checkOnSelect : false,
	selectOnCheck : false,
	data : [ {
		"iconCls" : 'icon-menm-item',
		"id" : 1,
		"name" : "爆管影响区域分析"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 2,
		"name" : "热力管线监测信息动态显示（GIS地图）"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 3,
		"name" : "热力管网热力工况分析"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 4,
		"name" : "热力管线形变信息分析"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 5,
		"name" : "热力管线开挖报警分析"
	}, {
		"iconCls" : 'icon-menm-item',
		"id" : 6,
		"name" : "电力管线开挖报警分析"
	}],
	onClickRow : function(row) {
		if (row.id == 1) {
			BufferAnalysis();
		}
		if(row.id == 2) {
			deviceControl.viewDevice('光纤', '');
		}
		if (row.id == 4) {
			shapeInfoAnalysis('热力', '光纤')
		}
		if (row.id == 5) {
			excavationAlarmAnalysis('热力')
		}
		if (row.id == 6) {
			excavationAlarmAnalysis('电力');
		}
	}
});

// ***************************************liuwei 雨水部分
// 20140926***************************//
// 雨水管线漏损分析 wei
$("#rainPipeLineFunTbl").datagrid({
	iconCls : 'icon-ok',
	animate : true,
	fit : true,
	fitColumns : true,
	collapsible : true,
	data : [ {
		elementName : '监测点信息查看'
	}, {
		elementName : '监测点信息查询'
	}, {
		elementName : '管道横截面图显示'
	}, {
		elementName : '管道纵断面图显示'
	}, {
		elementName : '历史监测数据曲线图'
	}, {
		elementName : '入流入渗分析'
	}, {
		elementName : '清除分析结果'
	}, {
		elementName : '节点过载统计分析'
	}, {
		elementName : '管道充满度分析'
	}, {
		elementName : '管道充满度频次统计'
	}, {
		elementName : '管道充满度空间分布'
	}, {
		elementName : '溢流点分布和统计'
	}, {
		elementName : '抢修专题图生成'
	}, {
		elementName : '溢流可能性分析'
	}],
	singleSelect : true,
	checkOnSelect : false,
	selectOnCheck : false,
	onClickRow : function(rowIndex, rowData) {
		// ***********************功能1********************************//
		// 功能：监测点信息查看
		if (rowIndex == 0) {
			var gridColumn = [ [ {
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
			} ] ];
			var leakRegionDlgHeightoffset = ($(document).height() - 590) + 'px';
			var leakRegionDlgWidthoffset = ($(document).width() - 280) + 'px';
			$("#choice-area-query-tbl").datagrid({
				columns : gridColumn,
				url : "alarm/device-realtime-data!getLiquidProps.do",
				onDblClickRow : function(rowIndex, rowData) {
					// 飞行定位到传感器位置
					var camerastate = myGlobalCtrl.Globe.CameraState;
					camerastate.Longitude = rowData.longtitude;
					camerastate.Latitude = rowData.latitude;
					camerastate.Distance = 10;
					myGlobalCtrl.Globe.FlyToCameraState(camerastate);
					myGlobalCtrl.Globe.FlyAlongLineSpeed = 180000;
					myGlobalCtrl.Globe.FlyAlongLineRotateSpeed = 180000;
					// 弹出dialog
					var LeakDlgIndex = $.layer({
								type : 1,
								offset : [
										leakRegionDlgHeightoffset,
										leakRegionDlgWidthoffset ],
								shade : [ '','',false ],
								area : ['auto','auto' ],
								page : {dom : '#leakLiquidDlg'}
							});
					var propColumn = "[[{field : 'devId', title:'设备编号'},"
							+ "{field : 'liquidData', title:'液位值'},"
							+ " {field : 'liquidPower', title:'设备电压'},"
							+ " {field : 'time', title:'上传时间'}]]";
					propColumn = eval('(' + propColumn + ')');
					// 显示漏损信息
					$("#leakLiquidInfo").datagrid({
						columns : propColumn,
						url : 'alarm/device-realtime-data!getLiquidRecord.do?model.devCode='
								+ rowData.devCode
								+ "&model.dataType=realtime&time="
								+ new Date(),
						singleSelect : true,
						fit : true
					});
				}
			});
			$("#arlam_record_layout").layout("expand", "south");
		}
		// ***********************功能2********************************//
		// 功能：监测点信息查询
		if (rowIndex == 1) {
			// 弹出dialog
			LiquidDlgIndex = $.layer({
				type : 1,
				title : "监测点信息查询",
				shade : [ '', '', false ],
				area : [ 'auto', 'auto' ],
				page : {
					dom : '#codeSearch-query-dialog'
				}
			});
		}
		// ***********************功能3********************************//
		// 管道横截面分析
		if (rowIndex == 2) {
			// 取消事件监听器
			myGlobalCtrl.detachEvent("FireFeatureMouseClick",showballoon);
			// 设置新的事件监听
			myGlobalCtrl.attachEvent("FireFeatureMouseClick",transverseSection);
		}
		// ***********************功能4********************************//
		// 管道纵断面分析
		if (rowIndex == 3) {
			// 取消事件监听器
			myGlobalCtrl.detachEvent("FireFeatureMouseClick",showballoon);
			// 设置新的事件监听
			myGlobalCtrl.attachEvent("FireFeatureMouseClick",longiSection);
		}
		// ***********************功能5********************************//
		// 历史监测数据曲线
		if (rowIndex == 4) {
			// 取消事件监听器
			myGlobalCtrl.detachEvent("FireFeatureMouseClick",showballoon);
			// 设置新的事件监听
			myGlobalCtrl.attachEvent("FireFeatureMouseClick",historyCurve);
		}
		// ***********************功能6********************************//
		// 入流入渗分析
		if (rowIndex == 5) {
			// *****获取需改变的雨水管线列表******//
			var layerIdLine = null;
			var layerIdPoint = null;
			var layerIdWu = null;
			var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("雨水管线");
			if (layer != null) {
				layerIdLine = layer.ID;
			}
			var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("雨水管线附属物");
			if (layer != null) {
				layerIdWu = layer.ID;
			}
			CollectGarbage();
			var layerId;
			$.ajax({
				url : "alarm/device-realtime-data!getAlarmLiquidRecord.do",
				type : "POST",
				success : function(data) {
					var data = eval('(' + data + ')');
					if (data[0] == "noneRain") {
						alert("当前未下雨，没有入流入渗管线！");
					} else {
						flag = true;
						var num = data.length;
						var i = 0;
						var j = 0;
						for (i = 0; i < num; i++) {
							if (myGlobalCtrl.Globe.Layers.Count > 0) {
								for ( var nu = 0; nu < myGlobalCtrl.Globe.Layers.Count; nu++) {
									var pLayer = myGlobalCtrl.Globe.Layers.GetLayerByID(nu);
									if ((pLayer != null)
											&& (pLayer.Caption() != "雨水管线")
											&& (pLayer.Name.substring(0,4) != "fttp")
											&& (pLayer.Caption() != "雨水管线附属物"))
										pLayer.Visible = false;
								}
								myGlobalCtrl.refresh();
								var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("雨水管线");
								if ((layer != null)) {
									layerId = layer.ID;
									features = GetAllFeaturesByID(layer.ID);
									for (j = 0; j < features.Count; j++) {
										var feature = features.Item(j);
										var defn = feature.GetFieldValue(0);
										if (defn == data[i]) {
											var line = feature.Geometry;
											var color = myGlobalCtrl.CreateColorRGBA();
											color.SetValue(250,0,0,255);
											line.Style.LineColor = color;
											myGlobalCtrl.Refresh();
											myGlobalCtrl.Globe.FlyToFeature(feature);
											break;
										}
									}
								}
							}
						}
					}
				}
			});
	
			var gridColumn = [ [ {
				field : 'dbCord',
				title : '编号'
			}, {
				field : 'liquid',
				title : '液位'
			}, {
				field : 'diameter',
				title : '管径'
			}, {
				field : 'type',
				title : '类别'
			}, {
				field : 'time',
				title : '上传时间'
			} ] ];
			$("#choice-area-query-tbl").datagrid({
				columns : gridColumn,
				url : "alarm/device-realtime-data!getAlarmLiquidRecordList.do",
				singleSelect : true,
				fit : true,
				onLoadSuccess : function(data) {
					if (data.total > 0) {
						$("#arlam_record_layout").layout("expand","south");
					}
				},
				// 飞行定位到传感器位置
				onDblClickRow : function(rowIndex, rowData) {
					flag = true;
					var featureCord = rowData.dbCord;
					features = GetAllFeaturesByID(layerIdLine);
					for (j = 0; j < features.Count; j++) {
						var feature = features.Item(j);
						var defn = feature.GetFieldValue(0);
						if (defn == featureCord) {
							myGlobalCtrl.Globe.FlyToFeature(feature);
							break;
						}
					}
					features = GetAllFeaturesByID(layerIdWu);
					for (j = 0; j < features.Count; j++) {
						var feature = features.Item(j);
						var defn = feature.GetFieldValue(0);
						feature.HighLight = true;
						if (defn == featureCord) {
							myGlobalCtrl.Globe.FlyToFeature(feature);
							break;
						}
					}
				}
	
			});
		}
	
		// ***********************功能7********************************//
		// 清除入流入渗分析记录
		if (rowIndex == 6) {
			myGlobalCtrl.Globe.MemoryLayer.RemoveAllFeature();
			var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("雨水管线");
			if ((layer != null)) {
				layerId = layer.ID;
				features = GetAllFeaturesByID(layer.ID);
				for (j = 0; j < features.Count; j++) {
					var feature = features.Item(j);
					var line = feature.Geometry;
					var color = myGlobalCtrl.CreateColorRGBA();
					color.SetValue(250, 128, 0, 255);
					line.Style.LineColor = color;
				}
			}
			myGlobalCtrl.Refresh();
			for ( var nu = 0; nu < myGlobalCtrl.Globe.Layers.Count; nu++) {
				var pLayer = myGlobalCtrl.Globe.Layers.GetLayerByID(nu);
	
				if ((pLayer != null) && (pLayer.Caption() != "雨水管线"))
					pLayer.Visible = true;
			}
			myGlobalCtrl.Refresh();
		}
		// ***********************功能8********************************//
		// 节点过载统计分析
		if (rowIndex == 7) {
			var layerId;
			var yubi = 0;
			var rainWall = 0;
			var s1 = [];
			var ticks = [];
			$.ajax({
				url : "alarm/device-realtime-data!getAlarmLiquidRecord.do",
				type : "POST",
				success : function(data) {
					var data = eval('(' + data + ')');
					if (data[0] == "noneRain") {
						alert("当前未下雨，没有过载节点！");
					} else {
						var num = data.length;
						var i = 0;
						var j = 0;
						for (i = 0; i < num; i++) {
							if (myGlobalCtrl.Globe.Layers.Count > 0) {
								var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("雨水管线附属物");
								if ((layer != null)) {
									layerId = layer.ID;
									features = GetAllFeaturesByID(layer.ID);
									for (j = 0; j < features.Count; j++) {
										var feature = features.Item(j);
										var defn = feature.GetFieldValue(0);
										if (defn == data[i]) {
											defn = feature.GetFieldValue(1);
											if (defn == "4001") {
												rainWall++;
											}
											if (defn == "4002") {
												yubi++;
											}
											break;
										}
									}
								}
							}
						}
						s1.push(rainWall);
						ticks.push("检修井");
						s1.push(yubi);
						ticks.push("雨蓖");
						dialogIndex = $.layer({
									type : 1,
									offset : [ '10px','' ],
									shade : [ '', '',false ],
									area : [ '710px','auto' ],
									page : {
										dom : '#all-area-cal-dialog'
									}
								});
	
						$("#all-area-barChart").html("");
						plot2 = $.jqplot(
										'all-area-barChart',
										[ s1 ],
										{
											stackSeries : true,
											captureLeftClick : true,
											seriesDefaults : {
												renderer : $.jqplot.BarRenderer,
												pointLabels : {
													show : true
												},
												rendererOptions : {
													barWidth : 30,
													barHeight : 30
												}
											},
											axesDefaults : {
												tickRenderer : $.jqplot.CanvasAxisTickRenderer,
												tickOptions : {
													angle : -30,
													fontSize : '10pt'
												}
											},
											axes : {
												xaxis : {
													renderer : $.jqplot.CategoryAxisRenderer,
													ticks : ticks
												}
											}
										});
					}
				}
			});
		}
		// ***********************功能9********************************//
		// 管道充满度分析
		if (rowIndex == 8) {
			// 取消事件监听器
			myGlobalCtrl.detachEvent("FireFeatureMouseClick",showballoon);
			// 设置新的事件监听
			myGlobalCtrl.attachEvent("FireFeatureMouseClick",pipelineFull);
		}
		// ***********************功能10********************************//
		// 管道充满度频次统计
		if (rowIndex == 9) {
			iframeIndex = $.layer({
				type : 2,
				title : "频次统计条件",
				offset : [ '20px', '' ],
				area : [ "250px", "400px" ],
				iframe : {
					src : 'fillPipe.jsp'
				}
			});
		}
		// ***********************功能11********************************//
		// 管道充满度空间分布
		if (rowIndex == 10) {
			iframeIndex = $.layer({
				type : 2,
				title : "设定充满度分析时间间隔",
				offset : [ '20px', '' ],
				area : [ "250px", "400px" ],
				iframe : {
					src : 'liquidPass.jsp'
				}
			});
		}
		// **************************************功能12********************************//
		// 溢流点分布和统计
		if (rowIndex == 11) {
			var layerId;
			var yubi = 0;
			var rainWall = 0;
			var s1 = [];
			var ticks = [];
			var featureLists = [ [] ];
			featureLists[0] = new Array();
			featureLists[1] = new Array();
			$.ajax({
				url : "alarm/device-realtime-data!getAlarmLiquidRecordOut.do",
				type : "POST",
				success : function(data) {
					var data = eval('(' + data + ')');
					var num = data.length;
					var i = 0;
					var j = 0;
					for (i = 0; i < num; i++) {
						if (myGlobalCtrl.Globe.Layers.Count > 0) {
							var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("雨水管线附属物");
							if ((layer != null)) {
								layerId = layer.ID;
								features = GetAllFeaturesByID(layer.ID);
								for (j = 0; j < features.Count; j++) {
									var feature = features.Item(j);
									var defn = feature.GetFieldValue(0);
									// alert(defn);
									if (defn == data[i]) {
										var defnNum = feature.GetFieldValue(1);
										if (defnNum == "4001") {
											rainWall++;
											featureLists[0].push(defn);
										}
										if (defnNum == "4002") {
											yubi++;
											featureLists[1].push(defn);
										}
										break;
									}
								}
							}
						}
					}
					s1.push(rainWall);
					ticks.push("检修井");
					s1.push(yubi);
					ticks.push("雨蓖");
					dialogIndex = $.layer({
								type : 1,
								offset : [ '10px', '' ],
								shade : [ '', '', false ],
								area : [ '710px','auto' ],// auto
								page : {
									dom : '#all-area-cal-dialog'
								}
							});
	
					$("#all-area-barChart").html("");
					plot2 = $.jqplot(
									'all-area-barChart',
									[ s1 ],
									{
										stackSeries : true,
										captureLeftClick : true,
										seriesDefaults : {
											renderer : $.jqplot.BarRenderer,
											pointLabels : {
												show : true
											},
											rendererOptions : {
												barWidth : 30,
												barHeight : 30
											}
										},
										axesDefaults : {
											tickRenderer : $.jqplot.CanvasAxisTickRenderer,
											tickOptions : {
												angle : -30,
												fontSize : '10pt'
											}
										},
										axes : {
											xaxis : {
												renderer : $.jqplot.CategoryAxisRenderer,
												ticks : ticks
											}
										}
									});
					// 给柱状图绑定事件
					$('#all-area-barChart').unbind(
							'jqplotDataClick');
					$('#all-area-barChart').bind('jqplotDataClick',function(ev,seriesIndex,pointIndex,data) {
										parent.layer.close(dialogIndex);
										popLiquidDataGrid(featureLists,pointIndex);
									});
				}
			});
		}
		// ***********************************************功能13**********************************************//
		// 抢修专题图生成
		if (rowIndex == 12) {
			var layerId;
			$.ajax({url : "alarm/device-realtime-data!getAlarmLiquidRecordOut.do",
					type : "POST",
					success : function(data) {
						var data = eval('(' + data + ')');
						if (data[0] == "noneRain") {
							alert("当前未下雨，没有溢水点！");
						} else {
							for ( var i = 0; i < data.length; i++) {
								if (myGlobalCtrl.Globe.Layers.Count > 0) {
									var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("雨水管线附属物");
									if ((layer != null)) {
										layerId = layer.ID;
										features = GetAllFeaturesByID(layer.ID);
										for ( var j = 0; j < features.Count; j++) {
											var feature = features.Item(j);
											var defn = feature.GetFieldValue(0);
											if (defn == data[i]) {
												var point = myGlobalCtrl.CreatePoint3d();
												var line = myGlobalCtrl.CreateGeoPolyline3D();
												var part = myGlobalCtrl.CreatePoint3ds();
												point = feature.Geometry.Position;
												point.Z = 10;
												part.Add2(point);
												point.X += 0.000000001;
												part.Add2(point);
												line.AddPart(part);
												var polygon = line.CreateBuffer(20,true,5,true,false);
												var newFeature = myGlobalCtrl.CreateFeature();
												newFeature.Geometry = polygon;
												myGlobalCtrl.Globe.MemoryLayer.AddFeature(newFeature);
												myGlobalCtrl.refresh();
												break;
											}
										}
									}
								}
							}
							// 列表
							var gridColumn = [ [ {
								field : 'dbCord',
								title : '溢水井编号',
								width : 250

							} ] ];
							var layer = myGlobalCtrl.Globe.Layers
									.GetLayerByCaption("雨水管线附属物");
							var features = GetAllFeaturesByID(layer.ID);

							var totalNum = data.length;
							var gridData = "{total:" + totalNum + ",";

							gridData += "rows:[";
							for ( var index = 0; index < totalNum; index++) {
								gridData += ("{dbCord:'"
										+ data[index] + "'},");
							}
							gridData = gridData.substring(0,gridData.lastIndexOf(","));
							gridData += "]}";

							gridData = eval('(' + gridData	+ ')');
							// 生成Datagrid
							$('#choice-area-query-tbl')
									.datagrid(
											{
												columns : gridColumn,
												data : gridData,
												singleSelect : true,
												fit : true,
												onDblClickRow : function(
														rowIndex,
														rowData) {
													//		    						alert(rowData.dbCord);
													//		    						flyToByLayerAndFeatureId(layer, rowData.dbCord);
													for (j = 0; j < features.Count; j++) {
														var feature = features
																.Item(j);
														var defn = feature
																.GetFieldValue(0);
														if (defn == rowData.dbCord) {
															myGlobalCtrl.Globe
																	.FlyToFeature(feature);
															break;
														}
													}
												}
											});
							$("#arlam_record_layout")
									.layout("expand",
											"south");
						}
					}
				});
		}
		//***********************************************功能14**********************************************//
		//溢流可能性分析
		if (rowIndex == 13) {
			//时间变化过程 
			$.ajax({
				url : "alarm/device-realtime-data!getAlarmLiquidForeOut.do",
				type : "POST",
				success : function(data) {
					for ( var nu = 0; nu < myGlobalCtrl.Globe.Layers.Count; nu++) {
						var pLayer = myGlobalCtrl.Globe.Layers.GetLayerByID(nu);
						if ((pLayer != null)
								&& (pLayer.Caption() != "雨水管线")
								&& (pLayer.Name.substring(0, 4) != "fttp")
								&& (pLayer.Caption() != "雨水管线附属物"))
							pLayer.Visible = false;
					}
					dataList = eval('(' + data + ')');
					if (dataList[0].pipeCode == "noneRain")
						alert("当前未下雨！");
					else {
						intervalFillId = setInterval(
								changeColor, 300);
					}
				}
			});
			//弹出 列表 
			$.ajax({
				url : "alarm/device-realtime-data!getAlarmLiquidForeOutT.do",
				type : "POST",
				success : function(data) {
					var data = eval('(' + data + ')');
					if (data[0].pipeCode != "noneRain") {
						for ( var i = 0; i < data.length; i++) {
							if (myGlobalCtrl.Globe.Layers.Count > 0) {
								var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("雨水管线附属物");
								if ((layer != null)) {
									layerId = layer.ID;
									features = GetAllFeaturesByID(layer.ID);
									for ( var j = 0; j < features.Count; j++) {
										var feature = features.Item(j);
										var defn = feature.GetFieldValue(0);
										if (defn == data[i].pipeCode) {
											var point = myGlobalCtrl.CreatePoint3d();
											var line = myGlobalCtrl.CreateGeoPolyline3D();
											var part = myGlobalCtrl.CreatePoint3ds();
											point = feature.Geometry.Position;
											point.Z = 10;
											part.Add2(point);
											point.X += 0.000000001;
											part.Add2(point);
											line.AddPart(part);
											var polygon = line.CreateBuffer(20,true,5,true,false);
											var newFeature = myGlobalCtrl.CreateFeature();
											newFeature.Geometry = polygon;
											myGlobalCtrl.Globe.MemoryLayer.AddFeature(newFeature);
											myGlobalCtrl.refresh();
											break;
										}
									}
								}
							}
						}
						//列表
						var gridColumn = [ [ {
							field : 'dbCord',
							title : '溢水井编号',
							width : 250
						}, {
							field : 'upTime',
							title : '预计溢水时间',
							width : 200
						} ] ];
						var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("雨水管线附属物");
						var features = GetAllFeaturesByID(layer.ID);
	
						var totalNum = data.length;
						var gridData = "{total:"+ totalNum + ",";
	
						gridData += "rows:[";
						for ( var index = 0; index < totalNum; index++) {
							gridData += ("{dbCord:'"
									+ data[index].pipeCode
									+ "',upTime:'"
									+ data[index].t + "'},");
						}
						gridData = gridData.substring(0,gridData.lastIndexOf(","));
						gridData += "]}";
	
						gridData = eval('(' + gridData + ')');
						//生成Datagrid
						$('#choice-area-query-tbl').datagrid({
							columns : gridColumn,
							data : gridData,
							singleSelect : true,
							fit : true,
							onDblClickRow : function(rowIndex,rowData) {
								for (j = 0; j < features.Count; j++) {
									var feature = features.Item(j);
									var defn = feature.GetFieldValue(0);
									if (defn == rowData.dbCord) {
										myGlobalCtrl.Globe.FlyToFeature(feature);
										break;
									}
								}
							}
						});
						$("#arlam_record_layout").layout("expand","south");
					}
				}
			});
			}
	
		}
});

function leakEvaluate() {
	alert("leakEvaluate");

	// 取消事件监听器
	myGlobalCtrl.detachEvent("FireTrackPolygonEnd", leakEvaluate);
	myGlobalCtrl.Globe.ClearLastTrackPolygon();
	myGlobalCtrl.Globe.Action = 0;
}

function choiceRegionArea() {
	parent.layer.close(regionDlgIndex);
	myGlobalCtrl.attachEvent("FireTrackPolygonEnd", choiceRegionAreaFromGis);
	SetAction(17);
}

function choiceRegionAreaFromGis(e) {
	regionDlgIndex = $.layer({
		type : 1,
		offset : [ '150px', '' ],
		shade : [ '', '', false ],
		area : [ 'auto', 'auto' ],
		page : {
			dom : '#createRegionDlg'
		}
	});

	var longitudeLatitudeCollects = "";
	var polygon = e;
	for ( var i = 0; i < polygon.PartCount; i++) {
		var polygonParts = polygon.Item(i);
		for ( var j = 0; j < polygonParts.Count - 1; j++) {
			var point = polygonParts.Item(j);
			var x = point.X;
			var y = point.Y;
			var longitudeLatitude = (x + ":" + y);
			longitudeLatitudeCollects += (longitudeLatitude + "-");
		}
		longitudeLatitudeCollects = longitudeLatitudeCollects.substring(0,
				longitudeLatitudeCollects.lastIndexOf("-"));
	}
	$("#regionArea").val(longitudeLatitudeCollects);

	myGlobalCtrl.detachEvent("FireTrackPolygonEnd", choiceRegionAreaFromGis);
	myGlobalCtrl.Globe.ClearLastTrackPolygon();
	myGlobalCtrl.Globe.Action = 0;
}
