package cc.iotkit.baetyl.feign;

import cc.iotkit.baetyl.constant.BaetylConstant;
import cc.iotkit.baetyl.dto.bo.CreateNodeBo;
import cc.iotkit.baetyl.dto.bo.UpdateCoreConfigByNameBo;
import cc.iotkit.baetyl.dto.bo.UpdateNodeBo;
import cc.iotkit.baetyl.dto.bo.UpdateNodePropertiesBo;
import cc.iotkit.baetyl.dto.vo.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@ConditionalOnClass(FeignClient.class)
@FeignClient(name = "baetylServiceFeignClient" , contextId = "baetylServiceFeignClient" ,url = "${baetyl.service-url:}")
public interface BaetylServiceFeignClient {

    @GetMapping(value = BaetylConstant.Url.NodeManagement.GetNodeAppsByName)
    GetNodeAppsByNameVo getNodeAppsByName(@PathVariable(value = "name") String name);

    @PutMapping(value = BaetylConstant.Url.NodeManagement.UpdateCoreConfigByName)
    UpdateCoreConfigByNameVo updateCoreConfigByName(@PathVariable(value = "name") String name, @RequestBody UpdateCoreConfigByNameBo.UpdateCoreConfigByNameBoBody data);

    @PostMapping(value = BaetylConstant.Url.NodeManagement.CreatNode)
    CreateNodeVo creatNode(@RequestBody  CreateNodeBo data);

    @DeleteMapping(value = BaetylConstant.Url.NodeManagement.DeleteNodeByName)
    Boolean deleteNodeByName(@PathVariable(value = "name") String name);

    @PutMapping(value = BaetylConstant.Url.NodeManagement.GetNodesBatch)
    GetNodesBatchVo getNodesBatch(@RequestBody  String[] data);

    @PutMapping(value = BaetylConstant.Url.NodeManagement.UpdateNodeProperties)
    UpdateNodePropertiesVo updateNodeProperties(@PathVariable(value = "name") String name, @RequestBody UpdateNodePropertiesBo.UpdateNodePropertiesBoBody state);

    @GetMapping(value = BaetylConstant.Url.NodeManagement.GetNodeByName)
    GetNodeByNameVo getNodeByName(@PathVariable(value = "name") String name);

    @PutMapping(value = BaetylConstant.Url.NodeManagement.UpdateNode)
    UpdateNodeVo updateNode(@PathVariable(value = "name") String name, @RequestBody UpdateNodeBo.UpdateNodeBoBody body);
    @GetMapping(value = BaetylConstant.Url.NodeManagement.GetNodeStats)
    GetNodeStatsVo getNodeStats(@PathVariable(value = "name") String name);
    @GetMapping(value = BaetylConstant.Url.NodeManagement.GetNodeCoreVersion)
    GetNodeCoreVersionVo getNodeCoreVersion(@PathVariable(value = "name") String name);
    @GetMapping(value = BaetylConstant.Url.NodeManagement.GetNodes)
    GetNodesVo getNodes(@RequestParam(value = "selector") String selector, @RequestParam(value = "fieldSelector") String fieldSelector, @RequestParam(value = "limit") Integer limit,
                        @RequestParam(value = "continue") String isContinue, @RequestParam(value = "pageNo") Integer pageNo, @RequestParam(value = "pageSize") Integer pageSize,
                        @RequestParam(value = "name") String name, @RequestParam(value = "nodeSelector") String nodeSelector);
    @GetMapping(value = BaetylConstant.Url.NodeManagement.GetCoreConfig)
    GetCoreConfigVo getCoreConfig(@PathVariable(value = "name") String name);
    @GetMapping(value = BaetylConstant.Url.NodeManagement.GetInstallCommand)
    String getInstallCommand(@PathVariable(value = "name") String name, @RequestParam(value = "mode") String mode);
    @GetMapping(value = BaetylConstant.Url.NodeManagement.GetNodeProperties)
    GetNodePropertiesVo getNodeProperties(@PathVariable(value = "name") String data);
}
