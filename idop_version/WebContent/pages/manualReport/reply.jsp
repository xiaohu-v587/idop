<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>报送</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  			.mini-grid-newRow {
			    background: none;
			}
			#detailTable tr td:first-of-type{
				height: 35px;
			}
  		</style>
	</head>
	<body>    
		<div class="mini-fit">
			<form id="form1" method="post" enctype="multipart/form-data">
				<input name="id" class="nui-hidden"/>
	        	<input name="rid" class="nui-hidden"/>
	        	<input name="table" class="nui-hidden"/>
	        	<input name="status" class="nui-hidden"/>
	        	<input name="task_status" class="nui-hidden"/>
	        	<input name="receive_org" class="nui-hidden"/>
	           	<table style="table-layout: fixed;padding-right:10%;" id="detailTable"  width="100%">
	               	<tr>
	                   	<td align="right" width="100">报表名称：</td>
	                   	<td align="left">    
	                       	<input name="name" style="width:100%;" class="nui-textbox" enabled="false"/>
	                   	</td>
	               	</tr>
					<tr>
	                   	<td align="right">报表说明：</td>
	                   	<td align="left">    
							<input name="desc" style="width:100%;" class="nui-textarea" enabled="false"/>
						</td>
					</tr>
				</table>
				<div id="toolbar" class="nui-toolbar" style="border:0;padding:5px;height:32px;">
					<table style="width:90%;margin:0 5%;">
						<tr>
							<td align="left">
								<a class="nui-button" id="addBtn" iconCls="" onclick="addRow()">增加行</a>
								<span class="separator"></span>
								<a class="nui-button" id="delBtn" iconCls="" onclick="removeRow()">删除行</a>
							</td>
							<td align="right">
								<a class="nui-button" iconCls="" onclick="attachment()">附件</a>
								<!-- <span class="separator"></span>
								<a class="nui-button" iconCls="" onclick="upload()">上传</a> -->
							</td>
						</tr>
					</table>
				</div>
			</form>
			<div id="datagrid1" class="mini-datagrid" idField='id' dataField="data" 
					emptyText="没有数据" showPager='false' allowCellEdit="true" allowCellSelect="true"
					sortMode="client" showEmptyText="true" multiSelect="true" allowMoveColumn="false" virtualScroll="true"
					editNextOnEnterKey="true" skipReadOnlyCell="true" navEditMode="false" editNextRowCell="false">
			</div>
		</div>
		<div class="nui-toolbar"  style="text-align: center;padding: 10px 0;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a id="saveBtn" class="nui-button" iconCls="" onclick="save">保存</a>
			<span class="separator"></span>
			<a id="replyBtn" class="nui-button" iconCls="" onclick="reply">保存并提交</a>
			<span class="separator"></span>
			<a class="nui-button" iconCls="" onclick="onCancel">取消</a>       
		</div>        
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var grid = $G.get("datagrid1");
	
	//标准方法接口定义
	function setData(data) {
		data = $G.clone(data);
		form.setData(data);
		if(data.status==3 || data.task_status==3 || data.task_status==4){
			$G.get("saveBtn").disable();
			$G.get("replyBtn").disable();
			$G.get("addBtn").disable();
			$G.get("delBtn").disable();
		}
		if(data.fillway==1){
			$G.get("addBtn").disable();
			$G.get("delBtn").disable();
		}
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (data){
	    	var zdmc = data.zdmc.split(",");
	    	var zdlx = data.zdlx.split(",");
	    	var xlpz = data.xlpz.split(",");
	    	var columns = [{header:"序号",type:"indexcolumn",headerAlign:"center"}];
			for(var i=0;i<zdmc.length-1;i++){
	    		var editor = {};
	    		if(zdlx[i]!="null" && $G.getbyName("status").getValue()==2 && $G.getbyName("task_status").getValue() <3 ){
	    			<%-- editor = {type:zdlx[i],style:"width:100%"};
	    			if(zdlx[i]=="datepicker"){
	    				editor = {type:zdlx[i],style:"width:100%",valueType:"date"};
	    			}
	    			if(zdlx[i]=="spinner"){
	    				editor = {type:zdlx[i],style:"width:100%",allowLimitValue:false};
	    			}
	    			if(zdlx[i]=="combobox"){
	    				var url = "<%=root%>/zxparam/getCombox?key=" + zdbh[i];
	    				editor = {type:zdlx[i],style:"width:100%",valueField:"name",textField:"name",url:url,allowInput:false,nullItemText:"请选择",emptyText:"请选择",showNullItem:"true"};
	    			} --%>
	    			if(zdlx[i]=="文本"){
	    				editor = {type:"textbox"};
	    			}else if(zdlx[i]=="数字"){
	    				editor = {type:"spinner", allowLimitValue:false, allowNull:true,decimalPlaces:'2'};
	    			}else if(zdlx[i]=="日期"){
	    				editor = {type:"datepicker", valueFormat:"yyyy-MM-dd"};
	    			}else if(zdlx[i]=="下拉"){
	    				var option = xlpz[i].split("；");
	    				var arr = [];
	    				for(var j=0;j<option.length;j++){
	    					arr.add({name:option[j]});
	    				}
	    				editor = {type:"combobox", textField:"name", valueField:"name", data:arr, allowInput:false};
	    			}
	    		}
	    		columns.add({header:zdmc[i], field:"column"+(i+1), headerAlign:"center", align:"center", allowSort:true, editor:editor, autoShowPopup:true});
	    	}
	    	grid.setColumns(columns);
	    	grid.setData(data.table);
	    });
		$G.postByAjax({rid:data.rid, org:data.receive_org},"<%=root%>/manualReport/getTable",ajaxConf);
	}
	
	/*
	 *保存数据
	 */
	function save(){
		/* grid.validate();
		if(grid.isValid() == false){
			var error = grid.getCellErrors()[0];
			grid.beginEditCell(error.record, error.column);
			return;
		} */
		var rows = grid.getData();
		if(rows.length<1){
			$G.alert("你还没有新增数据");
			return;
		}
		/* if(grid.getChanges(null,true)<1){
			$G.closemodaldialog("ok");
			return;
		} */
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.result=='success'){
	    		$G.alert("保存成功","提示",function(){
		    		$G.closemodaldialog("ok");
	    		});
	    	}else{
	    		$G.alert("保存失败");
	    	}
		});
	    var gridjson = $G.encode(grid.getChanges(null,true));
	    $G.postByAjax({reply:false,id:$G.getbyName("id").getValue(),rid:$G.getbyName("rid").getValue(),"grid":gridjson}, "<%=root%>/manualReport/saveTable", ajaxConf);
	}
	
	//提交
	function reply(){
		var rows = grid.getData();
		if(rows.length<1){
			$G.alert("你还没有新增数据");
			return;
		}
		$G.GcdsConfirm("确定提交此任务吗？", "提示", function(action) {
			if (action == 'ok') {
				var ajaxConf = new GcdsAjaxConf();
			    ajaxConf.setIsShowProcessBar(false);
				ajaxConf.setIsShowSuccMsg(false);
			    ajaxConf.setSuccessFunc(function (text){
			    	if(text.result=='success'){
			    		$G.alert("提交成功","提示",function(){
				    		$G.closemodaldialog("ok");
			    		});
			    	}else{
			    		$G.alert("提交失败");
			    	}
				});
			    var gridjson = $G.encode(grid.getChanges(null,true));
			    $G.postByAjax({reply:true, id:$G.getbyName("id").getValue(), rid:$G.getbyName("rid").getValue(), grid:gridjson}, "<%=root%>/manualReport/saveTable", ajaxConf);
          	}
        });
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }

	function addRow() {
	    var newRow = {};
	    var index = grid.getData().length;
	    if(grid.getSelected()!=null){
	    	index = grid.indexOf(grid.getSelected());
	    }
	    grid.addRow(newRow, index);
	}
	
	function removeRow() {
	    var rows = grid.getSelecteds();
	    if(rows.length>0){
	    	grid.removeRows(rows, false);
	    }else{
	    	$G.alert("请先选择！");
	    }
	}
	
	//上传文件
	function attachment() {
        var url = "<%=root%>/manualReport/attachment";
        var bizParams = {id:$G.getbyName("rid").getValue(), view:1};
        $G.showmodaldialog("附件列表", url, 600, 400, bizParams, function(action){
	    	 grid.reload();
	    });
	}
	
	//格式化日期
	function dateFormat(e){
		if(e.value){
			return mini.formatDate(new Date(e.value), "yyyy-MM-dd");
		}
	}
	
</script>