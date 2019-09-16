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
				       	<col width="20%"/>
				       	<col width="25%"/>
				       	<col width="25%"/>
				       	<col width="30%"/>
					</colgroup>
					<c:forEach items="${infolist}" var="item" varStatus="i">
						<tr>
							<td align="right">${item}：</td>
							<td align="left">
		                   		<input name="info_${item}" class="nui-textbox" emptyText="请输入字段长度" vtype="int" style="width:100px;"<c:forEach items="${list}" var="obj"><c:if test="${item==obj[0]}"><c:if test="${obj[1]!=0}">value="${obj[1]}"</c:if></c:if></c:forEach>/>                   
		                  	</td>
		                  	<td align="right">是否可编辑：</td>
							<td align="left">
									<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="edit_${item}" class="nui-combobox" style="width:100px;" textfield="remark" valuefield="val" 
                                url="<%=root%>/param/getDict?key=FIELD_EDIT" required="true"/>
		                  	</td>
		              	</tr>
	              	</c:forEach>
	              	
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
    	var urlStr = "<%=root%>/crud_auto/saveProperty";
	    var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(true);
		ajaxConf.setErrorFunc(function(){
			$G.alert("设置失败！");
		})
		ajaxConf.setSuccessFunc(function (text){
		    	if(text.flag==1){
		    		$G.closemodaldialog("ok");
		    	}
		    });
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    }
 	var info=JSON.parse('${data}');
 	var infolist=info.infolist;
 	var list=info.list;
 	for(var i in infolist){
 		for(var j in list){
 			if(infolist[i]==list[j][0]){
 				$G.getbyName("edit_"+infolist[i]).setValue(list[j][2]);
 			}
 		}
 	}
 		
 </script>