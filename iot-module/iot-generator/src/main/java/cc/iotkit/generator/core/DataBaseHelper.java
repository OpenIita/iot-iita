package cc.iotkit.generator.core;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.SpringUtils;
import cn.hutool.core.convert.Convert;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 数据库助手
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBaseHelper {

    private static final DynamicRoutingDataSource DS = SpringUtils.getBean(DynamicRoutingDataSource.class);

    /**
     * 获取当前数据库类型
     */
    public static DataBaseType getDataBaseType() {
        DataSource dataSource = DS.determineDataSource();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            return DataBaseType.find(databaseProductName);
        } catch (SQLException e) {
            throw new BizException(e.getMessage());
        }
    }

    public static boolean isMySql() {
        return DataBaseType.MY_SQL == getDataBaseType();
    }

    public static boolean isH2() {
        return DataBaseType.H2 == getDataBaseType();
    }


    public static boolean isOracle() {
        return DataBaseType.ORACLE == getDataBaseType();
    }

    public static boolean isPostgerSql() {
        return DataBaseType.POSTGRE_SQL == getDataBaseType();
    }

    public static boolean isSqlServer() {
        return DataBaseType.SQL_SERVER == getDataBaseType();
    }

    public static String findInSet(Object var1, String var2) {
        DataBaseType dataBasyType = getDataBaseType();
        String var = Convert.toStr(var1);
        if (dataBasyType == DataBaseType.SQL_SERVER) {
            // charindex(',100,' , ',0,100,101,') <> 0
            return String.format("charindex(',%s,' , ','+%s+',') <> 0",var, var2 );
        } else if (dataBasyType == DataBaseType.POSTGRE_SQL) {
            // (select position(',100,' in ',0,100,101,')) <> 0
            return String.format("(select position(',%s,' in ','||%s||',')) <> 0", var, var2);
        } else if (dataBasyType == DataBaseType.ORACLE) {
            // instr(',0,100,101,' , ',100,') <> 0
            return String.format("instr(','||%s||',' , ',%s,') <> 0",var2, var );
        }
        // find_in_set(100 , '0,100,101')
        return String.format("find_in_set('%s' , %s) <> 0", var, var2);
    }
}
