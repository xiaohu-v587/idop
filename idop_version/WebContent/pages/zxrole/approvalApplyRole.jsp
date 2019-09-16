<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="用户管理" style="width:100%;height:65px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">姓名：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
		                    <th align="right">审核结果：</th>
							<td align="left">
								<input style="width:165px;" class="nui-dictcombobox" name="applyStatus" 
									valueField="id" emptyText="请选择" textField="text" 
									data='[{"id":"-1","text":"未审核"},{"id":"0","text":"未通过"},{"id":"1","text":"通过"}]'/>
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                    <a class="nui-button" iconCls="icon-search" onclick="approval()">审核</a>
		                </td>
		            </tr>
		       	</table>
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/zxrole/getApplyRoleList" style="width: 100%;height: 100%;"
		    		multiSelect="true">
			        <div property="columns">
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="name" width="100" allowSort="true" headerAlign="center" align="left">用户姓名</div>
			            <div field="rolename" width="100" allowSort="true" headerAlign="center" align="left">角色名称</div>
			            <div field="orgname" width="100" allowSort="true" headerAlign="center" align="left">机构名称</div>
			            <div field="apply_status" width="100" allowSort="true" headerAlign="center" align="left" >审核状态</div>
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

	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}
	
	function approval(){
		var rows = grid.getSelecteds();
		if(rows.length > 0){
			var ids = "";
			for(var i = 0; i < rows.length; i++){
				var status = rows[i].status;
				if("0" == status || "1" == status){
					$G.alert("已审核的不能再次审核!");
					return;
				}
				ids += rows[i].id + ",";
			}
			ids = ids.substring(0, ids.length - 1);
			var url="<%=root%>/zxrole/approval";
			var bizParams = {ids: ids};
			$G.showmodaldialog("审核", url, 500, 350, bizParams, function(action){
	       		grid.reload();
			});
		}else{
			$G.alert("至少选择一行数据");
		}
	}

</script>