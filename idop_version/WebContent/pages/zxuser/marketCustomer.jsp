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
		<div id="panel1" class="nui-panel" title="潜在客户" style="width:100%;height:65px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">客户号：</th>
							<td align="left">
								<input id="customercode" name="customercode" class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">客户名称：</th>
							<td align="left">
								<input id="customername" name="customername" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
		                <td style="white-space:nowrap;text-align: center;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>
		                	<span class="separator"></span>
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                    <span class="separator"></span>
		                    <a class="nui-button" iconCls="icon-search" onclick="marketFeedback()">营销反馈</a>
		                </td>
		            </tr>
		       	</table>
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/zxtask/findReachCustomer" style="width: 100%;height: 100%;"
		    		multiSelect="true">
			        <div property="columns">
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false">id</div>
			            <div field="customercode" width="100" allowSort="true" headerAlign="center" align="center">客户号</div>
			            <div field="customername" width="100" allowSort="true" headerAlign="center" align="center">潜在客户</div>
			            <div field="cust_type" width="100" allowSort="true" headerAlign="center" align="center">营销客户类型</div>
			            <div field="daynum" width="100" allowSort="true" headerAlign="center" align="center" renderer="onRenderDayNum">剩余时间(天)</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();

	var grid =$G.get("datagrid1");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	
	grid.load();

	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}
	
	function onRenderDayNum(e){
		var val = e.value;
		if(val <= 0){
			e.rowStyle = "color:red;";
			return "已过期";
		}else{
			return val;
		}
	}
	
	function marketFeedback(){
		var row = grid.getSelected();
		if(row){
			var id = row.id;
			var customercode = row.customercode;
			var bizParams = {id: id, code: customercode};
			var url = "<%=root%>/zxMarket";
			$G.showmodaldialog("营销反馈", url, 500, 400, bizParams, function(action){
	       		grid.reload();
			});
		}else{
			$G.alert("请选择一行数据");
		}
	}
</script>