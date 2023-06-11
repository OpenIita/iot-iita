package cc.iotkit.baetyl.controller;

import cc.iotkit.baetyl.IBaetylService;
import cc.iotkit.baetyl.dto.bo.*;
import cc.iotkit.baetyl.dto.vo.*;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.web.core.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * baetyl接口
 *
 * @author longjun.tu
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/baetyl")
@Api(tags = "边缘计算管理")
public class BartylController extends BaseController {

  @Autowired
  private  IBaetylService configService;

  @ApiOperation("查询节点关联的应用")
  @PostMapping("/getNodeAppsByName")
  public GetNodeAppsByNameVo getNodeAppsByName(@Validated @RequestBody Request<String> request) {
    return configService.getNodeAppsByName(request.getData());
  }

  @ApiOperation("修改 core 配置（core 自升级）")
  @PostMapping("/updateCoreConfigByName")
  public UpdateCoreConfigByNameVo updateCoreConfigByName(@Validated @RequestBody Request<UpdateCoreConfigByNameBo> request) {
    return configService.updateCoreConfigByName(request.getData());
  }

  @ApiOperation("创建节点")
  @PostMapping("/creatNode")
  public CreateNodeVo creatNode(@Validated @RequestBody Request<CreateNodeBo> request) {
    return configService.creatNode(request.getData());
  }

  @ApiOperation("删除节点")
  @PostMapping("/deleteNodeByName")
  public Boolean deleteNodeByName(@Validated @RequestBody Request<String> request) {
    return configService.deleteNodeByName(request.getData());
  }

  @ApiOperation("批量查询节点")
  @PostMapping("/getNodesBatch")
  public GetNodesBatchVo getNodesBatch(@Validated @RequestBody Request<String[]> request) {
    return configService.getNodesBatch(request.getData());
  }

  @ApiOperation("更新节点属性")
  @PostMapping("/updateNodeProperties")
  public UpdateNodePropertiesVo updateNodeProperties(@Validated @RequestBody Request<UpdateNodePropertiesBo> request) {
    return configService.updateNodeProperties(request.getData());
  }

  @ApiOperation("查询节点")
  @PostMapping("/getNodeByName")
  public GetNodeByNameVo getNodeByName(@Validated @RequestBody Request<String> request) {
    return configService.getNodeByName(request.getData());
  }

  @ApiOperation("修改节点")
  @PostMapping("/updateNode")
  public UpdateNodeVo updateNode(@Validated @RequestBody Request<UpdateNodeBo> request) {
    return configService.updateNode(request.getData());
  }

  @ApiOperation("查询节点状态信息")
  @PostMapping("/getNodeStats")
  public GetNodeStatsVo getNodeStats(@Validated @RequestBody Request<String> request) {
    return configService.getNodeStats(request.getData());
  }

  @ApiOperation("罗列当前节点 core 版本号")
  @PostMapping("/getNodeCoreVersion")
  public GetNodeCoreVersionVo getNodeCoreVersion(@Validated @RequestBody Request<String> request) {
    return configService.getNodeCoreVersion(request.getData());
  }

  @ApiOperation("罗列节点")
  @PostMapping("/getNodes")
  public GetNodesVo getNodes(@Validated @RequestBody Request<GetNodesBo> request) {
    return configService.getNodes(request.getData());
  }

}
