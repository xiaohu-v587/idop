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
		                   	<th align="right">客户经理：</th>
							<td align="left">
								<input id="userName" name="userName" class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">机构：</th>
							<td align="left">
								<input id="orgId" class="nui-treeselect" url="<%=root%>/org/getList" dataField="datas" 
						 			name="orgId" textfield="orgname" valuefield="id" parentfield="upid"  
						 			valueFromSelect="false" multiSelect="false" expandOnLoad="0" popupHeight="470"
				 					allowInput="false" popupMaxHeight="600" style="width:165px;" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"/>
							</td>
	               		</tr>
	               		<tr>
		                    <th align="right">EHR号：</th>
							<td align="left">
								<input id="userNo" name="userNo" class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">期数：</th>
							<td align="left">
								<input id="period" name="period" class="mini-combobox" style="width:165px;" valuefield="period" textfield="period" emptyText="请选择..."
									url="<%=root%>/pccm_kpi_param/getPeriod"  allowInput="false" showNullItem="true" nullItemText="请选择..."/>
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
		                    <a class="nui-button" iconCls="icon-search" onclick="approval()">导出</a>
		                </td>
		            </tr>
		       	</table>
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/pccm_kpi_param/getAllKPIList" style="height: 100%;"
		    		multiSelect="true">
			        <div property="columns">
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="period" allowSort="true" headerAlign="center" width="60px" align="center">期次</div>
			            <div field="orgname" allowSort="true" headerAlign="center" align="center">所属机构</div>
			            <div field="user_no" allowSort="true" headerAlign="center" width="60px" align="center">EHR号</div>
			            <div field="name" allowSort="true" headerAlign="center" width="80px" align="center">客户经理姓名</div>
			            <div field="position_name" allowSort="true" headerAlign="center" align="center">岗位</div>
			            <div field="" allowSort="true" headerAlign="center" width="40px" align="center">级别</div>
			            <div field="name" allowSort="true" headerAlign="center" width="125px" align="center">客户经理专业资格等级</div>
			            <div field="kpi" allowSort="true" headerAlign="center" width="60px" align="center">KPI值</div>
			            <div field="orgname" allowSort="true" headerAlign="center" align="center">平移标准</div>
			            <div field="name" allowSort="true" headerAlign="center" align="center">晋升标准</div>
			            <div field="rolename" allowSort="true" headerAlign="center" align="center">下一等级中位值</div>
			            <div field="sort_num" allowSort="true" headerAlign="center" width="65px" align="center">同等级排名</div>
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