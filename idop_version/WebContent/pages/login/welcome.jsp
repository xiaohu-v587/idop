<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<html>
	<head>
	<link href="<%= request.getContextPath() %>/resource/css/welcome.css" rel="stylesheet" type="text/css" /> 
	</head>
  	<body>
  		<div class="nui-fit">
  			<div class="wdiv1">
	  			<ul>
	  				<li>
	  					<div>
	  						<a href="#">
	  							<img src="<%=request.getContextPath()%>/resource/images/home/home-icon2.png" alt="" border="0"/>
	  						</a>
	  					</div>
	  					<p>
	  						<a href="#" class="button">待处理任务</a>
	  					</p>
	  				</li>
	  				<li>
	  					<div>
	  						<a href="#">
	  							<img src="<%=request.getContextPath()%>/resource/images/home/home-icon3.png" alt="" border="0"/>
	  						</a>
	  					</div>
	  					<p>
	  						<a href="#" class="button">你好</a>
	  					</p>
	  				</li>
	  			</ul>
	  		</div>
	  		<div class="wdiv2">
	  			<div id="tabs2" class="nui-tabs" activeIndex="0" style="width:830px;height: 480px;">
				    <div title="公告栏" iconCls="icon-date">
				    <div id="datagrid1" class="nui-datagrid" style="width:100%;height:100%;" 
				   url="<%=root%>/notice/welcome" > 
		            <div property="columns">
		              <div type="indexcolumn" width="10"></div>
		              <div field="id" id="id"  visible="false"  width="50"  ></div>
		              <div field="bt"  width="250"  align="left" ></div>
		              <div field="fbsj"  width="55" dateFormat="yyyy-MM-dd HH:mm:ss" align="right" ></div> 
		          </div>
				     </div> 
				  </div>
				  
				    <div title="代办任务" iconCls="icon-date" >
				        2
				    </div>
				</div>
	  		</div>
  		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid1");
	grid.load();
	 $(function(){
         //用jQuery获取table中td值
         $("#datagrid1").click(function(){
            	 var row=grid.getSelected().id;
                 $.ajax({
    	  			  type:"post",
    	  			  url:"/notice/wetofrom",
    	  			  dataType:"json",
    	  			  data:"td="+row,
    	  			  success:function(data){
    	  				var url = "/notice/form";
    	  				var bizParams = {pageType:"edit",data:data,ly:"welcome"};
    	  		        $G.showmodaldialog("查看公告", url, 720, 550, bizParams,function(action){
    	  		        	
    	  			    	 })
    	  			    	window.location.reload();
    	  			  }});
        	 })
        
         }); 
         
	  
	  
</script>

<style type="text/css">
.mini-grid-cell, .mini-grid-headerCell,
.mini-grid-filterCell, .mini-grid-summaryCell
{
    overflow: hidden;
    padding:0px;
    border:0; 
    border-bottom:#d2d2d2 1px solid;
    cursor:default;
    text-align:left;
    overflow:hidden;
    _text-overflow:ellipsis;
    padding-left:4px;
    padding-right:4px;
}

.mini-grid-headerCell, .mini-grid-topRightCell
{
     display: none;
}

</style>