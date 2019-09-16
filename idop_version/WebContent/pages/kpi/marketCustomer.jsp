<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>商机转化率</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
  		<script src="<%=request.getContextPath()%>/resource/echarts/echarts.min.js" type="text/javascript"></script>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="商机转化率" style="width:100%;height:65px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
		   			<input class="nui-hidden" name="orgnum" id="orgnum"/>
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
							<th align="right">客户名称：</th>
							<td align="left">
								<input id="custName" name="custName" class="mini-textbox" style="width:165px;"/>
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
		                    <a class="nui-button" iconCls="icon-search" onclick="download()">导出</a> -->
		                    <!-- <span class="separator"></span>
		                    <a class="nui-button" iconCls="icon-search" onclick="calcUserMarketCustomerJob()">执行</a> -->
		                </td>
		                <td style="white-space:nowrap; text-align: right;">
		                	<a class="nui-button" iconCls="icon-undo" style="display: none;" id="goback" onclick="goback()">返回上层</a>
		                	<span class="separator"></span>
							<a class="nui-button" iconCls="icon-reload" id="dataType" style="display: none;" onclick="transSelect">机构</a>
						</td>
		            </tr>
		       	</table>
			</div>
			<div class="gridDiv" style="width: 100%;height: 30%">
		    	<div id="datagrid2" class="nui-datagrid" url="<%=root%>/kpi/findMarketCustomerRank" 
		    		style="width: 60%;height: 100%;float: left;" showpager="false">
			        <div property="columns">
			            <div field="sumnum" width="100" headerAlign="center" align="right">商机客户数</div>
			            <div field="succnum" width="140" headerAlign="center" align="right">转化客户数</div>
			            <div field="reach" width="100" headerAlign="center" align="right" renderer="onRenderReach">商机客户转化率</div>
			            <div field="ranknum" width="100" headerAlign="center" align="right">转化率排名</div>
			        </div>
				</div>
				<div id="datagridOrg2" class="nui-datagrid" url="<%=root%>/kpi/findOrgMarketRank" onrowdblclick="findOrgMarketRankFun" 
		    		style="width: 60%;height: 100%;float: left; display: none;" showpager="false">
			        <div property="columns">
			        	<div field="id" visible="false">id</div>
			        	<div field="by2" visible="false">by2</div>
			        	<div field="name" width="100" headerAlign="center" align="left">名称</div>
			            <div field="sumnum" width="100" headerAlign="center" align="right">商机客户数</div>
			            <div field="succnum" width="140" headerAlign="center" align="right">转化客户数</div>
			            <div field="reach" width="100" headerAlign="center" align="right" renderer="onRenderReach">商机客户转化率</div>
			        </div>
				</div>
				<div style="width: 40%;height: 100%; color: red;float: left;" >
					<div id="pieId" style="width: 90%;height: 100%;float:left;" ></div>
				</div>
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/kpi/findMarketCustomer" style="width: 100%;height: 100%;"
		    		multiSelect="true" onrowdblclick="custDetail" allowCellSelect="false">
			        <div property="columns">
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false">id</div>
			            <div field="customercode" visible="false">customercode</div>
			            <div field="clas_potential" visible="false">clas_potential</div>
			            <div field="dummy_cust_no" width="100" headerAlign="center" align="left">虚拟客户号</div>
			            <div field="customername" width="140" headerAlign="center" align="left">客户名称</div>
			            <div field="relate_cust_name" width="100" headerAlign="center" align="left">关联因素</div>
			            <div field="name" width="100" headerAlign="center" align="right">关联客户经理</div>
			            <div field="phone" width="60" headerAlign="center" align="right">客户经理联系方式</div>
			            <div field="marketing_stat" width="100" headerAlign="center" align="left" renderer="onRenderMarkStat">转化结果</div>
			        </div>
				</div>
				<div id="datagridOrg1" class="nui-datagrid" url="<%=root%>/kpi/findOrgMarketCustomer" style="width: 100%;height: 100%;display: none;"
		    		multiSelect="true" onrowdblclick="custDetail" allowCellSelect="false">
			        <div property="columns">
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false">id</div>
			            <div field="customercode" visible="false">customercode</div>
			            <div field="clas_potential" visible="false">clas_potential</div>
			            <div field="dummy_cust_no" width="100" headerAlign="center" align="left">虚拟客户号</div>
			            <div field="customername" width="140" headerAlign="center" align="left">客户名称</div>
			            <div field="relate_cust_name" width="100" headerAlign="center" align="left">关联因素</div>
			            <div field="name" width="100" headerAlign="center" align="left">关联客户经理</div>
			            <div field="phone" width="60" headerAlign="center" align="left">客户经理联系方式</div>
			            <div field="marketing_stat" width="100" headerAlign="center" align="left" renderer="onRenderMarkStat">转化结果</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();

	var grid =$G.get("datagrid1");
	var grid2 =$G.get("datagrid2");
	var grid3 =$G.get("datagridOrg2");
	var grid4 =$G.get("datagridOrg1");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	var dataType = "1";
	var ORGID = "";
	
	grid.load();
	grid2.load();
	pieInit();
	//查询当前用户是否能查看领导视图的数据,如果可以则返回相关机构号
	findUserOrg();
	
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
		if("1" == dataType){
			grid.load(data);
		}else{
			grid4.load(data);
		}
		//pieInit();
	}

	//重置
	function reset(){
		nui.get("custName").setValue("");
	}
	
	function onRenderMarkStat(e){
		var val = e.value;
		if(val == "1"){
			return "成功";
		}else if(val == "0" || val == "2"){
			return "转化中";
		}else{
			return "失败";
		}
	}
	
	function onRenderReach(e){
		var val = e.value;
		/* if(val == "0"){
			return "-";
		}else{
			return val + "%";
		} */
		return val + "%";
	}
	
	//饼图初始化
	function pieInit(type, queData){
		var url = "<%=root%>/kpi/findMarketCustomerRank";
		if("org" == type){
			url = "<%=root%>/kpi/findOrgMarketRank";
		}
		//var custName = $G.getbyName("custName").getValue();
		//var orgnum = $G.getbyName("orgnum").getValue();
		var myPieChart = echarts.init(document.getElementById("pieId"));
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (data){
	    	if(null != data.pdata && undefined != data.pdata){
	    		var option = {
					tooltip : {},
					color:['#005AB5','#D26900'],
					legend : {
						orient : 'vertical',
						x:'right',
						//padding:[40,0,20,0],
						data:['商机客户数','转化客户数'],
						textStyle: {
			    			color: '#FFFFFF'
				    	}
					},
					calculable:true,
					series : [{
						name:'',
						type:'pie',
						radius:'80%',
						data:data.pdata
					}]
				};
	    		//为图表实例加载数
				myPieChart.setOption(option);
	    	}
			//var data1 = form.getData();
			//grid2.load(data1);
	    });
	    if(null != queData && undefined != queData){
	    	queData.type = "pie";
			$G.postByAjax(queData, url ,ajaxConf);
	    }else{
			$G.postByAjax({}, url ,ajaxConf);	    	
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
	
	function calcUserMarketCustomerJob(){
		var url = "<%=root%>/kpi/calcUserMarketCustomerJob";
		$.ajax({
			url: url,
	        success: function (text) {
	        	
	        }
		});
	}
	
	function transSelect(){
		nui.get("custName").setValue("");
		if("1" == dataType){
			$("#datagrid2").hide();
			$("#datagridOrg2").show();
			
			$("#datagrid1").hide();
			$("#datagridOrg1").show();
			//查询机构列表数据
			var data = form.getData();
			pieInit("org", data);
			data.type = "";
			grid3.load(data);
			grid4.load(data);
			$("#dataType").find("span").html("个人");
			$("#goback").hide();
			dataType = "2";
			//nui.get("orgnum").setValue("");
		}else{
			$("#datagrid2").show();
			$("#datagridOrg2").hide();
			
			$("#datagrid1").show();
			$("#datagridOrg1").hide();
			//查询个人列表数据
			grid.load();
			pieInit();
			grid2.load();
			$("#dataType").find("span").html("机构");
			$("#goback").hide();
			dataType = "1";
			nui.get("orgnum").setValue(ORGID);
		}
	}
	
	function findUserOrg(){
		var url = "<%=root%>/kpi/findUserOrg";
		$.ajax({
			url: url,
	        success: function (text) {
	        	if(null != text.orgId && "" != text.orgId){
	        		$("#dataType").show();
	        		ORGID = text.orgId;
	        		nui.get("orgnum").setValue(text.orgId);
	        	}else{
	        		$("#dataType").hide();
	        		$("#goback").hide();
	        	}
	        }
		});
	}
	
	function findOrgMarketRankFun(e){
		var row = e.row;
        if (row) {
        	var id = row.id;
        	var orgLevel = row.by2;
        	if(undefined != id && undefined != orgLevel
        			&& null != id && null != orgLevel){
        		nui.get("orgnum").setValue(id);
        		var data = form.getData();
        		if("4" == orgLevel){
        			data.orgLevel = "4";
        		}
        		pieInit("org", data);
        		data.type = "";
        		grid3.load(data);
    			grid4.load(data);
    			$("#dataType").show();
    			$("#goback").show();
        	}
        }else{
        	alert("请选中一条记录");
        }
	}
	
	//根据条件下载指定数据文件
	function download(){
		var custName = nui.getbyName("custName").getValue();
		var orgnum = nui.get("orgnum").getValue();
		window.location="<%=root%>/kpi/downloadMarket?custName="+ custName + "&dataType="+ dataType + "&orgnum="+ orgnum;
	}
	
	
	//返回上层
	function goback(){
		var orgnum = nui.get("orgnum").getValue();
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
	        		nui.get("orgnum").setValue(parentorgnum);
	        		var data = form.getData();
	        		pieInit("org", data);
	        		data.type = "";
	        		grid3.load(data);
	    			grid4.load(data);
	        	}
	        }
		});
	}
</script>