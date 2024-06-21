package com.dataknocker.flink.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 基本配置类,kv
 */
public class Configuration implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private static final long serialVersionUID = 1L;

    private Map<String, Object> configData = new HashMap<>();

    private Optional<Object> getRawValue(String key) {
        return Optional.ofNullable(configData.get(key));
    }

    public String getString(String key, String defaultValue) {
        return getRawValue(key).map(o -> (String)o).orElse(defaultValue);
    }

    public void setString(String key, String value) {
        configData.put(key, value);
    }

    public byte[] getBytes(String key, byte[] defaultValue) {
        return getRawValue(key).map(
                o -> {
                    if (o.getClass().equals(byte[].class)) {
                        return (byte[]) o;
                    } else {
                        throw new IllegalArgumentException(
                                String.format("Expected value of type byte[] for key '%s' but found '%s'", key, o.getClass().getName())
                        );
                    }
                }
        ).orElse(defaultValue);
    }

    public void putBytes(String key, byte[] value) {
        configData.put(key, value);
    }
}
