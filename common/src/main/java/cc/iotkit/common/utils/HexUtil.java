package cc.iotkit.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;

public class HexUtil {

    private static final char[] CHARS_TABLES = "0123456789ABCDEF".toCharArray();
    static final byte[] BYTES = new byte[128];

    static {
        for (int i = 0; i < 10; i++) {
            BYTES['0' + i] = (byte) i;
            BYTES['A' + i] = (byte) (10 + i);
            BYTES['a' + i] = (byte) (10 + i);
        }
    }

    public static String toHexString(byte[] aBytes) {
        return toHexString(aBytes, 0, aBytes.length);
    }

    public static String toFormattedHexString(byte[] aBytes) {
        return toFormattedHexString(aBytes, 0, aBytes.length);
    }

    public static String toHexString(byte[] aBytes, int aLength) {
        return toHexString(aBytes, 0, aLength);
    }

    public static byte[] parseHex(String aHexString) {
        char[] src = aHexString.replace("\n", "").replace(" ", "").toUpperCase().toCharArray();
        byte[] dst = new byte[src.length / 2];

        for (int si = 0, di = 0; di < dst.length; di++) {
            byte high = BYTES[src[si++] & 0x7f];
            byte low = BYTES[src[si++] & 0x7f];
            dst[di] = (byte) ((high << 4) + low);
        }

        return dst;
    }

    public static String toFormattedHexString(byte[] aBytes, int aOffset, int aLength) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(aLength);
        sb.append("] :");
        for (int si = aOffset, di = 0; si < aOffset + aLength; si++, di++) {
            byte b = aBytes[si];
            if (di % 4 == 0) {
                sb.append("  ");
            } else {
                sb.append(' ');
            }
            sb.append(CHARS_TABLES[(b & 0xf0) >>> 4]);
            sb.append(CHARS_TABLES[(b & 0x0f)]);

        }

        return sb.toString();

    }

    public static String toHexString(byte[] aBytes, int aOffset, int aLength) {
        char[] dst = new char[aLength * 2];

        for (int si = aOffset, di = 0; si < aOffset + aLength; si++) {
            byte b = aBytes[si];
            dst[di++] = CHARS_TABLES[(b & 0xf0) >>> 4];
            dst[di++] = CHARS_TABLES[(b & 0x0f)];
        }

        return new String(dst);
    }

    public static String unwrapCharString(String charStr) {
        byte[] bytes = parseHex(charStr);
        StringBuilder rawStr = new StringBuilder();
        for (byte aByte : bytes) {
            rawStr.append((char) aByte);
        }
        return rawStr.toString();
    }

    /**
     * int转bytes
     */
    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(0, x);
        return buffer.array();
    }

    public static int checkSum(ByteBuffer buffer) {
        buffer.flip();
        byte sum = 0;
        while (buffer.hasRemaining()) {
            sum += buffer.get();
        }
        buffer.limit(buffer.capacity());
        return sum % 256;
    }

    public static byte[] toLowerBytes(byte[] bytes) {
        int len = bytes.length;
        byte[] r = new byte[len];
        for (int i = 0; i < len; i++) {
            r[len - i - 1] = bytes[i];
        }
        return r;
    }

    public static int toLowerInt(byte[] bytes) {
        int len = bytes.length;
        byte[] r = new byte[len];
        for (int i = 0; i < len; i++) {
            r[len - i - 1] = (byte) (bytes[i] - 0x33);
        }
        return ByteBuffer.wrap(r).getInt();
    }

    public static byte[] shortToBytes(short x) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(0, x);
        return buffer.array();
    }

    public static String readString(ByteBuffer buffer, int len) {
        byte[] dest = new byte[len];
        buffer.get(dest, 0, len);
        return new String(dest);
    }

//    public static int readLowerInt(ByteBuffer buffer, int len) {
//        int r = 0;
//        for (int i = 0; i < len; i++) {
//            byte b = buffer.get();
//            r += (i == 0 ? b - 0x33 : ((b - 0x33) * Math.pow(10, i)));
//        }
//        return r;
//    }

    public static String readHexIntString(ByteBuffer buffer) {
        int b = buffer.get();
        String hex = Integer.toHexString(b - 0x33).replace("f", "");
        return StringUtils.leftPad(hex, 2, "0");
    }

    public static byte[] add33Bytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] + 0x33);
        }
        return bytes;
    }

    public static byte[] minus33Bytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] - 0x33);
        }
        return bytes;
    }

    public static byte[] readBytes(ByteBuffer buffer, int len) {
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = buffer.get();
        }
        return data;
    }

    public static byte[] readAndMinus33Bytes(ByteBuffer buffer, int len) {
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = (byte) (buffer.get() - 0x33);
        }
        return data;
    }

    public static int bcdInt(String row) {
        String bcd = bcdString(row);
        bcd = bcd.replace("FF", "0");
        return Integer.parseInt(bcd);
    }

    public static int bcdInt(ByteBuffer buffer, int len) {
        byte[] bytes = readAndMinus33Bytes(buffer, len);
        return bcdInt(HexUtil.toHexString(bytes));
    }

    public static String bcdString(String row) {
        char[] chars = row.toCharArray();
        int len = chars.length;
        char[] newChars = new char[len];

        for (int i = 0; i < len; i += 2) {
            newChars[i] = chars[len - i - 2];
            newChars[i + 1] = chars[len - i - 1];
        }
        return String.valueOf(newChars);
    }

    public static byte[] intBcdAdd33(int v, int len) {
        String strV = String.valueOf(v);
        strV = StringUtils.leftPad(strV, len * 2, '0');

        return add33Bytes(HexUtil.parseHex(bcdString(strV)));
    }

}