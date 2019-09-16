<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@include file="/common/nuires.jsp" %>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务反馈</title>
</head>
<body>
<div class="mini-toolbar" style="padding: 2px; border-bottom: 0;">
		<table style="width: 100%;">
			<tr>
				<td align="left" cosplan="1">任务说明：
				<input id="mission_remark" name="mission_remark" class="mini-textarea" style="width:80%;height:100px;" /></td>
			</tr>
			<tr>
				<td>
					<a class="mini-button" id="searchBtn" onclick="addRows()">新增行</a>
					<input class="mini-htmlfile" name="Fdata" id="file1" style="height: 25px; width: 200px;" />
					<a class="mini-button" onclick="impt" style="width: 80px;">导入</a>
					<a class="mini-button" id="downBtn" onclick="download()" >下载</a>
					<a class="mini-button" id="revokeBtn" onclick="revokeTask()">下载模板</a>
					<a class="mini-button" id="implementSituationBtn" onclick="taskImplemSituation()">查看任务附件</a>
					<a class="mini-button" id="implementSituationBtn" onclick="feedBack()">点击反馈</a>
				</td>
				
			</tr>
		</table>
	</div>
	<div class="mini-fit" id=""hot_div>
		<div style="overflow:auto;">
			<div style="overflow:hidden;">
				<div id = "hot"></div>
			</div>
		</div>
	</div>
	
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="mini-button" onclick="save">确定</a> 
			<a class="mini-button" onclick="onCancel">关闭</a>       
		</div>	
</body>
</html>
<script>
mini.parse();

var hotElement = document.querySelector('#hot');//<====绑定handsontable初始化div 
var cols = [];//<====handsontable列头显示值对应的实际字段与其他配置
var hotSettings = {//<====handsontable的配置
		data: createEmptyData(),//数据源
        autoWrapRow: true,
        minRows: 1,
        stretchH: "all",     //last:延伸最后一列,all:延伸所有列,none默认不延伸。
        rowHeaders: true, //当值为true时显示行头，当值为数组时，行头为数组的值
		colHeaders: true,
        columnSorting: true, //允许排序
        sortIndicator: true,
	    filters: true,
	    licenseKey:'non-commercial-and-evaluation',
        autoColumnSize: true, //当值为true且列宽未设置时，自适应列大小
	    language:'zh-CN',
	    mergeCells: true,
	    contextMenu: {
	      callback: function (key, options) {
		    if (key === 'about') {
		    setTimeout(function () {
				alert("This is a context menu with default and custom options mixed");
		 	  }, 100);
			}
	 	 },
		  items: {
			"row_above": {
			  disabled: function () {
				return hot.getSelected()[0] === 0;
			  }
			},
			"row_below": {},
			"hsep1": "---------",
			"remove_row": {
			  name: 'Remove this row, ok?',
			  disabled: function () {
				// if first row, disable this option
				return hot.getSelected()[0] === 0
			  }
			},
			"hsep2": "---------",
			"about": {name: 'About this menu'},
			"duohangquchukongge":{
				name:'多行去除空格',
				callback:function(key,selected,clickEvent){
					var start = selected[0].start.row;
					var end = selected[0].end.row;
					var cols = selected[0].start.col;
					var colsData = hot.getDataAtCol(cols);
					var sum = 0;
					function getDevareSpace(str){
						var str = str.replace(/,/gi,",");
						var arr = "";
						var list = str.split(",");
						for(var i = 0,len = list.length; i<len;i++ ){
							arr+= list[i].replace(/\s*/g,"")+",";
						}
						return arr.slice(0,arr.length -1);
					}
					
					for(var i = start ;i< end;i++){
						if(typeof colsData[i] == "string"){
							hot.setDataAtCell(i,cols,getDevareSpace(colsData[i]));
							}
						}
					}
			},
			"suodinghang":{
				name:'锁定行',
				hidden: function () { // `hidden` can be a boolean or a function
				  return  (this.getSelectedLast()[2] != 0 && this.getSelectedLast()[2] <= hotSettings.fixedRowsTop) ? true : false
				},
				callback:function(key,selected,clickEvent){
					var start = selected[0].start.row;
					hotSettings.fixedRowsTop = start;
					hot.updateSettings(hotSettings);
				}
			},
			"jiesuohang":{
				name:'解除锁定行',
				hidden: function () { // `hidden` can be a boolean or a function
				  return  (hotSettings.fixedRowsTop != 0 && this.getSelectedLast()[2] <= hotSettings.fixedRowsTop) ? false : true
				  //return this.getSelectedLast()[1] == hotSettings.fixedRowsTop ; // `this` === hot3
				},
				callback:function(key,selected,clickEvent){
					hotSettings.fixedRowsTop = 0;
					hot.updateSettings(hotSettings);
				}
			},
			"row_above":{},
			"row_below":{},
			"col_left":{},
			"col_right":{},
			"---------":{},
			"remove_row":{},
			"remove_col":{},
			"clear_column":{},
			"undo":{},
			"redo":{},
			"make_read_only":{},
			"alignment":{},
			"cut":{},
			"copy":{},
			"mergeCells":{},
			
		  }
		},
		dropdownMenu: [
		'remove_col',
		'---------',
		'make_read_only',
		'---------',
		'alignment',
		'duohangquchukongge'
	  ]
	      };
    var hot = new Handsontable(hotElement, hotSettings);
    
    
    
    function createEmptyData() {
        var tArray = [];
        var k;
        var j;
        for (k = 0; k < 10; k++) {
          tArray[k] = [];
          for (j = 0; j < 15; j++) {
            tArray[k][j] = null;
          }
        }
        return tArray;
      }    
    
    //反馈 
    // type应该是传值传过来的，此处先写死 
    function feedBack(){
    	mini.confirm("确定反馈该任务？", "提示", function(action) {
			if (action == "ok") {
				$.ajax({
					url:"<%=root%>/missionForUser/retoactionTsak?",
					type:"post",
					data:{id:id,type:0},
					success : function(text) {
						var flag = text;
						if(flag == 1000){
							mini.alert("无此权限!");
						}else if(flag == 1){
							mini.alert("反馈成功");
						}else{
		                		mini.alert("反馈失败!");
		                	}
					}
	 		  });
	 		}
	 	});
    }
    
    
    
    function CloseWindow(action) {            
     	
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();            
    }
    
    function onCancel(e) {
        CloseWindow("cancel");
    }   
</script>