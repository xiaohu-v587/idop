<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<%@ include file="/common/nuires.jsp"%>
    <title>新章申请</title>
	<style type="text/css">
    	html, body{
        	margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    	}
	</style> 
</head>
<body >
	<div id="layout1" class="mini-layout"  style="width:100%;height:40px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden;background:#0583c1;">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table">
						<tr>
		                   	<th align="right">角色：</th>
							<td>
	          					<input id="role" name="role" class="mini-combobox" nullItemText="请选择..." emptyText="请选择..." 
	          					url="<%=root%>/rolemutual/getCombobox" textfield="name" dataField="records" valuefield="id"
	                   		        allowInput="false"/> 
          					</td>
		                   	<th align="right">互斥角色：</th>
							<td>
	          					<input id="reject" name="reject" class="mini-combobox" nullItemText="请选择..." emptyText="请选择..." 
	          					url="<%=root%>/rolemutual/getCombobox" textfield="name" dataField="records" valuefield="id"
	                   		        allowInput="false"/> 
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
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()">新增</a>
							<a class="mini-button" iconCls="icon-edit" id="editBtn"  onclick="edit()">编辑</a>
							<a class="mini-button" iconCls="icon-remove" id="delBtn"  onclick="del()">删除</a>
						</td>
						 <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
					</tr>
				</table>
		</div>
			
             
			<div class="mini-fit">
				<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;height:100%;" sortMode="client" allowUnselect="false"
					url="<%=root%>/rolemutual/getList" oncellclick=""  onselectionchanged=""  onload="" onshowrowdetail="" onrowclick=""
				    autoEscape="false" >
					<div property="columns">
						<div headerAlign="center" type="indexcolumn">序号</div>
				        <div field="id" visible="false" headerAlign="center" allowSort="true"><fmt:message key="id"/></div>
				        <div field="js" width="100" allowSort="true" headerAlign="center" align="center">角色</div>
	                    <div field="hcjs" width="100" allowSort="true" headerAlign="center" align="center">互斥角色</div>
	                    
					</div>
				</div>  
			</div>
</body>
</html>
<script type="text/javascript">
      
	$G.parse();
	var grid = $G.get("datagrid1");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	
	grid.load();
	
	//新增
	function add(){
		var url = "<%=root%>rolemutual/form";
		var bizParams = {pageType:"add"};
		$G.showmodaldialog("角色互斥-新增", url, 500, 300, bizParams, function(action){
			grid.reload();
		});
	}
	//编辑
	function edit(){
		var row = grid.getSelected();
		if(row){
			var url = "<%=root%>/rolemutual/form";
			var bizParams = {pageType:"edit",id:row.id};
			$G.showmodaldialog("角色互斥-编辑", url, 500, 300, bizParams, function(action){
				grid.reload();
			});
		}else{
			$G.alert("请先选择一行数据！");
		}
	}

	//删除
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
	                var ajaxConf = new GcdsAjaxConf();
	                ajaxConf.setIsAsync(false);
	        		ajaxConf.setIsShowSuccMsg(false);
	        		ajaxConf.setIsShowProcessBar(false);
	        		ajaxConf.setErrorFunc(function(){
	    				$G.alert("操作失败！");
	    			})
                	ajaxConf.setSuccessFunc(function (text){
						if(text.result > "1"){
							$G.closemodaldialog("ok");
						}
						grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>rolemutual/del", ajaxConf);
				}
            });
		}else{
			$G.alert("请先选择一行数据！");
		}
	}

	//查询
	function search(){
		var info="{";
		var value=$G.getbyName("role").getValue();
		if(value!=''){
			info+="\"role\":"+"\""+value+"\""+",";
		}
		var value=$G.getbyName("reject").getValue();
		if(value!=''){
			info+="\"reject\":"+"\""+value+"\""+",";
		}
		if(info.charAt(info.length - 1)==","){
			info=info.substring(0,info.length-1);
		}
		info+="}";
		info=eval('(' + info + ')');
		grid.load(info);
	}
	
	//grid行单击事件
	function onSelectionChanged(e) {
		
	}

	//重置
	function reset(){
		form.reset();
	}
	
	//显示行详细
	function onShowRowDetail() {
	}
	
	//表格渲染
	function onDrawCell(e) {
	}
</script>