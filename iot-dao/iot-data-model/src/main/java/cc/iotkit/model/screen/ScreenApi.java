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

package cc.iotkit.model.screen;

import cc.iotkit.model.Owned;
import lombok.Data;

/**
 * @Author：tfd
 * @Date：2023/6/25 15:04
 */
@Data
public class ScreenApi implements Owned<Long> {

    private Long id;
    private String uid;
    /**
     * 大屏id
     */
    private Long screenId;

    /**
     * 接口路径
     */
    private String apiPath;

    /**
     * 接口参数
     */
    private String apiParams;

    /**
     * 请求方法
     */
    private String httpMethod;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 创建时间
     */
    private Long createAt;

    /**
     * 转换脚本
     */
    private String script;
}
