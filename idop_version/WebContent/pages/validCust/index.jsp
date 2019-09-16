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
		   			<input id="orgNum" name="orgNum" class="nui-hidden" />
		   			<input id="deptLevel" name="deptLevel" class="nui-hidden" />
		   			<input id="month" name="month" class="nui-hidden" />
		   			<input id="lastMonth" name="lastMonth" class="nui-hidden" />
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">客户名称：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">客户号：</th>
							<td align="left">
								<input id="cust_no" name="cust_no" class="mini-textbox" style="width:165px;"/>
							</td>
							<th align="right">PA日期：</th>
							<td align="left">
								<input id="pa_date" name="pa_date" class="mini-combobox" textfield="month" valuefield="data_date" 
								url="<%=root%>/validCust/validCustCountDate" showNullItem="true" style="width:165px;"/>
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
							<span >PA数据日期：</span>
							<span id="newDate"></span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span ><font color="yellow">单位：万元</></span>
		                </td>
						<td style="width:75%;" align="center">
							<a class="mini-button" iconCls="icon-reload" onclick="reload()" >刷新</a>
							<span class="separator"></span>
							<a class="mini-button" iconCls="icon-search" onclick="search()" >查询</a>
							<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-reload" onclick="reset()" >重置</a>
          	     			<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-download" onclick="downClasSummary()" >导出统计表</a>
		                	<!-- 
		                	<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-download" onclick="downdetail()" >导出明细表</a> -->
                			<span class="separator"></span>
                 			<a class="mini-button"   iconCls="icon-download" onclick="downTask('2')" >导出明细</a>
		                </td>
		                 <td style="white-space:nowrap; text-align: right;">
		                	<a class="nui-button" iconCls="icon-undo" style="display: none;" id="goback" onclick="goback()">返回上层</a>
						</td>
		            </tr>
		       	</table>   
			</div>
				<div id="datagrid1" class="nui-datagrid" url="<%=root%>/validCust/getValidCustCountList" allowHeaderWrap="true"
		    		multiSelect="false" showPager="false" onRowClick="onRowClick" onrowdblclick="onRowdblclick" style="width:100%;height:45%;">
			        <div property="columns">
			        	<div headerAlign="center" width="40" type="indexcolumn"   align="right">序号</div> 
			        	<div field="orgname" width="140" headerAlign="center" allowSort="true"  align="left">机构名称</div>
			        	<div header="年初客户数" headerAlign="center">
			        		<div property="columns">
					            <div field="base_count" width="100" headerAlign="center" allowSort="true"  align="right">全部客户（基础客户）</div>                
					            <div field="year_valid_count" width="80" allowSort="true" headerAlign="center" align="right">有效客户</div> 
					            <div field="year_pa" width="100" headerAlign="center" allowSort="true"  align="right">其中，PA大于5000</div>                
					            <div field="year_ave_count" width="100" allowSort="true" headerAlign="center" align="right">其中，日均存款大于20万</div>
			            	</div>
			            </div>
			        	<div header="当月客户数" headerAlign="center">
			        		<div property="columns">
					            <div field="month_count"  headerAlign="center" allowSort="true"  align="right">全部客户（基础客户）</div>                
					            <div field="month_valid_count" allowSort="true" headerAlign="center" align="right">有效客户</div> 
					            <div field="month_pa" headerAlign="center" allowSort="true"  align="right">其中，PA大于5000</div>                
					            <div field="month_ave_count" allowSort="true" headerAlign="center" align="right">其中，日均存款大于20万</div>
			            	</div>
			            </div>
			        	<div header="当月客户数较年初" headerAlign="center">
			        		<div property="columns">
			        			<div header="全部客户（基础客户）" headerAlign="center">
				        			<div property="columns">
					        			<div field="month_up"  headerAlign="center" allowSort="true"  align="right">增量</div>                
							            <div field="month_amp" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			        			<div header="有效客户" headerAlign="center">
				        			<div property="columns">
					        			<div field="month_valid_up"  headerAlign="center" allowSort="true"  align="right">增量</div>                
							            <div field="month_valid_amp" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			        			<div header="其中，PA大于5000" headerAlign="center">
				        			<div property="columns">
					        			<div field="month_pa_up"  headerAlign="center" allowSort="true"  align="right">增量</div>                
							            <div field="month_pa_amp" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			        			<div header="其中，日均存款大于20万" headerAlign="center">
				        			<div property="columns">
					        			<div field="month_ave_up"  headerAlign="center" allowSort="true"  align="right">增量</div>                
							            <div field="month_ave_amp" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			            	</div>
			            </div>
			        	<div header="当月客户数较上月末" headerAlign="center">
			        		<div property="columns">
			        			<div header="全部客户（基础客户）" headerAlign="center">
				        			<div property="columns">
					        			<div field="last_month_up"  headerAlign="center" allowSort="true"  align="right">增量</div>                
							            <div field="last_month_amp" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			        			<div header="有效客户" headerAlign="center">
				        			<div property="columns">
					        			<div field="last_month_valid_up"  headerAlign="center" allowSort="true"  align="right">增量</div>                
							            <div field="last_month_valid_amp" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			        			<div header="其中，PA大于5000" headerAlign="center">
				        			<div property="columns">
					        			<div field="last_month_pa_up"  headerAlign="center" allowSort="true"  align="right">增量</div>                
							            <div field="last_month_pa_amp" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			        			<div header="其中，日均存款大于20万" headerAlign="center">
				        			<div property="columns">
					        			<div field="last_month_ave_up"  headerAlign="center" allowSort="true"  align="right">增量</div>                
							            <div field="last_month_ave_amp" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div> 
				        			</div>
				        		</div>
			            	</div>
			            </div>
			            <div field="deptlevel" headerAlign="center" allowSort="true" visible="false">deptlevel</div>
			        </div>
				</div>
		    	<div id="datagrid2" class="nui-datagrid" sortMode="client" url="<%=root%>/validCust/getCustList" style="width: 100%;height: 40%;"
		    		multiSelect="true" >
			        <div property="columns">
			        	<!-- <div type="checkcolumn" name="checkCloumn" width="3"></div> -->
			        	<div headerAlign="center" width="3" type="indexcolumn" align="right">序号</div>
			            <div field="orgname" width="15" headerAlign="center" allowSort="true"  align="left">责任中心名称</div>                
			            <div field="name" width="15" allowSort="true" headerAlign="center" align="left">客户名称</div>        
			            <div field="cust_no" width="10" allowSort="true" headerAlign="center" align="right">客户号</div> 
			            <div field="valid_cust" width="10" allowSort="true" headerAlign="center" align="left" renderer="onRender">是否有效户</div> 
			            <div field="ck_nrj_zcy" width="10" allowSort="true" headerAlign="center" align="right">存款日均</div> 
			            <div field="bn_fic_nrj" width="10" allowSort="true" headerAlign="center" align="right">表内理财日均</div> 
			            <div field="account_contrib_before_tax" width="10" allowSort="true" headerAlign="center" align="right">税费前利润</div> 
			        </div>
				</div>
				</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	
	var month = "${month}";
	var lastMonth = "${lastMonth}";
	$G.getbyName("month").setValue(month);
	$G.getbyName("lastMonth").setValue(lastMonth);
	$("#newDate").html(month);
	var grid1 =$G.get("datagrid1");
	var grid2 =$G.get("datagrid2");
	var form = $G.getForm("form1");
	var ORGID = "";
	findUserOrg();

	function reload(){
		reset();
		//pageInit();
	}
	
	//页面数据初始化
	function pageInit(){
		firstSel(1);
	}
	
	//页面初始化时默认选中第一行
	function firstSel(initFlag){
		if(initFlag==1){
			grid1.on("load",function(){
				if(initFlag==1){
					grid1.select(0);
					onRowClick();
				}
			});
		}
		grid1.load();
	}
	
	//查询当前用户能查询几级机构数据，返回相对应的机构id
	function findUserOrg(){
		var url = "<%=root%>/kpi/findUserOrg";
		$.ajax({
			url: url,
	        success: function (text) {
	        	if(null != text.orgId && "" != text.orgId){
	        		ORGID = text.orgId;
	        		nui.get("orgNum").setValue(text.orgId);
	        		var data = form.getData();
	        		grid1.load(data);
	        		grid2.load(data);
	        	}
	        }
		});
	}
	
	//页面初始化时默认选中第一行
	function firstSel(initFlag){
		
		if(initFlag==1){
			grid1.on("load",function(){
				if(initFlag==1){
					grid1.select(0);//
					onRowClick();
				}
			});
		}
		grid1.load();
	}
	
	//分层列表行点击事件
	function onRowClick(e){
		var row = e.row;
        if (row) {
        	var id = row.id;
        	var deptLevel = row.deptlevel;
        	if(undefined != id && null != id){
        		nui.get("orgNum").setValue(id);
        		nui.get("deptLevel").setValue(deptLevel);
        		var data = form.getData();
        		grid2.load(data);
    			$("#goback").show();
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
        		nui.get("orgNum").setValue(id);
        		var data = form.getData();
        		grid1.load(data);
    			$("#goback").show();
        	}
        }else{
        	alert("请选中一条记录");
        }
	}
	
	function onRender(e){
		var val = e.value;
		if (val == "1") {
			return "是";
		}else{
			return "否";
		}
	}
	
	//下载层及汇总
	function downClasSummary(){
		var month = nui.get("month").getValue();
		var pa_date = nui.get("pa_date").getValue();
		window.location="<%=root%>/validCust/downloadValidCustCount?month=" + month + "&pa_date=" + pa_date;
	}
	
	//下载明细
	function downdetail(){
		var cust_no=$G.getbyName("cust_no").getValue();
		var name=$G.getbyName("name").getValue();
		var type_click=$G.getbyName("type_click").getValue();
		var clas_five=$G.getbyName("clas_five").getValue();
		var clas_click=$G.getbyName("clas_click").getValue();
		var flag=$G.getbyName("flag").getValue();
		window.location="<%=root%>/zxMyCust/downdetail?cust_no=" + cust_no 
				+ "&name=" + name + "&type_click=" + type_click + "&clas_five=" 
				+ clas_five+ "&flag=" + flag + "&clas_click=" + clas_click;   
	}

	//查询
	function search(){
		grid1.load(form.getData());
		var paDate=nui.get("pa_date").getValue();
		if (paDate !=null && paDate != '') {
			$("#newDate").html("");
			$("#newDate").html(paDate);
		}
		grid2.load(form.getData());
		//firstSel();
	}

	//重置
	function reset(){
		//form.reset();
		$G.getbyName("name").setValue("");
		$G.getbyName("cust_no").setValue("");
		$G.get("pa_date").setValue("");
	}
	
	function custDetail(e){
		var row = e.row;
        if (row) {
        	var id = row.id;
        	var customercode = row.customercode;
        	var clas_potential = row.clas_potential;
        	if(null != customercode && "" != customercode){
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
        	}
        }else{
           alert("请选中一条记录");
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
	
	//返回上层
	function goback(){
		var orgnum = nui.get("orgNum").getValue();
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
	        		nui.get("orgNum").setValue(parentorgnum);
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

