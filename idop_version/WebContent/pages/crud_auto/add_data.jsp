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
			<input class="nui-hidden" name="searchFieldType"/>
	       	<div style="padding-left:1px;">
				<table style="table-layout:fixed;">
					<colgroup>
				       	<col width="25%"/>
				       	<col width="20%"/>
				       	<col width="30%"/>
				       	<col width="25%"/>
					</colgroup>
						<tr>
							<td align="right">字段名称:</td>
							<td align="left"><input class="nui-textbox" name="field_name" value="${item_field}" readonly="true" style="width:100px;"/></td>
							<td align="right">关联字典表字段：</td>
							<td align="left">
		                   		<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="item_field" class="nui-combobox" style="width:100px;" textfield="name" valuefield="key" 
                                url="<%=root%>/param/getGl" value="" onvaluechanged="getGlField();"/>                   
		                  	</td>
		              	</tr>
		              	<tr>
		              		<td align="right">关联其他表：</td>
							<td align="left">
									<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="gl_table_name" class="nui-combobox" style="width:100px;" textfield="table_name" valuefield="table_name" 
                                url="<%=root%>/crud_auto/getTableNames" value="" onvaluechanged="getFields();"/>
		                  	</td>
		              		<td align="right">关联字段：</td>
		              		<td align="left">
		              				<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="gl_field" class="nui-combobox" style="width:100px;" textfield="column_name" valuefield="column_name" value=""/>
		              		</td>
		              	</tr>
		              	<tr>
		              		<td align="right">下拉框字段：</td>
		              		<td align="left">
		              			<input showNullItem="true" nullItemText="请选择两个字段" emptyText="请选择两个字段" name="selct_field" class="nui-combobox" style="width:100px;" textfield="column_name" valuefield="column_name" 
                                 multiSelect="true" value=""/>
		              		</td>
		              		<td align="right">设置查询字段：</td>
		              		<td align="left">
		              			<input showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="search_field" class="nui-combobox" style="width:100px;" textfield="column_name" valuefield="column_name" 
                              multiSelect="true" value=""/>
		              		</td>
		              	</tr>
		              	<tr>
		              		<td align="right">设置查询字段名称：</td>
		              		<td align="left">
		              			<input class="nui-textbox" name="search_field_name" style="width:100px;" required="true"/>
		              		</td>
		              		<td align="right">设置查询字段类型：</td>
		              		<td align="left">
		              			<a class="mini-button" iconCls="icon-addnew" onclick="setFieldType();" plain="true">设置字段类型</a>
		              		</td>
		              	</tr>
		              	<tr>
		              		<td align="right">查询字段类型</td>
		              		<td align="left">
		              			<input type="hidden" id="search_field_type"/>
		              			<input class="nui-textbox" emptyText="请设置字段类型" name="field_type" readonly="true" style="width:100px;"/>
		              			<input class="nui-hidden" name="search_field_type"/>
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
 	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
		if(pageType=="edit"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
		    	form.setData(text.record);
		    	var table_name=$G.getbyName("gl_table_name").getValue();
		    	var url="<%=root%>/crud_auto/getFieldsByTableName?table_name="+table_name;
				if(table_name!=''){
					$G.getbyName("gl_field").setUrl(url);
					$G.getbyName("selct_field").setUrl(url);
					$G.getbyName("search_field").setUrl(url);
				}else{
					$G.getbyName("gl_field").setUrl("");
					$G.getbyName("selct_field").setUrl("");
					$G.getbyName("search_field").setUrl("");
					$G.getbyName("gl_field").setValue("");
		 			$G.getbyName("selct_field").setValue("");
		 			$G.getbyName("search_field").setValue("");
		 			$G.getbyName("search_field_name").setValue("");
		 			$G.getbyName("field_type").setValue("");
		 			$G.getbyName("search_field_type").setValue("");
				}
				var tt=$G.getbyName("search_field_type");
				var ttValue=tt.value;
				var paramTypes="";
				var value="";
				if(ttValue.indexOf(",")!=-1){
					var strs= new Array(); //定义一数组 
					strs=ttValue.split(","); //字符分割 
					for (i=0;i<strs.length ;i++ ){
						var value=mini.getDictText("FIELD_TYPE",strs[i]);
						paramTypes+=value+",";
					}
					value=paramTypes.substring(0,paramTypes.length-1);
				}else{
					value=mini.getDictText("FIELD_TYPE",ttValue);
				}
				$G.getbyName("field_type").setValue(value);
				$G.getbyName("field_name").setValue(infos.item_field);
 				$G.getbyName("table_name").setValue(infos.table_name);
 				$G.getbyName("search_field_name").setValue(infos.name);
				
			});
			$G.postByAjax({table_name:'${table_name}',item_field:infos.item_field},"<%=root%>/crud_auto/getGlDetail",ajaxConf);
		}
	}
	function save(){
		if($G.getbyName("item_field").getValue()==""&&$G.getbyName("gl_table_name").getValue()==""){
			$G.alert("请选择关联字典表字段或者关联其他表");
			return false;
		}
		if($G.getbyName("gl_table_name").getValue()!=""){
			var selct_field=$G.getbyName("selct_field").getValue();
			if($G.getbyName("gl_field").getValue()==''){
				$G.alert("请选择关联字段");
				return false;
			}
			if(selct_field==''){
				$G.alert("请选择下拉框字段");
				return false;
			}
			if($G.getbyName("search_field").getValue()==''){
				$G.alert("请设置查询字段");
				return false;
			}
			if($G.getbyName("search_field_name").getValue()==''){
				$G.alert("请设置查询字段名称");
				return false;
			}
			if($G.getbyName("field_type").getValue()==''){
				$G.alert("请设置查询字段类型");
				return false;
			}
			var strs= new Array(); //定义一数组 
			if(selct_field.indexOf(",")!=-1){
				strs=selct_field.split(",");
				if(strs.length>2){
					$G.alert("只能选择两个字段");
					return false;
				}
			}else{
				$G.alert("请选择两个字段");
				return false;
			}
		
		}
    	var urlStr = "<%=root%>/crud_auto/saveGlTableInfo";
	    var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(true);
		ajaxConf.setErrorFunc(function(){
			$G.alert("操作失败！");
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
 	function getGlField(){
 		var table_name=$G.getbyName("gl_table_name").getValue();
 		if(table_name!=''){
 			$G.alert("关联其他表后,就不能关联字典表字段");
 			$G.getbyName("item_field").setValue("");
 			return false;
 		}
 		if($G.getbyName("item_field").getValue()==''){
 			$G.getbyName("search_field_name").setValue("");
 		}
 		
 	}
 	function getFields(){
 		if($G.getbyName("item_field").getValue()!=''){
 			$G.alert("关联字典表字段后,就不能关联其他表");
 			$G.getbyName("gl_table_name").setValue("");
 			return false;
 		}
 		var table_name=$G.getbyName("gl_table_name").getValue();
 		var url="<%=root%>/crud_auto/getFieldsByTableName?table_name="+table_name;
 		if(table_name!=''){
 			$G.getbyName("gl_field").setUrl(url);
 			$G.getbyName("selct_field").setUrl(url);
 			$G.getbyName("search_field").setUrl(url);
 		}else{
 			$G.getbyName("gl_field").setUrl(url);
 			$G.getbyName("selct_field").setUrl(url);
 			$G.getbyName("search_field").setUrl(url);
 			$G.getbyName("gl_field").setValue("");
 			$G.getbyName("selct_field").setValue("");
 			$G.getbyName("search_field").setValue("");
 			$G.getbyName("search_field_name").setValue("");
 			$G.getbyName("field_type").setValue("");
 			$G.getbyName("search_field_type").setValue("");
 		}
 	}
 	function setFieldType(){
 		if($G.getbyName("gl_table_name").getValue()==''){
 			$G.alert("只有关联其他表时，才能设置查询字段类型");
 			return false;
 		}
 		var gl_table_name=$G.getbyName("gl_table_name").getValue();
 		var search_field=$G.getbyName("search_field");
 		var value=search_field.getValue();
 		if(value==''){
 			$G.alert("请先设置查询字段");
 		}
 		var url = "<%=root%>/crud_auto/setFieldType?gl_table_name="+gl_table_name+"&table_name="+$G.getbyName("table_name").getValue()+"&infos="+value;
		var bizParams = {pageType:"add"};
		$G.showmodaldialog("设置查询字段类型", url, 500, 450, bizParams, function(data){
			$G.getbyName("field_type").setValue(data.text);
			$G.getbyName("search_field_type").setValue(data.value);
			$G.getbyName("searchFieldType").setValue("已设置类型");
		});
 	}
 </script>