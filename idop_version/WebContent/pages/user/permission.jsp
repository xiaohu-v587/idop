<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
		<title>公金联盟</title>
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
		</style>
	</head>
	<body>
       	    <div class="mini-fit">
			    <input name="id"  class="mini-hidden" />
			    <ul id="tree1"  name="tree1" class="mini-tree"  url="<%=root%>/org/getListByUser?noliketype=2" style="width:100%;padding:5px;" 
				    dataField="datas" textfield="orgname" idfield="orgnum" parentfield="upid" showTreeIcon="true"  
				    resultAsTree="false" expandOnLoad="0" showFolderCheckBox="true">        
                </ul>
		    </div>    
		    <div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
			     borderStyle="border-left:0;border-bottom:0;border-right:0;">
                <a id="btnOk" class="mini-button" iconCls="" onclick="onOk" style="margin-right: 20px;">提交</a>
                <a id="btnCancle" class="mini-button" iconCls="" onclick="onCancel()" >取消</a>
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
  //标准方法接口定义
    function setData(data) {
   	   data = mini.clone(data);
       var id = data.id;
       mini.getbyName("id").setValue(id); 
       
       $.ajax({
            url: "<%=root%>/user/getHaveOrg",
            type : 'post',
            data:{key:data.id},
            cache: false,
            success: function (text) {
            	var data = text.orgNums;
            	if (null != data) {
					nui.get("tree1").setValue(data);
				}
            }
        }); 
    }
    
    /*提交选择的*/
    function SaveData(){
   	    var orgnum =  mini.getbyName("tree1").getValue();
    	var id =  mini.getbyName("id").getValue();
    	
		if(orgnum != null && id!=null &&orgnum!=""&&id!=""){
			$.ajax({
				url : "<%=root%>/user/permissionOrgEdit",
				type : 'post',
				data : {
					org_id : orgnum,
					id:id
				},
				cache : false,
				success : function(text) {
					//nui.alert("操作成功！");
					$G.closemodaldialog("ok");
				},
				error : function(jqXHR, textStatus, errorThrown) {
					alert(jqXHR.responseText);
					$G.closemodaldialog("cancel");
				}
			});
			mini.alert("设置成功!");
		} else {
			mini.alert("请选中机构!");
		}
    }
    
    //确定提交
    function onOk() {	
    	SaveData();
    }
    //
    function CloseWindow(action) {        
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();            
    }
  
    //取消  
    function onCancel(e) {
        CloseWindow("cancel");
    }
    
    
 </script>
