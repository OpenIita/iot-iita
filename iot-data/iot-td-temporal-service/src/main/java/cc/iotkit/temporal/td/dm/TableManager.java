package cc.iotkit.temporal.td.dm;

import java.util.List;

public class TableManager {

    /**
     * 创建超级表模板（含存在判断）
     */
    private static final String CREATE_STABLE_INE_TPL = "CREATE STABLE IF NOT EXISTS %s (%s) TAGS (%s);";

    /**
     * 删除超级表
     */
    private static final String DROP_STABLE_TPL = "DROP STABLE IF EXISTS %s;";

    /**
     * 获取表的结构信息
     */
    private static final String DESC_TB_TPL = "DESCRIBE %s;";

    /**
     * 超级表增加列
     */
    private static final String ALTER_STABLE_ADD_COL_TPL = "ALTER STABLE %s ADD COLUMN %s;";

    /**
     * 超级表修改列
     */
    private static final String ALTER_STABLE_MODIFY_COL_TPL = "ALTER STABLE %s MODIFY COLUMN %s;";

    /**
     * 超级表删除列
     */
    private static final String ALTER_STABLE_DROP_COL_TPL = "ALTER STABLE %s DROP COLUMN %s;";

    /**
     * 获取创建表sql
     */
    public static String getCreateSTableSql(String tbName, List<TdField> fields, TdField... tags) {
        if (fields.size() == 0) {
            return null;
        }

        //生成字段片段
        StringBuilder sbField = new StringBuilder("time timestamp,");

        for (TdField field : fields) {
            sbField.append(FieldParser.getFieldDefine(field));
            sbField.append(",");
        }
        sbField.deleteCharAt(sbField.length() - 1);

        String fieldFrag = sbField.toString();

        //生成tag
        StringBuilder sbTag = new StringBuilder();
        for (TdField tag : tags) {
            sbTag.append(FieldParser.getFieldDefine(tag))
                    .append(",");
        }
        sbTag.deleteCharAt(sbTag.length() - 1);

        return String.format(CREATE_STABLE_INE_TPL, tbName, fieldFrag, sbTag.toString());

    }

    /**
     * 取正确的表名
     *
     * @param name 表象
     */
    public static String rightTbName(String name) {
        return name.toLowerCase().replace("-", "_");
    }

    /**
     * 获取表详情的sql
     */
    public static String getDescTableSql(String tbName) {
        return String.format(DESC_TB_TPL, tbName);
    }

    /**
     * 获取添加字段sql
     */
    public static String getAddSTableColumnSql(String tbName, List<TdField> fields) {
        StringBuilder sbAdd = new StringBuilder();
        for (TdField field : fields) {
            sbAdd.append(String.format(ALTER_STABLE_ADD_COL_TPL,
                    tbName,
                    FieldParser.getFieldDefine(field)
            ));
        }
        return sbAdd.toString();
    }

    /**
     * 获取修改字段sql
     */
    public static String getModifySTableColumnSql(String tbName, List<TdField> fields) {
        StringBuilder sbModify = new StringBuilder();
        for (TdField field : fields) {
            sbModify.append(String.format(ALTER_STABLE_MODIFY_COL_TPL,
                    tbName,
                    FieldParser.getFieldDefine(field)
            ));
        }
        return sbModify.toString();
    }

    /**
     * 获取删除字段sql
     */
    public static String getDropSTableColumnSql(String tbName, List<TdField> fields) {
        StringBuilder sbDrop = new StringBuilder();
        for (TdField field : fields) {
            sbDrop.append(String.format(ALTER_STABLE_DROP_COL_TPL,
                    tbName,
                    field.getName()
            ));
        }
        return sbDrop.toString();
    }

}
