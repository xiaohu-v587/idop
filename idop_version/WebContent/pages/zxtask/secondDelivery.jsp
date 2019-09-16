<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>二次派送</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<%@include file="/common/nuires.jsp" %>
	</head>
	<body>
    	<div class="nui-fit">
			<form id="form1" method="post">
	        <input name="user_id" class="nui-hidden" />
	       	<div style="padding-top: 180px;">
				<table style="table-layout:fixed;" align="center">
					<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr>
					<tr>
						<td align="right">报表名称：</td>
						<td align="left">
	                   		<input name="name" id="name" class="nui-textbox"/>
	                  	</td>
	              	</tr>
	              	<tr>
	                  	<td align="right">报送说明：</td>
	                  	<td align="left">
	                   		<input name="name" id="name" class="nui-textbox"/>
	                  	</td>
	              	</tr>
	              	<tr>
						<td align="right">接收人：</td>
	                  	<td align="left">
							<input name="nick_name" class="nui-textbox"  emptyText="请输入昵称"/>
						</td>
					</tr>
					<tr>
						<td align="right">是否删除代办：</td>
						<td align="left">
							<select name="sfty" class="nui-radiobuttonlist" onvaluechanged="valueChange(this)" vtype="rangeChar:1,50">
                        		<option value="0" selected="selected">是</option>
                        		<option value="1" >否</option>
                        	</select>
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<a class="nui-button" iconCls="icon-save" onclick="save">确定</a>
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
 	/*
 	 *获取用户信息
 	 */
 	function getUserInfo(){
		$.ajax({
			url: "<%=root%>/zxuser/getUserInfo",
            success: function (text) {
            	if(null != text.record){
            		var r = text.record;
            		$G.getbyName("user_id").setValue(r.user_id);
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
		var user_id = $G.getbyName("user_id").getValue();
		var role_id = $G.getbyName("role_id").getValue();
		var nick_name = $G.getbyName("nick_name").getValue();
		var head_url = $G.getbyName("head_url").getValue();
		var json = {user_id: user_id, role_id: role_id, nick_name: nick_name, head_url: head_url};
		// 判断是否达到最大用户在线数
		// 异步请求数据
    	var ajaxConf = new GcdsAjaxConf();
    	ajaxConf.setIsShowSuccMsg(false);
    	ajaxConf.setIsShowProcessBar(false);
    	ajaxConf.setSuccessFunc(
    		function(data) {
    			window.location = "<%=root%>/";
    		}
		);
		$G.postByAjax(json, "<%=root%>/zxrole/saveApplyRole", ajaxConf);
	}
 
 </script>