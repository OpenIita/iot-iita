package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbBigScreen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:54
 */
public interface BigScreenRepository extends JpaRepository<TbBigScreen, String> {
    List<TbBigScreen> findByUid(String uid);

    Page<TbBigScreen> findByUid(String uid, Pageable pageable);

    TbBigScreen findByUidAndIsDefault(String uid, boolean isDefault);

    long countByUid(String uid);
}
