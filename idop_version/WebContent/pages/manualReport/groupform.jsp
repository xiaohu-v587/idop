<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>新增群组</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  			.mini-grid-newRow {
			    background: none;
			}
			tr td:first-of-type{
				width: 100px;
				height: 35px;
			}
  		</style>
	</head>
	<body>    
		<div class="mini-fit">     
			<form id="form1" method="post">
				<input name="pageType" class="nui-hidden"/> 
	        	<input name="groupId" class="nui-hidden" value="<%=request.getAttribute("groupId") %>"/>
	           	<table style="table-layout: fixed;padding-right:10%;" id="detailTable"  width="100%">
	               	<tr>
	                   	<td align="right">群组名称：</td>
	                   	<td align="left">    
	                       	<input name="groupName" style="width:100%;" class="nui-textbox" required="true"/>
	                   	</td>
	               	</tr>
					<tr>
	                   	<td align="right">群组说明：</td>
	                   	<td align="left">    
							<input name="groupNote" style="width:100%;" class="nui-textarea" required="true"/>
						</td>
					</tr>
					<tr style="display:none">
	                   	<td align="right">选择层级：</td>
	                   	<td align="left">    
	                   		<input name="level" id="level" class="mini-combobox" style="width:100%;" required="true" textfield="name" valuefield="key"  emptyText="请选择"
							 <%-- url="<%=root%>/param/getCombox" --%>data='[{"key":"2","name":"分行"},{"key":"3","name":"支行"},{"key":"4","name":"网点"}]' />
						</td>
					</tr>
					<tr>
	                   	<td align="right">群组使用范围：</td>
	                   	<td align="left">    
	                   		<input name="function_group" id="function_group" class="mini-combobox" style="width:100%;" required="true" textfield="remark" valuefield="val" value="0" emptyText="请选择"
	                   		data='[{"val":"0","remark":"不限制"},{"val":"1","remark":"报表模块"},{"val":"2","remark":"任务模块"}]' />
						</td>
					</tr>
					<tr>
	                   	<td align="right">群组模式：</td>
	                   	<td align="left">    
	                   		<input name="group_mode" id="group_mode" class="mini-combobox" style="width:100%;" required="true" textfield="remark" valuefield="val" value="0" emptyText="请选择"
	                   		data='[{"val":"0","remark":"机构"},{"val":"2","remark":"角色"},{"val":"3","remark":"机构和角色"},{"val":"4","remark":"机构人员"},{"val":"5","remark":"角色人员"},{"val":"6","remark":"人员"}]' onvaluechanged="typeValueChaged"/>
						</td>
					</tr>
					<tr>
	                   	<td align="right" id="type_title">机构：</td>
	                   	<td align="left" >    
							<input name="itemNames" style="width:80%" class="nui-textbox"  enabled="false"/>
							<input name="flag" class="nui-hidden"/>
							<a class="nui-button" style="width:15%;float:right;" onclick="select()">自定义</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="nui-toolbar" style="text-align: center;padding: 10px 0;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" iconCls="" onclick="save">保存</a>
			<span class="separator"></span>
			<a class="nui-button" iconCls="" onclick="onCancel">返回</a>       
		</div>        
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var pageType = $G.getbyName("pageType");
	var items = [];
	
	
	//标准方法接口定义
	function setData(data) {
		data = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
		$G.getbyName("pageType").setValue(data.pageType);
		if (data.pageType == "edit") {
			form.setData(data);
			nui.get("level").setValue(data.level);
			if(data.flag == "1"){
				items = data.items;
			}
			if(data.group_mode == "3"){
				var itemNames = '';
				var isFir = true;
				for(var i=0;i<items.length;i++){
					if(!isNullObj(items[i].orgnum)){
						if(isFir){
							isFir = false
						}else{
							itemNames += ',';	
						}
						itemNames += items[i].orgname;	
					}
				}
				
				itemNames += '$|$';
				
				isFir = true;
				for(var i=0;i<items.length;i++){
					if(!isNullObj(items[i].role_id)){
						if(isFir){
							isFir = false
						}else{
							itemNames += ',';	
						}
						itemNames += items[i].rolename;	
					}
				}
				
				$G.getbyName("itemNames").setValue(itemNames);
				
			}
		}
	}
	
	//根据类型改变标题内容
	function typeValueChaged(e){
		var val = e.vlaue;
		$("#type_title").html(e.selected.remark+"："); 
		items = [];
	}
	
	
	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/manualReport/saveGroup";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.result=='success'){
	    		$G.alert("保存成功","提示",function(){
	    			$G.closemodaldialog("ok");
	    		});
	    	}else{
	    		$G.alert("保存失败");
	    	}
		});
	    var data = form.getData();
	    data.items = items;
	    var formjson = $G.encode(data);
	    $G.postByAjax({"form":formjson}, urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }

	function select(){
		var val = $G.get("group_mode").getValue();
		var url = "<%=root%>/manualReport/";
		var type = $("#type_title");
		var height = 800;
		var width = 600;
		if('0' == val){
			url+="groupitem";
		}else if('2' == val){
			url+="grouprole";
		}else if('3' == val){
			url+="grouporgrole";
		}else if('4' == val){
			url+="grouporguser";
		}else if('5' == val){
			url+="grouproleuser";
		}else if('6' == val){
			url+="groupnewuser";
			height = 900;
			width = "80%";
		}else{
			
		}
		
		var bizParams = {pageType:"noadd",name:$G.getbyName("itemNames").getValue(),items:items};
		$G.showmodaldialog("选择群组对象", url, height,width, bizParams, function(data){
				items = data.items;
	        	$G.getbyName("itemNames").setValue(data.itemNames);
	        	$G.getbyName("flag").setValue(data.flag);
	    });
	}
	
	function getData() {
		var data = form.getData();
		    data.items = items;
		return data;
	}
</script>