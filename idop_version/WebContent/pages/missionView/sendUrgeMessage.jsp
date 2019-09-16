<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@include file="/common/nuires.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发送催办消息</title>
</head>
<body>
<div class="nui-fit">
    		<input name="pageType" class="nui-hidden"/>
			<form id="form1" method="post">
	        	<input name="id" class="nui-hidden" />
	        	<input name="model_cfg_id" class="nui-hidden" />
	        	<input name="user_no" class="nui-hidden" />
	       		<div style="padding-left:1px;padding-bottom:5px;">
					<table style="table-layout:fixed;" align="center">
						<colgroup>
					       	<col width="13%"/>
					       	<col width="87%"/>
						</colgroup>
						<tr>
		              		<td align="left">通知方式：</td>
		              		<td align="left">
		              			<input name="message_type" id="message_type" multiSelect="false"  class="nui-dictcombobox" valueField="val" textField="remark"  url="<%=root%>/param/getParam?key=yygl_mission_channel" value="0" emptyText="请选择消息通知方式" style="width:95%"/>
	                        </td>
	                    </tr>
		              	<tr>
							<td align="left">消息内容：</td>
							<td align="left">    
								<input id="message_content" name=message_content class="nui-textarea" style="width:95%;height:150px;" required="true" /></td>
			              	</td>
		              	</tr>
					</table>
				</div> 
			</form>
		</div>  
		<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button"  onclick="save">保存</a> 
			<a class="nui-button"  onclick="onCancel">关闭</a>       
		</div>
</body>
</html>
<script>
$G.parse();
var form = $G.getForm("form1");

/*
 *保存数据
 */
function save(){
	var o = form.getData();
	form.validate();
	if (form.isValid() == false) {
		$G.alert("请完善必输项。");
		return;
	}
	 var json = $G.encode([o]);
	 var messageType = $G.get("message_type").getValue();
	 var messageContent = $G.get("message_content").getValue();
	
	var urlStr = "<%=root%>/missionView/saveUrgMessage?id=${ids}&usernos=${usernos}";
	$.ajax({
        url: urlStr,	
        type: 'post',
        data:  {data:json},
        cache: false,
        success: function (text) {
        	if(text >= 1){
        		$G.alert("发送成功!");
        		CloseWindow("save");
        	}else{
        		$G.alert("发送失败!");
        		CloseWindow("error");
        	}
        },
        error: function (jqXHR, textStatus, errorThrown) {
            CloseWindow();
        }
    });
   
}


/**
 * 取消按钮按下时的事件
 */
function onCancel(e) {
	CloseWindow("cancel");
}
/**
 * 关闭窗体的事件
 */
function CloseWindow(action) {
	if (window.CloseOwnerWindow)
		return window.CloseOwnerWindow(action);
	else
		window.close();
}
</script>