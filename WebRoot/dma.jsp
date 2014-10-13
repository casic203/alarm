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
	width: 120px;
}
</style>
</head>
<body class="easyui-layout">
	<div id="forTest"></div>
	<div data-options="region:'west',title:'区域',split:true" style="width:300px;">
		<table id="dma_tree_grid" style="display:none" data-options="">
			<thead>
				<tr>
					<th data-options="field:'elementName', width:180">分区结构</th>
				</tr>
			</thead>
		</table>
	</div>
	<div data-options="region:'center',title:'维护'" style="padding:5px;background:#eee;text-align: center;">

		<!-- 分区 -->
		<div id="region_menu" class="easyui-menu" style="width: 50px;display: none;">
			<div data-options="iconCls:'icon-remove'">删除</div>
		</div>
		<table id="region_tbl" style="width: 100%;margin-bottom: 30px;">
		</table>
		<div id="region_add_dlg" style="padding: 20px 10px 0px 10px;text-align: center;">
			<form id="region_form">
				<table>
					<tr>
						<td>分区编码</td>
						<td><input id="region_no" name="no" type="text"></td>
						<td>分区名称</td>
						<td><input id="region_name" name="name" type="text" class="easyui-validatebox" data-options="required:true"></td>
					</tr>
					<tr>
						<td>用户数量</td>
						<td><input id="region_user_count" name="userCount" type="text" class="easyui-validatebox" data-options="validType:'number'"></td>
						<td>夜间正常用水量</td>
						<td><input id="region_normal_water" name="normalWater" type="text"></td>
					</tr>
					<tr>
						<td>管道总长度</td>
						<td><input id="region_pipe_leng" name="pipeLeng" type="text"></td>
						<td>户表后总长度</td>
						<td><input id="region_user_pipe_leng" name="userPipeLeng" type="text"></td>
					</tr>
					<tr>
						<td>管道连接总数</td>
						<td><input id="region_pipe_links" name="pipeLinks" type="text"></td>
						<td>ICF</td>
						<td><input id="region_icf" name="icf" type="text"></td>
					</tr>
					<tr>
						<td>漏损控制目标</td>
						<td><input id="region_leak_control_rate" name="leakControlRate" type="text"></td>
						<td>日售水量</td>
						<td><input id="region_sale_water" name="saleWater" type="text"></td>
					</tr>
					<tr>
						<td colspan="4" style="text-align: right;padding: 5px 5px;"><input id="region_parent_dma" name="bDataParent_DMA" type="hidden"> <a id="region_btn_ok" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a> <a id="region_btn_cancel" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="position_new_dlg" style="padding: 20px 10px 0px 10px;text-align: center;">
			<form id="position_new_form">
				<table>
					<tr>
						<td>监测点名称</td>
						<td><input id="new_position_name" name="name" type="text"></td>
						<td>经度</td>
						<td><input id="new_position_longitude" name="longitude" type="text"></td>
						<td>纬度</td>
						<td><input id="new_position_latitude" name="latitude" type="text"></td>
					</tr>
					<tr>
						<td>监测点类型</td>
						<td><input id="new_position_data_pos_type" name="bDataPosType" type="text"></td>
						<td>排序码</td>
						<td><input id="new_position_sort_code" name="sortCode" type="text"></td>
						<td>是否启用</td>
						<td><select id="new_position_is_use" class="easyui-combobox" name="isUse" style="width:120px;">
								<option value="1">是</option>
								<option value="0">否</option>
						</select></td>
					</tr>
					<tr>
						<td>流向</td>
						<td><select id="new_position_direction" name="direction" class="easyui-combobox" style="width:120px;">
								<option value="1">流入</option>
								<option value="-1">流出</option>
						</select></td>
						<td><input id="new_position_parent_dma" name="dmaID" type="hidden"></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="6" style="text-align: left;">备注</td>
					</tr>
					<tr>
						<td colspan="6"><textarea id="new_position_comment" name="comment" rows="5" style="width: 100%;"></textarea></td>
					</tr>
					<tr>
						<td colspan="6" style="text-align: right;padding: 5px 5px;">
							<a id="position_btn_new_ok" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a> 
							<a id="position_btn_new_cancel" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="position_add_dlg" style="padding: 20px 10px 0px 10px;text-align: center;">
			<form id="position_add_form">
				<table>
					<tr>
						<td>监测点</td><td><input id="add_position" name="model.ID" class="easyui-combobox" data-options="valueField:'id',textField:'name'"></td>
						<td>流向</td>
						<td>
							<input id="add_position_parent_dma" name="dmaID" type="hidden">
							<select id="add_position_direction" name="direction" class="easyui-combobox" style="width:120px;">
								<option value="1">流入</option>
								<option value="-1">流出</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="4" style="text-align: right;padding: 5px 5px;">
							<a id="position_btn_add_ok" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a> 
							<a id="position_btn_add_cancel" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a></td>
					</tr>
				</table>
			</form>
		</div>

		<!-- 监测点 -->
		<div id="pos_menu" class="easyui-menu" style="width: 50px;display: none;">
			<div data-options="iconCls:'icon-remove'">删除</div>
		</div>
		<table id="pos_tbl" style="width: 100%;">
		</table>
		<div id="eqt_add_dlg" style="padding: 20px 10px 0px 10px;text-align: center;">
			<form id="eqt_form" method="post">
				<table>
					<tr>
						<td>设备</td>
						<td><input id="eqt_device" name="model.id" type="text"></td>
						<td>设备类型</td>
						<td>
							<select id="eqt_type" name="sensorType" class="easyui-combobox" style="width:120px;">
								<option value="Data3A">供水流量</option>
								<option value="Data40">供水压力</option>
								<option value="Data02">供水噪声</option>
								<option value="Data50">排水液位</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>材质</td>
						<td>
							<select id="pipeMaterial" name="pipeMaterial" class="easyui-combobox" style="width:120px;">
								<option value="球墨铸铁">球墨铸铁</option>
								<option value="铸铁">铸铁</option>
								<option value="钢/镀锌">钢/镀锌</option>
								<option value="铜">铜</option>
								<option value="钢筋混凝土/水泥">钢筋混凝土/水泥</option>
								<option value="铅">铅</option>
								<option value="铅银/铜合金">铅银/铜合金</option>
								<option value="玻璃钢">玻璃钢</option>
								<option value="陶瓷">陶瓷</option>
								<option value="PVC">PVC</option>
								<option value="PE">PE</option>
							</select>
						</td>
						<td>管径</td>
						<td><input id="eqt_pipe_size" name="pipeSize" type="text"></td>
					</tr>
					<tr>
						<td>低值报警</td>
						<td><input id="eqt_low_instant_value" name="lowInstantValue" type="text"></td>
						<td>高值报警</td>
						<td><input id="eqt_high_instant_value" name="highInstantValue" type="text"></td>
					</tr>
					<tr>
						<td>起始累计流量</td>
						<td><input id="eqt_start_total_value" name="startTotalValue" type="text"></td>
						<td><input id="eqt_parent_pos" name="positionID" type="hidden"></td><td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="4" style="text-align: right;padding: 5px 5px;"><a id="eqt_btn_ok" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a> <a id="eqt_btn_cancel" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a></td>
					</tr>
				</table>
			</form>
		</div>

		<!-- 设备 -->
		<div id="eqt_menu" class="easyui-menu" style="width: 50px;display: none;">
			<div data-options="iconCls:'icon-remove'">移除</div>
		</div>
		<table id="eqt_tbl" style="width: 100%;">
		</table>
 
        <div class="easyui-window" id="dma-sale-mgr" title="分区售水量管理" style="width:600px;height:400px" data-options="closed:true">
             <table id="dma-sale-tbl"></table>
        </div>
    
        <div class="easyui-window" id="dma-sale-add" title="添加售水量" style="width:238px;height:201px" data-options="closed:true">
			<table>
			    <input type="hidden" id="dmaId" name="dmaId" />
				<tr>
					<td>开始日期</td>
					<td>
					    <input id="beginDate" type="text" class="easyui-datebox" required="required"/>
					</td>
				</tr>
				<tr>
					<td>结束日期</td>
					<td>
					    <input id="endDate" type="text" class="easyui-datebox" name="endDate" required="required"/>
					</td>
				</tr>
				<tr>
					<td>售水量</td>
					<td>
					    <input id="water" type="text" class="easyui-numberbox" name="water" data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td>无收益水量</td>
					<td>
					    <input id="noValueWater" type="text" class="easyui-numberbox" name="noValueWater" data-options="required:true"/>
					</td>
				</tr>
				<tr>
					 <td><a href="#" id="sale_water_add_ok" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a></td>
					 <td><a href="#" id="sale_water_add_cancel" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a></td>
				</tr>
			</table>
		</div>
    
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/s/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script src="${ctx}/s/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/s/alarm/dma.js"></script>
