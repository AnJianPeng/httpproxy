import net.penganjian.proxyserver.protocol.ConnectProxyHandler;
import net.penganjian.proxyserver.protocol.WelcomeHandler;
import net.penganjian.proxyserver.util.RequestEchoHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;


public class ProxyServerInitializer extends ChannelInitializer<SocketChannel> {
	//LineBasedFrameLength允许的最大长度
	private final int maxLength;
	
	public ProxyServerInitializer(int maxLength){
		this.maxLength = maxLength;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new LineBasedFrameDecoder(maxLength));
		ch.pipeline().addLast(new StringDecoder());
		
		//echo requests from clients
//		ch.pipeline().addLast(new RequestEchoHandler());
		
		//handler related with http proxy server
		ch.pipeline().addLast(new WelcomeHandler());
	}
}
