<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>标签查询&推送</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="标签查询&推送" style="width:100%;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
    		 <div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
	               		<tr>
		                   	<th align="right">客户号：</th>
							<td align="left">
								<input name="custno" class="nui-textbox"/>   
							</td>
							<th align="right">客户名称：</th>
							<td align="left">
								<input name="custname" class="nui-textbox"/>
							</td>
							<th align="right">标签：</th>
							<td align="left">
								<input name="label_key" class="nui-textbox"/>
								<input name="label" class="nui-textbox"/>
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="export()" plain="true">导出</a>
          	     			<a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="send()" plain="true">推送</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;" url="<%=root%>/pccm_label_send/getList" multiSelect="true">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false"></div>    
			            <div field="" headerAlign="center" allowSort="true"  align="right">机构号</div> 
			            <div field="" allowSort="true" headerAlign="center" align="left">机构名称</div>                 
			            <div field="custno" allowSort="true" headerAlign="center" align="right">客户号</div>
			            <div field="custname" allowSort="true" headerAlign="center" align="center">客户名称</div> 
			           	<div field="" allowSort="true" headerAlign="center" align="left">认领客户经理</div>
			           	<div field="" allowSort="true" headerAlign="center" align="left">财务报表详情</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid1");
	var form = $G.getForm("form1");
	grid.load();
	
	function export(){
		
	}
	function send(){
		
	}
	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}
</script>

