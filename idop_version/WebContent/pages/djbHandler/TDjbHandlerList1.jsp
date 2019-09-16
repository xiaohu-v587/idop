<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<script src="<%=request.getContextPath()%>/resource/js/panorama.js" type="text/javascript"></script>
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
 	body{
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
			             <a id="update" class="mini-button"  onclick="edit()">修改</a>
			            &nbsp;&nbsp;
			            <a class="mini-button"  onclick="detail()" >详情</a>
			            &nbsp;&nbsp;
			            <a id="export" class="mini-button"  onclick="download()" style="display:none">导出</a>
			             <a id="export2" class="mini-button" onclick="download2()" >导出</a>
			            
			            &nbsp;&nbsp;
			              <a id="remove" class="mini-button"  onclick="remove()" >删除</a>
			            &nbsp;&nbsp;
			            <a id="approve" class="mini-button"  onclick="approve()" style="display:none">提交</a>
		            </td>
				</tr>
			</table>
		</div>
		<div id ="gridpanal" class="mini-fit">
		    <div id="datagrid1"  dataField="data" class="mini-datagrid" style="width:100%;height:95%;" sortMode="client" allowUnselect="false"
				url="<%=root%>/djbHandler/queryList?djbid=<%=request.getAttribute("djbid")%>&flag=1" onrowclick=""  multiSelect="true" allowCellSelect="false" allowResize="true"
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
    grid.load();
    var djbid = "${djbid}";
    var orgid = "${orgid}";
     var useflag = "${useflag}";
     var level = "${level}";
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
	            url: "<%=root%>/djbHandler/queryHeaders1",
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
      //编辑登记簿
    function edit() {
	      var row = grid.getSelected();
	      djbid = "${djbid}";
	      var sfpz = "${sfpz}";
	      if(useflag=="0"){
	      	$G.alert("登记簿已停用，不允许修改！");
    		return;
	      }
	      if(sfpz==1){
	      	 if(row.status == "0"||row.status == "2"){
	      		mini.alert("待复核数据无法修改！");
	      		return;
	      	}
	     }
	     // var sfpz = '${sfpz}';
	      if (row) {
	        var bizData = {
	          pageType : "edit",
	          id : row.id,
	          djbid : djbid,
	          status : row.status
	          };
	         //sfpz : sfpz
	        var url = "<%=root %>/djbHandler/editDetail?djbid="+djbid+"&pageType=edit&id="+row.id+"&status="+row.status;
			$.ajax({
	            url: "<%=root%>/djbHandler/checkUserOrg",
	            data:{orgid:row.jgbh,pageType:"edit"},
	            cache: false,
	            success: function (text) {
		            if(text.flag == "1"){
		            	mini.open({
	           				 title: "修改",
	           				 url: url,
	           				 width: 800,
	           				 height: 450,
	           				 allowResize: false,
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
		            }else{
		            	mini.alert("非本机构数据无法进行此操作","提示");
		            }
		        }
		      }) 
		    } else {
	          mini.alert("请选中一条记录", "提示");
	      }
   }
      function statusRender(e){
    	 var status = e.value;
    	 var str ="";
    	 if(status == "2"){
    		 str = "待提交";
    	 }else if(status == "0"){
    		 str = "已提交";
    	 }else{
    	  	 str = "已复核";
    	 }
    	 return str;
     }
     
     function dreawPanal(){
    	 djbid = "${djbid}";
    	 $.ajax({
	            url: "<%=root%>/djbHandler/queryCxtj",
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
     
     function setData(data){
     var infos = mini.clone(data);
    	 var sfpz=infos.sfpz;
    	 if(sfpz==0){
    	  $("#approve").css("display", "none");
    	 }else{
    	 $("#approve").css("display", "none");
    	 }
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
								input1.attr({id:obj.ysm,name:obj.ysm,valueField:"val",textField:"remark",url:"<%=root%>/param/getKeyList?key="+obj.mblx}).addClass("mini-combobox");
							
								tr.append($("<td/>").append(obj.yshy+":"),$("<td/>").append(input1).attr({width:"100px"}));
							}else if(zdlx == 4){
								input1.attr({id:obj.ysm,name:obj.ysm,format:'yyyy-MM-dd'}).addClass("mini-datepicker");
								tr.append($("<td/>").append(obj.yshy+":"),$("<td/>").append(input1).attr({width:"100px"}));
							}else{
								input1.attr({id:obj.ysm,name:obj.ysm}).addClass("mini-textbox");
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

  //删除TDjbBhzdy
        function remove() {
          var rows = grid.getSelecteds();
          if (rows.length > 0) {
          if(level=="0"){
          	alert("非系统管理员，无操作权限");
          	return;
          }
            mini.confirm("确定删除选中记录？", "删除提示", function(action) {
              if (action == 'ok') {
                var ids = "";
                for(var index = 0;index < rows.length;index++){
                  if(ids == ""){
                    ids = rows[index].id;
                  } else {
                    ids += "," + rows[index].id;
                  }
                }
               $.ajax({
            	   url:"<%=root %>/djbHandler/delete",
            	   data:{ids:ids,djbid:"${djbid}"},
            	   success:function(text){
        			if(text.flag == "1"){
        				mini.alert("删除成功！");
        				grid.reload();
        			}else{
        				mini.alert("删除失败！");
        				grid.reload();
        			}
        		}
               });
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
        var bizdata={"sz":sz,"jgcxtj":jgcxtj,"startTime":startTime,"endTime":endTime};
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
	        var url ="<%=root%>/djbHandler/editDetail1?djbid="+djbid+"&pageType=detail&id="+row.id ;
			$G.showmodaldialog("详情",url,800,500,bizData,function(action){
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
      
      //下载
      function download(){
    	   var length = arrs.length;
    	   //将所有查询条件存到数组里
    	   var sz = "";
    	   //将机构多选存储在数组中
    	   var jgcxtj = "";
    	   var djbid="${djbid}";
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
		  window.location=encodeURI("<%=root%>/djbHandler/download?sz="+sz+"&djbid="+djbid+"&flag=1&jgcxtj="+jgcxtj);
      }
       
       function download2(){
	       var data = form.getData();
		   var length = arrs.length;
		   data['pageSize']  = 1;
		   data['pageIndex'] = 0;
    	   //将所有查询条件存到数组里
    	   var sz = "";
    	   //将机构多选存储在数组中
    	   var jgcxtj = "";
    	   var djbid="${djbid}";
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
    	   data.sz = sz;
    	   data.djbid=djbid;
    	   data.flag="1";
    	   data.jgcxtj=jgcxtj;
    	    data['execlfilename'] = '登记簿目录列表';
				callHeadAndTextToDataByDataGrid2(grid,data); 
			 window.location="<%=root%>/djbHandler/download2?"+connUrlByJson(data);
		
	}
       
       
       function approve(){
		var row = grid.getSelected();
		var djbid = "${djbid}";
		var status = row.status;
		if(status!="2"){
			alert("请选择状态为待提交的数据进行提交！");
			return;
		}
		if (row) {
			var bizData = {
				id : row.id,
				djbid : djbid
			};
			$G.showmodaldialog("提交审批界面","<%=root %>/djbHandler/approves?djbid="+djbid+"&id="+row.id,800, 500,bizData,function (action) {
				if(action=="save"){
					mini.alert("提交成功!");
					grid.reload();
				}
			});
		} else {
			mini.alert("请选中一条记录", "提示");
		}
	}
	/**
 * 组合自定义表格 头部数据
 * @author 常显阳 20181203
 * @param id
 * @param data {a:b，c:d} 
 */	
function callHeadAndTextToDataByDataGrid2(grid,data){	
	var gridcolumns = grid.getBottomColumns();
	var headers = [],columns=[];
	for(var i=2;i<gridcolumns.length;i++){
			headers.push(gridcolumns[i].header);
			columns.push(gridcolumns[i].field);
	}
	data["execlheaders"] = headers.join(",");
	data["execlcolumns"] = columns.join(",");
}
      </script>
    </body>
  </html>
