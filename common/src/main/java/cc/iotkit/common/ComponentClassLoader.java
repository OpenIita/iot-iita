package cc.iotkit.common;

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

import org.apache.commons.io.IOUtils;

public class ComponentClassLoader {
    private static final Map<String, URLClassLoader> classLoaders = new HashMap<>();

    protected static <T> Class<T> findClass(String name, String clsName) throws ClassNotFoundException {
        ClassLoader classLoader = classLoaders.get(name);
        return (Class<T>) classLoader.loadClass(clsName);
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
        if (!method.canAccess(classLoader)) {
            method.setAccessible(true);
        }

        URL url = jarPath.toURI().toURL();
        method.invoke(classLoader, url);
        InputStream is = classLoader.getResourceAsStream("component.spi");
        if (is == null) {
            return null;
        }

        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    public static <T> T getComponent(String name, File jarFile) throws Exception {
        String className = addUrl(name, jarFile);
        Class<T> componentClass = findClass(name, className);
        return componentClass.newInstance();
    }

}
