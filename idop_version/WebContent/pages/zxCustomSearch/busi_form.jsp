<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  			.mini-grid-newRow {
			    background: none;
			}
  		</style>
	</head>
	<body>    
	<div class="nui-fit">
		<div style="width:100%;height:75%;border-bottom: solid;
			    border-color: #2fa2fd; border-width: 1px;color:#ffffff;">
			<form id="form1" method="post">
	        	<input name="base_pid" class="nui-hidden"/>
	        	<input name="base_id" class="nui-hidden"/>
	        	<input name="orgIds" class="nui-hidden"/>
	        	<input name="filedIds" class="nui-hidden"/>
	        	<input name="pageType" class="nui-hidden"/>
	        	<input name="base_seq_name" class="nui-hidden"/>
	           	<table style="table-layout: fixed;padding-top: 20px;" id="detailTable"  width="100%">
	           		<colgroup>
				       	<col width="17%"/>
				       	<col width="32%"/>
				       	<col width="17%"/>
				       	<col width="32%"/>
					</colgroup>
	               	<tr>
	                   	<td align="right">表名：</td>
	                   	<td align="left" colspan="3">    
	                       	<input name="table_cn_pname" class="nui-textbox"  allowInput="false" style="width:280px"/>
	                       	<input name="table_en_pname" class="nui-hidden"/>
	                   	</td>
	                   	<td align="right"> </td>
	                    <td align="left"> </td>
	               	</tr>
	               	<tr>
	                   	<td align="right">创建子表名：</td>
	                   	<td align="left" colspan="3">
	                       	<input name="table_cn_name" class="nui-textbox" required="true" style="width:280px"/>
	                   	</td>
	                   	<td align="right"> </td>
	                    <td align="left"> </td>
	               	</tr>
	               	<tr style="width:100%;height: 200px;overflow: hidden;">
	                   	<td align="right">
	                   		勾选字段：<br/>
	                   		<a class="nui-button" id="selectAll" onclick="selectAll()">全选</a>&nbsp;&nbsp;
	                   	</td>
	                   	<td align="left" colspan="3" id="fileds">
	                   	</td>
	               	</tr>
				</table>
			</form>
		</div>
		
		<div style="width:100%;height:10%;color:#ffffff;">
			<input name="pageType" class="nui-hidden"/>      
			<form id="form2" method="post">
	        	<input name="id" class="nui-hidden"/>
	           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
	           		<colgroup>
				       	<col width="17%"/>
				       	<col width="32%"/>
				       	<col width="17%"/>
				       	<col width="32%"/>
					</colgroup>
	               	<tr>
	                   	<td align="right">授权：</td>
	                   	<td align="left" colspan="3">    
							<input id="orgTree" class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" style="width:280px"
								name="orgTree" textfield="orgname" valuefield="id" parentfield="upid"  valueFromSelect="false" multiSelect="true"
	    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="onCloseClick" showRadioButton="false" showFolderCheckBox="true"
	       						popupWidth="280" popupHeight="200" popupMaxHeight="350"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" iconCls="icon-ok" onclick="save">创建下载任务</a>
			<span class="separator"></span>
			<a class="nui-button" iconCls="icon-close" onclick="onCancel">返回</a>       
		</div> 
		</div>       
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var tree = $G.get("tree1");
	var selFlag = "1"; //定义一个标志，用于全选/反选
	
	//标准方法接口定义
	function setData(data) {
		var infos = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
		$G.getbyName("pageType").setValue(infos.pageType);
		$G.getbyName("base_pid").setValue(infos.base_pid);
		$G.getbyName("table_cn_pname").setValue(infos.table_cn_pname);
		checkBoxLayout(infos.base_pid);
		if (infos.pageType == "add") {
			$G.getbyName("base_seq_name").setValue(infos.base_seq_name);
			$G.getbyName("table_en_pname").setValue(infos.table_en_pname);
		}else if (infos.pageType == "edit") {
			$G.getbyName("base_id").setValue(infos.base_id);
			$G.getbyName("table_cn_name").setValue(infos.table_cn_name);
	    	$G.getbyName("orgTree").setValue(infos.org_ids);
	    	setCheckBoxValue(infos.filed_ids);
		}
	}
	
	function checkBoxLayout(base_pid){
		 var ajaxConf = new GcdsAjaxConf();
		 ajaxConf.setIsShowProcessBar(true);
		 ajaxConf.setIsAsync(false);
		 ajaxConf.setIsShowSuccMsg(false);
	     ajaxConf.setSuccessFunc(function (text){
	    	var fieldList = text.fieldList;
	    	if(fieldList&&fieldList.length>0){
	    		var htmlStr = "<div style='width:100%;height: 185px;overflow-y: scroll;overflow: auto;'><table style='width:100%;'>";
	    		var a = parseInt(fieldList.length/3);//取整
		    	var b = fieldList.length%3;//取余
		    	for(var i=0;i<a;i++){
		    		htmlStr +=" <tr>"
				    		+"    	<td align='left'>"
				    		+"    		<input name='filed' id='"+fieldList[i*3].filed_id+"' value='"+fieldList[i*3].filed_id+"' class='nui-checkbox' />"
				    		+"    		<span>"+fieldList[i*3].filed_cn_name+"</span>"
				    		+"    	</td>"
				    		+"    	<td align='left'>"
				    		+"    		<input name='filed' id='"+fieldList[i*3+1].filed_id+"' value='"+fieldList[i*3+1].filed_id+"' class='nui-checkbox' />"
				    		+"    		<span>"+fieldList[i*3+1].filed_cn_name+"</span>"
				    		+"    	</td>"
				    		+"    	<td align='left'>"
				    		+"    		<input name='filed' id='"+fieldList[i*3+2].filed_id+"' value='"+fieldList[i*3+2].filed_id+"' class='nui-checkbox' />"
				    		+"    		<span>"+fieldList[i*3+2].filed_cn_name+"</span>"
				    		+"    	</td>"
				    		+" </tr>";
		    	}
		    	if(b>0){
		    		htmlStr +=" <tr>";
			    	for(var j=0;j<b;j++){
			    		htmlStr +="    	<td align='left'>"
					    		+"    		<input name='filed' id='"+fieldList[3*a+j].filed_id+"' value='"+fieldList[3*a+j].filed_id+"' class='nui-checkbox' />"
					    		+"    		<span>"+fieldList[3*a+j].filed_cn_name+"</span>"
					    		+"    	</td>";
			    	}
		    		htmlStr +=" </tr>";
		    	}
		    	htmlStr +=" </table></div>";
		    	$("#fileds").append(htmlStr);
				$G.parse();
	    	}
		});
		$G.postByAjax({base_pid:base_pid},"<%=root%>/zxCustomSearch/getFieldDetail",ajaxConf);
	}
	
	/*
	 *编辑给复选框赋值
	 */
	function setCheckBoxValue(filed_ids){
		if(null!=filed_ids&&""!=filed_ids&&"undefined"!=filed_ids){
			var checkBoxList = filed_ids.split(",");
			for(var i=0;i<checkBoxList.length;i++){
				$G.get(checkBoxList[i]).setChecked(true);
			}
		}
	}
	
	/*
	 *字段全选/反选
	 */
	function selectAll(){
		var checkBoxList = $G.getsbyName("filed");
		if(null!=checkBoxList&&""!=checkBoxList&&"undefined"!=checkBoxList&&checkBoxList.length>0){
			if("1"==selFlag){
				for(var i=0;i<checkBoxList.length;i++){
					checkBoxList[i].setChecked(true);
				}
				$G.get("selectAll").setText("反选");
				selFlag="2";
			}else if("2"==selFlag){
				for(var i=0;i<checkBoxList.length;i++){
					checkBoxList[i].setChecked(false);
				}
 				$G.get("selectAll").setText("全选");
				selFlag="1";
			}
		}
	}
	
	/*
	 *复选框勾选值
	 */
	function checkBoxValue(){
		var filedIds="";
		var checkBoxList = $G.getsbyName("filed");
		if(null!=checkBoxList&&""!=checkBoxList&&"undefined"!=checkBoxList&&checkBoxList.length>0){
			for(var i=0;i<checkBoxList.length;i++){
				if(true == checkBoxList[i].getChecked()){
					if(""==filedIds){
						filedIds = checkBoxList[i].getDefaultValue();
					}else{
						filedIds += ","+checkBoxList[i].getDefaultValue();
					}
				}
			}
		}
		$G.getbyName("filedIds").setValue(filedIds);
	}
	
	/*
	 *保存数据
	 */
	function save(){
		//复选框勾选值
		checkBoxValue();
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
		$G.submitForm("form1", "<%=root%>/zxCustomSearch/addChildModel", ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	 // 下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
</script>