<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<html>
<head>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/resource/images/logo/20.jpg" /> 
	<script type="text/javascript">
	$(function() {
		$G.getbyName("username").focus();
		// 监听回车事件
		addEnterEvent();
	});
	
	// 监听回车事件
	function addEnterEvent() {
		$("#loginForm").keydown(function(event) {
			if (event.keyCode == 13) {
				Button_submit_onClick(event);
			}
		});
	}
	</script>
	<title>登录</title>
	<style type="text/css">
 		body,html{height:100%;padding:0;margin:0;}
		#userlogin_body {background:url(pages/login/bg.png)  no-repeat left top; background-size:100% 100%; font: 12px/150% arial, "宋体" ,helvetica,sans-serif;text-decoration: none;}
		
		.logo_box{width:420px;height:55px;margin:0 auto;background-image:url(pages/login/logo2.png);background-repeat:no-repeat;background-size:100% 100%;z-index:2;position:absolute;left:50%;margin-left:-210px;top:40%;margin-top:-200px;}
		.logo{display:block;margin-left:150px;font-size:20px;color:#2fa2fd;margin-top:10px;padding-left:10px;border-left:1px #2fa2fd solid;}
		
		.user_login{width:420px;height:240px;background-image:url(pages/login/user-login-bg.png);background-repeat:no-repeat;background-size:100% 100%;z-index:1;position:absolute;left:50%;margin-left:-210px;top:40%;margin-top:-140px;}
		
		.user_main_box{
			width:100%; height:122px; margin-left:auto;margin-right:auto;margin-top:30px;
			}
		li{list-style:none;}
		ul{border:0; padding:0; margin:0 auto;}
		.user_main_box ul li{
			float:left;
			}
		.user_main_box ul {
			clear: both; padding-left:100px; font-weight:bold;
		}
		.user_main_text{
			width:60px; font-size:14px; line-height:30px; margin-top:16px; color:#2fa2fd;
			}
		.user_main_input{margin-top:16px;}
		.user_main_input input{
			width:160px;background-color:#2fa2fd; border:none; border-radius:4px; height:30px; color:#ffffff; padding-left:10px;
			}
		.user_button{
			 width:100%;
			}
		.user_button input{
			width:160px; height:40px;background-image:url(pages/login/user_button.png);background-repeat:no-repeat;background-size:100% 100%;font-size:14px; color:#FFF; margin-left:130px; font-weight:bold;background-color:#102862;border:none;
			}
	</style>
</head>
<body id=userlogin_body>
		<div class="logo_box">
    </div>
	<div class="user_login">
    	<div class="user_main_box">
			<ul>
				<li class="user_main_text" style="color:#2fa2fd;">
					用户名：
				</li>
				<li class="user_main_input">
					<input type="text" name="username"  id="username" maxlength="20"/>
				</li>
			</ul>
			<ul>
				<li class="user_main_text" style="color:#2fa2fd;">
					密 码：
				</li>
				<li class="user_main_input">
					<input  type="password" id='password' name="password"/>
				</li>
			</ul>
		</div>
    	<div class="user_button">
        	<input class="ibtnentercssclass" type="button" value="登录" onclick="Button_submit_onClick()"/>
        </div>     
    </div>
	</body>
	<script type="text/javascript">
		$G.parse();
		function Button_submit_onClick(self) {
			var username = $("#username").val();
			if(username == '') {
				if(username == '') {
					$G.GcdsAlert(username);
					return;
				}
			}
			var password = $("#password").val();

			var json = {username:username,passwd:password};
			// 判断是否达到最大用户在线数
			// 异步请求数据
	    	var ajaxConf = new GcdsAjaxConf();
	    	ajaxConf.setIsShowSuccMsg(false);
	    	ajaxConf.setIsShowProcessBar(false);
	    	ajaxConf.setSuccessFunc(
	    		function(data) {
	    			if(data.code == '0000') {
	    				window.location = "<%=root%>/";
	    			}
	    			else{
		            	mini.alert(data.desc);
		            }
	    		}
			);
			$G.postByAjax(json, "/login", ajaxConf);
		}
	</script>
</html>