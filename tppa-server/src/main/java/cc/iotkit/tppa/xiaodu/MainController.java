package cc.iotkit.tppa.xiaodu;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.tppa.xiaodu.handler.IRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/xiaodu")
public class MainController implements ApplicationContextAware {

    private Pattern requestNamePattern = Pattern.compile(".*\"name\":\"(\\w+)\".*");
    private Map<String, IRequestHandler> handlerMap = new HashMap<>();


    @PostMapping("/receive")
    public Object receive(@RequestBody String msg) {
        log.info("receive msg:{}", msg);
        Matcher matcher = requestNamePattern.matcher(msg);
        if (!matcher.matches()) {
            return null;
        }

        String name = matcher.group(1);
        IRequestHandler handler = handlerMap.get(name);
        if (handler == null) {
            return null;
        }
        Object response = handler.handle(JsonUtil.parse(msg, handler.getRequestType()));
        log.info("response data:{}", JsonUtil.toJsonString(response));
        return response;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(IRequestHandler.class).forEach((name, handler) ->
                handlerMap.put(handler.getName(), handler));

    }
}
