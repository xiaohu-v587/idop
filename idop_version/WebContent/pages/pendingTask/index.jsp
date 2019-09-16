<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="待审批流程" style="width:100%;height:131px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">任务ID：</th>
							<td align="left">
								<input  name="id" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right">任务所属人：</th>
							<td align="left">
								<input  name="owner" class="mini-textbox" style="width:165px;"/>
							</td>
		                    <th align="right">任务名称：</th>
							<td align="left">
								<input  name="name" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
	               		<tr>
		                   	<th align="right">key：</th>
							<td align="left">
								<input  name="taskDefinitionKey" class="mini-textbox" style="width:165px;"/>
							</td>
		                    <th align="right">任务创建时间（开始）：</th>
							<td align="left">
								<input   name="after_createTime" class="nui-datepicker" allowInput="false" emptyText="请选择..."  style="width:165px;" onvaluechanged="retchange"/>
							</td>
		             <th align="right">任务创建时间（结束）：</th>
		                    <td align="left">    
		                    	<input   name="before_createTime" class="nui-datepicker" allowInput="false" emptyText="请选择..."  style="width:165px;" onvaluechanged="retchange"/>
		                    </td>
	               		</tr>
	               		<tr>
	               			<th align="right">任务描述:</th>
							<td align="left" >
								<input  name="description" class="mini-textbox" style="width:165px;"/>
							</td>
							 <th align="right">任务逾期时间（开始）：</th>
							<td align="left">
								<input   name="after_dueDate" class="nui-datepicker" allowInput="false" emptyText="请选择..."  style="width:165px;" onvaluechanged="retchange"/>
							</td>
		                   <th align="right">任务逾期时间（结束）：</th>
		                    <td align="left">    
		                    	<input   name="before_dueDate" class="nui-datepicker" allowInput="false" emptyText="请选择..."  style="width:165px;" onvaluechanged="retchange"/>
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
							<!-- <a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">新增</a>
          	     			<a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()" plain="true">编辑</a>
          	     			<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
                			<a class="mini-button" iconCls="icon-undo" id="addBtn" onclick="convertToModel()" plain="true">转换为Model</a> -->
                			<!-- <a class="mini-button" iconCls="icon-download"  id="download" onclick="download()" plain="true">下载</a> -->
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/pendingTask/list" style="width: 100%;height: 100%;"
		    		multiSelect="true" allowUnselect="false" autoEscape="false">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>    
			            <div field="id"  headerAlign="center" allowSort="true"  align="center">任务ID</div>                
			            <div field="taskdefinitionkey"  allowSort="true" headerAlign="center" align="center">任务Key</div>            
			            <div field="name"  allowSort="true" headerAlign="center" align="center">任务名称</div> 
			            <div field="processdefinitionid" allowSort="true" renderer="onDiagramresourcenameRenderer" headerAlign="center" align="center">流程定义ID</div> 
			            <div field="processinstanceid"   allowSort="true" headerAlign="center" align="center" >流程实例ID</div>
			            <div field="priority"   allowSort="true" headerAlign="center" align="center" >优先级</div>
			            <div field="createtime"   allowSort="true" headerAlign="center" align="center" >任务创建日期</div>
			        	<div field="duedate"   allowSort="true" headerAlign="center" align="center" >任务逾期日</div>
			        	<div field="description"   allowSort="true" headerAlign="center" align="center" >任务描述</div>
			        	<div field="owner"   allowSort="true" headerAlign="center" align="center" >任务所属人</div>
			        	<div field="assignee"  renderer="onAssigneeRenderer"  allowSort="true" headerAlign="center" align="center" >操作</div>
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

	//新增
	function add() {
        var url = "<%=root%>/workflow/deployUpload";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("定义新的流程定义", url, 500, 250, bizParams, function(action){
	    	 grid.reload();
	    });
	}

	//编辑
	function edit(){
		var row = grid.getSelected();
		if (row) {
			var url = "<%=root%>/position/form";
			var bizParams = {pageType:"edit",id:row.id};
	        $G.showmodaldialog("编辑岗位", url, 500, 250, bizParams, function(action){
		    	 grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
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
	                    	ids = rows[index].deploymentid;
	                  	} else {
	                    	ids += "," + rows[index].deploymentid;
	                  	}
	                }
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/workflow/del", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
		}
	}

	//重置密码
	function resetPwd(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			$G.GcdsConfirm("确定重置已选中用户的密码？", "删除提示", function(action) {
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
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/user/resetPwd", ajaxConf);
              	}
			});
		}else{
			$G.alert("请选中一条记录");
		}
	}

	//下载
	function download(){
		var orgnum=$G.getbyName("orgnum").getValue();
		var jgh=$G.getbyName("jgh").getValue();
		var userno=$G.getbyName("userno").getValue();
		var name=$G.getbyName("name").getValue();
		window.location="<%=root%>/user/download?orgnum=" + orgnum + "&jgh=" + jgh + "&userno=" + userno + "&name=" + name;   
	}
	
	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
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
	
	function onAssigneeRenderer(e){
		var value = e.value;
		var obj = '';
		if(value)
			obj ='<a href="javascript:updateState()">签收</a>'
		if(!value)
			obj ='<a href="javascript:updateState()">办理</a>'
		return obj;
	}
	    function updateState(){
		var row = grid.getSelected();
		if(row){
			$G.GcdsConfirm("确定执行？", "提示", function(action) {
				if (action == 'ok') {
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	var state = '';
                	if(row.suspended)
                		state = 'active';
        			if(!row.suspended)
        				state = 'suspend';
        			$G.postByAjax({"state":state,"id":row.id}, "<%=root%>/myTask/updateState", ajaxConf);
              	}
			});
		}else{
			$G.alert("请选中一条记录");
		}
	} 
	
	
	/* function updateState(){
		var row = grid.getSelected();
		if(row){
			var url = "/pendingTask/form";
            var bizParams = {action:"0"};
            $G.showmodaldialog("流程审批", url, 440, 350, bizParams, function(action){
            	grid.load();
		    });
		}else{
			$G.alert("请选中一条记录");
		}
	} */
	
	
	function convertToModel(){
		var row = grid.getSelected();
		if(row){
			$G.GcdsConfirm("确定转换为Model？", "提示", function(action) {
				if (action == 'ok') {
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"processdefinitionid":row.id}, "<%=root%>/workflow/convertToModel", ajaxConf);
              	}
			});
		}else{
			$G.alert("请选中一条记录");
		}
	}
	
	function userFun(){
		var row = position_grid.getSelected();
	   	var urlStr = "<%=root%>/org/positionAllot";
		var bizParams = {name:row.name,id:row.id};
		$G.showmodaldialog("岗位用户分配", urlStr, 800, 500, bizParams, function(action){
			position_grid.reload();
		});
	}
	
	function onResourcenameRenderer(e){
		var value = e.value;
		var obj = '<a target="_blank" href="<%=root%>/workflow/loadByDeployment?processdefinitionid='+e.record.processdefinitionid+'&resourcetype=xml" >查看流程定义</a>';
		return obj;
	}
	function onDiagramresourcenameRenderer(e){
		var obj = '<a target="_blank" href="<%=root%>/workflow/readResource?processDefinitionId='+e.record.processdefinitionid+'&executionId='+e.record.processinstanceid+'" >查看流程图</a>';
		return obj;
	}
		
</script>

