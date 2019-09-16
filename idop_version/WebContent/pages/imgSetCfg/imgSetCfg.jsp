<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 图谱配置主界面
	*
	* @author 陈佳争
	* @date 2019-05-24
-->
	<head>
		<title>图谱配置主界面</title>
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
      <div size="80px;" showCollapseButton="true" style="border: 0;">
        <div class="mini-panel" title="查询条件" style="width: 100%;height: 100%;">
          <div id="form1" class="mini-form" align="left" style="width:1000px;height:100%;">
            <table id="table1" class="table">
              <tr>
                <td class="form_label">标签维度:</td>
                <td colspan="1">
                	<input class="mini-combobox" id="indicator_dimension" name="indicator_dimension" url="<%=root%>/param/getDict?key=ZBWD" valueField="val" textField="remark" nullItemText="" emptyText="请选择..."/>
                </td>
                <td class="form_label">业务模块:</td>
                <td colspan="1">
                	<input class="mini-combobox" id="module" name="module" url="<%=root%>/param/getDict?key=dop_ywtype" valueField="val" textField="remark" nullItemText="" emptyText="请选择..."/>
                </td>
                <td class="form_label">标签编码:</td>
                <td colspan="1">
                	<input class="mini-textbox" id="indicator_code" name="indicator_code"/>
                </td>
                <td class="form_label">标签名称:</td>
                <td colspan="1">
                	<input class="mini-textbox" name="indicator_name" id="indicator_name"/>
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
<!--               <a id="view" class="mini-button"  onclick="view()">查看</a> -->
<!--               &nbsp;&nbsp; -->
              <a id="add" class="mini-button"  onclick="add()">新增</a>
              &nbsp;&nbsp;
              <a id="update" class="mini-button"  onclick="edit()">编辑</a>
              &nbsp;&nbsp;
              <a id="remove" class="mini-button"  onclick="remove()">删除</a>
            </td>
          </tr>
        </table>
      </div>
      <div class="mini-fit">
		<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 98%;" sortMode="client" emptyText="没有对应的记录"
			allowUnselect="false" url="<%=root%>/imgSetCfg/getList" multiSelect="true" allowCellSelect="false" allowResize="true" autoEscape="false"
			  showEmptyText="true">
			<div property="columns">
	            <div type="checkcolumn"></div>
	            <div type="indexcolumn"  width="40" headerAlign="center">序号</div>
	            <div field="id" headerAlign="center" allowSort="true" visible="false"  align="center">id</div>
	            <div field="indicator_dimension" headerAlign="center"  width="150" allowSort="true"  align="center" >标签维度</div>
	            <div field="module" headerAlign="center"  width="150" allowSort="true"  align="center" >业务模块</div>
	            <div field="indicator_code" headerAlign="center"  width="150" allowSort="true"  align="center" >标签编号</div>
	            <div field="indicator_name" headerAlign="center" width="150" allowSort="true"  align="center" >标签名称</div>
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

  	//新增
  	function add(){
  		var data = {
			action : "add"
		};
        $G.showmodaldialog("新增", "<%=root%>/imgSetCfg/toAdd", 800, 300, data, function(action){
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
  	        $G.showmodaldialog("编辑", "<%=root%>/imgSetCfg/toEdit?id=" + row.id, 800, 300, data, function(action){
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
					url : "<%=root%>/imgSetCfg/delete?key=" + row.id,
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
  </script>
</body>
</html>