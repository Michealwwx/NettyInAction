package NIO.BlockingIO;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @ClassName: TestBlockingIo
 * @Description: 测试阻塞io
 * 一、使用NIO完成网络通信的三个核心
 * 1.Channel（通道）负责连接
 * java.nio.channel.Channel 接口
 * |---SelectableChannel
 * |---SocketChannel
 * |---ServerSocketChannel
 * |---DatagramChannel
 * <p>
 * |---Pipe.SinkChannel
 * |---Pipe.SourceChannel
 * FileChannel不是可选择模式；Selectable针对的是网络传输的Channel不针对本地Channel；
 * 选择器Selector用来监控网络Channel
 * <p>
 * 2.Buffer(缓冲区)：负责数据的存取；
 * <p>
 * <p>
 * 3.Selector（选择器）：是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况；
 * @Author: wuwx
 * @Date: 2019-01-29 11:13
 **/
public class TestBlockingIo {
    /**
     * 客户端向服务器发送图片数据
     */
    @Test
    public void client() {
        SocketChannel socketChannel = null;
        FileChannel inChannel = null;
        try {
            //1.获取通道
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            //2.分配指定大小的缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (inChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                //3.读取本地文件并发送到服务器;
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socketChannel != null) {
                    socketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (inChannel != null) {
                    inChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 服务端接收客户端的数据；
     */
    @Test
    public void server() {
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        FileChannel outChannel = null;
        try {
            //1.获取通道；并且绑定端口号；
            serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(9898));
            //TODO  默认阻塞；
            serverSocketChannel.configureBlocking(true);
            //2.获取客户端连接的通道；
            socketChannel = serverSocketChannel.accept();
            //3.接收客户端的数据并保存到本地；
            outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            //4.分配缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // read，write都是针对Channel本身的操作，
            // read表示将channel中的数据读取到缓冲区，
            // write表示将缓冲区中的数据写到channel中；
            while (socketChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                outChannel.write(byteBuffer);
                byteBuffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocketChannel != null) {
                    serverSocketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
