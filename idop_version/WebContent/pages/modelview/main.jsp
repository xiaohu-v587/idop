<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title>
<%-- <script src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js" type="text/javascript"></script>  --%>
<script src="<%=request.getContextPath()%>/resource/echarts/radarmap.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/echarts/linemap.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/main1.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/echarts.js"></script>
<%-- 	
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/jquery.min.js"></script>
 --%>
 <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/index.css">
 <script type="text/javascript" src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js"></script>
<style type="text/css">
.nav{width:50%;float:left;}
.nav_box{width:99%;height:42px;background-color:#ffffff;margin-top:0.5%;padding-top:10px;}
.nav span{width:auto;height:16px;float:left;font-size:16px;line-height:15px;margin-left:10px;padding-right:10px;margin-top:8px;color:#4b4a4b;cursor:pointer;}
.content_box{width:100%;float:left;height:100%;}
.tr_h{height:33px}
.table_1{margin:5px;width:"100%";table-layout: fixed;};
.content_top{height:100px;width:100%; text-align: center;color:#F00;}
.main_box{width:100%;height:100%;}
.td1{width:10%;text-align:center;}
.td2{width:10%;text-align:center;border-bottom:2px #f0eff5 solid;background-color:#fbfbfb;}
.tr1{width:100%;height:30px;margin:0 auto;background-color:#e1e7f3;color:#4b4a4b;font-size:16px;}
.tr2{width:100%;height:30px;margin:0 auto;border:#e1e7f3 2px solid;color:#484a4c;font-size:14px;}
.page_box{font-size:12px;margin-top:20px;margin-right:1%;width:150px;float:right;}
.table_box{width:98%;margin:0 auto;padding-top:10px;}
.iframe_c{width:100%;height:100%;}
.clickclass{}
</style>
</head>
<body>
	<div class="main_box" >
    	<div class="nav_box">
            <div class="nav" >
            	<span class="border_r clickclass" name="yyqj" url="<%=root %>/modelview/indexYq?orgnum=<%=request.getAttribute("org") %>">
                	模块全景视图
                </span>
            	<span class="border_r clickclass" name="jgwd" url="<%=root %>/modelview/indexJw?orgid=<%=request.getAttribute("org") %>">
                	机构维度
                </span>
            	<span class="border_r clickclass" name="rywd" url="<%=root %>/modelview/indexRw?orgnum=<%=request.getAttribute("org") %>">
                	人员维度
                </span>
            	<span class="clickclass" name="yjxi" url="<%=root %>/modelview/indexYj?orgnum=<%=request.getAttribute("org") %>">
                	预警信息
                </span>
               
            </div>
             <span class="ywmk"  style="float:right;">
                                          当前业务模块：<span  id="busimode"  name="busimode" class="dqywmk" ></span>
                	<select name="model" id="model" class="nui-combobox"   onvaluechanged="onvalueChenaged"  nullItemText="切换模块" emptyText="切换模块" >
								<option value = '1001_01'>智能柜台</option>
								<option value = '1001_02'>现金出纳</option>
								<option value = '1001_03'>集中核准</option>
								<option value = '1001_04'>柜员管理</option>
                  	</select>
                </span>
        </div>
        <div class="content_box" >
        	<iframe id="main" class="iframe_c" frameborder="0" src="<%=root %>/modelview/indexYq?orgnum=<%=request.getAttribute("org") %>">
        	</iframe>
        </div>
    </div>
</body>
</html>
<script type="text/javascript">
$G.parse();
	$("[name='yyqj']").css("color","#3796e8");
	$(".clickclass").bind("click",{},function(e){
		var cname =  $(this).attr("name");
		var url = $(this).attr("url");
		var src = $("#main").attr("src");
		if(src != url){
			$("#main").attr({"src":url});	
		}
		//移除样式
		$(this).siblings('span').css("color","#4b4a4b");
		$(this).css("color","#3796e8");
		
	})
	
	//获取业务模块
	var models="<%=request.getAttribute("model")%>";
   
	$G.getbyName("model").setValue(models);
    /* var text=$G.getbyName("model").getText();
	 $("#busimode").html(text); */
	 
	//切换业务模块 
   function onvalueChenaged(){
	   $("#busimode").html("");
	   var textvalue=$G.getbyName("model").getText();
	   var model=$G.getbyName("model").getValue();
	  // $("#busimode").html(textvalue);
     
		$.ajax({
			url: "<%=root%>/zxuser/updateMode?model="+model,
            success: function (text) {
            	if( text.flag ){
            		nui.alert("修改完成后请刷新页面！");
            	}else{
            		nui.alert("修改失败！");
            		return;
            	}
           	},
           	error:function(){
           		nui.alert("更新失败！");
           		return;
           	}
    	});
	   
   }
	
</script>
