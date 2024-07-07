
package com.yuantu.labor.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 员工对象 employee
 *
 * @author ahadon
 * @date 2023-09-06
 */

@Data
public class EmpImportVO implements IExcelDataModel, IExcelModel {


    @Excel(name = "序号")
    private String orderNum;

    /**
     * 员工姓名 q
     */
    @Excel(name = "员工姓名")
    @NotBlank(message = "不能为空")
    private String empName;


    /**
     * 员工状态 q
     * ：1在职，2
     * 新入职，3
     * 辞职，4
     * 辞退，5
     * 即将到龄
     * ，6到龄
     * ，7返聘
     */
    @Excel(name = "员工状态")
    // @NotBlank(message = "不能为空")
    private String empStatus;


    /**
     * 备注
     */
    @Excel(name = "备注")
    @Size(message = "字符长度不能超过200", max = 200)
    private String empRemark;
    /**
     *  q
     */
    @Excel(name = "性别")
    private String empGender;
    /**
     *  q
     */
//    @Excel(name = "年龄")
//    private Integer empAge;

    /**
     *  q
     */
    @Excel(name = "民族")
    // @NotBlank(message = "不能为空")
    private String nation;
    /**
     * 身份证 q
     */
    @Excel(name = "身份证号")
    @Pattern(regexp = "\\d{17}[0-9Xx]", message = "格式不正确")
    //@NotBlank(message = "不能为空")
    private String empIdcard;

    /**
     *  q
     */
    @Excel(name = "出生日期")
    @Past(message = "出生日期不能晚于当前日期")
    private Date birthDate;
    /**
     *  q
     */
    @Excel(name = "预计退休日期")
    private Date empExpireTime;
    /**
     *  q
     */
    @Excel(name = "用工年龄上限日期")
    private Date hireLimitDate;

    @Excel(name = "协同编码")
    private String empCode;

    @Excel(name = "用工公司")
    // @NotBlank(message = "不能为空")
    private String empEmployingUnits;


    @Excel(name = "用工部门")
    private String empDeptName;


    @Excel(name = "部门信息2")
    @Size(message = "字符长度不能超过60", max = 60)
    private String deptInfoTwo;


    @Excel(name = "部门信息3")
    @Size(message = "字符长度不能超过60", max = 60)
    private String deptInfoThree;


    /**
     * 人力资源公司 q
     */
    @Excel(name = "劳务公司")
    @Size(message = "字符长度不能超过60", max = 60)
    //@NotBlank(message = "不能为空")
    private String empHrCompany;

    /**
     *  q
     */
    @Excel(name = "岗位类别")
   // @NotBlank(message = "不能为空")
    private String empPositionCategory;


    @Excel(name = "任职时间")
    private Date onJobTime;

    /**
     * 岗位 q
     */
    @Excel(name = "岗位名称")
  //  @NotBlank(message = "不能为空")
    private String empPosition;

    /**
     * 岗级 q
     */
    @Excel(name = "岗级")
   // @NotBlank(message = "不能为空")
    private String empPositionLevel;

    /**
     * 薪级 q
     */
    @Excel(name = "薪级")
  //  @NotBlank(message = "不能为空")
    private String empSalaryLevel;

    @Excel(name = "薪级下限")
    private String salaryLevelMin;

    @Excel(name = "薪级上限")
    private String salaryLevelMax;

    /**
     * 政治面貌 q
     */
    @Excel(name = "政治面貌")
    // @NotBlank(message = "不能为空")
    private String empPoliticalStatus;

    @Excel(name = "籍贯")
    private String nativePlace;

    @Excel(name = "出生地")
    private String birthPlace;

    @Excel(name = "户籍所在地地址")
    private String domicilePlace;
    /**
     * 家庭地址
     */
    @Excel(name = "现居住地地址")
    private String empHomeAddress;


    @Excel(name = "学校")
    private String school;

    /**
     * 学历 q
     */
    @Excel(name = "学历")
    private String empEducation;

    @Excel(name = "最高学历")
    // @NotBlank(message = "不能为空")
    private String highestEducation;

    /**
     * 专业 q
     */
    @Excel(name = "所学专业")
    // @NotBlank(message = "不能为空")
    private String empSpeciality;


    @Excel(name = "毕业时间")
    private Date graduateTime;

    /**
     * 职称
     */
    @Excel(name = "职称")
    private String empTitle;


    @Excel(name = "取得时间")
    private Date acquireTime;


    @Excel(name = "参加工作时间")
    private Date attendTime;


    @Excel(name = "断供月数")
    private Integer refuseMonth;

    @Excel(name = "进本企业前累计工作时间(月数)")
    private Integer accumulativeMonth;

    /**
     *  q
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "进本企业时间")
    @Past(message = "进入企业时间不能晚于当前日期")
    // @NotNull(message = "不能为空")
    private Date empHiredate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合同起始")
    @Past(message = "进入合同起始时间不能晚于当前日期")
    private Date contractStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合同结束")
    private Date contractEndTime;

    @Excel(name = "工作电话")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "格式不正确")
    private String workPhone;


    /**
     * 联系电话
     */
   // @NotBlank(message = "不能为空")
    @Excel(name = "联系方式")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "格式不正确")
    private String empTelephone;


    /**
     * 人员类别
     */
    //@Excel(name = "人员类别")
    // @NotBlank(message = "不能为空")
    // private String empCategory;


    /**
     * 推荐人
     */
    @Excel(name = "推荐人")
    @Size(message = "字符长度不能超过10", max = 10)
    private String empReference;


    @Excel(name = "紧急联系人1-与本人关系")
    private String oneFamAppellation;

    @Excel(name = "紧急联系人1-联系方式")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "格式不正确")
    private String oneFamPhone;

    // @Excel(name = "紧急联系人1员工亲属年龄")
    //private String oneFamAge;

    // @Excel(name = "紧急联系人1员工亲属工作单位及岗位")
    //private String oneFamUnitPost;

    @Excel(name = "紧急联系人2-与本人关系")
    private String twoFamAppellation;

    @Excel(name = "紧急联系人2-联系方式")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "格式不正确")
    private String twoFamPhone;

    // @Excel(name = "紧急联系人2员工亲属年龄")
    //private String twoFamAge;

    // @Excel(name = "紧急联系人2员工亲属工作单位及岗位")
    //private String twoFamUnitPost;


    /**
     * 行号
     */
    private int rowNum;

    /**
     * 错误消息
     */
    private String errorMsg;

    @Override
    public Integer getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmpImportVO that = (EmpImportVO) o;
        return Objects.equals(empIdcard, that.empIdcard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empIdcard);
    }

}
