<%@ page language="java" pageEncoding="UTF-8"%>

<style>
#menu-accordion li {
	height: 35px;
	width: 100%;
}

#menu-accordion li div {
	padding: 10px;
	cursor: pointer;
}

#menu-accordion li:HOVER {
	background-color: #F2F2F2;
}
</style>

<div id="menu-accordion" class="easyui-accordion" data-options="iconCls:'icon-help', fit:true">

<!--  
	<region:region-permission permission="设备分区管理:read" region="app.2">
		<div title="设备分区管理">
			<div style="height:100%; ">
				<table id="regionTbl" style="display:none" data-options="">
					<thead>
						<tr>
							<th data-options="field:'checked', checkbox:true, width:5"></th>
							<th data-options="field:'elementName', width:180">分区结构</th>
						</tr>
					</thead>
				</table>
				<div id="regionMenu">
					<a class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
						onclick="beginCreateRegion()">创建</a> <a class="easyui-linkbutton"
						data-options="iconCls:'icon-no'" onclick="beginDeleteRegion()">删除</a>
					<a class="easyui-linkbutton" data-options="iconCls:'icon-redo'"
						onclick="beginModifyRegion()">修改</a>
				</div>
			</div>
		</div>
	</region:region-permission>

	<region:region-permission permission="设备信息管理:read" region="app.2">
		<div title="设备信息管理">
			<div style="height:100%;">
				<table id="device_info_manager_tbl" style="display:none"
					data-options="">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</region:region-permission>

	<region:region-permission permission="报警信息管理:read" region="app.2">
		<div title="报警信息管理">
			<div style="height:100%;">
				<table id="alarm_info_manager_tbl">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</region:region-permission>

	<region:region-permission permission="工单管理:read" region="app.2">
		<div title="工单管理">
			<div style="height:100%;">
				<table id="work_sheet_manager_tbl">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</region:region-permission>

	<region:region-permission permission="管网查询统计:read" region="app.2">
		<div title="管网查询统计">
			<div style="height:100%;">
				<table id="pipe_search_tbl">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</region:region-permission>

	<region:region-permission permission="给水管线专项分析:read" region="app.2">
		<div title="给水管线专项分析">
			<div style="height:100%; ">
				<table id="waterPipeLineFunTbl">
					<thead>
						<tr>
							<th data-options="field:'name', width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</region:region-permission>

	<region:region-permission permission="燃气管线专项分析:read" region="app.2">
		<div title="燃气管线专项分析">
			<div style="height:100%; ">
				<table id="gasPipeTbl">
					<thead>
						<tr>
							<th data-options="field:'name', width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</region:region-permission>

	<region:region-permission permission="雨污水管线专项分析:read" region="app.2">
		<div title="雨污水管线专项分析">
			<div style="height:100%; ">
				<table id="rainPipeLineFunTbl" style="display:none" data-options="">
					<thead>
						<tr>
							<th data-options="field:'elementName', width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</region:region-permission>

	<region:region-permission permission="其余管线专项分析:read" region="app.2">
		<div title="其余管线专项分析">
			<div style="height:100%; ">
				<table id="otherPipeLineTbl">
					<thead>
						<tr>
							<th data-options="field:'name', width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</region:region-permission>

	<region:region-permission permission="系统设置:read" region="app.2">
		<div title="系统设置">
			<div style="height:100%;">
				<table id="database_manage_tbl" style="display:none">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</region:region-permission>
-->	

<!-- -->	
		<div title="设备分区管理">
			<div style="height:100%; ">
				<table id="regionTbl" style="display:none" data-options="">
					<thead>
						<tr>
							<th data-options="field:'checked', checkbox:true, width:5"></th>
							<th data-options="field:'elementName', width:180">分区结构</th>
						</tr>
					</thead>
				</table>
				<div id="regionMenu">
					<a class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
						onclick="beginCreateRegion()">创建</a> <a class="easyui-linkbutton"
						data-options="iconCls:'icon-no'" onclick="beginDeleteRegion()">删除</a>
					<a class="easyui-linkbutton" data-options="iconCls:'icon-redo'"
						onclick="beginModifyRegion()">修改</a>
				</div>
			</div>
		</div>

		<div title="设备信息管理">
			<div style="height:100%;">
				<table id="device_info_manager_tbl" style="display:none"
					data-options="">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

		<div title="报警信息管理">
			<div style="height:100%;">
				<table id="alarm_info_manager_tbl">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

		<div title="工单管理">
			<div style="height:100%;">
				<table id="work_sheet_manager_tbl">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

		<div title="管网查询统计">
			<div style="height:100%;">
				<table id="pipe_search_tbl">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

		<div title="给水管线专项分析">
			<div style="height:100%; ">
				<table id="waterPipeLineFunTbl">
					<thead>
						<tr>
							<th data-options="field:'name', width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

		<div title="燃气管线专项分析">
			<div style="height:100%; ">
				<table id="gasPipeTbl">
					<thead>
						<tr>
							<th data-options="field:'name', width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

		<div title="雨污水管线专项分析">
			<div style="height:100%; ">
				<table id="rainPipeLineFunTbl" style="display:none" data-options="">
					<thead>
						<tr>
							<th data-options="field:'elementName', width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

		<div title="其余管线专项分析">
			<div style="height:100%; ">
				<table id="otherPipeLineTbl">
					<thead>
						<tr>
							<th data-options="field:'name', width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

		<div title="系统设置">
			<div style="height:100%;">
				<table id="database_manage_tbl" style="display:none">
					<thead>
						<tr>
							<th data-options="field:'name',width:180">功能列表</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
 
</div>