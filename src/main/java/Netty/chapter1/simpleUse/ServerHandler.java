package Netty.chapter1.simpleUse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName: ServerHandler
 * @Description: 服务器处理器；
 * @Author: wuwx
 * @Date: 2019-01-31 13:53
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
