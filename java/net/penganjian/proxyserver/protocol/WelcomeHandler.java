package net.penganjian.proxyserver.protocol;

import java.util.regex.Matcher;

import net.penganjian.proxyserver.uris.HttpRegex;
import io.netty.channel.*;

public class WelcomeHandler extends ChannelInboundHandlerAdapter {
	/*
	 * used different handlers based on the different request mthod from clients
	 */
	private volatile ChannelInboundHandler methodRelatedHandler = null;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx){
		
	}
	
	@Override
	public void channelRead(final ChannelHandlerContext ctx,Object msg) throws Exception{
		if(methodRelatedHandler == null){
			/*
			 * by now we have not get enough information to determine 
			 * what specific handler to use, in this block all things will be done
			 */
			String line = (String)msg;
			Matcher matcher = HttpRegex.connectRequestPattern.matcher(line);
			if(matcher.find())
				methodRelatedHandler = new ConnectProxyHandler();
			else
				methodRelatedHandler = new NonConnectProxyHandler();
			methodRelatedHandler.channelRead(ctx, msg);
		}else{
			methodRelatedHandler.channelRead(ctx, msg);
		}
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(methodRelatedHandler == null){
			
		}else{
			methodRelatedHandler.exceptionCaught(ctx, cause);
		}
    }
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if(methodRelatedHandler == null){
		
		}else{
			methodRelatedHandler.channelInactive(ctx);
		}
    }
	
}
