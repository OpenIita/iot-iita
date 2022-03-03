package cc.iotkit.ruleengine.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ActionExecutorManager implements ApplicationContextAware {

    private static Map<String, ActionExecutor> EXECUTORS = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ActionExecutor> beans = applicationContext.getBeansOfType(ActionExecutor.class);
        beans.forEach((name, bean) -> EXECUTORS.put(bean.getName(), bean));
    }

    public ActionExecutor getExecutor(String name) {
        return EXECUTORS.get(name);
    }

    public void execute(String name, String data) {
        ActionExecutor<?> executor = getExecutor(name);
        if (executor == null) {
            log.error("action executor not found by name:{}", name);
            return;
        }

        executor.execute(data);
    }
}
