<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
	* 标签
	* @author 陈佳争
	* @date 2019-05-27
-->
<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>标签</title>
	<style type="text/css">
		html,body {
			margin: 0;
			padding: 0;
			border: 0;
			width: 100%;
			height: 100%;
			overflow: hidden;
		}
	</style>
</head>
<body>
    <div class="mini-fit">
    <input name="pageType" class="mini-hidden" />
        <form id="dataform1" method="post">
        <input name="id" class="nui-hidden" />
        	<div style="padding-bottom: 20px; padding-top: 20px;">
          		<table style="table-layout: fixed;" align="center" >
          			<tr>
              			<td class="form_label">标签编号:</td>
              			<td >
                			<input class="mini-textbox" name="indicator_code" enabled="true"  height="25px" id="indicator_code" width="210px" ></input>
            			</td>
              			<td class="form_label">标签名称:</td>
              			<td >
                			<input class="mini-textbox" width="210px" height="25px" id="indicator_name" name="indicator_name"  ></input>
            			</td>
        			</tr>
            		<tr>
              			<td class="form_label">业务模块:</td>
              			<td >
                			<input class="mini-combobox" width="210px"    url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="module" name="module"   valueField="val" textField="remark" required="true" 
           	 				onvaluechanged=""/>
            			</td>
              			<td class="form_label">标签维度:</td>
              			<td >
                			<input class="mini-combobox" width="210px"    url="<%=root%>/param/getDict?key=bqwd" 
           	 				id="dimension" name="dimension"   valueField="val" textField="remark" required="true"/>
            			</td>
        			</tr>
				</table>
			</div>
		</form>
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnOk" class="mini-button"  onclick="saveJson()" style="margin-right: 20px;">确定</a> 
		<a id="btnCancle" class="mini-button"  onclick="onCancel()">取消</a>
	</div>
	<script type="text/javascript">
	   $G.parse();
	  var form = new mini.Form("dataform1");
	  
	  function saveJson() {
		  var urlStr = "<%=root%>/imgSetCfg/save";
		    var ajaxConf = new GcdsAjaxConf();
		    ajaxConf.setErrorFunc(function(){
		    	$G.alert("操作失败！");
		    })
		    ajaxConf.setSuccessFunc(function (){
				$G.closemodaldialog("ok");
			});
		    $G.submitForm("dataform1", urlStr, ajaxConf);
	  }
	
	  function setData(data){
	 		var infos = $G.clone(data);
			var pageType=infos.action;
			$G.getbyName("pageType").setValue(pageType);
			if (pageType == "edit") {
				$G.getbyName('indicator_code').disable();
				var ajaxConf=new GcdsAjaxConf();
				ajaxConf.setIsShowProcessBar(true);
				ajaxConf.setIsShowSuccMsg(false);
				ajaxConf.setSuccessFunc(function(text){
					form.setData(text.datas);
				});
				$G.postByAjax({key:infos.id},"<%=root%>/imgSetCfg/getDetailById",ajaxConf);
			}
	 	}
	  
	//关闭窗口
  	function CloseWindow(action) {
		if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
		else window.close();
  	}
	
	  //取消
	  function onCancel() {
			CloseWindow("cancel");
	  }
	  
	 
	</script>
</body>
</html>