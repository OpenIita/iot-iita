package cc.iotkit.comp.DLT645.analysis;

import lombok.Data;

/**
 * 功能码
 */
@Data
public class DLT645FunCode {

    public static final String func_v97_00000 = "保留";
    public static final String func_v97_00001 = "读数据";
    public static final String func_v97_00010 = "读后续数据";
    public static final String func_v97_00011 = "重读数据";
    public static final String func_v97_00100 = "写数据";
    public static final String func_v97_01000 = "广播校时";
    public static final String func_v97_01010 = "写设备地址";
    public static final String func_v97_01100 = "更改通信速率";
    public static final String func_v97_01111 = "修改密码";
    public static final String func_v97_10000 = "最大需量清零";
    /**
     * 方向：主站发出=false，从站应答=true
     */
    private boolean direct = false;
    /**
     * 从站是否异常应答
     */
    private boolean error = false;
    /**
     * 功能代码
     */
    private byte code = 0;
    /**
     * 是否最后的尾部
     */
    private boolean next = false;

    public static DLT645FunCode decodeEntity(byte func) {
        DLT645FunCode dlt645FunCode = new DLT645FunCode();
        dlt645FunCode.decode(func);
        return dlt645FunCode;
    }

    public static int getCodev1997(String text) {
        if (func_v97_00000.equals(text)) {//
            return 0b00000;
        }
        if (func_v97_00001.equals(text)) {
            return 0b00001;
        }
        if (func_v97_00010.equals(text)) {
            return 0b00010;
        }
        if (func_v97_00011.equals(text)) {
            return 0b00011;
        }
        if (func_v97_00100.equals(text)) {
            return 0b00100;
        }
        if (func_v97_01000.equals(text)) {
            return 0b01000;
        }
        if (func_v97_01010.equals(text)) {
            return 0b01010;
        }
        if (func_v97_01100.equals(text)) {
            return 0b01100;
        }
        if (func_v97_01111.equals(text)) {
            return 0b01111;
        }
        if (func_v97_10000.equals(text)) {
            return 0b10000;
        }
        return 0b00000;
    }

    /**
     * 编码
     *
     * @return 功能码
     */
    public byte encode() {
        int func = 0;
        if (this.direct) {
            func |= 0x80;
        }
        if (this.error) {
            func |= 0x40;
        }
        if (this.next) {
            func |= 0x20;
        }
        func |= this.code & 0x1F;

        return (byte) func;
    }

    /**
     * 生成功能码
     *
     * @param dlt645FunCode
     * @return
     */
    public byte encodeFunCode(DLT645FunCode dlt645FunCode) {
        return dlt645FunCode.encode();
    }

    /**
     * 解码
     *
     * @param func
     */
    public void decode(byte func) {
        this.direct = (func & 0x80) > 0;
        this.error = (func & 0x40) > 0;
        this.next = (func & 0x20) > 0;
        this.code = (byte) (func & 0x1F);
    }

    public String getCodeTextV1997() {
        if (this.code == 0b00000) {
            return func_v97_00000;
        }
        if (this.code == 0b01000) {
            return func_v97_01000;
        }
        if (this.code == 0b00001) {
            return func_v97_00001;
        }
        if (this.code == 0b00010) {
            return func_v97_00010;
        }
        if (this.code == 0b00100) {
            return func_v97_00100;
        }
        if (this.code == 0b01010) {
            return func_v97_01010;
        }
        if (this.code == 0b01100) {
            return func_v97_01100;
        }
        if (this.code == 0b01111) {
            return func_v97_01111;
        }
        if (this.code == 0b10000) {
            return func_v97_10000;
        }

        return "";
    }

    /**
     * 获取文本描述
     *
     * @return 文本描述
     */
    public String getMessage() {
        String message = "";
        if (this.direct) {
            message += "从站发出:";
        } else {
            message += "主站发出:";
        }

        message += this.getCodeTextV1997();

        if (this.error) {
            message += ":异常";
        } else {
            message += ":正常";
        }


        if (this.next) {
            message += ":还有后续帧";
        } else {
            message += ":这是末尾帧";
        }

        return message;
    }
}
