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
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/modelview/getYjDetailTableList" style="width: 100%;height: 100%;" 
		    		multiSelect="false" allowAlternating="true">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>    
			            <div field="create_time" width="100" headerAlign="center" allowSort="true"  align="right">预警时间</div>                
			            <div field="warning_name" width="100%"  allowSort="true" headerAlign="center" align="left">预警名称</div>            
			            <div field="orgname" width="150" allowSort="true" headerAlign="center" align="left">机构名称</div> 
			            <div field="busi_modulename" width="100" allowSort="true" headerAlign="center" align="center">业务模块</div> 
			            <div field="warning_type_code" width="150" allowSort="true" headerAlign="center" align="center">预警类型</div> 
			            <div field="warning_status" width="80" allowSort="true" headerAlign="center" align="center" renderer="onWarningStatusRender">预警状态</div>
			            <div field="warning_level" width="80" allowSort="true" headerAlign="center" align="center" renderer="onWarningLevelRender">预警等级</div>
			            <div field="last_check_stat" width="80" allowSort="true" headerAlign="center" align="center" renderer="onLastCheckStatRender">核查结果</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();

	var grid =$G.get("datagrid1");
	
	function setData(data){
		grid.load(data);	
	}	
	function onWarningStatusRender(e){
		return $G.getDictText("dop_warning_sta",e.value);
	}
	function onWarningLevelRender(e){
		return $G.getDictText("dop_warning_lvl",e.value);
	}
	function onLastCheckStatRender(e){
		return $G.getDictText("dop_check_stat",e.value);
	}
	
</script>

