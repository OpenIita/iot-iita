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

package cc.iotkit.data.model;

import cc.iotkit.common.tenant.listener.TenantListener;
import cc.iotkit.model.screen.Screen;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

/**
 * @Author：tfd
 * @Date：2023/6/25 15:02
 */
@Data
@Entity
@Table(name = "screen")
@AutoMapper(target = Screen.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "long")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@EntityListeners(TenantListener.class)
public class TbScreen {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    private Long id;
    /**
     * 大屏名称
     */
    private String name;

    /**
     * 资源文件
     */
    private String resourceFile;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 发布状态
     */
    private String state;

    /**
     * 创建时间
     */
    private Long createAt;

    /**
     * 是否为默认大屏
     */
    private Boolean isDefault;
}
