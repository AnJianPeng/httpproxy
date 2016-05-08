package net.penganjian.proxyserver.httpinterparser;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	
	/*
	 * 应该提供get方法使得外部能够获得对应属性
	 * 方法形似 get(T t);
	 * 
	 * 
	 * 建议自己定义一个类RequestOption，
	 * 可以参考@see net.penganjian.proxyserver.protocol.ConnectOption
	 */
	
	/*
	 * we make important attributes as Member variables
	 */
	private String hostName;
	private int portNum = 80;
	private Map<String,String> options = new HashMap<String,String>();
	//...
	
	public void addOption(String key,String value){
		options.put(key, value);
	}
	
	public String getHostName() throws Exception {
		if(hostName == null)
			throw new Exception("hostName is null");
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public int getPortNum() {
		return portNum;
	}
	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
}
