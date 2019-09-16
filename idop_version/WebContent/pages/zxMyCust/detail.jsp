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
		<div class="nui-fit" style="padding-top: 1px;">
			<form id="form1">
				<input id="id" name="id" class="nui-hidden"/>
				<input id="customercode" name="customercode" class="nui-hidden"/>
				<input id="orgnum" name="orgnum" class="nui-hidden"/>
			</form>
	    	<div id="datagrid1" class="nui-datagrid"  style="width: 100%;height: 100%;"
	    		multiSelect="true" showPager="false">
		        <div property="columns">            
		            <div field="resp_center_no" width="10" headerAlign="center" align="center">责任中心号</div>                
		            <div field="customername" width="17" headerAlign="center" align="center">客户名称</div>                
		            <div field="customercode" width="10" headerAlign="center" align="center">客户号</div>                
		            <div field="payroll" width="8" headerAlign="center" align="center">代发薪</div>            
		            <div field="setcard" width="8" headerAlign="center" align="center">结算卡</div> 
		            <div field="sms" width="8" headerAlign="center" align="center">短信通</div> 
		            <div field="return_box" width="8" headerAlign="center" align="center">回单箱</div>
		            <div field="cyber_bank" width="8" headerAlign="center" align="center">网银</div>
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
		//$G.getbyName("id").setValue(infos.id);
		$G.getbyName("customercode").setValue(infos.customercode);
		$G.getbyName("orgnum").setValue(infos.orgnum);
		var url="<%=root%>/zxMyCust/productdetail?";
		grid.setUrl(url);
		grid.load(form.getData());
	}
 </script>