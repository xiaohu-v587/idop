<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 算法管理主界面
	*
	* @author Liu Dongyuan
	* @date 2018-11-09
-->
	<head>
		<title>算法管理主界面</title>
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
                <td class="form_label">业务模块:</td>
                <td colspan="1">
                	<input class="mini-combobox" width="80px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="ywtype" name="ywtype"   onvaluechanged="" valueField="val" textField="remark"></input>
                </td>
                <td class="form_label">适用层级:</td>
                <td colspan="1">
                	<input class="mini-combobox" width="80px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_apply" 
           	 				id="apply" name="apply"   onvaluechanged="" valueField="val" textField="remark"></input>
                </td>
                <td class="form_label">指标预警编号:</td>
                <td colspan="1">
                	<input  width="120px"  class="mini-textbox" name="indexnum" id="indexnum"></input>
                </td>
                <td class="form_label">指标预警名称:</td>
                <td colspan="1">
                	<input class="mini-textbox" name="indexname" id="indexname"></input>
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
            </td>
          </tr>
        </table>
      </div>
      <div class="mini-fit">
		<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 98%;" sortMode="client" emptyText="没有对应的记录"
			allowUnselect="false" url="<%=root%>/algorith/getList" multiSelect="true" allowCellSelect="false" allowResize="true" autoEscape="false"
			  showEmptyText="true">
			<div property="columns">
	            <div type="checkcolumn"></div>
	            <div type="indexcolumn"  width="40" headerAlign="center">序号</div>
	            <div field="id" headerAlign="center" allowSort="true" visible="false"  align="center">id</div>
	            <div field="ywtype" headerAlign="center" renderer="onYwTypeRender" width="150" allowSort="true"  align="center" >业务模块</div>
	            <div field="indexnum" headerAlign="center"  width="150" allowSort="true"  align="center" >指标预警编号</div>
	            <div field="indexname" headerAlign="center"  width="150" allowSort="true"  align="center" >指标预警名称</div>
	            <div field="assigntype" headerAlign="center" renderer="onAssTypeRender"   width="150" allowSort="true"  align="center" >赋分方式</div>
	            <div field="caltype" headerAlign="center"  width="150" allowSort="true"  align="center" >计算方式</div>
	            <div field="apply" headerAlign="center"  renderer="onApplyRender"   width="150" allowSort="true"  align="center" >适用层级</div>
	          	<div field="marks" headerAlign="center"  width="150" allowSort="true"  align="center">满分</div>
	          	<div field="weight" headerAlign="center"  width="150" allowSort="true"  align="center" >权重</div>
	          	<div field="describtion" headerAlign="center"  width="250" allowSort="true"  align="center" >描述</div>
	          	<div field="byzd1" headerAlign="center"  visible="false" allowSort="true"  align="center" >备用字段一</div>
	          	<div field="byzd2" headerAlign="center"  visible="false" allowSort="true"  align="center" >备用字段二</div>
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
  	

  	//跳转至编辑页面
  	function edit(){	
  		var row=grid.getSelected();
  		if(row){
			var data = {
				action : "edit",id:row.id
			};
  	        $G.showmodaldialog("新增", "<%=root%>/algorith/toEdit?id=" + row.id, 820, 600, data, function(action){
  		    	 grid.reload();
  		    });
  	  	}else{
			mini.alert("请选中一条记录", "提示")
  	  	 }
  	}

  	//新增算法
  	function add(){
  		var data = {
				action : "add"
		};
        $G.showmodaldialog("新增", "<%=root%>/algorith/toAdd", 820, 600, data, function(action){
	    	 grid.reload();
	    });
  	}
  	
  	//删除算法
  	function remove(){
  		var row=grid.getSelected();
		if (!row) {
			mini.alert("请先选中一条记录！");
			return;
		}
		mini.confirm("确定删除该算法？", "确定？", function(action) {
			if (action == "ok") {			
				$.ajax({
					url : "<%=root%>/algorith/delete?key=" + row.id,
					success : function(text) {
			          	   var record = $G.decode(text).record;
			          	   //$G.alert(record);
			          	   if(record=="0"){
			         		 $G.alert("删除失败!");
			          	   }else{
			          	     $G.alert("删除成功！");
			          	   }
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
	function onYwTypeRender(e){
		var textVal = mini.getDictText("dop_ywtype",e.value);
		return textVal;
	}
  	
  	//适用层级匹配中文
	function onApplyRender(e){
		var textVal = mini.getDictText("dop_apply",e.value);
		return textVal;
	}
  	
  	//赋值方式匹配中文
	function onAssTypeRender(e){
		var textVal = mini.getDictText("dop_asstype",e.value);
		return textVal;
	}
  	
  	//计算方式匹配中文
	function onCalTypeRender(e){
		var textVal = mini.getDictText("dop_caltype",e.value);
		return textVal;
	}
  	
/*   	//风险等级匹配中文
	function onLevelRender(e){
		var textVal = mini.getDictText("dop_level",e.value);
		return textVal;
	} */
  </script>
</body>
</html>