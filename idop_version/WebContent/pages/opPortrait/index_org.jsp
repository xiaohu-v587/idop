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
	
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/panorama.css">
 <script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/echarts-wordcloud.min.js"></script>
<style type="text/css">
.nav{width:50%;float:left;}
.nav span{width:auto;height:16px;float:left;font-size:16px;line-height:15px;margin-left:10px;padding-right:10px;margin-top:8px;color:#4b4a4b;cursor:pointer;}
.content_box{float:left;margin-top:0.5%;}
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
	<div class="main_box" >
		
		<div class="content_box " style="width:100%; height:40%; background-color:#ffffff;">
        	<div class="content_top"  id="rpt1" > 
				<form id="form1">
				
			   		<div class="table_1"  style="width:100%;color:#201f35;">
					
						<ul>
							<li align="right" class="search_li">机构名称：</li>
							<li align="left">
								<input id="orgid" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
									name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
									allowInput="false" showClose="true"  onvaluechanged="onOrgidChanged" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" value="<%=request.getAttribute("org") %>"/>
							</li>
	               		</ul>
											
			   		</div>
				
		   		</form>
        	</div>
        
        	<div class="content_box" style="height:20%;width:100%; border-top:1px #e7ebef solid;">
        		<div class="content_c" id="rpt2" style="height:80%;width:100%;color:#F00;">
        		<span class="title" style="margin-left:1%">
        			机构基本信息
        		</span>
        		 <table style="table-layout: fixed;width:100%;height:50%;font-size:10px;margin-left:1%;" id="areabasic">
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
	
		<div class="content_box " id="wordCloud" style="width:59%;height:88%;margin-right:0.5%; background-color:#fff">	        	
		</div>
		
		<div class="content_box "  id="div2" style="width:40%;height:88%; background-color:#fff">
	        	 <table style="table-layout: fixed;width:100%;font-size:10px" >
	        	 <tr>
	        	 <td align="center" width="40%"  style="background-color: gray;font-size: 16px">标签内容</td>
	        	 <td align="center" width="60%" style="background-color: gray;font-size: 16px">标签描述</td>
	        	 <table style="table-layout: fixed;width:100%;font-size:10px" id="tab">
	        	 </table>
	        	 </tr>
	        	 
	        	 </table>
	        	
		</div>
		 
    </div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var root = "<%=root%>";
	var mychart; 
	
	var form = $G.getForm("form1");
	
	var wordCloud=echarts.init(document.getElementById("wordCloud"));
	
	function findBaseData(data){
  		$G.ajax({
    	  	data:data,
    	  	url:"<%=root%>/opPortrait/getBaseData",
    	  	success:function(data){	  	
    	  		findWordCloud(data);
    	  	},
			error:function(text){
			}
      })
  	}
	
	function findWordCloud(data){

  		console.log(data);
		wordCloud.clear();
  		var wordCloudoption={
			
			tooltip:{
				show:true
			},
			series:[{
				type:'wordCloud',
				textPadding:0,
				autoSize:{
					enable:true,
					minSize:6
				},
				textStyle:{
					normal:{
						color:function(){
							return 'rgb('+[
								Math.round(Math.random()*160),
								Math.round(Math.random()*160),
								Math.round(Math.random()*160)
							].join(',')+')';
						}
					},
					
				},
				
			}]
		};

			wordCloudoption.series[0].data=data.series;
			wordCloud.setOption(wordCloudoption);
	}
	

  	
	$(function(){
		var data = form.getData();
		loadAreaBasic();
		findBaseData(data);
		loadTab();
	});
	
	function onOrgidChanged(){
		var data = form.getData();
		loadAreaBasic();
		findBaseData(data);
		loadTab();
		
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
			  
			  	for(var i=0;i<formt.length;i=i+2){
				  htmls+="<tr >"+
		                   	"<td align='left' style='width:35%;font-size: 14px'>"+formt[i].remark+"</td>"+
							"<td align='left' style='width:15%;font-size: 14px'>"+
								"<span >"+formt[i].indicator_value+"</span>"+
							"</td>"+	
							
							"<td align='left' style='width:35%;font-size: 14px'>"+formt[i+1].remark+"</td>"+
							"<td align='left' style='width:15%;font-size: 14px'>"+
								"<span >"+formt[i+1].indicator_value+"</span>"+
							"</td>"+
						"</tr>";
			  }
			  
			  $("#areabasic").html(htmls);
			}
		})
	}	
	
function loadTab(){
		
		$.ajax({
			url: "<%=root%>/opPortrait/getTab",
			data:form.getData(),
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);
			  var formt =  data.data;
			  var htmls = "";
			
				  for(var i=0;i<formt.length;i++){
					  htmls+="<tr >"+
			                   	"<td align='center' style='width:40%;font-size: 14px'>"+formt[i].indicator_code+"</td>"+
								"<td align='center' style='width:60%;font-size: 14px'>"+
									"<span >"+formt[i].indicator_name+"</span>"+
								"</td>"+	
							"</tr>";
			  	}
			  	
			  
			  
			  $("#tab").html(htmls);
			}
		})
	}	
</script>
