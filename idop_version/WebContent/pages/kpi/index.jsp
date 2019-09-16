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
		<div id="panel1" class="nui-panel" title="KPI查询" style="width:100%;height:111px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">机构：</th>
							<td align="left">
								<input id="orgSelect" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
									name="org_id" textfield="orgname" valuefield="id" parentfield="upid"  
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
									allowInput="false" showClose="true" oncloseclick="onCloseClick" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" style="width:165px;"/>
							</td>
		                    <th align="right">客户经理：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
	               		<tr>
	               			<th align="right">EHR号：</th>
							<td align="left">
								<input id="cust_no" name="cust_no" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right">期数：</th>
							<td align="left">
								<input id="date" name="date"  class="nui-datepicker" allowInput="false" style="width:165px;"/>
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
          	     			<a class="mini-button" iconCls="icon-download" onclick="download()" plain="true">下载</a>
          	     			<a class="mini-button" iconCls="icon-node" onclick="detail1()" plain="true">详情</a>
          	     			<a class="mini-button" iconCls="icon-node" onclick="test()" plain="true">KPI测试</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/kpi/listKpi" style="width: 100%;height: 100%;"
		    		multiSelect="true">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div headerAlign="center" width="5%" type="indexcolumn">序号</div>
			            <div field="period" width="10%" headerAlign="center" allowSort="true"  align="right">期次</div> 
			            <div field="name" width="13%" headerAlign="center" allowSort="true"  align="left">客户经理</div>                
			            <div field="orgname" width="15%" allowSort="true" headerAlign="center" align="left">所属机构</div>            
			            <div field="cust_mgr_no" width="10%" allowSort="true" headerAlign="center" align="right">EHR</div> 
			            <div field="cur_post" width="10%" allowSort="true" headerAlign="center" align="left">岗位</div> 
			            <div field="mgr_level" width="10%" allowSort="true" headerAlign="center" align="left">级别</div> 
			            <div field="mgr_prof_level" width="12%" allowSort="true" headerAlign="center" align="right" >客户经理专业资格等级</div>
			            <div field="kpi" width="15%" allowSort="true" headerAlign="center" align="right" >KPI值</div>
			            <div field="trans_standard" width="15%" allowSort="true" headerAlign="center" align="right" >平移标准</div>
			            <div field="up_standard" width="15%" allowSort="true" headerAlign="center" align="right" >晋升标准</div>
			            <div field="next_level_val" width="15%" allowSort="true" headerAlign="center" align="right" >下一等级中位值</div>
			            <div field="same_level_sort" width="15%" allowSort="true" headerAlign="center" align="left" >同等级排名</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();

	var grid =$G.get("datagrid1");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	
	grid.load();

	//新增
	function detail1() {
		var row = grid.getSelected();
		var url = null;
		if (row) {
			if("1"==row.user_type||"2"==row.user_type||"4"==row.user_type){
				url = "<%=root%>/kpi/detail";
			}else if("3"==row.user_type){
				url = "<%=root%>/kpi/detail_acc_cust";
			}else if("5"==row.user_type){
				url = "<%=root%>/kpi/detail_admi_cust?";
			}
			var bizParams = {cust_mgr_no:row.cust_mgr_no,period:row.period};
	        $G.showmodaldialog("KPI贡献", url, 800, 500, bizParams, function(action){
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
		var org_id=$G.getbyName("org_id").getValue();
		var date=$G.getbyName("date").getValue();
		window.location="<%=root%>/kpi/download?cust_no=" + cust_no 
				+ "&name=" + name + "&org_id=" + org_id + "&date=" + date;   
	}
	//下载
	function test(){
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
		});
		$G.postByAjax({},"<%=root%>/kpi/kpiJob",ajaxConf);
		
	}

	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }

	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}

</script>

