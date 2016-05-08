package net.penganjian.proxyserver.httpinterparser;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	
	/*
	 * Ӧ���ṩget����ʹ���ⲿ�ܹ���ö�Ӧ����
	 * �������� get(T t);
	 * 
	 * 
	 * �����Լ�����һ����RequestOption��
	 * ���Բο�@see net.penganjian.proxyserver.protocol.ConnectOption
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
