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
		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
	</head>
	<body>
		<!-- 控制区域 -->
		<div class="nui-toolbar" style="padding: 2px; border-bottom: 0;">
			<table style="width: 100%;">
				<tr>
					<td style="width: 100%;">
						<input name="pageType" class="nui-hidden"/>
						<form id="form1" method="post">
				        	<input name="pid" class="nui-hidden" />
				        	<input name="model_cfg_id" class="nui-hidden" />
				        	选择文件： <input class="mini-htmlfile" name="Fdata" id="file1" style="height: 25px; width: 200px;" />
							<a class="mini-button" onclick="impt"  iconcls="">导入</a>
							<a class="mini-button" onclick="preview" iconcls="">预览</a>
							<a class="mini-button" onclick="showtxt" iconcls="">表格行列设置</a>
						</form>
					</td>
				</tr>
			</table>
		</div>
		<!-- 表格绘制区域 -->
    	<div class="nui-fit" id="hot_div">
			<div  style="heoverflow:auto;" >
	       		<div style="overflow:hidden;" >
					<div id="hot" ></div>
				</div>	
			</div>
		</div>  
		<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" iconCls="" onclick="save">确定</a> 
			<a class="nui-button" iconCls="" onclick="onCancel">关闭</a>       
		</div>
		<!-- 设置行列区域，可根据需要自行扩展 -->
		<div id="div_row_col" style="width:300px; height: 250; top: 120px; left: 35%; border: 2px solid #629ED9; position: absolute; background-color: gray; display: none;">
            <div style="margin-top: 15px; margin-bottom: 15px; margin-left: 10px;">
                <font color="#fff">请输入创建的表格行和列数</font>
            </div>
            <div style="text-align: center;">
                <table style="table-layout:fixed;" align="center">
						<colgroup>
					       	<col width="15%"/>
					       	<col width="85%"/>
						</colgroup>
						<tr>
							<td align="right">行数：</td>
							<td align="left">
		                   		<input name="init_row" class="nui-textbox" maxlength="10" vtype="range:0,100000;int"  emptyText="请输入行" style="width:100%"/>                   
		                  	</td>
		              	</tr>
		              	<tr>
		                  	<td align="right">列数：</td>
		                  	<td align="left" style="white-space: nowrap;">
		                    	<input name="init_col" class="nui-textbox" maxlength="10" vtype="range:0,2000;int" emptyText="请输入列" style="width:100%"/>      
		                  	</td>
		              	</tr>
		         </table>
            </div>
            <div style="text-align: center;margin-top:10px;margin-bottom:10px;">
                <input class="nui-button" Text="确认" onclick="resetRowCol" />
                <input class="nui-button" Text="关闭" onclick="hidtxt" />
            </div>
        </div>  
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var form = $G.getForm("form1");
	//初始化设置
 	function setData(data){
 		var infos = $G.clone(data);
 		var copyflag = infos.copyflag; //复制标志
		var pageType=infos.pageType; //当前操作类型 add / edit
		$G.getbyName("pageType").setValue(pageType);
		if(pageType=="edit"){
			 //查询当前表默认配置
			 //查询当前表内数据
			 //查询列配置信息
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsAsync(false);
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				var model = text.model;
				var datas = text.datas;
				//重新绘制页面
				hotSettings.fixedRowsTop = model.data_start_row;
				hotSettings.mergeCells = $G.decode(model.mergecells);
				hotSettings.data = formatModelData(datas);
				columns = text.columns;
				hot.updateSettings(hotSettings);
		    });
			$G.postByAjax({model_cfg_id:'<%=request.getAttribute("id") %>'},"<%=root%>/missiondatareport/getModelConfigInfo",ajaxConf);
			 
		}else if(pageType=="add"){
			 //定义模板配置id,如果是新增页面时,需要传入配置id,以及生成模型id
			 //新增模式下弹出提示询问添加的行和列数据
			 if(copyflag && copyflag == 1){
					var ajaxConf = new GcdsAjaxConf();
					ajaxConf.setIsAsync(false);
					ajaxConf.setIsShowProcessBar(true);
					ajaxConf.setIsShowSuccMsg(false);
				    ajaxConf.setSuccessFunc(function (text){
						var model = text.model;
						var datas = text.datas;
						//重新绘制页面
						hotSettings.fixedRowsTop = model.data_start_row;
						hotSettings.mergeCells = $G.decode(model.mergecells);
						hotSettings.data = formatModelData(datas);
						columns = text.columns;
						hot.updateSettings(hotSettings);
				    });
					$G.postByAjax({model_cfg_id:infos.id},"<%=root%>/missiondatareport/getModelConfigInfo",ajaxConf);
		 	 }else{
		 		showtxt();
		 	 }
		}
		//存放中转参数
		$G.getbyName("model_cfg_id").setValue('<%=request.getAttribute("id") %>');
		$G.getbyName("pid").setValue('<%=request.getAttribute("pid") %>');
 	}
 	/*
 		将 [{}] 转为 [[]]
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
		//如果初始行数据太少，默认补充一部分
		if(datas.length < minrow){
			var col = datas.length == 0 ? 5 : datas[0].length;
			var data_def = Handsontable.helper.createEmptySpreadsheetData((minrow-datas.length),col);
			hotdatas = hotdatas.concat(data_def);
		}
		return hotdatas;
 	}
 	//排序算法
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

 	/*
	 *保存数据
	 */
	function save(){
		
		//常规模板数据验证
		if(!validTemplet()){
			return;
		}
		
		//获取配置信息
 		var setting =  hot.getSettings();
 		
		//获取到模板内容
		
		//模板内数据
		var sourcedata =  hot.getSourceData();
		
		var sourcedata_t = [];
		//对结果数据做处理，例如空行去除
		for(var i=0,len=sourcedata.length;i<len;i++){
			if(!hot.isEmptyRow(i)){
				sourcedata_t.push(sourcedata[i])
			}
		}
		
		//模板头部参数
		var columns_cfg = columns; 
		
		//模板数据开始行
		var data_start_row = setting.fixedRowsTop;
		
		//模板头部合并参数
		var mergecells = hot.getPlugin('MergeCells').mergedCellsCollection.mergedCells; //该参数必须在锁定行后才能读取，因为锁定行时会把单元格解除合并
		
		//模板id
		var model_cfg_id = $G.getbyName("model_cfg_id").getValue();
		//任务配置id
		var pid = $G.getbyName("pid").getValue();
		
		var row = hot.getDataAtRow(data_start_row-1);
		
		//组装任务数据
		var data = {
			sourcedata:$G.encode(sourcedata_t), //数据内容
			columnscfg:$G.encode(columns_cfg), //列配置
			data_start_row:data_start_row,	//数据开始行
			mergecells:$G.encode(mergecells), //合并配置数据,目前只做到的了头部简单行合并，如果需要表单合并功能之类的，可能需要自己扩展
			taskid:pid, //任务配置主表id
			model_cfg_id:model_cfg_id, //模板id
			collen:row.length//列数
		}
		
		//提交	
 		var pageType=$G.getbyName("pageType").getValue();
    	var urlStr = "<%=root%>/missiondatareport/save";
		if(pageType=="edit"){
			urlStr = "<%=root%>/missiondatareport/update";
		}
	    
		var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text == '1'){
	    		$G.alert("保存成功","",function(action){
	    			$G.closemodaldialog("OK");	
	   			});
	    	}else if(text == '-1'){
	    		$G.alert("表字段最大扩至 500个字段，超出后需要授权修改（修改字典[mission_max_column]的配置）");
	    	}else{
	    		$G.alert("保存失败");
	    	}
		});
	    $G.postByAjax(data, urlStr, ajaxConf);
	}
 	//验证模板
 	function validTemplet(){
 		var isok = false;
 		var msgObj = {msg:''};
 		//获取配置信息
 		var setting =  hot.getSettings();
 		//获取锁定头
 		var fixedRowsTop = setting.fixedRowsTop;
 		//校验锁定头是否指定内容
		if(validFixeTop(msgObj, fixedRowsTop)){
			$G.alert(msgObj.msg);
			return isok;
		};
		
 		//列未设定时采用默认配置 纯文本 无任何限制
		
 		//验证头部列是否存在列头参数
		var row = hot.getDataAtRow(fixedRowsTop-1);
		if(validRowColNull(msgObj,row)){
			$G.alert(msgObj.msg);
			return isok;
		};
		
		/* //验证是否存在空列情况
		if(hot.countEmptyCols() > 0){
			
		} */
		
		//验证数据模板中是否存在中间行为空情况
		var countRows = hot.countRows();
		for(var i = fixedRowsTop;i<countRows;i++){
			if(fixedRowsTop<i && i<(countRows-1) && !hot.isEmptyRow(i-1) && hot.isEmptyRow(i) && !hot.isEmptyRow(i+1)){
				$G.alert("数据模板中第["+(i+1)+"]行存在中间行为空情况","error");
				return isok;
			}	
		}
		
		return true;
 	}
 	
 	//验证行中是否有列为空
 	function validRowColNull(msg,row){
 		var isok = false;
 		for(var i=0,len = row.length;i<len;i++){
 			if(isNullObj(row[i])){
 				isok = true;
 				msg.msg+='指定列头，锁定行中存在列名为空！<br/>'
 				break;
 			}
 		}
 		return isok;
 	}
 	
 	//是否锁定头，目前都必须有头部，如有其他场景，自行扩展
 	function validFixeTop(msg,fixedRowsTop){
 		var isok = false;
 		if(fixedRowsTop <= 0){
			msg.msg+='未指定锁定头<br/>'
			isok = true
 		}
 		return isok;
 	}
	//给父页面传值
 	function getData(){
 		var result = {
 				 model_cfg_id : $G.getbyName("model_cfg_id").getValue()
 		};
 		return result;
 	}
 	
 	function onCancel(e) {
		$G.closemodaldialog("cancel");
    } 
 	
 	//显示行列输入页面
	function showtxt(){
		$("#div_row_col").show();
	}
	
	//隐藏行列输入框
	function hidtxt(){
		$("#div_row_col").hide();
	}
	
 	
	var excelDatas = [];
	//导入
	var url = "<%=root%>/missionconfig/showExcelDetail";
	function impt() {
		ajaxFileUpload1(url, function(data1) {
			var text = $.parseJSON($(data1).html());
			var data = text.data;
			excelDatas = data;
			hotSettings.data = data;
			columns = [];
			//初始化列配置数组
			if(data.length > 0){
				for(var i = 0,len = data[0].length;i< len;i++){
					columns[i] = {};
				}
			}
			//重新绘制
			hotSettings.fixedRowsTop = 0;
			hotSettings.mergeCells = true;
			hot.updateSettings(hotSettings)
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
				data: { isArr : true },            //附加的额外参数
				dataType : 'json/text', //返回值类型 一般设置为json
				success : function(data1, status) //服务器成功响应处理函数
				{
					mini.unmask(document.body);
					callback(data1);
					var text = $.parseJSON($(data1).html());
					var data = text.data;
					excelDatas = data;
					console.log("导入数据data ")
					console.log(excelDatas)
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
	
	//数据保持入库，由于数据量可能过大,不再页面间传递,直接保存到数据库中
	
	//右键打开列属性配置页面
	//模板配置
	function setColumn(bizParams,callback){
		//根据分类扩展配置场景
		//指向不同的模板配置页面
		var id =  $G.getbyName("model_cfg_id").getValue();
		var pageType =  $G.getbyName("pageType").getValue();
		var url = "<%=root%>/missiondatareport/geColumnCfg?pid="+id+"&id=";
		bizParams.pid = id;
		bizParams.pageType = pageType;
		$G.showmodaldialog("列配置", url, 600, 500, bizParams, function(data){
        	callback(data);
	    });
	}

	//resetRowCol 
	/**
		默认列数设定
	*/
	function resetRowCol(){
		var row = $G.getbyName("init_row").getValue();
		var col = $G.getbyName("init_col").getValue();
		hotSettings.data = Handsontable.helper.createEmptySpreadsheetData(row,col);;
		columns = [];
		for(var i = 0,len = hotSettings.data[0].length;i< len;i++){
			columns[i] = {};
		}
		hot.updateSettings(hotSettings);
	}
	
	function preview() {
		
		//常规模板数据验证
		if(!validTemplet()){
			return;
		}
		
		//获取配置信息
 		var setting =  hot.getSettings();
 		
		//获取到模板内容
		
		//模板内数据
		var sourcedata =  hot.getSourceData();
		
		//模板头部参数
		var columns_cfg = columns; 
		
		//模板数据开始行
		var data_start_row = setting.fixedRowsTop;
		
		//模板头部合并参数
		var mergecells = hot.getPlugin('MergeCells').mergedCellsCollection.mergedCells; //该参数必须在锁定行后才能读取，因为锁定行时会把单元格解除合并
		
		//模板id
		var model_cfg_id = $G.getbyName("model_cfg_id").getValue();
		//任务配置id
		var pid = $G.getbyName("pid").getValue();
		
		var pageType =  $G.getbyName("pageType").getValue();;
		
		//组装任务数据
		var bizParams = {
			data: sourcedata, //当前模板数据
			columns_cfg: columns_cfg, //列配置参数
			data_start_row: data_start_row, //数据开始行
			mergecells: mergecells, //合并参数
			taskid: pid, //任务配置id
			model_cfg_id: model_cfg_id, //模板id
			hotSettings: setting, //当前表单设置
			pageType: pageType //操作分类
		}
		var url = "<%=root%>/missiondatareport/preview";
    	$G.showmodaldialog("预览", url, "100%", "100%", bizParams, function(data){
	    	
    	});
    }
	var columns = [];
	var data_def = Handsontable.helper.createEmptySpreadsheetData(20,6);
	//定义头部列配置数据
	
	for(var i = 0,len = data_def[0].length;i< len;i++){
		columns[i] = {};
	}
	
 	//----handsontable 初始化 开始------
    var hotElement = document.querySelector('#hot');//<====绑定handsontable初始化div 
    
    var hotSettings = {//<====handsontable的配置
        data: data_def,//数据源
        height: $("#hot_div").height(),
        stretchH: "all",     //last:延伸最后一列,all:延伸所有列,none默认不延伸。
        rowHeaders: true, //当值为true时显示行头，当值为数组时，行头为数组的值
		colHeaders: true,
        columnSorting: true,//允许排序
        sortIndicator: true,
        //fillHandle: true, ////当值为true时，允许拖动单元格右下角，将其值自动填充到选中的单元格
		filters: true,
		licenseKey:'non-commercial-and-evaluation',
        autoColumnSize: true, //当值为true且列宽未设置时，自适应列大小
		language:'zh-CN',
		mergeCells: true,
		manualColumnResize: true,
		headerTooltips:true,//工具栏启用
		search: true,// 查询
		manualRowResize: true,
		autoWrapRow: true,
		manualRowMove: true,
		manualColumnMove: true,
		dropdownMenu: true,
		minSpareRows: 1,
		//viewportRowRenderingOffset: 30, //渲染行数 当行或者列太多的时候，handsontable为了加载速度。会只渲染部分数据。当页面有动作的时候才会再次渲染剩余数据。
		//viewportColumnRenderingOffset: 200,//渲染列数
		afterColumnMove:function(oldIndex,newIndex){ 
			var columns_t = mini.clone(columns);
			//检测列移除事件
			//往后移动列位置
			if(((oldIndex+"").split(","))[0] < newIndex){
				var ola = (oldIndex+"").split(","); //移动的列可以是多个相连的
				for(var i=0,len = ola.length;i<len;i++){
					//在列配置数组中在新的位置后面插入拖动的列配置
					columns.splice(parseInt(newIndex+"")+i,0,columns_t[parseInt(ola[i]+"")]);
				}
				//删除移动的列索引元素，因为在上面循环里已经在新的索引位置添加过了，所以要把旧的索引元素删除掉
				columns.splice(parseInt(ola[0]+""),ola.length);
			}else{//往前移动列位置
				//移动的列索引
    			var ola = (oldIndex+"").split(",");
    			for(var i=0,len = ola.length;i<len;i++){
					//拖动位置
    				columns.splice(parseInt(newIndex+"")+i,0,columns_t[parseInt(ola[i]+"")]);
				}
    			//在拖动的开始列加上拖动的列数,的坐标进行删除，删除拖动的列数
				columns.splice(parseInt(ola[0]+"")+ola.length,ola.length);
			}
		},
		contextMenu: {
		  callback: function (key, options) {
			//处理列动作
			if (key === 'remove_col') {
				//删除时，需要把对应的列配置元素删除掉
				for(var i=0,len=options.length;i<len;i++){
					columns.splice(options[i].start.col,1);
				}
			}else if (key === 'col_left') {
				//在左侧添加列，初始化一个空的元素过去
				columns.splice(options[0].start.col,0,{});
			}else if (key === 'col_right') {
				//在右侧添加列，需要在当前列索引位置加一，初始化一个空的元素
				columns.splice(options[0].start.col+1,0,{});
			}
		  },
		  items: {
			"trim_col":{
				name:'多行去除空格',
				callback:function(key,selected,clickEvent){
					var start = selected[0].start.row; //开始行
					var end = selected[0].end.row; //结束行
					var cols = selected[0].start.col; //开始列
					var colsData = hot.getDataAtCol(cols); //列值
					var sum = 0;
					
					//去除单元格左右的空格
					
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
						if(typeof colsData[i] == "string"){//只去除string类型的值空格
							hot.setDataAtCell(i,cols,getDevareSpace(colsData[i]));
						}
					}
				}
			},
			"lock_row":{
				name:'锁定行',
				hidden: function () { // `hidden` can be a boolean or a function 选中行未锁定时，且在未锁定区域时，展示菜单
				  return   (this.getSettings().fixedRowsTop == 0 || this.getSelectedLast()[2] > (hotSettings.fixedRowsTop-1)) ? false : true;
				},
				callback:function(key,selected,clickEvent){
					var start = selected[0].start.row;
					hotSettings.fixedRowsTop = start+1;//锁定行位置，例如 第一行索引为 1不是0
					hot.updateSettings(hotSettings);
				}
			},
			"unlock_row":{
				name:'解除锁定行',
				hidden: function () { // `hidden` can be a boolean or a function 选中行锁定时，并且在锁定区域时展示
				  return  (hotSettings.fixedRowsTop != 0 && this.getSelectedLast()[2] < hotSettings.fixedRowsTop) ? false : true
				  //return this.getSelectedLast()[1] == hotSettings.fixedRowsTop ; // `this` === hot3
				},
				callback:function(key,selected,clickEvent){
					hotSettings.fixedRowsTop = 0; //设置锁定行为 0 ,其实就是 不锁定
					hot.updateSettings(hotSettings);
				}
			},
			"set_column":{
				name:'设置列属性',
				hidden: function () { // `hidden` can be a boolean or a function //选中单元格，行锁定时，并且在锁定区域时，展示
				  return  (hotSettings.fixedRowsTop != 0 && this.getSelectedLast()[2] <= hotSettings.fixedRowsTop) ? false : true
				},
				disabled:function(){
					return this.getSelectedLast()[1] != this.getSelectedLast()[3];	//如果选中多列，禁用掉该设置
				},
				callback:function(key,selected,clickEvent){
					var col = selected[0].start.col;
					var colheader = hot.getColHeader(col);
					//找到所有的列类型
					setColumn({col:col,colheader:colheader,headers:hot.getColHeader(),olddata:columns[col]}, function(data){
						//console.log(data)
						columns[col] = data;
						//修改锁定行后的该列单元格格式为设定格式
						/* var setting =  this.getSettings();
						var row  = hot.countRenderedRows();
						var top = hotSettings.fixedRowsTop;
						
						for(;top<row;top++){
							setting.cell = [{row:top,col:col,type: data.col_type}]
						} */
						
					});
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
		}
		
    };
    var hot = new Handsontable(hotElement, hotSettings);
 </script>