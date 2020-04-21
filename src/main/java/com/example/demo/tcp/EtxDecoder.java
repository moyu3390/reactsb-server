package com.example.demo.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EtxDecoder extends ByteToMessageDecoder {

    private final byte STX = 0x02;
    private final byte ETX = 0x03;

    private Charset charset;
    protected ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public EtxDecoder() {
        this.charset = Charset.defaultCharset();
    }

    public EtxDecoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        while (byteBuf.isReadable()){
            byte b = byteBuf.readByte();

            if(b == ETX){
                list.add(byteArrayOutputStream.toString(charset.name()));
                decodeLast(channelHandlerContext, byteBuf, list);
            }
            else{
                if(b != STX){
                    byteArrayOutputStream.write(b);
                }
            }
        }// end while
    }

    @Override
    protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byteArrayOutputStream.reset();
        super.decodeLast(ctx, in, out);
    }
}
