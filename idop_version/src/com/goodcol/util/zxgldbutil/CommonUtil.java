package com.goodcol.util.zxgldbutil;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class CommonUtil {
	
//	private final static String rule_def1 = "(苏州|党委|清算|不计明细|工会|银行卡|记账式国债|理财|保卫科|信用卡|虚拟|人秘科|库房|金库|行长室|办公室|人员|其他|扬州分行本部|扬州分行国际结算部|扬州分行风险管理部|扬州法律与合规部|扬州分行监察部|扬州分行个人金融部|扬州分行保卫部|扬州分行公司业务部|扬州分行信息科技部|扬州分行人力资源部|扬州分行运营部|扬州分行党务工作部|扬州分行中小企业业务中心|扬州分行法律与合规部|扬州财务管理部|扬州财富管理与私人银行中心|淮安分行本部|淮安分行法律与合规部|淮安分行贸易金融部|淮安分行个人金融部)";
//	private final static String rule_def2 = "(淮安分行中小企业业务中心|淮安分行党务工作部|淮安分行监察保卫部|淮安分行人力资源部|淮安分行财务管理部|淮安分行风险管理部|淮安分行运营部|淮安财富管理与私人银行中心|淮安分行公司业务部|镇江分行本部|镇江分行保卫部|镇江分行法律与合规部|镇江分行信息科技部|镇江分行监察部|镇江分行风险管理部|镇江分行法律合规部|镇江分行个人金融部|镇江记帐式国债|镇江分行公司业务部|镇江内退|镇江分行授信执行部|镇江分行运营部|镇江分行国际结算部|镇江离退休|镇江分行财务管理部|镇江分行人力资源部|镇江分行中小企业业务中心)";
//	private final static String rule_def3 = "(镇江分行财富管理与私人银行中心|盐城分行本部|盐城分行公共部门|盐城分行国际结算部|盐城分行党务工作部|盐城分行保全部|盐城分行科技部|盐城分行个人金融部|盐城分行运营部|盐城分行人力资源部|盐城分行承兑汇票专用|盐城分行公司业务部|盐城分行中小企业中心|盐城分行监察保卫部|盐城分行财务管理部|中国银行盐城分行内控与法律合规部|无锡分行本部|无锡分行中小企业业务中心|无锡分行公司业务营销一中心|无锡分行党务工作部|无锡分行监察部|无锡分行私人银行中心|无锡分行公司业务营销三中心|无锡分行财务管理部|无锡分行授信执行部)";
//	private final static String rule_def4 = "(无锡分行法律合规部|无锡分行人力资源部|无锡分行公司业务营销二中心|无锡分行运营部|无锡分行个人金融部|无锡分行风险管理部|无锡分行信息科技部|无锡分行直属支行管理总部|无锡分行公司业务部|无锡分行国际结算部|无锡分行保卫部|无锡分行监察保卫部|国内结算与现金管理部|省行法律合规部|省行风险管理部|省行党务工作部|省行风险管理部第二授信评审中心|省行风险管理部第一授信评审中心|省行离退休|省行监察部|省行授信执行部|省行稽核部|省行财务管理部|营运中心|省行人力资源部|省行培训中心|省行运营控制部)";
//	private final static String rule_def5 = "(省行风险管理部第三授信评审中心|渠道管理与网络金融部|省行总务部|江苏省分行南京管理部|省行信息科技部|省行保卫部|徐州分行本部|徐州分行公司业务部|徐州分行党务工作部|徐州分行个人金融部|徐州分行信息科技部|徐州分行国际结算部|徐州分行监察部|徐州分行监察室|徐州分行人事教育部|徐州分行风险管理部|徐州分行中小企业业务中心|徐州分行保卫部|徐州分行财务管理部|徐州分行调研室|徐州分行法律与合规部|徐州分行市行票据中心|徐州分行稽核|徐州分行运营部|连云港分行本部|连云港分行风险管理部|连云港分行运营部)";
//	private final static String rule_def6 = "(连云港分行法律与合规部|连云港分行监察室|连云港分行财富管理与私人银行中心|连云港分行零售部消费信贷|连云港分行党务工作部|连云港分行人事教育部|连云港财务管理部|连云港公司业务部|连云港个人金融部|连云港分行中小企业业务中心|连云港国际结算部|泰州分行本部|泰州新区支行（银票）|泰州高港支行（银票）|泰州海陵支行（银票）|泰州分行保卫部|泰州分行信息科技部|泰州劝业支行（银票）|泰州分行个人金融部|中国银行泰州分行内控与法律合规部|姜堰支行（银票）|泰州分行公司业务部|泰州分行风险管理部|泰州中小企业部)";
//	private final static String rule_def7 = "(靖江支行（银票）|泰州分行监察保卫部|兴化支行（银票）|泰州分行法律与合规部|泰州分行人力资源部|泰州分行运营部|泰兴支行（银票）|泰州分行国际结算部|泰州分行财务管理部|泰州分行资产保全部|宿迁分行本部|宿迁分行财务管理部|宿迁分行监察部|宿迁分行财富管理与私人银行中心|宿迁分行法律与合规部|宿迁分行公司部|宿迁分行运营部|宿迁分行综合部|宿迁分行个金部|宿迁分行国际结算部|宿迁分行风险管理部|宿迁分行网点管理中心|宿迁分行中小企业业务中心|常州分行本部|常州分行授信执行部|常州分行财务管理部|常州分行法律与合规部)";
//	private final static String rule_def8 = "(常州分行人力资源部2|常州分行人力资源部|常州分行运营部|常州分行贸易金融部|常州分行风险管理部|常州分行消费信贷中心|常州分行个人金融部|常州分行党务工作部|常州分行公司金融部|常州分行信息科技部|常州分行保卫部|常州分行总务部2|常州分行总务部1|常州分行财富管理中心|常州分行监察部|常州分行中小企业业务中心|南通分行本部|南通资金计划处|南通分行个人金融部|南通公司业务部|南通分行保卫部|南通总务部|南通授信执行部|南通分行财务管理部|南通分行党务工作部|南通分行私人银行中心|南通人力资源部|南通保卫部)";
//	private final static String rule_def9 = "(南通分行中小企业业务中心|南通分行监察部|南通分行信息科技部|南通分行风险管理部|南通法律与合规部|南通国际结算部|财会|保卫|基金|国债|公共部门|储蓄|会计|盐城开放式资金|盐城分行东台支行基建办|无锡江阴支行卡部|无锡分行江阴支行监察部|无锡锡山支行行部|无锡分行宜兴支行监察部|南京地区溧水支行开放式资金|江浦|连云港灌南支行卡部|连云港核电支行存款科|连云港核电支行信贷科|泰州靖江支行营业部专柜|泰州海陵支行业务发展部(原)|泰州海陵支行综合管理部(原)|泰州国际结算部门|常州武进国际结算部|常州武进计财部)";
//	private final static String rule_def10 = "(中国银行常州武进支行监察部|常州溧阳国际结算|南通通州国际结算部|南通通州资金计划部|南通如皋国际结算部|南通如皋资金计划处|南通如东资金计划处|南通如东收付系统柜组|南通如东国际结算部|南通海安客户经理|南通海安国际结算部|南通海安人行对转账|南通海安电脑部|南通启东重要空白凭证库|南通启东国际结算部|南通崇川支行国贸|扬州仪征支行国际结算|扬州宝应支行国际结算部|盐城东台支行国际结算|无锡惠山支行国际结算|无锡惠山支行风险管理|中国银行无锡分行江阴支行风险管理部|无锡江阴支行国际结算部|无锡锡山支行国际结算部)";
//	private final static String rule_def11 = "(中国银行无锡分行宜兴支行风险管理部|无锡宜兴支行国际结算|无锡新区支行国际结算部|泰州靖江支行国际结算部|常州武进风险管理部|常州金坛支行国际结算|常州金坛支行风险管理部|南京地区萨家湾支行萨办|无锡锡山支行业管（风险）|无锡分行惠山支行综合管理部|南通营业部|南通港闸支行国贸|南通海门国际结算部|南通通州国贸|南通如东国贸|南通城东国贸|宿迁泗阳支行管理部|盐城大丰支行国际结算部|盐城城南支行营业部)";
//	
	private final static String rule_def12 = "(金融部|银行卡部|保全部|管理部|结算部|保卫部|执行部|银行卡中心|理财中心|委员会|科技部|市场部|企业部|综管部|培训中心|银行部|监察室|总务部|金库|清算|其他|保卫|分理|公共|企业中心|综合管|综合部|业务团队|会计|营运中心|专柜|财会部|信用卡|银行卡|计划部|资金|电脑部|信贷科|金融机构|业务部|行长室|发展部|业务中心|工作部|资源部|办公室|机构部|教育部|合规部|监察部|信贷中心|中心金库|计财部|工会|其他人员|公司部|银行中心|内退|稽核部|营销团队|国债|稽核|总务|虚拟|未聘|管理中心|营销中心|人员|调研室|结算|营销_中心|专柜)";
	private final static String rule_def13 = "(柜组|基金|库房|经理|个金部|退休|人秘科|管理总部|党委|不计明细)";
	//用户模块使用 过滤苏州及江苏省分行（南京地区）
	private final static String rule_def14 = "(苏州|不计明细)";

	
	public static String orgNameSql() {
		String namesql = " and a.orgname is not null  "
				+ " and a.orgname not like '%苏州%'  "
				+ " and a.orgname not like '%党委%' "
				+ " and a.orgname not like '%清算%' "
				+ " and a.orgname not like '%不计明细%' "
				+ " and a.orgname not like '%工会%' "
				+ " and a.orgname not like '%银行卡%' "
				+ " and a.orgname not like '%记账式国债%' "
				+ " and a.orgname not like '%理财%' "
				+ " and a.orgname not like '%保卫科%' "
				+ " and a.orgname not like '%信用卡%' "
				+ " and a.orgname not like '%虚拟%' "
				+ " and a.orgname not like '%人秘科%' "
				+ " and a.orgname not like '%库房%' "
				+ " and a.orgname not like '%金库%' "
				+ " and a.orgname not like '%行长室%' "
				+ " and a.orgname not like '%办公室%' "
				+ " and a.orgname not like '%人员%' "
				+ " and a.orgname not like '%其他%' "
				+ " and a.orgname not like '%扬州分行本部%' "
				+ " and a.orgname not like '%扬州分行国际结算部%' "
				+ " and a.orgname not like '%扬州分行风险管理部%' "
				+ " and a.orgname not like '%扬州法律与合规部%' "
				+ " and a.orgname not like '%扬州分行监察部%' "
				+ " and a.orgname not like '%扬州分行个人金融部%' "
				+ " and a.orgname not like '%扬州分行保卫部%' "
				+ " and a.orgname not like '%扬州分行公司业务部%' "
				+ " and a.orgname not like '%扬州分行信息科技部%' "
				+ " and a.orgname not like '%扬州分行人力资源部%' "
				+ " and a.orgname not like '%扬州分行运营部%' "
				+ " and a.orgname not like '%扬州分行党务工作部%' "
				+ " and a.orgname not like '%扬州分行中小企业业务中心%' "
				+ " and a.orgname not like '%扬州分行法律与合规部%' "
				+ " and a.orgname not like '%扬州财务管理部%' "
				+ " and a.orgname not like '%扬州财富管理与私人银行中心%' "
				+ " and a.orgname not like '%淮安分行本部%' "
				+ " and a.orgname not like '%淮安分行法律与合规部%' "
				+ " and a.orgname not like '%淮安分行贸易金融部%' "
				+ " and a.orgname not like '%淮安分行个人金融部%' "
				+ " and a.orgname not like '%淮安分行中小企业业务中心%' "
				+ " and a.orgname not like '%淮安分行党务工作部%' "
				+ " and a.orgname not like '%淮安分行监察保卫部%' "
				+ " and a.orgname not like '%淮安分行人力资源部%' "
				+ " and a.orgname not like '%淮安分行财务管理部%' "
				+ " and a.orgname not like '%淮安分行风险管理部%' "
				+ " and a.orgname not like '%淮安分行运营部%' "
				+ " and a.orgname not like '%淮安财富管理与私人银行中心%' "
				+ " and a.orgname not like '%淮安分行公司业务部%' "
				+ " and a.orgname not like '%镇江分行本部%' "
				+ " and a.orgname not like '%镇江分行保卫部%' "
				+ " and a.orgname not like '%镇江分行法律与合规部%' "
				+ " and a.orgname not like '%镇江分行信息科技部%' "
				+ " and a.orgname not like '%镇江分行监察部%' "
				+ " and a.orgname not like '%镇江分行风险管理部%' "
				+ " and a.orgname not like '%镇江分行法律合规部%' "
				+ " and a.orgname not like '%镇江分行个人金融部%' "
				+ " and a.orgname not like '%镇江记帐式国债%' "
				+ " and a.orgname not like '%镇江分行公司业务部%' "
				+ " and a.orgname not like '%镇江内退%' "
				+ " and a.orgname not like '%镇江分行授信执行部%' "
				+ " and a.orgname not like '%镇江分行运营部%' "
				+ " and a.orgname not like '%镇江分行国际结算部%' "
				+ " and a.orgname not like '%镇江离退休%' "
				+ " and a.orgname not like '%镇江分行财务管理部%' "
				+ " and a.orgname not like '%镇江分行人力资源部%' "
				+ " and a.orgname not like '%镇江分行中小企业业务中心%' "
				+ " and a.orgname not like '%镇江分行财富管理与私人银行中心%' "
				+ " and a.orgname not like '%盐城分行本部%' "
				+ " and a.orgname not like '%盐城分行公共部门%' "
				+ " and a.orgname not like '%盐城分行国际结算部%' "
				+ " and a.orgname not like '%盐城分行党务工作部%' "
				+ " and a.orgname not like '%盐城分行保全部%' "
				+ " and a.orgname not like '%盐城分行科技部%' "
				+ " and a.orgname not like '%盐城分行个人金融部%' "
				+ " and a.orgname not like '%盐城分行运营部%' "
				+ " and a.orgname not like '%盐城分行人力资源部%' "
				+ " and a.orgname not like '%盐城分行承兑汇票专用%' "
				+ " and a.orgname not like '%盐城分行公司业务部%' "
				+ " and a.orgname not like '%盐城分行中小企业中心%' "
				+ " and a.orgname not like '%盐城分行监察保卫部%' "
				+ " and a.orgname not like '%盐城分行财务管理部%' "
				+ " and a.orgname not like '%中国银行盐城分行内控与法律合规部%' "
				+ " and a.orgname not like '%无锡分行本部%' "
				+ " and a.orgname not like '%无锡分行中小企业业务中心%' "
				+ " and a.orgname not like '%无锡分行公司业务营销一中心%' "
				+ " and a.orgname not like '%无锡分行党务工作部%' "
				+ " and a.orgname not like '%无锡分行监察部%' "
				+ " and a.orgname not like '%无锡分行私人银行中心%' "
				+ " and a.orgname not like '%无锡分行公司业务营销三中心%' "
				+ " and a.orgname not like '%无锡分行财务管理部%' "
				+ " and a.orgname not like '%无锡分行授信执行部%' "
				+ " and a.orgname not like '%无锡分行法律合规部%' "
				+ " and a.orgname not like '%无锡分行人力资源部%' "
				+ " and a.orgname not like '%无锡分行公司业务营销二中心%' "
				+ " and a.orgname not like '%无锡分行运营部%' "
				+ " and a.orgname not like '%无锡分行个人金融部%' "
				+ " and a.orgname not like '%无锡分行风险管理部%' "
				+ " and a.orgname not like '%无锡分行信息科技部%' "
				+ " and a.orgname not like '%无锡分行直属支行管理总部%' "
				+ " and a.orgname not like '%无锡分行公司业务部%' "
				+ " and a.orgname not like '%无锡分行国际结算部%' "
				+ " and a.orgname not like '%无锡分行保卫部%' "
				+ " and a.orgname not like '%无锡分行监察保卫部%' "
				+ " and a.orgname not like '%国内结算与现金管理部%' 　 "
				+ " and a.orgname not like '%省行法律合规部%' "
				+ " and a.orgname not like '%省行风险管理部%' "
				+ " and a.orgname not like '%省行党务工作部%' "
				+ " and a.orgname not like '%省行风险管理部第二授信评审中心%' "
				+ " and a.orgname not like '%省行风险管理部第一授信评审中心%' "
				+ " and a.orgname not like '%省行离退休%' "
				+ " and a.orgname not like '%省行监察部%' "
				+ " and a.orgname not like '%省行授信执行部%' "
				+ " and a.orgname not like '%省行稽核部%' "
				+ " and a.orgname not like '%省行财务管理部%' "
				+ " and a.orgname not like '%营运中心%' "
				+ " and a.orgname not like '%省行人力资源部%' "
				+ " and a.orgname not like '%省行培训中心%' "
				+ " and a.orgname not like '%省行运营控制部%' "
				+ " and a.orgname not like '%省行风险管理部第三授信评审中心%' "
				+ " and a.orgname not like '%渠道管理与网络金融部%' "
				+ " and a.orgname not like '%省行总务部%' "
				+ " and a.orgname not like '%江苏省分行南京管理部%' "
				+ " and a.orgname not like '%省行信息科技部%' "
				+ " and a.orgname not like '%省行保卫部%' "
				+ " and a.orgname not like '%徐州分行本部%' "
				+ " and a.orgname not like '%徐州分行公司业务部%' "
				+ " and a.orgname not like '%徐州分行党务工作部%' "
				+ " and a.orgname not like '%徐州分行个人金融部%' "
				+ " and a.orgname not like '%徐州分行信息科技部%' "
				+ " and a.orgname not like '%徐州分行国际结算部%' "
				+ " and a.orgname not like '%徐州分行监察部%' "
				+ " and a.orgname not like '%徐州分行监察室%' "
				+ " and a.orgname not like '%徐州分行人事教育部%' "
				+ " and a.orgname not like '%徐州分行风险管理部%' "
				+ " and a.orgname not like '%徐州分行中小企业业务中心%' "
				+ " and a.orgname not like '%徐州分行保卫部%' "
				+ " and a.orgname not like '%徐州分行财务管理部%' "
				+ " and a.orgname not like '%徐州分行调研室%' "
				+ " and a.orgname not like '%徐州分行法律与合规部%' "
				+ " and a.orgname not like '%徐州分行市行票据中心%' "
				+ " and a.orgname not like '%徐州分行稽核%' "
				+ " and a.orgname not like '%徐州分行运营部%' "
				+ " and a.orgname not like '%连云港分行本部%' "
				+ " and a.orgname not like '%连云港分行风险管理部%' "
				+ " and a.orgname not like '%连云港分行运营部%' "
				+ " and a.orgname not like '%连云港分行法律与合规部%' "
				+ " and a.orgname not like '%连云港分行监察室%' "
				+ " and a.orgname not like '%连云港分行财富管理与私人银行中心%' "
				+ " and a.orgname not like '%连云港分行零售部消费信贷%' "
				+ " and a.orgname not like '%连云港分行党务工作部%' "
				+ " and a.orgname not like '%连云港分行人事教育部%' "
				+ " and a.orgname not like '%连云港财务管理部%' "
				+ " and a.orgname not like '%连云港公司业务部%' "
				+ " and a.orgname not like '%连云港个人金融部%' "
				+ " and a.orgname not like '%连云港分行中小企业业务中心%' "
				+ " and a.orgname not like '%连云港国际结算部%' "
				+ " and a.orgname not like '%泰州分行本部%' "
				+ " and a.orgname not like '%泰州新区支行（银票）%' "
				+ " and a.orgname not like '%泰州高港支行（银票）%' "
				+ " and a.orgname not like '%泰州海陵支行（银票）%' "
				+ " and a.orgname not like '%泰州分行保卫部%' "
				+ " and a.orgname not like '%泰州分行信息科技部%' "
				+ " and a.orgname not like '%泰州劝业支行（银票）%' "
				+ " and a.orgname not like '%泰州分行个人金融部%' "
				+ " and a.orgname not like '%中国银行泰州分行内控与法律合规部%' "
				+ " and a.orgname not like '%姜堰支行（银票）%' "
				+ " and a.orgname not like '%泰州分行公司业务部%' "
				+ " and a.orgname not like '%泰州分行风险管理部%' "
				+ " and a.orgname not like '%泰州中小企业部%' "
				+ " and a.orgname not like '%靖江支行（银票）%' "
				+ " and a.orgname not like '%泰州分行监察保卫部%' "
				+ " and a.orgname not like '%兴化支行（银票）%' "
				+ " and a.orgname not like '%泰州分行法律与合规部%' "
				+ " and a.orgname not like '%泰州分行人力资源部%' "
				+ " and a.orgname not like '%泰州分行运营部%' "
				+ " and a.orgname not like '%泰兴支行（银票）%' "
				+ " and a.orgname not like '%泰州分行国际结算部%' "
				+ " and a.orgname not like '%泰州分行财务管理部%' "
				+ " and a.orgname not like '%泰州分行资产保全部%' "
				+ " and a.orgname not like '%宿迁分行本部%' "
				+ " and a.orgname not like '%宿迁分行财务管理部%' "
				+ " and a.orgname not like '%宿迁分行监察部%' "
				+ " and a.orgname not like '%宿迁分行财富管理与私人银行中心%' "
				+ " and a.orgname not like '%宿迁分行法律与合规部%' "
				+ " and a.orgname not like '%宿迁分行公司部%' "
				+ " and a.orgname not like '%宿迁分行运营部%' "
				+ " and a.orgname not like '%宿迁分行综合部%' "
				+ " and a.orgname not like '%宿迁分行个金部%' "
				+ " and a.orgname not like '%宿迁分行国际结算部%' "
				+ " and a.orgname not like '%宿迁分行风险管理部%' "
				+ " and a.orgname not like '%宿迁分行网点管理中心%' "
				+ " and a.orgname not like '%宿迁分行中小企业业务中心%' "
				+ " and a.orgname not like '%常州分行本部%' "
				+ " and a.orgname not like '%常州分行授信执行部%' "
				+ " and a.orgname not like '%常州分行财务管理部%' "
				+ " and a.orgname not like '%常州分行法律与合规部%' "
				+ " and a.orgname not like '%常州分行人力资源部2%' "
				+ " and a.orgname not like '%常州分行人力资源部%' "
				+ " and a.orgname not like '%常州分行运营部%' "
				+ " and a.orgname not like '%常州分行贸易金融部%' "
				+ " and a.orgname not like '%常州分行风险管理部%' "
				+ " and a.orgname not like '%常州分行消费信贷中心%' "
				+ " and a.orgname not like '%常州分行个人金融部%' "
				+ " and a.orgname not like '%常州分行党务工作部%' "
				+ " and a.orgname not like '%常州分行公司金融部%' "
				+ " and a.orgname not like '%常州分行信息科技部%' "
				+ " and a.orgname not like '%常州分行保卫部%' "
				+ " and a.orgname not like '%常州分行总务部2%' "
				+ " and a.orgname not like '%常州分行总务部1%' "
				+ " and a.orgname not like '%常州分行财富管理中心%' "
				+ " and a.orgname not like '%常州分行监察部%' "
				+ " and a.orgname not like '%常州分行中小企业业务中心%' "
				+ " and a.orgname not like '%南通分行本部%' "
				+ " and a.orgname not like '%南通资金计划处%' "
				+ " and a.orgname not like '%南通分行个人金融部%' "
				+ " and a.orgname not like '%南通公司业务部%' "
				+ " and a.orgname not like '%南通分行保卫部%' "
				+ " and a.orgname not like '%南通总务部%' "
				+ " and a.orgname not like '%南通授信执行部%' "
				+ " and a.orgname not like '%南通分行财务管理部%' "
				+ " and a.orgname not like '%南通分行党务工作部%' "
				+ " and a.orgname not like '%南通分行私人银行中心%' "
				+ " and a.orgname not like '%南通人力资源部%' "
				+ " and a.orgname not like '%南通保卫部%' "
				+ " and a.orgname not like '%南通分行中小企业业务中心%' 　 "
				+ " and a.orgname not like '%南通分行监察部%' "
				+ " and a.orgname not like '%南通分行信息科技部%' "
				+ " and a.orgname not like '%南通分行风险管理部%' "
				+ " and a.orgname not like '%南通法律与合规部%' "
				+ " and a.orgname not like '%南通国际结算部%' "
				+ " and a.orgname not like '%财会%' "
				+ " and a.orgname not like '%保卫%' "
				+ " and a.orgname not like '%基金%' "
				+ " and a.orgname not like '%国债%' "
				+ " and a.orgname not like '%公共部门%' "
				+ " and a.orgname not like '%储蓄%' "
				+ " and a.orgname not like '%会计%' "
				+ " and a.orgname not like '%盐城开放式资金%' "
				+ " and a.orgname not like '%盐城分行东台支行基建办%' "
				+ " and a.orgname not like '%无锡江阴支行卡部%' "
				+ " and a.orgname not like '%无锡分行江阴支行监察部%' "
				+ " and a.orgname not like '%无锡锡山支行行部%' "
				+ " and a.orgname not like '%无锡分行宜兴支行监察部%' "
				+ " and a.orgname not like '%南京地区溧水支行开放式资金%' "
				+ " and a.orgname not like '%江浦%' "
				+ " and a.orgname not like '%连云港灌南支行卡部%' "
				+ " and a.orgname not like '%连云港核电支行存款科%' "
				+ " and a.orgname not like '%连云港核电支行信贷科%' "
				+ " and a.orgname not like '%泰州靖江支行营业部专柜%' "
				+ " and a.orgname not like '%泰州海陵支行业务发展部(原)%' "
				+ " and a.orgname not like '%泰州海陵支行综合管理部(原)%' "
				+ " and a.orgname not like '%泰州国际结算部门%' "
				+ " and a.orgname not like '%常州武进国际结算部%' "
				+ " and a.orgname not like '%常州武进计财部%' "
				+ " and a.orgname not like '%中国银行常州武进支行监察部%' "
				+ " and a.orgname not like '%常州溧阳国际结算%' "
				+ " and a.orgname not like '%南通通州国际结算部%' "
				+ " and a.orgname not like '%南通通州资金计划部%' "
				+ " and a.orgname not like '%南通如皋国际结算部%' "
				+ " and a.orgname not like '%南通如皋资金计划处%' "
				+ " and a.orgname not like '%南通如东资金计划处%' "
				+ " and a.orgname not like '%南通如东收付系统柜组%' "
				+ " and a.orgname not like '%南通如东国际结算部%' "
				+ " and a.orgname not like '%南通海安客户经理%' "
				+ " and a.orgname not like '%南通海安国际结算部%' "
				+ " and a.orgname not like '%南通海安人行对转账%' "
				+ " and a.orgname not like '%南通海安电脑部%' "
				+ " and a.orgname not like '%南通启东重要空白凭证库%' "
				+ " and a.orgname not like '%南通启东国际结算部%' "
				+ " and a.orgname not like '%南通崇川支行国贸%' "
				+ " and a.orgname not like '%扬州仪征支行国际结算%' "
				+ " and a.orgname not like '%扬州宝应支行国际结算部%' "
				+ " and a.orgname not like '%盐城东台支行国际结算%' "
				+ " and a.orgname not like '%无锡惠山支行国际结算%' "
				+ " and a.orgname not like '%无锡惠山支行风险管理%' "
				+ " and a.orgname not like '%中国银行无锡分行江阴支行风险管理部%' "
				+ " and a.orgname not like '%无锡江阴支行国际结算部%' "
				+ " and a.orgname not like '%无锡锡山支行国际结算部%' "
				+ " and a.orgname not like '%中国银行无锡分行宜兴支行风险管理部%' "
				+ " and a.orgname not like '%无锡宜兴支行国际结算%' "
				+ " and a.orgname not like '%无锡新区支行国际结算部%' "
				+ " and a.orgname not like '%泰州靖江支行国际结算部%' "
				+ " and a.orgname not like '%常州武进风险管理部%' "
				+ " and a.orgname not like '%常州金坛支行国际结算%' "
				+ " and a.orgname not like '%常州金坛支行风险管理部%' "
				+ " and a.orgname not like '%南京地区萨家湾支行萨办%' "
				+ " and a.orgname not like '%无锡锡山支行业管（风险）%' "
				+ " and a.orgname not like '%无锡分行惠山支行综合管理部%' "
				+ " and a.orgname not like '%南通营业部%' "
				+ " and a.orgname not like '%南通港闸支行国贸%' "
				+ " and a.orgname not like '%南通海门国际结算部%' "
				+ " and a.orgname not like '%南通通州国贸%' "
				+ " and a.orgname not like '%南通如东国贸%' "
				+ " and a.orgname not like '%南通城东国贸%' "
				+ " and a.orgname not like '%宿迁泗阳支行管理部%' "
				+ " and a.orgname not like '%盐城大丰支行国际结算部%' "
				+ " and a.orgname not like '%盐城城南支行营业部%' "
				+ " and a.orgnum !='001000000' ";
		// +" and a.orgname not like '%不计明细%' ";
		return namesql;
	}
	
	
	public static String orgNameSqlNoBenBu() {
		String namesql = " and a.orgname is not null and a.orgnum !='001000000' "+
		  "and not REGEXP_LIKE(a.orgname,'"+rule_def12+"')"+
		  "and not REGEXP_LIKE(a.orgname,'"+rule_def13+"')";
		
		return namesql;
	}
	
	public static String orgNameSqlToUser() {
		String namesql = " and a.orgname is not null  "+
		  " and not REGEXP_LIKE(a.orgname,'"+rule_def14+"')"+ 
		  " and a.orgnum !='001000000' ";
		return namesql;
	}
	
	
	
	
	
	
	
	/**
	 * 读取单元格的值
	 */
	public static Object findCellValue(HSSFCell cell) {
		Object cellValue = null;
		if(null!=cell){
			//判断Cell类型
			switch (cell.getCellType()) {
				case 0://cell.CELL_TYPE_NUMERIC
					cellValue = (int)cell.getNumericCellValue();
					break;
				case 1://cell.CELL_TYPE_STRING
					cellValue = cell.getStringCellValue().trim();
					break;
				case 2://cell.CELL_TYPE_FORMULA
					cellValue = cell.getDateCellValue();
					break;
				default:
					break;
				}
		}
		return cellValue;
	}

}
