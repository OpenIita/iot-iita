package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceRepository extends JpaRepository<TbSpace, String> {

    List<TbSpace> findByUidOrderByCreateAtDesc(String uid);

    List<TbSpace> findByUidAndHomeIdOrderByCreateAtDesc(String uid, String homeId);

    List<TbSpace> findByHomeId(String homeId);

    List<TbSpace> findByUid(String uid);

    Page<TbSpace> findByUid(String uid, Pageable pageable);

}
