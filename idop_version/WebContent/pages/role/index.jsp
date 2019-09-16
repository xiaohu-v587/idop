<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="角色管理" style="width:100%;height:74px;" showToolbar="false" showCollapseButton="false"
			showFooter="false" allowResize="false" collapseOnTitleClick="false">
			<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table >
						<tr>
		                   	<th align="right">角色名称：</th>
							<td align="left">
								<input id="jgh" name="jsmc" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right"></th>
							<td align="left">
							</td>
		                    <th align="right"></th>
							<td align="left">
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
		
		<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
			<table style="width:100%;">
				<tr>
					<td style="width:100%;">
						<a class="mini-button" iconCls="" id="addBtn" onclick="add()" >新增</a>
            	    	<a class="mini-button" iconCls="" id="editBtn" onclick="edit()" >编辑</a>
            	     	<a class="mini-button" iconCls="" id="removeBtn" onclick="del()" >删除</a>
					</td>
                    <td style="white-space:nowrap;">
                    	<a class="nui-button" iconCls="" onclick="reset()">重置</a> 
						<a class="mini-button" iconCls="" onclick="search()">查询</a>
                    </td>
				</tr>
			</table>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;"  sortMode="client" allowUnselect="false"
				url="<%=root%>/role/getList" oncellclick=""  onselectionchanged="onSelectionChanged" multiSelect="false" autoEscape="false"
				onshowrowdetail="onShowRowDetail">
		        <div property="columns">
		              <div type="checkcolumn"  name="checkCloumn"></div>
		              <div field="ID" name="ID" width="50" visible="false"></div>
		              <div headerAlign="center" type="indexcolumn" align="right">序号</div>
		              <div field="id"  visible="false"  width="50" headerAlign="center" allowSort="true"><fmt:message key="ID"/></div>
		              <div field="name"  width="160" allowSort="true" align="left" headerAlign="center">角色名称</div>
		              <div name="action1" width="60" renderer="onActionRenderer3" field="role_level"   allowSort="true" align="center" headerAlign="center">角色级别</div>
                      <div field="sjname" width="140" allowSort="true" align="left" headerAlign="center">上级角色名称</div>
                      <div field="remark" width="100"  allowSort="true" align="left" headerAlign="center">描述</div>
                      <!-- <div name="action" width="60" renderer="onActionRenderer" headerAlign="center" align="center">角色用户分配</div> --> 
		        </div>
			</div>  
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid1");
	var form = $G.getForm("form1");
	grid.load();

	function add(){
		var url = "<%=root%>/role/form";
		var bizParams = {pageType:"add"};
		$G.showmodaldialog("新增角色", url, 500, 220, bizParams, function(action){
			grid.reload();
		});
	}

	function edit(){
		var row = grid.getSelected();
		if(row){
			var url = "<%=root%>/role/form";
			var bizParams = {pageType:"edit",id:row.id};
			$G.showmodaldialog("编辑角色", url, 500, 220, bizParams, function(action){
				grid.reload();
			});
		}else{
			$G.alert("请先选择一行数据！");
		}
	}

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
	                ajaxConf.setIsAsync(false);
	        		ajaxConf.setIsShowSuccMsg(false);
	        		ajaxConf.setIsShowProcessBar(false);
                	ajaxConf.setSuccessFunc(function (text){
						if(text=="2"){
							$G.alert("无此权限，请与管理员联系");
                        }else{
							var record = text;
                         	if(record=="1"){
								$G.alert("删除成功!");
							}else if(record=="3"){
								$G.alert("该角色下分配了用户，不能删除!");
							}else if(record=="4"){
								$G.alert("该角色被分配了权限，不能删除!");
							}else if(record=="5"){
								$G.alert("该角色有从属角色，不能删除!");
							}
                        } 
                    	grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/role/del", ajaxConf);
				}
            });
		}else{
			$G.alert("请先选择一行数据！");
		}
	}

	function search(){
		 var jsmc = $G.getbyName("jsmc").getValue();
         grid.load({key:jsmc});
	}

	//重置
	function reset(){
		form.reset();
	}

	function onActionRenderer(e){
		var obj = '<a href="javascript:userFun()" style="img-decoration:none"><img src="<%=root%>/resource/nui/themes/icons/user.png" border="0"></a>';
		return obj;
	}

	function userFun(){
		var row = grid.getSelected();
	   	var urlStr = "<%=root%>/role/allot";
		var bizParams = {rolename:row.name,id:row.id};
		$G.showmodaldialog("岗位用户分配", urlStr, 800, 500, bizParams, function(action){
			grid.reload();
		});
	}

	function onSelectionChanged(){
		var rows = grid.getSelecteds();
		if(rows.length>1){
			$G.get("editBtn").disable();
		}else{
			$G.get("editBtn").enable();
		}
	}
</script>