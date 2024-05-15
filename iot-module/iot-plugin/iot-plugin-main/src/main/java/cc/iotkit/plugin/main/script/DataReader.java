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


import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

import java.util.function.Consumer;

/**
 * 数据包读取器
 *
 * @author sjg
 */
public class DataReader {

    public static RecordParser getParser(Consumer<Buffer> receiveHandler) {
        RecordParser parser = RecordParser.newFixed(4);
        // 设置处理器
        parser.setOutput(new Handler<>() {
            // 表示当前数据长度
            int size = -1;

            @Override
            public void handle(Buffer buffer) {
                //-1表示当前还没有长度信息，需要从收到的数据中取出长度
                if (-1 == size) {
                    //取出长度
                    size = buffer.getInt(0);
                    //动态修改长度
                    parser.fixedSizeMode(size);
                } else {
                    //如果size != -1, 说明已经接受到长度信息了，接下来的数据就是protobuf可识别的字节数组
                    byte[] buf = buffer.getBytes();
                    receiveHandler.accept(Buffer.buffer(buf));
                    //处理完后要将长度改回
                    parser.fixedSizeMode(4);
                    //重置size变量
                    size = -1;
                }
            }
        });
        return parser;
    }
}
