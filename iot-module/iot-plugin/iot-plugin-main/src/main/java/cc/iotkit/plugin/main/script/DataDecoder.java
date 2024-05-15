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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据解码
 *
 * @author sjg
 */
@Slf4j
public class DataDecoder {

    public static DataPackage decode(Buffer buffer) {
        DataPackage data = new DataPackage();
        ReadField rf = new ReadField(0, buffer);

        data.setMid(rf.read());
        data.setPluginId(rf.read());
        data.setMethod(rf.read());
        data.setArgs(rf.read());
        data.setResult(rf.read());
        return data;
    }


    @Data
    @AllArgsConstructor
    private static class ReadField {

        private int idx = 0;

        private Buffer buffer;

        private String read() {
            int len = buffer.getInt(idx);
            idx += 4;
            String s = new String(buffer.getBytes(idx, idx + len));
            idx += len;
            return s;
        }

    }
}
