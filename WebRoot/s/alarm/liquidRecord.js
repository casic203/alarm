
//
function fillStat(){	
	var upTime= $('#upTime').datetimebox('getValue');
	var upTimes = upTime.split("/");
	
	var endTime= $('#endTime').datetimebox('getValue');
	var endTimes = endTime.split("/");
	
	var endTimeall= endTimes[2]*10000+endTimes[1]*100+endTimes[0];
	var upTimeall= upTimes[2]*10000+upTimes[1]*100+upTimes[0];

	if(upTimeall>endTimeall){
			alert("输入的统计时间不正确！");  
	   }
	  else
	  {
		  window.parent.fillStatB(upTime,endTime);
	  }
}
function fillAnaly(){
	var upTime= $('#upTime').datetimebox('getValue');
	var upTimes = upTime.split("/");
	
	var endTime= $('#endTime').datetimebox('getValue');
	var endTimes = endTime.split("/");
	
	var timesMore = $('#upYear').combobox('getValue');
	//alert(timesMore);
	
	var endTimeall= endTimes[2]*10000+endTimes[1]*100+endTimes[0];
	var upTimeall= upTimes[2]*10000+upTimes[1]*100+upTimes[0];

	if(upTimeall>endTimeall){
			alert("输入的分析时间不正确！");  
	   }
	  else
	  {
		  window.parent.fillAnalyB(upTime,endTime,timesMore);
	  }
	
}