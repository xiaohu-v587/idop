$G = nui;
function GcdsAjaxConf(){
	this.dataType = "json";
	this.type = "POST";
	this.contentType = "application/x-www-form-urlencoded";
	this.isSuccMsg = true;
	this.isShowProcessBar = true;
	this.beforeSubmitFunc = null;
	this.successFunc = null;
	this.errorFunc = null;
	this.processMsg = null;
	this.async = true;
}
GcdsAjaxConf.prototype.setContentType = function(contentType){
	this.contentType = contentType;
};

GcdsAjaxConf.prototype.setContentTypeToJson = function(){
	this.contentType = "application/json";
};

GcdsAjaxConf.prototype.setIsShowSuccMsg = function(showSuccmsg){
	this.isSuccMsg = showSuccmsg;
};

GcdsAjaxConf.prototype.setIsShowProcessBar = function(isShowProcessBar){
	this.isShowProcessBar = isShowProcessBar;
};

GcdsAjaxConf.prototype.setBeforeSubmitFunc = function(beforeSubmitFunc){
	this.beforeSubmitFunc = beforeSubmitFunc;
};

GcdsAjaxConf.prototype.setSuccessFunc = function(successFunc){
	this.successFunc = successFunc;
};

GcdsAjaxConf.prototype.setErrorFunc = function(errorFunc){
	this.errorFunc = errorFunc;
};

GcdsAjaxConf.prototype.setIsAsync = function(isAsync){
	this.async = isAsync;
};

GcdsAjaxConf.prototype.submitForm = function(formId, reqUrl){
	$G.submitForm(formId, reqUrl, this);
};

GcdsAjaxConf.prototype.postByAjax = function(postData, reqUrl){
	$G.postByAjax(postData, reqUrl, this);
};
//Ajax提交表单
nui.submitForm = function(formId,reqUrl,ajaxConf){
	var tempAjaxConf = null;
	if(ajaxConf){
		tempAjaxConf = ajaxConf;
	}else{
		tempAjaxConf = new GcdsAjaxConf();
	}
	var formTemp = new nui.Form("#" + formId);
	formTemp.validate();
    if (formTemp.isValid() == false){
    	return false;
    }
    //有提交前校验函数，则执行该函数
	if(tempAjaxConf.beforeSubmitFunc && !tempAjaxConf.beforeSubmitFunc()){
		return false;
	}
	//表单校验通过继续提交
	if(tempAjaxConf.isShowProcessBar){
		tempAjaxConf.processMsg = this.showProcessBar();
	}
	var ajaxSetting = this.getAjaxSetting(tempAjaxConf);
	if(ajaxSetting.contentType == "application/json"){
		ajaxSetting.data = nui.encode(formTemp.getData());
	}else{
		ajaxSetting.data = formTemp.getData(false,true);
	}
	ajaxSetting.url = this.formatUrl(reqUrl);
	$.ajax(ajaxSetting);
};
//Ajax提交数据
nui.postByAjax = function(postData,reqUrl,ajaxConf){
	var tempAjaxConf = null;
	if(ajaxConf){
		tempAjaxConf = ajaxConf;
	}else{
		tempAjaxConf = new GcdsAjaxConf();
	}
	if(tempAjaxConf.beforeSubmitFunc && !tempAjaxConf.beforeSubmitFunc()){
		return false;
	}

	if(tempAjaxConf.isShowProcessBar){
		tempAjaxConf.processMsg = this.showProcessBar();
	}
	
	var ajaxSetting = this.getAjaxSetting(tempAjaxConf);
	ajaxSetting.url = this.formatUrl(reqUrl);
	if(postData){
		ajaxSetting.data = postData;
	}
	$.ajax(ajaxSetting);
};
//grid批量提交编辑数据
nui.batchSubmit = function(gridId, reqUrl, ajaxConf, formId){
	var formData = null;
	var tempAjaxConf = null;
	if(ajaxConf){
		tempAjaxConf = ajaxConf;
	}else{
		tempAjaxConf = new GcdsAjaxConf();
	}
	//有formID，则获取form数据
    if(formId){
    	var formTemp = new nui.Form("#" + formId);
    	formTemp.validate();
        if (formTemp.isValid() == false){
        	return false;
        }
        formData = formTemp.getData();
    }
    //有提交前校验函数，则执行该函数
	if(tempAjaxConf.beforeSubmitFunc && !tempAjaxConf.beforeSubmitFunc()){
		return false;
	}
	//表单校验通过继续提交
	if(tempAjaxConf.isShowProcessBar){
		tempAjaxConf.processMsg = this.showProcessBar();
	}
	//json数据流方式提交
	tempAjaxConf.setContentTypeToJson();
	//准备提交参数
    var json = null;
    //有formData，则获取form数据
    if(formData){
    	if(typeof(gridId) != "string"){
    		//数组，提交多grid
    		for(var index = 0;index < gridId.length;index++){
    			var gridData = $G.formatGridSubmitData(gridId[index]);
            	formData[gridId[index] + "AddList"] = gridData[0];
            	formData[gridId[index] + "UpdateList"] = gridData[1];
            	formData[gridId[index] + "DeleteList"] = gridData[2];
    		}
    	} else {
    		var gridData = $G.formatGridSubmitData(gridId);
        	formData.addList = gridData[0];
        	formData.updateList = gridData[1];
        	formData.deleteList = gridData[2];
    	}
    	json = nui.encode(formData);
    }else{
    	var submitData = {};
    	if(typeof(gridId) != "string"){
    		//数组，提交多grid
    		for(var index = 0;index < gridId.length;index++){
    			var gridData = $G.formatGridSubmitData(gridId[index]);
    			submitData[gridId[index] + "AddList"] = gridData[0];
    			submitData[gridId[index] + "UpdateList"] = gridData[1];
    			submitData[gridId[index] + "DeleteList"] = gridData[2];
    		}
    	} else {
    		var gridData = $G.formatGridSubmitData(gridId);
    		submitData.addList = gridData[0];
    		submitData.updateList = gridData[1];
    		submitData.deleteList = gridData[2];
    	}
    	json = nui.encode(submitData);
    }
	var ajaxSetting = this.getAjaxSetting(tempAjaxConf);
	ajaxSetting.data = json;
	ajaxSetting.url = this.formatUrl(reqUrl);
	$.ajax(ajaxSetting);
};
nui.formatGridSubmitData = function (gridId){
	var rtnArray = new Array();
	var addArray=new Array(); 
    var deleteArray=new Array(); 
    var updateArray=new Array();
    var data = nui.get(gridId).getChanges();
    for(var index = 0; index < data.length; index++){
    	row=data[index];
    	if(row._state=="added"){          //新增集合
    		addArray.push(row);
    	}else  if(row._state=="modified"){//修改集合
    		updateArray.push(row);
    	}else{                            //删除集合
    		deleteArray.push(row);
    	}
    }
    rtnArray.push(addArray);
    rtnArray.push(updateArray);
    rtnArray.push(deleteArray);
    return rtnArray;
};
//为URL添加前缀
nui.formatUrl = function(reqUrl){
	if(appContext == null || appContext == "" || appContext == "/"){
		appContext = "";
	}
	var flagStr = appContext + "/";
	var rtnValue = "";
	if(reqUrl.startsWith(flagStr)){
		rtnValue = reqUrl;
	} else {
		if(reqUrl.startsWith("/")){
			rtnValue = appContext + reqUrl;
		} else {
			rtnValue = appContext + "/" + reqUrl;
		}
	}
	if(rtnValue.indexOf("?") > 0){
		rtnValue = rtnValue + "&_reqseq=" + Math.random();
	} else {
		rtnValue = rtnValue + "?_reqseq=" + Math.random();
	}
	return rtnValue;
};
//显示进度条
nui.showProcessBar = function(){
	return top.topShowProcessBar();
};
nui.closeProcessBar = function(processBar){
	return top.topCloseProcessBar(processBar);
};
//默认Ajax配置参数对象
nui.getAjaxSetting = function(ajaxConf){
	//var ajaxConf = new GcdsAjaxConf();
	var ajaxSetting = {};
	ajaxSetting.dataType = ajaxConf.dataType;
	ajaxSetting.type = ajaxConf.type;
	ajaxSetting.async = ajaxConf.async;
	
	ajaxSetting.error = function(data, textStatus, jqXHR){
		if(ajaxConf.errorFunc){
			ajaxConf.errorFunc(data);
		}
		if(ajaxConf.processMsg){
			$G.closeProcessBar(ajaxConf.processMsg);
		}
		try{
			var jsonData = $.parseJSON(data.responseText);
			if(jsonData.isBizErr){//业务错误标识
				$G.GcdsError(jsonData.errMsg);
			}else{
				//title, url, width, height, bizParams, callBackFunc, isShowCloseIcon, isAllowResize
				$G.showmodaldialog(message.Gcdsui.errorPrompt,"exceptionCtrler/toAjaxExceptionPage.do",null,300,jsonData,null,null,false);
			}
		}catch(err){
			if(data.status == 403){
				$G.GcdsError(message.Gcdsui.noRight);
			}else if(data.status == 200){
				//Session超时或当前Session被其他Session逐出！
				top.window.location = $G.formatUrl("login/login.do?error=true");
			}
		}
	};
	ajaxSetting.success = function(data){
		if(ajaxConf.successFunc){
			ajaxConf.successFunc(data);
		}
		if(ajaxConf.processMsg){
			$G.closeProcessBar(ajaxConf.processMsg);
		}
		if(ajaxConf.isSuccMsg){
			$G.GcdsInfo(message.Gcdsui.success);
		}
	};
	ajaxSetting.contentType = ajaxConf.contentType;
	return ajaxSetting;
};
//打开模态窗口
nui.showmodaldialog = function(title, url, width, height, bizParams, callBackFunc, isShowCloseIcon, isAllowResize){
	var openConf = {};
	openConf.title = title;
	openConf.url = this.formatUrl(url);
	if(width){
		openConf.width = width;
	}else{
		openConf.width = 650;
	}
	if(height){
		openConf.height = height;
	}else{
		openConf.height = 450;
	}
	if(isShowCloseIcon != null){
		openConf.showCloseButton = isShowCloseIcon;
	}
	if(isAllowResize != null){
		openConf.allowResize = isAllowResize;
	}
	if(bizParams){
		openConf.onload = function () {
            var iframe = this.getIFrameEl();
            if(iframe != null && iframe.contentWindow != null && iframe.contentWindow.setData){
            	iframe.contentWindow.setData(bizParams);
            }
        };
	}
	if(callBackFunc){
		openConf.ondestroy = function(action){
			if("OK" == action.toUpperCase()){
				if(this.getIFrameEl().contentWindow.getData){
					var data = this.getIFrameEl().contentWindow.getData();
					data = nui.clone(data);
					callBackFunc(data);
				}else{
					callBackFunc();
				}
			}
		};
	}
	nui.open(openConf);
};
//关闭模态窗口
nui.closemodaldialog = function (action) {
	if (window.CloseOwnerWindow){
		return window.CloseOwnerWindow(action);
	}else{
		window.close();
	}
};

/*
 * MAP对象，实现MAP功能
 *
 * 接口：
 * size()     获取MAP元素个数
 * isEmpty()    判断MAP是否为空
 * clear()     删除MAP所有元素
 * put(key, value)   向MAP中增加元素（key, value) 
 * remove(key)    删除指定KEY的元素，成功返回True，失败返回False
 * get(key)    获取指定KEY的元素值VALUE，失败返回NULL
 * element(index)   获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
 * containsKey(key)  判断MAP中是否含有指定KEY的元素
 * containsValue(value) 判断MAP中是否含有指定VALUE的元素
 * values()    获取MAP中所有VALUE的数组（ARRAY）
 * keys()     获取MAP中所有KEY的数组（ARRAY）
 *
 * 例子：
 * var map = new Map();
 *
 * map.put("key", "value");
 * var val = map.get("key")
 * ……
 *
 */
function Map() {
	this.elements = new Array();
}

//获取MAP元素个数
Map.prototype.size = function() {
	return this.elements.length;
};

//判断MAP是否为空
Map.prototype.isEmpty = function() {
	return (this.elements.length < 1);
};

//删除MAP所有元素
Map.prototype.clear = function() {
	this.elements = new Array();
};

//向MAP中增加元素（key, value) 
Map.prototype.put = function(_key, _value) {
	this.remove(_key);
	this.elements.push( {
		key : _key,
		value : _value
	});
};

//删除指定KEY的元素，成功返回True，失败返回False
Map.prototype.remove = function(_key) {
	var bln = false;
	try {
		for (var i = 0; i < this.elements.length; i++) {
			if (this.elements[i].key == _key) {
				this.elements.splice(i, 1);
				return true;
			}
		}
	} catch (e) {
		bln = false;
	}
	return bln;
};

//获取指定KEY的元素值VALUE，失败返回NULL
Map.prototype.get = function(_key) {
	try {
		for (var i = 0; i < this.elements.length; i++) {
			if (this.elements[i].key == _key) {
				return this.elements[i].value;
			}
		}
	} catch (e) {
		return null;
	}
};

//获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
Map.prototype.element = function(_index) {
	if (_index < 0 || _index >= this.elements.length) {
		return null;
	}
	return this.elements[_index];
};

//判断MAP中是否含有指定KEY的元素
Map.prototype.containsKey = function(_key) {
	var bln = false;
	try {
		for (var i = 0; i < this.elements.length; i++) {
			if (this.elements[i].key == _key) {
				bln = true;
			}
		}
	} catch (e) {
		bln = false;
	}
	return bln;
};

//判断MAP中是否含有指定VALUE的元素
Map.prototype.containsValue = function(_value) {
	var bln = false;
	try {
		for (var i = 0; i < this.elements.length; i++) {
			if (this.elements[i].value == _value) {
				bln = true;
			}
		}
	} catch (e) {
		bln = false;
	}
	return bln;
};

//获取MAP中所有VALUE的数组（ARRAY）
Map.prototype.values = function() {
	var arr = new Array();
	for (var i = 0; i < this.elements.length; i++) {
		arr.push(this.elements[i].value);
	}
	return arr;
};

//获取MAP中所有KEY的数组（ARRAY）
Map.prototype.keys = function() {
	var arr = new Array();
	for (var i = 0; i < this.elements.length; i++) {
		arr.push(this.elements[i].key);
	}
	return arr;
};

Map.prototype.toJson = function() {
	var jsonData = {};
	for(var i =0 ;i<this.elements.length;i++){
		if(this.elements[i].key){
			jsonData[this.elements[i].key] = this.elements[i].value;
		}
	}
	return jsonData;
};
//跨Iframe提示信息
nui.GcdsAlert = function(msg, title, callBackFun, params){
	top.topAlert(msg, title, callBackFun, params);
};
function topAlert(msg, title, callBackFun, params){
	if(params && params.length > 0){
		var newMsg = $G.msgFormat(msg, params);
		nui.alert(newMsg, title, callBackFun);
	}else{
		nui.alert(msg, title, callBackFun);
	}
}
nui.GcdsInfo = function(msg, title, callBackFun, params){
	top.topInfo(msg, title, callBackFun, params);
};
function topInfo(msg, title, callBackFun, params){
	if(params && params.length > 0){
		var newMsg = $G.msgFormat(msg, params);
		nui.info(newMsg, title, callBackFun);
	}else{
		nui.info(msg, title, callBackFun);
	}
}
nui.GcdsError = function(msg, title, callBackFun, params){
	top.topError(msg, title, callBackFun, params);
};
function topError(msg, title, callBackFun, params){
	if(params && params.length > 0){
		var newMsg = $G.msgFormat(msg, params);
		nui.error(newMsg, title, callBackFun);
	}else{
		nui.error(msg, title, callBackFun);
	}
}
//跨Iframe显示进度条
function topShowProcessBar(){
	return nui.loading(message.Gcdsui.wait, message.Gcdsui.prompt);
}
function topCloseProcessBar(processBar){
	nui.hideMessageBox(processBar);
}
//跨Iframe提示信息
nui.GcdsConfirm = function(msg, title, callBackFun, params){
	top.topConfirm(msg, title, callBackFun, params);
};
function topConfirm(msg, title, callBackFunc, params){
	if(params && params.length > 0){
		var newMsg = $G.msgFormat(msg, params);
		nui.confirm(newMsg, title, callBackFunc);
	}else{
		nui.confirm(msg, title, callBackFunc);
	}
}
//提示信息参数化
nui.msgFormat = function (msg, params) {
    msg = msg || "";
    return msg.replace(/\{(\d+)\}/g, function (m, i) {
        return params[i];
    });
};

nui.getForm = function (formId) {
    return new nui.Form(formId);
};

nui.codeTypeRender = function(e, field, codeTypeData){
	if(typeof(codeTypeData) != "string"){
		for(var index = 0;index < codeTypeData.length;index++){
			if(e.row[field] == codeTypeData[index].id){
				return codeTypeData[index].text;
			}
		}
	} else {
		 return $G.getDictText(codeTypeData, e.row[field]);
	}
};
//打开自定义窗口对话框配置类
function GcdsWinConfig(title, url, width, height, bizParams, callBackFunc){
	if(url){
    	this.url = $G.formatUrl(url);  //页面地址
	}
    this.title = title;            //标题
    this.iconCls = "";             //标题图标
    this.width = 800;              //宽度
    if(width){
    	this.width = width;
    }
    this.height = 600;     //高度
    if(height){
    	this.height = height;
    }
    if(bizParams) {
    	this.bizParams = bizParams;
    }
    if(callBackFunc){
    	this.callBackFunc = callBackFunc;
    }
    this.allowResize = false;       //允许尺寸调节
    this.allowDrag = false;         //允许拖拽位置
    this.showCloseButton = true;   //显示关闭按钮
    this.showMaxButton = false;     //显示最大化按钮
    this.showModal = true;         //显示遮罩
    this.showMinButton = false;
}
GcdsWinConfig.prototype.setUrl = function(url){
	this.url = $G.formatUrl(url);
};
GcdsWinConfig.prototype.setTitle = function(title){
	this.title = title;
};
GcdsWinConfig.prototype.setIconCls = function(iconCls){
	this.iconCls = iconCls;
};
GcdsWinConfig.prototype.setIsAllowResize = function(allowResize){
	this.allowResize = allowResize;
};
GcdsWinConfig.prototype.setIsAllowDrag = function(allowDrag){
	this.allowDrag = allowDrag;
};
GcdsWinConfig.prototype.setIsShowCloseButton = function(showCloseButton){
	this.showCloseButton = showCloseButton;
};
GcdsWinConfig.prototype.setIsShowMaxButton = function(showMaxButton){
	this.showMaxButton = showMaxButton;
};
GcdsWinConfig.prototype.setIsShowModal = function(showModal){
	this.showModal = showModal;
};
GcdsWinConfig.prototype.setIsShowMinButton = function(showMinButton){
	this.showMinButton = showMinButton;
};
//打开窗口
nui.showdialog = function(options){
	var openOptions = {};
	openOptions.url = options.url;
	openOptions.title = options.title;
	openOptions.iconCls = options.iconCls;
	openOptions.width = options.width;
	openOptions.height = options.height;
	openOptions.allowResize = options.allowResize;
	openOptions.allowDrag = options.allowDrag;
	openOptions.showMaxButton = options.showMaxButton;
	openOptions.showMinButton = options.showMinButton;
	openOptions.showCloseButton = options.showCloseButton;
	openOptions.showModal = options.showModal;
	if(options.bizParams){
		openOptions.onload = function () {
            var iframe = this.getIFrameEl();
            if(iframe != null && iframe.contentWindow != null && iframe.contentWindow.setData){
            	iframe.contentWindow.setData(options.bizParams);
            }
        };
	}
	if(options.callBackFunc){
		openOptions.ondestroy = function(action){
			if("OK" == action.toUpperCase()){
				if(this.getIFrameEl().contentWindow.getData){
					var data = this.getIFrameEl().contentWindow.getData();
					data = nui.clone(data);
					options.callBackFunc(data);
				}else{
					options.callBackFunc();
				}
			}
		};
	}
	nui.open(openOptions);
};
//关闭窗口
nui.closedialog = nui.closemodaldialog;