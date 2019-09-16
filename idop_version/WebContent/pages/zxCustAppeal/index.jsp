<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>客户申诉</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
		   			<input id="orgId" name="orgId" class="mini-hidden"/>
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">客户号/虚拟客户号：</th>
							<td align="left">
								<input id="cust_no" name="cust_no" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right">客户名称：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
<!-- 		                    <th align="right">客户经理：</th> -->
<!-- 							<td align="left"> -->
<!-- 								<input id="cust_mgr" name="cust_mgr" class="mini-textbox" style="width:165px;"/> -->
<!-- 							</td> -->
							<th align="right">申诉状态：</th>
							<td align="left">
								<input id="appeal_stat" name="appeal_stat" class="mini-combobox" style="width:165px;"
								url="<%=root%>/zxparam/getDict?key=appeal_stat" textfield="name" valuefield="val"
									allowInput="false" showNullItem="true" nullItemText="请选择" emptyText="请选择"/>
							</td>
	               		</tr>
	               	<!-- 	<tr>
 		                   	<th align="right">申诉状态：</th>
							<td align="left"> 
 								<input id="appeal_stat" name="appeal_stat" class="mini-combobox" style="width:165px;" 
								url="<%=root%>/zxparam/getDict?key=appeal_stat" textfield="name" valuefield="val"
 									allowInput="false" showNullItem="true" nullItemText="请选择" emptyText="请选择"/> 
 							</td> 
							<th align="right">.</th>
							<td align="left">
							</td>
		                   	<th align="right"></th>
		                    <td align="left">    
		                    </td>
		                    <th align="right"></th>
		                    <td align="left">    
		                    </td>
	               		</tr> -->
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
		            	<td style="width:10%;color:#FFFFFF" align="center">  
							<span >日期：</span>
							<span id="newDate"></span>
		                </td>
						<td style="width:90%;" align="center">
							<a class="mini-button" iconCls="icon-search" onclick="search()" >查询</a>
							<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-reload" onclick="reset()" >重置</a>
          	     			<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-download" onclick="download()" >导出</a>
		                	<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-edit" onclick="reject()" >驳回</a>
                			<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-edit" onclick="compuDistri()" >申诉处理</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid" class="nui-datagrid" sortMode="client" url="<%=root%>/zxCustAppeal/findCustList" style="width: 100%;height: 100%;"
		    		multiSelect="false">
			        <div property="columns">
			        	<div type="checkcolumn" name="checkCloumn" width="4"></div>
			        	<div headerAlign="center" width="4" type="indexcolumn">序号</div>  
			            <div field="cust_no" width="8" allowSort="true" headerAlign="center" align="right">客户号/虚拟客户号</div>
			            <div field="name" width="15" allowSort="true" headerAlign="center" align="left">客户名称</div>
			            <div field="appeal_per_name" width="8" allowSort="true" headerAlign="center" align="left">申诉人</div>
			            <div field="mobile" width="8" allowSort="true" headerAlign="center" align="left">申诉人联系方式</div>
			            <div field="appeal_reas" width="20" allowSort="true" headerAlign="center" align="left">申请方案</div>
			            <div field="appeal_stat_cn" width="8" allowSort="true" headerAlign="center" align="left">申诉状态</div>
			            <div field="appeal_result" width="10" allowSort="true" headerAlign="center" align="left">处理结果</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var orgId = "${orgId}";
	$G.getbyName("orgId").setValue(orgId);
	$G.getbyName("appeal_stat").setValue("1");
	var grid =$G.get("datagrid");
	var form = $G.getForm("form1");
	grid.load(form.getData());
	document.getElementById("newDate").innerHTML= findNewDate();

	function reject() {
		var row = grid.getSelected();
		if (row) {
			if(row.appeal_stat=="2"){
				$G.alert("此客户申诉已处理结束，不可再次操作！");
				return;
			}
		    var ajaxConf = new GcdsAjaxConf();
		    ajaxConf.setIsShowProcessBar(false);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
	    		$G.alert("操作成功");
	    		grid.reload();
			});
		    $G.postByAjax({appealid:row.appealid},"<%=root%>/zxCustAppeal/reject",ajaxConf);
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	function compuDistri() {
		var row = grid.getSelected();
		if (row) {
			if(row.appeal_stat=="2"){
				$G.alert("此客户申诉已处理结束，不可再次操作！");
				return;
			}
			var url = "<%=root%>/zxCustAppeal/compuDistri";
			var bizParams = {id:row.id,appealid:row.appealid};
	        $G.showmodaldialog("客户分配", url, 600, 500, bizParams, function(action){
		    	 grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}

	//下载
	function download(){
		var cust_no=$G.getbyName("cust_no").getValue();
		var name=$G.getbyName("name").getValue();
		var orgId=$G.getbyName("orgId").getValue();
		var appeal_stat=$G.getbyName("appeal_stat").getValue();
		window.location="<%=root%>/zxCustAppeal/download?cust_no=" + cust_no 
				+ "&name=" + name + "&orgId=" + orgId + "&appeal_stat=" + appeal_stat;   
	}
	
	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }

	//查询
	function search(){
		grid.load(form.getData());
	}

	//重置
	function reset(){
		form.reset();
		$G.getbyName("orgId").setValue(orgId);
		$G.getbyName("appeal_stat").setValue("1");
	}
	
	//查询最新数据时间
	function findNewDate(){
		var newDate;
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsAsync(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setSuccessFunc(function (data) {
			newDate=data.newDate;
		});
		$G.submitForm("form1","<%=root%>/zxCustAppeal/findNewDate",ajaxConf);
		return newDate;
	}

</script>

