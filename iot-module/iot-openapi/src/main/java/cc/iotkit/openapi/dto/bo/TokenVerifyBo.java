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

package cc.iotkit.openapi.dto.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(value = "TokenVerifyBo")
@Data
public class TokenVerifyBo {
    private static final long serialVersionUID = -1L;

    @NotBlank(message = "appid不能为空")
    @ApiModelProperty(value = "appid")
    private String appid;

    @NotBlank(message = "timestamp不能为空")
    @ApiModelProperty(value = "时间戳")
    private String timestamp;

    @NotBlank(message = "identifier不能为空")
    @ApiModelProperty(value = "标识符")
    private String identifier;

    @NotNull(message = "{tenant.number.not.blank}")
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

}
