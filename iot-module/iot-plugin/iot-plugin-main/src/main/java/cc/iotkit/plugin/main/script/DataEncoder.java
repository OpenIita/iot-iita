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

package cc.iotkit.plugin.main.script;

import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据编码
 *
 * @author sjg
 */
@Slf4j
public class DataEncoder {

    public static Buffer encode(DataPackage data) {

        Buffer body = Buffer.buffer();

        append(data.getMid(), body);

        append(data.getPluginId(), body);

        append(data.getMethod(), body);

        append(data.getArgs(), body);

        append(data.getResult(), body);

        Buffer buffer = Buffer.buffer();

        byte[] bytes = body.getBytes();
        buffer.appendInt(bytes.length);
        buffer.appendBytes(bytes);

        return buffer;
    }

    private static void append(String s, Buffer buffer) {
        byte[] bytes = s == null ? new byte[]{} : s.getBytes();
        buffer.appendInt(bytes.length);
        buffer.appendBytes(bytes);
    }
}
