package com.dataknocker.runtime.rpc;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class AkkaRpcService implements RpcService {
    private static final Logger LOG = LoggerFactory.getLogger(AkkaRpcService.class);

    private static Duration INF_DURATION = Duration.ofSeconds(21474835);

    private ActorSystem actorSystem;
    private Supervisor supervisor;

    public AkkaRpcService(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
        this.supervisor = Supervisor.create(this.actorSystem);
    }

    @Override
    public <C extends RpcGateway> CompletableFuture<C> connect(String address, Class<C> clazz) {
        return null;
    }

    @Override
    public <C extends RpcEndpoint & RpcGateway> RpcServer startServer(C rpcEndpoint) {
        ActorRegistration registration = registerAkkaRpcActor(rpcEndpoint);
        ActorRef actorRef = registration.getActorRef();
        LOG.info("Starting RPC endpoint for {} at {} ", rpcEndpoint.getClass().getName(), actorRef.path());
        return null;
    }

    private <C extends RpcEndpoint & RpcGateway> ActorRegistration registerAkkaRpcActor(C rpcEndpoint) {
        StartAkkaRpcActorResponse response = supervisor.startAkkaRpcActor(rpcEndpoint.getEndpointId());
        return response.orElseThrow(cause ->
                new AkkaRpcRuntimeException(
                        String.format("could not create akka rpc server for %s", rpcEndpoint.getEndpointId()),
                        cause));
    }


    private static class Supervisor {
        private static final String SUPERVISOR_NAME = "rpc";
        private ActorRef actor;

        private Supervisor(ActorRef actor) {
            this.actor = actor;
        }

        public static Supervisor create(ActorSystem actorSystem) {
            ActorRef actorRef = actorSystem.actorOf(Props.create(SupervisorActor.class), SUPERVISOR_NAME);
            return new Supervisor((actorRef));
        }

        public ActorRef getActor() {
            return actor;
        }

        public StartAkkaRpcActorResponse startAkkaRpcActor(String endpointId) {
            return Patterns.ask(this.actor, StartAkkaRpcActorRequest.of(endpointId), INF_DURATION)
                    .toCompletableFuture()
                    .thenApply(StartAkkaRpcActorResponse.class::cast)
                    .join();
        }

    }

    private static class SupervisorActor extends AbstractActor {

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(StartAkkaRpcActorRequest.class, this::createEndPointAkkaRpc)
                    .build();
        }

        private void createEndPointAkkaRpc(StartAkkaRpcActorRequest request) {
            ActorRef actorRef = getContext().actorOf(Props.create(AkkaRpcActor.class), request.getEndpointId());
            StartAkkaRpcActorResponse response = StartAkkaRpcActorResponse.sucess(ActorRegistration.of(actorRef));
            getSender().tell(response, getSender());
        }
    }

    public static class ActorRegistration {
        private ActorRef actorRef;

        private ActorRegistration(ActorRef actorRef) {
            this.actorRef = actorRef;
        }

        public ActorRef getActorRef() {
            return this.actorRef;
        }

        public static ActorRegistration of(ActorRef actorRef) {
            return new ActorRegistration(actorRef);
        }
    }

    public static class StartAkkaRpcActorRequest {
        private String endpointId;

        private StartAkkaRpcActorRequest(String endpointId) {
            this.endpointId = endpointId;
        }

        public String getEndpointId() {
            return endpointId;
        }

        public static StartAkkaRpcActorRequest of(String endpointId) {
            return new StartAkkaRpcActorRequest((endpointId));
        }
    }

    public static class StartAkkaRpcActorResponse {

        private ActorRegistration registration;

        private Throwable error;

        private StartAkkaRpcActorResponse(ActorRegistration registration, Throwable error) {
            this.registration = registration;
        }

//        private <X extends Throwable> ActorRegistration orElseThrow(Function<? super Throwable, ? extends X> throwableFunction) throws X {
//            if(registration != null) {
//                return registration;
//            } else {
//                throw throwableFunction.apply(error);
//            }
//        }

        public <X extends Throwable> ActorRegistration orElseThrow(
                Function<? super Throwable, ? extends X> throwableFunction) throws X {
            if (registration != null) {
                return registration;
            } else {
                throw throwableFunction.apply(error);
            }
        }

        public ActorRegistration getRegistration() {
            return registration;
        }

        public Throwable getError() {
            return error;
        }

        public static StartAkkaRpcActorResponse sucess(ActorRegistration registration) {
            return new StartAkkaRpcActorResponse(registration, null);
        }

        public static StartAkkaRpcActorResponse error(Throwable error) {
            return new StartAkkaRpcActorResponse(null, error);
        }

    }

    public class AkkaRpcRuntimeException extends RuntimeException {

        public AkkaRpcRuntimeException(String message) {
            super(message);
        }

        public AkkaRpcRuntimeException(Throwable cause) {
            super(cause);
        }

        public AkkaRpcRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
