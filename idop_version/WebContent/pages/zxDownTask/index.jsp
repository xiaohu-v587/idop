<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>下载任务列表</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/echarts/echarts.min.js"></script>
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		.txtDiv{
		    border-top: solid;
/*  		    border-right: solid; */
		    border-color: #2fa2fd;
		    border-width: 1px;
		    color:#ffffff;
  		}
  		</style>
	</head> 
	<body>
	<div class="nui-fit" style="overflow: hidden">
		<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;height:63px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
<!-- 		   			<input id="backOrg" name="backOrg" class="nui-hidden" /> -->
<!-- 		   			<input id="backLevel" name="backLevel" class="nui-hidden" /> -->
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">任务描述：</th>
							<td align="left">
								<input id="taskTxt" name="taskTxt" class="mini-textbox" style="width:165px;"/>
							</td>
<!-- 							<th align="right"></th> -->
<!-- 							<td align="left"></td> -->
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:90%;" align="center">
							<a class="mini-button" iconCls="icon-reload" onclick="reload()" >刷新</a>
							<span class="separator"></span>
							<a class="mini-button" iconCls="icon-search" onclick="search()" >查询</a>
							<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-reload" onclick="reset()" >重置</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
	    	<div class="gridDiv" style="width:100%;height:40%;">
				<div id="datagrid1" class="nui-datagrid" url="<%=root%>/zxDownTask/downTaskList" style="width: 100%;height: 100%;float:left;"
		    		multiSelect="false" allowHeaderWrap="true">
			        <div property="columns">
			        	<div headerAlign="center" width="4" type="indexcolumn" align="right">序号</div>
			            <div field="pkid" width="20" headerAlign="center" allowSort="true"  align="left">任务编号</div>                
			            <div field="job_person" width="7" allowSort="true" headerAlign="center" align="right">发起人</div> 
			            <div field="job_explain" width="14" headerAlign="center" allowSort="true"  align="right">任务描述</div>
			            <div field="job_time" width="10" headerAlign="center" allowSort="true"  align="right">创建时间</div>
			            <div field="begin_time" width="10" headerAlign="center" allowSort="true"  align="right">开始时间</div>
			            <div field="end_time" width="10" headerAlign="center" allowSort="true"  align="right">结束时间</div>
			            <div field="task_status" width="7" headerAlign="center" allowSort="true"  align="right">处理状态</div>
			            <div field="row_number" width="6" headerAlign="center" allowSort="true"  align="right">数据条数</div>
			            <div width="13" headerAlign="center" allowSort="true"  align="center" renderer="onRender">操作</div>
			        </div>
				</div>
			</div>
			
			<div class="gridDiv" style="width:100%;height:43%;">
		    	<div id="datagrid2" class="nui-datagrid" style="width: 100%;height:100%;" allowHeaderWrap="true"
		    		multiSelect="false" showPager="false" >
				</div>
			</div>
			
			<div class="txtDiv">
		    	预览数据最多显示<span id="viewSize"></span>条，若获取全部结果，请进行下载操作
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var down_path="${down_path}";
	var view_size="${view_size}";
	document.getElementById("viewSize").innerHTML= view_size;
	var grid1 =$G.get("datagrid1");
	var grid2 =$G.get("datagrid2");
	var form = $G.getForm("form1");
	grid1.load();
	
	//添加操作链接
	function onRender(e){
		var op ='<a href="javascript:lookInfo()"><font color="yellow">预览</font></a>&nbsp;&nbsp;'
			+'<a href="javascript:dataDown()"><font color="yellow">数据下载</font></a>&nbsp;&nbsp;'
			+'<a href="javascript:logDown()"><font color="yellow">日志下载</font></a>';
		return op;
	}
	
	//预览
	function lookInfo(){
		var row1 = grid1.getSelected();
		if (row1) {
			var status =row1.status;
			var task_status =row1.task_status;
			if("9"!=status){
				$G.alert(task_status+"，数据不可预览！");
				return;
			}
			var head =row1.table_head;
			var table_name =row1.table_name_full;
			var headArr = head.split(",");
			var columns = [
				{headerAlign:"center", width:"40",header:"序号", type:"indexcolumn", align:"right"}     
			];
			for (var i=0;i<headArr.length;i++){
				columns[i+1] = {field:""+headArr[i]+"", width:"100", header:headArr[i],headerAlign:"center", align:"right"} ;  
			}
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (data){
		    	grid2.set({
					columns:columns,
					data:data.viewList
				});
		    });
		    $G.postByAjax({view_size:view_size,table_name:table_name},"<%=root%>/zxDownTask/viewList",ajaxConf);
		}
	}
	
	//数据下载
	function dataDown(){
		var row1 = grid1.getSelected();
		if (row1) {
			var status =row1.status;
			var task_status =row1.task_status;
			if("9"!=status){
				$G.alert(task_status+"，数据不可下载！");
				return;
			}
			$G.GcdsConfirm("请遵守中国银行数据安全使用规范使用下载数据！数据严禁外传！", "下载提示", function(action) {
				if (action == 'ok') {
					window.location= down_path+row1.download_file_name;
              	}
            });
		}
	}
	
	//日志下载
	function logDown(){
		var row1 = grid1.getSelected();
		if (row1) {
			var pkid =row1.pkid;
			window.location= "<%=root%>/zxDownTask/logDown?pkid="+pkid;
		}
	}
	
	//刷新
	function reload(){
		grid1.load(form.getData());
		grid2.set({
			columns:[],
			data:[]
		});
	}
	
	//查询
	function search(){
		grid1.load(form.getData());
	}

	//重置
	function reset(){
		form.reset();
	}
	
</script>

