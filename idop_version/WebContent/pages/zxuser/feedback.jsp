<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>潜在客户</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div class="nui-fit">
			<form id="form1" method="post">
	        <input name="id" class="nui-hidden" />
	       	<div style="padding-top: 10px;">
				<table style="table-layout:fixed;" align="center">
					<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<h2>营销反馈</h2>
		              	</td>
	              	</tr>
					<tr>
						<td align="right">客户号：</td>
						<td align="left">
	                   		<input name="customercode" id="customercode" class="nui-textbox" style="width:200px;"/>
	                  	</td>
	              	</tr>
	              	<tr>
	                  	<td align="right">状态：</td>
	                  	<td align="left">
	                   		<input name="status" class="nui-dictcombobox" valueField="id" textField="text"
							       data='[{"id":"1","text":"待营销"},
							       {"id":"2","text":"营销中"},
							       {"id":"3","text":"营销成功"},
							       {"id":"4","text":"营销失败"}]' style="width:200px;"/>
	                  	</td>
	              	</tr>
	              	<tr>
						<td align="right">时间：</td>
	                  	<td align="left">
							<input id="marketDate" name="marketDate"  class="mini-datepicker" allowInput="false" style="width:200px;"/>
						</td>
					</tr>
					<tr>
						<td align="right">备注：</td>
						<td align="left">
							<input name="remark" class="nui-textarea" style="width:200px;"/>
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<a class="nui-button" iconCls="icon-save" onclick="save" style="color: black;">确定</a>
		              	</td>
	              	</tr> 
				</table>
			</div>
			</form>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	
	//标准方法接口定义
	function setData(data) {
		var info = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
		$G.getbyName("id").setValue(info.id);
		$G.getbyName("customercode").setValue(info.code);
	}
	
	function save(){
		var id = $G.getbyName("id").getValue();
		var customercode = $G.getbyName("customercode").getValue();
		var status = $G.getbyName("status").getValue();
		var marketDate = $G.getbyName("marketDate").getValue();
		var remark = $G.getbyName("remark").getValue();
		$.ajax({
			url: "<%=root%>/zxMarket/addMarketing",
			data: {id:id, customercode:customercode, status:status, marketDate:marketDate, remark: remark},
            success: function (text) {
            	if(text.flag){
            		$G.alert("操作成功","提示",function(){
            			$G.closemodaldialog("ok");
            		});
            	}else{
            		$G.alert("操作失败!");
            	}
            }
		});
	}
</script>