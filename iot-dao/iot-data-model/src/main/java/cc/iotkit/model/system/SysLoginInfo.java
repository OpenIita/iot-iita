/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.model.system;

import cc.iotkit.model.Id;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


/**
 * 系统访问记录视图对象 sys_logininfor
 *
 * @author Michelle.Chung
 * @date 2023-02-07
 */
@Data
public class SysLoginInfo implements Id<Long>,Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 访问ID
     */
    private Long id;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 登录状态（0成功 1失败）
     */
    private String status;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;


    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private Date loginTime;


}
