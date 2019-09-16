/////////////////////////////////////////////////////////
//DridGrid reload()方法重写，解决当前加载后没有数据的情况;
/////////////////////////////////////////////////////////

var callbackFuncs = {
	success:"",
	error:"",
	complete:""
};

nui.DataGrid.prototype.reload = function(success, error, complete){
	callbackFuncs.success = success;
	callbackFuncs.error = error;
	callbackFuncs.complete = complete;
	var _loadParams = this._dataSource.loadParams;
	this.load(_loadParams, succFunc, error);
};

function succFunc(){
	var totalCount = this.getTotalCount();
	var _loadParams = this.loadParams;
	if(totalCount <= this.pageIndex*this.pageSize && this.pageIndex != 0){
		_loadParams.pageIndex = _loadParams.pageIndex - 1;
		this.load(_loadParams, callbackFuncs.success, callbackFuncs.error, callbackFuncs.complete);
	}else{
		if(callbackFuncs.success){
			var succFuncion = callbackFuncs.success;
			succFuncion();
		}
	}
}


/////////////////////////////////////////////////////////////////////
//Calendar _CreateView()方法重写，加入dateCls,dateStyle两个属性的识别
/////////////////////////////////////////////////////////////////////

nui.Calendar.prototype._CreateView = function (viewDate, row, column) {
        var month = viewDate.getMonth();
        var date = this.getFirstDateOfMonth(viewDate);
        var firstDateOfWeek = new Date(date.getTime());
        var todayTime = mini.clearTime(new Date()).getTime();
        //var selectedTime = this.value ? mini.clearTime(this.value).getTime() : -1;

        var multiView = this.rows > 1 || this.columns > 1;

        var s = '';
        s += '<table class="mini-calendar-view" border="0" cellpadding="0" cellspacing="0">';
        //header
        if (this.showHeader) {
            s += '<tr ><td colSpan="10" class="mini-calendar-header"><div class="mini-calendar-headerInner">';
            if (row == 0 && column == 0) {
                s += '<div class="mini-calendar-prev">';
                if (this.showYearButtons) s += '<span class="mini-calendar-yearPrev"></span>';
                if (this.showMonthButtons) s += '<span class="mini-calendar-monthPrev"></span>';
                s += '</div>';
            }
            if (row == 0 && column == this.columns - 1) {
                s += '<div class="mini-calendar-next">';
                if (this.showMonthButtons) s += '<span class="mini-calendar-monthNext"></span>';
                if (this.showYearButtons) s += '<span class="mini-calendar-yearNext"></span>';
                s += '</div>';
            }
            s += '<span class="mini-calendar-title">' + mini.formatDate(viewDate, this.format); +'</span>';
            s += '</div></td></tr>';
        }

        //daysHeader
        if (this.showDaysHeader) {
            s += '<tr class="mini-calendar-daysheader"><td class="mini-calendar-space"></td>';
            if (this.showWeekNumber) {
                s += '<td sclass="mini-calendar-weeknumber"></td>';
            }

            for (var j = this.firstDayOfWeek, k = j + 7; j < k; j++) {
                var name = this.getShortWeek(j);
                s += '<td yAlign="middle">';
                s += name;
                s += '</td>';
                date = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1);
            }
            s += '<td class="mini-calendar-space"></td></tr>';
        }

        //days
        date = firstDateOfWeek;
        for (var i = 0; i <= 5; i++) {
            s += '<tr class="mini-calendar-days"><td class="mini-calendar-space"></td>';
            if (this.showWeekNumber) {
                var num = mini.getWeek(date.getFullYear(), date.getMonth() + 1, date.getDate());
                if (String(num).length == 1) num = "0" + num;
                s += '<td class="mini-calendar-weeknumber" yAlign="middle">' + num + '</td>';
            }
            for (var j = this.firstDayOfWeek, k = j + 7; j < k; j++) {
                var weekend = this.isWeekend(date);
                var clearTime = mini.clearTime(date).getTime();
                var isToday = clearTime == todayTime;
                var isSelected = this.isSelectedDate(date);

                if (month != date.getMonth() && multiView) {
                    clearTime = -1;
                }

                var e = this._OnDrawDate(date);

                s += '<td yAlign="middle" id="';
                s += this.uid + "$" + clearTime;
                s += '" class="mini-calendar-date ';
                if (weekend) {
                    s += ' mini-calendar-weekend ';
                }
                if (e.allowSelect == false) {
                    s += ' mini-calendar-disabled ';
                }

                if (month != date.getMonth() && multiView) {
                } else {
                    if (isSelected) {
                        s += ' ' + this._selectedDateCls + ' ';
                    }
                    if (isToday) {
                        s += ' mini-calendar-today ';
                    }
                }
                if (month != date.getMonth()) {
                    s += ' mini-calendar-othermonth ';
                }
                if (e.dateCls) s += ' ' + e.dateCls;

                s += '" style="';
                if (e.dateStyle) s += e.dateStyle;
                s += '">';

                if (month != date.getMonth() && multiView) {
                } else {

                    s += e.dateHtml;
                }
                s += '</td>';

                date = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1);
            }
            s += '<td class="mini-calendar-space"></td></tr>';
        }
        s += '<tr class="mini-calendar-bottom" colSpan="10"><td ></td></tr>';

        s += '</table>';
        return s;
};


/////////////////////////////////////////////////////////////////////
//Tabs控件，加入headMenu/showMenuButton 两个属性，支持tab头部展现菜单按钮并可以自定义菜单
/////////////////////////////////////////////////////////////////////
nui.Tabs.prototype.headMenuCls = 'mini-tab-menu';
nui.Tabs.prototype._initEvents = function () {
        mini._BindEvents(function () {
            mini.on(this.el, "mousedown", this.__OnMouseDown, this);
            mini.on(this.el, "click", this.__OnClick, this);
            mini.on(this.el, "mouseover", this.__OnMouseOver, this);
            mini.on(this.el, "mouseout", this.__OnMouseOut, this);
			mini.on(this.el, "dblclick", this.__OnDblClick, this); 
        }, this);
};
nui.Tabs.prototype.headMenu = '';
nui.Tabs.prototype.setHeadMenu = function (value){
		this.headMenu = value;
};
nui.Tabs.prototype.getHeadMenu = function (){
		return this.headMenu;
};
nui.Tabs.prototype.createTab = function (options) {
		var tab = mini.copyTo({
            _id: this._TabID++,
            name: "",
            title: "",

            newLine: false,

            iconCls: "",
            iconStyle: "",
            headerCls: "",
            headerStyle: "",
            bodyCls: "",
            bodyStyle: "",

            visible: true,
            enabled: true,
            showCloseButton: false,
			showMenuButton: false,
            active: false,

            url: "",
            loaded: false,
            refreshOnClick: false,
			closeWhenDblClick: true

        }, options);
        if (options) {
            options = mini.copyTo(options, tab);
            tab = options;
        }
        return tab;
        
};
nui.Tabs.prototype._create = function () {
		
        this.el = document.createElement("div");
        this.el.className = "mini-tabs";

        var s = '<table class="mini-tabs-table" cellspacing="0" cellpadding="0"><tr style="width:100%;">'
                    + '<td></td>'
                    + '<td style="text-align:left;vertical-align:top;width:100%;"><div class="mini-tabs-bodys"></div></td>'
                    + '<td></td>'
                + '</tr></table>';
        this.el.innerHTML = s;
        this._tableEl = this.el.firstChild;

        var tds = this.el.getElementsByTagName("td");
        this._td1El = tds[0];
        this._td2El = tds[1];
        this._td3El = tds[2];

        this._bodyEl = this._td2El.firstChild;
        this._borderEl = this._bodyEl;
        this.doUpdate();
		if(!this.headMenu){
			
			var menuItems = '<li id="_menu1" iconCls="icon-menu_close">关闭</li>'                
						+ '<li id="_menu2"  iconCls="icon-menu_closeothers">关闭其他</li>'
						+ '<li id="_menu3" iconCls="icon-menu_closeall">关闭所有</li>'       
						+ '<li id="_menu4" iconCls="icon-reload">刷新</li>';
			var menuEl = document.createElement("ul");
			menuEl.className="nui-contextmenu";
			menuEl.id="default_tabs_menu";
			menuEl.innerHTML = menuItems;
			this._bodyEl.appendChild(menuEl);
			this.headMenu = "default_tabs_menu";
		}
};
nui.Tabs.prototype.__OnClick = function (e) {
		
        var tab = this._getTabByEvent(e);
        if (!tab) return;
        if (tab.enabled) {
            var me = this;
            setTimeout(function () {
                if (mini.findParent(e.target, "mini-tab-close")) {
                    me._OnCloseButtonClick(tab, e);
                } else if (mini.findParent(e.target, "mini-tab-menu")) {
					var menu = nui.get(me.headMenu);
					if(me.headMenu == "default_tabs_menu"){
						menu._OnItemClick = function(item, htmlEvent){
							if(item.id == "_menu1"){
								me.removeTab(tab);
							}else if(item.id == "_menu2"){
								me.removeAll(tab);
							}else if(item.id == "_menu3"){
								me.removeAll();
							}else if(item.id == "_menu4"){
								me.reloadTab(tab);
							}
							if (this.hideOnClick) {
								if (this.isPopup) {
									this.hide();
								} else {
									this.hideItems();
								}
							}
						};
					}
					menu.showAtPos(e.pageX, e.pageY);
                } else {
                    var loadedUrl = tab.loadedUrl;
                    me._tryActiveTab(tab);
                    if (tab.refreshOnClick && tab.url == loadedUrl) {
                        me.reloadTab(tab);
                    }
                }
            }, 10);
        }
};
nui.Tabs.prototype.__OnDblClick = function(e){
		
		var tab = this._getTabByEvent(e);
        if (!tab) return;
		if(!tab.closeWhenDblClick){
			return;
		}
        if (tab.enabled) {
            var me = this;
            setTimeout(function () {
                if (mini.findParent(e.target, "mini-tab-close")) {
                    return;
                } else if (mini.findParent(e.target, "mini-tab-menu")) {
					return;
                } else {
                    me._OnCloseButtonClick(tab, e);
                }
            }, 10);
        }
};
nui.Tabs.prototype.setActiveIndex = function (value, load) {

        var tab = this.getTab(value);

        var acTab = this.getTab(this.activeIndex);

        var fire = tab != acTab;

        var el = this.getTabBodyEl(this.activeIndex);
        if (el) el.style.display = "none";
        if (tab) {
            this.activeIndex = this.tabs.indexOf(tab);
        } else {
            this.activeIndex = -1;
        }
        var el = this.getTabBodyEl(this.activeIndex);
        if (el) el.style.display = "";

        var el = this.getTabEl(acTab);
        if (el) mini.removeClass(el, this._tabActiveCls);

        var el = this.getTabEl(tab);
        if (el) mini.addClass(el, this._tabActiveCls);

        if (el && fire) {
            if (this.tabPosition == "bottom") {
                var tb = mini.findParent(el, "mini-tabs-header");
                if (tb) {
                    jQuery(this._headerEl).prepend(tb);
                }
            } else if (this.tabPosition == "left") {
                var td = mini.findParent(el, "mini-tabs-header").parentNode;
                if (td) {
                    td.parentNode.appendChild(td);
                }
            } else if (this.tabPosition == "right") {
                var td = mini.findParent(el, "mini-tabs-header").parentNode;
                if (td) {
                    jQuery(td.parentNode).prepend(td);
                }
            } else {
                var tb = mini.findParent(el, "mini-tabs-header");
                if (tb) {
                    this._headerEl.appendChild(tb);
                }
            }
            //var scrollLeft = this._headerEl.scrollLeft;
            this.doLayout();




            var rows = this.getTabRows();
            if (rows.length > 1) {

            } else {
                this._scrollToTab(this.activeIndex);
                this._doScrollButton();
            }

            for (var i = 0, l = this.tabs.length; i < l; i++) {
                var tabEl = this.getTabEl(this.tabs[i]);
                if (tabEl) {
                    mini.removeClass(tabEl, this._tabHoverCls);
                }
            }
        }
        var me = this;
        if (fire) {
            var e = {
                tab: tab,
                index: this.tabs.indexOf(tab),
                name: tab ? tab.name : ""
            };

            setTimeout(function () {    //延迟激发，这样确保UI都被继续创建后



                me.fire("ActiveChanged", e);

            }, 1);
        }

        //iframe load
        this._cancelLoadTabs(tab);
        if (load !== false) {
            if (tab && tab.url && !tab.loadedUrl) {
                var me = this;

                //setTimeout(function () {
                me.loadTab(tab.url, tab);
                //}, 1);
            }
        } else {
            //if (this.maskOnLoad) this.loading();
        }

        if (me.canLayout()) {
            try {
                mini.layoutIFrames(me.el);
            } catch (e) {
            }
        }
		
		if(this.activeIndex >=0 ){
			for (var i = 0, l = this.tabs.length; i < l; i++) {
				var t_tab = this.tabs[i];
				this.hideTabMenu(t_tab);
			}
			this.showTabMenu(this.tabs[this.activeIndex]);
		}
};
nui.Tabs.prototype.__hideContextMenu = function(){
	var contextMenu = nui.get(this.headMenu);
	if(contextMenu.isPopup){
		contextMenu.hide();
	}
	var activeTab = this.getActiveTab();
	for(var i = 0; i < this.tabs.length; i++){
		if(this.tabs[i] != activeTab){
			this.hideTabMenu(this.tabs[i]);
		}
	}
};
nui.Tabs.prototype.__OnMouseOver = function (e) {
		this.__hideContextMenu();
        var tab = this._getTabByEvent(e);
		var activeTab = this.getActiveTab();
		
        if (tab && tab.enabled) {
            var tabEl = this.getTabEl(tab);
            mini.addClass(tabEl, this._tabHoverCls);
            this.hoverTab = tab;
			
			if(tab != activeTab){
				this.showTabMenu(tab);
			}
        }
};
nui.Tabs.prototype.__OnMouseOut = function (e) {
		var activeTab = this.getActiveTab();
        if (this.hoverTab) {
            var tabEl = this.getTabEl(this.hoverTab);
            mini.removeClass(tabEl, this._tabHoverCls);
			
			if(this.hoverTab != activeTab  && !nui.get(this.headMenu).visible){
				this.hideTabMenu(this.hoverTab);
			}
        }
		this.hoverTab = null;
		
};

nui.Tabs.prototype.showTabMenu = function (tab){
	var tabEl = this.getTabEl(tab);
	if(tabEl){
		var menuEls = tabEl.getElementsByTagName("span");
		for(var i = menuEls.length - 1; i >= 0; i--){
			if(menuEls[i].className.indexOf("mini-tab-menu") != -1){
				menuEls[i].style.visibility = "visible";
				return;
			}
		}
	}
};
nui.Tabs.prototype.hideTabMenu = function (tab){
	var tabEl = this.getTabEl(tab);
	if(tabEl){
		var menuEls = tabEl.getElementsByTagName("span");
		for(var i = menuEls.length - 1; i >= 0; i--){
			if(menuEls[i].className.indexOf("mini-tab-menu") != -1){
				menuEls[i].style.visibility = "hidden";
				return;
			}
		}
	}
};
nui.Tabs.prototype._doUpdateTop = function () {
		
        //
        var isTop = this.tabPosition == "top";

        var s = '';
        if (isTop) {
            s += '<div class="mini-tabs-scrollCt">';
            s += '<div class="mini-tabs-nav"><a class="mini-tabs-leftButton" href="javascript:void(0)" hideFocus onclick="return false"></a><a class="mini-tabs-rightButton" href="javascript:void(0)" hideFocus onclick="return false"></a></div>';
            s += '<div class="mini-tabs-buttons"></div>';
        }
        s += '<div class="mini-tabs-headers">';
        var rows = this.getTabRows();
        for (var j = 0, k = rows.length; j < k; j++) {
            var tabs = rows[j];
            //var cls = "";
            s += '<table class="mini-tabs-header" cellspacing="0" cellpadding="0"><tr><td class="mini-tabs-space mini-tabs-firstSpace"><div></div></td>';
            for (var i = 0, l = tabs.length; i < l; i++) {
                var tab = tabs[i];
                var id = this._createTabId(tab);
                if (!tab.visible) continue;
                var index = this.tabs.indexOf(tab);
                var cls = tab.headerCls || "";
                if (tab.enabled == false) {
                    cls += ' mini-disabled';
                }
                s += '<td id="' + id + '" index="' + index + '"  class="mini-tab ' + cls + '" style="' + tab.headerStyle + '">';
                if (tab.iconCls || tab.iconStyle) {
                    s += '<span class="mini-tab-icon ' + tab.iconCls + '" style="' + tab.iconStyle + '"></span>';
                }
                s += '<span class="mini-tab-text">' + tab.title + '</span>';
                if (tab.showCloseButton) {
                    var ms = "";
                    if (tab.enabled) {
                        ms = 'onmouseover="mini.addClass(this, \'mini-tab-close-hover\')" onmouseout="mini.removeClass(this, \'mini-tab-close-hover\')"';
                    }
                    s += '<span class="mini-tab-close" ' + ms + '></span>';
                }
				if(tab.showMenuButton) {
					var ms = "";
                    if (tab.enabled) {
                        ms = 'onmouseover="mini.addClass(this, \'mini-tab-menu-hover\')" onmouseout="mini.removeClass(this, \'mini-tab-menu-hover\')"';
                    }
					var menuStyle='style="visibility: hidden"';
					if(i == 0){
						menuStyle = 'style="visibility: visible"';
					}
                    s += '<span class="mini-tab-menu" ' + ms + menuStyle +'></span>';
				}
                s += '</td>';
                if (i != l - 1) {
                    s += '<td class="mini-tabs-space2"><div></div></td>';
                }

            }
            s += '<td class="mini-tabs-space mini-tabs-lastSpace" ><div></div></td></tr></table>';
        }
        if (isTop) s += '</div>';
        s += '</div>';

        this._doClearElement();

        mini.prepend(this._td2El, s);

        var td = this._td2El;
        this._headerEl = td.firstChild.lastChild;
        if (isTop) {
            this._navEl = this._headerEl.parentNode.firstChild;
            this._leftButtonEl = this._navEl.firstChild;
            this._rightButtonEl = this._navEl.childNodes[1];
        }

        switch (this.tabAlign) {
            case "center":
                var hds = this._headerEl.childNodes;
                for (var i = 0, l = hds.length; i < l; i++) {
                    var tb = hds[i];
                    var tds = tb.getElementsByTagName("td");
                    tds[0].style.width = "50%";
                    tds[tds.length - 1].style.width = "50%";
                }
                break;
            case "right":
                var hds = this._headerEl.childNodes;
                for (var i = 0, l = hds.length; i < l; i++) {
                    var tb = hds[i];
                    var tds = tb.getElementsByTagName("td");
                    tds[0].style.width = "100%";
                }
                break;
            case "fit":
                break;
            default:
                var hds = this._headerEl.childNodes;
                for (var i = 0, l = hds.length; i < l; i++) {
                    var tb = hds[i];
                    var tds = tb.getElementsByTagName("td");
                    tds[tds.length - 1].style.width = "100%";
                }
                break;
        }
};
nui.Tabs.prototype._doUpdateLeft = function () {
		
		var s = '<table cellspacing="0" cellpadding="0"><tr>';
        var rows = this.getTabRows();
        for (var j = 0, k = rows.length; j < k; j++) {
            var tabs = rows[j];

            var cls = "";
            if (k > 1 && j != k - 1) {
                cls = "mini-tabs-header2";
            }
            s += '<td class="' + cls + '"><table class="mini-tabs-header" cellspacing="0" cellpadding="0">';
            s += '<tr ><td class="mini-tabs-space mini-tabs-firstSpace" ><div></div></td></tr>';

            for (var i = 0, l = tabs.length; i < l; i++) {
                var tab = tabs[i];
                var id = this._createTabId(tab);
                if (!tab.visible) continue;

                var index = this.tabs.indexOf(tab);

                var cls = tab.headerCls || "";
                if (tab.enabled == false) {
                    cls += ' mini-disabled';
                }
                s += '<tr><td id="' + id + '" index="' + index + '"  class="mini-tab ' + cls + '" style="' + tab.headerStyle + '">';
                if (tab.iconCls || tab.iconStyle) {
                    s += '<span class="mini-tab-icon ' + tab.iconCls + '" style="' + tab.iconStyle + '"></span>';
                }
                s += '<span class="mini-tab-text">' + tab.title + '</span>';
                if (tab.showCloseButton) {
                    var ms = "";
                    if (tab.enabled) {
                        ms = 'onmouseover="mini.addClass(this, \'mini-tab-close-hover\')" onmouseout="mini.removeClass(this, \'mini-tab-close-hover\')"';
                    }
                    s += '<span class="mini-tab-close" ' + ms + '></span>';
                }
				if(tab.showMenuButton) {
					var ms = "";
                    if (tab.enabled) {
                        ms = 'onmouseover="mini.addClass(this, \'mini-tab-menu-hover\')" onmouseout="mini.removeClass(this, \'mini-tab-menu-hover\')"';
                    }
					var menuStyle='style="visibility: hidden"';
					if(i == 0){
						menuStyle = 'style="visibility: visible"';
					}
                    s += '<span class="mini-tab-menu" ' + ms + menuStyle +'></span>';
				}
                s += '</td></tr>';

                if (i != l - 1) {
                    s += '<tr><td class="mini-tabs-space2"><div></div></td></tr>';
                }

            }

            s += '<tr ><td class="mini-tabs-space mini-tabs-lastSpace" ><div></div></td></tr>';
            s += '</table></td>';
        }

        s += '</tr ></table>';

        this._doClearElement();
        mini.addClass(this._td1El, "mini-tabs-header");
        mini.append(this._td1El, s);
        this._headerEl = this._td1El;
		

};
nui.Tabs.prototype.getAttrs = function (el) {
		
		var attrs = mini.Tabs.superclass.getAttrs.call(this, el);

        mini._ParseString(el, attrs,
            ["tabAlign", "tabPosition", "bodyStyle", "onactivechanged", "onbeforeactivechanged", "url",
                "ontabload", "ontabdestroy", "onbeforecloseclick", "oncloseclick",
                "titleField", "urlField", "nameField", "loadingMsg", "buttons", "headMenu"
             ]
        );
        mini._ParseBool(el, attrs,
            ["allowAnim", "showBody", "showHeader", "maskOnLoad", "plain"
             ]
        );
        mini._ParseInt(el, attrs,
            ["activeIndex"
             ]
        );

        var tabs = [];
        var nodes = mini.getChildNodes(el);
        for (var i = 0, l = nodes.length; i < l; i++) {
            var node = nodes[i];

            var o = {};
            tabs.push(o);

            o.style = node.style.cssText;
            mini._ParseString(node, o,
                ["name", "title", "url", "cls", "iconCls", "iconStyle", "headerCls", "headerStyle", "bodyCls", "bodyStyle",
                    "onload", "ondestroy", "data-options"
                 ]
            );
            mini._ParseBool(node, o,
                ["newLine", "visible", "enabled", "showCloseButton", "showMenuButton", "closeWhenDblClick", "refreshOnClick"
                 ]
            );

            //            var cs = mini.getChildNodes(node, true);
            //            o.body = cs;
            o.bodyParent = node;

            //data-options
            var options = o["data-options"];
            if (options) {

                options = eval("(" + options + ")");
                if (options) {
                    //attrs["data-options"] = options;
                    mini.copyTo(o, options);
                }
            }
        }
        attrs.tabs = tabs;

        return attrs;
		
		
};


/////////////////////////////////////////////////////////////////////
//Window属性，页面刷新时数据还可以带过来
/////////////////////////////////////////////////////////////////////

nui.Window.prototype._doLoad = function () {

    this._doRemoveIFrame(true);

    var st = new Date();
    var sf = this;

    this.loadedUrl = this.url;
    if (this.maskOnLoad) this.loading();

    jQuery(this._bodyEl).css("overflow", "hidden");

    var iframe = mini.createIFrame(this.url,
        function (_iframe, firstLoad) {
            var t = (st - new Date()) + sf._deferLoadingTime;

            if (t < 0) t = 0;
            setTimeout(function () {
                sf.unmask();
            }, t);

            //window.close
            try {
                sf._iframeEl.contentWindow.Owner = sf.Owner;
                sf._iframeEl.contentWindow.CloseOwnerWindow = function (action) {

                    sf.__HideAction = action;

                    //__onDestroy
                    var ret = true;
                    if (sf.__onDestroy) ret = sf.__onDestroy(action);
                    if (ret === false) {
                        return false;
                    }

                    var e = {
                        iframe: sf._iframeEl,
                        action: action
                    };

                    sf.fire("unload", e);

                    setTimeout(function () {
                        sf.destroy();
                    }, 10);

                };
            } catch (e) { }
            
			if (sf.__onLoad) sf.__onLoad();

			var e = {
				iframe: sf._iframeEl
			};

			//mini.repaint(sf.el);
			sf.fire("load", e);
            
        }
    );
    this._bodyEl.appendChild(iframe);
    this._iframeEl = iframe;

};

////////////////////////////////////////////////////////////////////////////////////////
//DataGrid 多选时，鼠标左键点击一行表示选中或者取消选中该行，同时不影响其他行的选择状态。
////////////////////////////////////////////////////////////////////////////////////////

nui.DataGrid.prototype.allowUnselect = true;


////////////////////////////////////////////////////////////////////////////////////////
//Tree控件拖动节点时，tips上显示的文字是拖动的节点名称
////////////////////////////////////////////////////////////////////////////////////////

nui.Tree.prototype._getDragText = function (dragNodes, selectNode) {
	var textField = this.getTextField();
	return selectNode[textField];
};
nui.Tree.prototype._OnDragStart = function (node, column) {
    var e = {
        node: node,
        nodes: this._getDragData(),
        column: column,
        cancel: false
    };
    e.record = e.node;
    e.records = e.nodes;
    e.dragText = this._getDragText(e.nodes, node);

    this.fire("dragstart", e);
    return e;
};

////////////////////////////////////////////////////////////////////////////////////////
//nui控件自动发送ajax请求，统一失败的方法
////////////////////////////////////////////////////////////////////////////////////////

nui.DataTree.prototype._doLoadAjax = nui.DataSource.prototype._doLoadAjax = nui.DataTable.prototype._doLoadAjax = function (params, success, error, complete, _successHandler) {
		
        params = params || {};
        if (mini.isNull(params.pageIndex)) params.pageIndex = this.pageIndex;
        if (mini.isNull(params.pageSize)) params.pageSize = this.pageSize;

        if (params.sortField) this.sortField = params.sortField;
        if (params.sortOrder) this.sortOrder = params.sortOrder;
        params.sortField = this.sortField;
        params.sortOrder = this.sortOrder;

        this.loadParams = params;

        var url = this._evalUrl();
        var type = this._evalType(url);

        var obj = mini._evalAjaxData(this.ajaxData, this);
        mini.copyTo(params, obj);

        var e = {
            url: url,
            async: this.ajaxAsync,
            type: type,
            data: params,
            params: params,
            cache: false,
            cancel: false
        };

        //ajaxOptions：async, type, dateType, contentType等都能在beforeload前修改。                

        mini.copyTo(e, this.ajaxOptions);

        this._OnBeforeLoad(e);
        if (e.cancel == true) {
            params.pageIndex = this.getPageIndex();
            params.pageSize = this.getPageSize();
            return;
        }

        //历史遗留问题：兼容e.params参数
        if (e.data != e.params && e.params != params) {
            e.data = e.params;
        }

        if (e.url != url && e.type == type) {
            e.type = this._evalType(e.url);
        }

        //处理自定义field
        var o = {};
        o[this.pageIndexField] = params.pageIndex;
        o[this.pageSizeField] = params.pageSize;
        if (params.sortField) o[this.sortFieldField] = params.sortField;
        if (params.sortOrder) o[this.sortOrderField] = params.sortOrder;

        if (this.startField && this.limitField) {
            o[this.startField] = params.pageIndex * params.pageSize;
            o[this.limitField] = params.pageSize;
        }

        //        delete params.pageIndex;
        //        delete params.pageSize;
        //        delete params.sortField;
        //        delete params.sortOrder;
        mini.copyTo(params, o);


        if (this.sortMode == 'client') {
            params[this.sortFieldField] = "";
            params[this.sortOrderField] = "";
        }

        //保存记录值
        var selected = this.getSelected();
        this._selectedValue = selected ? selected[this.idField] : null;
        if (mini.isNumber(this._selectedValue)) this._selectedValue = String(this._selectedValue);

        var me = this;
        me._resultObject = null;
        /*
        e.textStatus
        success     交互成功
        error       网络交互失败：404,500
        timeout     交互超时
        abort       交互终止
        servererror 网络交互成功，返回json，但是业务逻辑错误
        e.errorCode     服务端错误码
        e.errorMsg      错误描述信息
        e.stackTrace    错误定位信息
        */
        
        var async = e.async;
        mini.copyTo(e, {
            success: function (text, textStatus, xhr) {
                if (!text || text == "null") {
                    text = '{ tatal: 0, data: [] }';
                }
                
                delete e.params;
                var obj = { text: text, result: null, sender: me, options: e, xhr: xhr };
                var result = null;
                try {
                    //mini_doload(obj);
                    result = obj.result;
                    if (!result) {
                        result = mini.decode(text);
                    }
                } catch (ex) {
                    if(xhr.status == 403){
						$G.GcdsError(message.Gcdsui.noRight);
						return;
					}else if(xhr.status == 200){
						//Session超时或当前Session被其他Session逐出！
						top.window.location = $G.formatUrl("login/login.do?error=true");
						return;
					}
                }

                if (result && !mini.isArray(result)) {
                    result.total = parseInt(mini._getMap(me.totalField, result)); //result[me.totalField];
                    result.data = mini._getMap(me.dataField, result); //result[me.dataField];
                } else {
                    if (result == null) {
                        result = {};
                        result.data = [];
                        result.total = 0;
                    } else if (mini.isArray(result)) {
                        var r = {};
                        r.data = result;
                        r.total = result.length;
                        result = r;
                    }
                }
                if (!result.data) result.data = [];
                if (!result.total) result.total = 0;
                me._resultObject = result;

                if (!mini.isArray(result.data)) {
                    result.data = [result.data];
                }

                var ex = {
                    xhr: xhr,
                    text: text,
                    textStatus: textStatus,
                    result: result,
                    total: result.total,
                    data: result.data.clone(),

                    pageIndex: params[me.pageIndexField],
                    pageSize: params[me.pageSizeField]
                };


                var error = mini._getMap(me.errorField, result);
                var errorMsg = mini._getMap(me.errorMsgField, result);
                var stackTrace = mini._getMap(me.stackTraceField, result);

                if (mini.isNumber(error) && error != 0 || error === false) {
                    //server error
                    ex.textStatus = "servererror";
                    ex.errorCode = error;
                    ex.stackTrace = stackTrace || "";
                    ex.errorMsg = errorMsg || "";
                    if (mini_debugger == true) {
                        $G.GcdsError(url + "\n" + ex.textStatus + "\n" + ex.errorMsg + "\n" + ex.stackTrace);
                    }
                    me.fire("loaderror", ex);
                    if (error) error.call(me, ex);
                } else {

                    if (_successHandler) {
                        _successHandler(ex);
                    } else {
                        //pager
                        me.pageIndex = ex.pageIndex;
                        me.pageSize = ex.pageSize;
                        me.setTotalCount(ex.total);

                        //success
                        me._OnPreLoad(ex);

                        //data
                        me.setData(ex.data);

                        //checkSelectOnLoad
                        if (me._selectedValue && me.checkSelectOnLoad) {
                            var o = me.getbyId(me._selectedValue);
                            if (o) {
                                me.select(o);
                            }
                        }
                        //selectOnLoad                    
                        if (me.getSelected() == null && me.selectOnLoad && me.getDataView().length > 0) {
                            me.select(0);
                        }
                        me.fire("load", ex);

                        if (success) {
                            if (async) {
                                setTimeout(function () {
                                    success.call(me, ex);
                                }, 20);
                            } else {
                                success.call(me, ex);
                            }
                        }
                    }
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                if (textStatus == "abort") return;

                var ex = {
                    xhr: xhr,
                    text: xhr.responseText,
                    textStatus: textStatus
                };
                ex.errorMsg = xhr.responseText;
                ex.errorCode = xhr.status;


                if (mini_debugger == true) {
                    //alert(url + "\n" + ex.errorCode + "\n" + ex.errorMsg);
                }

                me.fire("loaderror", ex);
                if (error) error.call(me, ex);
            },
            complete: function (xhr, textStatus) {
                var ex = {
                    xhr: xhr,
                    text: xhr.responseText,
                    textStatus: textStatus
                };
                me.fire("loadcomplete", ex);
                if (complete) complete.call(me, ex);
                me._xhr = null;
            }
        });
        if (this._xhr) {
            //this._xhr.abort();
        }
        this._xhr = mini.ajax(e);
};

nui.ListBox.prototype._doLoad = function (params) {

        try {
            var url = eval(this.url);
            if (url != undefined) {
                this.url = url;
            }
        } catch (e) { }
        var url = this.url;

        var ajaxMethod = mini.ListControl.ajaxType;
        if (url) {
            if (url.indexOf(".txt") != -1 || url.indexOf(".json") != -1) {
                ajaxMethod = "get";
            }
        }

        var obj = mini._evalAjaxData(this.ajaxData, this);
        mini.copyTo(params, obj);

        var e = {
            url: this.url,
            async: false,
            type: this.ajaxType ? this.ajaxType : ajaxMethod,
            data: params,
            params: params,
            cache: false,
            cancel: false
        };
        this.fire("beforeload", e);
        if (e.data != e.params && e.params != params) {
            e.data = e.params;
        }
        if (e.cancel == true) return;

        var sf = me = this;
        var url = e.url;
        mini.copyTo(e, {
            success: function (text, textStatus, xhr) {
                delete e.params;
                var obj = { text: text, result: null, sender: me, options: e, xhr: xhr };
                var result = null;
                try {
                    //mini_doload(obj);
                    result = obj.result;
                    if (!result) {
                        result = mini.decode(text);
                    }
                } catch (ex) {
                	if(textStatus == 403){
						$G.GcdsError(message.Gcdsui.noRight);
					}else if(textStatus == 200){
						//Session超时或当前Session被其他Session逐出！
						top.window.location = $G.formatUrl("login/login.do?error=true");
					}
                }
                if (mini.isArray(result)) result = { data: result };
                if (sf.dataField) {
                    result.data = mini._getMap(sf.dataField, result);
                }
                if (!result.data) result.data = [];

                var ex = { data: result.data, cancel: false };
                sf.fire("preload", ex);
                if (ex.cancel == true) return;

                sf.setData(ex.data);

                sf.fire("load");

                setTimeout(function () {
                    sf.doLayout();
                }, 100);

            },
            error: function (xhr, textStatus, errorThrown) {
                var e = {
                    xhr: xhr,
                    text: xhr.responseText,
                    textStatus: textStatus,
                    errorMsg: xhr.responseText,
                    errorCode: xhr.status
                };
                if (mini_debugger == true) {
                    //alert(url + "\n" + e.errorCode + "\n" + e.errorMsg);
                }
                sf.fire("loaderror", e);
            }
        });

        this._ajaxer = mini.ajax(e);
};

nui.ajax = function (options) {
		var t_options = {};
		nui.copyTo(t_options, options);
		t_options.error = function(data, textStatus, jqXHR){
				if(options.error){
					options.error(data, textStatus, jqXHR);
				}
				try{
					var jsonData = $.parseJSON(data.responseText);
					if(jsonData.isBizErr){//业务错误标识
						$G.GcdsError(jsonData.errMsg);
					}else{
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
		return window.jQuery.ajax(t_options);
};

////////////////////////////////////////////////////////////////////////////////////////
//ckeditor 禁用最大化按钮
////////////////////////////////////////////////////////////////////////////////////////

nui.Richtext.prototype._doInitEditor = function () {
    if (this.isRender() == false) return;
    if (this.editor) return;
    var me = this;
    this._editor.set({
    	name: me.name,
    	id: me.id + "_editor"
    });
    this._fromEditor.id = this.id + "_fromEditor";
	this.editor = CKEDITOR.replace(me._fromEditor.id, {
		width:  me.width.replace("px", ""),
		height: me.height.replace("px", "")-100,
		readOnly: me.readOnly,
		removePlugins:'maximize,about,flash,save'
	});
	this.editor.isReady = false;
	this.editor.on("instanceReady", function(e) {
		e.editor.resize(e.editor.config.width, me.height.replace("px", ""));
		e.editor.isReady = true;
    	me.setValue(me.value);
    	me.fire("initeditor");
	});
	this.editor.on("resize", function(e) {
		var height = e.editor.container.$.clientHeight;
		mini.Richtext.superclass.setHeight.call(me, height);
	});
};

////////////////////////////////////////////////////////////////////////////////////////
//Tree的loadNode方法提供回调方法参数,options:{expand:false, success:function(e){},error:function(e){}}
////////////////////////////////////////////////////////////////////////////////////////

nui.DataTree.prototype.loadNode = function(node, options) {
	var expand = true;
	var succFunc = null;
	var errorFunc = null;
	if (options) {
		if (options.expand) {
			expand = options.expand;
		}
		if (options.success) {
			succFunc = options.success;
		}
		if (options.error) {
			errorFunc = options.error;
		}
	}

	this._loadingNode = node;
	var e = {
		node : node
	};
	this.fire("beforeloadnode", e);
	//

	var time = new Date();

	var me = this;
	me._doLoadAjax(me.loadParams, null, errorFunc, null, function(e) {
		var t = new Date() - time;
		if (t < 60)
			t = 60 - t;

		setTimeout(function() {

			//success
			e.node = node;
			me._OnPreLoad(e);

			e.node = me._loadingNode;
			me._loadingNode = null;

			var oldNodes = node[me.nodesField];
			me.removeNodes(oldNodes);

			var nodes = e.data;
			if (nodes && nodes.length > 0) {
				me.addNodes(nodes, node);

				if (expand !== false) {
					me.expand(node, true);
				} else {
					me.collapse(node, true);
				}
			} else {
				delete node[me.leafField];
				me.expand(node, true);
			}
			if(succFunc){
				succFunc(e);
			}

			me.fire("loadnode", e);
			me.fire("load", e);
		}, t);
	}, true);
};

////////////////////////////////////////////////////////////////////////////////////////
//ListBox 多列时，如果单元格文字长度超过单元格长度，鼠标移动到该文字上时应显示全部文字
////////////////////////////////////////////////////////////////////////////////////////

nui.ListBox.prototype._initEvents = function () {
      mini.ListBox.superclass._initEvents.call(this);
      mini._BindEvents(function () {
          mini_onOne(this._viewEl, "scroll", this.__OnScroll, this);
			mini.on(this._viewEl, "mouseover", this.__OnListBoxCellMouseOver, this);
      }, this);

};
nui.ListBox.prototype.__OnListBoxCellMouseOver = function(e) {
		if(mini.hasClass(e.target, "mini-listbox-cell-inner")){
          var cellEl = e.target;
          
          if (cellEl.scrollWidth > cellEl.clientWidth) {
              var s = cellEl.innerText || cellEl.textContent || "";
              cellEl.title = s.trim();
          } else {
              cellEl.title = "";
          }

          return;
      }
};
nui.ListBox.prototype.doUpdate = function () {
      if (this._allowUpdate === false) return;
      var hasColumns = this.columns && this.columns.length > 0;
      if (hasColumns) {
          mini.addClass(this.el, "mini-listbox-showColumns");
      } else {
          mini.removeClass(this.el, "mini-listbox-showColumns");
      }
      this._headerEl.style.display = hasColumns ? "" : "none";

      var sb = [];
      if (hasColumns) {
          sb[sb.length] = '<table class="mini-listbox-headerInner" cellspacing="0" cellpadding="0"><tr>';
          var ckAllId = this.uid + "$ck$all";
          sb[sb.length] = '<td class="mini-listbox-checkbox"><input type="checkbox" id="' + ckAllId + '"></td>';
          for (var j = 0, k = this.columns.length; j < k; j++) {
              var column = this.columns[j];
              var header = column.header;
              if (mini.isNull(header)) header = '&nbsp;';

              var w = column.width;
              if (mini.isNumber(w)) w = w + "px";

              sb[sb.length] = '<td class="';
              if (column.headerCls) sb[sb.length] = column.headerCls;
              sb[sb.length] = '" style="';
              if (column.headerStyle) sb[sb.length] = column.headerStyle + ";";
              if (w) {
                  sb[sb.length] = 'width:' + w + ';';
              }
              if (column.headerAlign) {
                  sb[sb.length] = 'text-align:' + column.headerAlign + ';';
              }
              sb[sb.length] = '">';
              sb[sb.length] = header;
              sb[sb.length] = '</td>';
          }
          sb[sb.length] = '</tr></table>';
      }
      this._headerEl.innerHTML = sb.join('');

      var sb = [];
      var data = this.data;

      sb[sb.length] = '<table class="mini-listbox-items" cellspacing="0" cellpadding="0">';

      if (this.showEmpty && data.length == 0) {

          sb[sb.length] = '<tr><td colspan="20">' + this.emptyText + '</td></tr>';
      } else {
          this._doNullItem();

          for (var i = 0, l = data.length; i < l; i++) {
              var item = data[i];

              var rowClsIndex = -1;
              var rowCls = " ";
              var rowStyleIndex = -1;
              var rowStyle = " ";

              sb[sb.length] = '<tr id="';
              sb[sb.length] = this._createItemId(i);
              sb[sb.length] = '" index="';
              sb[sb.length] = i;
              sb[sb.length] = '" class="mini-listbox-item ';

              if (item.enabled === false) {
                  sb[sb.length] = ' mini-disabled ';
              }

              rowClsIndex = sb.length;
              sb[sb.length] = rowCls;
              sb[sb.length] = '" style="';
              rowStyleIndex = sb.length;
              sb[sb.length] = rowStyle;
              sb[sb.length] = '">';

              var ckid = this._createCheckId(i);
              var ckName = this.name;
              var ckValue = this.getItemValue(item);

              var disable = '';
              if (item.enabled === false) {
                  disable = 'disabled';
              }
              sb[sb.length] = '<td class="mini-listbox-checkbox"><input ' + disable + ' id="' + ckid + '" type="checkbox" ></td>';

              if (hasColumns) {
                  for (var j = 0, k = this.columns.length; j < k; j++) {
                      var column = this.columns[j];

                      var e = this._OnDrawCell(item, i, column);

                      var w = column.width;
                      if (typeof w == "number") w = w + "px";

                      sb[sb.length] = '<td class="mini-listbox-cell-inner ';
                      if (e.cellCls) sb[sb.length] = e.cellCls;
                      sb[sb.length] = '" style="';
                      if (e.cellStyle) sb[sb.length] = e.cellStyle + ";";
                      if (w) {
                          sb[sb.length] = 'width:' + w + ';';
                      }
                      if (column.align) {
                          sb[sb.length] = 'text-align:' + column.align + ';';
                      }
                      sb[sb.length] = '">';
                      sb[sb.length] = e.cellHtml;
                      sb[sb.length] = '</td>';

                      if (e.rowCls) rowCls = e.rowCls;
                      if (e.rowStyle) rowStyle = e.rowStyle;
                  }
              } else {
                  var e = this._OnDrawCell(item, i, null);
                  sb[sb.length] = '<td class="';
                  if (e.cellCls) sb[sb.length] = e.cellCls;
                  sb[sb.length] = '" style="';
                  if (e.cellStyle) sb[sb.length] = e.cellStyle;
                  sb[sb.length] = '">';
                  sb[sb.length] = e.cellHtml;
                  sb[sb.length] = '</td>';

                  if (e.rowCls) rowCls = e.rowCls;
                  if (e.rowStyle) rowStyle = e.rowStyle;
              }

              sb[rowClsIndex] = rowCls;
              sb[rowStyleIndex] = rowStyle;

              sb[sb.length] = '</tr>';
          }
      }
      sb[sb.length] = '</table>';


      var innerHTML = sb.join("");

      this._viewEl.firstChild.innerHTML = innerHTML;

      this._doSelects();
      
      this.doLayout();
};

////////////////////////////////////////////////////////////////////////////////////////
//DataGrid/ListBox 全选框不生效
////////////////////////////////////////////////////////////////////////////////////////

nui.CheckColumn = function (config) {
    return nui.copyTo(
        { width: 30, cellCls: "mini-checkcolumn", headerCls: "mini-checkcolumn", hideable: true,
            _multiRowSelect: true,   //
            header: function (column) {

                //if(this.multiSelect == false) debugger
                var id = this.uid + "checkall";
                var s = '<input type="checkbox" id="' + id + '" />';
                if (this.multiSelect == false) s = "";
                return s;
            },
            getCheckId: function (record, column) {
                return this._gridUID + "$checkcolumn$" + record[this._rowIdField] + "$" + column._id;
            },
            init: function (grid) {
                grid.on("selectionchanged", this.__OnSelectionChanged, this);
                grid.on("HeaderCellClick", this.__OnHeaderCellClick, this);
            },
            renderer: function (e) {
                var id = this.getCheckId(e.record, e.column);
                var selected = e.sender.isSelected ? e.sender.isSelected(e.record) : false;

                var type = "checkbox";

                var grid = e.sender;
                if (grid.getMultiSelect() == false) type = "radio";

                var html = '<input type="' + type + '" id="' + id + '" ' + (selected ? "checked" : "") + ' hidefocus style="outline:none;" onclick="return false"/>';
                html += '<div class="mini-grid-radio-mask"></div>';
                return html;
                //return '<input type="' + type + '" id="' + id + '" ' + (selected ? "checked" : "") + ' hidefocus style="outline:none;" onmousedown="this._checked = this.checked;" onclick="this.checked = this._checked"/>';
            },
            __OnHeaderCellClick: function (e) {
                var grid = e.sender;
                if (e.column != this) return;
                var id = grid.uid + "checkall";
                var ck = document.getElementById(id);
                if (ck) {

                    if (grid.getMultiSelect()) {
                        if (ck.checked) {
                            //grid.selectAll();
                            grid.deselectAll();
                            var list = grid.getDataView();
                            grid.selects(list);
                        } else {
                            grid.deselectAll();
                        }
                    } else {
                        grid.deselectAll();
                        if (ck.checked) {
                            grid.select(0);
                        }
                    }
                    grid.fire("checkall");
                }
            },
            __OnSelectionChanged: function (e) {
                var grid = e.sender;
                var records = grid.toArray();
                var me = this;

                for (var i = 0, l = records.length; i < l; i++) {
                    var record = records[i];
                    var select = grid.isSelected(record);
                    var id = me.getCheckId(record, me); //grid.uid + "$checkcolumn$" + record[grid._rowIdField];
                    var ck = document.getElementById(id);

                    if (ck) ck.checked = select;
                }

                if (!this._timer) {
                    this._timer = setTimeout(function () {
                        me._doCheckState(grid);
                        me._timer = null;
                    }, 10);
                }
            },
            _doCheckState: function (grid) {

                //处理顶部的checkbox
                var id = grid.uid + "checkall";
                var ck = document.getElementById(id);
				var records = grid.toArray();
				var recordsLength = records.length;
                if (ck) {
					var selectNum = grid.getSelecteds().length;
                    if (recordsLength == selectNum) {
                        ck.checked = true;
                    } else {
                        ck.checked = false;
                    }
                }
            }
        }, config);
};
nui._Columns["checkcolumn"] = nui.CheckColumn;


nui.ListBox.prototype._OnItemClick = function (item, e) {
		
        if (this.isReadOnly() || this.enabled == false || item.enabled === false) {
            e.preventDefault();
            return;
        }

        var value = this.getValue();

        if (this.multiSelect) {
            if (this.isSelected(item)) {
                this.deselect(item);
                if (this._selected == item) {
                    this._selected = null;
                }
            } else {
                this.select(item);
                this._selected = item;
            }
            this._OnSelectionChanged();
        } else {
            if (!this.isSelected(item)) {
                this.deselectAll();
                this.select(item);
                this._selected = item;
                this._OnSelectionChanged();
            }
        }

        if (value != this.getValue()) {
            this._OnValueChanged();
        }
		
		//多选情况下，设置全选选择框的选择状态
		if (this.multiSelect) {
			var ckAllId = this.uid + "$ck$all";
			var ck = document.getElementById(ckAllId);
			if(ck){
				var total = this.getCount();
				var selectedsNum = 0;
				if(this._selecteds){
					selectedsNum = this._selecteds.length;
				}
				if(total == selectedsNum){
					ck.checked = true;
				}else{
					ck.checked = false;
				}
				
			}
		}
		

        var e = {
            item: item,
            htmlEvent: e
        };
        this.fire("itemclick", e);
};

////////////////////////////////////////////////////////////////////////////////////////
//Alert、Confirm按钮位置调整、带图标
////////////////////////////////////////////////////////////////////////////////////////
nui.MessageBox.buttonText = {
		ok: message.Gcdsui.ok,
        cancel: message.Gcdsui.cancel,
        yes: message.Gcdsui.yes,
        no: message.Gcdsui.no
};

nui.MessageBox.alertTitle = message.Gcdsui.alertTitle;
nui.MessageBox.confirmTitle = message.Gcdsui.confirmTitle;
nui.MessageBox.prompTitle = message.Gcdsui.prompTitle;
nui.MessageBox.prompMessage = message.Gcdsui.prompMessage;

nui.alert = function (message, title, callback) {
    return mini.MessageBox.show({
        minWidth: 250,
        title: title || mini.MessageBox.alertTitle,
        buttons: ["ok"],
		buttonsIconCls: [""],
        message: message,
        iconCls: "mini-messagebox-warning",
        callback: callback
    });
};

nui.info = function (message, title, callback) {
    return mini.MessageBox.show({
        minWidth: 250,
        title: title || mini.MessageBox.alertTitle,
        buttons: ["ok"],
		buttonsIconCls: [""],
        message: message,
        iconCls: "mini-messagebox-info",
        callback: callback
    });
};

nui.error = function (message, title, callback) {
    return mini.MessageBox.show({
        minWidth: 250,
        title: title || mini.MessageBox.alertTitle,
        buttons: ["ok"],
		buttonsIconCls: [""],
        message: message,
        iconCls: "mini-messagebox-error",
        callback: callback
    });
};

nui.confirm = function (message, title, callback) {
	return nui.MessageBox.show({
        minWidth: 250,
        title: title || mini.MessageBox.confirmTitle,
        buttons: ["ok", "cancel"],
		buttonsIconCls: ["", ""],
        message: message,
        iconCls: "mini-messagebox-question",
        callback: callback
    });
};

nui.MessageBox.show = function (options) {

    options = mini.copyTo({
        width: "auto",
        height: "auto",
        showModal: true,

        timeout: 0,

        minWidth: 150,
        maxWidth: 800,
        minHeight: 50,
        maxHeight: 350,

        showHeader: true,
        title: "",
        titleIcon: "",
        iconCls: "",
        iconStyle: "",
        message: "",
        html: "",

        spaceStyle: "margin-right:15px",

        showCloseButton: true,
        buttons: null,
		buttonsIconCls: null,
       // buttonWidth: 58,
        callback: null
    }, options);

    options.message = String(options.message);

    var callback = options.callback;

    var control = new mini.Window();

    control.setBodyStyle("overflow:hidden");
    control.setShowModal(options.showModal);
    control.setTitle(options.title || "");
    control.setIconCls(options.titleIcon);
    control.setShowHeader(options.showHeader);

    control.setShowCloseButton(options.showCloseButton);

    var id1 = control.uid + "$table", id2 = control.uid + "$content";

    var icon = '<div class="' + options.iconCls + '" style="' + options.iconStyle + '"></div>';
    var s = '<table class="mini-messagebox-table" id="' + id1 + '" style="" cellspacing="0" cellpadding="0"><tr><td>'
            + icon + '</td><td id="' + id2 + '" class="mini-messagebox-content-text">'
            + (options.message || "") + '</td></tr></table>';

    //var s = "";
    //control.setShowHeader(false);

    var ws = '<div class="mini-messagebox-content"></div>'
         + '<div class="mini-messagebox-buttons"></div>';
    control._bodyEl.innerHTML = ws;
    var contentEl = control._bodyEl.firstChild;
    if (options.html) {
        if (typeof options.html == "string") {
            contentEl.innerHTML = options.html;
        } else if (mini.isElement(options.html)) {
            contentEl.appendChild(options.html);
        }
    } else {
        contentEl.innerHTML = s;
    }

    control._Buttons = [];

    var buttonsEl = control._bodyEl.lastChild;
    if (options.buttons && options.buttons.length > 0) {
        for (var i = 0, l = options.buttons.length; i < l; i++) {
            var button = options.buttons[i];
            var text = mini.MessageBox.buttonText[button];
            if (!text) text = button;

            var btn = new mini.Button();
            btn.setText(text);
            btn.setWidth(options.buttonWidth);
            btn.render(buttonsEl);
            btn.action = button;
			if(options.buttonsIconCls && options.buttonsIconCls.length > i && options.buttonsIconCls[i]){
				btn.setIconCls(options.buttonsIconCls[i]);
			}
            btn.on("click", function (e) {
                var button = e.sender;
                if (callback) if (callback(button.action) === false) return;
                mini.MessageBox.hide(control);
            });

            if (i != l - 1) {
                btn.setStyle(options.spaceStyle);
            }

            control._Buttons.push(btn);
        }
    } else {
        buttonsEl.style.display = "none";
    }

    control.setMinWidth(options.minWidth);
    control.setMinHeight(options.minHeight);
    control.setMaxWidth(options.maxWidth);
    control.setMaxHeight(options.maxHeight);
    control.setWidth(options.width);
    control.setHeight(options.height);
    control.show(options.x, options.y, { animType: options.animType });




    var width = control.getWidth();
    control.setWidth(width);
    var height = control.getHeight();
    control.setHeight(height);

    var tb = document.getElementById(id1);
    if (tb) {
        tb.style.width = "100%";
    }
    var td = document.getElementById(id2);
    if (td) {
        td.style.width = "100%";
    }
    //}

    var firstButton = control._Buttons[0];
    if (firstButton) {
        firstButton.focus();
    } else {
        control.focus();
    }

    control.on("beforebuttonclick", function (e) {
        if (callback) callback("close");
        e.cancel = true;
        mini.MessageBox.hide(control);
    });
    mini.on(control.el, "keydown", function (e) {
        //            if (e.keyCode == 27) {
        //                if (callback) callback("close");
        //                e.cancel = true;
        //                mini.MessageBox.hide(control);
        //            }
    });


    if (options.timeout) {
        setTimeout(function () {
            mini.MessageBox.hide(control.uid);
        }, options.timeout);
    }

    return control.uid;
};

////////////////////////////////////////////////////////////////////////////////////////
//DataGrid前段排序，分页情况下，加载完分页数据后再做一次前端排序。每一次load时都清除下排序项
////////////////////////////////////////////////////////////////////////////////////////

nui.DataGrid.prototype.load = function (params, success, error, complete) {
    this._dataSource.load(params, success, error, complete);
	if(this._dataSource.sortMode == "client"){
		this.clearSort();
	}
};
nui.DataGrid.prototype.gotoPage = function (index, size) {
	if(this._dataSource.sortField){
		var dataType = this._columnModel._getDataTypeByField(this._dataSource.sortField);
		this._dataSource.sortColumnDataType = dataType;
	}
    this._dataSource.gotoPage(index, size);
};
nui.DataTable.prototype.gotoPage = function (index, size) {
    var params = this.loadParams || {};
    if (mini.isNumber(index)) params.pageIndex = index;
    if (mini.isNumber(size)) params.pageSize = size;
    this.load(params,function(){
		if(this.sortMode == "client" && this.sortField){
			this._doClientSortField(this.sortField,this.sortOrder,this.sortColumnDataType);
		}
	});
};

nui.VTypes.rangeChar = function (v, args){
    if (mini.isNull(v) || v === "") return true;
    //if (!v) return false;
    var min = parseFloat(args[0]), max = parseFloat(args[1]);
    if (isNaN(min) || isNaN(max)) return true;
    function isChinese(v) {
    	var re=new RegExp("[^\x00-\xff]","g");
        if (re.test(v)) return true;
        return false;
    }

    var len = 0;
    var ss = String(v).split("");
    for (var i = 0, l = ss.length; i < l; i++) {
        if (isChinese(ss[i])) {
            len += 2;
        } else {
            len += 1;
        }
    }

    if (min <= len && len <= max) return true;
    return false;	
};