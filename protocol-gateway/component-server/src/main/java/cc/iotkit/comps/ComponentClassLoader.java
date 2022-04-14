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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ComponentClassLoader {
    private static final Map<String, URLClassLoader> classLoaders = new HashMap<>();

    protected static Class<IComponent> findClass(String name, String clsName) throws ClassNotFoundException {
        ClassLoader classLoader = classLoaders.get(name);
        return (Class<IComponent>) classLoader.loadClass(clsName);
    }

    private static String addUrl(String name, File jarPath) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, IOException {
        URLClassLoader classLoader = classLoaders.get(name);
        if (classLoader != null) {
            classLoader.close();
        }

        classLoader = URLClassLoader.newInstance(new URL[]{jarPath.toURI().toURL()}, ClassLoader.getSystemClassLoader());
        classLoaders.put(name, classLoader);

        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        URL url = jarPath.toURI().toURL();
        method.invoke(classLoader, url);
        InputStream is = classLoader.getResourceAsStream("component.spi");
        return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
    }

    public static IComponent getComponent(String name, File jarFile) {
        try {
            String className = addUrl(name, jarFile);
            Class<IComponent> componentClass = findClass(name, className);
            return componentClass.newInstance();
        } catch (Throwable e) {
            log.error("instance component from jar error", e);
            return null;
        }
    }

}
