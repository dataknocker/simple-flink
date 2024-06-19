package com.dataknocker.runtime.taskmanager;

import com.dataknocker.runtime.rpc.RpcService;

/**
 * 管理Job的。管理和Job leader的连接。
 * TODO
 * 当前job master无ha，所以只有一个固定的
 */
public interface JobLeaderService {

    void start();
    void stop() throws  Exception;

    void addJob();
    void removeJob();
}
