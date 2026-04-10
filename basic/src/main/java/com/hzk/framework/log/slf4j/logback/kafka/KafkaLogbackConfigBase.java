package com.hzk.framework.log.slf4j.logback.kafka;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.spi.DeferredProcessingAware;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.SaslConfigs;

import java.util.Properties;

import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_TYPE_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;

/**
 * 璇诲彇logback閰嶇疆鏂囦欢涓璳afka鐨勯厤缃」
 * 濡備笅绀轰緥:
 * <appender name="stdot_json" class="com.pan.log.logback.KafkaAppender">
 * <topic>ierp-log-topic</topic>
 * <brokerList>172.18.2.131:9092</brokerList>
 * <compressionType>none</compressionType>
 * <syncSend>false</syncSend>
 * <keySerializerClass>org.apache.kafka.common.serialization.StringSerializer</keySerializerClass>
 * <valueSerializerClass>org.apache.kafka.common.serialization.ByteArraySerializer</valueSerializerClass>
 * </appender>
 * <p>Title: KafkaLogbackConfigBase</p>
 * <p>Description: </p>
 * <p>Company:Kingdee.com </p>
 *
 * @param <I>
 * @author rd_fuxiong_pan
 * @date 2017骞�4鏈�11鏃� 涓嬪崍2:59:35
 */
public abstract class KafkaLogbackConfigBase<I extends DeferredProcessingAware>
        extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static final String BOOTSTRAP_SERVERS_CONFIG = ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
    private static final String COMPRESSION_TYPE_CONFIG = ProducerConfig.COMPRESSION_TYPE_CONFIG;
    private static final String ACKS_CONFIG = ProducerConfig.ACKS_CONFIG;
    private static final String RETRIES_CONFIG = ProducerConfig.RETRIES_CONFIG;
    private static final String KEY_SERIALIZER_CLASS_CONFIG = ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
    private static final String VALUE_SERIALIZER_CLASS_CONFIG = ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
    private static final String SECURITY_PROTOCOL = CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
    private static final String SSL_TRUSTSTORE_LOCATION = SSL_TRUSTSTORE_LOCATION_CONFIG;
    private static final String SSL_TRUSTSTORE_PASSWORD = SSL_TRUSTSTORE_PASSWORD_CONFIG;
    private static final String SSL_KEYSTORE_TYPE = SSL_KEYSTORE_TYPE_CONFIG;
    private static final String SSL_KEYSTORE_LOCATION = SSL_KEYSTORE_LOCATION_CONFIG;
    private static final String SSL_KEYSTORE_PASSWORD = SSL_KEYSTORE_PASSWORD_CONFIG;
    private static final String SASL_JAAS_CONFIG = SaslConfigs.SASL_JAAS_CONFIG;
    private static final String SASL_MECHANISM = SaslConfigs.SASL_MECHANISM;
    private static final String ENABLE_IDEMPOTENCE = ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG;

    protected String brokerList = null;
    protected String topic = null;
    protected String compressionType = null;
    protected String keySerializerClass = null;
    protected String valueSerializerClass = null;
    protected String securityProtocol = null;
    protected String sslTruststoreLocation = null;
    protected String sslTruststorePassword = null;
    protected String sslKeystoreType = null;
    protected String sslKeystoreLocation = null;
    protected String sslKeystorePassword = null;
    protected String clientJaasConfPath = null;
    protected String kerb5ConfPath = null;
    protected String saslMechanism = null;
    protected String enableIdempotence = null;

    protected int retries = 0;
    protected int requiredNumAcks = Integer.MAX_VALUE;
    protected boolean syncSend = false;

    protected Encoder<ILoggingEvent> encoder;

    protected String userName = null;
    protected String password = null;

    protected Properties getProducerProperties() {
        // check for config parameter validity
        Properties props = new Properties();

        if (brokerList != null) {
            props.put(BOOTSTRAP_SERVERS_CONFIG, brokerList);
            System.setProperty(BOOTSTRAP_SERVERS_CONFIG, brokerList); //系统中其他kafka，默认用此brokerList
        }
        if (props.isEmpty()) {
            throw new ConfigException("The bootstrap servers property should be specified");
        }
        if (topic == null) {
            throw new ConfigException("Topic must be specified by the Kafka Logback appender");
        }
        if (compressionType != null) {
            props.put(COMPRESSION_TYPE_CONFIG, compressionType);
        }
        if (requiredNumAcks != Integer.MAX_VALUE) {
            props.put(ACKS_CONFIG, Integer.toString(requiredNumAcks));
        }
        if (retries > 0) {
            props.put(RETRIES_CONFIG, Integer.toString(retries));
        }
        if (securityProtocol != null) {
            props.put(SECURITY_PROTOCOL, securityProtocol);
            if ( securityProtocol.contains("SSL") && sslTruststoreLocation != null
                    && sslTruststorePassword != null) {
                props.put(SSL_TRUSTSTORE_LOCATION, sslTruststoreLocation);
                props.put(SSL_TRUSTSTORE_PASSWORD, sslTruststorePassword);

                if (sslKeystoreType != null && sslKeystoreLocation != null && sslKeystorePassword != null) {
                    props.put(SSL_KEYSTORE_TYPE, sslKeystoreType);
                    props.put(SSL_KEYSTORE_LOCATION, sslKeystoreLocation);
                    props.put(SSL_KEYSTORE_PASSWORD, sslKeystorePassword);
                }
            } else if ("SASL_PLAINTEXT".equals(securityProtocol)) {

                String config = getKafkaAuthConfig(userName, password);
                String mechanism = saslMechanism == null ? "PLAIN" : saslMechanism;

                props.put(SASL_MECHANISM, mechanism);
                props.put(SECURITY_PROTOCOL, securityProtocol);
                props.put(SASL_JAAS_CONFIG, config);
            } else if (securityProtocol.equals("SSL") && sslTruststoreLocation != null) {
                props.put(SSL_TRUSTSTORE_LOCATION, sslTruststoreLocation);
            }
        }
//        if (securityProtocol != null && securityProtocol.contains("SSL") && sslTruststoreLocation != null
//                && sslTruststorePassword != null) {
//            props.put(SSL_TRUSTSTORE_LOCATION, sslTruststoreLocation);
//            props.put(SSL_TRUSTSTORE_PASSWORD, sslTruststorePassword);
//
//            if (sslKeystoreType != null && sslKeystoreLocation != null && sslKeystorePassword != null) {
//                props.put(SSL_KEYSTORE_TYPE, sslKeystoreType);
//                props.put(SSL_KEYSTORE_LOCATION, sslKeystoreLocation);
//                props.put(SSL_KEYSTORE_PASSWORD, sslKeystorePassword);
//            }
//        } else if (securityProtocol != null && "SASL_PLAINTEXT".equals(securityProtocol)) {
//
//            String config = getKafkaAuthConfig(userName, password);
//            String mechanism = saslMechanism == null ? "PLAIN" : saslMechanism;
//
//            props.put(SASL_MECHANISM, mechanism);
//            props.put(SECURITY_PROTOCOL, securityProtocol);
//            props.put(SASL_JAAS_CONFIG, config);
//        } else if (securityProtocol != null && securityProtocol.equals("SSL") && sslTruststoreLocation != null) {
//            props.put(SSL_TRUSTSTORE_LOCATION, sslTruststoreLocation);
//        }

        if (keySerializerClass != null) {
            props.put(KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
        } else {
            props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        }
        if (valueSerializerClass != null) {
            props.put(VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);
        } else {
            props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        }
        if(enableIdempotence != null){
            props.put(ENABLE_IDEMPOTENCE, enableIdempotence);
        }
        if(enableIdempotence != null){
            props.put(ENABLE_IDEMPOTENCE, enableIdempotence);
        }
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 10000);
        return props;
    }

    private static String getKafkaAuthConfig(String userName, String pw) {
        if (StringUtils.isEmpty(userName)) {
            throw new ConfigException("Config item 'userName' of kafka appender can't be empty when securityProtocol is 'SASL_PLAINTEXT'.");
        }
        if (StringUtils.isEmpty(pw)) {
            throw new ConfigException("Config item 'password' of kafka appender can't be empty when securityProtocol is 'SASL_PLAINTEXT'.");
        }
        System.setProperty("logKafkaUser", userName); //系统中其他kafka，默认用此userName
        System.setProperty("logKafkaPwd", pw); //系统中其他kafka，默认用此password

        return "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + userName + "\" password=\"" + pw + "\";";
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    public void setRequiredNumAcks(int requiredNumAcks) {
        this.requiredNumAcks = requiredNumAcks;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setSyncSend(boolean syncSend) {
        this.syncSend = syncSend;
    }

    public void setKeySerializerClass(String clazz) {
        this.keySerializerClass = clazz;
    }

    public void setValueSerializerClass(String clazz) {
        this.valueSerializerClass = clazz;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public void setSslTruststoreLocation(String sslTruststoreLocation) {
        this.sslTruststoreLocation = sslTruststoreLocation;
    }

    public void setSslTruststorePassword(String sslTruststorePassword) {
        this.sslTruststorePassword = sslTruststorePassword;
    }

    public void setSslKeystorePassword(String sslKeystorePassword) {
        this.sslKeystorePassword = sslKeystorePassword;
    }

    public void setSslKeystoreType(String sslKeystoreType) {
        this.sslKeystoreType = sslKeystoreType;
    }

    public void setSslKeystoreLocation(String sslKeystoreLocation) {
        this.sslKeystoreLocation = sslKeystoreLocation;
    }

    public void setKerb5ConfPath(String kerb5ConfPath) {
        this.kerb5ConfPath = kerb5ConfPath;
    }

    public void setSaslMechanism(String saslMechanism) {
        this.saslMechanism = saslMechanism;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnableIdempotence(String enableIdempotence) {
        this.enableIdempotence = enableIdempotence;
    }
}
