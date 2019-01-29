package NIO.Pipe;

import com.sun.javafx.image.ByteToBytePixelConverter;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.Date;

/**
 * @ClassName: TestPipe
 * @Description: 测试管道； 这个两个线程间单向数据连接；
 * @Author: wuwx
 * @Date: 2019-01-29 17:03
 **/
public class TestPipe {

    @Test
    public void test1(){
        //1.获取管道
        try {
            Pipe pipe = Pipe.open();
            //2.将缓冲区中的数据写入到管道；
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //3.
            Pipe.SinkChannel sinkChannel =pipe.sink();
            byteBuffer.put((new Date().toString()+"哈哈哈").getBytes());
            byteBuffer.flip();
            sinkChannel.write(byteBuffer);
            //byteBuffer.clear();
            //读取缓冲区中的数据；
            Pipe.SourceChannel sourceChannel = pipe.source();
            byteBuffer.flip();
            int len = sourceChannel.read(byteBuffer);
            System.out.println(new String(byteBuffer.array(),0,len));
            sourceChannel.close();
            sinkChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }


}
