<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>预警阀值设置主界面</title>
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
      <div size="75px;" showCollapseButton="false" style="border: 0;">
        <div class="mini-panel" title="查询条件" style="width: 100%;height: 100%;">
          <div id="form1" class="mini-form" align="left" style="width:800px;height:100%;">
            <table id="table1" class="table">
              <tr>
                <td class="form_label">业务模块:</td>
                <td colspan="1">
                	<input class="mini-combobox" width="120px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_moduletype" 
           	 				id="moduletype" name="moduletype"   onvaluechanged="" valueField="val" textField="remark"></input>
                </td>
                <td class="form_label">指标编号:</td>
                <td colspan="1">
                	<input  width="120px"  class="mini-textbox" name="targetno" id="targetno"></input>
                </td>
              	<td class="form_label" colspan="2" align="center">
                	<a class="mini-button"  onclick="search()">查询</a>
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
              <a id="update" class="mini-button" onclick="edit()">编辑</a>
              &nbsp;&nbsp;
            </td>
          </tr>
        </table>
      </div>
      <div class="mini-fit">
		<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 98%;" sortMode="client" emptyText="没有对应的记录"
			allowUnselect="false" url="<%=root%>/warningthreshold/getList" multiSelect="false" allowCellSelect="false" allowResize="false" autoEscape="false"
			  showEmptyText="true" onrowdblclick='edit()'>
			<div property="columns">
	            <div type="checkcolumn"></div>
	            <div type="indexcolumn" headerAlign="center" width="40">序号</div>
	            <div field="moduletype" headerAlign="center" width="150" align="center" >业务模块</div>
	            <div field="targettypeno" headerAlign="center" width="150" align="center" >指标层级编号</div>
	            <div field="warningtype" headerAlign="center" width="150" align="center" >指标类型</div>
	            <div field="targetno" headerAlign="center"  width="150" align="center" >指标编号</div>
	            <div field="val" headerAlign="center"  width="100" align="center" >阀值</div>
	            <div field="val1" headerAlign="center" width="100" align="center" >阀值1</div>
	          	<div field="val2" headerAlign="center"  width="100" align="center">阀值2</div>
	          	<div field="val3" headerAlign="center"  width="100" align="center" >阀值3</div>
	          	<div field="effdate" headerAlign="center"  width="150" align="center" dateFormat="yyyy-MM-dd">生效日期</div>
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
  	
//   	grid.on('rowdblclick', function() {
//   		edit();
//   	});

  	//跳转至编辑页面
  	function edit(){	
  		var row=grid.getSelected();
  		if(row){
			var data = {
				action: "edit", moduletype: row.moduletype, targettypeno: row.targettypeno, targetno: row.targetno, effdate: row.effdate
			};
  	        $G.showmodaldialog("编辑", "<%=root%>/warningthreshold/toEdit", 820, 600, data, function(action){
  		    	 grid.reload();
  		    });
  	  	}else{
			mini.alert("请选中一条记录", "提示");
  	  	 }
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