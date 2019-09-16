<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div class="mini-fit">
			<input class="mini-hidden" name="pageType" value="0" />
			<form id="form1" method="post">
				<input name="id" class="mini-hidden" />
				<input name="role_level" class="nui-hidden" value="0"/>
				<table >
					<colgroup>
						<col width="10%"/>
						<col width="25%"/>
                        <col width="25%"/>
                        <col width="35%"/>
					</colgroup>
					<tr>
						<td align="right">角色名称：</td>
						<td align="left"  >    
							<input id="jsmc" style="width:220px;"  name="name" emptyText="角色名称" class="mini-textbox" maxlength="20" required="true" onvalidation="onJsmcValidation"/>
                         </td>
					</tr>
					<tr>
						<td align="right">角色级别：</td>
						<td align="left" >  
							<input name="role_level" class="mini-combobox" allowInput="false" style="width:220px;" 
	                   			textfield="remark" valuefield="val" required="true"
	                   			url="<%=root%>/param/getKeyList?key=ROLE_LEVEL"/>
						</td>
					</tr>
					<tr>
						<td align="right">上级角色：</td>
						<td align="left" >    
							<input id="key1" style="width:220px;" name="pid" class="mini-combobox" style="width:125px;" textfield="name" valuefield="id" emptyText="请选择..."
                                url="<%=root%>/role/getCombox" onvalidation="onPidValidation"  showNullItem="true" nullItemText="请选择..." />
						</td>
					</tr>
					<tr>
						<td align="right">角色描述：</td>
						<td align="left" >    
							<input name="remark" style="width:220px;" class="mini-textarea" style="width:140" vtype="rangeChar:0,128"   emptyText="角色描述"/>
						</td>
					</tr>
					<tr>
						<td align="right"></td>
						<td align="left" >    
							<input name="role_type" class="mini-checkbox" vtype="rangeChar:0,128" >基础角色</input>
						</td>
					</tr>
				</table>
           </form>
		</div>
		<div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
			borderStyle="border-left:0;border-bottom:0;border-right:0;">
            <a id="btnOk" class="mini-button" iconCls="" onclick="onOk()" style="margin-right: 20px;">确定</a>
            <a id="btnCancle" class="mini-button" iconCls="" onclick="onCancel()" >取消</a>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var roleData;
	
	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if(pageType=="edit"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
			    roleData=text.record;
				form.setData(text.record);
			});
			$G.postByAjax({key:infos.id},"<%=root%>/role/getDetail",ajaxConf);
		}
	}

	function onJsmcValidation(e){
		var pageType = $G.getbyName("pageType").getValue();
    	if(pageType=="edit" && roleData.name == $G.getbyName("name").getValue()){
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
          	var urlStr = "<%=root%>/role/check";
          	var infoKey = e.value;
			$G.postByAjax({name:infoKey},urlStr,ajaxConf);
		}
    }

	function onPidValidation(e){
		if (e.isValid) {
			var name = $G.getbyName("name").getValue();
			var pname = $G.getbyName("pid").getText();
			if(pname==name){
				e.errorText = "请不要将该角色自身设为上级角色";
				e.isValid = false;
			}
		}
	}
	
	function onOk(e){
		var urlStr = "<%=root%>/role/save";
		var pageType = $G.getbyName("pageType").getValue();
		if(pageType=="edit"){
			urlStr = "<%=root%>/role/update";
		}
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsAsync(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setSuccessFunc(function (text) {
			$G.closemodaldialog("ok");
		});
		$G.submitForm("form1",urlStr,ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
</script>