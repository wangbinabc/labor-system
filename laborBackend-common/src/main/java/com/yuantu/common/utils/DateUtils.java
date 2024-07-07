package com.yuantu.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.yuantu.common.enums.EmpGenderEnum;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endDate   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    public static int calculateAge(String idCard) {
        // 身份证中的年份
        int birthYear = Integer.parseInt(idCard.substring(6, 10));
        // 身份证中的月份
        int birthMonth = Integer.parseInt(idCard.substring(10, 12));
        // 身份证中的日期
        int birthDay = Integer.parseInt(idCard.substring(12, 14));

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 获取出生日期
        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);

        // 计算年龄差距
        Period period = Period.between(birthDate, currentDate);
        int age = period.getYears();

        return age;
    }

    public static Date calculateExpireTime(String idCard, String gender, String manRetireAge, String womanRetireAge) {
        // 身份证中的年份
        int birthYear = Integer.parseInt(idCard.substring(6, 10));
        // 身份证中的月份
        int birthMonth = Integer.parseInt(idCard.substring(10, 12));
        // 身份证中的日期
        int birthDay = Integer.parseInt(idCard.substring(12, 14));
        // 获取出生日期
        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        // 计算年龄并添加到出生日期
        LocalDate result;
        if (EmpGenderEnum.MALE.getKey().equals(gender)) {
            result = birthDate.plusYears(Integer.parseInt(manRetireAge));
        } else if (EmpGenderEnum.FEMALE.getKey().equals(gender)) {
            result = birthDate.plusYears(Integer.parseInt(womanRetireAge));
        } else {
            result = birthDate.plusYears(Integer.parseInt(manRetireAge));
        }
        // 将 LocalDate 转换为 Date
        return Date.from(result.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    public static long calculateDays(Date begin, Date end) {

        // 将 java.util.Date 转换为 java.time.LocalDate
        // 计算相差的天数
        long diffInMillies = end.getTime() - begin.getTime();
        // 计算相差的天数
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = format.parse("2023-09-16 02:15:30");
        Date date2 = format.parse("2023-09-16 03:45:21");
        long l = calculateDays(date2, date1);
        System.out.println(l);

    }


}
