<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>操作日志查询</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="操作日志详情" style="width:100%;height:111px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table >
						<tr>
		                   	<th align="right">操作人：</th>
							<td align="left">
								<input id="user_no" name="user_no" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right">操作时间：</th>
							<td align="left">
								<input id="create_time" name="create_time" class="nui-datepicker" allowInput="false" emptyText="请选择..." style="width:165px;"/>
							</td>
		                    <th align="right">操作菜单</th>
							<td align="left">
								<input id="menuname" name="menuname" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
	               		<tr>
		                   	<th align="right">操作类型：</th>
							<td align="left">
								<input id="type" name="type" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right"></th>
		                    <td align="left">    
		                    </td>
		                    <th align="right"></th>
		                    <td align="left">    
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
						<a class="mini-button" iconCls="icon-edit" id="previewBtn" onclick="preview()" >查看</a>
            	     	<!--<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a> -->
					</td>
                    <td style="white-space:nowrap;">
                    	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a> 
						<a class="mini-button" iconCls="icon-search" onclick="search()">查询</a>
                    </td>
				</tr>
			</table>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;"  sortMode="client" allowUnselect="false"
				url="<%=root%>/log/listLog" oncellclick=""  onselectionchanged="onSelectionChanged" multiSelect="true" autoEscape="false"
				onshowrowdetail="onShowRowDetail">
		        <div property="columns">
		              <div headerAlign="center" type="indexcolumn" align="right">序号</div>
		              <div field="id"  visible="false"  width="50" headerAlign="center" allowSort="true">id</div>
		              <div field="user_no"  width="160" allowSort="true" align="left" headerAlign="center">操作人</div>
		              <div field="create_time" dateFormat="yyyy-MM-dd HH:mm:ss" width="160" allowSort="true" align=""left"" headerAlign="center">操作时间</div>
		              <div field="create_time1" visible="false">create_time1</div>
		              <div field="menuname"  width="160" allowSort="true" align="left" headerAlign="center">操作菜单</div>
		              <div field="type"  width="160" allowSort="true" align="right" headerAlign="center">操作类型</div>
		              <div field="remark"  width="160" allowSort="true" align="left" headerAlign="center">备注</div>
		        </div>
			</div>  
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid1");
	var form = $G.getForm("form1");
	grid.load();

	//预览日志文件
	function preview(){
		var row = grid.getSelected();
		if (row) {
			var url = "/log/view?create_time="+row.create_time1+'&id='+row.id;
			$G.showmodaldialog("操作日志详情",url, 810, 500, null, null);
	}else{
			$G.alert("请先选中一条记录！");
		}
	}
		
	//删除操作记录
	function del(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
				if (action == 'ok') {
					var ids = "";
	                for(var index = 0;index < rows.length;index++){
						if(ids == ""){
	                    	ids = rows[index].id;
	                  	} else {
	                    	ids += "," + rows[index].id;
	                  	}
	                }
                	$G.postByAjax({"id":ids}, "/log/del");
                	 grid.load();
				}
            });
		}else{
			$G.alert("请先选择一行数据！");
		}
	}
	//根据查询条件查询信息
	function search(){
		var data = form.getData();
         grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}


	function onSelectionChanged(){
		var rows = grid.getSelecteds();
		if(rows.length>1){
			$G.get("previewBtn").disable();
		}else{
			$G.get("previewBtn").enable();
		}
	}
</script>