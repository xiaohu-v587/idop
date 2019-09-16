<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
   * 列表展示页配置
   *
   * Created on 
   * @author  
   * @reviewer 
-->
<head>
    <title>列表展示页配置</title>
</head>
<style type="text/css">
	table{width:96%;align:"center"}
	img {height:15px}
	 html, body{
	        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
	    }
</style>
<body>
	<!-- 标识页面是查看(query)、修改(edit)、新增(add) -->
	<div class="mini-fit">
		<input name="pageType" class="mini-hidden" />
		<form id="dataform1" method="post">
			<input class="mini-hidden" name="djbid" />
			<input class="mini-hidden" name="findPrefix" value="node_find_sfmhpp_" />
			<input class="mini-hidden" name="listPrefix" value="node_list_xsxs_" />
			<input class="mini-hidden" name="cxtjNum" />
			<input class="mini-hidden" name="jgpzNum" />
			<fieldset style="width: 96%;">
				<legend>公共属性</legend>
				<table style="table-layout: fixed;  height: 100%" align="center">
					<colgroup>
						<col width="15%" />
						<col width="20%" />
						<col width="15%" />
						<col width="20%" />
						<col width="20%" />
						<col width="20%" />
					</colgroup>
					<tr>
						<td class="form_label">登记簿名称:</td>
						<td ><input class="mini-textbox" name="djbmc" readonly="true" /></td>
						<td ></td>
						<td ></td>
						<td ></td>
						<td ></td>
					</tr>
				</table>
			</fieldset>
			<fieldset style="width: 96%;" id="lcpz">
				<div>查询条件属性&nbsp;&nbsp;&nbsp;&nbsp; 
					<a href="javascript:createFindNode('parentpoint','find')"><img border="0" src="../resource/image/add.jpg" /></a> &nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:reloadPz()" id="reloadPz" title="是否重新配置"><img border="0" src="../resource/image/reload.png" /></a> &nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="padding: 0px; margin: 0px;" >
					<table style="table-layout: fixed; width: 100%; height: 100%" align="center">
						<colgroup>
							<col width="15%" />
							<col width="15%" />
							<col width="70%" />
						</colgroup>
						<tr>
							<td colspan="1"><div id="sffhbm" name="sffhbm" class="mini-checkbox" text="是否按机构查询"></div></td>
							<td colspan="1"><div id="startTime" name="startTime" class="mini-checkbox" text="开始时间"></div></td>
							<td colspan="1"><div id="endTime" name="endTime" class="mini-checkbox" text="结束时间"></div></td>
						</tr>
					</table>
				</div>
				<div style="padding: 0px; margin: 0px;" id="parentpoint"></div>
			</fieldset>
			<fieldset style="width: 96%;" id="lcpz">
				<legend>结果列表配置</legend>
				<div style="padding: 0px; margin: 0px;;width: 100%;height:100%" id="parentpoint1"></div>
			</fieldset>
		</form>

	</div>
	<div class="mini-toolbar" id="toolbardiv"
		style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a class="mini-button" onclick="onOk()" style="margin-right: 20px;" > 确定 </a>
		<a class="mini-button" onclick="onCancel"  > 取消 </a>
	</div>
	<script type="text/javascript">
		mini.parse();
		$(function() {
			$("#reloadPz").hide();
		});
		 var form =  new mini.Form("dataform1");
		//json方式保存数据
		function saveJson() {
			var urlStr = "<%=root%>/djb/saveLbzspz";
			var o = form.getData(); 
		  	var json = mini.encode([o]);
			$.ajax({
					url: urlStr,
					type: 'post',
			        data: {data:json},
			        cache: false,
			        success: function (text) {
			            mini.alert("保存成功!");
			            CloseWindow("save");
			         }
			 });
		}
		var addCfg = new Array();
		var map = {};
		//获取父页面传递来的json数据
		function setData(data) {
			//跨页面传递的数据对象，克隆后才可以安全使用
			var infos = mini.clone(data);
			//保存list页面传递过来的页面类型：add表示新增、edit表示编辑
			mini.getbyName("pageType").setValue(infos.pageType);
			var record = infos.record;
			if(record){
				mini.getbyName("djbmc").setValue(record.djbmc);
				mini.getbyName("djbid").setValue(record.id);
			}
			//获取配置的字段数据
			var urlStr="<%=root%>/djb/findLbzspz";
			$.ajax({
	            url: urlStr,
	            data:{djbid:record.id},
	            cache: false,
	            success: function (data) {
	            	if((data.cxjg == 0 &&data.cxtjtotal==0)||data.jgpztotal==0){
						mini.confirm("未配置，是否开始配置？","提示",function(action){
							if(action == "ok"){
								loadYSData(record.id);
							}
						});
					}else{
						//是否按机构查询
						if(data.cxjg == 1){
							mini.get("sffhbm").setValue("1");
						}
						if(data.cxsjstart == 1){
							mini.get("startTime").setValue("1");
						}
						if(data.cxsjend == 1){
							mini.get("endTime").setValue("1");
						}
						var sfrq_arr = new Array() ;
						if(data.jgpztotal>0){
							dreawNodes1('parentpoint1',data.jgpzdata,data.jgpztotal,listNodes1);
			            	mini.parse();
						}
						map["jgpz"]=data.jgpzdata;
						 var countMax = 0;
						if(data.cxtjtotal>0){
							for(var i = 0;i<data.jgpztotal-countMax;i++){addCfg.push(i);};
							dreawNodes2('parentpoint',data.cxtjdata,data.cxtjtotal,data.jgpzdata,findNodes);
						} 
						$("#reloadPz").show();
					}
					mini.parse();
	       		}
	        });
			
		}
		
		var findNodesMap = {};//用于记录生成的下拉列表Name用于重复值校验
		//findNodesMap["sfrq_zdm"]="name";
		
		function reloadPz(){
			$("#parentpoint").empty();
			$("#parentpoint1").empty();
			mini.get("sffhbm").setValue("0");
			var djbid = mini.getbyName("djbid").getValue();
			findNodesMap = {};//清空下来菜单集合参数
			addCfg.splice(0,addCfg.length);//清空计数数组 
			loadYSData(djbid);
			
		}
		
		//当没有配置结果页面时，会进入此方法，进行页面配置
		function loadYSData(id){
			
			$.ajax({
	            url: "<%=root%>/djb/findAll",
	            data:{djbid:id},
	            cache: false,
	            success: function (data) {
	            	dreawNodes('parentpoint1',data,listNodes);
	            	mini.parse();
					map["jgpz"]=data.data;
					for(var i = 0;i<data.total;i++){addCfg.push(i)};
					//mini.getbyName('sfrq_zdm').setData(data.data);
					$("#reloadPz").hide();
	            }
			});
		}

		//确定保存或更新
		function onOk(e) {
			saveJson();
		}

		//取消
	    function onCancel(e) {
		     CloseWindow("cancel");
		}

		function CloseWindow(action) {        
		    if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
		    else window.close();            
		}
		
		function validationFind(e){
			if(e){
				for(key in findNodesMap){  
					var o = mini.getbyName(key);
					if(o.getValue()==e.value&&e.sender.name!=key){
						e.isValid = false;
               			e.errorText = "选中值已存在列表中";
               			return;
					}  
				} 
			}
		}
		function onFindNodeChange(e){
			if(isRedo){
			 	mini.alert("不允许重复数据！");
			}
			map[e.name]=e.value;
		}
		
		var node=(function(){  
	        //赋初值  
	        var count=0;  
	        //外部调用时形成闭包  
	        return function(){            
	            return ++count;  
	        }  
    	})()
    	
		var createFindNode = function(parintId){
			if(addCfg.length==0){return;}//限制生成元素个数
			var n = node();
			$("#"+parintId).append(dreawNewFindNodes(map["jgpz"],n));
			mini.parse();
		}
		
		//点击新增按钮时，拼接
		function dreawNewFindNodes(data,i){
			map["nodeDiv"+i]={id:"node_find_bhqz_"+i,idx:addCfg.shift()};
			findNodesMap["node_find_bhqz_"+i]="name";
			mini.getbyName("cxtjNum").setValue(i);
			return $("<div/>").attr({id:'nodeDiv'+i}).append(
				$("<table/>").append(  
					$("<tr/>").append($("<td/>").append("其他查询条件"+i+"：").attr({width:"15%"}))
			    	.append($("<td/>").append($('<input/>').attr({name:"node_find_bhqz_"+i,textField:"yshy",valueField:"ysm",data:mini.encode(data),onvalidation:'validationFind'}).addClass("mini-combobox")).attr({width:"25%"}))
					.append($("<td/>").append($('<div>').attr({id:"node_find_sfmhpp_"+i,name:"node_find_sfmhpp_"+i,text:'是否模糊匹配'}).addClass("mini-checkbox")).attr({width:'20%'}))
					.append($("<td/>").append($('<a/>').attr({id:"delnode_"+i,href:"javascript:DeleteElementDiv('nodeDiv"+i+"')"}).append("<img border='0' src='../resource/image/delete.gif' />")).attr({width:"40%"}))
				)
			);	
		}
		
		//编辑时回显
		function findNodes(data,i,data1){
			node();
			map["nodeDiv"+i]={id:"node_find_bhqz_"+i,idx:addCfg.shift()};
			findNodesMap["node_find_bhqz_"+(i+1)]="name";
			mini.getbyName("cxtjNum").setValue((i+1));
			return $("<div/>").append(
				$("<table/>").append( 
					$("<tr/>").attr({}).append($("<td/>").append("其他查询条件"+(i+1)+"：").attr({width:"15%"}))
			    	.append($("<td/>").append($('<input/>').attr({name:"node_find_bhqz_"+(i+1),textField:"yshy",valueField:"ysm",value:data[i].bhqz,data:mini.encode(data1),onvalidation:'validationFind',onvaluechanged:'validationFind'}).addClass("mini-combobox")).attr({width:"25%"}))
					.append($("<td/>").append($('<div>').attr({id:"node_find_bhqz_"+i,name:"node_find_sfmhpp_"+(i+1),text:'是否模糊匹配',value:data[i].sfmhpp}).addClass("mini-checkbox")).attr({width:'20%'}))
					.append($("<td/>").append($('<a/>').attr({id:"delnode_"+i,href:"javascript:DeleteElementDiv('nodeDiv"+i+"')"}).append("<img border='0' src='../resource/image/delete.gif' />")).attr({width:"40%"}))
				)
			).attr({id:'nodeDiv'+i});
		}	
		
		
		//新增
		function listNodes(data,i){
			mini.getbyName("jgpzNum").setValue(i);
			return $("<div/>").append(
				$("<table/>").append(
					$("<tr/>").append($("<td/>").append("列名 : ").attr({width:"5%"})).append($("<td/>").append(data.data[i].yshy).attr({width:"15%"}))
					.append($("<td/>").append($('<div/>').attr({name:"node_list_sfxs_"+i,text:'是否显示',value:function(){if(data.data[i].sfxs){return data.data[i].sfxs}else{return '1'}}}).addClass("mini-checkbox"),
					    $('<input/>').attr({name:"node_list_zdm_"+i,value:data.data[i].ysm}).addClass("mini-hidden")
					 ).attr({width:"15%"}))
					.append($("<td/>").append("显示顺序 : ").attr({width:"10%"}))
		   			.append($("<td/>").append($('<input/>').attr({name:"node_list_xsxs_"+i,value:function(){if(data.data[i].xsxs){return data.data[i].xsxs;}else{return (i+1);}},vtype:"int"}).addClass("mini-textbox")).attr({width:"20%"}))
					.append($("<td/>").append("显示宽度 : ").attr({width:"10%"}))
					.append($("<td/>").append($('<input/>').attr({name:"node_list_kd_"+i,value:getWidth(data.data[i].kd,data.data[i].cdxz,data.total),vtype:"int"}).addClass("mini-textbox"),"%").attr({width:"20%"}))
				)
			);
		}	
		//编辑
		function listNodes1(data,i,total){
			mini.getbyName("jgpzNum").setValue(i);
			return $("<div/>").append(
				$("<table/>").append(
					$("<tr/>").append($("<td/>").append("列名 : ").attr({width:"5%"})).append($("<td/>").append(data[i].yshy).attr({width:"15%"}))
					.append($("<td/>").append($('<div/>').attr({name:"node_list_sfxs_"+i,text:'是否显示',value:function(){if(data[i].sfxs){return data[i].sfxs}else{return '1'}}}).addClass("mini-checkbox"),
					    $('<input/>').attr({name:"node_list_zdm_"+i,value:data[i].ysm}).addClass("mini-hidden")
					 ).attr({width:"15%"}))
					.append($("<td/>").append("显示顺序 : ").attr({width:"10%"}))
		   			.append($("<td/>").append($('<input/>').attr({name:"node_list_xsxs_"+i,value:function(){if(data[i].xsxs){return data[i].xsxs;}else{return (i+1);}}}).addClass("mini-textbox")).attr({width:"20%"}))
					.append($("<td/>").append("显示宽度 : ").attr({width:"10%"}))
					.append($("<td/>").append($('<input/>').attr({name:"node_list_kd_"+i,value:getWidth(data[i].kd,data[i].cdxz,total)}).addClass("mini-textbox"),"%").attr({width:"20%"}))
				)
			);
		}	
		
		
		//结果页面配置(新增)
		function dreawNodes(point,data,callback){
			if(data){
				if(data.total>0){
					for (var i = 0; i < data.total; i++) {
						$("#"+point).append(callback(data,i));
					}
				}
			}
		}
		
		//结果页面配置（编辑）
		function dreawNodes1(point,data,total,callback){
			if(data){
				if(total>0){
					for (var i = 0; i < total; i++) {
						$("#"+point).append(callback(data,i,total));
					}
				}
			}
		}
		
		//结果页面配置（查询条件反显,编辑）
		function dreawNodes2(point,data,total,data1,callback){
			if(data){
				if(total>0){
					for (var i = 0; i < total; i++) {
						$("#"+point).append(callback(data,i,data1));
					}
				}
			}
		}
		
		
		function getWidth(kd,cdxz,total){
			if(kd){
				return kd;
			}else{
				return countWidth(cdxz,total);
			}
		}
		
		function countWidth(cdxz,total){
			if(cdxz){
				return Math.round((cdxz>0&&cdxz<=40)?10:(cdxz>40&&cdxz<=100?20:cdxz>100?40:10));
			}else{
				return Math.round(100/total);
			}
		}
	
	//动态删除div
	function DeleteElementDiv(id){
		var my = document.getElementById(id);
		delete findNodesMap[id];
		if (my != null){
			my.parentNode.removeChild(my);
		}
		mini.parse();
	}
	</script>
</body>
</html>
