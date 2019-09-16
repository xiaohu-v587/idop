<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>预警阀值修改</title>
	<style type="text/css">
		html,body {
			margin: 0;
			padding: 0;
			border: 0;
			width: 100%;
			height: 100%;
			overflow: hidden;
		}
	</style>
</head>
<body>
    <div class="mini-fit">
    	<input name="pageType" class="mini-hidden" />
        <form id="dataform1" method="post">
        	<div style="padding-bottom: 20px; padding-top: 20px;">
         		<input class="mini-hidden"  name="id"  id="id"/>
          		<table style="table-layout: fixed;" align="center" >
            		<tr>
              			<td class="form_label">业务模块:</td>
              			<td >
                			<input class="mini-textbox" id="moduletype" name="moduletype" width="200px" enabled="false"></input>
            			</td>
              			<td class="form_label">指标层级编号:</td>
              			<td >
                			<input class="mini-textbox" name="targettypeno" id="targettypeno" width="200px" enabled="false"></input>
            			</td>
        			</tr>
        			<tr>
              			<td class="form_label">指标类型:</td>
              			<td >
                			<input class="mini-textbox" id="warningtype" name="warningtype" width="200px" enabled="false"></input>
            			</td>
              			<td class="form_label">指标编号:</td>
              			<td >
                			<input class="mini-textbox" name="targetno" id="targetno" width="200px" enabled="false"></input>
            			</td>
        			</tr>
        			<tr>
              			<td class="form_label">阀值:</td>
              			<td >
                			<input class="mini-textbox" width="200px" id="val" name="val" selectOnFocus="true"></input>
            			</td>
              			<td class="form_label">阀值1:</td>
              			<td >
                			<input class="mini-textbox" name="val1" id="val1" width="200px"></input>
            			</td>
        			</tr>
        			<tr>
              			<td class="form_label">阀值2:</td>
              			<td >
                			<input class="mini-textbox" width="200px" id="val2" name="val2" ></input>
            			</td>
              			<td class="form_label">阀值3:</td>
              			<td >
                			<input class="mini-textbox" name="val3" id="val3" width="200px"></input>
            			</td>
        			</tr>
        			<tr>
              			<td class="form_label">生效日期:</td>
              			<td colspan="3">
                			<input class="mini-textbox" id="effdate" name="effdate" enabled="false" width="100%"></input>
            			</td>
        			</tr>
				</table>
			</div>
		</form>
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnOk" class="mini-button" onclick="saveJson()" style="margin-right: 20px;">确定</a> 
		<a id="btnCancle" class="mini-button"  onclick="onCancel()">取消</a>
	</div>
	<script type="text/javascript">
	  mini.parse();
	  var form = new mini.Form("dataform1");
	  
	  //保存或修改数据
	  function saveJson() {
	    //保存
	    var urlStr = "";
	    var pageType = mini.getbyName("pageType").getValue();//获取当前页面是编辑还是新增状态
	    //表示为编辑状态
	    if (pageType == "edit") {
	      urlStr = "<%=root%>/warningthreshold/update";
	    }
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    $G.submitForm("dataform1", urlStr, ajaxConf);
	  }
	  
	  
	  //获取父页面传递来的json数据
	  function setData(data) {
	    //跨页面传递的数据对象，克隆后才可以安全使用
	    var infos = mini.clone(data);
	    //保存list页面传递过来的页面类型：add表示新增、edit表示编辑
	    mini.getbyName("pageType").setValue(infos.action);
	    //如果是点击编辑类型页面
	    if (infos.action == "edit") {
	      //编辑页面根据主键加载业务信息
		    var moduletype = infos.moduletype;
	     	var targettypeno = infos.targettypeno;
	     	var targetno = infos.targetno;
	     	var effdate = infos.effdate;
			$.ajax({
				url: "<%=root%>/warningthreshold/getThresholdByKeys",
				data:{moduletype: moduletype, targettypeno: targettypeno, targetno: targetno, effdate: mini.formatDate(effdate, 'yyyyMMdd')},
				cache: false,
				success: function (text) {
					var formd = mini.decode(text).data;
					formd.effdate = formd.effdate.substring(0, 4) + "-" + formd.effdate.substring(4, 6) + "-" + formd.effdate.substring(6, 8);
					form.setData(formd);
					mini.get('val').focus();
				}
			});
	    }
	  }
	  
	  //关闭窗口
	  function CloseWindow(action) {
			if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
			else window.close();
	  }
	
	  //取消
	  function onCancel() {
			CloseWindow("cancel");
	  }
	  
	</script>
</body>
</html>