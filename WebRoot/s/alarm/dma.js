document.onreadystatechange = function() { // 当页面加载状态改变的时候执行这个方法
	if (document.readyState == "complete") { // 当页面加载完成之后执行
		/**
		 * 添加子分区对话框
		 */
		$('#region_add_dlg').dialog({
			title : '子分区信息',
			width : 490,
			cache : false,
			modal : true,
			closed : true
		});
		$("#region_btn_ok").bind('click', function() {
			$.messager.progress();
			$('#region_form').form({
				url : "alarm/water-pipeline-sub-region-manage!addSubDMA.do",
				onSubmit : function() {
					var isValid = $(this).form('validate');
					if (!isValid) {
						$.messager.progress('close'); // 隐藏进度条虽然形式是无效的
					}
					return isValid; // 返回false,将阻止表单提交
				},
				success : function(data) {
					$.messager.progress('close'); // 隐藏进度条同时提交成功
					$("#region_add_dlg").dialog("close");
					$("#region_tbl").datagrid("reload");
					$("#dma_tree_grid").treegrid("reload").treegrid({
						onLoadSuccess:function(data) {
							var selectDmaId = $("#region_parent_dma").val();
							if(selectDmaId != null && typeof(selectDmaId) != 'undefined') {
							    $("#dma_tree_grid").treegrid("select", selectDmaId);
							}
						}
					});
				}
			}).submit();
		});
		$("#region_btn_cancel").bind('click', function() {
			$("#region_add_dlg").dialog("close");
		});

		/**
		 * 新建监测点
		 */
		$('#position_new_dlg').dialog({
			title : '监测点信息',
			width : 600,
			cache : false,
			modal : true,
			closed : true
		});
		$("#position_btn_new_ok").bind('click', function() {
			$('#position_new_form').form({
				url : "alarm/water-pipeline-position-manage!addPosition.do",
				onSubmit : function() {
					var isValid = $(this).form('validate');
					if (!isValid) {
						$.messager.progress('close'); // 隐藏进度条虽然形式是无效的
					}
					return isValid; // 返回false,将阻止表单提交
				},
				success : function(data) {
					var result = eval("(" + data + ")");
					if(result.success){
						$("#pos_tbl").datagrid("reload");
						//$("#dma_tree_grid").treegrid("reload");
						$("#dma_tree_grid").treegrid("reload").treegrid({
							onLoadSuccess:function(data) {
								var selectDmaId = $("#region_parent_dma").val();
								if(selectDmaId != null && typeof(selectDmaId) != 'undefined') {
								    $("#dma_tree_grid").treegrid("select", selectDmaId);
								}
							}
						});
					}
					$("#position_new_dlg").dialog("close");
					$.messager.alert('结果', result.msg);
				}
			}).submit();
		});
		$("#position_btn_new_cancel").bind('click', function() {
			$("#position_new_dlg").dialog("close");
		});
		
		
		/**
		 * 添加监测点
		 */
		$('#position_add_dlg').dialog({
			title : '监测点信息',
			width : 370,
			cache : false,
			modal : true,
			closed : true
		});
		$("#position_btn_add_ok").bind('click', function() {
			$('#position_add_form').form({
				url : "alarm/water-pipeline-position-manage!addExistsPosition.do",
				onSubmit : function() {
					var isValid = $(this).form('validate');
					if (!isValid) {
						$.messager.progress('close'); // 隐藏进度条虽然形式是无效的
					}
					return isValid; // 返回false,将阻止表单提交
				},
				success : function(data) {
					var result = eval("(" + data + ")");
					if(result.success){
						$("#pos_tbl").datagrid("reload");
						//$("#dma_tree_grid").treegrid("reload");
						$("#dma_tree_grid").treegrid("reload").treegrid({
							onLoadSuccess:function(data) {
								var selectDmaId = $("#region_parent_dma").val();
								if(selectDmaId != null && typeof(selectDmaId) != 'undefined') {
								    $("#dma_tree_grid").treegrid("select", selectDmaId);
								}
							}
						});
					}
					$("#position_add_dlg").dialog("close");
					$("#add_position").combobox("clear");
					$.messager.alert('结果', result.msg);
				}
			}).submit();
		});
		$("#position_btn_add_cancel").bind('click', function() {
			$("#position_add_dlg").dialog("close");
		});

		/**
		 * 添加设备
		 */
		$("#eqt_add_dlg").dialog({
			title : '设备信息',
			width : 450,
			cache : false,
			modal : true,
			closed : true
		});
		$("#eqt_btn_ok").bind('click', function() {
			$('#eqt_form').form({
				url : "alarm/water-pipeline-device-manage!addDevice.do",
				onSubmit : function() {
					var isValid = $(this).form('validate');
					if (!isValid) {
						$.messager.progress('close'); // 隐藏进度条虽然形式是无效的
					}
					return isValid; // 返回false,将阻止表单提交
				},
				success : function(data) {
					var result = eval("(" + data + ")");
					if(result.success){
						$("#eqt_tbl").datagrid("reload");
						//$("#dma_tree_grid").treegrid("reload");
					}
					$("#eqt_add_dlg").dialog("close");
					$.messager.alert('结果', result.msg);
				}
			}).submit();
		});
		$("#eqt_btn_cancel").bind('click', function() {
			$("#eqt_add_dlg").dialog("close");
		});

		/**
		 * 初始化子分区表、监测点表、设备表
		 */
		$("#region_tbl").datagrid(region_gd_cfg).datagrid("getPanel").panel("close");
		$("#pos_tbl").datagrid(position_gd_cfg).datagrid("getPanel").panel("close");
		$("#eqt_tbl").datagrid(eqt_gd_cfg).datagrid("getPanel").panel("close");

		
		$("#dma_tree_grid").treegrid({
							animate : true,
							fit : true,
							fitColumns : true,
							collapsible : true,
							treeField : 'elementName',
							toolbar : '#regionMenu',
							idField : 'id',
							singleSelect : true,
							checkOnSelect : false,
							selectOnCheck : false,
							onClickRow : function(row) {
								if (row.isRegion) {
									$("#region_parent_dma").val(row.regionID);
									$("#new_position_parent_dma").val(row.regionID);
									$("#add_position_parent_dma").val(row.regionID);
									$("#add_position").combobox("reload","alarm/water-pipeline-region-scan!queryPosition.do?model.regionID="+row.regionID);
									$("#region_tbl").datagrid({
										url : "alarm/water-pipeline-region-scan!querySubDMAByID.do?model.regionID=" + row.regionID
									}).datagrid("getPanel").panel("open");
									$("#pos_tbl").datagrid({
										url : "alarm/water-pipeline-region-scan!queryPositionInfoByID.do?model.regionID=" + row.regionID
									}).datagrid("getPanel").panel("open");
									$("#eqt_tbl").datagrid("getPanel").panel("close");
								}
								if (row.isPosition) {
									$("#eqt_parent_pos").val(row.positionID);
									$("#region_tbl").datagrid("getPanel").panel("close");
									$("#pos_tbl").datagrid("getPanel").panel("close");
									$("#eqt_tbl").datagrid({
										url : "alarm/water-pipeline-region-scan!queryEquipmentInfoByID.do?model.positionID=" + row.positionID
									}).datagrid("getPanel").panel("open");
								}
							}
						});
		
		$("#dma_tree_grid").treegrid({url : "alarm/water-pipeline-region-scan!getWaterPipelineRegionTreeData.do"});
		
		$('#eqt_device').combobox({ 
			url:'alarm/device!queryActive.do', 
			valueField:'id', 
			textField:'devName' 
		});
		
		$("#sale_water_add_ok").bind('click', function() {
			//$('#dma-sale-add-form').submit();
			$.ajax({
				type:'POST',
				url:"alarm/dma-sale-water-mgr!add.do",
				data:{'dmaId':$("#dmaId").val(),
					'beginDate':$("#beginDate").datebox("getValue"),
					'endDate':$("#endDate").datebox("getValue"),
					'water':$("#water").val(),
					'noValueWater':$("#noValueWater").val()
				},
				success:function(result) {
					var result = eval("(" + result + ")");
					if (result.success) {
						$("#dma-sale-tbl").datagrid("reload"); // reload the user data
					}
					$.messager.alert('结果', result.msg);
					$("#dma-sale-add").window("close");
				}
			});
		});
		
		/*
		$('#dma-sale-add-form').form({
			url : "alarm/dma-sale-water-mgr!add.do",
			onSubmit : function() {
				alert($('#sDate').datebox('getValue'));
				return $(this).form("validate");
			},
			success : function(result) {
				//alert(result);
				$("#forTest").html(result);
				var result = eval("(" + result + ")");
				if (result.success) {
					$("#dma-sale-tbl").datagrid("reload"); // reload the user data
				}
				$.messager.alert('结果', result.msg);
				$("#dma-sale-add").window("close");
			}
		});
		*/
		
		$("#sale_water_add_cancel").bind('click', function() {
			$("#dma-sale-add").window("close");
		});
	}
}

var regin_col = [ [ {
	field : 'name',
	title : '分区名'
}, {
	field : 'userCount',
	title : '用户数量'
}, {
	field : 'normalWater',
	title : '正常夜间用水量'
}, {
	field : 'pipeLeng',
	title : '管道总长度'
}, {
	field : 'userPipeLeng',
	title : '户表后管道总长度'
}, {
	field : 'pipeLinks',
	title : '管道连接总数'
}, {
	field : 'icf',
	title : 'ICF'
}, {
	field : 'leakControlRate',
	title : '漏损控制目标值'
} ] ];

var region_gd_cfg = {
	title : "子分区信息列表",
	columns : regin_col,
	pagePosition : "top",
	rownumbers : true,
	singleSelect : true,
	width : 1124,
	height : 250,
	idField : "id",
	collapsible : true,
	toolbar : [ {
		iconCls : "icon-add",
		text : "添加子分区",
		handler : function() {
			$("#region_add_dlg").dialog("open").dialog("center");
		}
	}, "-", {
		iconCls : 'icon-remove',
		text : "移除子分区",
		handler : function() {
			var row = $("#region_tbl").datagrid("getSelected");
			if (row) {
				var node=$("#dma_tree_grid").treegrid("getSelected");
				$.messager.confirm("Confirm","您确定删除吗?",function(r) {
					if (r) {
						$.post(
							"alarm/water-pipeline-sub-region-manage!delSubDMA.do",
							{'model.regionID' : row.id},
							function(result) {
								if(result.success){
									$("#region_tbl").datagrid("reload");
									//$("#dma_tree_grid").treegrid("reload").treegrid("select",node.id);
									$("#dma_tree_grid").treegrid("reload").treegrid({
										onLoadSuccess:function(data) {
											var selectDmaId = $("#region_parent_dma").val();
											if(selectDmaId != null && typeof(selectDmaId) != 'undefined') {
											    $("#dma_tree_grid").treegrid("select", selectDmaId);
											}
										}
									});
									
								}
								$.messager.alert('结果', result.msg);
							}, "json");
					}
				});
		  	}
		}
	}, "-", {
		iconCls : "icon-add",
		text : "分区售水量管理",
		handler : function() {
			var row = $("#region_tbl").datagrid("getSelected");
			if(row) {
				$("#dmaId").val(row.id);
				$("#dma-sale-mgr").window("open");
				$("#dma-sale-tbl").datagrid({
					title : "售水量信息列表",
					columns : sale_water_col,
					url:"alarm/dma-sale-water-view!getSaleWaterList.do?dmaId=" + row.id,
					pagePosition : "top",
					rownumbers : true,
					singleSelect : true,
					width : 598,
					height : 299,
					idField : "id",
					collapsible : true,
					toolbar:[{
						iconCls : "icon-add",
						text : "添加",
						handler : function() {
							$("#dma-sale-add").window("open");
						}
					},'-',{
						iconCls : "icon-remove",
						text : "删除",
						handler : function() {
							var row = $("#dma-sale-tbl").datagrid("getSelected");
							if(row) {
								if(confirm("删除售水量?")) {
									$.ajax({
										type : 'POST',
										url : 'alarm/dma-sale-water-mgr!delete.do?model.id=' + row.id,
										success : function(msg) {
											$("#dma-sale-tbl").datagrid("reload");
										}
									});
								}
							} else {
								alert('没有选择记录');
							}
						}
					}]
				});
			}
		}
	} ],
	onRowContextMenu : function(e, rowIndex, rowData) {
		e.preventDefault();
		$(this).datagrid("clearSelections");
		$(this).datagrid("selectRow", rowIndex);
		$("#region_menu").menu("show", {
			left : e.pageX,
			top : e.pageY
		});
	}
};

var sale_water_col = [[{
	 field:'startDate', 
	 title:'开始日期'
}, {
	 field:'endDate', 
	 title:'结束日期'
}, {
	 field:'saleWater', 
	 title:'售水量'
}, {
	 field:'noValueWater', 
	 title:'无收益水量'
}]]

var position_col = [ [ {
	field : 'name',
	title : '监测点名称'
}, {
	field : 'longitude',
	title : '经度'
}, {
	field : 'latitude',
	title : '纬度'
}, {
	field : 'comment',
	title : '备注'
} ] ];

var position_gd_cfg = {
	title : "监测点列表",
	columns : position_col,
	rownumbers : true,
	width : 1124,
	height : 250,
	singleSelect : true,
	pageList : [ 10, 20, 30, 40, 50 ],
	pageSize : 10,
	idField : "id",
	closed : true,
	toolbar : [ {
		iconCls : "icon-add",
		text : "新建监测点",
		handler : function() {
			$("#position_new_dlg").dialog("open");
		}
	},"-",{
		iconCls : "icon-add",
		text : "添加监测点",
		handler : function() {
			$("#position_add_dlg").dialog("open");
		}
	}, "-", {
		iconCls : 'icon-remove',
		text : "移除监测点",
		handler : function() {
			var row = $("#pos_tbl").datagrid("getSelected");
			if (row) {
				var node=$("#dma_tree_grid").treegrid("getSelected");
				$.messager.confirm("Confirm","您确定删除吗?",function(r) {
					if (r) {
						$.post(
							"alarm/water-pipeline-position-manage!deletePosition.do?model.ID="+row.id+"&dmaID="+node.regionID,
							function(result) {
								if(result.success){
									$("#pos_tbl").datagrid("reload");
									//$("#dma_tree_grid").treegrid("reload").treegrid("select",node.id);
									$("#dma_tree_grid").treegrid("reload").treegrid({
										onLoadSuccess:function(data) {
											var selectDmaId = $("#region_parent_dma").val();
											if(selectDmaId != null && typeof(selectDmaId) != 'undefined') {
											    $("#dma_tree_grid").treegrid("select", selectDmaId);
											}
										}
									});
								}
								$.messager.alert('结果', result.msg);								
							}, "json");
					}
				});
		  	}
		}
	} ],
	onRowContextMenu : function(e, rowIndex, rowData) {
		e.preventDefault();
		$(this).datagrid("clearSelections");
		$(this).datagrid("selectRow", rowIndex);
		$("#pos_menu").menu("show", {
			left : e.pageX,
			top : e.pageY
		});
	}
};

var eqt_col = [ [ {
	field : 'devName',
	title : '设备名称'
}, {
	field : 'typeName',
	title : '传感器类型'
}, {
	field : 'longtitude',
	title : '经度'
}, {
	field : 'latitude',
	title : '纬度'
}, {
	field : 'factory',
	title : '生产厂'
} ] ];
var eqt_gd_cfg = {
	title : "设备列表",
	columns : eqt_col,
	fit : true,
	pagePosition : "top",
	pagination : true,
	rownumbers : true,
	singleSelect : true,
	pageList : [ 10, 20, 30, 40, 50 ],
	pageSize : 10,
	idField : "id",
	closed : true,
	toolbar : [ {
		iconCls : "icon-add",
		text : "添加设备",
		handler : function() {
			$("#eqt_add_dlg").dialog("open");
		}
	}, "-", {
		iconCls : 'icon-remove',
		text : "移除设备",
		handler : function() {
			var row = $("#eqt_tbl").datagrid("getSelected");
			if (row) {
				$.messager.confirm("Confirm","您确定删除吗?",function(r) {
					var node=$("#dma_tree_grid").treegrid("getSelected");
					if (r) {
						$.post(
							"alarm/water-pipeline-device-manage!deleteDevice.do?model.id="+row.id+"&positionID="+node.positionID,
							function(result) {
								if(result.success){
									$("#eqt_tbl").datagrid("reload");
									//$("#dma_tree_grid").treegrid("reload").treegrid("select",node.id);
								}
								$.messager.alert('结果', result.msg);	
							}, "json");
					}
				});
		  	}
		}
	} ],
	onRowContextMenu : function(e, rowIndex, rowData) {
		e.preventDefault();
		$(this).datagrid("clearSelections");
		$(this).datagrid("selectRow", rowIndex);
		$("#eqt_menu").menu("show", {
			left : e.pageX,
			top : e.pageY
		});
	}
};
