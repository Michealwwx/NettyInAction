package chapter2.NoneBlokingIo;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;

/**
 * @ClassName: TestDatagramChannelUdpIo
 * @Description: 测试DatagramChannel基于UDP协议的通道传输；
 * @Author: wuwx
 * @Date: 2019-01-29 15:46
 **/
public class TestDatagramChannelUdpIo {

    /**
     * 客户端
     */
    @Test
    public void client(){
        try {
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

//            Scanner scanner = new Scanner(System.in);
//
//            while (scanner.hasNext()){
//                String str = scanner.next();
                byteBuffer.put((new Date().toString()+"\n"+"哈哈哈").getBytes());
                byteBuffer.flip();
                //TODO UDP是发送；
                datagramChannel.send(byteBuffer,new InetSocketAddress("127.0.0.1",9898));
            //}
            datagramChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }

    /**
     * 服务端；
     */
    @Test
    public void server(){
        try {
            DatagramChannel serverDatagramChannel = DatagramChannel.open();
            serverDatagramChannel.configureBlocking(false);
            serverDatagramChannel.bind(new InetSocketAddress(9898));

            Selector selector = Selector.open();
            serverDatagramChannel.register(selector, SelectionKey.OP_READ);
            while (selector.select()>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey it = iterator.next();
                    if(it.isReadable()){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        serverDatagramChannel.receive(byteBuffer);
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(),0,byteBuffer.limit()));
                        byteBuffer.clear();
                    }


                }
                iterator.remove();
            }
            serverDatagramChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }


}
