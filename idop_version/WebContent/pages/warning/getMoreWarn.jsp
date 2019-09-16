<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>加载更多我的关注</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
		<style type="text/css">
			html,body {
				margin: 0;
				padding: 0;
				border: 0;
				width: 100%;
				height: 100%;
				overflow: hidden;
			}
		</style>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/index.css">
</head>

<body>
	<div class="mainDiv">    
		<div class="firstDiv">
        	<div class="secondDiv" id="moreWarnDiv" style="overflow-y:auto;height:100%"> 
        	</div>
		</div>       
        
    </div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var root = "<%=root%>";

	$(function(){
		 moreWarning();
	});	
	
	//展示更多重点预警信息（所有）
	function moreWarning(){
		$.ajax({
			url: "<%=root%>/warning_search/getImportantData",
			data:null,
			cache: false,
			success: function (text) {
			  var formt = mini.decode(text).data;
			  var sum = mini.decode(text).sum;
			  var flowDiv = document.getElementById("moreWarnDiv");
			  flowDiv.innerHTML="";
			  var htmls = "<span class='content_t'>当日重点预警信号<span class='font_c1'>"+sum+"</span>条</span>";
			  if(formt != null && formt.length > 0){
				  for(var i = 0;i<formt.length;i++){
					  htmls+= "</br><a  href='javascript:openParentTab(\""+formt[i].orgname+"重点预警\",\"<%=root%>/warning_search/toindexs?orgnum="+formt[i].deptno+"\")' style='text-decoration:none;' ><span class='content_t1'>"+formt[i].orgname+"产生"+formt[i].total+"条预警</span></a>";
				  }
				  flowDiv.innerHTML = htmls;
				  $G.parse();
			  }else{
				  flowDiv.innerHTML = htmls;
				  $G.parse();
			  }
			}
		})
	}

	function openParentTab(name,url){
		parent.createChialedTab({name:name,url:url,title:name});
		CloseWindow();
		
	}
	
	function CloseWindow() { 
	    	   // window.close();	 
	    	    $G.closemodaldialog("ok");	
	    }
</script>
