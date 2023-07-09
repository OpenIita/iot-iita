package cc.iotkit.contribution.repository;

import cc.iotkit.contribution.data.model.TbIotContributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * 贡献者对象 iot_contributor
 *
 * @author Lion Li
 * @date 2023-07-09
 */
public interface IotContributorRepository extends JpaRepository<TbIotContributor, Long>, QuerydslPredicateExecutor<TbIotContributor> {

}
