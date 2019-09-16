<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>KPI重跑数据</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="KPI参数设置" style="width:100%;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="runKpI()" plain="true">重跑</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;" url="<%=root%>/pccm_kpi_run/getList" multiSelect="true">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false"></div>    
			            <div field="quratz_status" allowSort="true" headerAlign="center" align="left">跑批状态</div>            
			            <div field="run_time" width="150" allowSort="true" headerAlign="center" align="right">重跑月份</div>
			            <div field="start_time" allowSort="true" headerAlign="center" align="left" dateFormat="yyyy-MM-dd HH:mm:ss">开始时间</div>
			            <div field="end_time" allowSort="true" headerAlign="center" align="left" dateFormat="yyyy-MM-dd HH:mm:ss">结束时间</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid1");
// 	var form = $G.getForm("form1");
	grid.load();
	//重跑
	function runKpI() {
        var url = "<%=root%>/pccm_kpi_run/form";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("重跑设置", url, 300, 200, bizParams, function(action){
	    	 grid.reload();
	    });
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

