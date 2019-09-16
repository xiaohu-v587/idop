<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@ include file="/common/nuires.jsp"%>
		<style type="text/css">
			html,body {
				font-size: 12px;
				padding: 0;
				margin: 0;
				border: 0;
				height: 100%;
				overflow: hidden;
			}

			#detailTable tr {
				line-height: 30px;
			}
		</style>
	</head>
	<body>
		<div class="mini-fit">
			<input name="id" class="mini-hidden" />
			<ul>
				<li align="left">
					首页选择：<input name="mainpage" id="mainpage" class="nui-dictcombobox" textfield="dictName" value = "0" valuefield="dictID" dictTypeId="MAIN_PAGE"/>
				</li>
			</ul>
			<ul id="tree1"  class="mini-tree"  url="<%=root%>/permission/getCheckTree" style="width:300px;padding:5px;" 
				dataField="datas" textfield="name"  idfield="id" parentfield="pid" showTreeIcon="true" textField="text" 
				resultAsTree="false"  expandOnLoad="true" showCheckBox="true" checkRecursive="true"   
				onbeforenodecheck="onBeforeNodeCheck" allowSelect="false" enableHotTrack="false" autoCheckedField="false">        
            </ul>
		</div>    
		<div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
			borderStyle="border-left:0;border-bottom:0;border-right:0;">
            <a id="btnOk" class="mini-button" iconCls="icon-ok" onclick="onOk()" style="margin-right: 20px;">确定</a>
            <a id="btnCancle" class="mini-button" iconCls="icon-close" onclick="onCancel()" >取消</a>
		</div>
	</body>
</html>
 <script type="text/javascript">
    mini.parse();
    var grid = mini.get("tree1");
    grid.load(); 
    
    
    function onBeforeNodeCheck(e) {
        var tree = e.sender;
        var node = e.node;
        if (tree.hasChildren(node)) {
            //e.cancel = true;
        }
    }
    
    /*保存选择的菜单*/
    function SaveData(){
    	var rows = grid.getCheckedNodes(true);
    	//mini.alert(mini.encode(rows));
   	    var id  = "";
   	    var did = "";
    	var roleid =  mini.getbyName("id").getValue();
    	
    	var mainpage =  mini.getbyName("mainpage").getValue();
    	if (rows.length > 0) {
			var ids = [];//增删改id
			var djid = [];//菜单id
			for ( var i = 0, l = rows.length; i < l; i++) {
				var r = rows[i];
				if(r._level=="2"){
					ids.push(r.id);
				}
				else{
					djid.push(r.id);
				}
				
			}
			var id = ids.join(',');
			var did = djid.join(',');
		}
    	//mini.alert(id);
    	var o = {
				roleid : roleid,
				menuid : id,
				functionid:did,
				mainpage:mainpage
			};
			var json = mini.encode(o);
			$.ajax({
				url : "<%=root%>/permission/savePower",
				type : 'post',
				data : {
					data : json
				},
				cache : false,
				success : function(text) {
					$G.closemodaldialog("ok");
				},
				error : function(jqXHR, textStatus, errorThrown) {
					alert(jqXHR.responseText);
					CloseWindow();

				}
			});
  
    }
    
    //标准方法接口定义
    function setData(data) {
    	    data = mini.clone(data);
            var id = data.id;
            mini.getbyName("id").setValue(id);
            nui.get("mainpage").setValue(data.mainpage);
            $.ajax({
                url: "<%=root%>/permission/getHaveMenu",
                data:{key:data.id},
                cache: false,
                success: function (text) {
                	var data = text.record;
                	var nodes = grid.findNodes(function(node) {
						return true;
					});
                	
              for ( var i = 0; i < nodes.length; i++) {
						for ( var j = 0; j < data.length; j++) {
							if (nodes[i].id == data[j].menuid&&grid.isLeaf (nodes[i])==true) {
								grid.checkNode(nodes[i]);
								break;
							}
						}
						
					}
                	
                }
            });
    }
    
    function CloseWindow(action) {        
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();            
    }
    
    //确定保存
    function onOk() {
    	SaveData();
    	mini.alert("角色权限分配成功");
    }
    
    //取消  
    function onCancel(e) {
        CloseWindow("cancel");
    }
    
    
 </script>
