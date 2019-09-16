<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<style type="text/css">
		.labelname{
			width:100px;
		}
	</style>
<body>    
	<div class="mini-fit">
		<input name="pageType" class="nui-hidden"/>      
		<form id="form1" method="post">
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
               	<tr>
                   	<td align="right" class="labelname">预警时间：</td>
                   	<td align="left">    
                       	<input name="create_time" class="nui-textbox" allowInput="false" style="width:90%;"/>
                   	</td>
                   		<td align="right" class="labelname">业务时间：</td>
					<td align="left">    
					       	<input name="data_date" class="nui-textbox" allowInput="false" style="width:90%;"/>
                   	</td> 
               	</tr>
				<tr>
						<td align="right" class="labelname">预警编号：</td>
                    <td align="left">    
                        <input name="flownum" class="nui-textbox" allowInput="false" style="width:90%;"/>
                    </td>
                   	<td align="right" class="labelname">预警名称：</td>
                   	<td align="left">    
						<input name="warning_name" class="nui-textbox" allowInput="false" style="width:90%;"/>
					</td>
				</tr>
				<tr>
				  <td align="right" class="labelname">预警类型：</td>
					<td align="left">    
                       	<input name="warning_type_code" class="nui-treeselect" url="<%=root%>/warning/getWarningTypeList" 
                       	 dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
                       	 valueFromSelect="false" multiSelect="false"  expandOnLoad="0" 
									showClose="true" oncloseclick="onCloseClick" enabled="false"
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" style="width:90%;"/>
                   	</td> 
                    <td align="right" class="labelname">业务模块：</td>
					<td align="left">    
                       	<input name="busi_module" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_ywtype" enabled="false"
						                 onvaluechanged="onywTypeChanged" textfield="remark" valuefield="val" emptyText="请选择..." style="width:90%;"/>
                   	</td> 
                   
				</tr>
				<tr>
					<td align="right" class="labelname">预警明细：</td>
					<td align="left" colspan="3" style="position: relative;width:820px; height:240px;">
						<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:96%;height:240px;" sortMode="client" allowUnselect="false"
						oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root %>/warning/getDetailData"
						autoEscape="true" onshowrowdetail="" showEmptyText="true" emptyText="暂无明细" 
						pageSize="5">
							<div id="detailColumns" property="columns"> 
							</div>
							
					   	</div> 
					   	<a class="nui-button" iconCls="" onclick="download()" style="width:90px;position: absolute; left: 300px; bottom: 6px;">导出预警明细</a>
					</td>
				</tr>
				<tr>
					<td align="right" class="labelname">信号状态：</td>
					<td align="left"><input name="signal_status" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_signal_stat" 
					valueField="val" textField="remark" style="width:90%;" required="true" onvaluechanged="checkcontroller"/></td>
					<td align="right" class="labelname">核查层级：</td>
					<td align="left"><input name="checker_level" class="nui-combobox" url="<%=root%>/warning/getCheckLevel?ids=<%=request.getAttribute("flownum") %>" 
					valuefield="val" textfield="remark" style="width:90%;"/></td>
				</tr>
				<tr id="check">
									<td align="right" class="labelname">是否问题：</td>
									<td align="left">
										<input name="is_question" class="nui-combobox" enabled="false" url="<%=root%>/param/getDict?key=dop_is_question" 
										        textfield="remark" valuefield="val" emptyText="" style="width:90%;"/>
									</td>
				</tr>
				<tr>
					<td align="right" class="labelname">认定说明：</td>
					<td align="left" colspan="3">
						<input name="indentify_remark" class="nui-textarea" style="width:96%;height:80px;"></input>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" onclick="save">确定</a> 
		<a class="nui-button" onclick="onCancel">取消</a>  
		         
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid1");
	var form = $G.getForm("form1");
	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if(pageType=="singleConfirm"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.record);
			});
			$G.postByAjax({flownum:infos.flownum},"<%=root%>/warning/getDetail",ajaxConf);
		}
		if(pageType=="indentifyDetail"){
			$G.getbyName("signal_status").setEnabled(false);
			$G.getbyName("checker_level").setEnabled(false);
			$G.getbyName("indentify_remark").setEnabled(false);
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.record);
			});
			$G.postByAjax({flownum:infos.flownum},"<%=root%>/warning/getDetail",ajaxConf);
		}
		getDetailHeader(infos.flownum);
	}
	
	/*
	 *保存数据
	 */
	function save(){
		form.validate();
		if(form.isValid() == false){
			$G.alert("请完善信息！");
		}else{
	    	var urlStr = "<%=root%>/warning/save";
		    var ajaxConf = new GcdsAjaxConf();
		    ajaxConf.setIsShowProcessBar(false);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
		    	if(text.result=='fail'){
		    		$G.alert("认定失败！");
		    	}else{
		    		$G.alert("操作成功！","提示",function(action){
		    			$G.closemodaldialog("ok");	
		    		});
		    	}
			});
		    $G.submitForm("form1", urlStr, ajaxConf);
		}
	}
	
	//获取明细header
	function getDetailHeader(flownum){
		var url = "<%=root%>/warning/getDetailHeaders";
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsAsync(false);
		ajaxConf.setSuccessFunc(function(text){
			grid.set({columns:text.headers});
			grid.load({flownum:flownum});
		});
		ajaxConf.postByAjax({flownum:flownum}, url);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	function checkcontroller(){
		var signal_status = $G.getbyName("signal_status");
		if(signal_status.value=="1"){
			$G.getbyName("is_question").setEnabled(true);
		}else{
			$G.getbyName("is_question").setEnabled(false);
		}
		
	}
	   //下载预警详情
	   function download(){
		  var flownum= $G.getbyName("flownum").getValue();
		   window.location="<%=root%>/warning/download?flownum="+flownum;
	   }
</script>