<%@ page language="java" pageEncoding="UTF-8"%>
<div id="deviceDisplayDlg" style="display:none">

<!--污水有害气体-->
<div id="szWSGasDisplayDlg" style="z-index:1;display:none;">
	<div title=""
			style="width:890px;min-height:480px;height:auto;padding:10px;background:#fafafa;"
			data-options="iconCls:'icon-search', closable:false, collapsible:true,minimizable:true,maximizable:true">
			<div>
				<table id="szWSGasDeviceInfoTbl" class="easyui-datagrid">
				</table>
			</div>
			<div style="margin-top:25px">
				<div id="szWSGasHistoryData" style="float:left; ">
					<label style="font-size:12px;">距离刷新时间还剩：<span
						id="countdown"></span></label>
					<!--污水有害气体历史曲线（氧气、CO,H2S,可燃气体）-->
				<div id="szWSGasRecord" style="width:690px;height:360px;"></div>
				</div>
			</div>
	</div>
</div>

<!--渗漏预警仪-->
<div id="adSLDeviceDisplayDlg" style="z-index:1;display:none;">
	<div title=""
			style="width:890px;min-height:480px;height:auto;padding:10px;background:#fafafa;"
			data-options="iconCls:'icon-search', closable:false, collapsible:true,minimizable:true,maximizable:true">
			<div>
				<table id="adSLDeviceInfoTbl" class="easyui-datagrid">
				</table>
			</div>
			<div style="margin-top:25px">
				<div id="adSLRealTimeData" style="float:left; ">
					<label style="font-size:12px;">距离刷新时间还剩：<span
						id="countdown"></span></label>
					<!--电池电量-->
				<div id="cell" style="width:690px;height:360px;"></div>
				</div>
					<!--噪声曲线-->
				<div id="adSLNoiseRecord" style="width:690px;height:360px;"></div>
			</div>
	</div>
</div>

<!--多功能漏损监测仪-->
<div id="adDSDeviceDisplayDlg" style="z-index:1;display:none;">
	<div title=""
			style="width:890px;min-height:480px;height:auto;padding:10px;background:#fafafa;"
			data-options="iconCls:'icon-search', closable:false, collapsible:true,minimizable:true,maximizable:true">
			<div>
				<table id="adDSDeviceInfoTbl" class="easyui-datagrid">
				</table>
			</div>
			<div style="margin-top:25px">
				<div id="adDSDeviceRealTimeData" style="float:left; ">
					<label style="font-size:12px;">距离刷新时间还剩：<span
						id="countdown"></span></label>
					<!--电池电量-->
				<div id="cell" style="width:690px;height:360px;"></div>
				</div>
					<!--噪声曲线、流量曲线、压力曲线-->
				<div id="adDSDeviceRecord" style="width:690px;height:360px;"></div>
			</div>
	</div>
</div>
</div>



