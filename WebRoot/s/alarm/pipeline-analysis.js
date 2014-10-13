
//globalLayerControl.setlayesInVisiable("天然气",“光纤”):设置上述2个图层可见，其余图层隐藏
//globalLayerControl.setAllLayersVisiable(true！false) 显示或者隐藏所有图层
//globalLayerControl.setFeatureVisiableByFieldValue(“光纤”，类型，“天然气”)
var globalLayerControl={
	setlayesInVisiable:function(){
			
		this.setAllLayersVisiable(false);
		for(i=1;i<myGlobalCtrl.Globe.Layers.Count;i++)
	        {
	            var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
				for(var j=0;j<arguments.length;j++)
				{
					if(layer.Caption().indexOf(arguments[j])==-1)
					{
						continue;
					}
					layer.Visible=true;
				}
	        }
	     myGlobalCtrl.refresh();
	},
	setAllLayersVisiable:function(isVisiable){
		for(i=1;i<myGlobalCtrl.Globe.Layers.Count;i++)
				{
					var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
					layer.Visible=isVisiable;
				}
			 myGlobalCtrl.refresh();
	},
	setFeatureVisiableByFieldValue:function(layername,fieldName,fieldValue){
		for(i=1;i<myGlobalCtrl.Globe.Layers.Count;i++){
	            var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
				if(layer.Caption().indexOf(layername)!=-1){
					var features = layer.GetAllFeatures();
					for ( var j = 0; j < features.Count; j++) {
						var feature = layer.GetFeatureByIndex(j);
						feature.Visible=false;
						  for(var i =0;i<feature.GetFieldCount();i++) {
							var defn = feature.GetFieldDefn(i);
							if(defn.name==fieldName&&feature.GetFieldValue(i)==fieldValue){
								feature.Visible=true;
							}
						}
					}
			}
		}
		 myGlobalCtrl.refresh();
	},
	getFeatureArrayByFeatureName:function(featureNameArray,laryerName, fiberType){
		
		var featureArray = new Array();
		for(var i=1;i<myGlobalCtrl.Globe.Layers.Count;i++){
	            var layer = myGlobalCtrl.Globe.Layers.GetLayerByID(i);
				if(layer.Caption()==laryerName){
					var features = layer.GetAllFeatures();
					for ( var k = 0; k < features.Count; k++) {
						var feature = layer.GetFeatureByIndex(k);
						for(var m=0;m<featureNameArray.length;m++){
							var fType = feature.GetFieldValue(feature.GetFieldCount() - 8);
							if(feature.Name==featureNameArray[m] && fType == fiberType){
							    featureArray.push(feature)
							    break;
							}
						}
					}
				}
		}
		return featureArray;
	}
}

function shapeInfoAnalysis(args0, args1) {
	globalLayerControl.setlayesInVisiable(args0, args1);
}

function excavationAlarmAnalysis(type) {
	$.ajax({
		url:'alarm/pipeline-alarm-analysis!excavationAnalysis.do',
		type:'POST',
				success : function(data) {
					try {
						var featureArray = [];
						var deviceCodeArray = [];
						var data = eval('(' + data + ')');
						for ( var i = 0; i < data.length; i++) {
							var deviceCode = data[i].code;
							deviceCodeArray.push(deviceCode);
						}

						var featureArray = globalLayerControl
								.getFeatureArrayByFeatureName(deviceCodeArray,
										'光纤', type);
						var gridColumn = "[[";
						var gridData = "{total:" + featureArray.length
								+ ", rows:[{";

						for ( var i = 0; i < featureArray.length; i++) {
							var feature = featureArray[i];
							for ( var j = 0; j < feature.GetFieldCount(); j++) {
								if (i == 0) {
									var fieldName = feature.GetFieldDefn(j).Name;
									gridColumn += ("{field:'" + fieldName
											+ "', title:'" + fieldName + "', width:90}, ");
								}
								var fieldValue = feature.GetFieldValue(j);
								fieldValue = ("'" + fieldValue + "'");
								gridData += ("" + fieldName + ":"
										+ fieldValue.replace(/\s+/g, '') + ",");
							}
						}
						gridColumn = gridColumn.substring(0, gridColumn
								.lastIndexOf(","));
						gridColumn += "]]";
						gridData = gridData.substring(0, gridData
								.lastIndexOf(","));
						gridData += "}]}";

						gridColumn = eval('(' + gridColumn + ')');
						gridData = eval('(' + gridData + ')');
						// 清除url
						$('#choice-area-query-tbl').datagrid({
							url : ''
						});
						$('#choice-area-query-tbl').datagrid({
							columns : gridColumn,
							data : gridData,
							fitColumn : true,
							singleSelect : true,
							fit : true,
							onClickRow : function(rowIndex, rowData) {

							}
						});

						$("#arlam_record_layout").layout("expand", "south");

					} catch (e) {
						
					}

				}
	});
}




