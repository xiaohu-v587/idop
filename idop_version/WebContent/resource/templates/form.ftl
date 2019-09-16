<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>新增、修改</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
		<style type="text/css">
			.nui-fit, #form1, .div_fit {
				width: 100%;
			}

			.div_fit {
				padding-top:10px;
			}

			.sub_div_fit {
				float: left;
				width: 48%;
				margin-bottom:10px;
			}
			.sub_div_fit_span1{
				display:block;
				width:28%;
				text-align:right;
				float:left;
			}
			.sub_div_fit_span2{
				display:block;
				width:72%;
				float:left;
			}
		</style>
	</head>
	<body>
    	<div class="nui-fit">
			<form id="form1" method="post">
			<input name="pageType" class="nui-hidden"/>
	        <input name="${pkField?lower_case}" class="nui-hidden" />
	       	<div class="div_fit">
		<#list saveUpdateFields as item>
			<#if '${item[2]}'!='3'>
			<div class="sub_div_fit">
				<span class="sub_div_fit_span1">${item[1]}：</span><#if '${item[2]}'='0'><span class="sub_div_fit_span2">
								<input name="${item[0]?lower_case}" class="nui-textbox"
									<#if '${item[4]}'='0'>readonly="true"</#if> <#if '${item[5]}'='1'>required="true"</#if>/>
							</span></#if> <#if '${item[2]}'='1'><span class="sub_div_fit_span2"> 
								<input name="${item[0]?lower_case}" class="nui-textbox"
									<#if '${item[3]}'!='0'>maxLength="${item[3]}"</#if>
									<#if '${item[4]}'='0'>readonly="true"</#if> <#if '${item[5]}'='1'>required="true"</#if>/>
						</span></#if> <#if '${item[2]}'='2'><span class="sub_div_fit_span2"> 
								<input name="${item[0]?lower_case}" class="nui-combobox" allowInput="true"
									showNullItem="true" nullItemText="请选择..." emptyText="请选择..."
									class="nui-combobox" textfield="name" valuefield="value"
									url="<%=root%>/module_index/getCombobox?table_name=${table_name}&field_name=${item[0]}"
									<#if '${item[4]}'='0'>readonly="true"</#if> <#if '${item[5]}'='1'>required="true"</#if>/>
							
						</span></#if> <#if '${item[2]}'='4'><span class="sub_div_fit_span2"> 
								<input name="${item[0]?lower_case}" format="yyyy-MM-dd"
									class="mini-datepicker"
									<#if '${item[4]}'='0'>readonly="true"</#if> <#if '${item[5]}'='1'>required="true"</#if>/>
							
				</span>
				</#if>
			</div>
			</#if>
		</#list>
			</div> 
		</form>
	</div>  
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a> 
		<a class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>       
	</div>  
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var form = $G.getForm("form1");
	
 	function setData(data){
 		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if (pageType == "edit") {
			var ajaxConf=new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
			ajaxConf.setSuccessFunc(function(text)){
				form.setData(text.datas);
			});
			$G.postByAjax({id:infos.id},"<%=root%>${url_path}/getDetail?table_name=${table_name}",ajaxConf);
		}
 	}

 	/*
	 *新增或修改
	 */
	function save(){
    	var urlStr = "<%=root%>${url_path}/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setErrorFunc(function(){
	    	$G.alert("操作失败！");
	    })
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
 	/*
 	*关闭
 	*/
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    } 
 </script>