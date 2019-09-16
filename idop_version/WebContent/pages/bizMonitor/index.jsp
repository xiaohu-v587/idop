<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/common/nuires.jsp" %>
<script src="<%=request.getContextPath()%>/resource/echarts/radarmap.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/echarts/linemap.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/echarts.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
<title>组合监测-业务监测</title>
<style type="text/css">
	body {
		overflow: auto;
/* 		padding-left: 20px; */
/* 		padding-top: 20px; */
	}
	#search {
		text-align: center;
	}
	#barWrap,#pieWrap,#gridWrap,#pie1Wrap {
		float: left;
		height: 50%;
		width: 49%;
		margin: 6px;
	}
	
</style>
</head>
<body>

<div size="100%" showCollapseButton="false" expanded="true" style="border-left: 0px;">
		<div style="overflow: auto;" align="center">
			<form id="form1" align="center">
				<div width="100%" align="center">
					<table>
						<tr>
							<td align="center">监测条件:</td>
							<td align="center"><input id="monitor_name" width="200px" name="monitor_name" class="mini-combobox"
								url="<%=root%>/bizMonitor/getMonitorList" multiSelect="false" allowInput="false" oncloseclick="onCloseClick" 
								dataField="data" valuefield="id" textfield="monitor_name" showClose="true" required="true" />
							</td>
							<td style="padding-left: 50px" align="center">监测时间:</td>
							<td align="center"><input id="date" name="date" required="true" class="mini-datepicker"
								format="yyyy-MM-dd" allowInput="false"  style="width: 96%" />
							</td>
							<td
								style="white-space: nowrap; text-align: center; margin-left: 0.5%">
								<a class="nui-button" onClick="search()" style="margin-left: 50px;">查询</a>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>

	<div id="button" align="right">
		<ul>
			<li>
				<a id="btn" class="nui-button" onClick="desc()" style="margin-right: 50px;">监测说明</a> 
			</li>
		</ul>
	</div>
<div class="content_box" style="height: 50%; width: 47%; border-top: 1px #e7ebef solid;" id="barWrap">
</div>
<div class="content_box" style="height: 50%; width: 47%; border-top: 1px #e7ebef solid;" id="pieWrap">
</div>

<div class="content_box" style="height: 50%; width: 47%; border-top: 1px #e7ebef solid; position: relative;" id="gridWrap" >
	<div align="center" style="font-size: 16px; font-weight: bold; height: 10%">
	</div>
	<div class="mini-datagrid" id="grid" width="100%" height="100%" sortMode="client" dataType="int" showEmptyText="true" emptyText="没有对应的记录" url="<%=root%>/bizMonitor/getList" onrowdblclick="toWarningSearch">
		<div property="columns" >
			<div type="indexcolumn" align="center">序号</div>
			<div align="center" headerAlign="center" field="module">业务模块</div>
			<div align="center" headerAlign="center" field="warnname">预警名称</div>
			<div align="center" headerAlign="center" field="d">监测时间</div>
			<div align="center" headerAlign="center" field="md">监测频率</div>
			<div align="center" headerAlign="center" field="warnnum" allowSort="true" dataType="int">数量</div>
		</div>
	</div>
	<a class="mini-button" iconCls="icon-download" onclick="download()" style="position: absolute; left: 300px; bottom: -8%;">导出</a>
</div>
<div class="content_box" style="height: 50%; width: 47%; border-top: 1px #e7ebef solid;"  id="pie1Wrap"></div>
</body>

<script type="text/javascript">
	$G.parse();
	
	var grid = $G.get("grid");
	var barGraph = echarts.init(document.getElementById("barWrap"));
	var pieChart = echarts.init(document.getElementById("pieWrap"));
	var pieChart1 = echarts.init(document.getElementById("pie1Wrap"));
	$("#gridWrap").hide();
	$("#barWrap").hide();
	$("#pieWrap").hide();
	$("#pie1Wrap").hide();
	$G.get("btn").hide();
	function search() {
		draw();
		
	}
	
	function onModuleRenderer(e) {
		var textVal = mini.getDictText("dop_ywtype",e.value);
		return textVal;
	}
	
	function draw() {
		$G.mask({
			el:document.body,
			html:'加载中...',
			cls:'mini-mask-loading',
		});
		$.ajax({
			url: "<%=root%>/bizMonitor/getData",
			data: {id: $G.get("monitor_name").getValue(), date: $G.get("date").getValue()},
			success: function(json) {
				
				if(json.text!=null){
    	  			$G.alert(json.text);
    	  			$("#btn").hide();
					$("#gridWrap").hide();
					$("#barWrap").hide();
					$("#pieWrap").hide();
					$("#pie1Wrap").hide();
					$G.unmask();
    	  		}else{
    	  			
    	  			
    	  		
				//柱状图
				var option = templtOption.bar;
				option.title={text:'数量分布',x:'center',y:'10px'};
				option.title.left = "center";
				option.xAxis[0].data = json.items;
				option.series[0].data = json.vals;
				barGraph.clear();
				barGraph.setOption(option);
				//饼状图1
				var option1 = templtOption.pie;
				option1.legend={data:json.legend1,orient:'vertical',x:'right',y:'center'};
	    		option1.title={text:'机构分布',x:'center',y:'10px'};
				option1.legend.data = json.items1;
				option1.series[0].data = json.vals1;
				option1.series[0].name = "预警";
				option1.series[0].radius="70%";
	    		option1.series[0].center=["50%","60%"];
				pieChart.clear();
				pieChart.setOption(option1);
				//饼状图2
				var option2 = templtOption.pie;
				option2.legend={data:json.legend2,orient:'vertical',x:'right',y:'center'};
	    		option2.title={text:'数量占比',x:'center',y:'10px'};
				option2.legend.data = json.items;
				option2.series[0].data = json.vals2;
				pieChart1.clear();
				pieChart1.setOption(option2);
				$G.unmask();
				
				getGridData();
				$("#btn").show();
				$("#gridWrap").show();
				$("#barWrap").show();
				$("#pieWrap").show();
				$("#pie1Wrap").show();
				}
			}
		});
	}
	
	function getGridData() {
		args = {};
		args.id = $G.get("monitor_name").getValue();
		args.date = $G.get("date").getValue();
		grid.load(args);
	}
	
	function desc() {
		var id = $G.get("monitor_name").getValue();
		if (id == "") {
			return;
		}
		var data = {
  				action: "view",
  				id: id
  			};
  	        $G.showmodaldialog("监测说明", "<%=root%>/cpc/toView", 1200, 600, data, function(action){
  		    	
  		    });
	}
	
	function toWarningSearch(eo) {
		var d = $G.get("date").getValue();
		var c = eo.record.indexcode;
		var fre = eo.record.md;
		var tab1 = {
			url: "<%=root%>/warning_search/toIndexFromBizMonitor?date=" + d +"&c=" + c + "&fre=" + fre,
			title: "预警信息查询"
		};
		var tabs = parent.$G.get("mainTabs");
		var id = "tab$" + tab1.name;
		var tab = tabs.getTab(id);
		var activeTab = tabs.getActiveTab();
		if(id == activeTab.name){
			tabs.reloadTab(tab);
			return;
		}
		if(!tab){
			tab1.name = id;
			tab1["showCloseButton"] = true; 
			tab1["showMenuButton"] = true;
	        tabs.addTab(tab1);
	        tabs.activeTab(tab1);
		}else{
			tabs.activeTab(tab);
			tab.url = tab1.url;
			tabs.reloadTab(tab);
		}
	}
	
	function download(){
	  	args.pageIndex ="0";
		args.pageSize = "999999";
		window.location="<%=root%>/bizMonitor/download?"+connUrlByJson(args);
	}
</script>
</html>