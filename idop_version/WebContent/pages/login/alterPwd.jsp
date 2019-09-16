<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file="/common/jstlres.jsp"%>
<head>
    <title>修改密码</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<%@include file="/common/nuires.jsp" %>
	<style type="text/css">
    html, body
    {
        font-size:12px;
        padding:0;
        margin:0;
        border:0;
        height:100%;
        overflow:hidden;
    }
    </style>

</head>
<body >   
	<div id="div1" class="mini-fit" >
		<form id="form1" method="post">
			<input name="mmflag" class="nui-hidden"/>
	        <table style="table-layout:fixed; padding-top: 10px; padding-left: 5px;" width="100%">
	            <tr>
	                <td style="width:100px;">原密码：</td>
	                <td>
	                    <input id="oldpwd" name="oldpwd"  class="mini-password" 
	                    	 requiredErrorText="原密码不能为空" required="true" style="width:150px;"
	                    	 />
	                </td>    
	            </tr>
	            <tr>
	                <td style="width:100px;">新密码：</td>
	                <td>
	                    <input id="newpwd" name="newpwd" onvalidation="onnewPwdValidation" class="mini-password" 
	                    	requiredErrorText="新密码不能为空" required="true" style="width:150px;" 
	                    	/>
	                </td>
	            </tr> 
	            <tr>
	                <td style="width:100px;">确认新密码：</td>
	                <td>
	                    <input id="renewpwd" name="renewpwd" onvalidation="onrenewPwdValidation" class="mini-password" 
	                    	requiredErrorText="重复密码不能为空" required="true" style="width:150px;" 
	                    	/>
	                </td>
	            </tr>         
	        </table>
		</form>
	</div>
	<div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
       borderStyle="border-left:0;border-bottom:0;border-right:0;">
            <a id="onOk" onclick="onOkClick" class="mini-button" iconCls="icon-ok"  style="width:60px;">确定</a>
	        <a onclick="onCancelClick" class="mini-button" iconCls="icon-close" style="width:60px;">取消</a>
            
    </div>
    
    <script type="text/javascript">
        $G.parse();
        var form = new $G.Form("form1");
        
      	//标准方法接口定义
        function SetData(data) {
        	 data = $G.clone(data);
        	 //alert("o");
        	 //$G.get("onOk").disable();
        }
      	
      /*
      //控制确定按钮状态
	  $("input").blur(function(){
		  var oldpwd = $G.get("oldpwd").getValue();
		  var newpwd = $G.get("newpwd").getValue();
		  var renewpwd = $G.get("renewpwd").getValue();
		  //alert("oldpwd:" + oldpwd + " newpwd:" + newpwd + " newpwd:" + renewpwd);
		  if (oldpwd != "" && newpwd != "" && newpwd == renewpwd) {
			$G.get("onOk").enable();
		  } else {
			$G.get("onOk").disable();
		  }
	  });
      */
      
        // 保存
        function saveData(){
        	form.validate();
        	if (form.isValid() == false) return;
        	var formData = form.getData();
        	formData = $G.encode([formData]);
        	$.ajax({
                url: '<%=root%>/saveAlterPwd',
		        type: 'post',
                data: { data: formData},
                cache: false,
                success: function (text) {
                	var flag = $G.decode(text).flag;
                	if($G.decode(text).mmflag=="1"){
                		$G.alert("您输入的密码过于简单,请重新输入密码！");
                		 $G.get("renewpwd").setValue("");
                		 $G.get("newpwd").setValue("");
                	}
                	if(flag == "-1"){
                		$G.alert("原密码错误！");
                	}else if(flag == "1"){
                		$G.alert("保存成功！","提示信息",function(action){
                			if(action == "ok"){
                				CloseWindow("save");
                			}
                		});
                	}else if(flag == "0"){
                		$G.alert("保存失败！");
                	}else if(flag == "2"){
                		$G.alert("新密码不可与老密码相同,请重新输入密码！");
                		$G.get("renewpwd").setValue("");
                		$G.get("newpwd").setValue("");	
                	}
                    //CloseWindow("save");
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    //alert(jqXHR.responseText);
                    CloseWindow();
                }
            });
        }
        
        
        //确定保存
        function onOkClick() {
        	saveData();
        }
        
        //取消  
        function onCancelClick(e) { 
        	if($G.getbyName("mmflag").getValue()=="1"){
                //CloseWindow("cancel"+$G.getbyName("mmflag").getValue());
                $G.alert("请修改密码！");
        	}else{
        		CloseWindow("cancel");
        	}
        }
        
        function CloseWindow(action) {    
            if (window.CloseOwnerWindow) 
            	window.CloseOwnerWindow(action);
             else
            	window.close();            
        }
        
        //验证旧密码
        function onoldPwdValidation(e){
        	
        	var oldpwdflag;
        	var vv = e.value;
        	$G.ajax({
                url: '<%=root%>/valiPwd',
		        type: 'post',
                data: { oldpwd: vv},
                cache: false,
                success: function (text) {
                    	oldpwdflag = $G.decode(text).flag;
                    	if(e.isValid){
                    		if(oldpwdflag == "0"){
                    			alert("hhhhhh");
                                e.errorMode = "icon";
                    			e.errorText = "密码不能少6个字符";
                                e.isValid = false;
                                alert(e.errorMode);
                                e.setValue("iiiii");
                    		}
                    	}
                    	
                }
            });
        	/*
        	e.errorText = "密码不能少于5个sdafd字符";
            e.isValid = false;
            */
        }
        
        // 验证新密码格式
        function onnewPwdValidation(e) {
        	var renewpwd = $G.get("renewpwd").getValue();
            if (e.isValid) {
                if (e.value.length < 6) {
                    e.errorText = "密码不能少于6个字符";
                    e.isValid = false;
                }else if(e.value.length > 8){
                	e.errorText = "密码不能大于8个字符";
                    e.isValid = false;
                }else if(renewpwd != null && renewpwd != ""){
                	if(e.value != renewpwd){
    	        		e.errorText = "两次密码不一致";
    	                e.isValid = false;
    	        	}else{
    	        		$G.get("renewpwd").setIsValid(true);
    	        	}
                }
            }
        }
        
        // 验证重复新密码格式
        function onrenewPwdValidation(e){
        	 var newpwd = $G.get("newpwd").getValue();
        	 if (e.isValid) {
                 if (e.value.length < 6) {
                     e.errorText = "密码不能少于6个字符";
                     e.isValid = false;
                 }else if(e.value.length > 8){
                	e.errorText = "密码不能大于8个字符";
                    e.isValid = false;
                 }else if(newpwd != null && newpwd != ""){
                	 if (e.isValid) {
         	        	if(e.value != newpwd){
         	        		e.errorText = "两次密码不一致";
         	                e.isValid = false;
         	        	}else{
         	        		$G.get("newpwd").setIsValid(true);
         	        	}
                 	}
                 }
             }
        }
        
    </script>

</body>
</html>