package cc.iotkit.manager.service;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.AppDesignRepository;
import cc.iotkit.manager.config.Constants;
import cc.iotkit.manager.model.vo.AppPageNode;
import cc.iotkit.model.product.AppDesign;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AppDesignService {


    @Autowired
    private AppDesignRepository appDesignRepository;

    public AppDesign getDesignDetail(String pk) {
        return appDesignRepository.findOne(Example.of(AppDesign.builder()
                .productKey(pk).build())).orElse(null);
    }

    @Cacheable(value = Constants.APP_DESIGN_CACHE, key = "#pk")
    public List<AppPageNode> getAppPageNodes(String pk) {
        AppDesign design = appDesignRepository.findOne(Example.of(AppDesign.builder()
                .productKey(pk).build())).orElse(null);
        if (design == null) {
            return new ArrayList<>();
        }

        String template = design.getTemplate();
        JsonNode node = JsonUtil.parse(template);
        List<AppPageNode> nodeTable = new ArrayList<>();

        String id = node.get("id").asText();
        JsonNode children = node.get("children");
        if (children.isArray()) {
            readSubNodes(id, children, nodeTable);
        }
        return nodeTable;
    }

    private void readSubNodes(String parentId, JsonNode nodes, List<AppPageNode> nodeTable) {
        nodes.forEach(node -> readNode(parentId, node, nodeTable));
    }

    private void readNode(String parentId, JsonNode node, List<AppPageNode> nodeTable) {
        AppPageNode pageNode = AppPageNode.builder()
                .id(node.get("id").asText())
                .name(node.get("name").asText())
                .parent(parentId)
                .build();
        if (node.has("props")) {
            pageNode.setProps(JsonUtil.parse(node.get("props").toString(), Map.class));
        }
        if (node.has("appearance")) {
            pageNode.setAppearance(JsonUtil.parse(node.get("appearance").toString(), Map.class));
        }
        if (node.has("binds")) {
            pageNode.setBinds(JsonUtil.parse(node.get("binds").toString(), Map.class));
        }
        if (node.has("action")) {
            pageNode.setAction(JsonUtil.parse(node.get("action").toString(), Map.class));
        }
        nodeTable.add(pageNode);

        String id = node.get("id").asText();
        JsonNode children = node.get("children");
        if (children.isArray()) {
            readSubNodes(id, children, nodeTable);
        }
    }
}
