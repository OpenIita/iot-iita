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
package cc.iotkit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo extends TenantModel implements Id<Long>, Serializable {

    public static final int USER_TYPE_PLATFORM = 0;
    public static final int USER_TYPE_CLIENT = 1;

    private Long id;

    /**
     * 用户账号
     */
    private String uid;

    /**
     * 密钥（密码加密后的内容）
     */
    private String secret;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 性别 0-未知 1-male,2-female
     */
    private Integer gender;

    /**
     * 头像地址
     */
    private String avatarUrl;

    private String email;

    private String address;

    /**
     * 当前家庭Id
     */
    private String currHomeId;

    /**
     * 用户类型
     * 0:平台用户
     * 1:终端用户
     */
    private Integer type;

    /**
     * 角色
     */
    private List<String> roles = new ArrayList<>();

    /**
     * 权限
     */
    private List<String> permissions = new ArrayList<>();

    /**
     * 用户使用的平台
     * 见:Constants.THIRD_PLATFORM
     */
    private List<String> usePlatforms = new ArrayList<>();

}
