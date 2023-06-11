package cc.iotkit.baetyl.service;

import cc.iotkit.baetyl.IBaetylService;
import cc.iotkit.baetyl.constant.BaetylConstant;
import cc.iotkit.baetyl.dto.bo.*;
import cc.iotkit.baetyl.dto.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@ConditionalOnProperty(value = "baetyl.api-type", havingValue = "webClient")
@Component
public class BaetylServiceWebclientImpl implements IBaetylService {


    @Autowired
    private WebClient webClient;


    @Override
    public GetNodeAppsByNameVo getNodeAppsByName(String name) {
        ParameterizedTypeReference<GetNodeAppsByNameVo> responseBodyType = new ParameterizedTypeReference<>() {};
        return webClient.get()
                .uri(BaetylConstant.Url.NodeManagement.GetNodeAppsByName.replace("{name}",name))
                .retrieve()
                .bodyToMono(responseBodyType)
                .block();
    }

    // TODO: 2023/6/10 后续有需要则实现


    @Override
    public UpdateCoreConfigByNameVo updateCoreConfigByName(UpdateCoreConfigByNameBo data) {
        return null;
    }

    @Override
    public CreateNodeVo creatNode(CreateNodeBo data) {
        return null;
    }

    @Override
    public Boolean deleteNodeByName(String data) {
        return null;
    }

    @Override
    public GetNodesBatchVo getNodesBatch(String[] data) {
        return null;
    }

    @Override
    public UpdateNodePropertiesVo updateNodeProperties(UpdateNodePropertiesBo data) {
        return null;
    }

    @Override
    public GetNodeByNameVo getNodeByName(String data) {
        return null;
    }

    @Override
    public UpdateNodeVo updateNode(UpdateNodeBo data) {
        return null;
    }

    @Override
    public GetNodeStatsVo getNodeStats(String data) {
        return null;
    }

    @Override
    public GetNodeCoreVersionVo getNodeCoreVersion(String data) {
        return null;
    }

    @Override
    public GetNodesVo getNodes(GetNodesBo data) {
        return null;
    }
}
