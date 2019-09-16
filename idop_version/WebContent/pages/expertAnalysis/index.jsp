<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>

	<head>
		<title>专家分析主界面</title>
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
					<input class="mini-combobox" width="96%" nullItemText="请选择模块" emptyText="请选择模块"
					url="<%=root%>/param/getDict?key=bi_module" id="bi_module" name="bi_module" 
					required="true"  onvaluechanged="moduleChanged" valueField="val" textField="remark" />
				</td>
				
				<td class="form_label" style="padding-left: 15px">分析名称:</td>
				<td colspan="1">
					<input class="mini-combobox" width="96%" nullItemText="请选择名称" emptyText="请选择名称" showNullItem="false" url=""
					id="page" name="page" dataField="data" valueField="id" textfield="page" onvaluechanged="valueChanged" multiSelect="false"   />
					
				</td>
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


function click(){
	var page=$G.get("page").getValue();
	$.ajax({
		url: "<%=root%>/expertAnalysis/getTicket?page="+page,
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
		$G.getbyName("page").setValue("");
		var url="<%=root%>/expertAnalysis/getPageName?val="+val;
		$G.getbyName("page").setUrl(url);

}


function valueChanged(){
	$("#iframe").attr("src","");
}
</script>


