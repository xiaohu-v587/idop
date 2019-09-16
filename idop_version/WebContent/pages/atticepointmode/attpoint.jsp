<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>网点首页</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/main1.css">
   		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/attpoint.css">
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		
  		</style>
</head>

<body>
	<div class="first_box" >
	<div class="main_box" style="height: 43%">
		<div class="content_box margin_l1" style="width: 32.5%">
        	<span class="title">
        		预警信号
        		<a  href="javascript:moreWarnSignal()" style="text-decoration:none;">  <!-- javascript:moreWarnSignal() -->
	            	<span class="detail_btn">
	            		>>
	            	</span>   
            	</a>
        	</span>
        	<div class="content_c" id="WarnSignal" > 	
        	</div>
		</div>
        
        <div class="content_box  margin_l1" style="width: 32.5%;">
        	<span class="title">
        	<a  href="javascript:MyFocu()" style="text-decoration:none;"><span style="font-size:20px;color:#0650bd;">+</span></a>
        		我的关注
        		<a  href="javascript:moreMyFocus()" style="text-decoration:none;">
	            	<span class="detail_btn">
	            		>>
	            	</span>  
            	</a> 
        	</span>
        	<div class="content_c" id="MyFocus"> 
        	</div>
		</div>  
		
		<div class="content_box margin_l1" style="width: 32.5%">
        	<span class="title">
        		我的流转
        		<a  href="javascript:moreMyFlow()" style="text-decoration:none;">
	            	<span class="detail_btn">
	            		>>
	            	</span>   
            	</a>
        	</span>
        	<div class="content_c" id="MyFlows"> 
        	</div>
		</div> 
		
	</div> 
	
		<div class="second_box " style="height:28%">
        	<span class="title">
        	
        		常用业务网站
        
        	</span>
        	<div class="content_c" id="comBusNet"  style=" padding-top:2%;height:98%;"> 
        	</div>
		</div>
		
		<div class="second_box" style="height:28%;">
        	<span class="title">
        		常用功能
        	</span>
        	<div class="content_c" id="comFunction" style="padding-top:2%; height:98%;top:80%"> 
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
		//加载预警信号
		loadWarnSignal();
		
			
	});
	
	
	//常用业务网站
	
	 function loadComBusNet(){
			$.ajax({
				url: " <%=root%>/mycombusnet/getComBusNetData",  
				data:null,
				cache: false,
				success: function (text) {
					//常用功能
				 loadComFunction();
				  var formt = mini.decode(text).data;
				  var sum = mini.decode(text).sum;
				  var disnum = mini.decode(text).disnum;
				  var flowDiv = document.getElementById("comBusNet");
				  var htmls = " ";
				  if(formt != null && formt.length > 0){
					  for(var i = 0;i<formt.length;i++){
						  //<img border="0" width="20px;" height="20px;" src="resource/image/"+formt[i].networkname+".png" />
						 // htmls+= "&nbsp;&nbsp;&nbsp;&nbsp;<a  href='"+formt[i].webaddress+"' style='text-decoration:none;' ><button>"+formt[i].networkname+"</button></a>";
						var n=(i+1)%6;
						if(n==0){
							n=6;
						}
						
						var lens=2+8.5*i;
						
		//					var imgurl=encodeURI("../idop/resource/image/多功能图标"+n+".png");
		//					htmls+= "&nbsp;&nbsp;&nbsp;&nbsp;<a  href='"+formt[i].remark+"'  target='_blank' style='text-decoration:none;' ><span  style=' position: absolute;top:63%; left:"+lens+"%; width: 85px;margin-left: 0px; color: black;'>"+formt[i].name+"</span><img border='0' width='100px;' height='100px;' src='"+imgurl+"'/>&nbsp;&nbsp;</a>";
		 				var imgurl=encodeURI("../idop/resource/image/"+formt[i].name+".png");
						 htmls+= "&nbsp;&nbsp;&nbsp;&nbsp;<a  href='"+formt[i].remark+"'  target='_blank' style='text-decoration:none;' ><img border='0' width='120px;' height='120px;' src='"+imgurl+"'/>&nbsp;&nbsp;</a>";
							 					 
						
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
	
	//添加常用业务网站
	function comBuinessNet(){
  		var data = {
				action : "index"
		};
        $G.showmodaldialog("添加/删除常用业务网站", "<%=root%>/mycombusnet/index", 700, 600, data, function(action){
	    	 grid.reload();
	    });
	}
	//常用功能
	function loadComFunction(){
		$.ajax({
			url: "<%=root%>/mycombusnet/getComFunctionData",
			data:null,
			cache: false,
			success: function (text) {
				//我的流转
				loadMyflow();
			  var formt = mini.decode(text).data;
			 
			  var flowDiv = document.getElementById("comFunction");
			  var htmls = " ";    
			  if(formt != null && formt.length > 0){
			  	
				  for(var i = 0;i<formt.length;i++){
					  var strurl=formt[i].url;
					  if(strurl == null || strurl == '') {
			            
			            	$G.GcdsAlert("未配置菜单URL");
			            	return;
			            } else if(strurl.indexOf("/") != 0) {
			            	strurl = "/" + strurl;
			            }					 
					  var url="<%=root%>"+strurl;
					  var len=2;
					  var lens=len+8*i;
					  //<img border='0' width='100px;' height='100px;' src='../resource/image/"+formt[i].networkname+".png'/>&nbsp;&nbsp;
					  //htmls+= "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:openParentTab(\""+formt[i].name+"\",\""+url+"\")'  style='text-decoration:none;' >"+formt[i].name+"</a>";
					var n=(i+1)%6;
					
						
					  htmls+= "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:openParentTab(\""+formt[i].name+"\",\""+url+"\")'  style='text-decoration:none;' >"+
					  "<span  style='text-align:center; position: absolute;margin-top:85px; width: 120px;margin-left: 0px; color: white;'>"+formt[i].name+"</span><img border='0' width='120px;' height='120px;' src='../idop/resource/image/多功能图标"+n+"v2.png'/>&nbsp;&nbsp;</a>";
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
   
	//加载预警信号
	function loadWarnSignal(){
		$.ajax({
			url: "<%=root%>/mycombusnet/getWarnSignalData",
			data:null,
			cache: false,
			success: function (text) {
			//加载我的关注
				loadMyFocus();
			  var formt = mini.decode(text).data;
			  var sum = mini.decode(text).sum;
			  var disnum = mini.decode(text).disnum;
			  var flowDiv = document.getElementById("WarnSignal");
			  var htmls = "<span class='content_t'>当日重点预警信号<span class='font_c1'>"+sum+"</span>条</span>";
			  if(formt != null && formt.length > 0){
				  for(var i = 0;i<formt.length;i++){
					  htmls+= "</br><a  href='javascript:openParentTab(\""+formt[i].orgname+"重点预警\",\"<%=root%>/AddComBusNet/toindex?orgnum="+formt[i].deptno+"\")' style='text-decoration:none;' ><span class='content_t1'>"+formt[i].orgname+"产生"+formt[i].total+"条预警</span></a>";
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
	

	//展示更多预警信号（所有）
	function moreWarnSignal(){
	  var data = {
				
		};
        $G.showmodaldialog("更多重点预警展示", "<%=root%>/warning/getMoreWarn", 600, 450, data, function(action){
	    	// grid.reload();
	    });
<%-- 		$.ajax({
			url: "<%=root%>/mycombusnet/getWarnSignalData",
			data:null,
			cache: false,
			success: function (text) {
			  var formt = mini.decode(text).data;
			  var sum = mini.decode(text).sum;
			  var flowDiv = document.getElementById("WarnSignal");
			  flowDiv.innerHTML="";
			  var htmls = "<span class='content_t'>当日重点预警信号<span class='font_c1'>"+sum+"</span>条</span>";
			  if(formt != null && formt.length > 0){
				  for(var i = 0;i<formt.length;i++){
					  htmls+= "</br><a  href='javascript:openParentTab(\""+formt[i].orgname+"重点预警\",\"<%=root%>/AddComBusNet/toindex?orgnum="+formt[i].deptno+"\")' style='text-decoration:none;' ><span class='content_t1'>"+formt[i].orgname+"产生"+formt[i].total+"条预警</span></a>";
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
	//加载我的流转数据
	function loadMyflow(){
		$.ajax({
			url: "<%=root%>/mycombusnet/getWarningInfo",
			data:null,
			cache: false,
			success: function (text) {
				  var indentWarn = mini.decode(text).indentWarning;
				  var checkWarn = mini.decode(text).checkWarning;
				  var approvalWarn = mini.decode(text).approvalWarning;
				  var searchCheckWarn = mini.decode(text).searchCheckWarning;
				  var djbdfh = mini.decode(text).djbdfh;
				  var flowDiv = document.getElementById("MyFlows");
				  var htmls = "";
				  var headHtmls = "";
				  var total = 0;
				  //待认定
				  if(indentWarn != null && indentWarn > 0){
					  htmls+= "</br><a  href='javascript:openParentTab(\"待认定预警信息\",\"<%=root%>/warning/toindex?qryflag=1\")' style='text-decoration:none;' ><span class='content_t1'>你有"+indentWarn+"笔预警信息待认定</span></a>";
				  	  total += indentWarn;
				  }
				  //待核查
				  if(checkWarn != null && checkWarn > 0){
					  htmls+= "</br><a  href='javascript:openParentTab(\"待核查预警信息\",\"<%=root%>/warning_manage/toindex?qryflag=2\")' style='text-decoration:none;' ><span class='content_t1'>你有"+checkWarn+"笔预警信息待核查</span></a>";
				  	  total += checkWarn;
				  }
				  //待审批
				  if(approvalWarn != null && approvalWarn > 0){
					  htmls+= "</br><a  href='javascript:openParentTab(\"待审批预警信息\",\"<%=root%>/warning_approv/toindex?qryflag=3\")' style='text-decoration:none;' ><span class='content_t1'>你有"+approvalWarn+"笔预警信息待复查</span></a>";
				  	  total += approvalWarn;
				  }
				   //待查复
				  if(searchCheckWarn != null && searchCheckWarn > 0){
					  htmls+= "</br><a  href='javascript:openParentTab(\"待查复预警信息\",\"<%=root%>/searchCheckRecall/toindex?qryflag=4\")' style='text-decoration:none;' ><span class='content_t1'>你有"+searchCheckWarn+"笔预警信息待查复</span></a>";
				  	  total += searchCheckWarn;
				  }
			
				   //登记簿待复核
				  if(djbdfh != null && djbdfh > 0){
					  htmls+= "</br><a  href='javascript:openParentTab(\"待复核登记簿信息\",\"<%=root%>/djbapprove/index?qryflag=4\")' style='text-decoration:none;' ><span class='content_t1'>你有"+djbdfh+"条登记簿数据待复核</span></a>";
				  	  total += djbdfh;
				  }
				  if(total == 0){
					  headHtmls = "<span class='content_t'>待处理任务<span class='font_c1'>0</span>笔</span>";
					  htmls = headHtmls + htmls;
				  }else{
					  headHtmls = "<span class='content_t'>待处理任务<span class='font_c1'>"+total+"</span>笔</span>";
					  htmls = headHtmls + htmls;
				  }
				  flowDiv.innerHTML = htmls;
				  //$G.parse();
				   setTimeout(loadMyflow,1800000);
			}
		})
	}
	
	//加载所有我的流转
	function moreMyFlow(){
		loadMyflow();
	}
	
	function openParentTab(name,url){
		parent.createChialedTab({name:name,url:url,title:name});
	}
	
	//我的关注
	function MyFocu(){
  		var data = {
				action : "index"
		};
         $G.showmodaldialog("添加/删除我的关注", "<%=root%>/atticepointfocus/index", 700, 600, data, function(action){
	    	 grid.reload();
	    }); 
	}
	

	
	//我的关注  加载更多
	function moreMyFocus(){
		
      var data = {
				
		};
	  $G.showmodaldialog("更多我的关注", "<%=root%>/atticepointfocus/getMoreFocus", 600, 450, data, function(action){
	    	// grid.reload();
	    });
	<%-- 	$.ajax({
			url: "<%=root%>/mycombusnet/getMyFocus",
			data:null,
			cache: false,
			success: function (text) {
				
			  var results = mini.decode(text).results;
			  var flowDiv = document.getElementById("MyFocus");
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
			url: "<%=root%>/mycombusnet/getMyFocus",
			data:null,
			cache: false,
			success: function (text) {
			//常用业务网站
			loadComBusNet();
			var data2 = mini.decode(text).data;
			  var disnum = mini.decode(text).disnum;
			  var flowDiv = document.getElementById("MyFocus");
			  var htmls = "";
			  if(data2 != null && data2.length > 0){
				  for(var i = 0;i<data2.length;i++){
					  if (data2[i].warncodename != undefined) {//预警
						  if (data2[i].username){
					  	  	htmls+= "<a href='javascript:openParentTab(\"预警信息查询\",\"<%=root%>/warning_search/toIndexFromMyFollowOfIndexPage?orgid="+data2[i].orgid+"&userno="+data2[i].userno+"&username="+data2[i].username+"&ywtype="+data2[i].busi_module+"&date="+data2[i].date+"&warn_name="+data2[i].mark_code+"\")' style='text-decoration:none;'><span class='content content_bg'>"+data2[i].orgname+data2[i].username+data2[i].name+"近"+data2[i].date+data2[i].warncodename+"预警："+data2[i].warningcount+"笔，确认存在问题："+data2[i].isquestioncount+"笔。"+"</span></a>";
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
 
  
</script>
