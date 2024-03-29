<%@ page language="java" pageEncoding="UTF-8"%>
<input type="hidden" id="currentFeatureName" />
<div style="display:none;" id="fiberDevicePropName"></div>
<div style="display:none;" id="fiberDevicePropValue"></div>
<div id="indexDialogArea" style="display:none">

	<%-- 选择区域统计 --%>
	<div id="barChart_hide"	style="z-index:1;display:none;height:400px;width:800px">
		<div style="margin-top:10px;">
		</div>
		<div id="barChartDiv">
			<div id="barChart" style="width:600px;height:360px;"></div>
		</div>
	</div>

	<%-- 全区域统计 --%>
	<div id="all-area-cal-dialog" style="z-index:1;display:none;height:390px;width:710px">
		<div id="all-area-barChart" style="width:710px;height:360px;"></div>
	</div>

	<%-- 关键字查询 --%>
	<div id="key-query-dialog"	style="z-index:1;display:none;width:360px;height:360px;">
		<div style="margin-top:15px;">
			<span><font style="font-size:13px;">关键字: </font>
			<input type="text" id="queryKeyText" /></span> 
			<span><a href="#" iconCls="icon-search" class="easyui-linkbutton" onclick="keyQuery()">查询</a></span>
		</div>
		<div style="margin-top:10px;">
			<table id="query-result-datagrid"></table>
		</div>
	</div>

	<div id="area_alarm_record_div"
		style="z-index:1;display:none;height:260px;width:260px">
		<div id="area_alarm_record_barChart" style="width:600px;height:400px;"></div>
	</div>

	<%-- 创建分区Dialog --%>
	<div id="createRegionDlg"
		style="z-index:1;display:none; font-size:12px;">
		<form id="ff" method="post">
			<div id="p" title=""
				style="width:290px;min-height:400px;height:auto;padding:10px;">
				<p style="margin-top:10px; margin-left:10px;">
					分区名：<input class="easyui-validatebox" type="text" id="regionName"
						name="model.regionName" data-options="required:true" />
				</p>
				<p style="margin-top:10px; margin-left:10px;">
					选择父区：<label id="choiceName"></label>&nbsp;&nbsp;
					<button onclick="choiceParentRegion()">选择</button>
				</p>
				<p style="margin-top:10px; margin-left:10px;">
					分区的经纬度集合：<input class="easyui-validatebox" type="text"
						id="regionArea" name="model.regionArea"
						data-options="required:true" />&nbsp;
					<button onclick="choiceRegionArea()">选择</button>
				</p>
				<p style="margin-top:10px; margin-left:10px;">选择设备：</p>
				<p style="margin-top:10px; margin-left:10px;">
				<table id="choiceDeviceTbl" class="easyui-datagrid"
					data-options="singleSelect:false, fitColumns:true, height:230">
					<thead>
						<tr>
							<th data-options="field:'devName',width:180">设备名</th>
						</tr>
					</thead>
				</table>
				</p>
				<p style="margin-top:10px; margin-left:10px; text-align:right">
					<button onclick="confirmCreateOrUpdateRegion()">
						<label id="labelBtn">创建<label>
					</button>
				</p>
				<input type="hidden" id="parentRegionId"
					name="model._parentRegionId" /> <input type="hidden"
					id="deviceIds" name="model.deviceIds" /> <input type="hidden"
					id="operationType" value="0" />
				<%--0:创建分区 1:修改分区 --%>
				<input type="hidden" id="regionId" name="model.regionId" />
				<%-- 分区ID(用于修改分区信息) --%>
			</div>
		</form>
	</div>

	<%-- 选择父分区Dialog --%>
	<div id="choiceParentRegionDlg"
		style="z-index:1;display:none; font-size:12px;">
		<div title=""
			style="width:290px;min-height:400px;height:auto;padding:10px;background:#fafafa;"
			data-options="iconCls:'icon-search', closable:false, collapsible:true,minimizable:true,maximizable:true">
			<table id="choiceParentRegionTbl" class="easyui-datagrid"
				data-options="singleSelect:true, idField:'id', fitColumns:true, height:230">
				<thead>
					<tr>
						<th data-options="field:'regionName',width:180">分区名</th>
					</tr>
				</thead>
			</table>
			<button onclick="confirmChoicePRegion()">选择</button>
		</div>
	</div>

	<%-- 分区查询Dialog --%>
	<div id="queryRegionDlg" style="z-index:1;display:none;">
		<div title=""
			style="width:290px;min-height:50px;height:auto;padding:10px;background:#fafafa;"
			data-options="iconCls:'icon-search', closable:false, collapsible:true,minimizable:true,maximizable:true">
			分区名:<input name="queryRegionName" id="queryRegionName" />
			&nbsp;&nbsp;
			<button onclick="queryRegion()">查询</button>
		</div>
	</div>

	<%-- 查看设备实时数据Dialog --%>
	<div id="viewDeviceDataDlg" style="z-index:1;display:none;">
		<div title=""
			style="width:890px;min-height:480px;height:auto;padding:10px;background:#fafafa;"
			data-options="iconCls:'icon-search', closable:false, collapsible:true,minimizable:true,maximizable:true">
			<div>
				<table id="deviceRealTimeTbl" class="easyui-datagrid">				
				</table>
				<table id="deviceConfigTbl" class="easyui-datagrid">
				</table>
			</div>
			<div style="margin-top:25px">
				<div id="justGage" style="float:left; ">
					<label style="font-size:12px;">距离刷新时间还剩：<span id="countdown"></span></label>
					<div id="flowJustGage" style="width:150px; height:100px; "></div>
					<div id="pressJustGage" style="width:150px; height:100px;"></div>
					<div id="noiseJustGage" style="width:150px; height:100px;"></div>
				</div>
				<div id="historyDataJqplot" style="width:690px;height:360px;"></div>
			</div>
		</div>
	</div>

	<%-- 配置设置Dialog --%> 
	<div id="paramConfigDlg" style="font-size: 12px;padding: 3px 3px 3px 3px;z-index:1;display:none;">
		<form id="config_add" method="post">
				<table >
				 	<tr><td colspan="4" >流量传感器</td></tr>
					<tr>
						<td>采集间隔:</td>
						<td><input id="sensor-flow-cjjg-edit" name="flow_cjjg" validType="intOrFloat" class="easyui-validatebox" required="true" value="0"/></td>
						<td>发送次数:</td>
						<td><input id="sensor-flow-fscs-edit" name="flow_fscs" validType="intOrFloat" class="easyui-validatebox" required="true" value="0"/></td>
					</tr>
					<tr><td colspan="4" >噪声传感器</td></tr>
					<tr>
						<td>密集开始时间:</td>
						<td><input class="easyui-timespinner" id="sensor-noise-mjkssj-edit" name="noise_mjkssj" class="easyui-validatebox" required="true" value="00:00"/></td>
						<td>密集间隔:</td>
						<td><input id="sensor-noise-mjjg-edit" name="noise_mjjg" validType="intOrFloat" class="easyui-validatebox" required="true" value="0"/></td>
					</tr>
					<tr>
						<td>密集样本数:</td>
						<td><input id="sensor-noise-mjybs-edit" name="noise_mjybs" validType="intOrFloat" class="easyui-validatebox" required="true" value="0"/></td>
						<td>松散开始时间:</td>
						<td><input class="easyui-timespinner" id="sensor-noise-sskssj-edit" name="noise_sskssj" class="easyui-validatebox" required="true" value="00:00"/></td>
					</tr>
					<tr>
						<td>松散间隔:</td>
						<td><input id="sensor-noise-ssjg-edit" name="noise_ssjg" validType="intOrFloat" class="easyui-validatebox" required="true"value="0"/></td>
						<td>松散样本数:</td>
						<td><input id="sensor-noise-ssybs-edit" name="noise_ssybs" validType="intOrFloat" class="easyui-validatebox" required="true" value="0"/></td>
					</tr>
					<tr>
						<td>无线开启时间:</td>
						<td><input class="easyui-timespinner" id="sensor-noise-wxkqsj-edit" name="noise_wxkqsj" class="easyui-validatebox" required="true" value="00:00"/></td>
						<td>无线关闭时间:</td>
						<td><input class="easyui-timespinner" id="sensor-noise-wxgbsj-edit" name="noise_wxgbsj" class="easyui-validatebox" required="true" value="00:00"/></td>
					</tr>
					<tr><td colspan="4" >压力传感器</td></tr>
					<tr>
						<td>采集间隔:</td>
						<td><input id="sensor-press-cjjg-edit" name="press_cjjg" validType="intOrFloat" type="text" class="easyui-validatebox" required="true" value="0"/></td>
						<td>发送次数:</td>
						<td><input id="sensor-press-fscs-edit" name="press_fscs" validType="intOrFloat" type="text" class="easyui-validatebox" required="true" value="0"/></td>
					</tr> 
					<tr>
						<td colspan="4" style="text-align: right;padding: 5px 5px;"> 
					 
						<a id="do_submitConfig_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a>  </td>
					</tr> 
				</table>
			</form> 
	</div>


	<%-- 液位监测仪-数据Dialog wei --%>
	<div id="leakLiquidDlg" style="z-index:1;display:none;">
		<div title=""
			style="width:250px;min-height:280px;height:auto;padding:10px;background:#fafafa;"
			data-options="iconCls:'icon-search', closable:false, collapsible:true,minimizable:true,maximizable:true">
			<table id="leakLiquidInfo" class="easyui-datagrid"></table>
		</div>
	</div>

	<%--液位监测仪 编号查询 (wei)--%>
	<div id="codeSearch-query-dialog"
		style="z-index:1;display:none;height:260px;width:260px">
		<div style="margin-top:15px;">
			<span><font style="font-size:13px;">液位传感器编号: </font><input
				type="text" id="liquidqueryKeyText" /></span> <span><a href="#"
				iconCls="icon-search" class="easyui-linkbutton"
				onclick="liquidkeyQuery()">查询</a></span>
		</div>
		<div style="margin-top:10px;"></div>
	</div>

	<%--报警弹出框 --%>
	<!-- 刘鑫报警
	<div id="alarmPopDlg" style="z-index:1;display:none; font-size:12px;width:506px;height:165px;">
	    <div style="float:left; width:131px;" id="alert-icon">
	        <img style="width: 131px; height:131px" src="${ctx}/images/alert-red.png"/>
	    </div>
	    <div style="height:307px;">
	        <table id="alarm-datagrid-tbl" class="easyui-datagrid">
	        		<thead>
						<tr>
							<th data-options="field:'propName',width:99"></th>
							<th data-options="field:'propValue',width:250"></th>
						</tr>
					</thead>
	        </table>
	        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-back'" style="float:right" onclick="jumpToAlarm()">飞到报警处</a>
	    </div>
	    <input type="hidden" id="current-alarm-device-code"/>
	</div>
-->
<!--
	<script id="alarmTemplate" type="text/x-jquery-tmpl">
		<li>
			<table  class="easyui-datagrid">
				<thead>
					<tr>
						<th data-options="field:'propName',width:99"></th>
						<th data-options="field:'propValue',width:250"></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>设备类型 </td>
						<td>${deviceType}</td>
					</tr>
					<tr>
						<td>事故原因 </td>
						<td>${message}</td>
					</tr>
					<tr>
						<td>负责人 </td>
						<td>${dealPerson}</td>
					</tr>
					<tr>
						<td>事故状态 </td>
						<td>${messageStatus}</td>
					</tr>
				</tbody>
			·</table>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-back'" style="float:right" onclick="jumpToAlarm(${deviceId},$(deviceType))">飞到报警处</a>
		</li>
	</script>
	-->

	<div id="alarmPopDlg">
		<div id="conn">
			<ul id="alarmList">
			    <!--  
				<li>
				   <table class="easyui-datagrid" id="alarmDatagrid" data-options="width:268, height:90">
				       <thead>
				          <tr>
						      <th data-options="field:'propName',width:99"></th>
						      <th data-options="field:'propValue',width:99">bbb</th>
					      </tr>
				       </thead>
				       <tbody>
				          <tr>
					          <td>aa</td>
					          <td>bb</td>
					      </tr>
				      </tbody>
			       </table>
			       <a class="easyui-linkbutton" href="#" style="float:right" onclick="jumpToAlarm()">飞到设备</a>
			    </li>
			    -->
			</ul>
		</div>
	</div>
	

	
	
	<%-- 查看燃气管线浓度历史曲线 --%>
	<div id="viewGasStrengthHistoryDlg" style="z-index:1;display:none;">
		<div id="lineGasStrengthHistory" style="width:690px;height: 360px;"></div>
	</div>

	<%-- 查看振动历史曲线 --%>
	<div id="viewVibratingCurveDlg" style="z-index:1;display:none;">
		<div id="lineVibratingCurve" style="width:690px;height: 360px;"></div>
	</div>

	<%-- 查看振动历史曲线 --%>
	<div id="vibratingPositionPopDlg" style="z-index:1;display:none;">
		<div style="width:690px;height: 360px;">
			<table id="vibrating_alarm"></table>
		</div>
	</div>

	<%-- 导出报警记录报表 --%>
	<div id="export-report-dlg" class="easyui-dialog" style="width:600px;height:150px;padding:10px 20px;" closed="true">
		<!--  <div class="ftitle">导出报警信息报表</div>-->
		<table style="width: 540px; height:50px">
			<tr>
				<td>
					<label style="font-size:14px;">报警记录日期范围:</label>&nbsp;&nbsp;&nbsp;&nbsp;
					<input id="alarmBeginDate" type="text" class="easyui-datebox" required="required" /> 
					<label style="font-size:14px;">~</label> 
					<input id="alarmEndDate" type="text" class="easyui-datebox"	required="required" />
				</td>
			</tr>
		</table>
		<div id="export-dlg-buttons">
			<a id="export-ok" onclick="doExport()" class="easyui-linkbutton" iconCls="icon-ok">确定</a> 
			<a href="#" id="export-cancel" class="easyui-linkbutton" iconCls="icon-cancel"
				onclick="javascript:$('#export-report-dlg').dialog('close')">返回</a>
		</div>
	</div>

</div>
<input type="hidden" id="currentFeatureName" />
<div style="display:none;" id="fiberDevicePropName"></div>
<div style="display:none;" id="fiberDevicePropValue"></div>
<input type="hidden" id="deviceQuery"/>
<div id="alarmRecordExportHtml" style="display:none"></div> 
