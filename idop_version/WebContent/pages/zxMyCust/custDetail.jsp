<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>客户详情</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<script src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js" type="text/javascript"></script>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="rpt6" style="height:500px;width:100%;background:#d0e1fc;"></div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	
	$(function(){
		canvas4();
	});
	
	function canvas4() {
		  var myChart = echarts.init(document.getElementById("rpt6"));
		  var option = {
		    title: {
		    },
		    tooltip: {
		      trigger: 'axis'
		    },
		    polar: [{
		    	name: {show : true},
		    	indicator : [
		    	             {text:'属性1',max:100},
		    	             {text:'属性2',max:100},
		    	             {text:'属性3',max:100},
		    	             {text:'属性4',max:100},
		    	             {text:'属性5',max:100},
		    	             {text:'属性5',max:100}
		    	             ],
		    	center: ['50%', '50%'],
		    	radius: 200
		    }],
		    
		    //数值序列
		    series: [{
		      name: '客户详情',
		      type: 'radar',
		      avoidLabelOverlap: false,
		      symbolSize: 5,
		      data: [{value:[100,80,80,80,80,80]}]
		    }]
	 	 };
	  		myChart.setOption(option);
		}
	
</script>

