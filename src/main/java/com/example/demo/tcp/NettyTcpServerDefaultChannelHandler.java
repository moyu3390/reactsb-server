package com.example.demo.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyTcpServerDefaultChannelHandler extends ChannelHandlerAdapter {

    ChannelGroup channels;
    EquipmentManager equipmentManager;
    NettyTcpServer nettyTcpServer;

    public NettyTcpServerDefaultChannelHandler(NettyTcpServer nettyTcpServer) {
        this.nettyTcpServer = nettyTcpServer;
        this.channels = nettyTcpServer.getChannels();
        this.equipmentManager = nettyTcpServer.getEquipmentManager();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.debug("channelRegistered={}", ctx.channel().remoteAddress());
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        super.channelActive(ctx);
        log.debug("channelActive={}", ctx.channel().remoteAddress());
        //this.send("Connected message by server. ^_^ Hello, client.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("channelInactive={}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            log.debug("ctx={}, id={}, port={}, dataLength={} ,data={}", ctx.name(), nettyTcpServer.getId(), nettyTcpServer.getPort(), msg.toString().getBytes().length ,msg.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        log.debug("exceptionCaught={}, {}",channel.remoteAddress().toString(), cause.toString());
    }
}