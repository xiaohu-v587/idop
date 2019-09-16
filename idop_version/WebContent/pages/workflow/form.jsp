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
				<colgroup>
				       	<col width="15%"/>
				       	<col width="40%"/>
				       	<col width="15%"/>
				       	<col width="40%"/>
				</colgroup>
               	<tr>
                   	<td align="right">登陆名：</td>
                   	<td align="left">    
                       	<input name="user_no" class="nui-textbox" required="true"/>
                   	</td>
                   	<td align="right">用户姓名：</td>
                    <td align="left">    
                        <input name="name" class="nui-textbox" required="true" />
                    </td>
               	</tr>
				<tr>
                   	<td align="right">机构：</td>
                   	<td align="left">    
						<input id="orgid" required="true" class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
							name="org_id" textfield="orgname" valuefield="id" parentfield="upid"  valueFromSelect="false" multiSelect="false"
    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="onCloseClick" showRadioButton="true" showFolderCheckBox="false"
       						popupWidth="205" popupHeight="150" popupMaxHeight="180"/>
					</td>
                    <td align="right">角色：</td>
					<td align="left">    
                       	<input name="role_id" class="nui-combobox" textfield="name" valuefield="id" url="<%=root%>/user/getRoleList" required="true"  
                       		emptyText="请选择角色" />
                   	</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a> 
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
		if(pageType=="edit"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.record);
			});
			$G.postByAjax({id:infos.id},"<%=root%>/user/getDetail",ajaxConf);
		}
	}
	
	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/user/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }    
</script>
