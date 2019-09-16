<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>角色审核</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<%@include file="/common/nuires.jsp" %>
    <style type="text/css">
	    html, body{
	        font-size:12px;
	        padding:0;
	        margin:0;
	        border:0;
	        height:100%;
	        overflow:hidden;
	    }
    
	    #detailTable tr{
	    	line-height:30px;
	    }
    </style>
</head>
<body>
	<div class="mini-fit">
		<input class="mini-hidden" name="pageType" value="0" />
			<form id="form1" method="post">
				<input name="ids" class="mini-hidden" />
				<div style="width:70%;margin-left:5%;margin-top:10%;">
					<table style="table-layout:fixed;" width="100%">
						<colgroup>
							<col width="20%"/>
							<col width="25%"/>
							<col width="25%"/>
							<col width="30%"/>
						</colgroup>
						<tr>
							<td align="right" style="width:200px;">审核说明：</td>
							<td align="left">
								<textarea id="approvalState" name="approvalState" style="width:200px;height:100px"
									class="nui-textarea" emptyText="审核说明" maxlength="150">
								</textarea>
							</td>
						</tr>
						<tr>
							<td align="right" style="width:200px;">审核结果：</td>
							<td align="left">
								<input style="width:200px;" class="nui-dictcombobox" name="applyStatus" 
									valueField="id" emptyText="请选择" textField="text" required="true" 
									data='[{"id":"0","text":"不通过"},{"id":"1","text":"通过"}]'/>
							</td>
						</tr>
					</table>
				</div>
          </form>     
     </div>    
     <div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnOk" class="mini-button" iconCls="icon-ok" onclick="saveData()" style="margin-right: 20px;">提交</a>
		<a id="btnCancle" class="mini-button" iconCls="icon-close" onclick="onCancel()">关闭</a>
     </div>
</body>
</html>

<script type="text/javascript">

	$G.parse();
	var form =  new mini.Form("form1");

	//标准方法接口定义
	function setData(data) {
		var data = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
		$G.getbyName("ids").setValue(data.ids);
		$G.getbyName("approvalState").setValue("");
	}
	
	//保存
	function saveData() {
		var applyStatus = mini.getbyName("applyStatus").getValue();
		if("" == applyStatus){
			$G.alert("请选择审核状态!");
			return;
		}else{
			var urlStr = "<%=root%>/zxrole/saveApproval";
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(false);
			ajaxConf.setIsShowSuccMsg(true);
			ajaxConf.setErrorFunc(function(){
				$G.alert("保存失败！");
			});
			ajaxConf.setSuccessFunc(function (text){
		    	if(text.flag == true){
		    		$G.closemodaldialog("ok");
		    	}else{
		    		$G.alert("保存失败！");
		    	}
		    });
	 	    $G.submitForm("form1", urlStr, ajaxConf);
		}
	}

	//取消  
	function onCancel(e) {
		$G.closemodaldialog("cancel");
	}
</script>