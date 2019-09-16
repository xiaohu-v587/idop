<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>对账单查询</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="对账单查询" style="width:100%;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
    		 <div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
	               		<tr>
	               			<th align="right">数据日期起：</th>
							<td align="left">
								<input name="start_time" class="nui-datepicker" required="true"/> 至<input name="end_time" class="nui-datepicker" required="true"/>   
							</td>
		                   	<th align="right">货币：</th>
							<td align="left">
								<input name="currency" class="nui-combobox" textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=CURRENCY" required="true"  
                       			allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择"/>
							</td>
							<th align="right">方向：</th>
							<td align="left">
								<input name="direction" class="nui-combobox" textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=DIRECTION" required="true"  
                       			allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择"/>
							</td>
						</tr>
						<tr>
							<th align="right">交易类型：</th>
							<td align="left">
									<input name="trade_type" class="nui-combobox" textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=TRADE_TYPE" required="true"  
                       			allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择"/>
							</td>
							<th align="right">客户号：</th>
							<td align="left">
								<input name="customercode" class="nui-textbox" required="true"/>   
							</td>
							<th align="right">账户号：</th>
							<td align="left">
								<input name="acc_no" class="nui-textbox" required="true"/>   
							</td>
						</tr>
						<tr>
							<th align="right">交易对手：</th>
							<td align="left">
								<input name="apponent_acc" class="nui-textbox" required="true"/>   
							</td>
							<th align="right">核算码：</th>
							<td align="left">
								<input name="acc_code" class="nui-textbox" required="true"/>   
							</td>
							<th align="right">交易机构：</th>
							<td align="left">
								<input name="trade_org" class="nui-textbox" required="true"/>   
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
          	     			<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;" url="<%=root%>/pccm_acc_search/getList" multiSelect="true">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false"></div>    
			            <div field="acc_no"  headerAlign="center" allowSort="true"  align="right">账号</div> 
			            <div field="acc_code" allowSort="true" headerAlign="center" align="right">核算码</div>
			            <div field="customerid" allowSort="true" headerAlign="center" align="right">客户</div> 
			            <div field="customercode" allowSort="true" headerAlign="center" align="right">客户号</div>                   
			            <div field="trade_org" allowSort="true" headerAlign="center" align="left">交易机构</div>            
			            <div field="curr" allowSort="true" headerAlign="center" align="left" >货币</div> 
			            <div field="trade_money" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny">交易金额(万元)</div>
			            <div field="direct" allowSort="true" headerAlign="center" align="left">方向</div>
			            <div field="apponent_acc" allowSort="true" headerAlign="center" align="right">对手账号</div>
			        	<div field="apponent_name" allowSort="true" headerAlign="center" align="left">对手名称</div>
			        	<div field="tra" allowSort="true" headerAlign="center" align="left">交易类型</div>
			        	<div field="create_time" allowSort="true" headerAlign="center" align="left">数据日期</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid1");
	var form = $G.getForm("form1");
	grid.load();

	//删除
	function del(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
				if (action == 'ok') {
                	var ids = "";
	                for(var index = 0;index < rows.length;index++){
						if(ids == ""){
	                    	ids = rows[index].id;
	                  	} else {
	                    	ids += "," + rows[index].id;
	                  	}
	                }
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/pccm_acc_search/del", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
		}
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
	
	<%--金额万元单位装换&千分位符号格式化处理 --%>
	function formatMoneny(e){
		var str=e.value;
		if(str==""||str==null){
			return "0";
		}
		str=(str/10000).toFixed(2);
		//var re=/(?=(?!(\b))(\d{3})+$)/g;
		//str=str.replace(re,',');
		return str;
	}
</script>

