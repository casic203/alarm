<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/s/jquery-easyui-1.3.2/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/s/jquery-easyui-1.3.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}//s/jquery-easyui-1.3.2/themes/frm.css" />
</head>
<body style="text-align: center;">

	<table id="dg"></table>

	<div id="region_dialog" style="display:none;">
	
		<div id="dlg_add" title="新增接警人" class="easyui-dialog" style="padding:20;width: 300px;" data-options="border : true,closed : true">
			<div class="ftitle">接警人信息</div>
			<br>
			<form id="fm_add" method="post">
				<div class="fitem">
					<label>账号:</label> <input id="personCode_add" name="personCode"
						class="easyui-validatebox" validType="account[3,20]" required="true" />
				</div>
				<div class="fitem">
					<label>姓名:</label> <input id="personName_add" name="personName"
						class="easyui-validatebox" validType="CHS" required="true" />
				</div>
				<div class="fitem">
					<label>电话:</label> <input id="telPhone_add" name="telePhone"
						class="easyui-validatebox" validType="mobile" />
				</div>
				<div class="fitem">
					<label>邮件:</label> <input id="email_add" name="email" class="easyui-validatebox"
						invalidMessage='邮箱格式不正确' validtype="email" />
				</div>
			</form>
		</div>


		<div id="dlg_edit" title="编辑接警人" class="easyui-dialog" style="padding:20;width: 300px;"
			data-options="border : true,closed : true">
			<div class="ftitle">接警人信息</div>
			<br>
			<form id="fm_edit" method="post">
				<div class="fitem">
					<label>账号:</label> <input id="personCode_edit" name="personCode"
						class="easyui-validatebox" validType="account[3,20]" required="true" />
				</div>
				<div class="fitem">
					<label>姓名:</label> <input id="personName_edit" name="personName"
						class="easyui-validatebox" validType="CHS" required="true" />
				</div>
				<div class="fitem">
					<label>电话:</label> <input id="telPhone_add" name="telePhone"
						class="easyui-validatebox" validType="mobile" />
				</div>
				<div class="fitem">
					<label>邮件:</label> <input id="email_add" name="email" class="easyui-validatebox"
						invalidMessage='邮箱格式不正确' validtype="email" />
				</div>
			</form>
		</div>

		<div id="dlg_query" title="查询接警人" class="easyui-dialog" style="padding:20;width: 300px;"
			data-options="border : true,closed : true">
			<div class="ftitle">接警人信息</div>
			<br>
			<form id="fm_query" method="post">
				<div class="fitem">
					<label>账号:</label> <input id="personCode_query" name="personCode" />
				</div>
				<div class="fitem">
					<label>姓名:</label> <input id="personName_query" name="personName" />
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
<script type="text/javascript" src="${ctx}/s/alarm/accept-person.js"></script>
