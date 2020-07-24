package org.hycode.net;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Type;

public class ByteToDataDecoder extends LengthFieldBasedFrameDecoder {

    public ByteToDataDecoder() {
        super(Integer.MAX_VALUE, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        in.readInt();
        int dataClassLen = in.readInt();
        byte[] dataClass = new byte[dataClassLen];
        in.readBytes(dataClass);

        int dataLen = in.readInt();
        byte[] data = new byte[dataLen];
        in.readBytes(data);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataClass);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Class<?> dataClassObj = (Class<?>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();

        byteArrayInputStream = new ByteArrayInputStream(data);
        objectInputStream = new ObjectInputStream(byteArrayInputStream);
        NetData dataObject = (NetData) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();


        DataPackage dataPackage = new DataPackage();

        dataPackage.setData(dataObject);
        dataPackage.setDataClass(dataClassObj);

        return dataPackage;
    }
}
