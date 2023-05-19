package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbOtaPackage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 21:54
 * @Description:
 */
public interface IOtaPackageRepository extends JpaRepository<TbOtaPackage, String> {
}
