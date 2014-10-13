var beginTime;
var endTime;
var s1;
var ticks;
var dialogIndex; // 弹出框对应的index,用于关闭弹出框
var globeAnalysisType; // 0表示传感器 ，1表示管线，2表示配件
var globeKeyQueryType; // 0表示传感器 ，1表示管线，2表示配件
var queryLayerArray = []; // 根据关键字查询出的所有layer数组
var currentLayerIdArray = [];
var curentPolyFeaturesArray = [];
var currentPolyFeatureIndex;
var sensorQueryType; // 传感器查询方式：0表示按所属类型查询 ，1表示按所属道路查询
var pList = new Array(5, 10);
var pSize = 5; // 每页显示的数量
var _global_begin_time;
var _global_end_time;
var pipeCollection = ['不明管线', '电通管线', '电信管线', '给水管线', '供电管线', 
                      '共通管线', '监控管线', '交通信号管线', '军用管线', '联通管线', 
                      '路灯管线', '天然气管线', '网通管线', '污水管线', '移动管线', '有线电视管线', '雨水管线'];

var _gridCfgTHC = {
	columns : [ [ {
		field : 'code',
		title : '设备编码'
	}, {
		field : 'device',
		title : '设备名称'
	}, {
		field : 'message',
		title : '记录内容'
	}, {
		field : 'recordDate',
		title : '日期'
	} ] ],
	singleSelect : true,
	pageSize : 5,
	fit : true,
	pageList : "[5,10,20,30,40,50]",
	pagePosition : "top",
	pagination : true
};

$("#tab_div").tabs({
	border : false,
	fit : true,
	cache : false,
	onSelect : function(title) {
		if (title != "Home") {
			var tab = $("#tab_div").tabs("getTab", title);
			tab.panel("refresh");
		}
	}
});

$(function() {
	$(".nav li").click(function() {
		$(".nav li").removeClass("active");
		$(this).addClass("active");
	});
});

function addTab(title, url) {
	if ($("#tab_div").tabs("exists", title)) {
		$("#tab_div").tabs("select", title);
	} else {
		var content = '<iframe scrolling="auto" frameborder="0" src="' + url
				+ '" style="width: 100%; height: 100%;"></iframe>';
		$("#tab_div").tabs('add', {
			title : title,
			content : content,
			closable : true,
			fit : true
		});
	}
}

function addTabV2(title, url) {
	if ($("#tab_div").tabs("exists", title)) {
		$("#tab_div").tabs("select", title);
	} else {
		$("#tab_div").tabs('add', {
			title : title,
			href : url,
			closable : true,
			fit : true
		});
	}
}

function message_TrackPolygonAnalysisEndEvent(e) {
	// 取消事件监听器
	myGlobalCtrl.detachEvent("FireTrackPolygonEnd",
			message_TrackPolygonAnalysisEndEvent);

	// modified by Zhang Fan
	if (globeAnalysisType == 0) {
		globeControl_query.displaySelectDevices(e);
		myGlobalCtrl.Globe.ClearLastTrackPolygon();
		myGlobalCtrl.Globe.Action = 0;
		return;
	}

	var s1 = [];
	var ticks = [];

	if (myGlobalCtrl.Globe.Layers.Count > 0) {
		curentPolyFeaturesArray = [];
		currentLayerIdArray = [];
		for ( var i = 1; i < myGlobalCtrl.Globe.Layers.Count + 1; i++) {
			var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
			var features = layer.FindFeaturesInPolygon(e, false);
			if (features == null || features.Count == 0) {
				continue;
			}

			var layerName = layer.Caption();
			// 根据统计类型与不同正则表达式匹配
			if (globeAnalysisType == 0) {
				// TODO:正则
				if (layerName.indexOf("设备") != -1) {
					ticks.push(layerName);
					s1.push(features.Count);
					curentPolyFeaturesArray.push(features);
					currentLayerIdArray.push(layer.ID);
				}
			} else if (globeAnalysisType == 1) {
				// TODO:正则
				if (isPipe(layerName)) {
					ticks.push(layerName);
					s1.push(features.Count);
					curentPolyFeaturesArray.push(features);
					currentLayerIdArray.push(layer.ID);
				}
			} else if (globeAnalysisType == 2) {
				// TODO:正则
				if (layerName.indexOf("配件") != -1) {
					ticks.push(layerName);
					s1.push(features.Count);
					curentPolyFeaturesArray.push(features);
					currentLayerIdArray.push(layer.ID);
				}
			}
		}
	}

	myGlobalCtrl.Globe.ClearLastTrackPolygon();
	myGlobalCtrl.Globe.Action = 0;

	if (currentLayerIdArray.length == 0) {
		alert("找不到记录");
		return;
	}

	dialogIndex = $.layer({
		type : 1,
		offset : [ '100px', '' ],
		shade : [ '', '', false ],
		area : [ '600px', 'auto' ],
		page : {
			dom : '#barChart_hide'
		}
	});

	$("#barChart").html("");
	plot2 = $.jqplot('barChart', [ s1 ], {
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
	$('#barChart').unbind('jqplotDataClick');
	$('#barChart').bind('jqplotDataClick',
			function(ev, seriesIndex, pointIndex, data) {
				parent.layer.close(dialogIndex);
				currentPolyFeatureIndex = pointIndex;
				popQueryDataGrid(currentLayerIdArray[pointIndex],
						curentPolyFeaturesArray[pointIndex]);
			});
}

var loadingIconDialogIndex;
function popQueryDataGrid(currentLayerId, curentPolyFeatures) {
	$("#arlam_record_layout").layout("expand", "south");

	// 生成Datagrid
	$('#choice-area-query-tbl').datagrid({
		columns : [],
		data : [],
		singleSelect : true,
		pageSize : pSize,
		pageList : pList,
		pagePosition : "top",
		rownumbers : true,
		fit : true,
		total : 0,
		pagination : true,
		onClickRow : function(rowIndex, rowData) {

		}
	});

	$("#choice-area").css("display", "none");
	$("#loading-state").css("display", "block");
	setTimeout(_asyncCall(currentLayerId, curentPolyFeatures), 500);
}

function _asyncCall(currentLayerId, curentPolyFeatures) {
	return function() {
		asyncCall(currentLayerId, curentPolyFeatures);
	}
}

function asyncCall(currentLayerId, curentPolyFeatures) {
	var fieldValueArray = [];
	var fieldNameArray = [];
	var features = curentPolyFeatures;
	var totalNum = features.Count;

	var returnData = loadDataGridData(currentLayerId, curentPolyFeatures, true,
			0, pSize, fieldNameArray, fieldValueArray);
	fieldNameArray = returnData[0];
	fieldValueArray = returnData[1];

	// 组装Datagrid的Column
	var gridColumn = "[[";
	for ( var index = 0; index < fieldNameArray.length; index++) {
		gridColumn += "{field:'" + fieldNameArray[index] + "', title:'"
				+ fieldNameArray[index] + "', width:50},";
	}
	gridColumn = gridColumn.substring(0, gridColumn.lastIndexOf(","));
	gridColumn += "]]";
	gridColumn = eval('(' + gridColumn + ')');

	// 组装Datagrid的数据
	var gridData = "{total:" + totalNum + ",";
	gridData += "rows:[";
	for ( var index = 0; index < fieldValueArray.length; index++) {
		gridData += (fieldValueArray[index] + ",");
	}
	gridData = gridData.substring(0, gridData.lastIndexOf(","));
	gridData += "]}";
	gridData = eval('(' + gridData + ')');

	// 清除url
	$('#choice-area-query-tbl').datagrid({
		url : ''
	});

	// 生成Datagrid
	$('#choice-area-query-tbl').datagrid({
		columns : gridColumn,
		data : gridData,
		singleSelect : true,
		pageSize : pSize,
		pageList : pList,
		pagePosition : "top",
		rownumbers : true,
		width : 1864,
		fit : true,
		total : totalNum,
		pagination : true,
		onLoadError : function() {
			alert("error..");
		},
		onDblClickRow : function(rowIndex, rowData) {
			flyToByLayerAndFeatureId(rowData.layerId, rowData.featureId);
		},
		onLoadSuccess : function(data) {
		},
		onBeforeLoad : function(param) {
		}
	});

	$('#choice-area-query-tbl')
			.datagrid('getPager')
			.pagination(
					{
						onSelectPage : function(pageNumber, pageSize) {
							// $('#choice-area-query-tbl').datagrid('loading');
							fieldNameArray = [];
							fieldValueArray = [];
							var returnData = loadDataGridData(currentLayerId,
									curentPolyFeatures, false, pageNumber,
									pageSize, fieldNameArray, fieldValueArray);
							fieldNameArray = returnData[0];
							fieldValueArray = returnData[1];

							var gridData = "{total:" + curentPolyFeatures.Count
									+ ",";
							gridData += "rows:[";
							for ( var index = 0; index < fieldValueArray.length; index++) {
								gridData += (fieldValueArray[index] + ",");
							}
							gridData = gridData.substring(0, gridData
									.lastIndexOf(","));
							gridData += "]}";
							gridData = eval('(' + gridData + ')');

							$('#choice-area-query-tbl').datagrid('loadData',
									gridData);
						}
					});
	$("#choice-area").css("display", "block");
	$("#loading-state").css("display", "none");
}

function loadDataGridData(layerId, curentPolyFeatures, isInit, pageNumber,
		pageSize, fieldNameArray, fieldValueArray) {
	var features = curentPolyFeatures;
	var beginIndex = 0, endIndex = pageSize;
	if (!isInit) {
		beginIndex = (pageNumber - 1) * pageSize;
		endIndex = beginIndex + pageSize;
	}
	if (endIndex > features.Count) {
		endIndex = features.Count;
	}

	fieldNameArray = [];
	fieldValueArray = [];
	for ( var j = beginIndex; j < endIndex; j++) {
		var feature = features.Item(j);
		var featureID = feature.ID;

		var fieldValueData;
		if (layerId != null) {
			fieldValueData = "{layerId:" + layerId + ", featureId:" + featureID
					+ ", ";
		} else {
			fieldValueData = "{featureId:" + featureID + ", ";
		}

		for ( var k = 0; k < feature.GetFieldCount(); k++) {
			var fieldName = feature.GetFieldDefn(k).Name;
			if (j == beginIndex) {
				fieldNameArray.push(fieldName);
			}

			var fieldValue = feature.GetFieldValue(k);
			fieldValue = ("'" + fieldValue + "'");
			fieldValueData += (fieldName + ":" + fieldValue.replace(/\s+/g, '') + ",");

		}
		fieldValueData = fieldValueData.substring(0, fieldValueData
				.lastIndexOf(","));
		fieldValueData += "}";
		fieldValueArray.push(fieldValueData);
	}
	var returnArray = new Array(fieldNameArray, fieldValueArray);
	return returnArray;
}

function trackPolygonAnalysisBeginEvent(analysisType) {
	myGlobalCtrl.attachEvent("FireTrackPolygonEnd",
			message_TrackPolygonAnalysisEndEvent);
	SetAction(17);
	globeAnalysisType = analysisType;
}

// 弹出查询框
function keyQueryDialog(keyQueryType) {
	globeKeyQueryType = keyQueryType;
	var keyQueryName = "";
	if (keyQueryType == 0) {
		keyQueryName = "设备查询";
	} else if (keyQueryType == 1) {
		keyQueryName = "管线查询";
	} else if (keyQueryType == 2) {
		keyQueryName = "配件查询";
	}

	// 清除上次查询数据
	$('#queryKeyText').val('');
	$("#query-result-datagrid").html("");

	dialogIndex = $.layer({
		type : 1,
		title : keyQueryName,
		offset : [ '100px', '' ],
		shade : [ '', '', false ],
		area : [ '270px', '360px' ],
		page : {
			dom : '#key-query-dialog'
		}
	});
}

function keyQuery() {
	var fieldValueArray = [];
	// var fieldNameArray = [];
	// 查询出的所有layer数组
	// var queryLayerArray = [];
	// layer的索引
	var queryLayerIndex = 0;
	// layer类别名字
	var layerTypeName = null;
	// 查询关键字
	var queryKey;

	// TODO:正则
	if (globeKeyQueryType == 0) {
		layerTypeName = "传感器";
	} else if (globeKeyQueryType == 1) {
		layerTypeName = "管线";
	} else if (globeKeyQueryType == 2) {
		layerTypeName = "配件";
	}
	queryKey = $("#queryKeyText").val();

	for ( var i = 1; i < myGlobalCtrl.Globe.Layers.Count + 1; i++) {
		var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
		var tmpLayerName = myGlobalCtrl.Globe.Layers.GetLayerByID(i).Caption();

		// TODO:正则
		if (tmpLayerName.indexOf(layerTypeName) != -1
				&& tmpLayerName.indexOf(queryKey) != -1) {
			features = GetAllFeaturesByID(i);
			tmpLayerName = "{图层名:'"
					+ tmpLayerName
					+ "', 操作:'<a style=\"color:red; cursor:pointer\" onclick=\"showFeaturesDialog("
					+ queryLayerIndex + ")\">查看要素</a>'}";
			queryLayerIndex++;
			fieldValueArray.push(tmpLayerName);
			queryLayerArray.push(layer);
		}
	}

	if (queryLayerArray.length == 0) {
		alert("找不到记录");
		return;
	}

	// 组装Datagrid的Column
	var gridColumn = "[[{field:'图层名', title:'图层名', width:100}, {field:'操作', title:'操作', width:100}]]";

	// 组装Datagrid的数据
	if (fieldValueArray.length != 0) {
		var gridData = "{total:" + fieldValueArray.length + ",";
		gridData += "rows:[";
		for ( var index = 0; index < fieldValueArray.length; index++) {
			gridData += (fieldValueArray[index] + ",");
		}
		gridData = gridData.substring(0, gridData.lastIndexOf(","));
		gridData += "]}";

		gridColumn = eval('(' + gridColumn + ')');
		gridData = eval('(' + gridData + ')');

		// 生成Datagrid
		$('#query-result-datagrid').datagrid({
			columns : gridColumn,
			data : gridData,
			fitColumn : true,
			singleSelect : true,
			width : 260,
			height : 275
		});

		// $("#arlam_record_layout").layout("expand", "south");
		// parent.layer.close(dialogIndex);
	}
}

// 显示该layer下的所有feature
function showFeaturesDialog(queryLayerIndex) {
	$("#arlam_record_layout").layout("expand", "south");

	var fieldValueArray = [];
	var fieldNameArray = [];
	var queryLayer = queryLayerArray[queryLayerIndex];
	var allFeatures = queryLayer.GetAllFeatures();

	if (allFeatures.Count > 0) {
		for ( var i = 0; i < allFeatures.Count; i++) {
			var featureFolder = allFeatures.Item(i);

			if (featureFolder.Features) {
				if (featureFolder.Features.Count != 0) {
					features = featureFolder.Features;
					allFeatures = features;
				}
			}
		}
	}

	var returnData = loadDataGridData(queryLayer.ID, allFeatures, true, 0,
			pSize, fieldNameArray, fieldValueArray);
	fieldNameArray = returnData[0];
	fieldValueArray = returnData[1];

	// 组装Datagrid的Column
	var gridColumn = "[[";
	for ( var index = 0; index < fieldNameArray.length; index++) {
		gridColumn += "{field:'" + fieldNameArray[index] + "', title:'"
				+ fieldNameArray[index] + "', width:50},";
	}
	gridColumn = gridColumn.substring(0, gridColumn.lastIndexOf(","));
	gridColumn += "]]";

	// 组装Datagrid的数据
	var gridData = "{total:" + allFeatures.Count + ",";
	gridData += "rows:[";
	for ( var index = 0; index < fieldValueArray.length; index++) {
		gridData += (fieldValueArray[index] + ",");
	}
	gridData = gridData.substring(0, gridData.lastIndexOf(","));
	gridData += "]}";
	gridColumn = eval('(' + gridColumn + ')');
	gridData = eval('(' + gridData + ')');

	// 清除url
	$('#choice-area-query-tbl').datagrid({
		url : ''
	});

	// 生成Datagrid
	$('#choice-area-query-tbl').datagrid({
		columns : gridColumn,
		data : gridData,
		fitColumn : true,
		singleSelect : true,
		pageSize : pSize,
		pageList : pList,
		pagePosition : "top",
		rownumbers : true,
		fit : true,
		pagination : true,
		onDblClickRow : function(rowIndex, rowData) {
			flyToByLayerAndFeatureId(rowData.layerId, rowData.featureId);
		},
		onClickRow : function(rowIndex, rowData) {

		}
	});

	$('#choice-area-query-tbl')
			.datagrid('getPager')
			.pagination(
					{
						onSelectPage : function(pageNumber, pageSize) {
							fieldNameArray = [];
							fieldValueArray = [];
							var returnData = loadDataGridData(queryLayer.ID,
									allFeatures, false, pageNumber, pageSize,
									fieldNameArray, fieldValueArray);
							fieldNameArray = returnData[0];
							fieldValueArray = returnData[1];

							var gridData = "{total:" + allFeatures.Count + ",";
							gridData += "rows:[";
							for ( var index = 0; index < fieldValueArray.length; index++) {
								gridData += (fieldValueArray[index] + ",");
							}
							gridData = gridData.substring(0, gridData
									.lastIndexOf(","));
							gridData += "]}";
							gridData = eval('(' + gridData + ')');

							$('#choice-area-query-tbl').datagrid('loadData',
									gridData);

						}
					});

	// $("#arlam_record_layout").layout("collapse", "west");

	$("#choice-area").css("display", "block");
	$("#loading-state").css("display", "none");

}

function loadLayer() {
	var features;
	var allData = "[";

	if (myGlobalCtrl.Globe.Layers.Count > 0) {
		for ( var i = 1; i < myGlobalCtrl.Globe.Layers.Count + 1; i++) {
			var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
			var layerName = layer.Caption();
			features = GetAllFeaturesByID(i);

			if (features.Count == 0) { // features.Count为0，则代表该图层为影像图层。
				str = "{text: '"
						+ layerName
						+ "', checked: 'true', state: 'closed', attributes: {isLayer: true, layerId: "
						+ layer.ID + "}},";
				allData += str;
			} else {// 该图层为矢量数据图层。
				str = "{text: '"
						+ layerName
						+ "', checked: 'true', state: 'closed', attributes: {isLayer: true, layerId: "
						+ layer.ID + "}},";
				allData += str;
			}
		}
		str = str.substring(0, str.lastIndexOf(","));
		if (myGlobalCtrl.Globe.Terrains.Count > 0) {
			for ( var j = 1; j < myGlobalCtrl.Globe.Terrains.Count + 1; j++) {
				var terrain = myGlobalCtrl.Globe.Terrains.GetTerrainByID(j);
				var terrainName = terrain.Caption();
				str = '<nodes><node text="' + terrainName + '" data="t'
						+ terrain.ID + '"/></nodes>';
				alert("Terrains -> str: " + str);
			}
		}
		allData = allData.substring(0, allData.lastIndexOf(","));
		allData += "]";
		allData = eval('(' + allData + ')');

		$('#layerTree')
				.tree(
						{
							checkbox : 'true',
							data : allData,
							onCheck : function(node, checked) {
								if (typeof (node.text) != 'undefined') {
									if (checked == false) {
										layervisible(node.text, false);
									} else {
										layervisible(node.text, true);
									}
								}
							},
							onDblClick : function(node) {
								if (node.attributes.isLayer == false) {
									flyToByLayerAndFeatureId(
											node.attributes.layerId,
											node.attributes.featureId);
								}
							},
							onBeforeExpand : function(node) {
								var cNode = $("#layerTree").tree("getChildren",
										node.target);
								if (typeof (cNode) == 'undefined'
										|| cNode.length == 0) {
									loadFeatures(node, node.attributes.layerId);
								}
							}
						});
	}
}

function loadFeatures(node, layerId) {
	var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(layerId);
	var layerName = layer.Caption();
	var features = GetAllFeaturesByID(layerId);

	var str = "[";
	for ( var j = 0; j < features.Count; j++) {
		var feature = features.Item(j);
		str += "{text: '" + feature.Name + "', id: " + feature.ID
				+ ", attributes: {isLayer: false, layerId: " + layerId
				+ ", featureId: " + feature.ID + "}},";
	}
	str = str.substring(0, str.lastIndexOf(","));
	str += "]";

	str = eval('(' + str + ')');
	$('#layerTree').tree('append', {
		parent : node.target,
		checkbox : 'true',
		data : str
	});
	$('#layerTree').tree('check', node.target);
}

function allAreaCal(layerTypeId) {
	// layer类别名字
	var layerTypeName = getLayerTypeNameById(layerTypeId);

	var s1 = [];
	var ticks = [];

	if (myGlobalCtrl.Globe.Layers.Count > 0) {
		curentPolyFeaturesArray = [];
		currentLayerIdArray = [];
		for ( var i = 1; i < myGlobalCtrl.Globe.Layers.Count + 1; i++) {
			var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
			var features = GetAllFeaturesByID(i);
			if (features == null || features.Count == 0) {
				continue;
			}

			var layerName = layer.Caption();
			// 根据统计类型与不同正则表达式匹配
			// if(layerName != '设备图层') {
			// ticks.push(layerName);
			// s1.push(features.Count);
			// curentPolyFeaturesArray.push(features);
			// currentLayerIdArray.push(layer.ID);
			// }
			if (layerTypeId == 0) {
				// TODO:正则
				if (layerName.indexOf(layerTypeName) != -1) {
					ticks.push(layerName);
					s1.push(features.Count);
					curentPolyFeaturesArray.push(features);
					currentLayerIdArray.push(layer.ID);
				}
			} else if (layerTypeId == 1) {
				// TODO:正则
				if (isPipe(layerName)) {
					ticks.push(layerName);
					s1.push(features.Count);
					curentPolyFeaturesArray.push(features);
					currentLayerIdArray.push(layer.ID);
				}
			} else if (layerTypeId == 2) {
				// TODO:正则
				if (layerName.indexOf(layerTypeName) != -1) {
					ticks.push(layerName);
					s1.push(features.Count);
					curentPolyFeaturesArray.push(features);
					currentLayerIdArray.push(layer.ID);
				}
			}
		}
	}

	myGlobalCtrl.Globe.ClearLastTrackPolygon();
	myGlobalCtrl.Globe.Action = 0;

	if (currentLayerIdArray.length == 0) {
		alert("找不到记录");
		return;
	}

	dialogIndex = $.layer({
		type : 1,
		offset : [ '100px', '' ],
		shade : [ '', '', false ],
		area : [ '710px', 'auto' ],
		page : {
			dom : '#all-area-cal-dialog'
		}
	});

	$("#all-area-barChart").html("");
	plot2 = $.jqplot('all-area-barChart', [ s1 ], {
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
	$('#all-area-barChart').unbind('jqplotDataClick');
	$('#all-area-barChart').bind(
			'jqplotDataClick',
			function(ev, seriesIndex, pointIndex, data) {
				parent.layer.close(dialogIndex);
				currentPolyFeatureIndex = pointIndex;
				popQueryDataGrid(currentLayerIdArray[pointIndex],
						curentPolyFeaturesArray[pointIndex]);
			});
}

function getLayerTypeNameById(layerTypeId) {
	var layerTypeName = null;
	if (layerTypeId == 0) {
		layerTypeName = "设备";
	} else if (layerTypeId == 1) {
		layerTypeName = "管线";
	} else if (layerTypeId == 2) {
		layerTypeName = "配件";
	}
	return layerTypeName;
}

function popSensorQueryDialog(sensorQueryType) {
	var titleName = null;
	if (sensorQueryType == 0) {
		titleName = "按所属类型查询";
		$('#input-road-name-area').css("visibility", "hidden");
	} else {
		titleName = "按所属道路查询";
		$('#input-road-name-area').css("visibility", "visible");
	}

	dialogIndex = $.layer({
		type : 1,
		title : titleName,
		offset : [ '100px', '' ],
		shade : [ '', '', false ],
		area : [ 'auto', 'auto' ],
		page : {
			dom : '#sensor-query-dialog'
		}
	});

	$("#sensor-query-type").val(sensorQueryType);
}

function beginAreaQuery(areaQueryType) {
	// 查询类型
	var sensorQueryType = $("#sensor-query-type").val();

	// 按所属类型查询
	if (sensorQueryType == 0) {
		// 全区域查询
		if (areaQueryType == 0) {
			message_SensorChoiceAresQueryEndEvent(null);
			// 选择区域查询
		} else if (areaQueryType == 1) {
			registerSensorChoiceAreaQuery();
		}
		// 按所属道路查询
	} else if (sensorQueryType == 1) {
		// 全区域查询
		if (areaQueryType == 0) {
			message_SensorChoiceAresQueryEndEventByRoad(null);
			// 选择区域查询
		} else if (areaQueryType == 1) {
			registerSensorChoiceAreaQueryByRoad();
		}
	} else {
		alert('错误类型: areaQueryType=' + areaQueryType);
	}

	parent.layer.close(dialogIndex);
}

// 注册传感器"选择区域查询"
function registerSensorChoiceAreaQuery() {
	myGlobalCtrl.attachEvent("FireTrackPolygonEnd",
			message_SensorChoiceAresQueryEndEvent);
	SetAction(17);
}

function registerSensorChoiceAreaQueryByRoad() {
	myGlobalCtrl.attachEvent("FireTrackPolygonEnd",
			message_SensorChoiceAresQueryEndEventByRoad);
	SetAction(17);
}

// 传感器"选择区域查询"回调方法
function message_SensorChoiceAresQueryEndEvent(e) {
	message_SensorChoiceAresQuery(e, false, null);
}

function message_SensorChoiceAresQueryEndEventByRoad(e) {
	var roadName = $('#roadName').val();
	message_SensorChoiceAresQuery(e, true, roadName);
}

function message_SensorChoiceAresQuery(e, isByRoad, roadName) {
	// 查询出的传感器
	var findSensorLayer = [];
	var findDeviceFeatureArray = [];
	// var layerId = null;
	for ( var i = 1; i < myGlobalCtrl.Globe.Layers.Count + 1; i++) {
		var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
		var features = null;
		if (e == null) {
			features = GetAllFeaturesByID(i);
		} else {
			features = layer.FindFeaturesInPolygon(e, true);
		}
		if (features == null || features.Count == 0) {
			continue;
		}

		var layerName = layer.Caption();
		if (layerName.indexOf("设备") != -1) {
			// 按所属类别查询
			if (!isByRoad) {
				findSensorLayer.push(layer);
				// 按所属道路查询
			} else {
				for ( var j = 0; j < features.Count; j++) {
					var feature = features.Item(j);
					// var featureName = feature.Name;

					for ( var k = 0; k < feature.GetFieldCount(); k++) {
						var fieldName = feature.GetFieldDefn(k).Name;
						if (fieldName.indexOf("道路") == -1) {
							continue;
						}
						var fieldValue = feature.GetFieldValue(k);
						if (fieldValue == roadName) {
							findSensorLayer.push(layer);
							findDeviceFeatureArray.push();
						}
						break;
					}
				}
			}
		}
	}

	// 组装Datagrid的Column
	if (findSensorLayer.length == 0) {
		alert("没有找到!");
		if (e != null) {
			myGlobalCtrl.detachEvent("FireTrackPolygonEnd",
					message_SensorChoiceAresQueryEndEvent);
			myGlobalCtrl.detachEvent("FireTrackPolygonEnd",
					message_SensorChoiceAresQueryEndEventByRoad);
			myGlobalCtrl.Globe.ClearLastTrackPolygon();
			myGlobalCtrl.Globe.Action = 0;
		}
		return;
	}
	var gridColumn = "[[{field:'图层名', title:'图层名'}]]";
	var gridData = "{total:" + findSensorLayer.length + ",";
	gridData += "rows:[";
	for ( var index = 0; index < findSensorLayer.length; index++) {
		gridData += ("{图层名:'" + findSensorLayer[index].Caption()
				+ "', attributes:{layerId:" + findSensorLayer[index].ID + "}},");
	}
	gridData = gridData.substring(0, gridData.lastIndexOf(","));
	gridData += "]}";
	// alert("gridData: " + gridData);
	gridColumn = eval('(' + gridColumn + ')');
	gridData = eval('(' + gridData + ')');

	// 扩展south区域
	$("#arlam_record_layout").layout("expand", "south");

	// 清除url
	$('#choice-area-query-tbl').datagrid({
		url : ''
	});

	$('#choice-area-query-tbl')
			.datagrid(
					{
						columns : gridColumn,
						data : gridData,
						fitColumn : true,
						singleSelect : true,
						pageSize : pSize,
						pageList : pList,
						pagePosition : "top",
						rownumbers : true,
						fit : true,
						pagination : true,
						onClickRow : function(rowIndex, rowData) {
							// alert("rowData.attributes.layerId: " +
							// rowData.attributes.layerId);
							if (typeof (rowData.attributes) != 'undefined') {
								var layerId = rowData.attributes.layerId;
								var allFeatures = GetAllFeaturesByID(layerId);
								var fieldNameArray = [];
								var fieldValueArray = [];

								var returnData = loadDataGridData(layerId,
										allFeatures, true, 0, pSize,
										fieldNameArray, fieldValueArray);
								fieldNameArray = returnData[0];
								fieldValueArray = returnData[1];

								// 组装Datagrid的Column
								var gridColumn = "[[";
								for ( var index = 0; index < fieldNameArray.length; index++) {
									gridColumn += "{field:'"
											+ fieldNameArray[index]
											+ "', title:'"
											+ fieldNameArray[index]
											+ "', width:50},";
								}
								gridColumn = gridColumn.substring(0, gridColumn
										.lastIndexOf(","));
								gridColumn += "]]";

								// 组装Datagrid的数据
								var gridData = "{total:" + allFeatures.Count
										+ ",";
								gridData += "rows:[";
								for ( var index = 0; index < fieldValueArray.length; index++) {
									gridData += (fieldValueArray[index] + ",");
								}
								gridData = gridData.substring(0, gridData
										.lastIndexOf(","));
								gridData += "]}";
								gridColumn = eval('(' + gridColumn + ')');
								gridData = eval('(' + gridData + ')');

								// 清除url
								$('#choice-area-query-tbl').datagrid({
									url : ''
								});

								$('#choice-area-query-tbl').datagrid(
										{
											columns : gridColumn,
											data : gridData,
											fitColumn : true,
											singleSelect : true,
											pageSize : pSize,
											pageList : pList,
											pagePosition : "top",
											rownumbers : true,
											fit : true,
											pagination : true,
											onDblClickRow : function(rowIndex,
													rowData) {
												flyToByLayerAndFeatureId(
														rowData.layerId,
														rowData.featureId);
											}
										});

								$('#choice-area-query-tbl')
										.datagrid('getPager')
										.pagination(
												{
													onSelectPage : function(
															pageNumber,
															pageSize) {
														fieldNameArray = [];
														fieldValueArray = [];
														var returnData = loadDataGridData(
																layerId,
																allFeatures,
																false,
																pageNumber,
																pageSize,
																fieldNameArray,
																fieldValueArray);
														fieldNameArray = returnData[0];
														fieldValueArray = returnData[1];

														var gridData = "{total:"
																+ allFeatures.Count
																+ ",";
														gridData += "rows:[";
														for ( var index = 0; index < fieldValueArray.length; index++) {
															gridData += (fieldValueArray[index] + ",");
														}
														gridData = gridData
																.substring(
																		0,
																		gridData
																				.lastIndexOf(","));
														gridData += "]}";
														gridData = eval('('
																+ gridData
																+ ')');

														$(
																'#choice-area-query-tbl')
																.datagrid(
																		'loadData',
																		gridData);
													}
												});
							}
						}
					});

	if (e != null) {
		myGlobalCtrl.detachEvent("FireTrackPolygonEnd",
				message_SensorChoiceAresQueryEndEvent);
		myGlobalCtrl.detachEvent("FireTrackPolygonEnd",
				message_SensorChoiceAresQueryEndEventByRoad);
		myGlobalCtrl.Globe.ClearLastTrackPolygon();
		myGlobalCtrl.Globe.Action = 0;
	}
}

function printit(printDiv) {
	parent.layer.close(dialogIndex);
	$('#fortest').html($('#barChartDiv').html());
	$('#sys-content').css("display", "none");
	$('#fortest').css("display", "block");
	window.print();
	$('#sys-content').css("display", "block");
	$('#fortest').css("display", "none");
	return false;
}

// 获取所有带有未处理报警记录的设备类型
function get_all_region_alarm_record_active() {
	$.post("alarm/gis-alarm-record!queryAlarmRecord4AllActive.do",{
		startTime : "",
		endTime : ""
	},function(result) {
		s1 = eval('(' + result.yy + ')');
		ticks = eval('(' + result.xx + ')');

		$("#area_alarm_record_barChart").html("");
		dialogIndex = $.layer({
			type : 1,
			offset : [ '20px', '' ],
			area : [ '650px', '450px' ],
			page : {
				dom : "#area_alarm_record_div"
			}
		});
		$.jqplot("area_alarm_record_barChart", [ s1 ], {
			seriesDefaults : {
				renderer : $.jqplot.BarRenderer,
				pointLabels : {
					show : true
				},
				rendererOptions : {
					barWidth : 30,
					barHeight : 10
				}
			},
			axes : {
				xaxis : {
					renderer : $.jqplot.CategoryAxisRenderer,
					ticks : ticks
				}
			}
		});

		$("#area_alarm_record_barChart").unbind('jqplotDataClick');
		$("#area_alarm_record_barChart").bind('jqplotDataClick',function(ev, seriesIndex, pointIndex,data) {
			parent.layer.close(dialogIndex);
			$("#arlam_record_layout").layout("expand", "south");
			$("#choice-area-query-tbl").datagrid(_gridCfgTHC).datagrid({
				url : "alarm/alarm-record!queryActiveAlarmRecordByType.do"
			}).datagrid("load",{
				typeName : ticks[pointIndex]
			});
			$("#choice-area-query-tbl").datagrid({
				"onClickRow" : function(rowIndex,rowData) {
					jumpToByFeatureName(rowData.code);
				}
			});
	
		});
	}, "json");
}

// 获取选定区域内带有未处理报警记录的设备类型
function get_part_region_alarm_record_active() {

	SetAction(17);
	myGlobalCtrl.attachEvent("FireTrackPolygonEnd",
			_get_part_region_alarm_record_active);
}

function _get_part_region_alarm_record_active(e) {
	var type = "";
	var code = "";
	$("#choice-area-query-tbl").datagrid(_gridCfgTHC);
	var layers = myGlobalCtrl.Globe.Layers;// 得到所有图层

	for ( var i = 1; i < layers.Count + 1; i++) {
		var layer = myGlobalCtrl.Globe.Layers.Item(i);
		if (null != layer && layer.Caption() == "设备图层") {
			var features = layer.FindFeaturesInPolygon(e, true);
			if (null != features) {
				for ( var j = features.Count; j >= 0; j--) {
					var feature = features.Item(j);
					if (null != feature) {
						var t = feature
								.GetFieldValue(feature.GetFieldCount() - 1);
						var c = feature.Name;

						if (type.indexOf(t, 0) < 0) {
							type += t + "&";
						}
						if (c && code.indexOf(c, 0) < 0) {
							code += c + "&";
						}
					}
				}
			}
		}
	}

	// 如果没有选中任何设备则什么也不操作
	if (code.length <= 1) {
		myGlobalCtrl.detachEvent("FireTrackPolygonEnd",
				_get_part_region_alarm_record_active);
		myGlobalCtrl.Globe.ClearLastTrackPolygon();
		SetAction(0);
		$("#txt_time_begin").val("");
		$("#txt_time_end").val("");
		alert("您没有选中设备！");
		return;
	}

	$.post("alarm/gis-alarm-record!queryAlarmRecord4PartActive.do",{
		devCodes : code,
		devType : type
	},
	function(result) {
		s1 = eval('(' + result.yy + ')');
		ticks = eval('(' + result.xx + ')');
		$("#area_alarm_record_barChart").html("");
		dialogIndex = $.layer({
			type : 1,
			offset : [ '20px', '' ],
			area : [ '650px', '450px' ],
			page : {
				dom : "#area_alarm_record_div"
			}
		});
		$.jqplot("area_alarm_record_barChart", [ s1 ], {
			seriesDefaults : {
				renderer : $.jqplot.BarRenderer,
				pointLabels : {
					show : true
				},
				rendererOptions : {
					barWidth : 30,
					barHeight : 10
				}
			},
			axes : {
				xaxis : {
					renderer : $.jqplot.CategoryAxisRenderer,
					ticks : ticks
				}
			}
		});

		$("#area_alarm_record_barChart").unbind("jqplotDataClick");
		$("#area_alarm_record_barChart").bind('jqplotDataClick',
				function(ev, seriesIndex, pointIndex,data) {
							parent.layer.close(dialogIndex);
							$("#arlam_record_layout").layout(
									"expand", "south");
							$("#choice-area-query-tbl").datagrid({
												url : "alarm/alarm-record!queryActiveAlarmRecordByCode4Type.do"
							}).datagrid("load",{
								devCodes : code,
								typeName : ticks[pointIndex]
							});
							$("#choice-area-query-tbl").datagrid({
								"onClickRow" : function(rowIndex,rowData) {
									jumpToByFeatureName(rowData.code);
								}
							});
				});
	}, "json");

	myGlobalCtrl.detachEvent("FireTrackPolygonEnd",
			_get_part_region_alarm_record_active);
	myGlobalCtrl.Globe.ClearLastTrackPolygon();

	SetAction(0);
}

function query_all_area_alarm_record_unactive(beginTime,endTime) {
	_query_all_area_alarm_record_unactive(beginTime,endTime);
}

function _query_all_area_alarm_record_unactive(beginTime,endTime) {
	$.post("alarm/gis-alarm-record!queryAlarmRecord4AllUnActive.do",{
		startTime : beginTime,
		endTime : endTime
	},function(result) {
		s1 = eval('(' + result.yy + ')');
		ticks = eval('(' + result.xx + ')');
		
		dialogIndex = $.layer({
			type : 1,
			offset : [ '20px', '' ],
			area : [ '650px', '450px' ],
			page : {
				dom : "#area_alarm_record_div"
			}
		});

		$("#area_alarm_record_barChart").html("");
		$.jqplot("area_alarm_record_barChart", [ s1 ], {
			seriesDefaults : {
				renderer : $.jqplot.BarRenderer,
				pointLabels : {	show : true	},
				rendererOptions : {
					barWidth : 30,
					barHeight : 10
				}
			},
			axes : {
				xaxis : {
					renderer : $.jqplot.CategoryAxisRenderer,
					ticks : ticks
				}
			}
		});

		$("#area_alarm_record_barChart").unbind('jqplotDataClick');
		$("#area_alarm_record_barChart").bind('jqplotDataClick',function(ev, seriesIndex, pointIndex,data) {
			parent.layer.close(dialogIndex);
			$("#arlam_record_layout").layout("expand", "south");
			$("#choice-area-query-tbl").datagrid(_gridCfgTHC).datagrid({
				url : "alarm/alarm-record!queryUnActiveAlarmRecordByTypeAndDt.do"
			}).datagrid("load",{
				typeName : ticks[pointIndex],
				beginTime : beginTime,
				endTime : endTime
			});
			$("#choice-area-query-tbl").datagrid({
				"onClickRow" : function(rowIndex, rowData) {
					jumpToByFeatureName(rowData.code);
				}
			});
		});
	}, "json");
}

function query_part_area_alarm_record_unactive(beginTime, endTime) {

	_golbal_begin_time = beginTime;
	_global_end_time = endTime;

	SetAction(17);
	myGlobalCtrl.attachEvent("FireTrackPolygonEnd",
			_query_part_area_alarm_record_unactive);
}

function _query_part_area_alarm_record_unactive(e) {

	var type = "";
	var code = "";
	$("#choice-area-query-tbl").datagrid(_gridCfgTHC);
	var layers = myGlobalCtrl.Globe.Layers;// 得到所有图层

	for ( var i = 1; i < layers.Count + 1; i++) {
		var layer = myGlobalCtrl.Globe.Layers.Item(i);
		if (null != layer && layer.Caption() == "设备图层") {
			var features = layer.FindFeaturesInPolygon(e, false);
			if (null != features) {
				for ( var j = features.Count; j >= 0; j--) {
					var feature = features.Item(j);
					if (null != feature) {
						var t = feature
								.GetFieldValue(feature.GetFieldCount() - 1);
						var c = feature.Name;
						if (t && type.indexOf(t, 0) < 0) {
							type += t + "&";
						}
						if (c && code.indexOf(c, 0) < 0) {
							code += c + "&";
						}
					}
				}
			}
		}
	}

	// 如果没有选中任何设备则什么也不操作
	if (code.length <= 1) {
		myGlobalCtrl.detachEvent("FireTrackPolygonEnd",
				_get_part_region_alarm_record_active);
		myGlobalCtrl.Globe.ClearLastTrackPolygon();
		SetAction(0);
		$("#txt_time_begin").val("");
		$("#txt_time_end").val("");
		alert("您没有选中设备！");
		return;
	}

	$
			.post(
					"alarm/gis-alarm-record!queryAlarmRecord4PartUnActive.do",
					{
						devCodes : code,
						devType : type,
						startTime : _global_begin_time,
						endTime : _global_end_time
					},
					function(result) {
						s1 = eval('(' + result.yy + ')');
						ticks = eval('(' + result.xx + ')');
						dialogIndex = $.layer({
							type : 1,
							offset : [ '20px', '' ],
							area : [ '650px', '450px' ],
							page : {
								dom : "#area_alarm_record_div"
							}
						});

						$("#area_alarm_record_barChart").html("");
						$.jqplot("area_alarm_record_barChart", [ s1 ], {
							seriesDefaults : {
								renderer : $.jqplot.BarRenderer,
								pointLabels : {
									show : true
								},
								rendererOptions : {
									barWidth : 30,
									barHeight : 10
								}
							},
							axes : {
								xaxis : {
									renderer : $.jqplot.CategoryAxisRenderer,
									ticks : ticks
								}
							}
						});

						$("#area_alarm_record_barChart").unbind(
								"jqplotDataClick");
						$("#area_alarm_record_barChart")
								.bind(
										'jqplotDataClick',
										function(ev, seriesIndex, pointIndex,
												data) {
											// alert(dialogIndex);
											parent.layer.close(dialogIndex);
											$("#arlam_record_layout").layout(
													"expand", "south");
											$("#choice-area-query-tbl")
													.datagrid(
															{
																url : "alarm/alarm-record!queryUnActiveAlarmRecordByTypeAndDt.do"
															})
													.datagrid(
															"load",
															{
																devCodes : code,
																typeName : ticks[pointIndex],
																beginTime : _global_begin_time,
																endTime : _global_end_time
															});

											$("#choice-area-query-tbl")
													.datagrid(
															{
																"onClickRow" : function(
																		rowIndex,
																		rowData) {
																	jumpToByFeatureName(rowData.code);
																}
															});

										});
					}, "json");

	myGlobalCtrl.detachEvent("FireTrackPolygonEnd",
			_query_part_area_alarm_record_unactive);
	myGlobalCtrl.Globe.ClearLastTrackPolygon();
	SetAction(0);
}

function look_all_earth() {
	var camerastate = myGlobalCtrl.Globe.CameraState;
	camerastate.Longitude = 120.65;
	camerastate.Latitude = 31.19;
	camerastate.Distance = 0;
	myGlobalCtrl.Globe.FlyToCameraState(camerastate);
}

function reloadPage(locationUrl) {
	window.location.href = locationUrl;
}

function addDev(result) {
	var position = myGlobalCtrl.CreatePoint3d();
	position.SetValue(result.dev.longtitude, result.dev.latitude,result.dev.height);
	globeFeatureManager.addFeature("设备图层",result.dev.devCode,"DEVICETYPE",result.dev.typeName,result.dev.location,position,0);
}

function delDev(result) {
	for ( var i = 1; i < myGlobalCtrl.Globe.Layers.Count + 1; i++) {
		var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
		if (layer.Caption() == "设备图层") {
			globeFeatureManager.deleteFeature(result.dev.devCode,layer);
		}
	}
}

function editDev(result) {
	if( null==result || null==result.dev || null==result.dev.devCode || null==result.dev.location){
		return;
	}
	
	for ( var i = 1; i < myGlobalCtrl.Globe.Layers.Count + 1; i++) {
		var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
		if (layer.Caption() == "设备图层") {
			globeFeatureManager.updateFilepathAndFieldvalue(result.dev.devCode,layer,result.dev.location);
		}
	}
}

function editDevType(srcPath, destPath, devNam) {
	if (srcPath == destPath) {
		return;
	}
	for ( var i = 1; i < myGlobalCtrl.Globe.Layers.Count + 1; i++) {
		var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
		if (layer.Caption() == "设备图层") {
			var features = layer.GetAllFeatures();
			for ( var j = features.Count; j >= 0; j--) {
				var feature = features.Item(j);
				if (feature != null && layer != null
						&& feature.Geometry != null
						&& feature.Geometry.Type == 305) {
					var model = feature.Geometry;
					if (model.FilePath == (myGlobalCtrl.Globe.DefaultRelativeDir + srcPath)) {
						// myGlobalCtrl.Globe.DefaultRelativeDir =
						// "http://192.168.0.113/locaspace/gcm/";
						model.FilePath = destPath;
						model.AltitudeMode = 0;
						feature.Geometry = model;
						feature.SetFieldValue(feature.GetFieldCount() - 1,
								devNam);
						layer.Save();
					}
				}
			}
		}
	}
	$("#regionTbl").treegrid("reload");
}

var alarmDialogIndex;
var alarmCodeMap = new HashMap();
function scheduleQueryAlarmInfo() {
	$
			.ajax({
				type : 'POST',
				url : 'alarm/query-alarm-info!queryAlarmRecord.do',
				success : function(msg) {
					try {
						if (msg == '') {
							return;
						}
						var msg = eval('(' + msg + ')');
						if (alarmCodeMap.containsKey(msg.newAlarmMsg.code)) {
							return;
						} else {
							alarmCodeMap.put(msg.newAlarmMsg.code,
									msg.newAlarmMsg.code);
						}

						if (alarmDialogIndex != null) {
							parent.layer.close(alarmDialogIndex);
						}

						// 弹出报警弹出框
						var alarmDlgWidthoffset = ($(document).width() - 506)
								+ 'px';
						var alarmDlgHeightoffset = ($(document).height() - 201)
								+ 'px';

						alarmDialogIndex = $.layer({
							type : 1,
							title : '您有一条新的报警信息',
							offset : [ alarmDlgHeightoffset,
									alarmDlgWidthoffset ],
							shade : [ '', '', false ],
							area : [ 'auto', 'auto' ],
							page : {
								dom : '#alarmPopDlg'
							}
						});

						$("#current-alarm-device-code").val(
								msg.newAlarmMsg.code);
						var gridData = "{total:4, rows:[";
						gridData += "{propName:'设备编号', propValue:'"
								+ msg.newAlarmMsg.code + "'},";
						gridData += "{propName:'设备名', propValue:'"
								+ msg.newAlarmMsg.device + "'},";
						gridData += "{propName:'报警日期', propValue:'"
								+ msg.newAlarmMsg.recordDate + "'},";
						gridData += "{propName:'报警信息', propValue:'"
								+ msg.newAlarmMsg.message + "'}]}";
						gridData = eval('(' + gridData + ')');
						$("#alarm-datagrid-tbl").datagrid({
							height : 131,
							data : gridData
						});

						// 在地图上喷泉
						if (msg.newAlarmMsg.device != '光纤') {
							for ( var i = 1; i < myGlobalCtrl.Globe.Layers.Count + 1; i++) {
								var layer = myGlobalCtrl.Globe.Layers
										.GetLayerByID(i);
								var layerName = layer.Caption();
								if (layerName == '设备图层') {
									var features = layer.GetFeatureByName(
											msg.newAlarmMsg.code, true);
									for ( var j = features.Count; j >= 0; j--) {
										var feature = features.Item(j);
										if (null != feature) {
											var deviceType = feature
													.GetFieldValue(feature
															.GetFieldCount() - 1);
											if (deviceType == '多功能漏损监测仪'
													|| deviceType == '渗漏预警仪'
													|| deviceType == '液位监测仪'
													|| deviceType == '爆管预警仪') {
												penquan(feature);
											} else if (deviceType == '燃气智能监测终端') {
												huomiao(feature);
											}
											break;
										}
									}
								}
							}
						} else {
							var itemValueArray = msg.newAlarmMsg.itemValue
									.split(",");
							flyToFiberGPSLocationFromDistance(
									msg.newAlarmMsg.code, itemValueArray[0]);
						}
					} catch (e) {
					}
				}
			});
}
setInterval("scheduleQueryAlarmInfo()", 5000);

function jumpToAlarm() {
	$("#tab_div").tabs("select", 'Home');
	flyToDeviceLayerByFeatureId($("#current-alarm-device-code").val());
}

var changeIndex = 1;
setInterval(
		function() {
			if (changeIndex++ % 2 == 0) {
				$("#alert-icon")
						.html(
								"<img style=\"width: 131px; height:131px\" src=\"images/alert-red.png\"/>");
			} else {
				$("#alert-icon")
						.html(
								"<img style=\"width: 131px; height:131px\" src=\"images/info-green.png\"/>");
			}
			if (changeIndex == 10) {
				changeIndex = 1;
			}
		}, 500);

$('#layer-slider').slider({
	mode : 'h',
	min : 0,
	max : 100,
	value : 100,
	onChange : function(newValue, oldValue) {
		setlayerOpaque(newValue);
	}
});

function deviceQueryDialog() {
	// 清除上次查询数据
	$('#deviceQueryKeyText').val('');

	// TAG
	// dialogIndex = $.layer({
	// type : 1,
	// title : '设备查询',
	// offset : [ '100px', '' ],
	// shade : [ '', '', true ],
	// area : [ '279px', '200px' ],
	// page : {
	// dom : '#device-query-dialog'
	// }
	// });
	deviceQueryTextInit();
	dialogIndex = $.layer({
		type : 2,
		title : '设备查询',
		offset : [ '20px', '' ],
		area : [ "332px", "200px" ],
		iframe : {
			src : 'common/layout/device-query-dialog.jsp'
		}
	});

}

function deviceQueryTextInit() {
	var tmpChkData = "";
	var gridData = "[";
	var idIndex = 1;
	var layers = myGlobalCtrl.Globe.Layers;// 得到所有图层
	for ( var i = 0; i < layers.count; i++) {
		var layerItem = myGlobalCtrl.Globe.Layers.Item(i);
		if (layerItem.Caption.indexOf("设备") != -1) {
			var featuresOfLayerItem = layerItem.GetAllFeatures();
			for ( var j = 0; j < featuresOfLayerItem.Count; j++) {
				var featureOfLayerItem = layerItem.GetFeatureByIndex(j);
				var deviceType = featureOfLayerItem
						.GetFieldValue(featureOfLayerItem.GetFieldCount() - 1);
				if (tmpChkData.indexOf(deviceType) == -1) {
					gridData += "{'id':" + idIndex + ", 'text':'" + deviceType
							+ "'}, ";
					idIndex++;
					tmpChkData += (deviceType + "-");
				}
			}
			break;
		}
	}
	gridData = gridData.substring(0, gridData.lastIndexOf(","));
	gridData += "]";
	$("#deviceQuery").val(gridData);
}

function deviceQuery(deviceQueryKeyText) {
	var deviceFeatures = [];
	var layerId = null;
	var layers = myGlobalCtrl.Globe.Layers;// 得到所有图层
	for ( var i = 0; i < layers.count; i++) {
		var layerItem = myGlobalCtrl.Globe.Layers.Item(i);
		if (layerItem.Caption.indexOf("设备") != -1) {
			layerId = layerItem.ID;
			var featuresOfLayerItem = layerItem.GetAllFeatures();
			for ( var j = 0; j < featuresOfLayerItem.Count; j++) {
				var featureOfLayerItem = layerItem.GetFeatureByIndex(j);
				var deviceType = featureOfLayerItem
						.GetFieldValue(featureOfLayerItem.GetFieldCount() - 1);
				if (typeof (deviceType) != 'undefined'
						&& deviceType.indexOf(deviceQueryKeyText) != -1) {
					deviceFeatures.push(featureOfLayerItem);
				}
			}
			break;
		}
	}

	if (deviceFeatures.length == 0) {
		alert("没有找到!");
		return;
	}

	$("#arlam_record_layout").layout("expand", "south");
	var fieldNameArray = [];
	var fieldValueArray = [];
	for ( var j = 0; j < deviceFeatures.length; j++) {
		var feature = deviceFeatures[j];
		var featureID = deviceFeatures[j].ID;

		var fieldValueData;
		if (layerId != null) {
			fieldValueData = "{layerId:" + layerId + ", featureId:" + featureID
					+ ", ";
		} else {
			fieldValueData = "{featureId:" + featureID + ", ";
		}

		for ( var k = 0; k < feature.GetFieldCount(); k++) {
			var fieldName = feature.GetFieldDefn(k).Name;
			if (j == 0) {
				fieldNameArray.push(fieldName);
			}

			var fieldValue = feature.GetFieldValue(k);
			fieldValue = ("'" + fieldValue + "'");
			fieldValueData += (fieldName + ":" + fieldValue.replace(/\s+/g, '') + ",");

		}
		fieldValueData = fieldValueData.substring(0, fieldValueData
				.lastIndexOf(","));
		fieldValueData += "}";
		fieldValueArray.push(fieldValueData);
	}

	// 组装Datagrid的Column
	var gridColumn = "[[";
	for ( var index = 0; index < fieldNameArray.length; index++) {
		var fieldName = fieldNameArray[index];
		if (fieldNameArray[index] == 'deviceType') {
			fieldName = '设备类型';
		}
		gridColumn += "{field:'" + fieldNameArray[index] + "', title:'"
				+ fieldName + "', width:100},";
	}
	gridColumn = gridColumn.substring(0, gridColumn.lastIndexOf(","));
	gridColumn += "]]";

	// 组装Datagrid的数据
	var gridData = "{total:" + fieldValueArray.length + ",";
	gridData += "rows:[";
	for ( var index = 0; index < fieldValueArray.length; index++) {
		gridData += (fieldValueArray[index] + ",");
	}
	gridData = gridData.substring(0, gridData.lastIndexOf(","));
	gridData += "]}";
	gridColumn = eval('(' + gridColumn + ')');
	gridData = eval('(' + gridData + ')');

	// 清除url
	$('#choice-area-query-tbl').datagrid({
		url : ''
	});

	// 生成Datagrid
	$('#choice-area-query-tbl').datagrid({
		columns : gridColumn,
		data : gridData,
		fitColumn : true,
		singleSelect : true,
		rownumbers : true,
		fit : true,
		onDblClickRow : function(rowIndex, rowData) {
			flyToByLayerAndFeatureId(rowData.layerId, rowData.featureId);
		},
		onClickRow : function(rowIndex, rowData) {

		}
	});
}

var globeControl_query = {
	countFeatureByFieldDef : function(layerName, fieldDef, e) {
		var map = new HashMap();
		var layers = myGlobalCtrl.Globe.Layers;// 得到所有图层
		for ( var i = 1; i < layers.Count + 1; i++) {
			var layer = myGlobalCtrl.Globe.Layers.Item(i);
			if (null != layer && layer.Caption() == layerName) {
				var features;
				if (e == undefined) {
					features = layer.GetAllFeatures();
				} else {
					features = layer.FindFeaturesInPolygon(e, true);
				}

				if (null != features) {
					for ( var j = features.Count; j >= 0; j--) {
						var feature = features.Item(j);
						if (null != feature) {
							// 遍历feature对应属性
							for ( var k = 0; k < feature.GetFieldCount(); k++) {

								var fieldName = feature.GetFieldDefn(k).Name;
								if (fieldName != fieldDef) {
									continue;
								}
								// 将coutName push到相应键值对里面去
								var countName = feature.GetFieldValue(k);
								if (countName != "") {
									if (map.containsKey(countName)) {
										var oldValue = map.get(countName);
										map.put(countName, oldValue + 1);
									} else {
										map.put(countName, 1);
									}
									break;
								}
							}
						}
					}
				}
			}
		}
		return map;
	},
	displayAllDevices : function() {
		var map = globeControl_query.countFeatureByFieldDef("设备图层",
				"deviceType");
		showBar("#all-area-cal-dialog", "#all-area-barChart", map);
	},
	displaySelectDevices : function(e) {
		var map = globeControl_query.countFeatureByFieldDef("设备图层",
				"deviceType", e);
		showBar("#all-area-cal-dialog", "#all-area-barChart", map);
	}
};

function showBar(divdlg, divBar, map) {

	var s1 = map.values();
	var ticks = map.keys();
	myGlobalCtrl.Globe.ClearLastTrackPolygon();
	myGlobalCtrl.Globe.Action = 0;

	if (map.size() == 0) {
		alert("找不到相应记录");
		return;
	}

	dialogIndex = $.layer({
		type : 1,
		offset : [ '100px', '' ],
		shade : [ '', '', false ],
		area : [ '710px', 'auto' ],
		page : {
			dom : divdlg
		}
	});

	$(divBar).html("");
	plot2 = $.jqplot(divBar.substring(1), [ s1 ], {
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
};

function HashMap() {
	/** Map 大小 * */
	var size = 0;
	/** 对象 * */
	var entry = new Object();

	/** 存 * */
	this.put = function(key, value) {
		if (!this.containsKey(key)) {
			size++;
		}
		entry[key] = value;
	}

	/** 取 * */
	this.get = function(key) {
		if (this.containsKey(key)) {
			return entry[key];
		} else {
			return null;
		}
	}

	/** 删除 * */
	this.remove = function(key) {
		if (delete entry[key]) {
			size--;
		}
	}

	/** 是否包含 Key * */
	this.containsKey = function(key) {
		return (key in entry);
	}

	/** 是否包含 Value * */
	this.containsValue = function(value) {
		for ( var prop in entry) {
			if (entry[prop] == value) {
				return true;
			}
		}
		return false;
	}

	/** 所有 Value * */
	this.values = function() {
		var values = new Array();
		for ( var prop in entry) {
			values.push(entry[prop]);
		}
		return values;
	}

	/** 所有 Key * */
	this.keys = function() {
		var keys = new Array();
		for ( var prop in entry) {
			keys.push(prop);
		}
		return keys;
	}

	/** Map Size * */
	this.size = function() {
		return size;
	}
}

// ************************************************雨污水仿真
// wei************************//
// 液位器查询 wei
function liquidkeyQuery() {
	queryKey = $("#liquidqueryKeyText").val();
	parent.layer.close(LiquidDlgIndex);
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

	$("#choice-area-query-tbl")
			.datagrid(
					{
						columns : gridColumn,
						url : "alarm/device-realtime-data!searchLiquidProps.do?model.devCode="
								+ queryKey,
						onLoadSuccess : function(data) {
							if (data.total > 0) {
								$("#arlam_record_layout").layout("expand",
										"south");
							} else {
								alert("未查到相关设备信息！");
							}
						},
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
							var leakRegionDlgIndex = $.layer({
								type : 1,
								offset : [ leakRegionDlgHeightoffset,
										leakRegionDlgWidthoffset ],
								shade : [ '', '', false ],
								area : [ 'auto', 'auto' ],
								page : {
									dom : '#leakLiquidDlg'
								}
							});

							var propColumn = "[[{field : 'devId', title:'设备编号'},"
									+ "{field : 'liquidData', title:'液位值'},"
									+ " {field : 'liquidPower', title:'设备电压'},"
									+ " {field : 'time', title:'上传时间'}]]";
							propColumn = eval('(' + propColumn + ')');
							//显示传感设备数据信息
							$("#leakLiquidInfo")
									.datagrid(
											{
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
	//					$("#arlam_record_layout").layout("expand", "south");   	    
}
function clear_data_grid() {
	$("#choice-area-query-tbl").datagrid(_gridCfgTHC).datagrid({
		url : ""
	}).datagrid("loadData", {
		total : 0,
		rows : []
	});
}

var deviceControl = {
		viewDevice:function(layername, deviceType) {
			var gridColumnData = "[[";
			var gridData = "";
			for(i=1;i<myGlobalCtrl.Globe.Layers.Count;i++){
	            var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
				if(layer.Caption().indexOf(layername) !=-1 ) {
					var features = layer.GetAllFeatures();
					gridData += ("{total:" + features.Count + ", rows:[");
					for ( var j = 0; j < features.Count; j++) {
						var feature = layer.GetFeatureByIndex(j);
						var curDeviceType = feature.GetFieldValue(feature.GetFieldCount() - 1);
						if(curDeviceType.indexOf(deviceType) == -1) {
							continue;
						}
						
						gridData += ("{layerId:" + layer.ID +", featureId:" + feature.ID + ", ");
						for(var k = 0; k < feature.GetFieldCount(); k ++) {
							fieldName = feature.GetFieldDefn(k).name;
							if(j == 0) {
								gridColumnData += ("{field:'" + fieldName + "', title:'" + (fieldName != "deviceType"?fieldName:"设备类型") + "', width:70}, ");
							}
							var fieldValue = feature.GetFieldValue(k);
							gridData += (fieldName + ":'" + fieldValue + "',");
						}
						gridData = gridData.substring(0, gridData.lastIndexOf(","));
						gridData += "},"
					}
					gridColumnData = gridColumnData.substring(0, gridColumnData.lastIndexOf(","));
					gridColumnData += "]]";
					gridData = gridData.substring(0, gridData.lastIndexOf(","));
					gridData += "]}";
					break;
				}
			}
			
			gridColumnData = eval('(' + gridColumnData + ')');
			gridData = eval('(' + gridData + ')');
			// 清除url
			$('#choice-area-query-tbl').datagrid({
				url : '',
				pagination : false
			});
			
			// 生成Datagrid
			$('#choice-area-query-tbl').datagrid({
				columns : gridColumnData,
				data : gridData,
				fitColumn : true,
				singleSelect : true,
				rownumbers : true,
				fit : true,
				onDblClickRow : function(rowIndex, rowData) {
					flyToByLayerAndFeatureId(rowData.layerId, rowData.featureId);
				}
			});
			
			$("#arlam_record_layout").layout("expand", "south");
		}
};

function isPipe(testLayer) {
	for(var i = 0;i < pipeCollection.length; i ++) {
		if(pipeCollection[i] == testLayer) {
			return true;
		}
	}
	return false;
}
