<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>客户统计</title>
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
<!-- 		   			<input id="backOrg" name="backOrg" class="nui-hidden" /> -->
<!-- 		   			<input id="backLevel" name="backLevel" class="nui-hidden" /> -->
		   			<input id="upOrg" name="upOrg" class="nui-hidden" />
		   			<input id="roleLevel" name="roleLevel" class="nui-hidden" />
		   			<input id="type_click" name="type_click" class="nui-hidden" />
		   			<input id="clas_click" name="clas_click" class="nui-hidden" />
		   			<input id="flag" name="flag" class="nui-hidden" />
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
					   	<colgroup>
						       	<col width="10%"/>
						       	<col width="15%"/>
						       	<col width="10%"/>
						       	<col width="15%"/>
						       	<col width="10%"/>
						       	<col width="15%"/>
						       	<col width="10%"/>
						       	<col width="15%"/>
						       	<col width="10%"/>
						       	<col width="15%"/>
						</colgroup>
						<tr>
		                   	<th align="right" ><span id="cust_no_lb"></span></th>
							<td align="left">
								<input id="cust_no" name="cust_no" class="mini-textbox" style="width: 100%;"/>
							</td>
		                   	<th align="right" >客户名称：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width: 100%;"/>
							</td>
							<th align="right" ><span id="clas_five_lb">五层分类：</span></th>
							<td align="left"><span id="clas_five_in">
								<input id="clas_five" name="clas_five" class="mini-combobox" style="width: 100%;"
									url="<%=root%>/zxparam/getDict?key=clas_five" textfield="name" valuefield="val"
									allowInput="false" showNullItem="true" nullItemText="请选择" emptyText="请选择"/>
									</span>
	                    	</td>
	                    	<th align="right">日期：</th>
							<td align="left">
								<input id="data_date" name="data_date" class="nui-datepicker" allowInput="true" style="width: 100%;"/>
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
							<span id="newDate"></span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span ><font color="yellow">单位：万元</></span>
		                </td>
						<td style="width:90%;" align="center">
							<a class="mini-button" iconCls="icon-search" onclick="search()" >查询</a>
							<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-reload" onclick="reset()" >重置</a>
          	     			<span class="separator"></span>
          	     			<a class="mini-button" iconCls="icon-download" onclick="downloadOrgList()" >导出组织汇总</a>
<!--           	     			<a class="mini-button" iconCls="icon-download" onclick="downClasSummary()" >导出组织汇总</a> -->
		                	<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-download" onclick="downTypeSummary()" >导出来源汇总</a>
                			<!-- <span class="separator"></span>
                			<a class="mini-button"   iconCls="icon-download" onclick="downdetail()" >导出明细</a>  -->
		               		<span class="separator"></span>
                 			<a class="mini-button"   iconCls="icon-download" onclick="downTask('1')" >导出我行明细</a> 
                			<span class="separator"></span>
                 			<a class="mini-button"   iconCls="icon-download" onclick="downTask('2')" >导出他行明细</a> 
                			<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-undo" onclick="backUp()" >返回上级</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
<!-- 			onRowClick="onRowClick" -->
	    	<div class="gridDiv" style="width:100%;height:26%;">
				<div id="datagrid1" class="nui-datagrid" url="<%=root%>/zxCustCnt/custByOrgList" style="width: 40%;height: 100%;float:left;"
		    		multiSelect="false" showPager="false"  onrowdblclick="onrowdblclick" onRowClick="onRowClick" allowHeaderWrap="true">
			        <div property="columns">
			        	<!-- <div type="checkcolumn" name="checkCloumn" width="4"></div> -->
			        	<div headerAlign="center" width="10" type="indexcolumn" align="right">序号</div>
			            <div field="orgname" width="40" headerAlign="center" allowSort="true"  align="left">机构名称</div>                
			            <div field="level_weight" width="35" allowSort="true" headerAlign="center" align="right">加权客户数</div> 
			            <div field="newcnt" width="35" headerAlign="center" allowSort="true"  align="right">新增客户数</div>
			        </div>
				</div>
				<div id="pieId" style="width: 55%;height: 100%;float:left;" >
				</div>
<%-- 				<div id="datagrid1" class="nui-datagrid" url="<%=root%>/zxCustCnt/custByOrgList" allowHeaderWrap="true" --%>
<!-- 		    		multiSelect="false" showPager="false" onrowdblclick="onrowdblclick"  -->
<!-- 		    		style="width:100%;height:100%;"> -->
<!-- 			        <div property="columns"> -->
<!-- 			        	<div field="orgname" width="100" headerAlign="center" allowSort="true" align="left">机构名称</div> -->
<!-- 			        	<div header="上年末" headerAlign="center"> -->
<!-- 			        		<div property="columns"> -->
<!-- 			            		<div field="last_year_five_cnt" width="70" headerAlign="center" allowSort="true"  align="right">五层客户<br/>系数0.06</div> -->
<!-- 					            <div field="last_year_four_cnt" width="70" allowSort="true" headerAlign="center" align="right">四层客户<br/>系数0.6</div>  -->
<!-- 					            <div field="last_year_three_cnt" width="70" headerAlign="center" allowSort="true"  align="right">三层客户<br/>系数1</div> -->
<!-- 					            <div field="last_year_two_cnt" width="70" allowSort="true" headerAlign="center" align="right">二层客户<br/>系数2</div>  -->
<!-- 					            <div field="last_year_one_cnt" width="70" headerAlign="center" allowSort="true"  align="right">一层客户<br/>系数3</div> -->
<!-- 					            <div field="last_year_add_cnt" width="70" allowSort="true" headerAlign="center" align="right">加权客户</div>  -->
<!-- 			            	</div> -->
<!-- 			            </div> -->
<!-- 			        	<div header="当月" headerAlign="center"> -->
<!-- 			        		<div property="columns"> -->
<!-- 					            <div field="month_five_cnt" width="70" headerAlign="center" allowSort="true"  align="right">五层客户<br/>系数0.06</div> -->
<!-- 					            <div field="month_four_cnt" width="70" allowSort="true" headerAlign="center" align="right">四层客户<br/>系数0.6</div>  -->
<!-- 					            <div field="month_three_cnt" width="70" headerAlign="center" allowSort="true"  align="right">三层客户<br/>系数1</div> -->
<!-- 					            <div field="month_two_cnt" width="70" allowSort="true" headerAlign="center" align="right">二层客户<br/>系数2</div>  -->
<!-- 					            <div field="month_one_cnt" width="70" headerAlign="center" allowSort="true"  align="right">一层客户<br/>系数3</div> -->
<!-- 					            <div field="month_add_cnt" width="70" allowSort="true" headerAlign="center" align="right">加权客户</div>  -->
<!-- 			            	</div> -->
<!-- 			            </div> -->
<!-- 			        	<div header="当月较年末" headerAlign="center"> -->
<!-- 			        		<div property="columns"> -->
<!-- 			        			<div header="五层客户" headerAlign="center"> -->
<!-- 				        			<div property="columns"> -->
<!-- 					        			<div field="last_year_five_up" width="70" headerAlign="center" allowSort="true"  align="right">增量</div> -->
<!-- 							            <div field="last_year_five_amp" width="70" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div>  -->
<!-- 				        			</div> -->
<!-- 				        		</div> -->
<!-- 			        			<div header="四层客户" headerAlign="center"> -->
<!-- 				        			<div property="columns"> -->
<!-- 					        			<div field="last_year_four_up"  width="70" headerAlign="center" allowSort="true"  align="right">增量</div>    -->
<!-- 							            <div field="last_year_four_amp" width="70" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div>  -->
<!-- 				        			</div> -->
<!-- 				        		</div> -->
<!-- 			        			<div header="三层客户" headerAlign="center"> -->
<!-- 				        			<div property="columns"> -->
<!-- 					        			<div field="last_year_three_up" width="70" headerAlign="center" allowSort="true"  align="right">增量</div>    -->
<!-- 							            <div field="last_year_three_amp" width="70" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div>  -->
<!-- 				        			</div> -->
<!-- 				        		</div> -->
<!-- 			        			<div header="二层客户" headerAlign="center"> -->
<!-- 				        			<div property="columns"> -->
<!-- 					        			<div field="last_year_two_up" width="70" headerAlign="center" allowSort="true"  align="right">增量</div>    -->
<!-- 							            <div field="last_year_two_amp" width="70" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div>  -->
<!-- 				        			</div> -->
<!-- 				        		</div> -->
<!-- 			        			<div header="一层客户" headerAlign="center"> -->
<!-- 				        			<div property="columns"> -->
<!-- 					        			<div field="last_year_one_up" width="70" headerAlign="center" allowSort="true"  align="right">增量</div>    -->
<!-- 							            <div field="last_year_one_amp" width="70" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div>  -->
<!-- 				        			</div> -->
<!-- 				        		</div> -->
<!-- 			        			<div header="加权客户" headerAlign="center"> -->
<!-- 				        			<div property="columns"> -->
<!-- 					        			<div field="last_year_add_up" width="70" headerAlign="center" allowSort="true"  align="right">增量</div>    -->
<!-- 							            <div field="last_year_add_amp" width="70" allowSort="true" headerAlign="center" align="right" renderer="onRenderAmpli">增幅</div>  -->
<!-- 				        			</div> -->
<!-- 				        		</div> -->
<!-- 			            	</div> -->
<!-- 			            </div> -->
<!-- 			        </div> -->
<!-- 				</div> -->
			</div>
			<div class="gridDiv" style="width:100%;height:26%;">
				<div id="datagrid2" class="nui-datagrid" url="<%=root%>/zxCustCnt/custByTypeList" style="width: 40%;height: 100%;float:left;"
		    		multiSelect="false" showPager="false" onRowClick="onRowClick2">
			        <div property="columns">
			        	<!-- <div type="checkcolumn" name="checkCloumn" width="4"></div> -->
			        	<div headerAlign="center" width="8" type="indexcolumn" align="right">序号</div>
			            <div field="incflg_cn" width="40" headerAlign="center" allowSort="true"  align="left">客户来源</div>                
			            <div field="cnt" width="40" allowSort="true" headerAlign="center" align="right">客户数</div> 
			        </div>
				</div>
				<div id="ringId" style="width: 55%;height: 100%;float:left;" >
				</div>
			</div>
			<div class="nui-fit">
		    	<div id="datagrid3" class="nui-datagrid" sortMode="client" url="<%=root%>/zxCustCnt/custDetailPage" style="width: 100%;height:100%;"
		    		multiSelect="false" onrowdblclick="custDetail">
			        <div property="columns">
<!-- 			        	<div type="checkcolumn" name="checkCloumn" width="3"></div> -->
			        	<div headerAlign="center" width="4" type="indexcolumn" align="right">序号</div>
			            <div name="resp_center_name_col" field="resp_center_name" width="13" headerAlign="center" allowSort="true"  align="left">机构名称</div>              
			            <div name="area_name_col" field="area_name" width="10" headerAlign="center" allowSort="true"  align="left">所属区域</div>
			            <div field="customername" width="15" allowSort="true" headerAlign="center" align="left">客户名称</div>        
			            <div name="customercode_col" field="customercode" width="10" allowSort="true" headerAlign="center" align="right">客户号</div> 
			            <div name="dummy_cust_no_col" field="dummy_cust_no" width="10" allowSort="true" headerAlign="center" align="right">虚拟客户号</div> 
			            <div name="clas_five_cn_col" field="clas_five_cn" width="10" allowSort="true" headerAlign="center" align="left">五层分类</div>
			            <div name="relate_cust_name_col" field="relate_cust_name" width="20" allowSort="true" headerAlign="center" align="left">关联客户名称</div>
			            <div name="relate_mgr_name_col" field="relate_mgr_name" width="10" allowSort="true" headerAlign="center" align="left">关联客户经理</div>
			            <div name="relate_mgr_mobile_col" field="relate_mgr_mobile" width="10" allowSort="true" headerAlign="center" align="right">关联客户经理联系方式</div>
			            <div name="incompoint_col" field="incompoint" width="10" allowSort="true" headerAlign="center" align="right"   renderer="formatMoneny">存款时点余额</div> 
			            <div name="incomday_col" field="incomday" width="10" allowSort="true" headerAlign="center" align="right"  renderer="formatMoneny">存款日均余额</div> 
<!-- 			            <div name="ilpoint_col" field="ilpoint" width="10" allowSort="true" headerAlign="center" align="right" renderer="onRenderCd">存贷比（时点）</div>  -->
<!-- 			            <div name="ilday_col" field="ilday" width="10" allowSort="true" headerAlign="center" align="right" renderer="onRenderCd">存贷比（日均）</div> -->
			            <div name="ilpoint_col" field="ilpoint" width="10" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny">表内理财日均</div> 
			            <div name="ilday_col" field="ilday" width="10" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny">表外理财日均</div> 
			            <div name="busi_inc_col" field="busi_inc" width="10" allowSort="true" headerAlign="center" align="right" renderer="formatMoneny">营收</div> 
			            <div name="claim_prop_col" width="10" headerAlign="center" align="right" renderer="onActionRenderer">产品覆盖率</div> 
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var flag="1";
	var upOrg = "${upOrg}";
	var roleLevel = "${roleLevel}";
	$G.getbyName("upOrg").setValue(upOrg);
	$G.getbyName("roleLevel").setValue(roleLevel);
	
// 	$G.getbyName("backOrg").setValue(upOrg);
// 	$G.getbyName("backLevel").setValue(roleLevel);
	var grid1 =$G.get("datagrid1");
	var grid2 =$G.get("datagrid2");
	var grid3 =$G.get("datagrid3");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	document.getElementById("newDate").innerHTML= findNewDate();
	grid1.load(form.getData());
	pageInit();
	
	//页面数据初始化
	function pageInit(){
 		firstSel(1);
 		colInit();
// 		grid3.load(form.getData());
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
	}
	
	//
	function backUp(){
		var orgnum = $G.getbyName("upOrg").getValue();
		if(orgnum != "${upOrg}"){
			var url = "<%=root%>/kpi/findParentOrgNum";
			$.ajax({
				url: url,
				data: {orgnum: orgnum},
		        success: function (text) {
		        	if(null != text.orgnum && text.orgnum !=""){
		        		$G.getbyName("upOrg").setValue(text.orgnum);
		        		//pageInit();
		        		grid1.load(form.getData());
		        	}
		        }
			});
		}
	}
	
	//组织统计列表行单击事件
	function onRowClick(){
		var row1 = grid1.getSelected();
		if (row1) {
			flag=row1.flag;//
			
			$G.getbyName("flag").setValue(row1.flag);
// 			$G.getbyName("upOrg").setValue(row1.id);
// 			$G.getbyName("roleLevel").setValue(row1.deptlevel);
			$G.getbyName("upOrg").setValue(row1.uporgid);
			$G.getbyName("roleLevel").setValue(row1.by2);
			$G.getbyName("type_click").setValue(row1.incflg);
			
			pieInit();
 			grid2.load(form.getData());
 			ringInit();
			colInit();
			if(row1.cnt==0){
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
	
	//组织统计列表行双击事件
	function onrowdblclick(e){
		var row1 = e.row;
		if (row1) {
			if(row1.id==upOrg){
				return;
			}else{
// 				$G.getbyName("upOrg").setValue(row1.id);
// 				$G.getbyName("roleLevel").setValue(row1.deptlevel);
				$G.getbyName("upOrg").setValue(row1.uporgid);
				$G.getbyName("roleLevel").setValue(row1.by2);
			}
			grid1.load(form.getData());
		}
	}
	
	//类别列表行点击事件
	function onRowClick2(){
		var row2 = grid2.getSelected();
		if (row2) {
			flag=row2.flag;
			$G.getbyName("flag").setValue(row2.flag);
			$G.getbyName("type_click").setValue(row2.incflg);
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
					//color:['#0066CC','#AE0000','#C6A300','#00BB00','#6FB7B7','#EA7500'],
					color:['#005AB5','#750000','#D26900','#6FB7B7','#01B468','#009393'],
					legend : {
						orient : 'vertical',
						x:'right',
						padding:[40,0,20,0],
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
			//点击事件
			//myPieChart.on("click",eClick);
	    });
		$G.postByAjax({upOrg:$G.getbyName("upOrg").getValue(),
						roleLevel:$G.getbyName("roleLevel").getValue(),dataDate:$G.get("data_date").getValue()},"<%=root%>/zxCustCnt/pieData",ajaxConf);
	}
	
	function eClick(param){
		$G.getbyName("clas_click").setValue(param.dataIndex+1);
		grid3.load(form.getData());
		$G.getbyName("clas_click").setValue("");
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
		$G.postByAjax({upOrg:$G.getbyName("upOrg").getValue(),
			roleLevel:$G.getbyName("roleLevel").getValue()},"<%=root%>/zxCustCnt/ringData",ajaxConf);
	}
	
	//下载层及汇总
	function downClasSummary(){
		var upOrg = "${upOrg}";
		var roleLevel = "${roleLevel}";
		window.location="<%=root%>/zxCustCnt/downClasSummary?upOrg="+ upOrg + "&roleLevel=" + roleLevel;
	}
	//下载类型汇总
	function downTypeSummary(){
		var upOrg = $G.getbyName("upOrg").getValue();
		var roleLevel = $G.getbyName("roleLevel").getValue();
		window.location="<%=root%>/zxCustCnt/downTypeSummary?upOrg="+ upOrg + "&roleLevel=" + roleLevel;
	}
	
	//下载明细
	function downdetail(){
		var cust_no=$G.getbyName("cust_no").getValue();
		var name=$G.getbyName("name").getValue();
		var clas_five=$G.getbyName("clas_five").getValue();
		//var cust_type=$G.getbyName("cust_type").getValue();
		
		var type_click=$G.getbyName("type_click").getValue();
		var flag=$G.getbyName("flag").getValue();
		var roleLevel=$G.getbyName("roleLevel").getValue();
		var upOrg=$G.getbyName("upOrg").getValue();
		window.location="<%=root%>/zxCustCnt/downdetail?cust_no=" + cust_no 
				+ "&name=" + name + "&type_click=" + type_click + "&clas_five=" 
				+ clas_five+ "&flag=" + flag
				+"&roleLevel=" + roleLevel+ "&upOrg=" + upOrg;
	}
	
	 // 下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }

	//查询
	function search(){
		grid1.load(form.getData());
		pieInit();
		var dataDate=nui.get("data_date").getValue();
		if (dataDate !=null && dataDate != '') {
			$("#newDate").html("");
			$("#newDate").html(dataDate.substr(0,10).replace(/-/g,""));
		}
		grid3.load(form.getData());
		//firstSel();
	}

	//重置
	function reset(){
		//form.reset();
		$G.getbyName("cust_no").setValue("");
		$G.getbyName("name").setValue("");
		$G.getbyName("clas_five").setValue("");
		$G.get("data_date").setValue("");
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
		$G.submitForm("form1","<%=root%>/zxCustCnt/findNewDate",ajaxConf);
		return newDate;
	}
	
	//添加覆盖率链接
	function onActionRenderer(e){
		var obj = null;
		if(e.record.product_prop){
			obj='<a href="javascript:lookInfo()"><font color="yellow">'+e.record.product_prop+'</font></a>';
		}
		return obj;
	}
	
	//查看产品覆盖率
	function lookInfo(){
		var rows = grid3.getSelected();
		if(rows){
           var url = "<%=root%>/zxMyCust/detail";
//			var bizParams = {id:rows.id};
  			var bizParams = {customercode:rows.customercode,orgnum:rows.orgnum};
   	        $G.showmodaldialog("产品覆盖率", url, 600, 250, bizParams, function(action){
   		    	 grid3.reload();
   		    });
		};
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
	
	function onRenderAmpli(e){
		return (e.value * 100).toFixed(2) + "%";
	}
	
	function downloadOrgList(){
		var upOrg = "${upOrg}";
		var dataDate = $G.get("data_date").getValue();
		window.location="<%=root%>/zxCustCnt/downloadOrgList?upOrg="+upOrg+"&dataDate="+dataDate;
	}
	
	function onRenderCd(e){
		var val = e.value;
		if(val&&val!="-"){
			val=(e.value * 100).toFixed(2) + "%";
		}
		return val;
	}
	
	function downTask(flg){
		var upOrg = "${upOrg}";
		var url = "<%=root%>/zxCustCnt/downTask";
		$.ajax({
			url: url,
			async: false, 
			data: {'upOrg': upOrg,flg:flg},
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

