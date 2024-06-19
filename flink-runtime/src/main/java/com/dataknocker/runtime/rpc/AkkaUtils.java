package com.dataknocker.runtime.rpc;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.ExtensionId;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

public class AkkaUtils {
    public static ActorSystem createActorSystem(String actorSystemName, String hostname, int port) {
        if (StringUtils.isNotBlank(hostname)) {
            return ActorSystem.create(actorSystemName, getAkkaConfig(hostname, port));
        } else {
            return ActorSystem.create(actorSystemName);
        }
    }

    public static Config getAkkaConfig(String hostname, int port) {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("hostname", hostname);
        valueMap.put("port", port);
        String configTpl = "akka {\n" +
                "  actor {\n" +
                "    provider = \"akka.remote.RemoteActorRefProvider\"\n" +
                "  }\n" +
                "  remote {\n" +
                "    netty.tcp {\n" +
                "      hostname = \"${hostname}\"\n" +
                "      port = ${port}\n" +
                "    }\n" +
                "  }\n" +
                "}";
        StringSubstitutor configString = new StringSubstitutor(valueMap);
        return ConfigFactory.parseString(configString.replace(configTpl));
    }

    /**
     * 获得actorRef的全路径
     * @param system
     * @param actorRef
     * @return
     */
    public static String getAkkaURL(ActorSystem system, ActorRef actorRef) {
        Address systemAddress = system.provider().getDefaultAddress();
        return actorRef.path().toStringWithAddress(systemAddress);
    }
}
