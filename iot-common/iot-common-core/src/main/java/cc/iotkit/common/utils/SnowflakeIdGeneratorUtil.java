package cc.iotkit.common.utils;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/18 10:20
 * @modificed by:
 */
public class SnowflakeIdGeneratorUtil {

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1577808000000L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;  //数据中心
    private long machineId;     //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳

    private static volatile SnowflakeIdGeneratorUtil snowflake = null;
    private static Object lock = new Object();

    public SnowflakeIdGeneratorUtil(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 获取单列
     *
     * @return
     */
    public static SnowflakeIdGeneratorUtil getInstanceSnowflake() {
        if (snowflake == null) {
            synchronized (lock) {
                if(snowflake == null){
                    long workerId;
                    long dataCenterId = getRandom();
                    try {
                        //第一次使用获取mac地址的
                        workerId = getWorkerId();
                    } catch (Exception e) {
                        workerId = getRandom();
                    }
                    snowflake = new SnowflakeIdGeneratorUtil(dataCenterId, workerId);
                }
            }
        }
        return snowflake;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    /**
     * 生成1-31之间的随机数
     *
     * @return
     */
    private static long getRandom() {
        int max = (int) (MAX_MACHINE_NUM);
        int min = 1;
        Random random = new Random();
        long result = random.nextInt(max - min) + min;
        return result;
    }

    private static long getWorkerId() throws SocketException, UnknownHostException, NullPointerException {
        @SuppressWarnings("unused")
        InetAddress ip = InetAddress.getLocalHost();

        NetworkInterface network = null;
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface nint = en.nextElement();
            if (!nint.isLoopback() && nint.getHardwareAddress() != null) {
                network = nint;
                break;
            }
        }

        if (network == null) {
            throw new NullPointerException("network is null");
        }
        @SuppressWarnings("ConstantConditions")
        byte[] mac = network.getHardwareAddress();
        long id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 11;
        if (id > MAX_MACHINE_NUM) {
            return getRandom();
        }
        return id;
    }

    public static void main(String[] args) {
        Set<Long> idList = new HashSet<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            long id = SnowflakeIdGeneratorUtil.getInstanceSnowflake().nextId();
            idList.add(id);
            // System.out.println("id="+id);
        }
        // System.out.println(idList.size());
        // System.out.println(System.currentTimeMillis() - start);
    }
}