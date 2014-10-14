/*
 * 报警记录的动态显示
 */

	//根据不同设备类型飞入到不同地方,并模拟相应事故现场

var global_deviceType=["光纤","燃气智能监测终端","爆管预警仪","渗漏预警仪","多功能漏损监测仪","液位监测仪"];
var global_deviceLayerName="设备图层";
function jumpToAlarm(deviceType, deviceCode,alarmValue){
	if(deviceType==global_deviceType[0]){
	//光纤报警
	var layer=myGlobalCtrl.Globe.Layers.GetLayerByCaption(deviceType);
	var order=alarmValue.indexOf(",");
	var dis = alarmValue.substring(0,order);
	 $.ajax({
		url: "alarm/vibrating-postion!queryPostionByDevAndDist.do",
        timeout: 5000,
		data: {devId:deviceCode, distance:dis},
        error: function (XMLHttpRequest, textStatus, errorThrown) {
				alert("无法获取光纤数据");
				},
        success: function (data, textStatus) {
				if(data==null){
					return;}
                if (textStatus == "success") { // 返回光纤经纬度坐标成功
                	var jdata=eval("(" + data + ")");
					flyToFiberByGPS(jdata.longitude,jdata.latitude);
                 }
			}
		});
			
	}
	else if(deviceType==global_deviceType[1]||deviceType==global_deviceType[2]||deviceType==global_deviceType[3]||
	deviceType==global_deviceType[4]||deviceType==global_deviceType[5]){
	//燃气报警
		var layer=myGlobalCtrl.Globe.Layers.GetLayerByCaption(global_deviceLayerName);
		var features= layer.GetFeatureByName(deviceCode,true);
		if(features.Count>1){
			alert("设备编号不唯一");
			return
		}
		
		var feature=features.Item(0);
		if(feature==null){
			alert("无该设备模型");
			return;
		}
		
		if(deviceType==global_deviceType[1]){
			huomiao(feature);
			//myGlobalCtrl.Globe.FlyToFeature(feature);
		}
		else{
			penquan(feature);
			myGlobalCtrl.Globe.FlyToFeature(feature);
		}
		
		feature.HighLight = true
		var model = feature.Geometry;
		var position = model.Position;
		var camerastate = myGlobalCtrl.Globe.CameraState;
		camerastate.Longitude = position.X;
		camerastate.Latitude = position.Y;
		camerastate.Distance = position.Z+20;
		myGlobalCtrl.Globe.JumpToCameraState(camerastate);
		
	}
	else{
		alert("无该报警类型");
	}
}

	//报警显示模板
var alarmDialogIndex;
var globalDevieType=["光纤","多功能漏损监测仪","渗漏预警仪","燃气智能监测终端","液位监测仪","爆管预警仪"];
var markup = "<li><table><thead><tr>"+
						"<th data-options=\"field:\'propName\',width:99\"></th>"+
						"<th data-options=\"field:\'propValue\',width:250\"></th>"+
					"</tr>"+
				"</thead>"+
				"<tbody>"+
					"<tr><td>设备类型</td><td>${deviceType}</td></tr>"+
					"<tr><td>消息 </td><td>${message}</td></tr>"+
					"<tr><td>接警人</td><td>${dealPerson}</td></tr>"+
					"<tr><td>状态</td><td>${messageStatus}</td></tr>"+
				"</tbody>"+
			"</table>"+
			"<a style=\"float:right\" onclick=jumpToAlarm(\"${deviceType}\",\"${deviceCode}\",\"${alarmValue}\")>飞到设备</a>"+
			"</li>";
			//"<a style=\"float:right\" onclick=\"jumpToAlarm(\"" +"${deviceType}\",\"" +"$(\{deviceCode}\"" +",\"" +"${alarmValue}\")\">" +"飞到设备</a>"+"</li>";
$(function () {
    (function longPolling() {
     $.ajax({
		url: "alarm/query-alarm-info!queryAlarmRecord.do",
       // timeout: 5000,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
				//alert("无法获取数据");
				},
           success: function (data, textStatus) {
                //todo: list
				
                if (textStatus == "success") { // ???
					
					if(data!=null){
						if(alarmDialogIndex==null){
							// 
							var alarmDlgWidthoffset = ($(document).width() - 306)
									+ 'px';
							var alarmDlgHeightoffset = ($(document).height() - 201)
									+ 'px';
							
							alarmDialogIndex = $.layer({
								type : 1,
								title : '最新报警记录',
								offset : [ alarmDlgHeightoffset,
										alarmDlgWidthoffset ],
								shade : [ '', '', false ],
								area : [ '300px', '200px' ],
								page : {
									dom : '#alarmPopDlg'
								}
						     });
							 
						}
					$("#alarmList").empty();
					var jdata=eval("(" + data + ")");
					//var tables = "<LI><TABLE><THEAD><TR><TH data-options=\"field:'propName',width:99\"></TH><TH data-options=\"field:'propValue',width:250\"></TH></TR></THEAD><TBODY><TR><TD>设备类型</TD><TD>燃气智能监测终端</TD></TR><TR><TD>消息 </TD><TD>燃气发生泄漏了</TD></TR><TR><TD>接警人</TD><TD>张帆</TD></TR><TR><TD>状态</TD><TD>未处理</TD></TR></TBODY></TABLE><A onclick=jumpToAlarm(FFFFFFFFFFF,燃气智能监测终端,default) style=\"FLOAT: right\" data-options=\"iconCls:'icon-back'\">飞到设备</A></LI><LI><TABLE><THEAD><TR><TH data-options=\"field:'propName',width:99\"></TH><TH data-options=\"field:'propValue',width:250\"></TH></TR></THEAD><TBODY><TR><TD>设备类型</TD><TD>多功能漏损监测仪</TD></TR><TR><TD>消息 </TD><TD>管线发生漏损</TD></TR><TR><TD>接警人</TD><TD>张帆</TD></TR><TR><TD>状态</TD><TD>未处理</TD></TR></TBODY></TABLE><A onclick=jumpToAlarm(000069130172,多功能漏损监测仪,default) style=\"FLOAT: right\" data-options=\"iconCls:'icon-back'\">飞到设备</A></LI><LI><TABLE><THEAD><TR><TH data-options=\"field:'propName',width:99\"></TH><TH data-options=\"field:'propValue',width:250\"></TH></TR></THEAD><TBODY><TR><TD>设备类型</TD><TD>燃气智能监测终端</TD></TR><TR><TD>消息 </TD><TD>fasdfasd</TD></TR><TR><TD>接警人</TD><TD>张帆</TD></TR><TR><TD>状态</TD><TD>未处理</TD></TR></TBODY></TABLE><A onclick=jumpToAlarm(,燃气智能监测终端,default) style=\"FLOAT: right\" data-options=\"iconCls:'icon-back'\">飞到设备</A></LI><LI><TABLE><THEAD><TR><TH data-options=\"field:'propName',width:99\"></TH><TH data-options=\"field:'propValue',width:250\"></TH></TR></THEAD><TBODY><TR><TD>设备类型</TD><TD>燃气智能监测终端</TD></TR><TR><TD>消息 </TD><TD>tdt</TD></TR><TR><TD>接警人</TD><TD>张帆</TD></TR><TR><TD>状态</TD><TD>未处理</TD></TR></TBODY></TABLE><A onclick=jumpToAlarm(,燃气智能监测终端,default) style=\"FLOAT: right\" data-options=\"iconCls:'icon-back'\">飞到设备</A></LI>";
					//$("#conn ul").empty();
					//$("#conn ul").html(tables);
					$.template( "alarmTemplate", markup );
					$.tmpl( "alarmTemplate", jdata ).appendTo( "#alarmList" );
					// $("#conn ul li table").addClass("easyui-datagrid");
					// $("#conn a").addClass("easyui-linkbutton");
				//	 alert($("#alarmPopDlg #conn #alarmList").html());
					}
					 setTimeout(longPolling,15000)
                 }
			}
		});
    })();     
});

$(function(){
	var scrtime;    
	$("#conn").hover(function(){
		clearInterval(scrtime);  },
		function(){         
			scrtime = setInterval(function(){       
				var ul = $("#conn ul"); 
				var liHeight = ul.find("li:last").height();
				ul.animate({marginTop : liHeight+40 +"px"},1000,function(){ 
					ul.find("li:last").prependTo(ul)
					ul.find("li:first").hide();
					ul.css({marginTop:0});
					ul.find("li:first").fadeIn(1000);
					}); 
				},3000);
			}).trigger("mouseleave");});




















