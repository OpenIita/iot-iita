package cc.iotkit.model.ota;

import cc.iotkit.model.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 21:13
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtaPackage implements Id<String> {

    private String id;

    private Long size;

    private String sign;

    private String name;

    private String version;

    private String url;

    private Long createAt;
}
