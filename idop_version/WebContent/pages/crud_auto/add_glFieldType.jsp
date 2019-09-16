<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>配置属性</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head> 
	<body>
		<div class="nui-fit">
			<form id="form1" method="post">
			<input class="nui-hidden" name="table_name" value="${table_name}"/>
			<input class="nui-hidden" name="infos" value="${infos}"/>
	       	<div style="padding-left:10px;">
				<table style="table-layout:fixed;">
					<colgroup>
				       	<col width="35%"/>
				       	<col width="65%"/>
					</colgroup>
					<c:forEach items="${list}" var="item">
						<tr>
							<td align="right">${item}：</td>
							<td align="left">
		                   			<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="type_${item}" class="nui-combobox" style="width:100px;" textfield="remark" valuefield="val" 
                                url="<%=root%>/param/getGlFieldType?key=FIELD_TYPE" required="true"/>                   
		                  	</td>
		              	</tr>
					</c:forEach>	              	
				</table>
			</div> 
		</form>
	</div>  
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a>
<!-- 		<a class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>        -->
	</div>
	</body>
</html>
 <script type="text/javascript">
 	$G.parse();
 	var form = $G.getForm("form1");
 	function getData() {
 		var objs = eval(${listArr});
 		var paramValues="";
 		var paramTexts="";
 		for(var i=0;i<objs.length;i++){
 			var typeValue=$G.getbyName("type_"+objs[i]).value;
 			var typeText=$G.getbyName("type_"+objs[i]).text;
 			paramValues+=typeValue+","
 			paramTexts+=typeText+",";
 		}
 		paramValues=paramValues.substring(0,paramValues.length-1); 
 		paramTexts=paramTexts.substring(0,paramTexts.length-1);
 		var obj = new Object(); 
 		obj.value = paramValues; 
 		obj.text = paramTexts; 
 		return obj;
	}
	function save(){
		var objs = eval(${listArr});
 		for(var i=0;i<objs.length;i++){
 			var typeValue=$G.getbyName("type_"+objs[i]).value;
 			if(typeValue==''){
 				$G.alert("请选择类型");
 				return false;
 			}
 		}
		$G.closemodaldialog("ok");

	}
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    }
 		
 </script>