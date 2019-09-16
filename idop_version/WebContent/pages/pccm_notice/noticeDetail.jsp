<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<script src="<%=root%>/resource/nui/resource/swfupload/swfupload.js"></script>
<title>公告详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/nuires.jsp"%>
</head>
<body>
	<div class="mini-fit">
		<form id="form1" method="post" action="<%=root%>/pccm_notice/save">
			<table style="table-layout: fixed;" id="detailTable" width="100%">
				<colgroup>
					<col width="20%" />
					<col width="30%" />
					<col width="20%" />
					<col width="30%" />
				</colgroup>
				<tr>
					<td align="right">公告标题：</td>
					<td align="left"><input name="title" class="nui-textbox"/></td>
					<td align="right">类型：</td>
					<td align="left"><input name="notice_type"
						class="nui-combobox" textfield="name" valuefield="val"
						url="<%=root%>/zxparam/getDict?key=NOTICE_TYPE"/></td>
				</tr>
				<tr>
					<td align="right">内容：</td>
					<td align="left" colspan="3">
						<textarea class="mini-textarea" name="content" 
							style="width:89.5%;height: 130px;"></textarea>
					</td>
				</tr>
				<tr>
					<td align="right">附件：</td>
					<td align="left" colspan="3">
						<input name="file_url"  style="width:83%;" class="nui-textbox"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="nui-toolbar"
		style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>
	</div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	function setData(data){
		var infos = $G.clone(data);
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
			form.setData(text.record);
		});
		$G.postByAjax({id:infos.id},"<%=root%>/pccm_notice/getDetail",ajaxConf);
	}

	function onCancel(e) {
		$G.closemodaldialog("cancel");
	}
</script>