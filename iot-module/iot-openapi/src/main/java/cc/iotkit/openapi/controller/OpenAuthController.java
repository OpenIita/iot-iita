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

package cc.iotkit.openapi.controller;

import cc.iotkit.common.api.Request;
import cc.iotkit.model.InvokeResult;
import cc.iotkit.openapi.dto.bo.TokenVerifyBo;
import cc.iotkit.openapi.service.OpenBaseService;
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"openapi-基础"})
@Slf4j
@RestController
@RequestMapping("/openapi")
public class OpenAuthController {

    @Autowired
    private OpenBaseService openBaseService;

    @SaIgnore
    @ApiOperation(value = "token获取", notes = "token获取", httpMethod = "POST")
    @PostMapping("/v1/getToken")
    public InvokeResult getToken(@RequestBody @Validated Request<TokenVerifyBo> request) {
        return new InvokeResult(openBaseService.getToken(request.getData()));
    }


}
