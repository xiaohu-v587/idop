<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="KPI风险评估" style="width:100%;height:111px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">机构：</th>
							<td align="left">
								<input id="orgSelect" class="mini-treeselect" url="<%=root%>/kpi/getOrgDatas" dataField="datas" 
									name="org_id" textfield="orgname" valuefield="id" parentfield="upid"  
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
									allowInput="false" showClose="true" oncloseclick="onCloseClick" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" style="width:165px;"/>
							</td>
		                   	<th align="right">风险类型：</th>
							<td align="left">
								<input id="punish_type" class="nui-combobox"  name="punish_type" allowInput="false" style="width:165px;"
								showClose="true" oncloseclick="onCloseClick" textfield="ptext" valuefield="pid"
								data="[{'pid':'0','ptext':'经济处理'},{'pid':'1','ptext':'纪律处分'}]" onvaluechanged="onTypeChanged"/>
							</td>
		                    <th align="right">客户经理姓名：</th>
							<td align="left">
								<input id="cust_name" name="cust_name" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
	               		<tr>
	               			<th align="right">创建人：</th>
							<td align="left">
								<input id="creater" name="creater" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right">创建时间：</th>
							<td align="left">
								<input id="create_time" name="create_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="width:165px;"/>
							</td>
		                   	<th align="right"></th>
		                    <td align="left">    
		                    </td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">新增</a>
          	     			<a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()" plain="true">编辑</a>
          	     			<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/custPuni/listCustPuni" style="width: 100%;height: 100%;"
		    		multiSelect="true">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div headerAlign="center" width="5%" type="indexcolumn">序号</div>     
			            <div field="cust_name" width="13%" headerAlign="center" allowSort="true"  align="left">客户经理姓名</div>                
			            <div field="orgname" width="15%" allowSort="true" headerAlign="center" align="left">所属机构</div>            
			            <div field="punish_type" width="10%" allowSort="true" headerAlign="center" align="left">风险类型</div> 
			            <div field="deduction" width="10%" allowSort="true" headerAlign="center" align="right">所扣分数</div> 
			            <div field="creater_name" width="10%" allowSort="true" headerAlign="center" align="left">创建人</div> 
			            <div field="create_time" width="12%" allowSort="true" headerAlign="center" align="right" >创建时间</div>
			            <div field="remark" width="15%" allowSort="true" headerAlign="center" align="left" >备注</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();

	var grid =$G.get("datagrid1");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	
	grid.load();

	//新增
	function add() {
        var url = "<%=root%>/custPuni/form";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("新增", url, 500, 250, bizParams, function(action){
	    	 grid.reload();
	    });
	}

	//编辑
	function edit(){
		var row = grid.getSelected();
		if (row) {
			var url = "<%=root%>/custPuni/form";
			var bizParams = {pageType:"edit",id:row.id};
	        $G.showmodaldialog("编辑", url, 500, 250, bizParams, function(action){
		    	 grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
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
	                    	ids = rows[index].id;
	                  	} else {
	                    	ids += "," + rows[index].id;
	                  	}
	                }
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/custPuni/del", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
		}
	}

	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
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

