package Netty.chapter1.simpleUse;

import io.netty.bootstrap.ServerBootstrap;

/**
 * @ClassName: ServerTest
 * @Description: 服务器；
 * @Author: wuwx
 * @Date: 2019-01-31 13:44
 **/
public class ServerTest {




    public void start(){
        //1.至少一个ChannelHandler：实现服务器对从客户端接收的消息的处理；即业务逻辑处理；

        //2.引导器；配置服务器的启动代码；
        ServerBootstrap serverBootstrap = new ServerBootstrap();


    }

}
