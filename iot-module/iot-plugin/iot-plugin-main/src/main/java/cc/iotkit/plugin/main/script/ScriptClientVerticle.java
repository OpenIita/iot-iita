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

import cn.hutool.core.util.HexUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sjg
 */
@Slf4j
public class ScriptClientVerticle extends AbstractVerticle {

    private NetClient netClient;

    private NetSocket socket;

    private AtomicInteger atMid = new AtomicInteger(0);

    @Override
    public void start() {
        initClient();
    }

    @Override
    public void stop() {
        if (null != netClient) {
            netClient.close();
        }
    }

    private void initClient() {
        NetClientOptions options = new NetClientOptions();
        options.setReconnectAttempts(Integer.MAX_VALUE);
        options.setReconnectInterval(20000L);
        netClient = vertx.createNetClient(options);
        RecordParser parser = DataReader.getParser(this::handle);

        netClient.connect(new ScriptServerConfig().getPort(), "127.0.0.1", result -> {
            if (result.succeeded()) {
                log.debug("connect tcp success");
                socket = result.result();
                socket.handler(parser);
            } else {
                log.error("connect tcp error", result.cause());
            }
        });
    }

    private short getMid() {
        atMid.compareAndSet(254, 0);
        return (short) atMid.getAndIncrement();
    }

    public String send(DataPackage data) {
        Buffer buffer = DataEncoder.encode(data);
        socket.write(buffer);
        Chan<DataPackage> chan = Chan.getInstance();
        DataPackage receiver = chan.get(data.getMid());
        if (receiver == null) {
            return null;
        }
        if (receiver.getMid().equals(data.getMid())) {
            return receiver.getResult();
        }
        return null;
    }

    public void handle(Buffer buffer) {
        DataPackage data = DataDecoder.decode(buffer);
        Chan<DataPackage> chan = Chan.getInstance();
        chan.put(data);
    }

}