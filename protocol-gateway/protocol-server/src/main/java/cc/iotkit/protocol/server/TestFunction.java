package cc.iotkit.protocol.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.apache.pulsar.common.functions.FunctionConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TestFunction {

    public static void main(String[] args) throws PulsarClientException, InterruptedException, PulsarAdminException {

        PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl("http://192.168.0.112:8080")
                .build();

        String gatewayName = "test3";
        String tenant = "public";
        String namespace = "default";
        String inputTopicsPattern = "device_raw";
        String fullInputTopic = String.format("persistent://%s/%s/%s", tenant, namespace, inputTopicsPattern);
        String outputTopic = "device_thing";
        String transform = "new (function() {\n    this.decode=function(msg){\n        //对msg进行解析，并返回物模型数据\n        var mqttMsg=JSON.parse(msg.content);\n        var topic=mqttMsg.topic;\n        var payload=mqttMsg.payload;\n\n        if(topic.endWith(\"/property/post\")){\n            //属性上报\n            return {\n                \"mid\":msg.mid,\n                \"productKey\":msg.productKey,    //可根据消息内容判断填写不同产品\n                \"deviceName\":msg.deviceName,\n                \"identifier\":\"propertyReport\",  //属性上报\n                \"occur\":new Date().getTime(),   //时间戳，设备上的事件或数据产生的本地时间\n                \"time\":new Date().getTime(),    //时间戳，消息上报时间\n                \"data\":payload.params\n            }\n        }else if(topic.indexOf(\"/event/\")>0){\n            var identifier=topic.substring(topic.lastIndexOf(\"/\")+1);\n            //事件上报\n            return {\n                \"mid\":msg.mid,\n                \"productKey\":msg.productKey,\n                \"deviceName\":msg.deviceName,\n                \"identifier\":identifier,\n                \"occur\":new Date().getTime(),\n                \"time\":new Date().getTime(),\n                \"data\":payload.params\n            }\n        }else if(topic.endWith(\"_reply\")){\n            var identifier=topic.substring(topic.lastIndexOf(\"/\")+1);\n            //服务回复\n            return {\n                \"mid\":msg.mid,\n                \"productKey\":msg.productKey,\n                \"deviceName\":msg.deviceName,\n                \"identifier\":identifier.replace(\"_reply\",\"Reply\"),\n                \"occur\":new Date().getTime(),\n                \"time\":new Date().getTime(),\n                \"code\":payload.code,\n                \"data\":payload.data\n            } \n        }\n        return null;\n    }\n\n})().decode(msg)";
        String functionClass = "cc.iotkit.protocol.function.UplinkTranslateFunction";
        String jarFile = "/Users/sjg/home/gitee/open-source/iotkit-parent/protocol-gateway/protocol-function/target/protocol-function-0.0.1-SNAPSHOT-jar-with-dependencies.jar";

        FunctionConfig functionConfig = new FunctionConfig();
        functionConfig.setTenant(tenant);
        functionConfig.setNamespace(namespace);
        functionConfig.setName("UplinkTranslateFunction_" + gatewayName);
        functionConfig.setRuntime(FunctionConfig.Runtime.JAVA);
        functionConfig.setParallelism(1);
        functionConfig.setClassName(functionClass);
        functionConfig.setProcessingGuarantees(FunctionConfig.ProcessingGuarantees.ATLEAST_ONCE);
        functionConfig.setTopicsPattern(fullInputTopic);
        functionConfig.setSubName(functionConfig.getName());
        functionConfig.setAutoAck(true);
        functionConfig.setOutput(String.format("persistent://%s/%s/%s", tenant, namespace, outputTopic));
        Map<String, Object> userConfig = new HashMap<>();
        userConfig.put("script", transform);
        functionConfig.setUserConfig(userConfig);
        for (String function : admin.functions().getFunctions(tenant, namespace)) {
            System.out.println(function);
            if (function.contains("test")) {
                admin.functions().deleteFunction(tenant, namespace, function);
            }
        }
//        if (admin.functions().getFunction(tenant, namespace, functionConfig.getName()) != null) {
//            admin.functions().updateFunction(functionConfig, jarFile);
//        } else {
//            admin.functions().createFunction(functionConfig, jarFile);
//        }

//        admin.functions().stopFunction("public", "default", "fun_4", 0);
//        admin.functions().startFunction("public", "default", "fun_4", 0);

//
//        PulsarClient client = PulsarClient.builder()
//                .serviceUrl("pulsar://localhost:6650")
//                .build();
//
//        client.newConsumer()
//                .topicsPattern(Pattern.compile("persistent://public/default/fun_out.*"))
//                .subscriptionName("test1")
//                .consumerName("test-fun-1")
//                .messageListener((MessageListener<byte[]>) (consumer, msg) -> {
//                    try {
//                        System.out.printf("==============received:%s\n", new String(msg.getValue()));
//                        consumer.acknowledge(msg);
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                    }
//                }).subscribe();


    }


}
