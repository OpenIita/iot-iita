package cc.iotkit.comps;

import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.IComponent;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ComponentClassLoader {

    protected Class<IComponent> findClass(String name) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return (Class<IComponent>) classLoader.loadClass("cc.iotkit.comp.mqtt.MqttComponent");
    }

    public void addUrl(File jarPath) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, MalformedURLException {
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        URL url = jarPath.toURI().toURL();
        method.invoke(classLoader, url);
    }
}
