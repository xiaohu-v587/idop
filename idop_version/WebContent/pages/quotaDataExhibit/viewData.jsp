<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/js/jquery.min.js"></script>
   		<script src="<%=request.getContextPath()%>/resource/js/panorama.js" type="text/javascript"></script>
   		
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
			<div class="nui-fit">
					<div id="form1" class="mini-form" align="left" style="width: 100%;height:10%;">
					<div  class="search_box" width="100%">
						
						<ul>
							<li class="search_li" align="right">一级明细名称:</li>
							<li align="left">
								<input class="mini-combobox" width="200px"  nullItemText="请选择..." emptyText="请选择..."  url="" 
           	 				id="mark_dimension" name="mark_dimension"  valueField="val" textField="remark" required="true"></input>
							</li>
						</ul>
						<ul>
							<li class="search_li" align="right"><a class="nui-button"  onClick="search()" style="margin-right:30px;">查询</a></li>
							<li class="search_li" align="right"><a class="nui-button"  onClick="download()" style="margin-right:30px;">导出</a></li>
						</ul>
					</div>
				</div>
				
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/quotaDataExhibit/getZbFirstDetail" style="width: 100%;height: 90%;" 
		    		multiSelect="false" allowAlternating="true">
			        <div property="columns">            
			            <div field="create_time" width="150" headerAlign="center" allowSort="true"  align="center">时间</div>                
			            <div field="lvl_4_branch_name" width="150" allowSort="true" headerAlign="center" align="center">机构名称</div>
			            <div field="indicator_code" width="35%"  allowSort="true" headerAlign="center" align="center" renderer="onValueRender">一级明细名称</div>            
<!-- 			           <div field="field" width="150" allowSort="true" headerAlign="center" align="center">明细</div> 
 -->			            <div field="indicator_value" width="100" allowSort="true" headerAlign="center" align="center">值</div> 
			            <!--<div field="warning_status" width="80" allowSort="true" headerAlign="center" align="center" renderer="onWarningStatusRender">预警状态</div>
			            <div field="warning_level" width="80" allowSort="true" headerAlign="center" align="center" renderer="onWarningLevelRender">预警等级</div>
			            <div field="last_check_stat" width="80" allowSort="true" headerAlign="center" align="center" renderer="onLastCheckStatRender">核查结果</div> -->
			        </div>
				</div>
			</div>
			 <div>
	                 <ul>
	                	<li style="margin-right:20px;height:10px;"><a class="down_box" href="javascript:download()" style='text-decoration:none;' >下载数据</a></li>
	                </ul> 
		        </div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var datas=null;
	var grid =$G.get("datagrid1");
	var flag=1;
	
	function setData(data){
		datas = data;
		var url = "<%=root%>/quotaDataExhibit/getZbFirstDetailList?indexname="+datas.indexname;
		$G.getbyName("mark_dimension").setUrl(url);
		mini.get("mark_dimension").select(0);
		search();
	}	
	function onValueRender(e){
		return $G.getDictText("first_detail",e.value);
	}
	function search(){
		var mark_dimension = $G.getbyName("mark_dimension").getValue();
		var columns  =  grid.getColumns();
		if(mark_dimension==null||mark_dimension=="null"||mark_dimension==""){
			$G.alert("请先选择一级明细名称");
			return;
		}else{
			if(flag==1){
				 $.ajax({
	                 url: "<%=root%>/quotaDataExhibit/queryField",	//查询该指标下面需要转码的field
	  		         type: 'post',
	                  data:  {mark_dimension:mark_dimension},
	                  cache: false,
	                  success: function (text) {
	                  	var rlist = mini.decode(text);
	  		            for(var i=0;i<rlist.length;i++){	  		           
	  		            	columns.push({header:rlist[i].column_desc,field:rlist[i].column_name,headerAlign:"center",align:"center",width:"100px"})
	  		            	grid.setColumns(columns);
	  		             }
	  		            flag=2;
	                  },
	                  error: function (jqXHR, textStatus, errorThrown) {
	                      alert(jqXHR.responseText);
	                      CloseWindow();
	                  }
	              });
			}			
			
			datas.mark_dimension = mark_dimension;
			
			grid.load(datas);
		}
	}
	function download(){
		var mark_dimension = $G.getbyName("mark_dimension").getValue();
		if(mark_dimension==null||mark_dimension=="null"||mark_dimension==""){
			$G.alert("请先选择一级明细名称");
			return;
		}else{
			datas.code = mark_dimension;
			datas.pageSize = "999999";
			callHeadAndTextToDataByDataGrid(grid,datas);
			
			window.location="<%=root%>/quotaDataExhibit/downloadDetails?"+connUrlByJson(datas);
		}
	}
</script>

