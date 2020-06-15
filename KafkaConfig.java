package com.bas.config;

import java.util.Properties;

public class KafkaConfig {

    public String topic;
    public Properties properties;

    public KafkaConfig() {
    }

    public KafkaConfig(String topic, Properties properties) {
        this.topic = topic;
        this.properties = properties;
    }
}
