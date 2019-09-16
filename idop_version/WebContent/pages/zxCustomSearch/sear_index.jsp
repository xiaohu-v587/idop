<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>业务定制字表</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;height:63px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
	               		<tr>
		                   	<th align="right">表英文名：</th>
							<td align="left">
								<input id="table_en_name" name="table_en_name"  class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">表中文名：</th>
							<td align="left">
								<input id="table_cn_name" name="table_cn_name" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/zxCustomSearch/getSerList" style="width: 100%;height: 100%;"
		    		multiSelect="true">
			        <div property="columns">            
			            <div headerAlign="center" width="6" type="indexcolumn">序号</div>      
			            <div field="table_en_name" width="15" headerAlign="center" allowSort="true"  align="center">表英文名</div>             
			            <div field="table_cn_name" width="15" allowSort="true" headerAlign="center" align="center">表中文名</div>
			            <div width="15" allowSort="true" headerAlign="center" align="center" renderer="onRender">操作</div>
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
	
	//添加操作链接
	function onRender(e){
		var op = '<a href="javascript:addDetail()"><font color="yellow">明细下载</font></a>&nbsp;&nbsp;'
				+'<a href="javascript:downCount()"><font color="yellow">汇总下载</font></a>';
		return op;
	}

	//创建明细下载任务
	function addDetail() {
		var row = grid.getSelected();
		if (row) {
	        var url = "<%=root%>/zxCustomSearch/downDetail";
	        var bizParams = {pageType:"addDetail",
	        		base_id:row.base_id,
	        		org_filed_name:row.org_filed_name,
	        		date_filed_name:row.date_filed_name,
	        		table_en_pname:row.table_en_pname,
	        		table_cn_name:row.table_cn_name
	        	};
	        $G.showmodaldialog("明细下载", url, 850, 500, bizParams, function(action){
		    	 //grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	//创建汇总下载任务
	function downCount() {
		var row = grid.getSelected();
		if (row) {
	        var url = "<%=root%>/zxCustomSearch/downCount";
	        var bizParams = {pageType:"addTotal",
	        		base_id:row.base_id,
	        		org_filed_name:row.org_filed_name,
	        		date_filed_name:row.date_filed_name,
	        		table_en_pname:row.table_en_pname,
	        		table_cn_name:row.table_cn_name
	        	};
	        $G.showmodaldialog("汇总下载", url, 850, 700, bizParams, function(action){
		    	 //grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}

	//查询
	function search(){
		grid.load(form.getData());
	}
	

	//重置
	function reset(){
		form.reset();
	}

</script>

