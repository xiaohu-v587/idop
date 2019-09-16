<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
   * 登记簿列表
   *
   * Created on 
   * @author  
   * @reviewer 
-->
<head>
    <title>登记簿列表</title>
</head>
 <style type ="text/css">
 	html, body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
</style>
<body>
<div class="mini-splitter" style="width:100%;height:100%;" vertical="true" borderStyle="border: 0;">
	<div size="100px;" showCollapseButton="true" style="border: 0;">
		<div class="mini-panel" title="查询条件" style="width: 100%;height: 100%;">
			<form id="form1" >
				<table id="table1">
	          		<tr>
		          		<td id="jgbh" style="display:none;width:90px;">机构:</td>
						<td id="jgbh1" style="display:none;width:200px;" colspan="3"><input id="jgcxtj" name="jgcxtj" style="width:300px;"
							class="mini-treeselect" url="<%=root%>/org/getListByUser"
							dataField="datas" textField="orgname" valueField="orgnum"
							parentField="upid" valueFromSelect="true" multiSelect="false"
							expandOnLoad="0" emptyText="机构" allowInput="false"
							showClose="true" oncloseclick="onCloseClick" 
							showRadioButton="false" showFolderCheckBox="true"
							popupWidth="286" popupHeight="470" popupMaxHeight="600" />
						</td>
		          		<td class="form_label" id="start1" style="display:none;">开始时间:</td>
            			<td id="start2" style="display:none;">
        					<input id="startTime" name="startTime" required="true"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="width:96%"/>
             			</td>
               			 <td class="form_label" id="end1" style="display:none;">结束时间:</td>
                	 	 <td colspan="1" id="end2" style="display:none;">
        					<input id="endTime" name="endTime" required="true"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="width:96%"/>
             			</td>
	          		</tr>
	          	</table>
	        </form>
		</div>
	</div>
	<div showCollapseButton="false" style="border: 0;">
		<div class="mini-toolbar" style="border-bottom: 0; padding: 0px;">
			<table style="width: 100%;">
				<tr>
		        	<td style="width: 100%;">
		        		<a class="mini-button"  onclick="search()">查询</a>
			            &nbsp;&nbsp;
			            <a class="mini-button"  onclick="reset()">重置</a>
			            &nbsp;&nbsp;
			             <a class="mini-button"  onclick="detail()" >详情</a>
			            &nbsp;&nbsp;
			            <a id="approve" class="mini-button"  onclick="approve()" >复核</a>
			           <!-- &nbsp;&nbsp;
			            <a class="mini-button"  onclick="Ok()">批量通过</a>-->
		            </td>
				</tr>
			</table>
		</div>
		<div id ="gridpanal" class="mini-fit">
		    <div id="datagrid1"  dataField="data" class="mini-datagrid" style="width:100%;height:95%;" sortMode="client" allowUnselect="false"
				url="<%=root %>/djbapprove/queryList?djbid=<%=request.getAttribute("djbid")%>&flag=0&status=<%=request.getAttribute("status")%>"  multiSelect="true"  allowResize="true"
				autoEscape="false"   showEmptyText="true" emptyText="没有对应的记录"  >
		    </div>
		</div>
	</div>
</div>
<script type="text/javascript">
    mini.parse();	//将页面的标签转换为可miniui可处理的对象
	dreawPanal(function(){
	});
    var form = new mini.Form("form1");
    var grid = mini.get("datagrid1");
    var djbid = "${djbid}";
    var status = "${status}";
    if(status=="2"){
    	document.getElementById("approve").style.display="none";
    }else{
    	document.getElementById("approve").style.display="";
    }
    var arrs = null;


 	$(document).ready(function(){
 		getHeader();
   		grid.load();
   	});

     function getHeader(){
     	//动态获取表格的列，设置表格列头后，再加载表格数据
     	djbid = "${djbid}";
   		grid.on("beforeload", function(){
	  		//获取动态的列
	  		$.ajax({
	            url: "<%=root %>/djbapprove/queryHeaders",
	            data:{djbid:djbid},
	            cache: false,
	            success: function (text) {
		            	grid.set({
							columns:text
						});
	            }
	     	});
	  	});
     }
     
     function statusRender(e){
    	 var status = e.value;
    	 var str = "待复核";
    	 if(status == 2){
    		 str = "已通过";
    	 }
    	 return str;
     }
     
     function dreawPanal(){
    	 djbid = "${djbid}";
    	 $.ajax({
	            url: "<%=root %>/djbapprove/queryCxtj",
	            data:{djbid:djbid},
	            cache: false,
	            success: function (text) {
		            arrs = text.cxtj;
					dreawFind(arrs);
	            }
	     	});
		}
     
  // 机构下拉框清空
     function onCloseClick(e) {
         var obj = e.sender;
         obj.setText("");
         obj.setValue("");
     }
     
     function setData(){
    	 
     }

   //绘制查询条件
 	function dreawFind(data){
 		dreawTable1(data);
 		mini.parse();
 	}

 	function dreawTable1(data){
		var table = $('<table/>'),obj,tr=null;//重置默认值
		var div;
		if(data){
			if(data.length>0){
				var tr;
				for(var i=0;i<data.length;i++){
					obj = data[i];
					var input1 = $("<input/>");
					if(obj){
						if(i%4 == 0){
							tr = $("<tr>");
						}
						//判断是不是查询机构
						if(obj.sffhbm == 1){
							$("#jgbh").css("display", "");
							$("#jgbh1").css("display", "");
						}else if(obj.isstarttime == 1){
							$("#start1").css("display", "");
							$("#start2").css("display", "");
						}else if(obj.isendtime == 1){
							$("#end1").css("display", "");
							$("#end2").css("display", "");
						}else{
							var zdlx = obj.zdlx;
							if(zdlx == 0 || zdlx == 2 || zdlx == 3|| zdlx == 5){
								input1.attr({id:obj.ysm,name:obj.ysm}).addClass("mini-textbox");
								tr.append($("<td/>").append(obj.yshy+":"),$("<td/>").append(input1).attr({width:"100px"}));
							}else if(zdlx == 1){
								input1.attr({id:obj.ysm,name:obj.ysm,valueField:"val",textField:"remark",url:"<%=root %>/param/getKeyList?key="+obj.mblx}).addClass("mini-combobox");
								tr.append($("<td/>").append(obj.yshy+":"),$("<td/>").append(input1).attr({width:"100px"}));
							}else if(zdlx == 4){
								input1.attr({id:obj.ysm,name:obj.ysm,format:'yyyy-MM-dd'}).addClass("mini-datepicker");
								tr.append($("<td/>").append(obj.yshy+":"),$("<td/>").append(input1).attr({width:"100px"}));
							}else{
								input1.attr({id:obj.ysm,name:obj.ysm}).addClass("mini-textarea");
								tr.append($("<td/>").append(obj.yshy+":"),$("<td/>").append(input1).attr({width:"100px"}));
							}
								
						}
						$("#table1").append(tr);
					}
				}
			}
		}
		return '';
	};
	
	//批量通过,只有没有下一审批人的才能
	function Ok(){
		//角色等级role_level
		var flag = "${flag}";
		//获取登记簿ID
		var djbid = "${djbid}";
		//如果flag=1,，则需要弹出下一审批人选择框，如果flag！=1，不需要弹出下一审批人选择框
        var rows = grid.getSelecteds();
        if (rows.length > 0) {
        	var ids = getRowsParaToJoin(rows,'id');
        	//弹出框选择下一审批人
        	if(flag == "1"){
        		$G.showmodaldialog("审批界面","<%=root %>/djbapprove/selApprove?ids="+ids+"&djbid="+djbid,300, 300,null,function (action) {
    				if(action=="save"){
    					mini.alert("审批成功!");
    					grid.reload();
    				}
    			});
        	}else{
		        mini.confirm("确定通过所有选中记录？", "提示", function(action) {
		            if (action == 'ok') {
			            AjaxToSet({
			                 url: "<%=root %>/djbapprove/allApprove?flag=0",
			                 data:{ids:ids,djbid:djbid},
			                 success: function (text) {
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

    //查看详情
    function detail() {
  	  var row = grid.getSelected();
	      djbid = "${djbid}";
	      if (row) {
	        var bizData = {
	          pageType : "edit",
	          id : row.id,
	          djbid : djbid
	          };
	       $G.showmodaldialog("详情","<%=root %>/djbapprove/editDetail?djbid="+djbid+"&pageType=detail&id="+row.id,800, 500,bizData,function (action) {
	      	 if(action=="save"){
	    		   mini.alert("修改成功!");
	    		   grid.reload();
	    	 }else{
	    		 grid.reload();
	    	 }
		     });
	      } else {
	          mini.alert("请选中一条记录", "提示");
	      }
     }


   //查询
    function search() {
 	   var length = arrs.length;
	   //将所有查询条件存到数组里
	   var sz = "";
	   //将机构多选存储在数组中
	   var jgcxtj = "";
	   var startTime=mini.get("startTime").getValue();
	   var endTime =mini.get("endTime").getValue();
	   for(var i=0;i<length;i++){
		   var v;
		   if(arrs[i].bhqz == "jgcxtj"){
			   jgcxtj = mini.get("jgcxtj").getValue();
		   }
			   if(arrs[i].ysm != null){
				   v = mini.get(arrs[i].ysm).getValue();
			   }else{
				   v = mini.get(arrs[i].bhqz).getValue();
			   }
			   if(v==""){
				  v=" "; 
			   }
			  sz+= v+",";
	   }
        var bizdata={"sz":sz,"jgcxtj":jgcxtj,"startTime":startTime,"endTime":endTime,"status":status};
        grid.load(bizdata);//grid查询
    }

  //重置查询条件
    function reset() {
      form.reset();
    }

    //enter键触发查询
    function onKeyEnter(e) {
      search();
    }
        
	function approve(){
		var row = grid.getSelected();
		var djbid = "${djbid}";
		if (row) {
			var bizData = {
				id : row.id,
				djbid : djbid
			};
			nui.showmodaldialog("复核","<%=root %>/djbapprove/approves?djbid="+djbid+"&id="+row.id,800, 500,bizData,function (action) {
					mini.alert("复核成功!");
					grid.reload();
			},false,false);
		} else {
			mini.alert("请选中一条记录", "提示");
		}
	}

      </script>
    </body>
  </html>
