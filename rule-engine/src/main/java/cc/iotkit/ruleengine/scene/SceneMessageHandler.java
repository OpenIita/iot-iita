package cc.iotkit.ruleengine.scene;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.mq.Request;
import cc.iotkit.model.mq.Response;
import cc.iotkit.ruleengine.listener.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class SceneMessageHandler {

    private Map<String, List<Scene>> topicSceneMap = new HashMap<>();
    private Map<String, Pattern> topicPatternMap = new HashMap<>();

    @Autowired
    private SceneExecutor sceneExecutor;

    public synchronized void putScene(Scene scene) {
        //先删除已经存在的场景
        removeScene(scene.getId());

        //将同一个场景中不同topic拆开存储
        for (Listener<?> listener : scene.getListeners()) {
            String topic = listener.getTopic();
            topicPatternMap.put(topic, Pattern.compile(""));
            topicSceneMap.putIfAbsent(topic, new ArrayList<>());
            List<Scene> scenes = topicSceneMap.get(topic);
            scenes.add(scene);
        }
    }

    public synchronized void removeScene(String sceneId) {
        Iterator<Map.Entry<String, List<Scene>>> iterator = topicSceneMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Scene>> entry = iterator.next();
            String topic = entry.getKey();
            //找出相同场景ID的场景删除
            entry.getValue().removeIf(s -> s != null && s.getId().equals(sceneId));
            //删除空的场景列表
            if (entry.getValue().size() == 0) {
                iterator.remove();
                topicPatternMap.remove(topic);
            }
        }
    }

    public void handler(String topic, String message) {
        topicSceneMap.forEach((t, scenes) -> {
            Pattern pattern = topicPatternMap.get(t);
            if (pattern == null) {
                return;
            }
            if (!pattern.matcher(topic).find()) {
                return;
            }

            Map<?, ?> params = new HashMap<>();
            //匹配到topic，取消息内容
            if (topic.endsWith("_reply")) {
                ResponseMsg response = JsonUtil.parse(message, ResponseMsg.class);
                params = response.getData();
            } else {
                RequestMsg request = JsonUtil.parse(message, RequestMsg.class);
                params = request.getParams();
            }

            //执行场景
            for (Scene scene : scenes) {
                sceneExecutor.execute(topic, params, scene);
            }
        });
    }

    private static class ResponseMsg extends Response<Map<?, ?>> {
    }

    private static class RequestMsg extends Request<Map<?, ?>> {

    }
}
