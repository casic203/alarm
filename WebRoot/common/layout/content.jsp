<%@ page language="java" pageEncoding="UTF-8"%>
<div title="Home">
	<div id="arlam_record_layout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north'" id="homeMenu" style="display:none; height:45px;">
			<table style="width: 100%;">
				<tr>
					<td style="padding: 10px;">图层透明度：</td>
					<td>
						<div id="layer-slider" style="width:300px"></div>
					</td>
					<td>&nbsp;&nbsp;&nbsp;</td>
					<td>
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true"
							onclick="SetAction(0)">三维导航</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" 
							onclick="SetAction(2)">距离量算</a> 
						<a href="#"	class="easyui-linkbutton" iconCls="icon-search" plain="true" 
							onclick="SetAction(3)">面积量算</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true"
							onclick="SetAction(4)">高度测量</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" 
							onclick="ClearMeasure()">清除量算</a> 
						<a href="#"	class="easyui-linkbutton" iconCls="icon-search" plain="true" 
							onclick="SetAction(5)">	选中对象</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true"
							onclick="globalLayerControl.setAllLayersVisiable(true)"> 显示全部管线</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" id="centerArea">
			<div id="globalDiv" style="height:100%; width:100%"></div>
		</div>
		<div data-options="region:'east',title:'漏损分区信息',split:true, collapsed:true"
			style="width:250px;">
			<table id="leakRegionInfo" class="easyui-datagrid"></table>
		</div>
		<div data-options="region:'south',title:'输出结果', split:true, collapsed:true"
			style="height:240px">
			<div id="choice-area" style="width:1303px; height:220px;">
				<table id="choice-area-query-tbl"></table>
			</div>
		</div>
	</div>
</div>
</div>