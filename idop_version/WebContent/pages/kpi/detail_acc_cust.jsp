<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>质效系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<!-- 行政事业客户经理 客户统计展示 -->
		<div class="nui-fit">
	    	<div id="datagrid1" class="nui-datagrid" style="width: 100%;height: 100%;"
	    		multiSelect="true" showPager="false">
		        <div property="columns">            
		            <div field="cust_name" width="13%" headerAlign="center" allowSort="true"  align="center">客户名称</div>                
		            <div field="clas_five" width="15%" allowSort="true" headerAlign="center" align="center">五层分类</div>            
		            <div field="cust_effe" width="10%" allowSort="true" headerAlign="center" align="center">客户效能值</div> 
		            <div field="public_msg" width="10%" allowSort="true" headerAlign="center" align="center">对公短信</div>
		            <div field="elec_retu_box" width="10%" allowSort="true" headerAlign="center" align="center">电子回单箱</div>
		            <div field="unit_sett_card" width="10%" allowSort="true" headerAlign="center" align="center">单位结算卡</div>
		            <div field="net_silver" width="10%" allowSort="true" headerAlign="center" align="center">网银</div>
		            <div field="deposit_day" width="10%" allowSort="true" headerAlign="center" align="center">日均存款</div> 
		            <div field="kpi" width="10%" allowSort="true" headerAlign="center" align="center">KPI值</div>
		        </div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid1");
	var form = $G.getForm("form1");
	function setData(data){
		var infos = $G.clone(data);
		var cust_mgr_no = infos.cust_mgr_no;
		var period = infos.period;
		if(cust_mgr_no&&period){
			var url="<%=root%>/kpi/getDetail?mgr_no="+cust_mgr_no+"&period="+period;
			grid.setUrl(url);
			grid.load();
		}
	}
 </script>
