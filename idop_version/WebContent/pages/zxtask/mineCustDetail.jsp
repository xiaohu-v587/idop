<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>我行潜力客户</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<%@include file="/common/nuires.jsp" %>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resource/zxcss/styleindex.css"/>
    <style type="text/css">
	    html, body{
	        font-size:12px;
	        padding:0;
	        margin:0;
	        border:0;
	        height:100%;
	        overflow:hidden;
	    }
    
	    #detailTable tr{
	    	line-height:30px;
	    }
    </style>
</head>
<body>
	<div class="mini-fit">
		<div class="detail" style="margin-left:-50px;padding-top:150px;">
            	<span class="heptagon_box">
            		<span class="minecust">
            		</span>
                </span>
                <span class="detail_style detail1">
                	<ul  class="detail_u">
                		<li class="detail_t1">
                			基本信息
                		</li>
                		<li class="detail_t2" id="baseInfo">
                		</li>
                	</ul>
                </span>
                <span class="detail_style detail2">
                	<ul  class="detail_u">
                		<li class="detail_t1">
                			存款
                		</li>
                		<li class="detail_t2" id="incomInfo">
                		</li>
                	</ul>
                </span>
                <span class="detail_style detail3">
                	<ul  class="detail_u">
                		<li class="detail_t1">
                			贷款
                		</li>
                		<li class="detail_t2" id="loansInfo">
                		</li>
                	</ul>
                </span>
                <span class="detail_style detail4">
                	<ul  class="detail_u">
                		<li class="detail_t1">
                			营收
                		</li>
                		<li class="detail_t2" id="revenueInfo">
                		</li>
                	</ul>
                </span>
                <span class="detail_style detail5">
                	<ul  class="detail_u">
                		<li class="detail_t1">
                			KPI贡献值
                		</li>
                		<li class="detail_t2" id="kpiInfo">
                		</li>
                	</ul>
                </span>
                <span class="detail_style detail6">
                	<ul  class="detail_u">
                		<li class="detail_t1">
                			产品覆盖
                		</li>
                		<li class="detail_t2" id="productCover">
                			
                		</li>
                	</ul>
                </span>
                <span class="detail_style detail7">
                	<ul  class="detail_u">
                		<li class="detail_t1">
                			五层类别
                		</li>
                		<li class="detail_t2" id="custypInfo">	
                		</li>
                	</ul>
                </span>
            </div>
        </div>
     </div>    
     <div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnCancle" class="mini-button" iconCls="icon-close" onclick="onCancel()" >取消</a>
     </div>
</body>
</html>

<script type="text/javascript">

	$G.parse();

	//取消  
	function onCancel(e) {
		$G.closemodaldialog("cancel");
	}

	//标准方法接口定义
    function setData(e) {
		var data = $G.clone(e);
		var pcpId = data.id;
		var period = null;
		if(data.period){
			period = data.period
		}
		var url = "<%=root%>/zxtask/findqlCustDetail";
		$.ajax({
			url: url,
			data: {'pcpId': pcpId,period:period},
			success: function (text) {
				if(null != text){
					var custInfo = text.custInfo;
					if(null != custInfo){
						//基本信息
						var artifical = custInfo.artifical;//法人
						if(null == artifical){
							artifical = "";
						}
						var telNo = custInfo.telno;//联系方式
						if(null == telNo){
							telNo = "";
						}
						
						var m_user_name = custInfo.m_user_name;//主办客户经理
						if(null == m_user_name){
							m_user_name = "";
						}
						var m_phone = custInfo.m_phone;//主办客户经理联系方式
						if(null == m_phone){
							m_phone = "";
						}
						$("#baseInfo").html("1、企业类型：<br>2、是否国结客户：<br>"
								+ "3、法人姓名：" + artifical + "<br>4、法人联系方式：" + telNo + "<br>"
								+ "5、主办客户经理："+m_user_name+"<br>"
								+"6、主办客户经理联系方式：" + m_phone);
						//存款信息
						var incomday = custInfo.incomday;//存款日均
						if(null == incomday){
							incomday = "0";
						}else{
							incomday = (incomday/10000).toFixed(2);
						}
						var incompoint = custInfo.incompoint;//存款时点
						if(null == incompoint){
							incompoint = "0";
						}else{
							incompoint = (incompoint/10000).toFixed(2);
						}
						$("#incomInfo").html("1、存款时点：" + incompoint + "万元" + "<br>"
								+ "2、存款日均：" + incomday + "万元");
						//贷款信息
						var loansday = custInfo.loansday;//贷款日均
						if(null == loansday){
							loansday = "0";
						}else{
							loansday = (loansday/10000).toFixed(2);
						}
						var loanspoint = custInfo.loanspoint;//贷款时点
						if(null == loanspoint){
							loanspoint = "0";
						}else{
							loanspoint = (loanspoint/10000).toFixed(2);
						}
						$("#loansInfo").html("1、贷款时点：" + loanspoint + "万元" + "<br>"
								+ "2、贷款日均：" + loansday + "万元");
						//营收信息
						var interest_inc = custInfo.interest_inc;//利息收入
						if(null == interest_inc){
							interest_inc = "0";
						}else{
							interest_inc = (interest_inc/10000).toFixed(2);
						}
						var non_interest_inc = custInfo.non_interest_inc;//非利息收入
						if(null == non_interest_inc){
							non_interest_inc = "0";
						}else{
							non_interest_inc = (non_interest_inc/10000).toFixed(2);
						}
						$("#revenueInfo").html("1、利息收入：" + interest_inc + "万元" + "<br>"
								+ "2、非利息收入：" + non_interest_inc + "万元");
						//KPI贡献信息
						var kpi = custInfo.kpi;//KPI分值
						if(null == kpi){
							kpi = "";
						}
						$("#kpiInfo").html("分值：" + kpi);
						//产品覆盖信息
						var product = "";//已叙做产品
						var cover = 0;//产品覆盖率
						var payroll = custInfo.payroll;
						if("1" == payroll){
							cover += 20;
							product += ",代发薪";
						}
						var setcard = custInfo.setcard;
						if("1" == setcard){
							cover += 20;
							product += ",结算卡";
						}
						var sms = custInfo.sms;
						if("1" == sms){
							cover += 20;
							product += ",短信通";
						}
						var return_box = custInfo.return_box;
						if("1" == return_box){
							cover += 20;
							product += ",回单箱";
						}
						var cyber_bank = custInfo.cyber_bank;
						if("1" == cyber_bank){
							cover += 20;
							product += ",网银";
						}
						if (cover > 0) {
							product = product.substr(1);
						}
						$("#productCover").html("1、已叙做产品：" + product + "<br>"
								+ "2、产品覆盖率：" + cover + "%");
						//十五层分类信息
						var custyp = custInfo.custyp;//分类
						if("null" == custyp || null == custyp || "" == custyp){
							//custyp = "非五层";
						}
							
						$("#custypInfo").html("1、分类：" + custyp);
					}else{
						$("#baseInfo").html("1、企业类型：<br>2、是否国结客户：<br>"
								+ "3、法人姓名： <br>4、法人联系方式： <br>"
								+ "5、主办客户经理： <br>"
								+"6、主办客户经理联系方式：");
						$("#incomInfo").html("1、存款时点：0万元" + "<br>"
								+ "2、存款日均：0万元");
						$("#loansInfo").html("1、贷款时点：0万元" + "<br>"
								+ "2、贷款日均：0万元");
						$("#revenueInfo").html("1、利息收入：0万元" + "<br>"
								+ "2、非利息收入：0万元");
						$("#kpiInfo").html("分值：");
						$("#productCover").html("1、已叙做产品：<br>"
								+ "2、产品覆盖率：0%");
						//十五层分类信息
						$("#custypInfo").html("1、分类：");
					}
				}
			}
		});
    }
</script>
