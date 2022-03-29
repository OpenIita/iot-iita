package cc.iotkit.comps;

import cc.iotkit.comp.IComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;

@Slf4j
public class ComponentClassLoader {

    protected Class<IComponent> findClass(String name) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return (Class<IComponent>) classLoader.loadClass(name);
    }

    private String addUrl(File jarPath) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, IOException {
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        URL url = jarPath.toURI().toURL();
        method.invoke(classLoader, url);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is = loader.getResourceAsStream("component.spi");
        return StreamUtils.copyToString(is, Charset.forName("UTF-8"));
    }

    public IComponent getComponent(File jarPath) {
        try {
            String className = addUrl(jarPath);
            Class<IComponent> componentClass = findClass(className);
            return componentClass.newInstance();
        } catch (Throwable e) {
            log.error("instance component from jar error", e);
            return null;
        }
    }

}
