<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<%@ include file="/common/nuires.jsp"%>
	    <title>报送报表详情</title>
		<style type="text/css">
	    	html, body{
	        	margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
	    	}
	  		.mini-panel-body{
	  			padding: 0px;
	  		}
		</style> 
	</head>
	<body>
		<div style="width:100%;height:78px;">
			<form id="form1">
				<input name="rid" class="nui-hidden"/>
				<input name="status" class="nui-hidden"/>
		   		<table style="table-layout: fixed;" class="search_table" width="100%">
					<tr>
	                   	<th align="right">报表名称：</th>
						<td align="left">
							<input name="name" class="mini-textbox" style="width:165px;" enabled="false"/>
						</td>
						<th align="right">报送频次：</th>
						<td align="left">
							<input name="rate" class="mini-combobox" style="width:165px;" enabled="false"
								textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=RATE_TYPE"
								nullItemText="请选择" emptyText="请选择"/>
						</td>
					</tr>
					<tr>
						<th align="right">截至日期：</th>
						<td align="left">
							<input name="end_time" class="nui-datepicker" style="width:165px;" enabled="false"/>
						</td>
               		</tr>
		   		</table>
	   		</form>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;height:100%;" 
				sortMode="client" allowUnselect="false" url="<%=root%>/manualReport/getReceives" autoEscape="false">
				<div property="columns">
					<div field="user_id" visible="false">报送对象id</div>
					<div field="status" visible="false">完成状态</div>
					<div headerAlign="center" type="indexcolumn" align="center">序号</div>
			        <div field="id" visible="false" width="50" headerAlign="center" allowSort="true"><fmt:message key="ID"/></div>
			        <div field="orgid" allowSort="true" headerAlign="center" align="left" visible="false">报送对象id</div>
			        <div field="name" allowSort="true" headerAlign="center" align="center">报送对象</div>
                    <div field="task_submitdate" allowSort="true" headerAlign="center" align="center" renderer="dateFormat">报送时间</div>
                    <div field="status_name" allowSort="true" headerAlign="center" align="center">处理状态</div>
                    <div headerAlign="center" align="center" renderer="onRender">操作</div>
				</div>
			</div>
		</div>
		<div class="mini-toolbar" style="text-align: center;padding:10px 0;" 
		     borderStyle="border-left:0;border-bottom:0;border-right:0;">
               <!-- <a id="okBtn" class="mini-button" iconCls="" onclick="onOk()">确定</a>
               <span class="separator"></span> -->
               <a id="cancelBtn" class="mini-button" iconCls="" onclick="onCancel()">关闭</a>
	    </div>   
	</body>
</html>
<script type="text/javascript">
     
	$G.parse();
	var grid =$G.get("datagrid1");
	var form = $G.getForm("form1");

	function setData(data){
		data = $G.clone(data);
		form.setData(data);
		grid.load({id:data.rid});
	}
	
	//格式化日期
	function dateFormat(e){
		var value = e.value;
		if(value) return $G.formatDate(value,'yyyy-MM-dd HH:mm:ss');
		return "";
	}
	
	//添加操作链接
	function onRender(e){
		var index = grid.indexOf(e.record);
		var op = "";
		if($G.getbyName("status").getValue() == 3){
			op += '<a href="javascript:view('+index+')"><font color="blue">预览</font></a>';
		}else if(e.record.status < 3){
			op += '<a href="javascript:remind('+index+')"><font color="blue">催办</font></a>';
		}else if(e.record.status == 3){
			op += '<a href="javascript:sendback('+index+')"><font color="blue">退回</font></a>';
		}else{
			op += '<a href="javascript:view('+index+')"><font color="blue">预览</font></a>&emsp;'
				+ '<a href="javascript:sendback('+index+')"><font color="blue">退回</font></a>';
		}
		return op;
	}
	
	//退回
	function sendback(index){
		var row = grid.getRow(index);
		$G.prompt("确定退回此报送吗？并请输入退回原因", "提示", function(action, value) {
			if (action == 'ok') {
               	var ajaxConf = new GcdsAjaxConf();
               	ajaxConf.setSuccessFunc(function (){
                   	grid.reload();
                });
               	$G.postByAjax({id:row.id,reason:value}, "<%=root%>/manualReport/sendback", ajaxConf);
            }
        });
	}
	//预览
	function view(index){
		var row = grid.getRow(index);
		var url = "<%=root%>/manualReport/view";
		var bizParams = {rid:$G.getbyName("rid").getValue(),uid:row.user_id,orgid:row.orgid};
        $G.showmodaldialog("预览", url, 800, 600, bizParams, function(action){
	    	 grid.reload();
	    });
	}
	//催办
	function remind(index){
		var row = grid.getRow(index);
		
		 $.ajax({
	 		url : "<%=root%>/manualReport/checkdata",
	 		data:{rid:row.report_id,id:row.id},
	 		async:false,
	 		success : function(text) {
	 			var flag = text.flag;
	 			debugger
	 				if(flag=="true"){
	 					$G.GcdsConfirm("确定催办吗？", "提示", function(action) {
								if (action == 'ok') {
               						var ajaxConf = new GcdsAjaxConf();
               						ajaxConf.setSuccessFunc(function (){
                   					grid.reload();
                					});
               					$G.postByAjax({id:row.id}, "<%=root%>/manualReport/remind", ajaxConf);
            					}
        					});
	 					}else{
	 						nui.alert("该机构尚未认领填报任务！");
	 					}
	 			}
	 		}) 
		
	}
	
	function onOk(e) {
		$G.closemodaldialog("ok");
    }
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
</script>