<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>流程发起</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
  		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head> 
	<body>
		<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">发起测试流程</a>
	</body>
</html>
<script type="text/javascript">
	
	function add(){
		var ajaxConf = new GcdsAjaxConf();
    	ajaxConf.setSuccessFunc(function (){
        	grid.reload();
        });
    	$G.postByAjax({"processKey":"test-AAA"}, "<%=root%>/processStart/startProcess", ajaxConf);
	}
	
	
</script>

