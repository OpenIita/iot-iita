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

package cc.iotkit.system.dto.vo;

import cc.iotkit.model.system.SysApp;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;



/**
 * 应用信息视图对象 SYS_APP
 *
 * @author tfd
 * @date 2023-08-10
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysApp.class)
public class SysAppVo implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;

    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称")
    private String appName;

    /**
     * appId
     */
    @ApiModelProperty(value = "appId")
    private String appId;

    /**
     * appSecret
     */
    @ApiModelProperty(value = "appSecret")
    private String appSecret;

    /**
     * 应用类型
     */
    @ApiModelProperty(value = "应用类型")
    private String appType;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


}
