<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>角色设置</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<%@include file="/common/nuires.jsp" %>
	</head>
	<body>
    	<div class="nui-fit">
			<form id="form1" method="post">
	        <input name="id" class="nui-hidden" />
	       	<div style="padding-top: 180px;">
				<table >
					<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<h2>员工角色申请dsfsdfd</h2>
		              	</td>
	              	</tr>
					<tr>
						<td align="right">姓名：</td>
						<td align="left">
	                   		<input name="name" id="name" class="nui-textbox"/>                   
	                  	</td>
	              	</tr>
	              	<tr>
	                  	<td align="right">角色：</td>
	                  	<td align="left">
	                   		<input name="role_id" class="mini-combobox" allowInput="false"
	                   			emptyText="请选择角色" textfield="name" valuefield="id"
	                   		    url="<%=root%>/zxglRole/getApplyRoleList"/>
	                  	</td>
	              	</tr>
	              	<tr>
						<td align="right">昵称：</td>
	                  	<td align="left">
							<input name="nick_name" class="nui-textbox"  emptyText="请输入昵称"/>                  
						</td>
					</tr>
					<tr>
						<td align="right">头dfdsfsdfsd：</td>
						<td align="left">
							<input name="remark" class="nui-textbox" maxlength="100"/>
		              	</td>
	              	</tr> 
	              	<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr> 
	              	<tr>
						<td align="center" colspan="2">
							<a class="nui-button" iconCls="" onclick="save">确定</a>
		              	</td>
	              	</tr> 
				</table>
			</div>
			<div style="margin-top: 50px;text-align: center;">
				<a>1、首次登录系统需要进行角色的申请</a><br/>
				<a>2、登录周期超过30天需要进行角色申请</a><br/>
				<a>3、角色申请审批通过之后才能进入系统</a>
			</div>
		</form>
	</div>  
	<!-- <div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a>
	</div> -->
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
 	getUserInfo();
 	/*
 	 *获取用户信息
 	 */
 	function getUserInfo(){
		$.ajax({
			url: "<%=root%>/getUserInfo",
            success: function (text) {
            	if(null != text.record){
            		var r = text.record;
            		$G.getbyName("id").setValue(r.id);
                  	$G.getbyName("name").setValue(r.user_name);
    	           	$G.getbyName("role_id").setValue(r.role_id);
    	           	$G.getbyName("nick_name").setValue(r.nick_name);
    	           	//$G.getbyName("uid").setValue(r.tid);
            	}
           	}
    	});
 	}
 	/*
	 *保存数据
	 */
	function save(){

	}
 
 </script>