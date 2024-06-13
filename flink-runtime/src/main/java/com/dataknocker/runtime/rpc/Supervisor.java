package com.dataknocker.runtime.rpc;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;

import java.time.Duration;
import java.util.function.Function;

/**
 * 用于启动管理对应AkkaRpcActor，如taskmanager等等
 */
public class Supervisor {
    private static final String SUPERVISOR_NAME = "rpc";

    private static Duration INF_DURATION = Duration.ofSeconds(21474835);
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

    public StartAkkaRpcActorResponse startAkkaRpcActor(SupervisorActor.PropsFactory propsFactory, String endpointId) {
        return Patterns.ask(this.actor, StartAkkaRpcActorRequest.of(propsFactory, endpointId), INF_DURATION)
                .toCompletableFuture()
                .thenApply(StartAkkaRpcActorResponse.class::cast)
                .join();
    }

    private static class SupervisorActor extends AbstractActor {

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(StartAkkaRpcActorRequest.class, this::createEndPointAkkaRpc)
                    .build();
        }

        private void createEndPointAkkaRpc(StartAkkaRpcActorRequest request) {
            ActorRef actorRef = getContext().actorOf(request.getPropsFactory().create(), request.getEndpointId());
            StartAkkaRpcActorResponse response = StartAkkaRpcActorResponse.sucess(ActorRegistration.of(actorRef));
            getSender().tell(response, getSender());
        }

        interface PropsFactory {
            Props create();
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

        private SupervisorActor.PropsFactory propsFactory;
        private String endpointId;

        private StartAkkaRpcActorRequest(SupervisorActor.PropsFactory propsFactory, String endpointId) {
            this.propsFactory = propsFactory;
            this.endpointId = endpointId;
        }

        public String getEndpointId() {
            return endpointId;
        }

        public SupervisorActor.PropsFactory getPropsFactory() {
            return propsFactory;
        }

        public static StartAkkaRpcActorRequest of(SupervisorActor.PropsFactory propsFactory, String endpointId) {
            return new StartAkkaRpcActorRequest(propsFactory, endpointId);
        }
    }

    public static class StartAkkaRpcActorResponse {

        private ActorRegistration registration;

        private Throwable error;

        private StartAkkaRpcActorResponse(ActorRegistration registration, Throwable error) {
            this.registration = registration;
        }

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

}