<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/js/jquery.min.js"></script>
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		 html, body{
	        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
	    }
	    
	    #contentbox {
			position: relative;
			width: 100%;
			height: 100%;
		}
		
		#functiondiv {
			height: 100%;
			margin-top: 0;
			margin-left: 262px;
			margin-bottom: 0;
			margin-right: 302px;
		}
		
		#menutreediv {
			height: 100%;
			width: 100%;
			position: absolute;
			top: 0px;
			left: 0px;
		}
		
		#urldiv {
			height: 100%;
			margin-top: 0;
			margin-left: 302px;
			margin-bottom: 0;
			margin-right: 0;
		}
		    
		    .custom_footer {
			text-align: right;
			padding: 2px;
		}
  		</style>
	</head> 
	<body>
		<div class="mini-toolbar" style="padding:2px;border-bottom:0;" id="inner_querypanel">
       <table style="width:100%;">
          <tr>
            <td style="width:100%;">
             	字段名称：
                <input id="remark" name="remark" class="mini-textbox" maxlength="50"/>
				<span class="separator"></span> 
				<a class="mini-button" iconCls="icon-search" onClick="query()">查询</a>
				<a class="mini-button" iconCls="icon-undo" id="addBtn" onclick="resetFun()">重置</a>
				<a class="mini-button" iconCls="icon-addnew"  onclick="onAddNode()">添加子类</a>
                <a class="mini-button" iconCls="icon-edit"  onclick="edit()">修改</a>
                <a class="mini-button" iconCls="icon-remove"  onclick="onRemoveNode()">删除</a>
            </td>
          </tr>
        </table>
    </div>
    <div class="mini-splitter" style="width:100%;height:98%;" borderStyle="border:0;">
       <div  size="270" maxSize="500" minSize="200" showCollapseButton="true" style="border-width:1px;">
          <div  showFooter="true" class="mini-panel-body" style="height: 93%;">
               <ul id="tree1"  class="mini-tree"  url="<%=root%>/gangedmenu/getZdwhList" style="width:100%;padding:5px;" 
                  dataField="datas" showTreeIcon="true" textField="remark"  idField="id" parentField="upid"  
                  resultAsTree="false" expandOnNodeClick="false" expandOnLoad="0" >        
               </ul>
          </div>
       </div>
       
       <div showCollapseButton="true"> 
          <div class="mini-fit" >
               <div id="datagrid1" dataField="datajg" idField="id" class="mini-datagrid" 
               	style="width:100%;height:95%;" sortMode="client" allowUnselect="false" 
               	url="<%=root%>/gangedmenu/getListZdwh" oncellclick="" 
				autoEscape="false" onshowrowdetail="onShowRowDetail" 
				sizeList="[10,20,50]" pageSize="10" showPager="false">
             	<div property="columns">  
             	   <div type="indexcolumn">序号</div>
             	   <div field="id" width="80" align="left" visible="false" headerAlign="left">主键</div>                    
	               <div field="remark" width="80" align="left" headerAlign="left">本级名称</div>                
	               <div field="val" width="60" align="left" headerAlign="left">本级编号</div>
	               <div field="sjremark" width="80" align="left" headerAlign="left">上级名称</div>
	               <div field="sjval" width="60" align="left" headerAlign="left">上级编号</div>
	                <div field="collate" width="20" align="left" visible="false" headerAlign="left">本级排序序号</div>
            	</div>
         	    </div>
           </div> 
       </div>     
 </div>
	</body>
</html>
<script type="text/javascript">
mini.parse();
var grid = mini.get("tree1");
//grid.load();
var grid1 = mini.get("datagrid1");
grid1.load();
//选中节点，右侧展示
grid.on("nodeclick", function (e) {
	grid1.load({ id: e.node.id});
}); 

//添加子节点 
function onAddNode(e) {
    var node = grid.getSelectedNode();
    var newNode = {REMARK: "新建节点"};
    if(node){
        grid.addNode(newNode, "add", node);
        var parentId = node.id;
        var val = node.val;
        var remark = node.remark;
        var collate=node.collate;
        var type = node.type;
        mini.open({
            url: "<%=root%>/gangedmenu/add",
            title: "新增", 
            width: 500, 
            height: 300,
            onload: function () {
                var iframe = this.getIFrameEl();
                var data = { action: "new", pid: parentId,parentNum: parentId,remark:remark,type:type,collate:collate};
                iframe.contentWindow.SetData(data);
            },
            ondestroy: function (action) {
            	 if(action == "save") {
            		  grid.reload();
            		  grid1.clearRows();
            	  }else if(action == "cancel") {
            		  grid.removeNode(newNode);
            	  }
            }
        });
    }
    else{
    	mini.alert("添加字段时，请先选中父字段!");
    }
}

//修改
function edit(){
	var node = grid.getSelectedNode();
	if(node){
		var pNode = grid.getParentNode ( node );
		var id = node.id;
		var pid = node.upid;
		var val = node.val;
        var remark = pNode.remark;
        var type = Node.type;
        var collate=node.collate;
       	mini.open({
             url:"<%=root%>/gangedmenu/edit",
             title: "编辑", 
             width: 500, 
             height: 350,
             onload: function () {
             	var iframe = this.getIFrameEl();
                var data = {action:"edit",id:id,val:val,pid:pid,remark:remark,type:type,collate:collate};
                iframe.contentWindow.SetData(data);
             },
             ondestroy: function (action) {
            	 if(action == "save") {
            		  grid.reload(); 
            		  grid1.clearRows();
            	  }
             }
        });
	}else{
		mini.alert("请先选中需要修改的字段");
	}
}

//删除子节点
function onRemoveNode(e) {
	var node = grid.getSelectedNode();
	if(node){
		if(node.children){
			mini.alert("请先删除子项");
		}
		else{
   		 	mini.confirm("确定删除选中节点?","删除",function(action){
   		 		if(action == "ok"){
  	          		$.ajax({
  	          			url: "<%=root%>/gangedmenu/del?id="+node.id,
  	          			success: function (text) {
  	          				var flag = mini.decode(text).flag;
  	          				if(flag == "1"){
  	          					grid.removeNode(node);
  	          					mini.alert("删除成功！");
  	          					grid1.clearRows();
  	          				}else {
  	          					mini.alert("删除失败！");
  	          				}
  	          			}
  	      			});
   		 		}
   		 	 }
   		  )
		}
	}
}

	//查询
function query(){
	var remark = mini.getbyName("remark").getValue();
	grid1.load({remark:remark});
}

//重置按钮
function resetFun(){
	mini.getbyName("remark").setValue("");
}
</script>

