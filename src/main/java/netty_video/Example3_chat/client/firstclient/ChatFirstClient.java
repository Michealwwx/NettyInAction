package netty_video.Example3_chat.client.firstclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 聊天客户端1
 *
 * @author wuwenxiang
 * @create 2019-02-10 12:16
 **/
public class ChatFirstClient {
    private String host;
    private int port;

    public ChatFirstClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        ChatFirstClient chatFirstClient = new ChatFirstClient("127.0.0.1",8082);
        chatFirstClient.start();
    }

    public void start() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .remoteAddress(host, port)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
                                    .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                    .addLast(new StringEncoder(CharsetUtil.UTF_8))
                                    .addLast(new FirstClientHandler());
                        }
                    });

            //表示服务器端的一个连接
            Channel channel = bootstrap.connect().sync().channel();
            //不断的读取用户在控制台上输入的内容；
            //channelFuture.channel().closeFuture().sync();
            System.out.println("请输入信息： ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                channel.writeAndFlush(br.readLine()+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }


    }

}
