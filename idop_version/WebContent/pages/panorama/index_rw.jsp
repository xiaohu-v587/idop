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
 --%><link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/panorama.css">
 <script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
<style type="text/css">
.nav{width:50%;float:left;}
.nav span{width:auto;height:16px;float:left;font-size:16px;line-height:15px;margin-left:10px;padding-right:10px;margin-top:8px;color:#4b4a4b;cursor:pointer;}
.content_box{width:48%;float:left;height:88%;margin-top:0.5%;}
.tr_h{height:33px}
.table_1{margin:5px;width:"100%";table-layout: fixed;};
.content_top{height:100px;width:100%; text-align: center;color:#F00;}
.main_box{width:100%;height:100%;}
.td1{width:10%;text-align:center;}
.td2{width:10%;text-align:center;border-bottom:2px #f0eff5 solid;background-color:#fbfbfb;}
.page_box{font-size:12px;margin-top:20px;margin-right:1%;width:230px;float:right;}
.table_box{width:98%;margin:0 auto;padding-top:10px;}
.search_li{width:60px}
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
		<div class="content_box" style="width:49.5%;height:50%; background-color:#ffffff">
        	<div class="content_c"  style="height:75%; background-color:#ffffff;margin-top:0.5%;">
	        		<div style="height:25px;">
	        			<div style="height:100%;padding-top:0%;">
	        				<form id="form1">				        					
			   					<div class="search_box" style="width: 98.5%;height:auto; border:0px" >
			              			<ul>
					                   	<li class="search_li" style="width: 60px;margin:0px" align="right">机构:</li>
					                   	<li align="left">
											<input id="orgid" required="true"  class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
												name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  valueFromSelect="false" multiSelect="false"
					    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="onCloseClick" showRadioButton="true" showFolderCheckBox="false"
					       						popupWidth="205" popupHeight="400" popupMaxHeight="480" onvaluechanged="onOrgChange" value="<%=request.getAttribute("org") %>"/>
										</li>
									</ul>
									<ul>
						                <li class="search_li" style="width: 60px;margin:0px" align="right">柜员名称:</li>
						                <li colspan="1"  align="left" >
											<input id="followed_teller"  name="followed_teller" class="mini-lookup"  textField="name"
												valueField="user_no" popupWidth="auto" popup="#gridPanel1" grid="#collarGrid" value="" text="" required="true"
												onvalidation="" onvaluechanged="" />
											<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width: 380px; height: 200px;"
												showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0">
												<div property="toolbar" style="width: 380px; padding: 5px; padding-left: 8px; text-align: center;">
													<div style="float: left; padding-bottom: 2px;">
														<span>员工号或姓名：</span> 
														<input id="keyText" class="mini-textbox" style="width: 120px;" emptyText="请输入" onenter="onSearchClick('collarGrid')"  maxlength="20" vtype="maxLength:20" />
														<a class="mini-button" onclick="onSearchClick('collarGrid')">查询</a>
														<a class="mini-button" onclick="onClearClick('followed_teller')">清除</a>
													</div>
													<div style="float: left; padding-bottom: 2px;"> &nbsp;&nbsp;<a class="mini-button" onclick="onClose('followed_teller')">关闭</a>
													</div>
													<div style="clear: both;"></div>
												</div>
												<div id="collarGrid" class="mini-datagrid" style="width: 380px; height: 100%;" borderStyle="border:0"
													showPageSize="false" showPageIndex="false" showPager="false" sortMode="client" onrowclick="">
													<div property="columns">
														<div type="checkcolumn"></div>
														<div field="org_id" visible="false"></div>
														<div field="name" align="center" headerAlign="center" width="90px;" allowSort="true">用户名</div>
														<div field="user_no" align="center" headerAlign="center" width="50px;" allowSort="true">员工号</div>
														<div field="orgname" align="center" headerAlign="center" width="150px;" allowSort="true">机构名称</div>
													</div>
												</div>
											</div>
						                </li>
						                </ul>
						                <ul>
						               <li class="search_li" style="width: 60px;margin:0px" align="right">日期:</li>
						                <li colspan="1" align="left">
						                	<input id="monthdate" class="nui-datepicker" format="yyyy年MM月" value="<%=request.getAttribute("month") %>" onvaluechanged="onMonthdateChanged"/>
						                </li>
			              			</ul>

			      				</div>
		   					</form>
	        			</div>
	        		</div>
	        		<div id="line1" style="height:80%;padding-top:2%;"> 
	        	</div>
         </div>
    </div>
   <div class="content_box margin_l1" style="width:49%;height:50%; background-color:#ffffff">
         <div class="content_c" id="mainMap"> 
        			<div style="height:82%;">
        				<input class="mini-hidden"  name="panPage"  id="panPage"/>
            		<input class="mini-hidden"  value="1" name="panIndex"  id="panIndex"/>
        		<table cellspacing="0" class="table_box" id="datagrid">
        			<!-- <tbody>
     						<tr class="tr1">
     							<td class="td1 border_l" field="orgname">机构名称</td>
     							<td class="td1" field="username">姓名</td>
     							<td class="td1 border_r" field="cut">问题数量</td>
     						</tr>
     						<tr class="tr2">
     							<td class="td2 border_l" field="orgname">南京分行</td>
     							<td class="td2" field="username">张三</td>
     							<td class="td2 border_r" field="cut">9</td>
     						</tr>
     						<tr class="tr2">
     							<td class="td2 border_l" field="orgname">无锡分行</td>
     							<td class="td2" field="username">李XXX</td>
     							<td class="td2 border_r" field="cut">7</td>
     						</tr>
     						<tr class="tr2">
     							<td class="td2 border_l" field="orgname">淮安分行</td>
     							<td class="td2" field="username">王XXX</td>
     							<td class="td2 border_r" field="cut">9</td>
     						</tr>
     						<tr class="tr2">
     							<td class="td2 border_l" field="orgname">徐州分行</td>
     							<td class="td2" field="username">赵XX</td>
     							<td class="td2 border_r" field="cut">9</td>
     						</tr>
     					</tbody> -->
                </table>
             </div>
             <div  class="content_p">
                 <ul class="page_box">
                 	<li style="margin-right:20px;height:10px;"><a class="down_box" href="javascript:download()" style='text-decoration:none;' >下载</a></li>
                	<li id="totalPage">共1页,第1页</li>
                    <a href="javascript:back()"><li class="back"></li></a>
                    <li class="pagination" id="indexPage">1</li>
                    <a href="javascript:go()"><li class="go"></li></a>
                </ul> 
							</div>
        	</div>
		</div>
		<div class="content_box" style="width:99%;height:300px; background-color:#ffffff">
				<div class="content_c" id="mainMap2">
      				<div style="height:72%">
      					<input class="mini-hidden"  name="panPage"  id="panPage1"/>
          			<input class="mini-hidden"  value="1" name="panIndex1"  id="panIndex1"/>
          			<input class="mini-hidden"  value="50" name="pageSize1"  id="pageSize1"/>
      				<table cellspacing="0" class="table_box" id="datagrid1">
      					<!-- <tbody>
	      					<tr class="tr1">
	      						<td class="td1 border_l" field="orgname">机构名称</td>
	      						<td class="td1" field="cut">问题数量</td>
	      						<td class="td1" field="a1001_01">智能柜台</td>
	      						<td class="td1" field="a1001_02">现金出纳</td>
	      						<td class="td1" field="a1001_03">集中核准</td>
	      						<td class="td1 border_r" field="a1001_04">柜员管理</td>
	      					</tr>
	      					<tr class="tr2">
		                	<td class="td2 border_l" field="orgname">南京分行</td>
		                	<td class="td2" field="cut">22</td>
		                	<td class="td2" field="a1001_01">7</td>
		                	<td class="td2" field="a1001_02">9</td>
		                	<td class="td2" field="a1001_03">5</td>
		                	<td class="td2 border_r" field="a1001_04">1</td>
	                	</tr>
	                	<tr class="tr2">
		                	<td class="td2 border_l" field="orgname">无锡分行</td>
		                	<td class="td2" field="cut">32</td>
		                	<td class="td2" field="a1001_01">10</td>
		                	<td class="td2" field="a1001_02">8</td>
		                	<td class="td2" field="a1001_03">10</td>
		                	<td class="td2 border_r" field="a1001_04">4</td>
	                	</tr>
	                	<tr class="tr2">
		                	<td class="td2 border_l" field="orgname">淮安分行</td>
		                	<td class="td2" field="cut">39</td>
		                	<td class="td2" field="a1001_01">12</td>
		                	<td class="td2" field="a1001_02">8</td>
		                	<td class="td2" field="a1001_03">14</td>
		                	<td class="td2 border_r" field="a1001_04">5</td>
	                	</tr>
	                	<tr class="tr2">
		                	<td class="td2 border_l" field="orgname">徐州分行</td>
		                	<td class="td2" field="cut">42</td>
		                	<td class="td2" field="a1001_01">15</td>
		                	<td class="td2" field="a1001_02">9</td>
		                	<td class="td2" field="a1001_03">13</td>
		                	<td class="td2 border_r" field="a1001_04">5</td>
	                	</tr>
	                	<tr class="tr2">
		                	<td class="td2 border_l" field="orgname">盐城分行</td>
		                	<td class="td2" field="cut">48</td>
		                	<td class="td2" field="a1001_01">15</td>
		                	<td class="td2" field="a1001_02">9</td>
		                	<td class="td2" field="a1001_03">20</td>
		                	<td class="td2 border_r" field="a1001_04">4</td>
	                	</tr>
      					</tbody> -->
                	</table>
                </div>
                 <div  class="content_p" style="margin-top:-18px;">
		                 <ul class="page_box">
		                 	<li style="margin-right:20px;height:10px;"><a class="down_box" href="javascript:download1()" style='text-decoration:none;' >下载</a></li>
		                	<li id="totalPage1">共1页,第1页</li>
		                    <a href="javascript:back1()"><li class="back"></li></a>
		                    <li class="pagination" id="indexPage1">1</li>
		                    <a href="javascript:go1()"><li class="go"></li></a>
		                </ul> 
		        		</div>
        	</div>
		</div> 
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var root = "<%=root%>";
	var mychart; 
	var keyText = mini.get("keyText"); 
	//$G.get("followed_teller").setReadOnly(false);
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
				findBarMapDate({
				orgid:$G.get("orgid").getValue(),
				monthdate:$G.get("monthdate").getValue(),
				followed_teller:$G.get("followed_teller").getValue()
				});
				loadPanoramic();
				loadPanoramic1();
		}
  
    function onOrgChange(e){
    	findBarMapDate({
    		orgid:$G.get("orgid").getValue(),
			monthdate:$G.get("monthdate").getValue(),
			followed_teller:$G.get("followed_teller").getValue()
			});
    	loadPanoramic();
    	loadPanoramic1();
    	if(e.value == null || e.value == ""){
			$G.get("followed_teller").setReadOnly(true);
		}else{
			$G.get("followed_teller").setReadOnly(false);
			$G.get("collarGrid").setData([]);
		}
    }

  	

  	
	function findBarMapDate(data){
  		$G.ajax({
    	  	data:data,
    	  	url:"<%=root%>/panorama/getRwBarList",
    	  	success:function(data){
    	  		option = templtOption.bar;
				option.legend.data=data.legend;
				option.xAxis[0].data=data.xAxis;
				option.series=data.series;
    	  		lineCharts.clear();
    	  		lineCharts.setOption(option);
    	  		$("#followed_teller").css("z-index",9999999);
    	  		$("#orgid").css("z-index",9999999);
    	  		$("#monthdate").css("z-index",9999999);
    	  	}
      }) 	
  	}

	$(function(){
		
		//加载默认机构数据
		loadPanoramic1()
		loadPanoramic();
		findBarMapDate({
			orgid:$G.get("orgid").getValue(),
			monthdate:$G.get("monthdate").getValue()
			});
	});
	
  //前一页
	function back1(){
		var data = {monthdate:$G.get("monthdate").getValue(),
				orgid:$G.get("orgid").getValue(),
				followed_teller:$G.get("followed_teller").getValue()};
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
			data['rwtype']='1';
			$.ajax({
				url: "<%=root%>/panorama/getRwTableList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var headers = mini.decode(text).headers;
				  var totalPage = mini.decode(text).total;
				  loadHtml(headers,formt,totalPage,panIndex);
				}
			})
		}else{
			return;
		}
	}
	
	
	//前一页
	function go1(){
		var data = {monthdate:$G.get("monthdate").getValue(),
				orgid:$G.get("orgid").getValue(),
				followed_teller:$G.get("followed_teller").getValue()};
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
			data['rwtype']='1';
			$.ajax({
				url: "<%=root%>/panorama/getRwTableList",
				data:data,
				cache: false,
				success: function (text) {
				  var formt = mini.decode(text).data;
				  var headers = mini.decode(text).headers;
				  var totalPage = mini.decode(text).total;
				  loadHtml1(headers,formt,totalPage,panIndex);
				}
			})
		}else{
			return;
		}
	}
	

	var configHeaders = [
		{field:"orgname",text:"机构名称"},
		{field:"cut",text:"问题数量"}
	]
	
	
	//加载机构全景视图数据
	function loadPanoramic1(){
		var data = {monthdate:$G.get("monthdate").getValue(),
				orgid:$G.get("orgid").getValue(),
				followed_teller:$G.get("followed_teller").getValue()};
		//总页数
		var panPage = $G.get("panPage1").getValue();
		//当前页
		var panIndex = $G.get("panIndex1").getValue();
		//每页最大数量
		var pageSize = $G.get("pageSize1").getValue();
		data['panPage'] = panPage;
		data['pageIndex'] = panIndex;
		data['pageSize'] = pageSize;
		data['rwtype']='1';
		$.ajax({
			url: "<%=root%>/panorama/getRwTableList",
			data:data,
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);
			  var headers = data.headers 
			  var formt =  data.data;
			  var totalPage = data.total;
			  loadHtml1(headers,formt,totalPage,panIndex);
			}
		})
	}
	

	function loadHtml1(headers1,formt,totalPage,panIndex){
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
			  			td.innerHTML= formt[i][headers[j]["field"]] || '';
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
		  $G.get("panIndex1").setValue(panIndex);
		  $G.parse();
	}
	
	
	
	
	
	 //前一页
	function back(){
			var data = {orgid:$G.get("orgid").getValue(),
				monthdate:$G.get("monthdate").getValue(),
				followed_teller:$G.get("followed_teller").getValue()};
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue(); 
		if(panIndex >= 2){
			panIndex = parseInt(panIndex)-1;
			data['pageIndex'] = panIndex;
			$.ajax({
				url: "<%=root%>/panorama/getKeynoteTaList",
				data:data,
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
			var data = {orgid:$G.get("orgid").getValue(),
				monthdate:$G.get("monthdate").getValue(),
				followed_teller:$G.get("followed_teller").getValue()};
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		
		if(panIndex < panPage){
			panIndex = parseInt(panIndex)+1;
			data['pageIndex'] = panIndex;
			$.ajax({
				url: "<%=root%>/panorama/getKeynoteTaList",
				data:data,
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
	

	var configHeaders1 = [
		{field:"orgname",text:"机构名称"},
		{field:"username",text:"姓名",render:function(e){}},
		{field:"cut",text:"问题数量",render:function(e){}}
	]
	
	
	//加载重关注名单
	function loadPanoramic(){
		var data = {orgid:$G.get("orgid").getValue(),
				monthdate:$G.get("monthdate").getValue(),
				followed_teller:$G.get("followed_teller").getValue()};
		//总页数
		var panPage = $G.get("panPage").getValue();
		//当前页
		var panIndex = $G.get("panIndex").getValue();
		data['panPage'] = panPage;
		data['pageIndex'] = panIndex;
		$.ajax({
			url: "<%=root%>/panorama/getKeynoteTaList",
			data:data,
			cache: false,
			success: function (text) {
			  var data = mini.decode(text);
			  var formt =  data.data;
			  var totalPage = data.total;
			  loadHtml(formt,totalPage,panIndex);
			}
		})
	}
	

	function loadHtml(formt,totalPage,panIndex){   
			var grid = $("#datagrid");
			grid.children().remove();
			
			var headers = $G.clone(configHeaders1);
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
			  			if(headers[j].render){
			  				td.innerHTML= headers[j].render(formt[i][headers[j]["field"]] || '');
			  			}else{
			  				td.innerHTML= formt[i][headers[j]["field"]] || '';
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
	
	/**
	 * 根据用户ID进行模糊查询
	 */
	function onSearchClick(e) {
		//var org = $G.get("followed_org").getValue();
		var grid1 = mini.get(e);
		var org = $G.get("orgid").getValue();
		grid1.setUrl("<%=root%>/myfocus/getAllUserList");
		grid1.load({ key : keyText.value,org:org});
	}
	

	function onClose(e) {
		var lookup2 = mini.get(e);
		lookup2.hidePopup();
		var value = $G.get("followed_teller").getValue();
		if(value != null && value != "" ){
			loadPanoramic();
			findBarMapDate({
	    		orgid:$G.get("orgid").getValue(),
				monthdate:$G.get("monthdate").getValue(),
				followed_teller:$G.get("followed_teller").getValue()
				});
		}
		
		
	}
	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
	
	/**
	 * 清空检索条件时
	 */
	function onClearClick(e) {
		var lookup2 = mini.get(e);
		lookup2.deselectAll();
	}
	
	
	function download(){
		var data = {
				panPage:999999999,
				pageIndex:1,
				execlfilename:'人员维度排名列表',
				method:"getKeynoteTaList"
		};
		callHeadAndTextToData("datagrid",data);
	    window.location="<%=root%>/panorama/download?"+connUrlByJson(data);
	}
	
	function download1(){
		var data = {
				panPage:999999999,
				pageIndex:1,
				rwtype:1,
				execlfilename:'人员维度列表',
				method:"getRwTableList"
		};
		callHeadAndTextToData("datagrid1",data);
	    window.location="<%=root%>/panorama/download?"+connUrlByJson(data);
	}
	
	function getFormData(){
		return  {monthdate:$G.get("monthdate").getValue(),
				orgid:$G.get("orgid").getValue(),
				followed_teller:$G.get("followed_teller").getValue()};
	}
	
</script>
