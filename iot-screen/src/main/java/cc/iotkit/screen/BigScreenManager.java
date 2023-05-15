package cc.iotkit.screen;

import cc.iotkit.comps.ApiTool;
import cc.iotkit.data.IBigScreenApiData;
import cc.iotkit.model.screen.BigScreen;
import cc.iotkit.model.screen.BigScreenApi;
import cc.iotkit.screen.api.ScreenApiHandle;
import cc.iotkit.screen.config.BigScreenConfig;
import cc.iotkit.screen.staticres.ScreenComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：tfd
 * @Date：2023/5/8 14:27
 */
@Slf4j
@Component
public class BigScreenManager {

    @Autowired
    private IBigScreenApiData bigScreenApiData;

    @Autowired
    private BigScreenConfig bigScreenConfig;

    private final Map<String, ScreenComponent> screens = new HashMap<>();
    private final Map<String, Boolean> states = new HashMap<>();

    public void register(BigScreen screen) {
        String id = screen.getId();
        if (screens.containsKey(id)) {
            return;
        }
        ScreenComponent screenComponent=new ScreenComponent();
        String[] pathNames=screen.getResourceFile().split("\\.");
        screenComponent.create(screen.getPort(),pathNames.length>1?pathNames[0]:"",bigScreenConfig);
        screens.put(id,screenComponent);
    }

    public void publish(BigScreen screen) {
        String id = screen.getId();
        ScreenComponent screenComponent = screens.get(id);
        if (screenComponent == null) {
            return;
        }
        ScreenApiHandle screenApiHandle=new ScreenApiHandle(id,bigScreenApiData.findByScreenId(id));
        screenApiHandle.putScriptEnv("apiTool", new ApiTool());
        screenComponent.setApiHandle(screenApiHandle);
        screenComponent.publish();
        states.put(id, true);
    }

    public void unpublish(BigScreen screen) {
        String id = screen.getId();
        ScreenComponent screenComponent = screens.get(id);
        if (screenComponent == null) {
            return;
        }
        screens.remove(id);
        states.remove(id);
        screenComponent.unpublish();
    }

    public void previewApis(BigScreen screen,List<BigScreenApi> screenApis) {
        String id = screen.getId();
        ScreenComponent screenComponent = screens.get(id);
        if (screenComponent == null) {
            return;
        }
        screenComponent.previewApis(screenApis);
    }

    public List<BigScreenApi> getScreenApis(BigScreen screen) {
        String id = screen.getId();
        ScreenComponent screenComponent = screens.get(id);
        if (screenComponent == null) {
            return null;
        }
        return screenComponent.getScreenApis();
    }

    public void debugMode(BigScreen screen,boolean state) {
        String id = screen.getId();
        ScreenComponent screenComponent = screens.get(id);
        if (screenComponent == null) {
            return;
        }
        screenComponent.debugMode(state);
    }

    public boolean isRunning(String id) {
        return states.containsKey(id) && states.get(id);
    }

}
