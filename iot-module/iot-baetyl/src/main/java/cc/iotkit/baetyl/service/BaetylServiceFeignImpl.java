package cc.iotkit.baetyl.service;

import cc.iotkit.baetyl.IBaetylService;
import cc.iotkit.baetyl.dto.bo.*;
import cc.iotkit.baetyl.dto.vo.*;
import cc.iotkit.baetyl.feign.BaetylServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty(value = "baetyl.api-type", havingValue = "feign")
@Component
public class BaetylServiceFeignImpl implements IBaetylService {

    @Lazy
    @Autowired
    private BaetylServiceFeignClient client;

    @Override
    public GetNodeAppsByNameVo getNodeAppsByName(String name) {
        return client.getNodeAppsByName(name);
    }

    @Override
    public UpdateCoreConfigByNameVo updateCoreConfigByName(UpdateCoreConfigByNameBo data) {
        return client.updateCoreConfigByName(data.getName(),data.getBody());
    }

    @Override
    public CreateNodeVo creatNode(CreateNodeBo data) {
        return client.creatNode(data);
    }

    @Override
    public Boolean deleteNodeByName(String data) {
        return client.deleteNodeByName(data);
    }

    @Override
    public GetNodesBatchVo getNodesBatch(String[] data) {
        return client.getNodesBatch(data);
    }

    @Override
    public UpdateNodePropertiesVo updateNodeProperties(UpdateNodePropertiesBo data) {
        return client.updateNodeProperties(data.getName(),data.getState());
    }

    @Override
    public GetNodeByNameVo getNodeByName(String data) {
        return client.getNodeByName(data);
    }

    @Override
    public UpdateNodeVo updateNode(UpdateNodeBo data) {
        return client.updateNode(data.getName(),data.getBody());
    }

    @Override
    public GetNodeStatsVo getNodeStats(String data) {
        return client.getNodeStats(data);
    }

    @Override
    public GetNodeCoreVersionVo getNodeCoreVersion(String data) {
        return client.getNodeCoreVersion(data);
    }

    @Override
    public GetNodesVo getNodes(GetNodesBo data) {
        return client.getNodes(data.getSelector(),data.getFieldSelector(),data.getLimit(),data.getIsContinue(),data.getPageNo(),data.getPageSize(),
                data.getName(),data.getNodeSelector());
    }

    @Override
    public GetCoreConfigVo getCoreConfig(String data) {
        return client.getCoreConfig(data);
    }

    @Override
    public String getInstallCommand(GetInstallCommandBo data) {
        return client.getInstallCommand(data.getName(),data.getMode());
    }

    @Override
    public GetNodePropertiesVo getNodeProperties(String data) {
        return client.getNodeProperties(data);
    }
}
