var lon;
var lat;
var alt;
var point3D;
var feature;
var dataPath;
var placemarkPath;
var layer;
var treeView;
var treeView1;
var divRightHeight;
var op;
var server = "192.168.10.123";
var deviceLayer = null;
var fiberLayer = null;
var myGlobalCtrl = null;
var _markLayer = null;
var _index = null;
var _tempStyle = null;
var pLayer = null;
var feature = null;
var features = null;
var featureTooltip = null;
var allfeatures = null;
var featurename = null;
var feature1 = null;
var i;
var terrain;
var ds;

var xtGasDisplay;
var szWSGasDisplay;
var fiberDisplay;
var adLiquidLevelDisplay;
var adSLDeviceDisplay;
var adDSDeviceDisplay;
var groundLayerName="180fd";

document.onreadystatechange = function() { // 当页面加载状态改变的时候执行这个方法
	if (document.readyState == "complete") { // 当页面加载完成之后执行
		InitGlobal('globalDiv');
		LoadData();

		$("#headArea").css("display", "block");
		$("#leftArea").css("display", "block");
		$("#homeMenu").css("display", "block");
		$("#indexDialogArea").css("display", "block");
		$("#deviceDisplayDlg").css("display", "block");
		xtGasDisplay = new XtGasDisplay();
		szWSGasDisplay = new SzWSGasDisplay();
		fiberDisplay = new FiberDisplay();
		adLiquidLevelDisplay = new AdLiquidLevelDisplay();
		adSLDeviceDisplay = new AdSLDeviceDisplay();
		adDSDeviceDisplay = new AdDSDeviceDisplay();
		xtGasDisplay.setSuccessor(szWSGasDisplay);
		szWSGasDisplay.setSuccessor(fiberDisplay);
		fiberDisplay.setSuccessor(adLiquidLevelDisplay);
		adLiquidLevelDisplay.setSuccessor(adSLDeviceDisplay);
		adSLDeviceDisplay.setSuccessor(adDSDeviceDisplay);

		setlayerOpaque(100);
	}

}

function LoadData() {
	ConnectServer(server, "1500");
	connectSqlserver();
	SetLatLonVisible(false);// 设置三维控件经纬度不可见

	var camerastate = myGlobalCtrl.Globe.CameraState;
	camerastate.Longitude = 120.610963; // 120.610963, 31.187621
	camerastate.Latitude = 31.187621;
	camerastate.Distance = 100;
	// myGlobalCtrl.Globe.FlyToCameraState(camerastate);
	myGlobalCtrl.Globe.JumpToCameraState(camerastate);

}

function connectSqlserver() {
	myGlobalCtrl.Globe.GroundOpaque = 0.5;
	myGlobalCtrl.Globe.DefaultRelativeDir = "http://192.168.10.123/locaspace/gcm/";// DefaultRelativeDir

	//ds = myGlobalCtrl.Globe.DataManager.OpenSqlServerDataSource(server, "",
	//		"casic_pipe_test", "sa", "acmenu1990");

	//ds = myGlobalCtrl.Globe.DataManager.OpenSqlServerDataSource(server, "",	"sz_taihu_base", "sa", "predator");
	
	//ds=myGlobalCtrl.Globe.DataManager.OpenOracleDataSource("192.168.10.123/orcl","","","scott","predator")
	ds=myGlobalCtrl.Globe.DataManager.OpenOracleDataSource("192.168.10.123/pipeline","","","scott","predator");
	myGlobalCtrl.refresh();
	if(null!=ds){
		for ( var i = 0; i < ds.Count; i++) {
			var layer = myGlobalCtrl.Globe.Layers.Add2(ds.Item(i));
		}
	}
	var camerastate = myGlobalCtrl.Globe.CameraState;
	camerastate.Longitude = 120.610963;
	camerastate.Latitude = 31.187621;
	camerastate.Distance = 100;
	myGlobalCtrl.Globe.JumpToCameraState(camerastate);

}

function setlayerOpaque(layerOpaque) {
	myGlobalCtrl.Globe.GroundOpaque=layerOpaque/100;
	var layer=myGlobalCtrl.Globe.Layers.getLayerByCaption(groundLayerName);
	if(layer!=null){
		layer.Opaque =layerOpaque;
	}
}

// 加载WEB三维控件
function DetectActiveX() {
	try {
		var comActiveX = new ActiveXObject(
				"LOCASPACEPLUGIN.LocaSpacePluginCtrl.1");
	} catch (e) {
		return false;
	}
	return true;
}

// 初始化地球
function InitGlobal(globalDiv) {
	if (DetectActiveX() == false) {

		var earthDiv = document.getElementById(globalDiv);

		earthDiv.innerHTML = "<IMG src=\"images/DownSetUp.JPG\"><a  href=\"http://www.higis.cn/wp-content/plugins/globe/GeoSceneGlobal/site/GSGlobal.zip\"><font size=1>您还没安装GeoSceneGlobal，点击这里安装!</A>"
		earthDiv.innerHTML = "<IMG src=\"images/DownSetUp.JPG\"><a  href=\"http://192.168.0.111/digitalcity/3d/LocaSpacePluginSetup_03-26.rar\"><font size=1>您还没安装GeoSceneGlobal，点击这里安装!</A>"

	} else {
		try {
			var earthDiv = document.getElementById(globalDiv);
			earthDiv.innerHTML = "<OBJECT ID=\"MyGlobal\" CLASSID=\"CLSID:0E7A33FF-6238-41A6-A38D-AC3F755F92B6\" WIDTH=\"100%\" HEIGHT=\"100%\"><param name=\"wmode\" value=\"transparent\"></OBJECT>";
			myGlobalCtrl = document.getElementById("MyGlobal");

			myGlobalCtrl.Globe.LatLonGridVisible = false; // 设置三维控件的经纬度为不显示
			featureTooltip = myGlobalCtrl.CreateBalloonEx();
			featureTooltip.Create(myGlobalCtrl.Handle);// 创建气泡
			myGlobalCtrl.Globe.FeatureMouseOverEnable = true;// 允许悬浮事件

			myGlobalCtrl.attachEvent("FireFeatureMouseClick", showballoon);//
			// FireFeatureMouseClick
			myGlobalCtrl.attachEvent("FireCameraBeginMove", hideballoon);// FireCameraBeginMove

		} catch (e) {
			alert(e.description);
		}
	}
}

function AfterTrackPolygonAnalysis(polygon) {
	var geoPit = myGlobalCtrl.CreateGeoPit();// new
	geoPit.PitPolygon = polygon;
	var depth = document.getElementById("trackDepth").value;
	geoPit.PitDepth = depth;
	geoPit.PitDepthUsing = true;
	myGlobalCtrl.Globe.AddPit("", geoPit);
	myGlobalCtrl.Globe.ClearLastTrackPolygon();
	myGlobalCtrl.Globe.Action = 0;
}

function SetAction(nValue)// 对于在场景中的动作进行设置
{
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.Action = nValue;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}
}

function ClearMeasure()// 清楚测量结果
{
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.ClearMeasure();
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}

}
// 场景平滑
function SetAntialiasing(bValue) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.Antialiasing = bValue;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}
}
// 光影大气
function SetShaderAtmosphereUsing(bValue) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.Atmosphere.ShaderUsing = bValue;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}

}
// 设置模型悬浮高亮
function SetFeatureMouseOverHighLight(bValue) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.FeatureMouseOverHighLight = bValue;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}

}
// 设置是否允许浮动事件
function SetFeatureMouseOverEnable(bValue) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.FeatureMouseOverEnable = bValue;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}
}
// 大气层
function SetAtmosphereVisible(bVisible) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.Atmosphere.visible = bVisible;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}
}
// 控制面板
function SetControlPanelVisible(bVisible) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.ControlPanelVisible = bVisible;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}
}
// 状态栏
function SetStatusBarVisible(bVisible) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.StatusBarVisible = bVisible;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}
}
// 经纬度
function SetLatLonVisible(bVisible) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.LatLonGridVisible = bVisible;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}
}
// 天空
function SetSkyVisible(bVisible) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.SkyVisible = bVisible;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}
}
// 设置相机模式:地下、地上、浏览模式
function SetCameraMode(nValue) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.CameraMode = nValue;
			myGlobalCtrl.refresh();
		}
	} catch (e) {
		alert(e.description);
	}
}
function ConnectServer(strIP, nPort) {
	// alert(layerFile);
	// alert(SE);
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.ConnectServer(strIP, nPort, "", "");
		}
	} catch (e) {
		alert(e.description);
	}
}

function SetEditSnapObject() {

}
// 加载图层git、kml
function AddLayer(layerFile) {// 添加数据方法，layerFile为数据路径
	try {
		if (myGlobalCtrl != null) {
			var start = layerFile.indexOf(".");
			if (layerFile.substring(start + 1, layerFile.length) != "gtt") {// 加载的数据是影像数据和矢量数据
				pLayer = myGlobalCtrl.Globe.Layers.Add1(layerFile);
				// 加载影像数据和矢量数据时使用myGlobalCtrl.Globe.Layers.Add1()，注意与加载地形数据时的区别。
				if (layerFile.substring(start + 1, layerFile.length) == "kml") {
					pLayer.Editable = true;// 设置图层的可编辑性，如要对图层中的模型进行编辑则需要将该属性设置为
				}
			} else {
				myGlobalCtrl.Globe.Terrains.Add1(layerFile);
				// 加载地形数据时使用myGlobalCtrl.Globe.Terrains.Add1();
			}
		}
	} catch (e) {
		alert(e.description);
	}
}
// 加载地形gtt
function AddTerrain(terrainFile) {
	try {
		if (myGlobalCtrl != null) {
			myGlobalCtrl.Globe.Terrains.Add1(terrainFile);
		}
	} catch (e) {
		alert(e.description);
	}
}

function GetAllFeaturesByID(id) {// 通过图层id获取该图层的所有要素
	if (myGlobalCtrl.Globe.Layers.Count > 0) {// 判断场景中是否有加载数据
		pLayer = myGlobalCtrl.Globe.Layers.GetLayerByID(id); // 根据图层id获取该图层。

		allfeatures = pLayer.GetAllFeatures();// 获取该图层的所有要素。

		if (allfeatures.Count > 0) {
			for (i = 0; i < allfeatures.Count; i++) { // 遍历图层中的所有要素
				featureFolder = allfeatures.Item(i);

				if (featureFolder.Features) {
					if (featureFolder.Features.Count != 0) {
						features = featureFolder.Features;
						return features;// 返回所有要素
					}
				} else
					return allfeatures;
			}
		} else {
			return allfeatures;
		}
	}
}
function GetFeatureID(layerid, featureid) {// 双击树节点触发事件，layerid，featureid皆为节点中储存的信息，layerid为图层id

	if (layerid == "m1") {// 判断是否是MemoryLayer
		feature = myGlobalCtrl.Globe.MemoryLayer.GetFeatureByID(featureid);
		myGlobalCtrl.Globe.FlyToFeature(feature);
	} else {
		if (layerid == 0) {// layerid如果是0的话则说明该图层是影像数据或地形数据
			layerid = featureid;// 如果是地形数据或影像数据的话则featureid就是该图层的id
			if (featureid.indexOf("t") > -1) {// 判断是否是地形数据
				pLayer = myGlobalCtrl.Globe.Terrains.GetTerrainByID(layerid
						.substring(1, layerid.length));
			} else {// 影像数据
				pLayer = myGlobalCtrl.Globe.Layers.GetLayerByID(layerid);
			}
			var rcBounds = pLayer.Bounds;// 获取图层的边界
			var pntCenter = rcBounds.Center;// 获取图层的中心点
			var pntPosition = myGlobalCtrl.CreatePoint3d();// new
			pntPosition.X = pntCenter.X;// 将图层中心点的坐标赋值给一个点
			pntPosition.Y = pntCenter.Y;
			pntPosition.Z = 1000;
			myGlobalCtrl.Globe.FlyToPosition(pntPosition, 1);// 飞行到图层的中心点
		} else {// 矢量数据
			pLayer = myGlobalCtrl.Globe.Layers.GetLayerByID(layerid);// 通过图层id获取图层
			feature = pLayer.GetFeatureByID(featureid);// 通过要素的id获取要素
			myGlobalCtrl.Globe.FlyToFeature(feature);// 飞行到要素
			if (pLayer.Name.indexOf("飞行路线") > -1) {
				var dFlyAboveLine = document.getElementById("textFlyAboveLine").value;
				// 获取设置沿线飞行的高度文本框中的内容
				var dFlyAloneLineSpeed = document.getElementById("textFlyAloneLineSpeed").value;
				// 获取设置沿线飞行的飞行速度文本框中的内容
				var dFlyAloneLineRotateSpeed = document.getElementById("textFlyAloneLineRotateSpeed").value;
				// 获取设置沿线飞行的拐弯速度文本框中的内容
				if (dFlyAboveLine != "" || dFlyAloneLineSpeed != ""
						|| dFlyAloneLineRotateSpeed != "") {
					myGlobalCtrl.Globe.FlyAlongLineSpeed = dFlyAloneLineSpeed;// 设置飞行速度
					myGlobalCtrl.Globe.FlyAlongLineRotateSpeed = dFlyAloneLineRotateSpeed;// 设置飞行拐弯速度
					myGlobalCtrl.Globe.FlyEyeAlongWithLine1(feature.Geometry,
							dFlyAboveLine, 85, true, 0, false);// 设置飞行路径（feature.Geometry），飞行高度（dFlyAboveLine）
				} else {// 如果没有设置飞行高度，速度和拐弯速度，则默认飞行速度为10m/s,拐弯速度10度/s
					myGlobalCtrl.Globe.FlyAlongLineSpeed = 10;
					myGlobalCtrl.Globe.FlyAlongLineRotateSpeed = 10;
					myGlobalCtrl.Globe.FlyEyeAlongWithLine(feature.Geometry);
				}

				myGlobalCtrl.Globe.CurFlyID = 4;
			}
		}
	}
}

var intervalId = null;
var viewSensorDataDlgIndex = null;
function showDeviceInfo(feature) {
	// 打开显示设备数据弹出框
	viewSensorDataDlgIndex = $.layer({
		type : 1,
		offset : [ '110px', '' ],
		shade : [ '', '', false ],
		area : [ 'auto', 'auto' ],
		page : {
			dom : '#viewDeviceDataDlg'
		},
		close : function(index) {
			if (intervalId != null) {
				clearInterval(intervalId);
				intervalId = null;
				parent.layer.close(viewSensorDataDlgIndex);
			}
		}
	});

	// 生成datagrid
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
	
	$("#deviceRealTimeTbl").datagrid({
		columns : gridColumn,
		url : encodeURI("alarm/device-realtime-data!getDeviceProps.do?model.devCode=" + feature.Name + "&time=" + new Date()), 
		onClickRow : function(row) {
//			$.layer({
//				type : 2,
//				title : "配置管理",
//				offset : [ '20px', '' ],
//				area : [ "450px", "350px" ],
//				iframe : {
//					src : 'config.jsp'
//				}
//			});
			var paramConfigDlgIndex = $.layer({
    			type : 1,
    			title:'配置设置信息 ',
    			offset : [ '250px', '' ],
    			shade : [ '', '', false ],
    			area : [ '460', '280' ],
    			page : {
    				dom : '#paramConfigDlg'
    			}
    		});
    		
    		$("#do_submitConfig_btn").unbind();
    		$("#do_submitConfig_btn").bind("click", function() { 
    			$("#config_add").submit(); 
    			
    		});
		}
	});
 
	$("#config_add").form({
		url : encodeURI("alarm/device-config!sendADData.do?model.devCode=" + feature.Name),
		onSubmit : function() {
			var isValid = $(this).form('validate');
			if (!isValid) {
				$.messager.progress('close'); // 隐藏进度条虽然形式是无效的
			}
			return isValid; // 返回false,将阻止表单提交
		},
		success : function(result) {
			var result = eval("(" + result + ")"); 
			if (result.success) {
				//$("#dg").datagrid("reload"); // reload the user data
				//$("#paramConfigDlg").dialog("close"); 
				alert("配置成功");
			}  
			$("#paramConfigDlg").dialog("close"); 
		}
	});

	// 配置信息框 by nn
	var gridConfigColumn = [ [ {
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
	} ] ];

	$("#deviceConfigTbl").datagrid({
		columns : gridConfigColumn,
		url : encodeURI("alarm/device-realtime-data!getDeviceData.do?model.dataType=param&model.devCode="  + feature.Name)
	});

	// 生成justgage, 显示实时数据
	$("#flowJustGage").html("");
	$("#pressJustGage").html("");
	$("#noiseJustGage").html("");
	var flowDeviceGage = new JustGage({
		id : "flowJustGage",
		value : 0,
		min : 0,
		max : 1000,
		title : "流量传感器",
		label : "",
		startAnimationTime : 2000,
		startAnimationType : ">",
		refreshAnimationTime : 1000,
		refreshAnimationType : "bounce"
	});
	var pressDeviceGage = new JustGage({
		id : "pressJustGage",
		value : 0,
		min : 0,
		max : 1000,
		title : "压力传感器",
		label : "",
		startAnimationTime : 2000,
		startAnimationType : ">",
		refreshAnimationTime : 1000,
		refreshAnimationType : "bounce"
	});
	var noiseDeviceGage = new JustGage({
		id : "noiseJustGage",
		value : 0,
		min : 0,
		max : 1000,
		title : "噪音传感器",
		label : "",
		startAnimationTime : 2000,
		startAnimationType : ">",
		refreshAnimationTime : 1000,
		refreshAnimationType : "bounce"
	});

	var second = 5;
	intervalId = setInterval(function() {
		if (second != 0) {
			$("#countdown").html(second + "秒");
			second--;
			return;
		} else {
			$("#countdown").html(second + "秒");
			second = 5;
			$.ajax({
				url : "alarm/device-realtime-data!getDeviceData.do",
				type : "POST",
				data : {
					'model.devCode' : feature.Name,
					'model.dataType' : 'realtime'
				},
				success : function(data) {
					var data = eval('(' + data + ')');
					var dataContent = data.data;
					var insData = parseFloat(dataContent.insData);
					var pressData = parseFloat(dataContent.pressData);
					var denseData = parseFloat(dataContent.denseData);
					if (typeof (insData) != 'undefined'
							&& insData != null) {
						flowDeviceGage.refresh(insData);
					}
					if (typeof (pressData) != 'undefined'
							&& pressData != null) {
						pressDeviceGage.refresh(pressData);
					}
					if (typeof (denseData) != 'undefined'
							&& denseData != null) {
						noiseDeviceGage.refresh(denseData);
					}
				}
			});
		}

	}, 1000);

	// 获取历史数据，生成曲线图
	$.ajax({
		url : "alarm/device-realtime-data!getDeviceData.do",
		type : "POST",
		data : {
			'model.devCode' : feature.Name,
			'model.dataType' : 'history'
		},
		success : function(data) {
			var data = eval('(' + data + ')');
			var labelArray = [];
			var dataArray = [];
			var flowData = [];
			var pressData = [];
			var noiseData = [];
			var dataContentArray = data.data;
			for ( var i = 0; i < dataContentArray.length; i++) {
				var sensorType = dataContentArray[i].sensorType;
				var innerDataArray = dataContentArray[i].data;
				for ( var j = 0; j < innerDataArray.length; j++) {
					var recordTime = innerDataArray[j].recordTime;
					var dataVal = innerDataArray[j].dataValue;
					if (sensorType == 'flow') {
						flowData.push([ recordTime,  parseFloat(dataVal)]);
					} else if (sensorType == 'press') {
						pressData.push([ recordTime, parseFloat(dataVal)]);
					} else if (sensorType == 'noise') {
						noiseData.push([ recordTime, parseFloat(dataVal)]);
					}
				}
			}
			if (flowData.length != 0) {
				dataArray.push(flowData);
				labelArray.push('流量传感器');
			}
			if (pressData.length != 0) {
				dataArray.push(pressData);
				labelArray.push('压力传感器');
			}
			if (noiseData.length != 0) {
				dataArray.push(noiseData);
				labelArray.push('噪音传感器');
			}

			$("#historyDataJqplot").html("");
			var jqplot = $.jqplot('historyDataJqplot', dataArray, {
				title : '传感器历史数据',
				series : [ {
					lineWidth : 2,
					markerOptions : {
						style : 'dimaon'
					}
				} ],
				axesDefaults : {
					tickRenderer : $.jqplot.CanvasAxisTickRenderer,
					tickOptions : {
						angle : -30,
						fontSize : '10pt'
					}
				},
				axes : {
					xaxis : {
						renderer : $.jqplot.CategoryAxisRenderer
					}
				},
				legend : {
					show : true,
					location : 'e',
					labels : labelArray
				}
			});
		}
	});
}

function showballoon(feature, evt) {
	var isDevice = chkIsDevice(feature);
	var deviceType = feature.GetFieldValue(feature.GetFieldCount() - 1);
	var isFiber = chkIsFiber(feature);
	if (isFiber) {
		deviceType = "光纤";
	}
	if (!isDevice && !isFiber) {
		if (feature.GetFieldCount() == 0) {
			return;
		}

		// 方法中的两个参数会在myGlobalCtrl的FireFeatureMouseClick事件触发时获取。
		var str = "";
		str += "<table border='1' cellspacing='0' cellpadding='0' style='margin-left:0px; margin-top:-10px;'>";
		for ( var i = 0; i < feature.GetFieldCount(); i++) {
			var defn = feature.GetFieldDefn(i);
			var fieldName = defn.Name;
			var fieldValue = feature.GetFieldValue(i);
			if (fieldValue == null || typeof ('fieldValue') == 'undefined' || fieldValue == '') {
				fieldValue = "无";
			}
			if (i % 2 == 0) {
				str += "<tr style='background-color:#C3F3C3;'><td>" + fieldName
						+ "</td><td> " + fieldValue + "</td></tr>";
			} else {
				str += "<tr style='background-color:#EAF6F9'><td>" + fieldName
						+ "</td><td> " + fieldValue + "</td></tr>";
			}
		}
		str += "</table>";
		featureTooltip.ShowBalloon1(evt.x, evt.y, str);// 显示气泡
	} else if (isDevice && (deviceType == '多功能漏损监测仪' || deviceType == '渗漏预警仪')) { // 设备图层，且为"多功能漏损检测仪"或"渗漏预警仪"
		showDeviceInfo(feature);
	} else {
		xtGasDisplay.handleRequest(deviceType, feature);
	}
}

function chkIsDevice(feature) {
	var layerCaption = feature.Layer.Caption;
	if (layerCaption == '设备图层') {
		return true;
	} else {
		return false;
	}
}

function chkIsFiber(feature) {
	var layerCaption = feature.Layer.Caption;
	if (layerCaption == '光纤') {
		return true;
	} else {
		return false;
	}
}

function hideballoon(feature, evt) {// 方法中的两个参数会在myGlobalCtrl的FireFeatureMouseClick事件触发时获取。
	if (featureTooltip.Visible) {
		featureTooltip.HideBalloon();
	}
	if (intervalId != null) {
		clearInterval(intervalId);
		intervalId = null;
		parent.layer.close(viewSensorDataDlgIndex);
	}
}
function setLayerVisible(id, visible) {// 方法中的参数id为要隐藏的图层的id，visible为图层的可见性true或false。
	if (id == "0") {// 区分影像数据、矢量数据及地形数据。
		for (i = 1; i < myGlobalCtrl.Globe.Layers.Count; i++) {
			myGlobalCtrl.Globe.Layers.GetLayerByID(i).Visible = visible;
		}
		myGlobalCtrl.refresh();
	} else {
		if (id.search("t") < 0) {// 图层为影像数据或矢量数据，在添加图层节点时地形数据的id前加“t”，以便区分各图层。
			pLayer = myGlobalCtrl.Globe.Layers.GetLayerByID(id);
			pLayer.Visible = visible;
			myGlobalCtrl.refresh();
		} else {// 图层为地形数据
			terrain = myGlobalCtrl.Globe.Terrains.GetTerrainByID(id.substring(1, id.length));
			terrain.Visible = visible;
			myGlobalCtrl.refresh();
		}
	}
}

function ClearPits() {
	myGlobalCtrl.Globe.RemoveAllPits();
}

function flytofeature() {
	var feature = myGlobalCtrl.Globe.SelectedObject;
	if (feature != null) {
		myGlobalCtrl.Globe.FlyToPointSpeed = myGlobalCtrl.Globe.FlyToPointSpeed * 3;
		myGlobalCtrl.Globe.FlyToFeature1(feature, 90, 45, 5);// 朝南
	}
}
function flytoposition() {
	var point = myGlobalCtrl.CreatePoint3d();
	point.X = 120.610963; // 经度
	point.Y = 31.187621; // 纬度
	point.Z = 100;// 高程
	myGlobalCtrl.Globe.FlyToPosition(point, 0); // 0：贴地 1：相对地表 2：海拔高度
}

function flyeyealongwithline() {
	var layer = myGlobalCtrl.Globe.Layers.GetLayerByCaption("")
	var feature = layer.GetFeatureByID();
	if (feature != null && feature.Geometry.Type == 302) {
		myGlobalCtrl.Globe.FlyEyeAlongWithLine1(feature.Geometry, 500, tilt, True, heading, True);
	}
}

// 根据设备id，飞到设备在地图中的所在位置
function flyToByFeatureName(nam) {
	var layers = myGlobalCtrl.Globe.Layers;// 得到所有图层
	for ( var i = 0; i < layers.count; i++) {
		var layerItem = myGlobalCtrl.Globe.Layers.Item(i);
		if (layerItem != null) {
			var featuresOfLayerItem = layerItem.GetAllFeatures();
			for ( var j = 0; j < featuresOfLayerItem.Count; j++) {
				var featureOfLayerItem = layerItem.GetFeatureByIndex(j);
				if (featureOfLayerItem.Name == nam) {
					featureOfLayerItem.HighLight = true
					var model = featureOfLayerItem.Geometry;
					var position = model.Position;
					var camerastate = myGlobalCtrl.Globe.CameraState;
					camerastate.Longitude = position.X;
					camerastate.Latitude = position.Y;
					camerastate.Distance = position.Z;
					myGlobalCtrl.Globe.JumpToCameraState(camerastate);
				}
			}
		}
	}
}

// 根据设备id，跳到设备在地图中的所在位置
function jumpToByFeatureName(nam) {
	var layers = myGlobalCtrl.Globe.Layers;// 得到所有图层
	for ( var i = 0; i < layers.count; i++) {
		var layerItem = myGlobalCtrl.Globe.Layers.Item(i);
		if (layerItem != null) {
			var featuresOfLayerItem = layerItem.GetAllFeatures();
			for ( var j = 0; j < featuresOfLayerItem.Count; j++) {
				var featureOfLayerItem = layerItem.GetFeatureByIndex(j);
				if (featureOfLayerItem.Name == nam) {

					featureOfLayerItem.HighLight = true

					var model = featureOfLayerItem.Geometry;
					var position = model.Position;

					var camerastate = myGlobalCtrl.Globe.CameraState;
					camerastate.Longitude = position.X;
					camerastate.Latitude = position.Y;
					camerastate.Distance = position.Z;

					myGlobalCtrl.Globe.JumpToCameraState(camerastate);

				}
			}
		}
	}
}

function flyToByLayerAndFeatureId(layerId, featureId) {
	var pLayer = myGlobalCtrl.Globe.Layers.GetLayerByID(layerId);// 通过图层id获取图层
	var feature = pLayer.GetFeatureByID(featureId);// 通过要素的id获取要素
	feature.HighLight = true
	try {
		var model = feature.Geometry;
		var position = model.Position;
		var camerastate = myGlobalCtrl.Globe.CameraState;
		camerastate.Longitude = position.X;
		camerastate.Latitude = position.Y;
		camerastate.Distance = position.Z;
		myGlobalCtrl.Globe.JumpToCameraState(camerastate);
	} catch (e) {
		myGlobalCtrl.Globe.FlyToFeature(feature);
	}
}

function flyToDeviceLayerByFeatureId(featureId) {
	if (deviceLayer == null) {
		if (ds != null) {
			for ( var i = 0; i < ds.Count; i++) {
				if (ds.Item(i).Caption == "设备图层") {
					deviceLayer = myGlobalCtrl.Globe.Layers
							.Item(ds.Item(i).Caption);
					break;
				}
			}
		}
	}
	var features = deviceLayer.GetFeatureByName(featureId, true);// 通过要素的id获取要素
	for ( var i = features.Count - 1; i >= 0; i--) {
		var feature = features.Item(i);
		feature.HighLight = true
		var model = feature.Geometry;
		var position = model.Position;
		var camerastate = myGlobalCtrl.Globe.CameraState;
		camerastate.Longitude = position.X;
		camerastate.Latitude = position.Y;
		camerastate.Distance = position.Z;
		myGlobalCtrl.Globe.JumpToCameraState(camerastate);
		break;
	}
}

function featureVisible(deviceCode, visible) {
	if (deviceLayer == null) {
		if (ds != null) {
			for ( var i = 0; i < ds.Count; i++) {
				if (ds.Item(i).Caption == "设备图层") {
					deviceLayer = myGlobalCtrl.Globe.Layers	.Item(ds.Item(i).Caption);
					break;
				}
			}
		}
	}
	var features = deviceLayer.GetFeatureByName(deviceCode, true);
	;
	for ( var i = features.Count - 1; i >= 0; i--) {
		feature = features.Item(i);
		feature.Visible = visible;
		myGlobalCtrl.refresh();
		break;
	}
}

function penquan(emitterFeature) {
	if (emitterFeature != null && emitterFeature.Geometry != null) {
		var geoParticle = myGlobalCtrl.CreateGeoParticle();
		var point3d = myGlobalCtrl.CreatePoint3D();
		point3d.SetValue(emitterFeature.Geometry.Position.X,
				emitterFeature.Geometry.Position.Y,
				emitterFeature.Geometry.Position.Z);
		geoParticle.Position = point3d;// (point3d.X, point3d.Y,point3d.Z)
		// ;//添加到这个经纬度位置
		geoParticle.AltitudeMode = 2;

		var emitter = myGlobalCtrl.CreatePointParticleEmitter();
		emitter.TexturePath = "ParticleImage/test.png";

		emitter.SetSizeFix1(0.5, 2);
		emitter.VelFix = 10;
		emitter.VelRnd = 2;

		emitter.GravityAcc = 9.8;
		emitter.AngleXYFix = 0;
		emitter.AngleXYRnd = 180;

		emitter.AngleXZFix = 88;
		emitter.AngleXZRnd = 2;

		// emitter.InnerRadius = 0;
		// emitter.OuterRadius = 0.03f;

		emitter.LifeFix = 5.0;
		emitter.LifeRnd = 1.0;

		emitter.RotFix = 0;
		emitter.RotRnd = 0;

		emitter.RotVelFix = 0;
		emitter.RotVelRnd = 0;

		emitter.EmitPerSec = 1000;

		var colorRndStart = myGlobalCtrl.CreateColorRGBA();
		colorRndStart.SetValue(255, 255, 255, 33);

		var colorRndEnd = myGlobalCtrl.CreateColorRGBA();
		colorRndEnd.SetValue(255, 255, 255, 11);
		emitter.ColorRndStart = colorRndStart;
		emitter.ColorRndEnd = colorRndEnd;
		emitter.IsLumAdded = false;

		// 将三个发射器添加到粒子对象中
		geoParticle.AddEmitter(emitter);

		geoParticle.Play();

		var feature = myGlobalCtrl.CreateFeature();
		feature.Geometry = geoParticle;
		myGlobalCtrl.Globe.MemoryLayer.AddFeature(feature);
	}
}

var viewSensorDataDlgIndex = null;
function show_history_gas_strength() {
	myGlobalCtrl.attachEvent('FireFeatureMouseClick',_show_history_gas_strength);
}
function _show_history_gas_strength(feature, evt) {
	if (feature.Layer.Caption != "设备图层") {
		return;
	}
	if (feature.GetFieldValue(feature.GetFieldCount() - 1) != "光纤") {
		return;
	}
	viewSensorDataDlgIndex = $.layer({
		type : 1,
		offset : [ '100px', '' ],
		shade : [ '', '', false ],
		area : [ '200px', '200px' ],
		page : {
			dom : "#dlg_time_query_panel_dlg"
		}
	});
	$("#do_query_btn_dlg").unbind();
	$("#do_query_btn_dlg").bind("click",function() {
						parent.layer.close(viewSensorDataDlgIndex);
						viewSensorDataDlgIndex = $.layer({
							type : 1,
							offset : [ '110px', '' ],
							shade : [ '', '', false ],
							area : [ 'auto', 'auto' ],
							page : {
								dom : '#viewGasStrengthHistoryDlg'
							}
						});
						$.ajax({url : "alarm/gas-strength!queryStrengthByDevId.do?devId=" + feature.Name,
							type : "POST",
							data : {
								startTime : $("#txt_time_begin").val(),
								endTime : $("#txt_time_end").val()
							},
							success : function(data) {
								var data = eval('(' + data + ')');
								var labelArray = [];
								var dataArray = [];
								var gasData = [];
								var dataContentArray = data.data;
								for ( var i = 0; i < dataContentArray.length; i++) {
									gasData.push([dataContentArray[i].upTime,dataContentArray[i].strength ]);
								}
								dataArray.push(gasData);
								labelArray.push('燃气浓度传感器');
	
								$("#lineGasStrengthHistory").html("");
								var jqplot = $.jqplot(
												"lineGasStrengthHistory",
												dataArray,
												{
													title : '燃气浓度历史数据',
													series : [ {
														lineWidth : 2,
														markerOptions : {
															style : 'dimaon'
														}
													} ],
													axesDefaults : {
														tickRenderer : $.jqplot.CanvasAxisTickRenderer,
														tickOptions : {
															angle : -30,
															fontSize : '10pt'
														}
													},
													axes : {
														xaxis : {
															renderer : $.jqplot.CategoryAxisRenderer
														}
													},
													legend : {
														show : true,
														location : 'e',
														labels : [ '燃气浓度传感器' ]
													}
												});
							}
						});
						myGlobalCtrl.detachEvent('FireFeatureMouseClick',_show_history_gas_strength);
					});
}

function show_vibrating_curve() {
	myGlobalCtrl.attachEvent('FireFeatureMouseClick', _show_vibrating_curve);
}
function _show_vibrating_curve(feature, evt) {
	if (feature.Layer.Caption != "设备图层") {
		return;
	}
	if (feature.GetFieldValue(feature.GetFieldCount() - 1) != "光纤") {
		return;
	}
	viewSensorDataDlgIndex = $.layer({
		type : 1,
		offset : [ '110px', '' ],
		shade : [ '', '', false ],
		area : [ 'auto', 'auto' ],
		page : {
			dom : '#viewVibratingCurveDlg'
		}
	});
	$.ajax({
		url : "alarm/vibrating-curve!queryVibratingCurve.do?devId="	+ feature.Name,
		type : "POST",
		success : function(data) {
			var data = eval('(' + data + ')');
			var labelArray = [];
			var dataArray = [];
			var vibData = [];
			var dataContentArray = data;

			var x = new Array();
			var y = new Array();
			x = dataContentArray.distance.split(",");
			y = dataContentArray.vibrating.split(",");

			if (x.length != y.length) {
				return;
			}
			$.each(x, function(index, val) {
				vibData.push([ x[index], y[index] ]);
			});

			dataArray.push(vibData);
			labelArray.push(data.logTime);

			$("#lineVibratingCurve").html("");
			var jqplot = $.jqplot("lineVibratingCurve", dataArray, {
				title : '振动曲线数据',
				series : [ {
					lineWidth : 2,
					markerOptions : {
						style : 'dimaon'
					}
				} ],
				axesDefaults : {
					tickRenderer : $.jqplot.CanvasAxisTickRenderer,
					tickOptions : {
						angle : -30,
						fontSize : '10pt'
					}
				},
				axes : {
					xaxis : {
						renderer : $.jqplot.CategoryAxisRenderer
					}
				},
				legend : {
					show : true,
					location : 'e',
					labels : [ data.logTime ]
				}
			});
		}
	});

	myGlobalCtrl.detachEvent('FireFeatureMouseClick', _show_vibrating_curve);
}

function show_vibrating_alarm_record() {
	$("#vibrating_alarm").datagrid({
		url : 'alarm/alarm-record!queryVibratingPosition.do',
		idField : "id",
		fit : true,
		pagePosition : "top",
		pagination : true,
		columns : [ [ {
			field : 'device',
			title : '设备',
			width : 100
		}, {
			field : 'deviceType',
			title : '设备类型',
			width : 100
		}, {
			field : 'message',
			title : '报警信息',
			width : 100,
			align : 'right'
		} ] ]
	});
	var alarmDlgHeightoffset = ($(document).height() - 380) + 'px';
	var alarmDlgWidthoffset = ($(document).width() - 700) + 'px';
	alarmDialogIndex = $.layer({
		type : 1,
		offset : [ alarmDlgHeightoffset, alarmDlgWidthoffset ],
		shade : [ '', '', false ],
		area : [ 'auto', 'auto' ],
		page : {
			dom : '#vibratingPositionPopDlg'
		}
	});

	setInterval(function() {
		$("#vibrating_alarm").datagrid("reload");
	}, 2000);
}

// 针对管线图层进行相应的缓冲区域分析
function BufferAnalysis() {
	try {
		var feature = myGlobalCtrl.Globe.SelectedObject;
		var line = feature.Geometry;

		if (feature.Layer.Caption.indexOf("管线") != -1 && line.Type == 302) {

			var polygon = line.CreateBuffer(10, false, 24, false, false);
			var ff = myGlobalCtrl.CreateFeature();
			ff.Geometry = polygon;
			myGlobalCtrl.Globe.MemoryLayer.AddFeature(ff);
			for ( var i = 0; i < myGlobalCtrl.Globe.Layers.Count; i++) {
				var fs = myGlobalCtrl.Globe.Layers.Item(i)
						.FindFeaturesInPolygon(polygon, false);
				for ( var j = 0; j < fs.Count; j++) {
					var fff = fs.Item(j);
					fff.HighLight = true;
				}
			}
		}
	} catch (e) {
		alert("请选择相应管线");
	}
}

// get the GPS location of fiber point based on devicename and distance
function flyToFiberGPSLocationFromDistance(deviceName, distance) {
	var layers = myGlobalCtrl.Globe.Layers;// 得到所有图层
	for ( var i = 0; i < layers.count; i++) {
		var layerItem = myGlobalCtrl.Globe.Layers.Item(i);
		if (layerItem.Caption == "光纤") {
			var featuresOfLayerItem = layerItem.GetAllFeatures();
			for ( var j = 0; j < featuresOfLayerItem.Count; j++) {
				var featureOfLayerItem = layerItem.GetFeatureByIndex(j);
				if (featureOfLayerItem.Name == deviceName) {
					var line = featureOfLayerItem.Geometry;
					if (line == null || line.Type != 302) {
						alert("定位光纤报警点错误");
						return;
					}
					var lineLength = distance;
					var segment = line.GetSegment(0, lineLength);
					if (segment != null) {
						var point = segment.Item(segment.PartCount - 1).Item(
								segment.Item(segment.PartCount - 1).Count - 1);
						var pntPosition = new ActiveXObject(
								"LOCASPACEPLUGIN.GSAPoint3d");
						pntPosition.X = point.X.toFixed(5);
						pntPosition.Y = point.Y.toFixed(5);
						pntPosition.Z = point.Z.toFixed(5);

						// 绘制园
						var lineLength = 5;
						var line = myGlobalCtrl.CreateGeoPolyline3D();
						var part = myGlobalCtrl.CreatePoint3ds();
						part.Add2(pntPosition);
						pntPosition.X += 0.000000001;
						part.Add2(pntPosition);
						line.AddPart(part);
						var polygon = line.CreateBuffer(lineLength * 2, true,
								5, true, false);
						var feature2 = myGlobalCtrl.CreateFeature();
						feature2.Geometry = polygon;
						myGlobalCtrl.Globe.MemoryLayer.AddFeature(feature2);

						myGlobalCtrl.Globe.FlyToPosition(pntPosition, 1);
					}
				}
			}
		}
	}
}

function huomiao(emitterFeature) {
	if (emitterFeature != null && emitterFeature.Geometry != null) {
		var geoParticle = myGlobalCtrl.CreateGeoParticle();
		var point3d = myGlobalCtrl.CreatePoint3D();
		point3d.SetValue(emitterFeature.Geometry.Position.X,
				emitterFeature.Geometry.Position.Y,
				emitterFeature.Geometry.Position.Z);
		geoParticle.Position = point3d;
		geoParticle.AltitudeMode = 2;

		var emitter = myGlobalCtrl.CreateRingParticleEmitter();
		emitter.TexturePath = "ParticleImage/flare1.png";// 烟1111111111111

		emitter.SetSizeFix1(1, 1);
		emitter.VelFix = 1;
		emitter.VelRnd = 5;

		emitter.AngleXYFix = 0;
		emitter.AngleXYRnd = 180;

		emitter.AngleXZFix = 90;
		emitter.AngleXZRnd = 0;

		emitter.LifeFix = 0.5;
		emitter.LifeRnd = 0.0;

		emitter.RotFix = 0;
		emitter.RotRnd = 0;

		emitter.RotVelFix = 0;
		emitter.RotVelRnd = 0;

		emitter.EmitPerSec = 100;
		emitter.IsLumAdded = true;

		var white = myGlobalCtrl.CreateColorRGBA();
		white.SetValue(255, 255, 255, 255);

		var black = myGlobalCtrl.CreateColorRGBA();
		black.SetValue(0, 0, 0, 255);

		emitter.ColorRndStart = white;
		emitter.ColorRndEnd = black;

		var effector2 = myGlobalCtrl.CreateColorParticleEffector();
		effector2.SetColorChanged1(0, -1, -1, 0);
		effector2.StartTime = 0.0;
		effector2.EndTime = -1.0;
		emitter.AddEffector(effector2);

		geoParticle.AddEmitter(emitter);

		geoParticle.Play();
		var feature = myGlobalCtrl.CreateFeature();
		feature.Geometry = geoParticle;

		myGlobalCtrl.Globe.MemoryLayer.AddFeature(feature);
	}
}

function longiSection(feature, evt) {
	// alert("服务器版");
	if (feature.Layer.Caption == "雨水管线") {
		// 方法中的两个参数会在myGlobalCtrl的FireFeatureMouseClick事件触发时获取。
		$
				.ajax({
					url : "alarm/device-realtime-data!getAlarmLiquidTransSection.do?model.dbCord="
							+ feature.name,
					type : "post",
					success : function(data) {
						var data = eval('(' + data + ')');
						var rate = data.liquid / data.diameter;
						var str = "<table>";
						str += '<p><center>雨水管线纵断面图显示</center></p>';
						str += '<center><img src="http://localhost:8080/alarm/images/longiSection/';
						if (rate == 0)
							str += '0';
						else if (rate > 0 && rate < 0.15)
							str += '10';
						else if (rate >= 0.15 && rate < 0.25)
							str += '20';
						else if (rate >= 0.25 && rate < 0.35)
							str += '30';
						else if (rate >= 0.35 && rate < 0.45)
							str += '40';
						else if (rate >= 0.45 && rate < 0.55)
							str += '50';
						else if (rate >= 0.55 && rate < 0.65)
							str += '60';
						else if (rate >= 0.65 && rate < 0.75)
							str += '70';
						else if (rate >= 0.75 && rate < 0.85)
							str += '80';
						else if (rate >= 0.85 && rate < 1)
							str += '90';
						else if (rate == 1)
							str += '100';
						str += '.jpg" width="190" height="95" /></center>';
						str += "<br/><center>液位高度=" + data.liquid + "，管线直径="
								+ data.diameter + "</center>";
						str += "</table>";
						featureTooltip.ShowBalloon1(evt.x, evt.y, str); // 显示气泡
					}
				});
		// 取消事件监听器
		myGlobalCtrl.detachEvent("FireFeatureMouseClick", longiSection);
		// 设置新的事件监听
		myGlobalCtrl.attachEvent("FireFeatureMouseClick", showballoon);
	}
}

function historyCurve(feature) {
	// alert("服务器版");
	if (feature.Layer.Caption == "雨水管线") {
		$
				.ajax({
					url : "alarm/device-realtime-data!getAlarmLiquidHistory.do?model.dbCord="
							+ feature.Name,
					type : "POST",
					success : function(data) {
						var data = eval('(' + data + ')');
						if (data[0] == "noneRain") {
							alert("当前未下雨，没有过载节点！");
						} else {
							$("#currentFeatureName").val(feature.Name);
							xtGasDisplayDlgIndex = $.layer({
								type : 2,
								title : '雨水管线液位历史监测数据曲线图',
								offset : [ '20px', '' ],
								area : [ "900px", "600px" ],
								iframe : {
									src : 'common/layout/historyCurveDlg.jsp'
								}
							});
						}
					}
				});
		// 取消事件监听器
		myGlobalCtrl.detachEvent("FireFeatureMouseClick", historyCurve);
		// 设置新的事件监听
		myGlobalCtrl.attachEvent("FireFeatureMouseClick", showballoon);
	}
}

function transverseSection(feature, evt) {
	// alert("服务器版");
	if (feature.Layer.Caption == "雨水管线") {
		// 方法中的两个参数会在myGlobalCtrl的FireFeatureMouseClick事件触发时获取。
		$
				.ajax({
					url : "alarm/device-realtime-data!getAlarmLiquidTransSection.do?model.dbCord="
							+ feature.name,
					type : "post",
					success : function(data) {
						var data = eval('(' + data + ')');
						var rate = data.liquid / data.diameter;
						var str = "<table>";
						str += '<p><center>雨水管线横截面图显示</center></p>';
						str += '<center><img src="http://localhost:8080/alarm/images/transSection/';
						if (rate == 0)
							str += '0';
						else if (rate > 0 && rate < 0.15)
							str += '10';
						else if (rate >= 0.15 && rate < 0.25)
							str += '20';
						else if (rate >= 0.25 && rate < 0.35)
							str += '30';
						else if (rate >= 0.35 && rate < 0.45)
							str += '40';
						else if (rate >= 0.45 && rate < 0.55)
							str += '50';
						else if (rate >= 0.55 && rate < 0.65)
							str += '60';
						else if (rate >= 0.65 && rate < 0.75)
							str += '70';
						else if (rate >= 0.75 && rate < 0.85)
							str += '80';
						else if (rate >= 0.85 && rate < 1)
							str += '90';
						else if (rate == 1)
							str += '100';
						str += '.jpg" width="190" height="95" /></center>';
						str += "<br/><center>液位高度=" + data.liquid + "，管线直径="
								+ data.diameter + "</center>";
						str += "</table>";
						featureTooltip.ShowBalloon1(evt.x, evt.y, str);// 显示气泡
					}
				});
		// 取消事件监听器
		myGlobalCtrl.detachEvent("FireFeatureMouseClick", transverseSection);
		// 设置新的事件监听
		myGlobalCtrl.attachEvent("FireFeatureMouseClick", showballoon);
	}
}

function pipelineFull(feature, evt) {
	if (feature.Layer.Caption == "雨水管线") {
		// 方法中的两个参数会在myGlobalCtrl的FireFeatureMouseClick事件触发时获取。
		$
				.ajax({
					url : "alarm/device-realtime-data!getAlarmLiquidTransSection.do?model.dbCord="
							+ feature.name,
					type : "post",
					success : function(data) {
						var data = eval('(' + data + ')');
						var rate = data.liquid / data.diameter;
						var str = "<table>";
						str += '<p><center>管道充满度分析</center></p>';
						str += '<center><img src="http://localhost:8080/alarm/images/transSection/';
						if (rate == 0)
							str += '0';
						else if (rate > 0 && rate < 0.15)
							str += '10';
						else if (rate >= 0.15 && rate < 0.25)
							str += '20';
						else if (rate >= 0.25 && rate < 0.35)
							str += '30';
						else if (rate >= 0.35 && rate < 0.45)
							str += '40';
						else if (rate >= 0.45 && rate < 0.55)
							str += '50';
						else if (rate >= 0.55 && rate < 0.65)
							str += '60';
						else if (rate >= 0.65 && rate < 0.75)
							str += '70';
						else if (rate >= 0.75 && rate < 0.85)
							str += '80';
						else if (rate >= 0.85 && rate < 1)
							str += '90';
						else if (rate == 1)
							str += '100';
						str += '.jpg" width="190" height="95" /></center>';
						var ratePercent = parseInt(rate * 100);
						str += "<br/><center>管道充满度=" + ratePercent + "%";
						str += "<br/><center>液位高度=" + data.liquid + "，管线直径="
								+ data.diameter + "</center>";
						str += "</table>";
						featureTooltip.ShowBalloon1(evt.x, evt.y, str);// 显示气泡
					}
				});
		// 取消事件监听器
		myGlobalCtrl.detachEvent("FireFeatureMouseClick", pipelineFull);
		// 设置新的事件监听
		myGlobalCtrl.attachEvent("FireFeatureMouseClick", showballoon);
	}
}
