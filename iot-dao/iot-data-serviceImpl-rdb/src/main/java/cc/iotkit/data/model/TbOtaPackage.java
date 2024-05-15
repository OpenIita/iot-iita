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

import cc.iotkit.model.ota.OtaPackage;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 21:25
 * @Description:
 */
@Data
@Entity
@Table(name = "ota_package")
@AutoMapper(target = OtaPackage.class)
public class TbOtaPackage {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    private Long id;

    private Long size;

    private String sign;

    private Boolean isDiff;

    private String md5;

    private String name;

    @Column(name = "[desc]")
    private String desc;

    private String version;

    private String url;

    private String signMethod;

    private String module;

    private String productKey;

    private String extData;

    private Long createAt;
}
