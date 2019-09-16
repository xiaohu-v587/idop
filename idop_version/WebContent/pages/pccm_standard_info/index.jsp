<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>标准管理</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="标准管理" style="width:100%;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">新增</a>
          	     			<a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()" plain="true">编辑</a>
          	     			<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
		                </td>
<!-- 		                <td style="white-space:nowrap;"> -->
<!-- 		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>   -->
<!-- 		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a> -->
<!-- 		                </td> -->
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;" url="<%=root%>/pccm_standard_info/getList" multiSelect="true">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false"></div>
			            <div field="standard_flag" allowSort="true" headerAlign="center" align="center">数据源</div>
			            <div field="data_flag"  headerAlign="center" allowSort="true"  align="center">标准</div> 
			            <div field="standard_remark" allowSort="true" headerAlign="center" align="center">标准描述</div> 
			            <div field="second_org_num" allowSort="true" headerAlign="center" align="center">二级机构</div>
			            <div field="third_org_num" allowSort="true" headerAlign="center" align="center">三级机构</div>
			            <div field="create_time" allowSort="true" headerAlign="center" align="center" dateFormat="yyyy-MM-dd">日期</div>            
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid1");
// 	var form = $G.getForm("form1");
	grid.load();

	//删除
	function del(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
				if (action == 'ok') {
                	var ids = "";
	                for(var index = 0;index < rows.length;index++){
						if(ids == ""){
	                    	ids = rows[index].id;
	                  	} else {
	                    	ids += "," + rows[index].id;
	                  	}
	                }
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/pccm_standard_info/del", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
		}
	}
	//新增
	function add() {
        var url = "<%=root%>/pccm_standard_info/form";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("新增", url, 900, 500, bizParams, function(action){
	    	 grid.reload();
	    });
	}

	//编辑
	function edit(){
		var row = grid.getSelected();
		if (row) {
			var url = "<%=root%>/pccm_standard_info/form";
			var bizParams = {pageType:"edit",id:row.id};
	        $G.showmodaldialog("编辑", url, 900, 500, bizParams, function(action){
		    	 grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}
</script>

