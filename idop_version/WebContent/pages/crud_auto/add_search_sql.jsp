<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>配置属性</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  			.mini-textarea{
  				margin-top:10px;
  			}
  		</style>
	</head> 
	<body>
		<div class="nui-fit" style="width:100%;padding-left:1%;">
			<form id="form1" method="post">
			<input class="nui-hidden" name="table_name" value="${table_name}"/>
	       	<div style="width:100%;">
				select语句：<textarea class="mini-textarea" name="sqlContent" emptyText="定义select语句，以select开头" style="width:98%;height:100px;" required="true">${sql_content}</textarea>
				form语句：<textarea class="mini-textarea" name="extrasqlContent" emptyText="定义from语句，以from开头" style="width:98%;height:80px;" required="true">${extra_sql_content}</textarea>
				where语句：<textarea class="mini-textarea" name="whereSqlContent" emptyText="定义where语句，以where开头,不需要加上 【and】条件判断，会在后台自动生成" style="width:98%;height:50px;" required="true">${where_sql_content}</textarea>
				order by语句：<textarea class="mini-textarea" name="orderBySqlContent" emptyText="定义order by 语句，以order by 开头" style="width:98%;height:50px;">${order_by_sql_content}</textarea>
				列表字段：<textarea class="mini-textarea" name="fields" emptyText="定义查询出来的字段，select语句中定义的查询字段必须拥有此框中的字段，否则列表页面将不会显示" style="width:98%;height:50px;" required="true">${fields}</textarea>
				字段类型：<textarea class="mini-textarea" name="fieldTypes" emptyText="定义查询出来的字段类型" style="width:98%;height:42px;" required="true">${field_types}</textarea>
				字段名称：<textarea class="mini-textarea" name="listFieldNames" emptyText="定义列表字段名称" style="width:98%;height:42px;" required="true">${list_field_names}</textarea>
				<div style="color:red;">注意：以上内容自动生成，可自定义【select语句框中以select 开头;from语句框中以from 开头;where语句框中以where 开头且不需要加上'and'条件判断，因为后台自动生成;order by语句框中以order by 开头;列表字段框中的字段必须是select语句中定义的查询字段中拥有的; 列表字段、字段类型、字段名称框中以英文逗号分割的值的个数要相等,英文逗号后面必须跟值。否则生成模块会出现异常】。字段类型：${strBuffer}</div>
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
		if($G.getbyName("sqlContent").getValue().indexOf("select ")!=0){
			$G.alert("select语句框必须以select 开头");
			return false;
		}
		if($G.getbyName("extrasqlContent").getValue().indexOf("from ")!=0){
			$G.alert("from语句框必须以from 开头");
			return false;
		}
		if($G.getbyName("whereSqlContent").getValue().indexOf("where ")!=0){
			$G.alert("where语句框必须以where 开头");
			return false;
		}
		if($G.getbyName("orderBySqlContent").getValue()!=""){
			if($G.getbyName("orderBySqlContent").getValue().indexOf("order by ")!=0){
				$G.alert("order by语句框必须以order by 开头");
				return false;
			}
		}
		var fields=$G.getbyName("fields").getValue();
		var fieldTypes=$G.getbyName("fieldTypes").getValue();
		var listFieldNames=$G.getbyName("listFieldNames").getValue();
		var strFields= new Array();
		var strFieldTypes=new Array();
		var strListFieldNames=new Array();
		if(fields.indexOf(",")!=-1){
			strFields=fields.split(",");
		}else{
			strFields.push(fields);
		}
		if(fieldTypes.indexOf(",")!=-1){
			strFieldTypes=fieldTypes.split(",");
		}else{
			strFieldTypes.push(fieldTypes);
		}
		if(listFieldNames.indexOf(",")!=-1){
			strListFieldNames=listFieldNames.split(",");
		}else{
			strListFieldNames.push(listFieldNames);
		}
		if(strFields.length!=strFieldTypes.length){
			$G.alert("列表字段与字段类型框中以英文逗号分割的值的个数要相等");
			return false;
		}
		if(strFields.length!=strListFieldNames.length){
			$G.alert("列表字段与字段名称框中以英文逗号分割的值的个数要相等");
			return false;
		}
		if(strFieldTypes.length!=strListFieldNames.length){
			$G.alert("字段类型与字段名称框中以英文逗号分割的值的个数要相等");
			return false;
		}
    	var urlStr = "<%=root%>/crud_auto/saveSqlContent";
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
 </script>