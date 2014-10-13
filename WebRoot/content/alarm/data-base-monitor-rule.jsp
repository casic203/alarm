<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/frm.css" />
</head>
<body style="text-align: center;">

	<table id="dg"></table>

	<div id="region_dialog" style="display:none;">
	
		<div id="dlg_add" title="新增监控规则" class="easyui-dialog" style="padding:20;width: 300px;"
			data-options="border : true,closed : true">
			<div class="ftitle">监控规则</div>
			<br>
			<form id="fm_add" method="post">
				<div class="fitem">
					<label>项目名称：</label> <input id="name_add" name="name" class="easyui-validatebox"
						data-options="required:true" />
				</div>
				<div class="fitem">
					<label>上限值：</label> <input id="max_add" name="max" class="easyui-validatebox"
						data-options="required:true" />
				</div>
				<div class="fitem">
					<label>下限值：</label> <input id="min_add" name="min" class="easyui-validatebox"
						data-options="required:true" />
				</div>
				<div class="fitem">
					<label>监控语句：</label>
					<textarea id="sql_add" name="sql" style="width: 100%;" rows="6"
						class="easyui-validatebox" data-options="required:true"></textarea>
				</div>
			</form>
		</div>


		<div id="dlg_edit" title="编辑监控规则" class="easyui-dialog" style="padding:20;width: 300px;"
			data-options="border : true,closed : true">
			<div class="ftitle">监控规则</div>
			<br>
			<form id="fm_edit" method="post">
				<div class="fitem">
					<label>项目名称:</label> <input id="name_edit" name="name" class="easyui-validatebox"
						data-options="required:true" />
				</div>
				<div class="fitem">
					<label>上限值:</label> <input id="max_edit" name="max" class="easyui-validatebox"
						data-options="required:true" />
				</div>
				<div class="fitem">
					<label>下限值:</label> <input id="min_edit" name="min" class="easyui-validatebox"
						data-options="required:true" />
				</div>
				<div class="fitem">
					<label>监控语句:</label>
					<textarea id="sql_edit" name="sql" class="easyui-validatebox"
						data-options="required:true"></textarea>
				</div>
			</form>
		</div>

		<div id="dlg_query" title="查询监控规则" class="easyui-dialog" style="padding:20;width: 300px;"
			data-options="border : true,closed : true">
			<div class="ftitle">查询信息</div>
			<br>
			<form id="fm_query" method="post">
				<div class="fitem">
					<label>项目名称:</label> <input id="name_query" name="name" />
				</div>
			</form>
		</div>

		<div id="dlg_set" title="设置" class="dlg_query" style="padding:20;width:330px;"
			data-options="border:true,closed:true">
			<form id="fm_set" method="post">
				<div class="fitem">
					<label>备份周期[Min]：</label><input id="cycle_set" name="backupCycle.cycle"
						class="easyui-validatebox" data-options="required:true" />
				</div>
				<div class="fitem">
					<label>批处理文件路径：</label><input id="path_set" name="backupCycle.path"
						class="easyui-validatebox" data-options="required:true" />
				</div>
			</form>			
		</div>
		
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/s/alarm/data-base-monitor-rule.js"></script>
