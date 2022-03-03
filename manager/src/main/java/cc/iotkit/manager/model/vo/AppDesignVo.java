package cc.iotkit.manager.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppDesignVo {

    private String id;

    private String productKey;

    private String html;

    private Boolean state;

    private Long modifyAt;

    private String productName;

    private String cateName;

}
