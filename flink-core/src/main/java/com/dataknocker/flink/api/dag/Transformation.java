package com.dataknocker.flink.api.dag;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Transformation<T> {
    protected int id;
    protected String name;

    protected String uid;

    //TODO 源码为什么不用线程安全？
    private static AtomicInteger idCounter = new AtomicInteger();

    public Transformation(String name) {
        this.id = getNewNodeId();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static int getNewNodeId() {
        return idCounter.incrementAndGet();
    }

    public abstract List<Transformation<?>> getInputs();
}
