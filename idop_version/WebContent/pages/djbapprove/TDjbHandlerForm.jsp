<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
   * 动态显示表结构
   *
   * Created on 
   * @zhaomiao  
   * @reviewer 
-->
<head>
    <title>动态显示表结构</title>
</head>
 <style type ="text/css">
 	html, body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
 </style>
<body>
	<!-- 标识页面是查看(query)、修改(edit)、新增(add) -->
	<div class="mini-fit">
		<input name="pageType" class="mini-hidden" />
		<form id="dataform1" method="post">
			<input name="djbid" class="mini-hidden" />
			<input name="id" class="mini-hidden" value="${id}" />
			<fieldset style="border: solid 1px #aaa; height: 98%;">
				<legend>信息录入</legend>
				<div style="padding: 0px; margin: 0px;" id="parentpoint">
				</div>
				<div style="padding: 2px; margin: 2px;">
					附件上传：<a class="mini-button" onclick="openW" id="filebtn">点击打开上传页面</a>
					<div id="datagrid2" dataField="datas" class="mini-datagrid"
									style="width: 100%; height: 180px;" sortMode="client"
									allowUnselect="false" oncellclick="" onrowdblclick=""
									onselectionchanged="" onload=""
									url="<%=root %>/common/queryFiles?pid=${id}" autoEscape="false"
									onshowrowdetail="" showEmptyText="true" emptyText="没有对应的附件信息"
									ondrawcell="ondrawcell2" pageSize="5">
						<div property="columns">
							<div field="ID" visible="false"></div>
							<div field="PID" visible="false">任务父id</div>
							<div field="FILENAME" headerAlign="center" align="center">附件名称</div>
							<div field="FILEPATH" headerAlign="center" align="center">附件路径</div>
							<div field="FILETYPE" headerAlign="center" align="center">附件格式</div>
							<div field="CREATEDATE" headerAlign="center" align="center">上传日期</div>
							<div field="YXPTID" visible="false"></div>
							<div field="PROC" headerAlign="center" align="center">操作</div>
						</div>
					</div>
				</div>
			</fieldset>
		</form>       
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a class="mini-button" onclick="onOk()" >保存</a>
		<a class="mini-button" onclick="onCancel()" style="margin-right: 20px;" > 取消 </a> 
	</div>
	<script type="text/javascript">
		mini.parse();
		var infos ;
		var form =  new mini.Form("dataform1");
		var grid2 = mini.get("datagrid2");	//获取datagrid2标签
        grid2.load();
		var state={dreawFlag:0};
		var arrs;
		$(document).ready(function(){
			var djbid = "${djbid}";
			$.ajax({
	            url: "<%=root %>/djbapprove/addDetail1",
	            data:{djbid:djbid},
	            cache: false,
	            success: function (text) {
		            arrs = text.zdlist;
		            dreaw(arrs); 
		            drawValue(arrs);
	            }
	     	});
	   	});
		

	 	function drawValue(aue){
			var pageType = mini.getbyName("pageType").getValue();//获取当前页面是编辑还是新增状态
			//表示为编辑状态
			if (pageType == "edit") {
				//根据当前模块信息ID(模块信息流程主键)获取当前节点所处要素
				postByAjaxX({djbid:infos.djbid,id:infos.id}, new Cap4jAjaxConf(), "djbapprove/getDatileData.do",function(data) {
						var form = mini.getForm("dataform1");//将普通form转为mini的form
						lowerJSONKey(data.data[0])
						form.setData(data.data[0]);
						mini.getbyName("djbid").setValue(infos.djbid);
						setComStr();
				});
			}else{
				setComStr(aue);
			}
		}
		
		function setComStr(e){
			for(var i=0;i<e.length;i++){
				var a = mini.getbyName(e[i].ysm);
			}
		}
		
		function dreaw(data){
			var parent =  $('#parentpoint');
			var editVideo = new Array();
			//分组 
			if(data){
				if(data.length>0){
					for(var i=0;i<data.length;i++){
							editVideo.push(data[i]);
					}
				}
			}
			parent.append(dreawTable(editVideo));
			mini.parse();
		};
		function dreawTable(data){
				var table = $('<table/>');//重置默认值
				var obj = null,tr = null;
				var  temp = 2,cellcount = temp<=data.length?temp:data.length,temp1 = cellcount;
				var tr_arr = new Array();
				if(data){
					if(data.length>0){
						for(var i=0;i<data.length;i++){
							obj = data[i];
							if(obj){
								if(cellcount>0){
									if(tr==null){
										tr = $('<tr>');
									}
									if(obj.zdlx=='6'){
										if(cellcount>1){
											tr.append($('<td/>').append(obj.YSHY).attr({width:"15%"}),$('<td/>').append(UIStore(obj)).attr({colspan:'3'}));
											cellcount--;//因为此处占据3列按照最大2组空位计算
										}else{
											if(tr){
												table.append(tr);
											}
											tr = $('<tr>').append($('<td/>').append(obj.YSHY).attr({width:"15%"}),$('<td/>').append(UIStore(obj)).attr({colspan:'3'}));
											table.append(tr);
											tr = null;
											cellcount = temp1;
											continue;
										}
									}else if(obj.zdlx){
										tr.append($('<td/>').append(obj.yshy).attr({width:"15%"}),$('<td/>').append(UIStore(obj)));
									}else{
										obj.zdlx='7';
										tr.append($('<td/>').append(obj.yshy).attr({width:"15%"}),$('<td/>').append(UIStore(obj)));
									}
									cellcount--;
									if(cellcount==0 || (i+1)==data.length){
										if(tr!=null){
											table.append(tr);
											tr = null;
										}
										cellcount = temp1;
									};
								}
							}
						}
						return table.attr({width:'100%',height:'100%'});
					}
				}
				return '';
			};
		
		function UIStore(element){
			/* var isReadOnly =function(){if(element.FXQY==0){return true;}return false;};
			var isEnabled = isReadOnly(); */
			/* var isSave = function(){if(element.SFJL=='0'){return "";}return element.ysm;}; */
			var e={type:null,Class:null,attr:null};
			switch(element.ZDLX){
				case '0' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz/2+"; minLength:"+element.cdxz/2,width:'80%'}}; break;
				case '1' :e = {type:'<div/>'  ,Class:'mini-combobox',attr:{valueField:"val",textField:"remark",url:"<%=root %>/param/getKeyList?key="+element.mblx,name:element.ysm,vtype:"maxLength:"+element.cdxz,width:'80%',allowInput:false}}; break;
				case '2' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz,width:'80%'}}; break;
				case '3' :e = {type:'<input/>',Class:'mini-spinner',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz,width:'80%',maxValue:'9999999999',decimalPlaces:'2',minValue:'-9999999999'}}; break;
				case '4' :e = {type:'<input/>',Class:'mini-datepicker',attr:{name:element.ysm,format:'yyyy-MM-dd HH:mm:ss',vtype:"maxLength:"+element.cdxz,width:'80%'}};  break;
				case '5' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz,onvalidation:'onMoneyValidation',width:'80%'}};; break;
				case '6' :e = {type:'<input/>',Class:'mini-textarea',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz,width:'92%'}}; break;
				case '7' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,width:'80%'}}; break;
				case '8' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '9' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '10' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '11' :e = {type:'<input/>',Class:'mini-textarea',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '12' :e = {type:'<div/>'  ,Class:'mini-combobox',attr:{valueField:"val",textField:"remark",url:"<%=root %>/param/getKeyList?key="+element.mblx,name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%',allowInput:false,multiSelect:true}}; break;
				case '13' :e = {type:'<div/>'  ,Class:'mini-combobox',attr:{valueField:"val",textField:"remark",url:"<%=root %>/param/getKeyList?key="+element.mblx,name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%',allowInput:true}}; break;

				default: return null;break;
			};
			
			return $(e.type).addClass(e.Class).attr(e.attr);
		}
		
		function onMoneyValidation(e){
	    	if (e.isValid) {
	    		if (new RegExp("^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{1,2})?))$").test(e.value) == false){
	    			e.errorText = "请输入正确的金额！"; 
	    			e.isValid = false;
	    		}
	    	}
    	}
		
		//json方式保存数据
		function saveJson() {
			//保存
			var urlStr = "<%=root %>/djbapprove/save";
			var pageType = mini.getbyName("pageType").getValue();//获取当前页面是编辑还是新增状态
			//表示为编辑状态
			if (pageType == "edit") {
				urlStr = "<%=root %>/djbapprove/update";
			}
			var o = form.getData(); 
		    var data = mini.encode([o]);           
		    form.validate();
		  //表示为编辑状态
		  	var json = mini.encode([o]);
		  	$.ajax({
	             url: urlStr,
			     type: 'post',
	             data: { data: json},
	             cache: false,
	             success: function (text) {
	             	if(pageType=="1"){
	             		mini.alert("修改成功!");
	             	}else{
	             		mini.alert("新增成功!");
	             	}
	             	CloseWindow("save");
	             }
	             ,error: function (jqXHR, textStatus, errorThrown) {
	                 alert(jqXHR.responseText);
	                 CloseWindow();
	             }
	         });
		}
		

		
		//获取父页面传递来的json数据
		function setData(data) {
			//跨页面传递的数据对象，克隆后才可以安全使用
			infos = mini.clone(data);
			//保存list页面传递过来的页面类型：add表示新增、edit表示编辑
			if(mini.getbyName("pageType")){
				mini.getbyName("pageType").setValue(infos.pageType);
			}
			mini.getbyName("djbid").setValue(infos.djbid);
			//如果是点击编辑类型页面
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
		
		
		function postByAjaxX(data,ajaxConf,url,fun){
				ajaxConf.setIsShowSuccMsg(false);
				ajaxConf.setSuccessFunc(fun);
				mini.postByAjax(data,url,ajaxConf);
		};
		//显示回退理由输入页面
		function backwork(){
			$("#DivBack").show();
		}
		
		//隐藏回退输入框
		function hidtxt(){
			$("#DivBack").hide();
		}
		 function upperJSONKey(jsonObj){
		    for (var key in jsonObj){  
		        jsonObj[key.toUpperCase()] = jsonObj[key];  
		        delete(jsonObj[key]);  
		    }  
		    return jsonObj;  
		} 
        
        function lowerJSONKey(jsonObj){
		    for (var key in jsonObj){  
		        jsonObj[key.toLowerCase()] = jsonObj[key];  
		        delete(jsonObj[key]);  
		    }  
		    return jsonObj;  
		} 
        
        function openW() {
     		 var pid = '${id}';
     	        mini.open({
     	            title: "上传面板",
     	            url: "<%=root %>/common/goFileUpload?pid="+pid+"&ebavleType=false&ebavleUrl=common/getParamCombox?KEY=1203",
     	            width: 600,
     	            height: 350,
     	            allowResize: false,
     	            onload: function () {
     	                var iframe = this.getIFrameEl();
     	                var data = {};  //模拟传递上传参数
     	                iframe.contentWindow.setData(data);
     	            },
     	            ondestroy: function (action) {
     	                if (action == "ok") {
     	                    var iframe = this.getIFrameEl();
     	                    var data = iframe.contentWindow.getData();
     	                    data = mini.clone(data);
     	                    grid2.reload();
     	                }
     	            }
     	        })
     	    }
       //翻译字段
         function ondrawcell1(e){
         	if(e.field == "proc") { // 操作
         		e.cellStyle="text-align:center";
     			e.cellHtml = "<a href='<%=root %>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURI(encodeURI(e.record.filename))+"&filePath="+encodeURI(encodeURI(e.record.filepath))+"'  target='_blank'>查看</a>";
         	}
         }
       //翻译字段
         function ondrawcell2(e){
         	if(e.field == "proc") { // 操作
         		e.cellStyle="text-align:center";
     			e.cellHtml = "<a href='<%=root %>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURI(encodeURI(e.record.filename))+"&filePath="+encodeURI(encodeURI(e.record.filepath))+"'  target='_blank'>查看</a>&nbsp;<a href='javascript:profile(\"1\",\""+e.record.id+"\",\""+e.record.createdate+"\",\""+e.record.yxptid+"\")'>删除</a>";
         	}
         }
       
         function profile(flag,id,createdate,yxptid) {
     		mini.confirm("是否确认删除?","删除确认",function(action){
     	 		if(action == "ok"){
                 	 $.ajax({
                         url: "<%=root %>/common/delFile",	//将请求提交至UserCtl的save方法
          		        type: 'post',
                          data:  {id:id,yxptid:yxptid,createdate:createdate},
                          cache: false,
                          success: function (text) {
                          	var flag = mini.decode(text).flag;
          		             if(flag == "1"){	
          		            	 mini.alert("删除成功！");
          		             	 grid2.reload();
          		             }else {	
          		            	mini.alert("删除失败！");
          		             }
                          },
                          error: function (jqXHR, textStatus, errorThrown) {
                              alert(jqXHR.responseText);
                              CloseWindow();
                          }
                      });
     	 		}});
     	}

		
	</script>
</body>
</html>
