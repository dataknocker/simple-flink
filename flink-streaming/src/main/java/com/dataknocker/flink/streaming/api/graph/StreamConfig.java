package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.configuration.Configuration;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;
import com.dataknocker.flink.util.InstantiationUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * stream相关配置
 */
public class StreamConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private Configuration config;


    public StreamConfig(Configuration configuration) {
        this.config = configuration;
    }

    public void setChainStart() {
        config.setBoolean(ConfigKeys.IS_CHAINED_VERTEX, true);
    }

    public boolean isChainStart() {
        return config.getBoolean(ConfigKeys.IS_CHAINED_VERTEX, false);
    }

    public void setChainEnd() {
        config.setBoolean(ConfigKeys.CHAIN_END, true);
    }

    public boolean isChainEnd() {
        return config.getBoolean(ConfigKeys.CHAIN_END, false);
    }

    public void setChainIndex(int chainIndex) {
        config.setInteger(ConfigKeys.CHAIN_INDEX, chainIndex);
    }

    public int getChainIndex() {
        return config.getInteger(ConfigKeys.CHAIN_INDEX, 0);
    }

    public void setOperatorName(String name) {
        this.config.setString(ConfigKeys.OPERATOR_NAME, name);
    }

    public String getOperatorName() {
        return config.getString(ConfigKeys.OPERATOR_NAME, null);
    }


    public void setInvokableClassName(String invokableClassName) {
        this.config.setString(ConfigKeys.INVOKABLE_CLASS_NAME, invokableClassName);
    }

    public String getInvokableClassName() {
        return config.getString(ConfigKeys.INVOKABLE_CLASS_NAME, null);
    }




    public void setVertexID(Integer vertexID) {
        config.setInteger(ConfigKeys.VERTEX_ID, vertexID);
    }

    public Integer getVertexID() {
        return config.getInteger(ConfigKeys.VERTEX_ID, 0);
    }

    public void setStreamOperatorFactory(StreamOperatorFactory<?> operatorFactory) {
        try {
            InstantiationUtil.writeObjectToConfig(config, ConfigKeys.SERIALIZED_UDF, operatorFactory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends StreamOperatorFactory<?>> T getStreamOperatorFactory(ClassLoader classLoader) {
        try {
            return (T) InstantiationUtil.readObjectFromConfig(config, ConfigKeys.SERIALIZED_UDF, classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setChainableOutEdges(List<StreamEdge> chainableOutputs) {
        try {
            InstantiationUtil.writeObjectToConfig(config, ConfigKeys.CHAINABLE_OUTPUTS, chainableOutputs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<StreamEdge> getChainableOutEdges(ClassLoader classLoader) {
        try {
            return InstantiationUtil.readObjectFromConfig(config, ConfigKeys.CHAINABLE_OUTPUTS, classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setNonChainableOutEdges(List<StreamEdge> nonChainableOutputs) {
        try {
            InstantiationUtil.writeObjectToConfig(config, ConfigKeys.NON_CHAINABLE_OUTPUTS, nonChainableOutputs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<StreamEdge> getNonChainableOutEdges(ClassLoader classLoader) {
        try {
            return InstantiationUtil.readObjectFromConfig(config, ConfigKeys.NON_CHAINABLE_OUTPUTS, classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setChainedConfigs(Map<Integer, StreamConfig> chainedConfigs) {
        try {
            InstantiationUtil.writeObjectToConfig(config, ConfigKeys.CHAINED_TASK_CONFIG, chainedConfigs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, StreamConfig> getChainedConfigs(ClassLoader classLoader) {
        try {
            return InstantiationUtil.readObjectFromConfig(config, ConfigKeys.CHAINED_TASK_CONFIG, classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, StreamConfig> getChainedConfigsWithSelf(ClassLoader classLoader) {
        try {
            Map<Integer, StreamConfig> chainedConfigs = InstantiationUtil.readObjectFromConfig(config, ConfigKeys.CHAINED_TASK_CONFIG, classLoader);
            chainedConfigs.put(getVertexID(), this);
            return chainedConfigs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface ConfigKeys {
        String VERTEX_ID = "vertexID";
        String SERIALIZED_UDF = "serializedUDF";
        String IS_CHAINED_VERTEX = "isChainedSubtask";
        String CHAIN_END = "chainEnd";
        String CHAIN_INDEX = "chainIndex";
        String OPERATOR_NAME = "operatorName";
        String CHAINED_TASK_CONFIG = "chainedTaskConfigs";
        String CHAINABLE_OUTPUTS = "chainableOutputs";
        String NON_CHAINABLE_OUTPUTS = "nonChainableOutputs";
        String INVOKABLE_CLASS_NAME = "invokableClassName";
    }
    public interface InputConfig extends Serializable {}

    /**
     * 输入配置
     */
    public class SourceInputConfig implements InputConfig {
        private static final long serialVersionUID = 1L;
        private StreamEdge inputEdge;

        public SourceInputConfig(StreamEdge inputEdge) {
            this.inputEdge = inputEdge;
        }

        public StreamEdge getInputEdge() {
            return inputEdge;
        }

        public String toString() {
            return inputEdge.toString();
        }

        public int hashCode() {
            return inputEdge.hashCode();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if(!(o instanceof SourceInputConfig)) {
                return false;
            }
            SourceInputConfig other = (SourceInputConfig) o;
            return Objects.equals(this.inputEdge, other.inputEdge);
        }

    }
}
