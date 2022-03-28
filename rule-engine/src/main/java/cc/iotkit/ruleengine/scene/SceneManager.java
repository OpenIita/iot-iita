package cc.iotkit.ruleengine.scene;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.DeviceCache;
import cc.iotkit.dao.SceneInfoRepository;
import cc.iotkit.model.rule.RuleAction;
import cc.iotkit.model.rule.SceneInfo;
import cc.iotkit.ruleengine.action.Action;
import cc.iotkit.ruleengine.action.DeviceAction;
import cc.iotkit.ruleengine.action.DeviceActionService;
import cc.iotkit.ruleengine.config.RuleConfiguration;
import cc.iotkit.ruleengine.filter.DeviceFilter;
import cc.iotkit.ruleengine.filter.Filter;
import cc.iotkit.ruleengine.listener.DeviceListener;
import cc.iotkit.ruleengine.listener.Listener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SceneManager {

    private Map<String, Scene> sceneMap = new HashMap<>();
    private Map<String, Set<String>> topicSceneMap = new HashMap<>();

    @Autowired
    private RuleConfiguration ruleConfiguration;

    @Autowired
    private SceneMessageHandler sceneMessageHandler;

    @Autowired
    private SceneInfoRepository sceneInfoRepository;

    @Autowired
    private DeviceCache deviceCache;

    @Autowired
    private DeviceActionService deviceActionService;

    public SceneManager() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(this::initScenes, 1, TimeUnit.SECONDS);
    }

    @SneakyThrows
    public void initScenes() {
        while (!ruleConfiguration.isReady()) {
            Thread.sleep(100);
        }

        int idx = 0;
        while (true) {
            Page<SceneInfo> scenes = sceneInfoRepository.findAll(PageRequest.of(idx,
                    1000, Sort.by(Sort.Order.desc("createAt"))));
            scenes.get().forEach(scene -> {
                try {
                    log.info("got scene {} to init", scene.getId());
                    add(scene);
                } catch (Throwable e) {
                    log.error("add scene error", e);
                }
            });
            idx++;
            if (scenes.getSize() == 0) {
                break;
            }
        }
    }

    public void add(SceneInfo sceneInfo) {
        Scene scene = parseScene(sceneInfo);
        sceneMap.put(scene.getId(), scene);
        sceneMessageHandler.putScene(scene);
        //订阅消息
        subscribe(scene);
    }

    public void remove(String sceneId) {
        Scene scene = sceneMap.get(sceneId);
        for (Listener<?> listener : scene.getListeners()) {
            String topic = listener.getTopic();
            Set<String> sceneIds = topicSceneMap.get(topic);
            sceneIds.remove(sceneId);

            if (sceneIds.size() == 0) {
                //topic没有场景关联，取消订阅
                ruleConfiguration.unsubscribeTopic(topic);
            }
        }

        sceneMap.remove(sceneId);
        sceneMessageHandler.removeScene(sceneId);
    }

    public void pause(String sceneId) {
        remove(sceneId);
    }

    public void resume(SceneInfo sceneInfo) {
        add(sceneInfo);
    }

    private void subscribe(Scene scene) {
        for (Listener<?> listener : scene.getListeners()) {
            if (listener == null) {
                return;
            }
            String topic = listener.getTopic();
            //已经订阅了，不重复订阅
            if (topicSceneMap.containsKey(topic)) {
                topicSceneMap.get(topic).add(scene.getId());
                continue;
            }
            ruleConfiguration.subscribeTopic(topic);
            Set<String> sceneIds = new HashSet<>();
            sceneIds.add(scene.getId());
            topicSceneMap.put(topic, sceneIds);
        }
    }

    private Scene parseScene(SceneInfo sceneInfo) {
        List<Listener<?>> listeners = new ArrayList<>();
        for (SceneInfo.Listener listener : sceneInfo.getListeners()) {
            listeners.add(parseListener(listener.getType(), listener.getConfig()));
        }
        List<Filter<?>> filters = new ArrayList<>();
        for (SceneInfo.Filter filter : sceneInfo.getFilters()) {
            filters.add(parseFilter(filter.getType(), filter.getConfig()));
        }
        List<Action<?>> actions = new ArrayList<>();
        for (RuleAction action : sceneInfo.getActions()) {
            actions.add(parseAction(action.getType(), action.getConfig()));
        }

        return new Scene(sceneInfo.getId(), sceneInfo.getName(), listeners, filters, actions);
    }

    private Listener<?> parseListener(String type, String config) {
        if (DeviceListener.TYPE.equals(type)) {
            return parse(config, DeviceListener.class);
        }
        return null;
    }

    private Filter<?> parseFilter(String type, String config) {
        if (DeviceFilter.TYPE.equals(type)) {
            DeviceFilter filter = parse(config, DeviceFilter.class);
            filter.setDeviceCache(deviceCache);
            return filter;
        }
        return null;
    }

    private Action<?> parseAction(String type, String config) {
        if (DeviceAction.TYPE.equals(type)) {
            DeviceAction action = parse(config, DeviceAction.class);
            action.setDeviceActionService(deviceActionService);
            return action;
        }
        return null;
    }

    private <T> T parse(String config, Class<T> cls) {
        return JsonUtil.parse(config, cls);
    }

}
