package net.penganjian.proxyserver.protocol;

import java.util.*;
import java.util.regex.*;

import net.penganjian.proxyserver.uris.HttpRegex;
import net.penganjian.proxyserver.util.RequestEchoHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import static net.penganjian.proxyserver.uris.HttpRegex.*;

public class ConnectProxyHandler extends ChannelInboundHandlerAdapter{
	/*
	 * map storing attributes
	 */
	private final Map<ConnectOption<?>,String> attriOptions = new LinkedHashMap<ConnectOption<?>,String>();
	private int countLine = 0;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx){
//		ctx.read();
	}
	
	@Override
	public void channelRead(final ChannelHandlerContext ctx,Object msg){
		String line = (String)msg;
		//如果是connect命令的第一行
		if(++countLine == 1){
			Matcher matcher = HttpRegex.connectRequestPattern.matcher(line);
			if(matcher.find() && matcher.groupCount() == 4){
				attriOptions.put(ConnectOption.HOST_NAME, matcher.group(2));
				attriOptions.put(ConnectOption.PORT_NUM, matcher.group(3));
			}else{
				ctx.channel().close();
			}
		}else if(!line.equals("")){//header line
			Matcher matcher = HttpRegex.connectRequestPattern.matcher(line);
			
		}else{//http request的结束
			//authorization
			if(attriOptions.containsKey(ConnectOption.HOST_NAME)
					&& attriOptions.containsKey(ConnectOption.PORT_NUM)){
				//change the pipeline
				changePipeLine(ctx);
			}else{
				ctx.writeAndFlush("HTTP/1.1 400 Bad Request\r\n");
			}
		}
//		ctx.channel().read();
	}
	

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        closeOnFlush(ctx.channel());
    }
	
	private void changePipeLine(ChannelHandlerContext ctx){
		// Start the connection attempt.
		final Channel inboundChannel = ctx.channel();
		String remoteHost =  attriOptions.get(ConnectOption.HOST_NAME);
		int remotePort = Integer.valueOf(attriOptions.get(ConnectOption.PORT_NUM));
		
        Bootstrap b = new Bootstrap();
        b.group(inboundChannel.eventLoop())
         .channel(ctx.channel().getClass())
         .handler(new ProxyBackenHandler(inboundChannel))
         .option(ChannelOption.AUTO_READ, false);
        ChannelFuture f = b.connect(remoteHost, remotePort);
        Channel outboundChannel = f.channel();
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess()){
                    // Close the connection if the connection attempt has failed.
                	System.out.println("connect "+remoteHost+" fail");
                    inboundChannel.close();
                }else{
                	ctx.writeAndFlush(Unpooled.copiedBuffer(HttpRegex.testReturn.getBytes()));
                	System.out.println("connect "+remoteHost+" succes");
                }
            }
        });
		
		ctx.pipeline().addFirst(new ProxyFrontedHandler(
				remoteHost,remotePort,outboundChannel));
	}

	/**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
