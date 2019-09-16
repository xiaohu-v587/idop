<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@include file="/common/nuires.jsp" %>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
							<td align="right">退回说明：</td>
							<td colspan="3" align="left">    
								<span id="files"  style="color:#F00"></span>
								<input id="mission_remark" name="mission_remark" class="nui-textarea" style="width:96%;height:150px;" /></td>
			              	</td>
		              	</tr>
		              	<tr>
		              		<td align="right">通知方式：</td>
		              		<td>
		              			<input name="message_type" id="message_type" multiSelect="true"  class="nui-dictcombobox" valueField="val" textField="remark"  url="<%=root%>/param/getParam?key=yygl_mission_channel" value="0" emptyText="请选择消息通知方式" style="width:90%"/>
	                        </td>
	                        <td align="right">消息模板：</td>
	                        <td>
		              			<input id="message_id" name="message_id"  style="width: 90%;"  class="nui-combobox" url="<%=root%>/param/getTemp" 
								valueField="id" textField="template_title"  emptyText="请选择..."  multiSelect="false" />
	                        </td>
		              	</tr>
		              	<tr>
		              		<td align="right">退回说明附件：</td>
							<td colspan="3" align="left">    
								<jsp:include page="../common/fileupload.jsp">					
									<jsp:param name="pid" value="<%=request.getAttribute("id") %>"/>
									<jsp:param name="type" value="<%=request.getAttribute("pageType") %>"/>
									<jsp:param name="pidtype" value="4"/>
								</jsp:include>
			              	</td>
		              	</tr> 
		              	<tr>
							<td align="right">文件列表：</td>
							<td colspan="3">
								<div id="datagrid1" dataField="datas" class="nui-datagrid" style="width: 96%; height: 150px;" sortMode="client"
									allowUnselect="false" oncellclick="" onrowdblclick=""
									onselectionchanged="" onload="" url="" autoEscape="false"
									onshowrowdetail="" showEmptyText="true" emptyText="没有对应的附件信息" ondrawcell="ondrawcell">
									<div property="columns">
										<div field="ID" visible="false"></div>
										<div field="PID" visible="false">任务父id</div>
										<div field="FILENAME" width="70%" headerAlign="center" align="center">附件名称</div>
										<div field="CREATEDATE" headerAlign="center" align="center">上传日期</div>
										<div field="YXPTID" visible="false"></div>
										<div field="PROCFORMISSION" headerAlign="center" align="center">操作
										</div>
									</div>
								</div>
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
	 *退回反馈
	 */
	function save(){
		var o = form.getData();
		form.validate();
		if (form.isValid() == false) {
			$G.alert("请完善必输项。");
			return;
		}
		 var json = $G.encode([o]);
		 var mission_remark = $G.get("mission_remark").getValue();
		 var message_type = $G.get("message_type").getValue();
		 var message_id = $G.get("message_id").getValue();
		 
		var urlStr = "/missionView/saveGoBack?id=${id}&missionName=${missionName} ";
		$.ajax({
	        url: urlStr,	
	        type: 'post',
	        data:  {data:json},
	        cache: false,
	        success: function (text) {
	        	if(text > "1"){
	        		$G.alert("退回成功!");
	        		CloseWindow("save");
	        	}else{
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
