package net.penganjian.proxyserver.filter;

import io.netty.channel.ChannelHandlerContext;

/*
 * a filter is used to a channel or a request/response process
 * a filter will change the behavior of the proxy, 
 * (ie: whether to cache the reponse; presistent/nonpersistent connection )
 * based on different things, such as the configuration of proxy / http headers
 * 
 * in a specific request/response process, different filters will be added to it
 * we need a elegant way to reside the change 
 */

/*
 * ����ģʽ���Լ���������ǿ��Ķ�
 * ʵ���˷��������ߺ�ʵ���ߵĽ������
 */


/*
 * filter the proxy ctx based on the info provided by o
 * in deed, it is the interface of command 
 */
public interface Filter<T> {
	void filter(T o,ChannelHandlerContext ctx);
}
