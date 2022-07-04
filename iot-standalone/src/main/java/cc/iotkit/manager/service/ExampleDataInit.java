/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.service;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ExampleDataInit {

    @Autowired
    private OauthClientRepository oauthClientRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
    @Autowired
    private HomeRepository homeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductModelRepository productModelRepository;
    @Autowired
    private ProtocolComponentRepository protocolComponentRepository;
    @Autowired
    private ProtocolConverterRepository protocolConverterRepository;
    @Autowired
    private RuleInfoRepository ruleInfoRepository;
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;
    @Autowired
    private TaskInfoRepository taskInfoRepository;
    @Autowired
    private ThingModelRepository thingModelRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private VirtualDeviceRepository virtualDeviceRepository;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @PostConstruct
    public void init() {
        try {
            File initFile = new File(".init");
            if (initFile.exists()) {
                return;
            }

            initData("category", categoryRepository);
            initData("deviceGroup", deviceGroupRepository);
            initData("deviceInfo", deviceInfoRepository);
            initData("home", homeRepository);
            initData("oauthClient", oauthClientRepository);
            initData("product", productRepository);
            initData("productModel", productModelRepository);
            initData("protocolComponent", protocolComponentRepository);
            initData("protocolConverter", protocolConverterRepository);
            initData("ruleInfo", ruleInfoRepository);
            initData("space", spaceRepository);
            initData("spaceDevice", spaceDeviceRepository);
            initData("taskInfo", taskInfoRepository);
            initData("thingModel", thingModelRepository);
            initData("userInfo", userInfoRepository);
            initData("virtualDevice", virtualDeviceRepository);

            log.info("init data finished.");

            FileUtils.write(initFile, "", Charsets.UTF_8);
        } catch (Throwable e) {
            log.error("init error", e);
        }
    }

    private <T> void initData(String name, ElasticsearchRepository repository) throws IOException {
        log.info("init {} data...", name);
        String json = FileUtils.readFileToString(new File("./data/init/" + name + ".json"), Charsets.UTF_8);
        List<T> list = JsonUtil.parse(json, new TypeReference<>() {
        });
        for (T obj : list) {
            repository.save(obj);
        }
    }

}
