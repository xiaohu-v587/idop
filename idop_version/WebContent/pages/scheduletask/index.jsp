<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="定时任务监控" style="width:100%;height:80px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table>
	               		<tr>
		                   	<th align="right">任务名称：</th>
							<td align="left">
								<input id="rwmc" name="rwmc" class="mini-textbox" style="width:165px;"/>
							</td>
		                   <th align="right">任务启动时间：</th>
							<td align="left">
								<input   name="rwqdsj" class="nui-datepicker" allowInput="false" emptyText="请选择..."  style="width:165px;" onvaluechanged="retchange"/>
							</td>
							<th align="right">任务结束时间：</th>
							<td align="left">
								<input   name="rwjssj" class="nui-datepicker" allowInput="false" emptyText="请选择..."  style="width:165px;" onvaluechanged="retchange"/>
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
						<a class="nui-button" iconCls="icon-ok" id="restartBtn" onclick="restart()">重新运行</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/scheduletask/list" style="width: 100%;height: 100%;"
		    		multiSelect="true" allowUnselect="false" autoEscape="false" onselectionchanged="onSelectionChanged">
			        <div property="columns">
			         <div type="checkcolumn"  name="checkCloumn"></div>
			           <div type="indexcolumn"   headerAlign="center" align="right">序号</div>   
			            <div field="rwbm"  headerAlign="center" allowSort="true"  align="left">任务编码</div>                
			            <div field="rwmc"  allowSort="true" headerAlign="center" align="left">任务名称</div>                    
			            <div field="rwkssj" dateFormat="yyyy-MM-dd HH:mm:ss" allowSort="true"  headerAlign="center" align="right">任务启动时间</div> 
			            <div field="rwjssj" dateFormat="yyyy-MM-dd HH:mm:ss"  allowSort="true" headerAlign="center" align="right" >任务结束时间</div>
			            <div field="rwzt" type="comboboxcolumn"  align="left"
									headerAlign="center" allowSort="true" renderer="zxztRenderer">
									任务状态
							<input property="editor" class="nui-dictcombobox" dictTypeId="DSRWZT"/>
						</div>
			        	<div field="bz"   allowSort="true" headerAlign="center" align="left" >备注</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid1");
	grid.load();
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	 $G.get("restartBtn").disable();

	function sfjkRenderer(e) {
        return $G.getDictText("SFQY", e.row.sfjk);
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
	
	function zxztRenderer(e) {
		var zt = $G.getDictText("DSRWZT",e.row.rwzt);
		return zt;
    } 
	
	function onSelectionChanged(){
		var rows = grid.getSelecteds();
		if(rows.length>1){
			$G.get("restartBtn").disable();
		}else{
			$G.get("restartBtn").enable();
		}
	}
	
	function restart(){
		var row = grid.getSelected();
		$.ajax({type:"post",url:"<%=root%>/scheduletask/restart", dataType:"json", data:"rwbm="+row.rwbm,
		       success:function(data){
		    	   var yxsj=data.yxsj;
		    	   $G.alert(yxsj);
	}
	  });
		
	}
	
</script>

