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
        <div class="mini-fit" id= "bigDiv">
            <form id="form1" method="post" style="height: 100%;">
				<input class="mini-hidden" id="roleId"/>
            	<input class="mini-hidden" id="roleName"/>
            	<input class="mini-hidden" id="delIds"/>
            	<input class="mini-hidden" id="addGridRows"/>
				<div style="width:99%; height: 99%; "  >
					<table style="height: 98%; width: 100%; border: #aaa solid 0px;">
						<tr style="height: 100%">
							<td style="width: 40%; heigt:100%; border: #aaa solid 0px;">
								<fieldset style="border:solid 1px #aaa; height: 98%;">
									<legend>待分配人员</legend>
									<div id="tree1" class="mini-tree" style="width:100%;height:400px; padding:5px;"
										showTreeIcon="true" resultAsTree="false" showCheckBox="true" checkRecursive="true"
									    allowResize="true" expandOnLoad="0" allowSelect="true" enableHotTrack="false"  iconField="iconcls"
									    url="<%=root%>/role/getOrgUserList" dataField="datas" textField="name" idField="id" parentField="upid" >
               						</div>
								</fieldset>   
							</td>
							<td style="width: 15%; height:100%; border: #aaa solid 0px;" align="center">
								<input type="button" value=">" onclick="add()"  style="width:60px;" /><br />
								<input type="button" value="&lt;&lt;" onclick="removes(2)" style="width:60px;"/><br />
								<input type="button" value="&lt;" onclick="removes(1)" style="width:60px;"/><br />
							</td>
							<td style="width: 45%; height:100%; border: #aaa solid 0px;">
								<fieldset style="border:solid 0px #aaa;padding:0; height: 98%;">
									<legend>角色&nbsp;<label style="color: red;" id="roleNameFill"></label>&nbsp;已分配人员：</legend>
									<div id="datagrid1" class="mini-datagrid" style="width:100%;height:96%;" onload = "gridonload"
										url="<%=root%>/role/getUsersByRoleId" dataField="datas" idfield="id" valuefield="id"
										showPageInfo = "true" multiSelect="true" pageSize="10" sizeList="[10,20,50,100]">
											<div property="columns">
										        <div type="indexcolumn"></div>
										        <div type="checkcolumn"></div> 
										        <div field="name" width="120" headerAlign="center" align="center" allowSort="true">员工姓名</div> 
										        <div field="user_no" width="120" headerAlign="center" align="center" allowSort="true">员工号</div>   
										        <div field="pname" width="120" headerAlign="center" align="center" allowSort="true">机构名称</div>
										        <div field="upid" visible="false"></div>    
										        <div field="flag" visible="false"></div>    
										        <div field="scome" visible="false"></div>    
										    </div>
									</div>
								</fieldset>
							</td>
						</tr>
					</table>
				</div>				
            </form>
       	</div>
       	<div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
       		borderStyle="border-left:0;border-bottom:0;border-right:0;">
            <a id="btnCancle" class="mini-button" iconCls="" onclick="onCancel()" >关闭</a>
            <a id="btnOk" class="mini-button" iconCls="" onclick="onOk()" enabled="false" style="margin-right: 20px; ">确定</a>
      	</div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form =  $G.getForm("form1");
	var tree1 = $G.get("tree1");
	var datagrid1 = $G.get("datagrid1");

	function setData(data) {
   	    data = $G.clone(data);
   	    var roleId = data.id;
   	    var roleName = data.rolename;
   	    // 隐藏域保存角色id和名称
   	    mini.get("roleId").setValue(roleId);
   	    mini.get("roleName").setValue(roleName);
   	    $("#roleNameFill").text(roleName);
   	    // 加载数据，查询所有具有roleId的人员
   	  	datagrid1.load({roleId:roleId});
   }

	// datagrid1加载完成时调用，如果有从树里增加的记录，就显示，默认选中
	function gridonload(){
		var addGridRows = $G.get("addGridRows").getValue();
		if(addGridRows != null && addGridRows != ""){
			var rows = $G.decode("[" + addGridRows + "]");
			datagrid1.addRows(rows,0);
			datagrid1.selects( rows, false);
		}
	}

	 //添加从左边树中选中的人员  
    function add() {
  	    var msgid;
   	    var nodes = tree1.getCheckedNodes(false); // 获取选中的树节点
   	    var delIds = $G.get("delIds").getValue();
   	    if(nodes.length > 0){
   	    	if(nodes.length  > 50){
   	    		$G.alert("单次移动条数过多");
   	    		return;
   	    	}
   	    	loading();
			var rows = [];
			// 循环树节点
			for(var i = 0; i < nodes.length; i++){
				var node = nodes[i];
				if(node.flag == "user"){
					var row;
   	    			if(node.scome2 == "rgrid"){
						row = "{" + "id:'" + node.id 
		   				 	+ "',name:'" + node.name 
							+ "',pname:'" + tree1.getParentNode(node).name 
			   				+ "',upid:'" + node.upid
			   				+ "',user_no:'" + node.user_no
			   				+ "',flag:'" + node.flag
			   				+ "',iconcls:'"+node.iconcls
			   				+ "'}";
   	    			 }else{
   	    				row = "{" + "id:'" + node.id 
		   				 	+ "',name:'" + node.name 
			   					+ "',pname:'" + tree1.getParentNode(node).name 
			   					+ "',upid:'" + node.upid
			   					+ "',user_no:'" + node.user_no
			   					+ "',flag:'" + node.flag
			   					+ "',iconcls:'"+node.iconcls
			   					+ "',scome:'" + "ltree"
			   					+ "'}";
   	    			 }
   	    			
					if(node.scome2 == "rgrid"){
						// 表示删除后的，不需要加到待增加中,在待删除中去掉它
						var id = node.id;
						var index1 = delIds.indexOf(id);
						var index2 = delIds.indexOf("'",index1)
						if((index1 - 1) != 0){// 不是第一个
      						index1 = index1 - 1;
						}
						var delstr = delIds.substring((index1 - 1), (index2 + 1));
						delIds = delIds.replace(delstr,"");
					}else{
						rows.push(row);
					}
					row = mini.decode("[" + row + "]");
					datagrid1.addRows(row,0);
					if(node.scome2 != "rgrid"){
						datagrid1.selects(row, false);
					}
					tree1.removeNode(node);
				}
			}
			rows = rows.toString();// 将这些变量保存
			// 获取之前的增加的记录
			var addGridRows = mini.get("addGridRows").getValue();
			// 如果之前的删除记录和现在的删除记录都存在，那么准备在中间加上逗号
			if(addGridRows != null && addGridRows != "" && rows != null && rows != ""){
				addGridRows = addGridRows + ",";
			}
			addGridRows = addGridRows + rows;
     		  // 保存隐藏域中
			$G.get("addGridRows").setValue(addGridRows);
			$G.get("delIds").setValue(delIds);
   	     }
		setbtnOk();
		unloading();
   }
   
	//移除右边选中的人员
	function removes(e) {
		var msgid;
  	  	var rows;
  	  	var delIds = [];
  	  	var roleId = mini.get("roleId").getValue();
  	  	var addGridRows = mini.get("addGridRows").getValue();
  	  	if(e == "1"){ // 选中的行
			rows = datagrid1.getSelecteds();
  	  	}else if(e == "2"){ // 全部的行
			rows = datagrid1.getData();
		}
		/*******/
		var rowsNum = rows.length;
		if(rows != null && rows.length > 0){
			if(rows.length > 50){
				$G.alert("单次移动条数过多");
				return;
			}
			loading();
			// 循环所有的行
			for(var i = 0; i < rows.length; i++){
				var row = rows[i];// 获取当前行
				var pnode = tree1.getNode(row.upid); // 在tree1里获取当前行的父节点
				var node;// 将当前行组成树的节点
				if(row.scome == "ltree"){
					// 表示是从左侧树中取得
					node = "{" + "id:'" + row.id + "',name:'" + row.name+ "',user_no:'" + row.user_no
				  		+ "',upid:'" + row.upid + "',flag:'" + row.flag+ "',iconcls:'" +row.iconcls+"'}";
				}else{
					node = "{" + "id:'" + row.id + "',name:'" + row.name+ "',user_no:'" + row.user_no 
				  		+ "',upid:'" + row.upid + "',flag:'" + row.flag+ "',scome2:'" + "rgrid" + "',iconcls:'" +row.iconcls+ "'}";
				}
				node = mini.decode(node);
				var index = 0;
				tree1.eachChild (pnode, function(node){
					if(tree1.isLeaf(node)){
						return false;
					}else{
						index++;
					}
				});
				// 将节点加到树上
				tree1.addNode (node, index, pnode);
				// 展开被插入及节点的所有父节点
				// 获取被插入的父节点的所有父节点集合，遍历展开
				if(rowsNum <= 3){
					tree1.expandPath ( node );
				}else{
					if(i == rowsNum-1)
						tree1.expandPath ( node );
				}
				// datagrid1删除行，false表示下一行不默认选中
				datagrid1.removeRow (row, false);
				// 记录删除的行数据
				if(row.scome == "ltree"){// 表示此条数据是从左侧添加的，还没有保存到数据库
					var id = row.ID;
					if(addGridRows != null && addGridRows != ""){
						var index1 = addGridRows.indexOf(id);
						var index2 = addGridRows.indexOf("}",index1);
						if((index1-5) != 0){
							// 表示不是位于开始
							index1 = index1 -1;
						}
						var delStr = addGridRows.substring((index1-5),index2 + 1);
						addGridRows = addGridRows.replace(delStr,"");
					}
				}else{
					// 表示数据时是真实存在的，做记录
					delIds.push("'" + row.id + "'");
				}
			}
			// 删除的记录放到隐藏域中
			delIds = delIds.toString();
    		// 获取之前的删除记录
    		var delIdsBefore = mini.get("delIds").getValue();
    		// 如果之前的删除记录和现在的删除记录都存在，那么准备在中间加上逗号
    		if(delIdsBefore != null && delIdsBefore != "" && delIds != null && delIds != ""){
    			delIdsBefore = delIdsBefore + ",";
			}
			delIds = delIdsBefore + delIds;
			// 保存隐藏域中
			mini.get("delIds").setValue(delIds);
    		mini.get("addGridRows").setValue(addGridRows);
			// 重新加载datagrid1并且去掉隐藏的删除记录
			datagrid1.load({delIds:delIds,roleId:roleId});
		}else{
			mini.alert("请选中一条或是多条！");
		}
  	  	setbtnOk();
  	  	unloading();
	}
    
    // 判断确定按钮是否可用
	function setbtnOk(){
		var addGridRows = mini.get("addGridRows").getValue(); 
		var delIds = mini.get("delIds").getValue();
		if(addGridRows != ""  || delIds != ""){
			$G.get("btnOk").enable();
		}else{
			$G.get("btnOk").disable();
		}
	}
   
	// 保存和删除选中的人员
	function SaveData(){
		// 获取数据，角色id，选中的行，删除的数据
		var roleId = $G.get("roleId").getValue();
		var addGridRows = $G.get("addGridRows").getValue(); 
		var delIds = $G.get("delIds").getValue();
		var rows = datagrid1.getSelecteds();
		var saveIds = [];
		if(rows != null){
			for(var i = 0; i < rows.length; i++){
				saveIds.push("'" + rows[i].id + "'");
			}
		}
		var saveNum = saveIds.length; // 保存的数量
		var delNum = 0; // 删除的数量
		if(delIds != null && delIds != ""){
			delNum = delIds.split(",").length;
		}
		saveIds = saveIds.toString();
		
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsAsync(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setSuccessFunc(function (data) {
			if(data=="2"){
				$G.alert("无此权限，请与管理员联系");
			}else{
				var saveFlag = mini.decode(data).saveFlag;
				var delFlag = mini.decode(data).delFlag;
				var flag = mini.decode(data).flag;
				if(saveFlag == saveNum && delFlag == delNum){
					$G.alert("更新成功!");
					$G.get("addGridRows").setValue("");
					$G.get("delIds").setValue("");
					datagrid1.load({roleId:roleId});
					datagrid1.deselectAll();
					tree1.uncheckAllNodes();
					$G.get("btnOk").disable();
					tree1.load();
				}else {
					$G.alert("保存失败");
				}
			}
		});
      	var urlStr = "<%=root%>/role/updateRoleUser";
		$G.postByAjax({roleId:roleId,saveIds:saveIds,delIds:delIds},urlStr,ajaxConf);
   }
    
    // 确定保存
    function onOk() {
    	SaveData();
    }
    
    // 取消  
    function onCancel(e) {
    	$G.closemodaldialog("cancel");
    } 
    
    function loading() {
        mini.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '操作中...'
        });
    }
    
    function unloading() {
        setTimeout(function () {
            mini.unmask(document.body);
        }, 0);
    }
</script>