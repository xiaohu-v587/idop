message = new GcdsUIMessage();

function GcdsUIMessage(){};

GcdsUIMessage.prototype.common = {
	notSaveData: "请先保存数据！",
	notSaveModifyData:"请先保存已修改的数据！",
	dataModify: "数据被修改了，是否先保存？",
	chooseMsg: "请选择一条记录！",
	chooseMsgUpdate: "请选中一条记录修改！",
	deleteUnitConfirmMsg: "确定删除选中记录？",
	executeSuccess: "执行成功！",
	prompt:"提示",
	uploadFile:"文件上传",
	uploadPrompt:"请选择上传文件",
	beginDateValidationMsg: "开始日期不能大于结束日期！",
	endDateValidationMsg: "结束日期不能小于开始日期！",
	exportSucess:"导出成功！请到以下路径查看：",
	exportFailed:"导出失败！请联系系统管理员"
};

GcdsUIMessage.prototype.login = {
	inputUsername:"用户标识不能为空",
	inputPassword:"密码不能为空",
	inputCaptcha:"验证码不能为空",
	reachMaxSession:"已达到系统最大在线用户数"
};

GcdsUIMessage.prototype.password = {
	confirmPwdNotSame: "您输入的两次新密码不一致!",
	passwordCombination:"密码安全级别不足,组合密码必须大于{0}位且包含字母、数字和特殊字符,请重新设置!",
	defaultpassword: "系统默认密码是：",
	title: "修改密码"
};

GcdsUIMessage.prototype.button = {
	advanceSearch: "高级查询",
	closeSearch: "关闭查询"
};

GcdsUIMessage.prototype.role = {
	addRole: "添加角色",
	editRole: "编辑角色",
	confirmDeleteRole: "确定删除选中的角色？",
	errorDeleteSystemRole: "{0}是管理角色，不能删除!",
	staffInfo: "用户信息",
	confirmAddStaff: "确定添加选中的用户？",
	unitInfo: "组织机构",
	confirmDeleteStaff: "确定删除选中的用户？"
	
};

GcdsUIMessage.prototype.staff = {
	addStaff: "添加用户",
	editStaff: "编辑用户",
	confirmDeleteStaff: "确定删除选中的用户？",
	roleInfo: "角色信息",
	confirmAddRole: "确定添加选中的角色？",
	unitInfo: "组织机构",
	confirmDeleteRole: "确定删除选中的角色？",
	noSelectParentNode: "不能选择父节点!",
	invalidNotesid : "NotesID为6位数字！",
	invalidIDNumber: "身份格式不正确，请输重新输入！",
	invalidEmail: "邮件格式不正确，请输重新输入！",
	confirmLockStaff: "确定锁定选中的用户？",
	confirmUnLockStaff: "确定解锁选中的用户？",
	confirmChangePassword: "确定修改密码？",
	lockedStaff: "用户已被锁定！",
	unLockedStaff: "正常用户，不需要解锁！",
	permissionInfo: "用户权限信息",
	invalidPhone: "电话号码格式不正确，请输入正确的电话号码！",
	invalidBirthday: "出生日期必需小于当前时间，请重新输入！",
	lockCancellationStaff: "注销用户，不需要锁定！",
	unlockCancellationStaff: "注销用户，不需要解锁！"
};
	
GcdsUIMessage.prototype.ci = {
	roleCiDetail: "角色操作流水详情",
	staffCiDetail:"用户操作流水详情",
	roleStaffCiDetail: "角色用户关联操作流水详情",
	check:"确定复核",
	cancelCheck:"确定撤销复核",
	checkNote:"该记录未审核，请先审核！",
	checkNote1:" 修改未审核，请先审核！",
	roleName:"角色 ",
	staffName:"用户 ",
	successText:"操作成功！",
	successFHText:"操作成功，待复核生效！"
};
GcdsUIMessage.prototype.menu = {
	notChangeMenuNode: "请先选择菜单节点!",
	notAddNode: "该菜单有对应的菜单路径，不能添加子菜单！",
	newNode:"新建节点1",
	urlChoose:"URL选择",
	chooseMenuTree:"选择菜单树",
	menuTree:"菜单树",
	delMenu:"确定删除选中菜单？",
	menuName:"菜单名称已存在，请重新填写！",
	menuRelationUrl:"关联URL"
};
GcdsUIMessage.prototype.url = {
	success:"执行成功！",
	isUrlScan:"您确定使用URL自动扫描吗？"
};


GcdsUIMessage.prototype.accessMonitor = {
	titleText: "功能访问统计",
	frequency:"次数",
	valueSuffix:"次",
	visit:"访问次数",
	noData:"没有数据。",
	hour:"点",
	day:"日",
	month:"月",
	accessTitle:"功能统计查询",
	dayAccess:"日粒度统计查询",
	monthAccess:"月粒度统计查询",
	yearAccess:"年粒度统计查询"
};

GcdsUIMessage.prototype.auditlog = {
	edit: "编辑",
	notSelectRow: "请选中一条记录",
	open: "开启",
	close: "关闭"
};

GcdsUIMessage.prototype.log4jconfig = {
	add: "新增",
	edit: "编辑",
	notSelectRow: "请选中一条记录",
	resetConfirm: "确定恢复所有日志的生产级别？"
};

GcdsUIMessage.prototype.codeInfo = {
	mblxbhIsExist: "你输入的值已存在",
	selectGridWindowTitle: "选择列表",
	codeInfoAddTitle: "新增码表条目",
	codeInfoEditTitle: "编辑码表条目",
	notSelectInfoRow: "请选中一条码表类型记录！",
	alertTitle: "提示",
	deleteInfoConfirmMsg: "确定删除选中的码表条目?",
	refreshSuccessMsg: "刷新成功",
	refreshFailureMsg: "刷新失败",
	addUserPassword:"添加用户密码",
	editUserPassword:"重置密码",
	deletePasswordConfirmMsg:"确定删除选中的用户密码?",
	confirmPwdNotSame:"您输入的两次密码不一致!"
};

GcdsUIMessage.prototype.codeType = {
	sjlxbhIsExist: "你输入的值已存在",
	booleanYes: "是",
	booleanNo: "否",
	codeTypeAddTitle: "新增码表",
	codeTypeEditTitle: "编辑码表",
	notSelectTypeRow: "请选中一条码表类型记录!",
	alertTitle: "提示",
	deleteTypeConfirmMsg: "确定删除选中码表及其条目?",
	technology:"技术",
	operation:"业务"
};

GcdsUIMessage.prototype.unit = {
	selectTreeTitle: "选择树",
	jgbhIsExist: "你输入的值已存在！",
	view: "查看",
	edit: "编辑",
	del: "删除",
	unitAddTitle: "新增机构",
	unitEditTitle: "编辑机构",
	unitLookTitle: "查看机构信息",
	staffAddTitle: "添加人员",
	notSelectRow: "请选中一条记录",
	migrateError: "迁徙机构失败",
	wait: "操作中，请稍后......",
	delError: "删除失败",
	moveConfirm: "确定拖动{0}吗？",
	virtualNode: "虚拟节点，不能添加人员！",
	defaultDept: "系统保留机构，不能对其操作！"
};

GcdsUIMessage.prototype.unitci = {
	viewTitle: "机构操作流水详情",
	notSelectRow: "请选中一条记录"
};

GcdsUIMessage.prototype.validation = {
	codeTableErrorMsg: "码表配置长度不能超过{0}字节",
	codeTableNotEqualErrorMsg: "码表配置长度必须等于{0}字节"
};
//字段集合管理
GcdsUIMessage.prototype.dataPermission = {
	lookDataPer: "查看字段权限",
	newDataPer: "新增字段权限",
	editDataPer: "编辑字段权限"
};
//机构集合管理
GcdsUIMessage.prototype.unitPermission = {
	chooseUnit: "选择组织机构",
	manageUnit: "管理组织机构",
	delUnit: "确定删除组织机构？"
};
//角色关联权限
GcdsUIMessage.prototype.roleassignper = {
	chooseUrlPer: "选择URL权限",
	chooseDataPer: "选择字段权限",
	chooseUnitPer: "选择组织机构的数据权限"
};
//权限添加角色
GcdsUIMessage.prototype.permissionRelationRole = {
	chooseRole: "选择角色",
	chooseMenuAddRole: "请选择要添加角色的菜单!",
	chooseRecordAddRole: "请选择一条记录添加角色!",
	notPer:"该角色已被锁定，不能分配权限！",
	notPerRol:"已被锁定，不能分配权限！"
};

GcdsUIMessage.prototype.information = {
	selectOtherConfirm:"未保存的内容会丢失，确定载入其他记录吗？",
	createNewConfirm:"未保存的内容会丢失，确定新建一条记录吗？",
	deleteConfirm:"确定删除记录？",
	titleIsNullAlert:"标题不能为空！",
	statusIsNullAlert:"状态不能为空！",
	groupIsNullAlert:"所属栏目不能为空！",
	odernoIsNullAlert:"排序码内容不能为空！",
	contentIsNullAlert:"发布信息内容不能为空！"
};

GcdsUIMessage.prototype.feedback = {
	feedbackViewTitle:"问题反馈查看",
	feedbackAnswerTitle:"问题反馈回复",
	feedbackAddTitle:"问题反馈新增",
	chooseRecordAlert:"请选择反馈问题！"
};

GcdsUIMessage.prototype.schedule = {
	startTask: "启动任务",
	stopTask: "停止任务",
	addTask: "新增定时任务",
	editTask: "修改定时任务",
	taskCode: "请先填写任务编码",
	taskClass: "定时任务执行类选择器",
	settingTitle: "CronTab表达式设置向导",
	errorDetail: "显示错误详情，功能未实现",
	reactiveTask: "手工重启该任务，功能未实现",
	executionDetail: "任务执行详情",
	abnormalRecord: "只显示异常记录",
	systemError: "系统错误，请联系管理员！",
	minSelected:"请选择分钟！",
	hourSelected:"请选择小时！",
	monthSelected:"请选择月份！",
	daySelected:"请选择天！",
	secSelected:"请输入正确的秒！",
	openMonitor:"开启监控",
	closeMonitor:"关闭监控"
};

//安全策略组管理
GcdsUIMessage.prototype.securityPolicyGroupManage = {
	oneLevel: "一级安全策略标准",
	twoLevel: "二级安全策略标准",
	threeLevel: "三级安全策略标准",
	userDefined: "自定义安全策略",
	enable: "启用",
	alreadyEnable: "已启用",
	restoreDefault:"恢复默认",
	submit: "提交",
	labelVai: "不能超过32位！",
	accessLog: "访问日志记录请输入yes或no！",
	sessionControl: "会话控制请输入yes或no！",
	pwdModifiedValid: "密码不可与前几次历史密码重复次数请输入整数！",
	passwordValid: "密码有效期请输入正整数！",
	mostErrorNumber: "密码允许的最多错误次数请输入正整数！",
	pwdShortestLength: "密码最短的长度限制请输入6到16之间的整数！",
	defalutPassword: "系统默认密码最短长度是",
	bit: "位！",
	auditLogRecord: "审计日志记录请输入yes或no！",
	combinedCipher: "是否一定需要组合密码请输入yes或no！",
	automaticallyUnlockUser: "是否自动解锁用户请输入yes或no！",
	automaticallyUnlockUserTime: "允许自动解锁，自动解锁用户时间请输入正整数！",
	firstLogin: "首次登录是否一定需要修改密码请输入yes或no！",
	defaultInputPassword: "系统默认密码不能输入汉字!",
	maximumSessions: "一个用户达到最大会话数后是否允许登录请输入yes或no!",
	numberSessions: "一个用户允许的会话数请输入整数!"
};

GcdsUIMessage.prototype.recycle = {
	deleteMessage:"确定彻底删除所选记录？删除后将不能还原！",
	revertMessage:"确定还原所选记录？"
};

GcdsUIMessage.prototype.Gcdsui = {
	errorPrompt:"错误提示",
	noRight:"您没有访问权限！",
	success:"操作成功！",
	wait:"处理中，请稍后...",
	prompt:"提示",
	ok: "确定",
    cancel: "取消",
    yes: "是",
    no: "否",
    alertTitle: "提醒",
    confirmTitle: "确认",
    prompTitle: "输入",
    prompMessage: "请输入内容："
};

GcdsUIMessage.prototype.dsm = {
	booleanYes:"是",
	booleanNo:"否",
	readOnly:"只读",
	modify:"修改",
	control:"完全控制",
	notSelectRow:"请选中一条记录",
	popedomManage:"加密权限组管理",
	confirmDelete:"确定要删除选中的加密权限组",
	deleteConfig:"删除配置",
	addStaff:"添加适用人员",
	delStaff:"删除适用人员",
	maxLength:"不能超过255个字符",
	popedomExisted:"权限编码已经存在,请重输",
	encryptExisted:"加密级别名称已经存在,请重输",
	startDatePrompt:"起始日期必须为今天",
	endDatePrompt:"截止日期必须大于今天",
	choiceStaff:"请选择要授权的人员！",
	hasStaff:"该策略已分配人员，不能删除！",
	success:"操作成功！",
	isExistedYHBH:"已分配过dsm策略，是否覆盖原先策略！"
};		
