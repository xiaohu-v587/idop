<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 算法管理主界面
	*
	* @author Liu Dongyuan
	* @date 2018-11-09
-->
	<head>
		<title>重点监测报表主界面</title>
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
<%-- <script src="<%=request.getContextPath()%>/common/timeTransportByBb.js" type="text/javascript"></script>
 --%><script type="text/javascript">
//解决中文参数问题
	function cjkEncode(text) {                                                                            
   if (text == null) {         
     return "";         
   }         
   var newText = "";         
   for (var i = 0; i < text.length; i++) {         
     var code = text.charCodeAt (i);          
     if (code >= 128 || code == 91 || code == 93) {  //91 is "[", 93 is "]".         
       newText += "[" + code.toString(16) + "]";         
     } else {         
       newText += text.charAt(i);         
     }         
   }         
   return newText;         
 }  
function querySonCd(){
	var parenttype = $G.get("parent").getValue();
	<%-- $.ajax({
		url : "<%=root%>/importantBbView/querySonData",
		data:{parenttype:parent_type},
		async:false,
		success : function(text) { --%>
          //	var record = $G.decode(text).data;
          	var calcombox = $G.get("bbname");
  		  	calcombox.setUrl("<%=root%>/importantBbView/querySonData?parenttype="+parenttype);
  			
		/* }
	}) */
	
}
function queryYm(){
	 	var myFrame = document.getElementById('myFrame');
	 	var bbname = $G.get("bbname").getValue();
	 	var str = "";
	 	//myFrame.removeAttribute("hidden");
	 	 $.ajax({
	 		url : "<%=root%>/importantBbView/getAllFirstLetter",
	 		data:{bbname:bbname},
	 		async:false,
	 		success : function(text) {
	 			var record = $G.decode(text).data;
	 			str = record;
	 			}
	 		}) 
	 			myFrame.src ="<%=root%>/importantBbView/queryPage?str="+str;
}
</script>
</head>

<body > 
  <div style="height:50px;">
	  <form>
		<table>
	            <tr>
		            <td class="form_label" colspan="1">
		                	报表类型:
		            </td>
	            	<td>
		              		<input name="parent" id="parent" class="nui-combobox" data="[{id:1,text:'集中核准'},{id:2,text:'现金出纳'},{id:3,text:'智能柜台'}]" 
		              				required="true" emptyText="请选择报表类型" onValueChanged="querySonCd" style="width:165px;"/>
		              		</td>
		             <td class="form_label" colspan="1">
		                	报表名称:
		            </td>
		            <td>
              			<input name="bbname" class="nui-combobox" id="bbname" style="width:255px;" nullItemText="请选择..." emptyText="请选择..."  url="" 
          	 				valueField="bb_name" textField="bb_name" required="true" onValueChanged="queryYm"/>
              		</td>
	        </tr>      
		 </table>
	  </form>
  </div>
  <div style="height:95%;">
  	<iframe id="myFrame" frameborder="0" name="myFrame" width="100%" height="100%"  ></iframe>
  </div>
  
  
</body>
</html>



