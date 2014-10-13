<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}//s/jquery-easyui-1.3.2/themes/frm.css" />

<style type="text/css">
table,td,tr {
	font-size: 12px;
}

td {
	padding: 3px 3px 3px 3px;
}

input {
	width: 140px;
}
</style>
</head>
<body style="text-align: center;">
	<table id="dg"></table>
	
	<div id="region_dialog" style="display: none;">
	
		<div id="dlg_add" class="easyui-dialog" title="新增设备" style="padding:10px 20px; width:500px;" closed="true">
			<div class="ftitle">设备信息</div>
			<form id="fm_add" method="post">
				<table>
					<tr>
						<td>编号</td>
						<td><input id="dev-code-add" name="devCode" class="easyui-validatebox" required="true"></td>
						<td>编码</td>
						<td><input id="dev-no-add" name="no" class="easyui-validatebox" required="true"></td>
					</tr>
					<tr>
						<td>名称</td>
						<td><input id="dev-name-add" name="devName" class="easyui-validatebox" required="true"></td>
						<td>经度</td>
						<td><input id="dev-longtitude-add" name="longtitude" class="easyui-validatebox" validType="intOrFloat" required="true"></td>
					</tr>
					<tr>
						<td>纬度</td>
						<td><input id="dev-latitude-add" name="latitude" class="easyui-validatebox" validType="intOrFloat" required="true"></td>
						<td>高度</td>
						<td><input id="dev-height-add" name="height" class="easyui-validatebox" validType="intOrFloat" required="true"></td>
					</tr>
					<tr>
						<td>类型</td>
						<td><input id="dev-type-add" name="deviceType.id" required="true" editable="false"></td>
						<td>工厂</td>
						<td><input id="dev_factory_add" name="factory"></td>
					</tr>
					<tr>
						<td>出厂日期</td>
						<td><input id="dev-out-date-add" name="outDate" class="easyui-datebox" editable="false"></td>
						<td>安装日期</td>
						<td><input id="dev-install-date-add" name="installDate" class="easyui-datebox" editable="false"></td>
					</tr>
					<tr>
						<td>负责人</td>
						<td><input id="dev-accept-person-add" name="acceptPerson.id" editable="false"></td>
						<td>安装位置</td>
						<td><input id="dev_data_position_add" name="model.installPosition"></td>
					</tr>
					<tr>
						<td>SIM卡号</td>
						<td><input id="dev_sim_id_add" name="simid" class="easyui-validatebox" validType="mobile"></td>
						<td>开始使用时间</td>
						<td><input id="dev_begin_use_time_add" name="model.beginUseTime" class="easyui-datebox" editable="false"></td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg_edit" class="easyui-dialog" title="修改设备" style="padding:10px 20px; width:500px;" closed="true">
			<div class="ftitle">设备信息</div>
			<br>
			<form id="fm_edit" method="post">
				<table>
					<tr>
						<td>编号</td>
						<td><input id="dev-code-edt" name="devCode" class="easyui-validatebox" required="true"></td>
						<td>编码</td>
						<td><input id="dev-no-edt" name="no" class="easyui-validatebox" required="true"></td>
					</tr>
					<tr>
						<td>名称</td>
						<td><input id="dev-name-edt" name="devName" class="easyui-validatebox" required="true"></td>
						<td>经度</td>
						<td><input id="dev-longtitude-edt" name="longtitude" class="easyui-validatebox" validType="intOrFloat" required="true"></td>
					</tr>
					<tr>
						<td>纬度</td>
						<td><input id="dev-latitude-edt" name="latitude"
							class="easyui-validatebox" validType="intOrFloat" required="true"></td>
						<td>高度</td>
						<td><input id="dev-height-edt" name="height"
							class="easyui-validatebox" validType="intOrFloat" required="true"></td>
					</tr>
					<tr>
						<td>类型</td>
						<td><input id="dev-type-edt" name="typeId" required="true" editable="false"></td>
						<td>工厂</td>
						<td><input id="dev_factory_edt" name="factory"></td>
					</tr>
					<tr>
						<td>出厂日期</td>
						<td><input id="dev-out-date-edt" name="outDate" class="easyui-datebox" editable="false"></td>
						<td>安装日期</td>
						<td><input id="dev-install-date-edt" name="installDate" class="easyui-datebox" editable="false"></td>
					</tr>
					<tr>
						<td>负责人</td>
						<td><input id="dev-accept-person-edt" name="personId" editable="false"></td>
						<td>安装位置</td>
						<td><input id="dev_data_position_edt" name="installPosition"></td>
					</tr>
					<tr>
						<td>SIM卡号</td>
						<td><input id="dev_sim_id_edt" name="simid"  class="easyui-validatebox" validType="mobile"></td>
						<td>开始使用时间</td>
						<td><input id="dev_begin_use_time_edt" name="beginUseTime" class="easyui-datebox"  editable="false"></td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg_query" class="easyui-dialog" title="查询设备" style="padding:10px 20px; width:300px;" closed="true">
			<div class="ftitle">设备信息</div>
			<br>
			<form id="fm_query" method="post" novalidate>
				<div class="fitem">
					<label>编号:</label> <input id="dev-code-query" name="devCode" style="width:140px;" />
				</div>
				<div class="fitem">
					<label>名称:</label> <input id="dev-name-query" name="devName" style="width:140px;" />
				</div>
				<div class="fitem">
					<label>类型:</label> <input id="dev-type-query" name="deviceType.id"  editable="false"/>
				</div>
				<div class="fitem">
					<label>负责人:</label> <input id="dev-accept-person-query" name="acceptPerson.id" editable="false"/>
				</div>
			</form>
		</div>
		
	</div>
</body>
</html>

<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/device.js"></script>