package cc.iotkit.generator.core;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;

/**
 * @author: Jay
 * @description:
 * @date:created in 2023/5/18 10:20
 * @modificed by:
 */
public class DbIdGenerator implements IdentifierGenerator {


  public  DbIdGenerator(Short workerId) {
//    使用网卡信息绑定雪花生成器
//    防止集群雪花ID重复

    IdGeneratorOptions options = new IdGeneratorOptions(workerId);
    YitIdHelper.setIdGenerator(options);
  }



  @Override
  public Number nextId(Object entity) {
    return YitIdHelper.nextId();
  }
}
