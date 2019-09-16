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
  		</style>
	</head>
	<body>    
		<div style="width:100%;height:15%;color:#ffffff;">
			<form id="form1" method="post">
				<input name="pageType" class="nui-hidden"/>
	        	<input name="base_id" class="nui-hidden"/>
	        	<input name="gridData" class="nui-hidden"/>
	        	<input name="orgIds" class="nui-hidden"/>
	           	<table style="table-layout: fixed;padding-top: 20px;" id="detailTable"  width="100%">
	           		<colgroup>
				       	<col width="17%"/>
				       	<col width="32%"/>
				       	<col width="17%"/>
				       	<col width="32%"/>
					</colgroup>
	               	<tr>
	                   	<td align="right">表英文名：</td>
	                   	<td align="left">    
	                       	<input name="table_en_name" class="nui-textbox" required="true"/>
	                   	</td>
	                   	<td align="right">表中文名：</td>
	                    <td align="left">    
	                        <input name="table_cn_name" class="nui-textbox" required="true"/>
	                    </td>
	               	</tr>
	               	<tr>
	                   	<td align="right">日期关联字段：</td>
	                   	<td align="left">
	                       	<input name="date_filed_name" class="nui-textbox" required="true"/>
	                   	</td>
	                   	<td align="right">机构关联字段：</td>
	                    <td align="left">    
	                        <input name="org_filed_name" class="nui-textbox" required="true"/>
	                    </td>
	               	</tr>
				</table>
			</form>
		</div>
		<div style="width:100%;height:60%;padding-top: 20px; border-bottom: solid;
			    border-color: #2fa2fd; border-width: 1px;overflow: hidden;">
			<div class="nui-toolbar" style="border-top:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
							<a class="nui-button" iconCls="icon-addnew" onclick="addRow()">新增行</a>
<!-- 							<span class="separator"></span> -->
		        	     	<a class="mini-button" iconCls="icon-remove" id="remove" onclick="removeRow()">删除行</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" href="<%=request.getContextPath()%>/xml/字段上传模板.xls" iconCls="icon-undo" id="modelDown" onclick="modelDown()">模板下载</a>
		                    <a class="nui-button" iconCls="icon-search" id="upload" onclick="ajaxFileUpload()">上传</a>
		                    <input class="mini-htmlfile" name="upload_file"  id="file1" style="width:200px;"/>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div id="datagrid1" class="mini-datagrid" idField='mid' style="width:100%;height:90%;"
				  showPager='false' allowCellEdit="true" allowCellSelect="true" multiSelect="true">
				<div property="columns">
				 	<div name="filed_id" field="filed_id" visible="false">字段ID</div>
					<div type="checkcolumn" name="checkCloumn"  width="5"></div>	
					<div name="sort_num" field="sort_num" headerAlign="center" align="right"  width="8">序号
						<input property="editor" class="mini-textbox" style="width:100%;"/>
					</div> 
					<div name="filed_en_name" field="filed_en_name" headerAlign="center" align="left"  width="10">字段名称
						<input property="editor" class="mini-textbox" style="width:100%;"/>
					</div> 
					<div name="filed_cn_name" field="filed_cn_name" headerAlign="center" align="right" width="10">字段别名
						<input property="editor" class="mini-textbox" style="width:100%;"/>
					</div> 
					<div name="filed_length" field="filed_length" headerAlign="center" align="left" width="10">字段长度
						<input property="editor" class="mini-textbox" style="width:100%;"/>
					</div> 
					<div name="filed_type" field="filed_type" headerAlign="center" align="right" width="10">字段类型
						<input property="editor" class="mini-textbox" style="width:100%;"/>
					</div> 
					<div name="description" field="description" headerAlign="center" align="left" width="15">字段描述
						<input property="editor" class="mini-textbox" style="width:100%;"/>
					</div>
				</div>
			</div>
		</div>
		<div style="width:100%;height:10%;color:#ffffff;">
			<form id="form2" method="post">
	           	<table style="table-layout: fixed;padding-top: 10px;" id="detailTable"  width="100%">
	           		<colgroup>
				       	<col width="17%"/>
				       	<col width="32%"/>
				       	<col width="17%"/>
				       	<col width="32%"/>
					</colgroup>
	               	<tr>
	                   	<td align="right">授权：</td>
	                   	<td align="left" colspan="3">    
							<input id="orgTree"  class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" style="width:280px"
								name="orgTree" textfield="orgname" valuefield="id" parentfield="upid"  valueFromSelect="false" multiSelect="true"
	    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="onCloseClick" showRadioButton="false" showFolderCheckBox="true"
	       						popupWidth="280" popupHeight="300" popupMaxHeight="350"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" iconCls="icon-save" onclick="save">保存</a>
			<span class="separator"></span>
			<a class="nui-button" iconCls="icon-close" onclick="onCancel">返回</a>       
		</div>        
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form1 = $G.getForm("form1");
	var form2 = $G.getForm("form2");
	var grid =$G.get("datagrid1");
	var tree =$G.get("orgTree");
	
	//标准方法接口定义
	function setData(data) {
		var infos = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
		var pageType = infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if (pageType == "edit") {
			$G.get("modelDown").hide();
    		$G.get("upload").hide();
    		$G.get("file1").hide();
			//获取明细
            var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
		    	form1.setData(text.record);
		    	$G.getbyName("pageType").setValue(pageType);
		    	grid.setData(text.reList);
		    	//编辑时，判断父表是否有子表，有不显示删除按钮，模板上传
		    	if(true == text.isExist){
		    		$G.get("remove").hide();
		    	}
		    	//权限赋值
		    	$G.getbyName("orgTree").setValue(text.orgTree);
			});
			$G.postByAjax({base_id:infos.base_id},"<%=root%>/zxCustomSearch/getDetail",ajaxConf);
		}
	}
	
	/*
	 *保存数据
	 */
	function save(){
		//将grid数据反序列成json数据
		$G.getbyName("gridData").setValue($G.encode(grid.getData()));
		//选中的权限机构
		var orgIds = $G.get("orgTree").getValue();
		if(null!=orgIds&&""!=orgIds){
			$G.getbyName("orgIds").setValue(orgIds);
		}
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.result=='success'){
	    		$G.alert("保存成功");
	    		$G.closemodaldialog("ok");
	    	}else{
	    		$G.alert("保存失败");
	    	}
		});
	    $G.submitForm("form1", "<%=root%>/zxCustomSearch/addBaseModel", ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	function addRow() {
	    var newRow = {};
	    grid.addRow(newRow);
	}
	
	function removeRow() {
	    var rows = grid.getSelecteds();
	    if(rows.length>0){
	    	grid.removeRows(rows, true);
	    }else{
	    	$G.alert("请先选择一条数据！");
	    }
	}
	
	function ajaxFileUpload() {
        var inputFile = $("#file1 > input:file")[0];
        $.ajaxFileUpload({
            url: "<%=root%>/zxCustomSearch/importData?",                 //用于文件上传的服务器端请求地址
            fileElementId: inputFile,               //文件上传域的ID
            //data: { a: 1, b: true },            //附加的额外参数
            dataType: 'json_pre',                   //返回值类型 一般设置为json
            success: function (data)    //服务器成功响应处理函数
            {	
            	grid.setData([]);
            	if(data.code=="0000"){
            		$G.alert(data.desc,"提示",function(){
            			//$G.closemodaldialog("ok");
            		});
            	}
				if(data.code=="9999"){
					$G.alert(data.desc,"提示",function(){
            			//$G.closemodaldialog("ok");
            		});
            	}
            	grid.setData(data.jsonData);
            },
            error: function (e)   //服务器响应失败处理函数
            {
                alert(e);
            },
            complete: function () {
                var jq = $("#file1 > input:file");
                jq.before(inputFile);
                jq.remove();
            }
        });
    }
	
	 // 下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
</script>