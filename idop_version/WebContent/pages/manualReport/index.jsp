<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<%@ include file="/common/nuires.jsp"%>
	    <title>手工报表</title>
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
		<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;height:108px;" showToolbar="false" showCollapseButton="false"
	    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">报表名称：</th>
							<td align="left">
								<input name="name" class="mini-textbox" style="width:222px;"/>
							</td>
							<th align="right">报送频次：</th>
							<td align="left">
								<input name="rate" class="mini-combobox" style="width:222px;"
								 	textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=RATE_TYPE"
									nullItemText="请选择" emptyText="请选择" showNullItem="true"/>
							</td>
						</tr>
						<tr>
							<th align="right">截至日期：</th>
							<td align="left">
								<input name="end_time" class="nui-datepicker" style="width:222px;" valueFormat="yyyyMMdd"/>
							</td>
		                    <th align="right">报表状态：</th>
							<td align="left">
								<input name="status" class="mini-combobox" style="width:222px;"
									emptyText="请选择" nullItemText="请选择" showNullItem="true"
									valueField="val" textfield="name" url="<%=root%>/zxparam/getDict?key=report_task_status"/>
							</td>
							<td style="white-space:nowrap;">
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
						<a class="mini-button" iconCls="" id="editBtn" onclick="edit()">编辑</a>
						<span class="separator"></span>
						<a class="mini-button" iconCls="" id="fileBtn" onclick="file()">附件</a>
						<span class="separator"></span>
						<a class="mini-button" iconCls="" id="delBtn" onclick="del()">删除</a>
						<a class="mini-button" iconCls="" id="recallBtn" onclick="recall()">召回</a>
						<span class="separator"></span>
						<a class="mini-button" iconCls="" id="startBtn" onclick="start()">发布</a>
						<a class="mini-button" iconCls="" id="stopBtn" onclick="stop()">停止</a>
						<a class="mini-button" iconCls="" id="transferBtn" onclick="transfer()" style="display:none">角色移交</a>
						<a class="mini-button" iconCls="" id="removeBtn" onclick="mainCreat()" style="display:none">创建主任务</a>
					</td>
				</tr>
			</table>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;height:100%" sortMode="client" allowUnselect="false"
				url="<%=root%>/manualReport/getReports" onselectionchanged="onSelectionChanged" onload="controlBtnState"
			    autoEscape="false" onshowrowdetail="onShowRowDetail" pageSize="20">
				<div property="columns">
					<div field="id" visible="false"><fmt:message key="ID"/></div>
			        <div field="task_frequency" visible="false">报送频次val</div>
			        <div field="task_status" visible="false">发布状态val</div>
					<div type="checkcolumn" name="checkCloumn"></div>  
					<div type="indexcolumn" headerAlign="center" align="center">序号</div>
			        <div field="task_name" allowSort="true" headerAlign="center" align="center">报表名称</div>
			        <div field="task_frequency_name" allowSort="true" headerAlign="center" align="center">报送频次</div>
			        <div field="task_startdate" dataType="date" allowSort="true" headerAlign="center" align="center" renderer="dateformat">发布日期</div>
                    <div field="task_enddate" dataType="date" allowSort="true" headerAlign="center" align="center" renderer="dateformat">截至日期</div>
                    <div field="task_status_name" allowSort="true" headerAlign="center" align="center">报表状态</div>
                    <div field="rate" allowSort="true" headerAlign="center" align="center" renderer="doMath">完成进度</div>
                    <div headerAlign="center" align="center" renderer="onRender">操作</div>
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

	//控制按钮的状态 
	function controlBtnState() {
		$G.get("editBtn").disable();
		$G.get("fileBtn").disable();
		$G.get("delBtn").show();
		$G.get("delBtn").disable();
		$G.get("recallBtn").hide();
		$G.get("startBtn").show();
		$G.get("startBtn").disable();
		$G.get("stopBtn").hide();
	}
	
	//当选中grid中的行    
	function onSelectionChanged(e){
		var record = grid.getSelected();
		controlBtnState();
		if(record.task_status < 2){
			$G.get("editBtn").enable();
			$G.get("fileBtn").enable();
			$G.get("delBtn").enable();
			$G.get("recallBtn").hide();
			$G.get("startBtn").enable();
			$G.get("stopBtn").hide();
		}else if(record.task_status==2){
			$G.get("editBtn").disable();
			$G.get("fileBtn").enable();
			$G.get("delBtn").hide();
			$G.get("recallBtn").show();
			$G.get("recallBtn").enable();
			$G.get("startBtn").hide();
			$G.get("stopBtn").show();
			$G.get("stopBtn").enable();
		}else if(record.task_status==3){
			$G.get("editBtn").disable();
			$G.get("fileBtn").disable();
			$G.get("delBtn").disable();
			$G.get("recallBtn").hide();
			$G.get("startBtn").hide();
			$G.get("stopBtn").show();
			$G.get("stopBtn").disable();
		}
		/* if($G.get("datagrid1").showPager==true) {
			seletecIndex = grid.indexOf(rows[0]);
		} */
		
	}
	
	//新增
	function add() {
        var url = "<%=root%>/manualReport/form";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("新建报表", url, 800, 600, bizParams, function(action){
	    	 grid.reload();
	    });
	}
	
	//编辑
	function edit() {
		var record = grid.getSelected();
        var url = "<%=root%>/manualReport/form";
        var bizParams = {pageType:"edit",id:record.id};
        $G.showmodaldialog("编辑报表", url, 800, 600, bizParams, function(action){
	    	 grid.reload();
	    });
	}
	
	//上传文件
	function file() {
		var record = grid.getSelected();
        var url = "<%=root%>/manualReport/attachment";
        var bizParams = {id:record.id, view:0};
        $G.showmodaldialog("附件管理", url, 600, 400, bizParams, function(action){});
	}
		
	//删除
	function del(){
		var record = grid.getSelected();
		$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
			if (action == 'ok') {
               	var ajaxConf = new GcdsAjaxConf();
               	ajaxConf.setSuccessFunc(function (){
                   	grid.reload();
                });
               	$G.postByAjax({id:record.id}, "<%=root%>/manualReport/delReport", ajaxConf);
             }
        });
	}
	
	//发布
	function start(e){
		var record = grid.getSelected();
		$G.GcdsConfirm("确定发布此任务吗？", "提示", function(action) {
			if (action == 'ok') {
               	var ajaxConf = new GcdsAjaxConf();
               	ajaxConf.setSuccessFunc(function (data){
               		if(data.result=="success"){
               			grid.reload();
               		}
                });
               	$G.postByAjax({"id":record.id}, "<%=root%>/manualReport/start", ajaxConf);
           }
        });
	}

	//召回
	function recall(e){
		var record = grid.getSelected();
		$G.GcdsConfirm("确定召回此任务吗？", "提示", function(action) {
			if (action == 'ok') {
               	var ajaxConf = new GcdsAjaxConf();
               	ajaxConf.setSuccessFunc(function (data){
               		if(data.result=="success"){
               			grid.reload();
               		}
                });
               	$G.postByAjax({"id":record.id}, "<%=root%>/manualReport/recall", ajaxConf);
             }
        });
	}
	
	//停止
	function stop(){
		var record = grid.getSelected();
		$G.GcdsConfirm("确定结束此任务吗？", "提示", function(action) {
			if (action == 'ok') {
            	var ajaxConf = new GcdsAjaxConf();
            	ajaxConf.setSuccessFunc(function (data){
            		if(data.result=="success"){
            			grid.reload();
            		}
                });
            	$G.postByAjax({"id":record.id}, "<%=root%>/manualReport/stop", ajaxConf);
          	}
        });
	}
	
	//报表管理员角色移交
	function transfer(){
		var url = "<%=root%>/manualReport/roleTransfer";
        var bizParams = {};
        $G.showmodaldialog("报表管理员角色移交", url, 600, 500, bizParams, function(action){
        	window.parent.location = "<%=root%>/";
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
	
	//格式化日期
	function dateformat(e){
		var value = e.value;
		if(value) return $G.formatDate(value,'yyyy-MM-dd');
		return "";
	}
	
	//完成进度百分比
	function doMath(e){
		if(e.record.task_status>=2){
			return ((e.value) * 100).toFixed(1) + "%";
		}else{
			return "";
		}
	}
	
	//添加操作链接
	function onRender(e){
		var index = grid.indexOf(e.record);
		var op = "";
		if(e.record.task_status > 1){
			op += '<a href="javascript:echart('+index+')"><font color="blue">图表查看</font></a>&emsp;'
				+ '<a href="javascript:list('+index+')"><font color="blue">列表查看</font></a>&emsp;'
				+ '<a href="javascript:download('+index+')"><font color="blue">下载</font></a>';
		}
		return op;
	}
	
	function detail(e){
		var record = e.record;
		var url = "<%=root%>/manualReport/detail";
        $G.showmodaldialog("报表详情", url, 800, 600, record, function(action){});
	}
	
	//查看图表
	function echart(index){
		var record = grid.getRow(index);
		var url = "<%=root%>/manualReport/echart";
		var bizParams = {pageType:"view", id:record.id, status:record.task_status};
	    $G.showmodaldialog("完成情况图表", url, 900, 600, bizParams, function(data){
			grid.reload();
		});
	}
	
	//查看列表
	function list(index){
		var record = grid.getRow(index);
		var url = "<%=root%>/manualReport/list";
		var bizParams = {rid:record.id, name:record.task_name, rate:record.task_frequency, end_time:record.task_enddate, status:record.task_status};
	    $G.showmodaldialog("完成情况列表", url, 800, 600, bizParams, function(data){
			grid.reload();
		});
	}
	
	//下载
	function download(index){
		var record = grid.getRow(index);
		window.location = "<%=root%>/manualReport/export?rid="+record.id;
	}
	
	//创建主任务
	function mainCreat() {
        var url = "<%=root%>/zxMainTask/form";
        $G.showmodaldialog("新建报表", url, 800, 600, null, function(action){
	    	 grid.reload();
	    });
	}
	
</script>