package com.yuantu.labor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuantu.common.annotation.Excel;
import com.yuantu.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.Objects;

/**
 * 员工快照对象 emp_history
 * 
 * @author ruoyi
 * @date 2023-09-19
 */

@Data
public class EmpHistoryInfoVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Long historyId;

    /** 变动类型  */
    @Excel(name = "变动类型", readConverterExp = "nochanges=-,add=新增,reduce=减少,mobilize=调动")
    private String changeType;

    /**
     * 快照年月,例如：2012-01
     */
   // @Excel(name = "快照年月")
    private String historyYearMoth;

    /**
     * 员工ID
     */
    //  @Excel(name = "员工ID")
    private Long empId;

    /**
     * 员工工号
     */
    //   @Excel(name = "员工工号")
    private String empCode;

    /**
     * 员工头像信息
     */
    // @Excel(name = "员工头像信息")
    private String empAvatarUrl;

    /**
     * 员工姓名
     */
    @Excel(name = "员工姓名")
    private String empName;

    /**
     * 身份证
     */
    @Excel(name = "身份证")
    private String empIdcard;



    /**
     * 员工年龄
     */
    @Excel(name = "员工年龄")
    private Long empAge;

    /**
     * 员工状态：1在职，2新入职，3辞职，4辞退，5即将到龄，6到龄，7返聘
     */
  //  @Excel(name = "员工状态", readConverterExp = "1=在职,2=新入职,3=辞职,4=辞退,5=即将到龄,6=到龄,7=返聘")
    private String empStatus;

    /**
     * 政治面貌   1中共党员 2中共预备党 3共青团员 4民革党员 5民盟盟员 6无党派人士 7群众
     */
   // @Excel(name = "政治面貌", readConverterExp = "1=中共党员,2=中共预备党员,3=共青团员,4=民革党员,5=民盟盟员,6=无党派人士,7=群众")
    private String empPoliticalStatus;

    /**
     * 性别  0 男  1 女
     */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String empGender;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String empDeptName;
    /**
     * 用工单位
     */
    @Excel(name = "用工单位")
    private String empEmployingUnits;

    /**
     * 人力资源公司
     */
    @Excel(name = "人力资源公司")
    private String empHrCompany;

    /**
     * 岗位
     */
    @Excel(name = "岗位", readConverterExp = "1=炊事员,2=驾驶员,3=车辆管理,4=后勤服务,5=文员,6=造价(技经管理)员,7=招标员,8=监理部资料员,9=监造(抽检)员,10=设计监理员")
    private String empPosition;


    /**
     * 学历   1高中 2中专 3大专 4本科 5硕士 6博士
     */
    @Excel(name = "学历", readConverterExp = "1=高中,2=中专,3=大专,4=本科,5=硕士,6=博士")
    private String empEducation;

    /**
     * 专业
     */
    @Excel(name = "专业")
    private String empSpeciality;

    /**
     * 职称
     */
  //  @Excel(name = "职称", readConverterExp = "1=助理级,2=中级,3=副高级,4=正高级")
    private String empTitle;

    /**
     * 人员类别
     */
  //  @Excel(name = "人员类别", readConverterExp = "1=外包,2=全民,3=借工,4=直签")
    private String empCategory;

    /**
     * 人员类型
     */
   // @Excel(name = "人员类型", readConverterExp = "1=普通员工,2=特级员工")
    private String empType;

    /**
     * 部门ID
     */
    //@Excel(name = "部门ID")
    private Long empDeptId;





    /**
     * 岗级  1岗级A 2岗级B 3岗级C
     */
   // @Excel(name = "岗级", readConverterExp = "1=一级,2=二级,3=三级,4=四级")
    private String empPositionLevel;


    /**
     * 入职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
  //  @Excel(name = "入职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date empHiredate;

    /**
     * 薪级  1薪级A 2薪级B 3薪级C
     */
  //  @Excel(name = "薪级", readConverterExp = "1=薪级A,2=薪级B,3=薪级C")
    private String empSalaryLevel;

    /**
     * 备注
     */
   // @Excel(name = "备注")
    private String empRemark;

    /**
     * 紧急联系人
     */
   // @Excel(name = "紧急联系人")
    private String empEmergencyContact;

    /**
     * 紧急联系人电话
     */
   // @Excel(name = "紧急联系人电话")
    private String empEmergencContactPhone;

    /**
     * 联系电话
     */
  //  @Excel(name = "联系电话")
    private String empTelephone;

    /**
     * 家庭地址
     */
   // @Excel(name = "家庭地址")
    private String empHomeAddress;

    /**
     * 推荐人
     */
   // @Excel(name = "推荐人")
    private String empReference;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
  //  @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date empUpdateTime;

    /**
     * 到龄时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
  //  @Excel(name = "到龄时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date empExpireTime;

//    /**  */
//    private Long historyId;
//
//
//    /** 员工ID */
//    @Excel(name = "员工ID")
//    private Long empId;
//
//    /** 员工工号 */
//    @Excel(name = "员工工号")
//    private String empCode;
//
//
//    /** 身份证 */
//    @Excel(name = "身份证")
//    private String empIdcard;
//
//    /** 员工姓名 */
//    @Excel(name = "员工姓名")
//    private String empName;
//
//    /** 员工年龄 */
//    @Excel(name = "员工年龄")
//    private Long empAge;
//
//    @Excel(name = "学历   1高中 2中专 3大专 4本科 5硕士 6博士")
//    private String empEducation;
//
//
//    /** 性别  0 男  1 女 */
//    @Excel(name = "性别  0 男  1 女")
//    private String empGender;
//
//
//    /** 部门ID */
//    @Excel(name = "部门ID")
//    private Long empDeptId;
//
//    /** 部门名称 */
//    @Excel(name = "部门名称")
//    private String empDeptName;
//
//    /** 用工单位 */
//    @Excel(name = "用工单位")
//    private String empEmployingUnits;
//
//    /** 人力资源公司 */
//    @Excel(name = "人力资源公司")
//    private String empHrCompany;
//
//
//    /** 岗位 */
//    @Excel(name = "岗位")
//    private String empPosition;
//
//    /** 入职时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "入职时间", width = 30, dateFormat = "yyyy-MM-dd")
//    private Date empHiredate;
//


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpHistoryInfoVO that = (EmpHistoryInfoVO) o;
        return Objects.equals(empId, that.empId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId);
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        EmpHistoryInfoVO that = (EmpHistoryInfoVO) o;
//        return Objects.equals(empIdcard, that.empIdcard);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(empIdcard);
//    }
}
