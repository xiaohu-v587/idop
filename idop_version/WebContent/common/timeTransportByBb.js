function timeTransport(date){
	var begindate = "";
	 var y = date.getFullYear();
	  var m = date.getMonth()+1;
	  var d = date.getDate();
	  m = m<10?("0"+m):m;
	  d = d<10?("0"+d):d;
	  begindate = y+""+m+""+d+"";
	  return begindate;
}

//机构下拉框清空
function onCloseClick(e) {
    var obj = e.sender;
    obj.setText("");
    obj.setValue("");
}

function onOrgChanged(){
	var org_id = $G.get("org_id").getValue();
}
