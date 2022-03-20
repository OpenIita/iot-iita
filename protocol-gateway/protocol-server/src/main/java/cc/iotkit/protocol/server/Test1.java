package cc.iotkit.protocol.server;

import cc.iotkit.protocol.DeviceMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.apache.pulsar.common.functions.FunctionConfig;

import java.util.Set;
import java.util.regex.Pattern;

public class Test1 {

    public static void main(String[] args) throws PulsarClientException, InterruptedException, PulsarAdminException {

//        PulsarAdmin admin = PulsarAdmin.builder()
//                .serviceHttpUrl("http://localhost:8080")
//                .build();
//
//        FunctionConfig functionConfig = new FunctionConfig();
//        functionConfig.setTenant("tenant");
//        functionConfig.setNamespace("namespace");
//        functionConfig.setName("functionName");
//        functionConfig.setRuntime(FunctionConfig.Runtime.JAVA);
//        functionConfig.setParallelism(1);
//        functionConfig.setClassName("org.apache.pulsar.functions.api.examples.ExclamationFunction");
//        functionConfig.setProcessingGuarantees(FunctionConfig.ProcessingGuarantees.ATLEAST_ONCE);
//        functionConfig.setTopicsPattern("persistent://public/default/test.*");
//        functionConfig.setSubName("fun-test1");
//        functionConfig.setAutoAck(true);
//        functionConfig.setOutput("persistent://public/default/fun_out");
//        admin.functions().createFunction(functionConfig, "/examples/api-examples.jar");


        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://192.168.0.112:6650")
                .build();

        client.newConsumer(JSONSchema.of(DeviceMessage.class))
                .topicsPattern(Pattern.compile("persistent://public/default/device_raw"))
                .subscriptionName("test1")
                .consumerName("test1")
                .messageListener((MessageListener<DeviceMessage>) (consumer, msg) -> {
                    try {
                        DeviceMessage m = msg.getValue();
                        System.out.printf("==============received:%s,%s%n", m.getMid(), m.getContent());
                        consumer.acknowledge(msg);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }).subscribe();


//        for (int i = 0; i < 3; i++) {
//            Producer<Msg> producer = client.newProducer(JSONSchema.of(Msg.class))
//                    .topic("test_" + i)
//                    .create();
//            for (int j = 0; j < 10; j++) {
//                producer.newMessage()
//                        .value(new Msg("QQQQQ" + i, "id_test_" + i + "_" + j))
//                        .property("aa", "1")
//                        .send();
//            }
//            Thread.sleep(100);
//        }


    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Msg {

        private String identifier;

        private String deviceId;

    }

}
