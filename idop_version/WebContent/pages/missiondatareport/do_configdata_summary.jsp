<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>数据汇总查看</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<%@include file="/common/nuires.jsp" %>
		<script src="<%=request.getContextPath()%>/resource/js/sPage/jquery.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/handsontable/handsontable.full.min.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/js/sPage/jquery.sPage.css" />
		<script src="<%=request.getContextPath()%>/resource/handsontable/handsontable.full.min.js"></script>
		<script src="<%=request.getContextPath()%>/resource/handsontable/zh-CN.min.js"></script>
		<script src="<%=request.getContextPath()%>/resource/js/sPage/jquery.sPage.js" type="text/javascript"></script>
		<style type="text/css">
	    	.num{
	            height: 10px;
	    		text-align: center;
	    	}
	    	.demo{
	            margin-bottom: 10px;
	            text-align: center;
	        }
    	</style>
	</head>
<body>
<div class="mini-toolbar" style="padding: 2px; border-bottom: 0;">
		<table style="width: 100%;">
			<tr>
				<td>
					<input name="id" class="nui-hidden" />
				    <input name="missionIssueId" class="nui-hidden" />
				    <input name="exportFlag" class="nui-hidden" />
					<a class="mini-button" iconCls="" id="downBtn" onclick="downloadSummay()" >下载</a>
				</td>
			</tr>
		</table>
	</div>
	<div class="mini-fit" id="hot_div">
		<div style="overflow:auto;">
			<div style="overflow:hidden;">
				<div id = "hot"></div>
			</div>
		</div>
	</div>
	<div id="pNum" class="num"></div>
	<div id="myPage" class="demo"></div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 0px;margin-bottom:40px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="mini-button" iconCls="" onclick="onCancel">关闭</a>       
		</div>	
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var infos;
	var mycolumns_cfg = [];
	var hot;
	var page = {
			pageIndex:1,
			pageSize:20,
		}
	var delIds = []; //处理删除的列
	function setData(data){
		//去后台读取反馈模板数据
		//查询列配置信息
		$G.getbyName("missionIssueId").setValue('<%=request.getAttribute("missionIssueId") %>');
		$G.getbyName("id").setValue('<%=request.getAttribute("id") %>');
		loadData(1)
	}
	
	function loadData(pageIndex){
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsAsync(false);
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	// qxzbzwzm , j
			mycolumns_cfg = text.columns;
			//组装任务数据
			infos = {
				data: formatModelDataKeyStr(text.datas),
				columns_cfg: text.columns,
				data_start_row: text.model.data_start_row,
				mergecells: $G.decode(text.model.mergecells),
			}	
			initHot(infos);
			
			$("#myPage").sPage({
	            page: page.pageIndex,//当前页码，必填
	            total:text.total,//数据总条数，必填
	            pageSize:page.pageSize,//每页显示多少条数据，默认10条
	            showTotal:true,//是否显示总条数，默认关闭：false
	            totalTxt:"共{total}条",//数据总条数文字描述，{total}为占位符，默认"共{total}条"
	            noData: false,//没有数据时是否显示分页，默认false不显示，true显示第一页
	            showSkip:true,//是否显示跳页，默认关闭：false
	            showPN:true,//是否显示上下翻页，默认开启：true
	            prevPage:"上一页",//上翻页文字描述，默认“上一页”
	            nextPage:"下一页",//下翻页文字描述，默认“下一页”
	            backFun:function(pageIdx){
	            	//点击分页按钮回调函数，返回当前页码
	            	if(pageIdx != page.pageIndex){
	                	page.pageIndex = pageIdx;
	                	loadData(pageIdx);	
	                }
	            	//$("#pNum").text(page);
	            }
	        });
			
	    });
		$G.postByAjax({pageIndex: pageIndex,pageSize: page.pageSize,id:'<%=request.getAttribute("id") %>',type:'<%=request.getAttribute("type") %>'},"<%=root%>/missiondatareport/getSummaryData",ajaxConf);
	}
	
	/*将 [{}] 转为 [[]]
 	*/
 	function formatModelData(datas){
 		var minrow = 20;
		//将数据转换为数组
		var hotdatas = [],array = [],nullarray = [];
		
		//为保证元素顺序问题，按照此顺序进行排序，先抽取元素 key 对 key进行排序处理，然后根据排序好的数组，抽取数据
		var keys = [];
		if(datas.length>0){
			for (key in datas[0]) {
				if (datas[0].hasOwnProperty(key)) keys.push(key);
			}	
		}
		//对数组进行排序
		keys.sort(sortNumber)
		
		for(var i=0,len=datas.length;i<len;i++){
			array = [];
			for(var j=0,jlen=keys.length;j<jlen;j++){
				array.push(datas[i][keys[j]]);
			}
			hotdatas[i] = array;
		}
		/* if(datas.length < minrow){
			var col = datas.length == 0 ? 5 : datas[0].length;
			
			var row = [];
			for(var i = 0;i<col;i++){
				row.push("");
			}
			
			
			var len = minrow-datas.length;
			for(var i=0;i<len;i++){
				hotdatas.push(row);
			}
			
			
			//var data_def = Handsontable.helper.createEmptySpreadsheetData((minrow-datas.length),col);
			//hotdatas = hotdatas.concat(data_def);
		} */
		return hotdatas;
 	}
	
	
 	/*将 [{}] 转为 [[]]
 	*/
 	function formatModelDataKeyStr(datas){
 		var minrow = 20;
		//将数据转换为数组
		var hotdatas = [],array = [],nullarray = [];
		
		//为保证元素顺序问题，按照此顺序进行排序，先抽取元素 key 对 key进行排序处理，然后根据排序好的数组，抽取数据
		var keys = [];
		if(datas.length>0){
			for (key in datas[0]) {
				if (datas[0].hasOwnProperty(key)) keys.push(key);
			}	
		}
		//对数组进行排序
		keys.sort(sortNumber)
		
		for(var i=0,len=datas.length;i<len;i++){
			array = {};
			for(var j=0,jlen=keys.length;j<jlen;j++){
				array[keys[j]] = datas[i][keys[j]];
			}
			hotdatas[i] = array;
		}
		/* if(datas.length < minrow){
			var col = datas.length == 0 ? 5 : datas[0].length;
			
			var row = {};
			for(var j=0,jlen=keys.length;j<jlen;j++){
				row[keys[j]] = "";
			}
			var len = minrow-datas.length;
			for(var i=0;i<len;i++){
				hotdatas.push(row);
			}
			
			
			//var data_def = Handsontable.helper.createEmptySpreadsheetData((minrow-datas.length),col);
			//hotdatas = hotdatas.concat(data_def);
		} */
		return hotdatas;
 	}
 	
 	function sortNumber(a,b){
 		return parseInt(a.slice(9)) - parseInt(b.slice(9)) 	
 	}
 	
 	/*
 		计算JSON对象元素个数
 	*/
 	function JSONLength(obj) {
 		var size = 0, key;
 		for (key in obj) {
 			if (obj.hasOwnProperty(key)) size++;
 		}
 		return size;
 	};

	
	//下载后台保存数据与查看时一样
	function downloadSummay(){
		 var createfrom = {};
		 createfrom.id = '<%=request.getAttribute("id") %>';
		 createfrom.type = '<%=request.getAttribute("type") %>';
		 createfrom["form"]={action: "<%=root%>/missiondatareport/downloadSummary" }; 
		 downFile(createfrom);
	}
	
	
	//根据列配置情况，设置列的格式情况	
	function formatColumns(columns_config){
		var columns = []
		for(var i = 0,len = columns_config.length;i<len;i++){
			columns[i] = {type:columns_config[i].col_type}			
			columns[i].data =  (columns_config[i].col_name+"").toLocaleLowerCase();
			columns[i].readOnly = true;
			if(columns_config[i].col_type == 'dropdown'){
				if(isNullObj(columns_config[i].col_data_upcol)){
					columns[i].source = formatArrayForDictKey(columns_config[i].col_binddata)
				}
			}
			
		}
		return columns;
	}
	
	//扩展dict插件，获取字典集合并并把某一元素转换为数组
	function formatArrayForDictKey(dicttypeid){
		return formatArrayForDict($G.getDictMap(dicttypeid),"remark");
	}
	
	function formatArrayForDictKeyEm(dicttypeid,em){
		return formatArrayForDict($G.getDictMap(dicttypeid),em);
	}
	
	function formatArrayForDict(dict,key){
		var dict_arr = [""];
		if(dict && dict.length>0){
			for(var i=0,len = dict.length;i<len;i++){
	 			dict_arr.push(dict[i][key])
	 		}	
		}
		return dict_arr;
	}
	
	var cellArrayUtil = {
			 indexs:{
				 
			 },
			 findColumnsIndex:function(arr,col){
				//key已存在
				 if(this.indexs.hasOwnProperty(col)){
					 if(arr[this.indexs[col]].data == col ){
						 return this.indexs[col];
					 }
				 }
				 //不存在key
				 for(var i=0,len = arr.length;i<len;i++){
					 if(arr[i].data == col){
						 this.indexs[col] = i;
						 return i;
					 }
				 }
				 return null;
			 },
			 findIndex:function(arr,row,col){
				 //key已存在
				 if(this.indexs.hasOwnProperty(row+'_'+col)){
					 if(arr[this.indexs[row+'_'+col]].row == row && arr[this.indexs[row+'_'+col]].col == col){
						 return this.indexs[row+'_'+col];
					 }
				 }
				 //不存在key
				 for(var i=0,len = arr.length;i<len;i++){
					 if(arr[i].row == row && arr[i].col == col){
						 this.indexs[row+'_'+col] = i;
						 return i;
					 }
				 }
				 return null;
			 },
			 find:function(arr,row,col){
				//key已存在
				 if(this.indexs.hasOwnProperty(row+'_'+col)){
					 if(arr[this.indexs[row+'_'+col]].row == row && arr[this.indexs[row+'_'+col]].col == col){
						 return arr[this.indexs[row+'_'+col]];
					 }
				 }
				 //不存在key
				 for(var i=0,len = arr.length;i<len;i++){
					 if(arr[i].row == row && arr[i].col == col){
						 this.indexs[row+'_'+col] = i;
						 return arr[i];
					 }
				 }
				 return null;
				 
			 },
			 removeCell:function(row,col,cell){
				 cell.splice(this.findIndex(cell,row,col), 1);
			 	 return cell;
			 },
			 removeCellRow:function(row,cell){
				 var cell_t = [];
				 for(var i=0,len = cell.length;i<len;i++){
					 if(cell[i].row != row){
						 cell_t.push(cell[i]);
					 }
				 }
				 cell = cell_t;
				 return cell_t;
			 },
			 removeCellCol:function(col,cell){
				 var cell_t = [];
				 for(var i=0,len = cell.length;i<len;i++){
					 if(cell[i].col != col){
						 cell_t.push(cell[i]);
					 }
				 }
				 cell = cell_t;
				 return cell_t;
			 },
			 changed:function(setting,row,col,newVal){
				/* if(isNullObj(newVal)){
					return;
				} */
				
				var cellIndex = this.findIndex(setting.cell,row,col);
				var dictid ;
				if(cellIndex != null){
					dictid = setting.cell[cellIndex].dictid;
				}else{
					dictid = mycolumns_cfg[col].col_binddata;
				}
				var value_key ;
				if(!isNullObj(dictid) && !isNullObj(newVal)){
					console.log("开始获取dictid ： "+newVal)
					value_key = $G.getDictVal(dictid,newVal);
				}
				var isNotFond = true;
				for(var i=0,len=mycolumns_cfg.length;i<len;i++){
					//寻找是否有下级级联列
					if(i != col && mycolumns_cfg[i].col_type == 'dropdown' && mycolumns_cfg[i].col_data_upcol == col){
		 				cellIndex = this.findIndex(setting.cell,row,i);
		 				if(cellIndex != null ){
		 					setting.cell[cellIndex].source = (isNullObj(value_key) ? [] : formatArrayForDictKey(value_key) ); 
		 		 			setting.cell[cellIndex].dictid = value_key;
		 					//递归检查是否还有级联列,如果有自动变更
			 				if(setting.cell[cellIndex].source.length == 0){
			 					hot.setDataAtCell(row,i,"");
			 				}
			 				this.changed(setting,row,i,setting.cell[cellIndex].source[0]);
		 				}else{
		 					setting.cell.push({row:row,col:i,type: 'dropdown',source:[],dictid:value_key}); 
		 					setting.cell[setting.cell.length-1].source = ( isNullObj(value_key) ? [] : formatArrayForDictKey(value_key) );
		 					//递归检查是否还有级联列,如果有自动变更
			 				if(setting.cell[setting.cell.length-1].source.length == 0){
			 					hot.setDataAtCell(row,i,"");
			 				}
			 				this.changed(setting,row,i,setting.cell[setting.cell.length-1].source[0]);
		 				}
		 				isNotFond = false;
					}
					
				}
				
				if(isNotFond){
					return;
				}
				 
			 }
	 };
	
	
	function initHot(info){
		var hotSettings = {//<====handsontable的配置  qxzbzwzm , j
		        data: info.data,//数据源
		        height: $("#hot_div").height(),
		        stretchH: "all",     //last:延伸最后一列,all:延伸所有列,none默认不延伸。
		        rowHeaders: true, //当值为true时显示行头，当值为数组时，行头为数组的值
				colHeaders: true,
		        columnSorting: true,//允许排序,隐藏列时不能使用排序，如果使用排序的话，会重新绘制，导致隐藏列无效
		       	sortIndicator: true,
				filters: true,
				licenseKey:'non-commercial-and-evaluation',
		        autoColumnSize: true, //当值为true且列宽未设置时，自适应列大小
				language:'zh-CN',
				manualColumnResize: true,
				headerTooltips:true,//工具栏启用
				search: true,// 查询
				manualRowResize: true,
				autoWrapRow: true,
				dropdownMenu: true,
				/* hiddenColumns: {
				    copyPasteEnabled: true,
				    indicators: true,
				    columns: [0]
				}, */
				contextMenu: {
				  callback: function (key, options) {
					
				  },
				  items: {
						"alignment":{},
						"cut":{},
						"copy":{},
					}
				}
		    };
			 
		 	var xHred = [];
		 	var temp = [];
		 	var topRows = info.data_start_row;
		 	var hotdata =  [];
		 	var idata = $G.clone(info.data);
		 	if(topRows>0){
	 			 //解析合并单元格数据 规则：
	 			 /*
	 			 	1.合并数据最终截止行按照fixedRowsTop 计算
	 			 	2.合并列计算逻辑
	 			 	//行合并
	 			 	0:
	 			 		col: 0
	 			 		colspan: 2
	 			 		removed: false
	 			 		row: 0
	 			 		rowspan: 1
	 			 	//多行多列合并情况
	 			 	 0:
	 		 			col: 0
	 		 			colspan: 2
	 		 			removed: false
	 		 			row: 0
	 		 			rowspan: 2
	 			 */
	 			 //对合并数据进行解析
	 			 //先克隆一份数据
	 			 var mergecells = mini.clone(info.mergecells);
	 			 var jlen = mergecells.length;
	 			 //开始循环解析数据
	 			 /*
	 			 	nestedHeaders: [
	 				  ['A', {label: 'B', colspan: 8}, 'C'],
	 				  ['D', {label: 'E', colspan: 4}, {label: 'F', colspan: 4}, 'G'],
	 				  ['H', 'I', 'J', 'K', 'L', 'M', 'N', 'R', 'S', 'T']
	 				],
	 			 */
	 			 var headercolarr = [],isc = true;
	 			 var nestedHeaders = []
	 			 var row = 0;
	 			 var ismergecell = mergecells.length>0;
	 			 //取出不需要合并的头部数据
	 			 for (var i = 0,len = idata.length; i < len; i++) {
	 				 if(i < topRows){
	 					 headercolarr.push(idata[i]);
	 					 var klen = headercolarr[i].length;
	 					 nestedHeaders[i] = [];
	 					 for(var k=0;k<klen;k++){//解析每一个头部字段
	 						if( headercolarr[i][k] == null || headercolarr[i][k] == undefined){//需要进行合并默认只以第一个需要合并的的列值作为合并值
	 							if(ismergecell)
	 								headercolarr[i][k]='no-value-null'
	 							else
	 								headercolarr[i][k]=''
	 						}
	 					 }
	 				 }else{
	 					 hotdata.push(idata[i])
	 				 }
	 			 }
	 			 //如果存在需要合并的数据
	 			 if(mergecells.length>0){
	 				 
	 				 //先对合并顺序进行排序
	 				 //1，行,2.列做数据排序
	 				
	 				 mergecells.sort(function (a, b) {
	 					    return  a.col - b.col;
	 					}).sort(function (a, b) {
	 					    return  a.row - b.row
	 					});
	 				 
	 				 
	
	 				for (var j=0;j<jlen;j++ ){
	 					//解析每行
	 					mergecells[j].col //开始列
	 					mergecells[j].colspan //合并单元格
	 					row = mergecells[j].row //数据合并行
	 					//nestedHeaders[row] = [] //初始化一个空数组
	 					if(headercolarr[row]){ //合并行必须在头部数据范围
	 						for(var k=0,klen = headercolarr[0].length;k<klen;k++){//解析每一个头部字段
	 							if(k == (mergecells[j].col+1))//需要进行合并默认只以第一个需要合并的的列值作为合并值
	 								headercolarr[row][k]={label:headercolarr[row][k],colspan:mergecells[j].colspan};//组合合并对象到数组里
	 						}
	 					}
	 				}
	 				for (var i = 0,len = headercolarr.length; i < len; i++) {
	 					var klen = headercolarr[i].length;
	 					nestedHeaders[i] = [];
	 					for(var k=0;k<klen;k++){//解析每一个头部字段
	 						if( headercolarr[i][k] != 'no-value-null'){//需要进行合并默认只以第一个需要合并的的列值作为合并值
	 							nestedHeaders[i].push(headercolarr[i][k])
	 						}
	 					}
	 				} 
	 				//nestedHeaders = headercolarr;
	 			 }else{
	 				 nestedHeaders = headercolarr;
	 			 }
	 			 hotSettings.nestedHeaders=nestedHeaders;
	 		 }else{
	 			 hotdata =  idata;
	 		 }
	 		 hotSettings.data = hotdata;
	 		 hotSettings.fixedRowsTop = 0;
	 		 if(info.columns_cfg){
	 			var columns_f = formatColumns(info.columns_cfg);
	 	     	hotSettings.columns = columns_f;
	 		 } 
	 		 //hotSettings.mergeCells = info.mergecells
	 		 //----handsontable 初始化 开始------
 		 var hotElement = document.querySelector('#hot');//<====绑定handsontable初始化div 
	 		 hot = new Handsontable(hotElement, hotSettings);
	}
	

    
    
    function CloseWindow(action) {            
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();            
    }
    
    function onCancel(e) {
        CloseWindow("cancel");
    }   
    

	
</script>