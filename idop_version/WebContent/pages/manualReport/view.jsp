<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html>
	<head>
		<title>预览</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body> 
		<div class="mini-fit">
			<div id="datagrid1" class="mini-datagrid" idField='mid' style="width:100%;margin:1% 0;border-right:0;"
					  dataField="data" emptyText="没有数据" showPager="false" allowRowSelect="false"
					  showEmptyText="true" multiSelect="true">
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid1");
	var orgid  = "";
	//标准方法接口定义
	function setData(data) {
		data = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
		orgid = data.orgid;
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (data){
	    	var zdmc = data.zdmc.split(",");
	    	var columns = [{header:"序号",type:"indexcolumn",headerAlign:"center"}];
			for(var i=0;i<zdmc.length-1;i++){
	    		columns.add({header:zdmc[i],field:"column"+(i+1),headerAlign:"center",align:"center"});
	    	}
	    	grid.setColumns(columns);
	    	grid.setData(data.table);
	    });
		$G.postByAjax({rid:data.rid,uid:data.uid,orgid:orgid},"<%=root%>/manualReport/getTableByUid",ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
</script>