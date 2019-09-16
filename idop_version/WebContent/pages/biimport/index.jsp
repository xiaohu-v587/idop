<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* BI数据导入主页面
	*
	* @author Liu Dongyuan
	* @date 2018-11-09
-->
	<head>
		<title>手工数据导入主界面</title>
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
 --%>
</head>

<body > 
  <div style="height:50px;">
	  <form>
		<table>
			<tr>
				<td style="padding-left: 15px; ">业务模块:</td>
				<td align="left">
					<input class="mini-combobox" width="96%" nullItemText="请选择导入业务模块" emptyText="请选择导入业务模块"
					url="<%=root%>/param/getDict?key=bi_module" id="bi_module" name="bi_module" 
					required="true"  onvaluechanged="moduleChanged" valueField="val" textField="remark" />
				</td>
				
				<td  class="form_label" style="padding-left: 15px;  ">业务类型:</td>
				<td colspan="1" id="type">
					<input name="bi_type" class="mini-combobox" id="bi_type"  valuefield="id" textfield="remark"
					 valueFromSelect="false" multiSelect="false" expandOnLoad="0" nullItemText="请选择导入业务类型" emptyText="请选择导入业务类型" allowInput="false"
					 showRadioButton="true" showFolderCheckBox="false" popupWidth="305px"   onvaluechanged="loadBiName" style="width: 96%" />
				</td>
				
				<td class="form_label" style="padding-left: 15px">数据名称:</td>
				<td colspan="1">
					<input class="mini-combobox" width="96%" nullItemText="请选择导入数据名称" emptyText="请选择导入数据名称" url=""
					id="bi_name" name="bi_name" valueField="id" textfield="remark" onvaluechanged="valueChanged" multiSelect="false"  popupWidth="350px" />
				</td>
			</tr>








				<!-- onValueChanged="querySonCd"  <td class="form_label" colspan="1">
		                	报表名称:
		            </td>
		            <td>
              			<input name="bbname" class="nui-combobox" id="bbname" style="width:255px;" nullItemText="请选择..." emptyText="请选择..."  url="" 
          	 				valueField="bb_name" textField="bb_name" required="true" onValueChanged="queryYm"/>
              		</td> -->
	        </tr>      
		 </table>
	  </form>
	  
	  	<div>
		  	<a class="nui-button" style="float:left;margin-left:6px; " onclick="click()">提交</a>
	  	</div>
	
  </div>

  <div  style="margin-top: 1%;height:100%">
 	<iframe style="margin-top: 1%;" frameborder="0" height="100%" width="100%" id="iframe" scrolling="auto">
 	
 	</iframe>
  </div>
  
  <div style="height:95%;">
  	<iframe id="myFrame" frameborder="0" name="myFrame" width="100%" height="100%"  ></iframe>
  </div>

</body>
</html>
<script type="text/javascript">

$G.parse();
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

function click(){
	var bi_name=$G.get("bi_name").getValue();
	$.ajax({
		url: "<%=root%>/biimport/getTicket?bi_name="+bi_name,
		cache: false,
		success: function (text) {
			var ticket = text.ticket;
			var biaddress = text.biaddress;
			//var str = biaddress+ticket;
			var str = biaddress+encodeURI(ticket);
			$("#iframe").attr("src",str);
		},
		error:function(){
			$G.alert("ticket错误");
		}
	})
}


function moduleChanged(e){
	var val=e.value;
	$("#iframe").attr("src","");
	if(val!='5'){
		$G.get("bi_type").setEnabled(false);
		$G.get("bi_type").setText("");
		$G.get("bi_type").setValue("");
		$G.getbyName("bi_name").setValue("");
		var url="<%=root%>/biimport/getBitypeList?val="+val;
		$G.getbyName("bi_name").setUrl(url);

	}else{
		$G.get("bi_type").setEnabled(true);
		var url="<%=root%>/biimport/getBitypeList?val="+val;
		$G.getbyName("bi_type").setUrl(url);
		loadBiName(e);
	}
	
}

function loadBiName(e){
	var val=e.value;
	$("#iframe").attr("src","");
	var bi_type=$G.get("bi_type").getValue();
	$G.getbyName("bi_name").setValue("");
	var url="<%=root%>/biimport/getBiNameList?bi_type="+bi_type;
	$G.getbyName("bi_name").setUrl(url);
}

function valueChanged(){
	$("#iframe").attr("src","");
}
</script>


