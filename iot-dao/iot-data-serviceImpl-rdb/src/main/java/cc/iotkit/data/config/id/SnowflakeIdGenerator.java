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

package cc.iotkit.data.config.id;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/18 10:20
 * @modificed by:
 */
@Component
public class SnowflakeIdGenerator implements IdentifierGenerator {

  @PostConstruct
  public void snowflakeIdGenerator() {
    // TODO: 2023/6/12 从配置文件中读取
    IdGeneratorOptions options = new IdGeneratorOptions((short) 1);
    YitIdHelper.setIdGenerator(options);
  }

  @Override
  public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
    Field id = null;
    try {
      id = o.getClass().getDeclaredField("id");
      id.setAccessible(true);
      Object val = id.get(o);
      if (Objects.nonNull(val)){
        return (Serializable) val;
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {

    }
    return YitIdHelper.nextId();
  }
}
