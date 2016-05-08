package net.penganjian.proxyserver.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProxyBackenHandler extends ChannelInboundHandlerAdapter {
	private volatile  Channel inboundChannel;
	
	public ProxyBackenHandler(Channel inbound){
		inboundChannel = inbound;
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.read();
    }
	
	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg){
		inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
//                	System.out.println("Reply succed");
                    ctx.channel().read();
                } else {
                	System.out.println(future.cause());
                    future.channel().close();
                }
            }
        });
	}
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ProxyFrontedHandler.closeOnFlush(inboundChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ProxyFrontedHandler.closeOnFlush(ctx.channel());
    }
}
