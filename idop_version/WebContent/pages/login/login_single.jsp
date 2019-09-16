<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<%@page import="com.goodcol.util.HttpClientExample"%>
<%@page import="org.jdom.*"%>
<%@page import="org.jdom.input.*"%>
<%@page import="java.io.StringReader"%>
<%@page import="org.xml.sax.InputSource"%>
<html>
<head>
<title>用户登录</title>
</head>

<%
	String ticket = request.getParameter("ticket");
	System.out.println("ticket:" + ticket);
	String sysid = request.getParameter("sysid"); //系统ID，暂时只定义科技文化银行管理系统ID为04
	System.out.println("sysid:" + sysid);
	String url = request.getParameter("returnurl"); //易慧平台跳转url
	System.out.println("returnurl:" + url);
	String manltr = request.getParameter("manltr"); //易慧平台ehr
	System.out.println("manltr:" + manltr);
	String ehr_id = null;
	if (url != null) {
%>
<body onload="javascript:toLogin();">
	<form name="loginForm" checkType="blur" method="post"
		action="login4singleInit" onsubmit="javascript:return toLogin();">
		<input type="hidden" name="userid" value="<%=manltr%>"> <input
			type="hidden" name="choose_lang" value="zh_CN"> <input
			type="hidden" name="sysid" value="<%=sysid%>"> <input
			type="hidden" name="url" value="<%=url%>">
	</form>
</body>
<script language="javascript">
	function toLogin() {
	  var today = new Date();
		var target_name = "_mainframe_" + today.getTime();
			
		var w = window.screen.availWidth - 5;
		var h = window.screen.availHeight - 50;
		var param = "menubar=no,toolbar=no,location=no,status=no,resizable=no,scrollbars=yes";
		param += ",width=" + w + "px,height=" + h + "px";
		param += ",innerWidth=" + w + "px,innerHeight=" + h + "px";
		param += ",screenX=0,screenY=0,top=0,left=0";
		var win = window.open("about:blank", target_name, param);
		//win.focus();
		loginForm.target = target_name;
		document.loginForm.submit();
		window.opener = null;
		window.close();
	}
</script>
<%
	} else {
		if (ticket != null) {
			try {
				String hruser_url1 = "http://21.200.1.60:81/bocpcommon.asmx/user_ticket_str?ticket="+ticket;
				//String hruser_url1 = "http://22.200.142.212:8000/BOCPCommon.asmx/user_ticket_str?ticket=" + ticket;
				System.out.println("hruser_url=" + hruser_url1);
				String respContentXml1 = HttpClientExample.getGetResponseWithHttpClient(hruser_url1, "utf-8");
				System.out
						.println("respContentXml1:" + respContentXml1);
				SAXBuilder builder = new SAXBuilder(false);
				StringReader sr = new StringReader(respContentXml1);
				InputSource is = new InputSource(sr);
				Document doc = builder.build(is);
				System.out.println(respContentXml1.indexOf("uid&gt;"));
				System.out.println(respContentXml1.indexOf("&lt;/uid"));
				System.out.println(respContentXml1.length());
				System.out.println();
				//System.out.println(HttpXml.getXml(ticket));
				String uid = respContentXml1.substring(
						(respContentXml1.indexOf("uid&gt;") + 7),
						respContentXml1.indexOf("&lt;/uid"));
				//respContentXml1.indexOf("uid&gt;")
				//respContentXml1.indexOf("&lt;/uid")
				//respContentXml1.substring(respContentXml1.indexOf("uid&gt;"),(respContentXml1.indexOf("&lt;/uid")-respContentXml1.indexOf("uid&gt;")-7))
				//uid&gt;025621&lt;/uid
				//String uid = doc.getRootElement().getChildText("uid");
				//Map map1=  XmlConverUtil.xmltoMap2(respContentXml1);
				System.out.println("uid:" + uid);
				//System.out.println(map1.get("uid")); 
				if (uid != null) {
					//String hruser_url = "http://22.200.142.212:8000/BOCPCommon.asmx/get_hr_userinfo_by_userid?userid=" + uid;
					String hruser_url = "http://21.200.1.60:81/BOCPCommon.asmx/get_hr_userinfo_by_userid?userid=" + uid;
					//System.out.println("hruser_url="+hruser_url);
					String respContentXml = HttpClientExample
							.getGetResponseWithHttpClient(hruser_url,
									"utf-8");
					//System.out.println("ww="+respContentXml);
					sr = new StringReader(respContentXml);
					is = new InputSource(sr);
					doc = builder.build(is);
					ehr_id = doc.getRootElement().getChildText("ehrid");
				}
				System.out.println("ehr_id=" + ehr_id);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			if (ehr_id == null) {
%>
<script language="javascript">
alert("用户EHR号为空，请重新登录!");
window.close();
</script>
<%
	} else {
%>
<body onload="javascript:toLogin();">
	<form name="loginForm" checkType="blur" method="post"
		action="login4singleInit" onsubmit="javascript:return toLogin();">
		<input type="hidden" name="userid" value="<%=ehr_id%>"> <input
			type="hidden" name="choose_lang" value="zh_CN"> <input
			type="hidden" name="sysid" value="<%=sysid%>"> <input
			type="hidden" name="ticket" value="<%=ticket%>">
	</form>
</body>
<script language="javascript">
function toLogin() {
    var today = new Date();
	var target_name = "_mainframe_" + today.getTime();
		
	var w = window.screen.availWidth - 5;
	var h = window.screen.availHeight - 50;
	var param = "menubar=no,toolbar=yes,location=no,status=no,resizable=yes,scrollbars=yes";
	param += ",width=" + w + "px,height=" + h + "px";
	param += ",innerWidth=" + w + "px,innerHeight=" + h + "px";
	param += ",screenX=0,screenY=0,top=0,left=0";
	var win = window.open("about:blank", target_name, param);
	//win.focus();
	loginForm.target = target_name;
	document.loginForm.submit();
	window.opener = null;
	window.close();
}
</script>
<%
}
}else{
%>
<script language="javascript">
alert("用户Ticket为空，请重新登录!");
window.close();
</script>
<%}
}%>
</html>
