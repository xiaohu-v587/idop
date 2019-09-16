<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp" %>

<html>
<head>
	<title>定时任务管理</title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<%@ include file="/common/nuires.jsp" %>
</head>
<body>
	<!-- 左右划分布局 -->
	<div id="horizontalSplitter" class="nui-splitter" handlerSize="1px"
		style="width: 100%; height: 100%; " borderStyle="border: 0;">
		<div size="100%" showCollapseButton="false" style="border: 0px">

			<!-- 左边主要操作区上下划分布局 -->
			<div class="nui-splitter" handlerSize="1px" vertical="true"
				style="width: 100%; height: 100%;" borderStyle="border: 0;">
				<div size="100%" showCollapseButton="false" style="border-bottom:0;">
					<div class="nui-toolbar" style="border-bottom: 0; padding: 0px;">
					<form id="form1">
						<table style="width: 100%;">
							<tr>
								<td style="width: 100%;">
									<a class="nui-button" iconCls="icon-addnew" id="addBtn" onclick="add()">新增</a>
									<a class="nui-button" iconCls="icon-edit" id="editBtn" onclick="edit()">编辑</a>
									<a class="nui-button" iconCls="icon-remove" id="removeBtn" onclick="confirmRemove()">删除</a>
									<a class="nui-button" iconCls="icon-ok" id="remonitorBtn" onclick="remonitor()">开启监控</a>
									<a class="nui-button" iconCls="icon-ok" id="jqBtn" onclick="jq()">开启集群</a>
									<a class="nui-button" iconCls="icon-ok" id="restartBtn" onclick="restart()">启动任务</a>
									
								</td>
								<td style="white-space: nowrap;"><input  id="key" 
									class="nui-textbox" emptyText="请输入任务名称" style="width: 150px;"
									onenter="onKeyEnter" /> <a class="nui-button"
									iconCls="icon-search" onclick="search()"></a></td>
							</tr>
						</table>
						</form>
					</div>

					<div class="nui-fit">
						<div id="datagrid1" dataField="data" class="nui-datagrid" allowUnselect="false" sortMode="client"
							style="width: 100%; height: 100%;"
							url="<%= request.getContextPath() %>/schedule/list" onrowclick="clickRow">
							<div property="columns">
								<div type="indexcolumn" width="10" headerAlign="center">序号</div>
								<div field="dsrwzj" headerAlign="center" allowSort="true"
									visible="false"></div>
								<div field="rwzxzz" headerAlign="center" allowSort="true"
									visible="false"></div>
								<div field="rwbm" width="20" headerAlign="center" align="left"
									allowSort="true">任务编码</div>
								<div field="rwmc" width="20" headerAlign="center" align="left"
									allowSort="true">任务名称</div>
								<div field="rwqdsj" width="20" headerAlign="center" align="left"
									allowSort="true">执行额度</div>
								<div field="rwlm" width="30" headerAlign="center" align="left"
									allowSort="true">任务类名</div>
								<div field="sfjk" type="comboboxcolumn" width="10" align="left"
									headerAlign="center" allowSort="true" renderer="sfjkRenderer">
									是否监控
									<input property="editor" class="nui-dictcombobox" dictTypeId="ismonitor"/>
								</div>
								<div field="jq" type="comboboxcolumn" width="10" align="left"
									headerAlign="center" allowSort="true" renderer="jqRenderer">
									是否集群
									<input property="editor" class="nui-dictcombobox" dictTypeId="CK_CDZT"/>
								</div>
								<div field="rwzt" type="comboboxcolumn" width="10" align="left"
									headerAlign="center" allowSort="true" renderer="rwztRenderer">
									任务状态
									<input property="editor" class="nui-dictcombobox" dictTypeId="CK_CDZT"/>
								</div>							
								<div field="id" width="30" headerAlign="center"
									allowSort="true" visible="false">id</div>
							</div>
						</div>
					</div>
				</div>
				<div size="210px" minSize="210px" showCollapseButton="false" style="border-top:0;">
					<div id="panel1" class="nui-panel" title="定时任务详细信息"
						style="width: 100%; height: 100%;" showToolbar="false"
						showCollapseButton="false" showFooter="false" allowResize="false">
						<div id="editForm1">
							<table>
								<tr>
									<td colspan="4"><input name="dsrwzj" class="nui-hidden" />
									</td>
								</tr>
								<tr>
									<td style="width: 120px">任务编码</td>
									<td style="width: 120px"><input name="rwbm"
										class="nui-textbox" style="width: 100%;" readonly="readonly" /></td>
									<td style="width: 120px">任务名称</td>
									<td style="width: 120px"><input name="rwmc"
										style="width: 100%;" class="nui-textbox" readonly="readonly" /></td>
								</tr>
								<tr>
									<td>执行额度</td>
									<td><input id="rwqdsj" name="rwqdsj" style="width: 100%;"
										class="nui-textbox" readonly="readonly" /></td>
									<td>任务状态</td>
									<td><input name="rwzt" class="nui-dictcombobox"
										property="editor" dictTypeId="CK_CDZT" style="width: 100%;"
										showNullItem="true" readonly="readonly"/></td>
								</tr>
								<tr>
									<td>任务类名</td>
									<td colspan="3"><input name="rwlm" style="width: 100%;"
										class="nui-textbox" readonly="readonly" /></td>
								</tr>
								<tr>
									<td>备注</td>
									<td colspan="3"><input name="bz" class="nui-textarea"
										style="width: 100%;" readonly="readonly" /></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	</div>

	<script type="text/javascript">

    	$G.parse();
        var grid = $G.get("datagrid1");
        var form = $G.getForm("form1");
        grid.load();
        
        var db = new $G.DataBinding();
        db.bindForm("editForm1", grid);
        initBtnState();
        
		function initBtnState() {
			$G.get("removeBtn").disable();
	        $G.get("editBtn").disable();
	        $G.get("restartBtn").disable();
	        $G.get("remonitorBtn").disable();
	        $G.get("jqBtn").disable();
		}
        
        function clickRow(e){
            $G.get("restartBtn").enable();
            $G.get("remonitorBtn").enable();
        	if(e.row.rwzt=="0"){
        		$G.get("removeBtn").disable();
        		$G.get("editBtn").disable(); 
        		$G.get("jqBtn").disable(); 
        		$G.get("restartBtn").setText(message.schedule.stopTask);
        		$G.get("restartBtn").setIconCls("icon-no");//修改停止按钮图标     
        		   
        	}else{
        		$G.get("removeBtn").enable();
        		$G.get("editBtn").enable(); 
        		$G.get("jqBtn").enable(); 
        		$G.get("restartBtn").setText(message.schedule.startTask);
        		$G.get("restartBtn").setIconCls("icon-ok");//修改停止按钮图标
        		
        	}
        	
        	if(e.row.sfjk=="0"){//监控为否
        		$G.get("remonitorBtn").setText(message.schedule.closeMonitor);
        		$G.get("remonitorBtn").setIconCls("icon-no");//修改监控按钮图标
        	}else{
        		$G.get("remonitorBtn").setText(message.schedule.openMonitor);
        		$G.get("remonitorBtn").setIconCls("icon-ok");//修改监控按钮图标
        	} 
        	
        	if(e.row.jq=="0"){//集群
        		$G.get("jqBtn").setText("关闭集群");
        		$G.get("jqBtn").setIconCls("icon-no");//修改停止按钮图标  
        	}else{
        		$G.get("jqBtn").setText("开启集群");
        		$G.get("jqBtn").setIconCls("icon-ok");//修改停止按钮图标     
        	} 
        }
        
            
        function rwztRenderer(e) {
        	return $G.getDictText("CK_CDZT", e.row.rwzt);
        }
        
        function jqRenderer(e) {
        	return $G.getDictText("SFQY", e.row.jq);
        }
        
        function sfjkRenderer(e) {
        	return $G.getDictText("SFQY", e.row.sfjk);
        } 
        
        //删除
        function confirmRemove() {
            var row = grid.getSelected();
            if (row) {
                $G.confirm(message.common.deleteUnitConfirmMsg,"",function(action){
                    if (action == "cancel") {
                    	return;
                	}
   			   		$G.postByAjax({id:row.id},"schedule/delete");
   			     	initBtnState();
   			   		grid.reload();
                });
            } else {
            	$G.alert("请选中一条记录");
            }
        }
       
        function search() {
            var rwmc = $G.get("key").getValue();
            grid.load({rwmc:rwmc});
        } 
        
        
        //新增
        function add() {
        	var url = "/schedule/form";
            var bizParams = {action:"0"};
            $G.showmodaldialog(message.schedule.addTask, url, 440, 350, bizParams, function(action){
            	grid.reload();
		    });
        }
        
        //编辑
        function edit() {
            var row = grid.getSelected();
            if (row) {
            	var url = "/schedule/form";
            	var bizParams = {action:"1",data:row,pageType:"1"};
            	$G.showmodaldialog(message.schedule.editTask, url, 440, 350, bizParams, function(action){
            		initBtnState();
            		grid.reload();
		   		});
            } else {
            	$G.alert("请选中一条记录");
            }
        }
        
        
        
        function onKeyEnter(e) {
            search();
        }
        
        function restart(){
        	var row = grid.getSelected();
          		if(row.rwzt=="0"){//状态为启用
       				row.rwzt = "1";
       			}else{
       				row.rwzt = "0";
       			}
   			$G.postByAjax({rwzt:row.rwzt,id:row.id,rwlm:row.rwlm,rwqdsj:row.rwqdsj,rwbm:row.rwbm},"schedule/restartTask");
   			initBtnState();
   			grid.reload();
   			
        }
        
        function jq(){
        	var row = grid.getSelected();
        	if(row.jq=="0"){//状态为启用
    				row.jq = "1";
    			}else{
    				row.jq = "0";
    			}  
         		$.ajax({type:"post",url:"/schedule/jqTask", dataType:"json", data:"jq="+row.jq+"&id="+row.id,
     		       success:function(data){
     		    	   if(data.hckg=='close'){
     		    		   $G.alert("请先打开缓存开关")
     		    		  if(row.jq=="0"){//状态为启用
     		    				row.jq = "1";
     		    			}else{
     		    				row.jq = "0";
     		    			}
     		    		  initBtnState();
       		   			grid.reload(); 
     		    	   }else{
     		    		initBtnState();
     		   			grid.reload(); 
     		    	   }
     	}
     	  });
        	
        }
        
        function remonitor(){
        	var row = grid.getSelected();
   			if(row.bz == "null" || row.bz == null){
        		row.bz = "";
        	}
   			if(row.sfjk=="0"){//监控为是
   				row.sfjk = "1";
   			}else{
   				row.sfjk = "0";
   			}
   			$G.postByAjax({sfjk:row.sfjk,id:row.id},"schedule/updatejk");
   			initBtnState();
   			grid.reload();
   			
        }

    </script>
</body>
</html>