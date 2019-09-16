<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预警信息</title>
<%-- <script src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js" type="text/javascript"></script>  --%>
<script src="<%=request.getContextPath()%>/resource/echarts/radarmap.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/echarts/linemap.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/js/panorama.js" type="text/javascript"></script>

	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/main1.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/echarts.js"></script>
<%-- 	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/jquery.min.js"></script>
 --%>	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/panorama.css">
<style type="text/css">
.nav{width:50%;float:left;}
.nav span{width:auto;height:16px;float:left;font-size:16px;line-height:15px;margin-left:10px;padding-right:10px;margin-top:8px;color:#4b4a4b;cursor:pointer;}
.content_box{width:48%;float:left;height:88%;margin-top:0.5%;}
.tr_h{height:33px}
.table_1{margin:5px;width:100%;};
.content_top{height:100px;width:100%; text-align: center;color:#F00;}
.main_box{width:100%;height:100%;}
.td1{width:8%;text-align:center;}
.td2{width:8%;text-align:center;border-bottom:2px #f0eff5 solid;background-color:#fbfbfb;}
.page_box{font-size:12px;margin-top:34px;margin-right:1%;width:200px;float:right;}
.table_box{width:98%;margin:0 auto;padding-top:10px;}
.border_l{border-left:2px #f0eff5 solid;}
.border_r{border-right:2px #fff solid;}
.tr2 td:hover{background-color:#3796e8;}
.table_1 ul{
	width:210px;
	float:left;
	margin-top:10px;
	margin:3px 0 0 5px;
	padding:0;
}
.table_1 ul li{
	float:left;
	margin:0;
	padding:0;
}
.search_li{
	width:70px;
	margin-left:5px;
}
</style>
</head>
<body>
<div class="main_box" style="overflow:auto;">
   <div class="content_box" style="width:66%;height:45%; background-color:#ffffff">
       	<div id="line1" style="height:100%;"> 
    	</div>
	</div>
	<div class="content_box" style="width:32.5%;height:45%;margin-left:0.5%; background-color:#ffffff">
       <form id="form1">
				<div class="table_1" style="margin-top:0%;margin-left:4%;font-size:10pt;">
					<ul class="tr3">	
               			<li align="right" class="search_li">业务时间：</li>
						<li align="left" >
							<input class="nui-datepicker" name="startime" id="startime" value="<%=request.getAttribute("startime") %>"/>
						</li>
					</ul>
					<ul>
						<li align="center" class="search_li">至:</li>
						<li align="left" >
							<input class="nui-datepicker" name="endtime" id="endtime" value="<%=request.getAttribute("endtime") %>"/>
						</lis>
                   </ul>
                   <ul class="tr3">	
               			<li align="right" class="search_li">机构：</li>
						<li align="left">
							<input id="orgid" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
								name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
								valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
								allowInput="false" showClose="true" 
								showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
								popupHeight="470" popupMaxHeight="600" value="<%=request.getAttribute("org") %>"/>
						</li>
					</ul>
					<ul>
               			<li align="right" class="search_li">数据类型：</li>
						<li align="left">
							<input name="yjdatatype" id="yjdatatype" class="nui-combobox" textfield="dictName" valuefield="dictID"  data="[{dictID:'0',dictName:'预警数量'},{dictID:'1',dictName:'确认问题数量'}]" value="0"  />
						</li>
                   </ul>
                   <ul class="tr3">
	                   	<li align="right" class="search_li">统计口径：</li>
						<li align="left">
							<input name="yjtjtype" id="yjtjtype" class="nui-combobox" textfield="dictName" valuefield="dictID" data="[{dictID:'0',dictName:'当前用户层级'},{dictID:'1',dictName:'下级管辖机构'}]" value="1"/>
						</li>
					</ul>
					<!-- 
					<ul>
						<li align="right" class="search_li">业务类型：</li>
						<li align="left">
							<input name="ywtype" id="ywtype" class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="dop_ywtype" value="1001" onvaluechanged="onDopQuerytype"/>
						</li>
					</ul> -->
                   <ul class="tr3" >
						<li align="right" class="search_li">预警等级：</li>
						<li align="left">
							<input name="warninglvl" id="warninglvl" class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="dop_warning_lvl" value="" />
						</li>
               		</ul>
               		<ul class="tr3" >
						<li align="right" class="search_li">预警名称：</li>
						<li align="left">
							<!-- <input  name="warningname" id="warningname" class="nui-textbox" /> -->
							<div id="warningname" name="warningname"  class="mini-autocomplete" valueField="warning_name" textField="warning_name" url="<%=root%>/panorama/markSearchs" 
				        		style="border:0;"  >
				        			<div property="columns">
				        				<div field="warning_name"  width="150" align="left"></div>
				        			</div>
				        		</div>
						</li>
               		</ul>
               		<ul class="tr3" style="height:25px;">
               			<li align="right" class="search_li"></li>
               			<li align="left">
               			</li>
               		</ul>
               		<ul class="tr3" style="height:25px;">
               			<li align="center" colspan=4>
							<a class="nui-button"  onclick="search()" style="width:20%;margin-left:200px;margin-top:-3px">查询</a>
						</li>
               		</ul>
		   		</div> 
 			</form>
	</div>
	<div class="content_box" style="width:99%;height:45%; background-color:#ffffff">
		<div class="content_c" id="mainMap2">
       			<div style="height:75%">
       				<input class="mini-hidden"  name="panPage"  id="panPage"/>
           			<input class="mini-hidden"  value="1" name="panIndex"  id="panIndex"/>
           			<input class="mini-hidden"  value="5" name="pageSize"  id="pageSize"/>
       				<table cellspacing="0" class="table_box" id="datagrid1">
       					
               		</table>
               	
               </div>
                <div style="height:10%" class="content_p">
	                 <ul class="page_box">
	                 	<li style="margin-right:20px;height:10px;"><a class="down_box" href="javascript:download()" style='text-decoration:none;' >下载</a></li>
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
  	

  	function search(){
  		var data = form.getData();
  		var tjType = $G.get("yjtjtype").getValue();
  		var yjdatatype=$G.get("yjdatatype").getValue();
  		findBarMapDate(data);
  		loadPanoramic(tjType,yjdatatype);
  	}
  	   
	function findBarMapDate(data){
  		$G.ajax({
    	  	data:data,
    	  	url:"<%=root%>/modelview/getYjBarList",
    	  	success:function(data){
    	  		option = templtOption.bar_stack;
				option.legend.data=data.legend;
				option.xAxis[0].data=data.xAxis;
				option.series=data.series;
				 for(var i=0;i<option.series.length;i++){
					if(option.series[i]['type'] == 'bar'){
						option.series[i]['stack'] = '总量';
						option.series[i]['label'] = {
							normal:{
								show:true,
								position:'insideRight'
							}
						}
						if(templtOption.ywtypeColor[i]){
							option.series[i]['itemStyle']={normal:{color:templtOption.ywtypeColor[i]}}
						}
						
					}
				} 
    	  		lineCharts.clear();
    	  		lineCharts.setOption(option);
    	  	}
      }) 	
  	}

	$(function(){
		//加载默认机构数据
		search();
	});
	
	
	
	
  //前一页
	function back(){
		var data = form.getData();
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue(); 
		//每页最大数量
		var pageSize = $G.get("pageSize").getValue();
		if(panIndex >= 2){
			panIndex = parseInt(panIndex)-1;
			data['pageIndex'] = panIndex;
			data['pageSize'] = pageSize;
			data['rwtype']='1';
			$.ajax({
				url: "<%=root%>/modelview/getYjTableList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var headers = mini.decode(text).headers;
				  var totalPage = mini.decode(text).total;
				  var tjType = $G.get("yjtjtype").getValue();
				  var yjdatatype=$G.get("yjdatatype").getValue();
				  loadHtml(headers,formt,totalPage,panIndex,tjType,yjdatatype);
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
		//每页最大数量
		var pageSize = $G.get("pageSize").getValue();
		
		if(panIndex < panPage){
			panIndex = parseInt(panIndex)+1;
			data['pageIndex'] = panIndex;
			data['pageSize'] = pageSize;
			$.ajax({
				url: "<%=root%>/modelview/getYjTableList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var headers = mini.decode(text).headers;
				  var totalPage = mini.decode(text).total;
				  var tjType = $G.get("yjtjtype").getValue();
				  var yjdatatype=$G.get("yjdatatype").getValue();
				  loadHtml(headers,formt,totalPage,panIndex,tjType,yjdatatype);
				}
			})
		}else{
			return;
		}
	}
	

	var configHeaders = [
		{field:"orgname",text:"机构名称"}
	]
	
	
	//加载机构全景视图数据
	function loadPanoramic(tjType,yjdatatype){
		var data = form.getData();
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		//每页最大数量
		var pageSize = $G.get("pageSize").getValue();
		data['panPage'] = panPage;
		data['pageIndex'] = panIndex;
		data['pageSize'] = pageSize;
		$.ajax({
			url: "<%=root%>/modelview/getYjTableList",
			data:data,
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);   
			  var headers = data.headers 
			  var formt =  data.data;
			  var totalPage = data.total;
			  loadHtml(headers,formt,totalPage,panIndex,tjType,yjdatatype);
			}
		})
	}
	

	function loadHtml(headers1,formt,totalPage,panIndex,tjType,yjdatatype){
			var grid = $("#datagrid1");
			var colName = "";
			var fieldName = "";
			var configHeader = [];
			if(tjType == '1'){
				fieldName = 'orgname';
				colName = '机构名称';
				configHeader = [
				             		{field:"orgname",text:"机构名称"}
				             	];
			}else if(tjType == '0'){
				fieldName = 'warning_name';
				colName = '问题名称';
				configHeader = [
				             		{field:"warning_name",text:"机构名称"}
				             	];
			}
			var colspan = headers1.length/2;
			var html='';
			if(yjdatatype == '1'){
				   html = '<tr class="tr1 ">'+
			       '<td class="td1 border_r" rowspan="2" style="width:10%;background:#2fa2fd;color:aliceblue;" filed="'+fieldName+'">'+colName+'</td>'+
			      // '<td class="td1 border_r" id="yacut"  style="color:aliceblue;background:#2fa2fd;"  colspan="'+colspan+'" filed="pm"> 预警数量</td>'+
			       '<td class="td1 " id="waut" style="color:aliceblue;background:#2fa2fd;"  colspan="'+colspan+'" filed="pm"> 问题数量</td>'+        
			       '</tr>'+
			       '<tr class="tr1 ">';
			}else if(yjdatatype=='0'){
				html = '<tr class="tr1 ">'+
			       '<td class="td1 border_r" rowspan="2" style="width:10%;background:#2fa2fd;color:aliceblue;" filed="'+fieldName+'">'+colName+'</td>'+
			       '<td class="td1 border_r" id="yacut"  style="color:aliceblue;background:#2fa2fd;"  colspan="'+colspan+'" filed="pm"> 预警数量</td>'+
			       '<td class="td1 " id="waut" style="color:aliceblue;background:#2fa2fd;"  colspan="'+colspan+'" filed="pm"> 问题数量</td>'+        
			       '</tr>'+
			       '<tr class="tr1 ">';
			}
		  
		   for(var j=0;j<headers1.length;j++){
				if(j+1==headers1.length){
		 				html+='<td class="td1 border_t" filed="'+headers1[j]["field"]+'">'+headers1[j]["text"]+'</td>';
		 			}else{
		 				html+='<td class="td1 border_r  border_t" filed="'+headers1[j]["field"]+'">'+headers1[j]["text"]+'</td>';
		 			}
			}
		    html+='</tr>';
			grid.html(html); 
			
			//每页最大数量
		  var pageSize =parseInt( $G.get("pageSize").getValue() );
			var headers = $G.clone(configHeader);
			for(var i=0;i<headers1.length;i++){
					headers.push(headers1[i]);
			}
		  //计算单元格数量开始绑定 宽度百分比
		  var defwidth = 100/headers.length;
		  if(formt != null &&formt.length > 0){
			  for(var i =0;i<formt.length;i++){
			  	var tr = document.createElement("tr");
					tr.setAttribute("class","tr2");
					tr.setAttribute("param",formt[i]['deptno']);
			  	for(var j=0;j<headers.length;j++){
			  			var td = document.createElement("td");
			  			if(j==0){
				  				td.setAttribute("class","td2 border_l");
			  			}else if(j+1==headers.length){
				 	 			td.setAttribute("class","td2 border_r");
			  			}else{
				  				td.setAttribute("class","td2");
			  			}
			  			td.innerHTML = formt[i][headers[j]["field"]] ;
			  			tr.appendChild(td);
			  			grid.append(tr);
			  			td = null;
			  	}
			  }
			  
		  }
		  var totalPage1 = parseInt(totalPage/pageSize);
		  if(totalPage%pageSize!=0 ){
			  totalPage1 = totalPage1+1;
		  }
		  $G.get("panPage").setValue(totalPage1);
		  document.getElementById("totalPage").innerHTML = "共"+totalPage1+"页,第"+panIndex+"页  ";
		  document.getElementById("indexPage").innerHTML = panIndex;
		  $G.get("panIndex").setValue(panIndex);
		  //$G.parse();
		  $(".tr2").bind("dblclick",{},function(text){
			    var name=text.currentTarget.firstElementChild.innerHTML;
				var param =  $(this).attr("param");
				var data = form.getData();
				data.orgid = param;
				if(data.yjtjtype=="0"){
					data.warningname=name;
				}else{
					data.warningname="";
				}
				
				data.yjtjtype = 0;
				
				openDetail(data);
		  })
	}
	
	function warnTypechange(e){
		var warning_type = $G.get("warning_type").getValue();
		$G.get("warn_name").setValue("");
		var url = "<%=root%>/complexQuery/getWarnNameList?val="+warning_type;
		$G.get("warn_name").setUrl(url);
		
	}
	function openDetail(bizParams){
		var url = "<%=root%>/modelview/toYjDetailTable";
		
        $G.showmodaldialog("预警明细", url, "60%", "70%", bizParams, function(action){
	    	 grid.reload();
	    });
	}
	function download(){
		var data = form.getData();
		data['pageSize']  = 99999999;
		data['pageIndex'] = 1;
		data['execlfilename'] = '预警信息列表';
		data['method'] = "getYjTableList";
		//callHeadAndTextToData("datagrid",data);
	    window.location="<%=root%>/modelview/download?"+connUrlByJson(data);
		
	}
</script>
