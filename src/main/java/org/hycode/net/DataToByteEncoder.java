package org.hycode.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class DataToByteEncoder extends MessageToByteEncoder<DataPackage> {
    protected void encode(ChannelHandlerContext channelHandlerContext, DataPackage dataPackage, ByteBuf byteBuf) throws Exception {
        byte[] data = dataPackage.getDataBytes();
        byte[] dataClass = dataPackage.getDataClassBytes();
        int dataLen = data.length;
        int dataClassLen = dataClass.length;

        int totalLen = 4 + dataLen + 4 + dataClassLen;

        byteBuf.writeInt(totalLen);//写入总长度

        byteBuf.writeInt(dataClassLen);//写入类型长度
        byteBuf.writeBytes(dataClass);//写入包类型
        byteBuf.writeInt(dataLen);//写入数据长度
        byteBuf.writeBytes(data);//写入数据
    }
}
