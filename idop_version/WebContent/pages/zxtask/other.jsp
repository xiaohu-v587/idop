<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>其他客户</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="其他客户" style="width:100%;height:100px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">客户号：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">客户名称：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
	               		<tr>
		                    <th align="right">标签：</th>
							<td align="left">
								<input style="width:100px;" class="nui-dictcombobox" name="applyStatus" 
									valueField="id" emptyText="请选择" textField="text" 
									data='[{"id":"-1","text":"未审核"},{"id":"0","text":"未通过"},{"id":"1","text":"通过"}]'/>
								<input style="width:100px;" class="nui-dictcombobox" name="applyStatus" 
									valueField="id" emptyText="请选择" textField="text" 
									data='[{"id":"-1","text":"未审核"},{"id":"0","text":"未通过"},{"id":"1","text":"通过"}]'/>
							</td>
							<th align="right"></th>
							<td align="left"></td>
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
		                    <a class="nui-button" iconCls="icon-search" onclick="approval()">导出</a>
		                </td>
		            </tr>
		       	</table>
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/zxrole/getApplyRoleList" style="width: 100%;height: 100%;"
		    		multiSelect="true">
			        <div property="columns">
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="name" width="100" allowSort="true" headerAlign="center" align="center">潜在客户</div>
			            <div field="rolename" width="100" allowSort="true" headerAlign="center" align="center">标签</div>
			            <div field="orgname" width="100" allowSort="true" headerAlign="center" align="center">详情</div>
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

</script>