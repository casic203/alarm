document.onreadystatechange = function() { // 当页面加载状态改变的时候执行这个方法
	if (document.readyState == "complete") { // 当页面加载完成之后执行
		$("#dg").datagrid(dg_config);
	}
}

var col = [ [ {
	field : 'name',
	title : '项目名称',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'max',
	title : '上限值',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'min',
	title : '下限值',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'current',
	title : '监控值',
	width : document.body.offsetWidth * 0.2
}, {
	field : 'status',
	title : '状态',
	width : document.body.offsetWidth * 0.1,
	styler: function(value,row,index){
				if (value == '异常'){
					return 'color:red;';
				} 
				if (value == '正常'){
					return 'color:green;';
				}
			}
} ] ];

var dg_config = {
	title : "监控规则信息",
	columns : col,
	fit : true,
	pagePosition : "top",
	pagination : true,
	rownumbers : true,
	singleSelect : true,
	pageList : [ 10, 20, 30, 40, 50 ],
	pageSize : 10,
	idField : "id",
	url : "../../alarm/data-base-monitor-rule!getResult.do"
};