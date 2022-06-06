package cc.iotkit.comp.http;

import cc.iotkit.common.utils.HexUtil;
import io.vertx.core.MultiMap;

import java.nio.ByteBuffer;
import java.util.*;

public class ProtocolUtil {

    /**
     * 将数据编码成68H16H协议数据包
     */
    public static String encode68H16H(String devId, String cardNo, Object[] values) {
        //构建数据域
        ByteBuffer bufferData = ByteBuffer.allocate(7);
        //模拟数据..
        bufferData.put((byte) 1);
        bufferData.putShort((short) 4);
        bufferData.putInt(100);
        byte[] data = bufferData.array();

        //起始符到卡号部分数据
        ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + 5 + data.length + 4 + 7 + 2);
        int len = 1 + 2 + 5 + data.length + 4 + 7 + 2;
        buffer.put((byte) len);
        buffer.put((byte) 0x68);
        buffer.putShort((short) 0);
        buffer.put(devId.getBytes());
        buffer.put(data);
        buffer.putInt(0);
        buffer.put(cardNo.getBytes());
        byte[] data1 = buffer.array();

        //校验码
        int check = HexUtil.calcCrc16(data1, 0, data1.length);
        //完整数据包
        buffer = ByteBuffer.allocate(1 + data1.length + 2 + 1);
        buffer.put((byte) (data1.length + 2));//帧长度
        buffer.put(data1);//起始符到卡号部分数据
        buffer.putShort((short) check);//检验码
        buffer.put((byte) 0x16);
        return HexUtil.toHexString(buffer.array());
    }

    /**
     * 将68H16H协议的base64字符串消息解码为map数据
     */
    public static Map<String, Object> decode68H16H(String base64Str) {
        byte[] bytes = Base64.getDecoder().decode(base64Str);

        Map<String, Object> decodeData = new HashMap<>();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.flip();
        byte len = buffer.get();//帧长度
        buffer.get();//帧起始符
        buffer.getShort();//预留2byte
        byte[] devId = new byte[5];//设备ID
        buffer.get(devId, 0, 5);
        String strDevId = new String(devId);
        decodeData.put("devId", strDevId);

        //数据域长度=帧长度-起始符-预留-设备ID-系统用-卡号-校验码
        int dataLen = len - 1 - 2 - 5 - 4 - 7 - 2;
        //数据域
        byte[] data = new byte[dataLen];
        buffer.get(data, 0, dataLen);
        Object[] values = ProtocolUtil.getTlvValues(data);
        //模拟取1个值
        decodeData.put("flow", values[0]);

        buffer.getInt();//系统用
        //卡号
        byte[] card = new byte[7];
        buffer.get(card, 0, card.length);
        String cardNo = new String(card);
        decodeData.put("cardNo", cardNo);

        return decodeData;
    }

    public static Object[] getTlvValues(byte[] data) {
        List<Object> result = new ArrayList<>();

        ByteBuffer dataBuff = ByteBuffer.wrap(data);
        dataBuff.flip();
        //对数据域解码...
        while (dataBuff.hasRemaining()) {
            byte t = dataBuff.get();
            byte l = dataBuff.get();
            byte[] bytesV = new byte[l];
            dataBuff.get(bytesV, 0, bytesV.length);
            if (t == 0) {
                //int
                result.add(HexUtil.bytesToInt(bytesV));
            }
            //..其它类型
        }
        return result.toArray();
    }

    public static Map<String, List<Object>> getListData(MultiMap multiMap) {
        Map<String, List<Object>> listData = new HashMap<>();
        for (Map.Entry<String, String> entry : multiMap.entries()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            listData.putIfAbsent(key, new ArrayList<>());
            listData.get(key).add(value);
        }
        return listData;
    }

    public static Map<String, Object> getData(MultiMap multiMap) {
        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, String> entry : multiMap.entries()) {
            data.put(entry.getKey(), entry.getValue());
        }
        return data;
    }
}
