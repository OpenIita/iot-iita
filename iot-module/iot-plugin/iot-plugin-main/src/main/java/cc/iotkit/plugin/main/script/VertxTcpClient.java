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
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.time.Duration;

/**
 * @author sjg
 */
@Slf4j
public class VertxTcpClient {
    @Getter
    private String id;
    public NetSocket socket;
    @Setter
    private long keepAliveTimeoutMs = Duration.ofSeconds(30).toMillis();
    private volatile long lastKeepAliveTime = System.currentTimeMillis();

    @Setter
    private RecordParser parser;

    public VertxTcpClient(String id) {
        this.id = id;
    }

    public void keepAlive() {
        lastKeepAliveTime = System.currentTimeMillis();
    }

    public boolean isOnline() {
        return System.currentTimeMillis() - lastKeepAliveTime < keepAliveTimeoutMs;
    }

    public void setSocket(NetSocket socket) {
        synchronized (this) {
            if (this.socket != null && this.socket != socket) {
                this.socket.close();
            }

            this.socket = socket
                    .closeHandler(v -> shutdown())
                    .handler(buffer -> {
                        if (log.isDebugEnabled()) {
                            log.debug("handle tcp client[{}] payload:[{}]",
                                    socket.remoteAddress(),
                                    Hex.encodeHexString(buffer.getBytes()));
                        }
                        keepAlive();
                        parser.handle(buffer);
                        if (this.socket != socket) {
                            log.warn("tcp client [{}] memory leak ", socket.remoteAddress());
                            socket.close();
                        }
                    });
        }
    }

    public void shutdown() {
        log.debug("tcp client [{}] disconnect", getId());
        synchronized (this) {
            if (null != socket) {
                execute(socket::close);
                this.socket = null;
            }
        }
    }

    public void sendMessage(Buffer buffer) {
        socket.write(buffer, r -> {
            keepAlive();
            if (!r.succeeded()) {
                log.error("client msg send failed", r.cause());
            }
        });
    }

    private void execute(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.warn("close tcp client error", e);
        }
    }

}
