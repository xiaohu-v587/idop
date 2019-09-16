<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>报送</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<body>    
		<div class="mini-fit">
			<form id="form1" method="post" enctype="multipart/form-data">
	           	<table style="table-layout:fixed;padding-right:10%;" id="detailTable" width="100%">
	               	<tr>
	                   	<td align="right" width="100" height="35">报表名称：</td>
	                   	<td align="left">    
	                       	<input name="task_name" style="width:100%;" class="nui-textbox" enabled="false"/>
	                   	</td>
	               	</tr>
	               	<tr>
	                   	<td align="right" height="35">截止日期：</td>
	                   	<td align="left">    
	                       	<input name="task_enddate" style="width:100%;" class="nui-datepicker" enabled="false"/>
	                   	</td>
	               	</tr>
	                <tr>
	                   	<td align="right" height="35">发送部门：</td>
	                   	<td align="left">    
	                       	<input name="issuer_org" style="width:100%;" class="nui-textbox" enabled="false"/>
	                   	</td>
	               	</tr>
					<tr>
	                   	<td align="right">报表说明：</td>
	                   	<td align="left">    
							<input name="task_detail" style="width:100%;height:150px" class="nui-textarea" enabled="false"/>
						</td>
					</tr>
					<tr>
	                   	<td align="right">附件列表：</td>
	                   	<td align="left">    
							<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;margin:1% 0" 
								sortMode="client" showPager="false" showEmptyText="true" emptyText="没有文件" 
								url="<%=root%>/manualReport/getFileList">
								<div property="columns">
									<div field="file_id" visible="false"><fmt:message key="ID"/></div>
									<div type="indexcolumn" headerAlign="center" align="center">序号</div>
							        <div field="file_name" allowSort="true" headerAlign="center" align="left" renderer="addLink">文件名</div>
								</div>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="nui-toolbar" style="text-align: center;padding: 10px 0;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" iconCls="" onclick="onCancel">关闭</a>       
		</div>        
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var grid = $G.get("datagrid1");
	
	//标准方法接口定义
	function setData(data) {
		data = $G.clone(data);
		form.setData(data);
		grid.load({id:data.report_id});
	}
	
	function addLink(e){
		var record = e.record;
		return "<a style='color:blue' href='<%=root%>/manualReport/download?id="+ record.file_id +"'>" + record.file_name + "</a>";
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
</script>