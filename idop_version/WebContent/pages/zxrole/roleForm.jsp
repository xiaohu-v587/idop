<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
<body>    
	<div class="mini-fit">
		<input name="pageType" class="nui-hidden"/>      
		<form id="form1" method="post">
        	<input name="id" class="nui-hidden"/>
           	<table style="table-layout: fixed; margin-top: 30px" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="25%"/>
				       	<col width="25%"/>
				       	<col width="25%"/>
				       	<col width="25%"/>
				</colgroup>
               	<tr>
                   	<td align="right">名称：</td>
                   	<td align="left">    
                       	<input name="name" class="nui-textbox" readonly="readonly"/>
                   	</td>
                   <td align="right">客户经理专业资格等级：</td>
                    <td align="left">    
                        <input name="spec_level" class="mini-combobox" allowInput="false"
	                   			textfield="name" valuefield="val" readonly="readonly"
	                   		    url="<%=root%>/zxparam/getCombox?key=ZJ"/>
                    </td>
               	</tr>
				<tr>
                   	<td align="right">平移标准：</td>
                   	<td align="left">    
                       	<input name="trans_stand" class="nui-textbox"/>
                   	</td>
                   	<td align="right">晋升标准：</td>
                    <td align="left">    
                        <input name="promote_stand" class="nui-textbox"/>
                    </td>
               	</tr>
               	<tr>
                   	<td align="right">下一等级中位值：</td>
                   	<td align="left">    
                       	<input name="next_value" class="nui-textbox"/>
                   	</td>
               	</tr>
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a> 
		<a class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>       
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");

	function setData(data){
		var infos = $G.clone(data);
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
			form.setData(text.r);
		});
		$G.postByAjax({id:infos.id},"<%=root%>/zxrole/findRoleInfoById",ajaxConf);
	}
	
	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/zxrole/saveApplyRoleInfo";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(true);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.flag){
	    		$G.closemodaldialog("ok");
	    	}
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
</script>