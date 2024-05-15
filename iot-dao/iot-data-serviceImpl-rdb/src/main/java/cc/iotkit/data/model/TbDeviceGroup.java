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

import cc.iotkit.model.device.DeviceGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "device_group")
@ApiModel(value = "设备组")
@AutoMapper(target = DeviceGroup.class)
public class TbDeviceGroup {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "设备组id")
    private String id;

    @ApiModelProperty(value = "设备组名称")
    private String name;

    /**
     * 所属用户
     */
    @ApiModelProperty(value = "所属用户")
    private String uid;

    /**
     * 分组说明
     */
    @ApiModelProperty(value = "分组说明")
    private String remark;

    /**
     * 设备数量
     */
    @ApiModelProperty(value = "设备数量")
    private int deviceQty;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private long createAt;

}
