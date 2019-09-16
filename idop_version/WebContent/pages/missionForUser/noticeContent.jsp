<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@include file="/common/nuires.jsp" %>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发送消息内容</title>
</head>
<body>
<div class="nui-fit">
    		<input name="pageType" class="nui-hidden"/>
			<form id="form1" method="post">
	        	<input name="id" class="nui-hidden" />
	        	<input name="model_cfg_id" class="nui-hidden" />
	       		<div style="padding-left:1px;padding-bottom:5px;">
					<table style="table-layout:fixed;" align="center">
						<colgroup>
					       	<col width="15%"/>
					       	<col width="27%"/>
					       	<col width="25%"/>
					       	<col width="33%"/>
						</colgroup>
		              	<tr>
							<td colspan="4" align="left">    
								<span id="files"  style="color:#F00"></span>
								<input id="mission_remark" name="mission_remark" class="nui-textarea" style="width:100%;height:150px;" /></td>
			              	</td>
		              	</tr>
					</table>
				</div> 
			</form>
		</div>  
		<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" onclick="save">发送</a> 
			<a class="nui-button" onclick="onCancel">关闭</a>       
		</div>
</body>
</html>
<script>
$G.parse();

//取消
function onCancel(e) {
    CloseWindow("cancel");
}

// 关闭窗口
function CloseWindow(action) {            
    if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
    else window.close();            
}
</script>