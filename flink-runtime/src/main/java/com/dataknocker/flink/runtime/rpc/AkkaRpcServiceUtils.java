package com.dataknocker.flink.runtime.rpc;

import akka.actor.ActorSystem;

import java.util.function.Function;

public class AkkaRpcServiceUtils {


    public ActorSystem createRemoteActorSystem() {

        return null;
    }

    public static class AkkaRpcServiceBuilder {
        private String address;
        private int port;

        private String actorSystemName = "flink";

        private AkkaRpcServiceBuilder() {

        }

        private AkkaRpcServiceBuilder(String address, int port) {
            this.address = address;
            this.port = port;
        }

        public AkkaRpcServiceBuilder withActorSystemName(String actorSystemName) {
            this.actorSystemName = actorSystemName;
            return this;
        }

        public AkkaRpcServiceBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public AkkaRpcServiceBuilder withPort(int port) {
            this.port = port;
            return this;
        }
        public AkkaRpcService createAndStart(Function<ActorSystem, AkkaRpcService> constructor) {
            ActorSystem actorSystem = AkkaUtils.createActorSystem(actorSystemName, address, port);
            return constructor.apply(actorSystem);
        }

        public AkkaRpcService createAndStart() {
            ActorSystem actorSystem = AkkaUtils.createActorSystem(actorSystemName, address, port);
            return new AkkaRpcService(actorSystem);
        }
    }

    public static AkkaRpcServiceBuilder newRemoteBuilder(String address, int port) {
        return new AkkaRpcServiceBuilder(address, port);
    }
    public static AkkaRpcServiceBuilder newLocalBuilder() {
        return new AkkaRpcServiceBuilder();
    }
}
