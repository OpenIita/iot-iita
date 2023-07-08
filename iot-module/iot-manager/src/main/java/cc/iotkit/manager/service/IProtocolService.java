package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.manager.dto.bo.ChangeStateBo;
import cc.iotkit.manager.dto.bo.protocolcomponent.ProtocolComponentBo;
import cc.iotkit.manager.dto.bo.protocolconverter.ProtocolConverterBo;
import cc.iotkit.manager.dto.vo.protocolcomponent.ProtocolComponentVo;
import cc.iotkit.manager.dto.vo.protocolconverter.ProtocolConverterVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: jay
 * @Date: 2023/5/29 11:28
 * @Version: V1.0
 * @Description: 协议组件接口
 */
public interface IProtocolService {

    // 上传jar包
    String uploadJar(MultipartFile file, String id);

    // 添加组件
    boolean addComponent(ProtocolComponentBo component);


    String saveComponent(ProtocolComponentBo component);

    ProtocolComponentVo getProtocolComponent(String id);

    boolean saveComponentScript(ProtocolComponentBo upReq);

    boolean deleteComponent(String data);

    Paging<ProtocolComponentVo> selectPageList(PageRequest<ProtocolComponentBo> query);

    Paging<ProtocolConverterVo> selectConvertersPageList(PageRequest<ProtocolConverterBo> query);

    boolean addConverter(ProtocolConverterBo converter);

    boolean editConverter(ProtocolConverterBo req);

    ProtocolConverterVo getConverter(String id);

    boolean saveConverterScript(ProtocolConverterBo req);

    boolean deleteConverter(String id);

    boolean changeComponentState(ChangeStateBo req);
}

