package net.penganjian.proxyserver.filter;

import io.netty.channel.ChannelHandlerContext;
import net.penganjian.proxyserver.httpinterparser.HttpRequest;

/*
 * all the http requests will be filtered by the filted created by this factory
 * use factory we don't need to create new objects to deal with coming requests
 * and free us from multithread problems 
 */



public class CombinedFilter implements Filter<HttpRequest>{
	private Filter<HttpRequest>[] httpRequestFilters = null;
	
	@Override
	public void filter(HttpRequest request,ChannelHandlerContext ctx){
		for(Filter<HttpRequest> filter:httpRequestFilters){
			//filter will chane the behavior of this proxy based on the request info
			filter.filter(request,ctx);
		}
	}
	
	private volatile static CombinedFilter instance;
	private CombinedFilter(){}
	
	public static CombinedFilter getInstance(){
		if(instance == null){
			synchronized(CombinedFilter.class){
				if(instance == null)
					instance = new CombinedFilter();
			}
		}
		return instance;
	}
}
