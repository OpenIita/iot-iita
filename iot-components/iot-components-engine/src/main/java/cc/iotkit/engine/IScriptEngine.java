package cc.iotkit.engine;

public interface IScriptEngine {

    void setScript(String key) throws  IScriptException;

    void putScriptEnv(String key, Object val);

    Object invokeMethod(String methodName, Object ...args) throws IScriptException;


}
