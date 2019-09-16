<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<body>
    	<div class="nui-fit">
    		<input name="pageType" class="nui-hidden"/>
			<form id="form1" method="post">
	        <input name="id" class="nui-hidden" />
	       	<input name="bz" class="nui-hidden" />
	       	<input name="qjgh" class="nui-hidden" />
	       	<div style="padding-left:11px;padding-bottom:5px;">
				<table style="table-layout:fixed;" align="center">
					<colgroup>
				       	<col width="20%"/>
				       	<col width="30%"/>
				       	<col width="20%"/>
				       	<col width="40%"/>
					</colgroup>
					<tr>
						<td align="right">上级机构名称：</td>
						<td align="left">
	                   		<input name="porg" class="nui-textbox" readonly="readonly"/>                   
	                  	</td>
	                  	<td align="right">上级机构号：</td>
	                  	<td align="left">
	                    	<input name="pnum" class="nui-textbox" readonly="readonly"/> 
	                  	</td>
	              	</tr>
	              	<tr>
						<td align="right">机构名称：</td>
	                  	<td align="left">
	                  		<input name="orgname" class="nui-textbox" required="true" maxlength="25" emptyText="请输入机构名称"/>
	                  	</td>
	                  	<td align="right">机构号：</td>
	                  	<td align="left">
	                   		<input name="orgnum" class="nui-textbox" required="true" maxlength="10" emptyText="请输入机构号" onvalidation="onOrgNumValidation"/>
	                  	</td>
	              	</tr>
	              	<tr>
	              		<td align="right">区域层级：</td>
	              		<td>
	              			<input name="by2" class="nui-combobox" data="[{id:1,text:'省行'},{id:2,text:'分行'},{id:3,text:'支行'},{id:4,text:'责任中心'}]" 
	              				required="true" emptyText="请选择区域层级"/>
	              		</td>
	              	</tr>
	              	<tr>
						<td align="right">联系人：</td>
	                  	<td align="left">
							<input name="linkman" class="nui-textbox"  maxlength="20"  emptyText="请输入联系人"/>                  
						</td>
	                  	<td align="right">联系电话：</td>
	                  	<td align="left">
							<input name="phonenum" class="nui-textbox"  maxlength="20"  emptyText="请输入联系电话"/> 
						</td>
					</tr>
					<tr>
						<td align="right">机构地址：</td>
						<td colspan="3" align="left">    
							<input name="orgadress" class="nui-textbox" maxlength="100" style="width:100%" />
		              	</td>
	              	</tr> 
				</table>
			</div> 
		</form>
	</div>  
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="" onclick="save">确定</a> 
		<a class="nui-button" iconCls="" onclick="onCancel">关闭</a>       
	</div>  
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var form = $G.getForm("form1");
	var orgData;
	
 	function setData(data){
 		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if(pageType=="edit"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.datas);
				orgData=text.datas;
				$G.getbyName("id").setValue(data.id);
				$G.getbyName("porg").setValue(data.parentName);
				$G.getbyName("pnum").setValue(data.parentNum);
			});
			$G.postByAjax({id:infos.id},"<%=root%>/org/getDetail",ajaxConf);
		}else if(pageType=="add"){
			 $G.getbyName("porg").setValue(infos.parentName);
	         $G.getbyName("pnum").setValue(infos.parentNum);
	         $G.getbyName("id").setValue(infos.pid);
		}
 	}

 	/*
	 *保存数据
	 */
	function save(){
		var pageType=$G.getbyName("pageType").getValue();
    	var urlStr = "<%=root%>/org/save";
		if(pageType=="edit"){
			urlStr = "<%=root%>/org/update";
		}
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}

	//验证机构号是否已存在
	function onOrgNumValidation(e){
		var pageType = $G.getbyName("pageType").getValue();
    	if(pageType=="edit" && orgData.orgnum == $G.getbyName("orgnum").getValue()){
        	return;
        }
		if (e.isValid) {
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsAsync(false);
			ajaxConf.setIsShowSuccMsg(false);
			ajaxConf.setIsShowProcessBar(false);
			ajaxConf.setSuccessFunc(function (text) {
				if (text.msg == "1") {
					e.errorText = "该机构号已存在";
					e.isValid = false;
				}
			});
          	var urlStr = "<%=root%>/org/checkOrgNum";
          	var infoKey = e.value;
			$G.postByAjax({orgNum:infoKey},urlStr,ajaxConf);
		}
	}
 	
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    } 
 </script>