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
  		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="流程配置管理" style="width:100%;height:71px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
	               		<tr>
		                   	<th align="right">名称：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right">key：</th>
		                    <td align="left">
		                    	<input id="key" name="key" class="mini-textbox" style="width:165px;"/>    
		                    </td>
		                   	<th align="right">是否只显示最新版本</th>
							<td align="left">
								<div id ="isNew" name="isNew" class="nui-checkbox" value="true" trueValue="1" falseValue="0" checked="true"  reanOnly="false" onvaluechanged="newschange"/>
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
							<!-- 
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">发布</a>
							 -->
          	     			<!-- <a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()" plain="true">重新发布</a> -->
          	     			<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
                			<!-- <a class="mini-button" iconCls="icon-undo" id="addBtn" onclick="convertToModel()" plain="true">转换为Model</a> -->
                			<a class="mini-button" iconCls="icon-download"  id="download" onclick="download()" plain="true">下载</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/workflow/processList" style="width: 100%;height: 100%;"
		    		multiSelect="true" allowUnselect="false" autoEscape="false">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>    
			            <div field="id"  headerAlign="center" allowSort="true"  align="center">流程定义Id</div>                
			            <div field="deploymentid"  allowSort="true" headerAlign="center" align="center">部署Id</div>            
			            <div field="name"  allowSort="true" headerAlign="center" align="center">名称</div> 
			            <div field="key" allowSort="true" headerAlign="center" align="center">KEY</div> 
			            <div field="version"  allowSort="true" headerAlign="center" align="center" >版本号</div>
			            <div field="resourcename"  renderer="onResourcenameRenderer" allowSort="true" headerAlign="center" align="center" >XML</div>
			            <div field="diagramresourcename" renderer="onDiagramresourcenameRenderer" allowSort="true" headerAlign="center" align="center" >图片</div>
			            <div field="deploy_deploymenttime" dateFormat="yyyy/MM/dd"  allowSort="true" headerAlign="center" align="center" >部署时间</div>
			            <div field="suspended"  renderer="onSuspendedRenderer" allowSort="true" headerAlign="center" align="center" >是否挂起</div>
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
	
	function onSuspendedRenderer(e){
		var value = e.value;
		var obj = '';
		if(value)
			obj ='<a href="javascript:updateState()">激活</a>'
		if(!value)
			obj ='<a href="javascript:updateState()">挂起</a>'
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
                	$G.postByAjax({"state":state,"processdefinitionid":row.id}, "<%=root%>/workflow/updateState", ajaxConf);
              	}
			});
		}else{
			$G.alert("请选中一条记录");
		}
	}
	
	
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
		var obj = '<a target="_blank" href="<%=root%>/workflow/loadByDeployment?processdefinitionid='+e.record.id+'&resourcetype=xml" >"'+value+'</a>';
		return obj;
	}
	function onDiagramresourcenameRenderer(e){
		var value = e.value;
		var obj = '<a target="_blank" href="<%=root%>/workflow/loadByDeployment?processdefinitionid='+e.record.id+'&resourcetype=image" >"'+value+'</a>';
		return obj;
	}
	
	function newschange(e){
		search();
  	}
</script>

