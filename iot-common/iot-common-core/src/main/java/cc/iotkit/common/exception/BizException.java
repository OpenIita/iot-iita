/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.common.exception;

import cc.iotkit.common.enums.ErrCode;

public class BizException extends RuntimeException{

    private int code;
    private String message;

    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }

    /**
     * 统一异常消息处理
     *
     * @param ErrCode 异常枚举值
     */
    public BizException(ErrCode ErrCode) {
        this.message = ErrCode.getValue();
    }

    public BizException(ErrCode ErrCode, Throwable cause) {
        super(cause);
        this.message = ErrCode.getValue();
    }

    /**
     * 自定义异常消息处理
     *
     * @param code
     * @param message
     */
    public BizException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
