<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<script type="text/javascript" src="<%=root%>/resource/nui/swfupload/swfupload.js"></script>
<script type="text/javascript">
		</script>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="layout1" class="mini-layout" style="width:100%;height:100%;"  borderStyle="border:solid 1px #aaa;">
		    <div title="center" region="center"  >
				 <div>
				           文件：<input class="mini-htmlfile" name="Fdata"  id="file1" style="width:300px;"/>
				      <input type="button" value="上传" onclick="ajaxFileUpload()"/>
				  </div>
   	 		</div>
		</div>
	</body>
</html>
<script type="text/javascript">
		mini.parse();
		function ajaxFileUpload() {
		        
		        var inputFile = $("#file1 > input:file")[0];

		        $.ajaxFileUpload({
		            url: '<%=root%>/workflow/deploy',                 //用于文件上传的服务器端请求地址
		            fileElementId: inputFile,               //文件上传域的ID
		            //data: { a: 1, b: true },            //附加的额外参数
		            dataType: 'json_pre',                   //返回值类型 一般设置为json
		            success: function (data, status)    //服务器成功响应处理函数
		            {
		            	if(data.code=="0000"){
		            		$G.alert("上传成功: " + data.desc,"提示",function(){
		            			$G.closemodaldialog("ok");
		            		});
		            	}
						if(data.code=="9999"){
							$G.alert("上传失败: " + data.desc,"提示",function(){
		            			$G.closemodaldialog("ok");
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
</script>
