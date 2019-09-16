<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/handsontable/handsontable.full.min.css"/>
		<script src="<%=request.getContextPath()%>/resource/handsontable/handsontable.full.min.js"></script>
		<script src="<%=request.getContextPath()%>/resource/handsontable/zh-CN.min.js"></script>
		<script src="<%=request.getContextPath()%>/resource/handsontable/xlsx.full.min.js"></script>
		<%-- <script src="<%=request.getContextPath()%>/resource/handsontable/xlsx.utils.js"></script> --%>
		<script src="<%=request.getContextPath()%>/resource/handsontable/Blob.js"></script>
		
		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
		<script>
		//如果使用 FileSaver.js 就不要同时使用以下函数
        function saveAs(obj, fileName) {//当然可以自定义简单的下载文件实现方式 
        	// for IE
            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(obj, fileName);
            }
         	// for Non-IE (chrome, firefox etc.)
            else {
                /* var a = document.createElement('a');
                document.body.appendChild(a);
                a.style = 'display: none';
                var url = window.URL.createObjectURL(csvData);
                a.href =  url;
                a.download = file_name;
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url); */
                var tmpa = document.createElement("a");
                tmpa.download = fileName || "下载";
                tmpa.href = URL.createObjectURL(obj); //绑定a标签
                tmpa.click(); //模拟点击实现下载
                setTimeout(function () { //延时释放
                    URL.revokeObjectURL(obj); //用URL.revokeObjectURL()来释放这个object URL
                }, 100);
            }
           
        }
        var wopts = { bookType: 'xlsx', bookSST: true, type: 'binary' };//这里的数据是用来定义导出的格式类型 
        function s2ab(s) {
            if (typeof ArrayBuffer !== 'undefined') {
                var buf = new ArrayBuffer(s.length);
                var view = new Uint8Array(buf);
                for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
                return buf;
            } else {
                var buf = new Array(s.length);
                for (var i = 0; i != s.length; ++i) buf[i] = s.charCodeAt(i) & 0xFF;
                return buf;
            }
        }

        var download_csv_using_blob = function (file_name, content) {
            var csvData = new Blob([content], { type: 'text/csv' });
            // for IE
            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(csvData, file_name);
            }
            // for Non-IE (chrome, firefox etc.)
            else {
                var a = document.createElement('a');
                document.body.appendChild(a);
                a.style = 'display: none';
                var url = window.URL.createObjectURL(csvData);
                a.href =  url;
                a.download = file_name;
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            }
        };
    	</script>
	</head>
	<body>
		<div class="nui-toolbar" style="padding: 2px; border-bottom: 0;">
			<table style="width: 100%;">
				<tr>
					<td style="width: 100%;">
						<input name="pageType" class="nui-hidden"/>
						<form id="form1" method="post">
				        	<input name="pid" class="nui-hidden" />
				        	<input name="model_cfg_id" class="nui-hidden" />
				        	<a class="mini-button" onclick="exportExecl" style="width: 80px;" iconcls="">导出</a>
						</form>
					</td>
				</tr>
			</table>
		</div>
    	<div class="nui-fit" id="hot_div">
			<div  style="heoverflow:auto;" >
	       		<div style="overflow:hidden;" >
					<div id="hot" ></div>
				</div>	
			</div>
		</div>  
		<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" iconCls="" onclick="onCancel">关闭</a>       
		</div>
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var form = $G.getForm("form1");
	var infos;
 	var columns_cfg = [];
 	var hot;
	function setData(data){
 		 infos = $G.clone(data);
 		//var copyflag = infos.copyflag;
		columns_cfg = $G.clone(infos.columns_cfg);
 		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if(pageType=="edit"){
		
		}else if(pageType=="add"){
			 //定义模板配置id,如果是新增页面时,需要传入配置id,以及生成模型id , qxzbzwzm , j
			 
		}
		
		$G.getbyName("model_cfg_id").setValue(infos.model_cfg_id);
		$G.getbyName("pid").setValue(infos.taskid);
		//新增模式下弹出提示询问添加的行和列数据
		initHot(infos);
 	}
 	
 	
 	
 	function exportExecl(){
 		//将表格进行转化数据格式 
 		var  idata= $G.clone(infos.data);
 		var  exportdata = [], temp = {};
 		var mergecells = mini.clone(infos.mergecells);
 		var merges = [];
 		for(var i=0,len = mergecells.length;i<len;i++){
 			merges.push({
 						s:{
 							c:	mergecells[i].col,
 							r:  mergecells[i].row
 						},
 						e:{
 							c: (parseInt(mergecells[i].col+"") + ( parseInt(mergecells[i].colspan+"")-1)),
 							r: (parseInt(mergecells[i].row+"") + ( parseInt(mergecells[i].rowspan+"")-1))
 						}
 					});		
 		}
 		
 		var wb = { SheetNames: ['Sheet1'], Sheets: {}, Props: {} };
        var data = XLSX.utils.aoa_to_sheet(idata);
        data["!merges"] = merges; 
        wb.Sheets['Sheet1'] = data;
        
        //此场景仅适用于 IE 10+ 和其他浏览器，后期可能下载放在后端进行处理下载
        
        saveAs(new Blob([s2ab(XLSX.write(wb, wopts))], { type: "application/octet-stream"}), "下载" + '.' + (wopts.bookType == "biff2" ? "xls" : wopts.bookType)); 
 		
 	}
 	//根据列配置情况，设置列的格式情况	
 	function formatColumns(columns_config){
 		var columns = []
 		for(var i = 0,len = columns_config.length;i<len;i++){
			columns[i] = {type:columns_config[i].col_type}			
 			if(columns_config[i].col_is_edit == '1'){
 				columns[i].readOnly = true;
 			}
			if(columns_config[i].col_type == 'dropdown'){
				if(isNullObj(columns_cfg[i].col_data_upcol)){
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
 	
 	//该工具类注释可参考 do_configdata_retoaction.jsp
 	var cellArrayUtil = {
			 indexs:{
				 
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
					dictid = columns_cfg[col].col_binddata;
				}
				var value_key ;
				if(!isNullObj(dictid) && !isNullObj(newVal)){
					value_key = $G.getDictVal(dictid,newVal);
				}
				var isNotFond = true;
				for(var i=0,len=columns_cfg.length;i<len;i++){
					//寻找是否有下级级联列
					if(i != col && columns_cfg[i].col_type == 'dropdown' && columns_cfg[i].col_data_upcol == col){
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
 		var hotSettings = {//<====handsontable的配置
 		        data: info.data,//数据源
 		        height: $("#hot_div").height(),
 		        stretchH: "all",     //last:延伸最后一列,all:延伸所有列,none默认不延伸。
 		        rowHeaders: true, //当值为true时显示行头，当值为数组时，行头为数组的值
 				colHeaders: true,
 		        columnSorting: true,//允许排序
 		        sortIndicator: true,
 		        fillHandle: true, ////当值为true时，允许拖动单元格右下角，将其值自动填充到选中的单元格
 				filters: true,
 				licenseKey:'non-commercial-and-evaluation',
 		        autoColumnSize: true, //当值为true且列宽未设置时，自适应列大小
 				language:'zh-CN',
 				mergeCells: false,
 				manualColumnResize: true,
 				headerTooltips:true,//工具栏启用
 				search: true,// 查询
 				manualRowResize: true,
 				autoWrapRow: true,
 				manualRowMove: true,
 				manualColumnMove: false,
 				dropdownMenu: true,
 				minSpareRows: 1,
 				beforeChange: function (changes, source) {
 					//获取当前单元格值变动情况
 					//获取列名
 					/*
 						0: 0
						1: 2
						2: undefined
						3: "图片格式串"
						
 					*/
 					
 					
					var  setting =  this.getSettings();
					var row = changes[0][0];
 					var col = changes[0][1];
 					var newVal = changes[0][3];
 					//当前列是否关联到了其他列,如果有关联自动变更下拉值
 					if(columns_cfg[col].col_type == 'dropdown'){
 						cellArrayUtil.changed(setting,row,col,newVal);
 						this.updateSettings(setting);
 					}
 				},
 				contextMenu: {
 				  callback: function (key, options) {
 					
 				  },
 				  items: {
 					"row_above":{},
 					"row_below":{},
 					"---------":{},
 					"remove_row":{},
 					"clear_column":{},
 					"undo":{},
 					"redo":{},
 					"make_read_only":{},
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
	 							if(k == mergecells[j].col)//需要进行合并默认只以第一个需要合并的的列值作为合并值
	 								headercolarr[row][k]={label:headercolarr[row][k],colspan:mergecells[j].colspan};//组合合并对象到数组里
	 						}
	 					}
	 				}
	 				for (var i = 0,len = headercolarr.length; i < len; i++) {
	 					var klen = headercolarr[i].length;
	 					nestedHeaders[i] = [];
	 					for(var k=0;k<klen;k++){//解析每一个头部字段
	 						console.log(headercolarr[i][k] +" : "+(headercolarr[i][k] != 'no-value-null'))
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
	 		 
	 		 //info.hotSettings.filters = false;
	 		 //info.hotSettings.contextMenu = false;
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
 	

	
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    } 
 	
	
	
 	    
 </script>