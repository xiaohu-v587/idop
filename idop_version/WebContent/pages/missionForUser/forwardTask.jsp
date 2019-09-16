<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@include file="/common/nuires.jsp" %>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务转发</title>
</head>
<body>
<div class="nui-fit">
    		<input name="pageType" class="nui-hidden"/>
			<form id="form1" method="post">
	        	<input name="id" class="nui-hidden" />
	        	<input name="mission_name" class="nui-hidden" />
	       		<div style="padding-left:1px;padding-bottom:5px;">
					<table style="table-layout:fixed;" align="center">
						<colgroup>
					       	<col width="15%"/>
					       	<col width="27%"/>
					       	<col width="25%"/>
					       	<col width="33%"/>
						</colgroup>
						<tr>
							<td align="right">完成天数（工作日）：</td>
		                  	<td align="left" style="white-space: nowrap;" >
		                   		<input name="mission_deadline" id="mission_deadline" class="nui-textbox" required="true" maxlength="10" vtype="range:0,365;int" style="width:35%" emptyText="请输入天数" />
		                   		&nbsp;
		                   		<input name="mission_deadline_hh" id="mission_deadline_hh" class="nui-textbox"  maxlength="10" vtype="range:0,24;int" style="width:35%" emptyText="小时"/>
		                   		&nbsp;:&nbsp;	
		                   		<input name="mission_deadline_mm" id="mission_deadline_mm" class="nui-textbox"  maxlength="10" vtype="range:0,60;int" style="width:30%" emptyText="分钟" />
		                  	</td>
		                  	<td align="right">消息通知方式：</td>
		                  	<td align="left">
								<input name="message_type" id="message_type" multiSelect="false"  class="nui-dictcombobox" valueField="val" textField="remark"  url="<%=root%>/param/getParam?key=yygl_mission_channel" value="0" emptyText="请选择消息通知方式" style="width:90%"/>
							</td>
		              	</tr>
		              	 <tr>
		              		<td align="right">消息通知模板：</td>
		                  	<td align="left">
								<input id="message_id" name="message_id"  style="width: 110%;"  class="nui-combobox" url="<%=root%>/param/getTemp" 
								valueField="id" textField="template_title"  emptyText="请选择..."  multiSelect="false" />
							</td>
		              	</tr> 
		              	<tr>
							<td align="right">任务转发说明：</td>
							<td colspan="3" align="left">    
								<span id="files"  style="color:#F00"></span>
								<input id="mission_remark" name="mission_remark" class="nui-textarea" style="width:96%;height:150px;" /></td>
			              	</td>
		              	</tr>
		              	<tr>
		              		<td></td>
		              		<td>
		              			<a id="btnChose" class="nui-button"  onclick="select()" >选择接收人</a>
	                        </td>
	                        <td></td>
		              	</tr> 
		              	<tr id="usersArea">
							<td align="right">任务接收人：</td>
		                    <td align="left" colspan="3" style="height:200px;">   
		                    	<div style="border: 0;padding: 0;margin: 0;height: 100%;width: 96%;overflow: scroll;">
									<input id="itemNames" name="itemNames" class="mini-textboxlist" style="width:100%;height:95%" allowInput="false"
									 textName="tblName" value="" text="" valueField="id" textField="text"/>
								</div>
		                    </td>
						</tr>
					</table>
				</div> 
			</form>
		</div>  
		<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" onclick="save">保存</a> 
			<a class="nui-button" onclick="onCancel">关闭</a>       
		</div>
</body>
</html>
<script>
	$G.parse();
	var form = $G.getForm("form1");
	var items = [];

	function select(){
		var url = "<%=root%>/manualReport/groupnewuser";
		var type = $("#type_title");
		var bizParams = {pageType:"noadd",name:$G.getbyName("itemNames").getText(),items:items};
		$G.showmodaldialog("选择群组对象", url, "80%","80%", bizParams, function(data){
				items = data.items;
				var ids = '';
				for(var i=0,len = items.length;i<len;i++){
						if(i > 0){
							ids += ',';	
						}
						ids += items[i].orgnum+"_"+items[i].role_id+"_"+items[i].user_no;
				}
	        	$G.getbyName("itemNames").setValue(ids);
	        	$G.getbyName("itemNames").setText(data.itemNames);
	    });
	}
	
	
	/*
 	 *转发任务
	 *保存数据
	 */
	function save(){
 		var data = form.getData();
		form.validate();
		if (form.isValid() == false) {
			$G.alert("请完善必输项。");
			return;
		}
		
		if(items.length == 0){
			$G.alert("请选择转发接收人人。");
			return;
		}
		
		data.items = $G.encode(items);
		data.id = "${id}";
		data.missionIssueId = "${missionIssueId}";
		data.missionName = "${missionName}";
    	var urlStr = "<%=root%>/missionForUser/save";
    	$.ajax({
            url: urlStr,	
	        type: 'post',
            data: data,
            cache: false,
            success: function (text) {
            	if(parseInt(text+"") >= 1){
            		$G.alert("转发成功!","提示",function(action){
            			CloseWindow("save");	
            		});
            	}else{
            		CloseWindow("error");
            	}
            },
            error: function (jqXHR, textStatus, errorThrown) {
                CloseWindow();
            }
        });
	}
	
	
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