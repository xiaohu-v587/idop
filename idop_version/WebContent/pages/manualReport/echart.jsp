<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body style="height:100%;width:100%;margin:0">
		<a style="position:fixed;left:30px;top:15px;width:50px;height:20px;background-color:#53e65f;color:#FFF;display:inline-block;vertical-align:middle;text-align:center">已完成</a>
		<a style="position:fixed;left:100px;top:15px;width:50px;height:20px;background-color:#e02626;color:#FFF;display:inline-block;vertical-align:middle;text-align:center">未完成</a>
		<div style="position:fixed;left:40%;bottom:10px;z-index:9">
			<a class="nui-button" id="remindBtn" iconCls="" onclick="remind()" >催办</a>
			<span class="separator"></span>
			<a class="nui-button" iconCls="" onclick="onCancel()">取消</a>
		</div>
		<div id="container" style="width:100%;height:100%"></div>
		<input id="rid" class="nui-hidden"/>
		<script src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js" type="text/javascript"></script>
		<script type="text/javascript">
			var dom = document.getElementById("container");	
			var myChart = echarts.init(dom);
			
			option = null;
			myChart.showLoading();
			
			function setData(data){
				data = $G.clone(data);
				$G.get("rid").setValue(data.id);
				if(data.status == 3){
					$G.get("remindBtn").disable();
				}
				$.get("<%=root%>/manualReport/getEchart?id=" + data.id,function(data){
					myChart.hideLoading();
					data = tree(data, null)[0];
					myChart.setOption(option={
						tooltip:{
							triggerOn:'mousemove',
							enterable:true,
							formatter:'联系方式：{c}'
						},
						series:[
						    {
						    	name:'状态图',
						    	type:'tree',
						    	data:[data],
						    	top:'0%',
						    	left:'10%',
						    	bottom:'5%',
						    	right:'15%',
						    	symbolSize:12,
								initialTreeDepth:2,
								itemStyle:{color:"#0f193e",borderColor:'#e02626',borderWidth:3},
						    	label:{
						    		normal:{position:'left',verticalAlign:'middle',align:'right',fontSize:12,color:'#000'}
						    	},
						    	leaves:{
						    		label:{position:'right',verticalAlign:'middle',align:'left'}
						    	},
						    	expandAndCollapse:true,
						    	animationDuration:550,
						    	animationDurationUpdate:100
						    }
						]
					});
				});
				if(option && typeof option === 'object'){
					myChart.setOption(option,true);
				}
				
			}
			
			window.onresize = myChart.resize;
			
			//催办
			function remind(){
				$G.GcdsConfirm("确定催办所有已领取但未完成此任务的人员吗？", "提示", function(action) {
					if (action == 'ok') {
		            	var ajaxConf = new GcdsAjaxConf();
		            	ajaxConf.setSuccessFunc(function (data){
		            		
		                });
		            	$G.postByAjax({"rid":$G.get("rid").getValue()}, "<%=root%>/manualReport/remind", ajaxConf);
		          	}
		        });
			}
			
			function tree(data, pid){
				var result = [], temp;
				for(var i=0;i<data.length;i++){
					if(data[i].parent_id == pid){
						var obj = {name:data[i].name,value:data[i].value,itemStyle:data[i].status==1?{borderColor:"#53e65f"}:null};
						temp = tree(data, data[i].id);
						if(temp.length > 0){
							obj.children = temp;
						}
						result.push(obj);
					}
				}
				return result;
			}
			
			function onCancel(e) {
				$G.closemodaldialog("cancel");
		    }
		</script>
	</body>
</html>
