<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>查看更多日志信息</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head>
	<body style="padding-left: 10px;">
		<div id="panel1" class="nui-fit" style="width:100%;height:100%;">
			<c:out value="${logInfo}" default="null" escapeXml="false"></c:out>
	   	</div>
	   	<div id="lastReadLine"></div>
	   	<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
			<table style="width:100%;">
				<tr>
					<td style="width:100%;padding-left: 700px;">
						<a class="mini-button" iconCls="icon-search" id="previewMore" onclick="previewMore()" plain="true">加载更多</a>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
<script type="text/javascript">
$G.parse();
$("#lastReadLine").hide();
$("#lastReadLine").html("${readline}");
var number=0;
function previewMore(){
	number++;
	var ajaxConf = new GcdsAjaxConf();
	 ajaxConf.setIsShowProcessBar(true);
	 ajaxConf.setIsShowSuccMsg(false);
	 ajaxConf.setSuccessFunc(function (text){
		 var logInfo=text.loginfoMore.split("&&");
		 $("#lastReadLine").html(logInfo[0]);
		 if(""==logInfo[1]){
			 $G.alert("无更多日志信息！");
		 }else if("<h2>未找到相关日志文件!</h2>"==logInfo[1]){
			 $G.alert("未找到相关日志文件!");
		 }else{
			 $("#panel1").append(logInfo[1]);
		 }
    });
	$G.postByAjax({"number":number}, "<%=root%>/log/viewMore?fileName="+"${fileName}"+"&uniCode="+"${uniCode}"+"&lastreadline="+$("#lastReadLine").html(), ajaxConf);
}
</script>