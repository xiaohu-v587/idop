<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<%@ include file="/common/nuires.jsp"%>
	    <title>手工报表效率详情</title>
		<style type="text/css">
	    	html, body{
	        	margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
	    	}
	  		.mini-panel-body{
	  			padding: 0px;
	  		}
		</style> 
	</head>
	<body>
		<div style="width:100%;height:48px;">
			<form id="form1">
		   		<table style="table-layout: fixed;" class="search_table" width="100%">
					<tr>
	                   	<th align="right">报表名称：</th>
						<td align="left">
							<input id="name" name="name" class="mini-textbox" style="width:165px;" enabled="false"/>
						</td>
						<%-- <th align="right">报送频次：</th>
						<td align="left">
							<input id="rate" name="rate" class="mini-combobox" style="width:165px;"
								textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=RATE_TYPE"
								nullItemText="请选择" emptyText="请选择"/>
						</td>
					</tr>
					<tr>
						<th align="right">截至日期：</th>
						<td align="left">
							<input id="end_time" name="end_time" class="nui-datepicker" style="width:165px;"/>
						</td> --%>
               		</tr>
		   		</table>
	   		</form>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;height:100%;" sortMode="client" allowUnselect="false"
				oncellclick=""  onselectionchanged=""  url="<%=root%>/manualReport/getTimeDetail"
			    autoEscape="false" onshowrowdetail="">
				<div property="columns">
					<!-- <div type="checkcolumn" name="checkCloumn"></div> -->
					<div headerAlign="center" type="indexcolumn" align="center">序号</div>
			        <div field="id" visible="false" width="50" headerAlign="center" allowSort="true"><fmt:message key="ID"/></div>
			        <div field="name" width="100"  allowSort="true" headerAlign="center" align="left">报表发送对象</div>
			        <div field="start_time" allowSort="true" headerAlign="center" align="left">任务开始时间</div>
			        <div field="finish_time" allowSort="true" headerAlign="center" align="left">任务完成时间</div>
                    <div field="take_time" allowSort="true" headerAlign="center" align="right" renderer="doMath">任务时长(天)</div>
				</div>
			</div>  
		</div>
	</body>
</html>
<script type="text/javascript">
     
	$G.parse();
	var grid =$G.get("datagrid1");
	var form = $G.getForm("form1");
	function setData(data){
		data = $G.clone(data);
		form.setData(data);
		grid.load({id:data.id});
	}
	
	function doMath(e){
		return ((e.value) * 1).toFixed(2);
	}
	
</script>