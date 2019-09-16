<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>任务反馈</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<%@include file="/common/nuires.jsp" %>
		<script src="<%=request.getContextPath()%>/resource/js/sPage/jquery.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/handsontable/handsontable.full.min.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resource/js/sPage/jquery.sPage.css" />
		<script src="<%=request.getContextPath()%>/resource/handsontable/handsontable.full.min.js"></script>
		<script src="<%=request.getContextPath()%>/resource/handsontable/zh-CN.min.js"></script>
		<script src="<%=request.getContextPath()%>/resource/handsontable/xlsx.full.min.js"></script>
		<script src="<%=request.getContextPath()%>/resource/handsontable/Blob.js"></script>
		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
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
		<script>
		//如果使用 FileSaver.js 就不要同时使用以下函数
        function saveAs(obj, fileName) {//当然可以自定义简单的下载文件实现方式 
        	// for IE
            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(obj, fileName);
            }
         	// for Non-IE (chrome, firefox etc.)
            else {
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
<div class="mini-toolbar" style="padding: 2px; border-bottom: 0;">
		<!-- 操作区域 -->
		<table style="width: 100%;">
			<tr>
				<td align="left" cosplan="1">任务说明：
				<input id="mission_remark" name="mission_remark" class="mini-textarea" readonly="true" style="width:99%;height:100px;" /></td>
			</tr>
			<tr>
				<td>
					<input name="id" class="nui-hidden" />
				    <input name="missionIssueId" class="nui-hidden" />
				    <input name="exportFlag" class="nui-hidden" />
				    <a class="mini-button" id="implementSituationBtn" onclick="validSave()">验证保存</a>
					<input class="mini-htmlfile" name="Fdata" id="file1" style="height: 25px; width: 200px;" />
					<a class="mini-button" onclick="impt" style="width: 80px;" iconcls="icon-ok" enabled="<%=request.getAttribute("imptEnabled") %>">导入</a>
					<a class="mini-button"  id="downBtn" onclick="exportExecl()" >下载当前显示内容</a>
					<a class="mini-button"  id="downBtn" onclick="downloadRetoaction()" >下载保存数据</a>
					<a class="mini-button" id="revokeBtn" onclick="downTeplment()">下载模板</a>
					<a class="mini-button" id="implementSituationBtn" onclick="uploadview()">查看任务附件</a>
				</td>
			</tr>
		</table>
	</div>
	<!-- 表格绘制区域 -->
	<div class="mini-fit" id="hot_div">
		<div style="overflow:auto;">
			<div style="overflow:hidden;" id="hot_p">
				<div id = "hot"></div>
			</div>
		</div>
	</div>
	<!-- 分页绘制区域 -->
	<div id="pNum" class="num"></div>
	<div id="myPage" class="demo"></div>
	<!-- 提交区域 -->
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 0px;margin-bottom:40px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="mini-button"  onclick="feedBack">提交</a> 
			<a class="mini-button"  onclick="onCancel">关闭</a>       
		</div>		
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var infos;//当前配置及值
	var mycolumns_cfg = [];//存储列配置信息
	var hot;//当前表格插件对象
	//分页配置信息
	var page = {
			pageIndex:1,//默认第一页
			pageSize:20,//每页默认20条
		}
	var delIds = []; //处理删除的列
	function setData(data){
		//去后台读取反馈模板数据
		//查询列配置信息
		$G.getbyName("missionIssueId").setValue('<%=request.getAttribute("missionIssueId") %>');//已发布表ID
		$G.getbyName("id").setValue('<%=request.getAttribute("id") %>');//待处理表ID
		//开始绘制第一页数据
		loadData(1)
	}
	
	
	
	function loadData(pageIndex){
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsAsync(false);
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	//此时其实是多了一个第0列的，该列是隐藏的，但是必须要保证列位数一致....所以在前面强制加一个配置
	    	text.columns.unshift({col_name:'DATA_CID_0',col_type:'text',col_is_edit:'1'});
	    	mycolumns_cfg = mini.clone(text.columns);
			$G.get("mission_remark").setValue(text.taskremark);
			//组装任务数据
			infos = {
				data: formatModelDataKeyStr(text.datas),//因为头部要排序，为了方便都进行了排序，一般开销
				
				//data: text.datas,
				columns_cfg: text.columns, //列配置信息
				data_start_row: text.model.data_start_row,//数据开始行
				mergecells: $G.decode(text.model.mergecells), //头合并配置
				//如果有关键字合并或者之类的需求时，可以参考官方的合并案例，进行配置化
			}	
			
			//if(hotSettings){
				initHot(infos);
			/* }else{
				hotSettings.data = infos.data;
				$("#hot_p").html('<div id = "hot"></div>');
				//hot.clear();
				
				//hot.loadData(data);
				var hotElement = document.querySelector('#hot');//<====绑定handsontable初始化div 
		 		hot = new Handsontable(hotElement, hotSettings);
			} */
			
			
			//重新绘制分页
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
	                if(pageIdx != page.pageIndex){//如果选中页码与当前页码一样时，不需要查询
	                	page.pageIndex = pageIdx; //重置页码索引，不然数据拿的不对
	                	loadData(pageIdx);	
	                }
	            	//$("#pNum").text(page);
	            }
	        });
			
	    });
		$G.postByAjax({pageIndex: pageIndex,pageSize: page.pageSize,id:'<%=request.getAttribute("id") %>'},"<%=root%>/missiondatareport/getRetoactionInfo",ajaxConf);
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
				if (datas[0].hasOwnProperty(key) && key.length >= 9) keys.push(key);
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
 	
 	//对元素进行1格式化
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

	
	//下载当前页面的数据
	function exportExecl(){
		//将表格进行转化数据格式 
		var  idata= formatModelData($G.clone(infos.data));
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
	
	//下载后台保存数据与查看时一样
	function downloadRetoaction(){
		 var createfrom = {};
		 createfrom.id = '<%=request.getAttribute("id") %>';
		 createfrom["form"]={action: "<%=root%>/missiondatareport/downloadRetoaction" }; 
		 //模拟form.submit提交下载请求
		 downFile(createfrom);
	}
	
	
	//根据列配置情况，设置列的格式情况	
	function formatColumns(columns_config){
		var columns = []
		for(var i = 0,len = columns_config.length;i<len;i++){
			//设置列类型
			columns[i] = {type:columns_config[i].col_type}			
			//设置列filed值，用于关联数据
			columns[i].data =  (columns_config[i].col_name+"").toLocaleLowerCase();
			//如果是不可编辑的话，默认设置为只读
			if(columns_config[i].col_is_edit == '1'){
				columns[i].readOnly = true;
			}
			//如果列是下拉的话，判定是不是最顶级的列，如果是，初始化下拉列表内容
			if(columns_config[i].col_type == 'dropdown'){
				if(isNullObj(columns_config[i].col_data_upcol)){
					columns[i].source = formatArrayForDictKey(columns_config[i].col_binddata)
				}
			}
			//判断当前页面是否是只读模式，如果是，强制列为 只读
			if("true" == "<%=request.getAttribute("readonly") %>"){
				columns[i].readOnly = true;
			}
			
		}
		return columns;
	}
	
	//扩展dict插件，获取字典集合并并把某一元素转换为数组
	function formatArrayForDictKey(dicttypeid){
		return formatArrayForDict($G.getDictMap(dicttypeid),"remark");
	}
	
	//格式化字典为数组
	function formatArrayForDictKeyEm(dicttypeid,em){
		return formatArrayForDict($G.getDictMap(dicttypeid),em);
	}
	
	//格式化字典为数组模式，因为下拉列表里面只支持纯数组，不支持 key : value 形式，除非自己研究扩展
	function formatArrayForDict(dict,key){
		var dict_arr = [""];
		if(dict && dict.length>0){
			for(var i=0,len = dict.length;i<len;i++){
	 			dict_arr.push(dict[i][key])
	 		}	
		}
		return dict_arr;
	}
	//单元格配置存储级联工具
	var cellArrayUtil = {
			 indexs:{//存储索引位置，如果索引列表中有了，直接取
				 
			 },
			 findColumnsIndex:function(arr,col){//在数组中找到某列的数组索引位置，如果没找到返回 null
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
			 findIndex:function(arr,row,col){//在单元格配置数组中找到某一单元格的数组索引位置，如果没找到返回 null
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
			 find:function(arr,row,col){//在单元格配置数组中找到某一单元格的配置信息，如果没找到返回 null
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
			 removeCell:function(row,col,cell){//移除单元格配置信息中某一个单元格的配置
				 cell.splice(this.findIndex(cell,row,col), 1);
			 	 return cell;
			 },
			 removeCellRow:function(row,cell){//移除单元格配置信息中该行的所有配置
				 var cell_t = [];
				 for(var i=0,len = cell.length;i<len;i++){
					 if(cell[i].row != row){
						 cell_t.push(cell[i]);
					 }
				 }
				 cell = cell_t;
				 return cell_t;
			 },
			 removeCellCol:function(col,cell){//移除单元格配置中该列的所有配置信息
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
				//找到列在数组中的索引位置通过 列集合，和行号，列号找到单元格配置信息
				var cellIndex = this.findIndex(setting.cell,row,col);
				var dictid ; //字典key
				if(cellIndex != null){ //如果列索引找到了，从现有的单元格配置中找字典的可以
					dictid = setting.cell[cellIndex].dictid;
				}else{//如果没找到，从默认的列配置中找到列中的默认字典key,没找到的话默认初始化为空
					dictid = mycolumns_cfg[col].hasOwnProperty("col_binddata")?mycolumns_cfg[col].col_binddata : "";
				}
				var value_key ;
				//通过key 和 remark 找到 子VAL 值,字典key和remark 值不能为空，不然无法定位
				if(!isNullObj(dictid) && !isNullObj(newVal)){
					value_key = $G.getDictVal(dictid,newVal);
				}
				//声明初始化 是否找到下一级标志，默认已找到
				var isNotFond = true;
				//对列进行循环，开始寻找下级关联列
				for(var i=0,len=mycolumns_cfg.length;i<len;i++){
					//寻找是否有下级级联列，必须是下拉类型的，并且与当前列关联
					if(i != col && mycolumns_cfg[i].col_type == 'dropdown' && mycolumns_cfg[i].col_data_upcol == col){
		 				//寻找该元素的是否已经存在单元格配置
						cellIndex = this.findIndex(setting.cell,row,i);
		 				//找到配置
		 				if(cellIndex != null ){
		 					//根据上级选中的子值得到的key，去获取下拉列表数据
		 					setting.cell[cellIndex].source = (isNullObj(value_key) ? [] : formatArrayForDictKey(value_key) ); 
		 		 			//重置单元格字典配置key
		 					setting.cell[cellIndex].dictid = value_key;
		 					//递归检查是否还有级联列,如果有自动变更，此时如果没有下拉值时，把当前单元格设置为空值
			 				if(setting.cell[cellIndex].source.length == 0){
			 					hot.setDataAtCell(row,i,"");
			 				}
		 					//检查当前元素是否还有下一级，默认扔一个下拉列表值
			 				this.changed(setting,row,i,setting.cell[cellIndex].source[0]);
		 				}else{
		 					//没找到，此时在列配置数组中添加该单元格的配置信息
		 					setting.cell.push({row:row,col:i,type: 'dropdown',source:[],dictid:value_key}); 
		 					//根据上级选中的子值得到的key，去获取下拉列表数据
		 					setting.cell[setting.cell.length-1].source = ( isNullObj(value_key) ? [] : formatArrayForDictKey(value_key) );
		 					//递归检查是否还有级联列,如果有自动变更，此时如果没有下拉值时，把当前单元格设置为空值
			 				if(setting.cell[setting.cell.length-1].source.length == 0){
			 					hot.setDataAtCell(row,i,"");
			 				}
			 				//检查当前元素是否还有下一级，默认扔一个下拉列表值
			 				this.changed(setting,row,i,setting.cell[setting.cell.length-1].source[0]);
		 				}
		 				//有下级,不需要跳出
		 				isNotFond = false;
					}
					
				}
				//如果没有找到下级，直接跳出
				if(isNotFond){
					return;
				}
				 
			 }
	 };
	var hotSettings
	
	//初始化绘制表格,因数据原因，每次重新绘制，开销略大
	function initHot(info){
		hotSettings = {//<====handsontable的配置
		        data: info.data,//数据源
		        height: $("#hot_div").height(),
		        stretchH: "all",     //last:延伸最后一列,all:延伸所有列,none默认不延伸。
		        rowHeaders: true, //当值为true时显示行头，当值为数组时，行头为数组的值
				colHeaders: true,//
		        columnSorting: false,//允许排序,隐藏列时不能使用排序，如果使用排序的话，会重新绘制，导致隐藏列无效
		       	sortIndicator: true,//排序开启
		        fillHandle: true, ////当值为true时，允许拖动单元格右下角，将其值自动填充到选中的单元格
				filters: true,//过滤器组件开启
				licenseKey:'non-commercial-and-evaluation',//非商用模式
		        autoColumnSize: true, //当值为true且列宽未设置时，自适应列大小
				language:'zh-CN',//语言设置为中文
				manualColumnResize: true,
				headerTooltips:true,//工具栏启用
				search: true,// 查询
				manualRowResize: true,
				autoWrapRow: true,
				manualRowMove: true,
				dropdownMenu: true,//列菜单开启
				minSpareRows: 1,//自动添加行，每次一行
				hiddenColumns: {
				    copyPasteEnabled: true,
				    indicators: true,
				    columns: [0]//隐藏第0列，用于存放ID之类的数据
				},
				beforeChange: function (changes, source) {
					//获取当前单元格值变动情况，（在单元格值变动前）
					//获取列名
					/*
						0: 0
						1: 2
						2: undefined
						3: "图片格式串"
						
					*/
					var  setting =  this.getSettings(); //获取当前配置
					var row = changes[0][0]; //行号
					var col = changes[0][1]; //列
					var newVal = changes[0][3]; //更改的值
 					
					//找到列索引坐标
 					var colIndex = cellArrayUtil.findColumnsIndex(setting.columns,col);
					//当前列是否关联到了其他列,如果有关联自动变更下拉值
					if(setting.columns[colIndex].type == 'dropdown'){
						//检查是否有相关联的列，如果有，进行级联动作
						cellArrayUtil.changed(setting,row,colIndex,newVal);
						//对配置进行更新
						this.updateSettings(setting);
					} 
				
					
				},
				beforeRemoveRow: function(index,amount,physicalRows,source){
					//行移除事件监控（在行移除前），如果移除了行，此时把行的id记录下来，后面进行删除动作
					//找到移除的行
					var row =  this.getSourceDataAtRow(index);
					//判断当前行的id 是否是空的，如果不是，检查在待删除的id数组中是否存在，不存在，添加id到数组中
					if(!isNullObj(row.data_cid_0) && $.inArray(row.data_cid_0, delIds) < 0){
						delIds.push(row.data_cid_0);	
					}
				},
				contextMenu: {
				  callback: function (key, options) {
					//处理列动作
					if (key === 'remove_row') {
						
					}
				  },
				  items: menuitems
				}
		    };
			//存贮右键菜单配置
			var menuitems = {};
			//如果是只读模式下,不允许对行做操作
			if("true" == "<%=request.getAttribute("readonly") %>"){
				menuitems = {
						"alignment":{},
						"cut":{},
						"copy":{},
				};
			}else{
				menuitems = {
						"row_above":{}, 
						"row_below":{},
						"---------":{},
						"remove_row":{},
						//"clear_column":{},
						"undo":{},
						"redo":{},
						"make_read_only":{},
						"alignment":{},
						"cut":{},
						"copy":{},
				};
			}
		 
		 	var xHred = [];
		 	var temp = []; 
		 	var topRows = info.data_start_row;//数据开始行
		 	var hotdata =  [];//存储去除头部的数据内容
		 	var idata = $G.clone(info.data);//克隆一份数据
		 	//如果没有头的情况下
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
	 			 var mergecells = mini.clone(info.mergecells);//克隆一份合并配置
	 			 var jlen = mergecells.length;//获取合并数据长度
	 			 //开始循环解析数据
	 			 /*
	 			 	nestedHeaders: [
	 				  ['A', {label: 'B', colspan: 8}, 'C'],
	 				  ['D', {label: 'E', colspan: 4}, {label: 'F', colspan: 4}, 'G'],
	 				  ['H', 'I', 'J', 'K', 'L', 'M', 'N', 'R', 'S', 'T']
	 				],
	 			 */
	 			 var headercolarr = [],isc = true;
	 			 var nestedHeaders = [] //声明初始化头部配置集合，只支持行合并
	 			 var row = 0; //声明初始化行号
	 			 var ismergecell = mergecells.length>0; //是否有合并配置
	 			 //取出不需要合并的头部数据
	 			 for (var i = 0,len = idata.length; i < len; i++) {
	 				 if(i < topRows){
	 					 headercolarr.push(idata[i]);
	 					 var klen = headercolarr[i].length;
	 					 nestedHeaders[i] = [];
	 					 for(var k=0;k<klen;k++){//解析每一个头部字段
	 						if( headercolarr[i][k] == null || headercolarr[i][k] == undefined){//需要进行合并默认只以第一个需要合并的的列值作为合并值
	 							if(ismergecell) //如果有合并配置时，对检测出来的空值做标记，后面进行清除
	 								headercolarr[i][k]='no-value-null'
	 							else
	 								headercolarr[i][k]='' //如果没有合并，初始化为 空字符串，null 不受空值
	 						}
	 					 }
	 				 }else{
	 					 //不是头，那就是数据了
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
	 				 
	 				 
					//处理头部，对指定指定合并区域，替换首元素为合并参数
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
					//去掉空值，打上标记的元素，剩余的就是头部合并参数，目前只支持行合并，未实现列合并
	 				for (var i = 0,len = headercolarr.length; i < len; i++) {
	 					var klen = headercolarr[i].length;
	 					nestedHeaders[i] = [];
	 					for(var k=0;k<klen;k++){//解析每一个头部字段
	 						if( headercolarr[i][k] != 'no-value-null'){//需要进行合并默认只以第一个需要合并的的列值作为合并值
	 							nestedHeaders[i].push(headercolarr[i][k])
	 						}
	 					}
	 				} 
	 			 }else{
	 				 //如果没有合并的头时，直接进行指向
	 				 nestedHeaders = headercolarr;
	 			 }
	 			 //设置头部集合参数
	 			 hotSettings.nestedHeaders=nestedHeaders;
	 		 }else{
	 			 //没有头
	 			 hotdata =  idata;
	 		 }
	 		 
 			 hotSettings.data = hotdata; //获得去除头部的数据内容
		 	 hotSettings.fixedRowsTop = 0;//设置锁定行为0行，也就是不锁定
	 		 
		 	 //格式化列配置，一般来讲如果需要对列的参数进行修改的，在此处进行扩展
		 	 if(info.columns_cfg){
	 			var columns_f = formatColumns(info.columns_cfg);
	 	     	hotSettings.columns = columns_f;
	 		 } 
	 		 
	 		 //每一次重新绘制时，需要清空下已绘制内容
	 		 $("#hot_p").html('<div id = "hot"></div>');
 		 	 var hotElement = document.querySelector('#hot');//<====绑定handsontable初始化div 
	 		 hot = new Handsontable(hotElement, hotSettings);
	}
	
 
    //反馈 
    function feedBack(){
    	 mini.confirm("确定反馈该任务？<br/>反馈前请先点击[验证保存]!", "提示", function(action) {
			if (action == "ok") {
				var data = {
						id:"<%=request.getAttribute("id") %>"
						
				}
				//如果当前记录是退回的，需要输入提交说明
				if("04" == "<%=request.getAttribute("status") %>"){
					 mini.prompt("请输入提交内容：", "请输入",function (action, value) {
		                if (action == "ok") {
		                    data.mission_remark = value //设置提交说明内容
		                    toTask(data);
		                } 
					 },true);
				}else{
					//常规提交，直接进行保存操作
					toTask(data)
				}
	 		}
	 	});
    }
    //提交反馈内容
    function toTask(data){
    	//验证保存
    	var urlStr = "<%=root%>/missiondatareport/retoactionTask";
		var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.flag == '1'){
	    		$G.alert("提交成功","",function(action){
	    			$G.closemodaldialog("OK");	
	   			});
	    	}else if(text.flag == '-1'){
	    		$G.alert(text.msg);
	    	}else{
	    		$G.alert("提交失败");
	    	}
		});
	    $G.postByAjax(data, urlStr, ajaxConf);
    	
    }
    
    
    function CloseWindow(action) {            
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();            
    }
    
    function onCancel(e) {
        CloseWindow("cancel");
    }   
    
    
    
    
    
	//导入
	function impt() {
		var url = "<%=root%>/missionconfig/showExcelDetail";
		ajaxFileUpload1(url, function(data1) {
			var text = $.parseJSON($(data1).html());
			var data = text.data;
			if(data && data.length>0 ){
				
				//检查列数
				if(JSONLength(data[0]) != (mycolumns_cfg.length -1 ) ){
					$G.alert("数据列与当前表单列数不一致，请检查！")
					return;
				}
				
				//检查列中的配置，每一列是否符合数据格式
				
				//data.
				
				//对列初始化一个空对象进去作为id
				for(var i= 0,len = data.length;i< len;i++){
					data[i]["data_cid_0"] = '';
				}
				//对所有的数据进行验证
				
				//需要验证列字段是否一致，如果不一致，不允许导入，且只允许无抽取，无分组数据时进行数据导入，不然影响过大会导致垃圾数据问题
				hotSettings.data = data;
				$("#hot_p").html('<div id = "hot"></div>');
				//hot.clear();
				
				//hot.loadData(data);
				var hotElement = document.querySelector('#hot');//<====绑定handsontable初始化div 
		 		hot = new Handsontable(hotElement, hotSettings);
				
				$("#myPage").hide();
				
				$G.getbyName("exportFlag").setValue('1'); //数据为导入模式，如果为导入模式时，清空掉数据库中的数据，重新添加进去
			}
		});
	}

	function ajaxFileUpload1(url, callback) {
		var inputFile = $("#file1 > input:file")[0];
		if (inputFile.value == "") {
			mini.alert("请先选择文件...")
			return;
		}
		mini.mask({
	        el: document.body,
	        cls: 'mini-mask-loading',
	        html: '上传中中...'
	    });
		
		$.ajaxFileUpload(
			{
				url : url, //用于文件上传的服务器端请求地址
				fileElementId : inputFile, //文件上传域的ID
				data: {"isAutoHeads":true,"autoHeadKey":"DATA_COL_"},            //附加的额外参数
				dataType : 'json/text', //返回值类型 一般设置为json
				success : function(data1, status) //服务器成功响应处理函数
				{
					mini.unmask(document.body);
					callback(data1);
				},
				error : function(data, status, e) //服务器响应失败处理函数
				{
					mini.unmask(document.body);
				},
				complete : function() {
					var jq = $("#file1 > input:file");
					jq.before(inputFile);
					jq.remove();
				}
			}
				);
	}
	
	//下载模板
	function downTeplment(){
		 var createfrom = {};
		 createfrom.id = $G.getbyName("id").getValue();
		 createfrom.missionIssueId = $G.getbyName("missionIssueId").getValue();
		 createfrom["form"]={action: "<%=root%>/missionForUser/downloadTaskTemplet" }; 
		 downFile(createfrom);
	}
	
	
	//验证保存
	function  validSave(){
		//常规模板数据验证
		if(!validTemplet()){
			return;
		}
		
		//模板内数据
		var sourcedata =  hot.getSourceData();
		var sourcedata_t = [];
		//对结果数据做处理，例如空行去除
		for(var i=0,len=sourcedata.length;i<len;i++){
			if(!hot.isEmptyRow(i)){
				//对元素进行补完计划，狗日的handsontable,自己添加的行，不填值不展示
				
				sourcedata_t.push(sourcedata[i])
				if(!isNullObj(sourcedata[i].data_cid_0) && $.inArray(sourcedata[i].data_cid_0, delIds) >= 0){
					//delIds.splice(delIds.indexOf(delIds.data_cid_0),1);	
				}
			}
		}
		//var olddata = 
		
		//var delIds = [];
		
		
		

		//主任务id
		var missionIssueId = $G.getbyName("missionIssueId").getValue();
		//任务id
		var id = $G.getbyName("id").getValue();
		//导入标识
		var exportFlag = $G.getbyName("exportFlag").getValue();
	
		//组装任务数据
		var data = {
			sourcedata:$G.encode(sourcedata_t),
			id:id,
			delIds:	delIds.join(','),
			missionIssueId:missionIssueId,
			exportFlag:exportFlag
		}
		
		//验证保存
    	var urlStr = "<%=root%>/missiondatareport/validSave";
		var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.flag == '1'){
	    		$G.alert("保存成功","",function(action){
	    			$G.closemodaldialog("OK");	
	   			});
	    	}else if(text.flag == '-1'){
	    		$G.alert(text.msg);
	    	}else{
	    		$G.alert("保存失败");
	    	}
		});
	    $G.postByAjax(data, urlStr, ajaxConf);				
	}
	
	function validTemplet(){
 		var isok = false;
 		var msgObj = {msg:''};
 		//获取配置信息
 		var setting =  hot.getSettings();
 		//获取锁定头
 		var fixedRowsTop = setting.fixedRowsTop;
		
		//验证数据模板中是否存在中间行为空情况
		var countRows = hot.countRows();
		for(var i = fixedRowsTop;i<countRows;i++){
			if(fixedRowsTop<i && i<(countRows-1) && !hot.isEmptyRow(i-1) && hot.isEmptyRow(i) && !hot.isEmptyRow(i+1)){
				$G.alert("数据模板中第["+(i+1)+"]行存在中间行为空情况","error");
				return isok;
			}	
		}
		//此处也可以对数据进行格式验证，目前没做，可以使用表格插件自带的验证组件进行单元格值验证
		
		return true;
 	}
	
	
	
	function uploadview(){
		var url = "<%=root%>/common/uploadview?id=<%=request.getAttribute("taskid") %>&pidtype=2";
		$G.showmodaldialog("查看", url, 600, 400, {}, function(data){
        	
	    });
	}
	
</script>