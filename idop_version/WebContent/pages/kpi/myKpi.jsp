<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>质效系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<script src="<%= request.getContextPath() %>/resource/echarts/echarts.min.js"></script>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		#mykpi{
  			width: 98.9%;
		    height: 100%;
		    border-style: solid;
		    border-color: #2fa2fd;
		    border-width: 1px;
  		}
  		#gridDiv1{
		    border-top: solid;
		    border-bottom: solid;
		    border-color: #2fa2fd;
		    border-width: 1px;
  		}
  		</style>
	</head> 
	<body>
		<div id="butt" style="width:99%;height:25px;text-align:right" >
			<a class="mini-button" iconCls="icon-edit" onclick="swi()" ><span id="swid"></span></a>    
		</div>
		<div id="mykpi" style="display:none;">
			<form id="form1">
				<input id="mgr_id" name="mgr_id" class="nui-hidden" />
				<input id="period" name="period" class="nui-hidden"/>
				<input id="clas_five" name="clas_five" class="nui-hidden"/>
			</form>
			<div style="width:100%;height:30%;">
		  		<div id="lineId" style="width:95%;height:100%;">
				</div>
			</div>
			<div id="gridDiv1" style="width:100%;height:33%;">
				<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/kpi/listFiveKpi" style="width: 40%;height: 100%;float:left;"
		    		multiSelect="false" showPager="false" onRowClick="onFiveClick">
			        <div property="columns">
<!-- 			        	<div type="checkcolumn" name="checkCloumn"></div>       -->
			            <div field="clas_five_cn" width="100" headerAlign="center" allowSort="true"  align="center">五层分类</div>                
			            <div field="kpi" width="100" allowSort="true" headerAlign="center" align="center" renderer="onRenderAmpli">KPI贡献</div> 
			        </div>
				</div>
				<div id="pieId" style="width: 55%;height: 100%;float:left;" >
				</div>
			</div>
			<div id="gridDiv2" style="width:100%;height:37%;">
		    	<div id="datagrid2" class="nui-datagrid" sortMode="client" url="<%=root%>/kpi/getDetail" style="width: 100%;height: 88%;"
		    		multiSelect="false" onrowdblclick="custDetail">
			        <div property="columns">            
			            <!-- <div type="checkcolumn" name="checkCloumn"></div> -->
			            <div headerAlign="center" width="4%" type="indexcolumn">序号</div>
			            <div field="orgname" width="12%" headerAlign="center" allowSort="true"  align="left">责任中心名称</div>                
			            <div field="cust_no" width="10%" headerAlign="center" allowSort="true"  align="right">客户号</div>                
			            <div field="cust_name" width="12%" headerAlign="center" allowSort="true"  align="left">客户名称</div>                
			            <div field="claim_prop" width="12%" allowSort="true" headerAlign="center" align="right">认领占比</div>            
			            <div field="cust_num" width="10%" allowSort="true" headerAlign="center" align="right">客户数（折）</div> 
			            <div field="cust_num_kpi" width="10%" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">客户数贡献度</div>
			            <div field="busi_inc" width="10%" allowSort="true" headerAlign="center" align="right"  renderer="formatMoneny">营业收入(万元)</div>
			            <div field="busi_inc_kpi" width="10%" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">营收贡献度</div>
			            <div field="kpi" width="10%" allowSort="true" headerAlign="center" align="left" renderer="onRenderAmpli">客户贡献值</div>
			        </div>
				</div>
			</div>
			<div id="gridDiv3" style="width:100%;height:37%;">
		    	<div id="datagrid3" class="nui-datagrid" sortMode="client" url="<%=root%>/kpi/getDetail" style="width: 100%;height: 88%;"
		    		multiSelect="false" onrowdblclick="custDetail">
			        <div property="columns">            
			            <!-- <div type="checkcolumn" name="checkCloumn"></div> -->
			            <div headerAlign="center" width="4%" type="indexcolumn">序号</div>
			            <div field="orgname" width="12%" headerAlign="center" allowSort="true"  align="left">责任中心名称</div>                
			            <div field="cust_no" width="10%" headerAlign="center" allowSort="true"  align="right">客户号</div>                
			            <div field="cust_name" width="12%" headerAlign="center" allowSort="true"  align="left">客户名称</div>                
			            <div field="claim_prop" width="12%" allowSort="true" headerAlign="center" align="right">认领占比</div>            
			            <div field="cust_effe" width="10%" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">客户效能</div> 
			            <div field="cust_effe_kpi" width="10%" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">客户效能贡献度</div>
			            <div field="product_effe" width="10%" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">产品效能</div>
			            <div field="product_effe_kpi" width="10%" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">产品效能贡献度</div>
			            <div field="kpi" width="10%" allowSort="true" headerAlign="center" align="left" renderer="onRenderAmpli">客户贡献值</div>
			        </div>
				</div>
			</div>
			<div id="gridDiv4" style="width:100%;height:37%;">
		    	<div id="datagrid4" class="nui-datagrid" sortMode="client" url="<%=root%>/kpi/getDetail" style="width: 100%;height: 88%;"
		    		multiSelect="true" onrowdblclick="custDetail">
			        <div property="columns">            
			            <!-- <div type="checkcolumn" name="checkCloumn"></div> -->
			            <div headerAlign="center" width="4%" type="indexcolumn">序号</div>
			            <div field="orgname" width="12%" headerAlign="center" allowSort="true"  align="left">责任中心名称</div>                
			            <div field="cust_no" width="10%" headerAlign="center" allowSort="true"  align="right">客户号</div>                
			            <div field="cust_name" width="12%" headerAlign="center" allowSort="true"  align="left">客户名称</div>                
			            <div field="claim_prop" width="12%" allowSort="true" headerAlign="center" align="right">认领占比</div>            
			            <div field="cust_num_true" width="10%" allowSort="true" headerAlign="center" align="right">实际维护客户数</div> 
			            <div field="depo_day_true" width="10%" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny">实际维护日均存款(万元)</div>
			            <div field="cust_expa_kpi" width="10%" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">客户拓展贡献度</div>
			            <div field="effi_prom_kpi" width="10%" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">效能提升贡献度</div>
			            <div field="kpi" width="10%" allowSort="true" headerAlign="center" align="left" renderer="onRenderAmpli">客户贡献值</div>
			        </div>
				</div>
			</div>
		</div>
		<div id="kpiList" style="width: 99%;height: 100%;display:none;">
			<div id="panel1" class="nui-panel" title="KPI查询" style="width:100%;height:111px;" showToolbar="false" showCollapseButton="false"
	    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
			   	<div class="nui-fit" style="overflow: hidden">
			   		<form id="form2">
				   		<table style="table-layout: fixed;" class="search_table" width="100%">
							<tr>
			                   	<th align="right">机构：</th>
								<td align="left">
									<input id="orgSelect" class="mini-treeselect" url="<%=root%>/kpi/getOrgDatas" dataField="datas" 
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
								<th align="right">岗位：</th>
								<td align="left">    
			                       	<input name="base_role" class="nui-combobox" textfield="name" valuefield="id" url="<%=root%>/user/getBasicRoleList"
			                       		emptyText="请选择岗位" popupWidth="200" popupHeight="260" showNullItem="true" nullItemText="- - 请 选 择 - -"/>
			                   	</td> 
		               		</tr>
		               		<tr>
		               			<th align="right">EHR号：</th>
								<td align="left">
									<input id="cust_no" name="cust_no" class="mini-textbox" style="width:165px;"/>
								</td>
			                   	<th align="right">期数：</th>
								<td align="left">
									<input id="date" name="date" class="mini-combobox" style="width:165px;"
									url="<%=root%>/kpi/getDateDatas" textfield="period" valuefield="period"
										allowInput="false" />
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
							<!--<a class="mini-button" iconCls="icon-node" onclick="detail1()" plain="true">详情</a> -->
<!--           	     			<a class="mini-button" iconCls="icon-node" onclick="test()" plain="true">KPI测试</a> -->
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div style="width: 100%;height: 100%;">
		    	<div id="datagrid5" class="nui-datagrid" sortMode="client" url="<%=root%>/kpi/listKpi" style="width: 100%;height: 72%;"
		    		multiSelect="false" onrowdblclick="onrowdblclick">
			        <div property="columns">            
 			            <!--<div type="checkcolumn" name="checkCloumn"></div> -->
			            <div headerAlign="center" width="3%" type="indexcolumn">序号</div>
			            <div field="period" width="8%" headerAlign="center" allowSort="true"  align="right">期次</div> 
			            <div field="second_orgname" width="11%" allowSort="true" headerAlign="center" align="left">二级分行名称</div>
			            <div field="third_orgname" width="11%" allowSort="true" headerAlign="center" align="left">中心支行名称</div>
			            <div field="orgname" width="11%" allowSort="true" headerAlign="center" align="left">责任中心名称</div>
			            <div field="name" width="8%" headerAlign="center" allowSort="true"  align="left">客户经理</div>        
			            <div field="cust_mgr_no" width="8%" allowSort="true" headerAlign="center" align="right">EHR</div> 
			            <div field="cur_post" width="8%" allowSort="true" headerAlign="center" align="left">岗位</div> 
			            <div field="mgr_level" width="8%" allowSort="true" headerAlign="center" align="left">岗位等级</div> 
			            <div field="kpi" width="8%" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">KPI值</div>
			            <div field="promote_stand" width="8%" allowSort="true" headerAlign="center" align="right" >晋升标准</div>
			            <div field="is_stand" width="8%" allowSort="true" headerAlign="center" align="left" >是否达标</div>
			        </div>
				</div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	
	//根据客户经理类别显示详情
	var cust_type="${cust_type}";
	//根据角色类别判断是否展示切换按钮
	var role_type="${role_type}";
	//按钮点击切换标识
	var flag=role_type;
	//
	var mgr_id = "${mgr_id}";
	var period = "${period}";
	
	var grid1 =$G.get("datagrid1");
	var grid2 =$G.get("datagrid2");
	var grid3 =$G.get("datagrid3");
	var grid4 =$G.get("datagrid4");
	
	var grid5 =$G.get("datagrid5");
	var form1 = $G.getForm("form1");	
	var form2 = $G.getForm("form2");	
	var listFlag="1";
	//页面初始化
	$(document).ready(function(){
		if("1"==role_type){
			$("#butt").css("display","none");
		}else{
			$("#butt").css("display","block");
		}
		swi(flag);
	});
	
	//按钮初始化
	function swi(){
		if(flag=="1"){
			$("#mykpi").css("display","block");
			$("#kpiList").css("display","none");
			document.getElementById("swid").innerHTML= "KPI列表";
			flag="2";
			mykpiInit();
		}else if(flag=="2"){
			$("#mykpi").css("display","none");
			$("#kpiList").css("display","block");
			document.getElementById("swid").innerHTML= "个人KPI";
			flag="1";
			kpiListInit();
			listFlag="2";
		}
	}
	
	//个人KPI初始化
	function mykpiInit(){
		$G.getbyName("mgr_id").setValue(mgr_id);
		$G.getbyName("period").setValue(period);
		lineInit();
		grid1.load(form1.getData());
		pieInit();
		detailInit();
	}
	//kpiList初始化
	function kpiListInit(){
		if(listFlag=="1"){
			form2.reset();
			$G.getbyName("date").select(0);
			grid5.load(form2.getData());
		}
	}
	
	//判断详情展示grid
	function detailInit(){
		
		$("#gridDiv2").css("display","none");
		$("#gridDiv3").css("display","none");
		$("#gridDiv4").css("display","none");
		grid2.setData([]);
		grid3.setData([]);
		grid4.setData([]);
		if("1"==cust_type||"2"==cust_type||"4"==cust_type){
			$("#gridDiv2").css("display","block");
			grid2.load(form1.getData());
		}else if("3"==cust_type){
			$("#gridDiv3").css("display","block");
			grid3.load(form1.getData());
		}else if("5"==cust_type){
			$("#gridDiv4").css("display","block");
			grid4.load(form1.getData());
		}
	}
	
	function onFiveClick(){
		var row = grid1.getSelected();
		if(row){
			$G.getbyName("clas_five").setValue(row.clas_five);
			detailInit();
		}
	}
	
	// 行点击事件
	function onrowdblclick(e){
		var row = e.row;
		if(row){
			cust_type = row.cust_type;
			mgr_id = row.cust_mgr_no;
			period = row.period;
			flag="1";
			swi();
		}
	}
	
	//默认选中第一行
// 	function firstSel(){
// 		grid1.on("load",function(){
// 			grid1.select(0);//
// 			onRowClick();
// 		});
// 	}
	//饼图初始化
	function pieInit(){
		var myPieChart = echarts.init(document.getElementById("pieId"));
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (data){
			var option = {
					/* title : {
						text : ''
					}, */
					//鼠标在上面时的提示框
					tooltip : {},
					color:['#005AB5','#750000','#D26900','#6FB7B7','#01B468','#009393'],
					legend : {
						orient : 'vertical',
						x:'right',
						data:['一层','二层','三层','四层','五层','非五层'],
						textStyle: {
			    			color: '#FFFFFF'
			    		}
					},
					calculable:true,
					series : [{
						name:'',
						type:'pie',
						radius:'55%',
						center:['50%','60%'],
						data:data.pdata
					}]
				};
			
			//为图表实例加载数
			myPieChart.setOption(option);
	    });
		$G.postByAjax({mgr_id:mgr_id,period:period},"<%=root%>/kpi/kpiPieData",ajaxConf);
	}
	
	//我的KPI折线图初始化
	function lineInit(){
		var myChart = echarts.init(document.getElementById("lineId"));
		
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (data){
			if(data){
				var option = {
// 		 				title : {
// 		 					text : 'KPI',
// 		 					x:'center',
// 		 					textStyle: {
// 				    			color: '#FFFFFF'
// 				    		}
// 		 				}, 
						//鼠标在上面时的提示框
						tooltip : {},
						legend : {
							data : ['KPI','平移值','晋升值','标准值'],
							orient : 'vertical',
							x:'right',
							y:'center',
							textStyle: {
				    			color: '#FFFFFF'
				    		}
						
						},
						xAxis:{
							data : data.mList,
							splitLine:{
								show : true,
								lineStyle:{
									type:'dashed'
								}
							},
							axisLine:{
								lineStyle:{
									color:'#FFFFFF',
									type:'dashed'
								}
							},
							type:'category',
							boundaryGap:true
						},
						yAxis:{
							type:'value',
							min:data.min,
							splitLine:{
								show : true,
								lineStyle:{
									type:'dashed'
								}
							},
							axisLine:{
								lineStyle:{
									color:'#FFFFFF',
									width:1,
									type:'dashed'
								}
							}
						},
						//数值序列
						series : [
							{
								name:'KPI',
								type:'line',
								itemStyle:{
									normal:{
										color:'#00FF00',
										lineStyle:{
											color:'#00FF00'
										}
									}
								},
								data:data.dList
							},
							{
								name:'平移值',
								type:'line',
								itemStyle:{
									normal:{
										color:'#FFFF00',
										lineStyle:{
											color:'#FFFF00'
										}
									}
								},
								data:data.moveList
							},
							{
								name:'晋升值',
								type:'line',
								itemStyle:{
									normal:{
										color:'#FF8800',
										lineStyle:{
											color:'#FF8800'
										}
									}
								},
								data:data.upList
							},
							{
								name:'标准值',
								type:'line',
								itemStyle:{
									normal:{
										color:'#FF00FF',
										lineStyle:{
											color:'#FF00FF'
										}
									}
								},
								data:data.nextList
							}
						]
				};
				//为图表实例加载数
				myChart.setOption(option);
			}
		});
		$G.postByAjax({mgr_id:$G.getbyName("mgr_id").getValue()},"<%=root%>/kpi/kpiLineData",ajaxConf);
		
	}
	
	//重置
	function reset(){
		form2.reset();
		$G.getbyName("date").select(0);
	}
	
	//查询
	function search(){
		grid5.load(form2.getData());
	}
	
	//下载
	function download(){
// 		var cust_no=$G.getbyName("cust_no").getValue();
// 		var name=$G.getbyName("name").getValue();
// 		var org_id=$G.getbyName("org_id").getValue();
		var date=$G.getbyName("date").getValue();
		window.location="<%=root%>/kpi/download?date=" + date;   
	}

	function custDetail(e){
		var row = e.row;
        if (row) {
        	var id = row.orgnum+row.cust_no;
//         	var customercode = row.customercode;
//         	var clas_potential = row.clas_potential;
//         	if(null != customercode && "" != customercode){
//         		if("A3" == clas_potential || "B3" == clas_potential || "C3" == clas_potential 
//         				|| "D3" == clas_potential || "E3" == clas_potential){
//         			//我行潜力客户
//         			toCustDetail("3", id, period);
//         		}else{
//         			//我行客户
        			toCustDetail("2", id, period);
//         		}
//         	}else{
//         		//他行客户
//         		toCustDetail("1", id, period);
//         	}
        }else{
           alert("请选中一条记录");
        }
	}
	
	//打开客户详情
	function toCustDetail(custType, id, period) {
		var bizParams = {id: id, period:period};
		var url="";
		var title = "";
// 		if("1" == custType){
// 			//他行
<%-- 			url="<%=root%>/zxtask/custDetail"; --%>
// 			title = "他行客户";
// 		}else if("2" == custType){
			//我行
			url="<%=root%>/zxtask/mineCustDetail";
			title = "我行客户";
// 		}else{
// 			//我行潜力
<%-- 			url="<%=root%>/zxtask/qlCustDetail"; --%>
// 			title = "我行潜力客户";
// 		}
		
		$G.showmodaldialog(title, url, 1000, 800, bizParams, function(action){
       		grid.reload();
		});
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
	//保留两位小数
	function onRenderAmpli(e){
		//var str=e.value;
		
		return parseFloat(e.value).toFixed(2);
	}
</script>

