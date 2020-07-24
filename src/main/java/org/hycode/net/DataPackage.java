package org.hycode.net;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class DataPackage {
    private NetData data;
    private long sessionId;
    private Class<?> dataClass;

    public DataPackage() {
    }

    static DataPackage warp(NetData netData) {
        DataPackage dataPackage = new DataPackage();
        dataPackage.setData(netData);
        dataPackage.setDataClass(netData.getClass());
        return dataPackage;
    }

    public Class<?> getDataClass() {
        return dataClass;
    }

    public void setDataClass(Class<?> dataClass) {
        this.dataClass = dataClass;
    }

    public byte[] toBytes(Object o) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ba);
            oos.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ba.toByteArray();
    }

    public byte[] getDataBytes() {
        return toBytes(data);
    }

    public byte[] getDataClassBytes() {
        return toBytes(dataClass);
    }

    public <T extends NetData> T getData() {
        return (T) data;
    }

    public void setData(NetData data) {
        this.data = data;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "DataPackage{" +
                "data=" + data +
                ", sessionId=" + sessionId +
                ", dataClass=" + dataClass +
                '}';
    }
}
