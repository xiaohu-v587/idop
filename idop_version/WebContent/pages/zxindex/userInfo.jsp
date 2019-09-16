<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>个人信息维护</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<%@include file="/common/nuires.jsp" %>
   		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head>
	<body>
    	<div class="nui-fit">
			<form id="form1" method="post">
	        <input name="user_id" class="nui-hidden" />
	       	<div style="padding-top: 10px;">
				<table style="table-layout:fixed;" align="center">
					<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<h2>个人信息维护</h2>
		              	</td>
	              	</tr>
					<tr>
						<td align="right">联系方式：</td>
						<td align="left">
	                   		<input name="phone" id="phone" class="nui-textbox" style="width:200px;"/>
	                  	</td>
	              	</tr>
	              	<tr>
						<td align="right">昵称：</td>
	                  	<td align="left">
							<input name="nick_name" class="nui-textbox"  emptyText="请输入昵称" style="width:200px;"/>
						</td>
					</tr>
					<tr>
	                  	<td align="right">角色：</td>
	                  	<td align="left">
	                   		<input name="role_id" class="mini-combobox" allowInput="false"
	                   			emptyText="请选择角色" textfield="name" valuefield="id"
	                   		    url="<%=root%>/zxparam/getCombox?key=role" style="width:200px;"/>
	                  	</td>
	              	</tr>
					<tr>
						<td align="right">头像：</td>
						<td align="left">
							<input class="mini-htmlfile" name="upload_file" id="file1" style="width:200px;"/>
		              	</td>
	              	</tr>
	              	<tr>
						<td align="right"></td>
						<td align="left">
							<img src="" alt="" id="userImg" width="150px" height="120px"/>
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<a class="nui-button" iconCls="icon-save" onclick="" style="color: black;">确定</a>
		              	</td>
	              	</tr> 
				</table>
			</div>
		</form>
	</div>
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
 	var form =  new mini.Form("form1");
 	getUserInfo();
 	/*
 	 *获取用户信息
 	 */
 	function getUserInfo(){
		$.ajax({
			url: "<%=root%>/zxindex/getUserInfo",
            success: function (text) {
            	if(null != text.record){
            		var r = text.record;
            		$G.getbyName("user_id").setValue(r.user_id);
                  	$G.getbyName("nick_name").setValue(r.nick_name);
    	           	$G.getbyName("upload_file").setValue(r.upload_file);
    	           	$("#userImg").attr("src", nick_name);
    	           	//$G.getbyName("nick_name").setValue(r.nick_name);
    	           	//$G.getbyName("uid").setValue(r.tid);
            	}
           	}
    	});
 	}
 	/*
	 *保存数据
	 */
	function save(){
		var user_id = $G.getbyName("user_id").getValue();
		var nick_name = $G.getbyName("nick_name").getValue();
		var upload_file = $G.getbyName("upload_file").getValue();
		//var nick_name = $G.getbyName("nick_name").getValue();
		// 异步请求数据
    	var file = $("#file1 > input:file")[0];
		var url = "<%=root%>/zxrole/saveApplyRole";
    	$.ajaxFileUpload({
            url: url, //用于文件上传的服务器端请求地址
            fileElementId: file,//文件上传域的ID
            data: {user_id: user_id, upload_file: upload_file, nick_name: nick_name},//附加的额外参数
            dataType: 'json_pre',//返回值类型 一般设置为json
            success: function (data, status){
            	alert(nui.encode(data));
            	//服务器成功响应处理函数
           		$G.alert("角色申请成功!","提示",function(){
           			//$G.closemodaldialog("ok");
           			window.location = "<%=root%>/";
           		});
            },
            error: function (data, status, e){
            	$G.alert("角色申请失败!","提示",function(){
           			//$G.closemodaldialog("ok");
           			window.location = "<%=root%>/";
           		});
            }
        });
	}
 
 </script>