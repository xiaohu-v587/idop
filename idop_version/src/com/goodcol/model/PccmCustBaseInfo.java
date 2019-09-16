package com.goodcol.model;

import java.math.BigDecimal;

public class PccmCustBaseInfo{
    /** 客户号 */
    private String custNo;

    /** 客户名称 */
    private String name;

    /** 信贷客户名称 */
    private String loanCustName;

    /** 客户类型 */
    private String custType;

    /** 客户子类型 */
    private String custSubType;

    /** 证件类型 */
    private String idType;

    /** 证件号码 */
    private String idNumber;

    /** 证件到期日 */
    private String cuidIdExpireDate;

    /** 所属行 */
    private String brNo;

    /** 所属行名称 */
    private String brName;

    /** 客户状态 */
    private String custStatus;

    /** 客户经理号 */
    private String custMgrNo;

    /** 客户经理名称 */
    private String custMgrName;

    /** 客户经理联系方式 */
    private String custMgrContact;

    /** 客户经理所属机构 */
    private String custMgrBrNo;

    /** 客户经理所属机构名称 */
    private String custMgrBrName;

    /** 行业分类 */
    private String industryCode;

    /** 企业性质 */
    private String busOwsp;

    /** 主营业务 */
    private String businessScope;

    /** 注册国家 */
    private String nationalityCode;

    /** 注册资本货币 */
    private String registryCurr;

    /** 注册资本金额 */
    private BigDecimal capitalAmount;

    /** 注册地址 */
    private String regAddr;

    /** 注册时间 */
    private String busRegisterDate;

    /** 企业创建年份 */
    private String createDate;

    /** 企业规模 */
    private String companySize;

    /** 是否上市公司 */
    private String onStockMarket;

    /** 集团客户标识 */
    private String groupFlag;

    /** 客户简称 */
    private String shrtName;

    /** 行业授信政策 */
    private String industryCreditPolic;

    /** 信用评级 */
    private String creditRating;

    /** 是否集团管理成员 */
    private String isgrpmngmb;

    /** 是否集团内核心成员 */
    private String isgrpcoremb;

    /** 是否集团母公司 */
    private String isgrpprtcomp;

    /** 实收资本（万元） */
    private BigDecimal factcap;

    /** 财报日期 */
    private String rptDate;

    /** 流动资产 */
    private BigDecimal filed1;

    /** 货币资金 */
    private BigDecimal filed2;

    /** 短期投资 */
    private BigDecimal filed3;

    /** 应收票据 */
    private BigDecimal filed4;

    /** 应收账款 */
    private BigDecimal filed5;

    /** 预付账款 */
    private BigDecimal filed6;

    /** 存货 */
    private BigDecimal filed7;

    /** 其他应收款 */
    private BigDecimal filed8;

    /** 固定资产 */
    private BigDecimal filed9;

    /** 长期投资 */
    private BigDecimal filed10;

    /** 在建工程 */
    private BigDecimal filed11;

    /** 无形资产 */
    private BigDecimal filed12;

    /** 资产总计 */
    private BigDecimal filed13;

    /** 短期借款 */
    private BigDecimal filed14;

    /** 应付票据 */
    private BigDecimal filed15;

    /** 应付账款 */
    private BigDecimal filed16;

    /** 预收账款 */
    private BigDecimal filed17;

    /** 应付股利 */
    private BigDecimal filed18;

    /** 长期借款 */
    private BigDecimal filed19;

    /** 应付债券 */
    private BigDecimal filed20;

    /** 长期应付款 */
    private BigDecimal filed21;

    /** 所有者权益 */
    private BigDecimal filed22;

    /** 实收资本 */
    private BigDecimal filed23;

    /** 资本公积 */
    private BigDecimal filed24;

    /** 盈余公积 */
    private BigDecimal filed25;

    /** 未分配利润 */
    private BigDecimal filed26;

    /** 人民币存款时点 */
    private BigDecimal ckCurrCny;

    /** 人民币存款日均 */
    private BigDecimal ckNrjCny;

    /** 12个月人民币存款时点最高额 */
    private BigDecimal ck12topCny;

    /** 外币存款时点 */
    private BigDecimal ckCurrWb;

    /** 外币存款日均 */
    private BigDecimal ckNrjWb;

    /** 12个月外币存款时点最高额 */
    private BigDecimal ck12topWb;

    /** 人民币活期存款时点余额 */
    private BigDecimal ckHqcurrCny;

    /** 人民币活期存款日均余额 */
    private BigDecimal ckHqnrjCny;

    /** 人民币定期存款时点余额 */
    private BigDecimal ckDqcurrCny;

    /** 人民币定期存款日均余额 */
    private BigDecimal ckDqnrjCny;

    /** 外币活期存款时点余额 */
    private BigDecimal ckHqcurrWb;

    /** 外币活期存款日均余额 */
    private BigDecimal ckHqnrjWb;

    /** 外币定期存款时点余额 */
    private BigDecimal ckDqcurrWb;

    /** 外币定期存款日均余额 */
    private BigDecimal ckDqnrjWb;

    /** 是否授信客户 */
    private String isSxcust;

    /** 授信分类 */
    private String giventype;

    /** 信用评级 */
    private String loanCreditRating;

    /** 授信总额 */
    private BigDecimal loancash;

    /** 授信种类 */
    private String loanType;

    /** 中小企业贷款产品 */
    private String loanProZx;

    /** 最新批复有效期 */
    private Short orpvdpd;

    /** 人民币贷款时点 */
    private BigDecimal loanCurrCny;

    /** 人民币贷款日均 */
    private BigDecimal loanNrjCny;

    /** 12个月人民币贷款时点最高额 */
    private BigDecimal loan12topCny;

    /** 人民币贸易融资时点 */
    private BigDecimal rzCurrCny;

    /** 人民币贸易融资日均 */
    private BigDecimal rzNrjCny;

    /** 贴现时点余额 */
    private BigDecimal txCurr;

    /** 贴现日均余额 */
    private BigDecimal txNrj;

    /** 外币贷款时点 */
    private BigDecimal loanCurrWb;

    /** 外币贷款日均 */
    private BigDecimal loanNrjWb;

    /** 12个月外币贷款时点最高额 */
    private BigDecimal loan12topWb;

    /** 外币贸易融资时点 */
    private BigDecimal rzCurrWb;

    /** 外币贸易融资日均 */
    private BigDecimal rzNrjWb;

    /** 是否开通网银 */
    private String isBocnet;

    /** 是否网银交易客户 */
    private String isBocnetTran;

    /** 当日网银大额转账交易笔数（交易金额大于1000万） */
    private BigDecimal bocnetDetranNum;

    /** 当日网银大额转账交易金额（交易金额大于1000万） */
    private BigDecimal bocnetDetranAmt;

    /** 是否开通网银购买理财功能 */
    private String isBocnetTranxpad;

    /** 3个月网银渠道购买理财交易笔数 */
    private BigDecimal bocnetPad3monNum;

    /** 3个月网银渠道购买理财交易金额 */
    private BigDecimal bocnetPad3monAmt;

    /** 3个月网银交易笔数 */
    private BigDecimal bocnetTran3monNum;

    /** 6个月网银交易笔数 */
    private BigDecimal bocnetTran6monNum;

    /** 12个月网银交易笔数 */
    private BigDecimal bocnetTran12monNum;

    /** 3个月网银交易金额 */
    private BigDecimal bocnetTran3monAmt;

    /** 6个月网银交易金额 */
    private BigDecimal bocnetTran6monAmt;

    /** 12个月网银交易金额 */
    private BigDecimal bocnetTran12monAmt;

    /** 是否开通网上对账功能 */
    private String isBocnetDz;

    /** 当月是否已完成网上对账 */
    private String isDzDone;

    /** 是否办理集团网银 */
    private String isGroupBocnet;

    /** 是否开通B2C支付业务 */
    private String isB2c;

    /** B2C支付_当年累计交易笔数 */
    private BigDecimal b2cTranNum;

    /** B2C支付_当年累计交易金额 */
    private BigDecimal b2cTranAmt;

    /** 是否开通B2B支付业务 */
    private String isB2b;

    /** B2B支付_当年累计交易笔数 */
    private BigDecimal b2bTranNum;

    /** B2B支付_当年累计交易金额 */
    private BigDecimal b2bTranAmt;

    /** 是否报关即时通交易客户 */
    private String isBgjstTran;

    /** 最近12个月进口贸易总金额 */
    private BigDecimal totJkmy12monAmt;

    /** 是否办理养老金业务 */
    private String isYlj;

    /** 上年度养老金托管业务量 */
    private BigDecimal yljTgamtLy;

    /** 本年度养老金托管业务量 */
    private BigDecimal yljTgamt;

    /** 上年度养老金账管业务量 */
    private BigDecimal yljZgamtLy;

    /** 本年度养老金账管业务量 */
    private BigDecimal yljZgamt;

    /** 上年度养老金中收业务量 */
    private BigDecimal yljTgMidLy;

    /** 本年度养老金中收业务量 */
    private BigDecimal yljTgMid;

    /** 是否办理大额存单 */
    private String isDecd;

    /** 大额存单时点余额 */
    private BigDecimal decdCurr;

    /** 大额存单日均余额 */
    private BigDecimal decdNrj;

    /** 是否开通结算卡 */
    private String isJscard;

    /** 开通结算卡张数 */
    private BigDecimal jscardNum;

    /** 是否开通短信通 */
    private String isDxt;

    /** 开通短信通数量 */
    private BigDecimal dxtNum;

    /** 是否开通回单自助服务 */
    private String isHdzz;

    /** 是否开通回单箱 */
    private String isHdx;

    /** 上一年度银行汇票业务量 */
    private BigDecimal hpAmtLy;

    /** 本年度银行汇票业务量 */
    private BigDecimal hpAmt;

    /** 上一年度银行本票业务量 */
    private BigDecimal bpAmtLy;

    /** 本年度银行本票业务量 */
    private BigDecimal bpAmt;

    /** 上一年度支票业务量 */
    private BigDecimal zpAmtLy;

    /** 本年度支行业务量 */
    private BigDecimal zpAmt;

    /** 是否开办中央财政授权支付 */
    private String isZycasqzf;

    /** 是否开办上门服务 */
    private String isSmfw;

    /** 是否开办旅保通服务 */
    private String isLbt;

    /** 是否开办现金管理 */
    private String isGcms;

    /** 开办现金管理时间 */
    private String gcmsOpenDate;

    /** 现金管理业务角色 */
    private String gcmsRole;

    /** 现金管理产品类型 */
    private String gcmsProd;

    /** 上年度现金管理归集量 */
    private BigDecimal gcmsGjamtLy;

    /** 本年度现金管理归集量 */
    private BigDecimal gcmsGjamt;

    /** 上年度现金管理下拨量 */
    private BigDecimal gcmsXbLy;

    /** 本年度现金管理下拨量 */
    private BigDecimal gcmsXb;

    /** 现金管理存款日均余额 */
    private BigDecimal gcmsCkNrj;

    /** 现金管理存款日均较上日变动金额 */
    private BigDecimal gcmsCkNrjBsr;

    /** 现金管理存款日均较上年变动金额 */
    private BigDecimal gcmsCkNrjBsn;

    /** 是否购买过表内理财 */
    private String isBnFic;

    /** 表内理财余额 */
    private BigDecimal bnFicCurr;

    /** 表内理财年日均 */
    private BigDecimal bnFicNrj;

    /** 表内理财收益 */
    private BigDecimal bnFicProfit;

    /** 是否购买过表外理财 */
    private BigDecimal isBwFic;

    /** 人民币期次类时点 */
    private BigDecimal ficCurrCny;

    /** 人民币期次类日均 */
    private BigDecimal ficNrjCny;

    /** 外币期次类时点 */
    private BigDecimal ficCurrWb;

    /** 外币期次类日均 */
    private BigDecimal ficNrjWb;

    /** 人民币日积月累时点 */
    private BigDecimal rjylCurrCny;

    /** 人民币日积月累日均 */
    private BigDecimal rjyxNrjCny;

    /** 外币日积月累时点 */
    private BigDecimal rjylCurrWb;

    /** 外币日积月累日均 */
    private BigDecimal rjyxNrjWb;

    /** 表外理财总时点余额 */
    private BigDecimal bwFicCurr;

    /** 表外理财总年日均 */
    private BigDecimal bwFicNrj;

    /** 表外理财总收益 */
    private BigDecimal bwFicProfit;

    /** 我行承销直融产品种类 */
    private String zrType;

    /** 我行承销直融产品金额 */
    private BigDecimal ztAmt;

    /** 我行承销直融产品份额 */
    private BigDecimal zrFe;

    /** 我行承销直融产品余额 */
    private BigDecimal zrCurr;

    /** 客户投资额度总额 */
    private BigDecimal investLinesTot;

    /** 客户投资额度剩余金额 */
    private BigDecimal investLinesRemain;

    /** 客户投资额度余额 */
    private BigDecimal investLinesBal;

    /** 客户标类理财额度总额 */
    private BigDecimal blFiclinesTot;

    /** 客户标类理财额度剩余金额 */
    private BigDecimal blFiclinesRemain;

    /** 客户标类理财额度余额 */
    private BigDecimal blFiclinesBal;

    /** 客户存续期限额 */
    private BigDecimal custCxqxe;

    /** 客户存续期限额剩余金额 */
    private BigDecimal custCxqxeRemain;

    /** 客户存续期限额余额 */
    private BigDecimal custCxqxeBal;

    /** 是否通过非标理财融资 */
    private String isFbFic;

    /** 非标理财融资时点余额 */
    private BigDecimal fbFicCurr;

    /** 上年度即期结售汇交易量 */
    private BigDecimal fxsmAmtLy;

    /** 本年度即期结售汇交易量 */
    private BigDecimal fxsmAmt;

    /** 是否签署NAFMII协议 */
    private String isNafmii;

    /** 上年度远期结售汇业务量 */
    private BigDecimal forwardCustfxAmtLy;

    /** 本年度远期结售汇业务量 */
    private BigDecimal forwardCustfxAmt;

    /** 上年度外汇买卖业务量 */
    private BigDecimal frexAmtLy;

    /** 本年度外汇买卖业务量 */
    private BigDecimal frexAmt;

    /** 是否办理利率掉期 */
    private String isRatedq;

    /** 上年度利率掉期交易量 */
    private BigDecimal ratedqAmtLy;

    /** 本年度利率掉期交易量 */
    private BigDecimal ratedqAmt;

    /** 利率掉期余额 */
    private BigDecimal ratedqCurr;

    /** 是否办理货币掉期 */
    private String isCurdq;

    /** 上年度货币掉期交易量 */
    private BigDecimal curdqAmtLy;

    /** 本年度货币掉期交易量 */
    private BigDecimal curdqAmt;

    /** 货币掉期余额 */
    private BigDecimal curdqCurr;

    /** 是否办理贵金属租赁 */
    private String isGoldlease;

    /** 贵金属租赁业务余额 */
    private BigDecimal goldleaseCurr;

    /** 是否叙做贵金属远期 */
    private String isXzGoldfx;

    /** 是否签约柜台债券 */
    private String isGtzq;

    /** 是否为银行间债券会员 */
    private String isBankZqmem;

    /** 柜台债券上年交易量 */
    private BigDecimal gtzqAmtLy;

    /** 柜台债券本年交易量 */
    private BigDecimal gtzqAmt;

    /** 是否与我行开展同存业务 */
    private String isTc;

    /** 是否为金交所会员 */
    private String isJjsMem;

    /** 是否有大宗商品业务（含大豆、原油等） */
    private String isDzsp;

    /** 是否代发薪 */
    private String isDfx;

    /** 是否购买过中银保险 */
    private String isBocInsu;

    /** 上年度中银保险总金额 */
    private BigDecimal bocInsuAmtLy;

    /** 本年度中银保险总金额 */
    private BigDecimal bocInsuAmt;

    /** 投保保险险种 */
    private String insuType;

    /** 存款利息收入 */
    private BigDecimal ckIntInc;

    /** 贷款利息收入 */
    private BigDecimal loanIntInc;

    /** 净利息收入 */
    private BigDecimal intInc;

    /** 营业收入贡献 */
    private BigDecimal operatingIncContrib;

    /** 税费前利润贡献 */
    private BigDecimal accountContribBeforeTax;

    /** 各核算码项下中收贡献 */
    private BigDecimal misContrib;

    /**
     * 获取 客户号
     * @return CUST_NO
     */
    public String getCustNo() {
        return custNo;
    }

    /**
     * 设置 客户号
     * @parameter CUST_NO
     */
    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    /**
     * 获取 客户名称
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 客户名称
     * @parameter NAME
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取 信贷客户名称
     * @return LOAN_CUST_NAME
     */
    public String getLoanCustName() {
        return loanCustName;
    }

    /**
     * 设置 信贷客户名称
     * @parameter LOAN_CUST_NAME
     */
    public void setLoanCustName(String loanCustName) {
        this.loanCustName = loanCustName;
    }

    /**
     * 获取 客户类型
     * @return CUST_TYPE
     */
    public String getCustType() {
        return custType;
    }

    /**
     * 设置 客户类型
     * @parameter CUST_TYPE
     */
    public void setCustType(String custType) {
        this.custType = custType;
    }

    /**
     * 获取 客户子类型
     * @return CUST_SUB_TYPE
     */
    public String getCustSubType() {
        return custSubType;
    }

    /**
     * 设置 客户子类型
     * @parameter CUST_SUB_TYPE
     */
    public void setCustSubType(String custSubType) {
        this.custSubType = custSubType;
    }

    /**
     * 获取 证件类型
     * @return ID_TYPE
     */
    public String getIdType() {
        return idType;
    }

    /**
     * 设置 证件类型
     * @parameter ID_TYPE
     */
    public void setIdType(String idType) {
        this.idType = idType;
    }

    /**
     * 获取 证件号码
     * @return ID_NUMBER
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * 设置 证件号码
     * @parameter ID_NUMBER
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * 获取 证件到期日
     * @return CUID_ID_EXPIRE_DATE
     */
    public String getCuidIdExpireDate() {
        return cuidIdExpireDate;
    }

    /**
     * 设置 证件到期日
     * @parameter CUID_ID_EXPIRE_DATE
     */
    public void setCuidIdExpireDate(String cuidIdExpireDate) {
        this.cuidIdExpireDate = cuidIdExpireDate;
    }

    /**
     * 获取 所属行
     * @return BR_NO
     */
    public String getBrNo() {
        return brNo;
    }

    /**
     * 设置 所属行
     * @parameter BR_NO
     */
    public void setBrNo(String brNo) {
        this.brNo = brNo;
    }

    /**
     * 获取 所属行名称
     * @return BR_NAME
     */
    public String getBrName() {
        return brName;
    }

    /**
     * 设置 所属行名称
     * @parameter BR_NAME
     */
    public void setBrName(String brName) {
        this.brName = brName;
    }

    /**
     * 获取 客户状态
     * @return CUST_STATUS
     */
    public String getCustStatus() {
        return custStatus;
    }

    /**
     * 设置 客户状态
     * @parameter CUST_STATUS
     */
    public void setCustStatus(String custStatus) {
        this.custStatus = custStatus;
    }

    /**
     * 获取 客户经理号
     * @return CUST_MGR_NO
     */
    public String getCustMgrNo() {
        return custMgrNo;
    }

    /**
     * 设置 客户经理号
     * @parameter CUST_MGR_NO
     */
    public void setCustMgrNo(String custMgrNo) {
        this.custMgrNo = custMgrNo;
    }

    /**
     * 获取 客户经理名称
     * @return CUST_MGR_NAME
     */
    public String getCustMgrName() {
        return custMgrName;
    }

    /**
     * 设置 客户经理名称
     * @parameter CUST_MGR_NAME
     */
    public void setCustMgrName(String custMgrName) {
        this.custMgrName = custMgrName;
    }

    /**
     * 获取 客户经理联系方式
     * @return CUST_MGR_CONTACT
     */
    public String getCustMgrContact() {
        return custMgrContact;
    }

    /**
     * 设置 客户经理联系方式
     * @parameter CUST_MGR_CONTACT
     */
    public void setCustMgrContact(String custMgrContact) {
        this.custMgrContact = custMgrContact;
    }

    /**
     * 获取 客户经理所属机构
     * @return CUST_MGR_BR_NO
     */
    public String getCustMgrBrNo() {
        return custMgrBrNo;
    }

    /**
     * 设置 客户经理所属机构
     * @parameter CUST_MGR_BR_NO
     */
    public void setCustMgrBrNo(String custMgrBrNo) {
        this.custMgrBrNo = custMgrBrNo;
    }

    /**
     * 获取 客户经理所属机构名称
     * @return CUST_MGR_BR_NAME
     */
    public String getCustMgrBrName() {
        return custMgrBrName;
    }

    /**
     * 设置 客户经理所属机构名称
     * @parameter CUST_MGR_BR_NAME
     */
    public void setCustMgrBrName(String custMgrBrName) {
        this.custMgrBrName = custMgrBrName;
    }

    /**
     * 获取 行业分类
     * @return INDUSTRY-CODE
     */
    public String getIndustryCode() {
        return industryCode;
    }

    /**
     * 设置 行业分类
     * @parameter INDUSTRY-CODE
     */
    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    /**
     * 获取 企业性质
     * @return BUS-OWSP
     */
    public String getBusOwsp() {
        return busOwsp;
    }

    /**
     * 设置 企业性质
     * @parameter BUS-OWSP
     */
    public void setBusOwsp(String busOwsp) {
        this.busOwsp = busOwsp;
    }

    /**
     * 获取 主营业务
     * @return BUSINESS-SCOPE
     */
    public String getBusinessScope() {
        return businessScope;
    }

    /**
     * 设置 主营业务
     * @parameter BUSINESS-SCOPE
     */
    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    /**
     * 获取 注册国家
     * @return NATIONALITY-CODE
     */
    public String getNationalityCode() {
        return nationalityCode;
    }

    /**
     * 设置 注册国家
     * @parameter NATIONALITY-CODE
     */
    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    /**
     * 获取 注册资本货币
     * @return REGISTRY-CURR
     */
    public String getRegistryCurr() {
        return registryCurr;
    }

    /**
     * 设置 注册资本货币
     * @parameter REGISTRY-CURR
     */
    public void setRegistryCurr(String registryCurr) {
        this.registryCurr = registryCurr;
    }

    /**
     * 获取 注册资本金额
     * @return CAPITAL-AMOUNT
     */
    public BigDecimal getCapitalAmount() {
        return capitalAmount;
    }

    /**
     * 设置 注册资本金额
     * @parameter CAPITAL-AMOUNT
     */
    public void setCapitalAmount(BigDecimal capitalAmount) {
        this.capitalAmount = capitalAmount;
    }

    /**
     * 获取 注册地址
     * @return REG-ADDR
     */
    public String getRegAddr() {
        return regAddr;
    }

    /**
     * 设置 注册地址
     * @parameter REG-ADDR
     */
    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr;
    }

    /**
     * 获取 注册时间
     * @return BUS-REGISTER-DATE
     */
    public String getBusRegisterDate() {
        return busRegisterDate;
    }

    /**
     * 设置 注册时间
     * @parameter BUS-REGISTER-DATE
     */
    public void setBusRegisterDate(String busRegisterDate) {
        this.busRegisterDate = busRegisterDate;
    }

    /**
     * 获取 企业创建年份
     * @return CREATE_DATE
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * 设置 企业创建年份
     * @parameter CREATE_DATE
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取 企业规模
     * @return COMPANY-SIZE
     */
    public String getCompanySize() {
        return companySize;
    }

    /**
     * 设置 企业规模
     * @parameter COMPANY-SIZE
     */
    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    /**
     * 获取 是否上市公司
     * @return ON-STOCK-MARKET
     */
    public String getOnStockMarket() {
        return onStockMarket;
    }

    /**
     * 设置 是否上市公司
     * @parameter ON-STOCK-MARKET
     */
    public void setOnStockMarket(String onStockMarket) {
        this.onStockMarket = onStockMarket;
    }

    /**
     * 获取 集团客户标识
     * @return GROUP-FLAG
     */
    public String getGroupFlag() {
        return groupFlag;
    }

    /**
     * 设置 集团客户标识
     * @parameter GROUP-FLAG
     */
    public void setGroupFlag(String groupFlag) {
        this.groupFlag = groupFlag;
    }

    /**
     * 获取 客户简称
     * @return SHRT-NAME
     */
    public String getShrtName() {
        return shrtName;
    }

    /**
     * 设置 客户简称
     * @parameter SHRT-NAME
     */
    public void setShrtName(String shrtName) {
        this.shrtName = shrtName;
    }

    /**
     * 获取 行业授信政策
     * @return INDUSTRY-CREDIT-POLIC
     */
    public String getIndustryCreditPolic() {
        return industryCreditPolic;
    }

    /**
     * 设置 行业授信政策
     * @parameter INDUSTRY-CREDIT-POLIC
     */
    public void setIndustryCreditPolic(String industryCreditPolic) {
        this.industryCreditPolic = industryCreditPolic;
    }

    /**
     * 获取 信用评级
     * @return CREDIT-RATING
     */
    public String getCreditRating() {
        return creditRating;
    }

    /**
     * 设置 信用评级
     * @parameter CREDIT-RATING
     */
    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    /**
     * 获取 是否集团管理成员
     * @return ISGRPMNGMB
     */
    public String getIsgrpmngmb() {
        return isgrpmngmb;
    }

    /**
     * 设置 是否集团管理成员
     * @parameter ISGRPMNGMB
     */
    public void setIsgrpmngmb(String isgrpmngmb) {
        this.isgrpmngmb = isgrpmngmb;
    }

    /**
     * 获取 是否集团内核心成员
     * @return ISGRPCOREMB
     */
    public String getIsgrpcoremb() {
        return isgrpcoremb;
    }

    /**
     * 设置 是否集团内核心成员
     * @parameter ISGRPCOREMB
     */
    public void setIsgrpcoremb(String isgrpcoremb) {
        this.isgrpcoremb = isgrpcoremb;
    }

    /**
     * 获取 是否集团母公司
     * @return ISGRPPRTCOMP
     */
    public String getIsgrpprtcomp() {
        return isgrpprtcomp;
    }

    /**
     * 设置 是否集团母公司
     * @parameter ISGRPPRTCOMP
     */
    public void setIsgrpprtcomp(String isgrpprtcomp) {
        this.isgrpprtcomp = isgrpprtcomp;
    }

    /**
     * 获取 实收资本（万元）
     * @return FACTCAP
     */
    public BigDecimal getFactcap() {
        return factcap;
    }

    /**
     * 设置 实收资本（万元）
     * @parameter FACTCAP
     */
    public void setFactcap(BigDecimal factcap) {
        this.factcap = factcap;
    }

    /**
     * 获取 财报日期
     * @return RPT_DATE
     */
    public String getRptDate() {
        return rptDate;
    }

    /**
     * 设置 财报日期
     * @parameter RPT_DATE
     */
    public void setRptDate(String rptDate) {
        this.rptDate = rptDate;
    }

    /**
     * 获取 流动资产
     * @return FILED1
     */
    public BigDecimal getFiled1() {
        return filed1;
    }

    /**
     * 设置 流动资产
     * @parameter FILED1
     */
    public void setFiled1(BigDecimal filed1) {
        this.filed1 = filed1;
    }

    /**
     * 获取 货币资金
     * @return FILED2
     */
    public BigDecimal getFiled2() {
        return filed2;
    }

    /**
     * 设置 货币资金
     * @parameter FILED2
     */
    public void setFiled2(BigDecimal filed2) {
        this.filed2 = filed2;
    }

    /**
     * 获取 短期投资
     * @return FILED3
     */
    public BigDecimal getFiled3() {
        return filed3;
    }

    /**
     * 设置 短期投资
     * @parameter FILED3
     */
    public void setFiled3(BigDecimal filed3) {
        this.filed3 = filed3;
    }

    /**
     * 获取 应收票据
     * @return FILED4
     */
    public BigDecimal getFiled4() {
        return filed4;
    }

    /**
     * 设置 应收票据
     * @parameter FILED4
     */
    public void setFiled4(BigDecimal filed4) {
        this.filed4 = filed4;
    }

    /**
     * 获取 应收账款
     * @return FILED5
     */
    public BigDecimal getFiled5() {
        return filed5;
    }

    /**
     * 设置 应收账款
     * @parameter FILED5
     */
    public void setFiled5(BigDecimal filed5) {
        this.filed5 = filed5;
    }

    /**
     * 获取 预付账款
     * @return FILED6
     */
    public BigDecimal getFiled6() {
        return filed6;
    }

    /**
     * 设置 预付账款
     * @parameter FILED6
     */
    public void setFiled6(BigDecimal filed6) {
        this.filed6 = filed6;
    }

    /**
     * 获取 存货
     * @return FILED7
     */
    public BigDecimal getFiled7() {
        return filed7;
    }

    /**
     * 设置 存货
     * @parameter FILED7
     */
    public void setFiled7(BigDecimal filed7) {
        this.filed7 = filed7;
    }

    /**
     * 获取 其他应收款
     * @return FILED8
     */
    public BigDecimal getFiled8() {
        return filed8;
    }

    /**
     * 设置 其他应收款
     * @parameter FILED8
     */
    public void setFiled8(BigDecimal filed8) {
        this.filed8 = filed8;
    }

    /**
     * 获取 固定资产
     * @return FILED9
     */
    public BigDecimal getFiled9() {
        return filed9;
    }

    /**
     * 设置 固定资产
     * @parameter FILED9
     */
    public void setFiled9(BigDecimal filed9) {
        this.filed9 = filed9;
    }

    /**
     * 获取 长期投资
     * @return FILED10
     */
    public BigDecimal getFiled10() {
        return filed10;
    }

    /**
     * 设置 长期投资
     * @parameter FILED10
     */
    public void setFiled10(BigDecimal filed10) {
        this.filed10 = filed10;
    }

    /**
     * 获取 在建工程
     * @return FILED11
     */
    public BigDecimal getFiled11() {
        return filed11;
    }

    /**
     * 设置 在建工程
     * @parameter FILED11
     */
    public void setFiled11(BigDecimal filed11) {
        this.filed11 = filed11;
    }

    /**
     * 获取 无形资产
     * @return FILED12
     */
    public BigDecimal getFiled12() {
        return filed12;
    }

    /**
     * 设置 无形资产
     * @parameter FILED12
     */
    public void setFiled12(BigDecimal filed12) {
        this.filed12 = filed12;
    }

    /**
     * 获取 资产总计
     * @return FILED13
     */
    public BigDecimal getFiled13() {
        return filed13;
    }

    /**
     * 设置 资产总计
     * @parameter FILED13
     */
    public void setFiled13(BigDecimal filed13) {
        this.filed13 = filed13;
    }

    /**
     * 获取 短期借款
     * @return FILED14
     */
    public BigDecimal getFiled14() {
        return filed14;
    }

    /**
     * 设置 短期借款
     * @parameter FILED14
     */
    public void setFiled14(BigDecimal filed14) {
        this.filed14 = filed14;
    }

    /**
     * 获取 应付票据
     * @return FILED15
     */
    public BigDecimal getFiled15() {
        return filed15;
    }

    /**
     * 设置 应付票据
     * @parameter FILED15
     */
    public void setFiled15(BigDecimal filed15) {
        this.filed15 = filed15;
    }

    /**
     * 获取 应付账款
     * @return FILED16
     */
    public BigDecimal getFiled16() {
        return filed16;
    }

    /**
     * 设置 应付账款
     * @parameter FILED16
     */
    public void setFiled16(BigDecimal filed16) {
        this.filed16 = filed16;
    }

    /**
     * 获取 预收账款
     * @return FILED17
     */
    public BigDecimal getFiled17() {
        return filed17;
    }

    /**
     * 设置 预收账款
     * @parameter FILED17
     */
    public void setFiled17(BigDecimal filed17) {
        this.filed17 = filed17;
    }

    /**
     * 获取 应付股利
     * @return FILED18
     */
    public BigDecimal getFiled18() {
        return filed18;
    }

    /**
     * 设置 应付股利
     * @parameter FILED18
     */
    public void setFiled18(BigDecimal filed18) {
        this.filed18 = filed18;
    }

    /**
     * 获取 长期借款
     * @return FILED19
     */
    public BigDecimal getFiled19() {
        return filed19;
    }

    /**
     * 设置 长期借款
     * @parameter FILED19
     */
    public void setFiled19(BigDecimal filed19) {
        this.filed19 = filed19;
    }

    /**
     * 获取 应付债券
     * @return FILED20
     */
    public BigDecimal getFiled20() {
        return filed20;
    }

    /**
     * 设置 应付债券
     * @parameter FILED20
     */
    public void setFiled20(BigDecimal filed20) {
        this.filed20 = filed20;
    }

    /**
     * 获取 长期应付款
     * @return FILED21
     */
    public BigDecimal getFiled21() {
        return filed21;
    }

    /**
     * 设置 长期应付款
     * @parameter FILED21
     */
    public void setFiled21(BigDecimal filed21) {
        this.filed21 = filed21;
    }

    /**
     * 获取 所有者权益
     * @return FILED22
     */
    public BigDecimal getFiled22() {
        return filed22;
    }

    /**
     * 设置 所有者权益
     * @parameter FILED22
     */
    public void setFiled22(BigDecimal filed22) {
        this.filed22 = filed22;
    }

    /**
     * 获取 实收资本
     * @return FILED23
     */
    public BigDecimal getFiled23() {
        return filed23;
    }

    /**
     * 设置 实收资本
     * @parameter FILED23
     */
    public void setFiled23(BigDecimal filed23) {
        this.filed23 = filed23;
    }

    /**
     * 获取 资本公积
     * @return FILED24
     */
    public BigDecimal getFiled24() {
        return filed24;
    }

    /**
     * 设置 资本公积
     * @parameter FILED24
     */
    public void setFiled24(BigDecimal filed24) {
        this.filed24 = filed24;
    }

    /**
     * 获取 盈余公积
     * @return FILED25
     */
    public BigDecimal getFiled25() {
        return filed25;
    }

    /**
     * 设置 盈余公积
     * @parameter FILED25
     */
    public void setFiled25(BigDecimal filed25) {
        this.filed25 = filed25;
    }

    /**
     * 获取 未分配利润
     * @return FILED26
     */
    public BigDecimal getFiled26() {
        return filed26;
    }

    /**
     * 设置 未分配利润
     * @parameter FILED26
     */
    public void setFiled26(BigDecimal filed26) {
        this.filed26 = filed26;
    }

    /**
     * 获取 人民币存款时点
     * @return CK_CURR_CNY
     */
    public BigDecimal getCkCurrCny() {
        return ckCurrCny;
    }

    /**
     * 设置 人民币存款时点
     * @parameter CK_CURR_CNY
     */
    public void setCkCurrCny(BigDecimal ckCurrCny) {
        this.ckCurrCny = ckCurrCny;
    }

    /**
     * 获取 人民币存款日均
     * @return CK_NRJ_CNY
     */
    public BigDecimal getCkNrjCny() {
        return ckNrjCny;
    }

    /**
     * 设置 人民币存款日均
     * @parameter CK_NRJ_CNY
     */
    public void setCkNrjCny(BigDecimal ckNrjCny) {
        this.ckNrjCny = ckNrjCny;
    }

    /**
     * 获取 12个月人民币存款时点最高额
     * @return CK_12TOP_CNY
     */
    public BigDecimal getCk12topCny() {
        return ck12topCny;
    }

    /**
     * 设置 12个月人民币存款时点最高额
     * @parameter CK_12TOP_CNY
     */
    public void setCk12topCny(BigDecimal ck12topCny) {
        this.ck12topCny = ck12topCny;
    }

    /**
     * 获取 外币存款时点
     * @return CK_CURR_WB
     */
    public BigDecimal getCkCurrWb() {
        return ckCurrWb;
    }

    /**
     * 设置 外币存款时点
     * @parameter CK_CURR_WB
     */
    public void setCkCurrWb(BigDecimal ckCurrWb) {
        this.ckCurrWb = ckCurrWb;
    }

    /**
     * 获取 外币存款日均
     * @return CK_NRJ_WB
     */
    public BigDecimal getCkNrjWb() {
        return ckNrjWb;
    }

    /**
     * 设置 外币存款日均
     * @parameter CK_NRJ_WB
     */
    public void setCkNrjWb(BigDecimal ckNrjWb) {
        this.ckNrjWb = ckNrjWb;
    }

    /**
     * 获取 12个月外币存款时点最高额
     * @return CK_12TOP_WB
     */
    public BigDecimal getCk12topWb() {
        return ck12topWb;
    }

    /**
     * 设置 12个月外币存款时点最高额
     * @parameter CK_12TOP_WB
     */
    public void setCk12topWb(BigDecimal ck12topWb) {
        this.ck12topWb = ck12topWb;
    }

    /**
     * 获取 人民币活期存款时点余额
     * @return CK_HQCURR_CNY
     */
    public BigDecimal getCkHqcurrCny() {
        return ckHqcurrCny;
    }

    /**
     * 设置 人民币活期存款时点余额
     * @parameter CK_HQCURR_CNY
     */
    public void setCkHqcurrCny(BigDecimal ckHqcurrCny) {
        this.ckHqcurrCny = ckHqcurrCny;
    }

    /**
     * 获取 人民币活期存款日均余额
     * @return CK_HQNRJ_CNY
     */
    public BigDecimal getCkHqnrjCny() {
        return ckHqnrjCny;
    }

    /**
     * 设置 人民币活期存款日均余额
     * @parameter CK_HQNRJ_CNY
     */
    public void setCkHqnrjCny(BigDecimal ckHqnrjCny) {
        this.ckHqnrjCny = ckHqnrjCny;
    }

    /**
     * 获取 人民币定期存款时点余额
     * @return CK_DQCURR_CNY
     */
    public BigDecimal getCkDqcurrCny() {
        return ckDqcurrCny;
    }

    /**
     * 设置 人民币定期存款时点余额
     * @parameter CK_DQCURR_CNY
     */
    public void setCkDqcurrCny(BigDecimal ckDqcurrCny) {
        this.ckDqcurrCny = ckDqcurrCny;
    }

    /**
     * 获取 人民币定期存款日均余额
     * @return CK_DQNRJ_CNY
     */
    public BigDecimal getCkDqnrjCny() {
        return ckDqnrjCny;
    }

    /**
     * 设置 人民币定期存款日均余额
     * @parameter CK_DQNRJ_CNY
     */
    public void setCkDqnrjCny(BigDecimal ckDqnrjCny) {
        this.ckDqnrjCny = ckDqnrjCny;
    }

    /**
     * 获取 外币活期存款时点余额
     * @return CK_HQCURR_WB
     */
    public BigDecimal getCkHqcurrWb() {
        return ckHqcurrWb;
    }

    /**
     * 设置 外币活期存款时点余额
     * @parameter CK_HQCURR_WB
     */
    public void setCkHqcurrWb(BigDecimal ckHqcurrWb) {
        this.ckHqcurrWb = ckHqcurrWb;
    }

    /**
     * 获取 外币活期存款日均余额
     * @return CK_HQNRJ_WB
     */
    public BigDecimal getCkHqnrjWb() {
        return ckHqnrjWb;
    }

    /**
     * 设置 外币活期存款日均余额
     * @parameter CK_HQNRJ_WB
     */
    public void setCkHqnrjWb(BigDecimal ckHqnrjWb) {
        this.ckHqnrjWb = ckHqnrjWb;
    }

    /**
     * 获取 外币定期存款时点余额
     * @return CK_DQCURR_WB
     */
    public BigDecimal getCkDqcurrWb() {
        return ckDqcurrWb;
    }

    /**
     * 设置 外币定期存款时点余额
     * @parameter CK_DQCURR_WB
     */
    public void setCkDqcurrWb(BigDecimal ckDqcurrWb) {
        this.ckDqcurrWb = ckDqcurrWb;
    }

    /**
     * 获取 外币定期存款日均余额
     * @return CK_DQNRJ_WB
     */
    public BigDecimal getCkDqnrjWb() {
        return ckDqnrjWb;
    }

    /**
     * 设置 外币定期存款日均余额
     * @parameter CK_DQNRJ_WB
     */
    public void setCkDqnrjWb(BigDecimal ckDqnrjWb) {
        this.ckDqnrjWb = ckDqnrjWb;
    }

    /**
     * 获取 是否授信客户
     * @return IS_SXCUST
     */
    public String getIsSxcust() {
        return isSxcust;
    }

    /**
     * 设置 是否授信客户
     * @parameter IS_SXCUST
     */
    public void setIsSxcust(String isSxcust) {
        this.isSxcust = isSxcust;
    }

    /**
     * 获取 授信分类
     * @return GIVENTYPE
     */
    public String getGiventype() {
        return giventype;
    }

    /**
     * 设置 授信分类
     * @parameter GIVENTYPE
     */
    public void setGiventype(String giventype) {
        this.giventype = giventype;
    }

    /**
     * 获取 信用评级
     * @return LOAN_CREDIT-RATING
     */
    public String getLoanCreditRating() {
        return loanCreditRating;
    }

    /**
     * 设置 信用评级
     * @parameter LOAN_CREDIT-RATING
     */
    public void setLoanCreditRating(String loanCreditRating) {
        this.loanCreditRating = loanCreditRating;
    }

    /**
     * 获取 授信总额
     * @return LOANCASH
     */
    public BigDecimal getLoancash() {
        return loancash;
    }

    /**
     * 设置 授信总额
     * @parameter LOANCASH
     */
    public void setLoancash(BigDecimal loancash) {
        this.loancash = loancash;
    }

    /**
     * 获取 授信种类
     * @return LOAN_TYPE
     */
    public String getLoanType() {
        return loanType;
    }

    /**
     * 设置 授信种类
     * @parameter LOAN_TYPE
     */
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    /**
     * 获取 中小企业贷款产品
     * @return LOAN_PRO_ZX
     */
    public String getLoanProZx() {
        return loanProZx;
    }

    /**
     * 设置 中小企业贷款产品
     * @parameter LOAN_PRO_ZX
     */
    public void setLoanProZx(String loanProZx) {
        this.loanProZx = loanProZx;
    }

    /**
     * 获取 最新批复有效期
     * @return ORPVDPD
     */
    public Short getOrpvdpd() {
        return orpvdpd;
    }

    /**
     * 设置 最新批复有效期
     * @parameter ORPVDPD
     */
    public void setOrpvdpd(Short orpvdpd) {
        this.orpvdpd = orpvdpd;
    }

    /**
     * 获取 人民币贷款时点
     * @return LOAN_CURR_CNY
     */
    public BigDecimal getLoanCurrCny() {
        return loanCurrCny;
    }

    /**
     * 设置 人民币贷款时点
     * @parameter LOAN_CURR_CNY
     */
    public void setLoanCurrCny(BigDecimal loanCurrCny) {
        this.loanCurrCny = loanCurrCny;
    }

    /**
     * 获取 人民币贷款日均
     * @return LOAN_NRJ_CNY
     */
    public BigDecimal getLoanNrjCny() {
        return loanNrjCny;
    }

    /**
     * 设置 人民币贷款日均
     * @parameter LOAN_NRJ_CNY
     */
    public void setLoanNrjCny(BigDecimal loanNrjCny) {
        this.loanNrjCny = loanNrjCny;
    }

    /**
     * 获取 12个月人民币贷款时点最高额
     * @return LOAN_12TOP_CNY
     */
    public BigDecimal getLoan12topCny() {
        return loan12topCny;
    }

    /**
     * 设置 12个月人民币贷款时点最高额
     * @parameter LOAN_12TOP_CNY
     */
    public void setLoan12topCny(BigDecimal loan12topCny) {
        this.loan12topCny = loan12topCny;
    }

    /**
     * 获取 人民币贸易融资时点
     * @return RZ_CURR_CNY
     */
    public BigDecimal getRzCurrCny() {
        return rzCurrCny;
    }

    /**
     * 设置 人民币贸易融资时点
     * @parameter RZ_CURR_CNY
     */
    public void setRzCurrCny(BigDecimal rzCurrCny) {
        this.rzCurrCny = rzCurrCny;
    }

    /**
     * 获取 人民币贸易融资日均
     * @return RZ_NRJ_CNY
     */
    public BigDecimal getRzNrjCny() {
        return rzNrjCny;
    }

    /**
     * 设置 人民币贸易融资日均
     * @parameter RZ_NRJ_CNY
     */
    public void setRzNrjCny(BigDecimal rzNrjCny) {
        this.rzNrjCny = rzNrjCny;
    }

    /**
     * 获取 贴现时点余额
     * @return TX_CURR
     */
    public BigDecimal getTxCurr() {
        return txCurr;
    }

    /**
     * 设置 贴现时点余额
     * @parameter TX_CURR
     */
    public void setTxCurr(BigDecimal txCurr) {
        this.txCurr = txCurr;
    }

    /**
     * 获取 贴现日均余额
     * @return TX_NRJ
     */
    public BigDecimal getTxNrj() {
        return txNrj;
    }

    /**
     * 设置 贴现日均余额
     * @parameter TX_NRJ
     */
    public void setTxNrj(BigDecimal txNrj) {
        this.txNrj = txNrj;
    }

    /**
     * 获取 外币贷款时点
     * @return LOAN_CURR_WB
     */
    public BigDecimal getLoanCurrWb() {
        return loanCurrWb;
    }

    /**
     * 设置 外币贷款时点
     * @parameter LOAN_CURR_WB
     */
    public void setLoanCurrWb(BigDecimal loanCurrWb) {
        this.loanCurrWb = loanCurrWb;
    }

    /**
     * 获取 外币贷款日均
     * @return LOAN_NRJ_WB
     */
    public BigDecimal getLoanNrjWb() {
        return loanNrjWb;
    }

    /**
     * 设置 外币贷款日均
     * @parameter LOAN_NRJ_WB
     */
    public void setLoanNrjWb(BigDecimal loanNrjWb) {
        this.loanNrjWb = loanNrjWb;
    }

    /**
     * 获取 12个月外币贷款时点最高额
     * @return LOAN_12TOP_WB
     */
    public BigDecimal getLoan12topWb() {
        return loan12topWb;
    }

    /**
     * 设置 12个月外币贷款时点最高额
     * @parameter LOAN_12TOP_WB
     */
    public void setLoan12topWb(BigDecimal loan12topWb) {
        this.loan12topWb = loan12topWb;
    }

    /**
     * 获取 外币贸易融资时点
     * @return RZ_CURR_WB
     */
    public BigDecimal getRzCurrWb() {
        return rzCurrWb;
    }

    /**
     * 设置 外币贸易融资时点
     * @parameter RZ_CURR_WB
     */
    public void setRzCurrWb(BigDecimal rzCurrWb) {
        this.rzCurrWb = rzCurrWb;
    }

    /**
     * 获取 外币贸易融资日均
     * @return RZ_NRJ_WB
     */
    public BigDecimal getRzNrjWb() {
        return rzNrjWb;
    }

    /**
     * 设置 外币贸易融资日均
     * @parameter RZ_NRJ_WB
     */
    public void setRzNrjWb(BigDecimal rzNrjWb) {
        this.rzNrjWb = rzNrjWb;
    }

    /**
     * 获取 是否开通网银
     * @return IS_BOCNET
     */
    public String getIsBocnet() {
        return isBocnet;
    }

    /**
     * 设置 是否开通网银
     * @parameter IS_BOCNET
     */
    public void setIsBocnet(String isBocnet) {
        this.isBocnet = isBocnet;
    }

    /**
     * 获取 是否网银交易客户
     * @return IS_BOCNET_TRAN
     */
    public String getIsBocnetTran() {
        return isBocnetTran;
    }

    /**
     * 设置 是否网银交易客户
     * @parameter IS_BOCNET_TRAN
     */
    public void setIsBocnetTran(String isBocnetTran) {
        this.isBocnetTran = isBocnetTran;
    }

    /**
     * 获取 当日网银大额转账交易笔数（交易金额大于1000万）
     * @return BOCNET_DETRAN_NUM
     */
    public BigDecimal getBocnetDetranNum() {
        return bocnetDetranNum;
    }

    /**
     * 设置 当日网银大额转账交易笔数（交易金额大于1000万）
     * @parameter BOCNET_DETRAN_NUM
     */
    public void setBocnetDetranNum(BigDecimal bocnetDetranNum) {
        this.bocnetDetranNum = bocnetDetranNum;
    }

    /**
     * 获取 当日网银大额转账交易金额（交易金额大于1000万）
     * @return BOCNET_DETRAN_AMT
     */
    public BigDecimal getBocnetDetranAmt() {
        return bocnetDetranAmt;
    }

    /**
     * 设置 当日网银大额转账交易金额（交易金额大于1000万）
     * @parameter BOCNET_DETRAN_AMT
     */
    public void setBocnetDetranAmt(BigDecimal bocnetDetranAmt) {
        this.bocnetDetranAmt = bocnetDetranAmt;
    }

    /**
     * 获取 是否开通网银购买理财功能
     * @return IS_BOCNET_TRANXPAD
     */
    public String getIsBocnetTranxpad() {
        return isBocnetTranxpad;
    }

    /**
     * 设置 是否开通网银购买理财功能
     * @parameter IS_BOCNET_TRANXPAD
     */
    public void setIsBocnetTranxpad(String isBocnetTranxpad) {
        this.isBocnetTranxpad = isBocnetTranxpad;
    }

    /**
     * 获取 3个月网银渠道购买理财交易笔数
     * @return BOCNET_PAD_3MON_NUM
     */
    public BigDecimal getBocnetPad3monNum() {
        return bocnetPad3monNum;
    }

    /**
     * 设置 3个月网银渠道购买理财交易笔数
     * @parameter BOCNET_PAD_3MON_NUM
     */
    public void setBocnetPad3monNum(BigDecimal bocnetPad3monNum) {
        this.bocnetPad3monNum = bocnetPad3monNum;
    }

    /**
     * 获取 3个月网银渠道购买理财交易金额
     * @return BOCNET_PAD_3MON_AMT
     */
    public BigDecimal getBocnetPad3monAmt() {
        return bocnetPad3monAmt;
    }

    /**
     * 设置 3个月网银渠道购买理财交易金额
     * @parameter BOCNET_PAD_3MON_AMT
     */
    public void setBocnetPad3monAmt(BigDecimal bocnetPad3monAmt) {
        this.bocnetPad3monAmt = bocnetPad3monAmt;
    }

    /**
     * 获取 3个月网银交易笔数
     * @return BOCNET_TRAN_3MON_NUM
     */
    public BigDecimal getBocnetTran3monNum() {
        return bocnetTran3monNum;
    }

    /**
     * 设置 3个月网银交易笔数
     * @parameter BOCNET_TRAN_3MON_NUM
     */
    public void setBocnetTran3monNum(BigDecimal bocnetTran3monNum) {
        this.bocnetTran3monNum = bocnetTran3monNum;
    }

    /**
     * 获取 6个月网银交易笔数
     * @return BOCNET_TRAN_6MON_NUM
     */
    public BigDecimal getBocnetTran6monNum() {
        return bocnetTran6monNum;
    }

    /**
     * 设置 6个月网银交易笔数
     * @parameter BOCNET_TRAN_6MON_NUM
     */
    public void setBocnetTran6monNum(BigDecimal bocnetTran6monNum) {
        this.bocnetTran6monNum = bocnetTran6monNum;
    }

    /**
     * 获取 12个月网银交易笔数
     * @return BOCNET_TRAN_12MON_NUM
     */
    public BigDecimal getBocnetTran12monNum() {
        return bocnetTran12monNum;
    }

    /**
     * 设置 12个月网银交易笔数
     * @parameter BOCNET_TRAN_12MON_NUM
     */
    public void setBocnetTran12monNum(BigDecimal bocnetTran12monNum) {
        this.bocnetTran12monNum = bocnetTran12monNum;
    }

    /**
     * 获取 3个月网银交易金额
     * @return BOCNET_TRAN_3MON_AMT
     */
    public BigDecimal getBocnetTran3monAmt() {
        return bocnetTran3monAmt;
    }

    /**
     * 设置 3个月网银交易金额
     * @parameter BOCNET_TRAN_3MON_AMT
     */
    public void setBocnetTran3monAmt(BigDecimal bocnetTran3monAmt) {
        this.bocnetTran3monAmt = bocnetTran3monAmt;
    }

    /**
     * 获取 6个月网银交易金额
     * @return BOCNET_TRAN_6MON_AMT
     */
    public BigDecimal getBocnetTran6monAmt() {
        return bocnetTran6monAmt;
    }

    /**
     * 设置 6个月网银交易金额
     * @parameter BOCNET_TRAN_6MON_AMT
     */
    public void setBocnetTran6monAmt(BigDecimal bocnetTran6monAmt) {
        this.bocnetTran6monAmt = bocnetTran6monAmt;
    }

    /**
     * 获取 12个月网银交易金额
     * @return BOCNET_TRAN_12MON_AMT
     */
    public BigDecimal getBocnetTran12monAmt() {
        return bocnetTran12monAmt;
    }

    /**
     * 设置 12个月网银交易金额
     * @parameter BOCNET_TRAN_12MON_AMT
     */
    public void setBocnetTran12monAmt(BigDecimal bocnetTran12monAmt) {
        this.bocnetTran12monAmt = bocnetTran12monAmt;
    }

    /**
     * 获取 是否开通网上对账功能
     * @return IS_BOCNET_DZ
     */
    public String getIsBocnetDz() {
        return isBocnetDz;
    }

    /**
     * 设置 是否开通网上对账功能
     * @parameter IS_BOCNET_DZ
     */
    public void setIsBocnetDz(String isBocnetDz) {
        this.isBocnetDz = isBocnetDz;
    }

    /**
     * 获取 当月是否已完成网上对账
     * @return IS_DZ_DONE
     */
    public String getIsDzDone() {
        return isDzDone;
    }

    /**
     * 设置 当月是否已完成网上对账
     * @parameter IS_DZ_DONE
     */
    public void setIsDzDone(String isDzDone) {
        this.isDzDone = isDzDone;
    }

    /**
     * 获取 是否办理集团网银
     * @return IS_GROUP_BOCNET
     */
    public String getIsGroupBocnet() {
        return isGroupBocnet;
    }

    /**
     * 设置 是否办理集团网银
     * @parameter IS_GROUP_BOCNET
     */
    public void setIsGroupBocnet(String isGroupBocnet) {
        this.isGroupBocnet = isGroupBocnet;
    }

    /**
     * 获取 是否开通B2C支付业务
     * @return IS_B2C
     */
    public String getIsB2c() {
        return isB2c;
    }

    /**
     * 设置 是否开通B2C支付业务
     * @parameter IS_B2C
     */
    public void setIsB2c(String isB2c) {
        this.isB2c = isB2c;
    }

    /**
     * 获取 B2C支付_当年累计交易笔数
     * @return B2C_TRAN_NUM
     */
    public BigDecimal getB2cTranNum() {
        return b2cTranNum;
    }

    /**
     * 设置 B2C支付_当年累计交易笔数
     * @parameter B2C_TRAN_NUM
     */
    public void setB2cTranNum(BigDecimal b2cTranNum) {
        this.b2cTranNum = b2cTranNum;
    }

    /**
     * 获取 B2C支付_当年累计交易金额
     * @return B2C_TRAN_AMT
     */
    public BigDecimal getB2cTranAmt() {
        return b2cTranAmt;
    }

    /**
     * 设置 B2C支付_当年累计交易金额
     * @parameter B2C_TRAN_AMT
     */
    public void setB2cTranAmt(BigDecimal b2cTranAmt) {
        this.b2cTranAmt = b2cTranAmt;
    }

    /**
     * 获取 是否开通B2B支付业务
     * @return IS_B2B
     */
    public String getIsB2b() {
        return isB2b;
    }

    /**
     * 设置 是否开通B2B支付业务
     * @parameter IS_B2B
     */
    public void setIsB2b(String isB2b) {
        this.isB2b = isB2b;
    }

    /**
     * 获取 B2B支付_当年累计交易笔数
     * @return B2B_TRAN_NUM
     */
    public BigDecimal getB2bTranNum() {
        return b2bTranNum;
    }

    /**
     * 设置 B2B支付_当年累计交易笔数
     * @parameter B2B_TRAN_NUM
     */
    public void setB2bTranNum(BigDecimal b2bTranNum) {
        this.b2bTranNum = b2bTranNum;
    }

    /**
     * 获取 B2B支付_当年累计交易金额
     * @return B2B_TRAN_AMT
     */
    public BigDecimal getB2bTranAmt() {
        return b2bTranAmt;
    }

    /**
     * 设置 B2B支付_当年累计交易金额
     * @parameter B2B_TRAN_AMT
     */
    public void setB2bTranAmt(BigDecimal b2bTranAmt) {
        this.b2bTranAmt = b2bTranAmt;
    }

    /**
     * 获取 是否报关即时通交易客户
     * @return IS_BGJST_TRAN
     */
    public String getIsBgjstTran() {
        return isBgjstTran;
    }

    /**
     * 设置 是否报关即时通交易客户
     * @parameter IS_BGJST_TRAN
     */
    public void setIsBgjstTran(String isBgjstTran) {
        this.isBgjstTran = isBgjstTran;
    }

    /**
     * 获取 最近12个月进口贸易总金额
     * @return TOT_JKMY_12MON_AMT
     */
    public BigDecimal getTotJkmy12monAmt() {
        return totJkmy12monAmt;
    }

    /**
     * 设置 最近12个月进口贸易总金额
     * @parameter TOT_JKMY_12MON_AMT
     */
    public void setTotJkmy12monAmt(BigDecimal totJkmy12monAmt) {
        this.totJkmy12monAmt = totJkmy12monAmt;
    }

    /**
     * 获取 是否办理养老金业务
     * @return IS_YLJ
     */
    public String getIsYlj() {
        return isYlj;
    }

    /**
     * 设置 是否办理养老金业务
     * @parameter IS_YLJ
     */
    public void setIsYlj(String isYlj) {
        this.isYlj = isYlj;
    }

    /**
     * 获取 上年度养老金托管业务量
     * @return YLJ_TGAMT_LY
     */
    public BigDecimal getYljTgamtLy() {
        return yljTgamtLy;
    }

    /**
     * 设置 上年度养老金托管业务量
     * @parameter YLJ_TGAMT_LY
     */
    public void setYljTgamtLy(BigDecimal yljTgamtLy) {
        this.yljTgamtLy = yljTgamtLy;
    }

    /**
     * 获取 本年度养老金托管业务量
     * @return YLJ_TGAMT
     */
    public BigDecimal getYljTgamt() {
        return yljTgamt;
    }

    /**
     * 设置 本年度养老金托管业务量
     * @parameter YLJ_TGAMT
     */
    public void setYljTgamt(BigDecimal yljTgamt) {
        this.yljTgamt = yljTgamt;
    }

    /**
     * 获取 上年度养老金账管业务量
     * @return YLJ_ZGAMT_LY
     */
    public BigDecimal getYljZgamtLy() {
        return yljZgamtLy;
    }

    /**
     * 设置 上年度养老金账管业务量
     * @parameter YLJ_ZGAMT_LY
     */
    public void setYljZgamtLy(BigDecimal yljZgamtLy) {
        this.yljZgamtLy = yljZgamtLy;
    }

    /**
     * 获取 本年度养老金账管业务量
     * @return YLJ_ZGAMT
     */
    public BigDecimal getYljZgamt() {
        return yljZgamt;
    }

    /**
     * 设置 本年度养老金账管业务量
     * @parameter YLJ_ZGAMT
     */
    public void setYljZgamt(BigDecimal yljZgamt) {
        this.yljZgamt = yljZgamt;
    }

    /**
     * 获取 上年度养老金中收业务量
     * @return YLJ_TG_MID_LY
     */
    public BigDecimal getYljTgMidLy() {
        return yljTgMidLy;
    }

    /**
     * 设置 上年度养老金中收业务量
     * @parameter YLJ_TG_MID_LY
     */
    public void setYljTgMidLy(BigDecimal yljTgMidLy) {
        this.yljTgMidLy = yljTgMidLy;
    }

    /**
     * 获取 本年度养老金中收业务量
     * @return YLJ_TG_MID
     */
    public BigDecimal getYljTgMid() {
        return yljTgMid;
    }

    /**
     * 设置 本年度养老金中收业务量
     * @parameter YLJ_TG_MID
     */
    public void setYljTgMid(BigDecimal yljTgMid) {
        this.yljTgMid = yljTgMid;
    }

    /**
     * 获取 是否办理大额存单
     * @return IS_DECD
     */
    public String getIsDecd() {
        return isDecd;
    }

    /**
     * 设置 是否办理大额存单
     * @parameter IS_DECD
     */
    public void setIsDecd(String isDecd) {
        this.isDecd = isDecd;
    }

    /**
     * 获取 大额存单时点余额
     * @return DECD_CURR
     */
    public BigDecimal getDecdCurr() {
        return decdCurr;
    }

    /**
     * 设置 大额存单时点余额
     * @parameter DECD_CURR
     */
    public void setDecdCurr(BigDecimal decdCurr) {
        this.decdCurr = decdCurr;
    }

    /**
     * 获取 大额存单日均余额
     * @return DECD_NRJ
     */
    public BigDecimal getDecdNrj() {
        return decdNrj;
    }

    /**
     * 设置 大额存单日均余额
     * @parameter DECD_NRJ
     */
    public void setDecdNrj(BigDecimal decdNrj) {
        this.decdNrj = decdNrj;
    }

    /**
     * 获取 是否开通结算卡
     * @return IS_JSCARD
     */
    public String getIsJscard() {
        return isJscard;
    }

    /**
     * 设置 是否开通结算卡
     * @parameter IS_JSCARD
     */
    public void setIsJscard(String isJscard) {
        this.isJscard = isJscard;
    }

    /**
     * 获取 开通结算卡张数
     * @return JSCARD_NUM
     */
    public BigDecimal getJscardNum() {
        return jscardNum;
    }

    /**
     * 设置 开通结算卡张数
     * @parameter JSCARD_NUM
     */
    public void setJscardNum(BigDecimal jscardNum) {
        this.jscardNum = jscardNum;
    }

    /**
     * 获取 是否开通短信通
     * @return IS_DXT
     */
    public String getIsDxt() {
        return isDxt;
    }

    /**
     * 设置 是否开通短信通
     * @parameter IS_DXT
     */
    public void setIsDxt(String isDxt) {
        this.isDxt = isDxt;
    }

    /**
     * 获取 开通短信通数量
     * @return DXT_NUM
     */
    public BigDecimal getDxtNum() {
        return dxtNum;
    }

    /**
     * 设置 开通短信通数量
     * @parameter DXT_NUM
     */
    public void setDxtNum(BigDecimal dxtNum) {
        this.dxtNum = dxtNum;
    }

    /**
     * 获取 是否开通回单自助服务
     * @return IS_HDZZ
     */
    public String getIsHdzz() {
        return isHdzz;
    }

    /**
     * 设置 是否开通回单自助服务
     * @parameter IS_HDZZ
     */
    public void setIsHdzz(String isHdzz) {
        this.isHdzz = isHdzz;
    }

    /**
     * 获取 是否开通回单箱
     * @return IS_HDX
     */
    public String getIsHdx() {
        return isHdx;
    }

    /**
     * 设置 是否开通回单箱
     * @parameter IS_HDX
     */
    public void setIsHdx(String isHdx) {
        this.isHdx = isHdx;
    }

    /**
     * 获取 上一年度银行汇票业务量
     * @return HP_AMT_LY
     */
    public BigDecimal getHpAmtLy() {
        return hpAmtLy;
    }

    /**
     * 设置 上一年度银行汇票业务量
     * @parameter HP_AMT_LY
     */
    public void setHpAmtLy(BigDecimal hpAmtLy) {
        this.hpAmtLy = hpAmtLy;
    }

    /**
     * 获取 本年度银行汇票业务量
     * @return HP_AMT
     */
    public BigDecimal getHpAmt() {
        return hpAmt;
    }

    /**
     * 设置 本年度银行汇票业务量
     * @parameter HP_AMT
     */
    public void setHpAmt(BigDecimal hpAmt) {
        this.hpAmt = hpAmt;
    }

    /**
     * 获取 上一年度银行本票业务量
     * @return BP_AMT_LY
     */
    public BigDecimal getBpAmtLy() {
        return bpAmtLy;
    }

    /**
     * 设置 上一年度银行本票业务量
     * @parameter BP_AMT_LY
     */
    public void setBpAmtLy(BigDecimal bpAmtLy) {
        this.bpAmtLy = bpAmtLy;
    }

    /**
     * 获取 本年度银行本票业务量
     * @return BP_AMT
     */
    public BigDecimal getBpAmt() {
        return bpAmt;
    }

    /**
     * 设置 本年度银行本票业务量
     * @parameter BP_AMT
     */
    public void setBpAmt(BigDecimal bpAmt) {
        this.bpAmt = bpAmt;
    }

    /**
     * 获取 上一年度支票业务量
     * @return ZP_AMT_LY
     */
    public BigDecimal getZpAmtLy() {
        return zpAmtLy;
    }

    /**
     * 设置 上一年度支票业务量
     * @parameter ZP_AMT_LY
     */
    public void setZpAmtLy(BigDecimal zpAmtLy) {
        this.zpAmtLy = zpAmtLy;
    }

    /**
     * 获取 本年度支行业务量
     * @return ZP_AMT
     */
    public BigDecimal getZpAmt() {
        return zpAmt;
    }

    /**
     * 设置 本年度支行业务量
     * @parameter ZP_AMT
     */
    public void setZpAmt(BigDecimal zpAmt) {
        this.zpAmt = zpAmt;
    }

    /**
     * 获取 是否开办中央财政授权支付
     * @return IS_ZYCASQZF
     */
    public String getIsZycasqzf() {
        return isZycasqzf;
    }

    /**
     * 设置 是否开办中央财政授权支付
     * @parameter IS_ZYCASQZF
     */
    public void setIsZycasqzf(String isZycasqzf) {
        this.isZycasqzf = isZycasqzf;
    }

    /**
     * 获取 是否开办上门服务
     * @return IS_SMFW
     */
    public String getIsSmfw() {
        return isSmfw;
    }

    /**
     * 设置 是否开办上门服务
     * @parameter IS_SMFW
     */
    public void setIsSmfw(String isSmfw) {
        this.isSmfw = isSmfw;
    }

    /**
     * 获取 是否开办旅保通服务
     * @return IS_LBT
     */
    public String getIsLbt() {
        return isLbt;
    }

    /**
     * 设置 是否开办旅保通服务
     * @parameter IS_LBT
     */
    public void setIsLbt(String isLbt) {
        this.isLbt = isLbt;
    }

    /**
     * 获取 是否开办现金管理
     * @return IS_GCMS
     */
    public String getIsGcms() {
        return isGcms;
    }

    /**
     * 设置 是否开办现金管理
     * @parameter IS_GCMS
     */
    public void setIsGcms(String isGcms) {
        this.isGcms = isGcms;
    }

    /**
     * 获取 开办现金管理时间
     * @return GCMS_OPEN_DATE
     */
    public String getGcmsOpenDate() {
        return gcmsOpenDate;
    }

    /**
     * 设置 开办现金管理时间
     * @parameter GCMS_OPEN_DATE
     */
    public void setGcmsOpenDate(String gcmsOpenDate) {
        this.gcmsOpenDate = gcmsOpenDate;
    }

    /**
     * 获取 现金管理业务角色
     * @return GCMS_ROLE
     */
    public String getGcmsRole() {
        return gcmsRole;
    }

    /**
     * 设置 现金管理业务角色
     * @parameter GCMS_ROLE
     */
    public void setGcmsRole(String gcmsRole) {
        this.gcmsRole = gcmsRole;
    }

    /**
     * 获取 现金管理产品类型
     * @return GCMS_PROD
     */
    public String getGcmsProd() {
        return gcmsProd;
    }

    /**
     * 设置 现金管理产品类型
     * @parameter GCMS_PROD
     */
    public void setGcmsProd(String gcmsProd) {
        this.gcmsProd = gcmsProd;
    }

    /**
     * 获取 上年度现金管理归集量
     * @return GCMS_GJAMT_LY
     */
    public BigDecimal getGcmsGjamtLy() {
        return gcmsGjamtLy;
    }

    /**
     * 设置 上年度现金管理归集量
     * @parameter GCMS_GJAMT_LY
     */
    public void setGcmsGjamtLy(BigDecimal gcmsGjamtLy) {
        this.gcmsGjamtLy = gcmsGjamtLy;
    }

    /**
     * 获取 本年度现金管理归集量
     * @return GCMS_GJAMT
     */
    public BigDecimal getGcmsGjamt() {
        return gcmsGjamt;
    }

    /**
     * 设置 本年度现金管理归集量
     * @parameter GCMS_GJAMT
     */
    public void setGcmsGjamt(BigDecimal gcmsGjamt) {
        this.gcmsGjamt = gcmsGjamt;
    }

    /**
     * 获取 上年度现金管理下拨量
     * @return GCMS_XB_LY
     */
    public BigDecimal getGcmsXbLy() {
        return gcmsXbLy;
    }

    /**
     * 设置 上年度现金管理下拨量
     * @parameter GCMS_XB_LY
     */
    public void setGcmsXbLy(BigDecimal gcmsXbLy) {
        this.gcmsXbLy = gcmsXbLy;
    }

    /**
     * 获取 本年度现金管理下拨量
     * @return GCMS_XB
     */
    public BigDecimal getGcmsXb() {
        return gcmsXb;
    }

    /**
     * 设置 本年度现金管理下拨量
     * @parameter GCMS_XB
     */
    public void setGcmsXb(BigDecimal gcmsXb) {
        this.gcmsXb = gcmsXb;
    }

    /**
     * 获取 现金管理存款日均余额
     * @return GCMS_CK_NRJ
     */
    public BigDecimal getGcmsCkNrj() {
        return gcmsCkNrj;
    }

    /**
     * 设置 现金管理存款日均余额
     * @parameter GCMS_CK_NRJ
     */
    public void setGcmsCkNrj(BigDecimal gcmsCkNrj) {
        this.gcmsCkNrj = gcmsCkNrj;
    }

    /**
     * 获取 现金管理存款日均较上日变动金额
     * @return GCMS_CK_NRJ_BSR
     */
    public BigDecimal getGcmsCkNrjBsr() {
        return gcmsCkNrjBsr;
    }

    /**
     * 设置 现金管理存款日均较上日变动金额
     * @parameter GCMS_CK_NRJ_BSR
     */
    public void setGcmsCkNrjBsr(BigDecimal gcmsCkNrjBsr) {
        this.gcmsCkNrjBsr = gcmsCkNrjBsr;
    }

    /**
     * 获取 现金管理存款日均较上年变动金额
     * @return GCMS_CK_NRJ_BSN
     */
    public BigDecimal getGcmsCkNrjBsn() {
        return gcmsCkNrjBsn;
    }

    /**
     * 设置 现金管理存款日均较上年变动金额
     * @parameter GCMS_CK_NRJ_BSN
     */
    public void setGcmsCkNrjBsn(BigDecimal gcmsCkNrjBsn) {
        this.gcmsCkNrjBsn = gcmsCkNrjBsn;
    }

    /**
     * 获取 是否购买过表内理财
     * @return IS_BN_FIC
     */
    public String getIsBnFic() {
        return isBnFic;
    }

    /**
     * 设置 是否购买过表内理财
     * @parameter IS_BN_FIC
     */
    public void setIsBnFic(String isBnFic) {
        this.isBnFic = isBnFic;
    }

    /**
     * 获取 表内理财余额
     * @return BN_FIC_CURR
     */
    public BigDecimal getBnFicCurr() {
        return bnFicCurr;
    }

    /**
     * 设置 表内理财余额
     * @parameter BN_FIC_CURR
     */
    public void setBnFicCurr(BigDecimal bnFicCurr) {
        this.bnFicCurr = bnFicCurr;
    }

    /**
     * 获取 表内理财年日均
     * @return BN_FIC_NRJ
     */
    public BigDecimal getBnFicNrj() {
        return bnFicNrj;
    }

    /**
     * 设置 表内理财年日均
     * @parameter BN_FIC_NRJ
     */
    public void setBnFicNrj(BigDecimal bnFicNrj) {
        this.bnFicNrj = bnFicNrj;
    }

    /**
     * 获取 表内理财收益
     * @return BN_FIC_PROFIT
     */
    public BigDecimal getBnFicProfit() {
        return bnFicProfit;
    }

    /**
     * 设置 表内理财收益
     * @parameter BN_FIC_PROFIT
     */
    public void setBnFicProfit(BigDecimal bnFicProfit) {
        this.bnFicProfit = bnFicProfit;
    }

    /**
     * 获取 是否购买过表外理财
     * @return IS_BW_FIC
     */
    public BigDecimal getIsBwFic() {
        return isBwFic;
    }

    /**
     * 设置 是否购买过表外理财
     * @parameter IS_BW_FIC
     */
    public void setIsBwFic(BigDecimal isBwFic) {
        this.isBwFic = isBwFic;
    }

    /**
     * 获取 人民币期次类时点
     * @return FIC_CURR_CNY
     */
    public BigDecimal getFicCurrCny() {
        return ficCurrCny;
    }

    /**
     * 设置 人民币期次类时点
     * @parameter FIC_CURR_CNY
     */
    public void setFicCurrCny(BigDecimal ficCurrCny) {
        this.ficCurrCny = ficCurrCny;
    }

    /**
     * 获取 人民币期次类日均
     * @return FIC_NRJ_CNY
     */
    public BigDecimal getFicNrjCny() {
        return ficNrjCny;
    }

    /**
     * 设置 人民币期次类日均
     * @parameter FIC_NRJ_CNY
     */
    public void setFicNrjCny(BigDecimal ficNrjCny) {
        this.ficNrjCny = ficNrjCny;
    }

    /**
     * 获取 外币期次类时点
     * @return FIC_CURR_WB
     */
    public BigDecimal getFicCurrWb() {
        return ficCurrWb;
    }

    /**
     * 设置 外币期次类时点
     * @parameter FIC_CURR_WB
     */
    public void setFicCurrWb(BigDecimal ficCurrWb) {
        this.ficCurrWb = ficCurrWb;
    }

    /**
     * 获取 外币期次类日均
     * @return FIC_NRJ_WB
     */
    public BigDecimal getFicNrjWb() {
        return ficNrjWb;
    }

    /**
     * 设置 外币期次类日均
     * @parameter FIC_NRJ_WB
     */
    public void setFicNrjWb(BigDecimal ficNrjWb) {
        this.ficNrjWb = ficNrjWb;
    }

    /**
     * 获取 人民币日积月累时点
     * @return RJYL_CURR_CNY
     */
    public BigDecimal getRjylCurrCny() {
        return rjylCurrCny;
    }

    /**
     * 设置 人民币日积月累时点
     * @parameter RJYL_CURR_CNY
     */
    public void setRjylCurrCny(BigDecimal rjylCurrCny) {
        this.rjylCurrCny = rjylCurrCny;
    }

    /**
     * 获取 人民币日积月累日均
     * @return RJYX_NRJ_CNY
     */
    public BigDecimal getRjyxNrjCny() {
        return rjyxNrjCny;
    }

    /**
     * 设置 人民币日积月累日均
     * @parameter RJYX_NRJ_CNY
     */
    public void setRjyxNrjCny(BigDecimal rjyxNrjCny) {
        this.rjyxNrjCny = rjyxNrjCny;
    }

    /**
     * 获取 外币日积月累时点
     * @return RJYL_CURR_WB
     */
    public BigDecimal getRjylCurrWb() {
        return rjylCurrWb;
    }

    /**
     * 设置 外币日积月累时点
     * @parameter RJYL_CURR_WB
     */
    public void setRjylCurrWb(BigDecimal rjylCurrWb) {
        this.rjylCurrWb = rjylCurrWb;
    }

    /**
     * 获取 外币日积月累日均
     * @return RJYX_NRJ_WB
     */
    public BigDecimal getRjyxNrjWb() {
        return rjyxNrjWb;
    }

    /**
     * 设置 外币日积月累日均
     * @parameter RJYX_NRJ_WB
     */
    public void setRjyxNrjWb(BigDecimal rjyxNrjWb) {
        this.rjyxNrjWb = rjyxNrjWb;
    }

    /**
     * 获取 表外理财总时点余额
     * @return BW_FIC_CURR
     */
    public BigDecimal getBwFicCurr() {
        return bwFicCurr;
    }

    /**
     * 设置 表外理财总时点余额
     * @parameter BW_FIC_CURR
     */
    public void setBwFicCurr(BigDecimal bwFicCurr) {
        this.bwFicCurr = bwFicCurr;
    }

    /**
     * 获取 表外理财总年日均
     * @return BW_FIC_NRJ
     */
    public BigDecimal getBwFicNrj() {
        return bwFicNrj;
    }

    /**
     * 设置 表外理财总年日均
     * @parameter BW_FIC_NRJ
     */
    public void setBwFicNrj(BigDecimal bwFicNrj) {
        this.bwFicNrj = bwFicNrj;
    }

    /**
     * 获取 表外理财总收益
     * @return BW_FIC_PROFIT
     */
    public BigDecimal getBwFicProfit() {
        return bwFicProfit;
    }

    /**
     * 设置 表外理财总收益
     * @parameter BW_FIC_PROFIT
     */
    public void setBwFicProfit(BigDecimal bwFicProfit) {
        this.bwFicProfit = bwFicProfit;
    }

    /**
     * 获取 我行承销直融产品种类
     * @return ZR_TYPE
     */
    public String getZrType() {
        return zrType;
    }

    /**
     * 设置 我行承销直融产品种类
     * @parameter ZR_TYPE
     */
    public void setZrType(String zrType) {
        this.zrType = zrType;
    }

    /**
     * 获取 我行承销直融产品金额
     * @return ZT_AMT
     */
    public BigDecimal getZtAmt() {
        return ztAmt;
    }

    /**
     * 设置 我行承销直融产品金额
     * @parameter ZT_AMT
     */
    public void setZtAmt(BigDecimal ztAmt) {
        this.ztAmt = ztAmt;
    }

    /**
     * 获取 我行承销直融产品份额
     * @return ZR_FE
     */
    public BigDecimal getZrFe() {
        return zrFe;
    }

    /**
     * 设置 我行承销直融产品份额
     * @parameter ZR_FE
     */
    public void setZrFe(BigDecimal zrFe) {
        this.zrFe = zrFe;
    }

    /**
     * 获取 我行承销直融产品余额
     * @return ZR_CURR
     */
    public BigDecimal getZrCurr() {
        return zrCurr;
    }

    /**
     * 设置 我行承销直融产品余额
     * @parameter ZR_CURR
     */
    public void setZrCurr(BigDecimal zrCurr) {
        this.zrCurr = zrCurr;
    }

    /**
     * 获取 客户投资额度总额
     * @return INVEST_LINES_TOT
     */
    public BigDecimal getInvestLinesTot() {
        return investLinesTot;
    }

    /**
     * 设置 客户投资额度总额
     * @parameter INVEST_LINES_TOT
     */
    public void setInvestLinesTot(BigDecimal investLinesTot) {
        this.investLinesTot = investLinesTot;
    }

    /**
     * 获取 客户投资额度剩余金额
     * @return INVEST_LINES_REMAIN
     */
    public BigDecimal getInvestLinesRemain() {
        return investLinesRemain;
    }

    /**
     * 设置 客户投资额度剩余金额
     * @parameter INVEST_LINES_REMAIN
     */
    public void setInvestLinesRemain(BigDecimal investLinesRemain) {
        this.investLinesRemain = investLinesRemain;
    }

    /**
     * 获取 客户投资额度余额
     * @return INVEST_LINES_BAL
     */
    public BigDecimal getInvestLinesBal() {
        return investLinesBal;
    }

    /**
     * 设置 客户投资额度余额
     * @parameter INVEST_LINES_BAL
     */
    public void setInvestLinesBal(BigDecimal investLinesBal) {
        this.investLinesBal = investLinesBal;
    }

    /**
     * 获取 客户标类理财额度总额
     * @return BL_FICLINES_TOT
     */
    public BigDecimal getBlFiclinesTot() {
        return blFiclinesTot;
    }

    /**
     * 设置 客户标类理财额度总额
     * @parameter BL_FICLINES_TOT
     */
    public void setBlFiclinesTot(BigDecimal blFiclinesTot) {
        this.blFiclinesTot = blFiclinesTot;
    }

    /**
     * 获取 客户标类理财额度剩余金额
     * @return BL_FICLINES_REMAIN
     */
    public BigDecimal getBlFiclinesRemain() {
        return blFiclinesRemain;
    }

    /**
     * 设置 客户标类理财额度剩余金额
     * @parameter BL_FICLINES_REMAIN
     */
    public void setBlFiclinesRemain(BigDecimal blFiclinesRemain) {
        this.blFiclinesRemain = blFiclinesRemain;
    }

    /**
     * 获取 客户标类理财额度余额
     * @return BL_FICLINES_BAL
     */
    public BigDecimal getBlFiclinesBal() {
        return blFiclinesBal;
    }

    /**
     * 设置 客户标类理财额度余额
     * @parameter BL_FICLINES_BAL
     */
    public void setBlFiclinesBal(BigDecimal blFiclinesBal) {
        this.blFiclinesBal = blFiclinesBal;
    }

    /**
     * 获取 客户存续期限额
     * @return CUST_CXQXE
     */
    public BigDecimal getCustCxqxe() {
        return custCxqxe;
    }

    /**
     * 设置 客户存续期限额
     * @parameter CUST_CXQXE
     */
    public void setCustCxqxe(BigDecimal custCxqxe) {
        this.custCxqxe = custCxqxe;
    }

    /**
     * 获取 客户存续期限额剩余金额
     * @return CUST_CXQXE_REMAIN
     */
    public BigDecimal getCustCxqxeRemain() {
        return custCxqxeRemain;
    }

    /**
     * 设置 客户存续期限额剩余金额
     * @parameter CUST_CXQXE_REMAIN
     */
    public void setCustCxqxeRemain(BigDecimal custCxqxeRemain) {
        this.custCxqxeRemain = custCxqxeRemain;
    }

    /**
     * 获取 客户存续期限额余额
     * @return CUST_CXQXE_BAL
     */
    public BigDecimal getCustCxqxeBal() {
        return custCxqxeBal;
    }

    /**
     * 设置 客户存续期限额余额
     * @parameter CUST_CXQXE_BAL
     */
    public void setCustCxqxeBal(BigDecimal custCxqxeBal) {
        this.custCxqxeBal = custCxqxeBal;
    }

    /**
     * 获取 是否通过非标理财融资
     * @return IS_FB_FIC
     */
    public String getIsFbFic() {
        return isFbFic;
    }

    /**
     * 设置 是否通过非标理财融资
     * @parameter IS_FB_FIC
     */
    public void setIsFbFic(String isFbFic) {
        this.isFbFic = isFbFic;
    }

    /**
     * 获取 非标理财融资时点余额
     * @return FB_FIC_CURR
     */
    public BigDecimal getFbFicCurr() {
        return fbFicCurr;
    }

    /**
     * 设置 非标理财融资时点余额
     * @parameter FB_FIC_CURR
     */
    public void setFbFicCurr(BigDecimal fbFicCurr) {
        this.fbFicCurr = fbFicCurr;
    }

    /**
     * 获取 上年度即期结售汇交易量
     * @return FXSM_AMT_LY
     */
    public BigDecimal getFxsmAmtLy() {
        return fxsmAmtLy;
    }

    /**
     * 设置 上年度即期结售汇交易量
     * @parameter FXSM_AMT_LY
     */
    public void setFxsmAmtLy(BigDecimal fxsmAmtLy) {
        this.fxsmAmtLy = fxsmAmtLy;
    }

    /**
     * 获取 本年度即期结售汇交易量
     * @return FXSM_AMT
     */
    public BigDecimal getFxsmAmt() {
        return fxsmAmt;
    }

    /**
     * 设置 本年度即期结售汇交易量
     * @parameter FXSM_AMT
     */
    public void setFxsmAmt(BigDecimal fxsmAmt) {
        this.fxsmAmt = fxsmAmt;
    }

    /**
     * 获取 是否签署NAFMII协议
     * @return IS_NAFMII
     */
    public String getIsNafmii() {
        return isNafmii;
    }

    /**
     * 设置 是否签署NAFMII协议
     * @parameter IS_NAFMII
     */
    public void setIsNafmii(String isNafmii) {
        this.isNafmii = isNafmii;
    }

    /**
     * 获取 上年度远期结售汇业务量
     * @return FORWARD_CUSTFX_AMT_LY
     */
    public BigDecimal getForwardCustfxAmtLy() {
        return forwardCustfxAmtLy;
    }

    /**
     * 设置 上年度远期结售汇业务量
     * @parameter FORWARD_CUSTFX_AMT_LY
     */
    public void setForwardCustfxAmtLy(BigDecimal forwardCustfxAmtLy) {
        this.forwardCustfxAmtLy = forwardCustfxAmtLy;
    }

    /**
     * 获取 本年度远期结售汇业务量
     * @return FORWARD_CUSTFX_AMT
     */
    public BigDecimal getForwardCustfxAmt() {
        return forwardCustfxAmt;
    }

    /**
     * 设置 本年度远期结售汇业务量
     * @parameter FORWARD_CUSTFX_AMT
     */
    public void setForwardCustfxAmt(BigDecimal forwardCustfxAmt) {
        this.forwardCustfxAmt = forwardCustfxAmt;
    }

    /**
     * 获取 上年度外汇买卖业务量
     * @return FREX_AMT_LY
     */
    public BigDecimal getFrexAmtLy() {
        return frexAmtLy;
    }

    /**
     * 设置 上年度外汇买卖业务量
     * @parameter FREX_AMT_LY
     */
    public void setFrexAmtLy(BigDecimal frexAmtLy) {
        this.frexAmtLy = frexAmtLy;
    }

    /**
     * 获取 本年度外汇买卖业务量
     * @return FREX_AMT
     */
    public BigDecimal getFrexAmt() {
        return frexAmt;
    }

    /**
     * 设置 本年度外汇买卖业务量
     * @parameter FREX_AMT
     */
    public void setFrexAmt(BigDecimal frexAmt) {
        this.frexAmt = frexAmt;
    }

    /**
     * 获取 是否办理利率掉期
     * @return IS_RATEDQ
     */
    public String getIsRatedq() {
        return isRatedq;
    }

    /**
     * 设置 是否办理利率掉期
     * @parameter IS_RATEDQ
     */
    public void setIsRatedq(String isRatedq) {
        this.isRatedq = isRatedq;
    }

    /**
     * 获取 上年度利率掉期交易量
     * @return RATEDQ_AMT_LY
     */
    public BigDecimal getRatedqAmtLy() {
        return ratedqAmtLy;
    }

    /**
     * 设置 上年度利率掉期交易量
     * @parameter RATEDQ_AMT_LY
     */
    public void setRatedqAmtLy(BigDecimal ratedqAmtLy) {
        this.ratedqAmtLy = ratedqAmtLy;
    }

    /**
     * 获取 本年度利率掉期交易量
     * @return RATEDQ_AMT
     */
    public BigDecimal getRatedqAmt() {
        return ratedqAmt;
    }

    /**
     * 设置 本年度利率掉期交易量
     * @parameter RATEDQ_AMT
     */
    public void setRatedqAmt(BigDecimal ratedqAmt) {
        this.ratedqAmt = ratedqAmt;
    }

    /**
     * 获取 利率掉期余额
     * @return RATEDQ_CURR
     */
    public BigDecimal getRatedqCurr() {
        return ratedqCurr;
    }

    /**
     * 设置 利率掉期余额
     * @parameter RATEDQ_CURR
     */
    public void setRatedqCurr(BigDecimal ratedqCurr) {
        this.ratedqCurr = ratedqCurr;
    }

    /**
     * 获取 是否办理货币掉期
     * @return IS_CURDQ
     */
    public String getIsCurdq() {
        return isCurdq;
    }

    /**
     * 设置 是否办理货币掉期
     * @parameter IS_CURDQ
     */
    public void setIsCurdq(String isCurdq) {
        this.isCurdq = isCurdq;
    }

    /**
     * 获取 上年度货币掉期交易量
     * @return CURDQ_AMT_LY
     */
    public BigDecimal getCurdqAmtLy() {
        return curdqAmtLy;
    }

    /**
     * 设置 上年度货币掉期交易量
     * @parameter CURDQ_AMT_LY
     */
    public void setCurdqAmtLy(BigDecimal curdqAmtLy) {
        this.curdqAmtLy = curdqAmtLy;
    }

    /**
     * 获取 本年度货币掉期交易量
     * @return CURDQ_AMT
     */
    public BigDecimal getCurdqAmt() {
        return curdqAmt;
    }

    /**
     * 设置 本年度货币掉期交易量
     * @parameter CURDQ_AMT
     */
    public void setCurdqAmt(BigDecimal curdqAmt) {
        this.curdqAmt = curdqAmt;
    }

    /**
     * 获取 货币掉期余额
     * @return CURDQ_CURR
     */
    public BigDecimal getCurdqCurr() {
        return curdqCurr;
    }

    /**
     * 设置 货币掉期余额
     * @parameter CURDQ_CURR
     */
    public void setCurdqCurr(BigDecimal curdqCurr) {
        this.curdqCurr = curdqCurr;
    }

    /**
     * 获取 是否办理贵金属租赁
     * @return IS_GOLDLEASE
     */
    public String getIsGoldlease() {
        return isGoldlease;
    }

    /**
     * 设置 是否办理贵金属租赁
     * @parameter IS_GOLDLEASE
     */
    public void setIsGoldlease(String isGoldlease) {
        this.isGoldlease = isGoldlease;
    }

    /**
     * 获取 贵金属租赁业务余额
     * @return GOLDLEASE_CURR
     */
    public BigDecimal getGoldleaseCurr() {
        return goldleaseCurr;
    }

    /**
     * 设置 贵金属租赁业务余额
     * @parameter GOLDLEASE_CURR
     */
    public void setGoldleaseCurr(BigDecimal goldleaseCurr) {
        this.goldleaseCurr = goldleaseCurr;
    }

    /**
     * 获取 是否叙做贵金属远期
     * @return IS_XZ_GOLDFX
     */
    public String getIsXzGoldfx() {
        return isXzGoldfx;
    }

    /**
     * 设置 是否叙做贵金属远期
     * @parameter IS_XZ_GOLDFX
     */
    public void setIsXzGoldfx(String isXzGoldfx) {
        this.isXzGoldfx = isXzGoldfx;
    }

    /**
     * 获取 是否签约柜台债券
     * @return IS_GTZQ
     */
    public String getIsGtzq() {
        return isGtzq;
    }

    /**
     * 设置 是否签约柜台债券
     * @parameter IS_GTZQ
     */
    public void setIsGtzq(String isGtzq) {
        this.isGtzq = isGtzq;
    }

    /**
     * 获取 是否为银行间债券会员
     * @return IS_BANK_ZQMEM
     */
    public String getIsBankZqmem() {
        return isBankZqmem;
    }

    /**
     * 设置 是否为银行间债券会员
     * @parameter IS_BANK_ZQMEM
     */
    public void setIsBankZqmem(String isBankZqmem) {
        this.isBankZqmem = isBankZqmem;
    }

    /**
     * 获取 柜台债券上年交易量
     * @return GTZQ_AMT_LY
     */
    public BigDecimal getGtzqAmtLy() {
        return gtzqAmtLy;
    }

    /**
     * 设置 柜台债券上年交易量
     * @parameter GTZQ_AMT_LY
     */
    public void setGtzqAmtLy(BigDecimal gtzqAmtLy) {
        this.gtzqAmtLy = gtzqAmtLy;
    }

    /**
     * 获取 柜台债券本年交易量
     * @return GTZQ_AMT
     */
    public BigDecimal getGtzqAmt() {
        return gtzqAmt;
    }

    /**
     * 设置 柜台债券本年交易量
     * @parameter GTZQ_AMT
     */
    public void setGtzqAmt(BigDecimal gtzqAmt) {
        this.gtzqAmt = gtzqAmt;
    }

    /**
     * 获取 是否与我行开展同存业务
     * @return IS_TC
     */
    public String getIsTc() {
        return isTc;
    }

    /**
     * 设置 是否与我行开展同存业务
     * @parameter IS_TC
     */
    public void setIsTc(String isTc) {
        this.isTc = isTc;
    }

    /**
     * 获取 是否为金交所会员
     * @return IS_JJS_MEM
     */
    public String getIsJjsMem() {
        return isJjsMem;
    }

    /**
     * 设置 是否为金交所会员
     * @parameter IS_JJS_MEM
     */
    public void setIsJjsMem(String isJjsMem) {
        this.isJjsMem = isJjsMem;
    }

    /**
     * 获取 是否有大宗商品业务（含大豆、原油等）
     * @return IS_DZSP
     */
    public String getIsDzsp() {
        return isDzsp;
    }

    /**
     * 设置 是否有大宗商品业务（含大豆、原油等）
     * @parameter IS_DZSP
     */
    public void setIsDzsp(String isDzsp) {
        this.isDzsp = isDzsp;
    }

    /**
     * 获取 是否代发薪
     * @return IS_DFX
     */
    public String getIsDfx() {
        return isDfx;
    }

    /**
     * 设置 是否代发薪
     * @parameter IS_DFX
     */
    public void setIsDfx(String isDfx) {
        this.isDfx = isDfx;
    }

    /**
     * 获取 是否购买过中银保险
     * @return IS_BOC_INSU
     */
    public String getIsBocInsu() {
        return isBocInsu;
    }

    /**
     * 设置 是否购买过中银保险
     * @parameter IS_BOC_INSU
     */
    public void setIsBocInsu(String isBocInsu) {
        this.isBocInsu = isBocInsu;
    }

    /**
     * 获取 上年度中银保险总金额
     * @return BOC_INSU_AMT_LY
     */
    public BigDecimal getBocInsuAmtLy() {
        return bocInsuAmtLy;
    }

    /**
     * 设置 上年度中银保险总金额
     * @parameter BOC_INSU_AMT_LY
     */
    public void setBocInsuAmtLy(BigDecimal bocInsuAmtLy) {
        this.bocInsuAmtLy = bocInsuAmtLy;
    }

    /**
     * 获取 本年度中银保险总金额
     * @return BOC_INSU_AMT
     */
    public BigDecimal getBocInsuAmt() {
        return bocInsuAmt;
    }

    /**
     * 设置 本年度中银保险总金额
     * @parameter BOC_INSU_AMT
     */
    public void setBocInsuAmt(BigDecimal bocInsuAmt) {
        this.bocInsuAmt = bocInsuAmt;
    }

    /**
     * 获取 投保保险险种
     * @return INSU_TYPE
     */
    public String getInsuType() {
        return insuType;
    }

    /**
     * 设置 投保保险险种
     * @parameter INSU_TYPE
     */
    public void setInsuType(String insuType) {
        this.insuType = insuType;
    }

    /**
     * 获取 存款利息收入
     * @return CK_INT_INC
     */
    public BigDecimal getCkIntInc() {
        return ckIntInc;
    }

    /**
     * 设置 存款利息收入
     * @parameter CK_INT_INC
     */
    public void setCkIntInc(BigDecimal ckIntInc) {
        this.ckIntInc = ckIntInc;
    }

    /**
     * 获取 贷款利息收入
     * @return LOAN_INT_INC
     */
    public BigDecimal getLoanIntInc() {
        return loanIntInc;
    }

    /**
     * 设置 贷款利息收入
     * @parameter LOAN_INT_INC
     */
    public void setLoanIntInc(BigDecimal loanIntInc) {
        this.loanIntInc = loanIntInc;
    }

    /**
     * 获取 净利息收入
     * @return INT_INC
     */
    public BigDecimal getIntInc() {
        return intInc;
    }

    /**
     * 设置 净利息收入
     * @parameter INT_INC
     */
    public void setIntInc(BigDecimal intInc) {
        this.intInc = intInc;
    }

    /**
     * 获取 营业收入贡献
     * @return OPERATING_INC_CONTRIB
     */
    public BigDecimal getOperatingIncContrib() {
        return operatingIncContrib;
    }

    /**
     * 设置 营业收入贡献
     * @parameter OPERATING_INC_CONTRIB
     */
    public void setOperatingIncContrib(BigDecimal operatingIncContrib) {
        this.operatingIncContrib = operatingIncContrib;
    }

    /**
     * 获取 税费前利润贡献
     * @return ACCOUNT_CONTRIB_BEFORE_TAX
     */
    public BigDecimal getAccountContribBeforeTax() {
        return accountContribBeforeTax;
    }

    /**
     * 设置 税费前利润贡献
     * @parameter ACCOUNT_CONTRIB_BEFORE_TAX
     */
    public void setAccountContribBeforeTax(BigDecimal accountContribBeforeTax) {
        this.accountContribBeforeTax = accountContribBeforeTax;
    }

    /**
     * 获取 各核算码项下中收贡献
     * @return MIS_CONTRIB
     */
    public BigDecimal getMisContrib() {
        return misContrib;
    }

    /**
     * 设置 各核算码项下中收贡献
     * @parameter MIS_CONTRIB
     */
    public void setMisContrib(BigDecimal misContrib) {
        this.misContrib = misContrib;
    }
}