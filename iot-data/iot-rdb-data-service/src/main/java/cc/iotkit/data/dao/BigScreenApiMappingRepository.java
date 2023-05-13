package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbBigScreenApiMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BigScreenApiMappingRepository extends JpaRepository<TbBigScreenApiMapping, String> {

    List<TbBigScreenApiMapping> findByScreenId(String screenId);

    TbBigScreenApiMapping findByScreenIdAndApiId(String screenId, String apiId);

    void deleteByScreenId(String screenId);

}
