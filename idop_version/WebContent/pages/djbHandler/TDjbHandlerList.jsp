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
	          		<tr id="jgbh" style="display:none;">
		          		<td style="width:90px;">机构:</td>
						<td style="width:300px;" colspan="3"><input id="jgcxtj" name="jgcxtj" style="width:300px;"
							class="mini-treeselect" url="<%=root %>/org/getListByUser"
							dataField="datas" textField="orgname" valueField="orgnum"
							parentField="upid" valueFromSelect="true" multiSelect="true"
							expandOnLoad="0" emptyText="机构" allowInput="false"
							showClose="true" oncloseclick="onCloseClick" 
							showRadioButton="false" showFolderCheckBox="true"
							popupWidth="286" popupHeight="470" popupMaxHeight="600" />
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
			            <a class="mini-button" iconCls="icon-add" onclick="add()" >增加</a>
			            &nbsp;&nbsp;
			            <a id="update" class="mini-button" iconCls="icon-edit" onclick="edit()">编辑</a>
			            &nbsp;&nbsp;
			            <a class="mini-button" iconCls="icon-new" onclick="detail()" >详情</a>
			            &nbsp;&nbsp;
			            <a id="remove" class="mini-button" iconCls="icon-remove" onclick="remove()" >删除</a>
			            &nbsp;&nbsp;
			            <a id="approve" class="mini-button" iconCls="icon-upload" onclick="approve()"  style="display:none">提交</a>
			            &nbsp;&nbsp;
			            <a class="mini-button" iconCls="icon-search" onclick="search()">查询</a>
			            &nbsp;&nbsp;
			            <a class="mini-button" iconCls="icon-reload" onclick="reset()">重置</a>
			            &nbsp;&nbsp;
			            <a id="onOk" class="mini-button" iconCls="icon-ok" onclick="onOk()" style="display:none">确认</a>
			            &nbsp;&nbsp;
			            <a class="mini-button" iconCls="icon-search" onclick="lookup()" style="display:none">查看审批记录</a>
		            </td>
				</tr>
			</table>
		</div>
		<div id ="gridpanal" class="mini-fit">
		    <div id="datagrid1"  dataField="data" class="mini-datagrid" style="width:100%;height:95%;" sortMode="client" allowUnselect="false"
				url="<%=root %>/djbHandler/queryList?djbid=<%=request.getAttribute("djbid")%>" onrowclick="onrowclick"  multiSelect="true" allowCellSelect="false" allowResize="true"
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
	            url: "<%=root %>/djbHandler/queryHeaders",
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
     
     function onrowclick(e){
    	 var sfpz = "${sfpz}";
    	 if(sfpz == "0"){
    		 var stat = e.record.status;
	  		 if(stat == "2"){
	  			 mini.get("update").setEnabled(true);
	  			 mini.get("remove").setEnabled(true);
	  			 mini.get("approve").setEnabled(false);
	  			 mini.get("onOk").setEnabled(true);
	  		 }else{
	  			 mini.get("update").setEnabled(false);
	  			 mini.get("remove").setEnabled(false);
	  			 mini.get("approve").setEnabled(false);
	  			 mini.get("onOk").setEnabled(false);
	  		 }
    	 }else{
    		 mini.get("onOk").hide();
	   		 var stat = e.record.status;
	  		 if(stat == "2" || stat == "3"){
	  			 mini.get("update").setEnabled(true);
	  			 mini.get("remove").setEnabled(true);
	  			 mini.get("approve").setEnabled(true);
	  		 }else{
	  			 mini.get("update").setEnabled(false);
	  			 mini.get("remove").setEnabled(false);
	  			 mini.get("approve").setEnabled(false);
	  		 }
    	 }
     }

     function dreawPanal(){
    	 djbid = "${djbid}";
    	 $.ajax({
	            url: "<%=root %>/djbHandler/queryCxtj",
	            data:{djbid:djbid},
	            cache: false,
	            success: function (text) {
		            arrs = text.cxtj;
					dreawFind(arrs);
	            }
	     	});
		}
     
     function statusRender(e){
    	 var status = e.value;
    	 var sfpz = '${sfpz}';
    	 if(sfpz == "0"){
    		 var str = "待确认";
    		 if(status == 1){
    			 str = "已通过";
    		 }
    	 }else{
	    	 var str = "未提交";
	    	 if(status == 0){
	    		 str = "已提交";
	    	 }else if(status == 1){
	    		 str = "已通过";
	    	 }else if(status == 3){
	    		 str = "已退回";
	    	 }
    	 }
    	 return str;
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
						}else{
							var zdlx = obj.zdlx;
							if(zdlx == 0 || zdlx == 2 || zdlx == 3|| zdlx == 5|| zdlx == 7){
								input1.attr({id:obj.ysm,name:obj.ysm}).addClass("mini-textbox");
								tr.append($("<td/>").append(obj.yshy+":"),$("<td/>").append(input1).attr({width:"100px"}));
							}else if(zdlx == 1){
								input1.attr({id:obj.ysm,name:obj.ysm,valueField:"val",textField:"remark",url:"<%=root %>/param/getKeyList?key="+obj.mblx}).addClass("mini-combobox");
								tr.append($("<td/>").append(obj.yshy+":"),$("<td/>").append(input1).attr({width:"100px"}));
							}else if(zdlx == 4){
								input1.attr({id:obj.ysm,name:obj.ysm,format:'yyyy-MM-dd HH:mm:ss'}).addClass("mini-datepicker");
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
	
	//确认数据
	function onOk(){
		var row = grid.getSelected();
        if (row) {
	        mini.confirm("数据确认后不能再修改,是否确认？", "数据确认", function(action) {
		        if (action == 'ok') {
			        $.ajax({
			        		url: "<%=root %>/djbHandler/queRen",
			        		data:{id:row.id,djbid:"${djbid}"}, 
			        		success:function(text){
				      	if(text.flag == "1"){
					      	mini.alert("数据确认成功！");
					      	grid.reload();
				      	}else{
					      	mini.alert("数据确认失败！");
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


	//新增TDjbBhzdy
    function add() {
    	djbid = "${djbid}";
		var bizData = {pageType : "add",djbid:djbid};//传入模态窗口页面的json数据
		//传入模态窗口页面的json数据
		mini.open({
	            title: "编辑登记簿",
	            url: "<%=root %>/djbHandler/addDetail?djbid="+djbid,
	            width: 950,
	            height: 600,
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

		//var url = "<%=root %>/djbHandler/addDetail?djbid="+djbid+"&pageType=add";
		    			//$G.showmodaldialog("新增信息模块",url,800,500,bizData,function(action){
		    			//	grid.reload();
		    			//});
	}

    //编辑登记簿
    function edit() {
	      var row = grid.getSelected();
	      djbid = "${djbid}";
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
			$G.showmodaldialog("编辑信息模块",url,800,500,bizData,function(action){
				 if(action=="save"){
		    		   mini.alert("修改成功!");
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
        var bizdata={"sz":sz,"jgcxtj":jgcxtj};
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
	      var sfpz = '${sfpz}';
	      if (row) {
	        var bizData = {
	          pageType : "detail",
	          id : row.id,
	          djbid : djbid,
	          status : row.status,
	          sfpz : sfpz
	          };
	        $G.showmodaldialog("编辑信息模块","<%=root %>/djbHandler/editDetail?djbid="+djbid+"&pageType=detail&id="+row.id+"&status="+row.status,800, 500,bizData,function (action) {
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
    	  var sz = "";
		  for(var i=0;i<length;i++){
	   		  var v;
	   		  if(arrs[i].YSM != null){
	   			  v = mini.get(arrs[i].ysm).getValue();
	   		  }else{
	   			  v = mini.get(arrs[i].bhqz).getValue();
	   		  }
	   		  if(v==""){
	   			  v=" "; 
	   		  }
	   		  sz+= v+",";
   	      }
		  window.location=encodeURI("<%=root %>/djbHandler/download?sz="+sz+"&djbid="+"${djbid}");
      }
        
        //删除TDjbBhzdy
        function remove() {
          var rows = grid.getSelecteds();
          if (rows.length > 0) {
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
        
	function approve(){
		var row = grid.getSelected();
		var djbid = "${djbid}";
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
	
	function lookup(){
		var row = grid.getSelected();
		var djbid = "${djbid}";
		if (row) {
			var bizData = {
				id : row.id,
				djbid : djbid
			};
			$G.showmodaldialog("审批记录查看","<%=root %>/djbHandler/lookup?djbid="+djbid+"&sjid="+row.id,700, 400,bizData,function (action) {
				grid.reload();
			});
		} else {
			mini.alert("请选中一条记录", "提示");
		}
	}

      </script>
    </body>
  </html>
