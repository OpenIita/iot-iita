package cc.iotkit.contribution.repository;

import cc.iotkit.contribution.data.model.TbIotContributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * 贡献者对象 iot_contributor
 *
 * @author Lion Li
 * @date 2023-07-04
 */
@Repository
public interface IotContributorRepository extends JpaRepository<TbIotContributor, Long>, QuerydslPredicateExecutor<TbIotContributor> {

}
