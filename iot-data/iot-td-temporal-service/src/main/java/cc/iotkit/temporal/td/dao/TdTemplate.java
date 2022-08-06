package cc.iotkit.temporal.td.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class TdTemplate extends JdbcTemplate {

    public TdTemplate() {
    }

    public TdTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public TdTemplate(DataSource dataSource, boolean lazyInit) {
        super(dataSource, lazyInit);
    }
}
