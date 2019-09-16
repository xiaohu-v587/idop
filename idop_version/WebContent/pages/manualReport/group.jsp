<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<%@ include file="/common/nuires.jsp"%>
	    <title>群组管理</title>
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
		                   	<th align="right">群组名称：</th>
							<td align="left" style="width: 240px;">
								<input name="name" class="mini-textbox" style="width:222px;"/>
							</td>
							<td>
						<a class="mini-button" iconCls="" onclick="search()">查询</a>
						<span class="separator"></span>
						<a class="nui-button" iconCls="" onclick="reset()">重置</a>
					</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
		<div class="nui-toolbar" style="border:0;padding:0px;height: 32px;">
			<table style="width:100%;">
				<tr>
					<td style="width:100%;">
						<a class="mini-button" iconCls="" id="addBtn" onclick="add()">新建</a>
						<span class="separator"></span>
						<a class="mini-button" iconCls="" id="editBtn" onclick="edit()">修改</a>
						<span class="separator"></span>
						<a class="mini-button" iconCls="" id="removeBtn" onclick="del()">删除</a>
					</td>
					
				</tr>
			</table>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;height:100%;" sortMode="client" allowUnselect="false"
				url="<%=root%>/manualReport/getGroups" oncellclick=""  onselectionchanged="onSelectionChanged" onload="controlBtnState"
			    autoEscape="false" onshowrowdetail="onShowRowDetail" pageSize="20">
				<div property="columns">
					<div type="checkcolumn" name="checkCloumn"></div>
					<div headerAlign="center" type="indexcolumn" align="center">序号</div>
			        <div field="id" visible="false">id</div>
			        <div field="send_level" visible="false">发送等级</div>
			        <div field="group_name" width="100" allowSort="true" headerAlign="center" align="center">群组名称</div>
			        <div field="itemnames" width="300" allowSort="true" headerAlign="center" align="left">群组内容</div>
                    <div field="group_note" width="100" allowSort="true" headerAlign="center" align="left">备注</div>
				</div>
			</div>  
		</div>
	</body>
</html>
<script type="text/javascript">
     
	$G.parse();
	var grid =$G.get("datagrid1");
	var form = $G.getForm("form1");
	var seletecIndex;
	grid.load();

	//新增
	function add() {
        var url = "<%=root%>/manualReport/groupform";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("新增", url, 600, 400, bizParams, function(action){
	    	 grid.reload();
	    });
	}
	
	//编辑
	function edit(){
		var record = grid.getSelected();
		if (record) {
			var url = "<%=root%>/manualReport/groupform";
			var bizParams = {pageType:"edit",groupId:record.id,groupName:record.group_name,itemNames:record.itemnames,itemUserIds:record.itemuserids,itemOrgIds:record.itemorgids,groupNote:record.group_note,level:record.send_level,function_group:record.function_group,group_mode:record.group_mode,flag:record.flag,items:record.items};
	        $G.showmodaldialog("编辑", url, 600, 400, bizParams, function(action){
		    	 grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}

	//删除
	function del(){
		var record = grid.getSelected();
		if(record){
			$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
				if (action == 'ok') {
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"id":record.id}, "<%=root%>/manualReport/delGroup", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
		}
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
	
	//控制按钮的状态 
	function controlBtnState() {
		$G.get("editBtn").disable();
		$G.get("removeBtn").disable();
	}

	//当选中grid中的行    
	function onSelectionChanged(e){
		var rows = grid.getSelecteds();
		if($G.get("datagrid1").showPager==true) {
			seletecIndex = grid.indexOf(rows[0]);
		}
		if (rows.length > 0) {
			$G.get("editBtn").enable();
            $G.get("removeBtn").enable();
		} else {
			$G.get("editBtn").disable();
			$G.get("removeBtn").disable();
		}
	}
	
	//添加操作链接
	function onRender(e){
		var op ='<a href="javascript:detail()"><font color="blue">查看</font></a>&emsp;'
				+'<a href="javascript:download()"><font color="blue">下载</font></a>&emsp;'
				+'<a href="javascript:remind()"><font color="blue">催办</font></a>';
		return op;
	}
	
	//查看
	function detail(){
		var row = grid.getSelected();
		if (row) {
			var url = "<%=root%>/manualReport/detail";
			var bizParams = {id:row.id};
	        $G.showmodaldialog("报送报表详情", url, 800, 600, bizParams, function(action){
		    	 grid.reload();
		    });
		}
	}
</script>