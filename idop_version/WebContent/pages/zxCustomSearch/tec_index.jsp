<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>技术定制父表</title>
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
		   			<input name="status" class="nui-hidden"/> 
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
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">新增</a>
          	     			<a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()" plain="true">编辑</a>
          	     			<a class="mini-button" iconCls="icon-edit" id="startBtn" onclick="startOrStop()" plain="true">启用/停用</a>
          	     			<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
		                </td>
		                <td style="white-space:nowrap;">
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>
		                </td>
		            </tr>
		       	</table>
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/zxCustomSearch/getTecList" style="width: 100%;height: 100%;"
		    		multiSelect="false">
			        <div property="columns">
			            <div headerAlign="center" width="6" type="indexcolumn">序号</div>
			            <div field="table_en_name" width="15" headerAlign="center" allowSort="true"  align="left">表英文名</div>
			            <div field="table_cn_name" width="15" allowSort="true" headerAlign="center" align="left">表中文名</div>
			            <div field="date_filed_name" width="10" allowSort="true" headerAlign="center" align="left">日期关联字段</div>
			            <div field="org_filed_name" width="10" allowSort="true" headerAlign="center" align="left">机构关联字段</div>
			            <div field="status_name" width="10" allowSort="true" headerAlign="center" align="left">表单状态</div>
			            <div field="remark" width="10" allowSort="true" headerAlign="center" align="center" renderer="onRender" >操作</div>
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

	//新增
	function add() {
        var url = "<%=root%>/zxCustomSearch/tecForm";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("新增", url, 600, 500, bizParams, function(action){
	    	 grid.reload();
	    });
	}

	//编辑
	function edit(){
		var row = grid.getSelected();
		if (row) {
			var url = "<%=root%>/zxCustomSearch/tecForm";
			var bizParams = {pageType:"edit",base_id:row.base_id};
	        $G.showmodaldialog("编辑", url, 600, 500, bizParams, function(action){
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
	                    	ids = rows[index].base_id;
	                  	} else {
	                    	ids += "," + rows[index].base_id;
	                  	}
	                }
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"base_ids":ids}, "<%=root%>/zxCustomSearch/deleteModel", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
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
	
	//添加操作链接
	function onRender(e){
		var op ='';
		if("1"==e.row.license_status){
			op ='<a href="javascript:lookInfo()"><font color="yellow">授权详情</font></a>';
		}
		return op;
	}
	
	//
	function lookInfo(){
		var row = grid.getSelected();
		if (row) {
			var url = "<%=root%>/zxCustomSearch/authDetail";
			var bizParams = {base_id:row.base_id};
	        $G.showmodaldialog("授权详情", url, 500, 500, bizParams, function(action){
		    	 //grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	function startOrStop(){
		var row = grid.getSelected();
		if (row) {
			var base_id=row.base_id;//获取ID
			var status = row.status;
			var msg = "";
			if(status == "1"){
				msg="确定停用？";
				status="0";
			}else if (status == "0"){
				msg="确定启用？";
				status="1";
			}
			var url = "<%=root%>/zxCustomSearch/changeStatus";
			var bizParams = {base_id:base_id,status:status};
			$G.confirm(msg, "确定",function (action){
				if(action == "ok"){
					var ajaxConf = new GcdsAjaxConf();
					ajaxConf.setIsShowProcessBar(true);
					ajaxConf.setIsShowSuccMsg(true);
				    ajaxConf.setSuccessFunc(function (text){
				    	grid.reload();
					});
					$G.postByAjax(bizParams,url,ajaxConf);
				}
			});
		}else{
			$G.alert("请先选中一条记录！");
		}
	}

</script>

