package com.dataknocker.runtime.taskmanager;

import com.dataknocker.runtime.jobmanager.JobMasterGateway;
import com.dataknocker.runtime.registration.RetryingRegistration;
import com.dataknocker.runtime.rpc.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultJobLeaderService implements JobLeaderService{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private String ownerAddress;
    private RpcService rpcService;

    public DefaultJobLeaderService(String ownerAddress, RpcService rpcService) {
        this.ownerAddress = ownerAddress;
        this.rpcService = rpcService;
    }

    public void start() {
        logger.info("Start job leader service.");
        String jobMasterAddress = "akka.tcp://flink@localhost:2550/user/rpc/jobmaster_0";
        RetryingRegistration<JobMasterGateway> registration = new JobMasterRetryingRegistration(ownerAddress, rpcService, jobMasterAddress);
        registration.startRegistration();
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void addJob() {

    }

    @Override
    public void removeJob() {

    }

    private static class JobMasterRetryingRegistration extends RetryingRegistration<JobMasterGateway> {
        private String ownerAddress;
        public JobMasterRetryingRegistration(String ownerAddress, RpcService rpcService,  String targetAddress) {
            super(rpcService, "JobMaster", JobMasterGateway.class, targetAddress);
            this.ownerAddress = ownerAddress;
        }

        @Override
        public void invokeRegistration(JobMasterGateway gateway) {
            gateway.registerTaskManager(this.ownerAddress);
        }
    }
}
