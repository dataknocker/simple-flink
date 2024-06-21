package com.dataknocker.flink.runtime.taskmanager;

import com.dataknocker.flink.configuration.Configuration;
import com.dataknocker.flink.runtime.execution.Environment;
import com.dataknocker.flink.runtime.execution.RuntimeEnvironment;
import com.dataknocker.flink.runtime.jobgraph.tasks.AbstractInvokable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

/**
 * Task核心实现
 */
public class Task implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    private int subtaskIndex;
    private String nameOfInvokableClass;

    private Environment environment;

    public Task(int subtaskIndex, Configuration configuration) {
        this.subtaskIndex = subtaskIndex;
        this.environment = new RuntimeEnvironment(configuration);
        this.nameOfInvokableClass = configuration.getString("invokableClassName", "");
    }
    @Override
    public void run() {
        try {
            doRun();
        } catch (Exception exception) {
            logger.error("Task {} failed", nameOfInvokableClass, exception);
        }
    }

    public void doRun() {
        try {
            AbstractInvokable invokable = loadAndInstantiateInvokable(nameOfInvokableClass, environment);
            invokable.restore();
            invokable.invoke();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static AbstractInvokable loadAndInstantiateInvokable(String nameOfInvokableClass, Environment env) throws Exception {
        try {
            Class<? extends AbstractInvokable> invokableClass = Class.forName(nameOfInvokableClass).asSubclass(AbstractInvokable.class);
            Constructor<? extends AbstractInvokable> constructor = invokableClass.getConstructor(Environment.class);
            return constructor.newInstance(env);
        } catch (Throwable t) {
            throw new Exception("Could not load the task's invokable class.", t);
        }
    }
}
