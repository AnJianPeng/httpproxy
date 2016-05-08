package net.penganjian.proxyserver.protocol;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

import net.penganjian.proxyserver.httpinterparser.BasicHttpRequestParser;
import net.penganjian.proxyserver.httpinterparser.HttpRequest;
import net.penganjian.proxyserver.httpinterparser.HttpRequestParser;
import net.penganjian.proxyserver.uris.HttpRegex;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

public class NonConnectProxyHandler extends ChannelInboundHandlerAdapter {
	private final HttpRequestParser parser = new BasicHttpRequestParser();
	private final StringBuffer buffer = new StringBuffer();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx){
		
	}
	
	@Override
	public void channelRead(final ChannelHandlerContext ctx,Object msg) {
		String line = (String)msg;
		buffer.append(line+"\r\n");
		try{
			boolean isEof = parser.parseNextLine(line);
			if(isEof){
				//http parse process has been finished
				HttpRequest request = parser.reset();
				startProxy(ctx,request,buffer.toString());
				buffer.delete(0, buffer.length());
			}
		}catch(Exception e){
			ctx.writeAndFlush("HTTP/1.1 400 Bad Request\r\n").
			addListener(ChannelFutureListener.CLOSE);
			//e.printStackTrace();
		}
		//have not bee finished,next line
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		
    }
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
    }
	
	private void startProxy(ChannelHandlerContext ctx,HttpRequest request,String str) throws Exception{
		// Start the connection attempt.
		final Channel inboundChannel = ctx.channel();
		
		//get the request attributes we need
		String remoteHost = request.getHostName();
		int remotePort = request.getPortNum();
		System.out.println(remoteHost+":"+remotePort);
		
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
                	System.out.println("get "+remoteHost+" fail");
                	System.out.println(future.cause());
                    inboundChannel.close();
                }else{
//                	System.out.println("get "+remoteHost+" succed");
                	future.channel().writeAndFlush(Unpooled.copiedBuffer(str.toString().getBytes())).addListener(new ChannelFutureListener(){

						@Override
						public void operationComplete(ChannelFuture future)
								throws Exception {
							if(future.isSuccess()){
//								System.out.println("write requests succed");
							}else{
								System.out.println(future.cause());
							}
						}
                		
                	});
                }
            }
        });
		        
		     
	}
}
