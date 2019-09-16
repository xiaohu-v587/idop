<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
 	<head>
		<title>数字运营</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@ include file="/common/nuires.jsp"%>
	</head>
	<style type="text/css">
	html{width:100%;height:100%;min-width:1080px;min-height:820px;overflow:auto;}
	    body{background-image:url(resource/zxcss/zximage/body_bg.jpg);background-repeat:no-repeat;background-size:100% 100%;background-size:100% 100%;
	height:100%;width:100%;}
    </style>
<body>
	<div id="layout1" class="nui-layout" style="width:100%;height:100%;">
	    <div class="header" style="overflow:hidden;" region="north" class="app-header" height="auto" showSplit="false" showHeader="false">
		    <div style="position:absolute;top:10px;left:10px;">
		    	<!-- <h1 style="margin:0;padding:15px;cursor:default;font-family:微软雅黑,黑体,宋体;color: #ffffff;
		    	">公金联盟</h1> -->
		    	<span style="display:block;background-image: url(resource/zxcss/zximage/logo.png);background-repeat:no-repeat;background-size:65%;width:800px;height:500px;margin:15px 0 0 20px;"></span>
		    </div>
	        <div style="position:absolute;top:18px;right:10px;">
	        </div>
	    </div>
	    <div title="center" region="center" style="width:100%;height:100%;overflow:hidden;" bodyStyle="" >
	       <div style="color:red;font-size:15px;margin-top: 200px;" > 
		        <p align="center"> 该用户还没有角色权限或者归属机构存在问题，请联系管理员</p>
		        <p align="center"> 请关闭当前页面，解决问题后，再尝试登录</p>
	       </div>
	    </div>
	</div>
	
</body>
</html>
