package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbProtocolComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProtocolComponentRepository extends JpaRepository<TbProtocolComponent, String> {

    List<TbProtocolComponent> findByState(String state);

}
