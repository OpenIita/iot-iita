package cc.iotkit.protocol.server.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.protocol.function.DecodeFunction;
import cc.iotkit.protocol.server.config.ProtocolConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.functions.FunctionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GatewayService {

    private PulsarAdmin pulsarAdmin;

    @Autowired
    private ProtocolConfig serverConfig;

    private PulsarAdmin getPulsarAdmin() throws PulsarClientException {
        if (pulsarAdmin == null) {
            pulsarAdmin = PulsarAdmin.builder()
                    .serviceHttpUrl(serverConfig.getPulsarServiceUrl())
                    .build();
        }
        return pulsarAdmin;
    }

    public void saveFunction(String tenant, String gatewayId, String script,
                             String jarFile) throws PulsarClientException, PulsarAdminException {
        saveFunction(tenant, gatewayId, "default", script, jarFile);
    }

    public void saveFunction(String tenant, String gatewayId,
                             String namespace, String script,
                             String jarFile) throws PulsarClientException, PulsarAdminException {
        PulsarAdmin pulsarAdmin = getPulsarAdmin();

//        String gatewayName = "";
//        String tenant = "public";
//        String namespace = "default";
//        String inputTopicsPattern = "";
        String fullInputTopic = String.format("persistent://%s/%s/%s",
                tenant, namespace, Constants.DEVICE_RAW_MESSAGE_TOPIC);
//        String outputTopic = "";
//        String transform = "";
//        String functionClass = "cc.iotkit.protocol.function.UplinkTranslateFunction";
//        String jarFile = "/Users/sjg/home/gitee/open-source/iotkit-parent/protocol-gateway/gateway-server/fun-test/target/fun-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar";

        String functionClass = DecodeFunction.class.getName();
        String functionName = functionClass.substring(functionClass.lastIndexOf(".") + 1) + "_" + gatewayId;

        FunctionConfig functionConfig = new FunctionConfig();
        functionConfig.setTenant(tenant);
        functionConfig.setNamespace(namespace);
        functionConfig.setName(functionName);
        functionConfig.setRuntime(FunctionConfig.Runtime.JAVA);
        functionConfig.setParallelism(1);
        functionConfig.setClassName(functionClass);
        functionConfig.setProcessingGuarantees(FunctionConfig.ProcessingGuarantees.ATLEAST_ONCE);
        functionConfig.setTopicsPattern(fullInputTopic);
        functionConfig.setSubName(functionConfig.getName());
        functionConfig.setAutoAck(true);
        functionConfig.setOutput(String.format("persistent://%s/%s/%s", tenant,
                namespace, Constants.THING_MODEL_MESSAGE_TOPIC));
        log.info("creating function:{}", JsonUtil.toJsonString(functionConfig));

        Map<String, Object> userConfig = new HashMap<>();
        userConfig.put("script", script);
        userConfig.put("gateway", gatewayId);
        functionConfig.setUserConfig(userConfig);

        if (pulsarAdmin.functions().getFunctions(tenant, namespace).contains(functionName)) {
            pulsarAdmin.functions().updateFunction(functionConfig, jarFile);
        } else {
            pulsarAdmin.functions().createFunction(functionConfig, jarFile);
        }
    }

    public void deleteFunction(String tenant, String gatewayId) throws PulsarClientException, PulsarAdminException {
        String namespace = "default";
        String functionClass = DecodeFunction.class.getName();
        String functionName = functionClass.substring(functionClass.lastIndexOf(".") + 1) + "_" + gatewayId;
        PulsarAdmin pulsarAdmin = getPulsarAdmin();
        if (!pulsarAdmin.functions().getFunctions(tenant, namespace).contains(functionName)) {
            log.warn("function does not found,delete success.");
            return;
        }
        pulsarAdmin.functions().deleteFunction(tenant, namespace, functionName);
    }
}
