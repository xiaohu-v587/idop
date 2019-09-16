<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>角色设置</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<%@include file="/common/nuires.jsp" %>
   		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head>
	<body>
    	<div class="nui-fit">
			<form id="form1" method="post">
	        <input name="id" class="nui-hidden" />
	       	<div style="padding-top: 20px;">
				<table style="padding:0 50px 40px 50px;table-layout:fixed;" align="center">
					<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<h2>角色切换</h2>
							<h4 style="color:red">提示:角色切换后，请关闭窗口后重新登录！</h4>
		              	</td>
	              	</tr>
	              	<tr>
						<td align="right">当前默认角色: </td>
						<td align="left" id="currentRole"></td>
	              	</tr>
	              	<tr style="height: 260px;">
						<td align="center" colspan="2">
							<div class="nui-fit">
								<div id="datagrid" class="nui-datagrid" url="<%=root%>/zxuser/userRoleList" 
						    		style="width: 240px;height: 80%;" showpager="false">
							        <div property="columns">
							        	<div field="id" visible="false">id</div>
							        	<div field="name" width="100" headerAlign="center" align="center">角色名称</div>
							        </div>
								</div>
							</div>
		              	</td>
					</tr>
					
	              	<tr>
						<td align="center" colspan="2">
							<a class="nui-button" iconCls="" onclick="save" style="color: #ffffff;">确定</a>
							&nbsp;&nbsp;&nbsp;
							<a class="nui-button" iconCls="" onclick="cancel" style="color: #ffffff;">取消</a>
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
 	getUserInfo();
 	var form = $G.getForm("form1");
 	var grid =$G.get("datagrid");

 	/*
 	 *获取用户信息
 	 */
 	function getUserInfo(){
		$.ajax({
			url: "<%=root%>/zxuser/getUserRoleInfo",
            success: function (text) {
            	if(null != text.record){
            		var r = text.record;
            		$G.getbyName("id").setValue(r.user_id);
            		$("#currentRole").html(r.role_name);
            		grid.load(form.getData());
            	}
           	}
    	});
 	}
 	/*
	 *保存数据
	 */
	function save(){
		var row = grid.getSelected();
		if(row){
			var id = row.id;
			var url = "<%=root%>/zxuser/saveDefaultRole";
			$.ajax({
				url: url,
				data: {id: id},
		        success: function (text) {
		        	if(text.flag){
		        	debugger
		        		//alert("切换成功!");
		        		$G.closemodaldialog("ok");
		        		var opened=window.open("","_top","");
		        		opened.opener=null;
		        		opened.parent.close();
		        	}else{
		        		alert("操作失败!");
		        	}
		        }
			});
		}else{
			$G.alert("请选择一条记录!");
		}
	}

 	function cancel(){
 		$G.closemodaldialog("cancel");
 	}
 </script>