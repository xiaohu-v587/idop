<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title>
<script
	src="<%=request.getContextPath()%>/resource/echarts/radarmap.min.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/echarts/linemap.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/js/panorama.js"
	type="text/javascript"></script>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resource/css/main1.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resource/js/echarts.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resource/css/panorama.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
	 <script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/echarts-wordcloud.min.js"></script>
<style type="text/css">
.nav {
	width: 50%;
	float: left;
}

.nav span {
	width: auto;
	height: 16px;
	float: left;
	font-size: 16px;
	line-height: 15px;
	margin-left: 10px;
	padding-right: 10px;
	margin-top: 8px;
	color: #4b4a4b;
	cursor: pointer;
}

.content_box {
	float: left;
	margin-top: 0.5%;
}

.tr_h {
	height: 33px
}

.table_1 {
	margin: 5px;
	width: 100%;
	font-size: 10pt;
}

.content_top {
	width: 100%;
	color: #F00;
}

.main_box {
	width: 100%;
	height: 100%;
}

.td1 {
	width: 10%;
	text-align: center;
}

.td2 {
	width: 10%;
	text-align: center;
	border-bottom: 2px #f0eff5 solid;
	background-color: #fbfbfb;
}

.table_box {
	width: 98%;
	margin: 0 auto;
	padding-top: 10px;
}

tr {
	height: 25px;
}

.table_1 ul {
	width: 250px;
	float: left;
	margin-top: 6px;
}

.table_1 ul li {
	float: left;
	border: 1px red soild;
}

.search_li {
	width: 80px;
}

.page_box {
	font-size: 12px;
	margin-top: 25px;
	margin-right: 1%;
	width: 350px;
	float: right;
}

.down_box {
	text-decoration: none;
	display: block;
	width: 80px;
	height: 20px;
	background-color: #158bd1;
	color: #ffffff;
	font-size: 12px;
	text-align: center;
	line-height: 20px;
	border-radius: 2px;
}

.search_box ul {
	float: left;
	width: auto;
	margin: 5px 0 5px 0;
	padding-left: 0;
}
</style>
</head>
<body>
	<div class="main_box" style="overflow: auto;">

		<div class="content_box "
			style="width: 100%; background-color: #ffffff; height: 28%">
			<div class="content_top" id="rpt1">
				<form id="form1">

					<div class="table_1" style="width: 100%; color: #201f35;">

						<ul>
							<li align="right" class="search_li">机构名称：</li>
							<li align="left"><input id="orgid" class="mini-treeselect"
								url="<%=root%>/org/getListByUser" dataField="datas" name="orgid"
								textfield="orgname" valuefield="id" parentfield="upid"
								valueFromSelect="false" multiSelect="false" expandOnLoad="0"
								allowInput="false" showClose="true" onvaluechanged=""
								showRadioButton="true" showFolderCheckBox="false"
								popupWidth="305" popupHeight="470" popupMaxHeight="600"
								value="<%=request.getAttribute("org")%>" /></li>
						</ul>
						<ul>
							<li class="search_li" style="width: 60px; margin: 0px"
								align="right">柜员名称:</li>
							<li colspan="1" align="left"><input id="followed_teller"
								name="followed_teller" class="mini-lookup" textField="name"
								valueField="user_no" popupWidth="auto" popup="#gridPanel1"
								grid="#collarGrid" value="" text="" required="true"
								onvalidation="" onvaluechanged="" />
								<div id="gridPanel1" class="mini-panel" title="header"
									iconCls="icon-add" style="width: 380px; height: 200px;"
									showToolbar="true" showCloseButton="true" showHeader="false"
									bodyStyle="padding:0" borderStyle="border:0">
									<div property="toolbar"
										style="width: 380px; padding: 5px; padding-left: 8px; text-align: center;">
										<div style="float: left; padding-bottom: 2px;">
											<span>员工号或姓名：</span> <input id="keyText" class="mini-textbox"
												style="width: 120px;" emptyText="请输入"
												onenter="onSearchClick('collarGrid')" maxlength="20"
												vtype="maxLength:20" /> <a class="mini-button"
												onclick="onSearchClick('collarGrid')">查询</a> <a
												class="mini-button"
												onclick="onClearClick('followed_teller')">清除</a>
										</div>
										<div style="float: left; padding-bottom: 2px;">
											&nbsp;&nbsp;<a class="mini-button"
												onclick="onCloseClick('followed_teller')">关闭</a>
										</div>
										<div style="clear: both;"></div>
									</div>
									<div id="collarGrid" class="mini-datagrid"
										style="width: 380px; height: 100%;" borderStyle="border:0"
										showPageSize="false" showPageIndex="false" showPager="false"
										sortMode="client" onrowclick="">
										<div property="columns">
											<div type="checkcolumn"></div>
											<div field="org_id" visible="false"></div>
											<div field="name" align="center" headerAlign="center"
												width="90px;" allowSort="true">用户名</div>
											<div field="user_no" align="center" headerAlign="center"
												width="50px;" allowSort="true">员工号</div>
											<div field="orgname" align="center" headerAlign="center"
												width="150px;" allowSort="true">机构名称</div>
										</div>
									</div>
								</div></li>
							<li><a class="nui-button" onClick="search()"
								style="margin-left: 10px;">查询</a></li>
						</ul>
					</div>


				</form>
			</div>

			<div class="content_box"
				style="height: 20%; width: 100%; border-top: 1px #e7ebef solid;">
				<div class="content_c" id="rpt2"
					style="height: 100%; width: 100%; color: #F00;">
					<span class="title" style="margin-left: 1%"> 员工基本信息 </span>
					<table
						style="table-layout: fixed; width: 100%; height:50%; font-size: 10pt; margin-left: 1%;"
						id="areabasic">
						<tr>
							<td align='left' style='width: 20%;'>员工姓名</td>
							<td align='left'><span id="name"></span></td>

							<td align='left' style='width: 20%;'>机构</td>
							<td align='left'><span id="org"></span></td>
						</tr>
						<tr>
							<td align='left' style='width: 20%;'>生日</td>
							<td align='left'><span id="birthday"></span></td>

							<td align='left' style='width: 20%;'>性别</td>
							<td align='left'><span id="gender"></span></td>
						</tr>
						<tr>
							<td align='left' style='width: 20%;'>电话</td>
							<td align='left'><span id="phone"></span></td>

							<td align='left' style='width: 20%;'>工作时间</td>
							<td align='left'><span id="worktime"></span></td>
						</tr>
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
	var wordCloud = echarts.init(document.getElementById("wordCloud"));
	var keyText = mini.get("keyText"); 
	var form = $G.getForm("form1");
	
	$(function(){
// 		loadAreaBasic();
		findBaseData();
		loadTab();
	});

  	function search(){
  		$G.mask({
				el:document.body,
				html:'加载中...',
				cls:'mini-mask-loading',
		});
		loadAreaBasic();
		findBaseData();
		loadTab();
		$G.unmask();
  	}
  	
	function loadAreaBasic(){
		$.ajax({
			url: "<%=root%>/opPortrait/getEmpBasicInfo",
			data:form.getData(),
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);
			  var formt = data.data;
			  $('#name').text(formt.name);
			  $('#phone').text(formt.phone);
			  $('#worktime').text(formt.worktime);
			  $('#birthday').text(formt.birthday);
			  $('#gender').text(formt.gender);
			  $('#org').text(formt.org);
			}
		});
	}	
	
	function findBaseData(){
  		$G.ajax({
    	  	data: form.getData(),
    	  	url: "<%=root%>/opPortrait/getWords",
    	  	success: function(data){	  	
    	  		findWordCloud(data);
    	  	},
			error:function(text){
			}
      	});
  	}
	
	function findWordCloud(data){
		wordCloud.clear();
  		var wordCloudoption={
			tooltip:{
				show: true
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
	
function loadTab(){
		
		$.ajax({
			url: "<%=root%>/opPortrait/getEmpTab",
			data:form.getData(),
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);
			  var formt =  data.data;
			  var htmls = "";
			  if(formt.length<2){
				  for(var i=0;i<formt.length;i=i+2){
					  htmls+="<tr >"+
			                   	"<td align='center' style='width:35%;font-size: 14px'>"+formt[i].indicator_code+"</td>"+
								"<td align='center' style='width:15%;font-size: 14px'>"+
									"<span >"+formt[i].indicator_name+"</span>"+
								"</td>"+	
							"</tr>";
				  }
			  }else{
				  for(var i=0;i<formt.length;i=i+2){
					  htmls+="<tr >"+
			                   	"<td align='center' style='width:35%;font-size: 14px'>"+formt[i].indicator_code+"</td>"+
								"<td align='center' style='width:15%;font-size: 14px'>"+
									"<span >"+formt[i].indicator_name+"</span>"+
								"</td>"+	
								
	 							"<td align='left' style='width:35%;'>"+formt[i+1].indicator_code+"</td>"+
	 							"<td align='left' style='width:15%;'>"+
	 								"<span >"+formt[i+1].indicator_name+"</span>"+
	 							"</td>"+
							"</tr>";
			  	}
			  }
			  $("#tab").html(htmls);
			}
		})
	}	
	
	/**
	 * 清空检索条件时
	 */
	function onClearClick(e) {
		var lookup2 = mini.get(e);
		lookup2.deselectAll();
	}
	
	function onCloseClick(e) {
		var lookup2 = mini.get(e);
		lookup2.hidePopup();
	}
	
	function onSearchClick(e) {
		var org = $G.get("orgid").getValue();
		var grid1 = mini.get(e);
		grid1.setUrl("<%=root%>/myfocus/getAllUserList?org=" + org);
		grid1.load({
			key : keyText.value
		});
	}
</script>
