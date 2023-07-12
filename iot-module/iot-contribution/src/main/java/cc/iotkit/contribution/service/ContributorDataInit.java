/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.contribution.service;

import cc.iotkit.common.utils.JsonUtils;

import cc.iotkit.contribution.data.IIotContributorData;
import cc.iotkit.contribution.data.impl.IotContributorDataImpl;
import cc.iotkit.contribution.model.IotContributor;
import cc.iotkit.data.ICommonData;
import cc.iotkit.model.Id;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
public class ContributorDataInit implements SmartInitializingSingleton {

    @Autowired
    private IIotContributorData contributorData;

    @Override
    public void afterSingletonsInstantiated() {
        //等redis实例化后再执行
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    initSysData();



                } catch (
                        Throwable e) {
                    log.error("init error", e);
                }
            }
        }, 100);

    }

    private void initSysData() throws IOException {

        initData("contributor", contributorData, new TypeReference<List<IotContributor>>() {
        });
    }

    private <T> T initData(String name, ICommonData service, TypeReference<T> type) throws IOException {
        log.info("init {} data...", name);
        if (service.count() > 0) {
            return null;
        }
        String json = FileUtils.readFileToString(new File("./data/init/" + name + ".json"), StandardCharsets.UTF_8);
        List list = (List) JsonUtils.parseObject(json, type);
        for (Object obj : list) {
            service.save((Id) obj);
        }
        return (T) list;
    }

}
