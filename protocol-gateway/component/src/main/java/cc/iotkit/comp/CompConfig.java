package cc.iotkit.comp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompConfig {

    private long cmdTimeout;

    private String other;

}
