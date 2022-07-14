package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<TbCategory, String> {

}
