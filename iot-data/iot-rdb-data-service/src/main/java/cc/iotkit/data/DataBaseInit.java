package cc.iotkit.data;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class DataBaseInit {

    @Autowired
    private JdbcTemplate template;

//    @PostConstruct
    public void initDb() throws IOException {
        //执行初始化数据库脚本
        File file = ResourceUtils.getFile("ddl.sql");
        String ddl = FileUtils.readFileToString(file, UTF_8);

        template.execute(ddl);
    }

}
