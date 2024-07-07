package com.yuantu.labor.cenum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author : wj
 * @create 2023/3/17 11:18
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum FileTypeEnum {

    PHOTO("1", "照片"),

    ID_CARD("2", "身份证"),

    PAPER("3", "最高职称证书"),

    EDUCATION("4", "最高学历"),

    EMP_INFO("9", "员工信息导入文件"),

    EMP_FAIL_INFO("10", "员工信息导入错误文件"),

    EMP_PAPER_INFO("12", "员工证件信息导入文件"),

    EMP_PAPER_FAIL_INFO("13", "员工证件错误信息导入文件"),

    TRAIN_PROJECT_INFO("14", "培训项目信息导入文件"),

    TRAIN_PROJECT_FAIL_INFO("15", "培训项目信息导入错误文件"),

    EMP_TRAIN_INFO("16", "培训记录信息导入文件"),

    EMP_TRAIN_FAIL_INFO("17", "培训记录信息导入错误文件"),

    TRAIN_RESULT_INFO("18", "培训成果信息导入文件"),

    TRAIN_RESULT_FAIL_INFO("19", "培训成果信息导入错误文件"),


    LOAN_WORKER_INFO("20", "借工信息导入文件"),

    LOAN_WORKER_FAIL_INFO("21", "借工信息导入错误文件"),

    EMP_RESUME_INFO("22", "员工履历信息导入文件"),

    EMP_RESUME_FAIL_INFO("23", "员工履历信息导入错误文件"),

    EMP_EXPERT_INFO("24", "专家信息导入文件"),

    EMP_EXPERT_FAIL_INFO("25", "专家信息导入错误文件"),

    SALARY_HISTORY_INFO("26", "薪酬变动信息导入文件"),

    SALARY_HISTORY_FAIL_INFO("27", "薪酬变动信息导入错误文件"),

    EMP_SALARY_INFO("28", "员工薪酬信息导入文件"),

    EMP_SALARY_FAIL_INFO("29", "员工薪酬信息导入错误文件"),

    EMP_PROFIT_INFO("30", "员工利润信息导入文件"),

    EMP_PROFIT_FAIL_INFO("31", "员工利润信息导入错误文件"),

    EMP_HIS_INFO("32", "员工历史信息导入文件"),

    EMP_HIS_FAIL_INFO("33", "员工历史信息导入错误文件"),

    EMP_RECORD("34", "员工档案"),

    EMP_OTHER("35", "员工其他"),

    EMP_ATTENDANCE_INFO("36", "考勤信息导入文件"),

    EMP_ATTENDANCE_FAIL_INFO("37", "考勤信息导入错误文件"),

    EMP_PERFORM_INFO("38", "绩效信息导入文件"),

    EMP_PERFORM_FAIL_INFO("39", "绩效信息导入错误文件"),

    EMP_WELFARE_INFO("40", "福利信息导入文件"),

    EMP_WELFARE_FAIL_INFO("41", "福利信息导入错误文件"),

    EMP_HEALTH_INFO("42", "健康信息导入文件"),

    EMP_HEALTH_FAIL_INFO("43", "健康信息导入错误文件"),

    EMP_POST_INFO("44", "职位信息导入文件"),

    EMP_POST_FAIL_INFO("45", "职位信息导入错误文件"),

    EMP_PROVINCE_PREFORM_INFO("46", "省公司绩效信息导入文件"),

    EMP_PROVINCE_PERFORM_FAIL_INFO("47", "省公司绩效信息导入错误文件"),

    ;


    private String key;

    private String value;
}
