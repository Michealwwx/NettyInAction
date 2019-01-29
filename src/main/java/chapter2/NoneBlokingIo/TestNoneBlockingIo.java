package chapter2.NoneBlokingIo;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @ClassName: TestNoneBlockingIo
 * @Description: 非阻塞IO
 * 1.通道
 * 非阻塞的只适用于网络channel，不适用于FileChannel（本地通道）
 * ServerSocketChannel
 * SocketChannel
 * DatagramChannel
 * <p>
 * Pipe.SinkChannel
 * Pipe.SourceChannel
 * 2.选择器
 * <p>
 * 3.缓冲区；
 * @Author: wuwx
 * @Date: 2019-01-29 14:39
 **/
public class TestNoneBlockingIo {
    /**
     * 客户端
     */
    @Test
    public void client() {
        SocketChannel socketChannel = null;
        try {
            //1.获取通道
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            //2.切换为非阻塞模式：
            socketChannel.configureBlocking(false);
            //3.分配指定大小缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //4.发送数据给服务端；
            //byteBuffer.put(LocalDateTime.now().toString().getBytes());
            System.out.println("请输入");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String string = scanner.next();
                System.out.println("接下来数据:");
                byteBuffer.put(("哈哈哈"+string).getBytes());
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();

            }
            socketChannel.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socketChannel != null) {
                    socketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务端
     */
    @Test
    public void server() {
        ServerSocketChannel serverSocketChannel = null;
        try {
            //1.获取服务端的通道；
            serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(9898));
            //2.切换为非阻塞模式；
            serverSocketChannel.configureBlocking(false);
            //3.获取选择器；
            Selector selector = Selector.open();
            //4.将通道注册到选择器上；并且设置监听通道的状态；
            /**
             * 通道的四种状态：
             * SelectionKey.OP_READ    （1）读
             * SelectionKey.OP_WRITE   （4）写
             * SelectionKey.OP_CONNECT  （8）连接
             * SelectionKey.OP_ACCEPT （16）接收
             * 若注册时不止监听一种事件，则用“位或”操作符连接
             * TODO  int interesting =  SelectionKey.OP_WRITE|SelectionKey.OP_READ
             */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //5.轮询获取选择器上已经准备就绪的事件；这是实现非阻塞的核心；
            while (selector.select() > 0) {
                //6.获取当前选择器中所有注册的“选择键”（已就绪的监听事件）
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                //7.迭代获取；
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    //8.判断是什么事件就绪；因为不同事件作不同操作；  这里如果接收就绪，就接收客户端的连接；
                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        //9 TODO 一定要注意一定要把客户端切换为非阻塞模式
                        socketChannel.configureBlocking(false);

                        //10.将该通道注册到选择器，并且监听其读取事件
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        //11.获取选择器上读事件就绪的通道；如果是读就绪，则开始读取数据；
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        //12.读取数据
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = socketChannel.read(byteBuffer))>0){
                            byteBuffer.flip();
                            System.out.println(new String(byteBuffer.array(),0,len));
                            byteBuffer.clear();
                        }
                    }

                }
                //TODO 操作完成后记得要将选择键取消掉；负责监听过的事件一直存在；
                iterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }

}
