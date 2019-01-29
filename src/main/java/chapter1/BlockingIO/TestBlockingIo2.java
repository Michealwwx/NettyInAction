package chapter1.BlockingIO;

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
 * @ClassName: TestBlockingIo2
 * @Description: TODO
 * @Author: wuwx
 * @Date: 2019-01-29 13:45
 **/
public class TestBlockingIo2 {
    /**
     * 客户端
     */
    @Test
    public void client() {
        SocketChannel socketChannel = null;
        FileChannel inChannel = null;
        try {
            //1.连接到服务器；
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
            //2.获取文件数据通道；
            inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            //3.将文件读取到buffer中;在从buffer写入到与服务器连接的通道中；
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (inChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
            //socketChannel.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(socketChannel!=null){
                    socketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(inChannel!=null){
                    inChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 服务端；
     */
    @Test
    public void server() {

        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        FileChannel fileChannel = null;
        try {
            //1.获取服务器上某个端口上连接的通道；
            serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(9999));
            //设置该通道为非阻塞；
            serverSocketChannel.configureBlocking(true);
            //2.获取与客户端连接的通道；
            socketChannel = serverSocketChannel.accept();
            //3.使用文件通道接收客户端传输过来的数据；
            fileChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            //4.分配缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            if (socketChannel != null) {
                while (socketChannel.read(byteBuffer) != -1) {
                    byteBuffer.flip();
                    fileChannel.write(byteBuffer);
                    byteBuffer.clear();
                }
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
                if (socketChannel != null) {
                    socketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


}
