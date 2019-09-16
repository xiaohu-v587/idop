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
					<td align="right" class="labelname">信号状态：</td>
					<td align="left"><input name="signal_status" class="nui-combobox"  data="[{id:1,text:'生效'}]"  value= "1"  enabled="false"
					style="width:90%;" required="true"></td>
					<td align="right" class="labelname">核查层级：</td>
					<td align="left"><input name="check_level" class="nui-combobox" url="" 
					valuefield="val" textfield="remark" style="width:90%;"/></td>
				</tr>
				<tr id="check">
									<td align="right" class="labelname">是否问题：</td>
									<td align="left">
										<input name="is_question" class="nui-combobox"  url="<%=root%>/param/getDict?key=dop_is_question" 
										        textfield="remark" valuefield="val" emptyText="" style="width:90%;"/>
									</td>
				</tr>
					<tr>
					<td align="right" class="labelname">认定说明：</td>
					<td align="left" colspan="3">
						<input name="indentify_remark" class="nui-textarea" maxlength="500" emptyText="最多输入500汉字" style="width:96%;height:80px;"></input>
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
		$G.getbyName("check_level").setUrl("<%=root%>/warning/getCheckLevel?ids="+infos.warning_codes);
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
	    		$G.alert("批量认定失败！");
	    	}else{
	    		$G.alert("批量认定成功！","提示",function(action){
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