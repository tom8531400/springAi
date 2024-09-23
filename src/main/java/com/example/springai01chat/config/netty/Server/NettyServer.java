package com.example.springai01chat.config.netty.Server;

import com.example.springai01chat.config.netty.Server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class NettyServer {
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        log.info("netty服務器已啟動...");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder())
                            .addLast(new HttpServerCodec())
                            .addLast(new HttpObjectAggregator(65536))
                            .addLast(new HttpContentCompressor())
                            .addLast(new WebSocketServerProtocolHandler("/ws"))
                            .addLast(new ServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(9111);
//            channelFuture.channel().closeFuture().sync();
        } catch (Exception ex) {
            throw new Exception("連線失敗" + ex.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
