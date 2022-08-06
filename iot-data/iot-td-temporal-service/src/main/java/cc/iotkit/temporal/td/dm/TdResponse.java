package cc.iotkit.temporal.td.dm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TdResponse {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_TB_NOT_EXIST = 866;

    private String status;

    private int code;

    private String desc;

    //[["time","TIMESTAMP",8,""],["powerstate","TINYINT",1,""],["brightness","INT",4,""],["deviceid","NCHAR",32,"TAG"]]
    private List data;

}
