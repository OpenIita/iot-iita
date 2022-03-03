package cc.iotkit.ruleengine.scene;

import cc.iotkit.dao.SceneLogRepository;
import cc.iotkit.model.rule.SceneLog;
import cc.iotkit.ruleengine.action.Action;
import cc.iotkit.ruleengine.filter.Filter;
import cc.iotkit.ruleengine.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 场景执行器
 */
@Component
@Slf4j
public class SceneExecutor {

    @Autowired
    private SceneLogRepository sceneLogRepository;

    public void execute(String topic, Map<?, ?> params, Scene scene) {
        if (!doListeners(topic, params, scene)) {
            log.info("The listener did not match the appropriate content,scene:{},{}", scene.getId(), scene.getName());
            return;
        }
        log.info("Start execute scene {},id:{}", scene.getName(), scene.getId());

        SceneLog sceneLog = new SceneLog();
        sceneLog.setId(UUID.randomUUID().toString());
        sceneLog.setSceneId(scene.getId());
        sceneLog.setState(SceneLog.STATE_MATCHED_LISTENER);

        try {
            if (!doFilters(scene)) {
                sceneLog.setState(SceneLog.STATE_UNMATCHED_FILTER);
                log.info("The filter did not match the appropriate content,scene:{},{}", scene.getId(), scene.getName());
                return;
            }
            sceneLog.setState(SceneLog.STATE_MATCHED_FILTER);

            doActions(scene);
            sceneLog.setState(SceneLog.STATE_EXECUTED_ACTION);
            sceneLog.setSuccess(true);
            log.info("Scene execution completed,id:{}", scene.getId());
        } catch (Throwable e) {
            log.error("Scene execution error,id:" + scene.getId(), e);
            sceneLog.setSuccess(false);
            sceneLog.setContent(e.getMessage());
        } finally {
            sceneLog.setLogAt(System.currentTimeMillis());
            sceneLogRepository.save(sceneLog);
        }
    }

    private boolean doListeners(String topic, Map<?, ?> params, Scene scene) {
        List<Listener<?>> listeners = scene.getListeners();
        for (Listener<?> listener : listeners) {
            if (listener.execute(topic, params)) {
                //只要有一个监听器匹配到数据即可
                return true;
            }
        }
        return false;
    }

    private boolean doFilters(Scene scene) {
        List<Filter<?>> filters = scene.getFilters();
        for (Filter<?> filter : filters) {
            //只要有一个过滤器未通过都不算通过
            if (!filter.execute()) {
                return false;
            }
        }
        return true;
    }

    private void doActions(Scene scene) {
        for (Action<?> action : scene.getActions()) {
            action.execute();
        }
    }

}
