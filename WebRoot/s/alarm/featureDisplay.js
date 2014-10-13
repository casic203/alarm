	var deviceGridColumn = [[{
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
	var rqConfigColumn = [[{
	    field : 'sjcjzq',
	    title : '数据采集周期'
    }, {
	    field : 'sjsczq',
	    title : '数据上传周期'
    } ]];
var FeatureDisplay = function() {
	this.successor = null;
};
FeatureDisplay.prototype = {
	handleRequest : function(operType, feature) {
		if (operType == this.featureType) {
			this.showFeature(feature);
		}
		if (this.successor) {
			this.successor.handleRequest(operType, feature);
		}
	},
	showFeature : function(feature) {
		// this method will be overriden in the subclass
		return false;
	},
	setSuccessor : function(successor) {
		this.successor = successor;
	}
};

// extend the featureDisplay Class
var XtGasDisplay = function() {
	this.featureType = "燃气智能监测终端";
};
extend(XtGasDisplay, FeatureDisplay)
XtGasDisplay.prototype.showFeature = function(feature) {
	// 打开显示设备数据弹出框
	$("#currentFeatureName").val(feature.Name);
	xtGasDisplayDlgIndex = $.layer({
		type : 2,
		title:'燃气智能监测终端设备信息',
		offset : [ '20px', '' ],				
		area : [ "900px","600px" ],
		iframe:{src:'common/layout/xtGasDisplayDlg.jsp'}
	});
};

// extend the featureDisplay Class
var SzWSGasDisplay = function() {
	this.featureType = "污水有害气体监测仪";
};
extend(SzWSGasDisplay, FeatureDisplay)
SzWSGasDisplay.prototype.showFeature = function(feature) {
	// TODO: display the gas device info from shen zheng Company
};

// extend the featureDisplay Class
var FiberDisplay = function() {
	this.featureType = "光纤";
};
extend(FiberDisplay, FeatureDisplay)
FiberDisplay.prototype.showFeature = function(feature) {
	var dataColumn = "[[";
	var gridData = "{total:1, rows:[{";
	for(var i =0;i<feature.GetFieldCount();i++) {
        var defn = feature.GetFieldDefn(i);
	    var fieldName = defn.Name;
	    var fieldValue = feature.GetFieldValue(i);
	    dataColumn += ("{field:'" + fieldName + "', title:'" +  fieldName + "'},");
	    gridData += (fieldName + ":'" + fieldValue + "',");
	}
	dataColumn = dataColumn.substring(0, dataColumn.lastIndexOf(","));
	dataColumn += "]]";
	gridData = gridData.substring(0, gridData.lastIndexOf(","));
	gridData += "}]}";
    $("#fiberDevicePropName").html(dataColumn);
    $("#fiberDevicePropValue").html(gridData);
    
	// 打开显示设备数据弹出框
    //1450px 630px
	$("#currentFeatureName").val(feature.Name);
	xtGasDisplayDlgIndex = $.layer({
		type : 2,
		title:'光纤',
		offset : [ '20px', '' ],				
		area : [ "960px","610px" ],
		iframe:{src:'common/layout/fiberDisplayDlg.jsp'}
	});
};

// extend the featureDisplay Class
var AdLiquidLevelDisplay = function() {
	this.featureType = "液位监测仪";
};
extend(AdLiquidLevelDisplay, FeatureDisplay)
AdLiquidLevelDisplay.prototype.showFeature = function(feature) {
	//打开显示设备数据弹出框
	$("#currentFeatureName").val(feature.Name);
	xtGasDisplayDlgIndex = $.layer({
		type : 2,
		title:'液位监测仪',
		offset : [ '20px', '' ],				
		area : [ "900px","600px" ],
		iframe:{src:'common/layout/adLiquidLevelDisplayDlg.jsp'}
	});
};

// extend the featureDisplay Class
var AdSLDeviceDisplay = function() {
	this.featureType = "渗漏预警仪";
};
extend(AdSLDeviceDisplay, FeatureDisplay)
AdSLDeviceDisplay.prototype.showFeature = function(feature) {
	// TODO: display SL device info from adler Company
};

// extend the featureDisplay Class
var AdDSDeviceDisplay = function() {
	this.featureType = "多功能漏损监测仪";
};
extend(AdDSDeviceDisplay, FeatureDisplay)
AdDSDeviceDisplay.prototype.showFeature = function(feature) {
	// TODO: display multi function device info from adler Company
};

function extend(subClass, superClass) {
	var F = function() {
	};
	F.prototype = superClass.prototype;
	subClass.prototype = new F();
	subClass.prototype.constructor = subClass;

	subClass.superClass = superClass.prototype;
	if (subClass.prototype.constructor == Object.prototype.constructor) {
		superClass.prototype.constructor = superClass;
	}
}
