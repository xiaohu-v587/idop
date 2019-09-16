<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@include file="/common/nuires.jsp" %>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择人员</title>
</head>
<body>
<div class="mini-fit" style="width:100%;height:100%;">
		<div class="mini-splitter" style="width:100%;height:92%;">
		    <div size="70%" showCollapseButton="true" style="padding:5px;overflow:scroll">
		    	<div name="multiEmployee">
    				<form id="form1">
    					<input id="pageType" name="pageType" class="mini-hidden" />
    					<input id="id" name="id"  class="mini-hidden" value="${id}"/>
    					<input id="uuid" name="uuid" class="mini-hidden" value="${uuid}"/>
    					<input id="checknum" name="checknum"  class="mini-hidden" value="0"/>
    					<input id="adduser_flag" name="adduser_flag"  class="mini-hidden" value="0"/>
    					<input id="checknum1" name="checknum1"  class="mini-hidden" value="${checknum1}"/>
    					<input id="checkcontent" name="checkcontent"  class="mini-hidden" />
    					<input id="level_flag" name="level_flag"  class="mini-hidden" />
    					<input id="group_userno" name="group_userno"  class="mini-hidden" />
			    		<table id="detailTable">
			    			<tr>
			    				<td>人员选择方式：</td>
			    				<td>
			    					<input id="sendtype" name="sendtype" class="mini-checkboxlist" repeatItems="2" repeatLayout="table" multiSelect="false"
	    								textField="text" valueField="id" data="[{text:'单人/多人',id:'0'},{text:'群组',id:'1'}]" onvaluechanged="findObject" required="true" />
			    				</td>
			    				<td colspan="2">
			    					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    					<input id="addgroup" name="addgroup" class="mini-button" text="新增群组"  onclick="addGroup" />
			    					<input id="delgroup" name="delgroup" class="mini-button" text="删除群组"  onclick="delGroup" />
			    				</td>
			    			</tr>
			    			<tr>
			    				<td>筛选条件：</td>
			    				<td colspan="2">
			    					<input id="sendobject" name="sendobject" class="mini-checkboxlist" style="width:300px" repeatItems="2" repeatLayout="table" multiSelect="false"
	    								textField="text" valueField="id" data="[{text:'角色',id:'2'},{text:'机构+角色',id:'3'},{text:'机构',id:'1'},{text:'人员',id:'0'}]" onvaluechanged="findObject"  required="true"/>	    							
			    				</td>
			    			</tr>
			    			<tr>
			    				<td></td>
			    				<td colspan="2">
			    				<input id="role_org" name="role_org" class="mini-checkboxlist"  repeatItems="2" repeatLayout="table" multiSelect="false"
	    								textField="text" valueField="id" data="[{text:'角色',id:'2'},{text:'机构',id:'1'}]" onvaluechanged="findObject" style="display: none;color:red" />
			    				</td>	
			    				
			    			</tr>
			    			<!-- <tr>
			    				<td>是否生产独立子任务：</td>
			    				<td>
			    					<input id="work_type" name="work_type" class="mini-checkboxlist" style="width:300px" repeatItems="2" repeatLayout="table" 
	    								textField="text" valueField="id" data="[{id:'1'}]"  />	    							
			    				</td>
			    			</tr> -->
			    			<tr>
			    				<td>已选择人员：</td>
			    				<td colspan="2">
			    					<input id="recipient" name="recipient" class="mini-hidden" style="width:300px;height:200px;" allowInput="false" required="true"/>
			    					<input id="recipientno" name="recipientno" class="mini-hidden"/>
			    				</td>
			    			</tr>
 						</table>
 							<div class="mini-toolbar" style="text-align: center;" borderStyle="border-left:0;border-bottom:0;border-right:0;">
 								<a class="mini-button" id="butquery" onclick="remove" iconCls="icon-remove" style="margin-left: 85%;">移除</a>
	 						</div>
	 						<div id="grid" class="mini-datagrid" style="width:100%;height:330px;" 
	                              			 url="" dataField="datas" idField="ID" valueField="ID" showPager="false"
								showPageInfo = "true" multiSelect="true" pageSize="10" sizeList="[10,20,50,100]"
								>
								    <div property="columns">
								        <div type="indexcolumn"></div>
								        <div type="checkcolumn"></div> 
								        <div field="SHOWNAME" width="120" headerAlign="center" align="center" allowSort="true">员工姓名</div> 
								        <div field="SHOWNUM" width="120" headerAlign="center" align="center" allowSort="true">员工号</div>   
								       <!--  <div field="PNAME" width="120" headerAlign="center" align="center" allowSort="true">机构名称</div>    -->
								    </div>
							</div>
				    </form>
		     	</div>
		    </div>

			<div size="50%" showCollapseButton="true">
				<div class="mini-fit">
					<form id="form2" style="width: 100%; height: 90%; display: none">
						<td style="width: 100%; height: 10%;">
							<div style="white-space: nowrap; width: 95%;">
								<input id="userid" name="userid" class="mini-textbox" emptyText="请输入员工号"
									style="margin-top: 2%; width: 40%;" />
									<input id="username" name="username" class="mini-textbox" emptyText="请输入姓名"
									style="margin-top: 2%; width: 40%;" /> <a class="mini-button"
									iconCls="icon-search" onclick="search()" style="margin-top: 2%">查询</a>
							</div>
						</td>
					</form>
					<div id="treegrid1" dataField="datas" class="mini-treegrid" url=""
						showTreeIcon="true" style="width: 100%; height: 100%;"
						treeColumn="showName" idField="SHOWNUM" parentField="UPID"
						resultAsTree="false" checkRecursive="true" autoCheckParent="false"
						allowResize="true" dataField="datas"
						showCheckBox="true">
						<div property="columns">
							<div headerAlign="center" type="indexcolumn">序号</div>
							<div field="SHOWNUM" visible="false">机构号</div>
							<div field="SHOWNAME" name="showName" width="160"
								headerAlign="left">对象名称</div>
							<div field="SHOWNUM" width="80" headerAlign="left">对象</div>
						</div>
					</div>
				</div>

				<div class="mini-toolbar"
					style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
					borderStyle="border-left:0;border-bottom:0;border-right:0;">
					<a class="mini-button" id="butquery" onclick="chooseAll"
						iconCls="icon-collapse" style="width: 60px; margin-right: 20px;">全选</a>
					<a class="mini-button" id="butquery" onclick="choose"
						iconCls="icon-ok" style="width: 60px; margin-right: 20px;">确定</a>
					<a class="mini-button" id="butcal" onclick="reset"
						iconCls="icon-close" style="width: 60px;">重置</a>
				</div>
			</div>

		</div>
		<div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
           	borderStyle="border-left:0;border-bottom:0;border-right:0;">
           		<a class="mini-button" id="butquery" onclick="save"  iconCls="icon-ok" style="width:60px;margin-right:20px;">确定</a>       
           		<a class="mini-button" id="butcal" onclick="onCancel" iconCls="icon-close" style="width:60px;">关闭</a>       
       	</div> 
</div>
</body>
</html>

<script>
mini.parse();
var treeGrid = mini.get("treegrid1");
//treeGrid.setShowCheckBox(true);
  var grid = mini.get("grid");
 var form = new mini.Form("form1");
 var form2 = new mini.Form("form2");
 var orgno = [];
 var roleno = [];
 var orgno1 = [];
 var roleno1 = [];
 
</script>