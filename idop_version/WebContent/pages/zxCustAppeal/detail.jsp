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
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="15%"/>
				       	<col width="40%"/>
				       	<col width="15%"/>
				       	<col width="40%"/>
				</colgroup>
<!-- 				<tr>                                                                                        -->
<!-- 	                <td align="right">客户号：</td>                                       -->
<!-- 					<td align="left">                                                               -->
<!-- 						<input id="cust_no" name="cust_no" class="nui-textbox" allowInput="false" style="width:165px;"/>                 -->
<!-- 					</td>                                                                           -->
<!-- 	                <td align="right">客户名称：</td>                                     -->
<!-- 					<td align="left">                                                               -->
<!-- 						<input id="name" name="name" class="nui-textbox" allowInput="false" style="width:165px;"/>         -->
<!-- 					</td>                                                                            -->
<!-- 	             </tr> -->
               	<tr>
                   	<td align="right">申诉原因：</td>
					<td align="left" colspan="3">
						<input id="appeal_reas" name="appeal_reas" class="nui-textarea" required="true" width="400px"  height="65px" />
					</td>
               	</tr>
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
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
			form.setData(text.record);
		});
		$G.postByAjax({id:infos.id},"<%=root%>/zxCustAppeal/getReason",ajaxConf);
	}
	
	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/custPuni/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
    		$G.alert("操作成功");
    		$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	
</script>