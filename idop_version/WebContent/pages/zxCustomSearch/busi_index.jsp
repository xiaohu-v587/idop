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
		<div style="width: 100%;height: 50%;">
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
									<input id="table_cn_name" name="table_en_name" class="mini-textbox" style="width:165px;"/>
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
							<font color="yellow">父表清单</font>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset1()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search1()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/zxCustomSearch/getBusiList" style="width: 100%;height: 100%;"
		    		multiSelect="false">
			        <div property="columns">            
			            <div headerAlign="center" width="6" type="indexcolumn">序号</div>      
			            <div field="table_en_name" width="15" headerAlign="center" allowSort="true"  align="center">表英文名</div>                
			            <div field="table_cn_name" width="15" allowSort="true" headerAlign="center" align="center">表中文名</div>            
			            <div width="15" allowSort="true" headerAlign="center" align="center" renderer="onRender1">操作</div>
			        </div>
				</div>
			</div>
		</div>
		<div style="width: 100%;height: 50%;">
			<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;height:63px;" showToolbar="false" showCollapseButton="false"
	    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
			   	<div class="nui-fit" style="overflow: hidden">
			   		<form id="form2">
				   		<table style="table-layout: fixed;" class="search_table" width="100%">
		               		<tr>
			                   	<th align="right">表英文名：</th>
								<td align="left">
									<input id="table_name_en" name="table_name_en"  class="mini-textbox" style="width:165px;"/>
								</td>
								<th align="right">表中文名：</th>
								<td align="left">
									<input id="table_name_cn" name="table_name_cn" class="mini-textbox" style="width:165px;"/>
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
							<font color="yellow">子表清单</font>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset2()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search2()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid2" class="nui-datagrid" sortMode="client" url="<%=root%>/zxCustomSearch/getBusiChildList" style="width: 100%;height: 100%;"
		    		multiSelect="false">
			        <div property="columns">            
			            <div headerAlign="center" width="6" type="indexcolumn">序号</div>      
			            <div field="table_en_name" width="15" headerAlign="center" allowSort="true"  align="center">表英文名</div>             
			            <div field="table_cn_name" width="15" allowSort="true" headerAlign="center" align="center">表中文名</div>
			            <div field="status_name" width="10" allowSort="true" headerAlign="center" align="center">表单状态</div>   
			            <div width="15" allowSort="true" headerAlign="center" align="center" renderer="onRender2">操作</div>
			        </div>
				</div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();

	var grid1 =$G.get("datagrid1");
	var form1 = $G.getForm("form1");
	var grid2 =$G.get("datagrid2");
	var form2 = $G.getForm("form2");
	grid1.load();
	grid2.load();

	//新增子表
	function add() {
        var row = grid1.getSelected();
		if (row) {
			var url = "<%=root%>/zxCustomSearch/busiForm";
	        var bizParams = {
	        		pageType:"add",
	        		base_pid:row.base_id,
	        		filed_ids:row.filed_ids,
	        		table_cn_pname:row.table_cn_name,
	        		table_en_pname:row.table_en_name,
	        		base_seq_name:row.base_seq_name
	        	};
	        $G.showmodaldialog("新增", url, 850, 400, bizParams, function(action){
		    	 grid2.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}

	//编辑
	function edit(){
		var row = grid2.getSelected();
		if (row) {
			var url = "<%=root%>/zxCustomSearch/busiForm";
			var bizParams = {
					pageType:"edit",
					base_id:row.base_id,
					table_cn_pname:row.table_cn_pname,
					table_cn_name:row.table_cn_name,
					filed_ids:row.filed_ids,
					org_ids:row.org_ids,
					base_pid:row.base_pid
				};
	        $G.showmodaldialog("编辑", url, 850, 400, bizParams, function(action){
		    	 grid2.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	//授权详情
	function detail(){
		var row = grid2.getSelected();
		if (row) {
			var url = "<%=root%>/zxCustomSearch/getDetail";
			var bizParams = {pageType:"edit",id:row.id};
	        $G.showmodaldialog("编辑", url, 800, 600, bizParams, function(action){
		    	 grid2.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}

	//删除
	function del(){
		var rows = grid2.getSelecteds();
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
                    	grid2.reload();
                    });
                	$G.postByAjax({"base_ids":ids}, "<%=root%>/zxCustomSearch/deleteChildModel", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
		}
	}
	
	//添加操作链接
	function onRender1(e){
		var op = '<a href="javascript:add()"><font color="yellow">创建子表</font></a>';
		return op;
	}
	
	//添加操作链接
	function onRender2(e){
		var op='';
		if("1"==e.row.license_status){
			op +='<a href="javascript:lookInfo()"><font color="yellow">授权详情</font></a>&nbsp;&nbsp;';
		}else{
			op +='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		}
		op +='<a href="javascript:edit()"><font color="yellow">编辑</font></a>&nbsp;&nbsp;';
		if("0"==e.row.status){
			op +='<a href="javascript:startOrStop()"><font color="yellow">启用表单</font></a>&nbsp;&nbsp;';
		}else if("1"==e.row.status){
			op +='<a href="javascript:startOrStop()"><font color="yellow">停用表单</font></a>&nbsp;&nbsp;';
		}
		op +='<a href="javascript:del()"><font color="yellow">删除</font></a>';
		return op;
	}
	
	function startOrStop(){
		var row = grid2.getSelected();
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
				    	grid2.reload();
					});
					$G.postByAjax(bizParams,url,ajaxConf);
				}
			});
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	//
	function lookInfo(){
		var row = grid2.getSelected();
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

	//查询
	function search1(){
		grid1.load(form1.getData());
	}
	//重置
	function reset1(){
		form1.reset();
	}
	//查询
	function search2(){
		grid2.load(form2.getData());
	}
	
	//重置
	function reset2(){
		form2.reset();
	}

</script>

