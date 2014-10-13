$(document).ready(function(){
	$("#dg").datagrid({
		title:"接警人",
		pageSize:20,
		url:"accept-person!query.do",
		fit:true,
		pagePosition:"top",
		pageList:[5,10,20,30,40,50],
		toolbar:"#toolbar",
		pagination:true,
		rownumbers:true,
		fitColumns:true,
		singleSelect:true,
		idField:"id",
		columns:[[
		          {field:'personCode',title:'编号'},
		          {field:'personName',title:'姓名'},
		          {field:'active',title:'状态'}
		          ]]
	});	
	
	$('#dlg').dialog({    
			title:'My Dialog',    
			width:400,    
			height:200,    
			closed:false,    
			cache:false,    
			modal:true,
			buttons:"#dlg-buttons"
	});
	
});


var url;

function newUser(){
	url="accept-person!save.do";
	$('#dlg').dialog('open').dialog('新增','新增');
	alert($(document).scollTop()+($(window).height()-280)*0.5));
	$('#dlg').panel("move",{top:$(document).scollTop()+($(window).height()-280)*0.5)});
	$('#fm').form('clear');
}
function editUser() {
	var row = $('#info_table').datagrid('getSelected');
	if (row) {
		url = 'accept-person!edit.do?id=' + row.id;
		$('#dlg').dialog('open').dialog('setTitle', 'Edit User');
		$('#fm').form('load', row);
	}
}
function saveUser() {
	$('#fm').form('submit', {
		url : url,			
		onSubmit : function() {
			return $(this).form('validate');
		},
		success : function(result) {
			var result = eval('(' + result + ')');
			$('#fm').form('clear');
			$('#dlg').dialog('close');		// close the dialog
			$('#info_table').datagrid('reload');	// reload the user data
		}
	});
}
function removeUser() {
	var row = $('#info_table').datagrid('getSelected');
	if (row) {
		$.messager.confirm('Confirm',
				'您确定删除吗?', function(r) {
					if (r) {
						$.post('accept-person!delete.do', {
							id : row.id
						}, function(result) {
							$('#dlg').dialog('close');		// close the dialog
							$('#info_table').datagrid('reload');	// reload the user data
						}, 'json');
					}
				});
	}
}