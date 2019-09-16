<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
   * 重要物品操作记录
   *
   * @author zhaomiao621 
   * Created on 2017-11-28
-->
<head>
    <title>登记簿操作记录</title> 

</head>
<body >  
<style type="text/css">
	body {
		margin: 0;
		padding: 0;
		border: 0;
		width: 100%;
		height: 100%;   
		overflow: hidden;
	}
</style>
<div style="border:0px;">
 <div  class="mini-toolbar" style="padding:5px;">
	<form id="form1" method="post">
        
        <input name="pageIndex" class="mini-hidden" value="0"/>
        <input name="pageSize"  class="mini-hidden" value="10"/>
    </form>  
    </div>
    </div>
	<div class="mini-fit">
    <div id="datagrid1" dataField="datas" class="mini-datagrid" style="width:100%;height:95%;" sortMode="client" allowUnselect="false"
				    url="<%=root %>/djbHandler/getApproverjl?sjid=${sjid}" showEmptyText="true" emptyText="没有对应的记录">
        <div property="columns">                    
			<div headerAlign="center" type="indexcolumn" width="50">序号</div> 
			<div field="id" visible="false" ></div>
            <div field="approver"  allowSort="true" headerAlign="center" align="center" >审批人</div>
            <div field="name"  allowSort="true" headerAlign="center" renderer="onJsrxmRender" align="center">审批人角色</div>
            <div field="status"  allowSort="true" headerAlign="center" renderer="onYjrxmRender" align="center">审批结果</div>
            <div field="apptime"  allowSort="true" headerAlign="center" align="center" dateFormat="yyyy-MM-dd HH:mm:ss" >审批时间</div>
            <div field="orgnum"  allowSort="true" headerAlign="center" renderer="onYjrxmRender" align="center">审批人所属机构</div>
        </div>
    </div> 
    </div>
    
    <iframe  id="callbackIframe" name="callbackIframe" style="display:none;"></iframe>
<script type="text/javascript">
	mini.parse();	//将页面的标签转换为可miniui可处理的对象
	var grid = mini.get("datagrid1");	//获取datagrid1标签
    grid.load();
	
	function setData(data){
		
	}
	
	//页面表单提交后，后台返回时调用的函数
	function callback(msg) {
		mini.alert(msg);

	}
</script>
</body>
</html>
