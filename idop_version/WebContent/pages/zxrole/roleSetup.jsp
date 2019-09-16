<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>信息核对</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<%@include file="/common/nuires.jsp" %>
   		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head>
	<body>
    	<div class="nui-fit" >
			<form id="form1" >
	        <input name="user_id" class="nui-hidden" />
	       	<div style="padding-top: 20px;">
				<table style="padding:0 70px 40px 50px;" align="center">
					<tr>
						<td align="center" colspan="2">
							<input name="user_id" id="user_id" class="nui-hidden" />
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<h2><span id="title">首次登陆，请修改正确的机构信息</span></h2>
		              	</td>
	              	</tr>
	              	<tr>
						<td align="right">机构：</td>
	                  	<td align="left">
							<input id="orgId" class="mini-treeselect" url="<%=root%>/org/getStepOrgList" dataField="datas" 
									name="orgId" textfield="orgname" valuefield="id" parentfield="upid"  
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
									allowInput="false" showClose="true" oncloseclick="onCloseClick" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="260"
									popupHeight="300" popupMaxHeight="400" style="width:200px;" required="true" />
						</td>
					</tr>
					<tr>
						<td align="right">姓名：</td>
						<td align="left">
	                   		<input name="name" id="name" class="nui-textbox" style="width:200px;" allowInput="false" enabled="false"/>
	                  	</td>
	              	</tr>
	              	<tr>
						<td align="right">手机号码：</td>
						<td align="left">
	                   		<input name="phone" id="phone" class="nui-textbox" style="width:200px;" allowInput="false" enabled="false"/>
	                  	</td>
	              	</tr>
	              	<tr>
	                  	<td align="right">当前角色：</td>
	                  	<td align="left">
	                   		<input name="role_name" class="nui-textbox" allowInput="false" style="width:200px;" enabled="false"/>
	                  	</td>
	              	</tr>
	              	<tr>
	                  	<td align="right">业务模块：</td>
	                  	<td align="left">
							<select name="model" id="model" class="nui-combobox"   onvaluechanged="onvalueChenaged" >
								<option value = '1001_01'>智能柜台</option>
								<option value = '1001_02'>现金出纳</option>
								<option value = '1001_03'>集中核准</option>
								<option value = '1001_04'>柜员管理</option>
							</select>
	                  	</td>
	              	</tr>
	              <%--	<tr>
	                  	<td align="right">切换角色：</td>
	                  	<td align="left">
	                   		<input name="role_id" class="mini-combobox" allowInput="false"
	                   			emptyText="请选择角色" textfield="name" valuefield="id"
	                   		    url="<%=root%>/zxuser/userRoleList" style="width:200px;"/>
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
						<td align="right">昵称：</td>
	                  	<td align="left">
							<input name="nick_name" class="nui-textbox"  emptyText="请输入昵称" style="width:200px;"/>
						</td>
					</tr>
					<tr>
						<td align="right">头像：</td>
						<td align="left">
							<input class="mini-htmlfile" name="upload_file" id="file1" style="width:200px;"/>
		              	</td>
	              	</tr>
	              	--%>
	              	<tr>
						<td align="center" colspan="2">
							
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<a class="nui-button" onclick="saveNew" style="color: #ffffff;">确认</a>
							<a class="nui-button" onclick="cancel" style="color: #ffffff;">关闭</a>
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
 	getCurrentRole();
 	var isfirst = "0";
 	var islogin = false;
 	function SetData(data){
		var infos = $G.clone(data);
		isfirst = infos.isfirst;
 	}
 	/*
 	 *获取用户信息
 	 */
 	function getUserInfo(){
		$.ajax({
			url: "<%=root%>/zxuser/getUserInfo",
            success: function (text) {
            	if(null != text.record){
            		var r = text.record;
            		//alert(r.orgname);
            		$G.getbyName("user_id").setValue(r.user_id);
            		$G.getbyName("model").setValue(r.model);
            		
					if(r.org_id != null && r.org_id!="" && r.org_id!="null" && r.org_id!=undefined){
						$G.getbyName("orgId").setValue(r.org_id);
						$G.getbyName("orgId").setEnabled(false);
						$("#title").html("请校验您的信息，若信息不一致，请至EHR平台进行更改");
					}else{
						$("#title").html("首次登陆，请修改正确的机构信息");
						$G.getbyName("orgId").setEnabled(true);
						islogin = true;
					}
                  	$G.getbyName("name").setValue(r.user_name);
    	           	$G.getbyName("phone").setValue(r.phone);
    	        	$G.getbyName("role_name").setValue(r.role_name);
    	        
    	           	//$G.getbyName("orgname").setValue(r.orgname);
    	           	//$G.getbyName("uid").setValue(r.tid);
            	}
           	}
    	});
 	}
 	/*
 	 *获取用户信息
 	 */
 	function getCurrentRole(){
		$.ajax({
			url: "<%=root%>/zxuser/getUserRoleInfo",
            success: function (text) {
            	if(null != text.record){
            		var r = text.record;
    	           	$G.getbyName("role_name").setValue(r.role_name);
            	}
           	}
    	});
 	}
	
	
	/**
		保存数据 by 20181224 cxy
	*/
	function saveNew(){
		form.validate();
		if (form.isValid() == false){
			return;
		}
	 	var rolename=$G.getbyName("role_name").getValue();  
	 	var model=$G.getbyName("model").getText();
       	if(rolename == "运营专家角色（省行）" || rolename == "运营专家角色（分行）"){
       		if(model == "" || model == null){
       			$G.alert("请先选择模块!");
           		return;
       		}
       		
       	}
       	
		var data = form.getData();
		$.ajax({
			url:"<%=root%>/zxuser/saveSetupUser",
			data:data,
			success:function(text){
				var flag =$G.decode(text).flag;
				if(flag){
					if(islogin){
						//调用主页面注销动作
						mini.alert("已修改信息,请重新登陆！","提示",function(action){
							//parent.loginOut();
							$G.closemodaldialog("cancel1");	
						})
					}else{
						$G.closemodaldialog("ok");	
					}
				}
			},
			error:function(u,v,x){
				
			}
		})
	}
	
	
 	/*
	 *保存数据(已弃用)
	 */
	function save(){
 		
 		var suffix = $G.getbyName("upload_file").getValue().trim();

 		if("" != suffix){
 			suffix = suffix.substr(suffix.lastIndexOf(".") + 1);
 			suffix = suffix.toUpperCase();
 	 		//JPG、JPEG、PNG、BMP、GIF
 	 		if(!("JPG" == suffix || "JPEG" == suffix || "PNG" == suffix 
 	 				|| "PNG" == suffix || "PNG" == suffix)){
 	 			$G.alert("上传头像文件格式错误,请重新选择!");
 	 			return; 			
 	 		}
 		}
		var user_id = $G.getbyName("user_id").getValue();
		//var role_id = $G.getbyName("role_id").getValue();
		var orgId = $G.getbyName("orgId").getValue();
		var nick_name = $G.getbyName("nick_name").getValue();
		var phone = $G.getbyName("phone").getValue();
	 	var rolename=$G.getbyName("role_name").getValue();  
		
       	if(rolename == "运营专家角色（省行）" || rolename == "运营专家角色（分行）"){
       		$G.alert("请先选择模块!");
       		return;
       	}
		// 异步请求数据
    	var file = $("#file1 > input:file")[0];
		var url = "<%=root%>/zxrole/saveApplyRole";
    	$.ajaxFileUpload({
            url: url, //用于文件上传的服务器端请求地址
            fileElementId: file,//文件上传域的ID
            data: {user_id: user_id, role_id: "", phone: phone, 
            	nick_name: nick_name, orgId: orgId},//附加的额外参数
            dataType: 'json_pre',//返回值类型 一般设置为json
            success: function (data, status){
            	//服务器成功响应处理函数
           		$G.alert("信息修改成功,机构信息需管理员审核后生效!","提示",function(){
           			$G.closemodaldialog("ok");
           			//window.location = "<%=root%>/";
           		});
            },
            error: function (data, status, e){
            	$G.alert("信息修改成功,机构信息需管理员审核后生效!","提示",function(){
           			$G.closemodaldialog("ok");
           			//window.location = "<%=root%>/";
           		});
            }
        });
    	//修改默认角色
    	var url2 = "<%=root%>/zxuser/saveDefaultRole";
		$G.postByAjax({user_id: user_id,role_id:role_id},url2,ajaxConf);
	}

 	function cancel(){
 		/*
 		if("1" == isfirst){
 			var user_id = $G.getbyName("user_id").getValue();
 			//进行首次登录系统时取消角色申请事件
 			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsAsync(false);
			ajaxConf.setIsShowSuccMsg(false);
			ajaxConf.setIsShowProcessBar(false);
			ajaxConf.setSuccessFunc(function (text) {
				var flag =$G.decode(text).flag;
				if(flag){
					$G.closemodaldialog("ok");
  				}
			});
          	var urlStr = "<%=root%>/zxrole/cancel";
			$G.postByAjax({user_id: user_id},urlStr,ajaxConf);
 		}else{
 			$G.closemodaldialog("ok");
 		}
 		*/
 		$G.closemodaldialog("ok");
 	}
 	
 	function onvalueChenaged(){
 		
 		nui.alert("修改完成后请刷新页面！");
 		
 	}
 	
 </script>