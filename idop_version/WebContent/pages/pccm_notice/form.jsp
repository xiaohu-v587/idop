<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<script src="<%=root%>/resource/nui/resource/swfupload/swfupload.js"></script>
<title>新增</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/nuires.jsp"%>
</head>
<body>
	<div class="mini-fit">
		<input name="pageType" class="nui-hidden" />
		<form id="form1" method="post" action="<%=root%>/pccm_notice/save">
			<input name="id" class="nui-hidden" />
			<table style="table-layout: fixed;" id="detailTable" width="100%">
				<colgroup>
					<col width="20%" />
					<col width="30%" />
					<col width="20%" />
					<col width="30%" />
				</colgroup>
				<tr>
					<td align="right">公告标题：</td>
					<td align="left"><input name="title" class="nui-textbox"
						required="true" emptyText="请输入标题" maxlength="30"/></td>
					<td align="right">类型：</td>
					<td align="left"><input name="notice_type"
						class="nui-combobox" textfield="name" valuefield="val"
						url="<%=root%>/zxparam/getDict?key=NOTICE_TYPE" required="true"
						allowInput="true" showNullItem="true" nullItemText="请选择"
						emptyText="请选择" /></td>
				</tr>
				<tr>
					<td align="right">内容：</td>
					<td align="left"><textarea class="mini-textarea"
							name="content" emptyText="请输入内容"
							style="width: 373px; height: 130px;" required="true" maxlength="1000"></textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="nui-toolbar"
		style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a> <a
			class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>
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
			$G.postByAjax({id:infos.id},"<%=root%>/pccm_notice/getDetail",ajaxConf);
		}
	}

	/*
	 *保存数据
	 */
	 function save(){
		 var urlStr = "<%=root%>/pccm_notice/save";
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(true);
		ajaxConf.setSuccessFunc(function(text) {
			if (text.flag == 1) {
				$G.closemodaldialog("ok");
			} else {
				ajaxConf.setIsShowSuccMsg(false);
				$G.alert(text.msg);
			}
		});
		$G.submitForm("form1", urlStr, ajaxConf);
	}
	function onCancel(e) {
		$G.closemodaldialog("cancel");
	}
</script>