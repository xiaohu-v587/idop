<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp" %>
<html>
<head>
    <title>定时任务管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <%@ include file="/common/nuires.jsp"%>
</head>
<body>
<div class="nui-fit">
	 <div id="horizontalSplitter" class="nui-splitter" handlerSize="1px"
		style="width: 100%; height: 100%; " borderStyle="border:0;">
		<div size="100%" showCollapseButton="false" style="border: 0px">
		    <form id="form1" method="post">
		        <div style="padding-left:10px;padding-right:10px;padding-bottom:5px;">
		            <table style="table-layout:fixed">
		            	<colgroup>
									        	<col width="25%"/>
										       	<col width="75%"/>
										        </colgroup>
		            	<tr>
		            		<td colspan="2">
		            			<input name="dsrwzj" id="dsrwzj" class="nui-hidden"/>
		            			 <input id="id" name="id" class=" nui-hidden" style="width: 100%;" vtype="rangeChar:1,200" required="true"/>
		            			<input class="nui-hidden" id="pageType" name="pageType" value="0" />   
		            		</td>
		            	</tr>
		                 <tr>
		                 	<td style="width: 85px">任务类名</td>
		                    <td style="width:320px">    
		                        <input  id="rwlm" name="rwlm"  style="width: 100%;background-color: #EDEDED"  class="nui-buttonedit" onvaluechanged="valueChange(this)" onbuttonclick="openJobClassSelector" vtype="rangeChar:1,250" required="true" validateOnLeave="false" />
	                    	    
		                    </td>
		                </tr>
		                <tr>
		                    <td>任务编码</td>
		                    <td>    
		                        <input id="rwbm" name="rwbm" class="nui-textbox" style="width: 100%;background-color: #EDEDED" vtype="rangeChar:1,200" required="true"/>
		                    </td>
		                </tr>
		                 <tr>
		                  	 <td>任务名称</td>
		                    <td >    
		                        <input id="rwmc"  name="rwmc" style="width: 100%;" class="nui-textbox" vtype="rangeChar:1,50" required="true"/>
		                    </td>
		                </tr>		                
		                 <tr>
		                 	<td>执行频度</td>
		                    <td >    
		                        <input  id="rwqdsj" name="rwqdsj" style="width: 100%;"  class="nui-buttonedit" onvaluechanged="valueChange(this)" vtype="rangeChar:1,120" onbuttonclick="checkIfCanOpenWizard" required="true" validateOnLeave="false" allowInput="false"/> 
		                    </td>
		                </tr>	
		                <tr>
		                  	<td>是否监控</td>
		                    <td >    
		                        
		                        <select name="sfjk" class="nui-radiobuttonlist" onvaluechanged="valueChange(this)" vtype="rangeChar:1,50">
	                        		<option value="0" selected="selected">是</option>
	                        		<option value="1" >否</option>
	                        	</select>
		                    </td>
		                </tr>	                
		                 <tr>
		                    <td >备注</td>
		                    <td >    
		                        <input name="bz" class="nui-textarea" onvaluechanged="valueChange(this)" style="width: 100%;height: 120px" vtype="rangeChar:1,255" />
		                        <input name="rwzt" class="nui-textbox" value="0" visible="false"/>
		                    </td>
		                </tr>
		                
		            </table>
		        </div>
		       </form>
       		</div>
       		
			</div>
        </div>  
       	<div class="nui-toolbar" style="text-align:center;padding-top:10px;padding-bottom:10px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
 		<a class="nui-button" id="cancelBtn" iconCls="icon-close" onclick="onCancel" style="margin-right:20px;">取消</a>
 		<a class="nui-button" id="okBtn" iconCls="icon-ok" onclick="onOk" enabled="false">确定</a>
	</div>
           
    
    
    <script type="text/javascript">
		changeButtonPosition($('.nui-toolbar:last'));
        $G.parse();
        var form = new $G.Form("form1");//将普通form转为nui的form
		var ORIGIN_JSON;
		
		function ctrlOkBtn(){
			$this = $(this);
        	var newData = form.getData();
        	var name = $this.attr("name");
        	newData[name] = $this.val();
        	var newJson = $G.encode(newData);
        	_isDirty(newJson);
		}
        $("#form1 :input.mini-textbox-input").keyup(ctrlOkBtn);
        $("#form1 :input.mini-textbox-input").change(ctrlOkBtn);

        function valueChange(e){
            _isDirty($G.encode(form.getData()));
        }
        
        function _isDirty(newJson){
        	if( $G.encode(ORIGIN_JSON) == newJson){
        		$G.get("okBtn").disable();
        	}else{
        		$G.get("okBtn").enable();
        	}
        }
        
        function checkIfCanOpenWizard(e) {
            if ($G.get("rwbm").getValue()) {
            	openCronTabWizard(this);
            }else {
				$G.cap4jAlert(message.schedule.taskCode);
            }
        }

        function openJobClassSelector(e) {
        
			$G.showmodaldialog(message.schedule.taskClass, "schedule/jobSelector", 650, 400, null, jobDetailsSetter);
        }

        function jobDetailsSetter(data) {           
			$G.get("rwlm").setValue(data.rwlm);
			$G.get("rwlm").setText(data.rwlm);
			$G.get("rwlm")._OnValueChanged();
			$G.get("rwbm").setValue(data.rwbm);
			$G.get("rwmc").setValue(data.rwmc); 
			      	
        }
        
        function openCronTabWizard(btnObject) {
        	var rwq=$G.get("rwqdsj").value;
            var url = "schedule/showCronTabWizard";
            var bizParams = {rwqdsj:rwq};
            $G.showmodaldialog(message.schedule.settingTitle, url, 750, 500, bizParams, function(data){
                    //获取选中、编辑的结果
                    var rwqdsj = $G.get("rwqdsj");
                    rwqdsj.setValue(data);
                    rwqdsj.setText(data);
                    rwqdsj._OnValueChanged();
                    
		    });
        }  
        

        //json方式保存数据
        function saveJson(){
        	//保存
            var urlStr = "schedule/save";
            var pageType = $G.get("pageType").getValue();//获取当前页面是编辑还是新增状态
            if(pageType=="0"){
            	var rwlm=$G.get("rwlm").value;
            	 $.ajax({type:"post",url:"checkrwlm", dataType:"json", data:"rwlm="+rwlm,
				       success:function(data){
				    	   if(data.count==0){
				    		   $G.alert("该定时任务已存在");
				    		   return false;
				    	   }
				       }})
            }
            //表示为编辑状态
            if(pageType=="1"){
            	urlStr = "schedule/update";
            }
        
	        //提交表单数据
			var ajaxConf = new GcdsAjaxConf();
		    ajaxConf.setSuccessFunc(function (){
				$G.closemodaldialog("ok");
			});
		    $G.submitForm("form1", urlStr, ajaxConf);
        }
        
        //页面间传输json数据
        function setData(data){
        	if (data.action == "1") {
                data = $G.clone(data);
                var json = data.data;
                form.setData(json);
                var rwqdsjTxt = $G.get("rwqdsj");
                rwqdsjTxt.setValue(json.rwqdsj);
                rwqdsjTxt.setText(json.rwqdsj);
                var rwlmTxt = $G.get("rwlm");
                rwlmTxt.setValue(json.rwlm);
                rwlmTxt.setText(json.rwlm);
                $G.get("pageType").setValue(data.action);
                $G.get("rwbm").setReadOnly(true);
                $G.get("rwlm").setReadOnly(true);
            }else{
            	$G.get("rwbm").setReadOnly(true);
                $G.get("rwlm").setReadOnly(false);
            }
        	
            ORIGIN_JSON = form.getData();
        }

		//获取form中的所有控件数据
        function getData() {
            return form.getData();
        }
        
        //确定保存或更新
        function onOk(e) {
        	var form = new $G.Form("form1");
			if (form.validate()){
            	saveJson();
			}
        }
        
        function onCancel(e) {
            $G.closemodaldialog("cancel");
        }

      
    </script>
</body>
</html>
