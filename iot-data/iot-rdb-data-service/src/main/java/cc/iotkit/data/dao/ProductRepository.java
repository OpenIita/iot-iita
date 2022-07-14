package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<TbProduct, String> {

    List<TbProduct> findByCategory(String category);

    List<TbProduct> findByUid(String uid);

    Page<TbProduct> findByUid(String uid, Pageable pageable);

    long countByUid(String uid);

}
