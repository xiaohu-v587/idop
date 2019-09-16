<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<title>配置属性</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/nuires.jsp"%>
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
			<input name="${pkField}" class="nui-hidden"/> 
			<input class="nui-hidden"
				name="table_name" value="${table_name}" />
			<div class="div_fit">
				<c:forEach items="${sufieldList}" var="item">
					<c:if test="${item[2]!=3&&item[2]!=5&&item[2]!=6&&item[2]!=7}">
					<div class="sub_div_fit">
						<span class="sub_div_fit_span1">${item[1]}：</span><c:if test="${item[2]==0}"><span class="sub_div_fit_span2">
								<input name="${item[0]}" class="nui-textbox"
									<c:if test="${item[4]==0}">readonly="true"</c:if> <c:if test="${item[5]==1}">required="true"</c:if>/>
							</span></c:if>  <c:if test="${item[2]==1}"><span class="sub_div_fit_span2">
								<input name="${item[0]}" class="nui-textbox"
									<c:if test="${item[3]!=0}">maxLength="${item[3]}"</c:if>
									<c:if test="${item[4]==0}">readonly="true"</c:if> <c:if test="${item[5]==1}">required="true"</c:if>/>
						</span></c:if>  <c:if test="${item[2]==2}"><span class="sub_div_fit_span2">
								<input name="${item[0]}" class="nui-combobox" allowInput="true"
									showNullItem="true" nullItemText="请选择..." emptyText="请选择..."
									class="nui-combobox" textfield="name" valuefield="value"
									url="<%=root%>/module_index/getCombobox?table_name=${table_name}&field_name=${item[0]}"
									<c:if test="${item[4]==0}">readonly="true"</c:if> <c:if test="${item[5]==1}">required="true"</c:if>/>
						</span></c:if>  <c:if test="${item[2]==4}"><span class="sub_div_fit_span2">
								<input name="${item[0]}" format="yyyy-MM-dd"
									class="mini-datepicker"
									<c:if test="${item[4]==0}">readonly="true"</c:if> <c:if test="${item[5]==1}">required="true"</c:if>/>
						</span>
						</c:if>
					</div>
					</c:if>
				</c:forEach>
			</div>
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
		if (pageType == "edit") {
			var ajaxConf=new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
			ajaxConf.setSuccessFunc(function(text){
				form.setData(text.datas);
				$G.getbyName("table_name").setValue('${table_name}');
				$G.getbyName("${pkField}").setValue('${uuid}');
			});
			$G.postByAjax({id:infos.id},"<%=root%>/module_index/module_getDetail?table_name=${table_name}",ajaxConf);
		}
 	}

	function save(){
    	var urlStr = "<%=root%>/module_index/module_save";
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(true);
		ajaxConf.setErrorFunc(function() {
			$G.alert("操作失败！");
		})
		ajaxConf.setSuccessFunc(function(text) {
			if (text.flag == 1) {
				$G.closemodaldialog("ok");
			}
		});
		$G.submitForm("form1", urlStr, ajaxConf);
	}
	function onCancel(e) {
		$G.closemodaldialog("ok");
	}
</script>