package com.dataknocker.flink.util;

import com.dataknocker.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 序列反序列工具类
 */
public class InstantiationUtil {
    private static final Logger LOG = LoggerFactory.getLogger(InstantiationUtil.class);


    public static <T> T readObjectFromConfig(Configuration config, String key, ClassLoader classLoader)
            throws IOException, ClassNotFoundException {
        byte[] bytes = config.getBytes(key, null);
        if (bytes == null) {
            return null;
        }
        return deserializeObject(bytes, classLoader);
    }

    public static void writeObjectToConfig(Configuration config, String key, Object object)
            throws IOException {
        byte[] bytes = serializeObject(object);
        config.setBytes(key, bytes);
    }

    public static <T> T deserializeObject(byte[] bytes, ClassLoader classLoader)
            throws IOException, ClassNotFoundException {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            //TODO 先用java自带序列化
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (T) objectInputStream.readObject();
        } finally {
            //TODO 为什么要做这个替换？是为了用户自定义classloader?
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    public static byte[] serializeObject(Object object) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return baos.toByteArray();
    }
}
