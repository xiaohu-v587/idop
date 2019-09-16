<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>配置属性</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  			.table_left{
  				padding:10px;
  				width:35%;
  				height:100%;
  				float:left;
  				overflow-y:scroll;
  				box-sizing:border-box;
  			}
  			.table1,.table2{
  				 border-collapse:collapse;
  			}
  			.table1 tr{
        		border-bottom: 1px solid #F0F0F0; 
  			}
  			.table2 tr td,.table1 tr td{
  				text-align:center;
  			}
  			.table2{
  				width:100%;
  			}
  			.table2 thead tr td{
  				color:#9A9A9A;
  			} 
  			.table2 tr td{
  				border:1px solid #E7EBEF;
  			}
  			.table2 tbody tr td{
  				color:#FFA757;
  			}
  			.detail_right{
  				width:65%;
  				height:100%;
  				float:left;
  				overflow-y:scroll;
  			}
  			a{
  				text-decoration:none;
  				 cursor:pointer;
  			}
  			.td_color{
  				color:red;
  			}
  			.mini-combobox{
  				padding-bottom:6px ! important;
  			}
  		</style>
	</head> 
	<body>
		<input type="hidden" id="query_criteria" value="${infoMap['query_criteria']}"/>
		<input type="hidden" id="list_field" value="${infoMap['list_field']}"/>
		<input type="hidden" id="save_update_field" value="${infoMap['save_update_field']}"/>
		<input type="hidden" id="table_name" value="${table_name}"/>
		<div class="table_left">
			<table class="table1">
				<thead>
					<tr>
						<td>表名</td><td><input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="table_name" class="nui-combobox" style="width:145px;" textfield="table_name" valuefield="table_name" 
                                url="<%=root%>/crud_auto/getTableNames" onvaluechanged="getFlow()"/></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${records}" var="list">
						<tr>
							<td colspan="2">
								<a href="javascript:void(0);" onclick="qiehuan(this,'${list.table_name}')" <c:if test="${table_name==list.table_name}">class="td_color"</c:if>>${list.table_name}</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="detail_right">
			<a class="mini-button" iconCls="icon-addnew" onclick="addQueryCriteria('');" plain="true">设置查询条件</a>
			<a class="mini-button" iconCls="icon-addnew" onclick="addListField('');" plain="true">设置列表字段</a>
			<a class="mini-button" iconCls="icon-addnew" onclick="addSaveUpdateField('');" plain="true">设置增、改字段</a>
			<a class="mini-button" iconCls="icon-addnew" onclick="addSql('0');" plain="true">设置新查询语句</a>
			<a class="mini-button" iconCls="icon-addnew" onclick="addSql('1');" plain="true">设置已定义查询语句</a>
			<a class="mini-button" iconCls="icon-add" onclick="generateModule();" plain="true">生成模块</a>
			<a class="mini-button" iconCls="icon-add" onclick="generateCode();" plain="true">生成代码</a>
			<table class="table2">
				<thead>
					<tr>
						<td><input type="checkbox" id="main_box" onchange="selectAll();"/></td><td width="90">表名</td><td width="90">字段名</td>
						<td>是否为查询条件</td><td>是否为列表字段</td><td>是否为增、改字段</td><td width="60">注释</td>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${listRecords}" var="item">
					<c:forEach items="${rs}" var="list">
					<c:if test="${list.column_name==item.column_name}">
					<tr>
						<td><input type="checkbox" value="${list.column_name}" name="box"/></td><td>${list.table_name}</td><td>${list.column_name}</td>
						<td><c:if test="${fn:contains((',').concat(record.query_criteria).concat(','),(',').concat(item.column_name).concat(','))}"><a title="可点击" href="javascript:void(0);" style="color:red;" onclick="addQueryCriteria('${infoMap['query_criteria']}')">√(可点击)</a></c:if></td><td><c:if test="${fn:contains((',').concat(record.list_field).concat(','),(',').concat(item.column_name).concat(','))}"><a title="可点击" href="javascript:void(0);" onclick="addListField('${infoMap['list_field']}')" style="color:red;">√(可点击)</a></c:if></td><td><c:if test="${fn:contains((',').concat(record.save_update_field).concat(','),(',').concat(item.column_name).concat(','))}"><a title="可点击" href="javascript:void(0);" onclick="addSaveUpdateField('${infoMap['save_update_field']}')" style="color:red;">√(可点击)</a></c:if></td><td title="${list.comments}">${list.comments}</td>
					</tr>
					</c:if>
				</c:forEach>
				</c:forEach>
				
				</tbody>
			</table>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	function getFlow(){
		var table_name = $G.getbyName("table_name").getValue();
		$("#table_name").val(table_name);
		if(table_name!=''){
			$(".table1 tbody").empty();
			$(".table1 tbody").append("<tr><td colspan='2' class='td_color'>"+table_name+"</td></tr>");
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(false);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				var listRecords=text.listRecords;
		    	var arr=text.records;
		    	var data=text.infoMap;
		    	var record=text.record;
		    	var query_criteria=','+record.query_criteria+',';
		    	var list_field=','+record.list_field+',';
		    	var save_update_field=','+record.save_update_field+',';
				$(".table2 tbody").empty();
				for(var n=0;n<listRecords.length;n++){
					for(var i=0;i<arr.length;i++){
						if(arr[i].column_name==listRecords[n].column_name){
							var cname=','+arr[i].column_name+',';
							var qc="";
							if(query_criteria.indexOf(cname)>=0){
								qc='√(可点击)';
							}
							var lf="";
							if(list_field.indexOf(cname)>=0){
								lf="√(可点击)";
							}
							var suf="";
							if(save_update_field.indexOf(cname)>=0){
								suf="√(可点击)";
							}
							$(".table2 tbody").append("<tr><td><input type='checkbox' name='box' value='"+arr[i].column_name+"'/></td><td>"+arr[i].table_name+"</td><td>"+arr[i].column_name+"</td><td><a title='可点击' href='javascript:void(0);' onclick='addQueryCriteria("+'"'+record.query_criteria+'"'+")' style='color:red;'>"+qc+"</a></td><td><a title='可点击' href='javascript:void(0);' onclick='addListField("+'"'+record.list_field+'"'+")' style='color:red;'>"+lf+"</a></td><td><a title='可点击' href='javascript:void(0);' onclick='addSaveUpdateField("+'"'+record.save_update_field+'"'+")' style='color:red;'>"+suf+"</a></td><td>"+arr[i].comments+"</td></tr>");
						}
					}
				}
				$("#query_criteria").val(data.query_criteria);
				$("#list_field").val(data.list_field);
				$("#save_update_field").val(data.save_update_field);
		    });
			$G.postByAjax({table_name:table_name},"<%=root%>/crud_auto/getFieldDetails",ajaxConf);
			
		}else{
			location.reload();
		}
	}
	function qiehuan(obj,table_name){
		$("#table_name").val(table_name);
		var as=$("td a");
		for(var i=0;i<as.length;i++){
			if($(as[i]).hasClass("td_color")){
				$(as[i]).removeClass("td_color");
			}	
		}
		$(obj).addClass("td_color");
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	var listRecords=text.listRecords;
			var arr=text.records;
			var data=text.infoMap;
			var record=text.record;
	    	var query_criteria=','+record.query_criteria+',';
	    	var list_field=','+record.list_field+',';
	    	var save_update_field=','+record.save_update_field+',';
			$(".table2 tbody").empty();
			for(var n=0;n<listRecords.length;n++){
				for(var i=0;i<arr.length;i++){
					if(arr[i].column_name==listRecords[n].column_name){
						var cname=','+arr[i].column_name+',';
						var qc="";
						if(query_criteria.indexOf(cname)>=0){
							qc='√(可点击)';
						}
						var lf="";
						if(list_field.indexOf(cname)>=0){
							lf="√(可点击)";
						}
						var suf="";
						if(save_update_field.indexOf(cname)>=0){
							suf="√(可点击)";
						}
						$(".table2 tbody").append("<tr><td><input type='checkbox' name='box' value='"+arr[i].column_name+"'/></td><td>"+arr[i].table_name+"</td><td>"+arr[i].column_name+"</td><td><a title='可点击' href='javascript:void(0);' onclick='addQueryCriteria("+'"'+record.query_criteria+'"'+")' style='color:red;'>"+qc+"</a></td><td><a title='可点击' href='javascript:void(0);' onclick='addListField("+'"'+record.list_field+'"'+")' style='color:red;'>"+lf+"</a></td><td><a title='可点击' href='javascript:void(0);' onclick='addSaveUpdateField("+'"'+record.save_update_field+'"'+")' style='color:red;'>"+suf+"</a></td><td>"+arr[i].comments+"</td></tr>");
					}
				}
			}
			$("#query_criteria").val(data.query_criteria);
			$("#list_field").val(data.list_field);
			$("#save_update_field").val(data.save_update_field);
	    });
		$G.postByAjax({table_name:table_name},"<%=root%>/crud_auto/getFieldDetails",ajaxConf);
	}
	function selectAll(){
		var flag = document.getElementById("main_box").checked;
		var cks = document.getElementsByName("box");
		if (flag) {
		   for ( var i = 0; i < cks.length; i++) {
		    cks[i].checked = true;
		   }
		 } else {
		   for ( var i = 0; i < cks.length; i++) {
		    cks[i].checked = false;
		   }
		  }
	}
	function getInfos(){
		var arr = new Array();
		var infos="";
		var boxs = document.getElementsByName("box");
		for (var i = 0; i < boxs.length; i++) {
			var obj = boxs[i];
			if (obj.checked) {
				arr.push($(obj).attr("value"));
			}
		}
		if(arr.length!=0){
			infos=arr.join(",");
		}
		return infos;
	}
	function addQueryCriteria(value){
		var infos=getInfos();
		if(value!=""){
			infos=value;
		}
		if(infos==""){
			$G.alert("请选择下面字段");
			return false;
		}
		var url = "<%=root%>/crud_auto/addForm?table_name="+$("#table_name").val()+"&infos="+infos+"&type=${query}";
		var bizParams = {pageType:"add"};
		$G.showmodaldialog("设置查询条件", url, 500, 450, bizParams, function(action){
			$("#query_criteria").val("query_criteria");
		});
		
	}
	function addListField(value){
		var infos=getInfos();
		if(value!=""){
			infos=value;
		}
		var table_name=$("#table_name").val();
		if(infos==""){
			$G.alert("请选择下面字段");
			return false;
		}
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
			if(text.flag==1){
				var url = "<%=root%>/crud_auto/addForm?table_name="+table_name+"&infos="+infos+"&type=${list}";
				var bizParams = {pageType:"add"};
				$G.showmodaldialog("设置列表字段", url, 500, 450, bizParams, function(action){
					$("#list_field").val("list_field");
				});
			}else{
				$G.alert(text.info);
			}
	    });
	    $G.postByAjax({table_name:table_name},"<%=root%>/crud_auto/getPkField?infos="+infos,ajaxConf);
	}
	function addSaveUpdateField(value){
		var infos=getInfos();
		if(value!=""){
			infos=value;
		}
		var table_name=$("#table_name").val();
		if(infos==""){
			$G.alert("请选择下面字段");
			return false;
		}
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
			if(text.flag==1){
				var url = "<%=root%>/crud_auto/addForm?table_name="+table_name+"&infos="+infos+"&type=${save_update}";
				var bizParams = {pageType:"add"};
				$G.showmodaldialog("设置增、改字段", url, 760, 450, bizParams, function(action){
					$("#save_update_field").val("save_update_field");
				});
			}else{
				$G.alert(text.info);
			}
	    });
	    $G.postByAjax({table_name:table_name},"<%=root%>/crud_auto/getPkField?infos="+infos,ajaxConf);
	}
	function generateCode(){
		if($("#query_criteria").val()==''){
			$G.alert("请设置查询条件");
			return false;
		}
		if($("#list_field").val()==''){
			$G.alert("请设置列表字段");
			return false;
		}
		if($("#save_update_field").val()==''){
			$G.alert("请设置增、改字段");
			return false;	
		}
		var url = "<%=root%>/crud_auto/addGenerateCode?table_name="+$("#table_name").val();
		var bizParams = {pageType:"add"};
		$G.showmodaldialog("生成代码", url, 550, 450, bizParams, function(action){
			
		});
	}
	function generateModule(){
		if($("#query_criteria").val()==''){
			$G.alert("请设置查询条件");
			return false;
		}
		if($("#list_field").val()==''){
			$G.alert("请设置列表字段");
			return false;
		}
		if($("#save_update_field").val()==''){
			$G.alert("请设置增、改字段");
			return false;	
		}
		var url = "<%=root%>/crud_auto/addModule";
		var bizParams = {pageType:"edit",table_name:$("#table_name").val()};
		$G.showmodaldialog("生成模块", url, 500, 450, bizParams, function(action){
			
		});
	}
	function addSql(value){
		if($("#list_field").val()==''){
			$G.alert("请先设置列表字段");
			return false;
		}
		var url = "<%=root%>/crud_auto/addSearchSql?table_name="+$("#table_name").val()+"&type="+value;
		var bizParams = {pageType:"add"};
		$G.showmodaldialog("设置查询语句", url, 500, 450, bizParams, function(action){
			
		});
	}
</script>