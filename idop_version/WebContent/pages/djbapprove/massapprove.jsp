<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
   * 登记簿数据提交界面
   *
   * Created on 
   * @zhaomiao  
   * @reviewer 
-->
<head>
    <title>提交界面</title>
</head>
 <style type ="text/css">
 	html, body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
 </style>
<body>
	<!-- 标识页面是查看(query)、修改(edit)、新增(add) -->
	<div class="mini-fit">
		<input name="pageType" class="mini-hidden" />
		<form id="dataform1" method="post">
			<table>
	        	<tr>
					<td>上级审批人/下级执行人：</td>
	         		<td style="white-space:nowrap;">
	                     <input id="nextApprover" name="nextApprover" class="mini-treeselect" 
	                     	valueField="id" textField="name" parentField="upid" dataField="nextAppData"
	                     	expandOnLoad="false" popupWidth="210" showClose="true" showTreeIcon="true" required="true"
	                     	onnodeclick="nextApponNodeSelect" oncloseclick="extAppononCloseClick" width="200px";/>
	                </td>
				</tr>
			</table>
		</form>       
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a class="mini-button" onclick="saveJson()" style="margin-right: 20px;" iconCls="icon-ok">保存</a>
		<a class="mini-button" onclick="onCancel()"  iconCls="icon-close"> 取消 </a> 
	</div>
	<script type="text/javascript">
		mini.parse();
		var form =  new mini.Form("dataform1");
        mini.get("nextApprover").load("<%=root %>/djbapprove/getNextOperatorList?djbid=${djbid}");
		
	    //点击节点
	    function nextApponNodeSelect(e) {
	      	if(e.node.FLAG == "org"){ 
	      		mini.get("nextApprover").setValue("");
	  	   }
	    }

	    //选择下一级的审批人员下拉框清空
	    function extAppononCloseClick(e) {
	        var obj = e.sender;
	        obj.setValue("");
	    }
		
		//json方式保存数据
		function saveJson() {
			//保存
			var urlStr = "<%=root %>/djbapprove/allApprove?flag=1";
		    form.validate();
		  	//表示为编辑状态
		  	var nextApprover = mini.get("nextApprover").getValue();
		  	var djbid = "${djbid}";
		  	var ids = "${ids}";
		  	$.ajax({
	             url: urlStr,
			     type: 'post',
	             data: {nextApprover:nextApprover,djbid:djbid,ids:ids},
	             cache: false,
	             success: function (text) {
		             CloseWindow("save");
	             }
	             ,error: function (jqXHR, textStatus, errorThrown) {
	                 alert(jqXHR.responseText);
	                 CloseWindow();
	             }
	         });
		}
		
 		//获取父页面传递来的json数据
		function setData(data) {
		}

		//取消
	   function onCancel(e) {
	        CloseWindow("cancel");
	    }
	
	   function CloseWindow(action) {        
	       if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
	       else window.close();            
	   }
		
		
	</script>
</body>
</html>
