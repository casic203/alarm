<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<title>维修反馈</title>
<meta name="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/frm.css" />
</head>
<style type="text/css">
table,td,tr {
	font-size: 12px;
}

#feedback-tbl {
	font-size: 12px;
	width: 100%;
	height: 200px;
	display: block;
	border: 1pt solid #E6E6E6;
	border-collapse: collapse;
	border-spacing: 0;
}

#feedback-tbl tr,td {
	border: 1pt solid #E6E6E6;
	border-collapse: collapse;
	border-spacing: 0;
}

#feedback-tbl td{
	padding: 5px 5px 5px 5px;
} 

</style>
<body>
		<div class="easyui-layout" id="hidden-tab" style="width: 100%; height: 100%; display:none"
			data-options="collapsible:false, border:false, fit:true">
			<div data-options="region:'west', collapsible:false" title=""
				style="width: 620px; ">
				<table id="work-sheet-feedback-tbl"></table>
			</div>
			<div class="feedback" data-options="region:'center'" title="详细信息">
				<table id="feedback-tbl">
					<tr>
						<td style="width:80px; height:30px;background-color: #F5F5F5;">是否反馈</td>
						<td><div id="idFeedback" style="background-color: #FAFAFA;"></div></td>
					</tr>
					<tr>
						<td style="width:80px; height:30px;background-color: #F5F5F5;">单号</td>
						<td><div id="sheetNo"></div></td>
					</tr>
					<tr>
						<td style="width:80px; height:30px; background-color: #F5F5F5;">设备</td>
						<td><div id="device"></div></td>
					</tr>
					<tr>
						<td style="width:80px; height:30px; background-color: #F5F5F5;">负责人</td>
						<td><div id="charger"></div></td>
					</tr>
					<tr>
						<td style="width:80px; height:30px; background-color: #F5F5F5;">任务开始日期</td>
						<td><div id="beginDate"></div></td>
					</tr>
					<tr>
						<td style="width:80px; height:30px; background-color: #F5F5F5;">任务结束日期</td>
						<td><div id="endDate"></div></td>
					</tr>
					<tr>
						<td style="width:80px; height:80px; background-color: #F5F5F5;">故障现象</td>
						<td><div id="alarmRecord"></div></td>
					</tr>
					<tr>
						<td style="width:80px; height:80px; background-color: #F5F5F5;">故障原因</td>
						<td><div id="alarmReason"></div></td>
					</tr>
					<tr>
						<td style="width:80px; height:80px; background-color: #F5F5F5;">解决方法</td>
						<td><div id="solution"></div></td>
					</tr>
				</table>
			</div>
		</div>
	
		<div id="work-sheet-feedback-dialog" class="easyui-dialog" title="反馈"
			style="padding:10px 20px;width:730px;height:460px; display: none;"
			data-options="iconCls:'icon-ok',resizable:true,modal:true,closed:true">
			<div class="ftitle">反馈表单</div>
			<br>
			<form id="fm_add" method="post">
				<input type="hidden" id="deviceId" /> <input type="hidden"
					id="isAder" name="isAder" /> <input type="hidden" id="workSheetId"
					name="workSheetId" /> <input type="hidden" id="workSheetFeedbackId"
					name="workSheetFeedbackId" />
				<table style="width:90%">
					<tr>
						<td style="width:20%">故障原因</td>
						<td colspan="5" style="text-align: center;"><textarea
								id="alarmReason-fm" name="alarmReason" rows="6"
								style="width: 100%;"></textarea></td>
					</tr>
	
					<tr id="leakageRow-ader">
						<td style="width:20%">漏损量</td>
						<td><input type="text" id="lossValues" name="lossValues" /></td>
					</tr>
					<tr id="dma-ader">
						<td style="width:20%">分区</td>
						<td>
							<div id="dmaName"></div> <input type="hidden" name="dmaId"
							id="dmaId" /> <a class="easyui-linkbutton" id="choose-dma-btn">选择分区</a>
						</td>
					</tr>
					<tr>
						<td style="width:20%">解决方法</td>
						<td colspan="5" style="text-align: center;"><textarea
								id="solution-fm" name="solution" rows="6" style="width: 100%;"></textarea></td>
					</tr>
					<tr>
						<td colspan="6" style="text-align: right; padding: 5;"><a
							id="edit_ok_btn" href="#" class="easyui-linkbutton"
							data-options="iconCls : 'icon-ok'">确定</a> <a id="edit_cls_btn"
							href="#" class="easyui-linkbutton"
							data-options="iconCls : 'icon-no'"
							onclick="javascript:$('#work-sheet-feedback-dialog').dialog('close');">关闭</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
	
		<div id="work-sheet-choose-dma-dialog" class="easyui-dialog"
			title="选择分区" style="width:218px; height:102px; display:none;"
			data-options="iconCls:'icon-ok', resizable:true, modal:true, closed:true">
			<table>
				<tr>
					<td>监测点</td>
					<td><input id="positionInfo"></td>
				</tr>
				<tr>
					<td>分区</td>
					<td><input id="dma"></td>
				</tr>
			</table>
		</div>
</body>

<script type="text/javascript">
	document.onreadystatechange = function() { // 当页面加载状态改变的时候执行这个方法
		if (document.readyState == "complete") { // 当页面加载完成之后执行
			$("#work-sheet-feedback-tbl").datagrid(_dg_cfg).datagrid({
				url : "work-sheet-feedback!query.do"
			});
		
		    $("#edit_ok_btn").bind("click", function() {
		    	$("#fm_add").submit();
		    });
		
			$("#fm_add").form({
				url : "work-sheet-feedback!submitFeedback.do",
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
						$("#work-sheet-feedback-tbl").datagrid("reload"); // reload the user data
					}
					$.messager.alert('结果', result.msg);
					$("#work-sheet-feedback-dialog").dialog("close");
				}
			});
		    
		    $("#choose-dma-btn").bind("click", function() {
		    	$("#work-sheet-choose-dma-dialog").dialog("open");
		    	
		    	$("#positionInfo").combobox({
		    		url:"work-sheet-feedback!getPosByDeviceId.do?deviceId=" + $("#deviceId").val(),
		    		valueField : 'id',
		    		textField : 'elementName',
		    		cache : false,
		    		editable : false,
		    		required : true,
		    		onSelect : function(record) {
		    			var posId = $("#positionInfo").combobox("getValue");
		    			if ("" != posId) {
		    				$.ajax({
		    					type : "POST",
		    					url : "work-sheet-feedback!getDMAByPosId.do?posId=" + posId,
		    					dataType : "json",
		    					success : function(data) {
		    						$("#dma").combobox("loadData", data);
		    					}
		    				});
		    			}
		    		}
		    	});
		    });
		    
		    $("#dma").combobox({
	    		valueField : 'id',
	    		textField : 'elementName',
	    		cache : false,
	    		editable : false,
	    		required : true,
	    		onSelect : function(record) {
	    			$("#dmaId").val(record.id);
	    			$("#dmaName").html(record.elementName);
	    			$("#work-sheet-choose-dma-dialog").dialog("close");
	    		}
		    });
			
			$("#hidden-tab").css("display", "block");
			$("#work-sheet-feedback-dialog").css("display", "block");
			$("#work-sheet-choose-dma-dialog").css("display", "block");
		}
	};

	var col = [ [ {
		field : 'sheetNo',
		title : '单号'
	}, {
		field : 'device',
		title : '设备'
	}, {
		field : 'charger',
		title : '负责人'
	}, {
		field : 'beginDate',
		title : '开始日期'
	}, {
		field : 'endDate',
		title : '结束日期'
	}, {
		field : 'sheetStatus',
		title : '状态',
		formatter:function(value, row, index) {
			if(value == '领取') {
				return "<font color='red'>未反馈</font>";
			} else {
				return value;
			}
		
		}
	} ] ];

	var _dg_cfg = {
		title : "已领取的工单",
		columns : col,
		fit : true,
		pagePosition : "top",
		pagination : true,
		rownumbers : true,
		singleSelect : true,
		pageList : [ 5, 10, 20, 30, 40, 50 ],
		pageSize : 10,
		idField : "id",
		toolbar : [ {
			iconCls : 'icon-remove',
			text : "维修反馈",
			handler : function() {
				$("#alarmReason-fm").val('');
				$("#lossValues").val('');
				$("#dmaName").html('');
				$("#dmaId").val('');
				$("#solution-fm").val('');
				$("#workSheetFeedbackId").val('');
				
				//获取工单号，通过工单号查询维修反馈信息
				var rowData = $("#work-sheet-feedback-tbl").datagrid("getSelected");
				if(rowData) {
				    $.ajax({
						type:"POST",
						url:"work-sheet-feedback!queryFeedback.do",
					    data: {
					    	  'model.id':rowData.id
					    },
						success:function(data) {
							data = eval('(' + data + ')');
							//弹出维修反馈表单
							$("#work-sheet-feedback-dialog").dialog("open");
							
							$("#workSheetId").val(rowData.id);
							if(data.isLeakage) {
								$("#isAder").val(1); //埃德尔设备
								$("#leakageRow-ader").css("display", "block");
								$("#dma-ader").css("display", "block");
							} else {
								$("#isAder").val(0); //非埃德尔设备
								$("#leakageRow-ader").css("display", "none");
								$("#dma-ader").css("display", "none");
							}
							
							$("#deviceId").val(data.deviceId);
							if(data.havaData) {
								$("#alarmReason-fm").val(data.data.alarmReason);
								$("#lossValues").val(data.data.lossValues);
								$("#dmaName").html(data.data.dmaName);
								$("#dmaId").val(data.data.dmaId);
								$("#solution-fm").val(data.data.solution);
								$("#workSheetFeedbackId").val(data.data.id);
							}
						}
					});
				}
			}
		} ],
		onSelect:function(rowIndex, rowData) {
			$("#alarmReason").html('');
			$("#solution").html('');
			
 			$("#feedback-tbl").css("display", "block");
			var sheetStatus = rowData.sheetStatus;
			if(sheetStatus == '已反馈') {
				$("#idFeedback").html("已反馈");
				
				//填充详细信息区域
			    $.ajax({
					type:"POST",
					url:"work-sheet-feedback!queryFeedback.do",
				    data: {
				    	  'model.id':rowData.id
				    },
					success:function(data) {
						data = eval('(' + data + ')');
						if(data.havaData) {
							$("#alarmReason").html(data.data.alarmReason);
							$("#solution").html(data.data.solution);
						}
					}
				});
				
			} else {
				$("#idFeedback").html("<font color='red'>未反馈</font>");
			}
			$("#alarmRecord").html(rowData.alarmRecord); 
			
			$("#sheetNo").html(rowData.sheetNo);
			$("#device").html(rowData.device);
			$("#charger").html(rowData.charger);
			$("#beginDate").html(rowData.beginDate);
			$("#endDate").html(rowData.endDate);
		}
	};
</script>

</html>

<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
