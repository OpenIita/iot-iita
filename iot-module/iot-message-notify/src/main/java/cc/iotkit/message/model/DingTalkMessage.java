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

package cc.iotkit.message.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * author: 石恒
 * date: 2023-05-08 15:58
 * description:
 **/
@Data
@Builder
public class DingTalkMessage implements Serializable {

    /**
     * 消息文本类型 目前只支持文本
     */
    private String msgtype;

    /**
     * 文本消息
     */
    private MessageContent text;

    @Data
    @Builder
    public static class MessageContent {

        /**
         * 文本内容
         */
        private String content;

    }
}
