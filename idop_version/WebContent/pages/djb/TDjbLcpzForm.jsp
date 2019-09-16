<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
   * 登记簿流程配置主页面
   *
-->
<head>
    <title>登记簿流程配置主页面</title>	
</head>
<body >
<style type="text/css">
    html, body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
</style>
    <div id="layout1" class="mini-layout" style="width:99.8%;height:99.7%;" borderStyle="border-right:1px #aaa solid;" >
         <div title="center" region="center" style="border:0px;">
             <div class="mini-toolbar" style="border-bottom:0;padding:0px;">
             	  <input id="djbid" name="djbid" class="mini-hidden" style="width:150px;"/>
                  <table style="width:100%;">
                       <tr>
                           <td style="width:100%;">
                             <a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()">新增</a>
		            	     <a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()">修改</a>
		            	     <a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="remove()" >删除</a>
                           </td>
                       </tr>
                  </table>
             </div>
             <div class="mini-fit">
                  <div id="datagrid1" dataField="datas" class="mini-datagrid" style="width:100%;height:100%;" sortMode="client" allowUnselect="false"
				    url="<%=root%>/djb/getList1?djbid=${djbid}" oncellclick="" onselectionchanged="onSelectionChanged"  onload="controlBtnState()"
				    autoEscape="false" onshowrowdetail="onShowRowDetail">
				        <div property="columns">
				        	  <div type="checkcolumn" name="checkCloumn"></div>
				              <div headerAlign="center" type="indexcolumn">序号</div>
				              <div field="id" visible="false"></div>
				              <div field="djbid" visible="false"></div>
				              <div field="current_operator_name" width="120" headerAlign="center" align="center"   allowSort="true">当前操作角色</div>
				              <div field="next_operator_group_name" width="120" headerAlign="center" align="center"   allowSort="true">上级审批(复核)角色/下级执行角色</div>
				              <div field="zdmc" width="120" headerAlign="center" align="center"   allowSort="true">上级审批(复核)角色/下级执行角色对应字段</div>
				        </div>
				  </div>  
             </div>
         </div>
    </div>

<script type="text/javascript">
       mini.parse();
       var layout = mini.get("layout1");
       var grid = mini.get("datagrid1");
       grid.load();
       
       //新增事务流程
	function add(){
    	var djbid = mini.get("djbid").getValue();
		mini.open({
			url:"<%=root%>/djb/addObj?djbid="+djbid,
			title: "新增通用事务审批流程维护", width: 550, height: 390,
			onload: function () {
			    var iframe = this.getIFrameEl();
				var data = { action: "add",djbid:djbid};
					iframe.contentWindow.setData(data);
			    },
			    ondestroy: function (action) {
			        if(action=="save"){
			            mini.alert("新增成功！");
			            grid.reload();
			        }
			    }
			});
       }
       
       //修改事务流程
       function edit(){
			var row =grid.getSelected();
			mini.open({
				url:"<%=root%>/djb/addObj?djbid="+row.djbid,
				title: "修改通用事务审批流程维护", width: 550, height: 390,
				onload: function () {
				     var iframe = this.getIFrameEl();
				     var data = { action: "edit",id:row.id,djbid:row.djbid};
				         iframe.contentWindow.setData(data);
				},
				ondestroy: function (action) {
				    if(action=="save"){
				         mini.alert("修改成功！");
				         grid.reload();
				    }
				}
			});
       }
       
        //删除按钮
       function remove(){
			var row =grid.getSelected();
			if(row){
				mini.confirm("确定删除？","提示",
				    function (action){
				         if(action == "ok"){
				           		grid.loading("操作中,请稍后......");
				           		$.ajax({
				           			url:"<%=root%>/djb/delObj",
				           			type: 'post',
				           			data: {key:row.id},
				           			cache: false,
				           			success : function (text){
				           				mini.alert("删除成功");
				           				grid.reload();
				           			}		
				           		});
				           }
				      }	   
				);
			}
       }
        
        //控制按钮的状态 
        function controlBtnState() {
       	 	mini.get("editBtn").disable();
            mini.get("removeBtn").disable();
        }
          
        //当选中grid中的行    
        function onSelectionChanged(e){  
	       	 var row = grid.getSelected();
	       	 if (row) {
	   			mini.get("editBtn").enable();
	   		    mini.get("removeBtn").enable();
	         }
        }
        
		//获取父页面传递来的json数据
		function setData(data) {
			//跨页面传递的数据对象，克隆后才可以安全使用
			var infos = mini.clone(data);
			mini.get("djbid").setValue(infos.djbid);
		}
</script>
</body>
</html>