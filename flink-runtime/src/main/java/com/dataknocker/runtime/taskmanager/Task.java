package com.dataknocker.runtime.taskmanager;

import com.dataknocker.runtime.jobgraph.tasks.AbstractInvokable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task核心实现
 */
public class Task implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    private int subtaskIndex;
    private String nameOfInvokableClass;

    public Task(int subtaskIndex, String nameOfInvokableClass) {
        this.subtaskIndex = subtaskIndex;
        this.nameOfInvokableClass = nameOfInvokableClass;
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
            AbstractInvokable invokable = loadAndInstantiateInvokable(nameOfInvokableClass);
            invokable.invoke();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static AbstractInvokable loadAndInstantiateInvokable(String nameOfInvokableClass) throws Exception {
        try {
            Class<? extends AbstractInvokable> invokableClass = Class.forName(nameOfInvokableClass).asSubclass(AbstractInvokable.class);
            return invokableClass.newInstance();
        } catch (Throwable t) {
            throw new Exception("Could not load the task's invokable class.", t);
        }
    }
}
