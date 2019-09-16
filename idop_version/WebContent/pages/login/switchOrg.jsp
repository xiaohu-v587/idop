<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@include file="/common/nuires.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>切换机构</title>
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
<body>
<div id="div1" class="nui-fit" >
		<ul id="tree" class="nui-tree" style="width:300px;padding:5px;" 
        showTreeIcon="true" textField="text" idField="id" expandOnLoad="true"></ul>
	</div>
	<div class="nui-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
       borderStyle="border-left:0;border-bottom:0;border-right:0;">
            <a id="onOk" onclick="onOkClick" class="nui-button" iconCls="icon-ok"  style="width:60px;">确定</a>
	        <a onclick="onCancelClick" class="nui-button" iconCls="icon-close" style="width:60px;">取消</a>
            
    </div>
</body>
</html>
<script type="text/javascript">
        $G.parse();
        var tree = $G.get("tree");
        var list = [];
        $G.ajax({
                url: '<%=root%>/loadOrgAndRole',
                type: 'POST',
                cache: false,
                dataType:'json',
                success: function (text) {
                	list = text;
	    			tree.loadList(list, "id", "pid");
                },
                error: function () {
                    $G.alert("获取用户机构和角色信息异常，请联系管理员！");
                }
            });
            
            
       //取消  
        function onCancelClick(e) { 
       		CloseWindow("cancel");
        }
        
        function CloseWindow(action) {    
            if (window.CloseOwnerWindow) 
            	window.CloseOwnerWindow(action);
             else
            	window.close();            
        }
        
        function onOkClick(){
       		var selectNode = tree.getSelectedNode ( );
        	if(selectNode === undefined || tree.isLeaf(selectNode) === true){
        		$G.alert("请选择机构进行切换！");
        	}else{
        		var orgnum = tree.getValue();
        		$G.ajax({
	                url: '<%=root%>/switchOrg?orgnum='+orgnum,
	                type: 'GET',
	                cache: false,
	                dataType:'json',
	                success: function (text) {
	                	CloseWindow("OK");
	                	parent.top.location.reload();
	                },
                	error: function () {
                   	 	$G.alert("机构切换异常,请联系系统管理员");
                	}
            	});
        	}
        }

    </script>