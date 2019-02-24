package netty_video.Example5_websocket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * 处理器
 *
 * @author wuwenxiang
 * @create 2019-02-24 21:50
 **/
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("收到信息："+msg.text());
        //TODO writeAndFlush中传输的内容类型是根据程序选择的协议以及特定的处理器
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器在"+ LocalDateTime.now()+"收到信息了"));
    }

    /**
     * 连接成功
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded ,channelId 是： " +ctx.channel().id().asLongText());
    }

    /**
     * 连接关闭
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved ,channelId 是 ： "+ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 了"+cause.toString());
        ctx.channel().close();
    }
}
