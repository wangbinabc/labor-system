
package com.yuantu.labor.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.yuantu.common.utils.StringUtils;
import lombok.Data;

import java.util.Date;


/**
 * 员工对象 employee
 *
 * @author ahadon
 * @date 2023-09-06
 */

@Data
public class EmpExportTemplateVO {


    public static final String bigTitle = "填写须知： \n" +
            "1.请勿修改当前模板结构\n" +
            "2.红色字段必填，黑色字段按照实际情况选填\n" +
            "3.日期格式统一按照“2023/9/1”格式填写\n" +
            "4.籍贯、出生地按照“省名/市名/区名”格式填写\n" +
            "5.表头下方为示例数据，上传前请删除";


    @ExcelProperty(value = {bigTitle, "序号"})
    @HeadFontStyle(color = 8)
    private String orderNum;

    /**
     * 员工姓名
     */
    @ExcelProperty(value = {bigTitle, "员工姓名"})
    @HeadFontStyle(color = 10)
    private String empName;


    /**
     * 员工状态
     * ：1在职，2
     * 新入职，3
     * 辞职，4
     * 辞退，5
     * 即将到龄
     * ，6到龄
     * ，7返聘
     */
    @ExcelProperty(value = {bigTitle, "员工状态"})
    @HeadFontStyle(color = 8)
    private String empStatus;

    /**
     * 备注
     */
    @ExcelProperty(value = {bigTitle, "备注"})
    @HeadFontStyle(color = 8)
    private String empRemark;


    @ExcelProperty(value = {bigTitle, "性别"})
    @HeadFontStyle(color = 8)
    private String empGender;

//    @ExcelProperty(value = {bigTitle, "年龄"})
//    @HeadFontStyle(color = 8)
//    private Integer empAge;


    @ExcelProperty(value = {bigTitle, "民族"})
    @HeadFontStyle(color = 8)
    private String nation;


    /**
     * 身份证
     */
    @ExcelProperty(value = {bigTitle, "身份证号"})
    @HeadFontStyle(color = 8)
    private String empIdcard;

    @ExcelProperty(value = {bigTitle, "出生日期"})
    @HeadFontStyle(color = 8)
    private String birthDate;


    @ExcelProperty(value = {bigTitle, "预计退休日期"})
    @HeadFontStyle(color = 8)
    private String empExpireTime;

    @ExcelProperty(value = {bigTitle, "用工年龄上限日期"})
    @HeadFontStyle(color = 8)
    private String hireLimitDate;

    @HeadFontStyle(color = 8)
    @ExcelProperty(value = {bigTitle, "协同编码"})
    private String empCode;

    @ExcelProperty(value = {bigTitle, "用工公司"})
    private String empEmployingUnits;


    @ExcelProperty(value = {bigTitle, "用工部门"})
    private String empDeptName;


    @ExcelProperty(value = {bigTitle, "部门信息2"})
    private String deptInfoTwo;


    @ExcelProperty(value = {bigTitle, "部门信息3"})
    private String deptInfoThree;

    /**
     * 人力资源公司
     */
    @ExcelProperty(value = {bigTitle, "劳务公司"})
    private String empHrCompany;

    /**
     * 人员类别
     */
    //@ExcelProperty(value = {bigTitle, "人员类别"})
    //private String empCategory;


    @ExcelProperty(value = {bigTitle, "岗位类别"})
    private String empPositionCategory;


    @ExcelProperty(value = {bigTitle, "任职时间"})
    private String onJobTime;

    /**
     * 岗位
     */
    @ExcelProperty(value = {bigTitle, "岗位名称"})
    private String empPosition;


    /**
     * 岗级
     */
    @ExcelProperty(value = {bigTitle, "岗级"})
    private String empPositionLevel;


    /**
     * 薪级
     */
    @ExcelProperty(value = {bigTitle, "薪级"})
    private String empSalaryLevel;


    /**
     * 薪级
     */
    @ExcelProperty(value = {bigTitle, "薪级下限"})
    private String salaryLevelMin;


    /**
     * 薪级
     */
    @ExcelProperty(value = {bigTitle, "薪级上限"})
    @HeadFontStyle(color = 8)
    private String salaryLevelMax;


    /**
     * 政治面貌
     */
    @ExcelProperty(value = {bigTitle, "政治面貌"})
    private String empPoliticalStatus;


    @ExcelProperty(value = {bigTitle, "籍贯"})
    private String nativePlace;

    @ExcelProperty(value = {bigTitle, "出生地"})
    private String birthPlace;

    @ExcelProperty(value = {bigTitle, "户籍所在地地址"})
    private String domicilePlace;

    /**
     * 家庭地址
     */
    @ExcelProperty(value = {bigTitle, "现居住地地址"})
    private String empHomeAddress;


    @ExcelProperty(value = {bigTitle, "学校"})
    private String school;


    /**
     * 学历
     */
    @ExcelProperty(value = {bigTitle, "学历"})
    private String empEducation;


    @ExcelProperty(value = {bigTitle, "最高学历"})
    private String highestEducation;

    /**
     * 专业
     */
    @ExcelProperty(value = {bigTitle, "所学专业"})
    private String empSpeciality;


    @ExcelProperty(value = {bigTitle, "毕业时间"})
    private Date graduateTime;

    /**
     * 职称
     */
    @ExcelProperty(value = {bigTitle, "职称"})
    private String empTitle;


    @ExcelProperty(value = {bigTitle, "取得时间"})
    private String acquireTime;


    @ExcelProperty(value = {bigTitle, "参加工作时间"})
    private String attendTime;


    @ExcelProperty(value = {bigTitle, "断供月数"})
    private Integer refuseMonth;


    @ExcelProperty(value = {bigTitle, "进本企业前累计工作时间(月数)"})
    private Integer accumulativeMonth;


    @ExcelProperty(value = {bigTitle, "进本企业时间"})
    private String empHiredate;


    @ExcelProperty(value = {bigTitle, "合同起始"})
    private String contractStartTime;

    @ExcelProperty(value = {bigTitle, "合同结束"})
    private String contractEndTime;

    /**
     * 联系电话
     */
    @ExcelProperty(value = {bigTitle, "联系方式"})
    private String empTelephone;


    @ExcelProperty(value = {bigTitle, "工作电话"})
    @HeadFontStyle(color = 8)
    private String workPhone;

    /**
     * 推荐人
     */
    @ExcelProperty(value = {bigTitle, "推荐人"})
    private String empReference;


    @ExcelProperty(value = {bigTitle, "紧急联系人1-与本人关系"})
    private String oneFamAppellation;

    @ExcelProperty(value = {bigTitle, "紧急联系人1-联系方式"})
    private String oneFamPhone;

    @ExcelProperty(value = {bigTitle, "紧急联系人2-与本人关系"})
    private String twoFamAppellation;

    @ExcelProperty(value = {bigTitle, "紧急联系人2-联系方式"})
    private String twoFamPhone;

    /**
     * 每个模板的首行高度， 换行数目+2 乘以15
     */
    public static int getHeadHeight() {
        return (StringUtils.getRowCounts(bigTitle) + 2) * 15;
    }


}
