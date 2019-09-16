<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<%@ include file="/common/nuires.jsp"%>
	    <title>手工报表效率查询</title>
		<style type="text/css">
	    	html, body{
	        	margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
	    	}
	  		.mini-panel-body{
	  			padding: 0px;
	  		}
		</style> 
	</head>
	<body >
		<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;height:78px;" showToolbar="false" showCollapseButton="false"
	    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">报表名称：</th>
							<td align="left">
								<input name="name" class="mini-textbox" style="width:222px;"/>
							</td>
							<%-- <th align="right">报送频次：</th>
							<td align="left">
								<input name="rate" class="mini-combobox" style="width:222px;"
								 	textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=RATE_TYPE"
									nullItemText="请选择" emptyText="请选择" showNullItem="true"/>
							</td> --%>
							<th align="right">截至日期：</th>
							<td align="left">
								<input name="end_time" class="nui-datepicker" style="width:222px;" valueFormat="yyyyMMdd"/>
							</td>
						</tr>
						<!-- <tr>
							<th align="right">截至日期：</th>
							<td align="left">
								<input name="end_time" class="nui-datepicker" style="width:165px;"/>
							</td>
		                    <th align="right">完成状态：</th>
							<td align="left">
								<input name="stat" class="mini-combobox" style="width:165px;"
									valueField="id" emptyText="请选择" textField="text" 
									data='[{"id":"0","text":"未完成"},{"id":"1","text":"已完成"}]'/>
							</td>
	               		</tr> -->
			   		</table>
		   		</form>
		   	</div>
		</div>
		<div class="nui-toolbar" style="border:0;padding:0px;height: 32px;">
			<table style="width:100%;text-align:center;">
				<tr>
					<td style="white-space:nowrap;">
						<a class="mini-button" iconCls="" onclick="search()">查询</a>
						<span class="separator"></span>
						<a class="nui-button" iconCls="" onclick="reset()">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;height:100%;" sortMode="client" 
			allowUnselect="false" url="<%=root%>/manualReport/getTimeList" autoEscape="false" pageSize="20">
				<div property="columns">
					<!-- <div type="checkcolumn" name="checkCloumn"></div> -->
					<div headerAlign="center" type="indexcolumn" align="center">序号</div>
			        <div field="id" visible="false" width="50" headerAlign="center" allowSort="true"><fmt:message key="ID"/></div>
			        <div field="name" width="100"  allowSort="true" headerAlign="center" align="left">报表名称</div>
			        <!-- <div field="rate_name" allowSort="true" headerAlign="center" align="left">报送频次</div> -->
                    <div field="stop_time" dataType="date" allowSort="true" headerAlign="center" align="left">截至日期</div>
                    <div field="sum_time" allowSort="true" headerAlign="center" align="right" renderer="doMath">任务总时间(天)</div>
                    <div field="avg_time" allowSort="true" headerAlign="center" align="right" renderer="doMath">平均时间(天)</div>
                    <div headerAlign="center" align="center" renderer="onRender">详情</div>
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

	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}
	
	function dateformat(e){
		var value = e.value;
		if(value) return $G.formatDate(value,'yyyy-MM-dd');
		return "";
	}
	
	//添加操作链接
	function onRender(e){
		var index = grid.indexOf(e.record);
		var op ='<a href="javascript:detail('+index+')"><font color="blue">详情</font></a>';
		return op;
	}
	
	//查看
	function detail(index){
		var record = grid.getRow(index);
		var url = "<%=root%>/manualReport/timeform";
		var bizParams = {pageType:"view",id:record.id,name:record.name,end_time:record.end_time};
        $G.showmodaldialog("手工报表效率详情", url, 800, 600, bizParams, function(action){
	    	 grid.reload();
	    });
	}
	
	function doMath(e){
		return ((e.value) * 1).toFixed(2);
	}
</script>