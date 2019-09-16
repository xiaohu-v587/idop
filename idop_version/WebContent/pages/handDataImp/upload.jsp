<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="panel1" class="nui-panel" title="手工数据导入" style="width:100%;height:150px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
			<div class="mini-fit">
				<input name="pageType" class="nui-hidden" />
				<input name="id" class="nui-hidden" />
				<table style="table-layout: fixed;    padding-top: 20px;" id="detailTable" width="100%">
					<colgroup>
						<col width="15%" />
						<col width="35%" />
					</colgroup>
					<tr>
						<td align="right">上传附件：</td>
						<td align="left"><input class="mini-htmlfile" name="upload_file"  id="file1" style="width:300px;"/></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
			 <table style="width:100%;">
	            <tr>
					<td style="width:100%;">
	                </td>
	                <td style="white-space:nowrap;">
	                	<a class="nui-button"  href="<%=request.getContextPath()%>/xml/Excel模板.xls">模板下载</a>  
	                    <a class="nui-button"  onclick="ajaxFileUpload()">上传</a>
	                </td>
	            </tr>
	       	</table>   
		</div>
	</body>
</html>
<script type="text/javascript">
		mini.parse();
		function ajaxFileUpload() {
		        
		        var inputFile = $("#file1 > input:file")[0];

		        $.ajaxFileUpload({
		            url: '<%=root%>/handDataImp/importData?',                 //用于文件上传的服务器端请求地址
		            fileElementId: inputFile,               //文件上传域的ID
		            //data: { a: 1, b: true },            //附加的额外参数
		            dataType: 'json_pre',                   //返回值类型 一般设置为json
		            success: function (data, status)    //服务器成功响应处理函数
		            {
		            	if(data.code=="0000"){
		            		$G.alert(data.desc,"提示",function(){
		            			//$G.closemodaldialog("ok");
		            		});
		            	}
						if(data.code=="9999"){
							$G.alert(data.desc,"提示",function(){
		            			//$G.closemodaldialog("ok");
		            		});
		            	}

		            },
		            error: function (data, status, e)   //服务器响应失败处理函数
		            {
		                alert(data);
		            },
		            complete: function () {
		                var jq = $("#file1 > input:file");
		                jq.before(inputFile);
		                jq.remove();
		            }
		        });
		    }
		function onCancel(e) {
			$G.closemodaldialog("cancel");
		}
</script>
