package netty_video.Example5_websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

/**
 * 服务端
 *
 * @author wuwenxiang
 * @create 2019-02-24 21:29
 **/
public class MyWebSocketServer {
    private int port;

    public MyWebSocketServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        MyWebSocketServer server = new MyWebSocketServer(8080);
        server.start();
    }

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //编解码字节与HttpRequest,HttpContent,LastHttpContent
                            pipeline.addLast(new HttpServerCodec());
                            //写入一个文件的内容；
                            pipeline.addLast(new ChunkedWriteHandler());
                            //聚合
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            //专门用来处理websocket
                            pipeline.addLast(new WebSocketServerProtocolHandler("/myPath"));
                            pipeline.addLast(new WebSocketServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }


}
