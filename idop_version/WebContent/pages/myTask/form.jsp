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
		    <input name="taskid" id="taskid" class="nui-hidden"/> 
		    <input name="processinstanceid" id="processinstanceid" class="nui-hidden"/> 
		        <div style="padding-left:10px;padding-right:10px;padding-bottom:5px;">
		            <table style="table-layout:fixed">
		            	<colgroup>
							<col width="25%"/>
							<col width="75%"/>
						 </colgroup>
		                <tr>
		                  	<td>是否同意</td>
		                    <td >    
		                        
		                        <select name="sfty" class="nui-radiobuttonlist" onvaluechanged="valueChange(this)" vtype="rangeChar:1,50">
	                        		<option value="0" selected="selected">同意</option>
	                        		<option value="1" >拒绝</option>
	                        	</select>
		                    </td>
		                </tr>	                
		                 <tr>
		                    <td >驳回/同意 意见</td>
		                    <td >    
		                        <input name="yj" class="nui-textarea"  style="width: 100%;height: 170px"  />
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
 		<a class="nui-button" id="okBtn" iconCls="icon-ok" onclick="saveJson()" >确定</a>
	</div>
	
    <script type="text/javascript">
        $G.parse();
        var form = new $G.Form("form1");//将普通form转为nui的form
        
        function onCancel(e) {
            $G.closemodaldialog("cancel");
        }

        
        function saveJson(){
        	 var urlStr="myTask/spyj";
        	 var ajaxConf = new GcdsAjaxConf();
     	    ajaxConf.setSuccessFunc(function (){
     			$G.closemodaldialog("ok");
     		});
        	 $G.submitForm("form1", urlStr,ajaxConf);
        }
        
        function setData(data){
    		var infos = $G.clone(data);
    			    var json = data.data;
    	            form.setData(json);
    	            $G.get("taskid").setValue(json.id);
    		}
      
    </script>
</body>
</html>
