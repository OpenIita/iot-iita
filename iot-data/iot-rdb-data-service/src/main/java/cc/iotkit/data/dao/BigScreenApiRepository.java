package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbBigScreenApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:54
 */
public interface BigScreenApiRepository extends JpaRepository<TbBigScreenApi, String> {
    List<TbBigScreenApi> findByUid(String uid);

    Page<TbBigScreenApi> findByUid(String uid, Pageable pageable);

    long countByUid(String uid);
}
