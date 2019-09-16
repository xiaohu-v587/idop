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
		<div id="panel1" class="nui-panel" title="通讯录" style="width:100%;height:111px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">所属机构：</th>
							<td align="left">
								<input id="orgSelect" class="mini-treeselect" url="<%=root%>/kpi/getOrgDatas" dataField="datas" 
									name="org_id" textfield="orgname" valuefield="id" parentfield="upid"  
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
									allowInput="false" showClose="true" oncloseclick="onCloseClick" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" style="width:165px;"/>
							</td>
 		                 	<th align="right">EHR号：</th>
							<td align="left">
								<input id="id" name="id" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
	               		<tr>
	               			<th align="right">员工姓名：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right">手机号码：</th>
							<td align="left">
								<input id="mobile" name="mobile"  class="mini-textbox" style="width:165px;"/>
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
							<a class="mini-button" iconCls="icon-download"  id="download" onclick="download()" plain="true">下载</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/telBook/listTel" style="width: 100%;height: 100%;"
		    		multiSelect="true">
			        <div property="columns">            
			            <div headerAlign="center" width="12" type="indexcolumn">序号</div>      
			            <div field="orgname" width="60" headerAlign="center" allowSort="true"  align="left">所属机构</div>                
			            <div field="id" width="24" allowSort="true" headerAlign="center" align="right">EHR号</div>            
			            <div field="name" width="40" allowSort="true" headerAlign="center" align="left">员工姓名</div> 
			            <div field="phone" width="40" allowSort="true" headerAlign="center" align="right">手机号码</div> 
			            <div field="remark" width="60" allowSort="true" headerAlign="center" align="left" >备注</div>
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

	//下载
	function download(){
	
		var org_id=$G.getbyName("org_id").getValue();
		var id=$G.getbyName("id").getValue();
		var name=$G.getbyName("name").getValue();
		var mobile=$G.getbyName("mobile").getValue();
		window.location="<%=root%>/telBook/download?org_id=" + org_id + "&id=" + id + "&mobile=" + mobile + "&name=" + name;   
	
	
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

</script>

