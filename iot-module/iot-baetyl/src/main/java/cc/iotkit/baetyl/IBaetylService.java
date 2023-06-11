package cc.iotkit.baetyl;

import cc.iotkit.baetyl.dto.bo.*;
import cc.iotkit.baetyl.dto.vo.*;

public interface IBaetylService {
    GetNodeAppsByNameVo getNodeAppsByName(String name);

    UpdateCoreConfigByNameVo updateCoreConfigByName(UpdateCoreConfigByNameBo data);

    CreateNodeVo creatNode(CreateNodeBo data);

    Boolean deleteNodeByName(String data);

    GetNodesBatchVo getNodesBatch(String[] data);

    UpdateNodePropertiesVo updateNodeProperties(UpdateNodePropertiesBo data);

    GetNodeByNameVo getNodeByName(String data);

    UpdateNodeVo updateNode(UpdateNodeBo data);

    GetNodeStatsVo getNodeStats(String data);

    GetNodeCoreVersionVo getNodeCoreVersion(String data);

    GetNodesVo getNodes(GetNodesBo data);
}
