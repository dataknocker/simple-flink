package com.dataknocker.runtime.tasks;

import com.dataknocker.runtime.jobgraph.tasks.AbstractInvokable;
/**
 * transform相关task
 */
public class StreamTask extends AbstractInvokable {

    @Override
    public void invoke() throws Exception {
        System.out.println("Do something...");
    }


}
