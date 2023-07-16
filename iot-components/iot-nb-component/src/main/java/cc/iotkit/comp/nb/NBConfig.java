/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.nb;

import lombok.Data;

@Data
public class NBConfig {

    private int port;

    private String sslKey;

    private String sslCert;

    private boolean ssl;

    private boolean useWebSocket;

}
