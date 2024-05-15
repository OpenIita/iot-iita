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

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class Chan<T> {

    private static final long THREAD_WAIT_TIME = 1000_000L * 10_000;

    private volatile T data;

    private volatile Thread t;

    private Chan() {

    }

    public static Chan getInstance() {
        return ChanSingleton.INSTANCE;
    }

    public T get(Object blocker) {
        this.t = Thread.currentThread();
        LockSupport.parkNanos(blocker, THREAD_WAIT_TIME);
        this.t = null;
        return data;
    }

    public void put(T data) {
        this.data = data;
        if (t == null) {
            return;
        }
        LockSupport.unpark(t);
    }

    private static class ChanSingleton {
        private static final Chan<?> INSTANCE = new Chan<>();
    }
}