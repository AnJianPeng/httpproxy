package net.penganjian.proxyserver.protocol;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProxyFrontedHandler extends ChannelInboundHandlerAdapter{
	private final Channel outboundChannel;
	
	private final String hostName;
	private final int portNum;
	
	public ProxyFrontedHandler(String hostName,int portNum,Channel channel){
		this.hostName = hostName;
		this.portNum = portNum;
		this.outboundChannel = channel;
	}
	
	
	@Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        if(outboundChannel.isActive()){
        	ChannelFuture f = outboundChannel.writeAndFlush(msg);
        	f.addListener(new ChannelFutureListener(){

				@Override
				public void operationComplete(ChannelFuture future)
						throws Exception {
					if(future.isSuccess())
						// was able to flush out data, start to read the next chunk
						ctx.channel().read();
//						System.out.println("write success");
					else{
						System.out.println("write fail");
						System.out.println(f.cause());
						future.channel().close();
					}
				}
        		
        	});
        }
    }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        closeOnFlush(ctx.channel());
    }
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
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
