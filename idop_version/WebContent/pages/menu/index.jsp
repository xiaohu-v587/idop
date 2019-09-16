<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<body>
    	<div class="nui-splitter" style="width:100%;height:100%;" borderStyle="border:0;">
			<div size="300" maxSize="500" minSize="200" showCollapseButton="true" style="border-width:1px; overflow: auto;" >
          		<div showFooter="true" class="nui-panel-body" style="height: 93%;">
					<ul id="tree1" class="nui-tree"  url="<%=root%>/menu/getMenuList" style="width:100%;padding:5px;" 
                  		dataField="datas" showTreeIcon="true" textfield="name"  idfield="id" parentfield="pid"  
                  		resultAsTree="false" expandOnLoad="true" >        
               		</ul>
          		</div>
       		</div>
			<div showCollapseButton="true" style="border-left:0;">
       				<table >
          				<tr>
          					<th>名称：</th>
			            	<td>
                                 <input id="name" name="name" class="nui-textbox"   />
			            	</td>
			            	<th>地址：</th>
          					<td>
          						 <input id="url" name="url" class="nui-textbox"   />
          					</td>
          				</tr>
          				<tr>
          					<th>状态：</th>
          					<td>
	          					<input id="enable" name="enable" class="mini-combobox" textfield="remark" valuefield="val"
	                   		       url="<%=root%>/param/getKeyList?key=CK_CDZT"  allowInput="false"/> 
          					</td>
          					<th></th>
          					<td></td>
          				</tr>
        			</table>
        			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
        				<table style="width:100%;">
			        		<tr>
			        			<td style="width:100%;">
			        				<a class="nui-button"   onclick="onAddNode()" >添加父节点</a>
			        				<a class="nui-button"   onclick="onAddChildNode()" >添加子节点</a>
						            <a class="nui-button"   onclick="edit()" >修改</a>
						            <a class="nui-button"   onclick="del()" >删除</a>
						            <!-- <a class="nui-button"   id="download" onclick="download()" plain="true">下载</a>-->
			        			</td>
			        			<td style="white-space:nowrap;">
			 						<a class="nui-button"  id="addBtn" onclick="resetFun()">重置</a>
			 						<a class="nui-button"  onClick="query()">查询</a>
			        			</td>
			        		</tr>
        				</table>
    				</div> 
          			<div class="nui-fit" >
               			<div id="datagrid1"  idfield="id" dataField="datas" class="nui-datagrid" sortMode="client" allowUnselect="false"
			               	style="width:100%;height:100%;" url="<%=root%>/menu/getList" autoEscape="false" sizeList="[10,20,50]"
					        pageSize="10">
			             	<div property="columns">  
			             	   <div type="indexcolumn" width="35" align="right">序号</div>                    
				               <div field="name" width="160" align="left" headerAlign="left">名称</div>            
				               <div field="url" width="60" align="left" headerAlign="left">路径</div>
				               <div field="enable"  width="60" align="left" headerAlign="left" renderer="onRenderType">状态</div>  
				               <div field="actikey"  width="60" align="left" headerAlign="left"   visible="false">工作流名称</div>                                  
			            	</div>
         				</div>
          			</div> 
       			</div>     
    		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var tree = $G.get("tree1");
	var grid = $G.get("datagrid1");
	
	grid.load();
	
	//根据条件查询
	function query(){
	  var name = mini.get("name").getValue();
	  var url = mini.get("url").getValue();
	  var enable = mini.get("enable").getValue();
	  grid.load({
	      name:name,
	      url:url,
	      enable:enable});
	 }
	 
	//重置
	function resetFun(){
	  mini.get("name").setValue("");
	  mini.get("url").setValue("");
	  mini.get("enable").setValue("");
	}

	//选中节点，右侧展示
    tree.on("nodeclick", function (e) {
    	grid.load({ pid: e.node.id}); 
    });

  	//添加节点 
    function onAddNode(e) {
        var newNode = {orgname: "新建节点"};
        tree.addNode(newNode, "add");
        var url="<%=root%>/menu/add";
        var bizParams = { pageType: "add"};
        $G.showmodaldialog("新增父机构", url, 500, 250, bizParams, function(action){
          		  	tree.reload(); 
          		  	grid.reload();
	    });
    }
    
    //添加子节点 
    function onAddChildNode(e) {
        var node = tree.getSelectedNode();
        var newNode = {name: "新建节点"};
        if(node){
	        tree.addNode(newNode, "add", node);
	        var parentId = node.id;
	        var parentName = node.name;
	        var url="<%=root%>/menu/add";
	        var bizParams = { pageType: "addchild", pid: parentId,name:parentName};
	        $G.showmodaldialog("新增子机构", url, 500, 350, bizParams, function(action){
           		  	tree.reload(); 
           		  	grid.reload();
		    });
        }else{
        	$G.alert("添加机构时，请先选中父机构");
        }
    }
    

    //修改机构
    function edit(){
    	var row = grid.getSelected();
    	if(row){
    		var id = row.id;
            var url="<%=root%>/menu/add";
	        var bizParams = { pageType: "edit", id:id};
	        $G.showmodaldialog("修改机构", url, 500, 350, bizParams, function(action){
	        	grid.reload();
	        	tree.reload();
		    });
            
    	}else{
    		$G.alert("请先选择一行数据！");
    	}
	}

	//删除机构
	function del(){
      var row = grid.getSelected();
      if (row) {
       var id = row.id;
       $G.confirm("确定删除该节点？", "确定？",
         function (action) {
          if (action == "ok") {
           grid.loading("操作中，请稍后......");
           $.ajax({
             url: "<%=root%>/menu/del?id="+id,
             success: function (text) {
          	   var record = $G.decode(text).record;
          	   //$G.alert(record);
          	   if(record=="1"){
         		 $G.alert("该节点下面还有其他的子节点，不可以删除!");
          	   }else{
          	     $G.alert("该节点已删除！");
          	   }
                 grid.reload();
                 tree.reload();
             }
           });
          }
         }
       );
      } else {
       $G.alert("请选中一条数据！")
      }
	}

	//下载
	function download(){
		var orgSelect = mini.get("orgSelect").getValue();
		var jgh = mini.get("jgh").getValue();
		var orgname = mini.get("orgname").getValue();
		var Node = tree.getSelectedNode().orgnum;
		window.location="<%=root%>/org/download?orgnum=" + orgSelect + "&jgh=" + jgh + "&orgname=" + orgname +"&pid=" + Node;
	}
	
	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }

	function onRenderType(e){
		return mini.getDictText("CK_CDZT",e.value)
	}
	
	
</script>