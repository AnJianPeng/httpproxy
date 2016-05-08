package net.penganjian.proxyserver.protocol;

import io.netty.util.UniqueName;

import java.util.concurrent.*;

import static io.netty.util.internal.ObjectUtil.checkNotNull;


public class ConnectOption<T> extends UniqueName{
	private static final ConcurrentMap<String,ConnectOption> names = new ConcurrentHashMap<String,ConnectOption>();
	
	public static final ConnectOption<String> PROXY_CONNECTION = valueOf("Proxy-Connection");
	public static final ConnectOption<String> HOST_NAME = valueOf("Host");
	public static final ConnectOption<String> PORT_NUM = valueOf("port");
	public static final ConnectOption<String> USER_AGENT = valueOf("User-Agent");
	
	@SuppressWarnings("unchecked")
	private static <T> ConnectOption<T> valueOf(String name){
		checkNotNull(name,"name");
		ConnectOption<T> option = names.get(name);
		if(option == null){
			option = new ConnectOption<T>(name);
			ConnectOption<T> old = names.putIfAbsent(name, option);
			if(old != null){
				option = old;
			}
		}
		return option;
	}
	
	@Deprecated
	private ConnectOption(String name){
		super(name);
	}
}
