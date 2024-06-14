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

package cc.iotkit.model.ota;

import cc.iotkit.model.Id;
import cc.iotkit.model.TenantModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author: 石恒
 * @Date: 2023/6/10 14:35
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtaPackage  extends TenantModel implements Id<Long> {

    private Long id;

    /**
     * 文件包大小
     */
    private Long size;

    /**
     * 签名
     */
    private String sign;

    /**
     * 是否差分升级
     */
    private Boolean isDiff;

    /**
     * 文件MD5后的值
     */
    private String md5;

    /**
     * 包名
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 版本
     */
    private String version;

    /**
     * 升级包地址
     */
    private String url;

    /**
     * 签名方式
     */
    private String signMethod;

    /**
     * 模块
     */
    private String module;

    /**
     * 扩展数据
     */
    private String extData;

    /**
     * 创建时间
     */
    private Long createAt;

    /**
     * 产品key
     */
    private String productKey;
}
