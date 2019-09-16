<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>商机转化</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="商机转化" style="width:100%;height:65px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">客户号：</th>
							<td align="left">
								<input id="customercode" name="customercode" class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">客户名称：</th>
							<td align="left">
								<input id="customername" name="customername" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
		            	<td style="white-space:nowrap; text-align: left;">
							<span id="dataDate" style="color: white;"></span>
						</td>
		                <td style="white-space:nowrap;text-align: center;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>
		                	<span class="separator"></span>
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                    <!-- <span class="separator"></span>
		                    <a class="nui-button" iconCls="icon-download" onclick="download()">导出</a> -->
		                     <span class="separator"></span>
		                    <a class="nui-button" iconCls="icon-ok" onclick="onSave()">营销反馈提交</a>
		                </td>
		            </tr>
		       	</table>
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/zxtask/findReachCustomer" style="width: 100%;height: 100%;"
		    		multiSelect="true" onrowdblclick="custDetail"  allowCellEdit="true" allowCellSelect="true" >
			        <div property="columns">
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false">id</div>
			            <div field="customercode" visible="false">customercode</div>
			            <div field="clas_potential" visible="false">clas_potential</div>
			            <div field="marketing_stat" visible="false">marketing_stat</div>
			            <div field="dummy_cust_no" width="60" allowSort="true" headerAlign="center" align="right">虚拟客户号</div>
			            <div field="customername" width="140" allowSort="true" headerAlign="center" align="left">客户名称</div>
			            <div field="relate_cust_name" width="100" allowSort="true" headerAlign="center" align="left">关联因素</div>
			            <div field="user_name" width="60" allowSort="true" headerAlign="center" align="left">关联客户经理</div>
			            <div field="phone" width="100" allowSort="true" headerAlign="center" align="right">关联客户经理联系方式</div>
			            <div field="daynum" width="60" allowSort="true" headerAlign="center" align="right" renderer="onRenderDayNum">剩余时间(天)</div>
			            <!-- <div field="mark_result" width="100" allowSort="true" headerAlign="center" align="center">营销结果</div> -->
			            <div name="mark_result" field="mark_result" width="120" headerAlign="center" align="left">营销反馈
							<input property="editor" class="mini-dictcombobox" style="width:100%;font-color:red" 
							textField="name" valueField="name" url="<%=root%>/zxparam/getCombox?key=MARKETING_STAT&val=1&flag=0"/>
						</div>
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
	var date = new Date();
	date.setDate(date.getDate() - 1);
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	if(parseInt(month) < 10){
		month = "0" + month;
	}
	var day = date.getDate();
	if(parseInt(day) < 10){
		day = "0" + day;
	}
	$("#dataDate").html("数据日期: " + year+ month + day);
	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}
	
	function onSave(){
		var rows = grid.getSelecteds();
		var marketList =  $G.encode(rows);
		if (rows.length > 0) {
			$.ajax({
	            url: "<%=root%>/zxtask/saveMarketResult", 
	            data: {marketList : marketList },//附加的额外参数
	            dataType: 'json',//返回值类型 一般设置为json
	            success: function (data){
	            	//服务器成功响应处理函数
	           		$G.alert("营销反馈提交成功!","提示");
	           		grid.reload();
	            },
	            error: function (data, status, e){
	            	$G.alert("营销反馈提交失败!","提示");
	            }
	        });
		} else {
			alert("请选择需要反馈的记录！");
		}
	}
	
	function custDetail(e){
		var row = e.row;
        if (row) {
        	var id = row.id;
        	var customercode = row.customercode;
        	//var clas_potential = row.clas_potential;
        	/* if(null != customercode && "" != customercode){
        		if("A3" == clas_potential || "B3" == clas_potential || "C3" == clas_potential 
        				|| "D3" == clas_potential || "E3" == clas_potential){
        			//我行潜力客户
        			toCustDetail("3", id);
        		}else{
        			//我行客户
        			toCustDetail("2", id);
        		}
        	}else{
        		//他行客户
        		toCustDetail("1", id);
        	} */
        	
        	if(null != customercode && "" != customercode){
        		//我行客户
    			toCustDetail("2", id);
        	}else{
        		//他行客户
        		toCustDetail("1", id);
        	}
        }else{
           alert("请选中一条记录");
        }
	}
	
	function onRenderDayNum(e){
		var val = e.value;
		if(val <= 0){
			e.rowStyle = "color:red;";
			return "已过期";
		}else{
			return val;
		}
	}
	
	//打开客户详情
	function toCustDetail(custType, id) {
		var bizParams = {id: id};
		var url="";
		var title = "";
		if("1" == custType){
			//他行
			url="<%=root%>/zxtask/custDetail";
			title = "他行客户";
		}else if("2" == custType){
			//我行
			url="<%=root%>/zxtask/mineCustDetail";
			title = "我行客户";
		}else{
			//我行潜力
			url="<%=root%>/zxtask/qlCustDetail";
			title = "我行潜力客户";
		}
		
		$G.showmodaldialog(title, url, 1000, 800, bizParams, function(action){
       		grid.reload();
		});
	}
	
	//根据条件下载指定数据文件
	function download(){
		var code = nui.getbyName("customercode").getValue();
		var name = nui.getbyName("customername").getValue();
		window.location="<%=root%>/zxtask/downloadReach?code="+ code + "&name="+ name;
	}
	
</script>