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
 	body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
 </style>
<body>
	<!-- 标识页面是查看(query)、修改(edit)、新增(add) -->
	<div class="mini-fit">
		<input name="pageType" class="mini-hidden" />
		<form id="dataform1" method="post">
			<input name="djbid" class="mini-hidden" />
			<input name="id" class="mini-hidden" />
			<fieldset style="border: solid 1px #aaa; height: 98%;">
				<legend>详情</legend>
				<div style="padding: 0px; margin: 0px;" id="parentpoint"></div>
				<div style="padding: 2px; margin: 2px;">
					附件上传：<a class="mini-button" onclick="openW" id="filebtn">点击打开上传页面</a>
					<div id="datagrid2" dataField="datas" class="mini-datagrid"
									style="width: 700px; height: 180px;" sortMode="client"
									allowUnselect="false" oncellclick="" onrowdblclick=""
									onselectionchanged="" onload=""
									url="<%=root %>/common/queryFiles?pid=${id}" autoEscape="false"
									onshowrowdetail="" showEmptyText="true" emptyText="没有对应的附件信息"
									ondrawcell="ondrawcell1" pageSize="5">
						<div property="columns">
							<div field="id" visible="false"></div>
							<div field="pid" visible="false">任务父id</div>
							<div field="filename" headerAlign="center" align="center">附件名称</div>
							<div field="filepath" headerAlign="center" align="center">附件路径</div>
							<div field="filetype" headerAlign="center" align="center">附件格式</div>
							<div field="createdate" headerAlign="center" align="center">上传日期</div>
							<div field="yxptid" visible="false"></div>
							<div field="proc" headerAlign="center" align="center">操作</div>
						</div>
					</div>
				</div>
			</fieldset>
		</form>       
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a class="mini-button" onclick="onOk()" id="onOk" >保存</a>
		<a class="mini-button" onclick="onCancel()" style="margin-right: 20px;" > 取消 </a> 
	</div>
	<script type="text/javascript">
		mini.parse();
		var infos ;
		var form =  new mini.Form("dataform1");
		var grid2 = mini.get("datagrid2");	//获取datagrid2标签
		 var pid = '${id}';
        grid2.load({pid:pid});
		var state={dreawFlag:0};
		var zdsmMap = {};//用于记录字段说明的name
		var yzmm = {};//保存是否需要验证密码
		var yzmmtg = {};//保存已验证密码且密码正确
		var arrs;
		$(document).ready(function(){
			var djbid = "${djbid}";
			var id = "${id}";
			var pageType = "${pageType}";
			$.ajax({
	            url: "<%=root %>/djbHandler/addDetail1",
	            data:{djbid:djbid},
	            cache: false,
	            success: function (text) {
		            arrs = text.zdlist;
		            dreaw(arrs); 
		            drawValue();
	            }
	     	});
	   	});
		

	 	function drawValue(){
	 		var djbid = "${djbid}";
			var id = "${id}";
			var pageType = "${pageType}";
				$.ajax({
		            url: "<%=root %>/djbHandler/getDatileData",
		            data:{djbid:djbid,id:id},
		            cache: false,
		            success: function (text) {
						form.setData(lowerJSONKey(text.data));
						mini.getbyName("djbid").setValue(djbid);
		            }
		     	});
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
						var bh = "input"+(i);
						var bh = $("<input/>");
						obj = data[i];
						var confirmbtn = $("<a/>");
						bh.attr({id:"zdsm"+i,border:1,value:obj.zdsm}).addClass("mini-textbox");
						//confirmbtn.attr({id:"confirm"+i,href:"javascript:confirmPassword("+"'"+i+"'"+")"}).append("<img border='0' src='../resource/images/actiDesig/ok.png' />");
						confirmbtn.attr({id:"confirm"+i,onclick:"confirmPassword("+"'"+i+"'"+")",href:"javascript:confirmPassword("+"'"+i+"'"+")"}).attr({style:" width:40px; height: 25px; ",class:"mini-button",value:"验证"}).append("验证");
						zdsmMap["zdsm"+i]="name";
						if(obj){
							if(cellcount>0){
								tr = $('<tr>');
								tr.append($('<td/>').append(obj.yshy).attr({width:"15%"}),$('<td/>').append(UIStore(obj,i)).attr({width:"180px"})).append($('<td/>').append("说明:").attr({width:"50px"}))
								.append($("<td/>").append(bh).attr({width:"80px"}));
								if(obj.sfym=="1"){
									yzmm["sfym"+i] = "false";
									tr.append($('<td/>').append("输入密码:").attr({width:"70px"})).append($("<td/>").append($("<input/>").attr({id:"sfym"+i,border:1,class:'mini-password',required:'true',width:"80px;"}))).append($("<td/>").attr({id:"ts"+i,width:"60px;"}).hide().append("验证通过"))
									.append($("<td/>").append(confirmbtn).attr({width:"50px;"}))
								}
								table.append(tr);
							}
						}
					}
					return table.attr({width:'100%',height:'100%'});
				}
			}
			return '';
			};
			function confirmPassword(i){
		var ysm = $G.get("ysm"+i).getValue();
		var sfym = $G.get("sfym"+i).getValue();
		if(ysm==""||ysm==null||ysm=="null"||sfym==""||sfym==null||sfym=="null"){
			$G.alert("请输入要验证用户的用户名和密码");
			return;
		}else{
			var urlStr = "<%=root %>/djbHandler/testValidate";
		  	$.ajax({
	             url: "<%=root %>/djbHandler/testValidate",
	             data: {ysm:ysm,sfym:sfym},
	             cache: false,
	             success: function (text) {
	             	if(text.flag==true||text.flag=="true"){
						yzmmtg["sfym"+i] = "true";
						$("#ts"+i).show();
						$G.alert("验证通过");
					}else{
							yzmmtg["sfym"+i] = "false";
							$G.get("sfym"+i).setValue("");
							$G.alert("验证失败");
						}
	             }
	             ,error: function (jqXHR, textStatus, errorThrown) {
	                 alert(jqXHR.responseText);
	                 CloseWindow();
	             }
	         });		
			}			
		}
		
			
			 function openW() {
     		 var pid = '${id}';
     	        mini.open({
     	            title: "上传面板",
     	            url: "<%=root%>/common/goFileUpload?pid="+pid,//+"&ebavleType=false&ebavleUrl=common/getParamCombox?KEY=1203"
     	            width: 600,
     	            height: 350,
     	            allowResize: false,
     	            onload: function () {
     	                var iframe = this.getIFrameEl();
     	                var data = {};  //模拟传递上传参数
     	                //iframe.contentWindow.setData(data);
     	            },
     	            ondestroy: function (action) {
     	                if (action == "cancel") {
     	                    var iframe = this.getIFrameEl();
     	                    //var data = iframe.contentWindow.getData();
     	                    //data = mini.clone(data);
     	                    grid2.load({pid:pid});
     	                }
     	            }
     	        })
	     	    }
		
		function UIStore(element,i){
			var e={type:null,Class:null,attr:null};
			if("${pageType}" == "detail"){
				switch(element.zdlx){
				case '0' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,vtype:"maxLength:"+element.cdxz/2+"; minLength:"+element.cdxz/2,readonly:"readonly",width:'80%'}}; break;
				case '1' :e = {type:'<div/>'  ,Class:'mini-combobox',attr:{valueField:"val",disable:true,textField:"remark",url:"<%=root %>/param/getKeyList?key="+element.mblx,name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%',allowInput:false}}; break;
				case '2' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,readonly:"readonly",vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '3' :e = {type:'<input/>',Class:'mini-spinner',attr:{name:element.ysm,readonly:"readonly",vtype:"maxLength:"+element.cdxz/2,width:'80%',maxValue:'9999999999',decimalPlaces:'2',minValue:'-9999999999'}}; break;
				case '4' :e = {type:'<input/>',Class:'mini-datepicker',attr:{name:element.ysm,readonly:"readonly",format:'yyyy-MM-dd HH:mm:ss',vtype:"maxLength:"+element.cdxz/2,width:'80%'}};  break;
				case '5' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,readonly:"readonly",vtype:"maxLength:"+element.cdxz/2,width:'80%'}};; break;
				case '6' :e = {type:'<input/>',Class:'mini-textarea',attr:{name:element.ysm,readonly:"readonly",vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '7' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,readonly:"readonly",vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '8' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,readonly:"readonly",vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '9' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,readonly:"readonly",vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '10' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,readonly:"readonly",vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '11' :e = {type:'<input/>',Class:'mini-textarea',attr:{name:element.ysm,readonly:"readonly",vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '12' :e = {type:'<div/>'  ,Class:'mini-combobox',attr:{valueField:"val",disable:true,textField:"remark",url:"<%=root %>/param/getKeyList?key="+element.mblx,name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%',allowInput:true}}; break;
				case '13' :e = {type:'<div/>'  ,Class:'mini-combobox',attr:{valueField:"val",disable:true,textField:"remark",url:"<%=root %>/param/getKeyList?key="+element.mblx,name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%',allowInput:false}}; break;
				default: return null;break;
				};
			}else{
				switch(element.zdlx){
				case '0' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2+"; minLength:"+element.cdxz/2,width:'80%',required:'true'}}; break;
				case '1' :e = {type:'<div/>'  ,Class:'mini-combobox',attr:{valueField:"val",id:"ysm"+i,textField:"remark",required:'true',url:"<%=root %>/param/getKeyList?key="+element.mblx,name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%',allowInput:false}}; break;
				case '2' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2,width:'80%',required:'true'}}; break;
				case '3' :e = {type:'<input/>',Class:'mini-spinner',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2,width:'80%',required:'true',maxValue:'9999999999',decimalPlaces:'2',minValue:'-9999999999'}}; break;
				case '4' :e = {type:'<input/>',Class:'mini-datepicker',attr:{name:element.ysm,id:"ysm"+i,format:'yyyy-MM-dd H:mm:ss',timeFormat:'H:mm:ss',showTime:'true',vtype:"maxLength:"+element.cdxz/2,width:'80%',required:'true',value:new Date()}};  break;
				case '5' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2,width:'80%',required:'true'}};; break;
				case '6' :e = {type:'<input/>',Class:'mini-textarea',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2,width:'80%',required:'true'}}; break;
				case '7' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2,width:'80%',readonly:"readonly"}}; break;
				case '8' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '9' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '10' :e = {type:'<input/>',Class:'mini-textbox',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '11' :e = {type:'<input/>',Class:'mini-textarea',attr:{name:element.ysm,id:"ysm"+i,vtype:"maxLength:"+element.cdxz/2,width:'80%'}}; break;
				case '12' :e = {type:'<div/>'  ,Class:'mini-combobox',attr:{valueField:"val",id:"ysm"+i,textField:"remark",required:'true',url:"<%=root %>/param/getKeyList?key="+element.mblx,name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%',allowInput:false,multiSelect:true}}; break;
				case '13' :e = {type:'<div/>'  ,Class:'mini-combobox',attr:{valueField:"val",id:"ysm"+i,textField:"remark",url:"<%=root %>/param/getKeyList?key="+element.mblx,name:element.ysm,vtype:"maxLength:"+element.cdxz/2,width:'80%',allowInput:false}}; break;
				
				default: return null;break;
				};
			}
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
			var urlStr = "<%=root %>/djbHandler/save?pageType=edit";
			var o = form.getData(); 
		    var data = mini.encode([o]);           
		    form.validate();
		   
		  //表示为编辑状态
		  	var json = mini.encode([o]);
		  	 for (var key in yzmm){  
		      if(yzmmtg[key]=="false"||yzmmtg[key]==null||yzmmtg[key]=="null"||yzmmtg[key]==""||yzmmtg[key]==undefined){
		    	alert("请验证密码");
				return;
		      }		    	
			}
			 if (form.isValid() == false)
				return;    
		  	$.ajax({
	             url: urlStr,
			     type: 'post',
	             data: { data: json},
	             cache: false,
	             success: function (text) {
	             	CloseWindow("save");
	             }
	             ,error: function (jqXHR, textStatus, errorThrown) {
	                 alert(jqXHR.responseText);
	                 CloseWindow();
	             }
	         });
		}
		
		//获取父页面传递来的json数据
		function SetData(data) {
			//克隆数据
			var infos = mini.clone(data);
			var status = infos.status;
			var pageType = infos.pageType;
			//先判断是否是不需要配置流程的登记簿，如果是，则退回原因隐藏；如果不是，再判断当前是【编辑】操作还是查看【详情】，如果是【编辑】，则隐藏原因；查看【详情】则隐藏确认
			var sfpz = infos.sfpz;
			//document.getElementById("field2").style.display="none";
			if(pageType == "edit"){
				//document.getElementById("field2").style.display="none";
				//编辑时，当信息的状态为待提交或者已退回时，可以上传附件
				if(status =="2" || status == "3"){
					mini.get("filebtn").enable();
				}else{
					mini.get("filebtn").disable();
				}
			}else{
				mini.get("onOk").hide();
				mini.get("filebtn").disable();
			}
		}

		//确定保存或更新
		function onOk(e) {
			if("${pageType}" == "detail"){
				CloseWindow("detail");
			}else{
				saveJson();
			}
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
		        jsonObj[key.toLocaleUpperCase()] = jsonObj[key];  
		    }  
		    return jsonObj;  
		} 
		 
		 function lowerJSONKey(jsonObj){
			 for (var key in jsonObj){ 
			     jsonObj[key.toLowerCase()] = jsonObj[key];  
			 }  
			 return jsonObj;  
		} 
		 
		//翻译字段
         function ondrawcell1(e){
			var status = "${status}";
         	if(e.field == "proc") { // 操作
         		var pageType = "${pageType}";
    			if(pageType == "edit"){
	         		if(status == "2" || status == "3"){
	         			e.cellStyle="text-align:center";
	         			e.cellHtml = "<a href='<%=root %>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURIComponent(encodeURIComponent(e.record.filename))+"&filePath="+encodeURIComponent(encodeURIComponent(e.record.filepath))+"'  target='_blank'>查看</a>&nbsp;<a href='javascript:profile(\"1\",\""+e.record.id+"\",\""+e.record.createdate+"\",\""+e.record.yxptid+"\")'>删除</a>";
	         		}else{
		         		e.cellStyle="text-align:center";
		     			e.cellHtml = "<a href='<%=root %>/common/receiveFileByURL?filetype="+e.record.filetype+"&fileName="+encodeURIComponent(encodeURIComponent(e.record.filename))+"&filePath="+encodeURIComponent(encodeURIComponent(e.record.filepath))+"'  target='_blank'>查看</a>";
	         		}
    			}else{
    				e.cellStyle="text-align:center";
	     			e.cellHtml = "<a href='<%=root %>/common/receiveFileByURL?filetype="+e.record.filetype+"&fileName="+encodeURIComponent(encodeURIComponent(e.record.filename))+"&filePath="+encodeURIComponent(encodeURIComponent(e.record.filepath))+"'  target='_blank'>查看</a>";
    			}
         	}
         }
		
     	//翻译字段
         function ondrawcell2(e){
         	if(e.field == "PROC") { // 操作
    			e.cellStyle="text-align:center";
	     		e.cellHtml = "<a href='<%=root %>/common/receiveFileByURL?filetype="+e.record.filetype+"&fileName="+encodeURIComponent(encodeURIComponent(e.record.filename))+"&filePath="+e.record.filepath+"'  target='_blank'>查看</a>";
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
     	 		}
     	 	});
     	}
       
	</script>
</body>
</html>
