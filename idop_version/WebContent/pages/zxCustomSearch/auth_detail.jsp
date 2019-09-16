<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  			.mini-grid-newRow {
			    background: none;
			}
  		</style>
	</head>
	<body>    
		<div class="nui-fit">
	        <input name="id" class="nui-hidden" />
	       	<div style="padding-top: 20px;">
				<table style="padding:0 50px 40px 50px;color:#ffffff;table-layout:fixed;" align="center">
					<tr>
						<td align="center" colspan="2">
		              	</td>
	              	</tr>
	              	<tr>
						<td align="center" colspan="2">
							<h2>授权详情</h2>
		              	</td>
	              	</tr>
	              	<tr style="height: 260px;">
						<td align="center" colspan="2">
							<div class="nui-fit">
								<div id="datagrid" class="nui-datagrid" 
						    		style="width: 240px;height: 100%;" showpager="false">
							        <div property="columns">
							        	<div field="orgname" width="100" headerAlign="center" align="center">机构名称</div>
							        </div>
								</div>
							</div>
		              	</td>
					</tr>
	              	<tr>
						<td align="center" colspan="2">
							<a class="nui-button" iconCls="icon-cancel" onclick="cancel" style="color: #ffffff;">关闭</a>
		              	</td>
	              	</tr> 
				</table>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid");
	
	//标准方法接口定义
	function setData(data) {
		var infos = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
        var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	grid.setData(text.reList);
		});
		$G.postByAjax({base_id:infos.base_id},"<%=root%>/zxCustomSearch/getAuthOrgs",ajaxConf);
	}
	
	function cancel(){
 		$G.closemodaldialog("ok");
 	}
	
</script>