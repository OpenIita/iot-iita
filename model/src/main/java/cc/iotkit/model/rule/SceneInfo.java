package cc.iotkit.model.rule;

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class SceneInfo implements Owned {

    public static final String STATE_STOPPED = "stopped";
    public static final String STATE_RUNNING = "running";

    @Id
    private String id;

    private String name;

    private List<Listener> listeners;

    private List<Filter> filters;

    private List<RuleAction> actions;

    private String uid;

    private String state;

    private String desc;

    private Long createAt;

    @Data
    public static class Listener {
        private String type;
        protected String config;
    }

    @Data
    public static class Filter {
        private String type;
        protected String config;
    }

}
