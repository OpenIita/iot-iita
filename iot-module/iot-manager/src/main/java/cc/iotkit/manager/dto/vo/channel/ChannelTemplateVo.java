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

package cc.iotkit.manager.dto.vo.channel;

import cc.iotkit.model.notify.ChannelTemplate;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "ChannelTemplateVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ChannelTemplate.class)
public class ChannelTemplateVo implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value="通道模板id")
    @ExcelProperty(value = "通道模板id")
    private Long id;

    @ApiModelProperty(value="通道配置id")
    @ExcelProperty(value = "通道配置id")
    private Long channelConfigId;

    @ApiModelProperty(value="通道模板名称")
    @ExcelProperty(value = "通道模板名称")
    private String title;

    @ApiModelProperty(value="通道模板内容")
    @ExcelProperty(value = "通道模板内容")
    private String content;

    @ApiModelProperty(value="创建时间")
    @ExcelProperty(value = "创建时间")
    private Long createAt;
}
