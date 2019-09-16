<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<%@include file="/common/nuires.jsp" %>
       <script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head>
<body>    
	<div class="mini-fit">
		<form id="form1" method="post" >
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="16%"/>
				       	<col width="40%"/>
				       	<col width="15%"/>
				       	<col width="40%"/>
				</colgroup>
               	<tr id="ycjs">
                	<td align="right">接收角色：</td>
					  <td align="left">    
                       		 <input  id="jsjs" name="jsjs" emptyText="请选择角色" style="width: 100%;"  class="nui-buttonedit" onvaluechanged="valueChange(this)" onbuttonclick="openJobClassSelector" vtype="rangeChar:1,250" required="true" validateOnLeave="false" />
                             <input name="jsjsmc" id="jsjsmc" class="nui-textbox" required="true" style="width: 350px;" />
                   </td>
                 </tr>
                   <tr>
                   	<td align="right">标题：</td>
                   	<td align="left">    
                       	<input name="bt" id="bt" class="nui-textbox" required="true" style="width: 350px;" />
                   	</td>
               	</tr>
               	
               	<tr id="sj" >
                   	<td align="right">发布时间：</td>
                   	<td align="left">    
                       	<input  name="fbsj" id="fbsj" class="nui-textbox"  style="width: 350px;"/>
                   	</td>
               	</tr>
               <tr id="fjkey">
				<td align="right">附件：</td>
					<td align="left">
					<input class="mini-htmlfile" name="fjdz"  id="fjdz" style="width:250px;"/>
				    <input  name="fjmc" id="fjmc" class="nui-textbox"  style="width: 250px;"/>
					</td>
					<td align="left" >
					 <a class="mini-button" iconCls="icon-download" id="download" href="javascript:checkFile()"   plain="true" >下载</a> 
					<a style="width: 103px;" class="mini-button" iconCls="icon-upload"  id="upload" onclick="ajaxFileUpload()" plain="true">上传(请点击)</a>
					</td>
				</tr>
               	<tr>
		              <td align="right">内容：</td>
                      <td align="left">     
		                  <input name="nr" id="nr" class="nui-textarea"  style="width: 550px;height: 350px"  required="true" />
		              </td>
		         </tr>
		          <tr style="display: none;">
                   	<td align="right">id：</td>
                   	<td align="left">    
                       	<input name="id" id="id" class="nui-textbox"  style="width: 350px;" />
                   	</td>
               	</tr>
               	<tr style="display: none;">
                   	<td align="right">fj：</td>
                   	<td align="left">    
                       	<input name="fj" id="fj" class="nui-textbox"  style="width: 350px;" />
                   	</td>
               	</tr>
               	<tr style="display: none;">
                   	<td align="right">jsjsbm：</td>
                   	<td align="left">    
                       	<input name="jsjsbm" id="jsjsbm" class="nui-textbox"  style="width: 350px;" />
                   	</td>
               	</tr>
			</table>
		</form>
		<input name="pageType" class="nui-hidden"/>     
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="onOk" id="qd">确定</a>  
		<a class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>       
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	 $("#sj").hide(); 
	 $("#download").hide(); 
	 $G.get("jsjs").setVisible(true);
     $G.get("jsjsmc").setVisible(false);
     $G.get("fjmc").setVisible(false);
	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
		var ly=infos.ly;
		$G.getbyName("pageType").setValue(pageType);
		if(pageType=="edit"){
			if(ly=="welcome"){
            	document.getElementById("ycjs").style.display="none";
            }
			data = $G.clone(data);
            var json = data.data;
            form.setData(json);
            var bt = $G.get("bt");
            bt.setValue(json.bt);
            var nr = $G.get("nr");
            nr.setValue(json.nr); 
            var fj = $G.get("fj");
            fj.setValue(json.fjdz); 
            var jsjsmc = $G.get("jsjsmc");
            jsjsmc.setValue(json.jsjsmc); 
            var fbsj = $G.get("fbsj");
            var sj=formatDateTime(json.fbsj);
            fbsj.setValue(sj); 
            bt.setReadOnly(true);
            nr.setReadOnly(true);
            $G.get("jsjsmc").setReadOnly(true);
            fbsj.setReadOnly(true);
            $G.get("qd").setVisible(false);
            $G.get("fjdz").setVisible(false);
            $G.get("jsjs").setVisible(false);
            $G.get("jsjsmc").setVisible(true);
            $("#sj").show(); 
            $("#upload").hide();
            $("#download").show(); 
            if(json.fjdz==null){
            	$("#fjkey").hide();
            }
            $G.get("fjmc").setVisible(true);
            var fjdzmc= json.fjdz;
            var mc=fjdzmc.split("\\");
            var fjmc=mc[mc.length-1];
            $G.get("fjmc").setValue(fjmc);
            $G.get("fjmc").setReadOnly(true);
		}
	}
	
	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/notice/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	//确定保存或更新
    function onOk(e) {
    	var form = new $G.Form("form1");
		if (form.validate()){
        	save();
		}
    }
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }  
	
	function formatDateTime(theDate) {
		var _hour = theDate.getHours();
		var _minute = theDate.getMinutes();
		var _second = theDate.getSeconds();
		var _year = theDate.getFullYear()
		var _month = theDate.getMonth();
		var _date = theDate.getDate();
		if(_hour<10){_hour="0"+_hour ;}
		if(_minute<10){_minute="0"+_minute;  }
		if(_second<10){_second="0"+_second  }
		if(_month < 9){_month = "0" + (_month+1);}
		if(_date<10){_date="0"+_date  }
		return  _year + "-" + _month + "-" + _date + " " + _hour + ":" + _minute + ":" + _second ;

		}
	
       function ajaxFileUpload() {
        var inputFile = $("#fjdz > input:file")[0];
        $.ajaxFileUpload({
            url: "<%=root%>/notice/saveFile",                 //用于文件上传的服务器端请求地址
            fileElementId: inputFile,               //文件上传域的ID
           /*  data: { a: 1, b: true },            //附加的额外参数 */
            dataType: 'json_pre',                   //返回值类型 一般设置为json
            success: function (data, status)    //服务器成功响应处理函数
            {
                var idkey=data.id;
                var id = $G.get("id");
                id.setValue(idkey); 
                $G.alert("附件上传成功！");
            },
            complete: function () {
                var jq = $("#fjdz > input:file");
                jq.before(inputFile);
                jq.remove();
            }
        });
    }
      
       function openJobClassSelector(e) {
           
			$G.showmodaldialog("请选择接收角色", "notice/jsjsSelector", 350, 300, null, jobDetailsSetter);
       }
       
       
       function jobDetailsSetter(data) { 
			$G.get("jsjs").setValue(data.jsjs);
			$G.get("jsjs").setText(data.jsjs);
			$G.get("jsjsbm").setValue(data.jsjsbm);
			      	
       }
       
      function checkFile(){
    	  var fjdz=$G.get("fj").getValue();
    	    $.ajax({type:"post",url:"checkFile", dataType:"json", data:"fjdz="+fjdz,
		       success:function(data){
		    	   if(data.cf=='yes'){
		    		   window.location.href="<%=root%>/notice/fileDown?fjdz="+fjdz;
		    	   }else{
		    		   $G.alert("目标文件已被移除或不存在！")
		    	   }
	}}) 
      } 
      
</script>
