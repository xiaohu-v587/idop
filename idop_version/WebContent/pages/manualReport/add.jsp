<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
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
			<input name="pageType" class="nui-hidden"/>      
			<form id="form1" method="post" action="<%=root%>/manualReport/excel" enctype="multipart/form-data">
	        	<input name="id" class="nui-hidden"/>
	           	<table style="table-layout: fixed;padding-right:10%;" id="detailTable"  width="100%">
	               	<tr>
	                   	<td align="right">报表名称：</td>
	                   	<td align="left">    
	                       	<input style="width:100%;" name="report_name" class="nui-textbox" required="true" />
	                   	</td>
	                   	<td align="right">报表说明：</td>
	                   	<td align="left">    
							<input name="desc" style="width:100%;" class="nui-textbox" required="true" />
						</td>
	                </tr>
	                <tr>
						<td align="right">填报方式：</td>
	               		<td align="left">
	               			<input name="way" class="mini-combobox" style="width:100%;" required="true" textfield="name" valuefield="key" emptyText="请选择" onvaluechanged="wayChanged"
							 <%-- url="<%=root%>/param/getCombox" --%>data='[{"key":"1","name":"网页补充填报"},{"key":"2","name":"excel或网页填报"},{"key":"3","name":"网页完全填报"}]'/>
	                 	</td>
	                 	<td align="right">机构关联字段：</td>
						<td align="left">    
	                       	<input name="field_org" class="nui-textbox" style="width:100%;" required="true"/>
	                   	</td>
						
	                </tr>
	                <tr>                   
	                    <td align="right">发送对象：</td>                    
	                    <td align="left">
	               			<input name="receive" style="width:80%;" class="nui-combobox" required="true"
							valueField="key" textField="name" emptyText="请选择" nullItemText="请选择"
							data='[{"key":"111","name":"玄武的负责人"},{"key":"222","name":"鼓楼的负责人"}]' value="111"/>
							<a class="nui-button" style="width:18%;float:right;" onclick="contact()">选择</a>
	                    </td>
	                   <td align="right">报送频次：</td>
	               		<td align="left">
	               			<input name="rate"  class="mini-combobox" style="width:100%;" required="true" textfield="name" valuefield="val" emptyText="请选择"
								url="<%=root%>/zxparam/getDict?key=RATE_TYPE"/>
	                    </td>
					</tr>
					<tr>
						<td align="right">截止日期：</td>
						<td align="left">
							<input name="end_date" class="nui-datepicker" required="true" emptyText="请选择" style="width:100%;"/>
						</td>
	               </tr>
	               <tr id="filerow" style="display:none;">
						<td align="right">表头及模板：</td>
	               		<td align="left" colspan="3">
	               			<input id="file1" name="excel" class="mini-htmlfile" style="width:100%;" limitType="*.xls;*.xlsx"  required="true"/>
	                    </td>
	               </tr>
				</table>
				<div id="toolbar" class="nui-toolbar" style="border:0;padding:5px;height:32px;display:none;">
					<table style="width:90%;margin:0 5%;">
						<tr>
							<td align="left">
								<a class="nui-button" iconCls="icon-addnew" onclick="addRow()">增加行</a>
								<span class="separator"></span>
								<a class="nui-button" iconCls="icon-remove" onclick="removeRow()">删除行</a>
							</td>
							<!-- <td align="right">
								<a class="nui-button" iconCls="icon-download" onclick="download()">模板下载</a>
								<span class="separator"></span>
								<a class="nui-button" iconCls="icon-upload" onclick="upload()">上传</a>
							</td> -->
						</tr>
					</table>
				</div>
			</form>
			<div id="datagrid1" class="mini-datagrid" idField='mid' style="width:90%;margin:0 5%;border-right:0;display:none;"
					  dataField="data" emptyText="无" showPager='false' allowCellEdit="true" allowCellSelect="true"
					  pageSize="10" showPageSize="false" showEmptyText="true" multiSelect="true">
				<div property="columns">
					<div type="checkcolumn" name="checkCloumn"></div>
					<div name="indexcolumn" type="indexcolumn" headerAlign="center" align="center">序号</div>		
					<div name="zdmc" field="zdmc" headerAlign="center" align="left">字段名称
						<input property="editor" class="mini-textbox" style="width:100%;"/>
					</div> 
				    <div name="zdlx" field="zdlx" headerAlign="center" align="center">字段类型
				    	<input property="editor" class="mini-combobox" style="width:100%;" textfield="name" valuefield="val"
                   		       url="<%=root%>/zxparam/getCombox?key=VALUE_TYPE" allowInput="false"/>
				    </div>
				</div>
			</div>
		</div>
		<div class="mini-toolbar" style="text-align: center;padding:10px 0;" 
		     borderStyle="border-left:0;border-bottom:0;border-right:0;">
               <a id="btnOk" class="mini-button" iconCls="" onclick="onOk">确定</a>
               <span class="separator"></span>
               <a id="btnCancle" class="mini-button" iconCls="" onclick="onCancel()">取消</a>
	    </div>     
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var grid = $G.get("datagrid1");
	var pageType = $G.getbyName("pageType");
	//$G.get("file1").disable();
	var filerow = $("#filerow");
	/* 
	上传文件通过form跳转到页面隐藏iframe来实现
	var iframe = document.createElement("iframe");
	iframe.width = 0;
	iframe.height = 0;
	iframe.border = 0;
	iframe.name = "form-iframe";
	iframe.id = "form-iframe";
	iframe.setAttribute("style","width:0;height:0;border:none");
	document.getElementById("form1").appendChild(iframe);
	document.getElementById("form1").target = "form-iframe";
	
	iframe.onload = function(){
		//var excel = document.createElement("excel");
		var responseData = this.contentDocument.body.textContent || this.contentWindow.document.body.textContent;
		var tablehead = JSON.parse(responseData).table;
		//删掉iframe
		setTimeout(function(){
			var _frame = document.getElementById("form-iframe");
			_frame.parentNode.removeChild(_frame);
		}, 100);
	}
	document.getElementById("form1").submit();
	*/
	
	$G.get("file1").on("fileselect",function(){
		var suffix = $G.get("file1").getValue().trim();
		// 异步请求数据
    	var file = $("#file1 > input:file")[0];
 		if("" != suffix){
 			suffix = suffix.substr(suffix.lastIndexOf(".") + 1);
 			suffix = suffix.toUpperCase();
 	 		if(!("XLS" == suffix || "XLSX" == suffix)){
 	 			return; 			
 	 		}else{
 	 			$.ajaxFileUpload({
 	 	            url: "<%=root%>/manualReport/excel",
 	 	            fileElementId: file,
 	 	            dataType: 'text',
 	 	            success: function (data, status){
 	 	           		grid.setData();
 	 	           		grid.show();
 	 	           		var table = data.split(",");
 	 	           		for(var i=0;i<table.length;i++){
 	 	           			var row = {zdmc:table[i]};
 	 	           			var index = grid.getData().length;
 	 	           			grid.addRow(row,index);
 	 	           		}
 	 	            },
 	 	            error: function (data, status, e){
 	 	            	$G.alert("文件获取失败!","提示",function(){
 	 	           			//$G.closemodaldialog("ok");
 	 	           		});
 	 	            },
 	 	            complete: function(){
 	 	            	var jq = $("#file1 > input:file");
 	 	            	jq.before(file);
 	 	            	jq.remove();
 	 	            }
 	 	        });
 	 		}
 		}
	});
	
	//标准方法接口定义
	<%-- function setData(data) {
		data = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
		if (data.pageType == "edit") {
            $G.getbyName("pageType").setValue(data.pageType);
            var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
		    	form.setData(text.record);
		    	grid.setDate(text.record);
			});
			$G.postByAjax({key:data.id},"<%=root%>/manualReport/getDetail",ajaxConf);
		}else{
			//$G.getbyName("name").setValue(data.name);
			//$G.getbyName("key").setValue(data.key);
		}
	} --%>
	
	function wayChanged(){
		if($G.getbyName("way").value=="1"){
			filerow.show();
			//$G.get("file1").enable();
			$G.get("toolbar").hide();
			grid.hide();
		}else if($G.getbyName("way").value=="2"){
			filerow.show();
			//$G.get("file1").enable();
			$G.get("toolbar").hide();
			grid.hide();
		}else{
			$G.get("toolbar").show();
			filerow.hide();
			//$G.get("file1").disable();
			//grid.setData();
			grid.show();
		}
	}
	
	/*
	 *保存数据
	 */
	function save(){
		var name = $G.getbyName("name").getValue();
		var end_date = $G.getbyName("end_date").getValue();
		var receive = $G.getbyName("receive").getValue();
		var cc = $G.getbyName("cc").getValue();
		var desc = $G.getbyName("desc").getValue();
		if ( name == '' || end_date == '' || receive == '' || cc == '' || desc == '' ) {
			$G.alert("任务信息填写不完整！");
			return;
		}
		var rows = grid.getData();
		if(rows.length>0){
			for(var i=0;i<rows.length;i++){
				if(rows[i].zdmc=="" || rows[i].zdmc==null ){
					$G.alert("列名有部分未填写");
					return;
				}else if(rows[i].zdmc.includes(",")){
					$G.alert("列名中不能包含','符号");
					return;
				}
			}
		}else{
			$G.alert("表内容为空");
			return;
		}
    	var urlStr = "<%=root%>/manualReport/save";
    	if(pageType=="edit"){//表示为编辑状态
    		urlStr = "<%=root%>/manualReport/update";
    	}
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.result=='success'){
	    		$G.alert("保存成功");
	    		//$G.closemodaldialog("ok");
	    	}else{
	    		$G.alert("保存失败");
	    	}
		});
	    var gridjson = $G.encode(grid.getData());
	    var formjson = $G.encode(form.getData());
	    //$G.submitForm("form1", urlStr, ajaxConf);
	    $G.postByAjax({"form":formjson,"grid":gridjson}, urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	function addRow() {
	    var newRow = {};
	    var index = grid.getData().length;
	    if(grid.getSelected()!=null){
	    	index = grid.indexOf(grid.getSelected())+1;
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
	
	function download(){
		window.location="<%=root%>/manualReport/download";
	}
	
	function upload(){
		$.ajax({
			url:"<%=root%>/manualReport/upload",
			data:$("#form1").serialize(),
			type:"post",
			dataType:"json",
			success:function(){$G.alert("上传成功！");},
			error:function(){$G.alert("后台无响应");}
		});
	}
	
	//新增	
	function contact(){
		var url = "<%=root%>/manualReport/groupuser";
        $G.showmodaldialog("选择联系人", url, 800, 600, null, function(data){
        	if(data!=null){
	        	$G.getbyName("receive").setValue(data.ids);
	        	$G.getbyName("receive").setText(data.names);
	        	$G.getbyName("receive").enable();
       		}
	    });
	}
	
</script>