<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>潜客提升</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		#treasureChest tr td{
  			border: 1px solid #2fa2fd;
  			text-align: center;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="潜客提升" style="width:100%;height:65px;" showToolbar="false" showCollapseButton="false"
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
							<span id="dataDate" style="color: white;"></span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span ><font color="yellow">单位：万元</></span>
						</td>
		                <td style="white-space:nowrap; text-align: center;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>
		                	<span class="separator"></span>
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                    <!-- <span class="separator"></span>
		                    <a class="nui-button" iconCls="icon-download" onclick="download()">导出</a> -->
		                </td>
		                <td style="white-space:nowrap; text-align: right;">
							<a class="mini-button" iconCls="icon-nodenew" onclick="treasureChest()">百宝箱</a>
						</td>
		            </tr>
		       	</table>
			</div>
			
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/zxtask/findLurkCustomer" style="width: 100%;height: 100%;"
		    		multiSelect="false" onrowdblclick="custDetail" onrowclick="updateTreasureChest" allowCellSelect="false">
			        <div property="columns">
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false">id</div>
			            <div field="clas_potential" visible="false">clas_potential</div>
			            <div field="orgname" width="100" headerAlign="center" align="left">责任中心名称</div>
			            <div field="customercode" width="100" headerAlign="center" align="right">客户号</div>
			            <div field="customername" width="150" headerAlign="center" align="left">客户名称</div>
<!-- 			            <div field="cust_type" width="80" headerAlign="center" align="left">潜在分类</div> -->
			            <div field="daynum" width="70" headerAlign="center" align="right" renderer="onRenderDayNum">剩余时间(天)</div>
			            <div field="incomday" width="80" headerAlign="center" align="right" renderer="formatMoneny">存款&理财日均</div>
			            <div field="balance" width="50" headerAlign="center" align="right" renderer="formatMoneny">目标差额</div>
<!-- 			            <div field="next_val" width="100" headerAlign="center" align="right" renderer="formatMoneny">下一层级达标默认值</div> -->
			            <div field="flag" width="50" headerAlign="center" align="left" renderer="onRenderFlag">是否达成</div>
			        </div>
				</div>
				<div style="display:none; width: 600px; height: 220px; z-index:10; position:relative; left:100%; top:-100%; margin-top:40px; padding-top:2%; background-image:url('<%=root%>/resource/zxcss/zximage/xuanfu_bg.png;'); background-size:100% 100%; background-repeat:no-repeat;" id="treasureChest">
		    	<table style="width: 90%;height: 85%; margin-left:4%; color: #FFFFFF; border: 1px solid #2fa2fd;background:rgba(15,25,61,0.8);
		    		border-collapse: collapse;">
			    	<tr>
				    	
				    	<td>客户号:</td>
				    	<td>
				    		<span style="color:#2fa2fd;" id="custNo" name="custNo"></span>
				    	</td>
				    	<td>客户名称:</td>
				    	<td>
				    		<span style="color:#2fa2fd;" id="custName" name="custName"></span>
				    	</td>
			    	</tr>
			    	<tr>
			    		
				    	<td  style="width:130px;">
				    		目标层级:
				    		<select id="targetTier">
					    		
				    		</select>
				    	</td>
				    	<td   style="width:130px;">
				    		考核时点:
				    		<select id="assessDay">
				    			
				    		</select>月末
				    	</td>
				    	<td  style="width:130px;">
				    	放几天？
				    		<input type="text" style="color:red; font-size:16px; height:20px; width:40px;" id="timeDay" name="timeDay" min="1" max="8"/>
				    	</td>
				    	<td rowspan="2">
				    		<a class="mini-button" iconCls="icon-nodenew" onclick="treasureClick" id="treasure">计算</a>
				    	</td>
			    	</tr>
			    	<tr>
				    	
				    	<td>日均存款及理财合计:</td>
				    	<td colspan="2" id="moneyTd">
				    		<input type="text" style="color:red; font-size:16px; height:20px; width:180px;" id="moneyInput" name="moneyInput" />元
				    	</td>
				    	<!-- <td>
				    		<a class="mini-button" iconCls="icon-nodenew" onclick="closeTreasureChest()">关闭</a>
				    	</td> -->
			    	</tr>
			    	<tr>
			    		<td style="color:red;" colspan="4" id="tipInfo"></td>
			    	</tr>
		    	</table>
			</div>
			</div>
			
	</body>
</html>
<script type="text/javascript">
	$G.parse();

	var grid =$G.get("datagrid1");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	var INCOMDAY = 0;
	var MAXTIMEDAY = 1;
	
	grid.load();
	/* 	var date = new Date();
		date.setDate(date.getDate() - 1);
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		if(parseInt(month) < 10){
			month = "0" + month;
		}
		var day = date.getDate();
		if(parseInt(day) < 10){
			day = "0" + day;
		} */
	$("#dataDate").html("数据日期: " + findNewDate());
	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
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
	
	//重置
	function reset(){
		form.reset();
	}

	function custDetail(e){
		var row = e.row;
        if (row) {
        	var id = row.id;
        	//var customercode = row.customercode;
        	var clas_potential = row.clas_potential;
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
        	if("A3" == clas_potential || "B3" == clas_potential || "C3" == clas_potential 
    				|| "D3" == clas_potential || "E3" == clas_potential){
    			//我行潜力客户
    			toCustDetail("3", id);
    		}else{
    			//我行客户
    			toCustDetail("2", id);
    		}
        }else{
           alert("请选中一条记录");
        }
	}
	
	function onRenderDayNum(e){
		//var val = e.value.toString();
		//if(null != val && "" != val && "null" != val){
			//var year = val.substr(0,4);
			//alert(year);
			//var month = val.substr(4,2);
			//alert(month);
			//var nowDate = new Date();
			//var date = new Date(nowDate.getFullYear(), nowDate.getMonth() + 1, 0);
			//var lastday = formatDateTime(date);
			
			// var now = formatDateTime(new Date());
			// if (lastday < now) {
			//	e.rowStyle = "color:red;";
			//	return "已过期";
			// } else {
				//alert(formatDateTime(nowDate));
				//alert(lastday);
			//return parseInt(lastday) - parseInt(formatDateTime(nowDate)) + 1;
			//}
		//}else{
		//	return val;
		//}
			var nowDate = new Date();
			var date = new Date(nowDate.getFullYear(), nowDate.getMonth() + 1, 0);
			var lastday = formatDateTime(date);
			return parseInt(lastday) - parseInt(formatDateTime(nowDate)) + 1;
	}
	
	function formatDateTime(theDate) {
		var _hour = theDate.getHours();
		var _minute = theDate.getMinutes();
		var _second = theDate.getSeconds();
		var _year = theDate.getFullYear();
		var _month = theDate.getMonth();
		var _date = theDate.getDate();
		if(_hour<10){_hour="0"+_hour ;}
		if(_minute<10){_minute="0"+_minute;  }
		if(_second<10){_second="0"+_second  }
		if(_month < 9){_month = "0" + (_month+1);}
		if(_date<10){_date="0"+_date  }
		return  _year + "" + _month + "" + _date;
		}
	
	//打开客户详情
	function toCustDetail(custType, id) {
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
		var bizParams = {id: id};
		$G.showmodaldialog(title, url, 1000, 800, bizParams, function(action){
       		grid.reload();
		});
	}
	
	var treasureChestFlag = false;
	//百宝箱点击事件
	function treasureChest(){
		var row = grid.getSelected();
		if(row){
			treasureChestAssignment(row);
			
			if(treasureChestFlag){
				treasureChestFlag = false;
				$("#treasureChest").animate({left:'100%'},600);
				setTimeout("$('#treasureChest').hide()", 600);
			}else{
				treasureChestFlag = true;
				$("#treasureChest").show();
			}
			$("#treasureChest").animate({left:'43%'},600);
		}else{
			$G.alert("请选择一条记录!");
		}
	}
	
	//根据条件下载指定数据文件
	function download(){
		var code = nui.getbyName("customercode").getValue();
		var name = nui.getbyName("customername").getValue();
		window.location="<%=root%>/zxtask/download?code="+ code + "&name="+ name;
	}

	//关闭百宝箱
	/* function closeTreasureChest(){
		$("#treasureChest").animate({left:'100%'},600);
		setTimeout("$('#treasureChest').hide()", 600);
	} */
	
	//百宝箱确定点击事件
	function treasureClick(){
		//获取选择的潜在层
		var targetTierVal = $("#targetTier").val();
		var targetTierNewVal = targetTierStandardVal(targetTierVal);
		
		//获取选中的考核时点
		var assessDayVal = $("#assessDay").val();
		//计算已过天数
		var date1 = new Date();
		date1.setMonth(0);
		date1.setDate(0);
		var date = new Date();
		var currentDay = parseInt((date.getTime()-date1.getTime())/(1000*3600*24));
		//计算达标天数
		date.setMonth(assessDayVal);
		date.setDate(0);
		var standardDay = parseInt((date.getTime()-date1.getTime())/(1000*3600*24));
		
		//获取金额合计
		var moneyVal = $("#moneyInput").val().trim();
		if("" != moneyVal){
			var timeDayVal = (targetTierNewVal*standardDay - INCOMDAY*currentDay)/moneyVal;
			timeDayVal = Math.ceil(timeDayVal);
			if(timeDayVal > MAXTIMEDAY){
				$("#tipInfo").html("提示: 您输入的金额过小,无法在有效天数内达成!");
			}else{
				$("#timeDay").val(Math.ceil(timeDayVal));
				$("#tipInfo").html("");
			}
		}else{
			//获取放几天
			//var timeDayVal = $G.getbyName("timeDay").getValue().trim();
			var timeDayVal = $("#timeDay").val().trim();
			if("" == timeDayVal || parseInt(timeDayVal) <= 0){
				timeDayVal = 1;
				$("#timeDay").val("1");
			}
			
			if(parseInt(MAXTIMEDAY) < parseInt(timeDayVal)){
				$("#tipInfo").html("提示: 您输入的天数超过当前时点的最大天数!");
			}else{
				//计算公式（（目标层级金额*达标天数-当前日均*已过天数）/放几天）
				var moneyVal = (targetTierNewVal*standardDay - INCOMDAY*currentDay)/timeDayVal;
				moneyVal = Math.round(moneyVal*100)/100;
				$("#moneyInput").val(moneyVal);
				$("#tipInfo").html("");
			}
		}
	}
	
	function targetTierStandardVal(val){
		if("5" == val){
			return 10000;
		}else if("4" == val){
			return 100000;
		}else if("3" == val){
			return 200000;
		}else if("2" == val){
			return 1000000;
		}else if("1" == val){
			return 10000000;
		}
	}
	
	function updateTreasureChest(){
		if(treasureChestFlag){
			var row = grid.getSelected();
			if(row){
				treasureChestAssignment(row);
			}
		}
	}
	
	function treasureChestAssignment(row){
		//获取选中的当前潜在层级
		var clas_potential = row.clas_potential;
		INCOMDAY = row.incomday;
		var clasPotentialOption = "";
		if("E3" == clas_potential){
			clasPotentialOption = "<option value='5'>五层</option>"
					+ "<option value='4'>四层</option>"
					+ "<option value='3'>三层</option>"
					+ "<option value='2'>二层</option>"
					+ "<option value='1'>一层</option>";
		}else if("D3" == clas_potential){
			clasPotentialOption = "<option value='4'>四层</option>"
					+ "<option value='3'>三层</option>"
					+ "<option value='2'>二层</option>"
					+ "<option value='1'>一层</option>";
		}else if("C3" == clas_potential){
			clasPotentialOption = "<option value='3'>三层</option>"
					+ "<option value='2'>二层</option>"
					+ "<option value='1'>一层</option>";
		}else if("B3" == clas_potential){
			clasPotentialOption = "<option value='2'>二层</option>"
					+ "<option value='1'>一层</option>";
		}else if("A3" == clas_potential){
			clasPotentialOption = "<option value='1'>一层</option>";
		}
		$("#targetTier").html(clasPotentialOption);
		var date = new Date();
		var month = date.getMonth() + 1;
		var assessDayOption = "";
		for(var i = month; i <= 12; i++){
			assessDayOption += "<option value='"+i+"'>"+i+"</option>";
		}
		$("#assessDay").html(assessDayOption);
		
		var date1 = new Date();
		date1.setMonth(0);
		date1.setDate(0);
		var date = new Date();
		var currentDay = parseInt((date.getTime()-date1.getTime())/(1000*3600*24));
		//计算达标天数
		date.setMonth(month);
		date.setDate(0);
		var standardDay = parseInt((date.getTime()-date1.getTime())/(1000*3600*24));
		//清空放几天和计算出来的金额值
		//$G.getbyName("timeDay").setValue("");
		//$G.getbyName("moneyInput").setValue("");
		$("#moneyInput").val("");
		var timeDayVal = standardDay - currentDay;
		if(timeDayVal <= 0){
			timeDayVal = 1;
		}
		MAXTIMEDAY = timeDayVal;
		$("#timeDay").val(timeDayVal);
		//给客户号和客户名称赋值
		var custNo = row.customercode;
		var custName = row.customername;
		$("#custNo").html(custNo);
		$("#custName").html(custName);
		$("#treasure").click();
	}
	
	//考核时点change事件
	$("#assessDay").change(function(){
		var assessDayVal = $("#assessDay").val();
		var date1 = new Date();
		date1.setMonth(0);
		date1.setDate(0);
		var date = new Date();
		var currentDay = parseInt((date.getTime()-date1.getTime())/(1000*3600*24));
		//计算达标天数
		date.setMonth(assessDayVal);
		date.setDate(0);
		var standardDay = parseInt((date.getTime()-date1.getTime())/(1000*3600*24));
		var timeDayVal = standardDay - currentDay;
		if(timeDayVal <= 0){
			timeDayVal = 1;
		}
		MAXTIMEDAY = timeDayVal;
		$("#timeDay").val(timeDayVal);
		$("#moneyInput").val("");
		$("#treasure").click();
	});
	
	//放几天获取焦点的事件，获取焦点时需要把合计清空
	$("#timeDay").focus(function(){
		$("#moneyInput").val("");
	});
	
	//金额合计获取焦点的事件，获取焦点时需要把放几天天数清空
	$("#moneyInput").focus(function(){
		$("#timeDay").val("");
	});
	
	function onRenderFlag(e){
		var balance = e.row.balance;
		if(0 == balance){
			return "是";
		}else{
			return "否";	
		}
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