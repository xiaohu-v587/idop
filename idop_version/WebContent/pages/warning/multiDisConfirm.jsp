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
        	<input name="ids" class="nui-hidden"/>
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				
					<tr>
					<td align="right" class="labelname">否定说明：</td>
					<td align="left" colspan="3">
						<input name="indentify_remark" maxlength="500" emptyText="最多输入500汉字" class="nui-textarea" style="width:96%;height:80px;"></input>
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
	var form = $G.getForm("form1");

	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		$G.getbyName("ids").setValue(infos.warning_codes);
	}
	
	/*
	 *保存数据
	 */
	function save(){
		loading();
    	var urlStr = "<%=root%>/warning/multiSave";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	unloading();
	    	if(text.result=='fail'){
	    		$G.alert("批量否定失败！");
	    	}else{
	    		$G.alert("批量否定成功！","提示",function(action){
	    			$G.closemodaldialog("ok");	
	    		});
	    	}
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	 function loading() {
	        mini.mask({
	            el: document.body,
	            cls: 'mini-mask-loading',
	            html: '操作中,请稍候...'
	        });
	    }
	    
	    function unloading() {
	        setTimeout(function () {
	            mini.unmask(document.body);
	        }, 0);
	    }
</script>