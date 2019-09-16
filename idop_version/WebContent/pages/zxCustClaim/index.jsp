<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>客户认领</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;height:156px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
		   			<input id="orgId" name="orgId" class="mini-hidden"/>
		   			<input id="flag" name="flag" class="mini-hidden"/>
		   			<input id="levelStr" name="levelStr" class="mini-hidden"/>
		   			<input id="cust_stat_flag" name="cust_stat_flag" class="mini-hidden"/>
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
		                    <th align="right">客户状态：</th>
							<td align="left">
								<input id="cust_stat" name="cust_stat" class="mini-combobox" style="width:165px;"
								url="<%=root%>/zxparam/getDict?key=cust_stat" textfield="name" valuefield="val" onvaluechanged='dataSelCha'
									allowInput="false" showNullItem="true" nullItemText="请选择" emptyText="请选择"/>
							</td>
	               		</tr>
	               		<tr>
	               			<th align="right">是否可撤回:</th>
		                    <td align="left">
		                    	<input id="is_back" name="is_back" class="mini-radiobuttonlist" style="width:165px;color:#ffffff"
								url="<%=root%>/zxparam/getDict?key=is_back" textfield="name" valuefield="val"
								repeatItems="2" repeatLayout="table" repeatDirection="horizontal"	/>
		                    </td>
	               			<th align="right"><span id="span_lb"></span></th>
							<td align="left"><span id="span_in"></span></td>
							<th align="right">上任客户经理：</th>
		                    <td align="left">
		                    	<input id="befor_name" name="befor_name" class="mini-textbox" style="width:165px;"/>
		                    </td>
		                   	
	               		</tr>
	               		<tr>
<!-- 	               			<th align="right">所属机构：</th> -->
<!-- 							<td align="left"> -->
<!-- 								<input id="orgSelect" class="mini-treeselect" dataField="datas"  -->
<!-- 									name="orgSelect" textfield="orgname" valuefield="id" parentfield="upid"   -->
<!-- 									valueFromSelect="false" multiSelect="false"  expandOnLoad="0" -->
<!-- 									allowInput="false" showClose="true" oncloseclick="onCloseClick"  -->
<!-- 									showRadioButton="true" showFolderCheckBox="false" popupWidth="305" -->
<!-- 									popupHeight="470" popupMaxHeight="600" style="width:165px;"/> -->
<!-- 							</td> -->
		                    <th align="right"><span id="area_lb"></span></th>
							<td align="left"><span id="area_in"></span></td>
							<th align="right"></th>
							<td align="left">.</td>
							<th align="right"></th>
							<td align="left">.</td>
	               		</tr>
	               		<tr>
		                    <td align="right" colspan="6" >
			                    <a class="mini-button" iconCls="icon-search" onclick="search()" ><font color="yellow">查询</font></a>
								<span class="separator"></span>
	          	     			<a class="mini-button" iconCls="icon-reload" onclick="reset()" ><font color="yellow">重置</font></a>
	          	     			<span class="separator"></span>
<!--           	     				<a class="mini-button" iconCls="icon-download" onclick="download()" ><font color="yellow">导出</font></a> -->
	          	     			<span id="down_lb"></span>
	          	     			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                    </td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
		            	<td style="width:30%;color:#FFFFFF" align="center">
							<div style="float:left;width:40%;">
								<div style="float:left;width:33%;">日期：</div>
								<div style="float:left;width:67%;text-align:left" id="newDate"></div>
							</div>
							<div style="float:left">当前客户池：<span id="swid"></span>&nbsp;</div>
		                </td>
						<td style="width:60%;" align="left">
							<a class="mini-button" iconCls="icon-reload" onclick="swi()" >客户池切换</a> 
		                	<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-edit" onclick="claim()" >认领</a>
                			<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-edit" onclick="claimMany()" >批量认领</a>
                			<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-edit" onclick="claimBack()" >撤回</a>
                			<span class="separator"></span>
                			<a class="mini-button" iconCls="icon-edit" onclick="claimComp()" >异议申述</a>

		                </td>
		                <td style="width:10%;color:#FFFFFF" align="center">  
							<span >总客户数：</span>
							<span id="total"></span>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
				<form id="form2">
		   			<input id="poolid" name="id" class="mini-hidden"/>
		   		</form>
		    	<div id="datagrid" class="nui-datagrid" sortMode="client" url="<%=root%>/zxCustClaim/findCustList" style="width: 100%;height: 100%;"
		    		multiSelect="true" onrowdblclick="custDetail" pageSize="15" onshowrowdetail="showDetail">
			        <div property="columns">
			        	<div width="4" type="expandcolumn"></div>
			        	<div width="4" type="checkcolumn"  name="checkCloumn"></div>
			        	<div headerAlign="center" width="4" type="indexcolumn" align="right">序号</div>
			        	<div name="resp_center_no_col" field="resp_center_no" width="8" allowSort="true" headerAlign="center" align="left">责任中心号</div>
			            <div name="resp_center_name_col" field="resp_center_name" width="15" allowSort="true" headerAlign="center" align="left">责任中心名称</div> 
			            <div name="cust_no_col" field="cust_no" width="8" allowSort="true" headerAlign="center" align="right">客户号</div> 
			            <div name="dummy_cust_no_col" field="dummy_cust_no" width="8" allowSort="true" headerAlign="center" align="right">虚拟客户号</div>
			            <div field="name" width="15" allowSort="true" headerAlign="center" align="left">客户名称</div> 
			            <div name="admi_area_col" field="admi_area" width="8" allowSort="true" headerAlign="center" align="left">行政区域</div> 
			            <div name="clas_five_cn_col" field="clas_five_cn" width="10" allowSort="true" headerAlign="center" align="left">五层分类</div>
			            <div name="relate_cust_name_col" field="relate_cust_name" width="20" allowSort="true" headerAlign="center" align="left">关联客户名称</div>
			            <div name="relate_mgr_name_col" field="relate_mgr_name" width="10" allowSort="true" headerAlign="center" align="left">关联客户经理</div>
			            <div name="relate_mgr_mobile_col" field="relate_mgr_mobile" width="10" allowSort="true" headerAlign="center" align="left">关联客户经理</br>联系方式</div>
			            <div name="remain_date_col" field="remain_date" width="8" allowSort="true" headerAlign="center" align="right">剩余认领时间</br>（天）</div> 
			            <div name="stay_date_col" field="stay_date" width="8" allowSort="true" headerAlign="center" align="right">客户池剩余</br>停留时间（天）</div> 
			            <div field="cust_stat_cn" width="8" allowSort="true" headerAlign="center" align="left">客户状态</div> 
			            <div field="claim_prop_all" width="8" allowSort="true" headerAlign="center" align="right" renderer="onClaimRenderer">已认领比例</div> 
<!-- 			            <div field="claim_cust_mgr_name" width="15" allowSort="true" headerAlign="center" align="center" >认领客户经理</div>  -->
			            <div field="to_claim_prop" width="8" allowSort="true" headerAlign="center" align="right" renderer="onToClaimRenderer">待认领比例</div>
			            <div field="befor_cust_mgr_name" width="10" allowSort="true" headerAlign="center" align="left">上任客户经理</div>
			        </div>
				</div>
				<div id="datagrid_form" style="display:none">
			        <div id="grid_detail" class="nui-datagrid" sortMode="client" url="<%=root%>/zxCustClaim/findClaimList" showPager="false" style="width: 100%;height: 100px;">
				        <div property="columns">
				        	<div headerAlign="center" width="6" type="indexcolumn">序号</div>
				        	<div field="claim_cust_mgr_id" width="15" allowSort="true" headerAlign="center" align="right">EHR号</div>
				        	<div field="claim_cust_mgr_name" width="15" allowSort="true" headerAlign="center" align="left">客户经理</div>
				        	<div field="claim_prop_str" width="15" allowSort="true" headerAlign="center" align="right">认领比例</div>
				        	
				        </div>
					</div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var userId = "${userId}";
	var orgId = "${orgId}";
	var level = "${level}";
	var flag = "${flag}";
	$G.getbyName("orgId").setValue(orgId);
	$G.getbyName("flag").setValue(flag);
	$G.getbyName("levelStr").setValue(level);
	var secOrgId = "${secOrgId}";
	var grid =$G.get("datagrid");
	var grid_detail =$G.get("grid_detail");
	var form = $G.getForm("form1");
	var form2 = $G.getForm("form2");
	$G.getbyName("cust_stat_flag").setValue("1");
	colInit();
	anInit();
	grid.load(form.getData());
	document.getElementById("total").innerHTML= findTotal();
	document.getElementById("newDate").innerHTML= findNewDate();

	function claim() {
		var rows = grid.getSelecteds();
		if (rows.length>1) {
			$G.alert("已选中多条记录，请点击批量认领按钮！");return;
		}
		var row = grid.getSelected();
		if (row) {
			if(row.cussts=="3"){
				$G.alert("此客户已认领完毕！不可认领！");
				return;
			}
// 			else if(row.cussts=="2" && row.claim_cust_mgr_id.indexOf(userId)>=0){
// 			    $G.alert("此客户您已认领！不可重复认领！");
// 			    return;
// 			}
// 			else {
// 				var ajaxConf = new GcdsAjaxConf();
// 				ajaxConf.setIsShowProcessBar(true);
// 				ajaxConf.setIsShowSuccMsg(false);
// 			    ajaxConf.setSuccessFunc(function (data){
// 					if(1==data.num){
// 						$G.alert("此客户已被申诉！不可认领！");
// 						return;
// 					}
// 				});
<%-- 				$G.postByAjax({id:row.id},"<%=root%>/zxCustAppeal/getAppNum",ajaxConf); --%>
// 			}
			var url = "<%=root%>/zxCustClaim/claim";
			var bizParams = {id:row.id,to_claim_prop:row.to_claim_prop};
	        $G.showmodaldialog("客户认领", url, 550, 250, bizParams, function(action){
		    	 grid.load(form.getData());
		    	 //grid.select(-1);
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	function claimComp() {
		var rows = grid.getSelecteds();
		if (rows.length>1) {
			$G.alert("已选中多条记录，请重新选择！");return;
		}
		var row = grid.getSelected();
		if (row) {
			if(row.cussts=="1"){
				$G.alert("此客户处于未认领状态！不可申诉！");
				return;
			}else{
				var ajaxConf = new GcdsAjaxConf();
				ajaxConf.setIsShowProcessBar(true);
				ajaxConf.setIsShowSuccMsg(false);
			    ajaxConf.setSuccessFunc(function (data){
					if(data.num>=1){
						$G.alert("此客户已被申诉！不可再次申诉！");
						return;
					}else{
						var url = "<%=root%>/zxCustClaim/claimComp";
						var bizParams = {id:row.id,flag:flag};
				        $G.showmodaldialog("客户认领申诉", url, 550, 250, bizParams, function(action){
					    	 grid.reload();
					    	 grid.select("");
					    });
					}
				});
				$G.postByAjax({id:row.id},"<%=root%>/zxCustAppeal/getAppNum",ajaxConf);
			}
		}else{
			$G.alert("请先选中一条记录！");
		}
	}

	//下载
	function download(){
		var orgId=$G.getbyName("orgId").getValue();
		var cust_no=$G.getbyName("cust_no").getValue();
		var name=$G.getbyName("name").getValue();
		var befor_name=$G.getbyName("befor_name").getValue();
		var clas_five="";
		if($G.getbyName("clas_five")){
			clas_five=$G.getbyName("clas_five").getValue();
		}
		var cust_stat=$G.getbyName("cust_stat").getValue();
		var cust_type="";
		if($G.getbyName("cust_type")){
			cust_type=$G.getbyName("cust_type").getValue();
		}
		var prov_code="";
		if($G.getbyName("prov_code")){
			prov_code=$G.getbyName("prov_code").getValue();
		}
		var area_code="";
		if($G.getbyName("area_code")){
			area_code=$G.getbyName("area_code").getValue();
		}
		var flag=$G.getbyName("flag").getValue();
		var levelStr=$G.getbyName("levelStr").getValue();
		window.location="<%=root%>/zxCustClaim/download?cust_no=" + cust_no+ "&orgId=" + orgId
				+ "&flag=" + flag + "&levelStr=" +levelStr+ "&prov_code=" +prov_code+ "&area_code=" +area_code+"&befor_name=" +befor_name
				+ "&name=" + name + "&clas_five=" + clas_five + "&cust_stat=" + cust_stat+ "&cust_type=" + cust_type;   
	}
	
	//切换
	function swi(){
		//把查询条件置空
		$G.getbyName("cust_no").setValue("");
		$G.getbyName("name").setValue("");
		if($G.getbyName("clas_five")){
			$G.getbyName("clas_five").setValue("");
		}
		if($G.getbyName("cust_type")){
			$G.getbyName("cust_type").setValue("");
		}
		$G.getbyName("cust_stat").setValue("");
		//$G.getbyName("cust_type").setValue("");
		$G.getbyName("cust_stat_flag").setValue("1");
		var org = $G.getbyName("orgId").getValue();
		if(level&&parseInt(level)>2){
			if(secOrgId&&orgId!=secOrgId){
				if(org==orgId){
					$G.getbyName("orgId").setValue(secOrgId);
					$G.getbyName("levelStr").setValue("2");
				}else{
					$G.getbyName("orgId").setValue(orgId);
					$G.getbyName("levelStr").setValue("3");
				}
				anInit();
				grid.load(form.getData());
				document.getElementById("total").innerHTML= findTotal();
				//document.getElementById("newDate").innerHTML= findNewDate();
			}
		}
	}
	
	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
	 
  //选中客户状态 将默认条件置空
	function dataSelCha(){
		$G.getbyName("cust_stat_flag").setValue("");
	}

	//查询
	function search(){
		grid.load(form.getData());
		anInit();
	}

	//重置
	function reset(){
		/* form.reset();
		$G.getbyName("orgId").setValue(orgId);
		$G.getbyName("flag").setValue(flag);
		$G.getbyName("levelStr").setValue(level); */
		//$G.getbyName("cust_stat_flag").setValue("1");
		//grid.load(form.getData());
		
		//重新编写重置按钮事件  2018年8月23日18:35:26  --liutao
		$G.get("cust_no").setValue("");
		$G.get("name").setValue("");
		$G.get("cust_stat").setValue("");
		if(null != $G.get("cust_type") && undefined != $G.get("cust_type")){
			$G.get("cust_type").setValue("");
		}
		if(null != $G.get("orgSelect") && undefined != $G.get("orgSelect")){
			$G.get("orgSelect").setValue("");
		}
		if(null != $G.get("clas_five") && undefined != $G.get("clas_five")){
			$G.get("clas_five").setValue("");
		}
		$G.get("befor_name").setValue("");
		$G.get("is_back").setValue("");
		if(null != $G.get("area_code") && undefined != $G.get("area_code")){
			$G.get("area_code").setValue("");
		}
	}

	//按钮切换
	function anInit(){
		var levelNow = $G.getbyName("levelStr").getValue();
		if(levelNow){
			if(parseInt(levelNow)>=3){
				document.getElementById("swid").innerHTML= "支行客户池";
				grid.hideColumn("remain_date_col");
				grid.showColumn("stay_date_col");
			}else{
				document.getElementById("swid").innerHTML= "分行客户池";
				grid.hideColumn("stay_date_col");
				grid.showColumn("remain_date_col");
			}
		}else{
			document.getElementById("swid").innerHTML= "分行客户池";
			grid.hideColumn("remain_date_col");
			grid.showColumn("stay_date_col");
		}
	}
	
	function colInit(){
		if(flag=="1"){
			grid.showColumn("cust_no_col");
			grid.showColumn("resp_center_name_col");
			grid.showColumn("resp_center_no_col");
			grid.showColumn("clas_five_cn_col");
			grid.hideColumn("dummy_cust_no_col");
			grid.hideColumn("relate_cust_name_col");
			grid.hideColumn("relate_mgr_name_col");
			grid.hideColumn("relate_mgr_mobile_col");
			grid.hideColumn("admi_area_col");
			
			document.getElementById("cust_no_lb").innerHTML= "客户号：";
			document.getElementById("span_lb").innerHTML= "五层分类：";
			document.getElementById("span_in").innerHTML= "<input id='clas_five' name='clas_five' class='mini-combobox' style='width:165px;' "
				+" url='<%=root%>/zxparam/getDict?key=clas_five' textfield='name' valuefield='val' "
				+"	allowInput='false' showNullItem='true' nullItemText='请选择' emptyText='请选择'/>";
			document.getElementById("area_lb").innerHTML= "";
			document.getElementById("area_in").innerHTML= "";
			document.getElementById("area_lb").innerHTML= "所属机构：";
			var orgUrl="<%=root%>/zxCustClaim/getOrgDatas?orgId="+secOrgId;
			document.getElementById("area_in").innerHTML= " <input id='orgSelect' class='mini-treeselect' dataField='datas'  "
				+" name='orgSelect' textfield='orgname' valuefield='id' parentfield='upid' url='"+orgUrl+"'  "
				+" valueFromSelect='false' multiSelect='false'  expandOnLoad='0' "
				+" allowInput='false' showClose='true' oncloseclick='onCloseClick'  "
				+" showRadioButton='true' showFolderCheckBox='false' popupWidth='305' "
				+" popupHeight='470' popupMaxHeight='600' style='width:165px;'/> ";
			
			document.getElementById("down_lb").innerHTML= " <a class='mini-button' iconCls='icon-download' onclick='downTask(\"1\")' ><font color='yellow'>导出</font></a> ";
			
		}else if(flag=="2"){
			grid.hideColumn("cust_no_col");
			grid.hideColumn("resp_center_name_col");
			grid.hideColumn("resp_center_no_col");
			grid.hideColumn("clas_five_cn_col");
			grid.showColumn("dummy_cust_no_col");
			grid.showColumn("relate_cust_name_col");
			grid.showColumn("relate_mgr_name_col");
			grid.showColumn("relate_mgr_mobile_col");
			grid.showColumn("admi_area_col");
			
			document.getElementById("cust_no_lb").innerHTML= "虚拟客户号：";
			document.getElementById("span_lb").innerHTML= "客户类型：";
			document.getElementById("span_in").innerHTML= "<input id='cust_type' name='cust_type' class='mini-combobox' style='width:165px;' "
				+" url='<%=root%>/zxparam/getDict?key=incflg' textfield='name' valuefield='val' "
				+"	allowInput='false' showNullItem='true' nullItemText='请选择' emptyText='请选择'/>";
			document.getElementById("area_lb").innerHTML= "";
			document.getElementById("area_in").innerHTML= "";
			document.getElementById("area_lb").innerHTML= "行政区域：";
			document.getElementById("area_in").innerHTML= "	<font color='white'>市</font> "
				+"  <input id='prov_code' name='prov_code' class='mini-combobox' style='width:65px;' "
				+"	  url='<%=root%>/zxCustClaim/getAreaList' textfield='name' valuefield='id' onvaluechanged='citySelCha' "
				+"	    allowInput='false' showNullItem='true' nullItemText='请选择' emptyText='请选择'/> "
				+"	  <font color='white'>区县</font> "
				+"	  <input id='area_code' name='area_code' class='mini-combobox' style='width:65px;' "
				+"	  textfield='name' valuefield='id' "
				+"	allowInput='false' showNullItem='true' nullItemText='请选择' emptyText='请选择'/> ";
			document.getElementById("down_lb").innerHTML= " <a class='mini-button' iconCls='icon-download' onclick='downTask(\"2\")' ><font color='yellow'>导出</font></a> ";
			
		}
		$G.parse();
	}
	
	//查询数据总数
	function findTotal(){
		var total=0;
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsAsync(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setSuccessFunc(function (data) {
			total=data.totalCnt;
		});
		$G.submitForm("form1","<%=root%>/zxCustClaim/findCustCount",ajaxConf);
		return total;
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
		$G.submitForm("form1","<%=root%>/zxCustClaim/findNewDate",ajaxConf);
		return newDate;
	}
	
	function claimMany() {
		var rows = grid.getSelecteds();
		if (rows.length>0) {			
			var ids = "";
			var codes="";
            for(var index = 0;index < rows.length;index++){
            	if(rows[index].cussts=="3"){
    				$G.alert("选中的客户存在已认领完毕的！请重新选择！");
    				return;
    			}
//             	else if(rows[index].cussts=="2" && rows[index].claim_cust_mgr_id.indexOf(userId)>=0){
//     			    $G.alert("选中的客户存在您已认领的！请重新选择！");
//     			    return;
//     			}
				if(index == 0){
                	ids = rows[index].id;
                	codes = rows[index].cust_no;
              	} else {
                	ids += "," + rows[index].id;
                	codes += "," + rows[index].cust_no;
              	}
            }
//             var ajaxConf = new GcdsAjaxConf();
//     		ajaxConf.setIsShowProcessBar(true);
//     		ajaxConf.setIsShowSuccMsg(false);
//     	    ajaxConf.setSuccessFunc(function (data){
//     	    	if(data&&data.claim_prop_max){
//     	    		var claim_prop_max=data.claim_prop_max;
<%--     		    	var url = "<%=root%>/zxCustClaim/claimMany"; --%>
//     				var bizParams = {ids:ids,codes:codes,claim_prop_max:claim_prop_max};
//     		        $G.showmodaldialog("客户批量认领", url, 450, 200, bizParams, function(action){
//     			    	 grid.reload();
//     			    });
//     	    	}
//     		});
<%--     		$G.postByAjax({ids:ids},"<%=root%>/zxCustClaim/claimManyCheck",ajaxConf); --%>
			var claim_prop_max=100;
			var url = "<%=root%>/zxCustClaim/claimMany";
			var bizParams = {ids:ids,codes:codes,claim_prop_max:claim_prop_max};
			$G.showmodaldialog("客户批量认领", url, 450, 200, bizParams, function(action){
				 grid.reload();
			});
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	//撤回
	function claimBack(){
		var rows = grid.getSelecteds();
		if (rows.length>0) {
			var ids = "";
            for(var index = 0;index < rows.length;index++){
				if(index == 0){
                	ids = rows[index].id;
              	} else {
                	ids += "," + rows[index].id;
              	}
				if(rows[index].cussts=="1"){
					$G.alert("选中的客户存在未认领状态，不可撤回！");return;
				}
            }
            var ajaxConf = new GcdsAjaxConf();
    		ajaxConf.setIsShowProcessBar(true);
    		ajaxConf.setIsShowSuccMsg(false);
    	    ajaxConf.setSuccessFunc(function (data){
    	    	if(data.backFlag=="2"){
    	    		$G.alert("选中的客户存在被认领时间大于一天，不可撤回！");return;
    	    	}else if(data.backFlag=="3"){
    	    		$G.alert("操作成功！");
    	    	}else{
    	    		$G.alert("操作失败！");
    	    	}
    		});
    		$G.postByAjax({ids:ids},"<%=root%>/zxCustClaim/claimBack",ajaxConf);
    		grid.reload();
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	function custDetail(e){
		var row = e.row;
        if (row) {
        	var id = row.id;
        	var customercode = row.cust_no;
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
	
	function citySelCha(){
		var prov_code = $G.getbyName("prov_code").getValue();
		if(prov_code){
			$G.getbyName("area_code").setUrl(" <%=root%>/zxCustClaim/getAreaList?pid="+prov_code);
		}
	}
	
	var datagrid_form = document.getElementById("datagrid_form");
	function showDetail(e){
		var grid = e.sender;
		var row = e.record;
// 		if(row.cussts=="1"){
// 			$G.alert("选中的客户是未认领状态，没有详情展示！");
// 			//$("#datagrid_form").css("display","none");
// 			return;
// 		}else{
			var td = grid.getRowDetailCellEl(row);
			$("#datagrid_form").css("display","block");
			td.appendChild(datagrid_form);
			grid_detail.load({id:row.id});
// 		}
	}
	
	function onClaimRenderer(e){
		var obj = null;
		if(e.record.cussts=="1"){
			obj = "0%";
		}else if(e.record.id){
			$G.getbyName("id").setValue(e.record.id);
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsAsync(false);
			ajaxConf.setIsShowSuccMsg(false);
			ajaxConf.setIsShowProcessBar(false);
			ajaxConf.setSuccessFunc(function (data) {
				if(data&&data.prop){
		    		obj=data.prop+"%";
				}
			});
			$G.submitForm("form2","<%=root%>/zxCustClaim/findClaimProp",ajaxConf);
		}
		e.record.claim_prop_all = obj;
		return obj;
	}
	
	function onToClaimRenderer(e){
		var obj = null;
		if(e.record.cussts=="1"){
			obj = "100%";
		}else if(e.record.id){
    		$G.getbyName("id").setValue(e.record.id);
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsAsync(false);
			ajaxConf.setIsShowSuccMsg(false);
			ajaxConf.setIsShowProcessBar(false);
			ajaxConf.setSuccessFunc(function (data) {
				if(data&&data.prop){
					obj=(100-parseInt(data.prop))+"%";
				}
			});
			$G.submitForm("form2","<%=root%>/zxCustClaim/findClaimProp",ajaxConf);
		}
		e.record.to_claim_prop = obj;
		return obj;
	}
	
	function getOrgSelData(){
		var orgUrl="<%=root%>/zxCustClaim/getOrgDatas?orgId="+secOrgId;
		$G.getbyName("orgSelect").setUrl(orgUrl);
	}
	
	function downTask(flg){
		var upOrg = "${secOrgId}";
		var url = "<%=root%>/zxCustClaim/downTask";
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

