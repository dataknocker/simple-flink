package com.dataknocker.flink.runtime.jobgraph;

import com.dataknocker.flink.configuration.Configuration;
import com.dataknocker.flink.runtime.jobgraph.tasks.AbstractInvokable;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JobGraph的点
 */
public class JobVertex implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_NAME = "(unnamed vertex)";

    private static AtomicInteger idCounter = new AtomicInteger(0);

    private int id;

    private String invokableClassName;

    private String name;

    private String operatorName;

    private Configuration configuration;

    public JobVertex() {
        this(DEFAULT_NAME);
    }

    public JobVertex(String name) {
        this.id = idCounter.incrementAndGet();
        this.name = name;
    }


    public Configuration getConfiguration() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    public String getInvokableClassName() {
        return invokableClassName;
    }

    public void setInvokableClassName(Class<? extends AbstractInvokable> invokable) {
        this.invokableClassName = invokable.getName();
    }

    public Class<? extends AbstractInvokable> getInvokableClass(ClassLoader cl) {
        try {
            return Class.forName(invokableClassName, true, cl).asSubclass(AbstractInvokable.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getID() {
        return id;
    }
}
