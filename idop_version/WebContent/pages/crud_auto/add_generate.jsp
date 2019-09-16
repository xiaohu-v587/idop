<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>生成代码</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head> 
	<body>
		<div class="nui-fit">
			<form id="form1" method="post">
	       	<div style="padding-left:7px;">
				<table style="table-layout:fixed;">
					<colgroup>
				       	<col width="20%"/>
				       	<col width="30%"/>
				       	<col width="20%"/>
				       	<col width="30%"/>
					</colgroup>
	              		<tr>
							<td align="right">表名：</td>
							<td align="left">
		                   		<input name="table_name" class="nui-textbox" readonly="true" value="${table_name}"/>                   
		                  	</td>
							<td align="right">包名：</td>
							<td align="left">
		                   		<input name="packageName" class="nui-textbox" emptyText="如：com.goodcol"  required="true"/>
		                  	</td>
		              	</tr>
		              	<tr>
		              		<td align="right">作者：</td>
							<td align="left">
		                   		<input name="author" class="nui-textbox" emptyText="请输入作者" required="true"/>                   
		                  	</td>
		                  		<td align="right">请求路径：</td>
		                  	<td align="left">
		                   		<input name="url_path" class="nui-textbox" emptyText="如：/ctl" required="true"/>                   
		                  	</td>
		              	</tr>
		              	<tr>
		              		<td align="right">控制器类名：</td>
		                  	<td align="left">
		                   		<input name="classN" class="nui-textbox" emptyText="请输入控制器类名" required="true"/>                   
		                  	</td>
		              		<td align="right">增改对话框宽度：</td>
		              		<td align="left">
		              			<input class="nui-textbox" name="dialog_width" 	vtype="int" required="true" value="${record.dialog_width}"/>
		              		</td>
		              	</tr>
		              	<tr>
		              		<td align="right">增改对话框高度：</td>
		              		<td align="left">
		              			<input class="nui-textbox" name="dialog_height" vtype="int" required="true" value="${record.dialog_height}"/>
		              		</td>
		              		<td align="right">模板路径：</td>
		                  	<td align="left">
		                   		<input name="template_path" class="nui-textbox" emptyText="请输入模板路径" required="true" value="${template_path}" readonly="true"/>                   
		                  	</td>
		              	</tr>
		              	<tr>
		              		<td align="right">生成路径：</td>
		                  	<td align="left">
		                   		<input name="dest_path" class="nui-textbox" emptyText="请输入生成路径" required="true" value="${dest_path}${table_name}" readonly="true"/>                 
		                  	</td>
		              	</tr>
				</table>
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
	function save(){
    	var urlStr = "<%=root%>/crud_auto/generateCode";
	    var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(true);
		ajaxConf.setErrorFunc(function(){
			$G.alert("代码生成失败");
		})
		ajaxConf.setSuccessFunc(function (text){
		    	if(text.result=='success'){
		    		$G.closemodaldialog("ok");
		    	}
		    });
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    } 
 </script>