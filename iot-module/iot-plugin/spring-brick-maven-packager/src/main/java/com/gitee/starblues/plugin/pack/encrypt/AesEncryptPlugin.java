package com.gitee.starblues.plugin.pack.encrypt;

import com.gitee.starblues.common.cipher.AbstractPluginCipher;
import com.gitee.starblues.common.cipher.AesPluginCipher;
import com.gitee.starblues.plugin.pack.PluginInfo;
import com.gitee.starblues.utils.ObjectUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.HashMap;
import java.util.Map;

/**
 * rsa 加密者
 *
 * @author starBlues
 * @since 3.0.1
 * @version 3.0.1
 */
public class AesEncryptPlugin implements EncryptPlugin{


    @Override
    public PluginInfo encrypt(EncryptConfig encryptConfig, PluginInfo pluginInfo) throws Exception{
        AesConfig aesConfig = encryptConfig.getAes();
        if(aesConfig == null){
            return null;
        }

        String secretKey = aesConfig.getSecretKey();
        if(ObjectUtils.isEmpty(secretKey)){
            throw new MojoExecutionException("encryptConfig.aes.secretKey can't be empty");
        }
        AbstractPluginCipher pluginCipher = new AesPluginCipher();
        Map<String, Object> params = new HashMap<>();
        params.put(AesPluginCipher.SECRET_KEY, secretKey);
        pluginCipher.initParams(params);

        String bootstrapClass = pluginInfo.getBootstrapClass();
        String encrypt = pluginCipher.encrypt(bootstrapClass);
        pluginInfo.setBootstrapClass(encrypt);
        return pluginInfo;
    }
}
