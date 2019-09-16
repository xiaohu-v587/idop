<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 组合参数配置主界面
	*
	* @author 
	* @date 2019-03-13
-->
	<head>
		<title>组合参数配置主界面</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
		<style type="text/css">
			html,body {
				margin: 0;
				padding: 0;
				border: 0;
				width: 100%;
				height: 100%;
				overflow: hidden;
			}
		</style>
	</head>
<body>
	<div class="mini-splitter" style="width:100%;height:100%;" vertical="true" borderStyle="border: 0;">
      <div size="100px;" showCollapseButton="true" style="border: 0;">
        <div class="mini-panel" title="查询条件" style="width: 100%;height: 100%;">
          <div id="form1" class="mini-form" align="left" style="width:800px;height:100%;">
            <table id="table1" class="table">
              <tr>
                <td class="form_label">监测类型:</td>
                <td colspan="1">
                	<input class="mini-combobox"   nullItemText="" emptyText="请选择..."  showNullItem="false" url="<%=root%>/param/getDict?key=monitor_type" 
           	 				id="monitor_type" name="monitor_type"   onvaluechanged="typeValueChanged()" valueField="val" textField="remark"></input>
                </td>
                <td class="form_label">监测机构层级:</td>
                <td colspan="1">
                	<input class="mini-combobox" nullItemText="" emptyText="请选择..." showNullItem="false" url="<%=root%>/param/getDict?key=monitor_level" 
           	 				id="monitor_level" name="monitor_level"   onvaluechanged="" valueField="val" textField="remark" enabled='false'></input>
                </td>
                <td class="form_label">监测频率:</td>
                <td colspan="1">
                	<input class="mini-combobox" nullItemText="" emptyText="请选择..." showNullItem="false" url="<%=root%>/param/getDict?key=monitor_rate" 
           	 				id="monitor_rate" name="monitor_rate"   onvaluechanged="" valueField="val" textField="remark"></input>
                </td>
                </tr>
                <tr>
                <td class="form_label">监测条件名称:</td>
                <td colspan="1">
                	<input class="mini-textbox" name="monitor_name" id="monitor_name"></input>
                </td>
                <td class="form_label">监测方式:</td>
                <td colspan="1">
                	<input class="mini-combobox" nullItemText="" emptyText="请选择..." showNullItem="false" url="<%=root%>/param/getDict?key=monitor_method" 
           	 				id="monitor_method" name="monitor_method"   onvaluechanged="" valueField="val" textField="remark"></input>
                </td>
                <td class="form_label">状态:</td>
                <td colspan="1">
                	<input class="mini-combobox" nullItemText="" emptyText="请选择..." showNullItem="false" url="<%=root%>/param/getDict?key=monitor_state" 
           	 				id="monitor_state" name="monitor_state"   onvaluechanged="" valueField="val" textField="remark"></input>
                </td>
              	<td class="form_label" align="center">
                	<a class="mini-button"  onclick="search()">查询</a>
                    &nbsp;&nbsp;
               	 	<a class="mini-button"  onclick="reset()">重置</a>
                </td>
              </tr>
      		</table>
        </div>
      </div>
    </div>
    <div showCollapseButton="false" style="border: 0;">
      <div class="mini-toolbar" style="border-bottom: 0; padding: 0px;">
        <table style="width: 100%;">
          <tr>
            <td style="width: 100%;">
              <a id="view" class="mini-button"  onclick="view()">查看</a>
              &nbsp;&nbsp;
              <a id="add" class="mini-button"  onclick="add()">新增</a>
              &nbsp;&nbsp;
              <a id="update" class="mini-button"  onclick="edit()">编辑</a>
              &nbsp;&nbsp;
              <a id="remove" class="mini-button"  onclick="remove()">删除</a>
              &nbsp;&nbsp;
              <a id="addTpl" class="mini-button"  onclick="tpl()">模板</a>
            </td>
          </tr>
        </table>
      </div>
      <div class="mini-fit">
		<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 98%;" sortMode="client" emptyText="没有对应的记录"
			allowUnselect="false" url="<%=root%>/cpc/getList" multiSelect="true" allowCellSelect="false" allowResize="true" autoEscape="false"
			  showEmptyText="true">
			<div property="columns">
	            <div type="checkcolumn"></div>
	            <div type="indexcolumn"  width="40" headerAlign="center">序号</div>
	            <div field="id" headerAlign="center" allowSort="true" visible="false"  align="center">id</div>
	            <div field="monitor_type" headerAlign="center"  width="150" allowSort="true"  align="center" >监测类型</div>
	            <div field="monitor_name" headerAlign="center"  width="150" allowSort="true"  align="center" >监测条件名称</div>
	            <div field="monitor_rate" headerAlign="center"  width="150" allowSort="true"  align="center" >监测频率</div>
	            <div field="monitor_level" headerAlign="center" width="150" allowSort="true"  align="center" >监测机构层级</div>
	          	<div field="monitor_method" headerAlign="center"  width="150" allowSort="true"  align="center">监测方式</div>
	          	<div field="monitor_state" headerAlign="center"  width="150" allowSort="true"  align="center" >状态</div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script type="text/javascript">
	mini.parse();
  	
  	var grid = mini.get("datagrid1");
  	var form = new mini.Form("form1");
  	grid.load();
  	
  	//当监测类型为机构监测时可选择监测机构层级
  	function typeValueChanged() {
  		var monitorType = mini.get("monitor_type").getValue();
  		var monitorLevelCombo = mini.get("monitor_level");
  		if (monitorType == 0) {
  			monitorLevelCombo.enable();
  		} else {
  			monitorLevelCombo.setValue(null);
  			monitorLevelCombo.disable();
  		}
  	}
  	
    //查看
  	function view(){
    	var row = grid.getSelected();
    	if (row) {
	   		var data = {
   				action: "view",
   				id: row.id
   			};
   	        $G.showmodaldialog("查看", "<%=root%>/cpc/toView", 1200, 600, data, function(action){
   		    	
   		    });	
    	} else {
    		mini.alert("请选中一条记录", "提示");
    	}
  	}
  	
    function tpl() {
    	var data = {
				action: "addTpl",
			};
	        $G.showmodaldialog("增删模板", "<%=root%>/cpc/toAddTpl", 1200, 600, data);	
    }

  	//新增
  	function add(){
  		var data = {
			action : "add"
		};
        $G.showmodaldialog("新增", "<%=root%>/cpc/toAdd", 1200, 600, data, function(action){
	    	grid.reload();
	    });
  	}
  	
  //编辑
  	function edit(){	
  		var row=grid.getSelected();
  		if(row){
			var data = {
				action : "edit",id:row.id
			};
  	        $G.showmodaldialog("编辑", "<%=root%>/cpc/toEdit?id=" + row.id, 1200, 600, data, function(action){
  		    	 grid.reload();
  		    });
  	  	}else{
			mini.alert("请选中一条记录", "提示");
  	  	 }
  	}
  	
  	//删除
  	function remove(){
  		var row=grid.getSelected();
		if (!row) {
			mini.alert("请先选中一条记录！");
			return;
		}
		mini.confirm("确定删除该配置？", "确定？", function(action) {
			if (action == "ok") {			
				$.ajax({
					url : "<%=root%>/cpc/delete?key=" + row.id,
					success : function(text) {
					    grid.reload();
					}
				});
			}
		});
  	}
  	
  	// 查询
  	function search(){
  		grid.load(form.getData());
  	}
  	
  	// 重置
  	function reset(){
  		form.reset();
  	}
  	
  	//业务模块匹配中文
// 	function onYwTypeRender(e){
// 		var textVal = mini.getDictText("dop_ywtype",e.value);
// 		return textVal;
// 	}
  	  	
  </script>
</body>
</html>