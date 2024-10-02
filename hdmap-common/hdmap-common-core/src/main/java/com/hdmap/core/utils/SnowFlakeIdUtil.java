package com.hdmap.core.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * @author admin
 * @date 2024/6/21 15:12
 * @version 1.0
 * @description: 雪花算法生成ID
 * 同台机器部署时，可以在jar的启动配置中设置 -Dhostname=xxx -Dunique_identifier=xxx
 * 来设置唯一标识
 */
@Slf4j
public class SnowFlakeIdUtil {

    private long machineId ;
    private long dataCenterId ;
    private Snowflake snowflake;


    public SnowFlakeIdUtil(long machineId, long dataCenterId) {
        this.machineId = machineId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 成员类，SnowFlakeUtil的实例对象的保存域
     */
    private static class IdGenHolder {
        private static final SnowFlakeIdUtil instance = new SnowFlakeIdUtil();
    }

    /**
     * 外部调用获取SnowFlakeUtil的实例对象，确保不可变
     */
    public static SnowFlakeIdUtil get() {
        return IdGenHolder.instance;
    }

    /**
     * 初始化构造，无参构造有参函数，默认节点都是0
     */
    public SnowFlakeIdUtil() {
        this(getWorkId(), getDataCenterId());
        this.snowflake = IdUtil.createSnowflake(machineId, dataCenterId);
    }

    public synchronized long id(){
        return snowflake.nextId();
    }

    public static Long getId() {
        return SnowFlakeIdUtil.get().id();
    }

    /**
     * workId使用IP生成
     * @return workId
     */
    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            log.info("hostAddress: {}", hostAddress);
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums = sums + b;
            }
            String uniqueIdentifier = System.getProperty("unique_identifier");
            log.info("uniqueIdentifier: {}", uniqueIdentifier);
            if (uniqueIdentifier != null) {
                sums += uniqueIdentifier.hashCode();
            }
            return (long) (sums % 32);
        }
        catch (UnknownHostException e) {
            // 失败就随机
            return RandomUtils.nextLong(0, 31);
        }
    }


    /**
     * dataCenterId使用hostName生成
     * 只要主机名符合host-1，host-2就可以自动提取机器标识
     * @return dataCenterId
     */
    private static Long getDataCenterId() {
        try {
            String hostName = SystemUtils.getHostName();
            if (StringUtils.isBlank(hostName)) {
                hostName = System.getProperty("hostname");
            }
            log.info("hostName: {}", hostName);
            int[] ints = StringUtils.toCodePoints(hostName);
            int sums = 0;
            for (int i: ints) {
                sums = sums + i;
            }
            String uniqueIdentifier = System.getProperty("unique_identifier");
            if (uniqueIdentifier != null) {
                sums += uniqueIdentifier.hashCode();
            }
            return (long) (sums % 32);
        }
        catch (Exception e) {
            // 失败就随机
            return RandomUtils.nextLong(0, 31);
        }
    }

}
