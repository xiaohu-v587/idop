<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>我的客户</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/echarts/echarts.min.js"></script>
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		.gridDiv{
		    border-top: solid;
 		    border-right: solid;
		    border-color: #2fa2fd;
		    border-width: 1px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;height:9.9%;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
		   			<input id="clas_click" name="clas_click" class="nui-hidden" />
		   			<input id="type_click" name="type_click" class="nui-hidden" />
		   			<input id="flag" name="flag" class="nui-hidden" />
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right"><span id="cust_no_lb"></span></th>
							<td align="left">
								<input id="cust_no" name="cust_no" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right">客户名称：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
								<th align="right" ><span id="clas_five_lb">五层分类：</span></th>
								<td align="left"><span id="clas_five_in">
									<input id="clas_five" name="clas_five" class="mini-combobox" style="width:165px;"
										url="<%=root%>/zxparam/getDict?key=clas_five" textfield="name" valuefield="val"
										allowInput="false" showNullItem="true" nullItemText="请选择" emptyText="请选择"/>
										</span>
		                    	</td>
	               		</tr>
<!-- 	               		<tr> -->
<!-- 		                   	<th align="right">客户类型：</th> -->
<!-- 							<td align="left"> -->
<!-- 								<input id="cust_type" name="cust_type" class="mini-combobox" style="width:165px;" -->
<%-- 									url="<%=root%>/zxparam/getDict?key=incflg" textfield="name" valuefield="val" --%>
<!-- 									allowInput="false" showNullItem="true" nullItemText="请选择" emptyText="请选择"/> -->
<!-- 							</td> -->
<!-- 		                   	<th align="right"></th> -->
<!-- 		                    <td align="left">     -->
<!-- 		                    </td> -->
<!-- 		                    <th align="right"></th> -->
<!-- 		                    <td align="left">     -->
<!-- 		                    </td> -->
<!-- 	               		</tr> -->
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 28px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
		            	<td style="width:10%;color:#FFFFFF" align="center">  
							<span >日期：</span>
							<span id="newDate"></span>
		                </td>
						<td style="width:90%;" align="center">
							<a class="mini-button" iconCls="icon-reload" onclick="reload()" >刷新</a>
							<span class="separator"></span>
							<a class="mini-button" iconCls="icon-search" onclick="search()" >查询</a>
							<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-reload" onclick="reset()" >重置</a>
          	     			<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-download" onclick="downClasSummary()" >导出层级汇总</a>
		                	<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-download" onclick="downTypeSummary()" >导出来源汇总</a>
                			<!-- <span class="separator"></span>
                			<a class="mini-button" iconCls="icon-download" onclick="downdetail()" >导出明细</a> -->
                			<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-download" onclick="tran()" >批量移交</a>
<!--                 			<span class="separator"></span> -->
<!--                 			<a class="mini-button" iconCls="icon-reload" onclick="swi()" ><span id="swid"></span></a> -->
                			<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-node" onclick="fiveText()" >五层分类定义</a>
                			<span class="separator"></span>
                 			<a class="mini-button"   iconCls="icon-download" onclick="downTask('1')" >导出我行明细</a> 
                			<span class="separator"></span>
                 			<a class="mini-button"   iconCls="icon-download" onclick="downTask('2')" >导出他行明细</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
	    	<div class="gridDiv" style="width:100%;height:26%;">
				<div id="datagrid1" class="nui-datagrid" url="<%=root%>/zxMyCust/clasFiveList" style="width: 45%;height: 100%;float:left;"
		    		multiSelect="false" showPager="false" onRowClick="onRowClick">
			        <div property="columns">
			        	<div type="checkcolumn" name="checkCloumn" width="7"></div>
			        	<div headerAlign="center" width="7" type="indexcolumn"  align="right">序号</div> 
			            <div field="clas_five_cn" width="16" headerAlign="center" allowSort="true"  align="left">客户层级</div>                
			            <div field="cnt" width="16" allowSort="true" headerAlign="center" align="right">客户数</div> 
			            <div field="incomday" width="17" headerAlign="center" allowSort="true"  align="right" renderer="formatMoneny">日均存款总额</div>                
			            <div field="finaday" width="17" allowSort="true" headerAlign="center" align="right"  renderer="formatMoneny">日均理财总额</div>
			            <div field="newcnt" width="16" allowSort="true" headerAlign="center" align="right">新开客户数</div> 
			        </div>
				</div>
				<div id="pieId" style="width: 50%;height: 100%;float:left;" >
				</div>
			</div>
			<div class="gridDiv" style="width:100%;height:26%;">
				<div id="datagrid2" class="nui-datagrid" url="<%=root%>/zxMyCust/custClasList" style="width: 45%;height: 100%;float:left;"
		    		multiSelect="false" showPager="false" onRowClick="onRowClick2">
			        <div property="columns">
			        	<div type="checkcolumn" name="checkCloumn" width="8%"></div>
			        	<div headerAlign="center" width="10%" type="indexcolumn" align="right">序号</div> 
			            <div field="incflg_cn" width="42%" headerAlign="center" allowSort="true"  align="left">客户来源</div>                
			            <div field="cnt" width="40%" allowSort="true" headerAlign="center" align="right">客户数</div> 
			        </div>
				</div>
				<div id="ringId" style="width: 50%;height: 100%;float:left;" >
				</div>
			</div>
			<div class="nui-fit">
		    	<div id="datagrid3" class="nui-datagrid" sortMode="client" url="<%=root%>/zxMyCust/myCustPage" style="width: 100%;height: 32%;"
		    		multiSelect="true" onrowdblclick="custDetail">
			        <div property="columns">
			        	<div type="checkcolumn" name="checkCloumn" width="3"></div>
			        	<div headerAlign="center" width="6" type="indexcolumn" align="right">序号</div>
			            <div name="resp_center_name_col" field="resp_center_name" width="13" headerAlign="center" allowSort="true"  align="left">责任中心名称</div>
			            <div name="area_name_col" field="area_name" width="10" headerAlign="center" allowSort="true"  align="left">所属区域</div>
			            <div field="customername" width="15" allowSort="true" headerAlign="center" align="left">客户名称</div>        
			            <div name="customercode_col" field="customercode" width="10" allowSort="true" headerAlign="center" align="right">客户号</div> 
			            <div name="dummy_cust_no_col" field="dummy_cust_no" width="10" allowSort="true" headerAlign="center" align="right">虚拟客户号</div> 
			            <div name="clas_five_cn_col" field="clas_five_cn" width="10" allowSort="true" headerAlign="center" align="left">五层分类</div>
			            <div name="relate_cust_name_col" field="relate_cust_name" width="20" allowSort="true" headerAlign="center" align="left">关联客户名称</div>
			            <div name="relate_mgr_name_col" field="relate_mgr_name" width="10" allowSort="true" headerAlign="center" align="left">关联客户经理</div>
			            <div name="relate_mgr_mobile_col" field="relate_mgr_mobile" width="10" allowSort="true" headerAlign="center" align="right">关联客户经理联系方式</div>
			            <div name="incompoint_col" field="incompoint" width="10" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny">存款时点余额</div> 
			            <div name="incomday_col" field="incomday" width="10" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny"> 存款日均余额</div> 
<!-- 			            <div name="ilpoint_col" field="ilpoint" width="10" allowSort="true" headerAlign="center" align="right" renderer="onRenderCd">存贷比（时点）</div>  -->
<!-- 			            <div name="ilday_col" field="ilday" width="10" allowSort="true" headerAlign="center" align="right" renderer="onRenderCd">存贷比（日均）</div>  -->
			            <div name="ilpoint_col" field="ilpoint" width="10" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny">表内理财日均</div> 
			            <div name="ilday_col" field="ilday" width="10" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny">表外理财日均</div> 
			            <div name="busi_inc_col" field="busi_inc" width="10" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny">营收</div> 
			            <div name="claim_prop_col" width="10" headerAlign="center" align="right" renderer="onActionRenderer">产品覆盖率</div> 
			            <div name="claim_prop" width="10" visible="false" >claim_prop</div> 
			        </div>
				</div>
			</div>
			<div style="color:#ffffff; display:none; width: 400px;height: 200px; z-index:10000; position:relative; left:100%; top:-80%; margin-top:-500px; padding-top:2%; padding-left:100px;background-image:url('<%=root%>/resource/zxcss/zximage/xuanfu_bg.png;'); background-size:100% 100%; background-repeat:no-repeat;" id="treasureChest">
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var flag="1";
	var btnFlag="1";
	var grid1 =$G.get("datagrid1");
	var grid2 =$G.get("datagrid2");
	var grid3 =$G.get("datagrid3");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	document.getElementById("newDate").innerHTML= findNewDate();
	pageInit();

	function reload(){
		reset();
		pageInit();
	}
	
	//页面数据初始化
	function pageInit(){
		firstSel(1);
		colInit();
		//swi();
		pieInit();
		ringInit();
		grid2.load();
		//grid3.load(form.getData());
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
	function onRowClick(){
		var row1 = grid1.getSelected();
		if (row1) {
			flag=row1.flag;
			$G.getbyName("flag").setValue(row1.flag);
			$G.getbyName("clas_click").setValue(row1.clas_five);
			$G.getbyName("type_click").setValue("4");
			grid2.select("");
			colInit();
			if(row1.cnt==0&&row1.clas_five_cn!="加权合计"){
				grid3.setData([]);
				grid3.setTotalCount(0);
			}else{
				grid3.load(form.getData());
			}
		}else{
			grid3.setData([]);
			grid3.setTotalCount(0);
		}
	}
	
	//类别列表行点击事件
	function onRowClick2(){
		var row2 = grid2.getSelected();
		if (row2) {
			flag=row2.flag;
			$G.getbyName("flag").setValue(row2.flag);
			$G.getbyName("type_click").setValue(row2.incflg);
			$G.getbyName("clas_click").setValue("");
			//firstSel(2);
			grid1.select("");//
			colInit();
			if(row2.cnt==0){
				grid3.setData([]);
				grid3.setTotalCount(0);
			}else{
				grid3.load(form.getData());
			}
		}else{
			grid3.setData([]);
			grid3.setTotalCount(0);
		}
	}
	
	//详情列表字段展示
	function colInit(){
		if(flag=="1"){
			grid3.showColumn("resp_center_name_col");
			grid3.showColumn("customercode_col");
			grid3.showColumn("incompoint_col");
			grid3.showColumn("incomday_col");
			grid3.showColumn("ilpoint_col");
			grid3.showColumn("ilday_col");
			grid3.showColumn("busi_inc_col");
			grid3.showColumn("claim_prop_col");
			grid3.showColumn("clas_five_cn_col");
			
			grid3.hideColumn("area_name_col");
			grid3.hideColumn("dummy_cust_no_col");
			grid3.hideColumn("relate_cust_name_col");
			grid3.hideColumn("relate_mgr_name_col");
			grid3.hideColumn("relate_mgr_mobile_col");
			
			document.getElementById("cust_no_lb").innerHTML= "客户号：";
			$("#clas_five_lb").css("display","block");
			$("#clas_five_in").css("display","block");
		}else if(flag=="2"||flag=="3"){
			grid3.hideColumn("resp_center_name_col");
			grid3.hideColumn("customercode_col");
			grid3.hideColumn("incompoint_col");
			grid3.hideColumn("incomday_col");
			grid3.hideColumn("ilpoint_col");
			grid3.hideColumn("ilday_col");
			grid3.hideColumn("busi_inc_col");
			grid3.hideColumn("claim_prop_col");
			grid3.hideColumn("clas_five_cn_col");
			
			grid3.showColumn("area_name_col");
			grid3.showColumn("dummy_cust_no_col");
			grid3.showColumn("relate_cust_name_col");
			grid3.showColumn("relate_mgr_name_col");
			grid3.showColumn("relate_mgr_mobile_col");
			
			document.getElementById("cust_no_lb").innerHTML= "虚拟客户号：";
			$("#clas_five_lb").css("display","none");
			$("#clas_five_in").css("display","none");
		}
	}
	
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
						padding:[40,0,20,0],
						data:['一层','二层','三层','四层','五层','非五层'],
						textStyle: {
			    			color: '#FFFFFF'
 			    		}
// 			    		data:[
// 						      {orient : 'vertical',name:'一层',y:50},
// 						      {orient : 'vertical',name:'二层',y:70},
// 						      {orient : 'vertical',name:'三层',y:60},
// 						      {orient : 'vertical',name:'四层',y:80},
// 						      {orient : 'vertical',name:'五层',y:100},
// 						      {orient : 'vertical',name:'非五层',y:100}],
					},
					calculable:true,
					series : [{
						name:'',
						type:'pie',
						radius:'55%',
// 						label:{
// 							normal:{
// 								show:true,
// 								formatter:'{b}:{c}({d}%)'
// 								formatter:'{b}:{c}'
// 							}
// 						},
						//center:['50%','60%'],
						data:data.pdata
					}]
				};
			
			//为图表实例加载数
			myPieChart.setOption(option);
	    });
		$G.postByAjax({},"<%=root%>/zxMyCust/pieData",ajaxConf);
	}
	
	//环形图初始化
	function ringInit(){
		var myPieChart = echarts.init(document.getElementById("ringId"));
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
						padding:[40,0,20,0],
						data:['个人贷款','供应链','财私','存量客户','集团客户','产品商机'],
						textStyle: {
			    			color: '#FFFFFF'
			    			//fontSize:18
			    		}
					},
					calculable:true,
					series : [{
						name:'',
						type:'pie',
						radius:['40%','62%'],
						//center:['50%','60%'],
						//color:['','','','','',''],
						data:data.pdata
					}]
				};
			
			//为图表实例加载数
			myPieChart.setOption(option);
	    });
		$G.postByAjax({},"<%=root%>/zxMyCust/ringData",ajaxConf);
	}
	
	//按钮初始化
// 	function swi(){
// 		$G.getbyName("clas_click").setValue(flag);
// 		colInit();
// 		if(flag=="1"){
// 			document.getElementById("swid").innerHTML= "他行";
// 			flag="2";
// 			grid3.load(form.getData());
// 		}else if(flag=="2"){
// 			document.getElementById("swid").innerHTML= "我行";
// 			flag="1";
// 			grid3.load(form.getData());
// 		}
// 	}

	//下载层及汇总
	function downClasSummary(){
		window.location="<%=root%>/zxMyCust/downClasSummary?";   
	}
	//下载类型汇总
	function downTypeSummary(){
		window.location="<%=root%>/zxMyCust/downTypeSummary?";   
	}
	
	//下载明细
	function downdetail(){
		var cust_no=$G.getbyName("cust_no").getValue();
		var name=$G.getbyName("name").getValue();
		var type_click=$G.getbyName("type_click").getValue();
		var clas_five=$G.getbyName("clas_five").getValue();
		//var cust_type=$G.getbyName("cust_type").getValue();
		var clas_click=$G.getbyName("clas_click").getValue();
		var flag=$G.getbyName("flag").getValue();
		window.location="<%=root%>/zxMyCust/downdetail?cust_no=" + cust_no 
				+ "&name=" + name + "&type_click=" + type_click + "&clas_five=" 
				+ clas_five+ "&flag=" + flag + "&clas_click=" + clas_click;   
	}
	
	 // 下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
	 // 
    function tran() {
		
		//var rows = grid.getSelecteds();
		var rows2 = grid3.getSelecteds();
		if(rows2.length>0){
           		var clas = null;
//                	var clas = "";
//                 for(var index = 0;index < rows.length;index++){
// 					if(clas == ""){
// 						clas = rows[index].clas_five;
//                   	} else {
//                   		clas += "," + rows[index].clas_five;
//                   	};
//                 }
                var claimids = "";
                var claimPropMin = 100;
                for(var i = 0;i < rows2.length;i++){
					if(claimids == ""){
						claimids = rows2[i].claim_id;
						//claimPropMin = parseInt(rows2[i].claim_prop);
                  	} else {
                  		claimids += "," + rows2[i].claim_id;
//                   		if(claimPropMin>parseInt(rows2[i].claim_prop)){
//                   			claimPropMin = parseInt(rows2[i].claim_prop);
//                   		}
                  	};
                  	 
                }
                var url = "<%=root%>/zxMyCust/transfer";
    			var bizParams = {org_id:"${org_id}",
    					clas:clas,claimids:claimids,claimPropMin:claimPropMin};
    	        $G.showmodaldialog("客户移交", url, 600, 500, bizParams, function(action){
    		    	 grid1.reload();
    		    	 grid2.reload();
    		    	 grid3.reload();
    		    });
		}else{
			$G.alert("请选中一条记录");
		};
    }
	 

	//查询
	function search(){
		grid3.load(form.getData());
		//firstSel();
	}

	//重置
	function reset(){
		//form.reset();
		$G.getbyName("cust_no").setValue("");
		$G.getbyName("name").setValue("");
		$G.getbyName("clas_five").setValue("");
	}
	
	//添加覆盖率链接
	function onActionRenderer(e){
		var obj = null;
		if(e.record.product_prop){
			obj='<a href="javascript:lookInfo()"><font color="yellow">'+e.record.product_prop+'</font></a>';
		}
		return obj;
		//<font color="yellow">联系我们：周煌/规划与产品/公司金融部/江苏/BOC</font>
	}
	
	//查看产品覆盖率
	function lookInfo(){
		var rows = grid3.getSelected();
		if(rows){
           var url = "<%=root%>/zxMyCust/detail";
//    			var bizParams = {id:rows.id};
   			var bizParams = {customercode:rows.customercode,orgnum:rows.orgnum};
   	        $G.showmodaldialog("产品覆盖率", url, 600, 250, bizParams, function(action){
   		    	 grid3.reload();
   		    });
		};
    }
	
	//五层分类定义
	function fiveText(){
		if(btnFlag=="1"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (data){
		    	$("#treasureChest").show();
				$("#treasureChest").animate({left:'48%'},600);
				document.getElementById("treasureChest").innerHTML= data.text;
		    });
			$G.postByAjax({},"<%=root%>/zxMyCust/clasFiveText",ajaxConf);
			btnFlag="2";
		}else if(btnFlag=="2"){
			$("#treasureChest").animate({left:'100%'},600);
			setTimeout("$('#treasureChest').hide()", 600);
			btnFlag="1";
		}
	}
	
	//查询最新数据时间
	function findNewDate(){
		var newDate="";
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsAsync(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setSuccessFunc(function (data) {
			if(data&&data.newDate){
				newDate=data.newDate;
			}
		});
		$G.submitForm("form1","<%=root%>/zxMyCust/findNewDate",ajaxConf);
		return newDate;
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
	
	function onRenderCd(e){
		var val = e.value;
		if(val&&val!="-"){
			val=(e.value * 100).toFixed(2) + "%";
		}
		return val;
	}
	
	function downTask(flg){
		var url = "<%=root%>/zxMyCust/downTask";
		$.ajax({
			url: url,
			async: false, 
			data: {flg:flg},
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

