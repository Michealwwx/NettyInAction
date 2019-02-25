package Netty.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * @ClassName: ByteBufSimpleUse2
 * @Description: 派生缓冲区操作;
 * @Author: wuwx
 * @Date: 2019-01-30 16:42
 **/
public class ByteBufSimpleUse2 {

    /**
     * 测试切片操作；
     */
    @Test
    public void testSlice(){
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello world",utf8);

        ByteBuf sliced = byteBuf.slice(0,1);
        System.out.println("sliced截取后："+sliced.toString(utf8));


        //setByte(index,数据)  设置指定位置的内容
        byteBuf.setByte(0,(byte)'X');
        System.out.println("setByte设置数据："+byteBuf.toString(utf8));

    }

    /**
     * 测试读写操作；
     * get()和set()操作，从给定的索引开始，并且保持索引不变；
     * read()和write()操作，从给定的索引开始，并且会根据已经访问过的字节数对索引进行调整；
     */
    @Test
    public void testReadAndWrite(){
        Charset utf8 = Charset.forName("Utf-8");
        ByteBuf byteBuf = Unpooled.copiedBuffer("哈嘿哦嗯",utf8);
        //1.获取能够读取的字节数
        int canReadBytesLength = byteBuf.readableBytes();
        System.out.println("canReadBytesLength:"+canReadBytesLength);
        //2.获取可被写入的字节数；
        int canWriteBytesLength = byteBuf.writableBytes();
        System.out.println("canWriteBytesLength:"+canWriteBytesLength);
        boolean readAble = byteBuf.isReadable();
        System.out.println("readAble:"+readAble);
        boolean writeAble = byteBuf.isWritable();
        System.out.println("writeAble:"+writeAble);
        int maxCapacity = byteBuf.maxCapacity();
        System.out.println("maxCapacity:"+maxCapacity);
        boolean haveBackingArray = byteBuf.hasArray();
        System.out.println("haveBackingArray:"+haveBackingArray);
        byte[] array = byteBuf.array();



    }


}
