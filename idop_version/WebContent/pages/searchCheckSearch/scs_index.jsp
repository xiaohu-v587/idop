<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script src="<%= request.getContextPath() %>/resource/js/jquery.min.js"></script>
 <script src="<%=request.getContextPath()%>/resource/js/panorama.js" type="text/javascript"></script>
 <%@include file="/common/nuires.jsp" %>
<title>查询查复查询</title>
<%@include file="/common/nuires.jsp" %>
</head>

<body>
<form id="form1">
	<table width="100%">
		<tr>
			<td style="width: 100px;">业务模块：</td>
			<td style="width: 300px;"><input name="biz_module" class="mini-combobox" style="width: 77%" url="<%=root%>/param/getDict?key=dop_ywtype" textfield="remark" valuefield="val" emptyText="请选择..." onvaluechanged="checkModelchange"/></td>
			<td style="width: 100px;">预警名称：</td>
			<td style="width: 300px;"><input id="warning_name" name="warning_name" class="mini-combobox" style="width: 77%" emptyText="请选择..."  multiSelect="false" valueField="warning_name" textField="warning_name"/></td>
			<td style="width: 100px;">查询编号：</td>
			<td style="width: 300px;"><input name="search_no" class="mini-textbox" style="width: 77%"/></td>
		</tr>
		<tr>
			<td style="width: 100px;">查询日期：</td>
			<td style="width: 300px;"><input name="search_start_date" class="mini-datepicker" />至<input name="search_end_date" class="mini-datepicker" /></td>
			<td style="width: 100px;">查询机构：</td>
			<td style="width: 300px;"><input name="search_org" class="mini-treeselect" style="width: 77%" url="<%=root%>/org/getListByOrg" dataField="datas" 
										name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
										valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" onvaluechanged=""
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600"/></td>
			<td style="width: 100px;">查复机构：</td>
			<td style="width: 300px;"><input name="check_org" class="mini-treeselect" style="width: 77%" url="<%=root%>/org/getListByUser?noliketype=2" dataField="datas" 
										name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
										valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" onvaluechanged=""
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600"/></td>
		</tr>
		<tr>
			<td style="width: 100px;">查复日期：</td>
			<td style="width: 300px;"><input name="check_start_date" class="mini-datepicker" />至<input name="check_end_date" class="mini-datepicker" /></td>
			<td style="width: 100px;">查询查复状态：</td>
			<td style="width: 300px;"><input name="sc_status" class="mini-combobox" style="width: 77%" url="<%=root%>/param/getDict?key=searchCheck_status" textfield="remark" valuefield="val" emptyText="请选择..."/></td>
			<td style="width: 100px;">是否超期：</td>
			<td style="width: 300px;"><input name="is_overdue" class="mini-combobox" style="width: 77%" url="<%=root%>/param/getDict?key=isOverdue" textfield="remark" valuefield="val" emptyText="请选择..."/></td>
		</tr>
		<tr>
			<td colspan="6" style="text-align: center;"><a class="mini-button" onClick="search()">查询</a><span class="separator"></span><a class="mini-button" onClick="reset()">重置</a></td>
		</tr>
	</table>
</form>


<div class="mini-toolbar" style="text-align: right;">
	<a class="mini-button" onclick="detail()">查询查复详情</a>&nbsp;&nbsp;<a class="mini-button" onclick="download()">导出</a>
</div>

<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="height: 100%" url="<%=root%>/searchCheckSearch/getList">
		<div property="columns">
			<div field="biz_module" headerAlign="center" align="center">业务模块</div>
			<div field="warning_name" headerAlign="center" align="center">预警名称</div>
			<div field="search_no" headerAlign="center" align="center">查询编号</div>
			<div field="search_date" headerAlign="center" align="center">查询日期</div>
			<div field="search_enddate" headerAlign="center" align="center">查询截止日期</div>
			<div field="search_org" headerAlign="center" align="center">查询机构</div>
			<div field="searcher" headerAlign="center" align="center">查询人</div>
			<div field="check_date" headerAlign="center" align="center">查复日期</div>
			<div field="check_org" headerAlign="center" align="center">查复机构</div>
			<div field="checker" headerAlign="center" align="center">查复人</div>
			<div field="sc_status" headerAlign="center" align="center">查询查复状态</div>
			<div field="is_over_due" headerAlign="center" align="center">是否超时</div>
			
		</div>
	</div>
</div>
</body>
<script>

	$G.parse();
	var grid = $G.get("datagrid1");
	var form = $G.getForm("form1");
	var warning_code = "";
	grid.load();

	function checkModelchange(e) {
		var val = e.value;
		var url = "<%=root%>/searchCheckRecall/getWarningTypeList?val="+val;
  		$G.getbyName("warning_name").setUrl(url);
	}
	
	function search() {
		var data = form.getData();
		grid.load(data);
	}
	
	//重置查询
	function reset(){
		form.reset();
	}
	
	//详情
	function detail() {
		var row = grid.getSelected();
		if(row){
			var flownum = row.search_no;
			var url = "<%=root%>/searchCheckSearch/detail?id="+flownum;
			var bizParams = {pageType: "detail", flownum: flownum};
			$G.showmodaldialog("查询查复详情",url,1000,675,bizParams,function(){
				grid.reload();
			});
		}else{
			$G.alert("请选择一行数据！");
		}
	}
	
	//导出
	function download() {
		var data = form.getData();
		data['pageSize']  = 1;
		data['pageIndex'] = 0;
		$.ajax({
			url: "<%=root%>/searchCheckSearch/getList",
			data:data,
			cache: false,
			success: function (text) {
			  var returndata = mini.decode(text);
			  data['pageSize']  = 9999999;
			  data['execlfilename'] = '查询查复列表';
// 			  if(returndata.total>30000){
// 				$G.alert("导出数据不能超过30000条！");
// 				return;
// 			  }
			  callHeadAndTextToDataByDataGrid(grid,data);
			  window.location="<%=root%>/searchCheckSearch/download?"+connUrlByJson(data);
			}
		});
	}
	
	function onCloseClick(e) {
    var obj = e.sender;
    obj.setText("");
    obj.setValue("");
  
}
</script>
</html>


