package cc.iotkit.manager.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppPageNode {

    private String id;

    private String name;

    private String parent;

    Map<String, Object> props;

    Map<String, Object> appearance;

    Map<String, Object> binds;

    Map<String, Object> action;

}
