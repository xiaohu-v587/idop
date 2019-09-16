<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>${menu_name}</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  			#panel1 .mini-panel-border .mini-panel-header .mini-panel-header-inner .mini-panel-title{
  				color:red;
  			}
  		</style>
	</head>
	<body>
		<div id="panel1" class="nui-panel" title="${menu_name}" style="width:100%;height:74px;" showToolbar="false" showCollapseButton="false"
			showFooter="false" allowResize="false" collapseOnTitleClick="false">
			<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
						<c:forEach items="${queryList}" var="item">
			                   		<th align="right">${item[1]}</th>
									<td align="left">
									<c:if test="${item[2]==0}">
										<input name="${item[0]}" class="nui-textbox"/>
									</c:if>
									<c:if test="${item[2]==1}">
										<input name="${item[0]}" class="nui-textbox"/>
									</c:if>
									<c:if test="${item[2]==2}">
										<input name="${item[0]}" allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..."  class="nui-combobox" textfield="name" valuefield="value"
										url="<%=root%>/module_index/getCombobox?table_name=${table_name}&field_name=${item[0]}"/>
									</c:if>
									<c:if test="${item[2]==4}">
										<input name="${item[0]}" format="yyyy-MM-dd" class="mini-datepicker"/>	
									</c:if>
								</td>
						</c:forEach>
	               				</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
		
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
			<table style="width:100%;">
				<tr>
					<td style="width:100%;">
						<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">新增</a>
            	    	<a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()" plain="true">编辑</a>
            	     	<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
					</td>
                    <td style="white-space:nowrap;">
                    	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a> 
						<a class="mini-button" iconCls="icon-search" onclick="search()">查询</a>
                    </td>
				</tr>
			</table>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;"  sortMode="client" allowUnselect="false"
				url="<%=root%>/module_index/getList?menu_id=${menu_id}" oncellclick=""  onselectionchanged="onSelectionChanged" multiSelect="true" autoEscape="false"
				onshowrowdetail="onShowRowDetail">
		        <div property="columns">
		           <c:forEach items="${fieldList}" var="item">
		              	<div field="${item[0]}" <c:if test="${item[0]==pkField}">visible="false"</c:if><c:if test="${item[2]=='4'}">dateFormat="yyyy-MM-dd HH:mm:ss"</c:if>allowSort="true" align="center" headerAlign="center">${item[1]}</div>
		             </c:forEach>
		        </div>
		        </div>
			</div>  
		</div>
	</body>
	<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid1");
	var form = $G.getForm("form1");
	grid.load();
	//新增
	function add(){
		var url = "<%=root%>/module_index/module_form?menu_id=${menu_id}";
		var bizParams = {pageType:"add"};
		$G.showmodaldialog("新增", url, ${dialog_width}, ${dialog_height}, bizParams, function(action){
			grid.reload();
		});
	}
	//编辑
	function edit(){
		var row = grid.getSelected();
		if(row){
			var url = "<%=root%>/module_index/module_form?menu_id=${menu_id}&uuid="+row.${pkField};
			var bizParams = {pageType:"edit",id:row.${pkField}};
			$G.showmodaldialog("编辑", url, ${dialog_width}, ${dialog_height}, bizParams, function(action){
				grid.reload();
			});
		}else{
			$G.alert("请先选择一行数据！");
		}
	}
	//删除
	function del(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
				if (action == 'ok') {
					var ids = "";
	                for(var index = 0;index < rows.length;index++){
						if(ids == ""){
	                    	ids = rows[index].${pkField};
	                  	} else {
	                    	ids += "," + rows[index].${pkField};
	                  	}
	                }
	                var ajaxConf = new GcdsAjaxConf();
	                ajaxConf.setIsAsync(false);
	        		ajaxConf.setIsShowSuccMsg(false);
	        		ajaxConf.setIsShowProcessBar(false);
	        		ajaxConf.setErrorFunc(function(){
	    				$G.alert("操作失败！");
	    			})
                	ajaxConf.setSuccessFunc(function (text){
						if(text.result=='success'){
							$G.alert("操作成功！");
// 							$G.closemodaldialog("ok");
						}
						grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/module_index/module_del?menu_id=${menu_id}", ajaxConf);
				}
            });
		}else{
			$G.alert("请先选择一行数据！");
		}
	}
	//查询
	function search(){
		var arr=eval('${querys}');
		var info="{";
		for(var i=0;i<arr.length;i++){
			var value=$G.getbyName(arr[i][0]).getValue();
			if(value!=''){
				info+="\""+arr[i][0]+"\":"+"\""+value+"\""+",";
			}
		}
		if(info.charAt(info.length - 1)==","){
			info=info.substring(0,info.length-1);
		}
		info+="}";
		info=eval('(' + info + ')');
		grid.load(info);
	}

	//重置
	function reset(){
		form.reset();
	}
</script>
</html>