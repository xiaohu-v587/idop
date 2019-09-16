<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
	<title>任务跟踪</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<%@include file="/common/nuires.jsp" %>
	<script src="<%=request.getContextPath()%>/resource/js/sPage/jquery.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/resource/echarts/jquery.circliful.min.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/echarts/jquery.circliful.css"/>
	<script src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js" type="text/javascript"></script>
</head>
<body>
	<div class="nui-toolbar" style="height:65px">
       	<div style="float: left;border:3px;width:20%;">
       		<!-- 机构视图不行，机构视图统计模型有问题，暂不开放，待找到合适的统计模型时开放 -->
       		<input id="searchType" name="searchType"  style="width: 120px;margin:20px 0px 0px 10px;width:50%"  class="nui-combobox" url="<%=root%>/param/getParam?key=yygl_mission_track" 
					valueField="val" textField="remark"  emptyText="请选择..."  value="1"  multiSelect="false" onvaluechanged="onSearchTypechanged" readOnly="true"/>
       	</div>
       	<div style="float: left;border:0px;width:50%;padding-top:15px;padding-left:4%;font-size:20px;" align="center">
       		${missionname }任务任务执行情况
       	</div>
       	<div style="float: right;border:3px;width:300px;height:96px;white-space:nowrap; " id="circul">
      	 </div>
	</div>
	<div class="nui-fit" style="width:100%;">
		<div id="echartbox" ></div>
	</div>
	<div id="menuBox" style="display: none;">
			<div><span class="menuTitle"></span></div>
			<!-- <ul>
				<li>菜单1</li>
				<li>菜单1</li>
				<li>菜单1</li>
				<li>菜单1</li>
			</ul> -->
		</div>
</body>
</html>
<script type="text/javascript">
/**
 * 初始化处理
 */
	$G.parse(); //将页面的标签转换为可$Gui可处理的对象
	onSearch();
	loadData();
	
	initDiv();
	
	function initDiv(){
		var container = document.getElementById('echartbox');
		var allNode=0;
		var height=window.innerHeight;
		var currentHeight=10*allNode;
		var newWidth=Math.max(currentHeight,height);
		container.style.width = window.innerWidth + 'px';
		container.style.height = newWidth + 'px';
	}
	
	function onSearchTypechanged(e){
		onSearch();
	}
	
	
	function loadData(){
		var	searchType = $G.get("searchType").getValue(); //查询方式
		$.ajax({
			type: "Post",
			async: true	,
			url: "<%=root%>/missionView/getTrackRate?id=${id}&searchType="+searchType+"&type=${type}",
			success: function(data) {
				console.log(data)
				$("#circul").html("");
				$("#circul").html(
						'<div id="myStat2" style="float:right;margin-right:20px;" data-dimension="65" data-text="'+data.finshRate+'%" data-info="" data-width="12" data-fontsize="10" data-percent="'+data.finshRate+'" data-fgcolor="#04B431" data-bgcolor="#eee"></div>'
						+
						'<div style="height:65px;float:right; "><div style="margin-top:45%;font-size:16px;">已反馈</div></div>'
						+
						'<div id="myStat1" style="float:right;margin-right:20px;" data-dimension="65" data-text="'+data.overRate+'%" data-info="" data-width="12" data-fontsize="10" data-percent="'+data.overRate+'" data-fgcolor="#FA5858" data-bgcolor="#eee"></div>'
						+
						'<div style="height:65px;float:right; "><div style="margin-top:45%;font-size:16px;">已逾期</div></div>'
				);
				$('#myStat1').circliful();
				$('#myStat2').circliful();
			},
			error: function(err) {
				$G.alert("查询失败");
			}
			});
	}
	
	function onSearch() {
		var	searchType = $G.get("searchType").getValue(); //查询方式
		getQueryData(searchType);
	}
	var myChart = echarts.init(document.getElementById('echartbox'));
	var trackOption ={
	        tooltip: {
	            trigger: 'item',
	            triggerOn: 'mousemove',
	            formatter: function(param, ticket, callback) 
	             {
	                var datas = '<p  style=" font-size:20px;">已反馈：'+param.data.feedbackcount+'人</p><p  style=" font-size:20px;">未反馈：'+param.data.notfeedback+'人</p><p  style=" font-size:20px;">任务进度：'+param.data.taskrate+'%</p>';
	                if(searchType == "1"){
	                	if(param.data.upid != "" && param.data.upid != null){
	                    	datas += '<p  style=" font-size:20px;">任务状态: '+param.data.user_mission_status+'</p>';
	                    } else{
	                    	datas += '<p  style=" font-size:20px;">任务状态: '+param.data.mission_issue_status+'</p>';
	                    }
	                }
	                
	                return datas;//内容还没返回时显示的内容
	              }
	        },
	        series:[
	            {
	                type: 'tree',
	                data: [],
	                verticalAlign: 'middle',
	                symbol: 'emptyCircle',
	                orient: 'vertical',
	                expandAndCollapse: true,
	                label: {
	                    normal: {
	                        position: 'left',
	                        rotate: -90,
	                        verticalAlign: 'middle',
	                        align: 'right',
	                        fontSize: 14
	                    }
	                },
	                leaves: {
	                    label: {
	                        normal: {
	                            position: 'right',
	                            rotate: -90,
	                            verticalAlign: 'middle',
	                            align: 'left'
	                        }
	                    }
	                },
	                animationDurationUpdate: 750
	            }
	        ]
	    };
	
	//自动调整高度
	myChart.on("click",function(pItem){
		var container = document.getElementById('echartbox');
		var allNode=0;
		var nodes=myChart._chartsViews[0]._data._graphicEls;
		for(var i=0,count =nodes.length;i<count;i++){
		   var node=nodes[i];
		   if(node===undefined)
			   continue;
		   allNode++;
		}
		var height=window.innerHeight;
		var currentHeight=10*allNode;
		var newWidth=Math.max(currentHeight,height);
		container.style.width = window.innerWidth + 'px';
		container.style.height = newWidth + 'px';
		myChart.resize();
	});
		var bindEChcartsContextMenu = function(ec){
		/*
		 这里占用了 mouseover 和  mouseout 事件，如果已经绑定了这两个事件的话，
		 只需要把 currentItem 赋值的代码放在事件中即可
		 */
		var currentItem = null;
        ec.on("mouseover",function(pItem){
        	currentItem = pItem;
        });
        ec.on("mouseout",function(pItem){
        	currentItem = null;
        });
        window.document.onclick = function(){
        	hideMenu()
        }
        document.oncontextmenu=function(event){
			if(currentItem){
				//currentItem 的右键菜单；
				showMenu(event,currentItem);
			}
			return false;
		}
	}
	//显示菜单 这里随便写一个
	var showMenu = function(event,item){
		var menu = document.getElementById("menuBox");
		menu.style.display = "block";
		menu.style.left = event.x+"px";
		menu.style.top = event.y+"px";
		menu.getElementsByClassName("menuTitle")[0].innerHTML = item.data.username;
	}
	var hideMenu = function(){
		document.getElementById("menuBox").style.display = "none";
	}
	bindEChcartsContextMenu(myChart);

	
	function getQueryData(searchType){
		$.ajax({
			type: "Post",
			async: true	,
			url: "<%=root%>/missionView/getTrackTasksList?id=${id}&searchType="+searchType+"&type=${type}",
			success: function(data) {
				formatData(data)
				console.log(data);
				trackOption.series[0].data[0]=data;
				myChart.clear();
				myChart.setOption(trackOption);
			},
			error: function(err) {
				$G.alert("查询失败");
			}
			});
	} 
 
	function formatData(data){
		
		if(data.hasOwnProperty("children")){
			for(var i=0,len = data.children.length;i<len;i++){
				if( "02" == data.children[i].user_mission_status ){
		
					data.children[i].itemStyle = {
							color : '#2EFEF7',
							borderColor:'#01DF01',
							borderWidth: 2.5,
						};
					data.children[i].lineStyle = {
							color : '#01DF01',
							borderColor:'#01DF01'
						};
					
					
				}else if(  "01" == data.children[i].user_mission_status ||  "03" == data.children[i].user_mission_status ){
					
					if( "1" == data.children[i].overdueflag ){
						
						data.children[i].itemStyle = {
								color : '#FE2E2E',
								borderColor:'#FE2E2E',
								borderWidth: 2.5,
							};
						data.children[i].lineStyle = {
								color : '#FE2E2E',
								borderColor:'#FE2E2E'
							};
						
					}
						
					
				}else if( "06" == data.children[i].user_mission_status  ){
					data.children[i].itemStyle = {
							color : '#3B170B',
							borderColor:'#3B170B',
							borderWidth: 2.5,
						};
					data.children[i].lineStyle = {
							color : '#3B170B',
							borderColor:'#3B170B'
						};
				}
				
				formatData(data.children[i]);
			}
		}else{
			if( "02" == data.user_mission_status ){
				
				data.itemStyle = {
						color : '#2EFEF7',
						borderColor:'#01DF01',
						borderWidth: 2.5,
					};
				data.lineStyle = {
						color : '#01DF01',
						borderColor:'#01DF01'
					};
				
				
			}else if(  "01" == data.user_mission_status ||  "03" == data.user_mission_status ){
			
			}else if( "1" == data.overdueflag ){
				
				data.itemStyle = {
						color : '#FE2E2E',
						borderColor:'#FE2E2E',
						borderWidth: 2.5,
					};
				data.lineStyle = {
						color : '#FE2E2E',
						borderColor:'#FE2E2E'
					};
				
			}else if( "06" == data.user_mission_status  ){
				data.itemStyle = {
						color : '#3B170B',
						borderColor:'#3B170B',
						borderWidth: 2.5,
					};
				data.lineStyle = {
						color : '#3B170B',
						borderColor:'#3B170B'
					};
			}
		}
	}

</script>