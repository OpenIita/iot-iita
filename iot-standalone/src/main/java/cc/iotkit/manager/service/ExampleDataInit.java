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
import cc.iotkit.data.*;
import cc.iotkit.model.Id;
import cc.iotkit.model.OauthClient;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.device.DeviceGroup;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.VirtualDevice;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ProductModel;
import cc.iotkit.model.product.ThingModel;
import cc.iotkit.model.protocol.ProtocolComponent;
import cc.iotkit.model.protocol.ProtocolConverter;
import cc.iotkit.model.rule.RuleInfo;
import cc.iotkit.model.rule.TaskInfo;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.space.Space;
import cc.iotkit.model.space.SpaceDevice;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ExampleDataInit {

    @Autowired
    private IOauthClientData oauthClientData;
    @Autowired
    private ICategoryData categoryData;
    @Autowired
    private IDeviceGroupData deviceGroupData;
    @Autowired
    private IDeviceInfoData deviceInfoData;
    @Autowired
    private IHomeData homeData;
    @Autowired
    private IProductData productData;
    @Autowired
    private IProductModelData productModelData;
    @Autowired
    private IProtocolComponentData protocolComponentData;
    @Autowired
    private IProtocolConverterData protocolConverterData;
    @Autowired
    private IRuleInfoData ruleInfoData;
    @Autowired
    private ISpaceData spaceData;
    @Autowired
    private ISpaceDeviceData spaceDeviceData;
    @Autowired
    private ITaskInfoData taskInfoData;
    @Autowired
    private IThingModelData thingModelData;
    @Autowired
    private IUserInfoData userInfoData;
    @Autowired
    private IVirtualDeviceData virtualDeviceData;


    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @PostConstruct
    public void init() {
        try {
            File initFile = new File(".init");
            if (initFile.exists()) {
                return;
            }

            initData("category", categoryData, new TypeReference<List<Category>>() {
            });
            initData("deviceGroup", deviceGroupData, new TypeReference<List<DeviceGroup>>() {
            });
            initData("deviceInfo", deviceInfoData, new TypeReference<List<DeviceInfo>>() {
            });
            initData("home", homeData, new TypeReference<List<Home>>() {
            });
            initData("oauthClient", oauthClientData, new TypeReference<List<OauthClient>>() {
            });
            initData("product", productData, new TypeReference<List<Product>>() {
            });
            initData("productModel", productModelData, new TypeReference<List<ProductModel>>() {
            });
            initData("protocolComponent", protocolComponentData, new TypeReference<List<ProtocolComponent>>() {
            });
            initData("protocolConverter", protocolConverterData, new TypeReference<List<ProtocolConverter>>() {
            });
            initData("ruleInfo", ruleInfoData, new TypeReference<List<RuleInfo>>() {
            });
            initData("space", spaceData, new TypeReference<List<Space>>() {
            });
            initData("spaceDevice", spaceDeviceData, new TypeReference<List<SpaceDevice>>() {
            });
            initData("taskInfo", taskInfoData, new TypeReference<List<TaskInfo>>() {
            });
            initData("thingModel", thingModelData, new TypeReference<List<ThingModel>>() {
            });
            initData("userInfo", userInfoData, new TypeReference<List<UserInfo>>() {
            });
            initData("virtualDevice", virtualDeviceData, new TypeReference<List<VirtualDevice>>() {
            });

            log.info("init data finished.");

            FileUtils.write(initFile, "", Charsets.UTF_8);
        } catch (Throwable e) {
            log.error("init error", e);
        }
    }

    private <T> void initData(String name, ICommonData service, TypeReference<T> type) throws IOException {
        log.info("init {} data...", name);
        String json = FileUtils.readFileToString(new File("./data/init/" + name + ".json"), Charsets.UTF_8);
        List<T> list = (List<T>) JsonUtil.parse(json, type);
        for (T obj : list) {
            service.add((Id) obj);
        }
    }


}
