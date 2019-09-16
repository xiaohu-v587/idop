<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>二次派送</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
			#detailTable tr td{
				height: 35px;
			}
  		</style>
	</head>
	<body>    
		<div class="mini-fit">
			<form id="form1" method="post">
	        	<input name="id" class="nui-hidden"/>
	           	<table style="table-layout: fixed;padding-right:10%" id="detailTable" width="100%">
	               	<tr>
	                   	<td align="right" width="100">报表名称：</td>
	                   	<td align="left">    
	                       	<input name="name" style="width:100%;" class="nui-textbox" enabled="false"/>
	                   	</td>
	               	</tr>
					<tr>
	                   	<td align="right">报表说明：</td>
	                   	<td align="left">    
							<input name="desc" style="width:100%;" class="nui-textarea" enabled="false"/>
						</td>
					</tr>
					<tr>
	                   	<td align="right">发送对象：</td>
	                   	<td align="left">    
							<input name="groupId" style="width:80%;" class="nui-combobox"
							valueField="id" textField="group_name" emptyText="我的群组" 
							url="<%=root%>/manualReport/myGroup" required="true"/>
							<input name="itemOrgIds" class="nui-hidden"/>
							<input name="itemUserIds" class="nui-hidden"/>
							<a class="nui-button" style="width:15%;float:right;" onclick="select()">自定义</a>
						</td>
					</tr>
					<tr id="finishrow"  style="display:none">
	                   	<td align="right">完成待办：</td>
	                   	<td align="left">    
							<input name="finish" style="width:100%;" class="nui-radiobuttonlist" textfield="name" valuefield="val" data="[{'val':'0','name':'否'},{'val':'3','name':'无需报送'},{'val':'4','name':'报送'}]" required="true"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="nui-toolbar"  style="text-align: center;padding: 10px 0;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" iconCls="" onclick="onOk">确定</a>
			<span class="separator"></span>
			<a class="nui-button" iconCls="" onclick="onCancel">取消</a>       
		</div>        
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	//var cb = $G.getbyName("groupId");
	
	//标准方法接口定义
	function setData(data) {
		data = $G.clone(data);
		form.setData(data);
		if(data.status > 2){
			$("#finishrow").hide();
		}
		<%-- var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (data){
			$G.getbyName("itemOrgIds").setValue(data.itemOrgIds);
	    	$G.getbyName("itemUserIds").setValue(data.itemUserIds);
	    	$G.getbyName("groupId").setText(data.itemNames);
	    });
		$G.postByAjax({id:data.id},"<%=root%>/manualReport/getForward",ajaxConf); --%>
	}
	
	//cb.on("valuechanged",function(e){
		//$G.getbyName("itemOrgIds").setValue();
    	//$G.getbyName("itemUserIds").setValue();
	//});
	
	//确定
	function onOk(e) {
		if($G.getbyName("finish").getValue()==3){
			$G.GcdsConfirm("确定移除已保存的报表内容并完成任务吗？", "提示", function(action) {
				if (action == 'ok') {
					forward();
				}
			});
		}else if($G.getbyName("finish").getValue()==4){
			$G.GcdsConfirm("确定提交已保存的报表内容吗？", "提示", function(action) {
				if (action == 'ok') {
					forward();
				}
			});
		}else{
			forward();
		}
    }
	
	//转发
	function forward(){
		var group = $G.getbyName("groupId").getValue();
		var orgids = $G.getbyName("itemOrgIds").getValue();
		var usrids = $G.getbyName("itemUserIds").getValue();
		if (group == null || group == '' && orgids== '' && usrids== '') {
			$G.alert("您还没有选择发送对象！");
			return;
		}
		var urlStr = "<%=root%>/manualReport/forward";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsAsync(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.result=='success'){
	    		$G.alert("转发成功","提示",function(){
		    		$G.closemodaldialog("ok");
	    		});
	    	}else{
	    		$G.alert("转发失败");
	    	}
		});
		var formjson = $G.encode(form.getData());
	    $G.postByAjax({"form":formjson}, urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }

	//选择发送对象
	function select(){
		var url = "<%=root%>/manualReport/groupitem";
		var bizParams = {org:$G.getbyName("itemOrgIds").getValue(),usr:$G.getbyName("itemUserIds").getValue(),name:$G.getbyName("groupId").getText()};
        $G.showmodaldialog("选择发送对象", url, 800, 600, bizParams, function(data){
       		if(data.act=="addgroup"){
        		$G.getbyName("groupId").load("<%=root%>/manualReport/myGroup");
        	}else{
        		$G.getbyName("groupId").setValue("null");
	        	$G.getbyName("groupId").setText(data.name);
	        	$G.getbyName("groupId").enable();
	        	$G.getbyName("itemOrgIds").setValue(data.org);
	        	$G.getbyName("itemUserIds").setValue(data.usr);
        	}
	    });
	}
</script>