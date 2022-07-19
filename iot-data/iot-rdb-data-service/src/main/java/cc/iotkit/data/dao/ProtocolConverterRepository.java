package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbProtocolConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProtocolConverterRepository extends JpaRepository<TbProtocolConverter, String> {

    List<TbProtocolConverter> findByUid(String uid);

    Page<TbProtocolConverter> findByUid(String uid, Pageable pageable);

    long countByUid(String uid);

}
