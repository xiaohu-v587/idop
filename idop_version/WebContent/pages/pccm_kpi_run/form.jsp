<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>重跑设置</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
<body>    
	<div class="mini-fit">
		<form id="form1" method="post">
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="30%"/>
				       	<col width="70%"/>
				</colgroup>
               	<tr>
                   	<td align="right">重跑月份：</td>
                   	<td align="left">
                   		<input name="run_time" class="nui-combobox" textfield="run_time" valuefield="run_time" url="<%=root%>/zxparam/getMonth" required="true"  
                       		allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择"/>
                   	</td>
               	</tr>
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a> 
		<a class="nui-button" iconCls="icon-undo" onclick="reset">重置</a>       
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	
	function setData(data){
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
			$G.getbyName("run_time").setValue(text.year_month);
		});
		$G.postByAjax({nowDate:'now'},"<%=root%>/pccm_kpi_run/getDetail",ajaxConf);
	}
	/*
	 *重跑数据
	 */
	function save(){
    	var urlStr = "<%=root%>/pccm_kpi_run/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(true);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.flag==1){
	    		$G.closemodaldialog("ok");
	    	}
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}

	//重置
	function reset(){
		form.reset();
	}
</script>