package com.example.demo.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;


@Data
@Slf4j
public class NettyTcpServer {

    private String id;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap b;
    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private int port;

    private EquipmentManager equipmentManager;

    public NettyTcpServer(int port) {
        this.port = port;

        bossGroup = new NioEventLoopGroup(); // (1)
        workerGroup = new NioEventLoopGroup();
        b = new ServerBootstrap(); // (2)

        final NettyTcpServer parent = this;

        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class) // (3)
                .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EtxDecoder(Charset.forName("cp949")));
                        ch.pipeline().addLast(new NettyTcpServerDefaultChannelHandler(parent));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
    }

    public void run() throws InterruptedException {
        ChannelFuture f = b.bind(port).sync();
//        f.channel().closeFuture().sync();
    }

    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public void send(String message){
//        Charset utf8 = Charset.forName("cp949");
//        byte[] s = message.getBytes(utf8);
        byte[] s = message.getBytes();
        ByteBuf b = PooledByteBufAllocator.DEFAULT.buffer(s.length).writeBytes(s);
        channels.writeAndFlush(b);
    }


    @Data
    class NettyTcpServerDefaultChannelHandler extends ChannelHandlerAdapter {

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
                log.debug("ctx={}, id={}, port={} ,data={}", ctx.name(), nettyTcpServer.getId(), nettyTcpServer.getPort() ,msg.toString());
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
}
