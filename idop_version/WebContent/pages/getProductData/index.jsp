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
  		<script src=""<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="流程" style="width:100%;height:1px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		 
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">新增申请</a>
							<a class="mini-button" iconCls="icon-edit" id="xqBtn" onclick="xq()" plain="true">详情</a>
          	     			<!-- <a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()" plain="true">编辑</a>
          	     			<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
                			<a class="mini-button" iconCls="icon-undo" id="addBtn" onclick="convertToModel()" plain="true">转换为Model</a> 
                			<a class="mini-button" iconCls="icon-download"  id="download" onclick="download()" plain="true">下载</a> -->
		                </td>
		                <td style="white-space:nowrap;">
		                	<!-- <a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a> -->
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/leave/taskList" style="width: 100%;height: 100%;"
		    		multiSelect="true" allowUnselect="false" autoEscape="false">
			        <div property="columns">   
			            <div type="checkcolumn"  name="checkCloumn"></div>         
			            <div type="indexcolumn"  headerAlign="center">序号</div>                
			            <div field="leavetype"  allowSort="true" headerAlign="center" align="center" renderer="qjlxRenderer" type="comboboxcolumn">假种</div>            
			            <div field="userid"  allowSort="true" headerAlign="center" align="center">申请人</div> 
			            <div field="createdate" allowSort="true"  headerAlign="center" align="center" dateFormat="yyyy-MM-dd HH:mm:ss">申请时间</div> 
			            <div field="starttime"   allowSort="true" headerAlign="center" align="center" dateFormat="yyyy-MM-dd HH:mm:ss">开始时间</div>
			            <div field="endtime"   allowSort="true" headerAlign="center" align="center" dateFormat="yyyy-MM-dd HH:mm:ss">结束时间</div>
			        	<div field="reason"    allowSort="true" headerAlign="center" align="center" >请假原因</div>
			        	<div field="processinstanceid"    allowSort="true" headerAlign="center" align="center" >流程实例id</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	
	var grid =$G.get("datagrid1");
	
	grid.load();

	function qjlxRenderer(e) {
        return $G.getDictText("QJLX", e.row.leavetype);
    } 
	
	
	//新增
	function add() {
        var url = "<%=root%>/getProductData/form";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("申请", url, "90%", "90%", bizParams, function(action){
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
	
	function onSuspendedRenderer(e){
		var value = e.value;
		var obj = '';
		if(!value)
			obj ='正常;('+e.record.pi_version+')';
		if(value)
			obj ='挂起;('+e.record.pi_version+')';
		return obj;
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
                	if(!row.suspended)
                		state = 'active';
        			if(row.suspended)
        				state = 'suspend';
                	$G.postByAjax({"state":state,"taskid":row.id}, "<%=root%>/leave/claim", ajaxConf);
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
	
	
	function xq(){
		
		var row = grid.getSelected();
        if (row) {
        	var url = "<%=root%>/leave/form";
        	 var bizParams = {pageType:"xq",data:row};
        	$G.showmodaldialog("请假详情", url, 500, 300, bizParams, function(action){
        		grid.load();
	   		});
        } else {
        	$G.alert("请选中一条记录");
        }
		
	}
		
</script>

