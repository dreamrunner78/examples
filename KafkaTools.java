package com.bas.tools;


import com.bas.config.KafkaConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class KafkaTools {


    public static KafkaConfig getKafkaProducerProperties(String kafkaproperties, String configname) {
        KafkaConfig kafkaConfig = new KafkaConfig();
        Properties properties = new Properties();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject rootObject = (JSONObject) jsonParser.parse(new FileReader(kafkaproperties));

            JSONObject config = (JSONObject) rootObject.get(configname);
            String topicname = (String) config.get("topic");
            Boolean avro = (Boolean) config.get("avro");

            JSONObject kafkaconfigs = (JSONObject) config.get("kafkaconfigs");
            for(Object key: kafkaconfigs.keySet()) {
                properties.put(key, kafkaconfigs.get(key));
            }
            if(avro) {
                //properties.put("key.serializer", KafkaAvroSerializer.class.getName());
                //properties.put("value.serializer", KafkaAvroSerializer.class.getName());
            }
            else {
                if(properties.get("key.serializer") == null) {
                    properties.put("key.serializer", StringSerializer.class.getName());
                }
                if(properties.get("value.serializer") == null) {
                    properties.put("value.serializer", StringSerializer.class.getName());
                }
            }
            kafkaConfig.topic = topicname;
            kafkaConfig.properties = properties;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kafkaConfig;
    }

    public static KafkaConfig getKafkaProducerPropertiesForFlink(String kafkaproperties, String configname) {
        KafkaConfig kafkaConfig = new KafkaConfig();
        Properties properties = new Properties();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject rootObject = (JSONObject) jsonParser.parse(new FileReader(kafkaproperties));

            JSONObject config = (JSONObject) rootObject.get(configname);
            String topicname = (String) config.get("topic");
            Boolean avro = (Boolean) config.get("avro");

            JSONObject kafkaconfigs = (JSONObject) config.get("kafkaconfigs");
            for(Object key: kafkaconfigs.keySet()) {
                properties.put(key, kafkaconfigs.get(key));
            }

            kafkaConfig.topic = topicname;
            kafkaConfig.properties = properties;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kafkaConfig;
    }


    public static KafkaConfig getKafkaConsumerProperties(String kafkaproperties, String configname) {
        KafkaConfig kafkaConfig = new KafkaConfig();
        Properties properties = new Properties();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject rootObject = (JSONObject) jsonParser.parse(new FileReader(kafkaproperties));

            JSONObject config = (JSONObject) rootObject.get(configname);
            String topicname = (String) config.get("topic");
            Boolean avro = (Boolean) config.get("avro");

            JSONObject kafkaconfigs = (JSONObject) config.get("kafkaconfigs");
            for(Object key: kafkaconfigs.keySet()) {
                properties.put(key, kafkaconfigs.get(key));
            }
            if(avro) {
                //properties.put("key.deserializer", KafkaAvroDeserializer.class.getName());
                //properties.put("value.deserializer", KafkaAvroDeserializer.class.getName());
            }
            else {
                if(properties.get("key.deserializer") == null) {
                    properties.put("key.deserializer", StringDeserializer.class.getName());
                }
                if(properties.get("value.deserializer") == null) {
                    properties.put("value.deserializer", StringDeserializer.class.getName());
                }
            }
            kafkaConfig.topic = topicname;
            kafkaConfig.properties = properties;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kafkaConfig;
    }

    public static <K,V> KafkaProducer<K,V> createKafkaProducer(KafkaConfig kafkaConfig) {
        KafkaProducer<K, V> producer = new KafkaProducer<K, V>(kafkaConfig.properties);
        return producer;
    }

    public static <K,V> KafkaConsumer<K,V> createKafkaConsumer(KafkaConfig kafkaConfig) {
        KafkaConsumer<K, V> consumer = new KafkaConsumer<K, V>(kafkaConfig.properties);
        return consumer;
    }

    public static <K,V> void publishToKafka(KafkaProducer<K,V> producer, String topicname, V record) {
        ProducerRecord<K,V> producerRecord = new ProducerRecord<K,V>(topicname, record);
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e == null) {

                }
                else {
                    e.printStackTrace();
                }

            }
        });
        producer.flush();
    }



}
