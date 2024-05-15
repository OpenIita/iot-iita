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

package cc.iotkit.screen.staticres;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.model.screen.ScreenApi;
import cc.iotkit.screen.api.ScreenApiHandle;
import cc.iotkit.screen.config.ScreenConfig;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author：tfd
 * @Date：2023/6/25 16:01
 */
@Slf4j
public class ScreenComponent {

    private Vertx vertx;
    private CountDownLatch countDownLatch;
    private String deployedId;
    private ScreenApiHandle apiHandle;
    private ScreenVerticle screenVerticle;

    public List<ScreenApi> getScreenApis() {
        return apiHandle.screenApis;
    }

    public void debugMode(boolean state) {
        apiHandle.debugMode = state;
    }

    public void create(int port, String packageName, ScreenConfig screenConfig) {
        vertx = Vertx.vertx();
        screenVerticle = new ScreenVerticle(port, packageName, screenConfig);
    }

    public void setApiHandle(ScreenApiHandle screenApiHandle) {
        this.apiHandle = screenApiHandle;
    }

    public void previewApis(List<ScreenApi> screenApis) {
        this.apiHandle.setScreenApis(screenApis);
    }

    public void publish() {
        try {
            screenVerticle.setApiHandler(apiHandle);
            countDownLatch = new CountDownLatch(1);
            Future<String> future = vertx.deployVerticle(screenVerticle);
            future.onSuccess((s -> {
                deployedId = s;
                countDownLatch.countDown();
            }));
            future.onFailure(e -> {
                countDownLatch.countDown();
                log.error("publish screen failed", e);
            });
            countDownLatch.await();
            future.succeeded();
        } catch (Throwable e) {
            throw new BizException(ErrCode.SCREEN_PUBLISH_ERROR, e);
        }
    }

    @SneakyThrows
    public void unPublish() {
        screenVerticle.stop();
        Future<Void> future = vertx.undeploy(deployedId);
        future.onSuccess(unused -> log.info("unPublish screen success"));
    }
}
