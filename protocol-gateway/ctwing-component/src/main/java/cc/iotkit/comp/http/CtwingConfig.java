/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.http;

import lombok.Data;

@Data
public class CtwingConfig {

    private int port;

    /**
     * ctwing推送消息加解密token
     */
    private String encryptToken;

    /**
     * ctwing应用的appKey
     */
    private String appKey;

    /**
     * ctwing应用的appSecret
     */
    private String appSecret;

}
