package com.example.springai01chat.config.netty.Server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Log4j2
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String UDPMESSAGE = "現在時間: " + dateFormat.format(System.currentTimeMillis());
    private static final String UDPMESSAGE2 = "歡迎來到聊天室~~";
    private static final String UDPMESSAGE3 = " 已上線";
    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        log.info("客戶端: {} 已加入服務", ctx.channel().remoteAddress());
        ctx.writeAndFlush(ctx.channel().remoteAddress() + UDPMESSAGE2);
        channels.writeAndFlush(ctx.channel().remoteAddress() + UDPMESSAGE3);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("接收客戶{} 消息: {}",ctx.channel().remoteAddress(), msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("錯誤訊息: {}", cause.getMessage());
        cause.printStackTrace();
        ctx.channel().close();
    }

    @Scheduled(cron = "0 50 * * * ?")
    public void udpMessage() {
        channels.writeAndFlush(UDPMESSAGE);
    }
}
