package Netty.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.Arrays;

/**
 * @ClassName: ByteBufSimpleUse1
 * @Description: 对比NIO中的ByteBuffer和Netty的ByteBuf
 * @Author: wuwx
 * @Date: 2019-01-29 17:34
 **/
public class ByteBufSimpleUse1 {

    @Test
    public void test1() {
        //
        ByteBuf byteBuf = Unpooled.buffer(8);

        byte[] bytes = {1, 2, 3, 4, 5};
        byteBuf.writeBytes(bytes);
        System.out.println("写入一段内容后ByteBuf数据为：" + Arrays.toString(byteBuf.array()));

        byte b1 = byteBuf.readByte();
        System.out.println("读取到的数据为：" + b1);

        //将读取到的内容丢弃；
        byteBuf.discardReadBytes();
        System.out.println("将ByteBuf中读取到的数据丢弃后：" + Arrays.toString(byteBuf.array()));

        //清空读写指针
        byteBuf.clear();
        System.out.println("清空读写指针后：" + Arrays.toString(byteBuf.array()));

    }


}
