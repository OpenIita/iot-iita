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

package cc.iotkit.temporal.iotdb.config;


import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * iotdb数据库配置
 *
 * @author sjg
 */
@Configuration
public class IotDbConf {

    @Value("${spring.iotdb-datasource.host}")
    private String host;

    @Value("${spring.iotdb-datasource.port}")
    private int port;

    @Value("${spring.iotdb-datasource.username}")
    private String username;

    @Value("${spring.iotdb-datasource.password}")
    private String password;

    @Bean
    public SessionPool getSession() {
        return new SessionPool.Builder()
                .host(host)
                .port(port)
                .user(username)
                .password(password)
                .build();
    }
}
