package com.dataknocker.flink.client;

import com.dataknocker.flink.runtime.taskmanager.TaskManagerRunner;

public class TaskManagerStarter {

    public static void main(String[] args) {
        TaskManagerRunner.startTaskExecutor();
    }
}
