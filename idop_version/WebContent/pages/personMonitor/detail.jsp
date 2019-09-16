<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>监测说明</title>
	<style type="text/css">
	html,body {
		margin: 0; 
		padding: 0; 
		border: 0; 
		width: 100%; 
		height: 100%; 
		
	} 
	</style> 
</head>
<body>
	
     <div class="content_box" style="height:80%;width:100%; "  >
     <div align="center" style="font-size: 16px;font-weight:bold;height:10%" ></div>
	    <div class="mini-fit">
	    	<div id="datagrid" dataField="data" class="mini-datagrid" style="width: 100%; height: 98%;" sortMode="client" emptyText="没有对应的记录"
				allowUnselect="false" url="<%=root%>/personMonitor/getDetail" multiSelect="true" allowCellSelect="false"  autoEscape="false"
				  showEmptyText="true">
				<div id="detailColumns" property="columns"  > 
 		            <div type="indexcolumn"  width="40" allowMove="false"  headerAlign="center">序号</div> 
 		            <div field="branch_name" headerAlign="center"  allowSort="true"  align="center">分行名称</div> 
 		            <div field="org_name" headerAlign="center" width="100"  allowSort="true"  align="center" >机构名称</div> 
 		            <div field="ehr_no" headerAlign="center" width="100" allowMove="false" allowSort="true" align="center">EHR号</div>
 		            <div field="teller_no" headerAlign="center" width="100"  allowSort="true"  align="center" >人员名称</div> 
 		            <div field="monitor_date" headerAlign="center"  width="100"  allowSort="true"  align="center" >监测时间</div> 
 		            <div field="monitor_rate" headerAlign="center"  width="100"  allowSort="true" url="<%=root%>/param/getDict?key=monitor_rate"  align="center" >监测频率</div> 
 		            <div field="count" headerAlign="center" width="100"  allowSort="true"  align="center"  dataType="int">预警数量</div> 
		        </div>
	       </div>
	    </div>
    </div>
    <div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;"
	 	borderStyle="border-left:0;border-bottom:0;border-right:0;">               	
		<a class="nui-button" iconCls="" onclick="download()" style="width:90px;">导出监测明细</a>  
	</div>

	
	<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid");
	var flag=1;
 	var datas=null;
	 
	  //关闭窗口
	  function CloseWindow(action) {
		if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
		else window.close();
	  }
	
	  //取消
	  function onCancel() {
  	  	CloseWindow("cancel");
	  }
	  
	//获取父页面传递来的json数据
	  function setData(data) {
		  cData = data;
		  var monitor_date=cData.monitor_date;
		  var monitor_name=cData.monitor_name;
		  var columns  =  grid.getColumns();			
			if(flag==1){
				 $.ajax({
	                 url: "<%=root%>/personMonitor/queryField",	//查询该指标下面需要转码的field
	  		         type: 'post',
	                 data:  {monitor_name:monitor_name},
	                 cache: false,
	                 success: function (text) {
	                 	var headers = mini.decode(text);
	  		           	for(var i=0;i<headers.length;i++){	  		           
	  		           		columns.push({header:headers[i].header,field:headers[i].field,headerAlign:"center",align:"center",width:"150px",allowSort:true,dataType:"int"})
	  		           		grid.setColumns(columns);
	  		            }
	  		           	flag=2;
	                 },
	                 error: function (jqXHR, textStatus, errorThrown) {
	                     alert(jqXHR.responseText);
	                     CloseWindow();
	                 }
	              });
				 grid.load({monitor_name:monitor_name,monitor_date:monitor_date});
			}
	}
	
	function download(){
	  	cData.pageIndex ="0";
		cData.pageSize = "999999";
		window.location="<%=root%>/personMonitor/download?"+connUrlByJson(cData);
	}
	  
	
	</script>
</body>
</html>