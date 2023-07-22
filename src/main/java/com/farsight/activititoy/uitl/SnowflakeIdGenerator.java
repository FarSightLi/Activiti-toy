package com.farsight.activititoy.uitl;

public class SnowflakeIdGenerator {
    // 开始时间戳（2020-01-01）
    private static final long START_TIMESTAMP = 1577836800000L;

    // 机器ID所占的位数
    private static final long MACHINE_BIT = 10L;

    // 序列号所占的位数
    private static final long SEQUENCE_BIT = 12L;

    // 机器ID的最大值，1023
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_BIT);

    // 序列号的最大值，4095
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    // 机器ID向左移12位
    private static final long MACHINE_LEFT = SEQUENCE_BIT;

    // 时间戳向左移22位
    private static final long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    // 上一次生成ID的时间戳
    private static long lastTimestamp = -1L;

    // 序列号
    private static long sequence = 0L;

    // 机器ID
    private static long machineId;

    public SnowflakeIdGenerator(long machineId) {
        if (machineId > MAX_MACHINE_ID || machineId < 0) {
            throw new IllegalArgumentException("Machine ID can't be greater than " + MAX_MACHINE_ID + " or less than 0.");
        }
        this.machineId = machineId;
    }

    // 生成唯一ID
    public static synchronized long generateId() {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID for " + (lastTimestamp - currentTimestamp) + " milliseconds.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = waitUntilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT) | (machineId << MACHINE_LEFT) | sequence;
    }

    //生成字符串的id
    public static String generateStringId() {
        long id = generateId();
        return Long.toString(id);
    }

    private static long waitUntilNextMillis(long lastTimestamp) {
        long currentTimestamp = System.currentTimeMillis();
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }
}

