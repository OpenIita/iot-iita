package cc.iotkit.protocol.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.apache.pulsar.common.functions.FunctionConfig;

import java.util.regex.Pattern;

public class Test1 {

    public static void main(String[] args) throws PulsarClientException, InterruptedException {
//        FunctionConfig functionConfig = new FunctionConfig();
//        functionConfig.setTenant("tenant");
//        functionConfig.setNamespace("namespace");
//        functionConfig.setName("functionName");
//        functionConfig.setRuntime(FunctionConfig.Runtime.JAVA);
//        functionConfig.setParallelism(1);
//        functionConfig.setClassName("org.apache.pulsar.functions.api.examples.ExclamationFunction");
//        functionConfig.setProcessingGuarantees(FunctionConfig.ProcessingGuarantees.ATLEAST_ONCE);
//        functionConfig.setTopicsPattern(sourceTopicPattern);
//        functionConfig.setSubName(subscriptionName);
//        functionConfig.setAutoAck(true);
//        functionConfig.setOutput(sinkTopic);
//        admin.functions().createFunction(functionConfig, fileName);


        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();

        client.newConsumer(JSONSchema.of(Msg.class))
                .topicsPattern(Pattern.compile("persistent://public/default/test.*"))
                .subscriptionName("test1")
                .consumerName("test1")
                .messageListener((MessageListener<Msg>) (consumer, msg) -> {
                    Msg m = msg.getValue();
                    System.out.printf("=====received:%s,%s%n", m.getIdentifier(), m.getDeviceId());
                }).subscribe();

        Producer<Msg> producer = client.newProducer(JSONSchema.of(Msg.class))
                .topic("test1234")
                .create();

        for (int i = 0; i < 1000; i++) {
            producer.send(new Msg("test", "xxxx11222333" + i));
            Thread.sleep(500);
        }


    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Msg {

        private String identifier;

        private String deviceId;

    }

}
