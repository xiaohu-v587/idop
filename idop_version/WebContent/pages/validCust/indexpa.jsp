<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>有效户统计</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/echarts/echarts.min.js"></script>
  		<%@include file="/common/nuires.jsp" %>
	</head> 
	<body>
	<div class="nui-fit">
		<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;height:10%;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
		   			<input id="org_id" name="org_id" class="nui-hidden" />
		   			<input id="deptLevel" name="deptLevel" class="nui-hidden" />
		   			<input id="month" name="month" class="nui-hidden" />
		   			<input id="lastMonth" name="lastMonth" class="nui-hidden" />
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">客户名称：</th>
							<td align="left">
								<input id="cust_name" name="cust_name" class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">客户号：</th>
							<td align="left">
								<input id="cust_no" name="cust_no" class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">PA日期：</th>
							<td align="left">
								<input id="pa_date" name="pa_date" class="mini-combobox" textfield="month" valuefield="data_date" 
								url="<%=root%>/validCust/validCustPaDate" showNullItem="true" style="width:165px;"/>
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 28px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
		            	<td style="width:25%;color:#FFFFFF" align="center">  
							<span >日期：</span>
							<span id="dataDate" style="color: white;"></span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span ><font color="yellow">单位：万元</></span>
		                </td>
						<td style="width:90%;" align="center">
							<a class="mini-button" iconCls="icon-search" onclick="search()" >查询</a>
							<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-reload" onclick="reset()" >重置</a>
          	     			<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-download" onclick="downClasSummary()" >导出层级汇总</a>
		                	<!-- <span class="separator"></span>
                			<a class="mini-button" iconCls="icon-download" onclick="downdetail()" >导出明细</a> -->
                			<span class="separator"></span>
                 			<a class="mini-button"   iconCls="icon-download" onclick="downTask('1')" >导出明细</a>
		                </td>
		                <td style="white-space:nowrap; text-align: right;">
		                	<a class="nui-button" iconCls="icon-undo" style="display: none;" id="goback" onclick="goback()">返回上层</a>
						</td>
		            </tr>
		       	</table>   
			</div>
				<div id="datagrid1" class="nui-datagrid" url="<%=root%>/validCust/findValidCustNumByOrgId" allowHeaderWrap="true"
		    		multiSelect="false" showPager="false" onrowdblclick="onRowdblclick" 
		    		onRowClick="onRowClick" style="width:100%;height:45%;">
			        <div property="columns">
			        	<div headerAlign="center" width="40" type="indexcolumn" align="right">序号</div>
			        	<div field="id" visible="false">机构id</div>
			        	<div field="orgname" width="140" headerAlign="center" allowSort="true" align="left">机构名称</div>
			        	<div header="年初客户数" headerAlign="center">
			        		<div property="columns">
					            <div field="lastyearsumnum"  headerAlign="center" allowSort="true"  align="right">全部客户（基础客户）</div>
					            <div field="lastyearnum" allowSort="true" headerAlign="center" align="right">PA不为零客户数</div> 
			            	</div>
			            </div>
			        	<div header="当前月客户数" headerAlign="center">
			        		<div property="columns">
					            <div field="monthsumnum"  headerAlign="center" allowSort="true"  align="right">全部客户（基础客户）</div>
					            <div field="monthnum" allowSort="true" headerAlign="center" align="right">PA不为零客户数</div> 
			            	</div>
			            </div>
			        	<div header="当前月客户数较年初" headerAlign="center">
			        		<div property="columns">
			        			<div header="全部客户（基础客户）" headerAlign="center">
				        			<div property="columns">
					        			<div field="incyearsumnum"  headerAlign="center" allowSort="true"  align="right">增量</div>
							            <div field="ampliyearsumnum" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			        			<div header="PA不为零客户数" headerAlign="center">
				        			<div property="columns">
					        			<div field="incyearnum"  headerAlign="center" allowSort="true"  align="right">增量</div>   
							            <div field="ampliyearnum" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			            	</div>
			            </div>
			        	<div header="当前月客户数较上月末" headerAlign="center">
			        		<div property="columns">
			        			<div header="全部客户（基础客户）" headerAlign="center">
				        			<div property="columns">
					        			<div field="incmonthsumnum"  headerAlign="center" allowSort="true"  align="right">增量</div>
							            <div field="amplilastmonthsumnum" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			        			<div header="PA不为零客户数" headerAlign="center">
				        			<div property="columns">
					        			<div field="incmonthnum"  headerAlign="center" allowSort="true"  align="right">增量</div> 
					        			<div field="amplilastmonthnum" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div>
				        			</div>
				        		</div>
			            	</div>
			            </div>
			        </div>
				</div>
		    	<div id="datagrid2" class="nui-datagrid" sortMode="client" url="<%=root%>/validCust/findValidCustInfoByOrgId" style="width: 100%;height: 40%;"
		    		multiSelect="true" >
			        <div property="columns">
			        	<div headerAlign="center" width="3" type="indexcolumn" align="right">序号</div>
			            <div field="orgname" width="20" headerAlign="center" allowSort="true"  align="left">责任中心名称</div>                
			            <div field="cust_name" width="20" allowSort="true" headerAlign="center" align="left">客户名称</div>        
			            <div field="cust_no" width="10" allowSort="true" headerAlign="center" align="right">客户号</div> 
			            <div field="account_contrib_before_tax" width="10" allowSort="true" headerAlign="center" align="right">税费前利润</div> 
			        </div>
				</div>
				</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid1 =$G.get("datagrid1");
	var grid2 =$G.get("datagrid2");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	var ORGID = "";
	var month = "${month}";
	$("#dataDate").html(month);
	nui.get("month").setValue(month);
	var lastMonth = "${lastMonth}";
	nui.get("lastMonth").setValue(lastMonth);
	findUserOrg();
	
	//分层列表行点击事件
	function onRowClick(e){
		var row = e.row;
        if (row) {
        	var id = row.id;
        	if(undefined != id && null != id){
        		nui.get("org_id").setValue(id);
        		var data = form.getData();
        		grid2.load(data);
        	}
        }else{
        	alert("请选中一条记录");
        }
	}
	
	function onRowdblclick(e){
		var row = e.row;
        if (row) {
        	var id = row.id;
        	if(undefined != id && null != id){
        		nui.get("org_id").setValue(id);
        		var data = form.getData();
        		grid1.load(data);
    			$("#goback").show();
        	}
        }else{
        	alert("请选中一条记录");
        }
	}
	
	//下载层及汇总
	function downClasSummary(){
		var month = nui.get("month").getValue();
		var pa_date = nui.get("pa_date").getValue();
		window.location="<%=root%>/validCust/downloadOrgPaNotZero?month=" + month + "&pa_date=" + pa_date;
	}

	//查询
	function search(){
		grid1.load(form.getData());
		var paDate=nui.get("pa_date").getValue();
		if (paDate !=null && paDate != '') {
			$("#dataDate").html("");
			$("#dataDate").html(paDate);
		}
		grid2.load(form.getData());
	}

	//重置
	function reset(){
		$G.getbyName("cust_name").setValue("");
		$G.getbyName("cust_no").setValue("");
		$G.get("pa_date").setValue("");
	}

	//查询当前用户能查询几级机构数据，返回相对应的机构id
	function findUserOrg(){
		var url = "<%=root%>/kpi/findUserOrg";
		$.ajax({
			url: url,
	        success: function (text) {
	        	if(null != text.orgId && "" != text.orgId){
	        		ORGID = text.orgId;
	        		nui.get("org_id").setValue(text.orgId);
	        		var data = form.getData();
	        		grid1.load(data);
	        		grid2.load(data);
	        	}
	        }
		});
	}
	
	//返回上层
	function goback(){
		var orgnum = nui.get("org_id").getValue();
		var url = "<%=root%>/kpi/findParentOrgNum";
		$.ajax({
			url: url,
			data: {orgnum: orgnum},
	        success: function (text) {
	        	var parentorgnum = "";
	        	if(null != text.orgnum){
	        		parentorgnum = text.orgnum;
	        	}
	        	if(ORGID == parentorgnum){
	        		$("#goback").hide();
	        	}else{
	        		$("#goback").show();
	        	}
	        	if("" != parentorgnum && parentorgnum != orgnum){
	        		nui.get("org_id").setValue(parentorgnum);
	        		var data = form.getData();
	        		grid1.load(data);
	    			grid2.load(data);
	        	}
	        }
		});
	}
	
	function onRenderAmpli(e){
		return ((e.value) * 100).toFixed(2) + "%";
	}
	
	function downTask(flg){
		var url = "<%=root%>/validCust/downTask";
		$.ajax({
			url: url,
			async: false, 
			data: {'upOrg': ORGID,flg:flg},
	        success: function (text) {
	        	if("T"==text.msg){
	        		$G.GcdsConfirm("创建下载任务成功，是否跳转下载任务列表？", "页面跳转提示", function(action) {
						if (action == 'ok') {
							var url ="<%=root%>/zxindex/findMenuInfoByUrl";
							$.ajax({
								url: url,
								async: false, 
								data: {'type': "downTask"},
						        success: function (text) {
						        	if(null != text.node && undefined != text.node){
						        		window.top.window.showTab(text.node);
						        	}
						        }
							});
		              	}
		            });
	        	}else{
	        		$G.alert("创建下载任务失败！");
	        	}
	        }
		});
	}
</script>

