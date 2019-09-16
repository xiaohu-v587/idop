<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<body>
    	<div class="nui-splitter" style="width:100%;height:100%;" borderStyle="border:0;">
			<div size="300" maxSize="500" minSize="200" showCollapseButton="true" style="border-width:1px; overflow: auto;" >
          		<div showFooter="true" class="nui-panel-body" style="height: 93%;">
					<ul id="tree1" class="nui-tree"  url="<%=root%>/org/getOrgList" style="width:100%;padding:5px;" 
                  		dataField="datas" showTreeIcon="true" textfield="orgname"  idfield="orgnum" parentfield="upid"  
                  		resultAsTree="false" expandOnLoad="0" >        
               		</ul>
          		</div>
       		</div>
       		<div size="100%" showCollapseButton="false" expanded="true" style="border-left:0px;">
				<div id="tabs1" class="mini-tabs" activeIndex="0" style="width: 100%; height: 100%;"  onactivechanged="onActivechanged()" 
					dataField="tabs" bodyStyle="border-left:0;border-right:0px;border-bottom:0px;">
	                 <div id="tab1" name="org" title="机构信息" >
		       				<form id="form1">
		       				<table >
		          				<tr>
		          					<th>机构：</th>
					            	<td>
						                <input id="orgSelect2" class="nui-treeselect" url="<%=root%>/org/getList" dataField="datas" 
								 			name="orgid" textfield="orgname" valuefield="orgnum" parentfield="upid"  
								 			valueFromSelect="false" multiSelect="false" expandOnLoad="0"
						 					allowInput="false" showClose="true" oncloseclick="onCloseClick" 
												showRadioButton="true" showFolderCheckBox="false" popupWidth="305" 
												popupHeight="470" popupMaxHeight="600"/>
					            	</td>
					            	<th>机构号：</th>
		          					<td>
		          						 <input id="jgh" name="jgh" class="nui-textbox"   />
		          					</td>
		          				</tr>
		          				<tr>
		          					<th>机构名称：</th>
		          					<td>
			          					<input id="orgname" name="orgname" class="nui-textbox" />
		          					</td>
		          					<th></th>
		          					<td></td>
		          				</tr>
		        			</table>
		        			</form>
		        			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
		        				<table style="width:100%;">
					        		<tr>
					        			<td style="width:100%;">
					        				<a class="nui-button" iconCls=""  onclick="onAddNode()" >添加子机构</a>
								            <a class="nui-button" iconCls=""  onclick="edit()" >修改</a>
								            <a class="nui-button" iconCls=""  onclick="del()" >删除</a>
								            <a class="nui-button" iconCls=""  id="download" onclick="download()" >下载</a>
					        			</td>
					        			<td style="white-space:nowrap;">
					 						<a class="nui-button" iconCls="" id="addBtn" onclick="reset()">重置</a>
					 						<a class="nui-button" iconCls="" onClick="search()">查询</a>
					        			</td>
					        		</tr>
		        				</table>
		    				</div> 
		          			<div class="nui-fit" style="width:100%;">
		               			<div id="datagrid1"  idfield="id" class="nui-datagrid" sortMode="client" allowUnselect="false"
					               	style="width: 100%;height: 100%;" url="<%=root%>/org/getListOrg" autoEscape="false"  >
					             	<div property="columns">  
					             	   <div type="indexcolumn" width="35">序号</div>                    
						               <div field="orgnum" width="100" align="right" headerAlign="left">机构号</div>            
						               <div field="orgname" width="100" align="left" headerAlign="left">机构名称</div>
						               <div field="by2" width="100" align="left" headerAlign="left" renderer="onRenderBy2">层级</div>                  
						               <div field="sjorgnum" width="100" align="right" headerAlign="left">上级机构号</div>
						               <div field="sjorgname" width="120" align="left" headerAlign="left">上级机构名称</div>
					            	</div>
		         				</div>
		          			</div> 
					</div>
					<div id="tab2" name="position"  title="岗位信息" >
							<form id="form2">
								<table  >
			          				<tr>
			          					<th>机构：</th>
						            	<td>
							                <input id="orgSelect2" class="nui-treeselect" url="<%=root%>/org/getList" dataField="datas" 
									 			name="orgnum" textfield="orgname" valuefield="id" parentfield="upid"  
									 			valueFromSelect="false" multiSelect="false" expandOnLoad="0"
							 					allowInput="false" showClose="true" oncloseclick="onCloseClick" 
													showRadioButton="true" showFolderCheckBox="false" popupWidth="305" 
													popupHeight="470" popupMaxHeight="600"/>
						            	</td>
						            	<th>机构号：</th>
			          					<td>
			          						 <input  name="jgh" class="nui-textbox"   />
			          					</td>
			          				</tr>
			          				<tr>
			          					<th>岗位名称：</th>
			          					<td>
				          					<input name="name" class="nui-textbox" />
			          					</td>
			          					<th></th>
			          					<td></td>
			          				</tr>
			        			</table>
		        			</form>
		        			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
		        				<table style="width:100%;">
					        		<tr>
					        			<td style="width:100%;">
					        				<a class="nui-button" iconCls=""  onclick="onAddPositionNode()" >添加</a>
								            <a class="nui-button" iconCls=""  onclick="onEditPositionNode()" >修改</a>
								            <a class="nui-button" iconCls=""  onclick="onDelPositionNode()" >删除</a>
								            <a class="nui-button" iconCls=""  id="downloadPosition" onclick="downloadPosition()" >下载</a>
					        			</td>
					        			<td style="white-space:nowrap;">
					 						<a class="nui-button" iconCls="" id="addBtn" onclick="position_reset()">重置</a>
					 						<a class="nui-button" iconCls="" onClick="position_search()">查询</a>
					        			</td>
					        		</tr>
		        				</table>
		    				</div> 
		          			<div class="nui-fit" style="width:100%;">
		               			<div id="datagrid2"  idfield="id" class="nui-datagrid" sortMode="client" allowUnselect="false"
					               	style="width: 100%;height: 100%;" url="<%=root%>/org/positionList"  multiSelect="true" autoEscape="false"  >
					             	<div property="columns">  
					             		<div type="checkcolumn" name="checkCloumn"></div>    
					             	    <div type="indexcolumn" width="35">序号</div>                    
					             	    <div field="name"  headerAlign="center" allowSort="true"  align="center">岗位名称</div> 
							            <div field="position_no"  headerAlign="center" allowSort="true"  align="center">岗位ID</div>                
							            <div field="describe"  allowSort="true" headerAlign="center" align="center">岗位描述</div>            
							            <div field="duty"  allowSort="true" headerAlign="center" align="center">岗位职责</div> 
							            <div field="term"  allowSort="true" headerAlign="center" dateFormat="yyyy-MM-dd" align="center">岗位任期</div> 
							            <div field="term_days"  allowSort="true" headerAlign="center"  align="center" >任期天数</div>
							            <div field="orgname"  allowSort="true" headerAlign="center"  align="center" >所属机构</div>
							            <div field="orgnum"  allowSort="true" headerAlign="center"  align="center" >所属机构号</div>
							            <div name="action"  renderer="onActionRenderer" headerAlign="center" align="center">岗位用户分配</div>
					            	</div>
		         				</div>
		          			</div> 
					</div>
				</div>
			</div>
    		</div>
	</body>
</html>

<script type="text/javascript">
	$G.parse();
	var tabs = $G.get("tabs1");
	var tree = $G.get("tree1");
	var grid = $G.get("datagrid1");
	var position_grid = $G.get("datagrid2");
	var form = $G.getForm("form1");
	var position_form = $G.getForm("form2");
	
	grid.load();
	position_grid.load();

	//选中节点，右侧展示
    tree.on("nodeclick", function (e) {
    	var tab = tabs.getActiveTab();
    	if(tab.name == "org")grid.load({ pid: e.node.id});
    	if(tab.name == "position")position_grid.load({orgnum: e.node.orgnum});
    });

  	//添加子节点 
    function onAddNode(e) {
        var node = tree.getSelectedNode();
        var newNode = {orgname: "新建节点"};
        if(node){
	        tree.addNode(newNode, "add", node);
	        var parentId = node.id;
	        var orgnum = node.orgnum;
	        var orgname = node.orgname;
	        var url="<%=root%>/org/add";
	        var bizParams = { pageType: "add", pid: parentId,parentNum: orgnum,parentName:orgname};
	        $G.showmodaldialog("新增机构", url, 500, 250, bizParams, function(action){
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
    		var orgnum = row.sjorgnum;
            var orgname = row.sjorgname;

            var url="<%=root%>/org/add";
	        var bizParams = { pageType: "edit", id:id,parentNum:orgnum,parentName:orgname};
	        $G.showmodaldialog("新增机构", url, 500, 250, bizParams, function(action){
	        	grid.reload();
		    });
            
	        
    	}else{
    		$G.alert("请先选择一行数据！");
    	}
	}

	//删除机构
	function del(){
		var row = grid.getSelected();
    	if(row){
    		$G.confirm("确定删除选中节点?","删除机构",function(action){
    			if(action == "ok"){
    				var ajaxConf = new GcdsAjaxConf();
    				ajaxConf.setIsAsync(false);
    				ajaxConf.setIsShowSuccMsg(false);
    				ajaxConf.setIsShowProcessBar(false);
    				ajaxConf.setSuccessFunc(function (text) {
    					var flag =$G.decode(text).flag;
						if(flag == "1"){
          					$G.alert("删除成功！");
          				}else if(flag == "2"){
          					$G.alert("此机构下人员非空，不能删除！");
          				}else {
          					$G.alert("请先删除子机构！");
          				}
          				grid.reload();
          				tree.reload();
    				});
    	          	var urlStr = "<%=root%>/org/del";
    				$G.postByAjax({id:row.id,orgnum:row.orgnum},urlStr,ajaxConf);
    			}
			});
    	}else{
    		$G.alert("请先选择一行数据！");
        }
	}

	//下载
	function download(){
		var orgid = mini.getbyName("orgid").getValue();
		var jgh = mini.getbyName("jgh").getValue();
		var orgname = mini.getbyName("orgname").getValue();
		window.location="<%=root%>/org/download?orgid=" + orgid + "&jgh=" + jgh + "&orgname=" + orgname;
	}
	
	//下载
	function downloadPosition(){
		var orgid = mini.getbyName("orgid").getValue();
		var jgh = mini.getbyName("jgh").getValue();
		var orgname = mini.getbyName("orgname").getValue();
		window.location="<%=root%>/org/downloadPosition?orgid=" + orgid + "&jgh=" + jgh + "&orgname=" + orgname;
	}

	function onRenderBy2(e){
		//{id:1,text:'建设局下属机构'},{id:2,text:'区镇'},{id:3,text:'街道'},{id:4,text:'社区'}
		if(e.value==1){
			return '省行';
		}else if(e.value==2){
			return '分行';
		}else if(e.value==3){
			return '支行'
		}else if(e.value==4){
			return '责任中心';
		}
	}
	
	//新增
	function onAddPositionNode() {
		var node = tree.getSelectedNode();
        if(node){
        	var url = "<%=root%>/org/addPosition";
        	var bizParams = {pageType:"add",orgnum:node.orgnum};
            $G.showmodaldialog("新增岗位", url, 500, 350, bizParams, function(action){
            	position_grid.reload();
    	    });
        }else{
        	$G.alert("添加岗位时，请先选中父机构");
        }
        
	}

	//编辑
	function onEditPositionNode(){
		var row = position_grid.getSelected();
		if (row) {
			var url = "<%=root%>/org/addPosition";
			var bizParams = {pageType:"edit",id:row.id};
	        $G.showmodaldialog("编辑岗位", url, 500, 350, bizParams, function(action){
	        	position_grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	//删除
	function onDelPositionNode(){
		var rows = position_grid.getSelecteds();
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
                		position_grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/org/delPosition", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
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
	
	//查询
	function position_search(){
		var data = position_form.getData();
		position_grid.load(data);
	}

	//重置
	function position_reset(){
		position_form.reset();
	}
	
	
	
	function onActionRenderer(e){
		var obj = '<a href="javascript:userFun()" style="img-decoration:none"><img src="<%=root%>/resource/nui/themes/icons/user.png" border="0"></a>';
		return obj;
	}

	function userFun(){
		var row = position_grid.getSelected();
	   	var urlStr = "<%=root%>/org/positionAllot";
		var bizParams = {name:row.name,id:row.position_no};
		$G.showmodaldialog("岗位用户分配", urlStr, 800, 500, bizParams, function(action){
			position_grid.reload();
		});
	}
	
	function onActivechanged(e){
		/* 
		var node = tree.getSelectedNode();
		var tab = tabs.getActiveTab();
    	if(tab.name == "org")grid.load({ pid: node.orgnum}); 
    	if(tab.name == "position")position_grid.load({orgnum: node.orgnum}); */
	}
	
</script>