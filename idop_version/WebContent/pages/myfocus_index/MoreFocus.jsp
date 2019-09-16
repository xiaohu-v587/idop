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
        	<div class="secondDiv" id="moreMyFocusDiv" style="overflow-y:auto;height:100%"> 
        	</div>
		</div>       
        
    </div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var root = "<%=root%>";

	$(function(){
		moreFocus();
	
	});	
	
	//我的关注  加载更多
	function moreFocus(){
		$.ajax({
			url: "<%=root%>/modelview/getMyFocus",
			data:null,
			cache: false,
			success: function (text) {	
				var data2 = mini.decode(text).data;
			  var flowDiv = document.getElementById("moreMyFocusDiv");
			  var htmls = "";
			  flowDiv.innerHTML = "";
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
	}

</script>
