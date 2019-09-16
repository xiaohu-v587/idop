<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/js/jquery.min.js"></script>
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="用户管理" style="width:100%;height:111px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table >
						<tr>
		                   	<th align="right">机构：</th>
							<td align="left">
								<input id="orgSelect" class="mini-treeselect" url="<%=root%>/org/getListByUser?noliketype=2" dataField="datas" 
									name="orgnum" textfield="orgname" valuefield="id" parentfield="upid"  
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
									allowInput="false" showClose="true" oncloseclick="onCloseClick" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" style="width:165px;"/>
							</td>
		                   	<th align="right">机构号：</th>
							<td align="left">
								<input id="jgh" name="jgh" class="mini-textbox" style="width:165px;"/>
							</td>
		                    <th align="right">用户EHR号：</th>
							<td align="left">
								<input id="userno" name="userno" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
	               		<tr>
		                   	<th align="right">姓名：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right">角色</th>
		                    <td align="left">  
		                    	<input id="rolename" name="rolename" class="mini-textbox" style="width:165px;"/> 
		                    </td>
		                    <th align="right"></th>
		                    <td align="left">    
		                    </td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
							<!-- <a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">新增</a> -->
          	     			<a class="mini-button" iconCls="" id="editBtn" onclick="edit()" >编辑</a>
          	     			<!-- <a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a> -->
		                	<span class="separator"></span>
                			<!-- <a class="mini-button" iconCls="icon-undo" id="addBtn" onclick="resetPwd()" plain="true">重置密码</a> -->
                			<a class="mini-button" iconCls=""  id="download" onclick="download()" style="display:none" >下载</a>
                			<a class="mini-button" iconCls=""  id="download2" onclick="download2()" >下载</a>
		                   <span class="separator"></span>
					        <a class="mini-button" iconCls=""  id="permission" onclick="permission()">数据权限范围</a>
	          
		                </td>     
		                <td style="white-space:nowrap;">
		                	<a class="nui-button"  onclick="reset()">重置</a>  
		                    <a class="nui-button"  onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/user/listUser" style="width: 100%;height: 100%;" 
		    		multiSelect="false">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>    
			            <div field="user_no" width="100" headerAlign="center" allowSort="true"  align="center">登陆名</div>                
			            <div field="name" width="100"  allowSort="true" headerAlign="center" align="center">用户姓名</div>
			            <div field="phone" width="150" allowSort="true" headerAlign="center" align="center">电话号码   </div>>         
			            <div field="orgname" width="150" allowSort="true" headerAlign="center" align="center">所属机构</div> 
			            <div field="bancsid" width="100" allowSort="true" headerAlign="center" align="center">机构号</div> 
			            <div field="rolename" width="150" allowSort="true" headerAlign="center" align="center">当前角色</div> 
			            <div field="stat1" width="80" allowSort="true" headerAlign="center" align="center" >状态</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();

	var grid =$G.get("datagrid1");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	
	grid.load();

	//新增
	function add() {
        var url = "<%=root%>/user/form";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("新增用户", url, 500, 250, bizParams, function(action){
	    	 grid.reload();
	    });
	}

	//编辑
	function edit(){
		var row = grid.getSelected();
		if (row) {
			var url = "<%=root%>/user/form";
			var bizParams = {pageType:"edit",id:row.id};
	        $G.showmodaldialog("编辑用户", url, 800, 600, bizParams, function(action){
		    	 grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}

	//删除
	function del(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
				if (action == 'ok') {
                	var ids = "";
	                for(var index = 0;index < rows.length;index++){
						if(ids == ""){
	                    	ids = rows[index].id;
	                  	} else {
	                    	ids += "," + rows[index].id;
	                  	}
	                }
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/user/del", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
		}
	}

	//重置密码
	function resetPwd(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			$G.GcdsConfirm("确定重置已选中用户的密码？", "删除提示", function(action) {
				if (action == 'ok') {
                	var ids = "";
	                for(var index = 0;index < rows.length;index++){
						if(ids == ""){
	                    	ids = rows[index].id;
	                  	} else {
	                    	ids += "," + rows[index].id;
	                  	}
	                }
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/user/resetPwd", ajaxConf);
              	}
			});
		}else{
			$G.alert("请选中一条记录");
		}
	}

	//下载
	function download(){
		var orgnum=$G.getbyName("orgnum").getValue();
		var jgh=$G.getbyName("jgh").getValue();
		var userno=$G.getbyName("userno").getValue();
		var name=$G.getbyName("name").getValue();
		var rolename=$G.getbyName("rolename").getValue();
		window.location="<%=root%>/user/download?orgnum=" + orgnum + "&jgh=" + jgh + "&userno=" + userno + "&name=" + name + "&rolename=" + rolename;   
	}
	//下载2
	function download2(){
	    var data = form.getData();
	    data['pageSize']  = 1;
	    data['pageIndex'] = 0;
		var orgnum=$G.getbyName("orgnum").getValue();
		var jgh=$G.getbyName("jgh").getValue();
		var userno=$G.getbyName("userno").getValue();
		var name=$G.getbyName("name").getValue();
		var rolename=$G.getbyName("rolename").getValue();
		window.location="<%=root%>/user/download2?orgnum=" + orgnum + "&jgh=" + jgh + "&userno=" + userno + "&name=" + name+ "&rolename=" + rolename;   
	}
	
	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }

	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}

	//弹出选择权限菜单树界面
	function permission(){
		var row = grid.getSelected();
		if (row) {
			var url = "<%=root%>/user/permission";
			var bizParams = {pageType:"edit",id:row.id,name:row.name};
	        $G.showmodaldialog("选择菜单树", url, 400, 550, bizParams, function(action){
	        	grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}

	
	
</script>

