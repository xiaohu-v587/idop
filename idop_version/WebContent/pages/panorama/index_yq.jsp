<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title>
<%-- <script src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js" type="text/javascript"></script>  --%>
<script src="<%=request.getContextPath()%>/resource/echarts/radarmap.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/echarts/linemap.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/js/panorama.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/main1.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/echarts.js"></script>
<%-- 	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/jquery.min.js"></script>
 --%><link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/panorama.css">
 <script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
<style type="text/css">
.nav{width:50%;float:left;}
.nav span{width:auto;height:16px;float:left;font-size:16px;line-height:15px;margin-left:10px;padding-right:10px;margin-top:8px;color:#4b4a4b;cursor:pointer;}
.content_box{float:left;height:88%;margin-top:0.5%;}
.tr_h{height:33px}
.table_1{margin:5px;width:100%;font-size:10pt;}
.content_top{width:100%;color:#F00;}
.main_box{width:100%;height:100%;}
.td1{width:10%;text-align:center;}
.td2{width:10%;text-align:center;border-bottom:2px #f0eff5 solid;background-color:#fbfbfb;}

.table_box{width:98%;margin:0 auto;padding-top:10px;}

tr{
	height:25px;
}
.table_1 ul{
	width:250px;
	float:left;
	margin-top:6px;
}
.table_1 ul li{
	float:left;
	border:1px red soild;
}
.search_li{
	width:80px;
}
.page_box{font-size:12px;margin-top:25px;margin-right:1%;width:350px;float:right;}

.down_box{
	text-decoration:none;
	display:block;
	width:80px;
	height:20px;
	background-color:#158bd1;
	color:#ffffff;
	font-size:12px;
	text-align:center;
	line-height:20px;
	border-radius:2px;
}
.search_box ul {
    float: left;
    width: auto;
    margin: 5px 0 5px 0;
    padding-left:0;
}
</style>
</head>
<body>
	<div class="main_box" style="overflow:auto;">
		<div class="content_box" id="div1" style="width:41%; background-color:#ffffff">
        	
		</div>
		
		<div class="content_box margin_l1" style="width:58.5%; background-color:#ffffff;">
        	<div class="content_top"  id="rpt1" > 
				<form id="form1">
				
			   		<div class="table_1"  style="width:100%;color:#201f35;">
						<ul>
		                   	<li align="right" class="search_li">开始时间：</li>
							<li align="left">
								<input class="nui-datepicker" name="startime" id="startime" value="<%=request.getAttribute("startime")%>"></input>
								<input class="mini-hidden"  name="dborgid"  id="dborgid"/>
							</li>
						</ul>
						<ul>
		                    <li align="right" class="search_li">结束时间：</li>
							<li align="left">
								<input class="nui-datepicker" name="endtime" id="endtime"  value="<%=request.getAttribute("endtime")%>"></input>
							</li>
						</ul>
						<ul>
							<li align="right" class="search_li">机构：</li>
							<li align="left">
								<input id="orgid" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
									name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
									allowInput="false" showClose="true"  onvaluechanged="onOrgidChanged" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" value="<%=request.getAttribute("org") %>"/>
							</li>
	               		</ul>
						<ul>
		                   	<li align="right" class="search_li">查询类型 ：</li>
							<li align="left">
								<input name="querytype" id="querytype" class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="dop_querytype" value="0" onvaluechanged="onDopQuerytype"/>
							</li>
						</ul>
						<ul>
	               			<li align="right" class="search_li">图表格式：</li>
							<li align="left">
								<input name="pattern" id="pattern" class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="dop_pattern1" value="0" onvaluechanged="onDopPattern" />
							</li>
						</ul>
						<ul style="width:35%;">
							<li align="right">
								
							</li>
							<li align="left" colspan="2">
								<div class="mini-checkbox" id="dbselect" text="设为对比机构" onvaluechanged="onAddDbArr"></div> 
							</li>
							<li align="left" style="width:230px;">
								<a class="nui-button" style="float:left;margin-left:6px;" onclick="reset()">重置</a>  
		                    	<a class="nui-button" style="float:left;margin-left:6px;" onclick="search()">查询</a>
		                    	<a class="nui-button" style="float:left;margin-left:6px;" onclick="onDbRemove()">清除对比机构</a>
							</li>
	               		</ul>
			   		</div>
				
			<!--    		<table class="table_1" >
						<tr class="tr_h">
		                   	<th align="right">开始时间：</th>
							<td align="left">
								<input class="nui-datepicker" name="startime" id="startime" ></input>
								<input class="mini-hidden"  name="dborgid"  id="dborgid"/>
							</td>
		                    <th align="right">结束时间：</th>
							<td align="left">
								<input class="nui-datepicker" name="endtime" id="endtime" ></input>
							</td>
							<th align="right">机构：</th>
							<td align="left">
								<input id="orgid" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
									name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
									allowInput="false" showClose="true"  onvaluechanged="onOrgidChanged" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" value="<%=request.getAttribute("org") %>"/>
							</td>
	               		</tr>
	               		<tr class="tr_h">
	               			<th align="right">图表格式：</th>
							<td align="left">
								<input name="pattern" id="pattern" class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="dop_pattern1" value="0" onvaluechanged="onDopPattern" />
							</td>
		                   	<th align="right">查询类型 ：</th>
							<td align="left">
								<input name="querytype" id="querytype" class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="dop_querytype" value="0" onvaluechanged="onDopQuerytype"/>
							</td>
							<th align="right">
								
							</th>
							<td align="left" colspan="2">
								<div class="mini-checkbox" id="dbselect" text="设为对比机构" onvaluechanged="onAddDbArr"></div> 
							</td>
							<td align="left">
								<a class="nui-button"  onclick="reset()">重置</a>  
		                    	<a class="nui-button"  onclick="search()">查询</a>
		                    	<a class="nui-button"  onclick="onDbRemove()">清除对比机构</a>
							</td>
	               		</tr>
			   		</table>    --> 
		   		</form>
        	</div>
        	
        	<div class="content_box" style="height:80%;width:60%;border-top:1px #e7ebef solid;" id="rpt6">
        	</div>
        	<div class="content_box" style="height:75%;width:39%;border-top:1px #e7ebef solid;">
        		<div class="content_c" id="rpt2" style="height:100%;width:100%;color:#F00;">
        		<span class="title" style="margin-left:1%">
        			地区基本信息
        		</span>
        		 <table style="table-layout: fixed;width:100%;font-size:10pt;margin-left:5%;" id="areabasic">
						<!-- <tr >
		                   	<td align="left" style="width:180px;">机构人数</td>
							<td align="left" >
								<span id="empnum">154</span>
							</td>
						</tr>
						<tr >
		                    <td align="left">经办柜员人数</td>
							<td align="left">
								<span id="operatenum">100</span>
							</td>
	                    </tr>
						<tr >
	               			<td align="left">个人加权客户数</td>
							<td align="left">
								<span id="pubbusivol">1000</span>
							</td>
						</tr>
						<tr >
		                   	<td align="left">公司加权客户数</td>
							<td align="left">
								<span id="pribusivol">1000</span>
							</td>
						</tr>
						<tr >
	               			<td align="left">对私年新开有效账户</td>
							<td align="left">
								<span id="pubcustnum">9999</span>
							</td>
						</tr>
						<tr >
		                   	<td align="left">对公年新开有效账户</td>
							<td align="left">
								<span id="pricustnum">9999</span>
							</td>
	               		</tr>
	               		<tr >
		                   	<td align="left" >网点日均存款</td>
							<td align="left" >
								<span id="empnum">44.7万</span>
							</td>
						</tr>
						<tr >
		                    <td align="left">个人日均存款</td>
							<td align="left">
								<span id="operatenum">51万</span>
							</td>
	                    </tr>
						<tr >
	               			<td align="left">公司日均存款</td>
							<td align="left">
								<span id="pubbusivol">999万</span>
							</td>
						</tr>
						<tr >
		                   	<td align="left">日均叫号量</td>
							<td align="left">
								<span id="pribusivol">4450</span>
							</td>
						</tr>
						<tr >
	               			<td align="left">柜员平均业务量</td>
							<td align="left">
								<span id="pubcustnum">4202</span>
							</td>
						</tr>
						<tr >
		                   	<td align="left">柜台平均处理时间</td>
							<td align="left">
								<span id="pricustnum">5分</span>
							</td>
	               		</tr> -->
			   		</table>
        		</div> 
        	</div>
		</div>
        <div class="content_box content_c margin_t1" style="width:100%;height:300px">
        	<div class="content_c" id="line1"> 
        		
        	</div>
        	<div class="content_c" id="mainMap2">
        		<div style="height:62%">
        		<input class="mini-hidden"  name="panPage"  id="panPage"/>
            	<input class="mini-hidden"  value="1" name="panIndex"  id="panIndex"/>
        		<table cellspacing="0" class="table_box" id="datagrid">
                	<!-- <tr class="tr1">
                    	<td class="td1" filed="orgname">
                        	机构名称
                        </td>
                        <td class="td1" filed="pm">
                        	排名
                        </td>
                    	<td class="td1" filed="warnnum">
                        	问题数量
                        </td>
                    	<td class="td1" filed="cut">
                        	评分
                        </td>
                        <td class="td1" filed="module01">
                        	智能柜台
                        </td>
                        <td class="td1" filed="module02">
							现金出纳
                        </td>
                        <td class="td1" filed="module03">
							集中核准
                        </td>
                        <td class="td1" filed="module04">
							集中作业
                        </td> 
                    </tr>-->
                </table>
                </div>
                 <div  class="content_p">
	                 <ul class="page_box">
	                 	<li style="margin-right:20px;height:10px;"><a class="down_box" href="javascript:download()" style='text-decoration:none;width:40px' >下载</a></li>
	                	<li style="margin-right:20px;height:10px;"><a class="down_box" href="javascript:downloadforwd()" style='text-decoration:none;' >下载网点数据</a></li>
	                	<li id="totalPage"></li>
	                    <a href="javascript:back()"><li class="back"></li></a>
	                    <li class="pagination" id="indexPage"></li>
	                    <a href="javascript:go()"><li class="go"></li></a>
	                </ul> 
		        </div>
        	</div>
		</div> 
    </div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var root = "<%=root%>";
	var mychart; 
	var form = $G.getForm("form1");
  var lineCharts = echarts.init(document.getElementById('line1'));
  var patternCharts  = echarts.init(document.getElementById('rpt6'));
  //用于记录对比机构数据
  var DbArray = new Array();		
  	//var grid = mini.get("datagrid");
  	//grid.load();
	
  /* 	var date = new Date();
  	var month = date.getMonth()+1; //0-11 0-代表1月
  	var quarter = Math.floor((month%3 == 0 ? (month/3):(month/3+1))); //季度
  	var halyyear = month<7?0:1;
  	var year = date.getFullYear(); */
  	var dicturl = "<%=root%>/param/getKeyList?key=";
  	
  	function onDopQuerytype(e){
  		if(e.value === "0"){
  			$G.get("pattern").load(dicturl+"dop_pattern1");
  		}else{
  			$G.get("pattern").load(dicturl+"dop_pattern2");
  		}
  		$G.get("pattern").select(0);
  	} 	
  	function onDopPattern(e){
  		if(e.value === "3"){
  			$("#db1").hide();
  		}else{
  			$("#db1").show();
  		}
  		
  	}
  	
  	function onAddDbArr(e){
  		  var orgid = nui.get("orgid").getValue();
  			if(e.value == "true"){
  				//加入数组
  				if($.inArray(orgid,DbArray) == -1){
  			  	DbArray.push(orgid);
  			  }
  			}else{
  				//移除数组
  				if($.inArray(orgid,DbArray) == 1){
  			  	DbArray.remove(orgid);
  			  }
  			}
				
  	}
  	
  	function onDbRemove(){
  				DbArray.length=0;
  				$G.get("dborgid").setValue("");
  				$G.get("dbselect").setValue("false");
  				search();
  	}
  	
  	function onOrgidChanged(e){
  		if($.inArray(e.value,DbArray) == 0){
  		 	$G.get("dbselect").setValue("true");
  		}else{
  			$G.get("dbselect").setValue("false");
  		}	
  	}
  	
  	Array.prototype.remove = function(val){
  		var index = this.indexOf(val);
  		if(index > -1){
  			this.splice(index,1);
  		}	
  	}
  	

  	/* function onDopRqtype(e){
  		var val;
  		if(e.value === "0"){
  			val = month;
  			$G.get("date").load(dicturl+"dop_monthdate");
  		}else if(e.value === "1"){
  			val = quarter;
  			$G.get("date").load(dicturl+"dop_quarterdate");
  		}else if(e.value === "2"){
  			val = halyyear;
  			$G.get("date").load(dicturl+"dop_halfyear");
  		}else if(e.value === "3"){
  			val = year;
  			$G.get("date").load(dicturl+"dop_yeardate");
  		}else{
  			$G.get("date").setData([]);//置空
  		}
  		$G.get("date").setValue(val);  		
  	} */
  	
  	function initDbSelect(){
  		var orgid = nui.get("orgid").getValue();
  		if($.inArray(orgid,DbArray) == 1){
  		 	$G.get("dbselect").setValue("true");
  		}else{
  			$G.get("dbselect").setValue("false");
  		}	
  	}
  	function search(){
  		$G.mask({
				el:document.body,
				html:'加载中...',
				cls:'mini-mask-loading',
		});
  		$G.get("dborgid").setValue(DbArray.join(','));
  		var data = form.getData();
  		findJsMap({orgnum:data.orgid});
  		findBaseDate(data,function(){
  			findLineMapDate(data,function(){
  				loadPanoramic(function(){
  					
  				});		
  			});
  			loadAreaBasic();
  			$G.unmask();
  		});
  		initDbSelect();
  	}
  	function reset(){
  		$G.get("pattern").load(dicturl+"dop_pattern1");
  		form.reset();
  	}
  	
  	function findBaseDate(data,callback){
  		$G.ajax({
    	  	data:data,
    	  	url:"<%=root%>/panorama/getGegendData",
    	  	success:function(data){
          	    findPatternMap(data);
				callback();
    	  	},
			error:function(text){
				callback();
			}
      }) 	
  	}
  	
  	
  	var lineOption = templtOption.line_for_smooth;
  	lineOption.title.text="";
  	
	function findLineMapDate(data,callback){
		data["cloumntype"] = showLineData["cloumntype"];
  		$G.ajax({
    	  	data:data,
    	  	url:"<%=root%>/panorama/getLineList",
    	  	success:function(data){
    	  		lineOption.legend.data=data.legend;
    	  		lineOption.xAxis[0].data=data.xAxis;
    	  		
    	  		lineOption.yAxis[0].splitNumber=7;//设置y轴之间间距
    	  		lineOption.yAxis[0].min=50;    //设置y轴最小值
    	  		lineOption.yAxis[0].max=400;    //设置y轴最大值
    	  		
    	  		lineOption.series = data.series;
    	  		lineCharts.clear();
    	  		lineCharts.setOption(lineOption);
				callback();
    	  	},
			error:function(text){
				callback();
			}
      }) 	
  	}
    var showLineData = {"cloumntype":"cut"}; 
	function showLine(orgid,column){
		showLineData["cloumntype"] = column;
		var data = form.getData();
		data["orgid"] = orgid;
  		findLineMapDate(data);

	}
  	
  	function findPatternMap(data){
  		patternCharts.clear();
  		
  		var pattern = $G.get("pattern").getValue();
  		var querytype = $G.get("querytype").getValue();
  		var option = {};
  		if("0" == querytype){//查询类型选评分
  			if("0" == pattern){//雷达图
				option = templtOption.radar;     
				option.legend.data=data.legend;
				option.radar[0].indicator=data.indicator;
				option.radar[0].radius=130;
				option.series[0].data=data.series;
				option.series[0].itemStyle = {};
				option.series[0].symbolSize=7;
				option.series[0].symbol='circle';
				
			}else if("1" == pattern){//柱形图
				option = templtOption.bar;
				option.legend.data=data.legend;
				option.xAxis[0].data=data.xAxis;
				option.series=data.series;
			}else{
				//后期可添加
			}
		}else{//查询类型选预警
			
			if("1" == pattern){//柱状图
				  option = templtOption.bar;
				  option.legend.data=data.legend;
				  option.xAxis[0].data=data.xAxis;
				  option.series=data.series;
			}else if("2" == pattern){//折线图
				  option = templtOption.line;
				  option.legend.data=data.legend;
    	  		  option.xAxis[0].data=data.xAxis;
    	  	      option.series = data.series;					
			}else if("3" == pattern){//饼图
				  option = templtOption.pie;
				  option.legend.data=data.legend;
				  option.series[0].data=data.series;	
			}else{
				//后期可添加
			}

		}
	  		patternCharts.setOption(option);
  		
  	}
	
	$(function(){
		
		//加载默认机构数据
		search();
		//loadPanoramic()
		//加载雷达图
		//findRaderMapFun();
		
		//findLineMapFun('line1');
		
		//加载地图数据
		//findJsMap({});
		//findLineMapDate({});
	});
	
	function findJsMap(paramdata){
		//后台查数据拼接，暂时在界面写
		var city = {
			    //13个市
			     "南京": "020000000",
			    "无锡": "003000000",
			    "徐州": "009000000",
			    "常州": "004000000",
			    "南通": "006000000",
			    "连云港": "010000000",
			    "淮安": "012000000",
			    "盐城": "005000000",
			    "扬州": "011000000",
			    "镇江": "008000000",
			    "泰州": "013000000",
			    "宿迁": "007000000"
			};
		
		//匹配当前机构代码
		var mapcity = {
			    //13个市
			    "020000000": "320100",
			    "003000000": "320200",
			    "009000000": "320300",
			    "004000000": "320400",
			    "006000000": "320600",
			    "010000000": "320700",
			    "012000000": "320800",
			    "005000000": "320900",
			    "011000000": "321000",
			    "008000000": "321100",
			    "013000000": "321200",
			    "007000000": "321300"
			};
		
		var citys = {
			    //13个市
			    "320100":"南京",
			    "320200":"无锡",
			    "320300":"徐州" ,
			    "320400": "常州",
			    "320600": "南通",
			    "320700": "连云港",
			    "320800": "淮安",
			    "320900": "盐城",
			   "321000" : "扬州",
			   "321100":  "镇江",
			   "321200" : "泰州",
			    "321300": "宿迁"
			};
		//地图数据
		var datas = null;
		//当orgstat=2时，登录人为分行；当orgstat=1时，登录人为总行。
		var orgstat;
		var org;
		
		//地图容器
		$("#div1").html('<div class="content_c" id="mainMap"  style="height:95%;"> </div>');
		
		mychart = echarts.init(document.getElementById('mainMap'));
		
		$.ajax({
			url: "<%=root%>/panorama/getWarnMap",
			data:paramdata,
			cache: false,
			async:false, 
			success: function (text) {
			  datas = mini.decode(text).data;
			  orgstat = mini.decode(text).orgstat;
			  org = mini.decode(text).org;
			}
		})
		
		//显示当前登录人所在分行地图数据
		if(orgstat == "2"){
			$.getJSON('<%=root%>/resource/echarts/city/'+ mapcity[org] +'.json', function(data){
				echarts.registerMap(citys[mapcity[org]], data);
				var d = [];
				for( var i=0;i<data.features.length;i++ ){
					for(var j=0;j<datas.length;j++){
						d.push({
							name:data.features[i].properties.name,
							value:datas[j].lv
						})
					}
				}
				//重新加载地图以及数据
				//选中的市地图
				renderMap(citys[mapcity[org]],d,2);
			});
		}else if(orgstat == 1){
			//绘制江苏省
			$.getJSON('<%=root%>/resource/echarts/jiangsu.json',function(geoJson){
				d = [];
				for( var i=0;i<geoJson.features.length;i++ ){
					    for(var j=0;j<datas.length;j++){
						if(citys[mapcity[datas[j].lvl_2_branch_no]] == geoJson.features[i].properties.name){
							d.push({
								name:geoJson.features[i].properties.name,
								value:datas[j].lv
							})
						}
					}
				}
				mapdata = d;
				//注册地图
				echarts.registerMap('jiangsu', geoJson);
				//绘制江苏省地图
				renderMap('jiangsu',mapdata,1);
				
			})
		}
		



	
		 
		function renderMap(map,data,stat){
			
			//清除上一次加载样式
			mychart.clear();
			//加载地图数据
			gycharts(map,data,stat);
			
			 //地图点击事件
			mychart.on('click',function (params) {
					var orgnum = city[params.name];
					//如果机构不存在,返回顶级地图时，机构号会为空
					if(!orgnum)
						orgnum = "<%=request.getAttribute("org") %>"
					
					//findJsMap({orgnum:orgnum});//查询子机构数据
					//设置表单机构参数
					nui.get("orgid").setValue(orgnum);
					onDbRemove(); //清除对比
					search();//执行点击查询
			});
			
		}
	}
	/*[
				    	      {name:'南京',value:50},
				    	      {name:'无锡',value:60},
				    	      {name:'徐州',value:0},
				    	      {name:'常州',value:1},
				    	      {name:'南通',value:13},
				    	      {name:'连云港',value:21},
				    	      {name:'淮安',value:12},
				    	      {name:'盐城',value:6},
				    	      {name:'扬州',value:8},
				    	      {name:'镇江',value:55},
				    	      {name:'泰州',value:2},
				    	      {name:'宿迁',value:34}
				    	      ]*/
	
	//绘制江苏省地图
	function gycharts(map,data,stat){
		if(stat == 1){
			mychart.setOption(option = {
					//地图背景色
					backgroundColor: '#ffffff',
					width:350,
				    title : {
				        text: '',//主标题文本，'\n'可换行
				        subtext: '',//副标题文本，'\n'可换行
				        sublink:'http://www.ldsun.com',
				        left: 'center',//地图显示位置
				        textStyle:{
				            color: '',
				            fontSize:12,
				            fontWeight:'normal',
				            fontFamily:"Microsoft YaHei"
				        },
				        subtextStyle:{
				        	color: '',
				            fontSize:8,
				            fontWeight:'normal',
				            fontFamily:"Microsoft YaHei"
				        }
				    },
				    tooltip: {
				    	//出发类型 默认为item 也可选axis
				        trigger: 'item',
				        formatter: '{b}'
				    },
				    //工具箱
				    toolbox: {
				    	//是否显示工具箱
				        show: true,
				        //布局方式
				        orient: 'vertical',
				        //浮动类型
				        left: 'right',
				        top: 'center',
				        feature: {
				        	//数据视图按钮
				            dataView: {readOnly: false},
				            //还原，复位原始图标
				            restore: {},
				            //保存图片
				            saveAsImage: {}
				        },
				        iconStyle:{
				        	//工具箱图标颜色
				        	normal:{
				        		color:'#CDCDCD'
				        	}
				        }
				    },
				    //热力图设置
				    visualMap:{
				    	min:0,
				    	max:100,
				    	text:['High','Low'],
				    	itemHeight:100,
				    	realtime:false,
				    	calculable:true,
				    	inRange:{
				    		color:['#6cbf8b','#e5df76','#e6b174','#e06e6e']
				    	}
				    },
				    //驱动图表生成的数据内容数组
				    series:[{
				    	//系列名称
				    	name:'江苏省风险地图',
				    	type:'map',//type必须为map类型
				    	mapType:map,//自定义扩展图标类型
				    	roam:'scale',//可以缩放
				    	//图形样式
				    	itemStyle:{
				    		//正常时的样式
				    		normal:{//边框线宽
				    			borderWidth:1,
				    			//边框颜色
				    			borderColor:'#ffffff',
				    			//标签
				    			label:{show:true}},
				    		//鼠标经过时的样式
				    		emphasis:{
				    			//边框线宽
				    			borderWidth:2,
				    			//边框颜色
				    			borderColor:'#ffffff',
				    			//标签
				    			label:{show:true}
				    		}
				    	},
				    	data:data
				    }] 
			},true);
		}else{
			mychart.setOption(option = {
					//地图背景色
					backgroundColor: '#ffffff',
				    title : {
				        text: '',//主标题文本，'\n'可换行
				        subtext: '',//副标题文本，'\n'可换行
				        sublink:'http://www.ldsun.com',
				        left: 'center',//地图显示位置
				        textStyle:{
				            color: '',
				            fontSize:16,
				            fontWeight:'normal',
				            fontFamily:"Microsoft YaHei"
				        },
				        subtextStyle:{
				        	color: '',
				            fontSize:10,
				            fontWeight:'normal',
				            fontFamily:"Microsoft YaHei"
				        }
				    },
				    tooltip: {
				    	//出发类型 默认为item 也可选axis
				        trigger: 'item',
				        formatter: '{b}'
				    },
				    //工具箱
				    toolbox: {
				    	//是否显示工具箱
				        show: true,
				        //布局方式
				        orient: 'vertical',
				        //浮动类型
				        left: 'right',
				        top: 'center',
				        feature: {
				        	//数据视图按钮
				            dataView: {readOnly: false},
				            //还原，复位原始图标
				            restore: {},
				            //保存图片
				            saveAsImage: {}
				        },
				        iconStyle:{
				        	//工具箱图标颜色
				        	normal:{
				        		color:'#CDCDCD'
				        	}
				        }
				    },
				    //热力图设置
				    visualMap:{
				    	min:0,
				    	max:100,
				    	text:['High','Low'],
				    	realtime:false,
				    	calculable:true,
				    	inRange:{
				    		color:['#6cbf8b','#e5df76','#e6b174','#e06e6e']
				    	}
				    },
				    //驱动图表生成的数据内容数组
				    series:[{
				    	name: map,
			            type: 'map',
			            mapType: map,
			            roam: 'scale',//地图可以放大缩小
			            nameMap:{
						    map:map
						},
			            label: {
				            normal:{
								show:true,
								textStyle:{
									color:'',
									fontSize:8
								}  
				            },
				            emphasis: {
				                show: true,
				                textStyle:{
									color:'',
									fontSize:8
								}
				            }
				        },
				        itemStyle: {
				            normal: {
				                areaColor: '#CDCDCD',
				                borderColor: 'dodgerblue'
				            },
				            emphasis: {
				                areaColor: 'darkorange'
				            }
				        },
			            data:data
				    }] 
			},true);
		}
	}
	
	
  //前一页
	function back(){
		var data = form.getData();
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue(); 
		if(panIndex >= 2){
			panIndex = parseInt(panIndex)-1;
			data['pageIndex'] = panIndex;
			$.ajax({
				url: "<%=root%>/panorama/getTableList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var headers = mini.decode(text).headers;
				  var totalPage = mini.decode(text).total;
				  var isend = mini.decode(text).isend;
				  loadHtml(headers,formt,totalPage,panIndex,isend);
				}
			})
		}else{
			return;
		}
	}
	
	
	//前一页
	function go(){
		var data = form.getData();
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		
		if(panIndex < panPage){
			panIndex = parseInt(panIndex)+1;
			data['pageIndex'] = panIndex;
			$.ajax({
				url: "<%=root%>/panorama/getTableList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var headers = mini.decode(text).headers;
				  var totalPage = mini.decode(text).total;
				   var isend = mini.decode(text).isend;
				  loadHtml(headers,formt,totalPage,panIndex,isend);
				}
			})
		}else{
			return;
		}
	}
	

	var configHeaders = [
		{field:"orgname",text:"机构名称"},
		{field:"pm",text:"排名"},
		//{field:"warnnum",text:"问题数量"},
		{field:"cut",text:"评分"}
	]
	
	
	//加载机构全景视图数据
	function loadPanoramic(callback){
		var data = form.getData();
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		data['panPage'] = panPage;
		data['pageIndex'] = panIndex;
		$.ajax({
			url: "<%=root%>/panorama/getTableList",
			data:data,
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);
			  var headers = data.headers 
			  var formt =  data.data;
			  var totalPage = data.total;
			  var isend = data.isend;
			  loadHtml(headers,formt,totalPage,panIndex,isend);
			  callback();
			}
		})
	}
	

	
	
	function loadHtml(headers1,formt,totalPage,panIndex,isend){
			var grid = $("#datagrid");
			grid.children().remove();
			
			var headers = $G.clone(configHeaders);
			for(var i=0;i<headers1.length;i++){
					headers.push(headers1[i]);
			}
			var tr = document.createElement("tr");
			tr.setAttribute("class","tr1");
			for(var j=0;j<headers.length;j++){
				var td = document.createElement("td");
				if(j==0){
	  				td.setAttribute("class","td1 border_l");
	  			}else if(j+1==headers.length){
		 	 		td.setAttribute("class","td1 border_r");
	  			}else{
		  			td.setAttribute("class","td1");
	  			}
	  			td.setAttribute("field",headers[j]["field"]);
	  			td.innerHTML= headers[j]["text"] || '';
	  			tr.appendChild(td);
			}
			grid.append(tr);
		 var oldrow = null,isendrow = false;
		 if(formt != null &&formt.length > 0){
			  for(var i =0;i<formt.length;i++){
			  	var tr = document.createElement("tr");
					tr.setAttribute("class","tr2");
				if(i==0){
					oldrow = formt[i];
				}else{
					oldrow = formt[i-1];
				}
				if(oldrow.by2 != formt[i].by2){
					isendrow = true;
				}
				
			  	for(var j=0;j<headers.length;j++){
			  			var td = document.createElement("td");
			  			if(j==0){
				  				td.setAttribute("class","td2 border_l");
			  			}else if(j+1==headers.length){
				 	 				td.setAttribute("class","td2 border_r");
			  			}else{
				  				td.setAttribute("class","td2");
			  			}
			  			var a = document.createElement("a");
			  			 if(j>2){
			  				a.setAttribute("href",""+formt[i]["deptno"]);
			  				td.innerHTML= "<a href='javascript:showLine(\"" +formt[i]["deptno"]+"\",\""+headers[j]["field"]+ "\")' style='text-decoration:none;'>"+formt[i][headers[j]["field"]] || ''+"</a>";
			  			}else{
							
			  				if( isend == "1" && headers[j]["field"] == "pm" && formt[i][headers[j]["field"]] == totalPage && isendrow ){
			  					td.innerHTML=  '';
			  				}else{
			  					td.innerHTML= formt[i][headers[j]["field"]] || '';
			  				}
			  				
			  			}
			  			tr.appendChild(td);
			  			grid.append(tr);
			  	}
			  }
			  
		  }
		  var totalPage1 = parseInt(totalPage/5);
		  if(totalPage%5!=0 ){
			  totalPage1 = totalPage1+1;
		  }
		  $G.get("panPage").setValue(totalPage1);
		  document.getElementById("totalPage").innerHTML = "共"+totalPage1+"页,第"+panIndex+"页  ";
		  document.getElementById("indexPage").innerHTML = panIndex;
		  $G.get("panIndex").setValue(panIndex);
		  $G.parse();
	}
	/*
		function loadHtml1(formt,totalPage,panIndex){
			var grid = $("#datagrid");
			var children = grid.children();
			var tr0 = $(children[0].children[0]);
			var header = tr0.children();
			var headers = [];
			var trs = children[0].children;
			for(var i=0;i<header.length;i++){
					headers.push($(header[i]).attr("filed"))
			}
			$(trs).remove();
			grid.append(tr0);
		  if(formt != null &&formt.length > 0){
			  for(var i =0;i<formt.length;i++){
			  	var tr = document.createElement("tr");
				tr.setAttribute("class","tr2");
			  	for(var j=0;j<headers.length;j++){
			  			var td = document.createElement("td");
			  			if(j==0){
				  				td.setAttribute("class","td2 border_l");
			  			}else if(j+1==headers.length){
				 	 				td.setAttribute("class","td2 border_r");
			  			}else{
				  				td.setAttribute("class","td2");
			  			}
			  			td.innerHTML= formt[i][headers[j]] || '';
			  			tr.appendChild(td);
			  			grid.append(tr);
			  	}
			  }
			  
		  }
		  var totalPage1 = parseInt(totalPage/5);
		  if(totalPage%5!=0 ){
			  totalPage1 = totalPage1+1;
		  }
		  $G.get("panPage").setValue(totalPage1);
		  document.getElementById("totalPage").innerHTML = "共"+totalPage1+"页,第"+panIndex+"页  ";
		  document.getElementById("indexPage").innerHTML = panIndex;
		  $G.get("panIndex").setValue(panIndex);
		  $G.parse();
	}*/
	
	

	
	function download(){
		form.validate();
		var data = form.getData();
		data['pageSize']  = 9999999;
		data['pageIndex'] = 1;
		data['execlfilename'] = '全景视图列表';
		data['method'] = "getTableList";
		callHeadAndTextToData("datagrid",data);
		if (form.isValid() == false){
			return;
		}else{
			window.location="<%=root%>/panorama/download?"+connUrlByJson(data);
		}
	}
	function downloadforwd(){
		form.validate();
		var data = form.getData();
		data['pageSize']  = 9999999;
		data['pageIndex'] = 1;
		data['execlfilename'] = '全景视图列表';
		data['method'] = "getTableListForWd";
		callHeadAndTextToData("datagrid",data);
		if (form.isValid() == false){
			return;
		}else{
			window.location="<%=root%>/panorama/download?"+connUrlByJson(data);
		}
	}
	
	
	function loadAreaBasic(){
		
		
		$.ajax({
			url: "<%=root%>/panorama/getAreaBasic",
			data:form.getData(),
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);
			  var formt =  data.data;
			  var htmls = "";
			  for(var i=0;i<formt.length;i++){
				  htmls+="<tr >"+
		                   	"<td align='left' style='width:65%;'>"+formt[i].remark+"</td>"+
							"<td align='left'>"+
								"<span >"+formt[i].indicator_value+"</span>"+
							"</td>"+
						"</tr>";
			  }
			  $("#areabasic").html(htmls);
			  
			}
		})
	}	
</script>
