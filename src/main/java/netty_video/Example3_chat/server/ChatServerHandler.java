package netty_video.Example3_chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 聊天服务器处理器
 * 需求：  客户端连接到服务器后，服务器在控制台打印XX已经上线，并且向其他客户端广播消息，XX已经上线；
 *
 * @author wuwenxiang
 * @create 2019-02-10 11:59
 **/
public class ChatServerHandler extends ChannelInboundHandlerAdapter {

    //channel组，用来记录客户端连接上来的channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //FIXME 需要显式释放与池化的ByteBuf实例相关的内存；
        ReferenceCountUtil.release(msg);

        Channel channel = ctx.channel();
        channelGroup.forEach(a -> {
                    if (a != channel) {
                        a.writeAndFlush(channel.remoteAddress()+"发送的信息 :"+msg+"\n");
                    }else {
                        a.writeAndFlush("自己"+msg+"\n");
                    }
                }
        );
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("客户端 ：" + channel.remoteAddress() + "已经上线了"+"\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("客户端 ：" + channel.remoteAddress() + "已经下线了"+"\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    /**
     * 表示客户端连接上来了
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        //TODO ChannelGroup 提供了  用于向他组内的所有channel调用writeAndFlush方法发送数据；
        channelGroup.writeAndFlush("客户端  ：" + channel.remoteAddress() + "连接上来了"+"\n");

        //把已经连接上来的客户端添加到channelGroup中；
        channelGroup.add(channel);
        System.out.println("客户端  ：" + channel.remoteAddress() + "连接上来了"+"\n");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //TODO 注意，一旦连接断开后，channelGroup会自动将断开的channel移除出去；
        //channelGroup.remove(channel);

        channelGroup.writeAndFlush("客户端 ：" + channel.remoteAddress() + "断开连接了"+"\n");
        System.out.println("客户端" + channel.remoteAddress() + "断开连接了"+"\n");
    }
}
