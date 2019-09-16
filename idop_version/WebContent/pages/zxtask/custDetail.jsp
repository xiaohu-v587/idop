<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>他行客户</title>
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
		<div class="detail_box">
        	<div class="detail" style="overflow:hidden;padding-top:100px;margin-left:40px;">
            	<span class="pentagon_box">
            		<span class="cust">
            		</span>
                </span>
                <span class="detail_style detail8">
                	<ul  class="detail_u">
               			<li class="detail_t1">
               				潜在原因
               			</li>
               			<li class="detail_t2" id="Info1">
               			</li>
               		</ul>
                </span>
                <span class="detail_style detail9">
                	<ul  class="detail_u">
                		<li class="detail_t1">
               				基本信息
               			</li>
               			<li class="detail_t2" id="Info2">
               			</li>
               		</ul>
                </span>
                <span class="detail_style detail10">
                	<ul  class="detail_u">
                		<li class="detail_t1">
               				营业范围
               			</li>
               			<li class="detail_t2" id="Info3">
               			</li>
               		</ul>
                </span>
                <span class="detail_style detail11">
                	<ul  class="detail_u">
                		<li class="detail_t1">
               				联系方式
               			</li>
               			<li class="detail_t2" id="Info4">
               			</li>
               		</ul>
                </span>
                <span class="detail_style detail12">
                	<ul  class="detail_u">
                		<li class="detail_t1">
               				财务信息
               			</li>
               			<li class="detail_t2" id="Info5">
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
		var url = "<%=root%>/zxtask/findCustDetail";
		$.ajax({
			url: url,
			data: {'pcpId': pcpId},
			success: function (text) {
				if(null != text){
					var custInfo = text.custInfo;
					if(null != custInfo){
						//潜在原因
						var isboccus = custInfo.isboccus;//关系客户名称
						if(null == isboccus || undefined == isboccus){
							isboccus = "";
						}
						var user_name = custInfo.user_name;//关联客户经理姓名
						if(null == user_name){
							user_name = "";
						}
						var phone = custInfo.phone;//关联客户经理手机号码
						if(null == phone){
							phone = "";
						}
						var orgname = custInfo.orgname;//关联客户经理机构归属
						if(null == orgname){
							orgname = "";
						}
						$("#Info1").html("1、关联客户名称：" + isboccus + "<br>"
								+ "2、关联客户经理姓名：" + user_name + "<br>"
								+ "3、关联客户经理手机号码：" + phone + "<br>"
								+ "4、关联客户经理机构归属：" + orgname);
						//基本信息
						var regaddr = custInfo.regaddr;//注册地址
						if(null == regaddr){
							regaddr = "";
						}
						var regdate = custInfo.regdate;//联系方式
						if(null == regdate){
							regdate = "";
						}
						var regcapitalamt = custInfo.regcapitalamt;//注册资本
						if(null == regcapitalamt){
							regcapitalamt = "0";
						}else{
							regcapitalamt = (regcapitalamt/10000).toFixed(2);
						}
						var artifical = custInfo.artifical;//法人
						if(null == artifical){
							artifical = "";
						}
						var m_user_name = custInfo.m_user_name;//主办客户经理
						if(null == m_user_name){
							m_user_name = "";
						}
						var m_phone = custInfo.m_phone;//主办客户经理联系方式
						if(null == m_phone){
							m_phone = "";
						}
						$("#Info2").html("1、注册地址："+ regaddr +"<br>"
								+ "2、注册时间："+ regdate +"<br>"
								+ "2、注册资本："+ regcapitalamt +"万元<br>"
								+ "4、法人姓名：" + artifical + "<br>"
								+ "5、企业类型：" + "<br>"
								+ "6、主办客户经理：" + m_user_name + "<br>"
								+ "7、主办客户经理联系方式：" + m_phone);
						//营业范围
						var generalproduct = custInfo.generalproduct;//主营业务
						if(null == generalproduct){
							generalproduct = "";
						}
						var industry_name = custInfo.industry_name;//所属行业
						if(null == industry_name){
							industry_name = "";
						}
						$("#Info3").html("1、主营业务：" + generalproduct + "<br>"
								+ "2、所属行业：" + industry_name);
						//联系方式
						var telno = custInfo.telno;
						if(null == telno){
							telno = "";
						}
						$("#Info4").html("1、联系方式：" + telno);
						//财务信息
						$("#Info5").html("1、上年主营业务收入：<br>"
								+ "2、上年净利润：");
					}else{
						$("#Info1").html("1、关联客户名称：<br>"
								+ "2、关联客户经理姓名：<br>"
								+ "3、关联客户经理手机号码：<br>"
								+ "4、关联客户经理机构归属：");
						$("#Info2").html("1、注册地址：<br>"
								+ "2、注册时间：<br>"
								+ "2、注册资本：<br>"
								+ "4、法人姓名：<br>"
								+ "5、企业类型：<br>"
								+ "6、主办客户经理：<br>"
								+ "7、主办客户经理联系方式：");
						$("#Info3").html("1、主营业务：<br>"
								+ "2、所属行业：");
						$("#Info4").html("1、联系方式：");
						//财务信息
						$("#Info5").html("1、上年主营业务收入：<br>"
								+ "2、上年净利润：");
					}
				}
			}
		});
    }
</script>
