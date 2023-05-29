package cc.iotkit.data.config.id;

import cc.iotkit.common.utils.SnowflakeIdGeneratorUtil;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/18 10:20
 * @modificed by:
 */
@Component
public class SnowflakeIdGenerator implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
    return SnowflakeIdGeneratorUtil.getInstanceSnowflake().nextId();
  }
}
