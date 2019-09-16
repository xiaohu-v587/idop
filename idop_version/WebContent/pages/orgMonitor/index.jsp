<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>

<head>
<title>机构监测界面</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/resource/echarts/radarmap.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/echarts/linemap.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/echarts.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
<%-- <script type="text/javascript" src="<%=root%>/resource/echarts/jquery.min.js"></script> --%>

<style type="text/css">
.content_box{float:left;overflow:hidden;height:88%;margin-top:0.5%;margin-right: 0.5%;margin-left: 0.5%;}
	#search {
		text-align: center;
	}
</style>

</head>
<body style="overflow: auto;">
	<div size="100%" showCollapseButton="false" expanded="true" style="border-left: 0px;">
		<div style="overflow: auto;" align="center">
			<form id="form1" align="center">
				<div width="100%" align="center">
					<table>
						<tr>
							<td align="center">监测条件:</td>
							<td align="center"><input id="monitor_name" width="200px" name="monitor_name" class="mini-combobox"
								url="<%=root%>/orgMonitor/getMonitorName" multiSelect="false" allowInput="false" oncloseclick="onCloseClick" 
								dataField="data" valuefield="id" textfield="monitor_name" showClose="true" required="true" />
							</td>
							<td style="padding-left: 50px" align="center">监测时间:</td>
							<td align="center"><input id="monitor_date" name="monitor_date" required="true" class="mini-datepicker"
								format="yyyy-MM-dd" allowInput="false" style="width: 96%" />
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
				<a id="view" class="nui-button" onClick="view()" style="margin-right: 50px;">监测说明</a> 
				<a class="nui-button" onClick="detail()" style="margin-right: 50px;">监测明细</a>
			</li>
		</ul>
	</div>
	<div class="content_box" style="height: 50%; width: 49%; border-top: 1px #e7ebef solid;" id="rpt1">
	</div>

	<div class="content_box" style="height: 50%; width: 49%; border-top: 1px #e7ebef solid;" id="rpt2">
	</div>
	
	<div class="content_box" style="height: 50%; width: 49%; border-top: 1px #e7ebef solid;" id="rpt4">
	</div>

	<div class="content_box" style="height: 50%; width: 49%; border-top: 1px #e7ebef solid;" id="rpt5">
	</div>
	
	<div class="content_box" style="height: 50%; width: 49%; border-top: 1px #e7ebef solid;" id="rpt3">
		<div align="center" style="font-size: 16px; font-weight: bold; height: 10%">
		</div>
		<div class="mini-fit">
			<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 98%;" sortMode="client"
				emptyText="没有对应的记录" allowUnselect="false" url="<%=root%>/orgMonitor/getList" multiSelect="false"
				allowCellSelect="false" allowResize="false" autoEscape="false"
				allowResizeColumn="false" showEmptyText="true" allowMove="false">
				<div property="columns">
					<div type="indexcolumn" width="40" allowMove="false" headerAlign="center">序号</div>
					<div field="branch_name" headerAlign="center" allowMove="false" allowSort="true" align="center">分行名称
					</div>
					<div field="org_no" headerAlign="center" width="100" allowMove="false" allowSort="true" align="center">机构号
					</div>
					<div field="org_name" headerAlign="center" width="100" allowMove="false" allowSort="true" align="center">机构名称
					</div>
					<div field="monitor_date" headerAlign="center" width="100" allowMove="false" allowSort="true" align="center">监测时间
					</div>
					<div field="monitor_rate" headerAlign="center" width="100" allowMove="false" allowSort="true" url="<%=root%>/param/getDict?key=monitor_rate" align="center">监测频率
					</div>
					<div field="count" headerAlign="center" width="100" allowMove="false" allowSort="true" dataType="int" align="center">数量
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="content_box" style="height: 50%; width: 49%; border-top: 1px #e7ebef solid;" id="rpt6">
		<div align="center" style="font-size: 16px; font-weight: bold; height: 10%; margin-top: 1%">近三期均被监测的机构
		</div>
		<div class="mini-fit">
			<div id="datagrid2" dataField="data" class="mini-datagrid" style="width: 100%; height: 98%;" sortMode="client"
				emptyText="没有对应的记录" allowUnselect="false" url="<%=root%>/orgMonitor/getOrg" multiSelect="false"
				allowCellSelect="false" allowResize="false" autoEscape="false"
				allowResizeColumn="false" showEmptyText="true" allowMove="false">
				<div property="columns">
					<div type="indexcolumn" width="40" allowMove="false" headerAlign="center">序号
					</div>
					<div field="branch_name" headerAlign="center" allowMove="false" allowSort="true" align="center">分行名称
					</div>
					<div field="org_no" headerAlign="center" width="100" allowMove="false" allowSort="true" align="center">机构号
					</div>
					<div field="org_name" headerAlign="center" width="100" allowMove="false" allowSort="true" align="center">机构名称
					</div>
					<div field="flag1" headerAlign="center" width="100" allowMove="false" allowSort="true" dataType="int" align="center">本期数据
					</div>
					<div field="flag2" headerAlign="center" width="100" allowMove="false" allowSort="true" dataType="int" align="center">上期数据
					</div>
					<div field="flag3" headerAlign="center" width="100" allowMove="false" allowSort="true" dataType="int" align="center">上上期数据
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");

	var patternCharts1 = echarts.init(document.getElementById('rpt1'));
	var patternCharts2 = echarts.init(document.getElementById('rpt2'));
	var patternCharts4 = echarts.init(document.getElementById('rpt4'));
	var patternCharts5 = echarts.init(document.getElementById('rpt5'));
	var grid1 = mini.get("datagrid1");
	var grid2 = mini.get("datagrid2");
	$("#button").hide();
	$(".content_box").hide();

	//监测条件下拉框清空

	function onCloseClick(e) {
		var obj = e.sender;
		obj.setText("");
		obj.setValue("");
	}

	//监测说明
	function view() {
		var monitor_name = $G.get("monitor_name").getValue();
		if (monitor_name == "") {
			$G.alert("请选择监测条件");
			return;
		}
		if (monitor_name != "") {
			var data = {
				action : "view",
				id : monitor_name
			};
			$G.showmodaldialog("监测说明", "<%=root%>/orgMonitor/toView", 1200, 600, data, function(action){
   		    	
   		    });	
	  }
	  }
	  
	    //监测明细
  	function detail(){
  	
    	var monitor_name = $G.get("monitor_name").getValue();
    	var monitor_date = $G.get("monitor_date").getValue();
    	
    	if(monitor_name==""){
			$G.alert("请选择监测条件");
			return;
		}
		if(monitor_date==""){
			$G.alert("请选择监测时间");
			return;
		}
    	if(monitor_name!=""&&monitor_date!=""){
    	
            var url="<%=root%>/orgMonitor/toDetail";
	        var bizParams = { monitor_name:monitor_name,monitor_date:monitor_date};
	        $G.showmodaldialog("监测详细", url, 1200, 600, bizParams, function(action){
	        	
		    });   	
	  }
	  }
	  
	function search(){
	
		
  		$G.mask({
				el:document.body,
				html:'加载中...',
				cls:'mini-mask-loading',
		});
  		var data=form.getData();
  		
  		findBaseDate(data,function(){
  			$G.unmask();
  			
  		});
  		
  	}
	
	function findBaseDate(data,callback){
  		$G.ajax({
  			
    	  	data:data,
    	  	url:"<%=root%>/orgMonitor/getGegendData",
    	  
    	  	success:function(data){	  	
    	  	
    	  		if(data.text!=null){
    	  			$G.alert(data.text);
    	  			$("#button").hide();
					$(".content_box").hide();
    	  		}else{
    	  			$("#button").show();
	 				$(".content_box").show();
    	  			findPatternMap(data);
    	  		}
          	    
				callback();
    	  	},
			error:function(text){
				callback();
			}
      })
  	}
	
	function findPatternMap(data){
  		
		patternCharts1.clear();
  		var option1 = {};
  		option1 = templtOption.bar;
	    option1.legend.data=data.legend1;
	    option1.title={text:'数量分布',x:'center',y:'10px'};
	    option1.xAxis[0].data=data.xAxis1;

		option1.series=data.series1;
		patternCharts1.setOption(option1);
  		
		patternCharts2.clear();
  		var option2 = {};
	    option2 = templtOption.pie;
	    option2.legend={data:data.legend2,orient:'vertical',x:'right',y:'center'};
	    option2.title={text:'机构分布',x:'center',y:'10px'};
	    option2.series[0].name="机构";
	    option2.series[0].radius="70%";
	    option2.series[0].center=["50%","60%"];
		option2.series[0].data=data.series2;	
  		patternCharts2.setOption(option2);
  		
  		patternCharts4.clear();
  		var option4 = {};
  		option4 = templtOption.bar_stack;
	    option4.xAxis[0].data=data.xAxis4;
	    option4.xAxis[0].axisLabel={show:true,interval:0,rotate:60,textStyle:{fontsize:'8'}};
		option4.series=data.series4;
		for(var i=0;i<option4.series.length;i++){
			if(option4.series[i]['type'] == 'bar'){
				option4.series[i]['stack'] = '总量';
				option4.series[i]['label'] = {
					normal:{
						show:false,
						position:'insideRight'
					}
				}	
			}
		} 
		option4.title={text:'数量最多的10个机构',x:'center',y:'10px'};
		patternCharts4.setOption(option4);
		
		patternCharts5.clear();
  		var option5 = {};
		option5 = templtOption.line;
	    option5.legend.data=data.legend5;
	    option5.xAxis[0].data=data.xAxis5;
	    option5.xAxis[0].axisLabel={interval:0,rotate:0},
        option5.series= data.series5;	
        option5.title={text:'监测机构数',x:'center',y:'10px'};
        patternCharts5.setOption(option5);
        
        grid1.load(form.getData());
        grid2.load(form.getData());
  	}
    
</script>
