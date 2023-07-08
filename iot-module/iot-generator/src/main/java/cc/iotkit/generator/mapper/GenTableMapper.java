package cc.iotkit.generator.mapper;

import cc.iotkit.generator.core.BaseMapperPlus;
import cc.iotkit.generator.domain.GenTable;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 业务 数据层
 *
 * @author Lion Li
 */
@InterceptorIgnore( tenantLine = "true")
public interface GenTableMapper extends BaseMapperPlus<GenTable, GenTable> {

    /**
     * 查询据库列表
     *
     * @param genTable 查询条件
     * @return 数据库表集合
     */
    @InterceptorIgnore( tenantLine = "true")
    Page<GenTable> selectPageDbTableList(@Param("page") Page<GenTable> page, @Param("genTable") GenTable genTable);

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    List<GenTable> selectDbTableListByNames(Collection<String> tableNames);

    /**
     * 查询所有表信息
     *
     * @return 表信息集合
     */
    List<GenTable> selectGenTableAll();

    /**
     * 查询表ID业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    GenTable selectGenTableById(Long id);

    /**
     * 查询表名称业务信息
     *
     * @param tableName 表名称
     * @return 业务信息
     */
    GenTable selectGenTableByName(String tableName);

    List<String> selectTableNameList(String dataName);


}
