/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.emqx;

import lombok.Data;

import java.util.List;

@Data
public class EmqxConfig {

    private int authPort;

    private String broker;

    private int port;

    private boolean ssl;

    private String clientId;

    private String username;

    private String password;

    private List<String> subscribeTopics;
}
