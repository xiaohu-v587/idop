<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
   * 信息模块配置管理
   *
   * Created on 
   * @author  
   * @reviewer 
-->
<head>
    <title>信息模块配置管理</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/js/jquery.min.js"></script>
  		<%@include file="/common/nuires.jsp" %>
</head>
  <body>
<style type="text/css">
    html, body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
    
</style>
   <div class="mini-layout"  style="width:99.8%;height:99.7%;" borderStyle="border-right:1px #aaa solid;">
    <div  showCollapseButton="false" style="border: 0;">
    <div  class="mini-toolbar" style="padding: 5px;height:30px;"   id="inner_querypanel">
          <form id="form1">
             <table id="table1">
                <tr>
                	<td >登记簿名称:</td>
		            <td >
		           		<input class="mini-textbox" name="djbmc"></input>&nbsp;&nbsp;
		            </td>
		            <td style="width:90px;">适用机构:</td>
					<td style="width:300px;" colspan="3"><input id="orgid" name="orgid" style="width:300px;"
									class="mini-treeselect" url="<%=root%>/org/getListByUser"
									dataField="datas" textField="orgname" valueField="id"
									parentField="upid" valueFromSelect="false" multiSelect="false"
									expandOnLoad="0" emptyText="机构" allowInput="false"
									showClose="true" oncloseclick="onCloseClick" 
									showRadioButton="true" showFolderCheckBox="false"
									popupWidth="286" popupHeight="470" popupMaxHeight="600" /></td>
              		<td >
	                	<a class="mini-button"  onclick="search()">查询</a>
	                	&nbsp;&nbsp;
	                	<a class="mini-button"  onclick="reset()">重置</a>
	                	&nbsp;&nbsp;
		              <a class="mini-button" onclick="add()">
		                	新增
		              </a>&nbsp;&nbsp;
		              <a id="update" class="mini-button"  onclick="edit()">
		                	修改
		              </a>&nbsp;&nbsp;
		              <a class="mini-button"  onclick="remove()">
		               	 	删除
		              </a>
		               <a class="mini-button"  onclick="useContrlStart()">
		               	 	启用
		              </a>
		               <a class="mini-button"  onclick="useContrlEnd()">
		               	 	停用
		              </a>
            		</td>
                </tr>
             </table>
             </form>
            </div>
      <div class="mini-fit">
        <div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;height:100%;" multiSelect="true"  allowUnselect="false"
				    url="<%=root%>/djb/query" oncellclick=""   
				    autoEscape="false" ondrawcell="" onshowrowdetail="onShowRowDetail">
          <div property="columns">
            <div type="checkcolumn">
            </div>
            <div type="indexcolumn"  width="40" headerAlign="center">
              	序号
            </div>
            <div field="id" headerAlign="center" width="120" allowSort="true" visible="false"  align="center">
              id
            </div>
            <div field="flag" headerAlign="center" width="120" allowSort="true"  visible="false"  align="center">
              flag
            </div>
            <div field="sfpz" headerAlign="center" width="120" allowSort="true" visible="false"  align="center">
              sfpz
            </div>
            <div field="djbmc" headerAlign="center"  width="120" allowSort="true"  align="center" >
              登记簿名称
            </div>
            <div field="orgname" headerAlign="center"   width="150" allowSort="true"  align="center" >
              创建机构
            </div>
            <div field="org_name" headerAlign="center"   width="150" allowSort="true"  align="center" >
              适用机构
            </div>
            <div field="cjr" headerAlign="center"  width="80" allowSort="true"   align="center">
              创建人
            </div>
            <div field="cjrq" headerAlign="center"  width="80" allowSort="true"  align="center" >
              创建日期
            </div>
            <div field="useflag" headerAlign="center" width="120" allowSort="true" renderer="onqtyRenderer"   align="center">
              是否启用
            </div>
             <div field="startusetime" headerAlign="center" width="120" allowSort="true"   align="center">
              启用时间
            </div>
             <div field="endusetime" headerAlign="center" width="120" allowSort="true"    align="center">
              停用时间
            </div>
            <div field="cz" headerAlign="center" width="200" allowSort="true" renderer="onCzRenderer"  align="center">
              操作
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script type="text/javascript">
    mini.parse();
    var grid = $G.get("datagrid1");
    var form =$G.getForm("form1");
    var updateBtn;
    $(function(){
	      grid = mini.get("datagrid1");
	      form = new mini.Form("form1");
	      updateBtn = mini.get("update");
	      grid.load();
    });
    
	
	function onCjrqRenderer(e){
        var value = e.value;
        if (value)  return mini.formatDate(value, 'yyyy-MM-dd');
   		return "";
	}
	
	//新增TDjbJcxx
    function add() {
      var bizData = {
        pageType : "add"
        };//传入模态窗口页面的json数据
        mini.open({
	            title: "新增登记簿",
	            url: "<%=root%>/djb/detail",
	            width: 600,
	            height: 350,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = bizData;  //模拟传递上传参数
	                iframe.contentWindow.SetData(data);
	            },
	            ondestroy: function (action) {
	                if (action == "save") {
	                    var iframe = this.getIFrameEl();
	                    //var data = iframe.contentWindow.GetData();
	                    //data = mini.clone(data);
	                    //var json = mini.encode(data);
	                    //alert("已完成上传数据：\n" + json);
	                    grid.reload();
	                }
	            }
	        })
      }
    

    //编辑TDjbJcxx
	function edit() {
		var row = grid.getSelected();
        if (row) {
        	var bizData = {
	            pageType : "edit",
	            id : row.id,
	            stat : row.flag
            };
        
         mini.open({
	            title: "修改登记簿",
	            url: "<%=root%>/djb/detail",
	            width: 600,
	            height: 350,
	            allowResize: false,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = bizData;  //模拟传递上传参数
	                iframe.contentWindow.SetData(data);
	            },
	            ondestroy: function (action) {
	                if (action == "edit") {
	                    var iframe = this.getIFrameEl();
	                    //var data = iframe.contentWindow.GetData();
	                    //data = mini.clone(data);
	                    //var json = mini.encode(data);
	                    //alert("已完成上传数据：\n" + json);
	                    grid.reload();
	                }
	            }
	        })
        
        
       
        } else {
            mini.alert("请选中一条记录", "提示");
        }
    }
    
    function useContrlStart() {
       var row = grid.getSelected();
       		 if (row=="undefined"||row==undefined||row==null) {
        		mini.alert("请选中一条记录", "提示");
        	 }else{
        	 	var id = row.id;
        	 	if(row.useflag=="0"){
        		$.ajax({
		                     url: "<%=root%>/djb/updateuse",
		                     data:{id:id},
		                     success: function (text) {
		                    	mini.alert(text.remark);
		                     	grid.reload();
		                     }
		                });
        		}else{
    		  		mini.alert("请选择已停用的纪录", "提示");

    			}
        	} 
    
    }
    
     function useContrlEnd() {
       var row = grid.getSelected();
        if (row=="undefined"||row==undefined||row==null) {
        	mini.alert("请选中一条记录", "提示");
        }else{
        	 if(row.useflag=="1"){
        	 var id = row.id;
      
        	$.ajax({
		                     url: "<%=root%>/djb/updatenouse",
		                     data:{id:id},
		                     success: function (text) {
		                    	mini.alert(text.remark);
		                     	grid.reload();
		                     }
		                });
		                 }else{
       						 mini.alert("请选择已启用的纪录", "提示");
       					 } 
        
        }
       
    
    
    }

    //删除TDjbJcxx
    function remove() {
    	//角色等级role_level
    	var role_level = "${role_level}";
        var rows = grid.getSelecteds();
        var tips = "";
        var disflag = "0";
        var name = "${name}";

        for(var s=0;s<rows.length;s++){
        	var row =rows[s];
        	if(row.flag == 1){
        		if(tips == ""){
					tips = row.djbmc;
				}else{
					tips = tips + ","+row.djbmc;
				}
        		disflag = "1";
        	}
        }
        
        if (rows.length > 0) {
     	       if(rows[0].flag == "1"&&name!="系统管理员"){
     	       		mini.alert("已存在数据，不允许删除");
	            }else{
	              mini.confirm("确定删除选中记录？", "删除提示", function(action) {
	            	if (action == 'ok') {
		            	var ids = getRowsParaToJoin(rows,'id');
		                $.ajax({
		                     url: "<%=root%>/djb/delete",
		                     data:{ids:ids},
		                     success: function (text) {
		                    	mini.alert(text.remark);
		                     	grid.reload();
		                     }
		                });
	                }
	            });
	            }
        } else {
             mini.alert("请选中一条记录", "提示");
        }
    }

    //重新刷新页面
    function refresh() {
        grid.reload();
        updateBtn.enable();
	}

        //查询
        function search() {
          grid.load(form.getData(false,true));//grid查询
        }

        //重置查询条件
        function reset() {
          form.reset();
        }

        //enter键触发查询
        function onKeyEnter(e) {
          search();
        }

        //当选择列时
        function selectionChanged() {
          var rows = grid.getSelecteds();
          if (rows.length > 1) {
            updateBtn.disable();
          } else {
            updateBtn.enable();
          }
        }
        
        function onCzRenderer(e){
            var record = e.record;
            var s = ' <a class="Edit_Button" href="javascript:yspz(\'' + record.id + '\')">要素配置</a>'
                    + ' <a class="Edit_Button" href="javascript:lbzspz(\'' + record.id + '\')">列表展示页配置</a>'
                    // + ' <a class="Edit_Button" href="javascript:lcpz(\'' + record.id + '\')">流程配置</a>'; 
            return s;
        }
        
        function onqtyRenderer(e){
            var value = e.value;
            if(value=="0"){
            	return "停用";
            }else{
            	return "启用";
            }
            
        }
        
        //要素配置
        function yspz(uid){
        	var row = grid.getSelected();
	       if (row.id == uid) {
	        	if(row.flag == "1"){
	        		mini.confirm(row.djbmc+"中已有数据，若重新配置会清空数据，请谨慎操作！","提示", function(action) {
	        			if (action == 'ok') {
	        				var bizData = {
	        			        id:row.id,
	        			        pageType : "edit"
	        			    };
	        			    $G.showmodaldialog("登记簿要素配置","<%=root%>/djb/ysblist",1350, 600,bizData,function (action) {
	        			        grid.reload();
	        			    });
	        		    }
	                });
	        	}else{
		          var bizData = {
		            id:row.id,
		            pageType : "edit"
		          };
		            $G.showmodaldialog("登记簿要素配置","<%=root%>/djb/ysblist",1200, 600,bizData,function (action) {
		                grid.reload();
		     		});
	        	}
		     } else {
		         mini.alert("请选中一条记录", "提示");
		     }
        } 
        
        //列表展示页配置
      	function lbzspz(uid){
      		var row = grid.getSelected();
	        if (row.id == uid) {
				var bizData = {pageType : "add",record:row};//传入模态窗口页面的json数据
				$G.showmodaldialog("列表展示页配置","<%=root%>/djb/lbzspzDetail",800, 600,bizData,function (action) {
	                grid.reload();
	     		});
		    }
      	}
      	
      	//流程配置
      	function lcpz(uid){
      		var row = grid.getSelected();
      		if(row.sfpz == "0"){
      			mini.alert("已设置为无流程配置","提示");
      			return;
      		}
	        if (row.id == uid) {
				var bizData = {pageType : "edit",record:row,djbid:uid};//传入模态窗口页面的json数据
				$G.showmodaldialog("流程配置","<%=root%>/djb/lcpzDetail?djbid="+uid,750, 390,bizData,function (action) {
	                grid.reload();
	     		});
		    }
      	}
      	 // 机构下拉框清空
        function onCloseClick(e) {
            var obj = e.sender;
            obj.setText("");
            obj.setValue("");
        }
        
      </script>
    </body>
  </html>
