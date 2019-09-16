/**
 * 返回字符串是否以该子字串开头
 */
String.prototype.startsWith = function(substrs) {
	return this.indexOf(substrs) == 0;
};

/**
 * 返回字符串是否以该子字串结尾
 */
String.prototype.endsWith = function(substrs) {
	if(substrs){
		//如果子串长度长于当前字串直接返回false
		if(substrs.length>this.length){
			return false;
		}else{
			var end = this.lastIndexOf(substrs);
			end = end + substrs.length;
			if(end==this.length){
				return true;
			}else{
				return false;
			}
		}
	}
	return false;
};


//校验码表字段长度
 function onValidationDict(e1,e2){
 	if (e1.isValid) {
        if ((e1.value+"").getBytes() > e2) {
            e1.errorText = String.format(message.validation.codeTableErrorMsg,e2);
            e1.isValid = false;
        }
    }
 }
 
 //校验码表字段长度
 function onValidationDictEqual(e1,e2){
 	if (e1.isValid) {
 		if(e1.value){
 			if ((e1.value+"").getBytes() != e2) {
            e1.errorText = String.format(message.validation.codeTableNotEqualErrorMsg,e2);
            e1.isValid = false;
        	}
 		}
    }
 }
 
 //校验电话号码格式
function onPhoneValidation(e) {
	//通常电话号码分为3种格式，1区号是3为，其他是8位；2区号是4位，其他是7位；3区号是4位，其他是8位数字
   	var pattern = /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/;
    if (e.value!="" && pattern.test(e.value) == false) {
        e.errorText = message.staff.invalidPhone;
        e.isValid = false;
    }
}
//开始日期必须小于结束日期
function onBeginDateValidation(e,endDatePicker){
	var begin = e.sender.value;
	var end = endDatePicker.value;
	if(!begin||!end){
		e.isValid = true;
		endDatePicker.setIsValid(true);
		return;
	}
	if (e.isValid) {
        if (begin > end) {
            e.errorText = message.common.beginDateValidationMsg;
            e.isValid = false;
        }else{
        	endDatePicker.setIsValid(true);
        }
        
    }
}
//结束日期必须大于开始日期
function onEndDateValidation(e,beginDatePicker){
	var begin = beginDatePicker.value;
	var end = e.sender.value;
	if(!begin||!end){
		e.isValid = true;
		beginDatePicker.setIsValid(true);
		return;
	}
	if (e.isValid) {
        if (begin > end) {
            e.errorText = message.common.endDateValidationMsg;
            e.isValid = false;
        }else{
        	beginDatePicker.setIsValid(true);
        }
    }
}

//字符串取长度，汉字长度为2
String.prototype.getBytes = function() {    
    var cArr = this.match(/[^\x00-\xff]/ig);    
    return this.length + (cArr == null ? 0 : cArr.length); 
}

function changeButtonPosition(container){
	var okBtn = getOkBtn(container);
	var cancelBtn = getCancelBtn(container);
	var okIndex = okBtn.index();
	var cancelIndex = cancelBtn.index();
	//确定按钮右》左
	if(needChange()){
    	//换样式
    	changeStyle(okBtn, cancelBtn);
    	//移除按钮
    	okBtn = okBtn.remove();
    	cancelBtn = cancelBtn.remove();
    	//Ok、cancel之外是否还有其它元素，有的话插在其它元素之前
    	var tmp1 = container.children().get(cancelIndex);
		if(tmp1){
    		okBtn.insertBefore(tmp1);
		}else{
			container.append(okBtn);
		}
		var tmp2 = container.children().get(okIndex);
		if(tmp2){
    		cancelBtn.insertBefore(tmp2);
		}else{
			container.append(cancelBtn);
		}
	}
}

function changeStyle(okBtn, cancelBtn){
	var okBtnStyle = okBtn.attr("style");
	var okBtnClass = okBtn.attr("class");
	var cancelBtnStyle = cancelBtn.attr("style");
	var cancelBtnClass = cancelBtn.attr("class");
	//清除原样式
	okBtn.removeAttr("style").removeAttr("class");
	cancelBtn.removeAttr("style").removeAttr("class");
	//添加新样式
	okBtn.attr("style",cancelBtnStyle);
	okBtn.attr("class",cancelBtnClass);
	cancelBtn.attr("style",okBtnStyle);
	cancelBtn.attr("class",okBtnClass);
	
}

function needChange() {
	return top.okBtnPosition=="left";
}

function getOkBtn(container) {
	var okBtn = container.find("[iconCls=icon-ok]");
	var saveBtn = container.find("[iconCls=icon-save]");
	if(okBtn.length > 0) {
		return okBtn;
	}else {
		return saveBtn;
	};
}

function getCancelBtn(container) {
	var closeBtn = container.find("[iconCls=icon-close]");
	var cancelBtn = container.find("[iconCls=icon-cancel]");
	if(closeBtn.length > 0) {
		return closeBtn;
	}else {
		return cancelBtn;
	}
}

/*自定义vtype*/
nui.VTypes["maxByteLengthErrorText"] = message.dsm.maxLength;
nui.VTypes["maxByteLength"] = function (v) {
    return v.getBytes() <= 255;
};

/**
 * @author 常显阳 20181203
 * @param data {a:b,c:d}
 * @returns string
 */
function connUrlByJson(data){
	var paramurl = [];
	if(data){
		for(var key in data){
			paramurl.push(key+"="+data[key]);		
		}
	}
	return paramurl.join("&");
}

function getRowsParaToJoin(rows,para){
  	 var ids = [];
       for (var i = 0, l = rows.length; i < l; i++) {
           var r = rows[i];
           ids.push(r[para]);
       }
       return ids.join(',');
   }
function setReadonly(ids,hideOrShow){
	if(ids){
		if(ids.length>0){
			for(var i=0;i<ids.length;i++){
				mini.get(ids[i]).set({readOnly:hideOrShow});
			}
		}
	}
}

function downForm(config){
	var downFormt = $("<form/>");
	if(!config.form.hasOwnProperty("method")){
		config.form["method"]="post";
	}
	if(!config.form.hasOwnProperty("target")){
		config.form["target"]="callbackIframe";
	}
	downFormt.attr(config.form);
	for(var key in config){
		if(key != "form"){
			downFormt.append($("<input/>").addClass("mini-hidden").attr({name:key,value:config[key]}));
		}
	}
	return downFormt;
}
function downFile(createfrom){
	 var downFormt = downForm(createfrom)
	 $("body").append(downFormt);
	 //检查当前页面是否有 callbackIframe
	 if($("#callbackIframe").length == 0){
		 $("body").append('<iframe  id="callbackIframe" name="callbackIframe" style="display:none;"></iframe>');
	 }
	 downFormt.submit();
	 downFormt.remove();
	 //$("#callbackIframe").remove();
}


function isNullObj(obj){
	return (obj == null || obj == undefined || ( typeof(obj) == "string" && myTrim(obj).length == 0 ) );
}

function myTrim(x) {
    return x.replace(/^\s+|\s+$/gm,'');
}

/**
 * 格式化时间 
 * @param value 201802111200 或 20180211
 * @returns {String}
 */
function renderTimes(value){
	if(value!=null && value!=undefined){
		if(typeof(value)=="String"){
			
		}else{
			value +="";
		}
	}
	var val = value;
	var str = "";
	if (!isNullObj(val)) {
		if(val.length>8){
			str = val.substring(0, 4) + "-" + val.substring(4, 6) + "-" + val.substring(6, 8)
			+ " " + val.substring(8, 10) + ":" + val.substring(10, 12) + ":" + val.substring(12, 14);
			
		}else{
			str = val.substring(0, 4) + "-" + val.substring(4, 6) + "-" + val.substring(6, 8);
		}
	}
	return str;
}