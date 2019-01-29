import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @ClassName: NioBuffer
 * @Description: nio中的buffer对象；
 * @Author: wuwx
 * @Date: 2019-01-24 15:21
 **/
public class NioBuffer {

    /**
     * 简单的buffer示例
     */
    @Test
    public void bufferExample1() throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile("","");
        FileChannel channel = null;
        //从channel中向buff中写数据；
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int readBuff = channel.read(byteBuffer);

        CharBuffer charBuffer = CharBuffer.allocate(111);
        charBuffer.put('a');
        //从buffer中读取数据
        byteBuffer.get();
        int writtenBuff = channel.write(byteBuffer);
    }

    /**
     * 选择器例子
     */
    @Test
    public void testSelectorExample1() throws IOException {
        //1. 新建选择器；
        Selector selector = Selector.open();
        SelectableChannel selectableChannel = null;

        // 2.向selector注册通道；

        //与selector一起使用时，Channel必须处于非阻塞模式下；这意味着不能将FileChannel 与Selector一起使用；
        selectableChannel.configureBlocking(false);

        //第二个参数：“interest集合”；表明在通过Selector监听Channel时对什么事件感兴趣；connect,accept,read,write四种事件；
        SelectionKey selectionKey = selectableChannel.register(selector, SelectionKey.OP_READ);


    }

}
