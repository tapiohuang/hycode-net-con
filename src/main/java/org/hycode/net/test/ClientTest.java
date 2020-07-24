package org.hycode.net.test;

import org.hycode.net.NetClient;
import org.hycode.net.NetSessionFilter;
import org.hycode.net.processors.HeartCheckProcessor;

public class ClientTest {
    public static void main(String[] args) {
        NetClient netClient = new NetClient();
        netClient.processor(new HeartCheckProcessor(netClient));
        netClient.filter(new NetSessionFilter(netClient));
        netClient.host("127.0.0.1").port(10056).connect();

/*        DataPackage<ArrayList<WxMessage>> dataPackage = new DataPackage<ArrayList<WxMessage>>();
        dataPackage.setDataType(new UploadMessage());
        dataPackage.setData(new ArrayList<WxMessage>());
        netClient.sendData(dataPackage);*/

    }
}
