package com.hdmap.data.ibd.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2023/8/29 10:48
 * @description: id工具
 */
@Slf4j
@Component
public class IdUtil {

    private static String dataPattern = "yyyyMMddHHmmssSSS";
    private static List<String> dateBaseLine = new ArrayList<String>(){{
        add("20230601000000000");
        add("20230301000000000");
    }};

    private IdUtil() {
    }

    /**
     * 长id转换短id
     */
    public static String longIdToShortId(String id) throws ParseException {
        // 分割
        String[] ids = id.split("\\|");
        List<String> transIds = new ArrayList<>();
        for (String id1 : ids) {
            if (StringUtils.isBlank(id1)) {
                continue;
            }
            // 转换后id
            log.info("long id:{}", id1);
            String transId = transId(id1);
            log.info("short id:{}", transId);
            transIds.add(transId);
        }
        // List -> String
        return String.join("|", transIds);
    }

    /**
     * 短id转长id
     */
    public static String shortIdToLongId(String id) throws ParseException {
        // 分割
        String[] ids = id.split("\\|");
        List<String> transIds = new ArrayList<>();
        for (String id1 : ids) {
            // 截取0到后2位，时间戳
            long idTimestamp = Long.parseLong(id1.substring(0, id1.length() - 2));
            // 截取后2位，随机位
            String random = id1.substring(id1.length() - 2);
            // 根据当前日期和dateBaseLine.get(0)计算计算次数
            int count = calcDateNum();
            for (int i = 0; i < count; i++) {
                // 偏移的日期
                Date dateOffset = addMonth(getDate(dateBaseLine.get(0)), i * 3);
                // id对应的日期
                String dateStr = DateFormatUtils.format(idTimestamp + dateOffset.getTime(), dataPattern);
                // 转换后id
                String transId = dateStr + random;
                // 在头部添加
                transIds.add(0, transId);
            }
            // 计算dateBaseLine.get(1)对应的id
            Date dateOffset = getDate(dateBaseLine.get(1));
            // id对应的日期
            String dateStr = DateFormatUtils.format(idTimestamp + dateOffset.getTime(), dataPattern);
            // 转换后id
            String transId = dateStr + random;
            // 在尾部添加
            transIds.add(transId);
        }
        // List -> String
        return String.join(" | ", transIds);
    }

    /**
     * 转换id
     */
    private static String transId(String id) throws ParseException {
        // 截取0到后2位，日期
        String dataStr = id.substring(0, id.length() - 2);
        // 截取后2位，随机位
        String random = id.substring(id.length() - 2);
        // 时间字符串转时间戳
        Date date = DateUtils.parseDate(dataStr, dataPattern);
        // 偏移日期 - 默认使用第一个偏移日期
        Date dateOffset = getDate(dateBaseLine.get(0));
        long diff = date.getTime() - dateOffset.getTime();
        if (diff < 0) {
            // 偏移日期 - 使用第二个偏移日期
            dateOffset = getDate(dateBaseLine.get(1));
            diff = date.getTime() - dateOffset.getTime();
        }
        // 判断diff位数超过10位时 - 使用第三个偏移日期：第一个偏移日期增加3个月
        if (diff > 9999999999L) {
            diff = switchTimeBaseLine(diff, date, dateOffset, dateBaseLine.get(0));
        }
        return diff + random;
    }

    /**
     * 获取偏移日期
     */
    private static Date getDate(String dataStr) throws ParseException {
        return DateUtils.parseDate(dataStr, dataPattern);
    }

    /**
     * 递归的切换偏移日期
     */
    private static long switchTimeBaseLine(long diff, Date date, Date dateOffset, String dataStr) throws ParseException {
        dateOffset = getDate(dataStr);
        // 日期上增加3个月
        dateOffset = addMonth(dateOffset, 3);
        diff = date.getTime() - dateOffset.getTime();
        // 超过10位数，切换基准，日期上增加3个月
        if (diff > 9999999999L) {
            return switchTimeBaseLine(diff, date, dateOffset, dateOffset.toString());
        }
        return diff;
    }

    private static Date addMonth(Date date, int amount) {
        // 日期上增加amount个月
        return DateUtils.addMonths(date, amount);
    }

    // 根据当前日期计算迭代次数
    private static int calcDateNum() throws ParseException {
        // dateBaseLine.get(0)转换为日期
        Date date = getDate(dateBaseLine.get(0));
        // 当前日期与dateBaseLine.get(0)的日期相差的月份数
        long monthNum = monthDiff(new Date(), date);
        // 迭代次数
        return (int) (monthNum / 3) + 1;
    }

    private static long monthDiff(Date date1, Date date2) {
        // Date转换为Temporal
        Temporal temporal1 = OffsetDateTime.ofInstant(date1.toInstant(), ZoneId.systemDefault());
        Temporal temporal2 = OffsetDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault());
        // 方法返回为相差月份
        return ChronoUnit.MONTHS.between(temporal2, temporal1);
    }


}
