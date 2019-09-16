<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
  		<style type="text/css">
  			.mini-grid-newRow {
			    background: none;
			}
			#detailTable tr td:nth-of-type(1),#detailTable tr td:nth-of-type(3){
				width: 100px;
				height: 35px;
			}
  		</style>
	</head>
	<body> 
		<div class="mini-fit">
			<form id="form1" method="post" action="<%=root%>/manualReport/excel" enctype="multipart/form-data">
				<input name="pageType" class="nui-hidden"/>
	        	<input name="reportId" class="nui-hidden"/>
	        	<input name="task_table_name" class="nui-hidden"/>
	           	<table style="table-layout: fixed;padding-right:10%;padding-top:10px" id="detailTable"  width="100%">
	               	<tr>
	                   	<td align="right">报表名称：</td>
	                   	<td align="left">    
	                       	<input style="width:100%;" name="tname" class="nui-textbox" maxLength="30" required="true"/>
	                   	</td>
	                   	<td align="right">发送对象：</td>                    
								<td align="left">
									<input name="groupId" style="width:70%;" class="nui-combobox"
							valueField="id" textField="group_name" emptyText="我的群组"
							url="<%=root%>/manualReport/myGroup" required="true"/>
							<input name="itemOrgIds" class="nui-hidden"/>
							<input name="itemUserIds" class="nui-hidden"/>
							<a class="nui-button" style="float:right;" onclick="select()">自定义</a>
								</td>
	                </tr>
	                <tr>                   
	                	<td align="right">报送频次：</td>
	               		<td align="left">
	               			<input name="rate"  class="mini-combobox" style="width:100%;" required="true" textfield="name" valuefield="val" emptyText="请选择"
								url="<%=root%>/zxparam/getDict?key=RATE_TYPE" value="0"/>
	                    </td>
	                    <td align="right">截止日期：</td>
						<td align="left">
							<input name="end_time" class="nui-datepicker" required="true" emptyText="请选择" style="width:100%"/>
						</td>
					</tr>
					<tr>
						<td align="right">填报方式：</td>
	               		<td align="left">
	               			<input name="fillway" class="mini-combobox" style="width:100%;" required="true" textfield="name" valuefield="key" emptyText="请选择"
							 <%-- url="<%=root%>/param/getCombox" --%>data='[{"key":"1","name":"网页补充填报"},{"key":"2","name":"网页完全填报"}]' onvaluechanged="wayChanged"/>
	                 	</td>
	                    <td align="right">机构关联字段：</td>
						<td align="left">    
	                       	<input name="org_field" class="nui-combobox" style="width:100%;" textfield="name" valuefield="val"
	                       	nullItemText="请选择" emptyText="请选择" showNullItem="true"/>
	                   	</td>
	               </tr>
	               <tr>
						<td align="right">表头及模板：</td>
	               		<td align="left">
	               			<input id="file" name="file" class="mini-htmlfile" style="width:100%;" limitType="*.xls;*.xlsx"  required="true"/>
	               			<input name="file_path" class="mini-hidden"/>
	               			<input name="file_name" class="mini-hidden"/>
	                    </td>
	                    <td align="right">模块：</td>
	               		<td align="left">
	               			<input name="busi_module" id="busi_module"  class="mini-combobox" style="width:100%;" required="true" valueField="val" textField="remark" emptyText="请选择"
								url="<%=root%>/param/getDict?key=dop_moduletype" />
	                    </td>
	                    <td align="right" style="display:none">机构信息：</td>
						<td align="left" style="display:none">    
	                       	<a class="nui-button" iconCls="" onclick="exportOrg()">下载</a>
	                   	</td>
	               </tr>
	               <tr>
	               		<td align="right">报表说明：</td>
	                   	<td align="left" colspan="3">    
							<input name="detail" style="width:100%;height:100px;" class="nui-textarea" maxLength="600" required="true"/>
						</td>
					</tr>
				</table>
				<div id="toolbar" class="nui-toolbar" style="border:0;padding:5px;height:32px;display:none;">
					<table style="width:90%;margin:0 5%;">
						<tr>
							<td align="left">
								<a class="nui-button" iconCls="" onclick="addRow()">增加行</a>
								<span class="separator"></span>
								<a class="nui-button" iconCls="" onclick="removeRow()">删除行</a>
							</td>
							<td align="right">
								
								<font color="blue">下拉框配置请以“；”分隔选项</font>
							</td>
						</tr>
					</table>
				</div>
			</form>
			<div id="datagrid1" class="mini-datagrid" idField='mid' style="width:90%;margin:0 5%;border-right:0;display:none;"
					  showPager='false' allowCellEdit="true" allowCellSelect="true" onlyCheckSelection="true" 
					  multiSelect="true" allowMoveColumn="false" allowCellWrap="false" editNextRowCell="true" editNextOnEnterKey="true" createOnEnter="false">
				<div property="columns">
					<div type="checkcolumn" name="checkCloumn"></div>
					<div name="indexcolumn" type="indexcolumn" headerAlign="center" align="center">序号</div>
					<div name="zdmc" field="zdmc" headerAlign="center">字段名称
						<input property="editor" class="mini-textbox" style="width:100%"/>
					</div> 
				    <div name="zdlx" field="zdlx" headerAlign="center" autoShowPopup="true">字段类型
				    	<input property="editor" class="mini-combobox" style="width:100%;" textfield="name" valuefield="name"
                   		       url="<%=root%>/zxparam/getDict?key=VALUE_TYPE" allowInput="false"
                   		       nullItemText="未定义时则不可修改" emptyText="请选择" showNullItem="true"/>
				    </div>
				    <div name="xlpz" field="xlpz" headerAlign="center">下拉配置
				    	<input property="editor" class="mini-textbox" style="width:100%" emptyText="请以“；”分隔选项"/>
				    </div>
				</div>
			</div>
			<div id="datagrid2" class="mini-datagrid" idField='mid' style="width:90%;margin:5%;border-right:0;display:none;"
					  dataField="data" emptyText="无" showPager='false' allowCellEdit="true" allowCellSelect="true"
					  pageSize="10" showPageSize="false" showEmptyText="true" multiSelect="true">
				<div property="columns"></div>
			</div>
		</div>
		<div class="mini-toolbar" style="text-align: center;padding:10px 0;" 
		     borderStyle="border-left:0;border-bottom:0;border-right:0;">
               <a id="saveBtn" class="mini-button" iconCls="" onclick="onSave()">保存</a>
               <span class="separator"></span>
               <!-- <a id="startBtn" class="mini-button" iconCls="" onclick="onStart()">发布</a>
               <span class="separator"></span> -->
               <a id="cancelBtn" class="mini-button" iconCls="" onclick="onCancel()">取消</a>
	    </div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var grid = $G.get("datagrid1");
	var grid2 = $G.get("datagrid2");//表格预览
	var cb = $G.getbyName("groupId");
	var file = $G.get("file");
	var pageType = $G.getbyName("pageType");
	var filerow = $("#filerow");
	$G.get("file").disable();
	$G.getbyName("end_time").setMinDate(new Date());
	
	grid.on("cellendedit",function(e){
		var table = grid.getData();
		var arr = new Array();
		for(var i=0;i<table.length;i++){
			arr.add({val:"column"+(i+1),name:table[i].zdmc});
		}
		$G.getbyName("org_field").setData(arr);
	});
	
	var zdlx = true;
	var xlpz = false;
	grid.on("cellclick",function(e){
		/* if(zdlx && e.field == "zdlx"){
			$G.alert("未选择将不可修改");
			zdlx = false;
		}else  */if(xlpz && e.field == "xlpz"){
			$G.alert("请以“；”分隔选项");
			xlpz = false;
		}
	});
	
	 cb.on("valuechanged",function(e){
		$G.getbyName("itemOrgIds").setValue();
    	$G.getbyName("itemUserIds").setValue();
	}); 
	
	file.on("fileselect",function(){
		var suffix = file.getValue().trim();
		// 异步请求数据
    	var excel = $("#file > input:file")[0];
 		if("" != suffix){
 			suffix = suffix.substr(suffix.lastIndexOf(".") + 1);
 			suffix = suffix.toUpperCase();
 	 		if(!("XLS" == suffix || "XLSX" == suffix)){
 	 			$G.alert("选择的文件类型不是表格");
 	 			return; 			
 	 		}else{
 	 			$G.get("saveBtn").disable();
 	 			$.ajaxFileUpload({
 	 	            url: "<%=root%>/manualReport/saveFile",
					data:{excel:"true"},
					fileElementId: excel,
 	 	            dataType: 'text',
 	 	            success: function (data, status){
 	 	            	$G.get("saveBtn").enable();
 	 	           		grid.setData();
 	 	          	 	grid.show();
 	 	           		/* grid2.setData();
 	 	           		grid2.show(); */
 	 	           		data = JSON.parse(data);
 	 	           		var head = data.table[0];
 	 	           		data.table.remove(head);
 	 	           		var columns = [];
 	 	           		var i=1;
 	 	           		//遍历列名
 	 	           		while(head.hasOwnProperty("column"+i)){
 	 	           			var column = "column"+i;
 	 	           			columns.add({header:head[column],field:column,headerAlign:"center",align:"center"});
 	 	           			i++;
 	 	           		};
 	 	           		/* grid2.setColumns(columns);
 	 	           		grid2.setData(data); */
 	 	           		var arr = [];
 	 	           		for(var j=1;j<=columns.length;j++){
 	 	           			var row = {zdmc:head["column"+j]};
 	 	           			var index = grid.getData().length;
 	 	           			grid.addRow(row,index);
 	 	           			arr.add({val:"column"+(j),name:grid.getData()[j-1].zdmc});
 	 	           		}
		 	 	  		$G.getbyName("org_field").setData(arr);
		 	 	 	 	$G.getbyName("file_path").setValue(data.filepath);
		 	 	 	 	$G.getbyName("file_name").setValue(data.filename);
 	 	            },
 	 	            error: function (data, status, e){
 	 	            	$G.alert("文件获取失败!","提示",function(){
 	 	           			//$G.closemodaldialog("ok");
 	 	           		});
 	 	            },
 	 	            complete: function(){
 	 	            	var jq = $("#file > input:file");
 	 	            	jq.before(excel);
 	 	            	jq.remove();
 	 	            }
 	 	        });
 	 		}
 		}
	});
	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
	//标准方法接口定义
	function setData(data) {
		data = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
		if (data.pageType == "edit") {
            $G.getbyName("pageType").setValue(data.pageType);
            var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (data){
		    	$G.getbyName("reportId").setValue(data.id);
		    	$G.getbyName("task_table_name").setValue(data.task_table_name);
		    	$G.getbyName("tname").setValue(data.task_name);
		    	if(data.group_id && data.group_id != "null"){
		    		$G.getbyName("groupId").setValue(data.group_id);
		    	}else{
		    		$G.getbyName("itemOrgIds").setValue(data.itemorgids);
			    	$G.getbyName("itemUserIds").setValue(data.itemuserids);
			    	$G.getbyName("groupId").setValue("null");
			    	$G.getbyName("groupId").setText(data.itemnames);
		    	}
		    	$G.getbyName("rate").setValue(data.task_frequency);
		    	$G.getbyName("end_time").setValue(data.task_enddate);
		    	$G.getbyName("fillway").setValue(data.task_fillway);
 	 	 	 	$G.getbyName("file").setText(data.task_file_name);
 	 	 	 	$G.getbyName("busi_module").setValue(data.busi_module);
		    	wayChanged();
	          	grid.show();
		    	$G.getbyName("file_path").setValue(data.task_attachments);
 	 	 	 	$G.getbyName("file_name").setValue(data.task_file_name);
		    	$G.getbyName("detail").setValue(data.task_detail);
		    	if(data.table_head){
		    		var zdmc = data.table_head.split(",");
			    	var zdlx = data.fields_location.split(",");
			    	var xlpz = data.serial_number.split(",");
			    	var arr = [];
			    	for(var i=0;i<zdmc.length-1;i++){
			    		var row;
			    		if(zdlx[i]=="null"){
			    			row = {zdmc:zdmc[i]};
			    		}else{
			    			row = {zdmc:zdmc[i],zdlx:zdlx[i]};
			    			if(xlpz[i]!="null"){
			    				row = {zdmc:zdmc[i],zdlx:zdlx[i],xlpz:xlpz[i]};
			    			}
			    		}
	           			grid.addRow(row,grid.getData().length);
	           			arr.add({val:"column"+(i+1),name:grid.getData()[i].zdmc});
			    	}
	 	 	  		$G.getbyName("org_field").setData(arr);
			    	$G.getbyName("org_field").setValue(data.org_field);
		    	}
			});
			$G.postByAjax({id:data.id},"<%=root%>/manualReport/getReport",ajaxConf);
		}else{
			//$G.getbyName("name").setValue(data.name);
			//$G.getbyName("key").setValue(data.key);
		}
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
	
	//选择接受对象	
	function select(){
		var url = "<%=root%>/manualReport/groupitem";
		var bizParams = {org:$G.getbyName("itemOrgIds").getValue(),usr:$G.getbyName("itemUserIds").getValue(),name:$G.getbyName("groupId").getText()};
		$G.showmodaldialog("选择发送对象", url, 800, 600, bizParams, function(data){
        	if(data.act=="addgroup"){
        		$G.getbyName("groupId").load("<%=root%>/manualReport/myGroup");
        	}else{
        		$G.getbyName("groupId").setValue("null");
	        	$G.getbyName("groupId").setText(data.name);
	        	$G.getbyName("groupId").enable();
	        	$G.getbyName("itemOrgIds").setValue(data.org);
	        	$G.getbyName("itemUserIds").setValue(data.usr);
        	}
	    });
	}
	
	function wayChanged(){
		if($G.getbyName("fillway").value=="1"){
			//补充填报
			filerow.show();
			file.enable();
			$G.get("toolbar").hide();
			grid.hide();
		}else if($G.getbyName("fillway").value=="2"){
			//完全填报
			$G.get("toolbar").show();
			filerow.hide();
			file.disable();
			//grid.setData();
			grid.show();
			$G.getbyName("file_path").setValue();
			$G.getbyName("file_name").setValue();
		}else{
			filerow.show();
			file.enable();
			$G.get("toolbar").hide();
			grid.hide();
		}
	}
	
	function exportOrg(){
		location = "<%=root%>/org/download";
	}
	
	//保存数据
	function onSave(){
		//表单数据校验
		form.validate();
		if(!form.isValid()){
			return;
		}
		//表格数据校验
		var rows = grid.getData();
		if(rows.length>0){
			var arr = [];
			for(var i=0;i<rows.length;i++){
				if(rows[i].zdmc == null || rows[i].zdmc.trim() == ""){
					$G.alert("字段名称有部分未填写");
					return;
				}else if(rows[i].zdmc.indexOf(",") != -1){
					$G.alert("字段名称不能包含','符号");
					return;
				}
				if(rows[i].zdlx=="下拉"){
					if(rows[i].xlpz == null || rows[i].xlpz.trim() == ""){
						$G.alert("字段类型为下拉时需填写下拉配置");
						return;
					}else if(rows[i].xlpz.indexOf(",") != -1){
						$G.alert("下拉配置不能包含','符号");
						return;
					}
				}
				if(arr.contains(rows[i].zdmc.trim())){
					$G.alert("字段名称不能重合");
					return;
				}
				arr.add(rows[i].zdmc.trim());
			}
		}else{
			$G.alert("报表还没有定义字段");
			return;
		}
    	var urlStr = "<%=root%>/manualReport/saveReport";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsAsync(true);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.result=='success'){
	    		$G.alert("保存成功","提示",function(){
		    		$G.closemodaldialog("ok");
	    		});
	    	}else{
	    		$G.alert("保存失败");
	    	}
		});
	    var gridjson = $G.encode(grid.getData());
	    var formjson = $G.encode(form.getData());
	    $G.postByAjax({"form":formjson,"grid":gridjson}, urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
</script>