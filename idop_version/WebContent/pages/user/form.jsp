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
	<div class="mini-fit">
		<input name="pageType" class="nui-hidden"/>      
		<form id="form1" method="post">
        	<input name="id" class="nui-hidden"/>
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
               	<tr>
                   	<td align="right">登陆名：</td>
                   	<td align="left">    
                       	<input name="user_no" class="nui-textbox" required="true" readOnly="true"/>
                   	</td>
                   	<td align="right">用户姓名：</td>
                    <td align="left">    
                        <input name="name" class="nui-textbox" required="true" />
                    </td>
               	</tr>
				<tr>
                   	<td align="right">机构：</td>
                   	<td align="left">    
						<input id="orgid" required="true" class="nui-treeselect" url="<%=root%>/org/getListByUser?noliketype=2" dataField="datas" 
							name="org_id" textfield="orgname" valuefield="id" parentfield="upid"  valueFromSelect="false" multiSelect="false"
    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="onCloseClick" showRadioButton="true" showFolderCheckBox="false"
       						popupWidth="280" popupHeight="300" popupMaxHeight="350"/>
					</td>
                   <td align="right">普通角色：</td>
					<td align="left">    
                       	<input name="common_role" class="nui-combobox" textfield="name" valuefield="id" url="<%=root%>/user/getCommonRoleList" multiSelect="true"
                       		emptyText="请选择普通角色" popupWidth="200" popupHeight="350" />
                   	</td>  
				</tr>
				<tr>
                   	<td align="right">电话号码：</td>
                    <td align="left">    
                        <input name="phone" class="nui-textbox" required="true" />
                    </td>
                    <td align="right">是否同步人资机构：</td>
					<td align="left">    
                       	<input name="hr_syn" class="nui-combobox" textfield="remark" valuefield="val" url="<%=root%>/param/getDict?key=hr_syn" onvaluechanged="synValueChanged"/>
                   	</td> 
				</tr>
				<tr>
                   	<td align="right">同步日期：</td>
                    <td align="left">    
                        <input name="syn_date" class="nui-datepicker" enabled="false" required="true"/>
                    </td>
				</tr>
			</table>
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

	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if(pageType=="edit"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.record);
				if ($G.getbyName('hr_syn').getValue() == "1") {
					$G.getbyName('syn_date').enable();
				}
			});
			$G.postByAjax({id:infos.id},"<%=root%>/user/getDetail",ajaxConf);
		}
	}
	
	/*
	 *保存数据
	 */
	function save(){
		
// 		var base_role = $G.getbyName("base_role").getValue();
		var common_role = $G.getbyName("common_role").getValue();
		/* if ((base_role == null || base_role == '') && (common_role == null || common_role == '')) {
			$G.alert("角色不能同时为空！");
			return;
		} */
    	var urlStr = "<%=root%>/user/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.result=='fail'){
	    		$G.alert("登陆名不能重复");
	    	}else{
	    		$G.alert("操作成功");
	    		$G.closemodaldialog("ok");
	    	}
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	function synValueChanged(e) {
		var synDate = $G.getbyName("syn_date");
		if (e.value == "1") {
			synDate.enable();
		} else {
			synDate.setValue("");
			synDate.disable();
		}
	}
</script>