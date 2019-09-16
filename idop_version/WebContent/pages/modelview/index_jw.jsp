<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>机构维度表</title>
<%-- <script src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js" type="text/javascript"></script>  --%>
<script src="<%=request.getContextPath()%>/resource/echarts/radarmap.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/echarts/linemap.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/js/panorama.js" type="text/javascript"></script>

	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/main1.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/echarts.js"></script>
<%-- 	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/jquery.min.js"></script>
 --%>		<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
	
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/panorama.css">
<style type="text/css">
.nav{width:50%;float:left;}
.nav span{width:auto;height:16px;float:left;font-size:16px;line-height:15px;margin-left:10px;padding-right:10px;margin-top:8px;color:#4b4a4b;cursor:pointer;}
.content_box{width:48%;float:left;height:90%;margin-top:0.5%;}
.tr_h{height:33px}
.table_1{margin:5px;width:"100%";table-layout: fixed;};
.content_top{height:100px;width:100%; text-align: center;color:#F00;}
.td1{width:10%;text-align:center;}
.td2{width:10%;text-align:center;border-bottom:2px #f0eff5 solid;background-color:#fbfbfb;}

.page_box{font-size:12px;margin-top:20px;margin-right:1%;width:230px;float:right;}
.table_box{width:98%;margin:0 auto;padding-top:10px;}
</style>
</head>
<body>
	<div class="main_box" style="overflow:auto;">
		<div class="content_box" style="width:49%;">
        	<div class="content_c" id="mainMap2" style="height:49%;background-color:#ffffff;">
        		<div style="height:14%;width:90px;background-image:url(<%=root%>/resource/zxcss/zximage/rbl.png);background-size:100% 100%;float:left;">
        		</div>
        		<div style="height:10%;margin-top:8px;float:right;">
        			<div style="height:100%;padding-top:4%;margin-right:9px">
        				<input id="monthdate" class="nui-datepicker" format="yyyy年MM月" value="<%=request.getAttribute("month") %>" onvaluechanged="onMonthdateChanged"/>
        			</div>
        		</div>
        		<div style="height:80%">
				<input class="mini-hidden"  name="panPage"  id="panPage"/>
            	<input class="mini-hidden"  value="1" name="panIndex"  id="panIndex"/>
            	<input class="mini-hidden"  value="5" name="pageSize"  id="pageSize"/>
        		<table cellspacing="0" class="table_box" id="datagrid">
        			<tbody>
	        			<tr class="tr1">
		        			<td class="td1 border_l" field="pm">排名</td>
		        			<td class="td1" field="orgname">机构名称</td>
		        			<td class="td1" field="pm">评价</td>
		        			<td class="td1 border_r" field="qs">变动趋势</td>
	        			</tr>
        			</tbody>
                </table>
                </div>
                 <div  class="content_p">
		                 <ul class="page_box">
		                	<li id="totalPage">共1页,第1页</li>
		                    <a href="javascript:backRedBlack()"><li class="back"></li></a>
		                    <li class="pagination" id="indexPage">1</li>
		                    <a href="javascript:goRedBlack()"><li class="go"></li></a>
		                </ul> 
		        </div>
        	</div>
        	<div class="content_c"  style="height:49.5%; background-color:#ffffff;margin-top:1.5%;">
	        		<div style="height:20px;float:right;">
	        			<div style="height:100%;padding-top:4%;margin-right:9px;">
	        					<input id="orgid" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
										name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
										valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
										allowInput="false" showClose="true"  onvaluechanged="onOrgidChanged" 
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600" value="<%=request.getAttribute("org") %>"/>
	        			</div>
	        		</div>
	        		<div id="line1" style="height:80%;padding-top:4%;"> 
	        	</div>
        	</div>
        	
		</div>
		<div class="content_box margin_l1" style="width:50.5%; background-color:#ffffff;margin-left:0.5%;">
        		<div class="content_c" id="mainMap2">
        		<div style="height:90%">
        		<input class="mini-hidden"  name="panPage1"  id="panPage1"/>
            	<input class="mini-hidden"  value="1" name="panIndex1"  id="panIndex1"/>
            	<input class="mini-hidden"  value="20" name="pageSize1"  id="pageSize1"/>
            	
        		<table cellspacing="0" class="table_box" id="datagrid1">
                	<tbody>
	                	<tr class="tr1">
		                	<td class="td1 border_l" field="orgname">机构名称</td>
		                	<!-- <td class="td1" field="cut">问题数量</td> -->
		                	<td class="td1" field="a1001_01">智能柜台</td>
		                	<td class="td1" field="a1001_02">现金出纳</td>
		                	<td class="td1" field="a1001_03">集中核准</td>
		                	<td class="td1 border_r" field="a1001_04">柜员管理</td>
	                	</tr>
	                	
                	</tbody>
                </table>
                </div>
                 <div  class="content_p">
		                 <ul class="page_box">
		                 	<li style="margin-right:20px;height:10px;"><a class="down_box" href="javascript:download()" style='text-decoration:none;' >下载</a></li>
		                	<li id="totalPage1">共1页,第1页</li>
		                    <a href="javascript:backRwTable()"><li class="back"></li></a>
		                    <li class="pagination" id="indexPage1">1</li>
		                    <a href="javascript:goRwTable()"><li class="go"></li></a>
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
	
	//var form = $G.getForm("form1");
  var lineCharts = echarts.init(document.getElementById('line1'));
  //用于记录对比机构数据
  var DbArray = new Array();		
  	//var grid = mini.get("datagrid");
  	//grid.load();
	
    /*var date = new Date();
  	var month = date.getMonth()+1; //0-11 0-代表1月
  	var quarter = Math.floor((month%3 == 0 ? (month/3):(month/3+1))); //季度
  	var halyyear = month<7?0:1;
  	var year = date.getFullYear();*/
  	var dicturl = "<%=root%>/param/getKeyList?key=";
  	
	function onMonthdateChanged(e){
		loadRwTable();
		loadRedBlackList();
	}
  
    function onOrgidChanged(e){
    	findLineMapDate({orgid:e.value});
    }

  	
  	
  	var lineOption = templtOption.line_temp;
  	lineOption.title.text="";
  	
	function findLineMapDate(data){
  		$G.ajax({
    	  	data:data,
    	  	url:"<%=root%>/modelview/getJwLineList",
    	  	success:function(data){
    	  		lineOption.legend.data=data.legend;
    	  		lineOption.xAxis[0].data=data.xAxis;
    	  		lineOption.series = data.series;
    	  		lineCharts.clear();
    	  		lineCharts.setOption(lineOption);
    	  		$("#orgid").css("z-index",9999999);
    	  	}
      }) 	
  	}

	$(function(){
		
	
		loadRwTable();
		loadRedBlackList();
		findLineMapDate({orgid:$G.get("orgid").getValue()});
	});
	
  //前一页
	function backRwTable(){
		var data = {monthdate:$G.get("monthdate").getValue()};
		//总页数
		var panPage = $G.get("panPage1").getValue();
		//当前页
		var panIndex = $G.get("panIndex1").getValue(); 
		//每页最大数量
		var pageSize = $G.get("pageSize1").getValue();
		if(panIndex >= 2){
			panIndex = parseInt(panIndex)-1;
			data['pageIndex'] = panIndex;
			data['pageSize'] = pageSize;
			data['rwtype']='0';
			$.ajax({
				url: "<%=root%>/modelview/getRwTableList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var headers = mini.decode(text).headers;
				  var totalPage = mini.decode(text).total;
				  loadHtmlRwTable(headers,formt,totalPage,panIndex);
				}
			})
		}else{
			return;
		}
	}
	
	
	//前一页
	function goRwTable(){
		var data = {monthdate:$G.get("monthdate").getValue()};
		//总页数
		var panPage = $G.get("panPage1").getValue();
		//当前页
		var panIndex = $G.get("panIndex1").getValue();
		//每页最大数量
		var pageSize = $G.get("pageSize1").getValue();
		if(panIndex < panPage){
			panIndex = parseInt(panIndex)+1;
			data['pageIndex'] = panIndex;
			data['pageSize'] = pageSize;
			data['rwtype']='0';
			$.ajax({
				url: "<%=root%>/modelview/getRwTableList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var headers = mini.decode(text).headers;
				  var totalPage = mini.decode(text).total;
				  loadHtmlRwTable(headers,formt,totalPage,panIndex);
				}
			})
		}else{
			return;
		}
	}
	

	var configHeaders = [
		{field:"orgname",text:"机构名称"},
		//{field:"cut",text:"问题数量"},
	]
	
	
	//加载右侧机构维度信息
	function loadRwTable(){
		var data = {monthdate:$G.get("monthdate").getValue()};
		//总页数
		var panPage = $G.get("panPage1").getValue();
		//当前页
		var panIndex = $G.get("panIndex1").getValue();
		//每页最大数量
		var pageSize = $G.get("pageSize1").getValue();
		data['panPage'] = panPage;
		data['pageIndex'] = panIndex;
		data['pageSize'] = pageSize;
		data['rwtype']='0';
		$.ajax({
			url: "<%=root%>/modelview/getRwTableList",
			data:data,
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);
			  var headers = data.headers 
			  var formt =  data.data;
			  var totalPage = data.total;
			  loadHtmlRwTable(headers,formt,totalPage,panIndex);
			}
		})
	}
	

	function loadHtmlRwTable(headers1,formt,totalPage,panIndex){
			var grid = $("#datagrid1");
			grid.children().remove();
			//每页最大数量
		  var pageSize =parseInt( $G.get("pageSize1").getValue() );
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
			  			td.innerHTML= $G.isNull(formt[i][headers[j]["field"]])?'':formt[i][headers[j]["field"]];
			  			tr.appendChild(td);
			  			grid.append(tr);
			  	}
			  }
			  
		  }
		  var totalPage1 = parseInt(totalPage/pageSize);
		  if(totalPage%pageSize!=0 ){
			  totalPage1 = totalPage1+1;
		  }
		  $G.get("panPage1").setValue(totalPage1);
		  document.getElementById("totalPage1").innerHTML = "共"+totalPage1+"页,第"+panIndex+"页  ";
		  document.getElementById("indexPage1").innerHTML = panIndex;
		  $G.get("panIndex").setValue(panIndex);
		  $G.parse();
	}
	
	
	
	
	
	 //前一页
	function backRedBlack(){
			var data = {monthdate:$G.get("monthdate").getValue()};
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue(); 
		if(panIndex >= 2){
			panIndex = parseInt(panIndex)-1;
			data['pageIndex'] = panIndex;
			$.ajax({
				url: "<%=root%>/modelview/getRedBlackList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var totalPage = mini.decode(text).total;
				  loadHtmlRedBlack(formt,totalPage,panIndex);
				}
			})
		}else{
			return;
		}
	}
	
	
	//下一页
	function goRedBlack(){
			var data = {monthdate:$G.get("monthdate").getValue()};
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		if(panIndex < panPage){
			panIndex = parseInt(panIndex)+1;
			data['pageIndex'] = panIndex;
			$.ajax({
				url: "<%=root%>/modelview/getRedBlackList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var totalPage = mini.decode(text).total;
				  loadHtmlRedBlack(formt,totalPage,panIndex);
				}
			})
		}else{
			return;
		}
	}
	

	var configHeaders1 = [
		{field:"pm",text:"排名"},
		{field:"orgname",text:"机构名称"},
		{field:"pm",text:"评价",render:function(e){
			var html = "";
			if(e.row.pm<=3){
				html='<span class="redflag_box"></span>';
			}else if((e.total-e.row.pm) < 3 ){
				html='<span class="blackflag_box"></span>';
  			}else{
	  			html='<span">--</span>';
  			}
			
			return html;
		}},
		{field:"qs",text:"变动趋势",render:function(e){
			var html = "";
			if(e.value>0){
				html='<span class="rise_box" title="'+e.value+'"></span>'
			}else if(e.value==0){
				html='<span title="'+e.value+'">==</span>'
			}else{
				html='<span class="decline_box" title="'+e.value+'"></span>'
			}
			return html;
		}}
	]
	
	
	//加载红黑榜数据
	function loadRedBlackList(){
		var data = {monthdate:$G.get("monthdate").getValue()};
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		data['panPage'] = panPage;
		data['pageIndex'] = panIndex;
		$.ajax({
			url: "<%=root%>/modelview/getRedBlackList",
			data:data,
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);
			  var formt =  data.data;
			  var totalPage = data.total;
			  loadHtmlRedBlack(formt,totalPage,panIndex);
			}
		})
	}
	

	function loadHtmlRedBlack(formt,totalPage,panIndex){   
			var grid = $("#datagrid");
			grid.children().remove();
			
			var headers = configHeaders1;
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
			  		
			  			if(headers[j].hasOwnProperty('render')){
			  				td.innerHTML= headers[j].render({
			  					rowIndex:i+1,
			  					row:formt[i],
			  					gridlength:formt.length,
								total:totalPage,
			  					field:headers[j]["field"],
			  					value:formt[i][headers[j]["field"]],
			  					columnIndex:j
			  				});
			  			}else{
			  				td.innerHTML= $G.isNull(formt[i][headers[j]["field"]])?'':formt[i][headers[j]["field"]];
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
		var data = {
				panPage:999999999,
				pageIndex:1,
				rwtype:0,
				execlfilename:'机构维度列表',
				method:"getRwTableList"
		};
		callHeadAndTextToData("datagrid1",data);
	    window.location="<%=root%>/modelview/download?"+connUrlByJson(data);
	}
	
</script>
