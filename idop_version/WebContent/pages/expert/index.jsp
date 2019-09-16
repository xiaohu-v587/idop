<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>

	<head>
		<title>专家分析报表参数配置主界面</title>
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
      <div size="75px;" showCollapseButton="true" style="border: 0;">
        <div class="mini-panel" title="查询条件" style="width: 100%;height: 100%;">
          <div id="form1" class="mini-form" align="left" style="width:800px;height:100%;">
            <table id="table1" class="table">
              <tr>
                <td class="form_label">业务模块:</td>
                <td colspan="1">
                	<input class="mini-combobox" width="120px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=bi_module" 
           	 				id="module" name="module"   onvaluechanged="" valueField="val" textField="remark"></input>
                </td>
                <td class="form_label">类型:</td>
                <td colspan="1">
                	<input class="mini-combobox" width="120px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dict" 
           	 				id="dict" name="dict"   onvaluechanged="" valueField="val" textField="remark"></input>
                </td>
                <td class="form_label">名称:</td>
                <td colspan="1">
                	<input class="mini-combobox" width="120px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/expert/getPageName" 
           	 				id="page" name="page"   onitemclick="pageChanged()" dataField="data" valueField="page" textField="page"></input>
                </td>
                <td class="form_label">资源ID:</td>
                <td colspan="1">
                	<input class="mini-textbox" name="resid" id="resid" ></input>
                </td>
              	<td class="form_label" colspan="2" align="center">
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
              <a id="add" class="mini-button"  onclick="add()">新增</a>
              &nbsp;&nbsp;
              <a id="update" class="mini-button"  onclick="edit()">编辑</a>
              &nbsp;&nbsp;
              <a id="remove" class="mini-button"  onclick="remove()">删除</a>
          </tr>
        </table>
      </div>
      <div class="mini-fit">
		<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 98%;" sortMode="client" emptyText="没有对应的记录"
			allowUnselect="false" url="<%=root%>/expert/getList" multiSelect="false" allowCellSelect="false" allowResize="true" autoEscape="false"
			  showEmptyText="true">
			<div property="columns">
	            <div type="checkcolumn"></div>
	            <div type="indexcolumn"  width="40" headerAlign="center">序号</div>
	            <div field="id" headerAlign="center" allowSort="true" visible="false"  align="center">id</div>
	            <div field="module" headerAlign="center"  width="100" allowSort="true"  align="center" >业务模块</div>
	            <div field="dict" headerAlign="center"  width="100" allowSort="true"  align="center" >类型</div>
	            <div field="page" headerAlign="center"  width="200" allowSort="true"  align="center" >名称</div>
	            <div field="resid" headerAlign="center" width="300" allowSort="true"  align="center" >资源ID</div>
	          	<div field="createuser" headerAlign="center"  width="100" allowSort="true"  align="center">创建人</div>
	          	<div field="createtime" headerAlign="center" dateFormat="yyyy-MM-dd HH:mm:ss" width="100" allowSort="true"  align="center" >创建时间</div>
	          	<div field="last_check_user" headerAlign="center"  width="100" allowSort="true"  align="center">最后修改人</div>
	          	<div field="last_check_time" headerAlign="center" dateFormat="yyyy-MM-dd HH:mm:ss" width="100" allowSort="true"  align="center" >最后修改时间</div>
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
  	
 	// 查询
  	function search(){
  		grid.load(form.getData());
  	}
 	// 重置
  	function reset(){
  		form.reset();
  	}

    //新增
  	function add(){
  		var data = {
			action : "add"
		};
        $G.showmodaldialog("新增", "<%=root%>/expert/toAdd", 600, 400, data, function(action){
	    	var pageurl="<%=root%>/expert/getPageName";
			$G.getbyName("page").load(pageurl);
			
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
  	        $G.showmodaldialog("编辑", "<%=root%>/expert/toEdit?id=" + row.id, 600, 400, data, function(action){
  		    	 grid.reload();
  		    	 var pageurl="<%=root%>/expert/getPageName";
				 $G.getbyName("page").load(pageurl);
			    
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
					url : "<%=root%>/expert/delete?key=" + row.id,
					success : function(text) {
						var pageurl="<%=root%>/expert/getPageName";
						$G.getbyName("page").load(pageurl);
						
					    grid.reload();
					},
					error:function(text){
						mini.alert("删除失败");
					}
				});
			}
		});
  	}
  	
  	
//   	function dictValueChanged(){
//   		var dict=$G.get("dict").getValue();
//   		$G.get("page").setValue("");
<%--   		url="<%=root%>/expert/dictValueChanged?dict="+dict; --%>
//   		$G.get("page").setUrl(url);
//   	}
  	  	
  </script>
</body>
</html>