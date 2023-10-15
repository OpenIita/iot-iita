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

import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.data.ICommonData;
import cc.iotkit.data.manager.*;
import cc.iotkit.data.system.*;
import cc.iotkit.model.Id;
import cc.iotkit.model.OauthClient;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.device.DeviceGroup;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.VirtualDevice;
import cc.iotkit.model.notify.Channel;
import cc.iotkit.model.notify.ChannelConfig;
import cc.iotkit.model.notify.ChannelTemplate;
import cc.iotkit.model.notify.NotifyMessage;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ProductModel;
import cc.iotkit.model.product.ThingModel;
import cc.iotkit.model.rule.RuleInfo;
import cc.iotkit.model.rule.TaskInfo;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.space.Space;
import cc.iotkit.model.space.SpaceDevice;
import cc.iotkit.model.system.*;
import cc.iotkit.temporal.IDbStructureData;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
public class ExampleDataInit implements SmartInitializingSingleton {

    @Value("${iita.init.data:true}")
    private boolean initDataFlg;

    @Autowired
    private IOauthClientData oauthClientData;
    @Autowired
    private ICategoryData categoryData;
    @Autowired
    private IDeviceGroupData deviceGroupData;
    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;
    @Autowired
    private IHomeData homeData;
    @Autowired
    private IProductData productData;
    @Autowired
    private IProductModelData productModelData;
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
    private IDbStructureData dbStructureData;
    @Autowired
    private IChannelData iChannelData;
    @Autowired
    private IChannelConfigData iChannelConfigData;
    @Autowired
    private IChannelTemplateData iChannelTemplateData;
    @Autowired
    private INotifyMessageData iNotifyMessageData;
    @Autowired
    private ISysDeptData sysDeptData;

    @Autowired
    private ISysMenuData sysMenuData;

    @Autowired
    private ISysPostData sysPostData;

    @Autowired
    private ISysRoleData sysRoleData;

    @Autowired
    private ISysUserData sysUserData;

    @Autowired
    private ISysTenantData sysTenantData;

    @Autowired
    private ISysConfigData sysConfigData;

    @Autowired
    private ISysDictData sysDictData;

    @Autowired
    private ISysDictTypeData sysDictTypeData;

    @Autowired
    private ISysLogininforData sysLogininforData;

    @Autowired
    private ISysNoticeData sysNoticeData;

    @Autowired
    private ISysOperLogData sysOperLogData;

    @Autowired
    private ISysOssData sysOssData;

    @Autowired
    private ISysOssConfigData sysOssConfigData;

    @Autowired
    private ISysRoleDeptData sysRoleDeptData;

    @Autowired
    private ISysRoleMenuData sysRoleMenuData;

    @Autowired
    private ISysTenantPackageData sysTenantPackageData;

    @Autowired
    private ISysUserPostData sysUserPostData;

    @Autowired
    private ISysUserRoleData sysUserRoleData;

    @Autowired
    private ISysAppData sysAppData;

    @Override
    public void afterSingletonsInstantiated() {
        //等redis实例化后再执行
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    File initFile = new File(".init");
                    if (initFile.exists()) {
                        return;
                    }

                    if (!initDataFlg) {
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
                    initData("ruleInfo", ruleInfoData, new TypeReference<List<RuleInfo>>() {
                    });
                    initData("space", spaceData, new TypeReference<List<Space>>() {
                    });
                    initData("spaceDevice", spaceDeviceData, new TypeReference<List<SpaceDevice>>() {
                    });
                    initData("taskInfo", taskInfoData, new TypeReference<List<TaskInfo>>() {
                    });
                    List<ThingModel> thingModels = initData("thingModel", thingModelData, new TypeReference<>() {
                    });
                    //初始化物模型时序数据结构
                    for (ThingModel thingModel : thingModels) {
                        dbStructureData.defineThingModel(thingModel);
                    }

                    initData("userInfo", userInfoData, new TypeReference<List<UserInfo>>() {
                    });
                    initData("virtualDevice", virtualDeviceData, new TypeReference<List<VirtualDevice>>() {
                    });
                    initData("channel", iChannelData, new TypeReference<List<Channel>>() {
                    });
                    initData("channelConfig", iChannelConfigData, new TypeReference<List<ChannelConfig>>() {
                    });
                    initData("channelTemplate", iChannelTemplateData, new TypeReference<List<ChannelTemplate>>() {
                    });
                    initData("notifyMessage", iNotifyMessageData, new TypeReference<List<NotifyMessage>>() {
                    });

                    initSysData();

                    log.info("init data finished.");

                    FileUtils.write(initFile, "", StandardCharsets.UTF_8);
                } catch (
                        Throwable e) {
                    log.error("init error", e);
                }
            }
        }, 100);

    }

    private void initSysData() throws IOException {
        initData("sys_config", sysConfigData, new TypeReference<List<SysConfig>>() {
        });

        initData("sys_dept", sysDeptData, new TypeReference<List<SysDept>>() {
        });

        initData("sys_dict_data", sysDictData, new TypeReference<List<SysDictData>>() {
        });

        initData("sys_dict_type", sysDictTypeData, new TypeReference<List<SysDictType>>() {
        });

        initData("sys_logininfor", sysLogininforData, new TypeReference<List<SysLoginInfo>>() {
        });
        initData("sys_menu", sysMenuData, new TypeReference<List<SysMenu>>() {
        });

        initData("sys_notice", sysNoticeData, new TypeReference<List<SysNotice>>() {
        });

        initData("sys_oper_log", sysOperLogData, new TypeReference<List<SysOperLog>>() {
        });

        initData("sys_oss", sysOssData, new TypeReference<List<SysOss>>() {
        });

        initData("sys_oss_config", sysOssConfigData, new TypeReference<List<SysOssConfig>>() {
        });

        initData("sys_post", sysPostData, new TypeReference<List<SysPost>>() {
        });
        initData("sys_role", sysRoleData, new TypeReference<List<SysRole>>() {
        });

        initData("sys_role_dept", sysRoleDeptData, new TypeReference<List<SysRoleDept>>() {
        });

        initData("sys_role_menu", sysRoleMenuData, new TypeReference<List<SysRoleMenu>>() {
        });

        initData("sys_tenant", sysTenantData, new TypeReference<List<SysTenant>>() {
        });

        initData("sys_tenant_package", sysTenantPackageData, new TypeReference<List<SysTenantPackage>>() {
        });

        initData("sys_user", sysUserData, new TypeReference<List<SysUser>>() {
        });

        initData("sys_user_post", sysUserPostData, new TypeReference<List<SysUserPost>>() {
        });

        initData("sys_user_role", sysUserRoleData, new TypeReference<List<SysUserRole>>() {
        });

        initData("sys_app", sysAppData, new TypeReference<List<SysApp>>() {
        });
    }

    private <T> T initData(String name, ICommonData service, TypeReference<T> type) {
        try {
            log.info("init {} data...", name);
            if (service.count() > 0) {
                new RuntimeException("原数据库已存在" + name + "的旧数据，请清除后再重新初始化！").printStackTrace();
                System.exit(0);
            }
            String json = FileUtils.readFileToString(new File("./data/init/" + name + ".json"), StandardCharsets.UTF_8);
            List list = (List) JsonUtils.parseObject(json, type);
            for (Object obj : list) {
                service.save((Id) obj);
            }
            return (T) list;
        } catch (Exception e) {
            log.error("initData error", e);
            return null;
        }
    }

}
