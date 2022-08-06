package cc.iotkit.temporal.td.dm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TdField {
    private String name;
    private String type;
    private int length;
}