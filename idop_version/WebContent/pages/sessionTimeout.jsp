<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>

<!--
   * 超时页面
   *
   * Created on 2015年12月29日14:47:19
   * @author zgq
   * @reviewer 
-->
<html>
<head>
    <title>会话超时页面</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<script type="text/javascript">
		function redoLogin() {
			top.window.location = "<%=request.getAttribute("root")%>/";
		}
		//setTimeout("redoLogin()",3000);
	</script>
</head>

<body style="overflow: hidden; margin-top: 30px;">
<table width="100%" height="100%" border="0" align="center" cellpadding="2" cellspacing="2">
	<tr>
		<td height="100%" align="center">
			<!-- <h1>用户会话超时，请关闭窗口，重新登录系统！</h1><a href="javascript:redoLogin()"><span style="font-size:13px;">(三秒后如未跳转，请点击此链接)</span></a> -->
			<!-- <font style="color: #FFFFFF">用户会话超时，请</font><a href="javascript:redoLogin()">重新登录</a>！ -->
			<font style="">用户会话超时，请关闭窗口，重新登录系统！</font>
		</td>
	</tr>
</table>
</body>
