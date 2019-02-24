package netty_video.Example4_heartBeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import netty_video.Example2.server.MyServerInitializer;

import java.net.InetSocketAddress;

/**
 * 服务器端
 *
 * @author wuwenxiang
 * @create 2019-02-23 22:39
 **/
public class MyServer {

    private int port;

    public MyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        MyServer mySocketServer = new MyServer(8082);
        mySocketServer.start();
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    //IdleStateHandler(空闲状态监测的处理器) :一定时间间隔内没有①读②写③读写 操作即会触发；
                                    .addLast(new IdleStateHandler(5,7,10))
                                    .addLast(new MyServerHandler());
                        }
                    })
            ;
            ChannelFuture c = bootstrap.bind().sync();
            c.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }
}
