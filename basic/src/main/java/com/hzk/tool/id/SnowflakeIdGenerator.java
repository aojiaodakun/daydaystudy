package com.hzk.tool.id;

public class SnowflakeIdGenerator {
    // 起始时间戳（2020-01-01）
    private final static long START_TIMESTAMP = 1577836800000L;

    // 每一部分占用的位数
    private final static long SEQUENCE_BIT = 12;   // 序列号占用的位数
    private final static long MACHINE_BIT = 5;     // 机器标识占用的位数
    private final static long DATACENTER_BIT = 5;  // 数据中心占用的位数

    // 每一部分的最大值
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);

    // 每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;  // 数据中心ID
    private long machineId;     // 机器ID
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上一次时间戳

    /**
     * 构造函数
     * @param datacenterId 数据中心ID (0~31)
     * @param machineId    机器ID (0~31)
     */
    public SnowflakeIdGenerator(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID can't be greater than " + MAX_DATACENTER_NUM + " or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("Machine ID can't be greater than " + MAX_MACHINE_NUM + " or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 生成下一个ID
     * @return 唯一ID
     */
    public synchronized long nextId() {
        long currentTimestamp = getCurrentTimestamp();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过，此时应当抛出异常
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - currentTimestamp) + " milliseconds");
        }

        // 如果是同一时间生成的，则进行序列号自增
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号已经达到最大值，需要等待下一毫秒
            if (sequence == 0L) {
                currentTimestamp = getNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，序列号重置
            sequence = 0L;
        }

        // 更新上次时间戳
        lastTimestamp = currentTimestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT)
                | (datacenterId << DATACENTER_LEFT)
                | (machineId << MACHINE_LEFT)
                | sequence;
    }

    /**
     * 获取下一毫秒
     * @param lastTimestamp 上次时间戳
     * @return 当前时间戳
     */
    private long getNextMillis(long lastTimestamp) {
        long currentTimestamp = getCurrentTimestamp();
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = getCurrentTimestamp();
        }
        return currentTimestamp;
    }

    /**
     * 获取当前时间戳
     * @return 当前时间戳(毫秒)
     */
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    // 测试
    public static void main(String[] args) {
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1);
        for (int i = 0; i < 10; i++) {
            long id = idGenerator.nextId();
            System.out.println("生成的ID: " + id + "，长度: " + String.valueOf(id).length());
        }
    }
}
