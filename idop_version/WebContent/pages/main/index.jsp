<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title>
<%-- <script src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js" type="text/javascript"></script>  --%>
<script src="<%=request.getContextPath()%>/resource/echarts/radarmap.min.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/main1.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/echarts.js"></script>
	<script src="<%=request.getContextPath()%>/resource/echarts/linemap.js" type="text/javascript"></script>
<%-- 	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/jquery.min.js"></script>
 --%>	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/index.css">
<!-- <style type="text/css">
#mini-buttonedit-input{border-style:none none solid none;}
</style> -->
</head>

<body>
	<div class="main_box">
		<div class="content_box" >
        	<span class="title">
        		模块全景图 
        	</span>
        	<div class="content_c" style="height:100%;width:100%; text-align: center;"> 
	        	<div  id="rpt6" style="height:100%;width:100%; text-align: center;"> 
	        	</div>
        	</div>
		</div> 
        
		<div class="content_box margin_l1" >
        	<span class="title" style="border:none;">
        		  &nbsp;&nbsp;&nbsp;&nbsp;
        	</span>
        	<div class="content_c"> 
	        	<div style="height:80%;">
	        		<input class="mini-hidden"  name="panPage"  id="panPage"/>
            		<input class="mini-hidden"  value="1" name="panIndex"  id="panIndex"/>
	        		<table id="panTable" cellspacing="0" class="table_box" ></table>
	        	</div >
		        <div  class="content_p">
		                 <ul class="page_box">
		                	<li id="totalPage"></li>
		                    <a href="javascript:back()"><li class="back"></li></a>
		                    <li class="pagination" id="indexPage"></li>
		                    <a href="javascript:go()"><li class="go"></li></a>
		                </ul> 
		        </div>
        	</div>
		</div>       
        
		<div class="content_box margin_l1" >
        	<span class="title">
        		我的流程
        		<a  href="javascript:moreFlow()" style="text-decoration:none;">
	            	<span class="detail_btn">
	            		>>
	            	</span>   
            	</a>
        	</span>
        	<div class="content_c" id="myflow"> 
        		
        	</div>
		</div>
        
		<div class="content_box margin_t1">
        	<span class="title">
        		风险地图 
        	</span>
        	<div class="content_c" id="mainMap" style="height:100%;"> 
        	</div>
		</div> 
        
		<div class="content_box margin_t1 margin_l1">
        	<span class="title">
        		重点预警信号
        		<a  href="javascript:moreWarning()" style="text-decoration:none;">
	            	<span class="detail_btn">
	            		>>
	            	</span>  
	            </a> 
        	</span>
        	<div class="content_c" id="warnsign"> 
        	</div>
		</div> 
        
		<div class="content_box margin_t1 margin_l1">
        	<span class="title">
        	<a  href="javascript:myFocus()" style="text-decoration:none;"><span style="font-size:20px;color:#0650bd;">+</span>
        	</a>
        		我的关注
        		<a  href="javascript:moreFocus()" style="text-decoration:none;">
	            	<span class="detail_btn">
	            		>>
	            	</span>  
            	</a> 
        	</span>
        	<div class="content_c" id="myFocusDiv"> 
        	</div>
		</div>       
    
    </div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var root = "<%=root%>";
	var mychart;
	//$G.get("tb").load();
	
	$(function(){
	
		//加载雷达图
		findBaseDate();
		
		
	});
	
	
	function findJsMap(){
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
		$.ajax({
			url: "<%=root%>/modelview/getWarnMap",
			data:null,
			cache: false,
			async:false,
			success: function (text) {
			  datas = mini.decode(text).data;
			  orgstat = mini.decode(text).orgstat;
			  org = mini.decode(text).org;
			  //重点预警信号
			  loadImportant();
			}
		})
		//地图容器
		mychart = echarts.init(document.getElementById('mainMap'));
		//显示当前登录人所在分行地图数据
		if(orgstat == "2"){
			$.getJSON('resource/echarts/city/'+ mapcity[org] +'.json', function(data){
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
			$.getJSON('resource/echarts/jiangsu.json',function(geoJson){
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
		
		 //地图点击事件，跳转到运营全景视图-预警信息页面
		mychart.on('click', function (params) {
			if(city[params.name]){
				//window.location.href="<%=root%>/modelview/indexYj?orgnum="+city[params.name]+"stat=0";
				parent.createChialedTab({name:params.name,url:"<%=root%>/modelview/indexYj?orgnum="+city[params.name]+"&stat=0",title:params.name+"预警信息"})
			}
		});       
		
		function renderMap(map,data,stat){
			//清除上一次加载样式
			mychart.clear();
			//加载地图数据
			gycharts(map,data,stat);
		}
	}
	
	
	//绘制江苏省地图
	function gycharts(map,data,stat){
		if(stat == 1){
			mychart.setOption(option = {
					//地图背景色
					backgroundColor: '#ffffff',
					width:285,
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
	
	
	function openParentTab(name,url){
		parent.createChialedTab({name:name,url:url,title:name});
	}
	
	
	//前一页
	function back(){
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		if(panIndex >= 2){
			panIndex = parseInt(panIndex)-1;
			$.ajax({
				url: "<%=root%>/modelview/getListByOrg",
				data:{pageIndex:panIndex,querypagetype:"1"},
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var totalPage = mini.decode(text).total;
				  loadHtml(formt,totalPage,panIndex);
				}
			})
		}else{
			return;
		}
	}
	
	
	//前一页
	function go(){
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		if(panIndex < panPage){
			panIndex = parseInt(panIndex)+1;
			$.ajax({
				url: "<%=root%>/modelview/getListByOrg",
				data:{pageIndex:panIndex,querypagetype:"1"  },
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var totalPage = mini.decode(text).total;
				  loadHtml(formt,totalPage,panIndex);
				}
			})
		}else{
			return;
		}
	}
	
	//加载机构全景视图数据
	function loadPanoramic(){
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		$.ajax({
			url: "<%=root%>/modelview/getListByOrg",
			data:{panPage:panPage,pageIndex:panIndex,querypagetype:"1"},
			cache: false,
			success: function (text) {
			  var formt = mini.decode(text).data;
			  var totalPage = mini.decode(text).total;
			  loadHtml(formt,totalPage,panIndex);
			  var show = "<%=request.getAttribute("show")%>";
			  if(show!=true && show!="true"){
					//我的流程
					loadMyflow();
				}else{
					findJsMap();
				}
			}
		})
	}

	
	function loadHtml(formt,totalPage,panIndex){
		  var tab = document.getElementById("panTable");
		  var htmls = "<tr class='tr1'><td class='td1' align='center' > 机构名称</td><td class='td1' align='center'>评分</td></tr>";
		  if(formt != null &&formt.length > 0){
			  for(var i =0;i<formt.length;i++){
				  htmls += "<tr class='tr2'><td class='td2 border_l' align='center' ><a  href='javascript:openParentTab(\""+formt[i].orgname+"运营全景图\",\"<%=root%>/modelview/index?orgnum="+formt[i].deptno+"\")' style='text-decoration:none;color:#000000;' >"+formt[i].orgname+"</a></td><td class='td2 border_r' align='center'><a  href='javascript:openParentTab(\""+formt[i].orgname+"运营全景图\",\"<%=root%>/modelview/index?orgnum="+formt[i].deptno+"\")' style='text-decoration:none;color:#000000;' >"+formt[i].total+"</a></td></tr>";
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
		  }else{
			  document.getElementById("totalPage").innerHTML = "共0页,第1页  ";
			  document.getElementById("indexPage").innerHTML = 1; 
		  }
		  $(tab).html(htmls);
		  //tab.innerHTML = htmls;
	}
	
	//加载重点预警信号
	function loadImportant(){
		$.ajax({
			url: "<%=root%>/warning_search/getImportantData",
			data:null,
			cache: false,
			success: function (text) {
				 //加载我的关注
			  loadMyFocus();
			  var formt = mini.decode(text).data;
			  var sum = mini.decode(text).sum;
			  var disnum = mini.decode(text).disnum;
			  var flowDiv = document.getElementById("warnsign");
			  var htmls = "<span class='content_t'>当日重点预警信号<span class='font_c1'>"+sum+"</span>条</span>";
			  if(formt != null && formt.length > 0){
				  for(var i = 0;i<formt.length;i++){
					  htmls+= "</br><a  href='javascript:openParentTab(\""+formt[i].orgname+"重点预警\",\"<%=root%>/warning_search/toindexs?orgnum="+formt[i].deptno+"\")' style='text-decoration:none;' ><span class='content_t1'>"+formt[i].orgname+"产生"+formt[i].total+"条预警</span></a>";
				  	  if(sum >= disnum && i == disnum - 1){
						  flowDiv.innerHTML = htmls;
						  $G.parse();
						  return;
				  	  }
				  }
			  	  if(sum <=disnum){
					  flowDiv.innerHTML = htmls;
					  $G.parse();
			  	  }
			  }else{
				  flowDiv.innerHTML = htmls;
				  $G.parse();
			  }
			}
		})
	}
	
	//加载我的流程数据
	function loadMyflow(){
		$.ajax({
			url: "<%=root%>/warning_search/getWarningInfoByModel",
			data:null,
			cache: false,
			success: function (text) {
				  var indentWarn = mini.decode(text).indentWarning;
				  var checkWarn = mini.decode(text).checkWarning;
				  var approvalWarn = mini.decode(text).approvalWarning;
				  var searchCheckWarn = mini.decode(text).searchCheckWarning;
				  var flowDiv = document.getElementById("myflow");
				  var htmls = "";
				  var model =  mini.decode(text).model;
				  var headHtmls = "";
				  var total = 0;
				  //待认定
				  if(indentWarn != null && indentWarn > 0){
					  htmls+= "</br><a  href='javascript:openParentTab(\"待认定预警信息\",\"<%=root%>/warning/toindex?qryflag=1&model="+model+"\")' style='text-decoration:none;' ><span class='content_t1'>你有"+indentWarn+"笔预警信息待认定</span></a>";
				  	  total += indentWarn;
				  }
				  //待核查
				  if(checkWarn != null && checkWarn > 0){
					  htmls+= "</br><a  href='javascript:openParentTab(\"待核查预警信息\",\"<%=root%>/warning_manage/toindex?qryflag=2&model="+model+"\")' style='text-decoration:none;' ><span class='content_t1'>你有"+checkWarn+"笔预警信息待核查</span></a>";
				  	  total += checkWarn;
				  }
				  //待查复
				  if(searchCheckWarn != null && searchCheckWarn > 0){
					  htmls+= "</br><a  href='javascript:openParentTab(\"待查复预警信息\",\"<%=root%>/searchCheckRecall/toindex?qryflag=4\")' style='text-decoration:none;' ><span class='content_t1'>你有"+searchCheckWarn+"笔预警信息待查复</span></a>";
				  	  total += searchCheckWarn;
				  }
				  //待审批
				  if(approvalWarn != null && approvalWarn > 0){
					  htmls+= "</br><a  href='javascript:openParentTab(\"待审批预警信息\",\"<%=root%>/warning_approv/toindex?qryflag=3&model="+model+"\")' style='text-decoration:none;' ><span class='content_t1'>你有"+approvalWarn+"笔预警信息待复查</span></a>";
				  	  total += approvalWarn;
				  }
				  if(total == 0){
					  headHtmls = "<span class='content_t'>待处理任务<span class='font_c1'>0</span>笔</span>";
					  htmls = headHtmls + htmls;
				  }else{
					  headHtmls = "<span class='content_t'>待处理任务<span class='font_c1'>"+total+"</span>笔</span>";
					  htmls = headHtmls + htmls;
				  }
				  flowDiv.innerHTML = htmls;
				  //加载地图数据
				  findJsMap();
				  //$G.parse();
				   setTimeout(loadMyflow,1800000);
			}
		})
	}
	
	//展示更多重点预警信息（所有）
	function moreWarning(){
	var data = {
				
		};
        $G.showmodaldialog("更多重点预警展示", "<%=root%>/warning/getMoreWarn", 600, 450, data, function(action){
	    	// grid.reload();
	    });
	<%-- 	$.ajax({
			url: "<%=root%>/warning_search/getImportantData",
			data:null,
			cache: false,
			success: function (text) {
			  var formt = mini.decode(text).data;
			  var sum = mini.decode(text).sum;
			  var flowDiv = document.getElementById("warnsign");
			  flowDiv.innerHTML="";
			  var htmls = "<span class='content_t'>当日重点预警信号<span class='font_c1'>"+sum+"</span>条</span>";
			  if(formt != null && formt.length > 0){
				  for(var i = 0;i<formt.length;i++){
					  htmls+= "</br><a  href='javascript:openParentTab(\""+formt[i].orgname+"重点预警\",\"<%=root%>/warning_search/toindex?orgnum="+formt[i].deptno+"\")' style='text-decoration:none;' ><span class='content_t1'>"+formt[i].orgname+"产生"+formt[i].total+"条预警</span></a>";
				  }
				  flowDiv.innerHTML = htmls;
				  $G.parse();
			  }else{
				  flowDiv.innerHTML = htmls;
				  $G.parse();
			  }
			}
		}) --%>
	}
	
	//我的关注
	function myFocus(){
  		var data = {
				action : "index"
		};
        $G.showmodaldialog("添加/删除我的关注", "<%=root%>/myfocus_index/index", 700, 600, data, function(action){
	    	 grid.reload();
	    });
	}
	
	//我的流程  加载更多（暂时用不到）
	function moreFlow(){
		return;
	}
	
	//我的关注  加载更多
	function moreFocus(){
	var data = {
				
		};
	  $G.showmodaldialog("更多我的关注", "<%=root%>/myfocus_index/getMoreFocus", 600, 450, data, function(action){
	    	// grid.reload();
	    });
	<%-- $.ajax({
			url: "<%=root%>/modelview/getMyFocus",
			data:null,
			cache: false,
			success: function (text) {
				
			  var results = mini.decode(text).results;
			  var flowDiv = document.getElementById("myFocusDiv");
			  var formt = results.split("+");
			  var htmls = "";
			  flowDiv.innerHTML = "";
			  if(formt != null && formt.length > 0){
				  for(var i = 0;i<formt.length;i++){
					  if(i == 0){
						  htmls+= "<span class='content content_bg'>"+formt[i]+"</span>";
					  }else{
						  htmls+= "<span class='content content_bg'>"+formt[i]+"</span>";
					  }
				  }
				  flowDiv.innerHTML = htmls;
				  $G.parse();
			  }else{
				  flowDiv.innerHTML = htmls;
				  $G.parse();
			  }
			}
		}) --%>
	}
	
	//加载我的关注
	function loadMyFocus(){
		$.ajax({
			url: "<%=root%>/modelview/getMyFocus",
			data:null,
			cache: false,
			success: function (text) {
			  var data2 = mini.decode(text).data;
			  var disnum = mini.decode(text).disnum;
			  var flowDiv = document.getElementById("myFocusDiv");
			  var htmls = "";
			  if(data2 != null && data2.length > 0){
				  for(var i = 0;i<data2.length;i++){
					  if (data2[i].warncodename != undefined) {//预警
						  if (data2[i].username){
					  	  	htmls+= "<a href='javascript:openParentTab(\"预警信息查询\",\"<%=root%>/warning_search/toIndexFromMyFollowOfIndexPage?orgid="+data2[i].orgid+"&userno="+data2[i].userno+"&username="+data2[i].username+"&ywtype="+data2[i].busi_module+"&date="+data2[i].date+"&warn_name="+data2[i].mark_code+"\")' style='text-decoration:none;'><span class='content content_bg'>"+data2[i].orgname+data2[i].username+data2[i].name+"近"+data2[i].date+"天"+data2[i].warncodename+"预警："+data2[i].warningcount+"笔，确认存在问题："+data2[i].isquestioncount+"笔。"+"</span></a>";
						  } else {
					  	  	htmls+= "<a href='javascript:openParentTab(\"预警信息查询\",\"<%=root%>/warning_search/toIndexFromMyFollowOfIndexPage?orgid="+data2[i].orgid+"&ywtype="+data2[i].busi_module+"&date="+data2[i].date+"&warn_name="+data2[i].mark_code+"\")' style='text-decoration:none;'><span class='content content_bg'>"+data2[i].orgname+data2[i].name+"近"+data2[i].date+"天"+data2[i].warncodename+"预警："+data2[i].warningcount+"笔，确认存在问题："+data2[i].isquestioncount+"笔。"+"</span></a>";
						  }
					  } else {//评分
				  	  	htmls+= "<a href='javascript:openParentTab(\""+data2[i].orgname+"模块全景图\",\"<%=root%>/modelview/index?orgnum="+data2[i].orgid+"\")' style='text-decoration:none;color:#000000;'><span class='content content_bg'>"+data2[i].orgname+data2[i].ywmkname+data2[i].lastmonth+"月运营评分"+data2[i].score+"分，较上个月"+data2[i].growflag+data2[i].growrate+"%"+"<span class='rise_box'></span></span></a>";
					  }
				  	  if(data2.length >= disnum && i == disnum - 1){
						  flowDiv.innerHTML = htmls;
						  $G.parse();
						  return;
				  	  } 
				  }
			  	 if(data2.length <=disnum){
					  flowDiv.innerHTML = htmls;
					  $G.parse();
			  	  } 
			  }else{
				  flowDiv.innerHTML = htmls;
				  $G.parse();
			  }
			}
		})
	}
	
	
	//加载雷达图
  	function findBaseDate(){
  		$G.ajax({
    	  	data:{pattern:"0",querytype:"0",lengedType:"0",querypagetype:"1"},
    	  	url:"<%=root%>/modelview/getDataByModel",
    	  	success:function(text){
          	    findPatternMap(text.data);
          		//加载机构全景视图数据
        		loadPanoramic();
    	  	}
      }) 	
  	}
	
  	function findPatternMap(data){
  		var patternCharts = echarts.init(document.getElementById('rpt6'));
  		patternCharts.clear();
  		var option = {
  				title:{
  					text:''
  				},
  				series:[{
  					name:'模块评分',
  					type:'gauge',
  					detail:{formatter:'{value}'},
  					data:[{value:data,name:''}]
  				}]
  		};
  		/**
		var option = templtOption.radar1;
		option.legend.data=data.legend;
		option.radar[0].indicator=data.indicator;
		option.series[0].data=data.series;
		option.series[0].data[0]['label'] = {
							normal:{
								show:true,
								color:"#201f35",
								position:'insideRight'
							}
						};
	    **/
		patternCharts.setOption(option);
  		
	  //地图点击事件，跳转到模块全景视图-预警信息页面
		patternCharts.on('click', function (params) {
			//if(params.targetType && params.targetType == "axisName"){
				parent.createChialedTab({name:"模块全景视图",url:"<%=root%>/modelview/index?ywtypename="+params.name,title:"模块全景视图"})
			//} 
		});  
  	}
</script>
