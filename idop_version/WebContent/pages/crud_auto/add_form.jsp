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
			<input class="nui-hidden" name="type" value="${type}"/>
			<input class="nui-hidden" name="table_name" value="${table_name}"/>
			<input class="nui-hidden" name="infos" value="${infos}"/>
	       	<div style="padding-left:1px;">
				<table style="table-layout:fixed;">
					<colgroup>
				       	<col width="20%"/>
				       	<col width="15%"/>
				       	<col width="15%"/>
				       	<col width="15%"/>
				       	<col width="15%"/>
				       	<col width="20%"/>
					</colgroup>
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr>
							<td align="right" <c:if test="${pkField==item}">style="color:red;"</c:if>>${item}：</td>
							<td align="left">
		                   		<input name="info_${item}" class="nui-textbox" emptyText="请输入字段名称" required="true" style="width:100px;" <c:forEach items="${txtList}" var="obj" varStatus="k">
							<c:if test="${item==obj[0]}"> value="${obj[1]}"</c:if>
		                  	</c:forEach>/>                   
		                  	</td>
		                  	<td align="right">类型：</td>
							<td align="left">
									<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="type_${item}" class="nui-combobox" style="width:100px;" textfield="remark" valuefield="val" 
                                url="<%=root%>/param/getDictByType?key=FIELD_TYPE&type=${type}" required="true" <c:if test="${pkField==item}"> value="3" readonly="true"</c:if> onitemclick="getDatas('${item}');"/>
		                  	</td>
		                  	<c:if test="${type=='SaveUpdateField'}">
		                  	<td align="right">限制文本长度：</td>
		              		<td align="left">
		                   		<input name="len_${item}" class="nui-textbox" emptyText="请输入限制长度" vtype="int" style="width:100px;" <c:forEach items="${lenList}" var="obj" varStatus="k"><c:if test="${item==obj[0]}"><c:if test="${obj[1]!=0}">value="${obj[1]}"</c:if></c:if></c:forEach>/>                   
		                  	</td>
		                  	</c:if>
		              	</tr>
		              	<c:if test="${type=='SaveUpdateField'}">
		              	<c:if test="${pkField==item}">
		              	<tr>
		                  	<td align="right">是否可编辑：</td>
		                  	<td align="left">
		                  			<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="edit_${item}" class="nui-combobox" style="width:100px;" textfield="remark" valuefield="val" 
                                url="<%=root%>/param/getDict?key=FIELD_EDIT" value="1" readonly="true" required="true"/>
		                  	</td>
		                  	<td align="right">是否必输：</td>
							<td align="left">
		                   		  <input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="input_${item}" class="nui-combobox" style="width:100px;" textfield="remark" valuefield="val" 
                                url="<%=root%>/param/getDict?key=INPUT_FIELD" value="1" readonly="true" required="true"/>               
		                  	</td>
		              	</tr>
		              	</c:if>
		              	<c:if test="${pkField!=item}">
		              	<tr>
		                  	<td align="right">是否可编辑：</td>
		                  	<td align="left">
		                  			<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="edit_${item}" class="nui-combobox" style="width:100px;" textfield="remark" valuefield="val" 
                                url="<%=root%>/param/getDict?key=FIELD_EDIT" required="true"/>
		                  	</td>
		                  	<td align="right">是否必输：</td>
							<td align="left">
		                   		  <input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="input_${item}" class="nui-combobox" style="width:100px;" textfield="remark" valuefield="val" 
                                url="<%=root%>/param/getDict?key=INPUT_FIELD" required="true"/>               
		                  	</td>
		              	</tr>
		              	</c:if>
		              	</c:if>
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
		var urlStr = "<%=root%>/crud_auto/saveField";
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
	var list=info.list;
 	var typeList=info.typeList;
  	var inputList=info.inputList;
  	var editList=info.editList;
 	for(var i in list){
 		for(var j in typeList){
 			if(list[i]==typeList[j][0]){
 				$G.getbyName("type_"+list[i]).setValue(typeList[j][1]);	
 			}
 		}
 		if(inputList.length>0){
 			for(var j in inputList){
 				if(list[i]==inputList[j][0]){
 	 				$G.getbyName("input_"+list[i]).setValue(inputList[j][1]);
 	 			}
 			}
 		}
 		if(editList.length>0){
 			for(var j in editList){
 				if(list[i]==editList[j][0]){
 	 				$G.getbyName("edit_"+list[i]).setValue(editList[j][1]);
 	 			}
 			}
 		}
 		
 	}
	function getDatas(value){
 		var item_data=$G.getbyName("type_"+value).getValue();
 		var name=$G.getbyName("info_"+value).getValue();
 		if(item_data==2){
 			var url = "<%=root%>/crud_auto/addData?item_field="+value+'&table_name=${table_name}';
 			var bizParams = {pageType:"edit",item_field:value,table_name:'${table_name}',name:name};
 			$G.showmodaldialog("关联表信息", url, 520, 360, bizParams, function(action){
 				
 			});
 		}
 	}
 </script>